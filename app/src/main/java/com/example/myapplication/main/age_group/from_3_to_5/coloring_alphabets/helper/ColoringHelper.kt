package com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.helper

import androidx.compose.ui.geometry.Offset

object ColoringHelper {
    fun createPath(points: List<Offset>): androidx.compose.ui.graphics.Path {
        val path = androidx.compose.ui.graphics.Path()

        if (points.isEmpty()) return path

        path.moveTo(points.first().x, points.first().y)

        for (i in 1 until points.size) {
            path.lineTo(points[i].x, points[i].y)
        }

        return path
    }
}