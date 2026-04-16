package com.example.myapplication.main.age_group.from_3_to_5.drag_and_drop_word.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.generation.letter.LetterRepository
import com.example.myapplication.main.age_group.from_3_to_5.missing_letter.view_model.DifficultyLevel
import com.example.myapplication.main.age_group.from_3_to_5.missing_letter.view_model.LetterItem
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.FeedbackConstant.feedbackMissingLetter
import com.example.myapplication.utils.FeedbackConstant.feedbackMissingLetterSubTitleForWrong
import com.example.myapplication.utils.FeedbackConstant.feedbackWrong
import com.example.myapplication.utils.FeedbackConstant.feedbackTitles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DragDropWordViewModel @Inject constructor() : ViewModel() {

    private val allWordsEasy = LetterRepository.missingLetterEasyWords + LetterRepository.missingLetterEasyWords4Basic
    private val allWordsMedium = LetterRepository.missingLetterMediumWords + LetterRepository.missingLetterEasyWords4Basic

    var uiState by mutableStateOf(DragDropWordUiState())
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
    var dragFromIndex by mutableStateOf<Int?>(null)
    private val _difficulty = MutableStateFlow(DifficultyLevel.EASY)
    val difficulty = _difficulty.asStateFlow()

    fun setDifficulty(level: DifficultyLevel) {
        _difficulty.value = level
        loadData()
    }

    private fun loadData() {
        val list = if (difficulty.value == DifficultyLevel.EASY){
            allWordsEasy
        }else{
            allWordsMedium
        }
        val first = list.randomOrNull() ?: "CAT"
        setupWord(first)
    }

    fun loadNextWord() {
        loadData()
    }

    fun updateSlotRect(index: Int, rect: Rect) {
        slotRects = slotRects + (index to rect)
    }


    // -------------------------
    // WORD SETUP
    // -------------------------
    fun setupWord(word: String) {

        val upper = word.uppercase().replace("-", "")
        targetWord = upper

        val length = upper.length

        // ----------------------------
        // 1. ALL SLOTS EMPTY
        // ----------------------------
        dropped = MutableList(length) { null }

        // ❌ NO FIXED LETTERS
        fixedIndices = emptySet()

        // ----------------------------
        // 2. LETTER POOL = EXACT WORD LETTERS ONLY
        // ----------------------------
        letters = upper.map { ch ->
            LetterItem(ch.toString())
        }.shuffled()

        // ----------------------------
        // 3. RESET STATE
        // ----------------------------
        clearDrag()

        uiState = uiState.copy(
            showError = false,
            showSuccess = false
        )
    }

    fun clearDrag() {
        dragging = null
        dragPosition = null
    }
    fun clearSlot(index: Int) {
        dropped = dropped.toMutableList().apply {
            set(index, null)
        }
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
    fun restoreToSlot(item: LetterItem, index: Int) {
        dropped = dropped.toMutableList().apply {
            set(index, item)
        }
    }
    fun returnToPool(item: LetterItem, index: Int) {

        // remove from slot
        dropped = dropped.toMutableList().apply {
            set(index, null)

        }

        // add back to pool
        if (!letters.contains(item)) {
            letters = letters + item
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
                    delay(100)
                    AudioPlayerManager.playSoundClap()

                    val randomTitle = feedbackTitles.random()
                    val randomSub = feedbackMissingLetter.random()

                    uiState = uiState.copy(
                        showSuccess = true,
                        feedbackTextRes = randomTitle,
                        feedbackSubTextRes = randomSub,
                        showError = false
                    )
                }

            } else {
                // ❌ WRONG ANSWER
                AudioPlayerManager.playSoundWrongAnswer()
                val randomTitle = feedbackWrong.random()
                val randomSub = feedbackMissingLetterSubTitleForWrong.random()
                uiState = uiState.copy(
                    showError = true,
                    feedbackTextRes = randomTitle,
                    feedbackSubTextRes = randomSub,
                )
            }
        }
    }

    fun closePopup() {
        uiState = uiState.copy(showSuccess = false)
    }

    fun removeError() {
        uiState = uiState.copy(showError = false)
    }
}