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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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

enum class KidsGradient {
    rosePink, creamMint, purpleBlue, peachYellow, grayBlue, pinkPeach,
    mintLime, skyLavender, tealCyan, aquaGreen, peachCoral, blueIndigo,
    indigoPurple, periwinkleBlue, lilacPink, pinkVanilla, softBlue, seaBlue,
    oceanBlue, meadowGreen
}

enum class KidsFloatingShape {
    none,
    hearts, mixShapes, stars, bubbles, dots, sparkles,
    musicNotes, diamonds, leaves, speechBubbles, curveLines, waves
}

private data class ShapeData(
    val x: Float,
    val y: Float,
    val size: Float,
    val speed: Float,
    val phase: Float
)

// MARK: - Position Sets

private val starShapes = listOf(
    ShapeData(0.05f, 0.12f, 0.055f, 1.00f, 0.0f),
    ShapeData(0.22f, 0.68f, 0.038f, 0.75f, 1.2f),
    ShapeData(0.35f, 0.25f, 0.050f, 1.25f, 0.7f),
    ShapeData(0.58f, 0.82f, 0.062f, 0.875f, 2.1f),
    ShapeData(0.78f, 0.18f, 0.036f, 1.125f, 0.5f),
    ShapeData(0.90f, 0.50f, 0.048f, 0.625f, 1.8f),
    ShapeData(0.12f, 0.44f, 0.030f, 1.375f, 3.0f),
    ShapeData(0.95f, 0.78f, 0.055f, 0.950f, 0.3f),
    ShapeData(0.45f, 0.08f, 0.040f, 1.050f, 2.5f),
    ShapeData(0.65f, 0.38f, 0.032f, 0.800f, 1.6f),
    ShapeData(0.10f, 0.85f, 0.055f, 0.950f, 0.3f),
)

private val bubbleShapes = listOf(
    ShapeData(0.04f, 0.10f, 0.060f, 0.950f, 0.5f),
    ShapeData(0.06f, 0.72f, 0.042f, 1.125f, 2.0f),
    ShapeData(0.20f, 0.87f, 0.052f, 0.700f, 1.4f),
    ShapeData(0.80f, 0.70f, 0.035f, 1.300f, 0.8f),
    ShapeData(0.92f, 0.15f, 0.065f, 0.825f, 3.2f),
    ShapeData(0.96f, 0.55f, 0.038f, 1.200f, 1.0f),
    ShapeData(0.48f, 0.94f, 0.058f, 0.875f, 0.2f),
    ShapeData(0.08f, 0.42f, 0.045f, 1.000f, 1.9f),
    ShapeData(0.50f, 0.40f, 0.032f, 1.050f, 1.1f),
    ShapeData(0.35f, 0.60f, 0.028f, 1.375f, 2.4f),
    ShapeData(0.70f, 0.30f, 0.038f, 0.900f, 0.8f),
    ShapeData(0.30f, 0.15f, 0.040f, 1.100f, 1.7f),
)

private val posSetA = listOf(
    ShapeData(0.06f, 0.08f, 0.052f, 0.950f, 0.3f),
    ShapeData(0.18f, 0.72f, 0.040f, 1.125f, 1.5f),
    ShapeData(0.32f, 0.20f, 0.048f, 0.800f, 2.2f),
    ShapeData(0.50f, 0.88f, 0.058f, 1.300f, 0.8f),
    ShapeData(0.68f, 0.12f, 0.036f, 1.000f, 3.1f),
    ShapeData(0.82f, 0.68f, 0.060f, 0.700f, 1.0f),
    ShapeData(0.92f, 0.35f, 0.038f, 1.200f, 2.7f),
    ShapeData(0.10f, 0.45f, 0.044f, 0.875f, 0.5f),
    ShapeData(0.45f, 0.50f, 0.030f, 1.375f, 1.8f),
    ShapeData(0.72f, 0.82f, 0.042f, 1.050f, 0.2f),
    ShapeData(0.38f, 0.05f, 0.034f, 1.500f, 2.0f),
)

