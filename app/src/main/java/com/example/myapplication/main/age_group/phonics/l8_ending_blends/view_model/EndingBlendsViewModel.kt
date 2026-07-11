package com.example.myapplication.main.age_group.phonics.l8_ending_blends.view_model

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

enum class EndBlendGroup(val label: String, val emoji: String,
                         val color: Color, val shadowColor: Color,
                         val hint: String) {
    ND_NT("N-Endings", "🎵", Color(0xFF3949AB), Color(0xFF1A237E), "nd  nt"),
    MP_LK("M-Endings", "💧", Color(0xFFC2185B), Color(0xFF880E4F), "mp  lk  lt"),
    SK_FT("S-Endings", "⚡", Color(0xFF00838F), Color(0xFF006064), "sk  ft  st")
}

data class EndBlendWord(
    val id: String = UUID.randomUUID().toString(),
    val word: String,
    val blendLength: Int
) {
    val blendPart: String get() = word.takeLast(blendLength)
    val restPart: String  get() = word.dropLast(blendLength)
    val displayWord: String get() = "${restPart}___"
}

data class EndBlendEntry(
    val id: String = UUID.randomUUID().toString(),
    val blend: String,
    val phonetic: String,
    val soundHint: String,
    val group: EndBlendGroup,
    val words: List<EndBlendWord>
)

data class EndBlendPracticeQuestion(
    val id: String = UUID.randomUUID().toString(),
    val word: String,
    val blendLength: Int,
    val correctBlend: String,
    val options: List<String>,
    val imageName: String
) {
    val displayWord: String get() = "${word.dropLast(blendLength)}___"
}

// ── UI States ──────────────────────────────────────────────────────────────────

data class EndBlendLearnUiState(
    val selectedGroup: EndBlendGroup = EndBlendGroup.ND_NT,
    val selectedBlend: EndBlendEntry? = null,
    val highlightedWordId: String? = null,
    val showWords: Boolean = false
)

data class EndBlendPracticeUiState(
    val currentIndex: Int = 0,
    val score: Int = 0,
    val selectedAnswer: String? = null,
    val isCorrect: Boolean? = null,
    val isFinished: Boolean = false,
    val shakeWrong: Boolean = false
)

// ── Data ───────────────────────────────────────────────────────────────────────

