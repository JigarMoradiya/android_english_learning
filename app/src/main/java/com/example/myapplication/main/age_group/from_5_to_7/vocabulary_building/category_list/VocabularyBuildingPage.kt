package com.example.myapplication.main.age_group.from_5_to_7.vocabulary_building.category_list

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.main.age_group.from_5_to_7.vocabulary_building.category_list.view_model.VocabularyBuildingViewModel
import com.example.myapplication.main.age_group.presentation.model.activities_age_3_5
import com.example.myapplication.main.age_group.presentation.model.vocabularyCategoryDataList
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.utils.AudioPlayerManager


@Composable
fun VocabularyBuildingPage(
    navController: NavController,
    viewModel: VocabularyBuildingViewModel = hiltViewModel(),
    onDetailClick : (String,String) -> Unit
) {

    val uiState = viewModel.uiState
    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundUI()
        Column(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)) {
            BackButtonWithText(
                modifier = Modifier,
                title = stringResource(R.string.vocabulary_building),
                onBackClick = { navController.popBackStack() }
            )

            // Grid of activities
            val screenHeight = with(LocalDensity.current) {
                LocalWindowInfo.current.containerSize.height.toDp()
            }

            LazyRow(
                contentPadding = PaddingValues(
                    start = DeviceInfo.screenPadding(),
                    end = DeviceInfo.screenPadding(), top = Dimens16
                ),
                horizontalArrangement = Arrangement.spacedBy(Dimens16),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.6f)
            ) {

                items(vocabularyCategoryDataList) { category ->
                    val str = stringResource(id = category.titleRes)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                AudioPlayerManager.playSoundMenuClick()
                                onDetailClick(category.type, str)
                            }
                    ) {

                        Image(
                            painter = painterResource(id = category.imageName),
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
