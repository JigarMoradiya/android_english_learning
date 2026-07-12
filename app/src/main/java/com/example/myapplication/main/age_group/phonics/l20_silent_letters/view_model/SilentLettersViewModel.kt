package com.example.myapplication.main.age_group.phonics.l20_silent_letters.view_model

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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

// MARK: - Models

data class SilentLettersWord(
    val word: String,
    val silentIndex: Int   // index of the silent letter in the word
) {
    // For learn-page display: split word around the silent letter
    val pre: String get() = word.substring(0, silentIndex)
    val silent: String get() = if (silentIndex < word.length) word[silentIndex].toString() else ""
    val suf: String get() = if (silentIndex + 1 < word.length) word.substring(silentIndex + 1) else ""
}

data class SilentLettersGroup(
    val pattern: String,
    val rule: String,
    val emoji: String,
    val accentColor: Color,
    val shadowColor: Color,
    val words: List<SilentLettersWord>
)

// MARK: - Data

val silentLettersGroups: List<SilentLettersGroup> = listOf(
    SilentLettersGroup(
        pattern = "kn",
        rule = "K is silent when followed by N — only N sounds",
        emoji = "🔕",
        accentColor = Color(0xFF455A64),
        shadowColor = Color(0xFF263238),
        words = listOf(
            SilentLettersWord("knife",  0),
            SilentLettersWord("know",   0),
            SilentLettersWord("kneel",  0),
            SilentLettersWord("knight", 0),
            SilentLettersWord("knit",   0),
            SilentLettersWord("knock",  0),
            SilentLettersWord("knot",   0),
            SilentLettersWord("knee",   0),
            SilentLettersWord("knack",  0)
        )
    ),
    SilentLettersGroup(
        pattern = "wr",
        rule = "W is silent when followed by R — only R sounds",
        emoji = "✍️",
        accentColor = Color(0xFF37474F),
        shadowColor = Color(0xFF263238),
        words = listOf(
            SilentLettersWord("write",   0),
            SilentLettersWord("wrist",   0),
            SilentLettersWord("wrong",   0),
            SilentLettersWord("wrap",    0),
            SilentLettersWord("wrote",   0),
            SilentLettersWord("wreck",   0),
            SilentLettersWord("wren",    0),
            SilentLettersWord("wrestle", 0),
            SilentLettersWord("wrinkle", 0)
        )
    ),
    SilentLettersGroup(
        pattern = "mb",
        rule = "B is silent when it follows M at the end of a word",
        emoji = "🐑",
        accentColor = Color(0xFF546E7A),
        shadowColor = Color(0xFF37474F),
        words = listOf(
            SilentLettersWord("lamb",  3),
            SilentLettersWord("climb", 4),
            SilentLettersWord("bomb",  3),
            SilentLettersWord("thumb", 4),
            SilentLettersWord("numb",  3),
            SilentLettersWord("comb",  3),
            SilentLettersWord("tomb",  3),
            SilentLettersWord("limb",  3),
            SilentLettersWord("crumb", 4)
        )
    ),
    SilentLettersGroup(
        pattern = "gn",
        rule = "G is silent when followed by N or at word's end before N",
        emoji = "🤫",
        accentColor = Color(0xFF607D8B),
        shadowColor = Color(0xFF455A64),
        words = listOf(
            SilentLettersWord("gnat",   0),
            SilentLettersWord("gnaw",   0),
            SilentLettersWord("gnome",  0),
            SilentLettersWord("sign",   2),
            SilentLettersWord("design", 4),
            SilentLettersWord("reign",  3),
            SilentLettersWord("align",  3)
        )
    ),
    SilentLettersGroup(
        pattern = "h",
        rule = "Sometimes H hides completely — you don't hear it at all",
        emoji = "🤐",
        accentColor = Color(0xFF00695C),
        shadowColor = Color(0xFF004D40),
        words = listOf(
            SilentLettersWord("hour",   0),
            SilentLettersWord("honest", 0),
            SilentLettersWord("ghost",  1)
        )
    ),
    SilentLettersGroup(
        pattern = "l",
        rule = "L can be silent after A — like in walk and calm",
        emoji = "🚶",
        accentColor = Color(0xFF8E24AA),
        shadowColor = Color(0xFF6A1B9A),
        words = listOf(
            SilentLettersWord("walk",  2),
            SilentLettersWord("talk",  2),
            SilentLettersWord("calm",  2),
            SilentLettersWord("half",  2)
        )
    ),
    SilentLettersGroup(
        pattern = "t",
        rule = "T can hide in the middle — like in listen and castle",
        emoji = "🏰",
        accentColor = Color(0xFFEF6C00),
        shadowColor = Color(0xFFE65100),
        words = listOf(
            SilentLettersWord("listen",  3),
            SilentLettersWord("castle",  3),
            SilentLettersWord("whistle", 4),
            SilentLettersWord("often",   2)
        )
    )
)

