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

// ----------------------------
// Color Option
// ----------------------------
sealed class ColorOption(val id: String) {

    data class Solid(val color: Color, val colorId: String) : ColorOption(colorId)

    data class Gradient(
        val colors: List<Color>,
        val colorId: String
    ) : ColorOption(colorId)

    fun toBrush(): Brush {
        return when (this) {
            is Solid -> Brush.linearGradient(listOf(color, color))
            is Gradient -> Brush.linearGradient(colors)
        }
    }
}

// ----------------------------
// Stroke
// ----------------------------
data class Stroke(
    val points: List<Offset>,
    val color: ColorOption,
    val strokeWidth: Float
)

// ----------------------------
// ViewModel
// ----------------------------
@HiltViewModel
class ColoringAlphabetsViewModel @Inject constructor() : ViewModel() {

    // 🔤 Items (same as iOS)
    var items = LetterRepository.colorAlphabets

    var currentIndex by mutableIntStateOf(0)
        private set

    val currentItem
        get() = items.getOrNull(currentIndex)

    // 🎨 Drawing
    var strokes by mutableStateOf<List<Stroke>>(emptyList())
        private set

    var currentColor by mutableStateOf<ColorOption>(
        ColorOption.Solid(Color.Red, "red")
    )

    var brushWidth by mutableFloatStateOf(20f)

    var isEraser by mutableStateOf(false)

    private var mediaPlayer: MediaPlayer? = null

    var currentStroke by mutableStateOf<List<Offset>>(emptyList())

    fun startStroke(point: Offset) {
        currentStroke = listOf(point)
    }

    fun addPoint(point: Offset) {
        currentStroke = currentStroke + point
    }

    fun breakStroke() {
        if (currentStroke.isNotEmpty()) {
            strokes = strokes + Stroke(
                points = currentStroke,
                color = currentColor,
                strokeWidth = brushWidth
            )
            currentStroke = emptyList()
        }
    }
    fun endStroke() {
        if (currentStroke.isNotEmpty()) {
            strokes = strokes + Stroke(
                points = currentStroke,
                color = currentColor,
                strokeWidth = brushWidth
            )
        }
        currentStroke = emptyList()
    }

    // ----------------------------
    // Navigation
    // ----------------------------
    fun nextItem(context: Context) {
        clearCanvas()

        currentIndex =
            if (currentIndex < items.lastIndex) currentIndex + 1 else 0

        playAudio(context)
    }

    fun previousItem(context: Context) {
        clearCanvas()

        currentIndex =
            if (currentIndex > 0) currentIndex - 1 else items.lastIndex

        playAudio(context)
    }

    // ----------------------------
    // Drawing
    // ----------------------------
    fun addStroke(stroke: Stroke) {
        strokes = strokes + stroke
    }

    fun clearCanvas() {
        strokes = emptyList()
    }

    // ----------------------------
    // Audio
    // ----------------------------
    fun playAudio(context: Context) {
        val name = currentItem?.audioName ?: return

        val resId = context.resources.getIdentifier(
            name,
            "raw",
            context.packageName
        )

        if (resId == 0) return

        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, resId)
        mediaPlayer?.start()
    }
}