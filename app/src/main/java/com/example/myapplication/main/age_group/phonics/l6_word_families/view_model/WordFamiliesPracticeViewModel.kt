package com.example.myapplication.main.age_group.phonics.l6_word_families.view_model

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

/** "Find the first letter" of a word family word, e.g. _at → c (cat). */
data class FamilyPracticeQuestion(
    val id: String = UUID.randomUUID().toString(),
    val word: String,
    val blankStart: Int,       // always 0 here (the onset)
    val options: List<String>,
    val imageName: String,
    val blankLen: Int = 1
) {
    val correct: String get() = word.substring(blankStart, blankStart + blankLen)
    val prefix: String get() = word.take(blankStart)
    val suffix: String get() = word.drop(blankStart + blankLen)
}

data class FamilyPracticeUiState(
    val currentIndex: Int = 0,
    val score: Int = 0,
    val selectedAnswer: String? = null,
    val isCorrect: Boolean? = null,
    val isFinished: Boolean = false,
    val shakeWrong: Boolean = false
)

// ── Data ───────────────────────────────────────────────────────────────────────

val wordFamiliesPracticeQuestions: List<FamilyPracticeQuestion> = listOf(
    FamilyPracticeQuestion(word = "cat", blankStart = 0, options = listOf("c", "b", "h"), imageName = "cat"),
    FamilyPracticeQuestion(word = "bat", blankStart = 0, options = listOf("b", "c", "r"), imageName = "bat"),
    FamilyPracticeQuestion(word = "hat", blankStart = 0, options = listOf("h", "c", "m"), imageName = "hat"),
    FamilyPracticeQuestion(word = "rat", blankStart = 0, options = listOf("r", "c", "f"), imageName = "rat"),
    FamilyPracticeQuestion(word = "fan", blankStart = 0, options = listOf("f", "m", "v"), imageName = "fan"),
    FamilyPracticeQuestion(word = "van", blankStart = 0, options = listOf("v", "f", "m"), imageName = "van"),
    FamilyPracticeQuestion(word = "dog", blankStart = 0, options = listOf("d", "l", "f"), imageName = "dog"),
    FamilyPracticeQuestion(word = "hen", blankStart = 0, options = listOf("h", "p", "t"), imageName = "hen"),
    FamilyPracticeQuestion(word = "pen", blankStart = 0, options = listOf("p", "h", "d"), imageName = "pen"),
    FamilyPracticeQuestion(word = "pig", blankStart = 0, options = listOf("p", "b", "d"), imageName = "pig"),
    FamilyPracticeQuestion(word = "pot", blankStart = 0, options = listOf("p", "h", "d"), imageName = "pot"),
    FamilyPracticeQuestion(word = "sun", blankStart = 0, options = listOf("s", "r", "f"), imageName = "sun"),
    FamilyPracticeQuestion(word = "map", blankStart = 0, options = listOf("m", "c", "n"), imageName = "map"),
    FamilyPracticeQuestion(word = "jug", blankStart = 0, options = listOf("j", "b", "r"), imageName = "jug"),
    FamilyPracticeQuestion(word = "fox", blankStart = 0, options = listOf("f", "b", "d"), imageName = "fox")
)

// ── Practice ViewModel ─────────────────────────────────────────────────────────

@HiltViewModel
class WordFamiliesPracticeViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager,
    private val levelProgressRepo: PhonicsLevelProgressRepository,
    private val phonicsSessions: PhonicsSessionRecorder
) : ViewModel() {

    private val questions = wordFamiliesPracticeQuestions.shuffled().take(10)
    var uiState by mutableStateOf(FamilyPracticeUiState()); private set

    // Parent-report session tracking
    private var sessionStartMs = System.currentTimeMillis()
    private val wrongWords = mutableListOf<String>()
    private val correctWords = mutableListOf<String>()

    val totalQuestions: Int get() = questions.size
    val currentQuestion: FamilyPracticeQuestion? get() = questions.getOrNull(uiState.currentIndex)

    private var shakeJob: Job? = null

    fun onAnswerTap(answer: String) {
        val q = currentQuestion ?: return
        if (uiState.selectedAnswer != null) return
        val correct = answer == q.correct
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
        uiState = FamilyPracticeUiState()
    }

    fun stop() { audioManager.stop() }

    override fun onCleared() { super.onCleared(); stop() }

    private fun advance() {
        val next = uiState.currentIndex + 1
        uiState = if (next >= questions.size) {
            levelProgressRepo.recordPractice(level = PhonicsListenLevelKey.wordFamilies, score = uiState.score, total = questions.size)
            phonicsSessions.recordPractice(PhonicsListenLevelKey.wordFamilies, uiState.score, questions.size, ((System.currentTimeMillis() - sessionStartMs) / 1000).toInt(), wrongWords.toList(), correctWords.toList())
            uiState.copy(isFinished = true)
        } else {
            uiState.copy(currentIndex = next, selectedAnswer = null, isCorrect = null, shakeWrong = false)
        }
    }
}
