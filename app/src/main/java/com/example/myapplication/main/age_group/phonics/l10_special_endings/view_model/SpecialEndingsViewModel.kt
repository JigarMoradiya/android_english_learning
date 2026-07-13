package com.example.myapplication.main.age_group.phonics.l10_special_endings.view_model

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

enum class SpecialEndingGroup(val label: String, val emoji: String,
                               val color: Color, val shadowColor: Color) {
    TCH("-TCH", "🎣", Color(0xFFE64A19), Color(0xFFBF360C)),
    DGE("-DGE", "🌉", Color(0xFF7B1FA2), Color(0xFF4A148C)),
    NK("-NK",   "💧", Color(0xFF00838F), Color(0xFF006064))
}

data class SpecialEndingWord(
    val id: String = UUID.randomUUID().toString(),
    val word: String,
    val endingLength: Int
) {
    val basePart: String   get() = word.dropLast(endingLength)
    val endingPart: String get() = word.takeLast(endingLength)
}

data class SpecialEndingEntry(
    val id: String = UUID.randomUUID().toString(),
    val ending: String,
    val displayEnding: String,
    val phonetic: String,
    val soundHint: String,
    val spellingRule: String,
    val group: SpecialEndingGroup,
    val words: List<SpecialEndingWord>
)

data class SpecialEndingPracticeQuestion(
    val id: String = UUID.randomUUID().toString(),
    val word: String,
    val endingLength: Int,
    val correctEnding: String,
    val options: List<String>,
    val imageName: String
) {
    val displayWord: String get() = "${word.dropLast(endingLength)}___"
}

// ── UI States ──────────────────────────────────────────────────────────────────

data class SpecialEndingLearnUiState(
    val selectedEntry: SpecialEndingEntry? = null,
    val highlightedWordId: String? = null,
    val showWords: Boolean = false
)

data class SpecialEndingPracticeUiState(
    val currentIndex: Int = 0,
    val score: Int = 0,
    val selectedAnswer: String? = null,
    val isCorrect: Boolean? = null,
    val isFinished: Boolean = false,
    val shakeWrong: Boolean = false
)

// ── Data ───────────────────────────────────────────────────────────────────────

val specialEndingsData: List<SpecialEndingEntry> = listOf(
    SpecialEndingEntry(
        ending="tch", displayEnding="-tch", phonetic="/tʃ/",
        soundHint="Same /tʃ/ as 'ch' — but after a short vowel, always spell it -tch",
        spellingRule="Short vowel + -tch  (not -ch) — ca-tch, ma-tch, wi-tch",
        group=SpecialEndingGroup.TCH, words=listOf(
            SpecialEndingWord(word="catch",  endingLength=3),
            SpecialEndingWord(word="match",  endingLength=3),
            SpecialEndingWord(word="patch",  endingLength=3),
            SpecialEndingWord(word="latch",  endingLength=3),
            SpecialEndingWord(word="hatch",  endingLength=3),
            SpecialEndingWord(word="batch",  endingLength=3),
            SpecialEndingWord(word="fetch",  endingLength=3),
            SpecialEndingWord(word="sketch", endingLength=3),
            SpecialEndingWord(word="witch",  endingLength=3),
            SpecialEndingWord(word="ditch",  endingLength=3),
            SpecialEndingWord(word="notch",  endingLength=3),
            SpecialEndingWord(word="watch",  endingLength=3),
            SpecialEndingWord(word="hutch",  endingLength=3))),
    SpecialEndingEntry(
        ending="dge", displayEnding="-dge", phonetic="/dʒ/",
        soundHint="The /j/ sound at word end — after a short vowel, write -dge not -j",
        spellingRule="Short vowel + -dge  (the /j/ sound) — ba-dge, bri-dge, fu-dge",
        group=SpecialEndingGroup.DGE, words=listOf(
            SpecialEndingWord(word="badge",  endingLength=3),
            SpecialEndingWord(word="edge",   endingLength=3),
            SpecialEndingWord(word="hedge",  endingLength=3),
            SpecialEndingWord(word="wedge",  endingLength=3),
            SpecialEndingWord(word="bridge", endingLength=3),
            SpecialEndingWord(word="ridge",  endingLength=3),
            SpecialEndingWord(word="fridge", endingLength=3),
            SpecialEndingWord(word="lodge",  endingLength=3),
            SpecialEndingWord(word="dodge",  endingLength=3),
            SpecialEndingWord(word="judge",  endingLength=3),
            SpecialEndingWord(word="fudge",  endingLength=3),
            SpecialEndingWord(word="budge",  endingLength=3),
            SpecialEndingWord(word="smudge", endingLength=3))),
    SpecialEndingEntry(
        ending="nk", displayEnding="-nk", phonetic="/ŋk/",
        soundHint="Nasal /ŋ/ hum + hard /k/ click — feel the buzz in your nose first!",
        spellingRule="N + K = nasal buzz + click  — si-nk, ta-nk, ho-nk",
        group=SpecialEndingGroup.NK, words=listOf(
            SpecialEndingWord(word="sink", endingLength=2),
            SpecialEndingWord(word="link", endingLength=2),
            SpecialEndingWord(word="pink", endingLength=2),
            SpecialEndingWord(word="rink", endingLength=2),
            SpecialEndingWord(word="wink", endingLength=2),
            SpecialEndingWord(word="bank", endingLength=2),
            SpecialEndingWord(word="rank", endingLength=2),
            SpecialEndingWord(word="tank", endingLength=2),
            SpecialEndingWord(word="honk", endingLength=2),
            SpecialEndingWord(word="bonk", endingLength=2),
            SpecialEndingWord(word="bunk", endingLength=2),
            SpecialEndingWord(word="dunk", endingLength=2),
            SpecialEndingWord(word="funk", endingLength=2),
            SpecialEndingWord(word="sunk", endingLength=2)))
)

