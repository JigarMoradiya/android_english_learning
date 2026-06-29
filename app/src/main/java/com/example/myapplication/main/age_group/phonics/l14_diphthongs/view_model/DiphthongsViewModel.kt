package com.example.myapplication.main.age_group.phonics.l14_diphthongs.view_model

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
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.resume

// ── Models ────────────────────────────────────────────────────────────────────

data class DiphthongWord(
    val id: String = UUID.randomUUID().toString(),
    val word: String,
    val diphthong: String
) {
    val pre: String get() {
        val idx = word.indexOf(diphthong)
        return if (idx >= 0) word.substring(0, idx) else ""
    }
    val suf: String get() {
        val idx = word.indexOf(diphthong)
        return if (idx >= 0) word.substring(idx + diphthong.length) else ""
    }
}

data class DiphthongGroup(
    val id: String = UUID.randomUUID().toString(),
    val sound: String,
    val emoji: String,
    val spellings: List<String>,
    val accentColor: Color,
    val shadowColor: Color,
    val words: List<DiphthongWord>
) {
    fun wordsBySpelling(spelling: String) = words.filter { it.diphthong == spelling }
}

// ── Data ──────────────────────────────────────────────────────────────────────

val diphthongGroups: List<DiphthongGroup> = listOf(
    DiphthongGroup(
        sound = "/ɔɪ/", emoji = "💰", spellings = listOf("oi", "oy"),
        accentColor = Color(0xFFE65100), shadowColor = Color(0xFFBF360C),
        words = listOf(
            DiphthongWord(word = "coin",  diphthong = "oi"), DiphthongWord(word = "oil",   diphthong = "oi"),
            DiphthongWord(word = "coil",  diphthong = "oi"), DiphthongWord(word = "foil",  diphthong = "oi"),
            DiphthongWord(word = "soil",  diphthong = "oi"), DiphthongWord(word = "join",  diphthong = "oi"),
            DiphthongWord(word = "toy",   diphthong = "oy"), DiphthongWord(word = "boy",   diphthong = "oy"),
            DiphthongWord(word = "joy",   diphthong = "oy"), DiphthongWord(word = "coy",   diphthong = "oy"),
            DiphthongWord(word = "soy",   diphthong = "oy"),
        )
    ),
    DiphthongGroup(
        sound = "/aʊ/", emoji = "☁️", spellings = listOf("ou", "ow"),
        accentColor = Color(0xFF00695C), shadowColor = Color(0xFF004D40),
        words = listOf(
            DiphthongWord(word = "out",   diphthong = "ou"), DiphthongWord(word = "loud",  diphthong = "ou"),
            DiphthongWord(word = "found", diphthong = "ou"), DiphthongWord(word = "count", diphthong = "ou"),
            DiphthongWord(word = "mouth", diphthong = "ou"), DiphthongWord(word = "cloud", diphthong = "ou"),
            DiphthongWord(word = "cow",   diphthong = "ow"), DiphthongWord(word = "now",   diphthong = "ow"),
            DiphthongWord(word = "how",   diphthong = "ow"), DiphthongWord(word = "town",  diphthong = "ow"),
            DiphthongWord(word = "down",  diphthong = "ow"), DiphthongWord(word = "brown", diphthong = "ow"),
        )
    ),
    DiphthongGroup(
        sound = "/ɔː/", emoji = "🐾", spellings = listOf("au", "aw"),
        accentColor = Color(0xFF4A148C), shadowColor = Color(0xFF311B92),
        words = listOf(
            DiphthongWord(word = "haul",  diphthong = "au"), DiphthongWord(word = "cause", diphthong = "au"),
            DiphthongWord(word = "sauce", diphthong = "au"), DiphthongWord(word = "pause", diphthong = "au"),
            DiphthongWord(word = "vault", diphthong = "au"), DiphthongWord(word = "saw",   diphthong = "aw"),
            DiphthongWord(word = "paw",   diphthong = "aw"), DiphthongWord(word = "jaw",   diphthong = "aw"),
            DiphthongWord(word = "draw",  diphthong = "aw"), DiphthongWord(word = "straw", diphthong = "aw"),
            DiphthongWord(word = "yawn",  diphthong = "aw"),
        )
    ),
)

