package com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.min

@Composable
fun GuidelineCanvas(
    modifier: Modifier = Modifier
) {

    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        // 🔥 make perfect square (same as iOS)
        val side = min(constraints.maxWidth, constraints.maxHeight)

        Canvas(
            modifier = Modifier.size(side.dp)
        ) {

            // =========================
            // GUIDELINE CALCULATION (iOS MATCH)
            // =========================
            val startY = 0.1f
            val endY = 0.9f

            val totalHeight = endY - startY
            val section = totalHeight / 3f

            val line1 = size.height * startY
            val line2 = size.height * (startY + section)
            val line3 = size.height * (startY + 2 * section)
            val line4 = size.height * endY

            // =========================
            // BACKGROUND (WHITE BOX)
            // =========================
            drawRoundRect(
                color = Color.White,
                size = size,
                cornerRadius = CornerRadius(24f, 24f),
                style = Fill
            )

            // Optional shadow effect (soft)
            drawRoundRect(
                color = Color.Gray.copy(alpha = 0.2f),
                size = size,
                cornerRadius = CornerRadius(24f, 24f),
                style = Stroke(width = 2f)
            )

            // =========================
            // DRAW LINES
            // =========================
            val dashEffect = PathEffect.dashPathEffect(
                floatArrayOf(16f, 10f)
            )

            listOf(line1, line2, line3, line4).forEachIndexed { index, y ->

                val color = when (index) {
                    0, 3 -> Color.Red.copy(alpha = 0.3f)   // top & bottom
                    else -> Color.Blue.copy(alpha = 0.2f) // middle lines
                }

                drawLine(
                    color = color,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 4f,
                    pathEffect = dashEffect
                )
            }
        }
    }
}