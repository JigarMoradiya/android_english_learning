package com.example.myapplication.main.age_group

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.main.age_group.presentation.model.activities_age_3_5
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.utils.AudioPlayerManager

@Composable
fun AgeGroup3to5Page(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundUI()
        Column(
            modifier = Modifier
                .fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            BackButtonWithText(
                title = stringResource(R.string.level1_title),
                onBackClick = { navController.popBackStack() },
                modifier = Modifier
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
                    .fillMaxWidth()
                    .height(screenHeight * 0.6f)
            ) {

                items(activities_age_3_5) { activity ->

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                AudioPlayerManager.playSoundMenuClick()
                                navController.navigate(activity.destination)
                            }
                    ) {

                        Image(
                            painter = painterResource(id = activity.img),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}