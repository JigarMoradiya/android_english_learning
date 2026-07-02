package com.example.myapplication.main.age_group.phonics.l26_compound_words.view_model

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

// ── Models ────────────────────────────────────────────────────────────────────

data class CWWord(val part1: String, val part2: String, val emoji: String) {
    val full: String get() = part1 + part2
}

data class CWGroup(
    val name:        String,
    val emoji:       String,
    val accentColor: Color,
    val shadowColor: Color,
    val words:       List<CWWord>
)

data class CWPracticeQuestion(
    val part1:        String,
    val correctPart2: String,
    val options:      List<String>,
    val emoji:        String
) {
    val answer: String get() = part1 + correctPart2
}

// ── Data ──────────────────────────────────────────────────────────────────────

val cwGroups: List<CWGroup> = listOf(
    CWGroup(
        name = "Nature", emoji = "🌤️",
        accentColor = Color(0xFF2E7D32), shadowColor = Color(0xFF1B5E20),
        words = listOf(
            CWWord("rain",    "bow",    "🌈"),
            CWWord("sun",     "shine",  "☀️"),
            CWWord("snow",    "flake",  "❄️"),
            CWWord("sun",     "flower", "🌻"),
            CWWord("water",   "fall",   "💧"),
            CWWord("rain",    "drop",   "🌧️"),
            CWWord("moon",    "light",  "🌙"),
            CWWord("sea",     "shell",  "🐚"),
            CWWord("thunder", "storm",  "⛈️"),
            CWWord("star",    "light",  "⭐")
        )
    ),
    CWGroup(
        name = "Home", emoji = "🏠",
        accentColor = Color(0xFF1565C0), shadowColor = Color(0xFF0D47A1),
        words = listOf(
            CWWord("bed",    "room",   "🛏️"),
            CWWord("bath",   "room",   "🛁"),
            CWWord("tooth",  "brush",  "🪥"),
            CWWord("door",   "bell",   "🔔"),
            CWWord("book",   "case",   "📚"),
            CWWord("mail",   "box",    "📬"),
            CWWord("fire",   "place",  "🔥"),
            CWWord("stair",  "case",   "🏡"),
            CWWord("pillow", "case",   "😴"),
            CWWord("cup",    "board",  "🍽️")
        )
    ),
    CWGroup(
        name = "Animals", emoji = "🦋",
        accentColor = Color(0xFF7B1FA2), shadowColor = Color(0xFF4A148C),
        words = listOf(
            CWWord("butter", "fly",   "🦋"),
            CWWord("lady",   "bug",   "🐞"),
            CWWord("fire",   "fly",   "✨"),
            CWWord("star",   "fish",  "⭐"),
            CWWord("dragon", "fly",   "🐲"),
            CWWord("cat",    "fish",  "🐱"),
            CWWord("sea",    "horse", "🐠"),
            CWWord("bumble", "bee",   "🐝"),
            CWWord("earth",  "worm",  "🪱"),
            CWWord("blue",   "bird",  "🦅")
        )
    ),
    CWGroup(
        name = "Play", emoji = "⚽",
        accentColor = Color(0xFFC62828), shadowColor = Color(0xFFB71C1C),
        words = listOf(
            CWWord("foot",   "ball",   "⚽"),
            CWWord("back",   "pack",   "🎒"),
            CWWord("note",   "book",   "📓"),
            CWWord("play",   "ground", "🛝"),
            CWWord("base",   "ball",   "⚾"),
            CWWord("cup",    "cake",   "🧁"),
            CWWord("birth",  "day",    "🎂"),
            CWWord("air",    "plane",  "✈️"),
            CWWord("class",  "room",   "🏫"),
            CWWord("basket", "ball",   "🏀")
        )
    )
)

