package com.example.myapplication.main.common

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.main.common.animations.ConfettiRainEffect
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled
import kotlinx.coroutines.delay

@Composable
fun ActivityCompletePopup(
    stars: Int,             // 1, 2, or 3
    score: Int,
    total: Int,             // 0 = learning screen, hides score row
    scoreLabel: String,     // e.g. "perfect! 🎯"
    feedbackTextRes: Int,   // string resource for title
    feedbackSubTextRes: Int,
    onNext: () -> Unit,
    onClose: () -> Unit
) {
    val cardWidth = LocalConfiguration.current.screenWidthDp * 0.5f
    val isPreview = LocalInspectionMode.current

    // Card spring-in
    var cardVisible by remember { mutableStateOf(isPreview) }
    val cardScale by animateFloatAsState(
        targetValue = if (cardVisible) 1f else 0.7f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMediumLow),
        label = "card_scale"
    )
    val cardAlpha by animateFloatAsState(
        targetValue = if (cardVisible) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMediumLow),
        label = "card_alpha"
    )

    // Stars staggered spring pop-in — in preview start fully revealed
    val starRevealed = remember { mutableStateListOf(isPreview, isPreview, isPreview) }
    val star0Scale by animateFloatAsState(
        targetValue = if (starRevealed[0]) 1f else 0.1f,
        animationSpec = spring(dampingRatio = 0.45f, stiffness = Spring.StiffnessMedium),
        label = "star0"
    )
    val star1Scale by animateFloatAsState(
        targetValue = if (starRevealed[1]) 1f else 0.1f,
        animationSpec = spring(dampingRatio = 0.45f, stiffness = Spring.StiffnessMedium),
        label = "star1"
    )
    val star2Scale by animateFloatAsState(
        targetValue = if (starRevealed[2]) 1f else 0.1f,
        animationSpec = spring(dampingRatio = 0.45f, stiffness = Spring.StiffnessMedium),
        label = "star2"
    )
    val starScales = listOf(star0Scale, star1Scale, star2Scale)

    LaunchedEffect(Unit) {
        cardVisible = true
        delay(400)
        for (i in 0 until stars.coerceIn(1, 3)) {
            starRevealed[i] = true
            delay(180)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.55f)),
        contentAlignment = Alignment.Center
    ) {
        // Card
        Column(
            modifier = Modifier
                .width(cardWidth.dp)
                .graphicsLayer {
                    scaleX = cardScale
                    scaleY = cardScale
                    alpha = cardAlpha
                }
                .shadow(Dimens24, RoundedCornerShape(Dimens24), ambientColor = Color.Black.copy(alpha = 0.25f))
                .clip(RoundedCornerShape(Dimens24))
                .background(Color.White)
        ) {
            // ── Header: purple gradient + trophy + title ──────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF9374EF), Color(0xFF5532D2))
                        )
                    )
                    .padding(vertical = Dimens16),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimens6)
                ) {
                    Text("🏆", style = MaterialTheme.typography.displayLarge.scaled())
                    Text(
                        text = stringResource(feedbackTextRes),
                        style = MaterialTheme.typography.titleMedium.scaled(),
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = Dimens12)
                    )
                }
            }

            // ── Content ───────────────────────────────────────────────────
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens8),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens16)
            ) {
                // Stars row
                Row(horizontalArrangement = Arrangement.spacedBy(Dimens20)) {
                    for (i in 0..2) {
                        val isGold = i < stars
                        Box(contentAlignment = Alignment.Center) {
                            // Glow behind gold star
                            if (isGold) {
                                Text(
                                    text = "★",
                                    style = MaterialTheme.typography.displayLarge.scaled(),
                                    color = Color(0xFFFFD700).copy(alpha = 0.35f),
                                    modifier = Modifier.graphicsLayer {
                                        scaleX = starScales[i] * 1.4f
                                        scaleY = starScales[i] * 1.4f
                                    }
                                )
                            }
                            Text(
                                text = "★",
                                style = MaterialTheme.typography.displayLarge.scaled(),
                                color = if (isGold) Color(0xFFFFD700) else Color(0xFFD5D5D5),
                                modifier = Modifier.graphicsLayer {
                                    scaleX = starScales[i]
                                    scaleY = starScales[i]
                                    alpha = if (isGold) starScales[i] else 1f
                                }
                            )
                        }
                    }
                }

                // Score row
                if (total > 0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Dimens6)
                    ) {
                        Text(
                            text = "$score",
                            style = MaterialTheme.typography.displayLarge.scaled(),
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF5532D2)
                        )
                        Text(
                            text = "/ $total",
                            style = MaterialTheme.typography.headlineLarge.scaled(),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF9090B0)
                        )
                        Text(
                            text = scoreLabel,
                            style = MaterialTheme.typography.bodyMedium.scaled(),
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF7070A0)
                        )
                    }
                }

                // Subtitle
                val subtitle = stringResource(feedbackSubTextRes)
                if (subtitle.isNotEmpty()) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium.scaled(),
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF9090B0),
                        textAlign = TextAlign.Center
                    )
                }

                // Buttons
                Spacer(modifier = Modifier.height(Dimens2))
                Row(horizontalArrangement = Arrangement.spacedBy(Dimens16)) {
                    KidsActionButton(
                        text = "Close",
                        icon = Icons.Filled.Close,
                        type = ButtonType.RED,
                        isSmall = true,
                        onClick = onClose
                    )
                    KidsActionButton(
                        text = "Continue",
                        icon = Icons.AutoMirrored.Filled.ArrowForward,
                        type = ButtonType.GREEN,
                        isSmall = true,
                        isIconStart = false,
                        onClick = onNext
                    )
                }
            }
        }

        if (!isPreview) ConfettiRainEffect()
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview(name = "3 Stars", widthDp = 700, heightDp = 400, showBackground = true)
@Composable
private fun Preview3Stars() {
    ActivityCompletePopup(
        stars = 3,
        score = 10,
        total = 10,
        scoreLabel = "perfect! 🎯",
        feedbackTextRes = com.example.myapplication.R.string.feedbackPhrases_1,
        feedbackSubTextRes = com.example.myapplication.R.string.feedbackMissingLetter_1,
        onNext = {},
        onClose = {}
    )
}

@Preview(name = "2 Stars", widthDp = 700, heightDp = 400, showBackground = true)
@Composable
private fun Preview2Stars() {
    ActivityCompletePopup(
        stars = 2,
        score = 7,
        total = 10,
        scoreLabel = "good job!",
        feedbackTextRes = com.example.myapplication.R.string.feedbackPhrases_2,
        feedbackSubTextRes = com.example.myapplication.R.string.feedbackMissingLetter_1,
        onNext = {},
        onClose = {}
    )
}

@Preview(name = "1 Star", widthDp = 700, heightDp = 400, showBackground = true)
@Composable
private fun Preview1Star() {
    ActivityCompletePopup(
        stars = 1,
        score = 3,
        total = 10,
        scoreLabel = "keep trying!",
        feedbackTextRes = com.example.myapplication.R.string.feedbackPhrases_3,
        feedbackSubTextRes = com.example.myapplication.R.string.feedbackMissingLetter_1,
        onNext = {},
        onClose = {}
    )
}
