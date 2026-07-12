package com.example.myapplication.main.age_group.phonics.l23_suffixes.view_model

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

// ── Models ────────────────────────────────────────────────────────────────────

enum class SuffixRule { JUST_ADD, DROP_Y }

data class SuffixWord(val base: String, val full: String, val rule: SuffixRule)

data class SuffixGroup(
    val suffix: String,
    val rawSuffix: String,
    val suffixLen: Int,
    val meaning: String,
    val emoji: String,
    val accentColor: Color,
    val shadowColor: Color,
    val words: List<SuffixWord>
)

// ── Data ──────────────────────────────────────────────────────────────────────

val suffixGroups: List<SuffixGroup> = listOf(
    SuffixGroup(
        suffix = "-ful", rawSuffix = "ful", suffixLen = 3,
        meaning = "full of", emoji = "💛",
        accentColor = Color(0xFF00695C), shadowColor = Color(0xFF004D40),
        words = listOf(
            SuffixWord("help",   "helpful",    SuffixRule.JUST_ADD),
            SuffixWord("care",   "careful",    SuffixRule.JUST_ADD),
            SuffixWord("joy",    "joyful",     SuffixRule.JUST_ADD),
            SuffixWord("play",   "playful",    SuffixRule.JUST_ADD),
            SuffixWord("thank",  "thankful",   SuffixRule.JUST_ADD),
            SuffixWord("hope",   "hopeful",    SuffixRule.JUST_ADD),
            SuffixWord("peace",  "peaceful",   SuffixRule.JUST_ADD),
            SuffixWord("power",  "powerful",   SuffixRule.JUST_ADD),
            SuffixWord("color",  "colorful",   SuffixRule.JUST_ADD),
            SuffixWord("wonder", "wonderful",  SuffixRule.JUST_ADD)
        )
    ),
    SuffixGroup(
        suffix = "-less", rawSuffix = "less", suffixLen = 4,
        meaning = "without", emoji = "🕳️",
        accentColor = Color(0xFF283593), shadowColor = Color(0xFF1A237E),
        words = listOf(
            SuffixWord("care",    "careless",    SuffixRule.JUST_ADD),
            SuffixWord("hope",    "hopeless",    SuffixRule.JUST_ADD),
            SuffixWord("fear",    "fearless",    SuffixRule.JUST_ADD),
            SuffixWord("help",    "helpless",    SuffixRule.JUST_ADD),
            SuffixWord("use",     "useless",     SuffixRule.JUST_ADD),
            SuffixWord("harm",    "harmless",    SuffixRule.JUST_ADD),
            SuffixWord("end",     "endless",     SuffixRule.JUST_ADD),
            SuffixWord("thought", "thoughtless", SuffixRule.JUST_ADD),
            SuffixWord("worth",   "worthless",   SuffixRule.JUST_ADD),
            SuffixWord("speech",  "speechless",  SuffixRule.JUST_ADD)
        )
    ),
    SuffixGroup(
        suffix = "-ness", rawSuffix = "ness", suffixLen = 4,
        meaning = "quality of", emoji = "✨",
        accentColor = Color(0xFFE65100), shadowColor = Color(0xFFBF360C),
        words = listOf(
            SuffixWord("kind",  "kindness",  SuffixRule.JUST_ADD),
            SuffixWord("dark",  "darkness",  SuffixRule.JUST_ADD),
            SuffixWord("sad",   "sadness",   SuffixRule.JUST_ADD),
            SuffixWord("good",  "goodness",  SuffixRule.JUST_ADD),
            SuffixWord("sick",  "sickness",  SuffixRule.JUST_ADD),
            SuffixWord("soft",  "softness",  SuffixRule.JUST_ADD),
            SuffixWord("loud",  "loudness",  SuffixRule.JUST_ADD),
            SuffixWord("weak",  "weakness",  SuffixRule.JUST_ADD),
            SuffixWord("happy", "happiness", SuffixRule.DROP_Y),
            SuffixWord("lazy",  "laziness",  SuffixRule.DROP_Y),
            SuffixWord("heavy", "heaviness", SuffixRule.DROP_Y),
            SuffixWord("ready", "readiness", SuffixRule.DROP_Y),
            SuffixWord("silly", "silliness", SuffixRule.DROP_Y),
            SuffixWord("easy",  "easiness",  SuffixRule.DROP_Y)
        )
    ),
    SuffixGroup(
        suffix = "-tion", rawSuffix = "tion", suffixLen = 4,
        meaning = "act of", emoji = "⚡",
        accentColor = Color(0xFF6A1B9A), shadowColor = Color(0xFF4A148C),
        words = listOf(
            SuffixWord("act",     "action",     SuffixRule.JUST_ADD),
            SuffixWord("direct",  "direction",  SuffixRule.JUST_ADD),
            SuffixWord("connect", "connection", SuffixRule.JUST_ADD),
            SuffixWord("protect", "protection", SuffixRule.JUST_ADD),
            SuffixWord("collect", "collection", SuffixRule.JUST_ADD),
            SuffixWord("inject",  "injection",  SuffixRule.JUST_ADD),
            SuffixWord("elect",   "election",   SuffixRule.JUST_ADD),
            SuffixWord("react",   "reaction",   SuffixRule.JUST_ADD),
            SuffixWord("select",  "selection",  SuffixRule.JUST_ADD),
            SuffixWord("correct", "correction", SuffixRule.JUST_ADD)
        )
    ),
    SuffixGroup(
        suffix = "-sion", rawSuffix = "sion", suffixLen = 4,
        meaning = "state / result", emoji = "🌀",
        accentColor = Color(0xFF1565C0), shadowColor = Color(0xFF0D47A1),
        words = listOf(
            SuffixWord("vi",      "vision",     SuffixRule.JUST_ADD),
            SuffixWord("ten",     "tension",    SuffixRule.JUST_ADD),
            SuffixWord("mis",     "mission",    SuffixRule.JUST_ADD),
            SuffixWord("exten",   "extension",  SuffixRule.JUST_ADD),
            SuffixWord("confu",   "confusion",  SuffixRule.JUST_ADD),
            SuffixWord("explo",   "explosion",  SuffixRule.JUST_ADD),
            SuffixWord("discus",  "discussion", SuffixRule.JUST_ADD),
            SuffixWord("inva",    "invasion",   SuffixRule.JUST_ADD),
            SuffixWord("pas",     "passion",    SuffixRule.JUST_ADD),
            SuffixWord("permis",  "permission", SuffixRule.JUST_ADD)
        )
    ),
    SuffixGroup(
        suffix = "-ly", rawSuffix = "ly", suffixLen = 2,
        meaning = "in that way", emoji = "💨",
        accentColor = Color(0xFF00838F), shadowColor = Color(0xFF006064),
        words = listOf(
            SuffixWord("slow",  "slowly",  SuffixRule.JUST_ADD),
            SuffixWord("quick", "quickly", SuffixRule.JUST_ADD),
            SuffixWord("loud",  "loudly",  SuffixRule.JUST_ADD),
            SuffixWord("soft",  "softly",  SuffixRule.JUST_ADD),
            SuffixWord("kind",  "kindly",  SuffixRule.JUST_ADD),
            SuffixWord("safe",  "safely",  SuffixRule.JUST_ADD),
            SuffixWord("happy", "happily", SuffixRule.DROP_Y)
        )
    ),
    SuffixGroup(
        suffix = "-er", rawSuffix = "er", suffixLen = 2,
        meaning = "person who does it", emoji = "🧑‍🏫",
        accentColor = Color(0xFF5E35B1), shadowColor = Color(0xFF4527A0),
        words = listOf(
            SuffixWord("teach", "teacher", SuffixRule.JUST_ADD),
            SuffixWord("sing",  "singer",  SuffixRule.JUST_ADD),
            SuffixWord("play",  "player",  SuffixRule.JUST_ADD),
            SuffixWord("paint", "painter", SuffixRule.JUST_ADD),
            SuffixWord("farm",  "farmer",  SuffixRule.JUST_ADD),
            SuffixWord("help",  "helper",  SuffixRule.JUST_ADD)
        )
    ),
    SuffixGroup(
        suffix = "-y", rawSuffix = "y", suffixLen = 1,
        meaning = "full of / like", emoji = "☔",
        accentColor = Color(0xFFEF6C00), shadowColor = Color(0xFFE65100),
        words = listOf(
            SuffixWord("rain",  "rainy",  SuffixRule.JUST_ADD),
            SuffixWord("wind",  "windy",  SuffixRule.JUST_ADD),
            SuffixWord("cloud", "cloudy", SuffixRule.JUST_ADD),
            SuffixWord("snow",  "snowy",  SuffixRule.JUST_ADD),
            SuffixWord("luck",  "lucky",  SuffixRule.JUST_ADD),
            SuffixWord("sleep", "sleepy", SuffixRule.JUST_ADD)
        )
    )
)

