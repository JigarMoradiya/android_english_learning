package com.example.myapplication.main.age_group.from_5_to_7.vocabulary_building.category_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.data.model.VocabularyCategoryType
import com.example.myapplication.main.age_group.from_5_to_7.vocabulary_building.category_detail.components.WordCard
import com.example.myapplication.main.age_group.from_5_to_7.vocabulary_building.category_detail.view_model.VocabularyBuildingDetailViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI


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
        Column(modifier = Modifier) {
            BackButtonWithText(
                title = title,
                onBackClick = { navController.popBackStack() }
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
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

