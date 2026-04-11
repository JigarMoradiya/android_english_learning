package com.example.myapplication.main.age_group.from_5_to_7.word_match_picture.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.generation.letter.LetterRepository
import com.example.myapplication.data.generation.letter.LetterRepository.vocabularyCategoryAllForWordMatchImage
import com.example.myapplication.main.age_group.from_3_to_5.match_letter_with_image.view_model.MatchLetterWithImageUiState
import com.example.myapplication.ui.theme.colorList
import com.example.myapplication.utils.AudioPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.plus
import kotlin.math.abs

@HiltViewModel
class WordMatchImageViewModel @Inject constructor(
) : ViewModel() {

    private val batchSize = 5

    var uiState by mutableStateOf(WordMatchImageUiState())
        private set

    var dragStart by mutableStateOf<Offset?>(null)
        private set

    var dragEnd by mutableStateOf<Offset?>(null)
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

        val allWords = (LetterRepository.all.flatMap {
            listOf(it.mainWord) + it.altWords
        } + vocabularyCategoryAllForWordMatchImage).shuffled()

        val uniqueWords = allWords.distinct().take(batchSize)

        val batch = uniqueWords.map { word ->
            word to word
        }
        dragStart = null
        dragEnd = null
        uiState = uiState.copy(
            batchLetters = batch,
            shuffledImages = batch.shuffled(),
            matchedLetters = emptySet(),
            matchedOrder = emptyList(),
            draggingLetter = null,
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

        dragStart = start
        dragEnd = start

        uiState = uiState.copy(
            draggingLetter = letter
        )
    }

    fun updateDrag(delta: Offset) {

        totalDrag += delta

        val start = dragStart ?: return

        dragEnd = start + totalDrag // ✅ NO uiState update
    }

    fun endDrag() {

        totalDrag = Offset.Zero

        val letter = uiState.draggingLetter ?: return
        val end = dragEnd ?: return

        val target = uiState.imageRects.entries.firstOrNull {
            it.value.contains(end)
        }?.key

        if (target == letter) {
            markLetterAsMatched(letter)
        }else{
            // WRONG MATCH
            AudioPlayerManager.playSoundWrongAnswer()
        }

        dragStart = null
        dragEnd = null

        uiState = uiState.copy(
            draggingLetter = null
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
            AudioPlayerManager.playSoundCorrectAnswer()
        }
    }

    // -----------------------------
    // POSITION TRACKING
    // -----------------------------
    fun updateLetterPosition(letter: String, offset: Offset) {
        if (uiState.letterPositions[letter] == offset) return
        val updated = uiState.letterPositions.toMutableMap()
        updated[letter] = offset
        uiState = uiState.copy(letterPositions = updated)
        recomputeFramesReady()
    }

    fun updateImagePosition(letter: String, offset: Offset) {
        if (uiState.imagePositions[letter] == offset) return
        val updated = uiState.imagePositions.toMutableMap()
        updated[letter] = offset
        uiState = uiState.copy(imagePositions = updated)
        recomputeFramesReady()
    }
    fun updateImageRect(letter: String, rect: Rect) {
        val updated = uiState.imageRects + (letter to rect)
        uiState = uiState.copy(imageRects = updated)
    }
    fun recomputeFramesReady() {

        if (uiState.framesReady) return // ✅ avoid repeat work

        val validLetters = uiState.batchLetters.map { it.first }.toSet()

        val letterOK = validLetters.all { uiState.letterPositions[it] != null }
        val imageOK = validLetters.all { uiState.imagePositions[it] != null }

        if (letterOK && imageOK) {
            uiState = uiState.copy(framesReady = true)
        }
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