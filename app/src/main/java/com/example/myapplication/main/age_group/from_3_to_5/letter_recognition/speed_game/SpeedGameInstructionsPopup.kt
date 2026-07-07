package com.example.myapplication.main.age_group.from_3_to_5.letter_recognition.speed_game

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled

// Shown once before the first round starts — kids need to know how the game
// works before tiles start appearing. Same two-part card shape as
// ActivityCompletePopup (gradient header band + white content area) so it
// reads as part of the same visual family, not a one-off dialog.
@Composable
fun SpeedGameInstructionsPopup(onStart: () -> Unit) {
    val cardWidth = LocalConfiguration.current.screenWidthDp * 0.52f

    var cardVisible by remember { mutableStateOf(false) }
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

    LaunchedEffect(Unit) { cardVisible = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.55f)),
        contentAlignment = Alignment.Center
    ) {
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
            // ── Header: orange gradient + clock + title ────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFFFFA35C), Color(0xFFE67639))
                        )
                    )
                    .padding(vertical = Dimens16),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimens12)
                ) {
                    Text(text = "⏱️", style = MaterialTheme.typography.displayLarge.scaled())
                    Text(
                        text = stringResource(R.string.speed_round_instructions_title),
                        style = MaterialTheme.typography.headlineSmall.scaled(),
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens20)
            ) {
                Text(
                    text = stringResource(R.string.speed_round_instructions_desc),
                    style = MaterialTheme.typography.bodyMedium.scaled(),
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF7070A0),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(Dimens16))

                KidsActionButton(
                    text = stringResource(R.string.start_game),
                    icon = Icons.AutoMirrored.Filled.ArrowForward,
                    type = ButtonType.GREEN,
                    isIconStart = false,
                    onClick = onStart
                )
            }
        }
    }
}
