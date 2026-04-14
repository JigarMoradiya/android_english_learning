package com.example.myapplication.main.common.buttons
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.ShadowOffset
import com.example.myapplication.ui.theme.AppDimens.ShadowOffsetText
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.ui.theme.getButtonColors

@Composable
fun KidsOptionButton(
    text: String,
    type: ButtonType,
    fontSize : TextUnit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = getButtonColors(type)
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.9f else 1f,
        animationSpec = spring(
            dampingRatio = 0.5f,
            stiffness = Spring.StiffnessMedium
        ),
        label = ""
    )

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .drawBehind {
                // 🔥 bottom depth
                drawRoundRect(
                    color = colors.base,
                    size = size,
                    cornerRadius = CornerRadius(50f, 50f),
                    topLeft = Offset(0f, Dimens2.toPx())
                )
            }
            .clip(RoundedCornerShape(50))
            .background(
                brush = colors.gradient
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick()
            }
            .padding(horizontal = Dimens12),
        contentAlignment = Alignment.Center
    ) {

        // 🔤 Text with shadow
        Box {
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = fontSize
                ),
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black.copy(alpha = 0.25f),
                modifier = Modifier.offset(ShadowOffsetText, ShadowOffsetText)
            )

            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = fontSize
                ),
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )
        }
    }
}