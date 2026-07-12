package com.example.myapplication.main.age_group.phonics.l5_short_vowel_rules.view_model

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

/** "Find the ending" of a spelling-rule word, e.g. du__ → ck (duck). Audio only. */
data class RulePracticeQuestion(
    val id: String = UUID.randomUUID().toString(),
    val word: String,          // e.g. "duck"
    val blankStart: Int,       // index where the ending begins
    val options: List<String>, // 3 ending choices, including the correct one
    val audioFile: String,     // phonics_word file (without folder)
    val blankLen: Int = 2
) {
    val correct: String get() = word.substring(blankStart, blankStart + blankLen)
    val prefix: String get() = word.take(blankStart)
    val suffix: String get() = word.drop(blankStart + blankLen)
}

data class RulePracticeUiState(
    val currentIndex: Int = 0,
    val score: Int = 0,
    val selectedAnswer: String? = null,
    val isCorrect: Boolean? = null,
    val isFinished: Boolean = false,
    val shakeWrong: Boolean = false
)

// ── Data ───────────────────────────────────────────────────────────────────────

val shortVowelRulesPracticeQuestions: List<RulePracticeQuestion> = listOf(
    RulePracticeQuestion(word = "bell", blankStart = 2, options = listOf("ll", "ck", "ng"), audioFile = "bell"),
    RulePracticeQuestion(word = "duck", blankStart = 2, options = listOf("ck", "ll", "nk"), audioFile = "duck"),
    RulePracticeQuestion(word = "ring", blankStart = 2, options = listOf("ng", "nk", "ck"), audioFile = "ring"),
    RulePracticeQuestion(word = "king", blankStart = 2, options = listOf("ng", "ck", "ss"), audioFile = "king"),
    RulePracticeQuestion(word = "sock", blankStart = 2, options = listOf("ck", "ng", "ss"), audioFile = "sock"),
    RulePracticeQuestion(word = "lock", blankStart = 2, options = listOf("ck", "ll", "nk"), audioFile = "lock"),
    RulePracticeQuestion(word = "tank", blankStart = 2, options = listOf("nk", "ng", "ck"), audioFile = "tank"),
    RulePracticeQuestion(word = "sink", blankStart = 2, options = listOf("nk", "ng", "ll"), audioFile = "sink"),
    RulePracticeQuestion(word = "miss", blankStart = 2, options = listOf("ss", "ck", "ll"), audioFile = "miss"),
    RulePracticeQuestion(word = "well", blankStart = 2, options = listOf("ll", "ss", "ck"), audioFile = "well"),
    RulePracticeQuestion(word = "buzz", blankStart = 2, options = listOf("zz", "ss", "ck"), audioFile = "buzz"),
    RulePracticeQuestion(word = "song", blankStart = 2, options = listOf("ng", "nk", "ck"), audioFile = "song"),
    RulePracticeQuestion(word = "bank", blankStart = 2, options = listOf("nk", "ng", "ck"), audioFile = "bank"),
    RulePracticeQuestion(word = "rock", blankStart = 2, options = listOf("ck", "ng", "ll"), audioFile = "rock"),
    RulePracticeQuestion(word = "kiss", blankStart = 2, options = listOf("ss", "ll", "ck"), audioFile = "kiss")
)

// ── Practice ViewModel ─────────────────────────────────────────────────────────

@HiltViewModel
class ShortVowelRulesPracticeViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager,
    private val levelProgressRepo: PhonicsLevelProgressRepository,
    private val phonicsSessions: PhonicsSessionRecorder
) : ViewModel() {

    private val questions = shortVowelRulesPracticeQuestions.shuffled().take(10)
    var uiState by mutableStateOf(RulePracticeUiState()); private set

    // Parent-report session tracking
    private var sessionStartMs = System.currentTimeMillis()
    private val wrongWords = mutableListOf<String>()
    private val correctWords = mutableListOf<String>()

    val totalQuestions: Int get() = questions.size
    val currentQuestion: RulePracticeQuestion? get() = questions.getOrNull(uiState.currentIndex)

    private var shakeJob: Job? = null

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
        uiState = RulePracticeUiState()
    }

    fun stop() { audioManager.stop() }

    override fun onCleared() { super.onCleared(); stop() }

    private fun advance() {
        val next = uiState.currentIndex + 1
        uiState = if (next >= questions.size) {
            levelProgressRepo.recordPractice(level = PhonicsListenLevelKey.shortVowelRules, score = uiState.score, total = questions.size)
            phonicsSessions.recordPractice(PhonicsListenLevelKey.shortVowelRules, uiState.score, questions.size, ((System.currentTimeMillis() - sessionStartMs) / 1000).toInt(), wrongWords.toList(), correctWords.toList())
            uiState.copy(isFinished = true)
        } else {
            uiState.copy(currentIndex = next, selectedAnswer = null, isCorrect = null, shakeWrong = false)
        }
    }
}
