package com.example.myapplication.main.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Shared countdown bar for timed game modes.
 * Drains from full to empty and shifts green → orange → red as time runs out.
 *
 * @param progress remaining fraction of time: 1.0 (full) … 0.0 (time up)
 */
@Composable
fun GameTimerBar(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 14.dp
) {
    val animated by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 100),
        label = "timerProgress"
    )
    val barColor = when {
        animated > 0.5f -> Color(0xFF2E7D32)
        animated > 0.25f -> Color(0xFFFF9800)
        else -> Color(0xFFD32F2F)
    }
    Box(
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(50))
            .background(Color.Gray.copy(alpha = 0.25f))
    ) {
        if (animated > 0f) {
            Box(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animated)
                    .clip(RoundedCornerShape(50))
                    .background(barColor)
            )
        }
    }
}
