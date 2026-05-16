package com.example.myapplication.main.age_group.from_6_to_8.grammar_basic

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.main.age_group.presentation.model.activities_age_6_8_grammar_basics
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens40
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.KidsIconSize
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.extensions.scaled

@Composable
fun GrammarBasicPage(
    navController: NavController
) {
    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundUI(isGreenGrassShow = false)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {

            // Header
            BackButtonWithText(
                title = stringResource(R.string.grammarBasics),
                onBackClick = { navController.popBackStack() }
            )

            // Middle section → takes remaining space and centers LazyRow
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                BoxWithConstraints(
                    modifier = Modifier.padding(bottom = Dimens16)
                ) {
                    val totalSpacing = Dimens16 * 3
                    val horizontalPadding =
                        DeviceInfo.screenHorizontalPadding() + Dimens16

                    val itemWidth = (maxWidth - horizontalPadding - totalSpacing) / 4

                    LazyRow(
                        contentPadding = PaddingValues(
                            start = DeviceInfo.screenHorizontalPadding(),
                            end = Dimens16,
                            top = Dimens16
                        ),
                        horizontalArrangement = Arrangement.spacedBy(Dimens16)
                    ) {
                        items(activities_age_6_8_grammar_basics) { activity ->
                            Column(
                                modifier = Modifier
                                    .width(itemWidth)
                                    .clip(RoundedCornerShape(Dimens16))
                                    .background(activity.txtColor.copy(alpha = 0.15f))
                                    .clickable {
                                        AudioPlayerManager.playSoundMenuClick()
                                        navController.navigate(activity.destination)
                                    }
                                    .padding(vertical = Dimens16),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = painterResource(activity.img),
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.size(itemWidth * 0.8f)
                                )

                                Spacer(modifier = Modifier.height(Dimens8))

                                Text(
                                    text = stringResource(activity.titleRes),
                                    color = Color.Black,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth(),
                                    style = MaterialTheme.typography
                                        .headlineMedium
                                        .scaled()
                                        .copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
            }

            // Always at bottom
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                GrammarChallengeCard(
                    navController = navController
                )
            }

            Spacer(modifier = Modifier.height(Dimens16))
        }
    }
}

@Composable
fun GrammarChallengeCard(
    navController: NavController
) {
    Button(
        onClick = {
            AudioPlayerManager.playSoundMenuClick()
            navController.navigate(RouteNavigation.MixedGrammarChallenge.route)
        },
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .shadow(
                elevation = Dimens8,
                shape = RoundedCornerShape(Dimens20)
            )
    ) {
        Row(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF5C6BC0),
                            Color(0xFF8E44AD)
                        )
                    ),
                    shape = RoundedCornerShape(Dimens20)
                )
                .padding(
                    horizontal = Dimens20,
                    vertical = Dimens10
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens16)
        ) {

            // Left icon
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(Dimens40)
            )

            Column(
                modifier = Modifier.wrapContentWidth(),
                verticalArrangement = Arrangement.spacedBy(Dimens2)
            ) {
                Text(
                    text = stringResource(R.string.grammar_challenge),
                    style = MaterialTheme.typography.titleMedium.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1
                )

                Text(
                    text = stringResource(R.string.beginner_medium_advanced),
                    style = MaterialTheme.typography.bodySmall.scaled(),
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.85f),
                    maxLines = 1
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}
