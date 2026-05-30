package com.example.myapplication.main.parent

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.utils.extensions.scaled

@Composable
fun ReviewBannerCard(
    title: String,
    subtitle: String,
    onLoveIt: () -> Unit,
    onNotReally: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "stars")
    val starScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
        label = "starScale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF7C3AED), Color(0xFF4F46E5))
                ),
                shape = RoundedCornerShape(Dimens16)
            )
            .padding(Dimens12)
    ) {

        // ── Decorative stars (top-right) ──────────────────────────────
        Row(
            modifier = Modifier.align(Alignment.TopEnd),
            horizontalArrangement = Arrangement.spacedBy(Dimens4)
        ) {
            repeat(5) { i ->
                val phase = ((starScale - 1f) + i * 0.05f).coerceIn(0f, 0.35f)
                Text(
                    text = "⭐",
                    style = MaterialTheme.typography.labelSmall.scaled(),
                    modifier = Modifier.graphicsLayer {
                        scaleX = 1f + phase
                        scaleY = 1f + phase
                    }
                )
            }
        }

        // ── Main content ──────────────────────────────────────────────
        Column(verticalArrangement = Arrangement.spacedBy(Dimens4)) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.scaled(),
                fontWeight = FontWeight.Black,
                color = Color.White
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.scaled(),
                color = Color.White.copy(alpha = 0.88f)
            )

            Row(modifier = Modifier.padding(top = Dimens4), horizontalArrangement = Arrangement.spacedBy(Dimens8)) {

                // "Yes, Love it!" — white pill button
                Box(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(100))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onLoveIt
                        )
                        .padding(horizontal = Dimens12, vertical = Dimens6),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "😍 Yes, Love it!",
                        style = MaterialTheme.typography.bodyMedium.scaled(),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4F46E5)
                    )
                }

                // "Not really" — ghost pill button
                Box(
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.18f), RoundedCornerShape(100))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onNotReally
                        )
                        .padding(horizontal = Dimens12, vertical = Dimens6),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "😕 Not really",
                        style = MaterialTheme.typography.bodyMedium.scaled(),
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        }
    }
}
