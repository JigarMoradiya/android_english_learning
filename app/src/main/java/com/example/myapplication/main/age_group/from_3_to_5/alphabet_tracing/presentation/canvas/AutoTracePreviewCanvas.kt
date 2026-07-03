package com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.presentation.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.min
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.helper.getStrokesForLetter
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.helper.sampleStroke
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.LetterMode
import kotlinx.coroutines.delay
import kotlin.math.min

// Auto-play timing
private const val POINTS_PER_FRAME = 2
private const val FRAME_DELAY_MS = 16L
private const val STROKE_PAUSE_MS = 400L
private const val LOOP_HOLD_MS = 1500L
private const val GUIDE_SPACING = 6f

/**
 * Non-interactive preview that "traces" the letter by itself, stroke by stroke,
 * with the same look as TracingCanvas (gray base letter, glow stroke, finger dot),
 * then holds the finished letter and loops forever.
 */
@Composable
fun AutoTracePreviewCanvas(
    letter: Char,
    mode: LetterMode,
    strokeColor: Color,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val isLowercase = mode == LetterMode.LOWERCASE
        val sizePx = min(constraints.maxWidth, constraints.maxHeight).toFloat()
        val guides = remember(letter, mode, sizePx) {
            val frame = Rect(0f, 0f, sizePx, sizePx)
            getStrokesForLetter(letter, mode).map { sampleStroke(it, frame, GUIDE_SPACING) }
        }
        val strokeWidth = sizePx * if (isLowercase) 0.055f else 0.085f

        var finishedCount by remember(guides) { mutableIntStateOf(0) }
        var tipIndex by remember(guides) { mutableIntStateOf(0) }

        LaunchedEffect(guides) {
            while (true) {
                finishedCount = 0
                tipIndex = 0
                delay(STROKE_PAUSE_MS)

                guides.forEachIndexed { idx, stroke ->
                    tipIndex = 0
                    var i = 0
                    while (i < stroke.size) {
                        i = (i + POINTS_PER_FRAME).coerceAtMost(stroke.size)
                        tipIndex = i
                        delay(FRAME_DELAY_MS)
                    }
                    finishedCount = idx + 1
                    tipIndex = 0
                    delay(STROKE_PAUSE_MS)
                }

                delay(LOOP_HOLD_MS)
            }
        }

        Canvas(modifier = Modifier.size(min(maxWidth, maxHeight))) {

            fun pathOf(points: List<Offset>) = Path().apply {
                moveTo(points.first().x, points.first().y)
                points.drop(1).forEach { lineTo(it.x, it.y) }
            }

            // Base gray letter
            guides.forEach { stroke ->
                drawPath(
                    path = pathOf(stroke),
                    color = Color.Gray,
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }

            // Finished strokes (glow + main)
            guides.take(finishedCount).forEach { stroke ->
                drawPath(
                    path = pathOf(stroke),
                    color = strokeColor.copy(alpha = 0.25f),
                    style = Stroke(
                        width = strokeWidth * if (isLowercase) 1.35f else 1.4f,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
                drawPath(
                    path = pathOf(stroke),
                    color = strokeColor,
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }

            // Currently animating stroke (glow + main + finger dot)
            val current = guides.getOrNull(finishedCount)
            if (current != null && tipIndex > 1) {
                val points = current.take(tipIndex)
                drawPath(
                    path = pathOf(points),
                    color = strokeColor.copy(alpha = 0.25f),
                    style = Stroke(
                        width = strokeWidth * if (isLowercase) 1.5f else 1.6f,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
                drawPath(
                    path = pathOf(points),
                    color = strokeColor,
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
                drawCircle(
                    color = strokeColor,
                    radius = strokeWidth * if (isLowercase) 0.16f else 0.2f,
                    center = points.last()
                )
            }
        }
    }
}