package com.example.myapplication.main.age_group.from_3_to_5.letter_recognition.mirror_letters

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.generation.letter.LetterRepository
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.getImageResFromWord
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled

// One calm animation, nothing else moving on screen: the first letter of the
// pair slowly flips into the second, on whichever axis actually matches how
// they're related (see MirrorLetterPair.isUpsideDownFlip) — then a button
// leads into tracing both letters. Each side of the flip also shows the
// letter's word + picture (b -> Bat, d -> Drum) so kids anchor the letter to
// something concrete, not just an abstract shape.
@Composable
fun MirrorLettersIntroPage(navController: NavController, pair: MirrorLetterPair) {

    val infiniteTransition = rememberInfiniteTransition(label = "flip")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 180f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, delayMillis = 600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "angle"
    )

    val isPastHalfway = angle >= 90f
    // Standard "card flip" trick: past the 90° edge-on point we show the new
    // letter but counter-rotate it so it always reads correctly, never mirrored.
    val displayLetter = if (isPastHalfway) pair.second else pair.first
    val displayAngle = if (isPastHalfway) angle - 180f else angle

    val displayWord = remember(displayLetter) {
        LetterRepository.all.firstOrNull { it.letter.equals(displayLetter.toString(), ignoreCase = true) }?.mainWord
    }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.purpleBlue, shape = KidsFloatingShape.stars)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            BackButtonWithText(
                title = "${stringResource(R.string.menu_tricky_twins)} - ${pair.title}",
                expandWidth = false,
                onBackClick = { navController.popBackStack() }
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.tricky_twins_intro_title),
                    style = MaterialTheme.typography.headlineLarge.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(Dimens16))

                // Flip letter and its word+picture side by side, not stacked —
                // keeps the total height low enough that the button below
                // always stays on screen.
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens24)
                ) {
                    Text(
                        text = displayLetter.toString(),
                        fontSize = 88.sp,
                        fontWeight = FontWeight.Black,
                        color = pair.accentColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .width(120.dp)
                            .graphicsLayer {
                                if (pair.isUpsideDownFlip) {
                                    rotationX = displayAngle
                                } else {
                                    rotationY = displayAngle
                                }
                                cameraDistance = 12 * density
                            }
                    )

                    // Word + picture for whichever letter is currently showing.
                    // Fixed width regardless of which word is showing — "Bat"
                    // vs "Drum" have different text widths, and without a
                    // fixed width here the Row reflows and the flip letter's
                    // position shifts every time the word changes.
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(110.dp)
                    ) {
                        displayWord?.let { word ->
                            getImageResFromWord(word)?.let { imageRes ->
                                Image(
                                    painter = painterResource(id = imageRes),
                                    contentDescription = word,
                                    modifier = Modifier.size(64.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                            Text(
                                text = word,
                                style = MaterialTheme.typography.headlineSmall.scaled(),
                                fontWeight = FontWeight.Medium,
                                color = Color.Black.copy(alpha = 0.7f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(Dimens16))

                KidsActionButton(
                    text = stringResource(R.string.tricky_twins_start_tracing),
                    icon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    type = ButtonType.BLUE,
                    isIconStart = false,
                    onClick = {
                        // KidsActionButton already plays the click sound itself
                        navController.navigate(RouteNavigation.MirrorLettersPractice.createRoute(pair.name))
                    }
                )
            }
        }
    }
}
