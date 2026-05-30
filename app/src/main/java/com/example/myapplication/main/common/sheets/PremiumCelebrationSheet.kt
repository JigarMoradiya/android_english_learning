package com.example.myapplication.main.common.sheets

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.example.myapplication.R
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.utils.extensions.scaled

@PreviewScreenSizes
@Preview(showBackground = true, backgroundColor = 0x88000000)
@Composable
private fun PremiumCelebrationSheetPreview() {
    MyApplicationTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.White, RoundedCornerShape(topStart = Dimens24, topEnd = Dimens24))
        ) {
            PremiumCelebrationSheet(onRateNow = {}, onLater = {})
        }
    }
}

@Composable
fun PremiumCelebrationSheet(
    onRateNow: () -> Unit,
    onLater: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "confetti")
    val emojiScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(tween(700), RepeatMode.Reverse),
        label = "emojiScale"
    )

    val emojis = listOf("🎉", "⭐", "🎊", "⭐", "🎉")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens24),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens16)
    ) {

        // ── Animated confetti row ──────────────────────────────────────
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens8)) {
            emojis.forEachIndexed { i, emoji ->
                val phase = ((emojiScale - 1f) + i * 0.05f).coerceIn(0f, 0.3f)
                Text(
                    text = emoji,
                    style = MaterialTheme.typography.headlineMedium.scaled(),
                    modifier = Modifier.graphicsLayer {
                        scaleX = 1f + phase
                        scaleY = 1f + phase
                    }
                )
            }
        }

        // ── Text ───────────────────────────────────────────────────────
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens8)
        ) {
            Text(
                text = stringResource(R.string.welcome_to_premium),
                style = MaterialTheme.typography.headlineMedium.scaled(),
                fontWeight = FontWeight.Black,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.you_re_amazing_mind_leaving_us_review),
                style = MaterialTheme.typography.bodyMedium.scaled(),
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }

        // ── Buttons ────────────────────────────────────────────────────
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            KidsActionButton(
                text = stringResource(R.string.rate_now),
                icon = Icons.Filled.Star,
                type = ButtonType.ORANGE,
                onClick = onRateNow
            )
            KidsActionButton(
                text = stringResource(R.string.maybe_later),
                type = ButtonType.NEGATIVE,
                onClick = onLater
            )
        }
    }
}