// ── Learn ViewModel ───────────────────────────────────────────────────────────

data class SuffixesLearnUiState(
    val selectedGroupIndex: Int = 0,
    val highlightedWord: String? = null,
    val showWords: Boolean = false
)

@HiltViewModel
class SuffixesLearnViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {
    var uiState by mutableStateOf(SuffixesLearnUiState()); private set

    val selectedGroup: SuffixGroup get() = suffixGroups[uiState.selectedGroupIndex]

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

    fun onWordTap(word: SuffixWord) {
        uiState = uiState.copy(highlightedWord = word.full)
        audioManager.playPhonicsSound("phonics_word/${word.full}")
    }
}

// ── Practice ──────────────────────────────────────────────────────────────────

data class SuffixesPracticeQuestion(
    val base: String,
    val suffix: String,
    val correct: String,
    val options: List<String>
)

val suffixesPracticeQuestions: List<SuffixesPracticeQuestion> = listOf(
    // -ful
    SuffixesPracticeQuestion("help",  "-ful", "helpful",   listOf("helpful",   "helpfull",  "helpfful",  "helppful")),
    SuffixesPracticeQuestion("care",  "-ful", "careful",   listOf("careful",   "carefull",  "carful",    "carefful")),
    SuffixesPracticeQuestion("peace", "-ful", "peaceful",  listOf("peaceful",  "peacefull", "peacful",   "peacceful")),
    SuffixesPracticeQuestion("power", "-ful", "powerful",  listOf("powerful",  "powerfull", "powerrful", "powwerful")),
    // -less
    SuffixesPracticeQuestion("care",  "-less", "careless",  listOf("careless",  "carelless", "carles",    "caareless")),
    SuffixesPracticeQuestion("hope",  "-less", "hopeless",  listOf("hopeless",  "hopelless", "hoples",    "hopeles")),
    SuffixesPracticeQuestion("fear",  "-less", "fearless",  listOf("fearless",  "fearlless", "fearles",   "feerless")),
    SuffixesPracticeQuestion("harm",  "-less", "harmless",  listOf("harmless",  "harmlless", "harmles",   "harmmless")),
    // -ness JUST_ADD
    SuffixesPracticeQuestion("kind",  "-ness", "kindness",  listOf("kindness",  "kindnness", "kindnes",   "kingness")),
    SuffixesPracticeQuestion("dark",  "-ness", "darkness",  listOf("darkness",  "darknness", "darknes",   "darckness")),
    SuffixesPracticeQuestion("sad",   "-ness", "sadness",   listOf("sadness",   "sadnness",  "sadnes",    "saddness")),
    SuffixesPracticeQuestion("weak",  "-ness", "weakness",  listOf("weakness",  "weaknness", "weaknes",   "weackness")),
    // -ness DROP_Y
    SuffixesPracticeQuestion("happy", "-ness", "happiness", listOf("happiness", "happyness", "hapiness",  "happines")),
    SuffixesPracticeQuestion("lazy",  "-ness", "laziness",  listOf("laziness",  "lazyness",  "lazines",   "lazzyness")),
    SuffixesPracticeQuestion("heavy", "-ness", "heaviness", listOf("heaviness", "heavyness", "heavines",  "heavviness")),
    SuffixesPracticeQuestion("ready", "-ness", "readiness", listOf("readiness", "readyness", "readines",  "readdiness")),
    // -tion
    SuffixesPracticeQuestion("act",     "-tion", "action",     listOf("action",     "acction",    "actoin",    "actioon")),
    SuffixesPracticeQuestion("direct",  "-tion", "direction",  listOf("direction",  "directtion", "diriction", "dirrection")),
    SuffixesPracticeQuestion("connect", "-tion", "connection", listOf("connection", "connecttion","conection", "connexion")),
    SuffixesPracticeQuestion("collect", "-tion", "collection", listOf("collection", "collecttion","colection", "collecktion")),
    // -sion
    SuffixesPracticeQuestion("ten",    "-sion", "tension",    listOf("tension",    "tention",    "tensian",   "tensoin")),
    SuffixesPracticeQuestion("mis",    "-sion", "mission",    listOf("mission",    "mision",     "mistion",   "mishion")),
    SuffixesPracticeQuestion("exten",  "-sion", "extension",  listOf("extension",  "extention",  "extensian", "extencion")),
    SuffixesPracticeQuestion("confu",  "-sion", "confusion",  listOf("confusion",  "confution",  "confusian", "confushion")),
    SuffixesPracticeQuestion("explo",  "-sion", "explosion",  listOf("explosion",  "exploshion", "explotion", "explosian")),
    SuffixesPracticeQuestion("inva",   "-sion", "invasion",   listOf("invasion",   "invashion",  "invation",  "invasian")),
    // -ly
    SuffixesPracticeQuestion("slow",  "-ly", "slowly",  listOf("slowly",  "slowley",  "slowlly",  "slowli")),
    SuffixesPracticeQuestion("quick", "-ly", "quickly", listOf("quickly", "quickley", "quicklly", "quickli")),
    // -er (person who)
    SuffixesPracticeQuestion("teach", "-er", "teacher", listOf("teacher", "teachar",  "teachor",  "teacherr")),
    SuffixesPracticeQuestion("sing",  "-er", "singer",  listOf("singer",  "singar",   "singor",   "singger")),
    // -y
    SuffixesPracticeQuestion("rain",  "-y",  "rainy",   listOf("rainy",   "rainey",   "rainny",   "raini")),
    SuffixesPracticeQuestion("luck",  "-y",  "lucky",   listOf("lucky",   "luckey",   "luccy",    "lucki"))
)

