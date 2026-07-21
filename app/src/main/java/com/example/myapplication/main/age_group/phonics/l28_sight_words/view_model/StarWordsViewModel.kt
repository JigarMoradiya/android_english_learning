package com.example.myapplication.main.age_group.phonics.l28_sight_words.view_model

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

data class SWPart(val text: String, val isHeart: Boolean)

// Heart-word breakdown: ✓ parts are decodable, ❤️ parts must be learned by
// heart. All-✓ words are fully decodable ("you can sound this one out!").
data class SWWord(val word: String, val parts: List<SWPart>)

/** Builder shorthand: regular (decodable) part. */
private fun p(text: String) = SWPart(text, isHeart = false)
/** Builder shorthand: heart (tricky) part. */
private fun h(text: String) = SWPart(text, isHeart = true)

data class SWSet(
    val name:        String,
    val emoji:       String,
    val accentColor: Color,
    val shadowColor: Color,
    val words:       List<SWWord>
)

data class SWPracticeQuestion(
    val correct: String,
    val options: List<String>   // 4 choices, shuffled
)

// ── Data ──────────────────────────────────────────────────────────────────────

val swSets: List<SWSet> = listOf(
    SWSet(
        name = "Set 1", emoji = "⭐",
        accentColor = Color(0xFFD81B60), shadowColor = Color(0xFF880E4F),
        words = listOf(
            SWWord("the",     listOf(p("th"), h("e"))),
            SWWord("was",     listOf(p("w"), h("a"), h("s"))),
            SWWord("said",    listOf(p("s"), h("ai"), p("d"))),
            SWWord("have",    listOf(p("h"), p("a"), p("v"), h("e"))),
            SWWord("they",    listOf(p("th"), h("ey"))),
            SWWord("once",    listOf(h("o"), p("n"), p("ce"))),
            SWWord("who",     listOf(h("wh"), h("o"))),
            SWWord("your",    listOf(p("y"), h("our"))),
            SWWord("because", listOf(p("bec"), h("au"), p("se"))),
            SWWord("friend",  listOf(p("fr"), h("ie"), p("nd")))
        )
    ),
    SWSet(
        name = "Set 2", emoji = "🌟",
        accentColor = Color(0xFF6A1B9A), shadowColor = Color(0xFF4A148C),
        words = listOf(
            SWWord("what",   listOf(p("wh"), h("a"), p("t"))),
            SWWord("where",  listOf(p("wh"), h("ere"))),
            SWWord("come",   listOf(p("c"), h("o"), p("me"))),
            SWWord("some",   listOf(p("s"), h("o"), p("me"))),
            SWWord("done",   listOf(p("d"), h("o"), p("ne"))),
            SWWord("one",    listOf(h("o"), p("ne"))),
            SWWord("two",    listOf(p("t"), h("wo"))),
            SWWord("of",     listOf(h("of"))),
            SWWord("could",  listOf(p("c"), h("ould"))),
            SWWord("people", listOf(p("p"), h("eo"), p("ple")))
        )
    ),
    SWSet(
        name = "Set 3", emoji = "✨",
        accentColor = Color(0xFF00838F), shadowColor = Color(0xFF006064),
        words = listOf(
            SWWord("there", listOf(p("th"), h("ere"))),
            SWWord("here",  listOf(p("h"), h("ere"))),
            SWWord("want",  listOf(p("w"), h("a"), p("nt"))),
            SWWord("very",  listOf(p("v"), p("e"), p("r"), p("y"))),
            SWWord("again", listOf(h("a"), p("g"), h("ai"), p("n"))),
            SWWord("any",   listOf(h("a"), p("ny"))),
            SWWord("been",  listOf(p("b"), h("ee"), p("n"))),
            SWWord("does",  listOf(p("d"), h("oe"), h("s"))),
            SWWord("after", listOf(p("af"), p("ter"))),
            SWWord("every", listOf(p("ev"), p("ery")))
        )
    )
)

