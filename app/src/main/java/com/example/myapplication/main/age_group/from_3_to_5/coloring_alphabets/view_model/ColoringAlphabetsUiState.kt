package com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.myapplication.R
import com.example.myapplication.utils.AppUtils.colorList
import java.util.UUID


data class ColoringAlphabetsUiState(
    val items: List<ColoringAlphabetModel> = emptyList(),
    val currentIndex: Int = 0,
    val strokes: List<StrokeData> = emptyList(),
    val colors: List<Color> = colorList,
    val selectedColor: Color = colorList.first(), // default first
    val strokeSize: Float = 50f,
    val isEraser: Boolean = false
)

data class StrokeData(
    val points: List<Offset>,
    val strokeWidth: Float,
    val color: Color,
    val isEraser: Boolean = false
)