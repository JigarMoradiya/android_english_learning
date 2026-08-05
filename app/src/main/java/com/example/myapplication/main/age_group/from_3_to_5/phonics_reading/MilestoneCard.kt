package com.example.myapplication.main.age_group.from_3_to_5.phonics_reading

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.myapplication.main.common.kidsShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.example.myapplication.ui.theme.AppDimens.Dimens3
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.utils.extensions.scaled

/**
 * The gold card that sits BETWEEN two levels on the phonics journey.
 *
 * It must not look like a level. A level node is a round numbered badge on one side of
 * the road with a star ring; this is a wide banner across the road with a word-badge
 * instead of a number, so a child scrolling past can never read it as "level 5 that I
 * somehow skipped". Keep identical to iOS MilestoneCardView.swift.
 */
@Composable
fun MilestoneCard(
    stone: PhonicsMilestoneItem,
    isDone: Boolean,
    isLocked: Boolean,
    onTap: () -> Unit,
) {
    // the star, gently breathing so the card is never a dead rectangle
    val transition = rememberInfiniteTransition(label = "milestoneShine")
    val shine by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "shine",
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens12),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens20)
            .kidsShadow(color = stone.color, shape = RoundedCornerShape(Dimens20), elevation = Dimens4)
            .background(
                Brush.linearGradient(listOf(stone.color, stone.color.copy(alpha = 0.82f))),
                RoundedCornerShape(Dimens20),
            )
            .border(Dimens3, Color.White.copy(alpha = 0.55f), RoundedCornerShape(Dimens20))
            .clip(RoundedCornerShape(Dimens20))
            .clickable { onTap() }
            .padding(horizontal = Dimens16, vertical = Dimens12),
    ) {
        Text(
            text = stone.emoji,
            style = MaterialTheme.typography.displaySmall.scaled(),
            modifier = Modifier.graphicsLayer {
                val s = 0.96f + 0.16f * shine
                scaleX = s
                scaleY = s
                rotationZ = -6f + 12f * shine
            },
        )

        Column(verticalArrangement = Arrangement.spacedBy(Dimens4), modifier = Modifier.weight(1f)) {

            // the WORD badge — this is what replaces a level number
            Text(
                text = stone.badge,
                style = MaterialTheme.typography.labelSmall.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.28f), RoundedCornerShape(50))
                    .padding(horizontal = Dimens10, vertical = Dimens3),
            )

            Text(
                text = stone.title,
                style = MaterialTheme.typography.bodyMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = stone.subtitle,
                style = MaterialTheme.typography.labelMedium.scaled(),
                color = Color.White.copy(alpha = 0.92f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Spacer(modifier = Modifier.size(Dimens4))

        Icon(
            imageVector = when {
                isLocked -> Icons.Default.Lock
                isDone -> Icons.Default.CheckCircle
                else -> Icons.Default.ArrowCircleRight
            },
            contentDescription = null,
            tint = Color.White.copy(alpha = if (isLocked) 0.85f else 1f),
            modifier = Modifier.size(Dimens24),
        )
    }
}
