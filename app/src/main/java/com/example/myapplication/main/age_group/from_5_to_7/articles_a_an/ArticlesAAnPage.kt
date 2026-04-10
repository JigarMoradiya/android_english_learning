package com.example.myapplication.main.age_group.from_5_to_7.articles_a_an

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_5_to_7.articles_a_an.view_model.ArticlesAAnViewModel
import com.example.myapplication.main.age_group.from_5_to_7.word_match_picture.view_model.WordMatchImageViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI


@Composable
fun ArticlesAAnPage(
    navController: NavController,
    viewModel: ArticlesAAnViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState

    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundUI(isGreenGrassShow = false)
        Column(modifier = Modifier) {
            BackButtonWithText(
                title = stringResource(R.string.articles_a_an),
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
