package com.example.myapplication.main.age_group.phonics.l25_consonant_le.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.utilities.AudioPhonicsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

// ── Models ────────────────────────────────────────────────────────────────────

data class CLEWord(
    val full:    String,
    val first:   String,
    val ending:  String
)

data class CLEGroup(
    val ending:      String,
    val emoji:       String,
    val rule:        String,
    val accentColor: Color,
    val shadowColor: Color,
    val words:       List<CLEWord>
)

// ── Data ──────────────────────────────────────────────────────────────────────

val cleGroups: List<CLEGroup> = listOf(
    CLEGroup(
        ending = "-ble", emoji = "🫧", rule = "sounds like /bəl/",
        accentColor = Color(0xFFC62828), shadowColor = Color(0xFFB71C1C),
        words = listOf(
            CLEWord("table",   "ta",   "ble"),
            CLEWord("able",    "a",    "ble"),
            CLEWord("bubble",  "bub",  "ble"),
            CLEWord("wobble",  "wob",  "ble"),
            CLEWord("fable",   "fa",   "ble"),
            CLEWord("stable",  "sta",  "ble"),
            CLEWord("cable",   "ca",   "ble"),
            CLEWord("gobble",  "gob",  "ble"),
            CLEWord("noble",   "no",   "ble"),
            CLEWord("dribble", "drib", "ble")
        )
    ),
    CLEGroup(
        ending = "-tle", emoji = "🐢", rule = "sounds like /təl/",
        accentColor = Color(0xFF0277BD), shadowColor = Color(0xFF01579B),
        words = listOf(
            CLEWord("little",  "lit",  "tle"),
            CLEWord("bottle",  "bot",  "tle"),
            CLEWord("turtle",  "tur",  "tle"),
            CLEWord("settle",  "set",  "tle"),
            CLEWord("rattle",  "rat",  "tle"),
            CLEWord("battle",  "bat",  "tle"),
            CLEWord("title",   "ti",   "tle"),
            CLEWord("gentle",  "gen",  "tle"),
            CLEWord("cattle",  "cat",  "tle"),
            CLEWord("brittle", "brit", "tle")
        )
    ),
    CLEGroup(
        ending = "-ple", emoji = "🍎", rule = "sounds like /pəl/",
        accentColor = Color(0xFF2E7D32), shadowColor = Color(0xFF1B5E20),
        words = listOf(
            CLEWord("apple",   "ap",   "ple"),
            CLEWord("simple",  "sim",  "ple"),
            CLEWord("purple",  "pur",  "ple"),
            CLEWord("maple",   "ma",   "ple"),
            CLEWord("ripple",  "rip",  "ple"),
            CLEWord("triple",  "tri",  "ple"),
            CLEWord("dimple",  "dim",  "ple"),
            CLEWord("sample",  "sam",  "ple"),
            CLEWord("temple",  "tem",  "ple"),
            CLEWord("couple",  "cou",  "ple")
        )
    ),
    CLEGroup(
        ending = "-dle / -gle", emoji = "🕯️", rule = "sounds like /dəl/ or /gəl/",
        accentColor = Color(0xFFE65100), shadowColor = Color(0xFFBF360C),
        words = listOf(
            CLEWord("candle",  "can",  "dle"),
            CLEWord("middle",  "mid",  "dle"),
            CLEWord("puddle",  "pud",  "dle"),
            CLEWord("handle",  "han",  "dle"),
            CLEWord("riddle",  "rid",  "dle"),
            CLEWord("jungle",  "jun",  "gle"),
            CLEWord("single",  "sin",  "gle"),
            CLEWord("eagle",   "ea",   "gle"),
            CLEWord("giggle",  "gig",  "gle"),
            CLEWord("noodle",  "noo",  "dle")
        )
    )
)

// ── Learn ViewModel ───────────────────────────────────────────────────────────

data class CLELearnUiState(
    val selectedGroupIndex: Int    = 0,
    val tappedWordFull:     String? = null,
    val showWords:          Boolean = false
)

@HiltViewModel
class ConsonantLeLearnViewModel @Inject constructor() : ViewModel() {
    var uiState by mutableStateOf(CLELearnUiState()); private set

    val selectedGroup: CLEGroup get() = cleGroups[uiState.selectedGroupIndex]

    fun onScreenAppear() {
        uiState = uiState.copy(showWords = false, tappedWordFull = null)
        viewModelScope.launch {
            delay(100)
            uiState = uiState.copy(showWords = true)
        }
    }

    fun onGroupTap(index: Int) {
        if (index == uiState.selectedGroupIndex) return
        uiState = uiState.copy(tappedWordFull = null, selectedGroupIndex = index)
    }

