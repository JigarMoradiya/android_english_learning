package com.example.myapplication.main.age_group.phonics.first_sentences.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.progress.PhonicsSessionRecorder
import com.example.myapplication.main.age_group.phonics.listen.view_model.PhonicsListenLevelKey
import com.example.myapplication.utilities.AudioPhonicsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * MILESTONE · Read Your First Sentences — screen 2, "Helper Words".
 *
 * The eight words a sentence cannot do without and phonics cannot yet reach. The teaching
 * point is honesty: these are NOT sounded out. A child who tries to decode "the" gets a
 * wrong answer and learns to distrust the method, so the screen says plainly that these
 * are learned by sight — the same framing Sight Words (Level 28) and Heart Words use.
 *
 * Keep identical to iOS FirstSentencesHelpersViewModel.swift.
 */
data class FirstSentencesHelpersUiState(
    val selected: String? = null,
    /** the word currently glowing while its audio plays */
    val speaking: String? = null,
    /** words the child has tapped at least once — the screen fills in as they go */
    val heard: Set<String> = emptySet(),
)

@HiltViewModel
class FirstSentencesHelpersViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager,
    private val phonicsSessions: PhonicsSessionRecorder,
) : ViewModel() {

    var uiState by mutableStateOf(FirstSentencesHelpersUiState()); private set

    /** cancels a stale glow if the child taps a second word before the first finishes */
    private var glowJob: Job? = null

    /**
     * time on this screen counts towards the parent report as LEARN, the way Reading
     * Ladder and Compare & Choose do
     */
    private val sessionStartMs = System.currentTimeMillis()
    private var recorded = false

    /**
     * Tap-to-play is the rule, so a word with no recording must not be shown as a card
     * that does nothing. It reappears by itself the moment the file is added.
     */
    fun hasAudio(word: String): Boolean = audioManager.audioExists(phonicsWordPath(word))

    /** only the words that can actually speak can be "heard" */
    private val shown: List<FirstSentenceHelper> get() = firstSentenceHelpers.filter { hasAudio(it.word) }

    val allHeard: Boolean get() = shown.isNotEmpty() && uiState.heard.size == shown.size

    fun onWordTap(helper: FirstSentenceHelper) {
        if (!hasAudio(helper.word)) return
        audioManager.stop()

        uiState = uiState.copy(
            selected = helper.word,
            speaking = helper.word,
            heard = uiState.heard + helper.word,
        )

        audioManager.playPhonicsSound(phonicsWordPath(helper.word))

        glowJob?.cancel()
        glowJob = viewModelScope.launch {
            delay(700)
            uiState = uiState.copy(speaking = null)
        }
    }

    fun stop() {
        glowJob?.cancel()
        audioManager.stop()
        recordLearnTimeIfNeeded()
    }

    private fun recordLearnTimeIfNeeded() {
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
