package com.example.myapplication.main.age_group.from_3_to_5.fill_blank.view_model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.LetterMode
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.FeedbackConstant.feedbackFillBlank
import com.example.myapplication.utils.FeedbackConstant.feedbackFillBlankSubtitleWrong
import com.example.myapplication.utils.FeedbackConstant.feedbackMissingLetterTitleForWrong
import com.example.myapplication.utils.FeedbackConstant.feedbackTitles
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FillBlankLettersViewModel @Inject constructor() : ViewModel() {

    var uiState by mutableStateOf(FillBlankLetterUiState())
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

        // ✅ 2. Create blanks
        val blankCount = when (size) {
            5 -> (2..3).random()
            6 -> 3
            else -> 2
        }
        val blankIndices = mutableSetOf<Int>()

        while (blankIndices.size < blankCount) {
            blankIndices.add((0 until size).random())
        }

        val topSlots = sequence.mapIndexed { index, letter ->
            if (blankIndices.contains(index)) null else letter
        }

        val correctLetters = blankIndices.map { sequence[it] }

        // ✅ 3. Bottom options (correct + wrong)
        val options = mutableListOf<String>()
        options.addAll(correctLetters)

        while (options.size < correctLetters.size + 3) {
            val random = alphabets.random()
            if (!sequence.contains(random) && !options.contains(random)) {
                options.add(random)
            }
        }
        val fixedIndices = sequence.mapIndexedNotNull { index, letter ->
            if (!blankIndices.contains(index)) index else null
        }.toSet()

        uiState = uiState.copy(
            topSlots = topSlots,
            correctLetters = correctLetters,
            bottomOptions = options.shuffled(),
            fullSequence = sequence,
            fixedIndices = fixedIndices,
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

            uiState = uiState.copy(topSlots = slots, bottomOptions = updatedBottom)

            validate()
        }
    }

    // ✅ Tap top → remove back
    fun onTopLetterClick(index: Int) {
        if (uiState.showSuccess) return

        val isFixed = uiState.fixedIndices.contains(index)
        if (isFixed) return

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
                val randomTitle = feedbackMissingLetterTitleForWrong.random()
                val randomSub = feedbackFillBlankSubtitleWrong.random()
                uiState.copy(
                    showError = true,
                    feedbackTextRes = randomTitle,
                    feedbackSubTextRes = randomSub,
                )
            }
        }else{
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