// ── Learn UI State ────────────────────────────────────────────────────────────

data class DiphthongsLearnUiState(
    val selectedGroup: DiphthongGroup = diphthongGroups[0],
    val highlightedWordId: String? = null,
    val showWords: Boolean = false
)

// ── Learn ViewModel ───────────────────────────────────────────────────────────

@HiltViewModel
class DiphthongsLearnViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {

    var uiState by mutableStateOf(DiphthongsLearnUiState()); private set
    private var animSession = UUID.randomUUID().toString()
    private var showWordsJob: Job? = null

    fun onScreenAppear() {
        val session = UUID.randomUUID().toString()
        animSession = session
        audioManager.stop()
        uiState = uiState.copy(showWords = false)
        showWordsJob?.cancel()
        showWordsJob = viewModelScope.launch {
            delay(100)
            if (animSession == session) uiState = uiState.copy(showWords = true)
        }
    }

    fun onGroupTap(group: DiphthongGroup) {
        if (group.id == uiState.selectedGroup.id) return
        val session = UUID.randomUUID().toString()
        animSession = session
        audioManager.stop()
        uiState = DiphthongsLearnUiState(selectedGroup = group)
        showWordsJob?.cancel()
        showWordsJob = viewModelScope.launch {
            delay(150)
            if (animSession == session) uiState = uiState.copy(showWords = true)
        }
    }

    fun onWordTap(word: DiphthongWord) {
        val session = UUID.randomUUID().toString()
        animSession = session
        audioManager.stop()
        uiState = uiState.copy(highlightedWordId = word.id)
        audioManager.playPhonicsSound("phonics_word/${word.word}")
        audioManager.onAudioCompleted = {
            // Only clear when audio actually finishes — matches iOS AudioPhonicsManager.onAudioCompleted callback.
            // If audio file doesn't exist, callback never fires → chip stays until next tap (radio-button UX).
            if (animSession == session) uiState = uiState.copy(highlightedWordId = null)
        }
    }

    fun stop() {
        animSession = UUID.randomUUID().toString()
        showWordsJob?.cancel()
        audioManager.stop()
    }

    override fun onCleared() { super.onCleared(); stop() }
}

// ── Practice Models ───────────────────────────────────────────────────────────

data class DiphthongsPracticeQuestion(
    val id: String = UUID.randomUUID().toString(),
    val word: String,
    val correctDiphthong: String,
    val options: List<String>
) {
    val displayWord: String get() = word.replace(correctDiphthong, "__")
}

data class DiphthongsPracticeUiState(
    val currentIndex: Int = 0,
    val score: Int = 0,
    val selectedAnswer: String? = null,
    val isCorrect: Boolean? = null,
    val isFinished: Boolean = false,
    val shakeWrong: Boolean = false
)

// ── Practice Data ─────────────────────────────────────────────────────────────

