package com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
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

    private val tolerance = 70f
    private val spacing = 6f

    private var cachedGuides: List<List<Offset>> = emptyList()
    private var cachedFrame: Rect? = null

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

    // ✅ START ONLY FROM FIRST POINT
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

    // ✅ STRICT CONTINUOUS TRACING (MAIN FIX)
    fun updateStroke(touch: Offset) {

        if (!uiState.isOnStroke) return

        val guide = cachedGuides.getOrNull(uiState.strokeIndex) ?: return

        val currentIndex = uiState.progressIndex

        val searchEnd = (currentIndex + 15).coerceAtMost(guide.lastIndex)
        val searchRange = (currentIndex + 1)..searchEnd

        var foundIndex = -1
        var minDistance = Float.MAX_VALUE

        for (i in searchRange) {
            val d = distance(touch, guide[i])
            if (d < tolerance && d < minDistance) {
                minDistance = d
                foundIndex = i
            }
        }

        if (foundIndex == -1) return
        if (foundIndex < currentIndex) return

        val newPoints = uiState.drawnPoints + guide.subList(
            currentIndex + 1,
            foundIndex + 1
        )

        // ✅ STROKE COMPLETED
        if (foundIndex == guide.lastIndex) {

            val nextStrokeIndex = uiState.strokeIndex + 1
            val nextGuide = cachedGuides.getOrNull(nextStrokeIndex)

            // 🔥 CHECK CONTINUATION
            if (nextGuide != null) {

                val nextStart = nextGuide.first()

                if (distance(touch, nextStart) <= tolerance) {

                    // 👉 CONTINUE SAME TOUCH
                    uiState = uiState.copy(
                        finishedStrokes = uiState.finishedStrokes + listOf(newPoints),
                        drawnPoints = listOf(nextStart),
                        strokeIndex = nextStrokeIndex,
                        progressIndex = 0,
                        isOnStroke = true // 🔥 KEEP TOUCH ACTIVE
                    )

                    return
                }
            }

            // ❌ NORMAL END
            uiState = uiState.copy(
                finishedStrokes = uiState.finishedStrokes + listOf(newPoints),
                drawnPoints = emptyList(),
                strokeIndex = nextStrokeIndex,
                progressIndex = 0,
                isOnStroke = false
            )

        } else {

            uiState = uiState.copy(
                drawnPoints = newPoints,
                progressIndex = foundIndex
            )
        }
    }

    fun changeMode(mode: LetterMode) {
        uiState = uiState.copy(
            mode = mode,
//            currentIndex = 0,
            strokeIndex = 0,
            progressIndex = 0,
            drawnPoints = emptyList(),
            finishedStrokes = emptyList(),
            isOnStroke = false
        )

        cachedGuides = emptyList()
        cachedFrame = null
    }

    fun clear() {
        uiState = uiState.copy(
            strokeIndex = 0,
            progressIndex = 0,
            drawnPoints = emptyList(),
            finishedStrokes = emptyList(),
            isOnStroke = false
        )

        cachedGuides = emptyList()
        cachedFrame = null
    }

    fun next() {
        val nextIndex = (uiState.currentIndex + 1) % letters.size

        uiState = uiState.copy(
            currentIndex = nextIndex,
            strokeIndex = 0,
            progressIndex = 0,
            drawnPoints = emptyList(),
            finishedStrokes = emptyList(),
            isOnStroke = false
        )

        cachedGuides = emptyList()
        cachedFrame = null
    }

    fun previous() {
        val prevIndex = if (uiState.currentIndex == 0)
            letters.lastIndex
        else
            uiState.currentIndex - 1

        uiState = uiState.copy(
            currentIndex = prevIndex,
            strokeIndex = 0,
            progressIndex = 0,
            drawnPoints = emptyList(),
            finishedStrokes = emptyList(),
            isOnStroke = false
        )

        cachedGuides = emptyList()
        cachedFrame = null
    }
}