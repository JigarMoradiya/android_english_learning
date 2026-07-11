package com.example.myapplication.main.age_group.phonics.l15_r_controlled.view_model

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

data class RControlledWord(
    val id: String = UUID.randomUUID().toString(),
    val word: String,
    val rTeam: String
) {
    val pre: String get() {
        val idx = word.indexOf(rTeam)
        return if (idx >= 0) word.substring(0, idx) else ""
    }
    val vowelChar: String get() = rTeam.first().toString()
    val postTeam: String get() {
        val idx = word.indexOf(rTeam)
        return if (idx >= 0) word.substring(idx + rTeam.length) else ""
    }
}

data class RControlledGroup(
    val id: String = UUID.randomUUID().toString(),
    val rTeam: String,
    val sound: String,
    val emoji: String,
    val accentColor: Color,
    val shadowColor: Color,
    val words: List<RControlledWord>
)

// ── Data ──────────────────────────────────────────────────────────────────────

val rControlledGroups: List<RControlledGroup> = listOf(
    RControlledGroup(rTeam = "ar", sound = "/ɑːr/", emoji = "🚗", accentColor = Color(0xFFF57F17), shadowColor = Color(0xFFE65100), words = listOf(
        RControlledWord(word = "car",  rTeam = "ar"), RControlledWord(word = "star", rTeam = "ar"),
        RControlledWord(word = "farm", rTeam = "ar"), RControlledWord(word = "jar",  rTeam = "ar"),
        RControlledWord(word = "arm",  rTeam = "ar"), RControlledWord(word = "art",  rTeam = "ar"),
        RControlledWord(word = "bark", rTeam = "ar"), RControlledWord(word = "dark", rTeam = "ar"),
        RControlledWord(word = "hard", rTeam = "ar"), RControlledWord(word = "park", rTeam = "ar"),
        RControlledWord(word = "yard", rTeam = "ar"), RControlledWord(word = "barn", rTeam = "ar"),
    )),
    RControlledGroup(rTeam = "or", sound = "/ɔːr/", emoji = "🌽", accentColor = Color(0xFFBF360C), shadowColor = Color(0xFF7F0000), words = listOf(
        RControlledWord(word = "for",   rTeam = "or"), RControlledWord(word = "fork",  rTeam = "or"),
        RControlledWord(word = "corn",  rTeam = "or"), RControlledWord(word = "born",  rTeam = "or"),
        RControlledWord(word = "horn",  rTeam = "or"), RControlledWord(word = "sort",  rTeam = "or"),
        RControlledWord(word = "short", rTeam = "or"), RControlledWord(word = "worn",  rTeam = "or"),
        RControlledWord(word = "cord",  rTeam = "or"), RControlledWord(word = "fort",  rTeam = "or"),
    )),
    RControlledGroup(rTeam = "er", sound = "/ɜːr/", emoji = "🌿", accentColor = Color(0xFF00695C), shadowColor = Color(0xFF004D40), words = listOf(
        RControlledWord(word = "her",   rTeam = "er"), RControlledWord(word = "fern",  rTeam = "er"),
        RControlledWord(word = "verb",  rTeam = "er"), RControlledWord(word = "term",  rTeam = "er"),
        RControlledWord(word = "germ",  rTeam = "er"), RControlledWord(word = "herd",  rTeam = "er"),
        RControlledWord(word = "stern", rTeam = "er"), RControlledWord(word = "serve", rTeam = "er"),
    )),
    RControlledGroup(rTeam = "ir", sound = "/ɜːr/", emoji = "🐦", accentColor = Color(0xFF283593), shadowColor = Color(0xFF1A237E), words = listOf(
        RControlledWord(word = "bird",  rTeam = "ir"), RControlledWord(word = "girl",  rTeam = "ir"),
        RControlledWord(word = "first", rTeam = "ir"), RControlledWord(word = "stir",  rTeam = "ir"),
        RControlledWord(word = "sir",   rTeam = "ir"), RControlledWord(word = "dirt",  rTeam = "ir"),
        RControlledWord(word = "firm",  rTeam = "ir"), RControlledWord(word = "third", rTeam = "ir"),
    )),
    RControlledGroup(rTeam = "ur", sound = "/ɜːr/", emoji = "🔥", accentColor = Color(0xFF6A1B9A), shadowColor = Color(0xFF4A148C), words = listOf(
        RControlledWord(word = "burn",  rTeam = "ur"), RControlledWord(word = "turn",  rTeam = "ur"),
        RControlledWord(word = "fur",   rTeam = "ur"), RControlledWord(word = "hurt",  rTeam = "ur"),
        RControlledWord(word = "curl",  rTeam = "ur"), RControlledWord(word = "surf",  rTeam = "ur"),
        RControlledWord(word = "nurse", rTeam = "ur"), RControlledWord(word = "burst", rTeam = "ur"),
    )),
    RControlledGroup(rTeam = "air", sound = "/eər/", emoji = "💇", accentColor = Color(0xFF00838F), shadowColor = Color(0xFF006064), words = listOf(
        RControlledWord(word = "hair",  rTeam = "air"), RControlledWord(word = "chair", rTeam = "air"),
        RControlledWord(word = "pair",  rTeam = "air"), RControlledWord(word = "fair",  rTeam = "air"),
        RControlledWord(word = "stair", rTeam = "air"), RControlledWord(word = "air",   rTeam = "air"),
    )),
    RControlledGroup(rTeam = "ear", sound = "/ɪər/", emoji = "👂", accentColor = Color(0xFF5E35B1), shadowColor = Color(0xFF4527A0), words = listOf(
        RControlledWord(word = "ear",   rTeam = "ear"), RControlledWord(word = "hear",  rTeam = "ear"),
        RControlledWord(word = "year",  rTeam = "ear"), RControlledWord(word = "near",  rTeam = "ear"),
        RControlledWord(word = "clear", rTeam = "ear"), RControlledWord(word = "dear",  rTeam = "ear"),
    )),
    RControlledGroup(rTeam = "ore", sound = "/ɔːr/", emoji = "🏪", accentColor = Color(0xFFD81B60), shadowColor = Color(0xFFAD1457), words = listOf(
        RControlledWord(word = "more",  rTeam = "ore"), RControlledWord(word = "store", rTeam = "ore"),
        RControlledWord(word = "score", rTeam = "ore"), RControlledWord(word = "shore", rTeam = "ore"),
        RControlledWord(word = "snore", rTeam = "ore"), RControlledWord(word = "chore", rTeam = "ore"),
    )),
)

