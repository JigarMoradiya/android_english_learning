package com.example.myapplication.main.age_group.phonics.l19_soft_c_soft_g.view_model

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

// MARK: - Models

data class SoftCSoftGWord(val word: String, val letter: String) {
    val letterIndex: Int get() = word.indexOf(letter)
    val pre: String get() = if (letterIndex >= 0) word.substring(0, letterIndex) else word
    val highlight: String get() = if (letterIndex >= 0) letter else ""
    val suf: String get() = if (letterIndex >= 0 && letterIndex + 1 < word.length) word.substring(letterIndex + 1) else ""
}

data class SoftCSoftGGroup(
    val title: String,
    val sound: String,
    val rule: String,
    val emoji: String,
    val isSoft: Boolean,
    val letter: String,
    val accentColor: Color,
    val shadowColor: Color,
    val words: List<SoftCSoftGWord>
)

// MARK: - Data

val softCSoftGGroups: List<SoftCSoftGGroup> = listOf(
    SoftCSoftGGroup(
        title = "Soft C",
        sound = "/s/ sound",
        rule = "C before e, i, or y → says /s/ like snake",
        emoji = "🐍",
        isSoft = true,
        letter = "c",
        accentColor = Color(0xFFBF360C),
        shadowColor = Color(0xFF7F0000),
        words = listOf(
            SoftCSoftGWord("city",   "c"), SoftCSoftGWord("cent",   "c"),
            SoftCSoftGWord("cycle",  "c"), SoftCSoftGWord("circle", "c"),
            SoftCSoftGWord("cell",   "c"), SoftCSoftGWord("cereal", "c"),
            SoftCSoftGWord("cinema", "c"), SoftCSoftGWord("dice",   "c"),
            SoftCSoftGWord("face",   "c"), SoftCSoftGWord("mice",   "c"),
            SoftCSoftGWord("nice",   "c"), SoftCSoftGWord("ice",    "c"),
            SoftCSoftGWord("grace",  "c"), SoftCSoftGWord("place",  "c")
        )
    ),
    SoftCSoftGGroup(
        title = "Hard C",
        sound = "/k/ sound",
        rule = "C before a, o, or u → says /k/ like cat",
        emoji = "🐱",
        isSoft = false,
        letter = "c",
        accentColor = Color(0xFF546E7A),
        shadowColor = Color(0xFF37474F),
        words = listOf(
            SoftCSoftGWord("cat",  "c"), SoftCSoftGWord("cup",  "c"),
            SoftCSoftGWord("coat", "c"), SoftCSoftGWord("car",  "c"),
            SoftCSoftGWord("cold", "c"), SoftCSoftGWord("cute", "c"),
            SoftCSoftGWord("calm", "c"), SoftCSoftGWord("cord", "c")
        )
    ),
    SoftCSoftGGroup(
        title = "Soft G",
        sound = "/j/ sound",
        rule = "G before e, i, or y → says /j/ like jar",
        emoji = "🫙",
        isSoft = true,
        letter = "g",
        accentColor = Color(0xFFE64A19),
        shadowColor = Color(0xFFBF360C),
        words = listOf(
            SoftCSoftGWord("gem",     "g"), SoftCSoftGWord("giraffe", "g"),
            SoftCSoftGWord("gym",     "g"), SoftCSoftGWord("giant",   "g"),
            SoftCSoftGWord("ginger",  "g"), SoftCSoftGWord("gentle",  "g"),
            SoftCSoftGWord("germ",    "g"), SoftCSoftGWord("cage",    "g"),
            SoftCSoftGWord("age",     "g"), SoftCSoftGWord("page",    "g"),
            SoftCSoftGWord("stage",   "g"), SoftCSoftGWord("orange",  "g")
        )
    ),
    SoftCSoftGGroup(
        title = "Hard G",
        sound = "/g/ sound",
        rule = "G before a, o, or u → says /g/ like gorilla",
        emoji = "🦍",
        isSoft = false,
        letter = "g",
        accentColor = Color(0xFF455A64),
        shadowColor = Color(0xFF263238),
        words = listOf(
            SoftCSoftGWord("gap",  "g"), SoftCSoftGWord("got",  "g"),
            SoftCSoftGWord("gum",  "g"), SoftCSoftGWord("game", "g"),
            SoftCSoftGWord("good", "g"), SoftCSoftGWord("girl", "g")
        )
    )
)

// MARK: - Learn ViewModel

data class SoftCSoftGLearnUiState(
    val selectedGroupIndex: Int = 0,
    val highlightedWord: String? = null,
    val showWords: Boolean = false
)

@HiltViewModel
class SoftCSoftGLearnViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {
    var uiState by mutableStateOf(SoftCSoftGLearnUiState()); private set

