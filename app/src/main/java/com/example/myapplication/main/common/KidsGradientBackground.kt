package com.example.myapplication.main.common

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

enum class KidsBackgroundScheme { FillBlank, ArrangeLetters }

private data class ShapeData(
    val x: Float,     // normalized 0..1
    val y: Float,     // normalized 0..1
    val size: Float,  // normalized relative to min(w,h)
    val speed: Float, // oscillation speed multiplier
    val phase: Float  // start phase offset
)

private val starShapes = listOf(
    ShapeData(0.05f, 0.12f, 0.055f, 1.00f, 0.0f),   // top-left corner
    ShapeData(0.22f, 0.68f, 0.038f, 0.75f, 1.2f),   // left, lower
    ShapeData(0.35f, 0.25f, 0.050f, 1.25f, 0.7f),   // center-left, upper
    ShapeData(0.58f, 0.82f, 0.062f, 0.875f, 2.1f),  // center-right, bottom
    ShapeData(0.78f, 0.18f, 0.036f, 1.125f, 0.5f),  // right, upper
    ShapeData(0.90f, 0.50f, 0.048f, 0.625f, 1.8f),  // right edge, middle
    ShapeData(0.12f, 0.44f, 0.030f, 1.375f, 3.0f),  // left edge, middle
    ShapeData(0.95f, 0.78f, 0.055f, 0.950f, 0.3f),  // bottom-right corner
    ShapeData(0.45f, 0.08f, 0.040f, 1.050f, 2.5f),  // top edge, left-center
    ShapeData(0.65f, 0.38f, 0.032f, 0.800f, 1.6f),  // center-right
    ShapeData(0.10f, 0.85f, 0.055f, 0.950f, 0.3f),  // bottom-left corner
)

private val bubbleShapes = listOf(
    ShapeData(0.04f, 0.10f, 0.060f, 0.950f, 0.5f),  // top-left corner
    ShapeData(0.06f, 0.72f, 0.042f, 1.125f, 2.0f),  // left edge, lower
    ShapeData(0.20f, 0.87f, 0.052f, 0.700f, 1.4f),  // bottom-left
    ShapeData(0.80f, 0.7f, 0.035f, 1.300f, 0.8f),  // bottom-right
    ShapeData(0.92f, 0.15f, 0.065f, 0.825f, 3.2f),  // top-right corner
    ShapeData(0.96f, 0.55f, 0.038f, 1.200f, 1.0f),  // right edge, middle
    ShapeData(0.48f, 0.94f, 0.058f, 0.875f, 0.2f),  // bottom edge, center
    ShapeData(0.08f, 0.42f, 0.045f, 1.000f, 1.9f),  // left edge, middle
    ShapeData(0.50f, 0.40f, 0.032f, 1.050f, 1.1f),  // center
    ShapeData(0.35f, 0.60f, 0.028f, 1.375f, 2.4f),  // center-left
    ShapeData(0.7f, 0.3f, 0.038f, 0.900f, 0.8f),  // center-right, upper
    ShapeData(0.3f, 0.15f, 0.040f, 1.100f, 1.7f),  // top center, slightly below
)

@Composable
fun KidsGradientBackground(scheme: KidsBackgroundScheme) {
    val gradientColors = remember(scheme) {
        when (scheme) {
            KidsBackgroundScheme.FillBlank -> listOf(Color(0xFFEDD9FF), Color(0xFFD0ECFF))
            KidsBackgroundScheme.ArrangeLetters -> listOf(Color(0xFFFFEDD5), Color(0xFFFFF8D0))
        }
    }

    val shapeColor = remember(scheme) {
        when (scheme) {
            KidsBackgroundScheme.FillBlank -> Color(0xFF9B59B6)
            KidsBackgroundScheme.ArrangeLetters -> Color(0xFFFF8C42)
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "kidsBackground")
    val animPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "bgPhase"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = gradientColors,
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            val minDim = min(size.width, size.height)
            val amplitude = size.height * 0.03f
            val shapes = if (scheme == KidsBackgroundScheme.FillBlank) starShapes else bubbleShapes

            for (shape in shapes) {
                val cx = shape.x * size.width
                val baseY = shape.y * size.height
                val dy = sin((animPhase * shape.speed + shape.phase).toDouble()).toFloat() * amplitude
                val cy = baseY + dy
                val r = shape.size * minDim

                when (scheme) {
                    KidsBackgroundScheme.FillBlank -> drawStar(cx, cy, r, shapeColor)
                    KidsBackgroundScheme.ArrangeLetters -> drawBubble(cx, cy, r, shapeColor)
                }
            }
        }
    }
}

private fun DrawScope.drawStar(cx: Float, cy: Float, outerRadius: Float, color: Color) {
    val innerRadius = outerRadius * 0.42f
    val path = Path()

    for (i in 0 until 10) {
        val angle = i * PI / 5.0 - PI / 2.0
        val r = if (i % 2 == 0) outerRadius else innerRadius
        val x = cx + r * cos(angle).toFloat()
        val y = cy + r * sin(angle).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    drawPath(path, color = color.copy(alpha = 0.13f))
}

private fun DrawScope.drawBubble(cx: Float, cy: Float, radius: Float, color: Color) {
    drawCircle(
        color = color.copy(alpha = 0.10f),
        radius = radius,
        center = Offset(cx, cy)
    )
    drawCircle(
        color = color.copy(alpha = 0.18f),
        radius = radius,
        center = Offset(cx, cy),
        style = Stroke(width = 2.dp.toPx())
    )
}

@androidx.compose.ui.tooling.preview.Preview(
    showBackground = true,
    device = "spec:width=1280dp,height=720dp,dpi=240"
)
@Composable
private fun PreviewFillBlank() {
    KidsGradientBackground(scheme = KidsBackgroundScheme.FillBlank)
}

@androidx.compose.ui.tooling.preview.Preview(
    showBackground = true,
    device = "spec:width=1280dp,height=720dp,dpi=240"
)
@Composable
private fun PreviewArrangeLetters() {
    KidsGradientBackground(scheme = KidsBackgroundScheme.ArrangeLetters)
}
