package com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.common.AppToolbarDropDownOnRight
import com.example.myapplication.data.generation.letter.LetterRepository
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.helper.distance
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.helper.getStrokesForLetter
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.helper.sampleStroke
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.presentation.BottomTracingControls
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.presentation.CenterLearningLayout
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.getImageResFromWord
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.utils.extensions.OtherEx.safeAction
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AlphabetTracingViewModel @Inject constructor() : ViewModel() {

    var uiState by mutableStateOf(LetterTracingUiState())
        private set

    var isPreviewMode: Boolean = false

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
    // 🎨 COLORS – 5 per letter, chosen to contrast with each letter's image
    // -------------------------------


    private val selectedColors: List<Color> = letterPalettes.map { it.random() }

    fun getLetterColor(): Color {
        return selectedColors[uiState.currentIndex % selectedColors.size]
    }

    fun previewAllStrokes(guides: List<List<Offset>>) {
        if (uiState.finishedStrokes.isEmpty() && guides.isNotEmpty()) {
            uiState = uiState.copy(
                finishedStrokes = guides,
                strokeIndex = guides.size
            )
        }
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
        if (uiState.drawnPoints.isNotEmpty() || uiState.finishedStrokes.isNotEmpty()) {
            resetState()
        }
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

}