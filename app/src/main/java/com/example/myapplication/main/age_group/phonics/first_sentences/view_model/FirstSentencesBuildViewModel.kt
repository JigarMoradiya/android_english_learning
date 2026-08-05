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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * MILESTONE · Read Your First Sentences — screen 3, "Build a Sentence".
 *
 * The Level 4 move, one step up. There, three SOUNDS arrived one at a time and then
 * blended into a word. Here, WORDS arrive one at a time and then read as a line — same
 * shape, so the child recognises what is being asked of them.
 *
 * Sequence:
 *   1. words drop in left to right, 0.45s apart
 *   2. a beat, then the line reads itself with the highlight travelling word to word
 *   3. the picture bounces in — the line meant something
 *
 * Keep identical to iOS FirstSentencesBuildViewModel.swift.
 */
enum class FirstSentenceBuildPhase { idle, wordsIn, reading, done }

data class FirstSentencesBuildUiState(
    val groupIndex: Int = 0,
    val sentenceIndex: Int = 0,
    val phase: FirstSentenceBuildPhase = FirstSentenceBuildPhase.idle,
    /** how many words have dropped in so far */
    val wordsShown: Int = 0,
    /** the word currently being read aloud, or null */
    val readingIndex: Int? = null,
    val showImage: Boolean = false,
)

@HiltViewModel
class FirstSentencesBuildViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager,
    private val phonicsSessions: PhonicsSessionRecorder,
) : ViewModel() {

    var uiState by mutableStateOf(FirstSentencesBuildUiState()); private set

    /**
     * every animation runs in this job, so tapping a new sentence mid-run abandons the old
     * one instead of the two fighting over the screen
     */
    private var playJob: Job? = null

    /** time on this screen counts towards the parent report as LEARN */
    private val sessionStartMs = System.currentTimeMillis()
    private var recorded = false

    val group: FirstSentenceGroup get() = firstSentenceGroups[uiState.groupIndex]
    val sentence: FirstSentence get() = group.sentences[uiState.sentenceIndex]

    fun canPlay(word: String): Boolean = audioManager.audioExists(phonicsWordPath(word))

    // ── Selection ─────────────────────────────────────────────────────────────

    fun selectGroup(index: Int) {
        if (index == uiState.groupIndex) return
        AudioPlayerManager.playSoundMenuClick()
        uiState = uiState.copy(groupIndex = index, sentenceIndex = 0)
        reset()
    }

    fun selectSentence(index: Int) {
        AudioPlayerManager.playSoundMenuClick()
        uiState = uiState.copy(sentenceIndex = index)
        reset()
    }

    /** tapping one word plays just that word — the tap-to-play rule */
    fun onWordTap(index: Int) {
        val key = FirstSentence.key(sentence.words[index])
        if (!canPlay(key)) return
        audioManager.stop()
        audioManager.playPhonicsSound(phonicsWordPath(key))
    }

    // ── The build ─────────────────────────────────────────────────────────────

    fun play() {
        playJob?.cancel()
        audioManager.stop()

        uiState = uiState.copy(
            phase = FirstSentenceBuildPhase.wordsIn,
            wordsShown = 0,
            readingIndex = null,
            showImage = false,
        )

        val target = sentence
        playJob = viewModelScope.launch {
            // 1 · the words arrive, one at a time
            for (i in target.words.indices) {
                uiState = uiState.copy(wordsShown = i + 1)
                val key = FirstSentence.key(target.words[i])
                if (canPlay(key)) audioManager.playPhonicsSound(phonicsWordPath(key))
                delay(450)
            }

            delay(350)

            // 2 · now the whole line, with the highlight travelling
            uiState = uiState.copy(phase = FirstSentenceBuildPhase.reading)
            playFirstSentence(audioManager, target) { i ->
                uiState = uiState.copy(readingIndex = i)
            }

            // 3 · the picture — the line meant something
            uiState = uiState.copy(
                readingIndex = null,
                showImage = true,
                phase = FirstSentenceBuildPhase.done,
            )
        }
    }

    fun reset() {
        playJob?.cancel()
        audioManager.stop()
        uiState = uiState.copy(
            phase = FirstSentenceBuildPhase.idle,
            wordsShown = 0,
            readingIndex = null,
            showImage = false,
        )
    }

    fun stop() {
        reset()
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
