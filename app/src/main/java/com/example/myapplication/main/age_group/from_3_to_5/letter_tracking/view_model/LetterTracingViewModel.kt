package com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.view_model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.ViewModel
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.tracing.LetterSkeleton
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.tracing.StrokeSegment
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.tracing.distance
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.tracing.sampleGuide
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.hypot

@HiltViewModel
class LetterTracingViewModel @Inject constructor() : ViewModel() {

    // =========================
    // UI STATE
    // =========================
    var uiState by mutableStateOf(LetterTracingUiState())
        private set

    // =========================
    // CONFIG
    // =========================
    private val tolerance = 40f
    private val guideSpacing = 6f

    // =========================
    // STABLE GUIDES CACHE (IMPORTANT FIX)
    // =========================
    private var cachedFrame: Rect? = null
    private var cachedGuides: List<List<Offset>> = emptyList()

    // =========================
    // LETTER STROKES (A SAMPLE)
    // =========================
    val strokes: List<List<Offset>> = listOf(
        listOf(Offset(0.5f, 0.1f), Offset(0.25f, 0.9f)), // left line
        listOf(Offset(0.5f, 0.1f), Offset(0.75f, 0.9f)), // right line
        listOf(Offset(0.36f, 0.5f), Offset(0.64f, 0.5f)) // middle line
    )

    // =========================
    // GET GUIDES (CACHED)
    // =========================
    fun getGuides(frame: Rect): List<List<Offset>> {

        // 🔥 VERY IMPORTANT: prevent regeneration
        if (cachedFrame == frame && cachedGuides.isNotEmpty()) {
            return cachedGuides
        }

        cachedFrame = frame

        cachedGuides = strokes.map { stroke ->
            sampleGuide(stroke, frame, guideSpacing)
        }

        return cachedGuides
    }

    // =========================
    // START STROKE
    // =========================
    fun startStroke(touch: Offset) {

        val guides = cachedGuides
        val guide = guides.getOrNull(uiState.strokeIndex) ?: return

        val startPoint = guide.first()

        val isNearStart = distance(touch, startPoint) <= tolerance
        val isNearPath = guide.any { distance(touch, it) <= tolerance }

        if (isNearStart || isNearPath) {

            uiState = uiState.copy(
                isOnStroke = true,
                drawnPoints = listOf(startPoint),
                progressIndex = 0
            )

            Log.d("TRACE", "Stroke Started at index = ${uiState.strokeIndex}")
        }
    }

    // =========================
    // UPDATE STROKE (FINAL FIX)
    // =========================
    fun updateStroke(touch: Offset) {

        if (!uiState.isOnStroke) return

        val guides = cachedGuides
        val guide = guides.getOrNull(uiState.strokeIndex) ?: return

        // 🔥 find closest valid point
        val closestIndex = guide.indexOfFirst {
            distance(touch, it) <= tolerance
        }

        if (closestIndex != -1 && closestIndex >= uiState.progressIndex) {

            val newPoints = guide.subList(0, closestIndex + 1)

            if (closestIndex == guide.lastIndex) {

                uiState = uiState.copy(
                    finishedStrokes = uiState.finishedStrokes + listOf(newPoints),
                    drawnPoints = emptyList(),
                    strokeIndex = uiState.strokeIndex + 1,
                    progressIndex = 0,
                    isOnStroke = false
                )

                Log.d("TRACE", "Stroke Completed → strokeIndex = ${uiState.strokeIndex}")

            } else {

                uiState = uiState.copy(
                    drawnPoints = newPoints,
                    progressIndex = closestIndex
                )
            }
        }
    }

    // =========================
    // RESET
    // =========================
    fun reset() {
        uiState = LetterTracingUiState()
        cachedGuides = emptyList()
        cachedFrame = null
    }

    // =========================
    // UTILS
    // =========================
    private fun scale(point: Offset, rect: Rect): Offset {
        return Offset(
            rect.left + point.x * rect.width,
            rect.top + point.y * rect.height
        )
    }

    private fun sampleGuide(
        stroke: List<Offset>,
        rect: Rect,
        spacing: Float
    ): List<Offset> {

        val guide = mutableListOf<Offset>()
        var last: Offset? = null

        stroke.forEach { pt ->
            val scaled = scale(pt, rect)

            if (last != null) {
                guide += interpolate(last!!, scaled, spacing)
            }

            last = scaled
        }

        return guide
    }

    private fun interpolate(a: Offset, b: Offset, step: Float): List<Offset> {

        val result = mutableListOf<Offset>()

        val dx = b.x - a.x
        val dy = b.y - a.y

        val dist = hypot(dx, dy)
        val steps = maxOf(1, (dist / step).toInt())

        for (i in 0..steps) {
            val t = i / steps.toFloat()
            result.add(
                Offset(
                    a.x + dx * t,
                    a.y + dy * t
                )
            )
        }

        return result
    }

    private fun distance(a: Offset, b: Offset): Float {
        return hypot(a.x - b.x, a.y - b.y)
    }
}
