package com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.generation.letter.LetterRepository
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.SessionRepository
import com.example.myapplication.utilities.pref.AppPreferencesHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ColoringAlphabetsViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val prefs: AppPreferencesHelper
) : ViewModel() {

    private val items = LetterRepository.colorAlphabets
    private val visitedLetters = mutableSetOf<String>()
    private var startTimeMs: Long = System.currentTimeMillis()

    private fun letterForIndex(index: Int) = ('A'.code + index).toChar().toString()

    var uiState by mutableStateOf(
        ColoringAlphabetsUiState(
            items = items,
            currentIndex = prefs.getCustomParamInt("coloring_index", 0).coerceIn(0, items.lastIndex),
            strokes = emptyList()
        )
    )
        private set

    private var currentPoints = mutableListOf<Offset>()
    var currentStroke by mutableStateOf<List<Offset>>(emptyList())
        private set

    private val undoStack = mutableListOf<List<StrokeData>>()
    private val redoStack = mutableListOf<List<StrokeData>>()
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

            undoStack.add(uiState.strokes)
            redoStack.clear()

            uiState = uiState.copy(
                strokes = uiState.strokes + StrokeData(
                    points = currentPoints.toList(),
                    strokeWidth = uiState.strokeSize,
                    brush = uiState.selectedBrush,
                    isEraser = uiState.isEraser
                )
            )
        }
        currentPoints.clear()
        currentStroke = emptyList()
    }


    fun undo() {
        if (undoStack.isNotEmpty()) {

            // move current → redo
            redoStack.add(uiState.strokes)

            // restore last
            val previous = undoStack.removeAt(undoStack.lastIndex)

            uiState = uiState.copy(strokes = previous)
        }
    }
    fun redo() {
        if (redoStack.isNotEmpty()) {

            // move current → undo
            undoStack.add(uiState.strokes)

            val next = redoStack.removeAt(redoStack.lastIndex)

            uiState = uiState.copy(strokes = next)
        }
    }
    fun clear() {
        undoStack.add(uiState.strokes)
        redoStack.clear()

        uiState = uiState.copy(strokes = emptyList())
    }
    fun next() {
        visitedLetters.add(letterForIndex(uiState.currentIndex))
        val next = (uiState.currentIndex + 1) % items.size
        undoStack.clear()
        redoStack.clear()
        uiState = uiState.copy(currentIndex = next, strokes = emptyList())
        prefs.setCustomParamInt("coloring_index", next)
    }

    fun previous() {
        visitedLetters.add(letterForIndex(uiState.currentIndex))
        val prev = if (uiState.currentIndex == 0) items.lastIndex else uiState.currentIndex - 1
        undoStack.clear()
        redoStack.clear()
        uiState = uiState.copy(currentIndex = prev, strokes = emptyList())
        prefs.setCustomParamInt("coloring_index", prev)
    }

    override fun onCleared() {
        super.onCleared()
        if (visitedLetters.isEmpty()) return
        val duration = ((System.currentTimeMillis() - startTimeMs) / 1000).toInt()
        sessionRepository.record(LearningSession(
            moduleId = ModuleID.COLORING_ALPHABETS,
            ageGroup = AgeGroup.THREE_TO_FIVE,
            durationSeconds = duration,
            score = visitedLetters.size,
            totalQuestions = 0,
            correctItems = visitedLetters.sorted()
        ))
    }
    fun selectBrush(brush: Brush) {
        uiState = uiState.copy(
            selectedBrush = brush,
            isEraser = false
        )
    }
    fun selectEraser() {
        uiState = uiState.copy(
            isEraser = !uiState.isEraser
        )
    }
}