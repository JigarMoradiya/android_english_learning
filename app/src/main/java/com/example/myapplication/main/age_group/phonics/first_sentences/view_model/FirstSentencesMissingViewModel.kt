package com.example.myapplication.main.age_group.phonics.first_sentences.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import javax.inject.Inject

/**
 * MILESTONE · Read Your First Sentences — screen 6, "Missing Word".
 *
 * One word is taken out of the line and three choices sit below it. The two wrong ones
 * are NEAR-MISSES from the database — sat/sit/sun, cap/cup/cop — so the answer cannot be
 * picked by word shape or first letter alone. The child has to read the line and decide
 * what fits, which is comprehension and decoding at once.
 *
 * Keep identical to iOS FirstSentencesMissingViewModel.swift.
 */
data class FirstSentencesMissingUiState(
    val round: Int = 0,
    val choices: List<String> = emptyList(),
    val chosen: String? = null,
    val isCorrect: Boolean? = null,
    val score: Int = 0,
    val finished: Boolean = false,
)

@HiltViewModel
class FirstSentencesMissingViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager,
    private val levelProgressRepo: PhonicsLevelProgressRepository,
    private val phonicsSessions: PhonicsSessionRecorder,
) : ViewModel() {

    var uiState by mutableStateOf(FirstSentencesMissingUiState()); private set

    private var order: List<FirstSentence> = emptyList()
    private var sessionStartMs = System.currentTimeMillis()
    private val correctItems = mutableListOf<String>()
    private val wrongItems = mutableListOf<String>()
    private var playJob: Job? = null
    private var advanceJob: Job? = null

    /** true once this run has been filed, so leaving does not file it twice */
    private var recorded = false

    val total: Int get() = order.size
    val sentence: FirstSentence? get() = order.getOrNull(uiState.round)

    /**
     * the round drawn on screen. Once the run is over this stays on the LAST one, so the
     * completion popup has the finished question sitting behind it rather than a blank
     * screen — the app shows its popup over the content, not instead of it.
     */
    val displaySentence: FirstSentence?
        get() = order.getOrNull(minOf(uiState.round, maxOf(0, order.size - 1)))

    /** the line with the missing word replaced by a blank, as display words */
    val blanked: List<String>
        get() {
            val s = displaySentence ?: return emptyList()
            var replaced = false
            return s.words.map { word ->
                if (!replaced && FirstSentence.key(word) == s.missing) {
                    replaced = true
                    "___"
                } else word
            }
        }

    init { start() }

    fun start() {
        // ten rounds, as every other phonics practice in the app — the bank is thirty-five
        // lines so a replay draws a genuinely different set rather than the same run again
        order = allFirstSentences.shuffled().take(10)
        sessionStartMs = System.currentTimeMillis()
        correctItems.clear()
        wrongItems.clear()
        recorded = false
        uiState = FirstSentencesMissingUiState()
        buildChoices()
    }

    private fun buildChoices() {
        val s = sentence
        if (s == null) {
            uiState = uiState.copy(finished = true)
            finish()
            return
        }
        uiState = uiState.copy(
            choices = (listOf(s.missing) + s.decoys).shuffled(),
            chosen = null,
            isCorrect = null,
        )
    }

    fun playSentence() {
        val s = displaySentence ?: return
        playJob?.cancel()
        playJob = viewModelScope.launch {
            playFirstSentence(audioManager, s) { }
        }
    }

    /** every choice is a word — tapping one hears it before committing */
    private fun previewChoice(word: String) {
        if (!audioManager.audioExists(phonicsWordPath(word))) return
        audioManager.stop()
        audioManager.playPhonicsSound(phonicsWordPath(word))
    }

    fun choose(word: String) {
        val s = sentence ?: return
        if (uiState.chosen != null) return

        val right = word == s.missing
        uiState = uiState.copy(chosen = word, isCorrect = right)
        previewChoice(word)
        if (right) {
            uiState = uiState.copy(score = uiState.score + 1)
            correctItems.add(s.missing)
            AudioPlayerManager.playSoundCorrectAnswer()
        } else {
            wrongItems.add(s.missing)
            AudioPlayerManager.playSoundWrongAnswer()
        }

        advanceJob?.cancel()
        advanceJob = viewModelScope.launch {
            delay(if (right) 1000L else 1500L)
            uiState = uiState.copy(round = uiState.round + 1)
            buildChoices()
        }
    }

    /** the run is over — marks the milestone done and files the parent-report row */
    private fun finish() {
        recorded = true
        levelProgressRepo.recordPractice(
            level = PhonicsListenLevelKey.firstSentences, score = uiState.score, total = order.size,
        )
        phonicsSessions.recordPractice(
            level = PhonicsListenLevelKey.firstSentences,
            score = uiState.score,
            total = order.size,
            durationSeconds = ((System.currentTimeMillis() - sessionStartMs) / 1000).toInt(),
            wrongItems = wrongItems.toList(),
            correctItems = correctItems.toList(),
        )
    }

    fun stop() {
        playJob?.cancel()
        advanceJob?.cancel()
        audioManager.stop()
        // left halfway — an unfinished run is not a score, but it is still time spent,
        // so it goes in as LEARN rather than vanishing from the report
        if (recorded) return
        recorded = true
        phonicsSessions.recordLearning(
            level = PhonicsListenLevelKey.firstSentences,
            mode = "LEARN",
            durationSeconds = ((System.currentTimeMillis() - sessionStartMs) / 1000).toInt(),
        )
    }

    override fun onCleared() {
        super.onCleared()
        stop()
    }
}
