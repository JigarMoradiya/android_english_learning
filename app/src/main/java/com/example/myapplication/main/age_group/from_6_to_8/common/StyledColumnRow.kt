package com.example.myapplication.main.age_group.from_6_to_8.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.AppDimens.Dimens1
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens8

@Composable
fun StyledRow(
    unlocked: Boolean,
    modifier: Modifier,
    padding : Dp = Dimens16,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()

            // ✨ Scale effect
            .graphicsLayer {
                scaleX = if (unlocked) 1f else 0.95f
                scaleY = if (unlocked) 1f else 0.95f
            }
            // 🎨 Background
            .background(
                brush = if (unlocked) {
                    Brush.linearGradient(
                        colors = listOf(
                            Color.Yellow.copy(alpha = 0.2f),
                            Color(0xFFFFA500).copy(alpha = 0.2f)
                        )
                    )
                } else {
                    Brush.linearGradient( // 👈 use brush instead of SolidColor
                        colors = listOf(
                            Color.Gray.copy(alpha = 0.15f),
                            Color.Gray.copy(alpha = 0.15f)
                        )
                    )
                },
                shape = RoundedCornerShape(Dimens8)
            )

            // 🧱 Border
            .border(
                width = if (unlocked) Dimens1 else 0.dp,
                color = if (unlocked) Color(0xFFFFA500).copy(alpha = 0.5f) else Color.Transparent,
                shape = RoundedCornerShape(Dimens8)
            )

            // 🔒 Locked opacity
            .alpha(if (unlocked) 1f else 0.6f)
            .padding(padding) // inner padding like SwiftUI
    ) {
        content()
    }
}

@Composable
fun StyledColumn(
    unlocked: Boolean,
    modifier: Modifier,
    padding : Dp = Dimens16,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()

            // ✨ Scale effect
            .graphicsLayer {
                scaleX = if (unlocked) 1f else 0.95f
                scaleY = if (unlocked) 1f else 0.95f
            }
            // 🎨 Background
            .background(
                brush = if (unlocked) {
                    Brush.linearGradient(
                        colors = listOf(
                            Color.Yellow.copy(alpha = 0.2f),
                            Color(0xFFFFA500).copy(alpha = 0.2f)
                        )
                    )
                } else {
                    Brush.linearGradient( // 👈 use brush instead of SolidColor
                        colors = listOf(
                            Color.Gray.copy(alpha = 0.15f),
                            Color.Gray.copy(alpha = 0.15f)
                        )
                    )
                },
                shape = RoundedCornerShape(Dimens12)
            )

            // 🧱 Border
            .border(
                width = if (unlocked) Dimens1 else 0.dp,
                color = if (unlocked) Color(0xFFFFA500).copy(alpha = 0.5f) else Color.Transparent,
                shape = RoundedCornerShape(Dimens12)
            )

            // 🔒 Locked opacity
            .alpha(if (unlocked) 1f else 0.6f)
            .padding(padding) // inner padding like SwiftUI
    ) {
        content()
    }
}