val swPracticeQuestions: List<SWPracticeQuestion> = listOf(
    // Set 1
    SWPracticeQuestion("the",     listOf("the",     "tha",     "teh",     "hte")),
    SWPracticeQuestion("was",     listOf("was",     "wos",     "waz",     "wass")),
    SWPracticeQuestion("said",    listOf("said",    "sed",     "sayd",    "saide")),
    SWPracticeQuestion("have",    listOf("have",    "hav",     "haev",    "havv")),
    SWPracticeQuestion("they",    listOf("they",    "thay",    "tehy",    "thei")),
    SWPracticeQuestion("once",    listOf("once",    "onse",    "wunce",   "onec")),
    SWPracticeQuestion("who",     listOf("who",     "hoo",     "hwo",     "whu")),
    SWPracticeQuestion("because", listOf("because", "becuase", "becos",   "becuz")),
    SWPracticeQuestion("friend",  listOf("friend",  "freind",  "frend",   "firend")),
    // Set 2
    SWPracticeQuestion("what",    listOf("what",    "wat",     "whot",    "waht")),
    SWPracticeQuestion("where",   listOf("where",   "wher",    "whare",   "wehre")),
    SWPracticeQuestion("done",    listOf("done",    "dun",     "donne",   "doen")),
    SWPracticeQuestion("one",     listOf("one",     "wun",     "onne",    "oen")),
    SWPracticeQuestion("two",     listOf("two",     "twoo",    "tuo",     "toow")),
    SWPracticeQuestion("could",   listOf("could",   "coud",    "culd",    "cuold")),
    SWPracticeQuestion("people",  listOf("people",  "peple",   "peopel",  "pepole")),
    // Set 3
    SWPracticeQuestion("there",   listOf("there",   "thair",   "ther",    "therre")),
    SWPracticeQuestion("want",    listOf("want",    "wamt",    "wannt",   "waant")),
    SWPracticeQuestion("very",    listOf("very",    "verry",   "veri",    "verey")),
    SWPracticeQuestion("again",   listOf("again",   "agen",    "agian",   "agane")),
    SWPracticeQuestion("been",    listOf("been",    "beem",    "bene",    "beeen")),
    SWPracticeQuestion("does",    listOf("does",    "doess",   "doez",    "dows"))
)

// ── Learn ViewModel ───────────────────────────────────────────────────────────

data class SWLearnUiState(
    val selectedSetIndex: Int     = 0,
    val activeWord:       String? = null
)

@HiltViewModel
class SWLearnViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {
    var uiState by mutableStateOf(SWLearnUiState()); private set

    val selectedSet: SWSet get() = swSets[uiState.selectedSetIndex]

    fun onSetTap(index: Int) {
        if (index == uiState.selectedSetIndex) return
        uiState = uiState.copy(selectedSetIndex = index, activeWord = null)
    }

    fun onWordTap(word: SWWord) {
        val next = if (uiState.activeWord == word.word) null else word.word
        uiState = uiState.copy(activeWord = next)
        if (next != null) {
            audioManager.playPhonicsSound("phonics_word/${word.word}")
        }
    }
}

// ── Practice ViewModel ────────────────────────────────────────────────────────

data class SWPracticeUiState(
    val currentIndex:   Int      = 0,
    val score:          Int      = 0,
    val selectedOption: String?  = null,
    val isCorrect:      Boolean? = null,
    val isFinished:     Boolean  = false,
    val shakeWrong:     Boolean  = false
)

@HiltViewModel
class SWPracticeViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager,
    private val levelProgressRepo: PhonicsLevelProgressRepository,
    private val phonicsSessions: PhonicsSessionRecorder
) : ViewModel() {
    var uiState by mutableStateOf(SWPracticeUiState()); private set

    // Parent-report session tracking
    private var sessionStartMs = System.currentTimeMillis()
    private val wrongWords = mutableListOf<String>()
    private val correctWords = mutableListOf<String>()
    private val questions = swPracticeQuestions.shuffled().take(10)
        .map { it.copy(options = it.options.shuffled()) }

    val totalQuestions: Int get() = questions.size
    val currentQuestion: SWPracticeQuestion? get() = questions.getOrNull(uiState.currentIndex)

    fun onOptionTap(option: String) {
        if (uiState.selectedOption != null) return
        val q = currentQuestion ?: return
        val correct = option == q.correct
        uiState = uiState.copy(selectedOption = option, isCorrect = correct, shakeWrong = !correct)
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
        uiState = SWPracticeUiState()
    }

    private fun advance() {
        val next     = uiState.currentIndex + 1
        val newScore = uiState.score + (if (uiState.isCorrect == true) 1 else 0)
        if (next >= questions.size) {
            uiState = uiState.copy(isFinished = true, score = newScore)
            levelProgressRepo.recordPractice(level = PhonicsListenLevelKey.sightWords, score = newScore, total = questions.size)
            phonicsSessions.recordPractice(PhonicsListenLevelKey.sightWords, newScore, questions.size, ((System.currentTimeMillis() - sessionStartMs) / 1000).toInt(), wrongWords.toList(), correctWords.toList())
        } else {
            uiState = SWPracticeUiState(currentIndex = next, score = newScore)
        }
    }
}
