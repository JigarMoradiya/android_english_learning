package com.example.myapplication.main.age_group.from_5_to_7.vocabulary_building.category_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.data.model.VocabularyCategoryType
import com.example.myapplication.main.age_group.from_5_to_7.vocabulary_building.category_detail.components.WordCard
import com.example.myapplication.main.age_group.from_5_to_7.vocabulary_building.category_detail.view_model.VocabularyBuildingDetailViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16


@Composable
fun VocabularyBuildingDetailPage(
    navController: NavController,
    categoryType : String, title : String,
    viewModel: VocabularyBuildingDetailViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState
    LaunchedEffect(Unit) {
        viewModel.getDetailList(categoryType)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundUI(isGreenGrassShow = false)
        Column(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)) {
            BackButtonWithText(
                title = title,
                onBackClick = { navController.popBackStack() }
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(
                    start = DeviceInfo.screenPadding(),
                    end = DeviceInfo.screenPadding(), top = Dimens16, bottom = Dimens16
                ),
                verticalArrangement = Arrangement.spacedBy(Dimens12),
                horizontalArrangement = Arrangement.spacedBy(Dimens12)
            ) {

                items(uiState.words) { word ->
                    WordCard(
                        buttonType = uiState.buttonType,
                        word = word,
                        img = word,
                        bgColor = viewModel.backgroundForCategory(word, categoryType),
                        isColorCategory = categoryType == VocabularyCategoryType.COLORS.name,
                        onSpeakClick = {
                            viewModel.speak(word)
                        }
                    )
                }
            }
        }
    }
}