val diphthongsPracticeQuestions: List<DiphthongsPracticeQuestion> = listOf(
    DiphthongsPracticeQuestion(word = "coin",  correctDiphthong = "oi", options = listOf("oi", "ou", "aw")),
    DiphthongsPracticeQuestion(word = "toy",   correctDiphthong = "oy", options = listOf("oy", "ow", "au")),
    DiphthongsPracticeQuestion(word = "cloud", correctDiphthong = "ou", options = listOf("ou", "oi", "aw")),
    DiphthongsPracticeQuestion(word = "cow",   correctDiphthong = "ow", options = listOf("ow", "oi", "au")),
    DiphthongsPracticeQuestion(word = "paw",   correctDiphthong = "aw", options = listOf("aw", "ou", "oy")),
    DiphthongsPracticeQuestion(word = "join",  correctDiphthong = "oi", options = listOf("oi", "ow", "au")),
    DiphthongsPracticeQuestion(word = "out",   correctDiphthong = "ou", options = listOf("ou", "aw", "oy")),
    DiphthongsPracticeQuestion(word = "joy",   correctDiphthong = "oy", options = listOf("oy", "au", "ou")),
    DiphthongsPracticeQuestion(word = "saw",   correctDiphthong = "aw", options = listOf("aw", "ow", "oi")),
    DiphthongsPracticeQuestion(word = "down",  correctDiphthong = "ow", options = listOf("ow", "oi", "au")),
    DiphthongsPracticeQuestion(word = "haul",  correctDiphthong = "au", options = listOf("au", "oi", "ow")),
    DiphthongsPracticeQuestion(word = "loud",  correctDiphthong = "ou", options = listOf("ou", "oy", "aw")),
    DiphthongsPracticeQuestion(word = "boy",   correctDiphthong = "oy", options = listOf("oy", "au", "ou")),
    DiphthongsPracticeQuestion(word = "draw",  correctDiphthong = "aw", options = listOf("aw", "ow", "oi")),
    DiphthongsPracticeQuestion(word = "foil",  correctDiphthong = "oi", options = listOf("oi", "au", "ow")),
)

// ── Practice ViewModel ────────────────────────────────────────────────────────

@HiltViewModel
class DiphthongsPracticeViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {

    private val questions = diphthongsPracticeQuestions.shuffled()
    var uiState by mutableStateOf(DiphthongsPracticeUiState()); private set

    val totalQuestions: Int get() = questions.size
    val currentQuestion: DiphthongsPracticeQuestion? get() = questions.getOrNull(uiState.currentIndex)

    private var shakeJob: Job? = null

    fun onAnswerTap(answer: String) {
        val q = currentQuestion ?: return
        if (uiState.selectedAnswer != null) return
        val correct = answer == q.correctDiphthong
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

    fun restart() { audioManager.stop(); uiState = DiphthongsPracticeUiState() }
    fun stop() { audioManager.stop() }
    override fun onCleared() { super.onCleared(); stop() }

    private fun advance() {
        val next = uiState.currentIndex + 1
        uiState = if (next >= questions.size)
            uiState.copy(isFinished = true)
        else
            uiState.copy(currentIndex = next, selectedAnswer = null, isCorrect = null, shakeWrong = false)
    }
}

// ── Listen Entry ──────────────────────────────────────────────────────────────

data class DiphthongsListenEntry(
    val id: String = UUID.randomUUID().toString(),
    val word: String,
    val diphthong: String,
    val pre: String,
    val suf: String,
    val accentColor: Color,
    val shadowColor: Color,
    val sound: String
) {
    val chars: List<String> get() = (pre + diphthong + suf).map { it.toString() }
    val pairIdx1: Int get() = pre.length
    val pairIdx2: Int get() = pre.length + 1
    val showArc: Boolean get() = pairIdx2 - pairIdx1 > 1

    fun charSegment(idx: Int): Int = when {
        idx < pre.length     -> 0
        idx <= pre.length + 1 -> 1
        else                  -> 2
    }
    fun hasSegment(seg: Int): Boolean = when (seg) {
        0    -> pre.isNotEmpty()
        1    -> true
        2    -> suf.isNotEmpty()
        else -> false
    }
    fun segmentAudio(seg: Int): String = when (seg) {
        0    -> if (pre.isEmpty()) "" else "phonics_letter/sound_${pre[0]}"
        1    -> "phonics_word/$diphthong"
        2    -> if (suf.isEmpty()) "" else "phonics_letter/sound_${suf[0]}"
        else -> "phonics_word/$word"
    }
    fun segmentLabel(seg: Int): String = when (seg) {
        0    -> if (pre.isEmpty()) "—" else pre
        1    -> diphthong
        2    -> if (suf.isEmpty()) "—" else suf
        else -> ""
    }
}

val diphthongsListenEntries: List<DiphthongsListenEntry> = diphthongGroups.flatMap { group ->
    group.words.map { word ->
        DiphthongsListenEntry(
            word = word.word, diphthong = word.diphthong,
            pre = word.pre, suf = word.suf,
            accentColor = group.accentColor, shadowColor = group.shadowColor,
            sound = group.sound
        )
    }
}

// ── Listen UI State ───────────────────────────────────────────────────────────

data class DiphthongsListenUiState(
    val segmentIndex: Int = -1,
    val isAutoMode: Boolean = false,
    val isPlaying: Boolean = false,
    val wordDone: Boolean = false,
    val playedSegments: Set<Int> = emptySet(),
    val isGoingForward: Boolean = true
)

// ── Listen ViewModel ──────────────────────────────────────────────────────────

@HiltViewModel
class DiphthongsListenViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {

