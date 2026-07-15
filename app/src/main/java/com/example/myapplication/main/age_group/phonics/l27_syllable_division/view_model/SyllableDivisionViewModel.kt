package com.example.myapplication.main.age_group.phonics.l27_syllable_division.view_model

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

data class SDWord(val syl1: String, val syl2: String, val emoji: String) {
    val full: String get() = syl1 + syl2
}

data class SDGroup(
    val name:        String,   // "VC/CV"
    val rule:        String,   // "Two consonants? Chop between!"
    val emoji:       String,
    val accentColor: Color,
    val shadowColor: Color,
    val words:       List<SDWord>
)

data class SDPracticeQuestion(
    val word:    String,
    val correct: String,        // "rab-bit"
    val options: List<String>,  // 4 choices, shuffled
    val emoji:   String
)

// ── Data ──────────────────────────────────────────────────────────────────────

val sdGroups: List<SDGroup> = listOf(
    SDGroup(
        name = "VC/CV", rule = "Two consonants? Chop between!", emoji = "🐰",
        accentColor = Color(0xFF2E7D32), shadowColor = Color(0xFF1B5E20),
        words = listOf(
            SDWord("rab", "bit",  "🐰"),
            SDWord("kit", "ten",  "🐱"),
            SDWord("sun", "set",  "🌇"),
            SDWord("bas", "ket",  "🧺"),
            SDWord("nap", "kin",  "🍽️"),
            SDWord("pic", "nic",  "🥪"),
            SDWord("muf", "fin",  "🧁"),
            SDWord("win", "ter",  "⛄"),
            SDWord("hap", "pen",  "✨"),
            SDWord("den", "tist", "🦷")
        )
    ),
    SDGroup(
        name = "V/CV", rule = "First vowel says its name!", emoji = "🐯",
        accentColor = Color(0xFF1565C0), shadowColor = Color(0xFF0D47A1),
        words = listOf(
            SDWord("ti",  "ger",  "🐯"),
            SDWord("pi",  "lot",  "👨‍✈️"),
            SDWord("pa",  "per",  "📄"),
            SDWord("mu",  "sic",  "🎵"),
            SDWord("spi", "der",  "🕷️"),
            SDWord("ba",  "by",   "👶"),
            SDWord("ro",  "bot",  "🤖"),
            SDWord("si",  "lent", "🤫"),
            SDWord("o",   "pen",  "🚪"),
            SDWord("e",   "ven",  "⚖️")
        )
    ),
    SDGroup(
        name = "VC/V", rule = "First vowel is short!", emoji = "🐫",
        accentColor = Color(0xFF7B1FA2), shadowColor = Color(0xFF4A148C),
        words = listOf(
            SDWord("cam",  "el", "🐫"),
            SDWord("lem",  "on", "🍋"),
            SDWord("riv",  "er", "🏞️"),
            SDWord("sev",  "en", "7️⃣"),
            SDWord("wag",  "on", "🛒"),
            SDWord("plan", "et", "🪐"),
            SDWord("mel",  "on", "🍈"),
            SDWord("cab",  "in", "🛖"),
            SDWord("rob",  "in", "🐦"),
            SDWord("drag", "on", "🐉")
        )
    ),
    SDGroup(
        name = "Schwa /ə/", rule = "Some syllables get lazy — the vowel just says \"uh\"!", emoji = "😴",
        accentColor = Color(0xFF5D4037), shadowColor = Color(0xFF3E2723),
        words = listOf(
            SDWord("a",   "bout", "💬"),
            SDWord("so",  "fa",   "🛋️"),
            SDWord("piz", "za",   "🍕"),
            SDWord("ze",  "bra",  "🦓"),
            SDWord("pan", "da",   "🐼"),
            SDWord("ex",  "tra",  "➕")
        )
    )
)