    val selectedGroup: SoftCSoftGGroup get() = softCSoftGGroups[uiState.selectedGroupIndex]

    fun onScreenAppear() {
        uiState = uiState.copy(showWords = false, highlightedWord = null)
        viewModelScope.launch {
            delay(100)
            uiState = uiState.copy(showWords = true)
        }
    }

    fun onGroupTap(index: Int) {
        if (index == uiState.selectedGroupIndex) return
        uiState = uiState.copy(highlightedWord = null, selectedGroupIndex = index)
    }

    fun onWordTap(word: SoftCSoftGWord) {
        uiState = uiState.copy(highlightedWord = word.word)
        audioManager.playPhonicsSound("phonics_word/${word.word}")
    }
}

// MARK: - Practice

data class SoftCSoftGPracticeQuestion(
    val word: String,
    val answer: String,   // "soft" or "hard"
    val letter: String    // "c" or "g" — which letter to highlight
)

val softCSoftGPracticeQuestions: List<SoftCSoftGPracticeQuestion> = listOf(
    SoftCSoftGPracticeQuestion("city",    "soft", "c"),
    SoftCSoftGPracticeQuestion("cent",    "soft", "c"),
    SoftCSoftGPracticeQuestion("ice",     "soft", "c"),
    SoftCSoftGPracticeQuestion("nice",    "soft", "c"),
    SoftCSoftGPracticeQuestion("gem",     "soft", "g"),
    SoftCSoftGPracticeQuestion("giraffe", "soft", "g"),
    SoftCSoftGPracticeQuestion("gym",     "soft", "g"),
    SoftCSoftGPracticeQuestion("giant",   "soft", "g"),
    SoftCSoftGPracticeQuestion("cat",     "hard", "c"),
    SoftCSoftGPracticeQuestion("cup",     "hard", "c"),
    SoftCSoftGPracticeQuestion("coat",    "hard", "c"),
    SoftCSoftGPracticeQuestion("gap",     "hard", "g"),
    SoftCSoftGPracticeQuestion("got",     "hard", "g"),
    SoftCSoftGPracticeQuestion("gum",     "hard", "g"),
    SoftCSoftGPracticeQuestion("cycle",   "soft", "c"),
    SoftCSoftGPracticeQuestion("circle",  "soft", "c")
)

data class SoftCSoftGPracticeUiState(
    val currentIndex: Int = 0,
    val score: Int = 0,
    val selectedAnswer: String? = null,
    val isCorrect: Boolean? = null,
    val isFinished: Boolean = false,
    val shakeWrong: Boolean = false
)

@HiltViewModel
class SoftCSoftGPracticeViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager,
    private val levelProgressRepo: PhonicsLevelProgressRepository,
    private val phonicsSessions: PhonicsSessionRecorder
) : ViewModel() {
    var uiState by mutableStateOf(SoftCSoftGPracticeUiState()); private set

    // Parent-report session tracking
    private var sessionStartMs = System.currentTimeMillis()
    private val wrongWords = mutableListOf<String>()
    private val correctWords = mutableListOf<String>()
    private val questions = softCSoftGPracticeQuestions.shuffled().take(10)

    val totalQuestions: Int get() = questions.size
    val currentQuestion: SoftCSoftGPracticeQuestion? get() = questions.getOrNull(uiState.currentIndex)

    fun onAnswerTap(answer: String) {
        if (uiState.selectedAnswer != null) return
        val q = currentQuestion ?: return
        val correct = answer == q.answer
        uiState = uiState.copy(selectedAnswer = answer, isCorrect = correct)
        if (correct) correctWords.add(q.word) else wrongWords.add(q.word)
        if (correct) {
            audioManager.playPhonicsSound("phonics_word/${q.word}")
        } else {
            AudioPlayerManager.playSoundWrongAnswer()
            viewModelScope.launch {
                delay(600); uiState = uiState.copy(shakeWrong = false)
            }
            uiState = uiState.copy(shakeWrong = true)
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
        uiState = SoftCSoftGPracticeUiState()
    }

    private fun advance() {
        val next = uiState.currentIndex + 1
        val newScore = uiState.score + (if (uiState.isCorrect == true) 1 else 0)
        if (next >= questions.size) {
            uiState = uiState.copy(isFinished = true, score = newScore)
            levelProgressRepo.recordPractice(level = PhonicsListenLevelKey.softCSoftG, score = newScore, total = questions.size)
            phonicsSessions.recordPractice(PhonicsListenLevelKey.softCSoftG, newScore, questions.size, ((System.currentTimeMillis() - sessionStartMs) / 1000).toInt(), wrongWords.toList(), correctWords.toList())
        }
        else { uiState = SoftCSoftGPracticeUiState(currentIndex = next, score = newScore) }
    }
}