    fun onWordTap(word: CLEWord) {
        val newTapped = if (uiState.tappedWordFull == word.full) null else word.full
        uiState = uiState.copy(tappedWordFull = newTapped)
    }
}

// ── Practice ──────────────────────────────────────────────────────────────────

data class CLEPracticeQuestion(
    val first:   String,
    val ending:  String,
    val correct: String,
    val options: List<String>
)

val clePracticeQuestions: List<CLEPracticeQuestion> = listOf(
    // -ple
    CLEPracticeQuestion("ap",  "-ple", "apple",  listOf("apple",  "appel",  "appal",  "apele")),
    CLEPracticeQuestion("sim", "-ple", "simple", listOf("simple", "simpel", "simpal", "simpul")),
    CLEPracticeQuestion("pur", "-ple", "purple", listOf("purple", "purpel", "purpal", "purpul")),
    CLEPracticeQuestion("ma",  "-ple", "maple",  listOf("maple",  "mapel",  "mapal",  "mapul")),
    CLEPracticeQuestion("rip", "-ple", "ripple", listOf("ripple", "rippel", "rippal", "ripal")),
    // -tle
    CLEPracticeQuestion("lit", "-tle", "little", listOf("little", "littel", "littal", "lital")),
    CLEPracticeQuestion("bot", "-tle", "bottle", listOf("bottle", "bottel", "bottal", "botal")),
    CLEPracticeQuestion("tur", "-tle", "turtle", listOf("turtle", "turtel", "turtal", "turtul")),
    CLEPracticeQuestion("rat", "-tle", "rattle", listOf("rattle", "rattel", "rattal", "ratal")),
    CLEPracticeQuestion("set", "-tle", "settle", listOf("settle", "settel", "settal", "setal")),
    // -ble
    CLEPracticeQuestion("ta",  "-ble", "table",  listOf("table",  "tabel",  "tabal",  "tabul")),
    CLEPracticeQuestion("bub", "-ble", "bubble", listOf("bubble", "bubbel", "bubbal", "bubal")),
    CLEPracticeQuestion("sta", "-ble", "stable", listOf("stable", "stabel", "stabal", "stabul")),
    CLEPracticeQuestion("wob", "-ble", "wobble", listOf("wobble", "wobbel", "wobbal", "wobal")),
    CLEPracticeQuestion("no",  "-ble", "noble",  listOf("noble",  "nobel",  "nobal",  "nobul")),
    // -dle / -gle
    CLEPracticeQuestion("can", "-dle", "candle", listOf("candle", "candel", "candal", "candul")),
    CLEPracticeQuestion("mid", "-dle", "middle", listOf("middle", "middel", "middal", "midal")),
    CLEPracticeQuestion("jun", "-gle", "jungle", listOf("jungle", "jungel", "jungal", "jungul")),
    CLEPracticeQuestion("sin", "-gle", "single", listOf("single", "singel", "singal", "singul")),
    CLEPracticeQuestion("ea",  "-gle", "eagle",  listOf("eagle",  "eagel",  "eagal",  "eagul"))
)

data class CLEPracticeUiState(
    val currentIndex:   Int     = 0,
    val score:          Int     = 0,
    val selectedAnswer: String? = null,
    val isCorrect:      Boolean? = null,
    val isFinished:     Boolean = false,
    val shakeWrong:     Boolean = false
)

@HiltViewModel
class ConsonantLePracticeViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {
    var uiState by mutableStateOf(CLEPracticeUiState()); private set
    private val questions = clePracticeQuestions.shuffled().map { it.copy(options = it.options.shuffled()) }

    val totalQuestions: Int get() = questions.size
    val currentQuestion: CLEPracticeQuestion? get() = questions.getOrNull(uiState.currentIndex)

    fun onAnswerTap(answer: String) {
        if (uiState.selectedAnswer != null) return
        val q = currentQuestion ?: return
        val correct = answer == q.correct
        uiState = uiState.copy(selectedAnswer = answer, isCorrect = correct, shakeWrong = !correct)
        if (correct) {
            audioManager.playPhonicsSound("phonics_word/${q.correct}")
        } else {
            viewModelScope.launch { delay(600); uiState = uiState.copy(shakeWrong = false) }
        }
        viewModelScope.launch {
            delay(if (correct) 1400L else 2000L)
            advance()
        }
    }

    fun restart() { uiState = CLEPracticeUiState() }

    private fun advance() {
        val next = uiState.currentIndex + 1
        val newScore = uiState.score + (if (uiState.isCorrect == true) 1 else 0)
        if (next >= questions.size) { uiState = uiState.copy(isFinished = true, score = newScore) }
        else { uiState = CLEPracticeUiState(currentIndex = next, score = newScore) }
    }
}
