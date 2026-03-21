package com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.tracing

import androidx.compose.ui.geometry.Offset
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.view_model.LetterMode
import kotlin.math.cos
import kotlin.math.sin

fun getStrokesForLetter(
    letter: Char,
    mode: LetterMode
): List<List<StrokeSegment>> {

    val line1 = LetterSkeleton.line1(mode)
    val line2 = LetterSkeleton.line2(mode)
    val line3 = LetterSkeleton.line3(mode)

    val center12 = LetterSkeleton.center12(mode)
    val center23 = LetterSkeleton.center23(mode)
    return when (letter) {

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
        'K' -> listOf(

            // vertical
            listOf(
                StrokeSegment.Line(Offset(0.3f, line1)),
                StrokeSegment.Line(Offset(0.3f, line3))
            ),

            // upper diagonal
            listOf(
                StrokeSegment.Line(Offset(0.3f, line2)),
                StrokeSegment.Line(Offset(0.7f, line1))
            ),

            // lower diagonal
            listOf(
                StrokeSegment.Line(Offset(0.3f, line2)),
                StrokeSegment.Line(Offset(0.7f, line3))
            )
        )
        'L' -> listOf(

            // vertical
            listOf(
                StrokeSegment.Line(Offset(0.3f, line1)),
                StrokeSegment.Line(Offset(0.3f, line3))
            ),

            // bottom
            listOf(
                StrokeSegment.Line(Offset(0.3f, line3)),
                StrokeSegment.Line(Offset(0.7f, line3))
            )
        )
        'M' -> listOf(

            // left vertical
            listOf(
                StrokeSegment.Line(Offset(0.25f, line1)),
                StrokeSegment.Line(Offset(0.25f, line3))
            ),

            // left diagonal up
            listOf(
                StrokeSegment.Line(Offset(0.25f, line1)),
                StrokeSegment.Line(Offset(0.5f, line2))
            ),

            // right diagonal up
            listOf(
                StrokeSegment.Line(Offset(0.5f, line2)),
                StrokeSegment.Line(Offset(0.75f, line1))
            ),

            // right vertical
            listOf(
                StrokeSegment.Line(Offset(0.75f, line1)),
                StrokeSegment.Line(Offset(0.75f, line3))
            )
        )
        'N' -> listOf(

            // left vertical
            listOf(
                StrokeSegment.Line(Offset(0.3f, line1)),
                StrokeSegment.Line(Offset(0.3f, line3))
            ),

            // diagonal
            listOf(
                StrokeSegment.Line(Offset(0.3f, line1)),
                StrokeSegment.Line(Offset(0.7f, line3))
            ),

            // right vertical
            listOf(
                StrokeSegment.Line(Offset(0.7f, line3)),
                StrokeSegment.Line(Offset(0.7f, line1))
            )
        )
        'O' -> listOf(

            listOf(
                StrokeSegment.Arc(
                    center = Offset(0.5f, (line1 + line3) / 2f),
                    radius = (line3 - line1) / 2f,

                    endAngle = (-Math.PI / 2f).toFloat(), // start top
                    startAngle = (3f * Math.PI / 2f).toFloat(), // full circle

                    clockwise = true
                )
            )
        )
        'P' -> listOf(

            // vertical
            listOf(
                StrokeSegment.Line(Offset(0.35f, line1)),
                StrokeSegment.Line(Offset(0.35f, line3))
            ),

            // top curve
            listOf(
                StrokeSegment.Line(Offset(0.35f, line1)),

                StrokeSegment.QuadCurve(
                    to = Offset(0.7f, (line1 + line2) / 2f),
                    control = Offset(0.7f, line1)
                ),

                StrokeSegment.QuadCurve(
                    to = Offset(0.35f, line2),
                    control = Offset(0.7f, line2)
                )
            )
        )
        'Q' -> listOf(

            // 1️⃣ circle
            listOf(
                StrokeSegment.Arc(
                    center = Offset(0.5f, (line1 + line3) / 2f),
                    radius = (line3 - line1) / 2f,

                    endAngle = (-Math.PI / 2f).toFloat(),
                    startAngle = (3f * Math.PI / 2f).toFloat(),

                    clockwise = true
                )
            ),

            // 2️⃣ tail
            listOf(
                StrokeSegment.Line(Offset(0.6f, line2 + 0.05f)),
                StrokeSegment.Line(Offset(0.8f, line3 - 0.05f))
            )
        )
        'R' -> listOf(

            // 1️⃣ vertical
            listOf(
                StrokeSegment.Line(Offset(0.35f, line1)),
                StrokeSegment.Line(Offset(0.35f, line3))
            ),

            // 2️⃣ top curve (same as P)
            listOf(
                StrokeSegment.Line(Offset(0.35f, line1)),

                StrokeSegment.QuadCurve(
                    to = Offset(0.7f, (line1 + line2) / 2f),
                    control = Offset(0.7f, line1)
                ),

                StrokeSegment.QuadCurve(
                    to = Offset(0.35f, line2),
                    control = Offset(0.7f, line2)
                )
            ),

            // 3️⃣ diagonal leg
            listOf(
                StrokeSegment.Line(Offset(0.35f, line2)),
                StrokeSegment.Line(Offset(0.7f, line3))
            )
        )
        'S' -> run {

            val topY = line1 - 0.01f
            val middleY = line2
            val bottomY = line3

            val sweep = (0.57f * 2f * Math.PI).toFloat()
            val steps = 30

            val segments = mutableListOf<List<StrokeSegment>>()

            // -------------------------------
            // 1️⃣ TOP ARC (sampled like iOS)
            // -------------------------------
            val radiusTop = (middleY - topY) / 2f
            val centerTop = Offset(0.5f, topY + radiusTop)

            val topPoints = mutableListOf<StrokeSegment>()

            val startTop = Math.PI.toFloat() - sweep / 2f
            val endTop = Math.PI.toFloat() + sweep / 2f

            for (i in 0..steps) {
                val t = i.toFloat() / steps
                val angle = endTop + (startTop - endTop) * t

                val x = centerTop.x + radiusTop * kotlin.math.cos(angle)
                val y = centerTop.y + radiusTop * kotlin.math.sin(angle)

                topPoints.add(StrokeSegment.Line(Offset(x, y)))
            }

            // -------------------------------
            // 2️⃣ BOTTOM ARC
            // -------------------------------
            val radiusBottom = (bottomY - middleY) / 2f
            val centerBottom = Offset(0.5f, middleY + radiusBottom)

            val bottomPoints = mutableListOf<StrokeSegment>()

            val startBottom = -sweep / 2f
            val endBottom = sweep / 2f

            var firstBottom: Offset? = null

            for (i in 0..steps) {
                val t = i.toFloat() / steps
                val angle = startBottom + (endBottom - startBottom) * t

                val x = centerBottom.x + radiusBottom * kotlin.math.cos(angle)
                val y = centerBottom.y + radiusBottom * kotlin.math.sin(angle)

                val pt = Offset(x, y)

                if (i == 0) firstBottom = pt

                bottomPoints.add(StrokeSegment.Line(pt))
            }

            // -------------------------------
            // 3️⃣ SNAP bottom to top
            // -------------------------------
            val topEnd = (topPoints.last() as StrokeSegment.Line).point
            val bottomStart = firstBottom!!

            val dx = topEnd.x - bottomStart.x
            val dy = topEnd.y - bottomStart.y

            val snappedBottom = bottomPoints.map {
                val p = (it as StrokeSegment.Line).point
                StrokeSegment.Line(Offset(p.x + dx, p.y + dy))
            }

            // -------------------------------
            // 4️⃣ ROTATE (make S straight)
            // -------------------------------
            val rotationCenter = Offset(0.5f, (line1 + line3) / 2f)
            val rotationAngle = 0.40f   // 🔥 tweak if needed

            fun rotateStroke(stroke: List<StrokeSegment>): List<StrokeSegment> {
                val cosA = cos(rotationAngle)
                val sinA = sin(rotationAngle)

                return stroke.map { seg ->
                    val p = (seg as StrokeSegment.Line).point

                    val dx = p.x - rotationCenter.x
                    val dy = p.y - rotationCenter.y

                    val x = dx * cosA - dy * sinA + rotationCenter.x
                    val y = dx * sinA + dy * cosA + rotationCenter.y

                    StrokeSegment.Line(Offset(x, y))
                }
            }

            val finalTop = rotateStroke(topPoints)
            val finalBottom = rotateStroke(snappedBottom)

            segments.add(finalTop)
            segments.add(finalBottom)

            segments
        }
        'T' -> listOf(
            listOf(
                StrokeSegment.Line(Offset(0.3f, line1)),
                StrokeSegment.Line(Offset(0.7f, line1))
            ),
            listOf(
                StrokeSegment.Line(Offset(0.5f, line1)),
                StrokeSegment.Line(Offset(0.5f, line3))
            )
        )
        'U' -> listOf(
            listOf(
                StrokeSegment.Line(Offset(0.3f, line1)),
                StrokeSegment.Line(Offset(0.3f, line3 - 0.1f))
            ),
            listOf(
                StrokeSegment.Arc(
                    center = Offset(0.5f, line3 - 0.1f),
                    radius = 0.2f,
                    startAngle = Math.PI.toFloat(),
                    endAngle = 0f,
                    clockwise = true
                )
            ),
            listOf(
                StrokeSegment.Line(Offset(0.7f, line3 - 0.1f)),
                StrokeSegment.Line(Offset(0.7f, line1))
            )
        )
        'V' -> listOf(
            listOf(
                StrokeSegment.Line(Offset(0.3f, line1)),
                StrokeSegment.Line(Offset(0.5f, line3))
            ),
            listOf(
                StrokeSegment.Line(Offset(0.5f, line3)),
                StrokeSegment.Line(Offset(0.7f, line1))
            )
        )
        'W' -> listOf(
            listOf(
                StrokeSegment.Line(Offset(0.2f, line1)),
                StrokeSegment.Line(Offset(0.35f, line3))
            ),
            listOf(
                StrokeSegment.Line(Offset(0.35f, line3)),
                StrokeSegment.Line(Offset(0.5f, line1))
            ),
            listOf(
                StrokeSegment.Line(Offset(0.5f, line1)),
                StrokeSegment.Line(Offset(0.65f, line3))
            ),
            listOf(
                StrokeSegment.Line(Offset(0.65f, line3)),
                StrokeSegment.Line(Offset(0.8f, line1))
            )
        )
        'X' -> listOf(
            listOf(
                StrokeSegment.Line(Offset(0.3f, line1)),
                StrokeSegment.Line(Offset(0.7f, line3))
            ),
            listOf(
                StrokeSegment.Line(Offset(0.7f, line1)),
                StrokeSegment.Line(Offset(0.3f, line3))
            )
        )
        'Y' -> listOf(
            listOf(
                StrokeSegment.Line(Offset(0.3f, line1)),
                StrokeSegment.Line(Offset(0.5f, line2))
            ),
            listOf(
                StrokeSegment.Line(Offset(0.7f, line1)),
                StrokeSegment.Line(Offset(0.5f, line2))
            ),
            listOf(
                StrokeSegment.Line(Offset(0.5f, line2)),
                StrokeSegment.Line(Offset(0.5f, line3))
            )
        )
        'Z' -> listOf(
            listOf(
                StrokeSegment.Line(Offset(0.3f, line1)),
                StrokeSegment.Line(Offset(0.7f, line1))
            ),
            listOf(
                StrokeSegment.Line(Offset(0.7f, line1)),
                StrokeSegment.Line(Offset(0.3f, line3))
            ),
            listOf(
                StrokeSegment.Line(Offset(0.3f, line3)),
                StrokeSegment.Line(Offset(0.7f, line3))
            )
        )

        'a' -> run {

            val centerY = (line2 + line3) / 2f

            val radiusY = (line3 - line2) / 2f
            val radiusX = radiusY * 0.9f

            val center = Offset(0.45f, centerY)

            val steps = 30
            val circle = mutableListOf<StrokeSegment>()

            // 🔥 start slightly right from top (like iOS)
            val startAngle = (-Math.PI / 2f + Math.PI / 4f).toFloat()
            val sweep = (2 * Math.PI).toFloat()

            for (i in 0..steps) {
                val t = i.toFloat() / steps
                val angle = startAngle - sweep * t   // anticlockwise

                val x = center.x + radiusX * cos(angle)
                val y = center.y + radiusY * sin(angle)

                circle.add(StrokeSegment.Line(Offset(x, y)))
            }

            // -------------------------------
            // vertical line (touch circle)
            // -------------------------------
            val rightX = center.x + radiusX

            val vertical = listOf(
                StrokeSegment.Line(Offset(rightX, line2)),
                StrokeSegment.Line(Offset(rightX, line3))
            )

            listOf(
                circle,
                vertical
            )
        }
        else -> emptyList()
    }
}