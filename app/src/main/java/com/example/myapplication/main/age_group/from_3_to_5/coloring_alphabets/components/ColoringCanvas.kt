package com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.helper.ColoringHelper.createPath
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.helper.VectorPathParser
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model.ColoringAlphabetsViewModel
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model.StrokeData
import kotlin.math.min

@Composable
fun ColoringCanvas(
    outlineName: String,
    strokes: List<StrokeData>,
    viewModel: ColoringAlphabetsViewModel
) {

    val context = LocalContext.current

    Canvas(
        modifier = Modifier
            .fillMaxSize()
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

        val rawPath = VectorPathParser.getPath(context, outlineName)

        // 🔥 scale
        val scale = min(size.width / 960f, size.height / 960f)
        val matrix = Matrix().apply { scale(scale, scale) }

        val path = rawPath.copy().apply { transform(matrix) }

        // 🔥 center
        val bounds = path.getBounds()
        val dx = (size.width - bounds.width) / 2 - bounds.left
        val dy = (size.height - bounds.height) / 2 - bounds.top
        path.translate(Offset(dx, dy))

        // 🔥 CLIP
        clipPath(path) {

            // ✅ OLD strokes
            strokes.forEach {
                drawPath(
                    path = createPath(it.points),
                    color = Color.Red,
                    style = Stroke(
                        width = it.strokeWidth,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }

            // 🔥 NEW: LIVE stroke (this was missing)
            if (viewModel.currentStroke.isNotEmpty()) {
                drawPath(
                    path = createPath(viewModel.currentStroke),
                    color = Color.Red,
                    style = Stroke(
                        width = 30f,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
        }
        // outline
        drawPath(
            path = path,
            color = Color.Gray,
            style = Stroke(width = 2f)
        )
    }
}