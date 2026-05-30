package com.example.myapplication.main.age_group.from_3_to_5.missing_letter

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.example.myapplication.main.age_group.from_3_to_5.missing_letter.components.MissingLetterScreen35
import com.example.myapplication.main.age_group.from_3_to_5.missing_letter.view_model.DifficultyLevel
import com.example.myapplication.main.age_group.from_3_to_5.missing_letter.view_model.MissingLetterViewModel35
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import com.example.myapplication.main.common.ActivityCompletePopup
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.CountdownBadge
import com.example.myapplication.main.common.InstructionBadge
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.buttons.KidsLabel


@Composable
fun MissingLetterPage35(
    navController: NavController, difficultyLevel : DifficultyLevel,
    viewModel: MissingLetterViewModel35 = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.setDifficulty(difficultyLevel)
    }

    val uiState = viewModel.uiState

    Box(modifier = Modifier.fillMaxSize()) {

        KidsGradientBackground(gradient = KidsGradient.skyLavender, shape = KidsFloatingShape.diamonds)
        Column(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)) {

            // HEADER
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButtonWithText(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.missing_letter),
                    onBackClick = { navController.popBackStack() }
                )

                if (uiState.showSuccess) {
                    KidsLabel("${uiState.round}/${uiState.totalRounds}")
                    CountdownBadge(
                        count = uiState.countdownValue,
                        modifier = Modifier.padding(end = Dimens16),
                        text = stringResource(R.string.next_word_in)
                    )
                } else {
                    InstructionBadge(
                        text = stringResource(R.string.drag_letters_to_complete_word),
                        modifier = Modifier.padding(end = Dimens16)
                    )
                    KidsLabel("${uiState.round}/${uiState.totalRounds}")
                }
            }

            MissingLetterScreen35(
                viewModel,
                modifier = Modifier.fillMaxSize()
            )
        }

        AnimatedVisibility(
            visible = uiState.showResult,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ActivityCompletePopup(
                stars = when {
                    uiState.correctCount.toFloat() / uiState.totalRounds >= 0.8f -> 3
                    uiState.correctCount.toFloat() / uiState.totalRounds >= 0.5f -> 2
                    else -> 1
                },
                score = uiState.correctCount,
                total = uiState.totalRounds,
                scoreLabel = if (uiState.correctCount == uiState.totalRounds) "perfect! 🎯" else "first try 🎯",
                feedbackTextRes = uiState.feedbackTextRes,
                feedbackSubTextRes = uiState.feedbackSubTextRes,
                onNext = { viewModel.restartGame() },
                onClose = { navController.popBackStack() }
            )
        }
    }
}