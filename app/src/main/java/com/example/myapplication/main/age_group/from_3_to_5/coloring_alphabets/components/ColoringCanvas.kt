package com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.helper.ColoringHelper.createPath
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.helper.VectorPathParser
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model.ColoringAlphabetsViewModel
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model.StrokeData
import kotlin.math.min

@Composable
fun ColoringCanvas(
    res: Int,
    outlineName: String,
    strokes: List<StrokeData>,
    viewModel: ColoringAlphabetsViewModel
) {

    val context = LocalContext.current

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer(alpha = 0.99f)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = viewModel::startStroke,
                    onDrag = { change, _ ->
                        change.consume()
                        viewModel.addPoint(change.position)
                    },
                    onDragEnd = viewModel::endStroke
                )
            }
    ) {

        val vector = VectorPathParser.getPath(context, res, outlineName)

//        val scale = min(size.width / 960f, size.height / 960f)
//        val matrix = Matrix().apply { scale(scale, scale) }
//        val scaledPath = vector.path.copy().apply { transform(matrix) }
//
//        val bounds = scaledPath.getBounds()
//        val dx = (size.width - bounds.width) / 2 - bounds.left
//        val dy = (size.height - bounds.height) / 2 - bounds.top
//
//        val finalPath = scaledPath.apply {
//            translate(Offset(dx, dy))
//        }

        // 🔥 GET ORIGINAL PATH
        val rawPath = vector.path.copy()

        // 🔥 ORIGINAL BOUNDS
        val originalBounds = rawPath.getBounds()

        // 🔥 ADD SAFE PADDING (IMPORTANT)
        val paddingPercent = 0.1f // 10% padding

        val paddedWidth = originalBounds.width * (1f + paddingPercent)
        val paddedHeight = originalBounds.height * (1f + paddingPercent)

        // 🔥 SCALE BASED ON PADDED SIZE
        val scaleX = size.width / paddedWidth
        val scaleY = size.height / paddedHeight
        val scale = min(scaleX, scaleY)

        // 🔥 APPLY SCALE
        val matrix = Matrix().apply {
            scale(scale, scale)
        }

        val scaledPath = rawPath.apply {
            transform(matrix)
        }

        // 🔥 NEW BOUNDS AFTER SCALE
        val scaledBounds = scaledPath.getBounds()

        // 🔥 CENTER PERFECTLY
        val dx = (size.width - scaledBounds.width) / 2 - scaledBounds.left
        val dy = (size.height - scaledBounds.height) / 2 - scaledBounds.top

        val finalPath = scaledPath.apply {
            translate(Offset(dx, dy))
        }

        // ============================================================
        // ✅ STEP 1: ORIGINAL PATH
        // ============================================================
        drawPath(
            path = finalPath,
            color = vector.color
        )

        // ============================================================
        // ✅ STEP 2: DRAW PAINT ON LAYER
        // ============================================================
        drawIntoCanvas { canvas ->

            val paint = Paint().apply {
                isAntiAlias = true
            }

            canvas.saveLayer(Rect(Offset.Zero, size), paint)

            // 🔥 CLIP FIRST
            clipPath(finalPath) {

                // DRAW OLD STROKES
                strokes.forEach { stroke ->

                    if (stroke.isEraser) {
                        drawPath(
                            path = createPath(stroke.points),
                            color = Color.Transparent,
                            style = Stroke(
                                width = stroke.strokeWidth,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            ),
                            blendMode = BlendMode.Clear
                        )
                    } else {
                        drawPath(
                            path = createPath(stroke.points),
                            brush = stroke.brush,
                            style = Stroke(
                                width = stroke.strokeWidth,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    }
                }

                // LIVE STROKE
                if (viewModel.currentStroke.isNotEmpty()) {

                    if (viewModel.uiState.isEraser) {
                        drawPath(
                            path = createPath(viewModel.currentStroke),
                            color = Color.Transparent,
                            style = Stroke(
                                width = viewModel.uiState.strokeSize,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            ),
                            blendMode = BlendMode.Clear
                        )
                    } else {
                        drawPath(
                            path = createPath(viewModel.currentStroke),
                            brush = viewModel.uiState.selectedBrush,
                            style = Stroke(
                                width = viewModel.uiState.strokeSize,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    }
                }
            }

            canvas.restore()
        }

        // ============================================================
        // ✅ STEP 3: DRAW OUTLINE ON TOP
        // ============================================================
//        drawPath(
//            path = finalPath,
//            color = vector.color,
//            style = Stroke(width = 2f)
//        )
    }
}