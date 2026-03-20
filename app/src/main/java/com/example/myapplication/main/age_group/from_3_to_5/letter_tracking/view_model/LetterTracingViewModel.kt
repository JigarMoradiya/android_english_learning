package com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.ViewModel
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

        // 🔥 allow small forward window (smooth tracing)
        val searchRange = (currentIndex + 1)..(currentIndex + 8).coerceAtMost(guide.lastIndex)

        var foundIndex = -1

        for (i in searchRange) {
            if (distance(touch, guide[i]) <= tolerance) {
                foundIndex = i
                break
            }
        }

        // ❌ nothing found → ignore
        if (foundIndex == -1) return

        // ❌ prevent backward
        if (foundIndex < currentIndex) return

        val newPoints = guide.subList(0, foundIndex + 1)

        // ✅ COMPLETE STROKE
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

    fun reset() {
        uiState = LetterTracingUiState()
        cachedGuides = emptyList()
        cachedFrame = null
    }

    // ---------------- utils ----------------

    private fun scale(p: Offset, rect: Rect): Offset {
        return Offset(
            rect.left + p.x * rect.width,
            rect.top + p.y * rect.height
        )
    }

    private fun sampleGuide(
        stroke: List<Offset>,
        rect: Rect,
        step: Float
    ): List<Offset> {

        val result = mutableListOf<Offset>()
        var last: Offset? = null

        stroke.forEach { pt ->
            val scaled = scale(pt, rect)

            if (last != null) {
                result += interpolate(last!!, scaled, step)
            }

            last = scaled
        }

        return result
    }

    private fun interpolate(a: Offset, b: Offset, step: Float): List<Offset> {

        val res = mutableListOf<Offset>()

        val dx = b.x - a.x
        val dy = b.y - a.y

        val dist = hypot(dx, dy)
        val steps = maxOf(1, (dist / step).toInt())

        for (i in 0..steps) {
            val t = i / steps.toFloat()
            res.add(Offset(a.x + dx * t, a.y + dy * t))
        }

        return res
    }

    private fun distance(a: Offset, b: Offset): Float {
        return hypot(a.x - b.x, a.y - b.y)
    }
}