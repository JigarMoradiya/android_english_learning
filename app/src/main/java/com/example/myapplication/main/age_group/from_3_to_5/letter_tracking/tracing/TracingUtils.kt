package com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.tracing

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import kotlin.math.hypot

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

