package com.example.myapplication.main.age_group.phonics.l22_prefixes.view_model

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

// ── Models ────────────────────────────────────────────────────────────────────

data class PrefixWord(val base: String, val full: String)

data class PrefixGroup(
    val prefix: String,
    val displayPrefix: String,
    val meaning: String,
    val emoji: String,
    val accentColor: Color,
    val shadowColor: Color,
    val words: List<PrefixWord>
)

// ── Data ──────────────────────────────────────────────────────────────────────

val prefixGroups: List<PrefixGroup> = listOf(
    PrefixGroup(
        prefix = "un", displayPrefix = "un-", meaning = "NOT", emoji = "🚫",
        accentColor = Color(0xFF1565C0), shadowColor = Color(0xFF0D47A1),
        words = listOf(
            PrefixWord("happy",  "unhappy"),
            PrefixWord("do",     "undo"),
            PrefixWord("lock",   "unlock"),
            PrefixWord("fair",   "unfair"),
            PrefixWord("safe",   "unsafe"),
            PrefixWord("pack",   "unpack"),
            PrefixWord("clear",  "unclear"),
            PrefixWord("kind",   "unkind"),
            PrefixWord("cover",  "uncover"),
            PrefixWord("wrap",   "unwrap")
        )
    ),
    PrefixGroup(
        prefix = "re", displayPrefix = "re-", meaning = "AGAIN", emoji = "🔄",
        accentColor = Color(0xFF2E7D32), shadowColor = Color(0xFF1B5E20),
        words = listOf(
            PrefixWord("do",    "redo"),
            PrefixWord("play",  "replay"),
            PrefixWord("write", "rewrite"),
            PrefixWord("build", "rebuild"),
            PrefixWord("use",   "reuse"),
            PrefixWord("fill",  "refill"),
            PrefixWord("heat",  "reheat"),
            PrefixWord("read",  "reread"),
            PrefixWord("open",  "reopen"),
            PrefixWord("paint", "repaint")
        )
    ),
    PrefixGroup(
        prefix = "pre", displayPrefix = "pre-", meaning = "BEFORE", emoji = "⏮️",
        accentColor = Color(0xFFE65100), shadowColor = Color(0xFFBF360C),
        words = listOf(
            PrefixWord("view",   "preview"),
            PrefixWord("pay",    "prepay"),
            PrefixWord("school", "preschool"),
            PrefixWord("heat",   "preheat"),
            PrefixWord("test",   "pretest"),
            PrefixWord("mix",    "premix"),
            PrefixWord("game",   "pregame"),
            PrefixWord("cook",   "precook"),
            PrefixWord("plan",   "preplan"),
            PrefixWord("wash",   "prewash")
        )
    ),
    PrefixGroup(
        prefix = "dis", displayPrefix = "dis-", meaning = "OPPOSITE", emoji = "↔️",
        accentColor = Color(0xFF6A1B9A), shadowColor = Color(0xFF4A148C),
        words = listOf(
            PrefixWord("agree",    "disagree"),
            PrefixWord("like",     "dislike"),
            PrefixWord("honest",   "dishonest"),
            PrefixWord("appear",   "disappear"),
            PrefixWord("connect",  "disconnect"),
            PrefixWord("obey",     "disobey"),
            PrefixWord("order",    "disorder"),
            PrefixWord("trust",    "distrust"),
            PrefixWord("comfort",  "discomfort"),
            PrefixWord("cover",    "discover")
        )
    ),
    PrefixGroup(
        prefix = "mis", displayPrefix = "mis-", meaning = "WRONGLY", emoji = "❌",
        accentColor = Color(0xFFC62828), shadowColor = Color(0xFFB71C1C),
        words = listOf(
            PrefixWord("take",   "mistake"),
            PrefixWord("place",  "misplace"),
            PrefixWord("read",   "misread"),
            PrefixWord("lead",   "mislead"),
            PrefixWord("use",    "misuse"),
            PrefixWord("count",  "miscount"),
            PrefixWord("spell",  "misspell"),
            PrefixWord("judge",  "misjudge"),
            PrefixWord("hear",   "mishear"),
            PrefixWord("match",  "mismatch")
        )
    ),
    PrefixGroup(
        prefix = "over", displayPrefix = "over-", meaning = "TOO MUCH", emoji = "🌊",
        accentColor = Color(0xFF0277BD), shadowColor = Color(0xFF01579B),
        words = listOf(
            PrefixWord("eat",   "overeat"),
            PrefixWord("sleep", "oversleep"),
            PrefixWord("flow",  "overflow"),
            PrefixWord("load",  "overload"),
            PrefixWord("heat",  "overheat")
        )
    ),
    PrefixGroup(
        prefix = "under", displayPrefix = "under-", meaning = "BELOW", emoji = "⬇️",
        accentColor = Color(0xFF00695C), shadowColor = Color(0xFF004D40),
        words = listOf(
            PrefixWord("water",  "underwater"),
            PrefixWord("ground", "underground"),
            PrefixWord("line",   "underline"),
            PrefixWord("cook",   "undercook")
        )
    ),
    PrefixGroup(
        prefix = "non", displayPrefix = "non-", meaning = "NOT / NONE", emoji = "🚱",
        accentColor = Color(0xFF6D4C41), shadowColor = Color(0xFF4E342E),
        words = listOf(
            PrefixWord("stop",  "nonstop"),
            PrefixWord("sense", "nonsense"),
            PrefixWord("fat",   "nonfat"),
            PrefixWord("stick", "nonstick")
        )
    )
)

