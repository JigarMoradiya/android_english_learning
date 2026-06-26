package com.example.myapplication.main.age_group.phonics.l11_digraphs.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.utilities.AudioPhonicsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

// ── Models ────────────────────────────────────────────────────────────────────

enum class DigraphGroup(val label: String, val emoji: String,
                        val color: Color, val shadowColor: Color,
                        val hint: String) {
    CH_SH("CH · SH", "🔊", Color(0xFFE65100), Color(0xFFBF360C), "ch  sh"),
    TH_WH("TH · WH", "💬", Color(0xFF00838F), Color(0xFF006064), "th  wh"),
    PH_QU("PH · QU", "📞", Color(0xFF7B1FA2), Color(0xFF4A148C), "ph  qu")
}

data class DigraphWord(
    val id: String = UUID.randomUUID().toString(),
    val word: String
) {
    val digraphPart: String get() = word.take(2)
    val restPart: String    get() = word.drop(2)
}

data class DigraphEntry(
    val id: String = UUID.randomUUID().toString(),
    val digraph: String,
    val phonetic: String,
    val soundHint: String,
    val group: DigraphGroup,
    val words: List<DigraphWord>
)

data class DigraphPracticeQuestion(
    val id: String = UUID.randomUUID().toString(),
    val word: String,
    val correctDigraph: String,
    val options: List<String>,
    val imageName: String
) {
    val displayWord: String get() = "___${word.drop(2)}"
}

// ── UI States ──────────────────────────────────────────────────────────────────

data class DigraphLearnUiState(
    val selectedGroup: DigraphGroup = DigraphGroup.CH_SH,
    val selectedDigraph: DigraphEntry? = null,
    val highlightedWordId: String? = null,
    val showWords: Boolean = false
)

data class DigraphPracticeUiState(
    val currentIndex: Int = 0,
    val score: Int = 0,
    val selectedAnswer: String? = null,
    val isCorrect: Boolean? = null,
    val isFinished: Boolean = false,
    val shakeWrong: Boolean = false
)

// ── Data ───────────────────────────────────────────────────────────────────────

val digraphsData: List<DigraphEntry> = listOf(
    // ── CH + SH ──
    DigraphEntry("", "ch", "/tʃ/", "Not /k/+/h/ — a brand-new /tʃ/ sound, like 'cheese'",
                 DigraphGroup.CH_SH, listOf(
        DigraphWord(word="chip"),  DigraphWord(word="chop"),
        DigraphWord(word="chat"),  DigraphWord(word="chin"),
        DigraphWord(word="check"), DigraphWord(word="chest"),
        DigraphWord(word="chick"), DigraphWord(word="chain"))),
    DigraphEntry("", "sh", "/ʃ/", "Not /s/+/h/ — a brand-new /ʃ/ sound, like 'shh!'",
                 DigraphGroup.CH_SH, listOf(
        DigraphWord(word="ship"),  DigraphWord(word="shop"),
        DigraphWord(word="shed"),  DigraphWord(word="shell"),
        DigraphWord(word="shift"), DigraphWord(word="shark"),
        DigraphWord(word="sheep"), DigraphWord(word="shoot"))),
    // ── TH + WH ──
    DigraphEntry("", "th", "/θ/", "Tongue to teeth — /θ/ in 'thin', /ð/ in 'that'",
                 DigraphGroup.TH_WH, listOf(
        DigraphWord(word="thin"),  DigraphWord(word="that"),
        DigraphWord(word="them"),  DigraphWord(word="this"),
        DigraphWord(word="thick"), DigraphWord(word="three"),
        DigraphWord(word="thumb"), DigraphWord(word="think"))),
    DigraphEntry("", "wh", "/w/", "Sounds just like /w/ — like 'where' and 'wheel'",
                 DigraphGroup.TH_WH, listOf(
        DigraphWord(word="whip"),  DigraphWord(word="when"),
        DigraphWord(word="what"),  DigraphWord(word="wheel"),
        DigraphWord(word="wheat"), DigraphWord(word="whale"),
        DigraphWord(word="while"), DigraphWord(word="which"))),
    // ── PH + QU ──
    DigraphEntry("", "ph", "/f/", "Looks like P+H but says /f/ — like 'phone' or 'photo'",
                 DigraphGroup.PH_QU, listOf(
        DigraphWord(word="phone"),    DigraphWord(word="photo"),
        DigraphWord(word="phrase"),   DigraphWord(word="phew"),
        DigraphWord(word="phantom"),  DigraphWord(word="pheasant"))),
    DigraphEntry("", "qu", "/kw/", "Always /kw/ together — like 'queen' or 'quiz'",
                 DigraphGroup.PH_QU, listOf(
        DigraphWord(word="queen"), DigraphWord(word="quiz"),
        DigraphWord(word="quilt"), DigraphWord(word="quick"),
        DigraphWord(word="quest"), DigraphWord(word="quack"),
        DigraphWord(word="quote"), DigraphWord(word="quite")))
)