val specialEndingsPracticeQuestions: List<SpecialEndingPracticeQuestion> = listOf(
    SpecialEndingPracticeQuestion(word="catch",  endingLength=3, correctEnding="tch", options=listOf("tch","dge","nk"),  imageName="catch"),
    SpecialEndingPracticeQuestion(word="match",  endingLength=3, correctEnding="tch", options=listOf("tch","nk","dge"),  imageName="match"),
    SpecialEndingPracticeQuestion(word="watch",  endingLength=3, correctEnding="tch", options=listOf("dge","tch","nk"),  imageName="watch"),
    SpecialEndingPracticeQuestion(word="witch",  endingLength=3, correctEnding="tch", options=listOf("tch","nk","dge"),  imageName="witch"),
    SpecialEndingPracticeQuestion(word="patch",  endingLength=3, correctEnding="tch", options=listOf("tch","dge","nk"),  imageName="patch"),
    SpecialEndingPracticeQuestion(word="badge",  endingLength=3, correctEnding="dge", options=listOf("dge","tch","nk"),  imageName="badge"),
    SpecialEndingPracticeQuestion(word="bridge", endingLength=3, correctEnding="dge", options=listOf("nk","dge","tch"),  imageName="bridge"),
    SpecialEndingPracticeQuestion(word="fudge",  endingLength=3, correctEnding="dge", options=listOf("dge","nk","tch"),  imageName="fudge"),
    SpecialEndingPracticeQuestion(word="judge",  endingLength=3, correctEnding="dge", options=listOf("tch","dge","nk"),  imageName="judge"),
    SpecialEndingPracticeQuestion(word="fridge", endingLength=3, correctEnding="dge", options=listOf("nk","tch","dge"),  imageName="fridge"),
    SpecialEndingPracticeQuestion(word="sink",   endingLength=2, correctEnding="nk",  options=listOf("nk","tch","dge"),  imageName="sink"),
    SpecialEndingPracticeQuestion(word="bank",   endingLength=2, correctEnding="nk",  options=listOf("nk","dge","tch"),  imageName="bank"),
    SpecialEndingPracticeQuestion(word="tank",   endingLength=2, correctEnding="nk",  options=listOf("dge","nk","tch"),  imageName="tank"),
    SpecialEndingPracticeQuestion(word="pink",   endingLength=2, correctEnding="nk",  options=listOf("tch","nk","dge"),  imageName="pink"),
    SpecialEndingPracticeQuestion(word="honk",   endingLength=2, correctEnding="nk",  options=listOf("nk","dge","tch"),  imageName="honk")
)

// ── Learn ViewModel ────────────────────────────────────────────────────────────

@HiltViewModel
class SpecialEndingsLearnViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {

    var uiState by mutableStateOf(SpecialEndingLearnUiState()); private set
    private var animSession = UUID.randomUUID().toString()
    private var showWordsJob: Job? = null

    fun onEntryTap(entry: SpecialEndingEntry) {
        val session = UUID.randomUUID().toString()
        animSession = session
        audioManager.stop()
        uiState = uiState.copy(highlightedWordId = null, showWords = false, selectedEntry = entry)
        showWordsJob?.cancel()
        showWordsJob = viewModelScope.launch {
            delay(150)
            if (animSession == session) uiState = uiState.copy(showWords = true)
        }
    }

    fun onWordTap(word: SpecialEndingWord) {
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

    fun onEndingSoundTap(entry: SpecialEndingEntry) {
        audioManager.playPhonicsSound("phonics_word/${entry.ending}")
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
class SpecialEndingsPracticeViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager,
    private val levelProgressRepo: PhonicsLevelProgressRepository,
    private val phonicsSessions: PhonicsSessionRecorder
) : ViewModel() {

    private val questions = specialEndingsPracticeQuestions.shuffled()
    var uiState by mutableStateOf(SpecialEndingPracticeUiState()); private set

    // Parent-report session tracking
    private var sessionStartMs = System.currentTimeMillis()
    private val wrongWords = mutableListOf<String>()
    private val correctWords = mutableListOf<String>()

    val totalQuestions: Int get() = questions.size
    val currentQuestion: SpecialEndingPracticeQuestion? get() =
        questions.getOrNull(uiState.currentIndex)

    private var shakeJob: Job? = null

    fun onAnswerTap(answer: String) {
        val q = currentQuestion ?: return
        if (uiState.selectedAnswer != null) return
        val correct = answer == q.correctEnding
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
        sessionStartMs = System.currentTimeMillis()
        wrongWords.clear()
        correctWords.clear()
        audioManager.stop()
        uiState = SpecialEndingPracticeUiState()
    }

    fun stop() { audioManager.stop() }
    override fun onCleared() { super.onCleared(); stop() }

    private fun advance() {
        val next = uiState.currentIndex + 1
        uiState = if (next >= questions.size) {
            levelProgressRepo.recordPractice(level = PhonicsListenLevelKey.specialEndings, score = uiState.score, total = questions.size)
            phonicsSessions.recordPractice(PhonicsListenLevelKey.specialEndings, uiState.score, questions.size, ((System.currentTimeMillis() - sessionStartMs) / 1000).toInt(), wrongWords.toList(), correctWords.toList())
            uiState.copy(isFinished = true)
        } else {
            uiState.copy(currentIndex = next, selectedAnswer = null, isCorrect = null, shakeWrong = false)
        }
    }
}
