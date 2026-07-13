package com.example.myapplication.main.common

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled

/**
 * Reusable kids-friendly "coming soon" gate for entry points that aren't ready yet.
 * Generic (title/message driven) so it can be reused beyond Phonics.
 */
@Composable
fun ComingSoonPopup(
    title: String = "Coming Soon!",
    message: String = "This adventure is getting its finishing touches — check back soon!",
    onClose: () -> Unit
) {
    val popupWidth = LocalConfiguration.current.screenWidthDp * 0.72f

    var startAnimation by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.7f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMediumLow),
        label = "comingSoonScale"
    )
    LaunchedEffect(Unit) { startAnimation = true }

    val infiniteTransition = rememberInfiniteTransition(label = "mascotBounce")
    val mascotOffsetY by infiniteTransition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "mascotOffset"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.55f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClose() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(popupWidth.dp)
                .graphicsLayer { scaleX = scale; scaleY = scale }
                .clip(RoundedCornerShape(Dimens24))
                .background(Color.White)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { /* absorb clicks so backdrop tap doesn't fire */ }
        ) {
            // Header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF9374EF), Color(0xFF5532D2))
                        )
                    )
                    .padding(vertical = Dimens16)
            ) {
                Image(
                    painter = painterResource(R.drawable._mascot_),
                    contentDescription = null,
                    modifier = Modifier
                        .height(64.dp)
                        .offset(y = mascotOffsetY.dp)
                )
                Spacer(modifier = Modifier.height(Dimens6))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.scaled(),
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = Dimens12)
                )
            }

            // Content
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(Dimens20)
            ) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium.scaled(),
                    color = Color(0xFF9090B0),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(Dimens16))

                KidsActionButton(
                    text = "Got it!",
                    type = ButtonType.GREEN,
                    onClick = onClose
                )
            }
        }
    }
}
