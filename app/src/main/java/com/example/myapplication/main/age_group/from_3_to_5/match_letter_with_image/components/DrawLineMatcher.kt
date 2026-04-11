package com.example.myapplication.main.age_group.from_3_to_5.match_letter_with_image.components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope

fun DrawScope.drawMatchedConnections(
    matchedOrder: List<String>,
    letterPositions: Map<String, Offset>,
    imagePositions: Map<String, Offset>,
    getColor: (Int) -> Color
) {
    matchedOrder.forEachIndexed { index, letter ->

        val start = letterPositions[letter]
        val end = imagePositions[letter]

        if (start != null && end != null) {

            val color = getColor(index)

            // Glow
            drawLine(
                color = color.copy(alpha = 0.25f),
                start = start,
                end = end,
                strokeWidth = 14f,
                cap = StrokeCap.Round
            )

            // Main
            drawLine(
                color = color,
                start = start,
                end = end,
                strokeWidth = 6f,
                cap = StrokeCap.Round
            )
        }
    }
}

fun DrawScope.drawDragConnection(
    start: Offset?,
    end: Offset?,
    color: Color
) {
    if (start == null || end == null) return

    // Glow
    drawLine(
        color = color.copy(alpha = 0.25f),
        start = start,
        end = end,
        strokeWidth = 14f,
        cap = StrokeCap.Round
    )

    // Main
    drawLine(
        color = color,
        start = start,
        end = end,
        strokeWidth = 6f,
        cap = StrokeCap.Round
    )

    // Finger dot
    drawCircle(
        color = color,
        radius = 10f,
        center = end
    )
}