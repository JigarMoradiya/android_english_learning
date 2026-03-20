package com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.tracing.StrokeSegment
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.tracing.distance
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.tracing.scale
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.view_model.LetterTracingUiState
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.view_model.LetterTracingViewModel
import kotlin.math.min

@Composable
fun TracingCanvas(viewModel: LetterTracingViewModel) {

    val uiState = viewModel.uiState

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        val side = min(constraints.maxWidth, constraints.maxHeight)

        val frame = Rect(
            (constraints.maxWidth - side) / 2f,
            (constraints.maxHeight - side) / 2f,
            (constraints.maxWidth + side) / 2f,
            (constraints.maxHeight + side) / 2f
        )


        // ✅ Drawing layer
        val guides = viewModel.getGuides(frame)

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            viewModel.startStroke(clampToFrame(it, frame))
                        },
                        onDrag = { change, _ ->
                            viewModel.updateStroke(clampToFrame(change.position, frame))
                        }
                    )
                }
        ) {

            val strokeWidth = side * 0.06f

            // BLACK letter
            viewModel.strokes.forEach { stroke ->
                stroke.zipWithNext().forEach {
                    drawLine(
                        Color.Black,
                        scale(it.first, frame),
                        scale(it.second, frame),
                        strokeWidth
                    )
                }
            }

            // RED finished
            uiState.finishedStrokes.forEach { stroke ->

                val path = Path().apply {
                    moveTo(stroke.first().x, stroke.first().y)
                    stroke.drop(1).forEach {
                        lineTo(it.x, it.y)
                    }
                }

                drawPath(
                    path = path,
                    color = Color.Red,
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }

            // CURRENT stroke
            if (uiState.drawnPoints.isNotEmpty()) {

                val path = Path().apply {
                    moveTo(uiState.drawnPoints.first().x, uiState.drawnPoints.first().y)

                    uiState.drawnPoints.drop(1).forEach {
                        lineTo(it.x, it.y)
                    }
                }

                drawPath(
                    path = path,
                    color = Color.Red,
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round,   // 🔥 IMPORTANT
                        join = StrokeJoin.Round  // 🔥 IMPORTANT
                    )
                )
            }

            // START POINT
            guides.getOrNull(uiState.strokeIndex)?.firstOrNull()?.let {
                drawCircle(Color.Green, strokeWidth, it)
            }
        }
    }
}

fun clampToFrame(point: Offset, frame: Rect) : Offset{
    return Offset(
        x = point.x.coerceIn(frame.left, frame.right),
        y = point.y.coerceIn(frame.top, frame.bottom)
    )
}