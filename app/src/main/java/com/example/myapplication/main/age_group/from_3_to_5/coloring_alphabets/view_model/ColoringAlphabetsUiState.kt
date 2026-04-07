package com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model

import androidx.compose.ui.geometry.Offset
import com.example.myapplication.R
import java.util.UUID


data class ColoringAlphabetsUiState(
    val items: List<ColoringAlphabetModel> = emptyList(),
    val currentIndex: Int = 0,
    val strokes: List<StrokeData> = emptyList()
)

data class StrokeData(
    val points: List<Offset>,
    val strokeWidth: Float
)