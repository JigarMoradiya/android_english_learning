package com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.components


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.helper.ColoringHelper.createPath
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model.ColoringAlphabetsViewModel
import kotlin.math.min

@Composable
fun ColoringCanvasFinal(
    viewModel: ColoringAlphabetsViewModel,
    letterRes: Int
) {
    val imageBitmap = ImageBitmap.imageResource(id = letterRes)

    Box(modifier = Modifier.fillMaxSize()) {

        // Gray background
        Image(
            painter = painterResource(letterRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
            alpha = 0.2f
        )

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
                        onDragEnd = { viewModel.endStroke() }
                    )
                }
        ) {

            val imgW = imageBitmap.width.toFloat()
            val imgH = imageBitmap.height.toFloat()

            val scale = min(size.width / imgW, size.height / imgH)

            val drawW = imgW * scale
            val drawH = imgH * scale

            val left = (size.width - drawW) / 2f
            val top = (size.height - drawH) / 2f

            val dstOffset = IntOffset(left.toInt(), top.toInt())
            val dstSize = IntSize(drawW.toInt(), drawH.toInt())

            drawIntoCanvas { canvas ->

                val paint = Paint()

                // 🔥 Layer 1: Draw strokes
                canvas.saveLayer(Rect(Offset.Zero, size), paint)

                viewModel.strokes.forEach { stroke ->
                    drawPath(
                        path = createPath(stroke.points),
                        brush = stroke.color.toBrush(),
                        style = Stroke(
                            width = stroke.strokeWidth,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                }

                // 🔥 Layer 2: HARD MASK (CRITICAL)
                paint.blendMode = BlendMode.DstIn

                drawImage(
                    image = imageBitmap,
                    srcOffset = IntOffset.Zero,
                    srcSize = IntSize(imageBitmap.width, imageBitmap.height),
                    dstOffset = dstOffset,
                    dstSize = dstSize
                )

                canvas.restore()
            }
        }
    }
}