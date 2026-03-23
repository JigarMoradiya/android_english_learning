package com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.generation.letter.LetterRepository
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.tracing.distance
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.tracing.getStrokesForLetter
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.tracing.sampleStroke
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LetterTracingViewModel @Inject constructor() : ViewModel() {

    var uiState by mutableStateOf(LetterTracingUiState())
        private set

    private val letters = ('A'..'Z').toList()
    val lettersData: List<Pair<String, String>> =
        LetterRepository.all.map { data ->
            data.letter to data.mainWord
        }

    private val tolerance = 70f
    private val spacing = 6f

    private var cachedGuides: List<List<Offset>> = emptyList()
    private var cachedFrame: Rect? = null


    // -------------------------------
    // 🎨 COLORS
    // -------------------------------
    private val shuffledColors: List<Color> = listOf(
        Color(0xFFE53935),
        Color(0xFFECC720),
        Color(0xFF1E88E5),
        Color(0xFF43A047),
        Color(0xFF8E24AA),
        Color(0xFF512DA8),
        Color(0xFFFF7043),
        Color(0xFF00ACC1),
        Color(0xFFD81B60),
        Color(0xFF028576)
    ).shuffled()

    fun getLetterColor(): Color {
        return shuffledColors[uiState.currentIndex % shuffledColors.size]
    }

    val currentLetter: Char
        get() {
            val base = letters[uiState.currentIndex]
            return if (uiState.mode == LetterMode.UPPERCASE) base else base.lowercaseChar()
        }

    // -------------------------------
    // 📐 GUIDES
    // -------------------------------
    fun getGuides(frame: Rect): List<List<Offset>> {

        if (cachedFrame == frame && cachedGuides.isNotEmpty()) {
            return cachedGuides
        }

        cachedFrame = frame

        val strokes = getStrokesForLetter(currentLetter, uiState.mode)

        cachedGuides = strokes.map {
            sampleStroke(it, frame, spacing)
        }

        return cachedGuides
    }

    // -------------------------------
    // ✋ START STROKE
    // -------------------------------
    fun startStroke(touch: Offset) {

        val guide = cachedGuides.getOrNull(uiState.strokeIndex) ?: return
        val startPoint = guide.first()

        if (distance(touch, startPoint) <= tolerance) {

            uiState = uiState.copy(
                isOnStroke = true,
                drawnPoints = listOf(startPoint),
                progressIndex = 0
            )
        }
    }

    // -------------------------------
    // 👉 TAP (DOT SUPPORT)
    // -------------------------------
    fun onTap(position: Offset) {

        val guide = cachedGuides.getOrNull(uiState.strokeIndex) ?: return

        if (guide.size <= 2) {
            val target = guide.first()
            if (distance(position, target) <= tolerance) {
                completeCurrentStroke()
            }
        }
    }

    private fun completeCurrentStroke() {
        uiState = uiState.copy(
            finishedStrokes = uiState.finishedStrokes + listOf(cachedGuides[uiState.strokeIndex]),
            strokeIndex = uiState.strokeIndex + 1,
            drawnPoints = emptyList(),
            progressIndex = 0,
            isOnStroke = false,
            isWaitingForNextStroke = true
        )
    }

    // -------------------------------
    // 🧲 FIND NEAREST POINT
    // -------------------------------
    private fun findNearestIndex(
        touch: Offset,
        guide: List<Offset>,
        start: Int,
        end: Int
    ): Int {

        var bestIndex = -1
        var minDist = Float.MAX_VALUE

        for (i in start..end) {
            val d = distance(touch, guide[i])
            if (d < minDist) {
                minDist = d
                bestIndex = i
            }
        }

        return if (bestIndex != -1 && minDist <= tolerance) bestIndex else -1
    }

    // -------------------------------
    // ✍️ UPDATE STROKE
    // -------------------------------
    fun updateStroke(touch: Offset) {

        val guides = cachedGuides

        // -------------------------------
        // 🔥 WAIT FOR NEXT STROKE
        // -------------------------------
        if (uiState.isWaitingForNextStroke) {

            val guide = guides.getOrNull(uiState.strokeIndex) ?: return
            val startPoint = guide.first()

            if (distance(touch, startPoint) <= tolerance) {

                uiState = uiState.copy(
                    isOnStroke = true,
                    isWaitingForNextStroke = false,
                    drawnPoints = listOf(startPoint),
                    progressIndex = 0
                )
            }

            return
        }

        // -------------------------------
        // NORMAL DRAWING
        // -------------------------------
        if (!uiState.isOnStroke) return

        val guide = guides.getOrNull(uiState.strokeIndex) ?: return

        val currentIndex = uiState.progressIndex
        val searchEnd = (currentIndex + 20).coerceAtMost(guide.lastIndex)

        val foundIndex = findNearestIndex(
            touch,
            guide,
            currentIndex + 1,
            searchEnd
        )

        if (foundIndex == -1) return
        if (foundIndex < currentIndex) return

        // -------------------------------
        // SNAP TO GUIDE (REAL SNAPPING)
        // -------------------------------
        val newPoints = uiState.drawnPoints + guide.subList(
            currentIndex + 1,
            foundIndex + 1
        )

        // -------------------------------
        // ✅ COMPLETE STROKE
        // -------------------------------
        if (foundIndex == guide.lastIndex) {

            val nextStrokeIndex = uiState.strokeIndex + 1

            uiState = uiState.copy(
                finishedStrokes = uiState.finishedStrokes + listOf(newPoints),
                drawnPoints = emptyList(),
                strokeIndex = nextStrokeIndex,
                progressIndex = 0,
                isOnStroke = false,
                isWaitingForNextStroke = true
            )

        } else {

            uiState = uiState.copy(
                drawnPoints = newPoints,
                progressIndex = foundIndex
            )
        }
    }

    // -------------------------------
    // 🔁 NAVIGATION
    // -------------------------------
    fun next() {
        val nextIndex = (uiState.currentIndex + 1) % letters.size
        resetForIndex(nextIndex)
    }

    fun previous() {
        val prevIndex =
            if (uiState.currentIndex == 0) letters.lastIndex
            else uiState.currentIndex - 1
        resetForIndex(prevIndex)
    }

    fun changeMode(mode: LetterMode) {
        uiState = uiState.copy(mode = mode)
        resetState()
    }

    fun clear() {
        resetState()
    }

    private fun resetForIndex(index: Int) {
        uiState = uiState.copy(currentIndex = index)
        resetState()
    }

    private fun resetState() {
        uiState = uiState.copy(
            strokeIndex = 0,
            progressIndex = 0,
            drawnPoints = emptyList(),
            finishedStrokes = emptyList(),
            isOnStroke = false,
            isWaitingForNextStroke = false
        )

        cachedGuides = emptyList()
        cachedFrame = null
    }

    fun playBackSound() {

    }
}