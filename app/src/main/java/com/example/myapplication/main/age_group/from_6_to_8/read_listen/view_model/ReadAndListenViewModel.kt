package com.example.myapplication.main.age_group.from_6_to_8.read_listen.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.model.ReadSentenceItemNew
import com.example.myapplication.data.model.UnitSelectionScreen
import com.example.myapplication.main.age_group.from_6_to_8.common.unit.data.SentenceProgressManager
import com.example.myapplication.utilities.TextToSpeechManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReadAndListenViewModel @Inject constructor(
    private val progressManager: SentenceProgressManager,
    private val ttsManager: TextToSpeechManager
) : ViewModel() {

    var uiState by mutableStateOf(ReadAndListenUiState())
        private set

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

    // Next
    fun nextSentence() {
        val lesson = uiState.lessonData ?: return

        if (uiState.currentSentenceIndex < lesson.sentences.size - 1) {
            uiState = uiState.copy(
                currentSentenceIndex = uiState.currentSentenceIndex + 1
            )
            checkIfCompleted()
        }
    }

    // Previous
    fun previousSentence() {
        if (uiState.currentSentenceIndex > 0) {
            uiState = uiState.copy(
                currentSentenceIndex = uiState.currentSentenceIndex - 1
            )
        }
    }

    // Toggle join
    fun toggleJoinWords() {
        uiState = uiState.copy(isSentenceJoined = !uiState.isSentenceJoined)
    }

    // First
    val isAtFirst: Boolean
        get() = uiState.currentSentenceIndex == 0

    // Last
    val isAtLast: Boolean
        get() = uiState.lessonData?.let {
            uiState.currentSentenceIndex == it.sentences.size - 1
        } ?: false

    // MARK: - Auto Completion
    private fun checkIfCompleted() {

        val lesson = uiState.lessonData ?: return
        if (uiState.hasMarkedComplete) return

        if (uiState.currentSentenceIndex == lesson.sentences.size - 1) {
            progressManager.markCompleted(type = uiState.screenType, lessonId = lesson.id)
            uiState = uiState.copy(hasMarkedComplete = true)
        }
    }
}