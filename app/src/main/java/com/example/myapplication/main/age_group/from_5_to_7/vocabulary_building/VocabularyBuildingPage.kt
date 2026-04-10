package com.example.myapplication.main.age_group.from_5_to_7.vocabulary_building

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_5_to_7.vocabulary_building.view_model.VocabularyBuildingViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI


@Composable
fun VocabularyBuildingPage(
    navController: NavController,
    viewModel: VocabularyBuildingViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState

    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundUI(isGreenGrassShow = false)
        Column(modifier = Modifier) {
            BackButtonWithText(
                title = stringResource(R.string.vocabulary_building),
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