// ── Learn ViewModel ───────────────────────────────────────────────────────────

data class PrefixesLearnUiState(
    val selectedGroupIndex: Int = 0,
    val highlightedWord: String? = null,
    val showWords: Boolean = false
)

@HiltViewModel
class PrefixesLearnViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {
    var uiState by mutableStateOf(PrefixesLearnUiState()); private set

    val selectedGroup: PrefixGroup get() = prefixGroups[uiState.selectedGroupIndex]

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

    fun onWordTap(word: PrefixWord) {
        uiState = uiState.copy(highlightedWord = word.full)
        audioManager.playPhonicsSound("phonics_word/${word.full}")
    }
}

// ── Practice ──────────────────────────────────────────────────────────────────

data class PrefixesPracticeQuestion(
    val prefix: String,
    val base: String,
    val correct: String,
    val options: List<String>
)

val prefixesPracticeQuestions: List<PrefixesPracticeQuestion> = listOf(
    // un-
    PrefixesPracticeQuestion("un", "happy", "unhappy",    listOf("unhappy",    "unhapppy",  "un-happy",  "nothappy")),
    PrefixesPracticeQuestion("un", "lock",  "unlock",     listOf("unlock",     "unlocck",   "un-lock",   "notlock")),
    PrefixesPracticeQuestion("un", "fair",  "unfair",     listOf("unfair",     "unfairr",   "un-fair",   "notfair")),
    PrefixesPracticeQuestion("un", "wrap",  "unwrap",     listOf("unwrap",     "unwrapp",   "un-wrap",   "notwrap")),
    // re-
    PrefixesPracticeQuestion("re", "play",  "replay",     listOf("replay",     "repllay",   "re-play",   "playagain")),
    PrefixesPracticeQuestion("re", "write", "rewrite",    listOf("rewrite",    "rewrrite",  "re-write",  "writeagain")),
    PrefixesPracticeQuestion("re", "fill",  "refill",     listOf("refill",     "refilll",   "re-fill",   "fillagain")),
    PrefixesPracticeQuestion("re", "open",  "reopen",     listOf("reopen",     "reoppen",   "re-open",   "openagain")),
    // pre-
    PrefixesPracticeQuestion("pre", "view", "preview",    listOf("preview",    "prevview",  "pre-view",  "beforeview")),
    PrefixesPracticeQuestion("pre", "heat", "preheat",    listOf("preheat",    "preeheat",  "pre-heat",  "beforeheat")),
    PrefixesPracticeQuestion("pre", "test", "pretest",    listOf("pretest",    "prretest",  "pre-test",  "beforetest")),
    PrefixesPracticeQuestion("pre", "wash", "prewash",    listOf("prewash",    "preewash",  "pre-wash",  "beforewash")),
    // dis-
    PrefixesPracticeQuestion("dis", "agree",   "disagree",   listOf("disagree",   "disaggree", "dis-agree",   "notagree")),
    PrefixesPracticeQuestion("dis", "appear",  "disappear",  listOf("disappear",  "disapear",  "dis-appear",  "notappear")),
    PrefixesPracticeQuestion("dis", "connect", "disconnect", listOf("disconnect", "disconect", "dis-connect", "notconnect")),
    PrefixesPracticeQuestion("dis", "trust",   "distrust",   listOf("distrust",   "distrrust", "dis-trust",   "nottrust")),
    // mis-
    PrefixesPracticeQuestion("mis", "spell", "misspell",   listOf("misspell",   "mispell",   "mis-spell",   "wrongspell")),
    PrefixesPracticeQuestion("mis", "read",  "misread",    listOf("misread",    "missread",  "mis-read",    "wrongread")),
    PrefixesPracticeQuestion("mis", "place", "misplace",   listOf("misplace",   "missplace", "mis-place",   "wrongplace")),
    PrefixesPracticeQuestion("mis", "match", "mismatch",   listOf("mismatch",   "missmatch", "mis-match",   "wrongmatch")),
    // over- / under- / non-
    PrefixesPracticeQuestion("over",  "eat",   "overeat",    listOf("overeat",    "overreat",   "over-eat",    "muchseat")),
    PrefixesPracticeQuestion("under", "water", "underwater", listOf("underwater", "underrwater","under-water", "belowwater")),
    PrefixesPracticeQuestion("non",   "stop",  "nonstop",    listOf("nonstop",    "nonnstop",   "non-stop",    "notstop"))
)