private val posSetB = listOf(
    ShapeData(0.03f, 0.18f, 0.055f, 1.050f, 0.6f),
    ShapeData(0.15f, 0.82f, 0.038f, 0.750f, 2.4f),
    ShapeData(0.28f, 0.35f, 0.045f, 1.250f, 1.1f),
    ShapeData(0.55f, 0.10f, 0.052f, 0.900f, 0.0f),
    ShapeData(0.70f, 0.65f, 0.032f, 1.200f, 3.0f),
    ShapeData(0.88f, 0.22f, 0.062f, 0.625f, 1.7f),
    ShapeData(0.95f, 0.78f, 0.040f, 1.375f, 0.4f),
    ShapeData(0.42f, 0.70f, 0.048f, 0.950f, 2.1f),
    ShapeData(0.60f, 0.42f, 0.028f, 1.550f, 1.3f),
    ShapeData(0.22f, 0.55f, 0.044f, 0.800f, 0.9f),
    ShapeData(0.78f, 0.08f, 0.036f, 1.125f, 2.6f),
)

private val posSetC = listOf(
    ShapeData(0.08f, 0.25f, 0.048f, 1.000f, 1.2f),
    ShapeData(0.22f, 0.88f, 0.042f, 1.300f, 2.8f),
    ShapeData(0.40f, 0.15f, 0.058f, 0.700f, 0.4f),
    ShapeData(0.58f, 0.75f, 0.035f, 1.125f, 1.9f),
    ShapeData(0.75f, 0.30f, 0.050f, 0.950f, 3.2f),
    ShapeData(0.90f, 0.60f, 0.062f, 0.800f, 0.7f),
    ShapeData(0.05f, 0.62f, 0.038f, 1.375f, 2.3f),
    ShapeData(0.35f, 0.48f, 0.028f, 1.500f, 0.0f),
    ShapeData(0.65f, 0.85f, 0.045f, 0.875f, 1.5f),
    ShapeData(0.82f, 0.10f, 0.040f, 1.200f, 2.6f),
    ShapeData(0.50f, 0.32f, 0.032f, 1.050f, 0.8f),
)

private val posSetD = listOf(
    ShapeData(0.05f, 0.30f, 0.055f, 0.875f, 1.8f),
    ShapeData(0.20f, 0.10f, 0.038f, 1.200f, 0.3f),
    ShapeData(0.38f, 0.80f, 0.052f, 0.750f, 2.5f),
    ShapeData(0.52f, 0.22f, 0.040f, 1.375f, 0.9f),
    ShapeData(0.68f, 0.55f, 0.030f, 1.050f, 3.1f),
    ShapeData(0.85f, 0.85f, 0.060f, 0.700f, 1.2f),
    ShapeData(0.95f, 0.18f, 0.035f, 1.300f, 2.0f),
    ShapeData(0.12f, 0.65f, 0.045f, 0.950f, 0.6f),
    ShapeData(0.42f, 0.40f, 0.028f, 1.625f, 1.4f),
    ShapeData(0.72f, 0.15f, 0.048f, 1.000f, 2.9f),
    ShapeData(0.88f, 0.48f, 0.036f, 1.125f, 0.1f),
)

private val heartShapes = listOf(
    ShapeData(0.08f, 0.12f, 0.062f, 0.950f, 0.0f),
    ShapeData(0.28f, 0.80f, 0.044f, 1.125f, 1.5f),
    ShapeData(0.45f, 0.06f, 0.055f, 0.750f, 2.2f),
    ShapeData(0.62f, 0.88f, 0.060f, 1.300f, 0.7f),
    ShapeData(0.82f, 0.10f, 0.040f, 1.000f, 3.1f),
    ShapeData(0.94f, 0.55f, 0.065f, 0.700f, 1.0f),
    ShapeData(0.15f, 0.48f, 0.046f, 1.200f, 2.7f),
    ShapeData(0.55f, 0.45f, 0.038f, 1.375f, 1.8f),
    ShapeData(0.88f, 0.82f, 0.052f, 0.900f, 0.4f),
    ShapeData(0.35f, 0.30f, 0.032f, 1.550f, 2.0f),
    ShapeData(0.72f, 0.42f, 0.048f, 1.050f, 0.9f),
    ShapeData(0.05f, 0.72f, 0.040f, 1.250f, 1.3f),
)