val digraphsPracticeQuestions: List<DigraphPracticeQuestion> = listOf(
    DigraphPracticeQuestion(word="chip",    correctDigraph="ch", options=listOf("ch","sh","th"), imageName="chip"),
    DigraphPracticeQuestion(word="chest",   correctDigraph="ch", options=listOf("ch","wh","ph"), imageName="chest"),
    DigraphPracticeQuestion(word="chain",   correctDigraph="ch", options=listOf("sh","ch","qu"), imageName="chain"),
    DigraphPracticeQuestion(word="ship",    correctDigraph="sh", options=listOf("sh","ch","th"), imageName="ship"),
    DigraphPracticeQuestion(word="shell",   correctDigraph="sh", options=listOf("th","sh","wh"), imageName="shell"),
    DigraphPracticeQuestion(word="shark",   correctDigraph="sh", options=listOf("ph","qu","sh"), imageName="shark"),
    DigraphPracticeQuestion(word="thin",    correctDigraph="th", options=listOf("th","wh","sh"), imageName="thin"),
    DigraphPracticeQuestion(word="three",   correctDigraph="th", options=listOf("ch","th","ph"), imageName="three"),
    DigraphPracticeQuestion(word="thumb",   correctDigraph="th", options=listOf("wh","th","qu"), imageName="thumb"),
    DigraphPracticeQuestion(word="whip",    correctDigraph="wh", options=listOf("wh","th","ch"), imageName="whip"),
    DigraphPracticeQuestion(word="whale",   correctDigraph="wh", options=listOf("sh","ph","wh"), imageName="whale"),
    DigraphPracticeQuestion(word="phone",   correctDigraph="ph", options=listOf("ph","wh","ch"), imageName="phone"),
    DigraphPracticeQuestion(word="photo",   correctDigraph="ph", options=listOf("sh","ph","qu"), imageName="photo"),
    DigraphPracticeQuestion(word="queen",   correctDigraph="qu", options=listOf("qu","ph","wh"), imageName="queen"),
    DigraphPracticeQuestion(word="quick",   correctDigraph="qu", options=listOf("ch","qu","th"), imageName="quick")
)

// ── Learn ViewModel ────────────────────────────────────────────────────────────

@HiltViewModel
class DigraphsLearnViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {

    var uiState by mutableStateOf(DigraphLearnUiState()); private set
    private var animSession = UUID.randomUUID().toString()
    private var showWordsJob: Job? = null

    fun onGroupTap(group: DigraphGroup) {
        if (group == uiState.selectedGroup) return
        animSession = UUID.randomUUID().toString()
        audioManager.stop()
        // No auto-select of digraph — just clear (matches iOS)
        uiState = DigraphLearnUiState(selectedGroup = group)
    }

    fun onDigraphTap(entry: DigraphEntry) {
        val session = UUID.randomUUID().toString()
        animSession = session
        audioManager.stop()
        uiState = uiState.copy(highlightedWordId = null, showWords = false, selectedDigraph = entry)
        showWordsJob?.cancel()
        showWordsJob = viewModelScope.launch {
            delay(150)
            if (animSession == session) uiState = uiState.copy(showWords = true)
        }
    }

    fun onWordTap(word: DigraphWord) {
        val session = UUID.randomUUID().toString()
        animSession = session
        audioManager.stop()
        uiState = uiState.copy(highlightedWordId = word.id)
        audioManager.playPhonicsSound("phonics_word/${word.word}")
        audioManager.onAudioCompleted = {
            if (animSession == session) uiState = uiState.copy(highlightedWordId = null)
        }
    }

    fun onLetterSoundTap(letter: String) {
        audioManager.playPhonicsSound("phonics_letter/sound_$letter")
    }

    fun onDigraphSoundTap(entry: DigraphEntry) {
        audioManager.playPhonicsSound("phonics_blend/${entry.digraph}")
    }

    fun stop() {
        animSession = UUID.randomUUID().toString()
        showWordsJob?.cancel()
        audioManager.stop()
    }

    override fun onCleared() { super.onCleared(); stop() }
}

// ── Practice ViewModel ─────────────────────────────────────────────────────────

@HiltViewModel
class DigraphsPracticeViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {

    private val questions = digraphsPracticeQuestions
    var uiState by mutableStateOf(DigraphPracticeUiState()); private set

    val totalQuestions: Int get() = questions.size
    val currentQuestion: DigraphPracticeQuestion? get() =
        questions.getOrNull(uiState.currentIndex)

    private var shakeJob: Job? = null

    fun onAnswerTap(answer: String) {
        val q = currentQuestion ?: return
        if (uiState.selectedAnswer != null) return
        val correct = answer == q.correctDigraph
        uiState = uiState.copy(selectedAnswer = answer, isCorrect = correct)
        if (correct) {
            uiState = uiState.copy(score = uiState.score + 1)
            audioManager.playPhonicsSound("phonics_word/${q.word}")
        } else {
            uiState = uiState.copy(shakeWrong = true)
            shakeJob?.cancel()
            shakeJob = viewModelScope.launch {
                delay(500)
                uiState = uiState.copy(shakeWrong = false)
            }
        }
        viewModelScope.launch {
            delay(1000L)
            advance()
        }
    }

    fun restart() {
        audioManager.stop()
        uiState = DigraphPracticeUiState()
    }

    fun stop() { audioManager.stop() }
    override fun onCleared() { super.onCleared(); stop() }

    private fun advance() {
        val next = uiState.currentIndex + 1
        uiState = if (next >= questions.size) {
            uiState.copy(isFinished = true)
        } else {
            uiState.copy(currentIndex = next, selectedAnswer = null, isCorrect = null, shakeWrong = false)
        }
    }
}
