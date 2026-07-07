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
    val selectedStyle: BrushTexture = BrushTexture.FLAT,
    // Matches iOS's DimensionsUtils.colorLetterBrushWidth tiers (40/60/80) —
    // this used to be 70/90, nearly double iOS's, which is why strokes
    // looked like they colored well past where the finger actually moved.
    val strokeSize: Float = if (AppDimens.isLargeTablet) 80F else if (AppDimens.isTablet) 60F else 40F,
    val isEraser: Boolean = false
)

data class StrokeData(
    val id: Long = System.nanoTime(),
    val points: List<Offset>,
    val strokeWidth: Float,
    val brush: Brush,
    val style: BrushTexture = BrushTexture.FLAT,
    val isEraser: Boolean = false,
    // Must match the seed used while the stroke was still live (see
    // ColoringAlphabetsViewModel.endStroke) so the procedural texture's
    // pseudo-random dots don't jump the instant the stroke is committed.
    val seed: Int = 0
)

// How a stroke's fill actually renders on the canvas — separate from its
// color. Procedural stand-ins for real brush artwork (no texture assets
// exist yet): flat is a plain smooth line, the rest fake a texture with
// simple shapes so each brush type visibly looks different while drawing.
enum class BrushTexture {
    FLAT, GRAIN, SPARKLE, DOTTED
}