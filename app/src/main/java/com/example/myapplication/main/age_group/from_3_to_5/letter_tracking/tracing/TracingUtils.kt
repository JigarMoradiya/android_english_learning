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

fun scale(p: Offset, rect: Rect): Offset {
    return Offset(
        rect.left + p.x * rect.width,
        rect.top + p.y * rect.height
    )
}

fun sampleGuide(
    stroke: List<Offset>,
    rect: Rect,
    step: Float
): List<Offset> {

    val result = mutableListOf<Offset>()
    var last: Offset? = null

    stroke.forEach { pt ->
        val scaled = scale(pt, rect)

        if (last != null) {
            result += interpolate(last!!, scaled, step)
        }

        last = scaled
    }

    return result
}

private fun interpolate(a: Offset, b: Offset, step: Float): List<Offset> {

    val res = mutableListOf<Offset>()

    val dx = b.x - a.x
    val dy = b.y - a.y

    val dist = hypot(dx, dy)
    val steps = maxOf(1, (dist / step).toInt())

    for (i in 0..steps) {
        val t = i / steps.toFloat()
        res.add(Offset(a.x + dx * t, a.y + dy * t))
    }

    return res
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