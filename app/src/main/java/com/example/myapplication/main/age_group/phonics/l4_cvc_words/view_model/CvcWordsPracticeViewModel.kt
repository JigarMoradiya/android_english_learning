package com.example.myapplication.main.age_group.phonics.l4_cvc_words.view_model

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

/** "Find the missing letter" in a CVC word, e.g. c_t → a (cat). */
data class CvcPracticeQuestion(
    val id: String = UUID.randomUUID().toString(),
    val word: String,          // full word e.g. "cat"
    val blankStart: Int,       // index of the blanked letter
    val options: List<String>, // 3 letter choices, including the correct one
    val imageName: String,     // asset image name
    val blankLen: Int = 1
) {
    val correct: String get() = word.substring(blankStart, blankStart + blankLen)
    val prefix: String get() = word.take(blankStart)
    val suffix: String get() = word.drop(blankStart + blankLen)
}

data class CvcPracticeUiState(
    val currentIndex: Int = 0,
    val score: Int = 0,
    val selectedAnswer: String? = null,
    val isCorrect: Boolean? = null,
    val isFinished: Boolean = false,
    val shakeWrong: Boolean = false
)

// ── Data ───────────────────────────────────────────────────────────────────────

val cvcPracticeQuestions: List<CvcPracticeQuestion> = listOf(
    CvcPracticeQuestion(word = "cat", blankStart = 0, options = listOf("c", "b", "h"), imageName = "cat"),
    CvcPracticeQuestion(word = "dog", blankStart = 0, options = listOf("d", "l", "f"), imageName = "dog"),
    CvcPracticeQuestion(word = "pig", blankStart = 0, options = listOf("p", "b", "d"), imageName = "pig"),
    CvcPracticeQuestion(word = "hat", blankStart = 2, options = listOf("t", "n", "d"), imageName = "hat"),
    CvcPracticeQuestion(word = "sun", blankStart = 1, options = listOf("u", "a", "o"), imageName = "sun"),
    CvcPracticeQuestion(word = "bat", blankStart = 0, options = listOf("b", "c", "r"), imageName = "bat"),
    CvcPracticeQuestion(word = "pen", blankStart = 2, options = listOf("n", "t", "g"), imageName = "pen"),
    CvcPracticeQuestion(word = "hen", blankStart = 1, options = listOf("e", "a", "i"), imageName = "hen"),
    CvcPracticeQuestion(word = "rat", blankStart = 0, options = listOf("r", "c", "m"), imageName = "rat"),
    CvcPracticeQuestion(word = "pot", blankStart = 2, options = listOf("t", "p", "d"), imageName = "pot"),
    CvcPracticeQuestion(word = "fan", blankStart = 0, options = listOf("f", "m", "p"), imageName = "fan"),
    CvcPracticeQuestion(word = "map", blankStart = 1, options = listOf("a", "e", "o"), imageName = "map"),
    CvcPracticeQuestion(word = "van", blankStart = 0, options = listOf("v", "f", "m"), imageName = "van"),
    CvcPracticeQuestion(word = "jug", blankStart = 1, options = listOf("u", "a", "o"), imageName = "jug"),
    CvcPracticeQuestion(word = "net", blankStart = 2, options = listOf("t", "d", "p"), imageName = "net")
)

// ── Practice ViewModel ─────────────────────────────────────────────────────────

@HiltViewModel
class CvcWordsPracticeViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager,
    private val levelProgressRepo: PhonicsLevelProgressRepository,
    private val phonicsSessions: PhonicsSessionRecorder
) : ViewModel() {

    private val questions = cvcPracticeQuestions.shuffled().take(10)
    var uiState by mutableStateOf(CvcPracticeUiState()); private set

    // Parent-report session tracking
    private var sessionStartMs = System.currentTimeMillis()
    private val wrongWords = mutableListOf<String>()
    private val correctWords = mutableListOf<String>()

    val totalQuestions: Int get() = questions.size
    val currentQuestion: CvcPracticeQuestion? get() = questions.getOrNull(uiState.currentIndex)

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
        uiState = CvcPracticeUiState()
    }

    fun stop() { audioManager.stop() }

    override fun onCleared() { super.onCleared(); stop() }

    private fun advance() {
        val next = uiState.currentIndex + 1
        uiState = if (next >= questions.size) {
            levelProgressRepo.recordPractice(level = PhonicsListenLevelKey.cvcWords, score = uiState.score, total = questions.size)
            phonicsSessions.recordPractice(PhonicsListenLevelKey.cvcWords, uiState.score, questions.size, ((System.currentTimeMillis() - sessionStartMs) / 1000).toInt(), wrongWords.toList(), correctWords.toList())
            uiState.copy(isFinished = true)
        } else {
            uiState.copy(currentIndex = next, selectedAnswer = null, isCorrect = null, shakeWrong = false)
        }
    }
}
