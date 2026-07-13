package com.example.myapplication.main.age_group.phonics.l12_magic_e.view_model

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
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.resume

// ── Models ────────────────────────────────────────────────────────────────────

data class MagicEWord(
    val id: String = UUID.randomUUID().toString(),
    val base: String,
    val magic: String
) {
    val vowelIndex: Int   get() = base.length - 2
    val preVowel: String  get() = base.substring(0, vowelIndex)
    val vowelChar: String get() = base.substring(vowelIndex, vowelIndex + 1)
    val postVowel: String get() = base.substring(vowelIndex + 1)
}

data class MagicEGroup(
    val id: String = UUID.randomUUID().toString(),
    val vowel: Char,
    val longLabel: String,
    val emoji: String,
    val accentColor: Color,
    val shadowColor: Color,
    val words: List<MagicEWord>
)

// ── Data ───────────────────────────────────────────────────────────────────────

val magicEGroups: List<MagicEGroup> = listOf(
    MagicEGroup(
        vowel = 'a', longLabel = "ā", emoji = "🎩",
        accentColor = Color(0xFFFF7043), shadowColor = Color(0xFFBF360C),
        words = listOf(
            MagicEWord(base = "cap", magic = "cape"), MagicEWord(base = "hat", magic = "hate"),
            MagicEWord(base = "mad", magic = "made"), MagicEWord(base = "tap", magic = "tape"),
            MagicEWord(base = "man", magic = "mane"), MagicEWord(base = "rat", magic = "rate"),
            MagicEWord(base = "fat", magic = "fate"), MagicEWord(base = "mat", magic = "mate"),
            MagicEWord(base = "pan", magic = "pane"), MagicEWord(base = "van", magic = "vane"),
            MagicEWord(base = "can", magic = "cane"), MagicEWord(base = "gap", magic = "gape"),
        )
    ),
    MagicEGroup(
        vowel = 'i', longLabel = "ī", emoji = "🪄",
        accentColor = Color(0xFFAB47BC), shadowColor = Color(0xFF6A1B9A),
        words = listOf(
            MagicEWord(base = "bit", magic = "bite"), MagicEWord(base = "hid", magic = "hide"),
            MagicEWord(base = "dim", magic = "dime"), MagicEWord(base = "pin", magic = "pine"),
            MagicEWord(base = "rip", magic = "ripe"), MagicEWord(base = "fin", magic = "fine"),
            MagicEWord(base = "win", magic = "wine"), MagicEWord(base = "kit", magic = "kite"),
            MagicEWord(base = "rid", magic = "ride"), MagicEWord(base = "din", magic = "dine"),
        )
    ),
    MagicEGroup(
        vowel = 'o', longLabel = "ō", emoji = "🌊",
        accentColor = Color(0xFF00ACC1), shadowColor = Color(0xFF00838F),
        words = listOf(
            MagicEWord(base = "hop", magic = "hope"), MagicEWord(base = "not", magic = "note"),
            MagicEWord(base = "cod", magic = "code"), MagicEWord(base = "rob", magic = "robe"),
            MagicEWord(base = "rod", magic = "rode"), MagicEWord(base = "con", magic = "cone"),
            MagicEWord(base = "ton", magic = "tone"), MagicEWord(base = "cop", magic = "cope"),
            MagicEWord(base = "nod", magic = "node"), MagicEWord(base = "mop", magic = "mope"),
        )
    ),
    MagicEGroup(
        vowel = 'u', longLabel = "ū", emoji = "✨",
        accentColor = Color(0xFF66BB6A), shadowColor = Color(0xFF388E3C),
        words = listOf(
            MagicEWord(base = "cub", magic = "cube"), MagicEWord(base = "cut", magic = "cute"),
            MagicEWord(base = "tub", magic = "tube"), MagicEWord(base = "dun", magic = "dune"),
            MagicEWord(base = "tun", magic = "tune"), MagicEWord(base = "dud", magic = "dude"),
        )
    ),
    // e_e is the rarest magic-e — only a handful of real words exist
    MagicEGroup(
        vowel = 'e', longLabel = "ē", emoji = "🐣",
        accentColor = Color(0xFFEC407A), shadowColor = Color(0xFFAD1457),
        words = listOf(
            MagicEWord(base = "pet",  magic = "Pete"),
            MagicEWord(base = "them", magic = "theme"),
        )
    ),
)

// ── UI State ──────────────────────────────────────────────────────────────────

data class MagicELearnUiState(
    val selectedGroup: MagicEGroup = magicEGroups[0],
    val highlightedWordId: String? = null,
    val showWords: Boolean = false
)

// ── ViewModel ─────────────────────────────────────────────────────────────────

@HiltViewModel
class MagicELearnViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {

