package com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.tracing

import androidx.compose.ui.geometry.Offset

sealed class StrokeSegment {

    data class Line(val point: Offset) : StrokeSegment()

    data class QuadCurve(
        val to: Offset,
        val control: Offset
    ) : StrokeSegment()

    data class Arc(
        val center: Offset,
        val radius: Float,
        val startAngle: Float,
        val endAngle: Float,
        val clockwise: Boolean
    ) : StrokeSegment()
}