package com.example.myapplication.main.age_group.from_3_to_5.arrange_letter_sequence.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.LetterMode
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.FeedbackConstant.feedbackFillBlank
import com.example.myapplication.utils.FeedbackConstant.feedbackFillBlankSubtitleWrong
import com.example.myapplication.utils.FeedbackConstant.feedbackWrong
import com.example.myapplication.utils.FeedbackConstant.feedbackTitles
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArrangeLetterInSequenceViewModel @Inject constructor() : ViewModel() {

    var uiState by mutableStateOf(ArrangeLetterInSequenceUiState())
        private set

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

        // ✅ 1. Pick random 5–6 sequence
        val size = (5..6).random()
        val startIndex = (0..(26 - size)).random()

        val sequence = alphabets.subList(startIndex, startIndex + size)

        // ✅ 2. ALL slots empty
        val topSlots = List<String?>(size) { null }

        // ✅ 3. Bottom = ALL letters shuffled
        val options = sequence.shuffled()

        uiState = uiState.copy(
            topSlots = topSlots,
            bottomOptions = options,
            fullSequence = sequence,
            showSuccess = false,
            showError = false
        )
    }

    // ✅ Tap bottom → fill first empty
    fun onBottomLetterClick(letter: String) {

        if (uiState.showSuccess) return

        val slots = uiState.topSlots.toMutableList()
        val firstEmpty = slots.indexOfFirst { it == null }

        if (firstEmpty != -1) {
            slots[firstEmpty] = letter

            val updatedBottom = uiState.bottomOptions.toMutableList()
            updatedBottom.remove(letter)

            uiState = uiState.copy(
                topSlots = slots,
                bottomOptions = updatedBottom
            )

            validate()
        }
    }

    // ✅ Tap top → remove back
    fun onTopLetterClick(index: Int) {

        if (uiState.showSuccess) return

        val slots = uiState.topSlots.toMutableList()
        val letter = slots[index] ?: return

        slots[index] = null

        val updatedBottom = uiState.bottomOptions.toMutableList()
        updatedBottom.add(letter)

        AudioPlayerManager.playSoundMenuClick()

        uiState = uiState.copy(
            topSlots = slots,
            bottomOptions = updatedBottom,
            showError = false
        )
    }

    // ✅ VALIDATE (same concept as your VM) :contentReference[oaicite:0]{index=0}
    private fun validate() {

        if (!uiState.topSlots.contains(null)) {

            val formed = uiState.topSlots.joinToString("")
            val correct = uiState.fullSequence.joinToString("")

            uiState = if (formed == correct) {

                AudioPlayerManager.playSoundClap()

                val randomTitle = feedbackTitles.random()
                val randomSub = feedbackFillBlank.random()

                uiState.copy(
                    showSuccess = true,
                    feedbackTextRes = randomTitle,
                    feedbackSubTextRes = randomSub,
                    showError = false
                )

            } else {

                AudioPlayerManager.playSoundWrongAnswer()

                val randomTitle = feedbackWrong.random()
                val randomSub = feedbackFillBlankSubtitleWrong.random()

                uiState.copy(
                    showError = true,
                    feedbackTextRes = randomTitle,
                    feedbackSubTextRes = randomSub
                )
            }

        } else {
            AudioPlayerManager.playSoundMenuClick()
        }
    }

    fun next() {
        uiState = uiState.copy(round = uiState.round + 1)
        generateGame()
    }

    fun changeMode(mode: LetterMode) {
        val round = if (uiState.showSuccess){
            uiState.round + 1
        }else{
            uiState.round
        }
        uiState = uiState.copy(mode = mode,round = round)
        generateGame()
    }

}