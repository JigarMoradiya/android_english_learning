package com.example.myapplication.main.common.buttons

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.ShadowOffset
import com.example.myapplication.ui.theme.AppDimens.ShadowOffsetText
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.ui.theme.getButtonColors
import com.example.myapplication.utils.AudioPlayerManager

@Composable
fun KidsActionButton(
    text: String,
    icon: ImageVector? = null,
    type: ButtonType,
    onClick: () -> Unit,
    isIconStart: Boolean = true,
    isSmall: Boolean = false,
    modifier: Modifier = Modifier
) {
    val colors = getButtonColors(type)
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.9f else 1f,
        label = ""
    )

    Box(
        modifier = modifier
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
                AudioPlayerManager.playSoundMenuClick()
                onClick()
            }
            .padding(horizontal = if (isSmall) Dimens8 else Dimens12, vertical = if (isSmall) Dimens6 else Dimens8)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            if (isIconStart && icon != null) {
                Box {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.Black.copy(alpha = 0.35f),
                        modifier = Modifier.size(if (isSmall) Dimens20 else Dimens24)
                            .offset(ShadowOffset, ShadowOffset), // shadow layer
                    )

                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(if (isSmall) Dimens20 else Dimens24)
                    )
                }
                Spacer(Modifier.width(Dimens6))
            }

            Box {
                // Shadow layer
                Text(
                    text = text,
                    color = Color.Black.copy(alpha = 0.35f),
                    style = if (isSmall) MaterialTheme.typography.labelMedium else MaterialTheme.typography.bodyLarge,
                    fontWeight = if (isSmall) FontWeight.SemiBold else FontWeight.Bold,
                    modifier = Modifier.offset(ShadowOffsetText, ShadowOffsetText)
                )


                // Main text
                Text(
                    text = text,
                    color = Color.White,
                    style = if (isSmall) MaterialTheme.typography.labelMedium else MaterialTheme.typography.bodyLarge,
                    fontWeight = if (isSmall) FontWeight.SemiBold else FontWeight.Bold
                )
            }

            if (!isIconStart && icon != null) {
                Spacer(Modifier.width(Dimens6))

                Box {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.Black.copy(alpha = 0.35f),
                        modifier = Modifier
                            .size(if (isSmall) Dimens20 else Dimens24)
                            .offset(ShadowOffset, ShadowOffset), // shadow layer
                    )

                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(if (isSmall) Dimens20 else Dimens24)
                    )
                }
            }
        }
    }
}
