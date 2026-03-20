package com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.tracing

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import kotlin.math.hypot

fun scale(point: Offset, rect: Rect): Offset {
    return Offset(
        rect.left + point.x * rect.width,
        rect.top + point.y * rect.height
    )
}

fun sampleGuide(
    stroke: List<Offset>,
    rect: Rect,
    spacing: Float
): List<Offset> {

    val guide = mutableListOf<Offset>()

    var last: Offset? = null

    stroke.forEach { pt ->
        val scaled = scale(pt, rect)

        if (last != null) {
            guide += interpolate(last!!, scaled, spacing)
        }

        last = scaled
    }

    return guide
}

fun interpolate(a: Offset, b: Offset, step: Float): List<Offset> {
    val result = mutableListOf<Offset>()

    val dx = b.x - a.x
    val dy = b.y - a.y

    val dist = hypot(dx, dy)
    val steps = maxOf(1, (dist / step).toInt())

    for (i in 0..steps) {
        val t = i / steps.toFloat()
        result.add(Offset(a.x + dx * t, a.y + dy * t))
    }

    return result
}

fun distance(a: Offset, b: Offset): Float {
    return hypot(a.x - b.x, a.y - b.y)
}

fun clampToFrame(point: Offset, frame: Rect): Offset {
    return Offset(
        x = point.x.coerceIn(frame.left, frame.right),
        y = point.y.coerceIn(frame.top, frame.bottom)
    )
}