// MARK: - Learn ViewModel

data class SilentLettersLearnUiState(
    val selectedGroupIndex: Int = 0,
    val highlightedWord: String? = null,
    val showWords: Boolean = false
)

@HiltViewModel
class SilentLettersLearnViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {
    var uiState by mutableStateOf(SilentLettersLearnUiState()); private set

    val selectedGroup: SilentLettersGroup get() = silentLettersGroups[uiState.selectedGroupIndex]

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

    fun onWordTap(word: SilentLettersWord) {
        uiState = uiState.copy(highlightedWord = word.word)
        audioManager.playPhonicsSound("phonics_word/${word.word}")
    }
}

// MARK: - Practice

data class SilentLettersPracticeQuestion(
    val word: String,
    val silentIndex: Int   // index of the silent letter the child must tap
)

val silentLettersPracticeQuestions: List<SilentLettersPracticeQuestion> = listOf(
    SilentLettersPracticeQuestion("knife",  0),
    SilentLettersPracticeQuestion("know",   0),
    SilentLettersPracticeQuestion("kneel",  0),
    SilentLettersPracticeQuestion("knee",   0),
    SilentLettersPracticeQuestion("knock",  0),
    SilentLettersPracticeQuestion("write",  0),
    SilentLettersPracticeQuestion("wrist",  0),
    SilentLettersPracticeQuestion("wrong",  0),
    SilentLettersPracticeQuestion("wrap",   0),
    SilentLettersPracticeQuestion("wrote",  0),
    SilentLettersPracticeQuestion("lamb",   3),
    SilentLettersPracticeQuestion("climb",  4),
    SilentLettersPracticeQuestion("thumb",  4),
    SilentLettersPracticeQuestion("sign",   2),
    SilentLettersPracticeQuestion("gnome",  0),
    SilentLettersPracticeQuestion("gnat",   0),
    SilentLettersPracticeQuestion("hour",   0),
    SilentLettersPracticeQuestion("walk",   2),
    SilentLettersPracticeQuestion("talk",   2),
    SilentLettersPracticeQuestion("listen", 3),
    SilentLettersPracticeQuestion("castle", 3)
)

data class SilentLettersPracticeUiState(
    val currentIndex: Int = 0,
    val score: Int = 0,
    val selectedLetterIndex: Int? = null,   // which tile the child tapped
    val isCorrect: Boolean? = null,
    val isFinished: Boolean = false,
    val shakeIndex: Int? = null             // index of a wrong-tap tile to shake
)

@HiltViewModel
class SilentLettersPracticeViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager,
    private val levelProgressRepo: PhonicsLevelProgressRepository,
    private val phonicsSessions: PhonicsSessionRecorder
) : ViewModel() {
    var uiState by mutableStateOf(SilentLettersPracticeUiState()); private set

    // Parent-report session tracking
    private var sessionStartMs = System.currentTimeMillis()
    private val wrongWords = mutableListOf<String>()
    private val correctWords = mutableListOf<String>()
    private val questions = silentLettersPracticeQuestions.shuffled()

    val totalQuestions: Int get() = questions.size
    val currentQuestion: SilentLettersPracticeQuestion? get() = questions.getOrNull(uiState.currentIndex)

    fun onLetterTap(index: Int) {
        if (uiState.selectedLetterIndex != null) return
        val q = currentQuestion ?: return
        val correct = index == q.silentIndex
        uiState = uiState.copy(selectedLetterIndex = index, isCorrect = correct,
            shakeIndex = if (!correct) index else null)
        if (correct) {
            audioManager.playPhonicsSound("phonics_word/${q.word}")
        } else {
            viewModelScope.launch {
                delay(600); uiState = uiState.copy(shakeIndex = null)
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
        uiState = SilentLettersPracticeUiState()
    }

    private fun advance() {
        val next = uiState.currentIndex + 1
        val newScore = uiState.score + (if (uiState.isCorrect == true) 1 else 0)
        if (next >= questions.size) {
            uiState = uiState.copy(isFinished = true, score = newScore)
            levelProgressRepo.recordPractice(level = PhonicsListenLevelKey.silentLetters, score = newScore, total = questions.size)
            phonicsSessions.recordPractice(PhonicsListenLevelKey.silentLetters, newScore, questions.size, ((System.currentTimeMillis() - sessionStartMs) / 1000).toInt(), wrongWords.toList(), correctWords.toList())
        }
        else { uiState = SilentLettersPracticeUiState(currentIndex = next, score = newScore) }
    }
}
