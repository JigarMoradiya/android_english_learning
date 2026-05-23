package com.example.myapplication.main.age_group.from_5_to_7.word_match_picture.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.generation.letter.LetterRepository
import com.example.myapplication.data.generation.letter.LetterRepository.vocabularyCategoryAllForWordMatchImage
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.ModuleProgressRepository
import com.example.myapplication.data.progress.SessionRepository
import com.example.myapplication.data.progress.models.MatchLetterWithImageProgress
import com.example.myapplication.ui.theme.colorList
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.FeedbackConstant.feedbackMatchLetterSubtitles
import com.example.myapplication.utils.FeedbackConstant.feedbackTitles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.plus
import kotlin.math.abs

@HiltViewModel
class WordMatchImageViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val moduleProgressRepository: ModuleProgressRepository
) : ViewModel() {

    private val batchSize = 5

    var uiState by mutableStateOf(WordMatchImageUiState())
        private set

    var dragStart by mutableStateOf<Offset?>(null)
        private set

    var dragEnd by mutableStateOf<Offset?>(null)
        private set

    private var wrongAttemptsInBatch = mutableSetOf<String>()
    private var batchStartMs = System.currentTimeMillis()
    private var totalDrag = Offset.Zero

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

        wrongAttemptsInBatch = mutableSetOf()
        batchStartMs = System.currentTimeMillis()
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

        dragEnd = start + totalDrag
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
        } else {
            if (target != null) {
                wrongAttemptsInBatch.add(letter)
            }
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

        // show popup when all matched
        if (updatedSet.size == uiState.batchLetters.size) {
            // COMPLETE
            viewModelScope.launch {
                delay(300)
                val score = batchSize - wrongAttemptsInBatch.size
                val stars = computeStars(score, batchSize)
                recordSession(score)
                AudioPlayerManager.playSoundClap()
                uiState = uiState.copy(
                    matchedLetters = updatedSet,
                    matchedOrder = updatedOrder,
                    batchScore = score,
                    earnedStars = stars,
                    scoreLabel = if (score == batchSize) "perfect! 🎯" else "first try 🎯",
                    feedbackTextRes = feedbackTitles.random(),
                    feedbackSubTextRes = feedbackMatchLetterSubtitles.random(),
                    showPopup = true
                )
            }
        } else {
            // MATCH
            AudioPlayerManager.playSoundCorrectAnswer()
            uiState = uiState.copy(matchedLetters = updatedSet, matchedOrder = updatedOrder)
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

        if (uiState.framesReady) return

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

    // -----------------------------
    // SESSION RECORDING
    // -----------------------------
    private fun recordSession(score: Int) {
        val duration = ((System.currentTimeMillis() - batchStartMs) / 1000).toInt()
        sessionRepository.record(
            LearningSession(
                moduleId = ModuleID.MATCH_WORD_WITH_PICTURE,
                ageGroup = AgeGroup.FIVE_TO_SEVEN,
                durationSeconds = duration,
                score = score,
                totalQuestions = batchSize
            )
        )

        val progress = moduleProgressRepository.load(ModuleID.MATCH_WORD_WITH_PICTURE, MatchLetterWithImageProgress::class.java)
            ?: MatchLetterWithImageProgress()

        val alphabet = ('A'..'Z').toList()
        val newWeakIndices = wrongAttemptsInBatch
            .mapNotNull { word ->
                val ch = word.firstOrNull()?.uppercaseChar() ?: return@mapNotNull null
                alphabet.indexOf(ch).takeIf { it >= 0 }
            }
            .filter { it !in progress.weakLetterIndices }

        moduleProgressRepository.save(
            progress.copy(
                totalRoundsCompleted = progress.totalRoundsCompleted + 1,
                weakLetterIndices = progress.weakLetterIndices + newWeakIndices
            ),
            ModuleID.MATCH_WORD_WITH_PICTURE
        )
    }

    // -----------------------------
    // HELPERS
    // -----------------------------
    private fun computeStars(score: Int, total: Int): Int {
        val ratio = score.toDouble() / total
        return when {
            ratio >= 1.0 -> 3
            ratio >= 0.6 -> 2
            else -> 1
        }
    }
}
