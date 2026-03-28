package com.example.myapplication.utils.extensions

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.ui.theme.PrimaryGreen
import com.example.myapplication.utils.AppConstants
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object OtherEx {
    var isActionEnabled by mutableStateOf(true)
        private set
    fun ViewModel.safeAction(action: () -> Unit) {
        if (!isActionEnabled) return

        isActionEnabled = false
        action()

        viewModelScope.launch {
            delay(AppConstants.clickDisableTime)
            isActionEnabled = true
        }
    }
}

fun Modifier.animatedNeonBorder(
    cornerRadius: Dp,
    strokeWidth: Dp
): Modifier = composed {

    val infiniteTransition = rememberInfiniteTransition(label = "")

    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 500f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    drawBehind {
        val stroke = strokeWidth.toPx()
        val radius = cornerRadius.toPx()

        val brush = Brush.verticalGradient(
            colors = listOf(
                Color.Transparent,
                Color(0xFF62F086),
                Color(0xFFF09B62),
                Color(0xFF9D62F0),
                Color(0xFF42A5F5),
                Color(0xFFF06292)
            ),
            startY = progress,
            endY = progress + size.height
        )

        // ✨ glow
        drawRoundRect(
            brush = brush,
            size = size,
            cornerRadius = CornerRadius(radius, radius),
            style = Stroke(width = stroke * 2),
            alpha = 0.25f
        )

        // 🎯 main border
        drawRoundRect(
            brush = brush,
            size = size,
            cornerRadius = CornerRadius(radius, radius),
            style = Stroke(width = stroke)
        )
    }
}