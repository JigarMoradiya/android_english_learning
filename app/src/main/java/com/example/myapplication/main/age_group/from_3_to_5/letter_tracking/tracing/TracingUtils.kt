package com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.tracing

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

fun sampleStroke(
    stroke: List<StrokeSegment>,
    frame: Rect,
    spacing: Float
): List<Offset> {

    val result = mutableListOf<Offset>()
    var last: Offset? = null

    stroke.forEach { seg ->

        when (seg) {

            is StrokeSegment.Line -> {
                val p = scale(seg.point, frame)
                if (last != null) result += interpolate(last!!, p, spacing)
                last = p
            }

            is StrokeSegment.QuadCurve -> {
                if (last != null) {
                    result += interpolateQuadCurve(
                        start = last!!,
                        control = scale(seg.control, frame),
                        end = scale(seg.to, frame),
                        step = spacing
                    )
                }
                last = scale(seg.to, frame)
            }

            is StrokeSegment.Arc -> {
                val steps = maxOf(
                    2,
                    ((kotlin.math.abs(seg.endAngle - seg.startAngle)) / 0.05f).toInt()
                )

                for (i in 0..steps) {
                    val t = i.toFloat() / steps
                    val angle = seg.startAngle + t * (seg.endAngle - seg.startAngle)

                    val pt = Offset(
                        seg.center.x + seg.radius * kotlin.math.cos(angle),
                        seg.center.y + seg.radius * kotlin.math.sin(angle)
                    )

                    result.add(scale(pt, frame))
                }
            }
        }
    }

    return result
}

fun scale(p: Offset, rect: Rect): Offset {
    return Offset(
        rect.left + p.x * rect.width,
        rect.top + p.y * rect.height
    )
}

fun interpolate(a: Offset, b: Offset, step: Float): List<Offset> {
    val result = mutableListOf<Offset>()
    val dx = b.x - a.x
    val dy = b.y - a.y
    val dist = hypot(dx, dy)
    val steps = maxOf(1, (dist / step).toInt())

    for (i in 0..steps) {
        val t = i.toFloat() / steps
        result.add(Offset(a.x + dx * t, a.y + dy * t))
    }
    return result
}

fun interpolateQuadCurve(
    start: Offset,
    control: Offset,
    end: Offset,
    step: Float
): List<Offset> {

    val points = mutableListOf<Offset>()
    val dist = hypot(end.x - start.x, end.y - start.y)
    val steps = maxOf(1, (dist / step).toInt())

    for (i in 0..steps) {
        val t = i.toFloat() / steps

        val x =
            (1 - t) * (1 - t) * start.x +
                    2 * (1 - t) * t * control.x +
                    t * t * end.x

        val y =
            (1 - t) * (1 - t) * start.y +
                    2 * (1 - t) * t * control.y +
                    t * t * end.y

        points.add(Offset(x, y))
    }

    return points
}

fun distance(a: Offset, b: Offset): Float {
    return hypot(a.x - b.x, a.y - b.y)
}



fun DrawScope.drawArrow(
    start: Offset,
    end: Offset,
    size: Float
) {

    val angle = atan2(end.y - start.y, end.x - start.x)

    val length = size
    val width = size * 0.6f

    val tip = start

    val left = Offset(
        tip.x - length * cos(angle) + width * sin(angle),
        tip.y - length * sin(angle) - width * cos(angle)
    )

    val right = Offset(
        tip.x - length * cos(angle) - width * sin(angle),
        tip.y - length * sin(angle) + width * cos(angle)
    )

    val path = Path().apply {
        moveTo(tip.x, tip.y)
        lineTo(left.x, left.y)
        lineTo(right.x, right.y)
        close()
    }

    drawPath(
        path = path,
        color = Color(0xFF2196F3)
    )
}