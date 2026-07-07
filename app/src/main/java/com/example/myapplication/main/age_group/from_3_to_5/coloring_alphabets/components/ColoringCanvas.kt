package com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.helper.ColoringHelper.createPath
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.helper.VectorPathParser
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model.BrushTexture
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model.ColoringAlphabetsViewModel
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model.StrokeData
import kotlin.math.floor
import kotlin.math.min
import kotlin.math.sin

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
                        // The touchscreen samples at up to 120-240Hz, but
                        // onDrag only reports the latest position once per
                        // display frame (~60Hz) — change.position alone
                        // silently drops the in-between samples. On a fast
                        // drag that turns a smooth path into a few long
                        // straight-line jumps, and if one of those jumps
                        // cuts a corner outside the letter's clip mask (even
                        // though the finger stayed on it), that middle
                        // portion gets clipped away — the disconnected
                        // stroke segments seen in testing. change.historical
                        // has the samples in between; drawing through those
                        // too keeps the path following where the finger
                        // actually went.
                        change.historical.forEach { viewModel.addPoint(it.position) }
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
                        drawTexturedStroke(
                            points = stroke.points,
                            brush = stroke.brush,
                            strokeWidth = stroke.strokeWidth,
                            style = stroke.style,
                            seed = stroke.seed
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
                        drawTexturedStroke(
                            points = viewModel.currentStroke,
                            brush = viewModel.uiState.selectedBrush,
                            strokeWidth = viewModel.uiState.strokeSize,
                            style = viewModel.uiState.selectedStyle,
                            seed = viewModel.currentStrokeSeed
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

// Deterministic "random" in [0, 1) from an integer seed — using true
// randomness here would make already-drawn parts of a stroke flicker on
// every recomposition triggered by a new point being added mid-drag.
private fun pseudoRandom(seed: Int): Float {
    val x = sin(seed * 12.9898) * 43758.5453
    return (x - floor(x)).toFloat()
}

// How a stroke's fill actually renders on the canvas — procedural stand-ins
// for real crayon/glitter/stamp artwork (no texture assets exist yet).
private fun DrawScope.drawTexturedStroke(
    points: List<Offset>,
    brush: Brush,
    strokeWidth: Float,
    style: BrushTexture,
    seed: Int
) {
    if (points.isEmpty()) return

    when (style) {
        BrushTexture.FLAT -> {
            drawPath(
                path = createPath(points),
                brush = brush,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
        }

        BrushTexture.GRAIN -> {
            // Sandy/speckled look — lots of small dots with slightly
            // randomized size/opacity/offset instead of one smooth fill.
            points.forEachIndexed { idx, pt ->
                val r1 = pseudoRandom(seed + idx)
                val r2 = pseudoRandom(seed + idx + 1000)
                val diameter = strokeWidth * (0.35f + r1 * 0.35f)
                val center = Offset(
                    pt.x + (r2 - 0.5f) * strokeWidth * 0.3f,
                    pt.y + (r1 - 0.5f) * strokeWidth * 0.3f
                )
                drawCircle(brush = brush, radius = diameter / 2, center = center, alpha = 0.55f + r2 * 0.45f)
            }
        }

        BrushTexture.SPARKLE -> {
            drawPath(
                path = createPath(points),
                brush = brush,
                alpha = 0.35f,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
            points.forEachIndexed { idx, pt ->
                if (idx % 3 != 0) return@forEachIndexed
                val r1 = pseudoRandom(seed + idx * 7)
                val r2 = pseudoRandom(seed + idx * 13)
                val offset = strokeWidth * 0.4f
                val dotSize = strokeWidth * (0.14f + r1 * 0.16f)
                val center = Offset(pt.x + (r1 - 0.5f) * offset, pt.y + (r2 - 0.5f) * offset)
                drawCircle(color = Color.White, radius = dotSize / 2, center = center, alpha = 0.5f + r2 * 0.5f)
            }
        }

        BrushTexture.DOTTED -> {
            // Evenly-spaced stamped dots along the path — a stand-in for a
            // repeating pattern stamp until real texture art exists.
            val spacing = (strokeWidth * 0.6f).coerceAtLeast(6f)
            var distanceSinceLastDot = spacing
            var previous = points.first()
            points.forEach { pt ->
                distanceSinceLastDot += (pt - previous).getDistance()
                if (distanceSinceLastDot >= spacing) {
                    drawCircle(brush = brush, radius = strokeWidth * 0.25f, center = pt)
                    distanceSinceLastDot = 0f
                }
                previous = pt
            }
        }
    }
}