package com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.myapplication.R
import java.util.UUID


data class ColoringAlphabetsUiState(
    val items: List<ColoringAlphabetModel> = emptyList(),
    val currentIndex: Int = 0,
    val strokes: List<StrokeData> = emptyList(),
    val colors: List<Color> = listOf(
        Color.Red,
        Color.Green,
        Color.Blue,
        Color.Yellow,
        Color.Magenta
    ),
    val selectedColor: Color = Color.Red, // default first
    val strokeSize: Float = 50f,
)

data class StrokeData(
    val points: List<Offset>,
    val strokeWidth: Float,
    val color: Color
)