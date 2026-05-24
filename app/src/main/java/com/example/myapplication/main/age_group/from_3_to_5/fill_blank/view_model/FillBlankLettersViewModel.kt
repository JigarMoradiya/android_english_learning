package com.example.myapplication.main.age_group.from_3_to_5.fill_blank.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.SessionRepository
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.LetterMode
import com.example.myapplication.main.age_group.from_3_to_5.fill_blank.BlankPosition
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.FeedbackConstant.feedbackFillBlank
import com.example.myapplication.utils.FeedbackConstant.feedbackFillBlankSubtitleWrong
import com.example.myapplication.utils.FeedbackConstant.feedbackWrong
import com.example.myapplication.utils.FeedbackConstant.feedbackTitles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FillBlankLettersViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    var uiState by mutableStateOf(FillBlankLetterUiState())
        private set

    private var countdownJob: Job? = null
    private var blankPosition: BlankPosition = BlankPosition.FIRST
    private val wrongAttemptsInBatch = mutableSetOf<String>()
    private var batchStartMs: Long = System.currentTimeMillis()

    init {
        generateGame()
    }

    fun setConfig(position: BlankPosition, mode: LetterMode) {
        blankPosition = position
        uiState = uiState.copy(mode = mode)
        generateGame()
    }

    private fun getAlphabet(): List<String> {
        return when (uiState.mode) {
            LetterMode.UPPERCASE -> ('A'..'Z').map { it.toString() }
            LetterMode.LOWERCASE -> ('a'..'z').map { it.toString() }
        }
    }

    fun generateGame() {
        countdownJob?.cancel()
        countdownJob = null
        val alphabets = getAlphabet()
        // ✅ 1. Pick random 5–6 sequence
        val size = (2..3).random()
        val startIndex = (0..(26 - size)).random()

        val sequence = alphabets.subList(startIndex, startIndex + size)
        val blankIndex = when (blankPosition) {
            BlankPosition.FIRST  -> 0
            BlankPosition.LAST   -> size - 1
            BlankPosition.MIDDLE -> size / 2
            BlankPosition.RANDOM -> (0 until size).random()
        }
        val blankIndices = mutableSetOf(blankIndex)

        val topSlots = sequence.mapIndexed { index, letter ->
            if (blankIndices.contains(index)) null else letter
        }

        val correctLetters = blankIndices.map { sequence[it] }

        // ✅ 3. Bottom options (correct + wrong)
        val options = mutableListOf<String>()
        options.addAll(correctLetters)

        val extraLetter = 2
        while (options.size < correctLetters.size + extraLetter) {
            val random = alphabets.random()
            if (!sequence.contains(random) && !options.contains(random)) {
                options.add(random)
            }
        }
        val fixedIndices = sequence.mapIndexedNotNull { index, letter ->
            if (!blankIndices.contains(index)) index else null
        }.toSet()

        uiState = uiState.copy(
            topSlots = topSlots,
            correctLetters = correctLetters,
            bottomOptions = options.shuffled(),
            fullSequence = sequence,
            fixedIndices = fixedIndices,
            isAnswerCorrect = false,
            showNext = false
        )

    }

    // ✅ Tap bottom → fill first empty
    fun onBottomLetterClick(letter: String) {
        if (uiState.isAnswerCorrect) return
        val slots = uiState.topSlots.toMutableList()
        val firstEmpty = slots.indexOfFirst { it == null }

        if (firstEmpty != -1) {
            slots[firstEmpty] = letter

            val updatedBottom = uiState.bottomOptions.toMutableList()
            updatedBottom.remove(letter)

            uiState = uiState.copy(topSlots = slots, bottomOptions = updatedBottom)

            validate()
        }
    }

    // ✅ Tap top → remove back
    fun onTopLetterClick(index: Int) {
        if (uiState.showNext) return

        val isFixed = uiState.fixedIndices.contains(index)
        if (isFixed) return

        val slots = uiState.topSlots.toMutableList()
        val letter = slots[index] ?: return

        slots[index] = null

        val updatedBottom = uiState.bottomOptions.toMutableList()
        updatedBottom.add(letter)

        AudioPlayerManager.playSoundMenuClick()

        uiState = uiState.copy(
            topSlots = slots,
            bottomOptions = updatedBottom,
            showNext = false
        )
    }

    private fun validate() {
        if (!uiState.topSlots.contains(null)) {
            val formed = uiState.topSlots.joinToString("")
            val correct = uiState.fullSequence.joinToString("")

            if (formed == correct) {
                AudioPlayerManager.playSoundCorrectAnswer()
                uiState = uiState.copy(
                    showNext = true,
                    isAnswerCorrect = true,
                    correctCount = uiState.correctCount + 1,
                    feedbackTextRes = feedbackTitles.random(),
                    feedbackSubTextRes = feedbackFillBlank.random(),
                )
            } else {
                AudioPlayerManager.playSoundWrongAnswer()
                wrongAttemptsInBatch.addAll(uiState.correctLetters)
                uiState = uiState.copy(
                    showNext = true,
                    isAnswerCorrect = false,
                    feedbackTextRes = feedbackWrong.random(),
                    feedbackSubTextRes = feedbackFillBlankSubtitleWrong.random(),
                )
            }
            startCountdown()
        } else {
            AudioPlayerManager.playSoundMenuClick()
        }
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            uiState = uiState.copy(countdown = 3)
            delay(1000L)
            uiState = uiState.copy(countdown = 2)
            delay(1000L)
            uiState = uiState.copy(countdown = 1)
            delay(1000L)
            next()
        }
    }

    fun next() {
        if (uiState.round >= uiState.totalRounds) {
            countdownJob?.cancel()
            countdownJob = null
            val duration = ((System.currentTimeMillis() - batchStartMs) / 1000).toInt()
            sessionRepository.record(
                LearningSession(
                    moduleId = ModuleID.FILL_THE_BLANK_LETTER,
                    ageGroup = AgeGroup.THREE_TO_FIVE,
                    durationSeconds = duration,
                    score = uiState.correctCount,
                    totalQuestions = uiState.totalRounds,
                    wrongItems = wrongAttemptsInBatch.toList(),
                    subConfig = "${blankPosition.subConfigName}|${uiState.mode.name}"
                )
            )
            uiState = uiState.copy(showResult = true)
        } else {
            uiState = uiState.copy(round = uiState.round + 1)
            generateGame()
        }
    }

    fun restartGame() {
        wrongAttemptsInBatch.clear()
        batchStartMs = System.currentTimeMillis()
        uiState = uiState.copy(round = 1, correctCount = 0, showResult = false)
        generateGame()
    }

    fun changeMode(mode: LetterMode) {
        uiState = uiState.copy(mode = mode, round = 1, correctCount = 0, showResult = false)
        generateGame()
    }
}