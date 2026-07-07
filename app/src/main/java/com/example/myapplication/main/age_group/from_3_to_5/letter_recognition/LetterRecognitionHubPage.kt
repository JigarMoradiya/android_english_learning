package com.example.myapplication.main.age_group.from_3_to_5.letter_recognition

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.SwapHoriz
import androidx.compose.material.icons.rounded.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_3_to_5.letter_recognition.speed_game.SpeedGameInstructionsPopup
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.extensions.scaled

// Entry point for Letter Recognition — a small fork instead of going straight
// into the grid, so the Tricky Twins mirror-letters activity has a home
// without needing its own separate main-menu tile.
@Composable
fun LetterRecognitionHubPage(navController: NavController) {
    // Shown here, before navigating in, rather than as an overlay on top of
    // an already-loaded Speed Round screen — same shape as Tricky Twins'
    // Picker -> Intro -> Practice flow, where "Start" is what actually
    // pushes you into the game.
    var showSpeedRoundInstructions by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.skyLavender, shape = KidsFloatingShape.diamonds)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            BackButtonWithText(
                title = stringResource(R.string.letter_recognition),
                onBackClick = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens24)
            ) {
                HubCard(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.explore_letters),
                    description = stringResource(R.string.explore_letters_desc),
                    icon = Icons.Rounded.TouchApp,
                    color = Color(0xFF3674B5)
                ) {
                    AudioPlayerManager.playSoundMenuClick()
                    navController.navigate(RouteNavigation.LetterRecognitionExplore.route)
                }

                Spacer(modifier = Modifier.width(Dimens16))

                HubCard(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.menu_tricky_twins),
                    description = stringResource(R.string.menu_tricky_twins_desc),
                    icon = Icons.Rounded.SwapHoriz,
                    color = Color(0xFF8E24AA)
                ) {
                    AudioPlayerManager.playSoundMenuClick()
                    navController.navigate(RouteNavigation.MirrorLettersPicker.route)
                }

                Spacer(modifier = Modifier.width(Dimens16))

                HubCard(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.menu_speed_round),
                    description = stringResource(R.string.menu_speed_round_desc),
                    icon = Icons.Rounded.Bolt,
                    color = Color(0xFFE67639)
                ) {
                    AudioPlayerManager.playSoundMenuClick()
                    showSpeedRoundInstructions = true
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        if (showSpeedRoundInstructions) {
            SpeedGameInstructionsPopup {
                showSpeedRoundInstructions = false
                navController.navigate(RouteNavigation.LetterSpeedGame.route)
            }
        }
    }
}

@Composable
private fun HubCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (pressed) 0.95f else 1f, label = "hubCardScale")

    Column(
        modifier = modifier
            .scale(scale)
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(20.dp), ambientColor = color.copy(alpha = 0.35f))
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
            .padding(vertical = Dimens24, horizontal = Dimens8),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Big friendly colored badge behind the icon, rather than a bare icon
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(Dimens16))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.scaled(),
            fontWeight = FontWeight.Bold,
            color = Color.Black.copy(alpha = 0.85f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall.scaled(),
            fontWeight = FontWeight.Medium,
            color = Color.Black.copy(alpha = 0.5f),
            textAlign = TextAlign.Center
        )
    }
}
