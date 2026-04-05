package com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.helper.ColoringHelper.createPath
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model.ColorOption
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model.ColoringAlphabetsViewModel
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model.Stroke


@Composable
fun ColoringCanvas(viewModel: ColoringAlphabetsViewModel) {

    val strokes = viewModel.strokes
    val currentColor = viewModel.currentColor
    val strokeWidth = viewModel.brushWidth
    val isEraser = viewModel.isEraser

    var currentPoints by remember { mutableStateOf<List<Offset>>(emptyList()) }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)

            // ✋ DRAW GESTURE
            .pointerInput(Unit) {
                detectDragGestures(

                    onDragStart = { offset ->
                        currentPoints = listOf(offset)
                    },

                    onDrag = { change, _ ->
                        change.consume()
                        currentPoints = currentPoints + change.position
                    },

                    onDragEnd = {

                        if (currentPoints.isNotEmpty()) {

                            val stroke = Stroke(
                                points = currentPoints,
                                color = if (isEraser)
                                    ColorOption.Solid(Color.White, "eraser")
                                else currentColor,
                                strokeWidth = strokeWidth
                            )

                            viewModel.addStroke(stroke)
                        }

                        currentPoints = emptyList()
                    }
                )
            }
    ) {

        // 🎨 Draw previous strokes
        strokes.forEach { stroke ->

            drawPath(
                path = createPath(stroke.points),
                brush = stroke.color.toBrush(),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke.strokeWidth)
            )
        }

        // ✨ Draw current stroke (live)
        if (currentPoints.isNotEmpty()) {
            drawPath(
                path = createPath(currentPoints),
                brush = currentColor.toBrush(),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
            )
        }
    }
}