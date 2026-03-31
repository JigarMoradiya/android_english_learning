package com.example.myapplication.main.age_group.from_3_to_5.missing_letter.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.generation.letter.LetterRepository
import com.example.myapplication.utils.AudioPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.min
import kotlin.random.Random
@HiltViewModel
class MissingLetterViewModel @Inject constructor() : ViewModel() {

    private var level: DifficultyLevel = DifficultyLevel.EASY

    private val allWordsEasy =
        LetterRepository.missingLetterEasyWords + LetterRepository.missingLetterEasyWords4Basic

    var uiState by mutableStateOf(MissingLetterUiState())
        private set

    var currentWordIndex by mutableIntStateOf(0)
        private set

    var targetWord by mutableStateOf("")
        private set

    var letters by mutableStateOf<List<LetterItem>>(emptyList())
        private set

    var dropped by mutableStateOf<List<LetterItem?>>(emptyList())
        private set

    var fixedIndices by mutableStateOf<Set<Int>>(emptySet())
        private set

    // ✅ DRAG STATE (FINAL)
    var dragging by mutableStateOf<LetterItem?>(null)
    var dragPosition by mutableStateOf<Offset?>(null)

    // ✅ SLOT RECTS
    var slotRects by mutableStateOf<Map<Int, Rect>>(emptyMap())
        private set

    fun updateSlotRect(index: Int, rect: Rect) {
        slotRects = slotRects + (index to rect)
    }

    init {
        val first = allWordsEasy.randomOrNull() ?: "CAT"
        setupWord(first)
    }

    // -------------------------
    // WORD SETUP
    // -------------------------
    fun setupWord(word: String) {

        val upper = word.uppercase()
        targetWord = upper

        val length = upper.length

        val blankIndex = (0 until length).random()

        dropped = upper.mapIndexed { i, c ->
            if (i == blankIndex) null else LetterItem(c.toString())
        }

        fixedIndices = dropped.mapIndexedNotNull { i, v ->
            if (v != null) i else null
        }.toSet()

        val pool = mutableListOf<LetterItem>()

        pool.add(LetterItem(upper[blankIndex].toString()))

        while (pool.size < 5) {
            val r = ('A'..'Z').random().toString()
            if (!upper.contains(r)) {
                pool.add(LetterItem(r))
            }
        }

        letters = pool.shuffled()

        clearDrag()
    }

    fun clearDrag() {
        dragging = null
        dragPosition = null
    }

    // -------------------------
    // PLACE
    // -------------------------
    fun place(item: LetterItem, index: Int) {

        if (fixedIndices.contains(index)) return

        letters = letters.toMutableList().apply { remove(item) }

        dropped = dropped.toMutableList().apply {
            set(index, item)
        }
    }

    fun fallbackReturn(item: LetterItem) {
        if (!letters.contains(item)) {
            letters = letters + item
        }
    }

    // -------------------------
    // VALIDATE
    // -------------------------
    fun validate() {

        val word = dropped.mapNotNull { it?.letter }.joinToString("")

        if (!dropped.contains(null)) {

            if (word == targetWord) {
                viewModelScope.launch {
                    delay(300)

                    uiState = uiState.copy(
                        showPopup = true,
                        feedbackText = "Great!",
                        feedbackSubText = "Well Done!",
                        showError = false // reset
                    )
                }

            } else {
                // ❌ WRONG ANSWER
                uiState = uiState.copy(
                    showError = true,
                    errorText = "Try Again!"
                )
            }
        }
    }

    fun loadNextWord() {
        val next = allWordsEasy.randomOrNull() ?: "DOG"
        setupWord(next)
    }

    fun closePopup() {
        uiState = uiState.copy(showPopup = false)
    }

    fun removeError() {
        uiState = uiState.copy(showError = false)
    }
}