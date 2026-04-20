package com.example.myapplication.main.age_group.from_6_to_8.read_listen.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.ReadSentenceItemNew
import com.example.myapplication.data.model.UnitSelectionScreen
import com.example.myapplication.main.age_group.from_6_to_8.common.unit.data.SentenceProgressManager
import com.example.myapplication.utilities.TextToSpeechManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

@HiltViewModel
class ReadAndListenViewModel @Inject constructor(
    private val progressManager: SentenceProgressManager,
    private val ttsManager: TextToSpeechManager
) : ViewModel() {

    private var speakingJob: Job? = null

    var uiState by mutableStateOf(ReadAndListenUiState())
        private set

    // user when joined sentences
    private var wordRanges: List<IntRange> = emptyList()

    fun setScreenTypeAndLessonData(screenType: UnitSelectionScreen, lessonData: ReadSentenceItemNew) {
        uiState = uiState.copy(screenType = screenType,lessonData = lessonData)
    }

    // Current sentence
    val currentSentence: String
        get() = uiState.lessonData
            ?.sentences
            ?.getOrNull(uiState.currentSentenceIndex)
            ?.text
            ?: ""

    // Words split
    val words: List<String>
        get() = currentSentence.split(" ")

    // First
    val isAtFirst: Boolean
        get() = uiState.currentSentenceIndex == 0

    // Last
    val isAtLast: Boolean
        get() = uiState.lessonData?.let {
            uiState.currentSentenceIndex == it.sentences.size - 1
        } ?: false

    // Next
    fun nextSentence() {
        val lesson = uiState.lessonData ?: return

        if (uiState.currentSentenceIndex < lesson.sentences.size - 1) {
            stopSpeaking()
            uiState = uiState.copy(currentSentenceIndex = uiState.currentSentenceIndex + 1)
            checkIfCompleted()
        }
    }

    // Previous
    fun previousSentence() {
        if (uiState.currentSentenceIndex > 0) {
            stopSpeaking()
            uiState = uiState.copy(currentSentenceIndex = uiState.currentSentenceIndex - 1)
        }
    }

    // Toggle join
    fun toggleJoinWords() {
        stopSpeaking()
        uiState = uiState.copy(isSentenceJoined = !uiState.isSentenceJoined)
    }

    // MARK: - Auto Completion
    private fun checkIfCompleted() {

        val lesson = uiState.lessonData ?: return
        if (uiState.hasMarkedComplete) return

        if (uiState.currentSentenceIndex == lesson.sentences.size - 1) {
            progressManager.markCompleted(type = uiState.screenType, lessonId = lesson.id)
            uiState = uiState.copy(hasMarkedComplete = true)
        }
    }

    fun speak() {
        val words = words
        if (words.isEmpty()) return

        stopSpeaking()

        if (uiState.isSentenceJoined) {
            speakJoined(words)
        } else {
            speakSplit(words)
        }
    }

    private fun prepareWordRanges(sentence: String, words: List<String>) {
        val ranges = mutableListOf<IntRange>()
        var currentIndex = 0

        words.forEach { word ->
            val start = sentence.indexOf(word, currentIndex)
            val end = start + word.length
            ranges.add(start until end)
            currentIndex = end
        }

        wordRanges = ranges
    }

    private fun getWordIndex(charIndex: Int): Int {
        return wordRanges.indexOfFirst { charIndex in it }
            .takeIf { it != -1 } ?: 0
    }
    private fun speakJoined(words: List<String>) {

        speakingJob?.cancel()

        ttsManager.stop()

        val sentence = words.joinToString(" ")

        prepareWordRanges(sentence, words)

        speakingJob = viewModelScope.launch {

            uiState = uiState.copy(
                isSpeaking = true,
                joinSentenceSpeakingIndex = 0
            )

            ttsManager.onWordSpoken = onWordSpoken@ { charIndex ->

                // ✅ Ignore if already stopped or sentence changed
                if (!uiState.isSpeaking) return@onWordSpoken

                val index = getWordIndex(charIndex)

                uiState = uiState.copy(joinSentenceSpeakingIndex = index)
            }

            ttsManager.speak(
                text = sentence,
                utteranceId = "joined"
            ) {
                viewModelScope.launch {
                    uiState = uiState.copy(
                        isSpeaking = false,
                        joinSentenceSpeakingIndex = null
                    )
                }
            }
        }
    }

    private fun speakSplit(words: List<String>) {

        ttsManager.stop()

        speakingJob?.cancel() // cancel previous if any

        speakingJob = viewModelScope.launch {

            uiState = uiState.copy(
                isSpeaking = true,
                splitSentenceWordIndex = 0
            )

            for ((index, word) in words.withIndex()) {

                // Stop if cancelled
                if (!isActive) return@launch

                uiState = uiState.copy(splitSentenceWordIndex = index)

                speakWord(word, "word_$index")
            }

            uiState = uiState.copy(
                isSpeaking = false,
                splitSentenceWordIndex = -1
            )
        }
    }
    private suspend fun speakWord(text: String, id: String) =
        suspendCancellableCoroutine { cont ->
            ttsManager.speak(text, id) {
                if (cont.isActive) cont.resume(Unit) { _, _, _ -> }
            }
        }

    fun stopSpeaking() {
        speakingJob?.cancel() // 👈 IMPORTANT
        speakingJob = null

        ttsManager.stop()
        ttsManager.onWordSpoken = null
        uiState = uiState.copy(
            isSpeaking = false,
            joinSentenceSpeakingIndex = null,
            splitSentenceWordIndex = -1
        )
    }
}