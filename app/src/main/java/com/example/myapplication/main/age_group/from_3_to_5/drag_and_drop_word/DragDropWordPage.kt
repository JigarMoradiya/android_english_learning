package com.example.myapplication.main.age_group.from_3_to_5.drag_and_drop_word

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_3_to_5.drag_and_drop_word.components.DragDropScreen
import com.example.myapplication.main.age_group.from_3_to_5.drag_and_drop_word.view_model.DragDropWordViewModel
import com.example.myapplication.main.age_group.from_3_to_5.missing_letter.view_model.DifficultyLevel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.CustomPopupView


@Composable
fun DragDropWordPage(
    navController: NavController,difficultyLevel : DifficultyLevel,
    viewModel: DragDropWordViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.setDifficulty(difficultyLevel)
    }

    val uiState = viewModel.uiState

    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundUI(isGreenGrassShow = false)

        Box(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)){
            // HEADER
            BackButtonWithText(
                title = stringResource(R.string.drag_drop_words),
                onBackClick = { navController.popBackStack() }
            )

            // CONTENT
            DragDropScreen(
                viewModel,
                modifier = Modifier.fillMaxSize()
            )

            AnimatedVisibility(
                visible = uiState.showPopup,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                CustomPopupView(
                    title = stringResource(uiState.feedbackTextRes),
                    description = stringResource(uiState.feedbackSubTextRes),
                    positiveButtonText = stringResource(R.string.continue_to_play),
                    negativeButtonText = stringResource(R.string.no_i_want_to_close),
                    icon = R.drawable.ic_complete,
                    widthMultiplier = 0.5f,
                    onPositiveTapped = { viewModel.loadNextWord() },
                    onNegativeTapped = {
                        viewModel.closePopup()
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}