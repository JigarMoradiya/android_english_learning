package com.example.myapplication.main.age_group.from_3_to_5.arrange_letter_sequence.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.LetterMode
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.FeedbackConstant.feedbackFillBlank
import com.example.myapplication.utils.FeedbackConstant.feedbackFillBlankSubtitleWrong
import com.example.myapplication.utils.FeedbackConstant.feedbackWrong
import com.example.myapplication.utils.FeedbackConstant.feedbackTitles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArrangeLetterInSequenceViewModel @Inject constructor() : ViewModel() {

    var uiState by mutableStateOf(ArrangeLetterInSequenceUiState())
        private set

    private var countdownJob: Job? = null

    init {
        generateGame()
    }

    private fun getAlphabet(): List<String> {
        return when (uiState.mode) {
            LetterMode.UPPERCASE -> ('A'..'Z').map { it.toString() }
            LetterMode.LOWERCASE -> ('a'..'z').map { it.toString() }
        }
    }

    fun generateGame() {
        val alphabets = getAlphabet()
        val size = (5..6).random()
        val startIndex = (0..(26 - size)).random()
        val sequence = alphabets.subList(startIndex, startIndex + size)

        uiState = uiState.copy(
            topSlots = List<String?>(size) { null },
            bottomOptions = sequence.shuffled(),
            fullSequence = sequence,
            showSuccess = false,
            showError = false,
            showNext = false,
        )
    }

    fun onBottomLetterClick(letter: String) {
        if (uiState.showNext) return
        val slots = uiState.topSlots.toMutableList()
        val firstEmpty = slots.indexOfFirst { it == null }
        if (firstEmpty != -1) {
            slots[firstEmpty] = letter
            val updatedBottom = uiState.bottomOptions.toMutableList()
            updatedBottom.remove(letter)
            uiState = uiState.copy(topSlots = slots, bottomOptions = updatedBottom)
            validate()
        }
    }

    fun onTopLetterClick(index: Int) {
        if (uiState.showNext) return
        val slots = uiState.topSlots.toMutableList()
        val letter = slots[index] ?: return
        slots[index] = null
        val updatedBottom = uiState.bottomOptions.toMutableList()
        updatedBottom.add(letter)
        AudioPlayerManager.playSoundMenuClick()
        uiState = uiState.copy(topSlots = slots, bottomOptions = updatedBottom, showError = false)
    }

    private fun validate() {
        if (uiState.topSlots.contains(null)) {
            AudioPlayerManager.playSoundMenuClick()
            return
        }

        val formed = uiState.topSlots.joinToString("")
        val correct = uiState.fullSequence.joinToString("")

        if (formed == correct) {
            AudioPlayerManager.playSoundClap()
            uiState = uiState.copy(
                showSuccess = true,
                showError = false,
                showNext = true,
                correctCount = uiState.correctCount + 1,
                feedbackTextRes = feedbackTitles.random(),
                feedbackSubTextRes = feedbackFillBlank.random(),
            )
            startCountdown()
        } else {
            AudioPlayerManager.playSoundWrongAnswer()
            uiState = uiState.copy(
                showError = true,
                showSuccess = false,
                feedbackTextRes = feedbackWrong.random(),
                feedbackSubTextRes = feedbackFillBlankSubtitleWrong.random(),
            )
        }
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            uiState = uiState.copy(countdown = 3)
            delay(1000L)
            uiState = uiState.copy(countdown = 2)
            delay(1000L)
            uiState = uiState.copy(countdown = 1)
            delay(1000L)
            next()
        }
    }

    fun next() {
        if (uiState.round >= uiState.totalRounds) {
            countdownJob?.cancel()
            countdownJob = null
            uiState = uiState.copy(showResult = true)
        } else {
            uiState = uiState.copy(round = uiState.round + 1)
            generateGame()
        }
    }

    fun restartGame() {
        uiState = uiState.copy(round = 1, correctCount = 0, showResult = false)
        generateGame()
    }

    fun changeMode(mode: LetterMode) {
        countdownJob?.cancel()
        countdownJob = null
        uiState = uiState.copy(mode = mode, round = 1, correctCount = 0, showResult = false)
        generateGame()
    }
}