val endingBlendsData: List<EndBlendEntry> = listOf(
    // ── N-Endings ──
    EndBlendEntry("", "nd", "/nd/", "Finish with /n/ then /d/ — like 'hand' or 'sand'",
                  EndBlendGroup.ND_NT, listOf(
        EndBlendWord(word="hand", blendLength=2), EndBlendWord(word="sand", blendLength=2),
        EndBlendWord(word="band", blendLength=2), EndBlendWord(word="land", blendLength=2),
        EndBlendWord(word="wind", blendLength=2), EndBlendWord(word="bend", blendLength=2),
        EndBlendWord(word="kind", blendLength=2), EndBlendWord(word="mind", blendLength=2))),
    EndBlendEntry("", "nt", "/nt/", "Finish with /n/ then /t/ — like 'tent' or 'mint'",
                  EndBlendGroup.ND_NT, listOf(
        EndBlendWord(word="tent", blendLength=2), EndBlendWord(word="dent", blendLength=2),
        EndBlendWord(word="mint", blendLength=2), EndBlendWord(word="hunt", blendLength=2),
        EndBlendWord(word="rent", blendLength=2), EndBlendWord(word="pant", blendLength=2),
        EndBlendWord(word="bent", blendLength=2), EndBlendWord(word="hint", blendLength=2))),
    // ── M/L-Endings ──
    EndBlendEntry("", "mp", "/mp/", "Finish with /m/ then /p/ — like 'jump' or 'lamp'",
                  EndBlendGroup.MP_LK, listOf(
        EndBlendWord(word="lamp", blendLength=2), EndBlendWord(word="camp", blendLength=2),
        EndBlendWord(word="damp", blendLength=2), EndBlendWord(word="ramp", blendLength=2),
        EndBlendWord(word="jump", blendLength=2), EndBlendWord(word="bump", blendLength=2),
        EndBlendWord(word="pump", blendLength=2), EndBlendWord(word="dump", blendLength=2))),
    EndBlendEntry("", "lk", "/lk/", "Finish with /l/ then /k/ — like 'milk' or 'bulk'",
                  EndBlendGroup.MP_LK, listOf(
        EndBlendWord(word="milk", blendLength=2), EndBlendWord(word="silk", blendLength=2),
        EndBlendWord(word="bulk", blendLength=2), EndBlendWord(word="hulk", blendLength=2),
        EndBlendWord(word="sulk", blendLength=2))),
    EndBlendEntry("", "lt", "/lt/", "Finish with /l/ then /t/ — like 'belt' or 'bolt'",
                  EndBlendGroup.MP_LK, listOf(
        EndBlendWord(word="belt", blendLength=2), EndBlendWord(word="melt", blendLength=2),
        EndBlendWord(word="salt", blendLength=2), EndBlendWord(word="bolt", blendLength=2),
        EndBlendWord(word="felt", blendLength=2), EndBlendWord(word="jolt", blendLength=2),
        EndBlendWord(word="wilt", blendLength=2), EndBlendWord(word="tilt", blendLength=2))),
    // ── S/F-Endings ──
    EndBlendEntry("", "sk", "/sk/", "Finish with /s/ then /k/ — like 'desk' or 'mask'",
                  EndBlendGroup.SK_FT, listOf(
        EndBlendWord(word="desk",  blendLength=2), EndBlendWord(word="disk",  blendLength=2),
        EndBlendWord(word="task",  blendLength=2), EndBlendWord(word="dusk",  blendLength=2),
        EndBlendWord(word="risk",  blendLength=2), EndBlendWord(word="mask",  blendLength=2),
        EndBlendWord(word="tusk",  blendLength=2), EndBlendWord(word="brisk", blendLength=2))),
    EndBlendEntry("", "ft", "/ft/", "Finish with /f/ then /t/ — like 'left' or 'gift'",
                  EndBlendGroup.SK_FT, listOf(
        EndBlendWord(word="left",  blendLength=2), EndBlendWord(word="soft",  blendLength=2),
        EndBlendWord(word="gift",  blendLength=2), EndBlendWord(word="loft",  blendLength=2),
        EndBlendWord(word="lift",  blendLength=2), EndBlendWord(word="raft",  blendLength=2),
        EndBlendWord(word="drift", blendLength=2), EndBlendWord(word="shift", blendLength=2))),
    EndBlendEntry("", "st", "/st/", "Finish with /s/ then /t/ — like 'nest' or 'fast'",
                  EndBlendGroup.SK_FT, listOf(
        EndBlendWord(word="best", blendLength=2), EndBlendWord(word="nest", blendLength=2),
        EndBlendWord(word="fast", blendLength=2), EndBlendWord(word="list", blendLength=2),
        EndBlendWord(word="dust", blendLength=2), EndBlendWord(word="rest", blendLength=2),
        EndBlendWord(word="just", blendLength=2), EndBlendWord(word="last", blendLength=2))),
    EndBlendEntry("", "ld", "/ld/", "Finish with /l/ then /d/ — like 'cold' or 'old'",
                  EndBlendGroup.MP_LK, listOf(
        EndBlendWord(word="cold", blendLength=2), EndBlendWord(word="old",  blendLength=2),
        EndBlendWord(word="gold", blendLength=2), EndBlendWord(word="held", blendLength=2),
        EndBlendWord(word="bold", blendLength=2), EndBlendWord(word="wild", blendLength=2))),
    EndBlendEntry("", "lp", "/lp/", "Finish with /l/ then /p/ — like 'help' or 'gulp'",
                  EndBlendGroup.MP_LK, listOf(
        EndBlendWord(word="help", blendLength=2), EndBlendWord(word="gulp", blendLength=2),
        EndBlendWord(word="yelp", blendLength=2), EndBlendWord(word="pulp", blendLength=2)))
)

