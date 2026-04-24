package com.example.myapplication.main.age_group.from_6_to_8.choose_the_right_sentence

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.main.age_group.presentation.model.activities_age_6_8_right_sentence
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens40
import com.example.myapplication.ui.theme.AppDimens.Dimens50
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.extensions.scaled


@Composable
fun ChooseTheRightSentencePage(
    navController: NavController
) {

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundUI()
        Column(
            modifier = Modifier
                .fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            BackButtonWithText(
                title = stringResource(R.string.chooseTheRightSentence),
                onBackClick = { navController.popBackStack() },
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.safeDrawing)
            )
            Spacer(Modifier.weight(1f))
            BoxWithConstraints(modifier = Modifier.padding(bottom = Dimens50)) {

                val totalSpacing = Dimens16 * 2 // 2 gaps between 3 items
                val horizontalPadding = DeviceInfo.screenHorizontalPadding() + Dimens16
                val itemWidth = (maxWidth - horizontalPadding - totalSpacing) / 3

                LazyRow(
                    contentPadding = PaddingValues(
                        start = DeviceInfo.screenHorizontalPadding(),
                        end = Dimens16,
                        top = Dimens16
                    ),
                    horizontalArrangement = Arrangement.spacedBy(Dimens16)
                ) {

                    items(activities_age_6_8_right_sentence) { activity ->

                        Column(
                            modifier = Modifier.width(itemWidth),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        AudioPlayerManager.playSoundMenuClick()
                                        navController.navigate(activity.destination)
                                    }
                            ) {
                                Image(
                                    painter = painterResource(id = activity.img),
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            Spacer(Modifier.height(Dimens8))

                            Text(
                                text = stringResource(activity.titleRes),
                                color = activity.txtColor,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                                style = MaterialTheme.typography.bodyMedium.scaled().copy(
                                    fontWeight = FontWeight.SemiBold,
                                    shadow = Shadow(
                                        color = Color.Black.copy(alpha = 0.6f),
                                        offset = Offset(1f, 1f),
                                        blurRadius = 0f
                                    )
                                )
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.weight(1f))
        }
    }
}
