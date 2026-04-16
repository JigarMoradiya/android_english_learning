package com.example.myapplication.main.age_group.from_5_to_7.articles_a_an.example

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.main.age_group.from_5_to_7.articles_a_an.example.view_model.ArticleMode
import com.example.myapplication.main.age_group.from_5_to_7.articles_a_an.example.view_model.ArticlesAAnExampleUiState
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.ButtonColors
import com.example.myapplication.utils.extensions.scaled


@Composable
fun KidsArticleToggle(
    uiState: ArticlesAAnExampleUiState,
    cardColors : ButtonColors,
    onModeChange: (ArticleMode) -> Unit
) {

    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens8),
        modifier = Modifier
            .wrapContentWidth()
            .padding(start = DeviceInfo.screenHorizontalPadding(), end = Dimens16)
    ) {

        ArticleMode.entries.forEach { mode ->

            val isSelected = uiState.mode == mode

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


            val bgBrush = if (isSelected) {
                cardColors.gradient
            } else {
                Brush.horizontalGradient(
                    listOf(Color.LightGray, Color.Gray)
                )
            }
            val base = if (isSelected) {
                cardColors.base
            } else {
                Color.DarkGray
            }

            Box(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                    .drawBehind {
                        // 🔥 bottom shadow (depth)
                        drawRoundRect(
                            color = base,
                            size = size,
                            cornerRadius = CornerRadius(50f, 50f),
                            topLeft = Offset(0f, 3.dp.toPx())
                        )
                    }
                    .clip(RoundedCornerShape(50))
                    .background(bgBrush)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        onModeChange(mode)
                    }
                    .padding(horizontal = Dimens16, vertical = Dimens8)
            ) {

                // 🔤 Text with shadow
                Box {
                    Text(
                        text = mode.label,
                        style = typography.bodyMedium.scaled(),
                        color = Color.Black.copy(alpha = 0.35f),
                        modifier = Modifier.offset(1.dp, 1.dp)
                    )

                    Text(
                        text = mode.label,
                        style = typography.bodyMedium.scaled(),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}