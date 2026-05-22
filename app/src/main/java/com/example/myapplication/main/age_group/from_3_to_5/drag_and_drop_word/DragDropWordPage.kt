package com.example.myapplication.main.age_group.from_3_to_5.drag_and_drop_word

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.main.age_group.from_3_to_5.drag_and_drop_word.components.DragDropScreen
import com.example.myapplication.main.age_group.from_3_to_5.drag_and_drop_word.view_model.DragDropWordViewModel
import com.example.myapplication.main.age_group.from_3_to_5.missing_letter.view_model.DifficultyLevel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.CountdownBadge
import com.example.myapplication.main.common.FeedbackText
import com.example.myapplication.main.common.InstructionBadge
import com.example.myapplication.main.common.animations.ConfettiRainEffect
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground


@Composable
fun DragDropWordPage(
    navController: NavController, difficultyLevel: DifficultyLevel,
    viewModel: DragDropWordViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.setDifficulty(difficultyLevel)
    }

    val uiState = viewModel.uiState

    Box(modifier = Modifier.fillMaxSize()) {

        KidsGradientBackground(gradient = KidsGradient.mintLime, shape = KidsFloatingShape.musicNotes)

        Box(modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            // HEADER
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButtonWithText(
                    modifier = Modifier.weight(1f),
                    title = if (difficultyLevel == DifficultyLevel.EASY) stringResource(R.string.drag_drop_words) else stringResource(R.string.word_jigsaw),
                    onBackClick = { navController.popBackStack() }
                )

                if (uiState.showSuccess) {
                    CountdownBadge(
                        count = uiState.countdownValue,
                        modifier = Modifier.padding(end = Dimens16,top = DeviceInfo.screenTopPadding()),
                        text = stringResource(R.string.next_word_in)
                    )
                } else {
                    InstructionBadge(
                        text = stringResource(R.string.drag_to_build_word),
                        modifier = Modifier.padding(end = Dimens16).padding(top = DeviceInfo.screenTopPadding())
                    )
                }
            }

            // CONTENT
            DragDropScreen(
                viewModel,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {

                FeedbackText(
                    title = stringResource(viewModel.uiState.feedbackTextRes),
                    subtitle = stringResource(viewModel.uiState.feedbackSubTextRes),
                    isSuccess = viewModel.uiState.showSuccess,
                    isVisible = viewModel.uiState.showError || viewModel.uiState.showSuccess
                )
            }
        }
    }
}
