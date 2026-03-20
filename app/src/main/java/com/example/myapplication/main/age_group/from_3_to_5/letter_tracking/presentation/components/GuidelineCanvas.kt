package com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect


@Composable
fun GuidelineCanvas() {

    Canvas(modifier = Modifier.fillMaxSize()) {

        val startY = 0.1f
        val endY = 0.9f
        val section = (endY - startY) / 3f

        val line1 = size.height * startY
        val line2 = size.height * (startY + section)
        val line3 = size.height * (startY + 2 * section)
        val line4 = size.height * endY

        drawRoundRect(
            color = Color.White,
            size = size,
            cornerRadius = CornerRadius(20f)
        )

        val dash = PathEffect.dashPathEffect(floatArrayOf(12f, 8f))

        listOf(line1, line2, line3, line4).forEachIndexed { i, y ->

            drawLine(
                color = if (i == 0 || i == 3)
                    Color.Red.copy(alpha = 0.3f)
                else
                    Color.Blue.copy(alpha = 0.2f),
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 4f,
                pathEffect = dash
            )
        }
    }
}