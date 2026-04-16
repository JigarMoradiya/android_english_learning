package com.example.myapplication.main.age_group.from_3_to_5.drag_and_drop_word

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
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
import com.example.myapplication.main.common.animations.ConfettiRainEffect
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.ButtonType


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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButtonWithText(
                    modifier = Modifier.weight(1f),
                    title = if (difficultyLevel == DifficultyLevel.EASY) stringResource(R.string.drag_drop_words) else stringResource(R.string.word_jigsaw) ,
                    onBackClick = { navController.popBackStack() }
                )

                if (uiState.showSuccess) {
                    KidsActionButton(
                        modifier = Modifier.padding(end = Dimens16),
                        text = stringResource(R.string.next),
                        icon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        type = ButtonType.GREEN,
                        isIconStart = false,
                        onClick = {
                            viewModel.loadNextWord()
                        }
                    )
                }
            }

            // CONTENT
            DragDropScreen(
                viewModel,
                modifier = Modifier.fillMaxSize()
            )
        }

        if (viewModel.uiState.showSuccess) {
            ConfettiRainEffect()
        }
    }
}