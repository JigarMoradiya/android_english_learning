package com.example.myapplication.main.age_group.from_3_to_5.match_letter_with_image.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap

@Composable
fun DragLine(
    start: Offset?,
    end: Offset?,
    color: Color
) {
    if (start == null || end == null) return

    Canvas(modifier = Modifier.fillMaxSize()) {

        // ✨ Outer glow (soft)
        drawLine(
            color = color.copy(alpha = 0.25f),
            start = start,
            end = end,
            strokeWidth = 20f,
            cap = StrokeCap.Round
        )

        // 🎯 Main line
        drawLine(
            color = color,
            start = start,
            end = end,
            strokeWidth = 8f,
            cap = StrokeCap.Round
        )
    }
}