// ── Learn UI State ────────────────────────────────────────────────────────────

data class RControlledLearnUiState(
    val selectedGroup: RControlledGroup = rControlledGroups[0],
    val highlightedWordId: String? = null,
    val showWords: Boolean = false
)

// ── Learn ViewModel ───────────────────────────────────────────────────────────

@HiltViewModel
class RControlledLearnViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {

    var uiState by mutableStateOf(RControlledLearnUiState()); private set
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

    fun onGroupTap(group: RControlledGroup) {
        if (group.id == uiState.selectedGroup.id) return
        val session = UUID.randomUUID().toString()
        animSession = session
        audioManager.stop()
        uiState = RControlledLearnUiState(selectedGroup = group)
        showWordsJob?.cancel()
        showWordsJob = viewModelScope.launch {
            delay(150)
            if (animSession == session) uiState = uiState.copy(showWords = true)
        }
    }

    fun onWordTap(word: RControlledWord) {
        val session = UUID.randomUUID().toString()
        animSession = session
        audioManager.stop()
        uiState = uiState.copy(highlightedWordId = word.id)
        audioManager.playPhonicsSound("phonics_word/${word.word}")
        audioManager.onAudioCompleted = {
            if (animSession == session) {
                uiState = uiState.copy(highlightedWordId = null)
            }
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

data class RControlledPracticeQuestion(
    val id: String = UUID.randomUUID().toString(),
    val word: String,
    val rTeam: String,
    val options: List<String>
) {
    val displayWord: String get() = word.replace(rTeam, "__")
}

data class RControlledPracticeUiState(
    val currentIndex: Int = 0,
    val score: Int = 0,
    val selectedAnswer: String? = null,
    val isCorrect: Boolean? = null,
    val isFinished: Boolean = false,
    val shakeWrong: Boolean = false
)

// ── Practice Data ─────────────────────────────────────────────────────────────

val rControlledPracticeQuestions: List<RControlledPracticeQuestion> = listOf(
    RControlledPracticeQuestion(word = "car",  rTeam = "ar", options = listOf("ar", "or", "ur")),
    RControlledPracticeQuestion(word = "star", rTeam = "ar", options = listOf("ar", "ir", "er")),
    RControlledPracticeQuestion(word = "farm", rTeam = "ar", options = listOf("ar", "er", "ir")),
    RControlledPracticeQuestion(word = "fork", rTeam = "or", options = listOf("or", "ar", "ur")),
    RControlledPracticeQuestion(word = "corn", rTeam = "or", options = listOf("or", "ar", "er")),
    RControlledPracticeQuestion(word = "horn", rTeam = "or", options = listOf("or", "ur", "ir")),
    RControlledPracticeQuestion(word = "her",  rTeam = "er", options = listOf("er", "ar", "or")),
    RControlledPracticeQuestion(word = "fern", rTeam = "er", options = listOf("er", "or", "ur")),
    RControlledPracticeQuestion(word = "verb", rTeam = "er", options = listOf("er", "ir", "or")),
    RControlledPracticeQuestion(word = "bird", rTeam = "ir", options = listOf("ir", "ar", "or")),
    RControlledPracticeQuestion(word = "girl", rTeam = "ir", options = listOf("ir", "er", "or")),
    RControlledPracticeQuestion(word = "stir", rTeam = "ir", options = listOf("ir", "ar", "or")),
    RControlledPracticeQuestion(word = "burn", rTeam = "ur", options = listOf("ur", "or", "ir")),
    RControlledPracticeQuestion(word = "turn", rTeam = "ur", options = listOf("ur", "ar", "ir")),
    RControlledPracticeQuestion(word = "curl", rTeam = "ur", options = listOf("ur", "er", "ar")),
    RControlledPracticeQuestion(word = "hair", rTeam = "air", options = listOf("air", "ear", "ore")),
    RControlledPracticeQuestion(word = "hear", rTeam = "ear", options = listOf("ear", "air", "ore")),
    RControlledPracticeQuestion(word = "more", rTeam = "ore", options = listOf("ore", "air", "ear")),
)

// ── Practice ViewModel ────────────────────────────────────────────────────────

@HiltViewModel
class RControlledPracticeViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {

    private val questions = rControlledPracticeQuestions.shuffled()
    var uiState by mutableStateOf(RControlledPracticeUiState()); private set

    val totalQuestions: Int get() = questions.size
    val currentQuestion: RControlledPracticeQuestion? get() = questions.getOrNull(uiState.currentIndex)

    private var shakeJob: Job? = null

    fun onAnswerTap(answer: String) {
        val q = currentQuestion ?: return
        if (uiState.selectedAnswer != null) return
        val correct = answer == q.rTeam
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

    fun restart() { audioManager.stop(); uiState = RControlledPracticeUiState() }
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

data class RControlledListenEntry(
    val id: String = UUID.randomUUID().toString(),
    val word: String,
    val rTeam: String,
    val pre: String,
    val postTeam: String,
    val accentColor: Color,
    val shadowColor: Color,
    val sound: String
) {
    val vowelChar: String get() = rTeam.first().toString()
    val chars: List<String> get() = (pre + rTeam + postTeam).map { it.toString() }
    val pairIdx1: Int get() = pre.length          // vowel index
    val pairIdx2: Int get() = pre.length + 1      // r index
    val showArc: Boolean get() = pairIdx2 - pairIdx1 > 1

    fun charSegment(idx: Int): Int = when {
        idx < pre.length      -> 0
        idx <= pre.length + 1 -> 1
        else                  -> 2
    }
    fun hasSegment(seg: Int): Boolean = when (seg) {
        0    -> pre.isNotEmpty()
        1    -> true
        2    -> postTeam.isNotEmpty()
        else -> false
    }
    fun segmentAudio(seg: Int): String = when (seg) {
        0    -> if (pre.isEmpty()) "" else "phonics_letter/sound_${pre[0]}"
        1    -> "phonics_word/$rTeam"
        2    -> if (postTeam.isEmpty()) "" else "phonics_letter/sound_${postTeam[0]}"
        else -> "phonics_word/$word"
    }
    fun segmentLabel(seg: Int): String = when (seg) {
        0    -> if (pre.isEmpty()) "—" else pre
        1    -> rTeam
        2    -> if (postTeam.isEmpty()) "—" else postTeam
        else -> ""
    }
}

val rControlledListenEntries: List<RControlledListenEntry> = rControlledGroups.flatMap { group ->
    group.words.map { word ->
        RControlledListenEntry(
            word = word.word, rTeam = word.rTeam,
            pre = word.pre, postTeam = word.postTeam,
            accentColor = group.accentColor, shadowColor = group.shadowColor,
            sound = group.sound
        )
    }
}

// ── Listen UI State ───────────────────────────────────────────────────────────

data class RControlledListenUiState(
    val segmentIndex: Int = -1,
    val isAutoMode: Boolean = false,
    val isPlaying: Boolean = false,
    val wordDone: Boolean = false,
    val playedSegments: Set<Int> = emptySet(),
    val isGoingForward: Boolean = true
)

// ── Listen ViewModel ──────────────────────────────────────────────────────────

@HiltViewModel
class RControlledListenViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {

    var wordIndex by mutableStateOf(0); private set
    var uiState   by mutableStateOf(RControlledListenUiState()); private set

    val totalWords: Int get() = rControlledListenEntries.size
    val currentEntry: RControlledListenEntry? get() = rControlledListenEntries.getOrNull(wordIndex)

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
        uiState = RControlledListenUiState(isAutoMode = uiState.isAutoMode, isGoingForward = true)
    }

    fun prevWord() {
        if (wordIndex <= 0) return
        pauseAutoPlay(); wordIndex -= 1
        uiState = RControlledListenUiState(isAutoMode = uiState.isAutoMode, isGoingForward = false)
    }

    fun toggleMode() {
        pauseAutoPlay()
        uiState = RControlledListenUiState(isAutoMode = !uiState.isAutoMode, isGoingForward = uiState.isGoingForward)
    }

    fun stop() { autoPlayJob?.cancel(); audioManager.stop() }
    override fun onCleared() { super.onCleared(); stop() }
}