    var uiState by mutableStateOf(MagicELearnUiState()); private set
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

    fun onGroupTap(group: MagicEGroup) {
        if (group.id == uiState.selectedGroup.id) return
        val session = UUID.randomUUID().toString()
        animSession = session
        audioManager.stop()
        uiState = MagicELearnUiState(selectedGroup = group)
        showWordsJob?.cancel()
        showWordsJob = viewModelScope.launch {
            delay(150)
            if (animSession == session) uiState = uiState.copy(showWords = true)
        }
    }

    fun onWordTap(word: MagicEWord) {
        val session = UUID.randomUUID().toString()
        animSession = session
        audioManager.stop()
        uiState = uiState.copy(highlightedWordId = word.id)
        audioManager.playPhonicsSound("phonics_word/${word.base}")
        audioManager.onAudioCompleted = {
            if (animSession == session) {
                viewModelScope.launch {
                    delay(200)
                    if (animSession == session) {
                        audioManager.playPhonicsSound("phonics_word/${word.magic}")
                        audioManager.onAudioCompleted = {
                            if (animSession == session)
                                uiState = uiState.copy(highlightedWordId = null)
                        }
                    }
                }
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

data class MagicEPracticeQuestion(
    val id: String = UUID.randomUUID().toString(),
    val baseWord: String,
    val correctMagic: String,
    val options: List<String>
)

data class MagicEPracticeUiState(
    val currentIndex: Int = 0,
    val score: Int = 0,
    val selectedAnswer: String? = null,
    val isCorrect: Boolean? = null,
    val isFinished: Boolean = false,
    val shakeWrong: Boolean = false
)

// ── Practice Data ─────────────────────────────────────────────────────────────

val magicEPracticeQuestions: List<MagicEPracticeQuestion> = listOf(
    MagicEPracticeQuestion(baseWord = "cap",  correctMagic = "cape",  options = listOf("cape", "hope", "bite")),
    MagicEPracticeQuestion(baseWord = "hat",  correctMagic = "hate",  options = listOf("hate", "note", "fine")),
    MagicEPracticeQuestion(baseWord = "mad",  correctMagic = "made",  options = listOf("made", "dude", "cone")),
    MagicEPracticeQuestion(baseWord = "tap",  correctMagic = "tape",  options = listOf("tape", "pine", "robe")),
    MagicEPracticeQuestion(baseWord = "man",  correctMagic = "mane",  options = listOf("mane", "tune", "hide")),
    MagicEPracticeQuestion(baseWord = "bit",  correctMagic = "bite",  options = listOf("bite", "cape", "note")),
    MagicEPracticeQuestion(baseWord = "hid",  correctMagic = "hide",  options = listOf("hide", "cane", "cube")),
    MagicEPracticeQuestion(baseWord = "dim",  correctMagic = "dime",  options = listOf("dime", "hope", "tape")),
    MagicEPracticeQuestion(baseWord = "pin",  correctMagic = "pine",  options = listOf("pine", "cape", "dude")),
    MagicEPracticeQuestion(baseWord = "hop",  correctMagic = "hope",  options = listOf("hope", "bite", "cane")),
    MagicEPracticeQuestion(baseWord = "not",  correctMagic = "note",  options = listOf("note", "pine", "made")),
    MagicEPracticeQuestion(baseWord = "rod",  correctMagic = "rode",  options = listOf("rode", "dime", "tape")),
    MagicEPracticeQuestion(baseWord = "cub",  correctMagic = "cube",  options = listOf("cube", "hope", "bite")),
    MagicEPracticeQuestion(baseWord = "cut",  correctMagic = "cute",  options = listOf("cute", "cape", "ride")),
    MagicEPracticeQuestion(baseWord = "tub",  correctMagic = "tube",  options = listOf("tube", "pine", "made")),
    MagicEPracticeQuestion(baseWord = "pet",  correctMagic = "Pete",  options = listOf("Pete", "tape", "bite")),
    MagicEPracticeQuestion(baseWord = "them", correctMagic = "theme", options = listOf("theme", "tune", "note")),
)

// ── Practice ViewModel ────────────────────────────────────────────────────────

@HiltViewModel
class MagicEPracticeViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager,
    private val levelProgressRepo: PhonicsLevelProgressRepository,
    private val phonicsSessions: PhonicsSessionRecorder
) : ViewModel() {

    private val questions = magicEPracticeQuestions.shuffled()
    var uiState by mutableStateOf(MagicEPracticeUiState()); private set

    // Parent-report session tracking
    private var sessionStartMs = System.currentTimeMillis()
    private val wrongWords = mutableListOf<String>()
    private val correctWords = mutableListOf<String>()

    val totalQuestions: Int get() = questions.size
    val currentQuestion: MagicEPracticeQuestion? get() = questions.getOrNull(uiState.currentIndex)

    private var shakeJob: Job? = null

    fun onAnswerTap(answer: String) {
        val q = currentQuestion ?: return
        if (uiState.selectedAnswer != null) return
        val correct = answer == q.correctMagic
        uiState = uiState.copy(selectedAnswer = answer, isCorrect = correct)
        if (correct) correctWords.add(q.correctMagic) else wrongWords.add(q.correctMagic)
        if (correct) {
            uiState = uiState.copy(score = uiState.score + 1)
            audioManager.playPhonicsSound("phonics_word/${q.correctMagic}")
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

    fun restart() { audioManager.stop(); uiState = MagicEPracticeUiState() }
    fun stop() { audioManager.stop() }
    override fun onCleared() { super.onCleared(); stop() }

    private fun advance() {
        val next = uiState.currentIndex + 1
        uiState = if (next >= questions.size) {
            levelProgressRepo.recordPractice(level = PhonicsListenLevelKey.magicE, score = uiState.score, total = questions.size)
            phonicsSessions.recordPractice(PhonicsListenLevelKey.magicE, uiState.score, questions.size, ((System.currentTimeMillis() - sessionStartMs) / 1000).toInt(), wrongWords.toList(), correctWords.toList())
            uiState.copy(isFinished = true)
        } else
            uiState.copy(currentIndex = next, selectedAnswer = null, isCorrect = null, shakeWrong = false)
    }
}

// ── Listen Entry + Data ────────────────────────────────────────────────────────

data class MagicEListenEntry(
    val id: String = UUID.randomUUID().toString(),
    val base: String,
    val magic: String,
    val accentColor: Color,
    val shadowColor: Color,
    val groupVowel: String,
    val longLabel: String
) {
    fun segmentAudio(segIdx: Int): String = when (segIdx) {
        0    -> "phonics_letter/sound_${magic[0]}"
        1    -> "phonics_word/${groupVowel}e"
        2    -> "phonics_letter/sound_${magic[2]}"
        else -> "phonics_word/$magic"
    }
    fun segmentLabel(segIdx: Int): String = when (segIdx) {
        0    -> magic[0].toString()
        1    -> "$groupVowel-e"
        2    -> magic[2].toString()
        else -> ""
    }
}

val magicEListenEntries: List<MagicEListenEntry> = magicEGroups.flatMap { group ->
    group.words.map { word ->
        MagicEListenEntry(
            base = word.base, magic = word.magic,
            accentColor = group.accentColor, shadowColor = group.shadowColor,
            groupVowel = group.vowel.toString(), longLabel = group.longLabel
        )
    }
}

// ── Listen UI State ───────────────────────────────────────────────────────────

data class MagicEListenUiState(
    val segmentIndex: Int = -1,
    val isAutoMode: Boolean = false,
    val isPlaying: Boolean = false,
    val wordDone: Boolean = false,
    val playedSegments: Set<Int> = emptySet(),
    val isGoingForward: Boolean = true
)

// ── Listen ViewModel ──────────────────────────────────────────────────────────

@HiltViewModel
class MagicEListenViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {

    var wordIndex by mutableStateOf(0); private set
    var uiState   by mutableStateOf(MagicEListenUiState()); private set

    val totalWords: Int get() = magicEListenEntries.size
    val currentEntry: MagicEListenEntry? get() = magicEListenEntries.getOrNull(wordIndex)

    private var autoPlayJob: Job? = null

    fun onSegmentTap(segIdx: Int) {
        val entry = currentEntry ?: return
        autoPlayJob?.cancel(); audioManager.stop()
        uiState = uiState.copy(segmentIndex = segIdx, playedSegments = uiState.playedSegments + segIdx, wordDone = false)
        audioManager.playPhonicsSound(entry.segmentAudio(segIdx))
        audioManager.onAudioCompleted = {
            if (uiState.segmentIndex == segIdx && segIdx == 2) {
                audioManager.playPhonicsSound("phonics_word/${entry.magic}")
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
                audioManager.playPhonicsSound("phonics_word/${entry.magic}")
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
        uiState = MagicEListenUiState(isAutoMode = uiState.isAutoMode, isGoingForward = true)
    }

    fun prevWord() {
        if (wordIndex <= 0) return
        pauseAutoPlay(); wordIndex -= 1
        uiState = MagicEListenUiState(isAutoMode = uiState.isAutoMode, isGoingForward = false)
    }

    fun toggleMode() {
        pauseAutoPlay()
        uiState = MagicEListenUiState(isAutoMode = !uiState.isAutoMode, isGoingForward = uiState.isGoingForward)
    }

    fun stop() { autoPlayJob?.cancel(); audioManager.stop() }
    override fun onCleared() { super.onCleared(); stop() }
}
