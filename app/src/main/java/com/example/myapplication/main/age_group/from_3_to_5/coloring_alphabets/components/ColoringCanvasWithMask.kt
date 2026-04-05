package com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.components


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntSize
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.helper.ColoringHelper.createPath
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model.ColoringAlphabetsViewModel


@Composable
fun ColoringCanvasWithMask(
    viewModel: ColoringAlphabetsViewModel,
    letterRes: Int
) {
    val strokes = viewModel.strokes

    // ✅ LOAD IMAGE HERE (Composable scope)
    val imageBitmap = ImageBitmap.imageResource(id = letterRes)

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { viewModel.startStroke(it) },
                    onDrag = { change, _ ->
                        change.consume()
                        viewModel.addPoint(change.position)
                    },
                    onDragEnd = {
                        viewModel.endStroke()
                    }
                )
            }
    ) {

        drawIntoCanvas { canvas ->
            val paint = Paint()

            canvas.saveLayer(size.toRect(), paint)

            // ✅ DRAW IMAGE
            drawImage(
                image = imageBitmap,
                dstSize = IntSize(size.width.toInt(), size.height.toInt())
            )

            // ✅ MASK MODE
            paint.blendMode = BlendMode.SrcIn

            // ✅ DRAW STROKES (INSIDE ONLY)
            strokes.forEach { stroke ->
                drawPath(
                    path = createPath(stroke.points),
                    brush = stroke.color.toBrush(),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke.strokeWidth)
                )
            }

            canvas.restore()
        }
    }
}