private val mixShapesSet = listOf(
    ShapeData(0.08f, 0.12f, 0.052f, 1.000f, 0.0f),
    ShapeData(0.28f, 0.80f, 0.040f, 1.250f, 1.5f),
    ShapeData(0.52f, 0.22f, 0.058f, 0.800f, 0.8f),
    ShapeData(0.72f, 0.68f, 0.044f, 1.125f, 2.2f),
    ShapeData(0.90f, 0.35f, 0.050f, 0.950f, 1.0f),
    ShapeData(0.15f, 0.50f, 0.038f, 1.375f, 2.8f),
    ShapeData(0.42f, 0.90f, 0.065f, 0.700f, 0.3f),
    ShapeData(0.68f, 0.15f, 0.042f, 1.200f, 1.8f),
    ShapeData(0.85f, 0.78f, 0.055f, 0.875f, 3.1f),
    ShapeData(0.32f, 0.38f, 0.032f, 1.500f, 0.6f),
    ShapeData(0.58f, 0.55f, 0.046f, 1.050f, 2.0f),
    ShapeData(0.95f, 0.88f, 0.040f, 1.300f, 1.3f),
    ShapeData(0.05f, 0.68f, 0.062f, 0.750f, 0.9f),
    ShapeData(0.45f, 0.08f, 0.036f, 1.450f, 2.5f),
    ShapeData(0.78f, 0.45f, 0.048f, 1.075f, 1.6f),
)

@Composable
fun KidsGradientBackground(gradient: KidsGradient, shape: KidsFloatingShape) {

    val gradientColors = remember(gradient) {
        when (gradient) {
            KidsGradient.rosePink       -> listOf(Color(0xFFFFE4F0), Color(0xFFFFCCE0))
            KidsGradient.creamMint      -> listOf(Color(0xFFFFF8F0), Color(0xFFF0FFF8))
            KidsGradient.purpleBlue     -> listOf(Color(0xFFEDD9FF), Color(0xFFD0ECFF))
            KidsGradient.peachYellow    -> listOf(Color(0xFFFFEDD5), Color(0xFFFFF8D0))
            KidsGradient.grayBlue       -> listOf(Color(0xFFF0EEF8), Color(0xFFE0E8F5))
            KidsGradient.pinkPeach      -> listOf(Color(0xFFFFE0EE), Color(0xFFFFD5E8))
            KidsGradient.mintLime       -> listOf(Color(0xFFD5F5E0), Color(0xFFE8FAD0))
            KidsGradient.skyLavender    -> listOf(Color(0xFFE0EEFF), Color(0xFFEDE0FF))
            KidsGradient.tealCyan       -> listOf(Color(0xFFD0F0F5), Color(0xFFD0EEFF))
            KidsGradient.aquaGreen      -> listOf(Color(0xFFC8F5EC), Color(0xFFD5F5D0))
            KidsGradient.peachCoral     -> listOf(Color(0xFFFFE8D5), Color(0xFFFFD5CC))
            KidsGradient.blueIndigo     -> listOf(Color(0xFFD5E5FF), Color(0xFFE0D5FF))
            KidsGradient.indigoPurple   -> listOf(Color(0xFFE0D5FF), Color(0xFFD5E5FF))
            KidsGradient.periwinkleBlue -> listOf(Color(0xFFDDE4FF), Color(0xFFE8D5FF))
            KidsGradient.lilacPink      -> listOf(Color(0xFFF0DEFF), Color(0xFFFFD5EC))
            KidsGradient.pinkVanilla    -> listOf(Color(0xFFFFF0F8), Color(0xFFFFFDE8))
            KidsGradient.softBlue       -> listOf(Color(0xFFE8F0FF), Color(0xFFF3E8FF))
            KidsGradient.seaBlue        -> listOf(Color(0xFFD0EEFF), Color(0xFFC8F5EE))
            KidsGradient.oceanBlue      -> listOf(Color(0xFFBBDEFB), Color(0xFFB3E5FC))
            KidsGradient.meadowGreen    -> listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9))
        }
    }

    val shapeColor = remember(gradient) {
        when (gradient) {
            KidsGradient.rosePink       -> Color(0xFFC2185B)
            KidsGradient.creamMint      -> Color(0xFFFF6B6B)
            KidsGradient.purpleBlue     -> Color(0xFF9B59B6)
            KidsGradient.peachYellow    -> Color(0xFFFF8C42)
            KidsGradient.grayBlue       -> Color(0xFF7F8C8D)
            KidsGradient.pinkPeach      -> Color(0xFFE91E8C)
            KidsGradient.mintLime       -> Color(0xFF27AE60)
            KidsGradient.skyLavender    -> Color(0xFF3F51B5)
            KidsGradient.tealCyan       -> Color(0xFF00ACC1)
            KidsGradient.aquaGreen      -> Color(0xFF16A085)
            KidsGradient.peachCoral     -> Color(0xFFE67E22)
            KidsGradient.blueIndigo     -> Color(0xFF2C3E8C)
            KidsGradient.indigoPurple   -> Color(0xFF6C3E8C)
            KidsGradient.periwinkleBlue -> Color(0xFF5C6BC0)
            KidsGradient.lilacPink      -> Color(0xFF9B27AF)
            KidsGradient.pinkVanilla    -> Color(0xFFFF4081)
            KidsGradient.softBlue       -> Color(0xFF1565C0)
            KidsGradient.seaBlue        -> Color(0xFF0277BD)
            KidsGradient.oceanBlue      -> Color(0xFF1565C0)
            KidsGradient.meadowGreen    -> Color(0xFF2E7D32)
        }
    }

