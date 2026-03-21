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
                StrokeSegment.Line(Offset(0.375f, line2)),
                StrokeSegment.Line(Offset(0.625f, line2))
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
                StrokeSegment.Line(Offset(0.40f, line1)),
                StrokeSegment.QuadCurve(
                    to = Offset(0.7f, center12),
                    control = Offset(0.7f, line1)
                ),
                StrokeSegment.QuadCurve(
                    to = Offset(0.40f, line2),
                    control = Offset(0.7f, line2)
                )
            ),

            // bottom curve
            listOf(
                StrokeSegment.Line(Offset(0.40f, line2)),
                StrokeSegment.QuadCurve(
                    to = Offset(0.7f, center23),
                    control = Offset(0.7f, line2)
                ),
                StrokeSegment.QuadCurve(
                    to = Offset(0.40f, line3),
                    control = Offset(0.7f, line3)
                )
            )
        )

        'C' -> listOf(

            listOf(
                StrokeSegment.Arc(
                    center = Offset(0.5f, (line1 + line3) / 2f),
                    radius = (line3 - line1) / 2f,
                    startAngle = 1.8f * Math.PI.toFloat(),   // start top-right
                    endAngle = 0.2f * Math.PI.toFloat(),     // end bottom-right
                    clockwise = true
                )
            )

        )

        'D' -> listOf(

            // 1️⃣ left vertical line
            listOf(
                StrokeSegment.Line(Offset(0.3f, line1)),
                StrokeSegment.Line(Offset(0.3f, line3))
            ),

            // 2️⃣ right curve
            listOf(
                StrokeSegment.Line(Offset(0.3f, line1)),

                StrokeSegment.QuadCurve(
                    to = Offset(0.75f, line2),
                    control = Offset(0.75f, line1)
                ),

                StrokeSegment.QuadCurve(
                    to = Offset(0.3f, line3),
                    control = Offset(0.75f, line3)
                )
            )
        )

        'E' -> listOf(

            // 1️⃣ vertical line
            listOf(
                StrokeSegment.Line(Offset(0.3f, line1)),
                StrokeSegment.Line(Offset(0.3f, line3))
            ),

            // 2️⃣ top horizontal
            listOf(
                StrokeSegment.Line(Offset(0.3f, line1)),
                StrokeSegment.Line(Offset(0.7f, line1))
            ),

            // 3️⃣ middle horizontal
            listOf(
                StrokeSegment.Line(Offset(0.3f, line2)),
                StrokeSegment.Line(Offset(0.6f, line2))
            ),

            // 4️⃣ bottom horizontal
            listOf(
                StrokeSegment.Line(Offset(0.3f, line3)),
                StrokeSegment.Line(Offset(0.7f, line3))
            )
        )

        'F' -> listOf(

            // 1️⃣ vertical line
            listOf(
                StrokeSegment.Line(Offset(0.3f, line1)),
                StrokeSegment.Line(Offset(0.3f, line3))
            ),

            // 2️⃣ top horizontal
            listOf(
                StrokeSegment.Line(Offset(0.3f, line1)),
                StrokeSegment.Line(Offset(0.7f, line1))
            ),

            // 3️⃣ middle horizontal
            listOf(
                StrokeSegment.Line(Offset(0.3f, line2)),
                StrokeSegment.Line(Offset(0.6f, line2))
            ),
        )

        'G' -> run {

            val centerX = 0.5f
            val centerY = (line1 + line3) / 2f
            val radius = (line3 - line1) / 2f

            // ✅ end must be center-right (0 radians)
            val endAngle = 0f

            // ✅ 85% circle sweep
            val sweep = (0.85f * 2f * Math.PI).toFloat()

            // ✅ start computed from sweep
            val startAngle = endAngle + sweep

            val arcEndX = centerX + radius

            listOf(

                // 1️⃣ ARC (85% circle, ending at center-right)
                listOf(
                    StrokeSegment.Arc(
                        center = Offset(centerX, centerY),
                        radius = radius,
                        startAngle = startAngle,
                        endAngle = endAngle,
                        clockwise = true
                    )
                ),

                // 2️⃣ horizontal line (perfect center alignment)
                listOf(
                    StrokeSegment.Line(
                        Offset(arcEndX - 0.3f, centerY)
                    ),
                    StrokeSegment.Line(
                        Offset(arcEndX, centerY)
                    )
                )
            )
        }
        'H' -> listOf(

            // 1️⃣ left vertical
            listOf(
                StrokeSegment.Line(Offset(0.3f, line1)),
                StrokeSegment.Line(Offset(0.3f, line3))
            ),

            // 2️⃣ right vertical
            listOf(
                StrokeSegment.Line(Offset(0.7f, line1)),
                StrokeSegment.Line(Offset(0.7f, line3))
            ),

            // 3️⃣ middle horizontal
            listOf(
                StrokeSegment.Line(Offset(0.3f, line2)),
                StrokeSegment.Line(Offset(0.7f, line2))
            )
        )
        'I' -> listOf(

            // 1️⃣ top horizontal
            listOf(
                StrokeSegment.Line(Offset(0.3f, line1)),
                StrokeSegment.Line(Offset(0.7f, line1))
            ),

            // 2️⃣ vertical line
            listOf(
                StrokeSegment.Line(Offset(0.5f, line1)),
                StrokeSegment.Line(Offset(0.5f, line3))
            ),

            // 3️⃣ bottom horizontal
            listOf(
                StrokeSegment.Line(Offset(0.3f, line3)),
                StrokeSegment.Line(Offset(0.7f, line3))
            )
        )
        'J' -> run {

            val centerX = 0.5f

            // 👉 stem stops above bottom (space for hook)
            val stemBottomY = line3 - 0.12f * (line3 - line1)

            val hookRadius = line3 - stemBottomY

            listOf(

                // 1️⃣ top horizontal bar
                listOf(
                    StrokeSegment.Line(Offset(0.3f, line1)),
                    StrokeSegment.Line(Offset(0.7f, line1))
                ),

                // 2️⃣ vertical stem
                listOf(
                    StrokeSegment.Line(Offset(centerX, line1)),
                    StrokeSegment.Line(Offset(centerX, stemBottomY))
                ),

                // 3️⃣ hook (arc)
                listOf(
                    StrokeSegment.Arc(
                        center = Offset(centerX - hookRadius, stemBottomY),
                        radius = hookRadius,

                        startAngle = 0f,                         // right side
                        endAngle = (Math.PI * 1.1f).toFloat(),   // curve downward-left

                        clockwise = false
                    )
                )
            )
        }

        else -> emptyList()
    }
}