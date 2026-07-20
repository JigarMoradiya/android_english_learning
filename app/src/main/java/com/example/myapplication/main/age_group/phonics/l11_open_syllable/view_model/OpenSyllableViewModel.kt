package com.example.myapplication.main.age_group.phonics.l11_open_syllable.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
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

enum class OpenSyllableGroup(
    val label: String,
    val emoji: String,
    val color: Color,
    val shadowColor: Color,
    val rule: String,
    val hint: String
) {
    E_WORDS("E Words", "🌿", Color(0xFF00897B), Color(0xFF00695C),
        "Vowel E at the end says /ē/",
        "me he she be we"),
    O_WORDS("O Words", "🌕", Color(0xFFE65100), Color(0xFFBF360C),
        "Vowel O at the end says /ō/",
        "go no so do pro"),
    I_Y_WORDS("I / Y Words", "⚡", Color(0xFF6A1B9A), Color(0xFF4A148C),
        "Vowel I or Y at end says /ī/",
        "hi by my fly shy sky")
}

data class OpenSyllableWord(
    val id: String = UUID.randomUUID().toString(),
    val word: String,
    val vowelIndex: Int
) {
    val prefix: String get() = word.take(vowelIndex)
    val vowelPart: String get() = word[vowelIndex].toString()
    val suffix: String get() = word.drop(vowelIndex + 1)
}

data class OpenSyllablePracticeQuestion(
    val id: String = UUID.randomUUID().toString(),
    val word: String,
    val correctVowel: String,
    val options: List<String>
)

// ── UI States ─────────────────────────────────────────────────────────────────

data class OpenSyllableLearnUiState(
    val selectedGroup: OpenSyllableGroup = OpenSyllableGroup.E_WORDS,
    val highlightedWordId: String? = null
)

data class OpenSyllablePracticeUiState(
    val currentIndex: Int = 0,
    val score: Int = 0,
    val selectedAnswer: String? = null,
    val isCorrect: Boolean? = null,
    val isFinished: Boolean = false,
    val shakeWrong: Boolean = false
)

// ── Data ───────────────────────────────────────────────────────────────────────

val openSyllableData: Map<OpenSyllableGroup, List<OpenSyllableWord>> = mapOf(
    OpenSyllableGroup.E_WORDS to listOf(
        OpenSyllableWord(word = "me",  vowelIndex = 1),
        OpenSyllableWord(word = "he",  vowelIndex = 1),
        OpenSyllableWord(word = "she", vowelIndex = 2),
        OpenSyllableWord(word = "be",  vowelIndex = 1),
        OpenSyllableWord(word = "we",  vowelIndex = 1),
        OpenSyllableWord(word = "the", vowelIndex = 2)
    ),
    OpenSyllableGroup.O_WORDS to listOf(
        OpenSyllableWord(word = "go",  vowelIndex = 1),
        OpenSyllableWord(word = "no",  vowelIndex = 1),
        OpenSyllableWord(word = "so",  vowelIndex = 1),
        OpenSyllableWord(word = "do",  vowelIndex = 1),
        OpenSyllableWord(word = "pro", vowelIndex = 2),
        OpenSyllableWord(word = "yo",  vowelIndex = 1)
    ),
    OpenSyllableGroup.I_Y_WORDS to listOf(
        OpenSyllableWord(word = "hi",  vowelIndex = 1),
        OpenSyllableWord(word = "by",  vowelIndex = 1),
        OpenSyllableWord(word = "my",  vowelIndex = 1),
        OpenSyllableWord(word = "fly", vowelIndex = 2),
        OpenSyllableWord(word = "shy", vowelIndex = 2),
        OpenSyllableWord(word = "sky", vowelIndex = 2),
        OpenSyllableWord(word = "cry", vowelIndex = 2),
        OpenSyllableWord(word = "dry", vowelIndex = 2)
    )
)

