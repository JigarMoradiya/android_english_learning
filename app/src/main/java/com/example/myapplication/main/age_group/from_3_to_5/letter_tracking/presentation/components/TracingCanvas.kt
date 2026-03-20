package com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.min
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.tracing.StrokeSegment
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.tracing.distance
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.tracing.scale
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.view_model.LetterTracingUiState
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.view_model.LetterTracingViewModel
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@Composable
fun TracingCanvas(viewModel: LetterTracingViewModel = hiltViewModel()) {

    val uiState = viewModel.uiState

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        val sizePx = min(constraints.maxWidth, constraints.maxHeight).toFloat()

        val frame = Rect(0f, 0f, sizePx, sizePx)

        val guides = viewModel.getGuides(frame)

        val strokeWidth = sizePx * 0.085f

        Canvas(
            modifier = Modifier
                .size(min(maxWidth, maxHeight))
                .pointerInput(Unit) {

                    detectDragGestures(
                        onDragStart = {
                            viewModel.startStroke(it)
                        },
                        onDrag = { change, _ ->
                            viewModel.updateStroke(change.position)
                        }
                    )
                }
        ) {

            // -------------------------------
            // 1️⃣ BLACK base letter (UNCHANGED)
            // -------------------------------
            guides.forEach { stroke ->

                val path = Path().apply {
                    moveTo(stroke.first().x, stroke.first().y)
                    stroke.drop(1).forEach {
                        lineTo(it.x, it.y)
                    }
                }

                drawPath(
                    path = path,
                    color = Color.Gray,
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }

            // ---------------------------------------
            // 2️⃣ WHITE DASH (ONLY CURRENT STROKE) ✅
            // ---------------------------------------
            guides.getOrNull(uiState.strokeIndex)?.let { stroke ->

                val path = Path().apply {
                    moveTo(stroke.first().x, stroke.first().y)
                    stroke.drop(1).forEach {
                        lineTo(it.x, it.y)
                    }
                }

                drawPath(
                    path = path,
                    color = Color.White.copy(alpha = 0.9f),
                    style = Stroke(
                        width = strokeWidth * 0.12f,
                        cap = StrokeCap.Round, // 👈 IMPORTANT (makes dots)
                        join = StrokeJoin.Round,
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(6f, 10f) // 👈 small = dot effect
                        )
                    )
                )
            }

            // -------------------------------
            // 3️⃣ RED finished strokes
            // -------------------------------
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

            // -------------------------------
            // 4️⃣ CURRENT stroke
            // -------------------------------
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
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }


            // -------------------------------
            // 5️⃣ START DOT + ARROW (FIXED) ✅
            // -------------------------------
            val isCompleted = uiState.strokeIndex >= guides.size

            if (!isCompleted) {

                val currentStroke = guides.getOrNull(uiState.strokeIndex)

                if (
                    currentStroke != null &&
                    uiState.progressIndex > 0 &&
                    uiState.progressIndex < currentStroke.size
                ) {

                    val point = currentStroke[uiState.progressIndex]

                    drawCircle(
                        color = Color.Black,
                        radius = strokeWidth * 0.28f,
                        center = point
                    )
                }

                guides.getOrNull(uiState.strokeIndex)?.firstOrNull()?.let { start ->

                    // 🟢 START DOT
                    drawCircle(Color.Green, strokeWidth * 0.4f, start)

                    // 🔵 ARROW
                    val guide = guides[uiState.strokeIndex]

                    if (guide.size > 5) {

                        val next = guide[5]

                        drawArrow(
                            start = start,
                            end = next,
                            size = strokeWidth * 0.6f
                        )
                    }
                }
            }
        }
    }
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