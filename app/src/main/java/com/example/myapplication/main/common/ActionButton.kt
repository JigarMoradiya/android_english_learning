package com.example.myapplication.main.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.PrimaryBlue

enum class ButtonType {
    BLUE,
    PINK,
    ORANGE,
    PURPLE,
    GREEN,
}

data class ButtonColors(
    val base: Color,
    val gradient: Brush
)

fun getButtonColors(type: ButtonType): ButtonColors {
    return when (type) {

        ButtonType.BLUE -> ButtonColors(
            base = Color(0xFF005EA9),
            gradient = Brush.horizontalGradient(
                listOf(
                    Color(0xFF42A5F5),
                    PrimaryBlue
                )
            )
        )

        ButtonType.PINK -> ButtonColors(
            base = Color(0xFFD5084E),
            gradient = Brush.horizontalGradient(
                listOf(
                    Color(0xFFF5538A),
                    Color(0xFFE91E63)
                )
            )
        )

        ButtonType.ORANGE -> ButtonColors(
            base = Color(0xFFC77601),
            gradient = Brush.horizontalGradient(
                listOf(
                    Color(0xFFEEAA47),
                    Color(0xFFFB8C00)
                )
            )
        )
        ButtonType.PURPLE -> ButtonColors(
            base = Color(0xFF5532D2),
            gradient = Brush.horizontalGradient(
                listOf(
                    Color(0xFF9374EF),
                    Color(0xFF6446CC)
                )
            )
        )

        ButtonType.GREEN -> ButtonColors(
            base = Color(0xFF108156),
            gradient = Brush.horizontalGradient(
                listOf(
                    Color(0xFF4CD09C),
                    Color(0xFF129864)
                )
            )
        )
    }
}

@Composable
fun ActionButton(
    text: String,
    icon: ImageVector,
    type: ButtonType,
    onClick: () -> Unit,
    isIconStart: Boolean = true,
) {
    val colors = getButtonColors(type)
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.9f else 1f,
        label = ""
    )


    Box(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .drawBehind {
                drawRoundRect(
                    color = colors.base, // bottom color
                    size = size,
                    cornerRadius = CornerRadius(50f, 50f),
                    topLeft = Offset(0f, 2.dp.toPx()) // bottom line width
                )
            }
            .clip(RoundedCornerShape(50))
            .background(colors.gradient)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick()
            }
            .padding(horizontal = Dimens12, vertical = Dimens8)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            if (isIconStart) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = Color.White,
                )
                Spacer(Modifier.width(Dimens6))
            }

            Text(
                text = text,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            if (!isIconStart) {
                Spacer(Modifier.width(Dimens6))
                Icon(
                    icon,
                    contentDescription = null,
                    tint = Color.White,
                )
            }
        }
    }
}