//    val shapes = remember(shape) {
//        when (shape) {
//            KidsFloatingShape.none          -> emptyList()
//            KidsFloatingShape.hearts        -> heartShapes
//            KidsFloatingShape.mixShapes     -> mixShapesSet
//            KidsFloatingShape.stars         -> starShapes
//            KidsFloatingShape.bubbles       -> bubbleShapes
//            KidsFloatingShape.dots          -> posSetA
//            KidsFloatingShape.sparkles      -> posSetA
//            KidsFloatingShape.musicNotes    -> posSetB
//            KidsFloatingShape.diamonds      -> posSetB
//            KidsFloatingShape.leaves        -> posSetC
//            KidsFloatingShape.speechBubbles -> posSetC
//            KidsFloatingShape.curveLines    -> posSetD
//        }
//    }

    val shapes: List<ShapeData> = emptyList()

    val mixColors = remember {
        listOf(
            Color(0xFFFF6B6B), Color(0xFF4ECDC4),
            Color(0xFFFFCA38), Color(0xFF6BCB77), Color(0xFFBC6CE8)
        )
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

            shapes.forEachIndexed { idx, shapeData ->
                val cx = shapeData.x * size.width
                val baseY = shapeData.y * size.height
                val dy = sin((animPhase * shapeData.speed + shapeData.phase).toDouble()).toFloat() * amplitude
                val cy = baseY + dy
                val r = shapeData.size * minDim

                when (shape) {
                    KidsFloatingShape.none          -> Unit
                    KidsFloatingShape.hearts        -> drawHeart(cx, cy, r, shapeColor)
                    KidsFloatingShape.mixShapes     -> {
                        val c = mixColors[idx % mixColors.size]
                        when (idx % 5) {
                            0    -> drawStar(cx, cy, r, c)
                            1    -> drawTriangle(cx, cy, r, c)
                            2    -> drawSquare(cx, cy, r, c)
                            3    -> drawBubble(cx, cy, r, c)
                            else -> drawDiamond(cx, cy, r, c)
                        }
                    }
                    KidsFloatingShape.stars         -> drawStar(cx, cy, r, shapeColor)
                    KidsFloatingShape.bubbles       -> drawBubble(cx, cy, r, shapeColor)
                    KidsFloatingShape.dots          -> drawDot(cx, cy, r, shapeColor)
                    KidsFloatingShape.sparkles      -> drawSparkle(cx, cy, r, shapeColor)
                    KidsFloatingShape.musicNotes    -> drawMusicNote(cx, cy, r, shapeColor)
                    KidsFloatingShape.diamonds      -> drawDiamond(cx, cy, r, shapeColor)
                    KidsFloatingShape.leaves        -> drawLeaf(cx, cy, r, shapeColor)
                    KidsFloatingShape.speechBubbles -> drawSpeechBubble(cx, cy, r, shapeColor)
                    KidsFloatingShape.curveLines    -> drawWavyLine(cx, cy, r, shapeColor)
                    KidsFloatingShape.waves         -> drawWave(cx, cy, r, shapeColor)
                }
            }
        }
    }
}

