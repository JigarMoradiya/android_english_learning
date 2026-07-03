package com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.presentation.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.min
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.helper.getStrokesForLetter
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.helper.sampleStroke
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.LetterMode
import kotlin.math.min

private const val GUIDE_SPACING = 6f

/**
 * Free-practice canvas: shows a faint reference letter behind the ruled lines
 * and lets the child draw freely on top — no validation or snapping.
 * Stroke state is hoisted so the page's Clear button can reset it.
 */
@Composable
fun FreeDrawCanvas(
    letter: Char,
    mode: LetterMode,
    strokeColor: Color,
    finishedStrokes: List<List<Offset>>,
    currentStroke: List<Offset>,
    onStrokeStart: (Offset) -> Unit,
    onStrokeMove: (Offset) -> Unit,
    onStrokeEnd: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val isLowercase = mode == LetterMode.LOWERCASE
        val sizePx = min(constraints.maxWidth, constraints.maxHeight).toFloat()
        val referenceGuides = remember(letter, mode, sizePx) {
            val frame = Rect(0f, 0f, sizePx, sizePx)
            getStrokesForLetter(letter, mode).map { sampleStroke(it, frame, GUIDE_SPACING) }
        }
        val letterStrokeWidth = sizePx * if (isLowercase) 0.055f else 0.085f
        val drawStrokeWidth = letterStrokeWidth * 0.5f

        // Keep paint inside the card: clamp touch points to the canvas bounds
        fun clampToBounds(point: Offset) = Offset(
            point.x.coerceIn(0f, sizePx),
            point.y.coerceIn(0f, sizePx)
        )

        Canvas(
            modifier = Modifier
                .size(min(maxWidth, maxHeight))
                .clipToBounds()
                .pointerInput(letter, mode) {
                    detectDragGestures(
                        onDragStart = { onStrokeStart(clampToBounds(it)) },
                        onDrag = { change, _ -> onStrokeMove(clampToBounds(change.position)) },
                        onDragEnd = { onStrokeEnd() },
                        onDragCancel = { onStrokeEnd() }
                    )
                }
        ) {

            fun smoothPathOf(points: List<Offset>) = Path().apply {
                moveTo(points.first().x, points.first().y)
                for (i in 1 until points.size) {
                    val prev = points[i - 1]
                    val curr = points[i]
                    val mid = Offset((prev.x + curr.x) / 2, (prev.y + curr.y) / 2)
                    quadraticTo(prev.x, prev.y, mid.x, mid.y)
                }
            }

            // Faint reference letter to copy
            referenceGuides.forEach { stroke ->
                val path = Path().apply {
                    moveTo(stroke.first().x, stroke.first().y)
                    stroke.drop(1).forEach { lineTo(it.x, it.y) }
                }
                drawPath(
                    path = path,
                    color = Color.Gray.copy(alpha = 0.15f),
                    style = Stroke(
                        width = letterStrokeWidth,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }

            // Child's strokes (glow + main), same style as tracing
            (finishedStrokes + listOf(currentStroke)).forEach { points ->
                if (points.size > 1) {
                    val path = smoothPathOf(points)
                    drawPath(
                        path = path,
                        color = strokeColor.copy(alpha = 0.25f),
                        style = Stroke(
                            width = drawStrokeWidth * 1.5f,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                    drawPath(
                        path = path,
                        color = strokeColor,
                        style = Stroke(
                            width = drawStrokeWidth,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                }
            }

            // finger follower dot while drawing
            if (currentStroke.size > 1) {
                drawCircle(
                    color = strokeColor,
                    radius = drawStrokeWidth * 0.4f,
                    center = currentStroke.last()
                )
            }
        }
    }
}