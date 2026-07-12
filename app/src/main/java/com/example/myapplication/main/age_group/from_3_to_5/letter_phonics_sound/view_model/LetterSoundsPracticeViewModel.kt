package com.example.myapplication.main.age_group.from_3_to_5.letter_phonics_sound.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.progress.PhonicsLevelProgressRepository
import com.example.myapplication.data.progress.PhonicsSessionRecorder
import com.example.myapplication.main.age_group.phonics.listen.view_model.PhonicsListenLevelKey
import com.example.myapplication.utilities.AudioPhonicsManager
import com.example.myapplication.utils.AudioPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

// ── Models ────────────────────────────────────────────────────────────────────

/** "Which letter do you hear?" — play a letter sound, pick the matching letter. */
data class LetterSoundPracticeQuestion(
    val id: String = UUID.randomUUID().toString(),
    val letter: String,        // e.g. "a" — the correct answer
    val options: List<String>  // 3 letter choices, including the correct one
) {
    val correct: String get() = letter
    // No word — the display shows just the answer box.
    val prefix: String get() = ""
    val suffix: String get() = ""
}

data class LetterSoundPracticeUiState(
    val currentIndex: Int = 0,
    val score: Int = 0,
    val selectedAnswer: String? = null,
    val isCorrect: Boolean? = null,
    val isFinished: Boolean = false,
    val shakeWrong: Boolean = false
)

// ── Data ───────────────────────────────────────────────────────────────────────

val letterSoundsPracticeQuestions: List<LetterSoundPracticeQuestion> = listOf(
    LetterSoundPracticeQuestion(letter = "a", options = listOf("e", "a", "o")),
    LetterSoundPracticeQuestion(letter = "b", options = listOf("d", "b", "p")),
    LetterSoundPracticeQuestion(letter = "c", options = listOf("c", "g", "t")),
    LetterSoundPracticeQuestion(letter = "m", options = listOf("n", "m", "w")),
    LetterSoundPracticeQuestion(letter = "s", options = listOf("s", "f", "z")),
    LetterSoundPracticeQuestion(letter = "t", options = listOf("d", "t", "p")),
    LetterSoundPracticeQuestion(letter = "o", options = listOf("o", "a", "u")),
    LetterSoundPracticeQuestion(letter = "f", options = listOf("v", "f", "s")),
    LetterSoundPracticeQuestion(letter = "r", options = listOf("r", "l", "w")),
    LetterSoundPracticeQuestion(letter = "e", options = listOf("i", "e", "a")),
    LetterSoundPracticeQuestion(letter = "p", options = listOf("b", "p", "d")),
    LetterSoundPracticeQuestion(letter = "g", options = listOf("g", "j", "k")),
    LetterSoundPracticeQuestion(letter = "u", options = listOf("o", "u", "a")),
    LetterSoundPracticeQuestion(letter = "d", options = listOf("d", "b", "t")),
    LetterSoundPracticeQuestion(letter = "n", options = listOf("m", "n", "l"))
)

// ── Practice ViewModel ─────────────────────────────────────────────────────────

@HiltViewModel
class LetterSoundsPracticeViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager,
    private val levelProgressRepo: PhonicsLevelProgressRepository,
    private val phonicsSessions: PhonicsSessionRecorder
) : ViewModel() {

    private val questions = letterSoundsPracticeQuestions.shuffled().take(10)
    var uiState by mutableStateOf(LetterSoundPracticeUiState()); private set

    // Parent-report session tracking
    private var sessionStartMs = System.currentTimeMillis()
    private val wrongWords = mutableListOf<String>()
    private val correctWords = mutableListOf<String>()

    val totalQuestions: Int get() = questions.size
    val currentQuestion: LetterSoundPracticeQuestion? get() = questions.getOrNull(uiState.currentIndex)

    private var shakeJob: Job? = null

    /** Play the current letter's sound — auto on each question and on tapping the speaker. */
    fun playCurrent() {
        val q = currentQuestion ?: return
        audioManager.playPhonicsSound("phonics_letter/sound_${q.letter}")
    }

    fun onAnswerTap(answer: String) {
        val q = currentQuestion ?: return
        if (uiState.selectedAnswer != null) return
        val correct = answer == q.correct
        uiState = uiState.copy(selectedAnswer = answer, isCorrect = correct)
        if (correct) correctWords.add(q.letter) else wrongWords.add(q.letter)
        if (correct) {
            uiState = uiState.copy(score = uiState.score + 1)
            audioManager.playPhonicsSound("phonics_letter/sound_${q.letter}")
        } else {
            AudioPlayerManager.playSoundWrongAnswer()
            uiState = uiState.copy(shakeWrong = true)
            shakeJob?.cancel()
            shakeJob = viewModelScope.launch {
                delay(600)
                uiState = uiState.copy(shakeWrong = false)
            }
        }
        viewModelScope.launch {
            delay(if (correct) 1200L else 1800L)
            advance()
        }
    }

    fun restart() {
        sessionStartMs = System.currentTimeMillis()
        wrongWords.clear()
        correctWords.clear()
        audioManager.stop()
        uiState = LetterSoundPracticeUiState()
    }

    fun stop() { audioManager.stop() }

    override fun onCleared() { super.onCleared(); stop() }

    private fun advance() {
        val next = uiState.currentIndex + 1
        uiState = if (next >= questions.size) {
            levelProgressRepo.recordPractice(level = PhonicsListenLevelKey.letterSounds, score = uiState.score, total = questions.size)
            phonicsSessions.recordPractice(PhonicsListenLevelKey.letterSounds, uiState.score, questions.size, ((System.currentTimeMillis() - sessionStartMs) / 1000).toInt(), wrongWords.toList(), correctWords.toList())
            uiState.copy(isFinished = true)
        } else {
            uiState.copy(currentIndex = next, selectedAnswer = null, isCorrect = null, shakeWrong = false)
        }
    }
}
