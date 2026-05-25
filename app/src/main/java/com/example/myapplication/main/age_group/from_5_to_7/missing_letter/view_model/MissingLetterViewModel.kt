package com.example.myapplication.main.age_group.from_5_to_7.missing_letter.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.generation.letter.LetterRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.FeedbackConstant.feedbackMissingLetter
import com.example.myapplication.utils.FeedbackConstant.feedbackMissingLetterSubTitleForWrong
import com.example.myapplication.utils.FeedbackConstant.feedbackTitles
import com.example.myapplication.utils.FeedbackConstant.feedbackWrong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlin.math.min
import kotlin.random.Random

@HiltViewModel
class MissingLetterViewModel57 @Inject constructor() : ViewModel() {

    private val allWordsEasy = LetterRepository.missingLetterEasyWords + LetterRepository.missingLetterEasyWords4Basic
    private val allWordsMedium = LetterRepository.missingLetterMediumWords + LetterRepository.missingLetterEasyWords4Basic

    var uiState by mutableStateOf(MissingLetterUiState57())
        private set

    var targetWord by mutableStateOf("")
        private set

    var letters by mutableStateOf<List<LetterItem>>(emptyList())
        private set

    var dropped by mutableStateOf<List<LetterItem?>>(emptyList())
        private set

    var fixedIndices by mutableStateOf<Set<Int>>(emptySet())
        private set

    var dragging by mutableStateOf<LetterItem?>(null)
    var dragPosition by mutableStateOf<Offset?>(null)

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
        val list = if (difficulty.value == DifficultyLevel.EASY) allWordsEasy else allWordsMedium
        val first = list.randomOrNull() ?: "CAT"
        setupWord(first)
    }

    fun loadNextWord() { loadData() }

    fun updateSlotRect(index: Int, rect: Rect) {
        slotRects = slotRects + (index to rect)
    }

    fun setupWord(word: String) {
        val upper = word.uppercase().replace("-", "")
        targetWord = upper
        val length = upper.length

        val blankCount = if (difficulty.value == DifficultyLevel.EASY) {
            1
        } else {
            when (length) {
                4 -> if (Random.nextBoolean()) 1 else 2
                5 -> 2
                6 -> if (Random.nextBoolean()) 2 else 3
                else -> min(3, length / 2)
            }
        }

        val blanks = mutableSetOf<Int>()
        while (blanks.size < blankCount) { blanks.add(Random.nextInt(length)) }

        val tempDropped = MutableList<LetterItem?>(length) { null }
        val tempFixed = mutableSetOf<Int>()
        upper.forEachIndexed { i, ch ->
            if (blanks.contains(i)) {
                tempDropped[i] = null
            } else {
                tempDropped[i] = LetterItem(ch.toString())
                tempFixed.add(i)
            }
        }
        dropped = tempDropped
        fixedIndices = tempFixed

        val pool = mutableListOf<LetterItem>()
        blanks.map { upper[it].toString() }.forEach { pool.add(LetterItem(it)) }

        val extraLetter = when (blankCount) { 1 -> 3; 2 -> 4; else -> 5 }
        while (pool.size < extraLetter) {
            val r = ('A'..'Z').random().toString()
            if (!upper.contains(r) && pool.none { it.letter == r }) { pool.add(LetterItem(r)) }
        }

        letters = pool.shuffled()
        clearDrag()
        uiState = uiState.copy(showError = false, showSuccess = false)
    }

    fun clearDrag() { dragging = null; dragPosition = null; dragFromIndex = null }
    fun clearSlot(index: Int) { dropped = dropped.toMutableList().apply { set(index, null) } }

    fun place(item: LetterItem, index: Int) {
        if (fixedIndices.contains(index)) return
        letters = letters.toMutableList().apply { remove(item) }
        dropped = dropped.toMutableList().apply { set(index, item) }
    }

    fun restoreToSlot(item: LetterItem, index: Int) {
        dropped = dropped.toMutableList().apply { set(index, item) }
    }

    fun returnToPool(item: LetterItem, index: Int) {
        dropped = dropped.toMutableList().apply { set(index, null) }
        if (!letters.contains(item)) { letters = letters + item }
    }

    fun fallbackReturn(item: LetterItem) {
        if (!letters.contains(item)) { letters = letters + item }
    }

    fun validate() {
        val word = dropped.mapNotNull { it?.letter }.joinToString("")
        if (!dropped.contains(null)) {
            if (word == targetWord) {
                AudioPlayerManager.playSoundCorrectAnswer()
                uiState = uiState.copy(
                    showSuccess = true,
                    feedbackTextRes = feedbackTitles.random(),
                    feedbackSubTextRes = feedbackMissingLetter.random(),
                    showError = false,
                    countdownValue = 3
                )
                viewModelScope.launch {
                    delay(1000); uiState = uiState.copy(countdownValue = 2)
                    delay(1000); uiState = uiState.copy(countdownValue = 1)
                    delay(1000); loadNextWord()
                }
            } else {
                val badSlots = mutableSetOf<Int>()
                targetWord.forEachIndexed { i, ch ->
                    if (!fixedIndices.contains(i)) {
                        val placed = dropped.getOrNull(i)
                        if (placed != null && placed.letter != ch.toString()) { badSlots.add(i) }
                    }
                }
                AudioPlayerManager.playSoundWrongAnswer()
                uiState = uiState.copy(
                    showError = true,
                    feedbackTextRes = feedbackWrong.random(),
                    feedbackSubTextRes = feedbackMissingLetterSubTitleForWrong.random(),
                    wrongSlots = badSlots
                )
            }
        } else {
            AudioPlayerManager.playSoundDragItem()
        }
    }

    fun closePopup() { uiState = uiState.copy(showSuccess = false) }
    fun removeError() { uiState = uiState.copy(showError = false, wrongSlots = emptySet()) }
}
