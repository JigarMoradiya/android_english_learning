package com.example.myapplication.main.age_group.phonics.l2_short_vowels.view_model

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

/** One "find the missing vowel" question, e.g. c_t → a (cat). */
data class ShortVowelPracticeQuestion(
    val id: String = UUID.randomUUID().toString(),
    val word: String,          // full word e.g. "cat"
    val vowelIndex: Int,       // position of the blanked vowel (1 for CVC words)
    val correctVowel: String,  // e.g. "a"
    val options: List<String>, // 3 vowel choices, including the correct one
    val imageName: String      // asset image name
) {
    val prefix: String get() = word.take(vowelIndex)
    val suffix: String get() = word.drop(vowelIndex + 1)
}

data class ShortVowelPracticeUiState(
    val currentIndex: Int = 0,
    val score: Int = 0,
    val selectedAnswer: String? = null,
    val isCorrect: Boolean? = null,
    val isFinished: Boolean = false,
    val shakeWrong: Boolean = false
)

// ── Data ───────────────────────────────────────────────────────────────────────

val shortVowelsPracticeQuestions: List<ShortVowelPracticeQuestion> = listOf(
    // /a/
    ShortVowelPracticeQuestion(word = "cat", vowelIndex = 1, correctVowel = "a", options = listOf("e", "a", "o"), imageName = "cat"),
    ShortVowelPracticeQuestion(word = "hat", vowelIndex = 1, correctVowel = "a", options = listOf("a", "i", "u"), imageName = "hat"),
    ShortVowelPracticeQuestion(word = "bat", vowelIndex = 1, correctVowel = "a", options = listOf("o", "e", "a"), imageName = "bat"),
    ShortVowelPracticeQuestion(word = "rat", vowelIndex = 1, correctVowel = "a", options = listOf("u", "a", "i"), imageName = "rat"),
    // /e/
    ShortVowelPracticeQuestion(word = "pen", vowelIndex = 1, correctVowel = "e", options = listOf("a", "e", "i"), imageName = "pen"),
    ShortVowelPracticeQuestion(word = "hen", vowelIndex = 1, correctVowel = "e", options = listOf("e", "o", "u"), imageName = "hen"),
    ShortVowelPracticeQuestion(word = "net", vowelIndex = 1, correctVowel = "e", options = listOf("i", "a", "e"), imageName = "net"),
    // /i/
    ShortVowelPracticeQuestion(word = "pig", vowelIndex = 1, correctVowel = "i", options = listOf("a", "i", "e"), imageName = "pig"),
    ShortVowelPracticeQuestion(word = "zip", vowelIndex = 1, correctVowel = "i", options = listOf("o", "i", "u"), imageName = "zip"),
    // /o/
    ShortVowelPracticeQuestion(word = "dog", vowelIndex = 1, correctVowel = "o", options = listOf("o", "a", "u"), imageName = "dog"),
    ShortVowelPracticeQuestion(word = "pot", vowelIndex = 1, correctVowel = "o", options = listOf("e", "o", "i"), imageName = "pot"),
    ShortVowelPracticeQuestion(word = "fox", vowelIndex = 1, correctVowel = "o", options = listOf("u", "a", "o"), imageName = "fox"),
    // /u/
    ShortVowelPracticeQuestion(word = "sun", vowelIndex = 1, correctVowel = "u", options = listOf("o", "u", "a"), imageName = "sun"),
    ShortVowelPracticeQuestion(word = "bus", vowelIndex = 1, correctVowel = "u", options = listOf("u", "i", "e"), imageName = "bus"),
    ShortVowelPracticeQuestion(word = "jug", vowelIndex = 1, correctVowel = "u", options = listOf("a", "o", "u"), imageName = "jug")
)

// ── Practice ViewModel ─────────────────────────────────────────────────────────

@HiltViewModel
class ShortVowelsPracticeViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager,
    private val levelProgressRepo: PhonicsLevelProgressRepository,
    private val phonicsSessions: PhonicsSessionRecorder
) : ViewModel() {

    private val questions = shortVowelsPracticeQuestions.shuffled().take(10)
    var uiState by mutableStateOf(ShortVowelPracticeUiState()); private set

    // Parent-report session tracking
    private var sessionStartMs = System.currentTimeMillis()
    private val wrongWords = mutableListOf<String>()
    private val correctWords = mutableListOf<String>()

    val totalQuestions: Int get() = questions.size
    val currentQuestion: ShortVowelPracticeQuestion? get() =
        questions.getOrNull(uiState.currentIndex)

    private var shakeJob: Job? = null

    fun onAnswerTap(answer: String) {
        val q = currentQuestion ?: return
        if (uiState.selectedAnswer != null) return
        val correct = answer == q.correctVowel
        uiState = uiState.copy(selectedAnswer = answer, isCorrect = correct)
        if (correct) correctWords.add(q.word) else wrongWords.add(q.word)
        if (correct) {
            uiState = uiState.copy(score = uiState.score + 1)
            audioManager.playPhonicsSound("phonics_word/${q.word}")
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
        uiState = ShortVowelPracticeUiState()
    }

    fun stop() { audioManager.stop() }

    override fun onCleared() { super.onCleared(); stop() }

    private fun advance() {
        val next = uiState.currentIndex + 1
        uiState = if (next >= questions.size) {
            levelProgressRepo.recordPractice(level = PhonicsListenLevelKey.shortVowels, score = uiState.score, total = questions.size)
            phonicsSessions.recordPractice(PhonicsListenLevelKey.shortVowels, uiState.score, questions.size, ((System.currentTimeMillis() - sessionStartMs) / 1000).toInt(), wrongWords.toList(), correctWords.toList())
            uiState.copy(isFinished = true)
        } else {
            uiState.copy(currentIndex = next, selectedAnswer = null, isCorrect = null, shakeWrong = false)
        }
    }
}
