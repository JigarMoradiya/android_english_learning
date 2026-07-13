package com.example.myapplication.main.age_group.phonics.l9_digraphs.view_model

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
    val word: String,
    val note: String = ""
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
        DigraphWord(word="chick"),
        DigraphWord(word="chain",  note="→ Vowel Team · L13"),
        DigraphWord(word="chase",  note="→ Magic E · L12"),
        DigraphWord(word="cheese", note="→ Vowel Team · L13"))),
    DigraphEntry("", "sh", "/ʃ/", "Not /s/+/h/ — a brand-new /ʃ/ sound, like 'shh!'",
                 DigraphGroup.CH_SH, listOf(
        DigraphWord(word="ship"),  DigraphWord(word="shop"),
        DigraphWord(word="shed"),  DigraphWord(word="shell"),
        DigraphWord(word="shift"),
        DigraphWord(word="sheep",  note="→ Vowel Team · L13"),
        DigraphWord(word="shine",  note="→ Magic E · L12"),
        DigraphWord(word="shark",  note="→ R-Controlled · L15"))),
    // ── TH + WH ──
    DigraphEntry("", "th", "/θ/", "Tongue to teeth — /θ/ in 'thin', /ð/ in 'that'",
                 DigraphGroup.TH_WH, listOf(
        DigraphWord(word="thin"),  DigraphWord(word="that"),
        DigraphWord(word="them"),  DigraphWord(word="this"),
        DigraphWord(word="thick"), DigraphWord(word="thumb"),
        DigraphWord(word="think"),
        DigraphWord(word="three",  note="→ Vowel Team · L13"),
        DigraphWord(word="throne", note="→ Magic E · L12"))),
    DigraphEntry("", "wh", "/w/", "Sounds just like /w/ — like 'when' and 'which'",
                 DigraphGroup.TH_WH, listOf(
        DigraphWord(word="whip"),  DigraphWord(word="when"),
        DigraphWord(word="what"),  DigraphWord(word="which"),
        DigraphWord(word="whale",  note="→ Magic E · L12"),
        DigraphWord(word="while",  note="→ Magic E · L12"),
        DigraphWord(word="wheel",  note="→ Vowel Team · L13"),
        DigraphWord(word="wheat",  note="→ Vowel Team · L13"))),
    // ── PH + QU ──
    DigraphEntry("", "ph", "/f/", "Looks like P+H but says /f/ — same sound as the letter F",
                 DigraphGroup.PH_QU, listOf(
        DigraphWord(word="phantom"),
        DigraphWord(word="phone",   note="→ Magic E · L12"),
        DigraphWord(word="photo",   note="→ Open Syllable · L11"),
        DigraphWord(word="phew",    note="→ Vowel Team · L13"),
        DigraphWord(word="phonics", note="→ Open Syllable · L11"))),
    DigraphEntry("", "qu", "/kw/", "Always /kw/ together — like 'quiz' or 'quick'",
                 DigraphGroup.PH_QU, listOf(
        DigraphWord(word="quiz"),  DigraphWord(word="quilt"),
        DigraphWord(word="quick"), DigraphWord(word="quest"),
        DigraphWord(word="quack"),
        DigraphWord(word="queen",  note="→ Vowel Team · L13"),
        DigraphWord(word="quote",  note="→ Magic E · L12"),
        DigraphWord(word="quite",  note="→ Magic E · L12")))
)

