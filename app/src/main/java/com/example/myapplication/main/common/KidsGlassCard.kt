package com.example.myapplication.main.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Replicates iOS `kidsGlassCard` in Compose:
 *   - White gradient background (92% → 80% alpha, top-left to bottom-right)
 *   - Gradient stroke border (strokeColor 75% → 28% → 8%)
 *   - Inner top-edge highlight line (pure white, subtle)
 *   - Soft elevation shadow
 *
 * Usage — as a modifier:
 *   Modifier.kidsGlassCard(cornerRadius = Dimens12, strokeColor = rule.color)
 *
 * Usage — as a wrapper composable:
 *   KidsGlassCardBox(cornerRadius = Dimens12, strokeColor = rule.color) { ... }
 */
fun Modifier.kidsGlassCard(
    cornerRadius: Dp = 12.dp,
    strokeColor: Color = Color.White,
    elevation: Dp = 3.dp
): Modifier {
    val shape = RoundedCornerShape(cornerRadius)
    return this
        // 1. Soft shadow (tinted with strokeColor for warmth)
        .shadow(
            elevation = elevation,
            shape = shape,
            clip = false,
            ambientColor = strokeColor.copy(alpha = 0.10f),
            spotColor = strokeColor.copy(alpha = 0.14f)
        )
        // 2. Clip so background + inner highlight stay inside the rounded shape
        .clip(shape)
        // 3. Glass background: white gradient + inner highlight line
        .drawBehind {
            val w = size.width
            val h = size.height
            val r = cornerRadius.toPx()

            // Translucent white fill — lets background gradient show through like frosted glass
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.68f),
                        Color.White.copy(alpha = 0.48f)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(w, h)
                ),
                cornerRadius = CornerRadius(r, r),
                size = Size(w, h)
            )

            // Inner top-edge highlight (light reflection on frosted glass surface)
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.75f),
                        Color.White.copy(alpha = 0.00f)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(0f, h * 0.24f)
                ),
                cornerRadius = CornerRadius(r, r),
                size = Size(w, h * 0.24f)
            )
        }
        // 4. Gradient stroke border — accent color fades from top-left to bottom-right
        .border(
            width = 1.2.dp,
            brush = Brush.linearGradient(
                colorStops = arrayOf(
                    0.00f to strokeColor.copy(alpha = 0.75f),
                    0.55f to strokeColor.copy(alpha = 0.28f),
                    1.00f to strokeColor.copy(alpha = 0.08f)
                )
            ),
            shape = shape
        )
}

/**
 * Wrapper composable version — use when you need content padding built-in.
 */
@Composable
fun KidsGlassCardBox(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 12.dp,
    strokeColor: Color = Color.White,
    elevation: Dp = 3.dp,
    contentPadding: Dp = 14.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .kidsGlassCard(
                cornerRadius = cornerRadius,
                strokeColor = strokeColor,
                elevation = elevation
            )
            .padding(contentPadding),
        content = content
    )
}