val cwPracticeQuestions: List<CWPracticeQuestion> = listOf(
    // Nature
    CWPracticeQuestion("rain",    "bow",    listOf("bow",    "drop",  "coat",  "fall"),   "🌈"),
    CWPracticeQuestion("sun",     "flower", listOf("flower", "shine", "set",   "light"),  "🌻"),
    CWPracticeQuestion("snow",    "flake",  listOf("flake",  "ball",  "man",   "storm"),  "❄️"),
    CWPracticeQuestion("water",   "fall",   listOf("fall",   "way",   "melon", "front"),  "💧"),
    CWPracticeQuestion("moon",    "light",  listOf("light",  "shine", "beam",  "flower"), "🌙"),
    // Home
    CWPracticeQuestion("tooth",   "brush",  listOf("brush",  "paste", "ache",  "fairy"),  "🪥"),
    CWPracticeQuestion("door",    "bell",   listOf("bell",   "step",  "way",   "knob"),   "🔔"),
    CWPracticeQuestion("book",    "case",   listOf("case",   "shelf", "worm",  "store"),  "📚"),
    CWPracticeQuestion("bed",     "room",   listOf("room",   "time",  "side",  "spread"), "🛏️"),
    CWPracticeQuestion("mail",    "box",    listOf("box",    "man",   "bag",   "slot"),   "📬"),
    // Animals
    CWPracticeQuestion("butter",  "fly",    listOf("fly",    "cup",   "scotch","milk"),   "🦋"),
    CWPracticeQuestion("lady",    "bug",    listOf("bug",    "bird",  "fish",  "cat"),    "🐞"),
    CWPracticeQuestion("star",    "fish",   listOf("fish",   "light", "bird",  "dust"),   "⭐"),
    CWPracticeQuestion("sea",     "horse",  listOf("horse",  "shell", "bird",  "weed"),   "🐠"),
    CWPracticeQuestion("bumble",  "bee",    listOf("bee",    "bug",   "fly",   "bird"),   "🐝"),
    // Play
    CWPracticeQuestion("foot",    "ball",   listOf("ball",   "print", "path",  "wear"),   "⚽"),
    CWPracticeQuestion("back",    "pack",   listOf("pack",   "yard",  "bone",  "flip"),   "🎒"),
    CWPracticeQuestion("birth",   "day",    listOf("day",    "mark",  "place", "right"),  "🎂"),
    CWPracticeQuestion("air",     "plane",  listOf("plane",  "port",  "craft", "line"),   "✈️"),
    CWPracticeQuestion("cup",     "cake",   listOf("cake",   "board", "holder","size"),   "🧁")
)

// ── Learn ViewModel ───────────────────────────────────────────────────────────

data class CWLearnUiState(
    val selectedGroupIndex: Int     = 0,
    val activeWordFull:     String? = null
)

@HiltViewModel
class CWLearnViewModel @Inject constructor() : ViewModel() {
    var uiState by mutableStateOf(CWLearnUiState()); private set

    val selectedGroup: CWGroup get() = cwGroups[uiState.selectedGroupIndex]

    fun onGroupTap(index: Int) {
        if (index == uiState.selectedGroupIndex) return
        uiState = uiState.copy(selectedGroupIndex = index, activeWordFull = null)
    }

    fun onWordTap(word: CWWord) {
        val next = if (uiState.activeWordFull == word.full) null else word.full
        uiState = uiState.copy(activeWordFull = next)
    }
}

// ── Practice ViewModel ────────────────────────────────────────────────────────

data class CWPracticeUiState(
    val currentIndex:   Int      = 0,
    val score:          Int      = 0,
    val selectedOption: String?  = null,
    val isCorrect:      Boolean? = null,
    val isFinished:     Boolean  = false,
    val shakeWrong:     Boolean  = false
)

@HiltViewModel
class CWPracticeViewModel @Inject constructor() : ViewModel() {
    var uiState by mutableStateOf(CWPracticeUiState()); private set
    private val questions = cwPracticeQuestions.shuffled()
        .map { it.copy(options = it.options.shuffled()) }

    val totalQuestions: Int get() = questions.size
    val currentQuestion: CWPracticeQuestion? get() = questions.getOrNull(uiState.currentIndex)

    fun onOptionTap(option: String) {
        if (uiState.selectedOption != null) return
        val q = currentQuestion ?: return
        val correct = option == q.correctPart2
        uiState = uiState.copy(selectedOption = option, isCorrect = correct, shakeWrong = !correct)
        if (!correct) {
            viewModelScope.launch { delay(600); uiState = uiState.copy(shakeWrong = false) }
        }
        viewModelScope.launch {
            delay(if (correct) 1400L else 2000L)
            advance()
        }
    }

    fun restart() { uiState = CWPracticeUiState() }

    private fun advance() {
        val next     = uiState.currentIndex + 1
        val newScore = uiState.score + (if (uiState.isCorrect == true) 1 else 0)
        if (next >= questions.size) {
            uiState = uiState.copy(isFinished = true, score = newScore)
        } else {
            uiState = CWPracticeUiState(currentIndex = next, score = newScore)
        }
    }
}
