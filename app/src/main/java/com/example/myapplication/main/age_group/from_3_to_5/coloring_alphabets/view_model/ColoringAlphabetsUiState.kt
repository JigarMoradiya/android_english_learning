package com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.myapplication.ui.theme.AppDimens
import com.example.myapplication.ui.theme.colorList
import com.example.myapplication.ui.theme.gradientBrushList


data class ColoringAlphabetsUiState(
    val items: List<ColoringAlphabetModel> = emptyList(),
    val currentIndex: Int = 0,
    val strokes: List<StrokeData> = emptyList(),
    val brushes: List<Brush> = gradientBrushList,
    val selectedBrush: Brush = gradientBrushList.first(),
    val strokeSize: Float = if (AppDimens.isTablet) 90F else 70F,
    val isEraser: Boolean = false
)

data class StrokeData(
    val points: List<Offset>,
    val strokeWidth: Float,
    val brush: Brush,
    val isEraser: Boolean = false
)