data class SuffixesPracticeUiState(
    val currentIndex: Int = 0,
    val score: Int = 0,
    val selectedAnswer: String? = null,
    val isCorrect: Boolean? = null,
    val isFinished: Boolean = false,
    val shakeWrong: Boolean = false
)

@HiltViewModel
class SuffixesPracticeViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager,
    private val levelProgressRepo: PhonicsLevelProgressRepository,
    private val phonicsSessions: PhonicsSessionRecorder
) : ViewModel() {
    var uiState by mutableStateOf(SuffixesPracticeUiState()); private set

    // Parent-report session tracking
    private var sessionStartMs = System.currentTimeMillis()
    private val wrongWords = mutableListOf<String>()
    private val correctWords = mutableListOf<String>()
    private val questions = suffixesPracticeQuestions.shuffled().map { it.copy(options = it.options.shuffled()) }

    val totalQuestions: Int get() = questions.size
    val currentQuestion: SuffixesPracticeQuestion? get() = questions.getOrNull(uiState.currentIndex)

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

    fun restart() {
        sessionStartMs = System.currentTimeMillis()
        wrongWords.clear()
        correctWords.clear()
        uiState = SuffixesPracticeUiState()
    }

    private fun advance() {
        val next = uiState.currentIndex + 1
        val newScore = uiState.score + (if (uiState.isCorrect == true) 1 else 0)
        if (next >= questions.size) {
            uiState = uiState.copy(isFinished = true, score = newScore)
            levelProgressRepo.recordPractice(level = PhonicsListenLevelKey.suffixes, score = newScore, total = questions.size)
            phonicsSessions.recordPractice(PhonicsListenLevelKey.suffixes, newScore, questions.size, ((System.currentTimeMillis() - sessionStartMs) / 1000).toInt(), wrongWords.toList(), correctWords.toList())
        }
        else { uiState = SuffixesPracticeUiState(currentIndex = next, score = newScore) }
    }
}
