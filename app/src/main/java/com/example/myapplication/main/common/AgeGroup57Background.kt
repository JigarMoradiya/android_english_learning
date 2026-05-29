package com.example.myapplication.main.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

private data class AgeGroup57Orb(
    val cx: Float, val cy: Float,
    val radius: Float,
    val color: Color,
    val speed: Float, val phase: Float, val drift: Float
)

private data class AgeGroup57Sparkle(
    val cx: Float, val cy: Float,
    val size: Float,
    val speed: Float, val phase: Float
)

private val age57Orbs = listOf(
    AgeGroup57Orb(0.08f, 0.18f, 230f, Color(0xFF38BDF8), 0.10f, 0.0f,  26f),
    AgeGroup57Orb(0.80f, 0.12f, 195f, Color(0xFF2DD4BF), 0.08f, 1.5f,  20f),
    AgeGroup57Orb(0.52f, 0.82f, 210f, Color(0xFFA78BFA), 0.11f, 0.8f,  24f),
    AgeGroup57Orb(0.20f, 0.78f, 170f, Color(0xFF67E8F9), 0.07f, 2.2f,  18f),
    AgeGroup57Orb(0.88f, 0.58f, 160f, Color(0xFF818CF8), 0.12f, 1.0f,  20f),
    AgeGroup57Orb(0.45f, 0.35f, 180f, Color(0xFF34D399), 0.09f, 3.1f,  14f),
)

private val age57Sparkles = listOf(
    AgeGroup57Sparkle(0.04f, 0.09f, 0.026f, 0.40f, 0.0f),
    AgeGroup57Sparkle(0.92f, 0.07f, 0.020f, 0.35f, 1.2f),
    AgeGroup57Sparkle(0.26f, 0.91f, 0.028f, 0.45f, 0.5f),
    AgeGroup57Sparkle(0.71f, 0.87f, 0.018f, 0.38f, 2.0f),
    AgeGroup57Sparkle(0.95f, 0.40f, 0.022f, 0.42f, 1.7f),
    AgeGroup57Sparkle(0.06f, 0.56f, 0.016f, 0.30f, 3.0f),
    AgeGroup57Sparkle(0.50f, 0.04f, 0.024f, 0.50f, 0.3f),
    AgeGroup57Sparkle(0.74f, 0.50f, 0.015f, 0.55f, 2.5f),
    AgeGroup57Sparkle(0.37f, 0.14f, 0.019f, 0.36f, 1.1f),
    AgeGroup57Sparkle(0.60f, 0.63f, 0.021f, 0.44f, 0.7f),
)

@Composable
fun AgeGroup57Background(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(Unit) {
        val startNanos = withFrameNanos { it }
        while (true) {
            withFrameNanos { frameNanos ->
                time = (frameNanos - startNanos) / 1_000_000_000f
            }
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        drawRect(
            brush = Brush.linearGradient(
                colorStops = arrayOf(
                    0.00f to Color(0xFFF0F9FF),
                    0.45f to Color(0xFFF5FFFE),
                    1.00f to Color(0xFFEFF6FF)
                ),
                start = Offset(0f, 0f),
                end   = Offset(size.width, size.height)
            )
        )
        drawAge57Orbs(time)
        drawAge57Sparkles(time)
    }
}

private fun DrawScope.drawAge57Orbs(time: Float) {
    for (orb in age57Orbs) {
        val cx = orb.cx * size.width  + sin(time * orb.speed * PI.toFloat() + orb.phase) * orb.drift
        val cy = orb.cy * size.height + cos(time * orb.speed * PI.toFloat() + orb.phase + 1f) * orb.drift * 0.6f
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0.00f to orb.color.copy(alpha = 0.30f),
                    0.60f to orb.color.copy(alpha = 0.10f),
                    1.00f to orb.color.copy(alpha = 0.00f)
                ),
                center = Offset(cx, cy),
                radius = orb.radius
            ),
            radius = orb.radius,
            center = Offset(cx, cy)
        )
    }
}

private fun DrawScope.drawAge57Sparkles(time: Float) {
    val color     = Color(0xFF0284C7)
    val minDim    = min(size.width, size.height)
    val amplitude = size.height * 0.025f
    for (sp in age57Sparkles) {
        val cx = sp.cx * size.width
        val cy = sp.cy * size.height + sin(time * sp.speed * PI.toFloat() + sp.phase) * amplitude
        val r  = sp.size * minDim
        val ir = r * 0.25f
        val path = Path()
        for (i in 0 until 8) {
            val angle  = i * PI.toFloat() / 4f - PI.toFloat() / 4f
            val radius = if (i % 2 == 0) r else ir
            val x = cx + radius * cos(angle)
            val y = cy + radius * sin(angle)
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()
        drawPath(path, color = color.copy(alpha = 0.11f))
    }
}