val openSyllablePracticeQuestions: List<OpenSyllablePracticeQuestion> = listOf(
    OpenSyllablePracticeQuestion(word = "me",  correctVowel = "e", options = listOf("e", "o", "i")),
    OpenSyllablePracticeQuestion(word = "go",  correctVowel = "o", options = listOf("o", "e", "i")),
    OpenSyllablePracticeQuestion(word = "fly", correctVowel = "y", options = listOf("y", "e", "o")),
    OpenSyllablePracticeQuestion(word = "he",  correctVowel = "e", options = listOf("e", "i", "o")),
    OpenSyllablePracticeQuestion(word = "no",  correctVowel = "o", options = listOf("o", "y", "e")),
    OpenSyllablePracticeQuestion(word = "my",  correctVowel = "y", options = listOf("y", "o", "e")),
    OpenSyllablePracticeQuestion(word = "she", correctVowel = "e", options = listOf("e", "o", "i")),
    OpenSyllablePracticeQuestion(word = "so",  correctVowel = "o", options = listOf("o", "e", "y")),
    OpenSyllablePracticeQuestion(word = "sky", correctVowel = "y", options = listOf("y", "i", "o")),
    OpenSyllablePracticeQuestion(word = "be",  correctVowel = "e", options = listOf("e", "o", "y")),
    OpenSyllablePracticeQuestion(word = "do",  correctVowel = "o", options = listOf("o", "e", "i")),
    OpenSyllablePracticeQuestion(word = "shy", correctVowel = "y", options = listOf("y", "e", "o")),
    OpenSyllablePracticeQuestion(word = "we",  correctVowel = "e", options = listOf("e", "i", "o")),
    OpenSyllablePracticeQuestion(word = "hi",  correctVowel = "i", options = listOf("i", "e", "o")),
    OpenSyllablePracticeQuestion(word = "cry", correctVowel = "y", options = listOf("y", "o", "e"))
)

// ── Learn ViewModel ────────────────────────────────────────────────────────────

@HiltViewModel
class OpenSyllableLearnViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {

    var uiState by mutableStateOf(OpenSyllableLearnUiState()); private set

    fun onGroupTap(group: OpenSyllableGroup) {
        audioManager.stop()
        uiState = OpenSyllableLearnUiState(selectedGroup = group)
    }

    fun onWordTap(word: OpenSyllableWord) {
        val id = word.id
        audioManager.stop()
        uiState = uiState.copy(highlightedWordId = id)
        audioManager.playPhonicsSound("phonics_word/${word.word}")
        audioManager.onAudioCompleted = {
            if (uiState.highlightedWordId == id) uiState = uiState.copy(highlightedWordId = null)
        }
    }

    fun onVowelSoundTap(vowel: String) {
        audioManager.playPhonicsSound("phonics_letter/sound_$vowel")
    }

    fun stop() { audioManager.stop() }

    override fun onCleared() { super.onCleared(); stop() }
}

// ── Practice ViewModel ─────────────────────────────────────────────────────────

@HiltViewModel
class OpenSyllablePracticeViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager,
    private val levelProgressRepo: PhonicsLevelProgressRepository,
    private val phonicsSessions: PhonicsSessionRecorder
) : ViewModel() {

    private val questions = openSyllablePracticeQuestions.shuffled().take(10)
    var uiState by mutableStateOf(OpenSyllablePracticeUiState()); private set

    // Parent-report session tracking
    private var sessionStartMs = System.currentTimeMillis()
    private val wrongWords = mutableListOf<String>()
    private val correctWords = mutableListOf<String>()

    val totalQuestions: Int get() = questions.size
    val currentQuestion: OpenSyllablePracticeQuestion? get() =
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
        uiState = OpenSyllablePracticeUiState()
    }

    fun stop() { audioManager.stop() }

    override fun onCleared() { super.onCleared(); stop() }

    private fun advance() {
        val next = uiState.currentIndex + 1
        uiState = if (next >= questions.size) {
            levelProgressRepo.recordPractice(level = PhonicsListenLevelKey.openSyllable, score = uiState.score, total = questions.size)
            phonicsSessions.recordPractice(PhonicsListenLevelKey.openSyllable, uiState.score, questions.size, ((System.currentTimeMillis() - sessionStartMs) / 1000).toInt(), wrongWords.toList(), correctWords.toList())
            uiState.copy(isFinished = true)
        } else {
            uiState.copy(currentIndex = next, selectedAnswer = null, isCorrect = null, shakeWrong = false)
        }
    }
}
