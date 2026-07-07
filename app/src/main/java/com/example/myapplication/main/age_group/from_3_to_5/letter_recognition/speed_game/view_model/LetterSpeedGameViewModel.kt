package com.example.myapplication.main.age_group.from_3_to_5.letter_recognition.speed_game.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.SessionRepository
import com.example.myapplication.utilities.AudioPhonicsManager
import com.example.myapplication.utils.AudioPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

// "Find the Letter Fast" — a 5-round batch, same shape as Article Choice /
// Missing Letter: one question on screen at a time, tap an answer, a 3-2-1
// countdown before the next one, then a result popup. The earlier version
// ran on a live 45s clock with letters advancing instantly on any tap, which
// meant the tap sound and the next letter's audio overlapped and mixed —
// this batch+countdown shape guarantees a clean gap between rounds.
@HiltViewModel
class LetterSpeedGameViewModel @Inject constructor(
    private val audioPhonicsManager: AudioPhonicsManager,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    var uiState by mutableStateOf(LetterSpeedGameUiState())
        private set

    // Kept small and visually distinct at first so early rounds aren't already
    // testing the b/d/p/q-style mix-ups — those only enter the pool once the
    // child is doing well.
    private val easyPool = "ACEHLOST".toList()
    private val mediumPool = "ACEHLOSTIJKMNRUV".toList()
    private val allLetters = ('A'..'Z').toList()

    private var poolLevel = 0
    private var correctStreak = 0
    private var batchIndex = 0

    private var countdownJob: Job? = null
    private var batchStartTime = System.currentTimeMillis()
    private val correctLetters = mutableListOf<String>()
    private val wrongAttempts = mutableSetOf<Char>()

    fun loadNewBatch() {
        countdownJob?.cancel()
        uiState = LetterSpeedGameUiState()
        poolLevel = 0
        correctStreak = 0
        batchIndex = 0
        batchStartTime = System.currentTimeMillis()
        correctLetters.clear()
        wrongAttempts.clear()
        loadWord()
    }

    private fun setPool(level: Int) {
        val pool = when (level) {
            0 -> easyPool
            1 -> mediumPool
            else -> allLetters
        }
        uiState = uiState.copy(pool = pool.shuffled())
    }

    private fun loadWord() {
        countdownJob?.cancel()
        setPool(poolLevel)
        val target = uiState.pool.randomOrNull()
        uiState = uiState.copy(
            targetLetter = target,
            selectedAnswer = null,
            isAnswerCorrect = false,
            countdown = 3,
            questionIndex = batchIndex
        )
        generateDisplayOptions()
        playTargetAudio()
    }

    // Target + 3 random decoys from the same difficulty pool, shuffled —
    // only 4 letters ever show on screen at once.
    private fun generateDisplayOptions() {
        val target = uiState.targetLetter ?: return
        val decoys = uiState.pool.filter { it != target }.shuffled().take(3)
        uiState = uiState.copy(displayOptions = (listOf(target) + decoys).shuffled())
    }

    private fun playTargetAudio() {
        val letter = uiState.targetLetter ?: return
        audioPhonicsManager.playPhonicsSound("phonics_letter/sound_${letter.lowercaseChar()}")
    }

    fun replayTargetAudio() {
        playTargetAudio()
    }

    fun onTapLetter(letter: Char) {
        if (uiState.selectedAnswer != null || uiState.targetLetter == null) return
        val target = uiState.targetLetter
        val isCorrect = letter == target

        uiState = uiState.copy(selectedAnswer = letter, isAnswerCorrect = isCorrect)

        if (isCorrect) {
            correctLetters.add(letter.toString())
            correctStreak += 1
            AudioPlayerManager.playSoundCorrectAnswer()
            uiState = uiState.copy(correctCount = uiState.correctCount + 1)

            // Every 3 in a row, the pool grows — easy → medium → full alphabet.
            if (correctStreak % 3 == 0 && poolLevel < 2) {
                poolLevel += 1
            }
        } else {
            correctStreak = 0
            wrongAttempts.add(letter)
            AudioPlayerManager.playSoundWrongAnswer()
        }

        batchIndex += 1

        if (batchIndex >= uiState.totalQuestions) {
            viewModelScope.launch {
                delay(500)
                showBatchComplete()
            }
        } else {
            startCountdown()
        }
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            for (i in 2 downTo 1) {
                delay(1000)
                uiState = uiState.copy(countdown = i)
            }
            delay(1000)
            loadWord()
        }
    }

    private fun showBatchComplete() {
        countdownJob?.cancel()
        uiState = uiState.copy(
            lastScore = uiState.correctCount,
            showBatchPopup = true
        )
        recordSession()
    }

    fun stopTimer() {
        countdownJob?.cancel()
    }

    private fun recordSession() {
        val duration = ((System.currentTimeMillis() - batchStartTime) / 1000).toInt()
        sessionRepository.record(
            LearningSession(
                moduleId = ModuleID.LETTER_SPEED_GAME,
                ageGroup = AgeGroup.THREE_TO_FIVE,
                durationSeconds = duration,
                score = uiState.correctCount,
                totalQuestions = uiState.totalQuestions,
                wrongItems = wrongAttempts.map { it.toString() },
                correctItems = correctLetters.toList()
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
    }
}
