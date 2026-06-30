package com.example.myapplication.main.age_group.phonics.l18_three_letter_blends.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

// MARK: - Models

data class ThreeLetterBlendsWord(val word: String, val blend: String) {
    val blendEnd: Int get() = blend.length
    val pre: String get() = ""
    val highlight: String get() = blend
    val suf: String get() = word.substring(blendEnd)
}

data class ThreeLetterBlendsGroup(
    val blend: String,
    val rule: String,
    val emoji: String,
    val accentColor: Color,
    val shadowColor: Color,
    val words: List<ThreeLetterBlendsWord>
)

// MARK: - Data

val threeLetterBlendsGroups: List<ThreeLetterBlendsGroup> = listOf(
    ThreeLetterBlendsGroup(
        blend = "str",
        rule = "str — three sounds snap together!",
        emoji = "💪",
        accentColor = Color(0xFFF9A825),
        shadowColor = Color(0xFFE65100),
        words = listOf(
            ThreeLetterBlendsWord("strong",  "str"),
            ThreeLetterBlendsWord("street",  "str"),
            ThreeLetterBlendsWord("string",  "str"),
            ThreeLetterBlendsWord("strip",   "str"),
            ThreeLetterBlendsWord("strange", "str"),
            ThreeLetterBlendsWord("stream",  "str"),
            ThreeLetterBlendsWord("strike",  "str"),
            ThreeLetterBlendsWord("stripe",  "str")
        )
    ),
    ThreeLetterBlendsGroup(
        blend = "spl",
        rule = "spl — s+p+l all at once!",
        emoji = "💦",
        accentColor = Color(0xFF0097A7),
        shadowColor = Color(0xFF006064),
        words = listOf(
            ThreeLetterBlendsWord("splash",    "spl"),
            ThreeLetterBlendsWord("split",     "spl"),
            ThreeLetterBlendsWord("splat",     "spl"),
            ThreeLetterBlendsWord("splendid",  "spl"),
            ThreeLetterBlendsWord("splinter",  "spl")
        )
    ),
    ThreeLetterBlendsGroup(
        blend = "spr",
        rule = "spr — like spring bursting open!",
        emoji = "🌱",
        accentColor = Color(0xFF558B2F),
        shadowColor = Color(0xFF33691E),
        words = listOf(
            ThreeLetterBlendsWord("spring",   "spr"),
            ThreeLetterBlendsWord("sprint",   "spr"),
            ThreeLetterBlendsWord("spray",    "spr"),
            ThreeLetterBlendsWord("spread",   "spr"),
            ThreeLetterBlendsWord("sprout",   "spr"),
            ThreeLetterBlendsWord("sprinkle", "spr")
        )
    ),
    ThreeLetterBlendsGroup(
        blend = "thr",
        rule = "thr — th digraph + r",
        emoji = "3️⃣",
        accentColor = Color(0xFF6A1B9A),
        shadowColor = Color(0xFF4A148C),
        words = listOf(
            ThreeLetterBlendsWord("three",  "thr"),
            ThreeLetterBlendsWord("threw",  "thr"),
            ThreeLetterBlendsWord("throw",  "thr"),
            ThreeLetterBlendsWord("through","thr"),
            ThreeLetterBlendsWord("thread", "thr"),
            ThreeLetterBlendsWord("throat", "thr"),
            ThreeLetterBlendsWord("thrill", "thr")
        )
    ),
    ThreeLetterBlendsGroup(
        blend = "scr",
        rule = "scr — s+c+r all at the start!",
        emoji = "😱",
        accentColor = Color(0xFFBF360C),
        shadowColor = Color(0xFF7F0000),
        words = listOf(
            ThreeLetterBlendsWord("scream",   "scr"),
            ThreeLetterBlendsWord("scratch",  "scr"),
            ThreeLetterBlendsWord("scrap",    "scr"),
            ThreeLetterBlendsWord("screen",   "scr"),
            ThreeLetterBlendsWord("scroll",   "scr"),
            ThreeLetterBlendsWord("scramble", "scr")
        )
    )
)

// MARK: - Learn ViewModel

data class ThreeLetterBlendsLearnUiState(
    val selectedGroupIndex: Int = 0,
    val highlightedWord: String? = null,
    val showWords: Boolean = false
)