    var wordIndex by mutableStateOf(0); private set
    var uiState   by mutableStateOf(DiphthongsListenUiState()); private set

    val totalWords: Int get() = diphthongsListenEntries.size
    val currentEntry: DiphthongsListenEntry? get() = diphthongsListenEntries.getOrNull(wordIndex)

    private var autoPlayJob: Job? = null

    fun onSegmentTap(segIdx: Int) {
        val entry = currentEntry ?: return
        if (!entry.hasSegment(segIdx)) return
        autoPlayJob?.cancel(); audioManager.stop()
        uiState = uiState.copy(segmentIndex = segIdx, playedSegments = uiState.playedSegments + segIdx, wordDone = false)
        audioManager.playPhonicsSound(entry.segmentAudio(segIdx))
        audioManager.onAudioCompleted = {
            if (uiState.segmentIndex == segIdx && segIdx == 2) {
                audioManager.playPhonicsSound("phonics_word/${entry.word}")
                uiState = uiState.copy(wordDone = true, segmentIndex = -1)
            }
        }
    }

    fun startAutoPlay() {
        val entry = currentEntry ?: return
        if (uiState.isPlaying) return
        uiState = uiState.copy(isPlaying = true, segmentIndex = -1, playedSegments = emptySet(), wordDone = false)
        autoPlayJob = viewModelScope.launch {
            for (segIdx in 0..2) {
                if (!entry.hasSegment(segIdx)) continue
                uiState = uiState.copy(segmentIndex = segIdx, playedSegments = uiState.playedSegments + segIdx)
                suspendCancellableCoroutine { cont ->
                    audioManager.playPhonicsSound(entry.segmentAudio(segIdx))
                    audioManager.onAudioCompleted = { if (cont.isActive) cont.resume(Unit) }
                    cont.invokeOnCancellation { audioManager.stop() }
                }
                delay(120)
            }
            uiState = uiState.copy(wordDone = true, segmentIndex = -1)
            suspendCancellableCoroutine { cont ->
                audioManager.playPhonicsSound("phonics_word/${entry.word}")
                audioManager.onAudioCompleted = { if (cont.isActive) cont.resume(Unit) }
                cont.invokeOnCancellation { audioManager.stop() }
            }
            uiState = uiState.copy(isPlaying = false)
        }
    }

    fun pauseAutoPlay() {
        autoPlayJob?.cancel(); autoPlayJob = null
        audioManager.stop(); uiState = uiState.copy(isPlaying = false)
    }

    fun nextWord() {
        if (wordIndex >= totalWords - 1) return
        pauseAutoPlay(); wordIndex += 1
        uiState = DiphthongsListenUiState(isAutoMode = uiState.isAutoMode, isGoingForward = true)
    }

    fun prevWord() {
        if (wordIndex <= 0) return
        pauseAutoPlay(); wordIndex -= 1
        uiState = DiphthongsListenUiState(isAutoMode = uiState.isAutoMode, isGoingForward = false)
    }

    fun toggleMode() {
        pauseAutoPlay()
        uiState = DiphthongsListenUiState(isAutoMode = !uiState.isAutoMode, isGoingForward = uiState.isGoingForward)
    }

    fun stop() { autoPlayJob?.cancel(); audioManager.stop() }
    override fun onCleared() { super.onCleared(); stop() }
}
