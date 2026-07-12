package com.example.myapplication.main.age_group.phonics.l3_blending.view_model

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

/** "Find the missing sound" in a 2-sound blend, e.g. a_ → t (at). Audio only. */
data class BlendSoundPracticeQuestion(
    val id: String = UUID.randomUUID().toString(),
    val word: String,          // 2-sound unit e.g. "at"
    val blankStart: Int,       // index of the blanked letter
    val options: List<String>, // 3 letter choices, including the correct one
    val audioFile: String,     // phonics_word file (without folder)
    val blankLen: Int = 1
) {
    val correct: String get() = word.substring(blankStart, blankStart + blankLen)
    val prefix: String get() = word.take(blankStart)
    val suffix: String get() = word.drop(blankStart + blankLen)
}

data class BlendSoundPracticeUiState(
    val currentIndex: Int = 0,
    val score: Int = 0,
    val selectedAnswer: String? = null,
    val isCorrect: Boolean? = null,
    val isFinished: Boolean = false,
    val shakeWrong: Boolean = false
)

// ── Data ───────────────────────────────────────────────────────────────────────

val blendingPracticeQuestions: List<BlendSoundPracticeQuestion> = listOf(
    BlendSoundPracticeQuestion(word = "at", blankStart = 1, options = listOf("t", "n", "m"), audioFile = "at"),
    BlendSoundPracticeQuestion(word = "an", blankStart = 0, options = listOf("a", "i", "u"), audioFile = "an"),
    BlendSoundPracticeQuestion(word = "in", blankStart = 1, options = listOf("n", "t", "p"), audioFile = "in"),
    BlendSoundPracticeQuestion(word = "up", blankStart = 1, options = listOf("p", "s", "t"), audioFile = "up"),
    BlendSoundPracticeQuestion(word = "ox", blankStart = 1, options = listOf("x", "n", "t"), audioFile = "ox"),
    BlendSoundPracticeQuestion(word = "it", blankStart = 0, options = listOf("i", "a", "o"), audioFile = "it"),
    BlendSoundPracticeQuestion(word = "am", blankStart = 1, options = listOf("m", "n", "t"), audioFile = "am"),
    BlendSoundPracticeQuestion(word = "us", blankStart = 1, options = listOf("s", "p", "n"), audioFile = "us"),
    BlendSoundPracticeQuestion(word = "ba", blankStart = 1, options = listOf("a", "o", "e"), audioFile = "ba"),
    BlendSoundPracticeQuestion(word = "go", blankStart = 1, options = listOf("o", "a", "i"), audioFile = "go"),
    BlendSoundPracticeQuestion(word = "me", blankStart = 1, options = listOf("e", "a", "o"), audioFile = "me"),
    BlendSoundPracticeQuestion(word = "no", blankStart = 1, options = listOf("o", "e", "u"), audioFile = "no"),
    BlendSoundPracticeQuestion(word = "do", blankStart = 1, options = listOf("o", "a", "e"), audioFile = "do"),
    BlendSoundPracticeQuestion(word = "we", blankStart = 1, options = listOf("e", "i", "a"), audioFile = "we"),
    BlendSoundPracticeQuestion(word = "si", blankStart = 1, options = listOf("i", "o", "u"), audioFile = "si")
)

// ── Practice ViewModel ─────────────────────────────────────────────────────────

@HiltViewModel
class BlendingPracticeViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager,
    private val levelProgressRepo: PhonicsLevelProgressRepository,
    private val phonicsSessions: PhonicsSessionRecorder
) : ViewModel() {

    private val questions = blendingPracticeQuestions.shuffled().take(10)
    var uiState by mutableStateOf(BlendSoundPracticeUiState()); private set

    // Parent-report session tracking
    private var sessionStartMs = System.currentTimeMillis()
    private val wrongWords = mutableListOf<String>()
    private val correctWords = mutableListOf<String>()

    val totalQuestions: Int get() = questions.size
    val currentQuestion: BlendSoundPracticeQuestion? get() = questions.getOrNull(uiState.currentIndex)

    private var shakeJob: Job? = null

    /** Play the current word's sound — auto on each question and on tapping the speaker. */
    fun playCurrent() {
        val q = currentQuestion ?: return
        audioManager.playPhonicsSound("phonics_word/${q.audioFile}")
    }

    fun onAnswerTap(answer: String) {
        val q = currentQuestion ?: return
        if (uiState.selectedAnswer != null) return
        val correct = answer == q.correct
        uiState = uiState.copy(selectedAnswer = answer, isCorrect = correct)
        if (correct) correctWords.add(q.word) else wrongWords.add(q.word)
        if (correct) {
            uiState = uiState.copy(score = uiState.score + 1)
            audioManager.playPhonicsSound("phonics_word/${q.audioFile}")
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
        uiState = BlendSoundPracticeUiState()
    }

    fun stop() { audioManager.stop() }

    override fun onCleared() { super.onCleared(); stop() }

    private fun advance() {
        val next = uiState.currentIndex + 1
        uiState = if (next >= questions.size) {
            levelProgressRepo.recordPractice(level = PhonicsListenLevelKey.blending, score = uiState.score, total = questions.size)
            phonicsSessions.recordPractice(PhonicsListenLevelKey.blending, uiState.score, questions.size, ((System.currentTimeMillis() - sessionStartMs) / 1000).toInt(), wrongWords.toList(), correctWords.toList())
            uiState.copy(isFinished = true)
        } else {
            uiState.copy(currentIndex = next, selectedAnswer = null, isCorrect = null, shakeWrong = false)
        }
    }
}
