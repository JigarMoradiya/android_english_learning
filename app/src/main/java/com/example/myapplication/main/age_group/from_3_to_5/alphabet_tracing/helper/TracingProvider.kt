package com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.helper

import androidx.compose.ui.geometry.Offset
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.LetterMode
import kotlin.math.cos
import kotlin.math.sin

fun getStrokesForLetter(
    letter: Char,
    mode: LetterMode
): List<List<StrokeSegment>> {

    val line1 = LetterSkeleton.line1(mode)
    val line2 = LetterSkeleton.line2(mode)
    val line3 = LetterSkeleton.line3(mode)
    val line4 = LetterSkeleton.line4(mode)

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


            // 1️⃣ vertical line
            listOf(
                StrokeSegment.Line(Offset(0.5f, line1)),
                StrokeSegment.Line(Offset(0.5f, line3))
            ),

            // 2️⃣ top horizontal
            listOf(
                StrokeSegment.Line(Offset(0.3f, line1)),
                StrokeSegment.Line(Offset(0.7f, line1))
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
                        endAngle = (Math.PI * 0.8f).toFloat(),   // curve downward-left

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

            val sweep = (0.57f * 2f * Math.PI).toFloat()
            val steps = 30

            val segments = mutableListOf<List<StrokeSegment>>()

            // -------------------------------
            // 1️⃣ TOP ARC (sampled like iOS)
            // -------------------------------
            val radiusTop = (line2 - topY) / 2f
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
            val radiusBottom = (line3 - line2) / 2f
            val centerBottom = Offset(0.5f, line2 + radiusBottom)

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
                StrokeSegment.Line(Offset(0.3f, line3 - 0.2f))
            ),
            listOf(
                StrokeSegment.Arc(
                    center = Offset(0.5f, line3 - 0.2f),
                    radius = 0.2f,
                    startAngle = Math.PI.toFloat(),
                    endAngle = 0f,
                    clockwise = true
                )
            ),
            listOf(
                StrokeSegment.Line(Offset(0.7f, line3 - 0.2f)),
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
        'a' -> listOf(
            listOf(
                StrokeSegment.Arc(
                    center = Offset(0.45f, (line2 + line3) / 2f),
                    radius = (line3 - line2) / 2f,

                    // ✅ start at TOP-RIGHT (45° from top)
                    endAngle = (7f * Math.PI / 4f).toFloat(),

                    // ✅ full circle anti-clockwise
                    startAngle = (7f * Math.PI / 4f + 2f * Math.PI).toFloat(),

                    clockwise = false
                )
            ),

            listOf(
                StrokeSegment.Line(
                    Offset(0.45f + (line3 - line2) / 2f, line2)
                ),
                StrokeSegment.Line(
                    Offset(0.45f + (line3 - line2) / 2f, line3)
                )
            )
        )
        'b' -> lowercaseStemArcLetter(
            topY = line1,
            midY = line2,
            bottomY = line3,
            flipHorizontally = true
        )
        'c' -> listOf(
            listOf(
                StrokeSegment.Arc(
                    center = Offset(0.5f, (line2 + line3) / 2f),
                    radius = (line3 - line2) / 2f,
                    startAngle = 1.7f * Math.PI.toFloat(),   // start top-right
                    endAngle = 0.3f * Math.PI.toFloat(),     // end bottom-right
                    clockwise = true
                )
            )
        )
        'd' -> lowercaseStemArcLetter(
            topY = line1,
            midY = line2,
            bottomY = line3,
            flipHorizontally = false
        )
        'e' -> run {

            val centerX = 0.5f
            val centerY = (line2 + line3) / 2f

            val radiusY = (line3 - line2) / 2f
            val radiusX = radiusY * 0.9f

            val steps = 25

            val arc = mutableListOf<StrokeSegment>()

            // 🔥 start from right side (like iOS)
            val startAngle = 0f

            // 🔥 ~85% circle (not full)
            val sweep = (2f * Math.PI * 0.85f).toFloat()

            for (i in 0..steps) {
                val t = i.toFloat() / steps

                // clockwise (same as iOS feel)
                val angle = startAngle - sweep * t

                val x = centerX + radiusX * cos(angle)
                val y = centerY + radiusY * sin(angle)

                arc.add(StrokeSegment.Line(Offset(x, y)))
            }

            // -------------------------------
            // horizontal line (middle bar)
            // -------------------------------
            val horizontal = listOf(
                StrokeSegment.Line(Offset(centerX - radiusX, centerY)),
                StrokeSegment.Line(Offset(centerX + radiusX * 0.8f, centerY))
            )

            listOf(
                horizontal,
                arc
            )
        }
        'f' -> run {

            val topY = line1
            val bottomY = line3

            val totalHeight = bottomY - topY

            // -------------------------------
            // 1️⃣ HOOK (top curve)
            // -------------------------------
            val hookHeight = totalHeight * 0.2f
            val hookRadius = hookHeight

            val hookCenterX = 0.5f + hookRadius
            val hookCenterY = topY + hookHeight

            val steps = 20
            val hook = mutableListOf<StrokeSegment>()

            val startAngle = Math.PI.toFloat()
            val endAngle = (Math.PI * 1.7f).toFloat()

            for (i in 0..steps) {
                val t = i.toFloat() / steps
                val angle = endAngle + (startAngle - endAngle) * t

                val x = hookCenterX + hookRadius * cos(angle)
                val y = hookCenterY + hookRadius * sin(angle)

                hook.add(StrokeSegment.Line(Offset(x, y)))
            }

            // -------------------------------
            // 2️⃣ VERTICAL STEM
            // -------------------------------
            val stemTopY = hookCenterY
            val stemBottomY = bottomY

            val stem = listOf(
                StrokeSegment.Line(Offset(0.5f, stemTopY)),
                StrokeSegment.Line(Offset(0.5f, stemBottomY))
            )

            // -------------------------------
            // 3️⃣ HORIZONTAL LINE (middle)
            // -------------------------------
            val midY = (line1 + line2) / 2f

            val horizontal = listOf(
                StrokeSegment.Line(Offset(0.4f, midY)),
                StrokeSegment.Line(Offset(0.62f, midY))
            )

            listOf(
                hook,
                stem,
                horizontal
            )
        }
        'g' -> run {
            val centerX = 0.5f
            val centerY = (line2 + line3) / 2f

            val radiusY = (line3 - line2) / 2f
            val radiusX = radiusY * 0.9f

            val steps = 30

            // -------------------------------
            // 1️⃣ TOP CIRCLE (clockwise)
            // -------------------------------
            val circle = mutableListOf<StrokeSegment>()

            val startAngle = (-Math.PI / 4f).toFloat() // top-right start
            val sweep = (2f * Math.PI).toFloat()

            for (i in 0..steps) {
                val t = i.toFloat() / steps
                val angle = startAngle - sweep * t

                val x = centerX + radiusX * cos(angle)
                val y = centerY + radiusY * sin(angle)

                circle.add(StrokeSegment.Line(Offset(x, y)))
            }

            // -------------------------------
            // 2️⃣ RIGHT VERTICAL STEM
            // -------------------------------
            val stemX = centerX + radiusX

            val stemBottom = line4 - 0.08f

            val stem = listOf(
                StrokeSegment.Line(Offset(stemX, line2)),
                StrokeSegment.Line(Offset(stemX, stemBottom))
            )

            // -------------------------------
            // 3️⃣ BOTTOM HOOK
            // -------------------------------
            val hookRadius = 0.08f
            val hookCenterX = stemX - hookRadius

            val hookSteps = 15
            val hook = mutableListOf<StrokeSegment>()

            val hookStart = 0f
            val hookEnd = (Math.PI * 0.8f).toFloat()

            for (i in 0..hookSteps) {
                val t = i.toFloat() / hookSteps
                val angle = hookStart + (hookEnd - hookStart) * t

                val x = hookCenterX + hookRadius * cos(angle)
                val y = stemBottom + hookRadius * sin(angle)

                hook.add(StrokeSegment.Line(Offset(x, y)))
            }

            listOf(
                circle,
                stem,
                hook
            )
        }
        'h' -> run {

            val topY = line1
            val midY = line2
            val bottomY = line3

            val leftX = 0.38f
            val rightX = 0.62f

            val arcRadius = 0.05f
            val steps = 12

            // -------------------------------
            // 1️⃣ LEFT VERTICAL
            // -------------------------------
            val leftVertical = listOf(
                StrokeSegment.Line(Offset(leftX, topY)),
                StrokeSegment.Line(Offset(leftX, bottomY))
            )

            // -------------------------------
            // 2️⃣ HORIZONTAL (to arc start)
            // -------------------------------
            val horizontalEndX = rightX - arcRadius

            val horizontal = listOf(
                StrokeSegment.Line(Offset(leftX, midY)),
                StrokeSegment.Line(Offset(horizontalEndX, midY))
            )

            // -------------------------------
            // 3️⃣ TOP CURVE (quarter arc)
            // -------------------------------
            val arcCenterX = horizontalEndX
            val arcCenterY = midY + arcRadius

            val arc = mutableListOf<StrokeSegment>()

            val startAngle = (-Math.PI / 2f).toFloat() // up
            val endAngle = 0f                          // right

            for (i in 0..steps) {
                val t = i.toFloat() / steps
                val angle = startAngle + (endAngle - startAngle) * t

                val x = arcCenterX + arcRadius * cos(angle)
                val y = arcCenterY + arcRadius * sin(angle)

                arc.add(StrokeSegment.Line(Offset(x, y)))
            }

            // -------------------------------
            // 4️⃣ RIGHT VERTICAL
            // -------------------------------
            val rightVerticalStart = (arc.last() as StrokeSegment.Line).point

            val rightVertical = listOf(
                StrokeSegment.Line(rightVerticalStart),
                StrokeSegment.Line(Offset(rightX, bottomY))
            )

            listOf(
                leftVertical,
                horizontal,
                arc,
                rightVertical
            )
        }
        'i' -> run {

            val topY = line2
            val bottomY = line3

            val centerX = 0.5f

            // -------------------------------
            // 1️⃣ VERTICAL STROKE
            // -------------------------------
            val vertical = listOf(
                StrokeSegment.Line(Offset(centerX, topY)),
                StrokeSegment.Line(Offset(centerX, bottomY))
            )

            // -------------------------------
            // 2️⃣ DOT (just a point)
            // -------------------------------
            val dotY = line2 - 0.06f

            val dot = listOf(
                StrokeSegment.Line(Offset(centerX, dotY)),
                StrokeSegment.Line(Offset(centerX, dotY))
            )

            listOf(
                vertical,
                dot
            )
        }
        'j' -> run {

            val centerX = 0.5f
            // -------------------------------
            // 1️⃣ DOT
            // -------------------------------
            val dotY = line2 - 0.06f

            val dot = listOf(
                StrokeSegment.Line(Offset(centerX, dotY)),
                StrokeSegment.Line(Offset(centerX, dotY))
            )

            // -------------------------------
            // 2️⃣ VERTICAL STEM
            // -------------------------------
            val hookRadius = 0.08f
            val stemBottom = line4 - hookRadius

            val stem = listOf(
                StrokeSegment.Line(Offset(centerX, line2)),
                StrokeSegment.Line(Offset(centerX, stemBottom))
            )

            // -------------------------------
            // 3️⃣ BOTTOM HOOK
            // -------------------------------
            val hookCenterX = centerX - hookRadius

            val steps = 20
            val hook = mutableListOf<StrokeSegment>()

            val startAngle = 0f
            val endAngle = (Math.PI * 0.8f).toFloat() // smooth like g

            for (i in 0..steps) {
                val t = i.toFloat() / steps
                val angle = startAngle + (endAngle - startAngle) * t

                val x = hookCenterX + hookRadius * cos(angle)
                val y = stemBottom + hookRadius * sin(angle)

                hook.add(StrokeSegment.Line(Offset(x, y)))
            }

            listOf(
                stem,
                hook,
                dot
            )
        }
        'k' -> run {

            val leftX = 0.4f
            val rightX = 0.62f

            // -------------------------------
            // 1️⃣ LEFT VERTICAL
            // -------------------------------
            val vertical = listOf(
                StrokeSegment.Line(Offset(leftX, line1)),
                StrokeSegment.Line(Offset(leftX, line3))
            )

            // -------------------------------
            // 2️⃣ UPPER DIAGONAL
            // -------------------------------
            val upperDiagonal = listOf(
                StrokeSegment.Line(Offset(leftX, center23)),
                StrokeSegment.Line(Offset(rightX, line2))
            )

            // -------------------------------
            // 3️⃣ LOWER DIAGONAL
            // -------------------------------
            val lowerDiagonal = listOf(
                StrokeSegment.Line(Offset(leftX, center23)),
                StrokeSegment.Line(Offset(rightX, line3))
            )

            listOf(
                vertical,
                upperDiagonal,
                lowerDiagonal
            )
        }
        'l' -> run {

            val topY = line1
            val bottomY = line3

            val centerX = 0.5f

            listOf(
                listOf(
                    StrokeSegment.Line(Offset(centerX, topY)),
                    StrokeSegment.Line(Offset(centerX, bottomY))
                )
            )
        }
        'm' -> run {

            val leftX = 0.32f
            val midX = 0.5f
            val rightX = 0.68f

            val arcRadius = 0.05f
            val steps = 12

            // -------------------------------
            // 1️⃣ LEFT VERTICAL
            // -------------------------------
            val leftVertical = listOf(
                StrokeSegment.Line(Offset(leftX, line2 - 0.02f)),
                StrokeSegment.Line(Offset(leftX, line3))
            )

            // -------------------------------
            // 2️⃣ FIRST HORIZONTAL
            // -------------------------------
            val h1EndX = midX - arcRadius

            val horizontal1 = listOf(
                StrokeSegment.Line(Offset(leftX, line2)),
                StrokeSegment.Line(Offset(h1EndX, line2))
            )

            // -------------------------------
            // 3️⃣ FIRST ARC
            // -------------------------------
            val arc1 = mutableListOf<StrokeSegment>()
            val arc1CenterY = line2 + arcRadius

            for (i in 0..steps) {
                val t = i.toFloat() / steps
                val angle = (-Math.PI / 2f + (Math.PI / 2f) * t).toFloat()

                val x = h1EndX + arcRadius * cos(angle)
                val y = arc1CenterY + arcRadius * sin(angle)

                arc1.add(StrokeSegment.Line(Offset(x, y)))
            }

            // -------------------------------
            // 4️⃣ MIDDLE VERTICAL
            // -------------------------------
            val midStart = (arc1.last() as StrokeSegment.Line).point

            val middleVertical = listOf(
                StrokeSegment.Line(midStart),
                StrokeSegment.Line(Offset(midX, line3))
            )

            // -------------------------------
            // 5️⃣ SECOND HORIZONTAL
            // -------------------------------
            val h2EndX = rightX - arcRadius

            val horizontal2 = listOf(
                StrokeSegment.Line(Offset(midX, line2)),
                StrokeSegment.Line(Offset(h2EndX, line2))
            )

            // -------------------------------
            // 6️⃣ SECOND ARC
            // -------------------------------
            val arc2 = mutableListOf<StrokeSegment>()
            val arc2CenterX = h2EndX
            val arc2CenterY = line2 + arcRadius

            for (i in 0..steps) {
                val t = i.toFloat() / steps
                val angle = (-Math.PI / 2f + (Math.PI / 2f) * t).toFloat()

                val x = arc2CenterX + arcRadius * cos(angle)
                val y = arc2CenterY + arcRadius * sin(angle)

                arc2.add(StrokeSegment.Line(Offset(x, y)))
            }

            // -------------------------------
            // 7️⃣ RIGHT VERTICAL
            // -------------------------------
            val rightStart = (arc2.last() as StrokeSegment.Line).point

            val rightVertical = listOf(
                StrokeSegment.Line(rightStart),
                StrokeSegment.Line(Offset(rightX, line3))
            )

            listOf(
                leftVertical,
                horizontal1,
                arc1,
                middleVertical,
                horizontal2,
                arc2,
                rightVertical
            )
        }
        'n' -> run {

            val leftX = 0.38f
            val rightX = 0.62f

            val arcRadius = 0.05f
            val steps = 12

            // -------------------------------
            // 1️⃣ LEFT VERTICAL
            // -------------------------------
            val leftVertical = listOf(
                StrokeSegment.Line(Offset(leftX, line2 - 0.02f)),
                StrokeSegment.Line(Offset(leftX, line3))
            )

            // -------------------------------
            // 2️⃣ HORIZONTAL
            // -------------------------------
            val horizontalEndX = rightX - arcRadius

            val horizontal = listOf(
                StrokeSegment.Line(Offset(leftX, line2)),
                StrokeSegment.Line(Offset(horizontalEndX, line2))
            )

            // -------------------------------
            // 3️⃣ ARC (top curve)
            // -------------------------------
            val arcCenterX = horizontalEndX
            val arcCenterY = line2 + arcRadius

            val arc = mutableListOf<StrokeSegment>()

            val startAngle = (-Math.PI / 2f).toFloat()
            val endAngle = 0f

            for (i in 0..steps) {
                val t = i.toFloat() / steps
                val angle = startAngle + (endAngle - startAngle) * t

                val x = arcCenterX + arcRadius * cos(angle)
                val y = arcCenterY + arcRadius * sin(angle)

                arc.add(StrokeSegment.Line(Offset(x, y)))
            }

            // -------------------------------
            // 4️⃣ RIGHT VERTICAL
            // -------------------------------
            val rightVerticalStart = (arc.last() as StrokeSegment.Line).point

            val rightVertical = listOf(
                StrokeSegment.Line(rightVerticalStart),
                StrokeSegment.Line(Offset(rightX, line3))
            )

            listOf(
                leftVertical,
                horizontal,
                arc,
                rightVertical
            )
        }
        'o' -> listOf(
            listOf(
                StrokeSegment.Arc(
                    center = Offset(0.5f, (line2 + line3) / 2f),
                    radius = (line3 - line2) / 2f,
                    endAngle = (-Math.PI / 2f).toFloat(), // start top
                    startAngle = (3f * Math.PI / 2f).toFloat(), // full circle
                    clockwise = true
                )
            )
        )
        'p' -> lowercaseStemArcLetter(
            topY = line2,
            midY = line2,
            bottomY = line3,
            line4 = line4,
            flipHorizontally = true
        )
        'q' -> lowercaseStemArcLetter(
            topY = line2,
            midY = line2,
            bottomY = line3,
            line4 = line4,
            flipHorizontally = false
        )
        'r' -> run {

            val topY = line2
            val bottomY = line3

            val stemX = 0.45f

            // -------------------------------
            // 1️⃣ VERTICAL STEM
            // -------------------------------
            val vertical = listOf(
                StrokeSegment.Line(Offset(stemX, topY)),
                StrokeSegment.Line(Offset(stemX, bottomY))
            )

            // -------------------------------
            // 2️⃣ TOP CURVE (small cap)
            // -------------------------------
            val arcRadius = 0.1f
            val steps = 18

            val startYOffset = 0f

            val arcCenterX = stemX + arcRadius
            val arcCenterY = topY + startYOffset + arcRadius

            val arc = mutableListOf<StrokeSegment>()

            val startAngle = Math.PI.toFloat()
            val endAngle = (Math.PI * 1.65f).toFloat()

            for (i in 0..steps) {
                val t = i.toFloat() / steps
                val angle = startAngle + (endAngle - startAngle) * t

                val x = arcCenterX + arcRadius * cos(angle)
                val y = arcCenterY + arcRadius * sin(angle)

                arc.add(StrokeSegment.Line(Offset(x, y)))
            }

            listOf(
                vertical,
                arc
            )
        }
        's' -> run {

            val topY = line2            // 🔥 changed
            val middleY = (line2 + line3) / 2f
            val bottomY = line3

            val sweep = (0.57f * 2f * Math.PI).toFloat()
            val steps = 30

            val segments = mutableListOf<List<StrokeSegment>>()

            // -------------------------------
            // 1️⃣ TOP ARC
            // -------------------------------
            val radiusTop = (middleY - topY) / 2f
            val centerTop = Offset(0.5f, topY + radiusTop)

            val topPoints = mutableListOf<StrokeSegment>()

            val startTop = Math.PI.toFloat() - sweep / 2f
            val endTop = Math.PI.toFloat() + sweep / 2f

            for (i in 0..steps) {
                val t = i.toFloat() / steps
                val angle = endTop + (startTop - endTop) * t

                val x = centerTop.x + radiusTop * cos(angle)
                val y = centerTop.y + radiusTop * sin(angle)

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

                val x = centerBottom.x + radiusBottom * cos(angle)
                val y = centerBottom.y + radiusBottom * sin(angle)

                val pt = Offset(x, y)

                if (i == 0) firstBottom = pt

                bottomPoints.add(StrokeSegment.Line(pt))
            }

            // -------------------------------
            // 3️⃣ SNAP
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
            // 4️⃣ ROTATE (same as uppercase)
            // -------------------------------
            val rotationCenter = Offset(0.5f, (topY + bottomY) / 2f)
            val rotationAngle = 0.40f

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
        't' -> listOf(

            // vertical
            listOf(
                StrokeSegment.Line(Offset(0.5f, line1)),
                StrokeSegment.Line(Offset(0.5f, line3))
            ),

            // horizontal
            listOf(
                StrokeSegment.Line(Offset(0.38f, (line1 + line2) / 2f)),
                StrokeSegment.Line(Offset(0.62f, (line1 + line2) / 2f))
            )
        )
        'u' -> run {

            val topY = line2
            val bottomY = line3

            val leftX = 0.4f
            val rightX = 0.62f


            // left vertical (partial)
            val leftVertical = listOf(
                StrokeSegment.Line(Offset(leftX, topY)),
                StrokeSegment.Line(Offset(leftX, bottomY - 0.05f))
            )

            // bottom curve (2 quad for smooth)
            val midX = (leftX + rightX) / 2f

            val curve1 = listOf(
                StrokeSegment.Line(Offset(leftX, bottomY - 0.05f)),
                StrokeSegment.QuadCurve(
                    to = Offset(midX, bottomY),
                    control = Offset(leftX, bottomY)
                )
            )

            val curve2 = listOf(
                StrokeSegment.Line(Offset(midX, bottomY)),
                StrokeSegment.QuadCurve(
                    to = Offset(rightX, bottomY - 0.04f),
                    control = Offset(rightX, bottomY)
                )
            )

            val rightVertical = listOf(
                StrokeSegment.Line(Offset(rightX, topY)),
                StrokeSegment.Line(Offset(rightX, bottomY))
            )

            listOf(leftVertical, curve1, curve2, rightVertical)
        }
        'v' -> listOf(

            listOf(
                StrokeSegment.Line(Offset(0.4f, line2)),
                StrokeSegment.Line(Offset(0.51f, line3))
            ),

            listOf(
                StrokeSegment.Line(Offset(0.51f, line3)),
                StrokeSegment.Line(Offset(0.62f, line2))
            )
        )
        'w' -> listOf(

            listOf(
                StrokeSegment.Line(Offset(0.35f, line2)),
                StrokeSegment.Line(Offset(0.45f, line3))
            ),

            listOf(
                StrokeSegment.Line(Offset(0.45f, line3)),
                StrokeSegment.Line(Offset(0.55f, line2))
            ),

            listOf(
                StrokeSegment.Line(Offset(0.55f, line2)),
                StrokeSegment.Line(Offset(0.65f, line3))
            ),

            listOf(
                StrokeSegment.Line(Offset(0.65f, line3)),
                StrokeSegment.Line(Offset(0.75f, line2))
            )
        )
        'x' -> listOf(

            listOf(
                StrokeSegment.Line(Offset(0.4f, line2)),
                StrokeSegment.Line(Offset(0.62f, line3))
            ),

            listOf(
                StrokeSegment.Line(Offset(0.62f, line2)),
                StrokeSegment.Line(Offset(0.4f, line3))
            )
        )
        'y' -> run {

            val topY = line2
            val midY = line3
            val bottomY = line4

            val leftX = 0.4f
            val rightX = 0.62f
            val midX = (leftX + rightX) / 2f

            val leftStroke = listOf(
                StrokeSegment.Line(Offset(leftX, topY)),
                StrokeSegment.Line(Offset(midX, midY))
            )

            val rightStroke = listOf(
                StrokeSegment.Line(Offset(rightX, topY)),
                StrokeSegment.QuadCurve(
                    to = Offset(midX - 0.12f, bottomY),
                    control = Offset(midX, midY + 0.1f)
                )
            )

            listOf(leftStroke, rightStroke)
        }
        'z' -> listOf(

            listOf(
                StrokeSegment.Line(Offset(0.4f, line2)),
                StrokeSegment.Line(Offset(0.62f, line2))
            ),

            listOf(
                StrokeSegment.Line(Offset(0.62f, line2)),
                StrokeSegment.Line(Offset(0.4f, line3))
            ),

            listOf(
                StrokeSegment.Line(Offset(0.4f, line3)),
                StrokeSegment.Line(Offset(0.62f, line3))
            )
        )
        else -> emptyList()
    }
}