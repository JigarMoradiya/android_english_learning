package com.example.myapplication.main.age_group.from_5_to_7.drag_and_drop_word.view_model

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
import com.example.myapplication.main.age_group.from_5_to_7.missing_letter.view_model.DifficultyLevel
import com.example.myapplication.main.age_group.from_5_to_7.missing_letter.view_model.LetterItem
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
class DragDropWordViewModel57 @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val allWordsEasy = LetterRepository.missingLetterEasyWords + LetterRepository.missingLetterEasyWords4Basic
    private val allWordsMedium = LetterRepository.missingLetterMediumWords + LetterRepository.missingLetterEasyWords4Basic

    private val wrongAttemptsInBatch = mutableSetOf<String>()
    private val correctAttemptsInBatch = mutableSetOf<String>()
    private var currentRoundHadWrong = false
    private var batchStartMs: Long = System.currentTimeMillis()
    private var batchWords: List<String> = emptyList()

    var uiState by mutableStateOf(DragDropWordUiState57())
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
        pickBatch()
        setupWord(batchWords.firstOrNull() ?: "CAT")
    }

    private fun wordsForLevel() = if (difficulty.value == DifficultyLevel.EASY) allWordsEasy else allWordsMedium

    private fun pickBatch() {
        val batch = wordsForLevel().shuffled().take(uiState.totalRounds).toMutableList()
        if (difficulty.value != DifficultyLevel.EASY && batch.isNotEmpty()) {
            // Long challenge words with image assets — one is guaranteed per jigsaw game
            batch[batch.lastIndex] = listOf("Elephant", "Dinosaur").random()
        }
        // Short words first, the big one last
        batchWords = batch.sortedBy { it.length }
    }

    fun loadNextWord() {
        if (uiState.round >= uiState.totalRounds) {
            val duration = ((System.currentTimeMillis() - batchStartMs) / 1000).toInt()
            sessionRepository.record(LearningSession(
                moduleId = ModuleID.WORD_JIGSAW,
                ageGroup = AgeGroup.FIVE_TO_SEVEN,
                durationSeconds = duration,
                score = uiState.correctCount,
                totalQuestions = uiState.totalRounds,
                wrongItems = wrongAttemptsInBatch.toList(),
                correctItems = correctAttemptsInBatch.toList(),
                subConfig = difficulty.value.name
            ))
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

    fun setupWord(word: String) {
        val upper = word.uppercase().replace("-", "")
        targetWord = upper
        dropped = MutableList(upper.length) { null }
        fixedIndices = emptySet()
        letters = upper.map { ch -> LetterItem(ch.toString()) }.shuffled()
        clearDrag()
        uiState = uiState.copy(showError = false, showSuccess = false)
    }

    fun clearDrag() { dragging = null; dragPosition = null }
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
                if (!currentRoundHadWrong) correctAttemptsInBatch.add(targetWord)
                val newCorrectCount = if (!currentRoundHadWrong) uiState.correctCount + 1 else uiState.correctCount
                AudioPlayerManager.playSoundCorrectAnswer()
                uiState = uiState.copy(
                    showSuccess = true,
                    feedbackTextRes = feedbackTitles.random(),
                    feedbackSubTextRes = feedbackMissingLetter.random(),
                    showError = false,
                    countdownValue = 3,
                    correctCount = newCorrectCount
                )
                viewModelScope.launch {
                    delay(1000); uiState = uiState.copy(countdownValue = 2)
                    delay(1000); uiState = uiState.copy(countdownValue = 1)
                    delay(1000); loadNextWord()
                }
            } else {
                currentRoundHadWrong = true
                wrongAttemptsInBatch.add(targetWord)
                AudioPlayerManager.playSoundWrongAnswer()
                uiState = uiState.copy(
                    showError = true,
                    feedbackTextRes = feedbackWrong.random(),
                    feedbackSubTextRes = feedbackMissingLetterSubTitleForWrong.random()
                )
            }
        } else {
            AudioPlayerManager.playSoundDragItem()
        }
    }

    fun closePopup() { uiState = uiState.copy(showSuccess = false) }
    fun removeError() { uiState = uiState.copy(showError = false) }
}
