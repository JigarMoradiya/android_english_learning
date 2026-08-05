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
 * MILESTONE · Read Your First Sentences — screen 5, "Which Picture?".
 *
 * THE SCREEN THAT MAKES THIS READING RATHER THAN DECODING. Every other screen can be
 * passed by sounding words out without any idea what they mean. Here the child is shown a
 * line and three pictures, and only one matches — which cannot be answered by decoding
 * alone. If a child can do this page, they are reading.
 *
 * The two wrong pictures come from OTHER sentences in the same vowel group, so they are
 * near-misses rather than obviously silly, and the answer cannot be found by elimination.
 *
 * Keep identical to iOS FirstSentencesPictureViewModel.swift.
 */
data class FirstSentencesPictureUiState(
    val round: Int = 0,
    val options: List<FirstSentence> = emptyList(),
    val chosen: String? = null,
    val isCorrect: Boolean? = null,
    val score: Int = 0,
    val finished: Boolean = false,
)

@HiltViewModel
class FirstSentencesPictureViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager,
    private val levelProgressRepo: PhonicsLevelProgressRepository,
    private val phonicsSessions: PhonicsSessionRecorder,
) : ViewModel() {

    var uiState by mutableStateOf(FirstSentencesPictureUiState()); private set

    /** one round per sentence, shuffled once so a replay is not the same order */
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

    init { start() }

    fun start() {
        // ten rounds, as every other phonics practice in the app — the bank is thirty-five
        // lines so a replay draws a genuinely different set rather than the same run again
        order = allFirstSentences.shuffled().take(10)
        sessionStartMs = System.currentTimeMillis()
        correctItems.clear()
        wrongItems.clear()
        recorded = false
        uiState = FirstSentencesPictureUiState()
        buildOptions()
    }

    private fun buildOptions() {
        val answer = sentence
        if (answer == null) {
            uiState = uiState.copy(finished = true)
            finish()
            return
        }
        // decoys from the SAME vowel group — a near-miss, not an obviously wrong picture
        val siblings = firstSentenceGroups
            .firstOrNull { g -> g.sentences.any { it.id == answer.id } }
            ?.sentences?.filter { it.id != answer.id }
            .orEmpty()
        val decoys = siblings.shuffled().take(2)
        uiState = uiState.copy(
            options = (listOf(answer) + decoys).shuffled(),
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

    fun choose(option: FirstSentence) {
        val answer = sentence ?: return
        if (uiState.chosen != null) return

        val right = option.id == answer.id
        uiState = uiState.copy(chosen = option.id, isCorrect = right)
        if (right) {
            uiState = uiState.copy(score = uiState.score + 1)
            correctItems.add(answer.text)
            AudioPlayerManager.playSoundCorrectAnswer()
        } else {
            wrongItems.add(answer.text)
            AudioPlayerManager.playSoundWrongAnswer()
        }

        advanceJob?.cancel()
        advanceJob = viewModelScope.launch {
            delay(if (right) 900L else 1400L)
            uiState = uiState.copy(round = uiState.round + 1)
            buildOptions()
        }
    }

    /**
     * the run is over — this is what marks the milestone done, ticks its card, and puts a
     * row in the parent report
     */
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