val sdPracticeQuestions: List<SDPracticeQuestion> = listOf(
    // VC/CV
    SDPracticeQuestion("rabbit", "rab-bit",  listOf("rab-bit", "ra-bbit", "rabb-it", "r-abbit"),  "🐰"),
    SDPracticeQuestion("kitten", "kit-ten",  listOf("kit-ten", "ki-tten", "kitt-en", "kitte-n"),  "🐱"),
    SDPracticeQuestion("basket", "bas-ket",  listOf("bas-ket", "ba-sket", "bask-et", "baske-t"),  "🧺"),
    SDPracticeQuestion("winter", "win-ter",  listOf("win-ter", "wi-nter", "wint-er", "winte-r"),  "⛄"),
    SDPracticeQuestion("muffin", "muf-fin",  listOf("muf-fin", "mu-ffin", "muff-in", "m-uffin"),  "🧁"),
    SDPracticeQuestion("sunset", "sun-set",  listOf("sun-set", "su-nset", "suns-et", "sunse-t"),  "🌇"),
    // V/CV
    SDPracticeQuestion("tiger",  "ti-ger",   listOf("ti-ger",  "tig-er",  "t-iger",  "tige-r"),   "🐯"),
    SDPracticeQuestion("pilot",  "pi-lot",   listOf("pi-lot",  "pil-ot",  "p-ilot",  "pilo-t"),   "👨‍✈️"),
    SDPracticeQuestion("paper",  "pa-per",   listOf("pa-per",  "pap-er",  "p-aper",  "pape-r"),   "📄"),
    SDPracticeQuestion("music",  "mu-sic",   listOf("mu-sic",  "mus-ic",  "m-usic",  "musi-c"),   "🎵"),
    SDPracticeQuestion("spider", "spi-der",  listOf("spi-der", "spid-er", "sp-ider", "spide-r"),  "🕷️"),
    SDPracticeQuestion("robot",  "ro-bot",   listOf("ro-bot",  "rob-ot",  "r-obot",  "robo-t"),   "🤖"),
    // VC/V
    SDPracticeQuestion("camel",  "cam-el",   listOf("cam-el",  "ca-mel",  "came-l",  "c-amel"),   "🐫"),
    SDPracticeQuestion("lemon",  "lem-on",   listOf("lem-on",  "le-mon",  "lemo-n",  "l-emon"),   "🍋"),
    SDPracticeQuestion("seven",  "sev-en",   listOf("sev-en",  "se-ven",  "seve-n",  "s-even"),   "7️⃣"),
    SDPracticeQuestion("planet", "plan-et",  listOf("plan-et", "pla-net", "plane-t", "pl-anet"),  "🪐"),
    SDPracticeQuestion("dragon", "drag-on",  listOf("drag-on", "dra-gon", "drago-n", "dr-agon"),  "🐉"),
    SDPracticeQuestion("river",  "riv-er",   listOf("riv-er",  "ri-ver",  "rive-r",  "r-iver"),   "🏞️"),
    // Schwa
    SDPracticeQuestion("about",  "a-bout",   listOf("a-bout",  "ab-out",  "abo-ut",  "abou-t"),   "💬"),
    SDPracticeQuestion("pizza",  "piz-za",   listOf("piz-za",  "pi-zza",  "pizz-a",  "p-izza"),   "🍕"),
    SDPracticeQuestion("zebra",  "ze-bra",   listOf("ze-bra",  "zeb-ra",  "z-ebra",  "zebr-a"),   "🦓")
)

// ── Learn ViewModel ───────────────────────────────────────────────────────────

data class SDLearnUiState(
    val selectedGroupIndex: Int     = 0,
    val activeWordFull:     String? = null
)

@HiltViewModel
class SDLearnViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {
    var uiState by mutableStateOf(SDLearnUiState()); private set

    val selectedGroup: SDGroup get() = sdGroups[uiState.selectedGroupIndex]

    fun onGroupTap(index: Int) {
        if (index == uiState.selectedGroupIndex) return
        uiState = uiState.copy(selectedGroupIndex = index, activeWordFull = null)
    }

    fun onWordTap(word: SDWord) {
        val next = if (uiState.activeWordFull == word.full) null else word.full
        uiState = uiState.copy(activeWordFull = next)
        if (next != null) {
            audioManager.playPhonicsSound("phonics_word/${word.full}")
        }
    }
}

// ── Practice ViewModel ────────────────────────────────────────────────────────

data class SDPracticeUiState(
    val currentIndex:   Int      = 0,
    val score:          Int      = 0,
    val selectedOption: String?  = null,
    val isCorrect:      Boolean? = null,
    val isFinished:     Boolean  = false,
    val shakeWrong:     Boolean  = false
)

@HiltViewModel
class SDPracticeViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager,
    private val levelProgressRepo: PhonicsLevelProgressRepository,
    private val phonicsSessions: PhonicsSessionRecorder
) : ViewModel() {
    var uiState by mutableStateOf(SDPracticeUiState()); private set

    // Parent-report session tracking
    private var sessionStartMs = System.currentTimeMillis()
    private val wrongWords = mutableListOf<String>()
    private val correctWords = mutableListOf<String>()
    private val questions = sdPracticeQuestions.shuffled()
        .map { it.copy(options = it.options.shuffled()) }

    val totalQuestions: Int get() = questions.size
    val currentQuestion: SDPracticeQuestion? get() = questions.getOrNull(uiState.currentIndex)

    fun onOptionTap(option: String) {
        if (uiState.selectedOption != null) return
        val q = currentQuestion ?: return
        val correct = option == q.correct
        uiState = uiState.copy(selectedOption = option, isCorrect = correct, shakeWrong = !correct)
        if (correct) {
            audioManager.playPhonicsSound("phonics_word/${q.word}")
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
        uiState = SDPracticeUiState()
    }

    private fun advance() {
        val next     = uiState.currentIndex + 1
        val newScore = uiState.score + (if (uiState.isCorrect == true) 1 else 0)
        if (next >= questions.size) {
            uiState = uiState.copy(isFinished = true, score = newScore)
            levelProgressRepo.recordPractice(level = PhonicsListenLevelKey.syllableDivision, score = newScore, total = questions.size)
            phonicsSessions.recordPractice(PhonicsListenLevelKey.syllableDivision, newScore, questions.size, ((System.currentTimeMillis() - sessionStartMs) / 1000).toInt(), wrongWords.toList(), correctWords.toList())
        } else {
            uiState = SDPracticeUiState(currentIndex = next, score = newScore)
        }
    }
}