val endingBlendsPracticeQuestions: List<EndBlendPracticeQuestion> = listOf(
    EndBlendPracticeQuestion(word="hand", blendLength=2, correctBlend="nd", options=listOf("nd","nt","mp"), imageName="hand"),
    EndBlendPracticeQuestion(word="tent", blendLength=2, correctBlend="nt", options=listOf("nt","nd","st"), imageName="tent"),
    EndBlendPracticeQuestion(word="lamp", blendLength=2, correctBlend="mp", options=listOf("mp","lk","nd"), imageName="lamp"),
    EndBlendPracticeQuestion(word="milk", blendLength=2, correctBlend="lk", options=listOf("lk","mp","lt"), imageName="milk"),
    EndBlendPracticeQuestion(word="desk", blendLength=2, correctBlend="sk", options=listOf("sk","st","ft"), imageName="desk"),
    EndBlendPracticeQuestion(word="left", blendLength=2, correctBlend="ft", options=listOf("ft","sk","st"), imageName="left"),
    EndBlendPracticeQuestion(word="belt", blendLength=2, correctBlend="lt", options=listOf("lt","lk","st"), imageName="belt"),
    EndBlendPracticeQuestion(word="nest", blendLength=2, correctBlend="st", options=listOf("st","sk","ft"), imageName="nest"),
    EndBlendPracticeQuestion(word="jump", blendLength=2, correctBlend="mp", options=listOf("mp","nd","lk"), imageName="jump"),
    EndBlendPracticeQuestion(word="sand", blendLength=2, correctBlend="nd", options=listOf("nd","nt","st"), imageName="sand"),
    EndBlendPracticeQuestion(word="hunt", blendLength=2, correctBlend="nt", options=listOf("nt","nd","mp"), imageName="hunt"),
    EndBlendPracticeQuestion(word="gift", blendLength=2, correctBlend="ft", options=listOf("ft","st","sk"), imageName="gift"),
    EndBlendPracticeQuestion(word="dust", blendLength=2, correctBlend="st", options=listOf("st","nd","ft"), imageName="dust"),
    EndBlendPracticeQuestion(word="mask", blendLength=2, correctBlend="sk", options=listOf("sk","ft","lt"), imageName="mask"),
    EndBlendPracticeQuestion(word="bulk", blendLength=2, correctBlend="lk", options=listOf("lk","lt","mp"), imageName="bulk"),
    EndBlendPracticeQuestion(word="cold", blendLength=2, correctBlend="ld", options=listOf("ld","lt","nd"), imageName="cold"),
    EndBlendPracticeQuestion(word="help", blendLength=2, correctBlend="lp", options=listOf("lp","lk","mp"), imageName="help")
)

// ── Learn ViewModel ────────────────────────────────────────────────────────────

@HiltViewModel
class EndingBlendsLearnViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {

    var uiState by mutableStateOf(EndBlendLearnUiState()); private set
    private var animSession = UUID.randomUUID().toString()
    private var showWordsJob: Job? = null

    fun onGroupTap(group: EndBlendGroup) {
        animSession = UUID.randomUUID().toString()
        audioManager.stop()
        val firstBlend = endingBlendsData.firstOrNull { it.group == group }
        uiState = EndBlendLearnUiState(selectedGroup = group, selectedBlend = firstBlend)
        scheduleShowWords()
    }

    fun onBlendTap(blend: EndBlendEntry) {
        val session = UUID.randomUUID().toString()
        animSession = session
        audioManager.stop()
        uiState = uiState.copy(selectedBlend = blend, showWords = false, highlightedWordId = null)
        showWordsJob?.cancel()
        showWordsJob = viewModelScope.launch {
            delay(150)
            if (animSession == session) uiState = uiState.copy(showWords = true)
        }
    }

    fun onWordTap(word: EndBlendWord) {
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

    fun onBlendSoundTap(blend: EndBlendEntry) {
        audioManager.playPhonicsSound("phonics_word/${blend.blend}")
    }

    fun stop() {
        animSession = UUID.randomUUID().toString()
        showWordsJob?.cancel()
        audioManager.stop()
    }

    override fun onCleared() { super.onCleared(); stop() }

    private fun scheduleShowWords() {
        val session = animSession
        showWordsJob?.cancel()
        showWordsJob = viewModelScope.launch {
            delay(250)
            if (animSession == session) uiState = uiState.copy(showWords = true)
        }
    }
}

// ── Practice ViewModel ─────────────────────────────────────────────────────────

@HiltViewModel
class EndingBlendsPracticeViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {

    private val questions = endingBlendsPracticeQuestions.shuffled()
    var uiState by mutableStateOf(EndBlendPracticeUiState()); private set

    val totalQuestions: Int get() = questions.size
    val currentQuestion: EndBlendPracticeQuestion? get() =
        questions.getOrNull(uiState.currentIndex)

    private var shakeJob: Job? = null

    fun onAnswerTap(answer: String) {
        val q = currentQuestion ?: return
        if (uiState.selectedAnswer != null) return
        val correct = answer == q.correctBlend
        uiState = uiState.copy(selectedAnswer = answer, isCorrect = correct)
        if (correct) {
            uiState = uiState.copy(score = uiState.score + 1)
            audioManager.playPhonicsSound("phonics_word/${q.word}")
        } else {
            uiState = uiState.copy(shakeWrong = true)
            shakeJob?.cancel()
            shakeJob = viewModelScope.launch {
                delay(600)
                uiState = uiState.copy(shakeWrong = false)
            }
        }
        viewModelScope.launch {
            delay(if (correct) 1200L else 1800L)
            advance()
        }
    }

    fun restart() {
        audioManager.stop()
        uiState = EndBlendPracticeUiState()
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