data class PrefixesPracticeUiState(
    val currentIndex: Int = 0,
    val score: Int = 0,
    val selectedAnswer: String? = null,
    val isCorrect: Boolean? = null,
    val isFinished: Boolean = false,
    val shakeWrong: Boolean = false
)

@HiltViewModel
class PrefixesPracticeViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager,
    private val levelProgressRepo: PhonicsLevelProgressRepository,
    private val phonicsSessions: PhonicsSessionRecorder
) : ViewModel() {
    var uiState by mutableStateOf(PrefixesPracticeUiState()); private set

    // Parent-report session tracking
    private var sessionStartMs = System.currentTimeMillis()
    private val wrongWords = mutableListOf<String>()
    private val correctWords = mutableListOf<String>()
    private val questions = prefixesPracticeQuestions.shuffled().map { it.copy(options = it.options.shuffled()) }

    val totalQuestions: Int get() = questions.size
    val currentQuestion: PrefixesPracticeQuestion? get() = questions.getOrNull(uiState.currentIndex)

    fun onAnswerTap(answer: String) {
        if (uiState.selectedAnswer != null) return
        val q = currentQuestion ?: return
        val correct = answer == q.correct
        uiState = uiState.copy(selectedAnswer = answer, isCorrect = correct, shakeWrong = !correct)
        if (correct) {
            audioManager.playPhonicsSound("phonics_word/${q.correct}")
        } else {
            AudioPlayerManager.playSoundWrongAnswer()
            viewModelScope.launch { delay(600); uiState = uiState.copy(shakeWrong = false) }
        }
        viewModelScope.launch {
            delay(if (correct) 1400L else 2000L)
            advance()
        }
    }

    fun restart() {
        sessionStartMs = System.currentTimeMillis()
        wrongWords.clear()
        correctWords.clear()
        uiState = PrefixesPracticeUiState()
    }

    private fun advance() {
        val next = uiState.currentIndex + 1
        val newScore = uiState.score + (if (uiState.isCorrect == true) 1 else 0)
        if (next >= questions.size) {
            uiState = uiState.copy(isFinished = true, score = newScore)
            levelProgressRepo.recordPractice(level = PhonicsListenLevelKey.prefixes, score = newScore, total = questions.size)
            phonicsSessions.recordPractice(PhonicsListenLevelKey.prefixes, newScore, questions.size, ((System.currentTimeMillis() - sessionStartMs) / 1000).toInt(), wrongWords.toList(), correctWords.toList())
        }
        else { uiState = PrefixesPracticeUiState(currentIndex = next, score = newScore) }
    }
}