val digraphsPracticeQuestions: List<DigraphPracticeQuestion> = listOf(
    DigraphPracticeQuestion(word="chip",    correctDigraph="ch", options=listOf("ch","sh","th"), imageName="chip"),
    DigraphPracticeQuestion(word="chest",   correctDigraph="ch", options=listOf("ch","wh","ph"), imageName="chest"),
    DigraphPracticeQuestion(word="chop",    correctDigraph="ch", options=listOf("sh","ch","qu"), imageName="chop"),
    DigraphPracticeQuestion(word="ship",    correctDigraph="sh", options=listOf("sh","ch","th"), imageName="ship"),
    DigraphPracticeQuestion(word="shell",   correctDigraph="sh", options=listOf("th","sh","wh"), imageName="shell"),
    DigraphPracticeQuestion(word="shed",    correctDigraph="sh", options=listOf("ph","qu","sh"), imageName="shed"),
    DigraphPracticeQuestion(word="thin",    correctDigraph="th", options=listOf("th","wh","sh"), imageName="thin"),
    DigraphPracticeQuestion(word="think",   correctDigraph="th", options=listOf("ch","th","ph"), imageName="think"),
    DigraphPracticeQuestion(word="thumb",   correctDigraph="th", options=listOf("wh","th","qu"), imageName="thumb"),
    DigraphPracticeQuestion(word="whip",    correctDigraph="wh", options=listOf("wh","th","ch"), imageName="whip"),
    DigraphPracticeQuestion(word="when",    correctDigraph="wh", options=listOf("sh","ph","wh"), imageName="when"),
    DigraphPracticeQuestion(word="which",   correctDigraph="wh", options=listOf("wh","qu","sh"), imageName="which"),
    DigraphPracticeQuestion(word="phantom", correctDigraph="ph", options=listOf("ph","wh","ch"), imageName="phantom"),
    DigraphPracticeQuestion(word="quill",   correctDigraph="qu", options=listOf("qu","ph","wh"), imageName="quill"),
    DigraphPracticeQuestion(word="quick",   correctDigraph="qu", options=listOf("ch","qu","th"), imageName="quick"),
    DigraphPracticeQuestion(word="quest",   correctDigraph="qu", options=listOf("qu","sh","ph"), imageName="quest")
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
        audioManager.playPhonicsSound("phonics_word/${entry.digraph}")
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
    private val audioManager: AudioPhonicsManager,
    private val levelProgressRepo: PhonicsLevelProgressRepository,
    private val phonicsSessions: PhonicsSessionRecorder
) : ViewModel() {

    private val questions = digraphsPracticeQuestions
    var uiState by mutableStateOf(DigraphPracticeUiState()); private set

    // Parent-report session tracking
    private var sessionStartMs = System.currentTimeMillis()
    private val wrongWords = mutableListOf<String>()
    private val correctWords = mutableListOf<String>()

    val totalQuestions: Int get() = questions.size
    val currentQuestion: DigraphPracticeQuestion? get() =
        questions.getOrNull(uiState.currentIndex)

    private var shakeJob: Job? = null

    fun onAnswerTap(answer: String) {
        val q = currentQuestion ?: return
        if (uiState.selectedAnswer != null) return
        val correct = answer == q.correctDigraph
        uiState = uiState.copy(selectedAnswer = answer, isCorrect = correct)
        if (correct) correctWords.add(q.word) else wrongWords.add(q.word)
        if (correct) {
            uiState = uiState.copy(score = uiState.score + 1)
            audioManager.playPhonicsSound("phonics_word/${q.word}")
        } else {
            AudioPlayerManager.playSoundWrongAnswer()
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
        sessionStartMs = System.currentTimeMillis()
        wrongWords.clear()
        correctWords.clear()
        audioManager.stop()
        uiState = DigraphPracticeUiState()
    }

    fun stop() { audioManager.stop() }
    override fun onCleared() { super.onCleared(); stop() }

    private fun advance() {
        val next = uiState.currentIndex + 1
        uiState = if (next >= questions.size) {
            levelProgressRepo.recordPractice(level = PhonicsListenLevelKey.digraphs, score = uiState.score, total = questions.size)
            phonicsSessions.recordPractice(PhonicsListenLevelKey.digraphs, uiState.score, questions.size, ((System.currentTimeMillis() - sessionStartMs) / 1000).toInt(), wrongWords.toList(), correctWords.toList())
            uiState.copy(isFinished = true)
        } else {
            uiState.copy(currentIndex = next, selectedAnswer = null, isCorrect = null, shakeWrong = false)
        }
    }
}
