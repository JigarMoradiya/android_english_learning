package com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.tracing

import androidx.compose.ui.geometry.Offset

object LetterSkeleton {

    fun getStrokes(letter: Char): List<List<StrokeSegment>> {

        return when (letter) {

            'A' -> listOf(
                listOf(
                    StrokeSegment.Line(Offset(0.5f, 0.1f)),
                    StrokeSegment.Line(Offset(0.25f, 0.9f))
                ),
                listOf(
                    StrokeSegment.Line(Offset(0.5f, 0.1f)),
                    StrokeSegment.Line(Offset(0.75f, 0.9f))
                ),
                listOf(
                    StrokeSegment.Line(Offset(0.36f, 0.5f)),
                    StrokeSegment.Line(Offset(0.64f, 0.5f))
                )
            )

            else -> emptyList()
        }
    }
}