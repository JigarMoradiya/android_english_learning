package com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.generation.letter.LetterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class ColoringAlphabetsViewModel @Inject constructor() : ViewModel() {

    // ✅ use your repository
    private val items = LetterRepository.colorAlphabets

    var uiState by mutableStateOf(
        ColoringAlphabetsUiState(
            items = items,
            currentIndex = 0,
            strokes = emptyList()
        )
    )
        private set

    private var currentPoints = mutableListOf<Offset>()
    var currentStroke by mutableStateOf<List<Offset>>(emptyList())
        private set
    fun startStroke(point: Offset) {
        currentPoints = mutableListOf(point)
        currentStroke = listOf(point)
    }

    fun addPoint(point: Offset) {
        currentPoints.add(point)
        currentStroke = currentPoints.toList()
    }

    fun endStroke() {
        if (currentPoints.isNotEmpty()) {
            uiState = uiState.copy(
                strokes = uiState.strokes + StrokeData(
                    points = currentPoints.toList(),
                    strokeWidth = uiState.strokeSize,
                    color = uiState.selectedColor,
                    isEraser = uiState.isEraser   // 🔥 KEY
                )
            )
        }
        currentPoints.clear()
        currentStroke = emptyList()
    }

    fun clear() {
        uiState = uiState.copy(strokes = emptyList())
    }

    fun next() {
        val next = (uiState.currentIndex + 1) % items.size
        uiState = uiState.copy(currentIndex = next, strokes = emptyList())
    }

    fun previous() {
        val prev =
            if (uiState.currentIndex == 0) items.lastIndex
            else uiState.currentIndex - 1

        uiState = uiState.copy(currentIndex = prev, strokes = emptyList())
    }

    fun selectColor(color: Color) {
        uiState = uiState.copy(selectedColor = color, isEraser = false)
    }
    fun selectEraser() {
        uiState = uiState.copy(isEraser = true)
    }
}