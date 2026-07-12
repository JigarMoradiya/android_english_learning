package com.example.myapplication.main.age_group.phonics.l17_y_as_vowel.view_model

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

data class YAsVowelWord(val word: String) {
    // Index of 'y' in the word for highlighting
    val yIndex: Int get() = word.indexOf('y')
    val pre: String get() = if (yIndex >= 0) word.substring(0, yIndex) else word
    val highlight: String get() = if (yIndex >= 0) "y" else ""
    val suf: String get() = if (yIndex >= 0 && yIndex + 1 < word.length) word.substring(yIndex + 1) else ""
}

data class YAsVowelGroup(
    val label: String,
    val sound: String,
    val rule: String,
    val emoji: String,
    val accentColor: Color,
    val shadowColor: Color,
    val words: List<YAsVowelWord>
)

// MARK: - Data

val yAsVowelGroups: List<YAsVowelGroup> = listOf(
    YAsVowelGroup(
        label = "Y = /ī/",
        sound = "/ī/ — long I",
        rule = "Y at end of a 1-syllable word says its long name: I",
        emoji = "🌙",
        accentColor = Color(0xFF0097A7),
        shadowColor = Color(0xFF006064),
        words = listOf(
            YAsVowelWord("fly"), YAsVowelWord("sky"), YAsVowelWord("cry"),
            YAsVowelWord("dry"), YAsVowelWord("spy"), YAsVowelWord("fry"),
            YAsVowelWord("my"), YAsVowelWord("by"), YAsVowelWord("try"),
            YAsVowelWord("why")
        )
    ),
    YAsVowelGroup(
        label = "Y = /ē/",
        sound = "/ē/ — long E",
        rule = "Y at end of a 2+ syllable word says E",
        emoji = "😊",
        accentColor = Color(0xFF00838F),
        shadowColor = Color(0xFF005B6A),
        words = listOf(
            YAsVowelWord("happy"), YAsVowelWord("baby"), YAsVowelWord("funny"),
            YAsVowelWord("candy"), YAsVowelWord("puppy"), YAsVowelWord("sunny"),
            YAsVowelWord("windy"), YAsVowelWord("tiny"), YAsVowelWord("body"),
            YAsVowelWord("cozy")
        )
    ),
    YAsVowelGroup(
        label = "Y in middle",
        sound = "/ī/ in middle",
        rule = "Y in the middle of a word usually says I",
        emoji = "💪",
        accentColor = Color(0xFF006064),
        shadowColor = Color(0xFF004D40),
        words = listOf(
            YAsVowelWord("gym"), YAsVowelWord("myth"), YAsVowelWord("lynx"),
            YAsVowelWord("crypt"), YAsVowelWord("hymn"), YAsVowelWord("nymph"),
            YAsVowelWord("pygmy")
        )
    )
)

// MARK: - Learn ViewModel

data class YAsVowelLearnUiState(
    val selectedGroupIndex: Int = 0,
    val highlightedWord: String? = null,
    val showWords: Boolean = false
)

@HiltViewModel
class YAsVowelLearnViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {
    var uiState by mutableStateOf(YAsVowelLearnUiState()); private set

    val selectedGroup: YAsVowelGroup get() = yAsVowelGroups[uiState.selectedGroupIndex]

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

    fun onWordTap(word: YAsVowelWord) {
        uiState = uiState.copy(highlightedWord = word.word)
        audioManager.playPhonicsSound("phonics_word/${word.word}")
    }
}

// MARK: - Practice

data class YAsVowelPracticeQuestion(
    val word: String,
    val answer: String  // "/ī/" or "/ē/"
)

val yAsVowelPracticeQuestions: List<YAsVowelPracticeQuestion> = listOf(
    YAsVowelPracticeQuestion("fly",   "/ī/"),
    YAsVowelPracticeQuestion("sky",   "/ī/"),
    YAsVowelPracticeQuestion("cry",   "/ī/"),
    YAsVowelPracticeQuestion("dry",   "/ī/"),
    YAsVowelPracticeQuestion("spy",   "/ī/"),
    YAsVowelPracticeQuestion("gym",   "/ī/"),
    YAsVowelPracticeQuestion("myth",  "/ī/"),
    YAsVowelPracticeQuestion("happy", "/ē/"),
    YAsVowelPracticeQuestion("baby",  "/ē/"),
    YAsVowelPracticeQuestion("funny", "/ē/"),
    YAsVowelPracticeQuestion("candy", "/ē/"),
    YAsVowelPracticeQuestion("puppy", "/ē/"),
    YAsVowelPracticeQuestion("sunny", "/ē/"),
    YAsVowelPracticeQuestion("windy", "/ē/"),
    YAsVowelPracticeQuestion("tiny",  "/ē/")
)

data class YAsVowelPracticeUiState(
    val currentIndex: Int = 0,
    val score: Int = 0,
    val selectedAnswer: String? = null,
    val isCorrect: Boolean? = null,
    val isFinished: Boolean = false,
    val shakeWrong: Boolean = false
)

@HiltViewModel
class YAsVowelPracticeViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager,
    private val levelProgressRepo: PhonicsLevelProgressRepository,
    private val phonicsSessions: PhonicsSessionRecorder
) : ViewModel() {
    var uiState by mutableStateOf(YAsVowelPracticeUiState()); private set

    // Parent-report session tracking
    private var sessionStartMs = System.currentTimeMillis()
    private val wrongWords = mutableListOf<String>()
    private val correctWords = mutableListOf<String>()
    private val questions = yAsVowelPracticeQuestions.shuffled()

    val totalQuestions: Int get() = questions.size
    val currentQuestion: YAsVowelPracticeQuestion? get() = questions.getOrNull(uiState.currentIndex)

    fun onAnswerTap(answer: String) {
        if (uiState.selectedAnswer != null) return
        val q = currentQuestion ?: return
        val correct = answer == q.answer
        uiState = uiState.copy(selectedAnswer = answer, isCorrect = correct)
        if (correct) correctWords.add(q.word) else wrongWords.add(q.word)
        if (correct) {
            audioManager.playPhonicsSound("phonics_word/${q.word}")
        } else {
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
        uiState = YAsVowelPracticeUiState()
    }

    private fun advance() {
        val next = uiState.currentIndex + 1
        val newScore = uiState.score + (if (uiState.isCorrect == true) 1 else 0)
        if (next >= questions.size) {
            uiState = uiState.copy(isFinished = true, score = newScore)
            levelProgressRepo.recordPractice(level = PhonicsListenLevelKey.yAsVowel, score = newScore, total = questions.size)
            phonicsSessions.recordPractice(PhonicsListenLevelKey.yAsVowel, newScore, questions.size, ((System.currentTimeMillis() - sessionStartMs) / 1000).toInt(), wrongWords.toList(), correctWords.toList())
        }
        else { uiState = YAsVowelPracticeUiState(currentIndex = next, score = newScore) }
    }
}
