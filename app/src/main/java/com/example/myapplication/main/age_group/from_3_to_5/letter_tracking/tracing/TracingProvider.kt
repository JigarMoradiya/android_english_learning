package com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.tracing

import androidx.compose.ui.geometry.Offset
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.view_model.LetterMode

fun getStrokesForLetter(
    letter: Char,
    mode: LetterMode
): List<List<StrokeSegment>> {

    val line1 = LetterSkeleton.line1(mode)
    val line2 = LetterSkeleton.line2(mode)
    val line3 = LetterSkeleton.line3(mode)

    val center12 = LetterSkeleton.center12(mode)
    val center23 = LetterSkeleton.center23(mode)

    val offsetX = 0.02f
    return when (letter.uppercaseChar()) {

        'A' -> listOf(
            listOf(
                StrokeSegment.Line(Offset(0.5f, line1)),
                StrokeSegment.Line(Offset(0.25f, line3))
            ),
            listOf(
                StrokeSegment.Line(Offset(0.5f, line1)),
                StrokeSegment.Line(Offset(0.75f, line3))
            ),
            listOf(
                StrokeSegment.Line(Offset(0.40f, line2)),
                StrokeSegment.Line(Offset(0.60f, line2))
            )
        )

        'B' -> listOf(

            // vertical
            listOf(
                StrokeSegment.Line(Offset(0.4f, line1)),
                StrokeSegment.Line(Offset(0.4f, line3))
            ),

            // top curve
            listOf(
                StrokeSegment.Line(Offset(0.40f + offsetX, line1)),
                StrokeSegment.QuadCurve(
                    to = Offset(0.7f, center12),
                    control = Offset(0.7f, line1)
                ),
                StrokeSegment.QuadCurve(
                    to = Offset(0.40f + offsetX, line2),
                    control = Offset(0.7f, line2)
                )
            ),

            // bottom curve
            listOf(
                StrokeSegment.Line(Offset(0.40f + offsetX, line2)),
                StrokeSegment.QuadCurve(
                    to = Offset(0.7f, center23),
                    control = Offset(0.7f, line2)
                ),
                StrokeSegment.QuadCurve(
                    to = Offset(0.40f + offsetX, line3),
                    control = Offset(0.7f, line3)
                )
            )
        )

        else -> emptyList()
    }
}