// MARK: - Shape Drawers

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
    drawCircle(color.copy(alpha = 0.10f), radius = radius, center = Offset(cx, cy))
    drawCircle(color.copy(alpha = 0.18f), radius = radius, center = Offset(cx, cy), style = Stroke(width = 2.dp.toPx()))
}

private fun DrawScope.drawDot(cx: Float, cy: Float, radius: Float, color: Color) {
    drawCircle(color.copy(alpha = 0.15f), radius = radius, center = Offset(cx, cy))
}

private fun DrawScope.drawSparkle(cx: Float, cy: Float, outerRadius: Float, color: Color) {
    val innerRadius = outerRadius * 0.25f
    val path = Path()
    for (i in 0 until 8) {
        val angle = i * PI / 4.0 - PI / 4.0
        val r = if (i % 2 == 0) outerRadius else innerRadius
        val x = cx + r * cos(angle).toFloat()
        val y = cy + r * sin(angle).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    drawPath(path, color = color.copy(alpha = 0.14f))
}

private fun DrawScope.drawMusicNote(cx: Float, cy: Float, outerRadius: Float, color: Color) {
    drawOval(
        color = color.copy(alpha = 0.14f),
        topLeft = Offset(cx - outerRadius * 0.55f, cy + outerRadius * 0.25f),
        size = Size(outerRadius, outerRadius * 0.72f)
    )
    drawLine(
        color = color.copy(alpha = 0.14f),
        start = Offset(cx + outerRadius * 0.38f, cy + outerRadius * 0.55f),
        end   = Offset(cx + outerRadius * 0.38f, cy - outerRadius * 0.75f),
        strokeWidth = maxOf(2f, outerRadius * 0.12f)
    )
    val flag = Path()
    flag.moveTo(cx + outerRadius * 0.38f, cy - outerRadius * 0.75f)
    flag.quadraticTo(
        cx + outerRadius * 1.10f, cy - outerRadius * 0.55f,
        cx + outerRadius * 0.38f, cy - outerRadius * 0.25f
    )
    drawPath(flag, color = color.copy(alpha = 0.14f), style = Stroke(width = maxOf(2f, outerRadius * 0.12f)))
}

private fun DrawScope.drawLeaf(cx: Float, cy: Float, outerRadius: Float, color: Color) {
    val path = Path()
    path.moveTo(cx, cy - outerRadius)
    path.quadraticTo(cx + outerRadius, cy, cx, cy + outerRadius * 0.6f)
    path.quadraticTo(cx - outerRadius, cy, cx, cy - outerRadius)
    path.close()
    drawPath(path, color = color.copy(alpha = 0.14f))
}

private fun DrawScope.drawSpeechBubble(cx: Float, cy: Float, outerRadius: Float, color: Color) {
    val w = outerRadius * 2.0f
    val h = outerRadius * 1.3f
    drawRoundRect(
        color = color.copy(alpha = 0.13f),
        topLeft = Offset(cx - w / 2, cy - h / 2),
        size = Size(w, h),
        cornerRadius = CornerRadius(outerRadius * 0.4f)
    )
    val tailTop = cy + h / 2
    val tail = Path()
    tail.moveTo(cx - outerRadius * 0.20f, tailTop)
    tail.lineTo(cx - outerRadius * 0.55f, tailTop + outerRadius * 0.5f)
    tail.lineTo(cx + outerRadius * 0.15f, tailTop)
    tail.close()
    drawPath(tail, color = color.copy(alpha = 0.13f))
}

private fun DrawScope.drawWavyLine(cx: Float, cy: Float, outerRadius: Float, color: Color) {
    val path = Path()
    val halfW = outerRadius * 1.3f
    val amp = outerRadius * 0.4f
    val steps = 20
    for (i in 0..steps) {
        val t = i.toFloat() / steps
        val x = cx - halfW + t * halfW * 2
        val y = cy + amp * sin((t * 2 * PI).toDouble()).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    drawPath(path, color = color.copy(alpha = 0.18f), style = Stroke(width = maxOf(2f, outerRadius * 0.12f)))
}

private fun DrawScope.drawDiamond(cx: Float, cy: Float, outerRadius: Float, color: Color) {
    val path = Path()
    path.moveTo(cx,                      cy - outerRadius)
    path.lineTo(cx + outerRadius * 0.7f, cy)
    path.lineTo(cx,                      cy + outerRadius)
    path.lineTo(cx - outerRadius * 0.7f, cy)
    path.close()
    drawPath(path, color = color.copy(alpha = 0.14f))
}

private fun DrawScope.drawHeart(cx: Float, cy: Float, r: Float, color: Color) {
    val scale = r / 14.0f
    val yBase = cy - r * 0.20f
    val path = Path()
    for (i in 0..80) {
        val t = i.toFloat() / 80.0f * 2.0f * PI.toFloat()
        val s = sin(t.toDouble()).toFloat()
        val hx = cx + scale * 16.0f * s * s * s
        val hy = yBase - scale * (
            13.0f * cos(t.toDouble()).toFloat()
            - 5.0f * cos((2 * t).toDouble()).toFloat()
            - 2.0f * cos((3 * t).toDouble()).toFloat()
            - cos((4 * t).toDouble()).toFloat()
        )
        if (i == 0) path.moveTo(hx, hy) else path.lineTo(hx, hy)
    }
    path.close()
    drawPath(path, color = color.copy(alpha = 0.15f))
}

private fun DrawScope.drawTriangle(cx: Float, cy: Float, outerRadius: Float, color: Color) {
    val path = Path()
    path.moveTo(cx, cy - outerRadius)
    path.lineTo(cx + outerRadius * 0.87f, cy + outerRadius * 0.5f)
    path.lineTo(cx - outerRadius * 0.87f, cy + outerRadius * 0.5f)
    path.close()
    drawPath(path, color = color.copy(alpha = 0.14f))
}

private fun DrawScope.drawSquare(cx: Float, cy: Float, outerRadius: Float, color: Color) {
    val side = outerRadius * 1.5f
    drawRoundRect(
        color = color.copy(alpha = 0.13f),
        topLeft = Offset(cx - side / 2, cy - side / 2),
        size = Size(side, side),
        cornerRadius = CornerRadius(outerRadius * 0.15f)
    )
}

private fun DrawScope.drawWave(cx: Float, cy: Float, outerRadius: Float, color: Color) {
    val halfW = outerRadius * 2.0f
    val amp = outerRadius * 0.35f
    val steps = 24
    repeat(3) { layer ->
        val yOff = cy + layer * outerRadius * 0.5f - outerRadius * 0.5f
        val path = Path()
        for (i in 0..steps) {
            val t = i.toFloat() / steps
            val x = cx - halfW + t * halfW * 2
            val y = yOff + amp * sin((t * 2 * PI + layer * PI / 3.0).toDouble()).toFloat()
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        drawPath(
            path,
            color = color.copy(alpha = 0.12f - layer * 0.03f),
            style = Stroke(width = maxOf(2f, outerRadius * 0.10f))
        )
    }
}

// MARK: - Previews

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, device = "spec:width=1280dp,height=720dp,dpi=240")
@Composable private fun PreviewHearts()        { KidsGradientBackground(KidsGradient.rosePink,       KidsFloatingShape.hearts) }

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, device = "spec:width=1280dp,height=720dp,dpi=240")
@Composable private fun PreviewMixShapes()     { KidsGradientBackground(KidsGradient.creamMint,      KidsFloatingShape.mixShapes) }

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, device = "spec:width=1280dp,height=720dp,dpi=240")
@Composable private fun PreviewStars()         { KidsGradientBackground(KidsGradient.purpleBlue,     KidsFloatingShape.stars) }

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, device = "spec:width=1280dp,height=720dp,dpi=240")
@Composable private fun PreviewBubbles()       { KidsGradientBackground(KidsGradient.peachYellow,    KidsFloatingShape.bubbles) }

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, device = "spec:width=1280dp,height=720dp,dpi=240")
@Composable private fun PreviewDots()          { KidsGradientBackground(KidsGradient.grayBlue,       KidsFloatingShape.dots) }

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, device = "spec:width=1280dp,height=720dp,dpi=240")
@Composable private fun PreviewSparkles()      { KidsGradientBackground(KidsGradient.pinkPeach,      KidsFloatingShape.sparkles) }

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, device = "spec:width=1280dp,height=720dp,dpi=240")
@Composable private fun PreviewMusicNotes()    { KidsGradientBackground(KidsGradient.mintLime,       KidsFloatingShape.musicNotes) }

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, device = "spec:width=1280dp,height=720dp,dpi=240")
@Composable private fun PreviewDiamonds()      { KidsGradientBackground(KidsGradient.skyLavender,    KidsFloatingShape.diamonds) }

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, device = "spec:width=1280dp,height=720dp,dpi=240")
@Composable private fun PreviewLeaves()        { KidsGradientBackground(KidsGradient.aquaGreen,      KidsFloatingShape.leaves) }

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, device = "spec:width=1280dp,height=720dp,dpi=240")
@Composable private fun PreviewSpeechBubbles() { KidsGradientBackground(KidsGradient.blueIndigo,     KidsFloatingShape.speechBubbles) }

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, device = "spec:width=1280dp,height=720dp,dpi=240")
@Composable private fun PreviewCurveLines()    { KidsGradientBackground(KidsGradient.indigoPurple,   KidsFloatingShape.curveLines) }

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, device = "spec:width=1280dp,height=720dp,dpi=240")
@Composable private fun PreviewTealMusicNotes() { KidsGradientBackground(KidsGradient.tealCyan,      KidsFloatingShape.musicNotes) }

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, device = "spec:width=1280dp,height=720dp,dpi=240")
@Composable private fun PreviewPeriwinkle()    { KidsGradientBackground(KidsGradient.periwinkleBlue, KidsFloatingShape.stars) }

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, device = "spec:width=1280dp,height=720dp,dpi=240")
@Composable private fun PreviewLilac()         { KidsGradientBackground(KidsGradient.lilacPink,      KidsFloatingShape.sparkles) }

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, device = "spec:width=1280dp,height=720dp,dpi=240")
@Composable private fun PreviewPinkVanilla()   { KidsGradientBackground(KidsGradient.pinkVanilla,    KidsFloatingShape.stars) }

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, device = "spec:width=1280dp,height=720dp,dpi=240")
@Composable private fun PreviewSoftBlue()      { KidsGradientBackground(KidsGradient.softBlue,       KidsFloatingShape.bubbles) }

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, device = "spec:width=1280dp,height=720dp,dpi=240")
@Composable private fun PreviewSeaBlue()       { KidsGradientBackground(KidsGradient.seaBlue,        KidsFloatingShape.curveLines) }

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, device = "spec:width=1280dp,height=720dp,dpi=240")
@Composable private fun PreviewPeachCoral()    { KidsGradientBackground(KidsGradient.peachCoral,     KidsFloatingShape.diamonds) }

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, device = "spec:width=1280dp,height=720dp,dpi=240")
@Composable private fun PreviewOceanBlue()     { KidsGradientBackground(KidsGradient.oceanBlue,      KidsFloatingShape.waves) }
