package com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.ViewModel
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.tracing.distance
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.tracing.sampleGuide
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.hypot

@HiltViewModel
class LetterTracingViewModel @Inject constructor() : ViewModel() {

    var uiState by mutableStateOf(LetterTracingUiState())
        private set

    private val tolerance = 70f
    private val spacing = 6f

    private var cachedGuides: List<List<Offset>> = emptyList()
    private var cachedFrame: Rect? = null

    // ✅ A letter strokes
    private val strokes = listOf(
        listOf(Offset(0.5f, 0.1f), Offset(0.25f, 0.9f)),
        listOf(Offset(0.5f, 0.1f), Offset(0.75f, 0.9f)),
        listOf(Offset(0.40f, 0.5f), Offset(0.60f, 0.5f))
    )

    fun getGuides(frame: Rect): List<List<Offset>> {

        if (cachedFrame == frame && cachedGuides.isNotEmpty()) {
            return cachedGuides
        }

        cachedFrame = frame

        cachedGuides = strokes.map { stroke ->
            sampleGuide(stroke, frame, spacing)
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
        if (foundIndex < currentIndex) return // no backward

        val newPoints = uiState.drawnPoints + guide.subList(
            currentIndex + 1,
            foundIndex + 1
        )

        if (foundIndex == guide.lastIndex) {

            uiState = uiState.copy(
                finishedStrokes = uiState.finishedStrokes + listOf(newPoints),
                drawnPoints = emptyList(),
                strokeIndex = uiState.strokeIndex + 1,
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
            strokeIndex = 0,
            progressIndex = 0,
            drawnPoints = emptyList(),
            finishedStrokes = emptyList(),
            isOnStroke = false
        )

        // 🔥 reset cache (IMPORTANT)
        cachedGuides = emptyList()
        cachedFrame = null
    }

    fun reset() {
        uiState = LetterTracingUiState()
        cachedGuides = emptyList()
        cachedFrame = null
    }

    fun previous() {

    }
    fun next() {

    }
}