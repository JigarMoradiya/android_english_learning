package com.example.myapplication.main.common

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.myapplication.ui.theme.AppDimens.Dimens40
import com.example.myapplication.utils.extensions.scaled

@Composable
fun CountdownBadge(
    count: Int,
    modifier: Modifier = Modifier,
    text: String = "Next word in "
) {
    val countdownColor = when (count) {
        3 -> Color(0xFF2E7D32)
        2 -> Color(0xFFE65100)
        else -> Color(0xFFB71C1C)
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.scaled(),
            fontWeight = FontWeight.SemiBold,
            color = countdownColor
        )

        AnimatedContent(
            targetState = count,
            transitionSpec = {
                (fadeIn() + scaleIn(initialScale = 0.8f)) togetherWith
                        (fadeOut() + scaleOut(targetScale = 0.8f))
            },
            label = "countdownAnimation"
        ) { currentCount ->

            Box(
                modifier = Modifier
                    .size(Dimens40)
                    .clip(CircleShape)
                    .background(
                        when (currentCount) {
                            3 -> Color(0xFF2E7D32)
                            2 -> Color(0xFFE65100)
                            else -> Color(0xFFB71C1C)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$currentCount",
                    style = MaterialTheme.typography.titleLarge.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}