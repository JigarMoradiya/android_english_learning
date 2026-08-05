package com.example.myapplication.main.age_group.phonics.first_sentences.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.progress.PhonicsSessionRecorder
import com.example.myapplication.main.age_group.phonics.listen.view_model.PhonicsListenLevelKey
import com.example.myapplication.utilities.AudioPhonicsManager
import com.example.myapplication.utils.AudioPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * MILESTONE · Read Your First Sentences — screen 4, "Read It Yourself".
 *
 * The child goes FIRST. Level 4 ends with "This one's yours — sound it out" and a real
 * two-second silence; this is the same beat for a whole line. Nothing reads itself until
 * the child asks it to, because a page that reads aloud the moment it opens teaches
 * listening, not reading.
 *
 * Keep identical to iOS FirstSentencesReadViewModel.swift.
 */
data class FirstSentencesReadUiState(
    val groupIndex: Int = 0,
    val index: Int = 0,
    /** the child has asked to hear it — only then does the line read */
    val revealed: Boolean = false,
    val readingIndex: Int? = null,
    /** keyed "group-sentence", so the ticks survive hopping between vowels */
    val heard: Set<String> = emptySet(),
)

@HiltViewModel
class FirstSentencesReadViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager,
    private val phonicsSessions: PhonicsSessionRecorder,
) : ViewModel() {

    var uiState by mutableStateOf(FirstSentencesReadUiState()); private set

    private var playJob: Job? = null

    /** time on this screen counts towards the parent report as LEARN */
    private val sessionStartMs = System.currentTimeMillis()
    private var recorded = false

    /**
     * The lines are browsed one VOWEL at a time, the way Build a Sentence is. Thirty-five
     * lines behind a single pair of arrows is a corridor; five doors of seven is a choice,
     * and it lets a child go back to the vowel they are shaky on.
     */
    val group: FirstSentenceGroup get() = firstSentenceGroups[uiState.groupIndex]
    val sentence: FirstSentence get() = group.sentences[uiState.index]
    val total: Int get() = group.sentences.size
    val isLast: Boolean get() = uiState.index == total - 1

    fun isHeard(i: Int): Boolean = uiState.heard.contains("${uiState.groupIndex}-$i")

    fun selectGroup(index: Int) {
        if (index == uiState.groupIndex) return
        AudioPlayerManager.playSoundMenuClick()
        uiState = uiState.copy(groupIndex = index)
        go(0)
    }

    fun onWordTap(i: Int) {
        val key = FirstSentence.key(sentence.words[i])
        if (!audioManager.audioExists(phonicsWordPath(key))) return
        audioManager.stop()
        audioManager.playPhonicsSound(phonicsWordPath(key))
    }

    /** "Hear it" — the child has read it themselves first */
    fun reveal() {
        playJob?.cancel()
        uiState = uiState.copy(
            revealed = true,
            heard = uiState.heard + "${uiState.groupIndex}-${uiState.index}",
        )
        val target = sentence
        playJob = viewModelScope.launch {
            playFirstSentence(audioManager, target) { i ->
                uiState = uiState.copy(readingIndex = i)
            }
        }
    }

    fun next() {
        if (isLast) return
        AudioPlayerManager.playSoundMenuClick()
        go(uiState.index + 1)
    }

    fun previous() {
        if (uiState.index == 0) return
        AudioPlayerManager.playSoundMenuClick()
        go(uiState.index - 1)
    }

    private fun go(index: Int) {
        playJob?.cancel()
        audioManager.stop()
        uiState = uiState.copy(index = index, revealed = false, readingIndex = null)
    }

    fun stop() {
        playJob?.cancel()
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
