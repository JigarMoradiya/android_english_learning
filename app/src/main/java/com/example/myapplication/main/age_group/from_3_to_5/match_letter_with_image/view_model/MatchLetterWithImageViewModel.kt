package com.example.myapplication.main.age_group.from_3_to_5.match_letter_with_image.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.generation.letter.LetterRepository
import com.example.myapplication.utilities.TextToSpeechManager
import com.example.myapplication.utils.AppUtils.colorList
import com.example.myapplication.utils.AudioPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class MatchLetterWithImageViewModel @Inject constructor() : ViewModel() {

    private val batchSize = 5

    var uiState by mutableStateOf(MatchLetterWithImageUiState())
        private set

    init {
        loadNewBatch()
    }

    fun getLetterColor(letter: String): Color {
        return colorList[abs(letter.hashCode()) % colorList.size]
    }

    fun getLineColor(index: Int): Color {
        return colorList[index % colorList.size]
    }

    // -----------------------------
    // LOAD
    // -----------------------------
    fun loadNewBatch() {

        val letters = LetterRepository.all.shuffled().take(batchSize)

        val batch = letters.map {
            val allWords = listOf(it.mainWord) + it.altWords
            val word = allWords.random()
            it.letter to word
        }

        uiState = uiState.copy(
            batchLetters = batch,
            shuffledImages = batch.shuffled(),
            matchedLetters = emptySet(),
            matchedOrder = emptyList(),
            draggingLetter = null,
            dragStart = null,
            dragEnd = null,
            letterPositions = emptyMap(),
            imagePositions = emptyMap(),
            imageRects = emptyMap(),
        )
    }

    // -----------------------------
    // DRAG
    // -----------------------------
    // NEW VARIABLE
    private var totalDrag = Offset.Zero

    fun startDrag(letter: String, start: Offset) {
        totalDrag = Offset.Zero

        uiState = uiState.copy(
            draggingLetter = letter,
            dragStart = start,
            dragEnd = start
        )
    }

    fun updateDrag(delta: Offset) {

        totalDrag += delta // accumulate once here only

        val start = uiState.dragStart ?: return

        uiState = uiState.copy(
            dragEnd = start + totalDrag // ✅ CORRECT
        )
    }

    fun endDrag() {

        totalDrag = Offset.Zero

        val letter = uiState.draggingLetter ?: return
        val end = uiState.dragEnd ?: return

        val target = uiState.imageRects.entries.firstOrNull { entry ->
            entry.value.contains(end)
        }?.key

        if (target == letter) {
            markLetterAsMatched(letter)
        }

        uiState = uiState.copy(
            draggingLetter = null,
            dragStart = null,
            dragEnd = null
        )
    }

    fun markLetterAsMatched(letter: String) {

        if (uiState.matchedLetters.contains(letter)) return

        val updatedSet = uiState.matchedLetters + letter
        val updatedOrder = uiState.matchedOrder + letter

        uiState = uiState.copy(
            matchedLetters = updatedSet,
            matchedOrder = updatedOrder
        )

        // ✅ OPTIONAL: show popup when all matched
        if (updatedSet.size == uiState.batchLetters.size) {
            // COMPLETE
            viewModelScope.launch {
                delay(200)
                AudioPlayerManager.playSoundClap()
                uiState = uiState.copy(
                    showPopup = true,
                    feedbackText = "Great!",
                    feedbackSubText = "All matched!"
                )
            }
        }else{
            // MATCH
            AudioPlayerManager.playSoundDragItem()
        }
    }

    // -----------------------------
    // POSITION TRACKING
    // -----------------------------
    fun updateLetterPosition(letter: String, offset: Offset) {
        val updated = uiState.letterPositions + (letter to offset)
        uiState = uiState.copy(letterPositions = updated)
        recomputeFramesReady()
    }

    fun updateImagePosition(letter: String, offset: Offset) {
        val updated = uiState.imagePositions + (letter to offset)
        uiState = uiState.copy(imagePositions = updated)
        recomputeFramesReady()
    }
    fun updateImageRect(letter: String, rect: Rect) {
        val updated = uiState.imageRects + (letter to rect)
        uiState = uiState.copy(imageRects = updated)
    }
    fun recomputeFramesReady() {
        val validLetters = uiState.batchLetters.map { it.first }.toSet()

        val letterOK = validLetters.all { uiState.letterPositions[it] != null }
        val imageOK = validLetters.all { uiState.imagePositions[it] != null }

        uiState = uiState.copy(
            framesReady = letterOK && imageOK
        )
    }
    // -----------------------------
    fun playAgain() {
        uiState = uiState.copy(
            round = uiState.round + 1,
            showPopup = false
        )
        loadNewBatch()
    }

    fun closePopup() {
        uiState = uiState.copy(showPopup = false)
    }
}