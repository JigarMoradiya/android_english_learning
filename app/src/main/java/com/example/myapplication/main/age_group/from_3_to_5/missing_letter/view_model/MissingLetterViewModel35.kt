package com.example.myapplication.main.age_group.from_3_to_5.missing_letter.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.generation.letter.LetterRepository
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.SessionRepository
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
class MissingLetterViewModel35 @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val wrongAttemptsInBatch = mutableSetOf<String>()
    private val correctAttemptsInBatch = mutableSetOf<String>()
    private var currentRoundHadWrong = false
    private var batchStartMs: Long = System.currentTimeMillis()
    private var batchWords: List<String> = emptyList()


    private val allWordsEasy = LetterRepository.missingLetterEasyWords + LetterRepository.missingLetterEasyWords4Basic
    private val allWordsMedium = LetterRepository.missingLetterMediumWords + LetterRepository.missingLetterEasyWords4Basic

    var uiState by mutableStateOf(MissingLetterUiState35())
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
        pickBatch()
        setupWord(batchWords.firstOrNull() ?: "CAT")
    }

    private fun wordsForLevel(): List<String> =
        if (difficulty.value == DifficultyLevel.EASY) allWordsEasy else allWordsMedium

    private fun pickBatch() {
        batchWords = wordsForLevel().shuffled().take(uiState.totalRounds)
    }

    fun loadNextWord() {
        if (uiState.round >= uiState.totalRounds) {
            val duration = ((System.currentTimeMillis() - batchStartMs) / 1000).toInt()
            sessionRepository.record(
                LearningSession(
                    moduleId = ModuleID.MISSING_LETTER,
                    ageGroup = AgeGroup.THREE_TO_FIVE,
                    durationSeconds = duration,
                    score = uiState.correctCount,
                    totalQuestions = uiState.totalRounds,
                    wrongItems = wrongAttemptsInBatch.toList(),
                    correctItems = correctAttemptsInBatch.toList(),
                    subConfig = difficulty.value.name
                )
            )
            uiState = uiState.copy(showResult = true, showSuccess = false)
        } else {
            currentRoundHadWrong = false
            uiState = uiState.copy(round = uiState.round + 1)
            setupWord(batchWords[uiState.round - 1])
        }
    }

    fun restartGame() {
        wrongAttemptsInBatch.clear()
        correctAttemptsInBatch.clear()
        currentRoundHadWrong = false
        batchStartMs = System.currentTimeMillis()
        uiState = uiState.copy(round = 1, correctCount = 0, showResult = false)
        pickBatch()
        setupWord(batchWords.firstOrNull() ?: "CAT")
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
            1
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
        val extraLetter = when (blankCount) {
            1 -> {
                3
            }
            2 -> {
                4
            }
            else -> {
                5
            }
        }
        while (pool.size < extraLetter) {

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
            showSuccess = false
        )
    }

    fun clearDrag() {
        dragging = null
        dragPosition = null
        dragFromIndex = null

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
                AudioPlayerManager.playSoundCorrectAnswer()
                if (!currentRoundHadWrong) {
                    correctAttemptsInBatch.add(targetWord)
                }
                val newCorrectCount = if (!currentRoundHadWrong) uiState.correctCount + 1 else uiState.correctCount
                uiState = uiState.copy(
                    showSuccess = true,
                    feedbackTextRes = feedbackTitles.random(),
                    feedbackSubTextRes = feedbackMissingLetter.random(),
                    showError = false,
                    countdownValue = 3,
                    correctCount = newCorrectCount
                )
                viewModelScope.launch {
                    delay(1000)
                    uiState = uiState.copy(countdownValue = 2)
                    delay(1000)
                    uiState = uiState.copy(countdownValue = 1)
                    delay(1000)
                    loadNextWord()
                }
            } else {
                currentRoundHadWrong = true
                wrongAttemptsInBatch.add(targetWord)
                val badSlots = mutableSetOf<Int>()
                targetWord.forEachIndexed { i, ch ->
                    if (!fixedIndices.contains(i)) {
                        val placed = dropped.getOrNull(i)
                        if (placed != null && placed.letter != ch.toString()) {
                            badSlots.add(i)
                        }
                    }
                }
                AudioPlayerManager.playSoundWrongAnswer()
                uiState = uiState.copy(
                    showError = true,
                    feedbackTextRes = feedbackWrong.random(),
                    feedbackSubTextRes = feedbackMissingLetterSubTitleForWrong.random(),
                    wrongSlots = badSlots,
                )
            }
        }else{
            AudioPlayerManager.playSoundDragItem()
        }
    }

    fun closePopup() {
        uiState = uiState.copy(showSuccess = false)
    }

    fun removeError() {
        uiState = uiState.copy(showError = false, wrongSlots = emptySet())
    }
}