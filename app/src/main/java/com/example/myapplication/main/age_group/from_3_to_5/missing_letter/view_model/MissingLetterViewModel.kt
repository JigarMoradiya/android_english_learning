package com.example.myapplication.main.age_group.from_3_to_5.missing_letter.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.generation.letter.LetterRepository
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.FeedbackConstant.feedbackMissingLetter
import com.example.myapplication.utils.FeedbackConstant.feedbackMissingLetterSubTitleForWrong
import com.example.myapplication.utils.FeedbackConstant.feedbackMissingLetterTitleForWrong
import com.example.myapplication.utils.FeedbackConstant.feedbackTitles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.min
import kotlin.random.Random

@HiltViewModel
class MissingLetterViewModel @Inject constructor() : ViewModel() {


    private val allWordsEasy = LetterRepository.missingLetterEasyWords + LetterRepository.missingLetterEasyWords4Basic
    private val allWordsMedium = LetterRepository.missingLetterMediumWords + LetterRepository.missingLetterEasyWords4Basic

    var uiState by mutableStateOf(MissingLetterUiState())
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
//        val first = list.randomOrNull() ?: "CAT"
        val first = "ELEPHANT"
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
        // 1. DECIDE HOW MANY BLANKS
        // ----------------------------
        val blankCount = if (difficulty.value == DifficultyLevel.EASY){
            4
        }else{
            when (length) {
                4 -> if (Random.nextBoolean()) 1 else 2
                5 -> 2
                6 -> if (Random.nextBoolean()) 2 else 3
                else -> min(3, length / 2)
            }
        }

        val blanks = mutableSetOf<Int>()
        while (blanks.size < blankCount) {
            blanks.add(Random.nextInt(length))
        }

        // ----------------------------
        // 2. BUILD DROPPED + FIXED
        // ----------------------------
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

        // ----------------------------
        // 3. BUILD LETTER POOL
        // ----------------------------

        val pool = mutableListOf<LetterItem>()

        // ⭐ ADD EXACT MISSING LETTERS (INCLUDING DUPLICATES)
        val missingLetters = blanks.map { upper[it].toString() }

        missingLetters.forEach {
            pool.add(LetterItem(it))
        }

        // ⭐ ADD RANDOM UNIQUE LETTERS
        val extraLetter = if (difficulty.value == DifficultyLevel.EASY){
            2
        }else{
            3
        }
        while (pool.size < (missingLetters.size + extraLetter)) {

            val r = ('A'..'Z').random().toString()

            if (
                !upper.contains(r) && // not part of word
                pool.none { it.letter == r } // no duplicate random
            ) {
                pool.add(LetterItem(r))
            }
        }

        letters = pool.shuffled()

        // ----------------------------
        // 4. RESET STATE
        // ----------------------------
        clearDrag()

        uiState = uiState.copy(
            showError = false,
            showPopup = false
        )
    }

    fun clearDrag() {
        dragging = null
        dragPosition = null
    }
    fun clearSlot(index: Int) {
        dropped = dropped.toMutableList().apply {
            AudioPlayerManager.playSoundDragItem()
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
            AudioPlayerManager.playSoundDragItem()
            set(index, item)
        }
    }
    fun returnToPool(item: LetterItem, index: Int) {

        // remove from slot
        dropped = dropped.toMutableList().apply {
            AudioPlayerManager.playSoundDragItem()
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
                        showPopup = true,
                        feedbackTextRes = randomTitle,
                        feedbackSubTextRes = randomSub,
                        showError = false
                    )
                }

            } else {
                // ❌ WRONG ANSWER
                AudioPlayerManager.playSoundWrongAnswer()
                val randomTitle = feedbackMissingLetterTitleForWrong.random()
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
        uiState = uiState.copy(showPopup = false)
    }

    fun removeError() {
        uiState = uiState.copy(showError = false)
    }
}