@HiltViewModel
class ThreeLetterBlendsLearnViewModel @Inject constructor() : ViewModel() {
    var uiState by mutableStateOf(ThreeLetterBlendsLearnUiState()); private set

    val selectedGroup: ThreeLetterBlendsGroup get() = threeLetterBlendsGroups[uiState.selectedGroupIndex]

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

    fun onWordTap(word: ThreeLetterBlendsWord) {
        uiState = uiState.copy(highlightedWord = word.word)
    }
}

// MARK: - Practice

data class ThreeLetterBlendsPracticeQuestion(
    val word: String,
    val answer: String,          // the correct blend
    val options: List<String>    // 4 blend choices
) {
    val displayWord: String get() = "___${word.substring(answer.length)}"
}

val threeLetterBlendsPracticeQuestions: List<ThreeLetterBlendsPracticeQuestion> = listOf(
    ThreeLetterBlendsPracticeQuestion("strong",  "str", listOf("str", "spl", "spr", "scr")),
    ThreeLetterBlendsPracticeQuestion("street",  "str", listOf("str", "thr", "scr", "spl")),
    ThreeLetterBlendsPracticeQuestion("splash",  "spl", listOf("spl", "str", "spr", "scr")),
    ThreeLetterBlendsPracticeQuestion("split",   "spl", listOf("spl", "thr", "str", "scr")),
    ThreeLetterBlendsPracticeQuestion("spring",  "spr", listOf("spr", "str", "spl", "thr")),
    ThreeLetterBlendsPracticeQuestion("spray",   "spr", listOf("spr", "scr", "thr", "spl")),
    ThreeLetterBlendsPracticeQuestion("three",   "thr", listOf("thr", "str", "scr", "spl")),
    ThreeLetterBlendsPracticeQuestion("throw",   "thr", listOf("thr", "spr", "str", "scr")),
    ThreeLetterBlendsPracticeQuestion("scream",  "scr", listOf("scr", "str", "thr", "spl")),
    ThreeLetterBlendsPracticeQuestion("scratch", "scr", listOf("scr", "spr", "str", "thr")),
    ThreeLetterBlendsPracticeQuestion("string",  "str", listOf("str", "spr", "thr", "scr")),
    ThreeLetterBlendsPracticeQuestion("sprout",  "spr", listOf("spr", "str", "scr", "spl")),
    ThreeLetterBlendsPracticeQuestion("thread",  "thr", listOf("thr", "str", "spl", "scr")),
    ThreeLetterBlendsPracticeQuestion("screen",  "scr", listOf("scr", "thr", "spl", "spr")),
    ThreeLetterBlendsPracticeQuestion("strip",   "str", listOf("str", "spl", "thr", "scr"))
)

data class ThreeLetterBlendsPracticeUiState(
    val currentIndex: Int = 0,
    val score: Int = 0,
    val selectedAnswer: String? = null,
    val isCorrect: Boolean? = null,
    val isFinished: Boolean = false,
    val shakeWrong: Boolean = false
)

@HiltViewModel
class ThreeLetterBlendsPracticeViewModel @Inject constructor() : ViewModel() {
    var uiState by mutableStateOf(ThreeLetterBlendsPracticeUiState()); private set
    private val questions = threeLetterBlendsPracticeQuestions.shuffled()

    val totalQuestions: Int get() = questions.size
    val currentQuestion: ThreeLetterBlendsPracticeQuestion? get() = questions.getOrNull(uiState.currentIndex)

    fun onAnswerTap(answer: String) {
        if (uiState.selectedAnswer != null) return
        val q = currentQuestion ?: return
        val correct = answer == q.answer
        uiState = uiState.copy(selectedAnswer = answer, isCorrect = correct)
        if (!correct) {
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

    fun restart() { uiState = ThreeLetterBlendsPracticeUiState() }

    private fun advance() {
        val next = uiState.currentIndex + 1
        val newScore = uiState.score + (if (uiState.isCorrect == true) 1 else 0)
        if (next >= questions.size) { uiState = uiState.copy(isFinished = true, score = newScore) }
        else { uiState = ThreeLetterBlendsPracticeUiState(currentIndex = next, score = newScore) }
    }
}
