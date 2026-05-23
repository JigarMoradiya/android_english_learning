package com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.presentation.canvas

import android.graphics.BitmapFactory
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.min
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.helper.drawArrow
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.LetterMode
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.AlphabetTracingViewModel
import com.example.myapplication.main.common.getImageResFromWord
import kotlin.math.min

@Composable
fun TracingCanvas(viewModel: AlphabetTracingViewModel = hiltViewModel()) {

    val uiState = viewModel.uiState
    val strokeColor = viewModel.getLetterColor()

    val context = LocalContext.current
    val currentWord = viewModel.lettersData.getOrNull(uiState.currentIndex)?.second
    val imageRes = getImageResFromWord(currentWord)
    val imageBitmap: ImageBitmap? = remember(imageRes) {
        imageRes?.let { BitmapFactory.decodeResource(context.resources, it)?.asImageBitmap() }
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val isLowercase = uiState.mode == LetterMode.LOWERCASE
        val sizePx = min(constraints.maxWidth, constraints.maxHeight).toFloat()
        val frame = Rect(0f, 0f, sizePx, sizePx)
        val guides = viewModel.getGuides(frame)
        val strokeWidth = sizePx * if (isLowercase) 0.055f else 0.085f

        Canvas(
            modifier = Modifier
                .size(min(maxWidth, maxHeight))
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { offset ->
                            viewModel.onTap(offset)   // 👈 NEW
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { viewModel.startStroke(it) },
                        onDrag = { change, _ ->
                            viewModel.updateStroke(change.position)
                        }
                    )
                }
        ) {

            val isCompleted = uiState.strokeIndex >= guides.size

            // -------------------------------
            // 1️⃣ BASE LETTER
            // -------------------------------
            guides.forEach { stroke ->

                val path = Path().apply {
                    moveTo(stroke.first().x, stroke.first().y)
                    stroke.drop(1).forEach { lineTo(it.x, it.y) }
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

            // -------------------------------
            // 2️⃣ DASH GUIDE
            // -------------------------------
            guides.getOrNull(uiState.strokeIndex)?.let { stroke ->

                val path = Path().apply {
                    moveTo(stroke.first().x, stroke.first().y)
                    stroke.drop(1).forEach { lineTo(it.x, it.y) }
                }

                val dashLength = strokeWidth * 0.2f
                val gapLength = strokeWidth * 0.2f

                drawPath(
                    path = path,
                    color = Color.White.copy(alpha = 0.9f),
                    style = Stroke(
                        width = strokeWidth * 0.12f,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashLength, gapLength))
                    )
                )
            }

            // -------------------------------
            // 3️⃣ FINISHED STROKES (GLOW)
            // -------------------------------
            uiState.finishedStrokes.forEach { stroke ->

                val path = Path().apply {
                    moveTo(stroke.first().x, stroke.first().y)
                    stroke.drop(1).forEach { lineTo(it.x, it.y) }
                }

                // glow
                drawPath(
                    path = path,
                    color = strokeColor.copy(alpha = 0.25F),
                    style = Stroke(
                        width = strokeWidth * if (isLowercase) 1.35f else 1.4f,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )

                // main
                drawPath(
                    path = path,
                    color = strokeColor,
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }

            // -------------------------------
            // 3b️⃣ FINISHED STROKE DECORATIONS
            // -------------------------------
            imageBitmap?.let { bitmap ->
                val decorSize = sizePx * 0.06f
                val decorStep = 8
                val decorAlpha = if (isCompleted) 1f else 0.35f

                uiState.finishedStrokes.forEachIndexed { idx, stroke ->
                    stroke.filterIndexed { pidx, _ -> pidx % decorStep == 0 }
                        .forEachIndexed { pidx, point ->
                            val rotDeg = ((pidx * 47 + idx * 23) % 60 - 30).toFloat()
                            withTransform({ rotate(degrees = rotDeg, pivot = point) }) {
                                drawImage(
                                    image = bitmap,
                                    dstOffset = IntOffset(
                                        (point.x - decorSize / 2).toInt(),
                                        (point.y - decorSize / 2).toInt()
                                    ),
                                    dstSize = IntSize(decorSize.toInt(), decorSize.toInt()),
                                    alpha = decorAlpha
                                )
                            }
                        }
                }
            }

            // -------------------------------
            // 4️⃣ CURRENT STROKE (SMOOTH + GLOW)
            // -------------------------------
            if (uiState.drawnPoints.size > 1) {

                val points = uiState.drawnPoints

                val smoothPath = Path().apply {

                    moveTo(points.first().x, points.first().y)

                    for (i in 1 until points.size) {
                        val prev = points[i - 1]
                        val curr = points[i]

                        val mid = Offset(
                            (prev.x + curr.x) / 2,
                            (prev.y + curr.y) / 2
                        )

                        quadraticTo(
                            prev.x,
                            prev.y,
                            mid.x,
                            mid.y
                        )
                    }
                }

                // glow
                drawPath(
                    path = smoothPath,
                    color = strokeColor.copy(alpha = 0.25f),
                    style = Stroke(
                        width = strokeWidth * if (isLowercase) 1.5f else 1.6f,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )

                // main
                drawPath(
                    path = smoothPath,
                    color = strokeColor,
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )

                // finger follower dot
                drawCircle(
                    color = strokeColor,
                    radius = strokeWidth * if (isLowercase) 0.16f else 0.2f,
                    center = points.last()
                )
            }

            // -------------------------------
            // 4b️⃣ LIVE STROKE DECORATIONS
            // -------------------------------
            imageBitmap?.let { bitmap ->
                val decorSize = sizePx * 0.06f
                val decorStep = 8

                uiState.drawnPoints.filterIndexed { pidx, _ -> pidx % decorStep == 0 }
                    .forEachIndexed { pidx, point ->
                        val rotDeg = ((pidx * 47) % 60 - 30).toFloat()
                        withTransform({ rotate(degrees = rotDeg, pivot = point) }) {
                            drawImage(
                                image = bitmap,
                                dstOffset = IntOffset(
                                    (point.x - decorSize / 2).toInt(),
                                    (point.y - decorSize / 2).toInt()
                                ),
                                dstSize = IntSize(decorSize.toInt(), decorSize.toInt()),
                                alpha = 1f
                            )
                        }
                    }
            }

            // -------------------------------
            // 5️⃣ START DOT + ARROW + PROGRESS DOT
            // -------------------------------
            if (!isCompleted) {

                val currentStroke = guides.getOrNull(uiState.strokeIndex)

                // progress dot
                if (
                    currentStroke != null &&
                    uiState.progressIndex > 0 &&
                    uiState.progressIndex < currentStroke.size
                ) {
                    val point = currentStroke[uiState.progressIndex]

                    drawCircle(
                        color = Color.Black,
                        radius = strokeWidth * if (isLowercase) 0.22f else 0.28f,
                        center = point
                    )
                }

                // start dot + arrow
                guides.getOrNull(uiState.strokeIndex)?.firstOrNull()?.let { start ->

                    drawCircle(
                        color = Color.Black,
                        radius = strokeWidth * if (isLowercase) 0.32f else 0.4f,
                        center = start
                    )

                    val guide = guides[uiState.strokeIndex]

                    if (guide.size > 5) {

                        val next = guide[5]

                        drawArrow(
                            start = start,
                            end = next,
                            size = strokeWidth * if (isLowercase) 0.5f else 0.6f,
                        )
                    }
                }
            }
        }
    }
}