package com.example.myapplication.main.age_group

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawing
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
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.main.age_group.presentation.model.activities_age_5_7
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.isTablet
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.extensions.scaled

@Composable
fun AgeGroup5to7Page(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundUI()
        Column(
            modifier = Modifier
                .fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            BackButtonWithText(
                title = stringResource(R.string.level2_title),
                onBackClick = { navController.popBackStack() },
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.safeDrawing)
            )

            // Grid of activities
            val screenHeight = with(LocalDensity.current) {
                LocalWindowInfo.current.containerSize.height.toDp()
            }

            LazyRow(
                contentPadding = PaddingValues(
                    start = DeviceInfo.screenHorizontalPadding(),
                    end = DeviceInfo.screenHorizontalPadding(), top = Dimens16
                ),
                horizontalArrangement = Arrangement.spacedBy(Dimens16),
                modifier = Modifier
            ) {

                items(activities_age_5_7) { activity ->

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .clickable {
                                    AudioPlayerManager.playSoundMenuClick()
                                    navController.navigate(activity.destination)
                                }
                        ) {

                            Image(
                                painter = painterResource(id = activity.img),
                                contentDescription = null,
                                contentScale = ContentScale.FillHeight,
                                modifier = Modifier
                                    .height(screenHeight * 0.5f)
                            )
                        }

                        Spacer(Modifier.height(Dimens8))

                        Text(
                            text = stringResource(activity.titleRes),
                            color = activity.txtColor,
                            style = MaterialTheme.typography.titleMedium.scaled().copy(
                                fontWeight = FontWeight.Black,
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
    }
}