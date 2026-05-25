package com.example.myapplication.main.age_group.from_3_to_5.arrange_letter_sequence

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SwapHoriz
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.LetterMode
import com.example.myapplication.main.age_group.from_3_to_5.arrange_letter_sequence.components.BottomArrangeLetterOptions
import com.example.myapplication.main.age_group.from_3_to_5.arrange_letter_sequence.components.TopArrangeLetterSlots
import com.example.myapplication.main.age_group.from_3_to_5.arrange_letter_sequence.view_model.ArrangeLetterInSequenceViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.CountdownBadge
import com.example.myapplication.main.common.FeedbackText
import com.example.myapplication.main.common.InstructionBadge
import com.example.myapplication.main.age_group.from_6_to_8.common.ResultView
import com.example.myapplication.main.common.animations.ConfettiRainEffect
import com.example.myapplication.main.common.buttons.KidsLabel
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.utils.extensions.scaled

@Composable
fun ArrangeLetterInSequencePage(
    navController: NavController,
    mode: LetterMode,
    viewModel: ArrangeLetterInSequenceViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.setMode(mode)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        KidsGradientBackground(gradient = KidsGradient.peachYellow, shape = KidsFloatingShape.bubbles)

        Column(
            modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // HEADER
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButtonWithText(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.arrange_letter_in_sequence),
                    onBackClick = { navController.popBackStack() }
                )

                KidsLabel("${viewModel.uiState.round}/${viewModel.uiState.totalRounds}")

                if (viewModel.uiState.showNext && !viewModel.uiState.showResult) {
                    CountdownBadge(
                        count = viewModel.uiState.countdown,
                        modifier = Modifier.padding(end = Dimens16),
                        text = stringResource(R.string.next_letter_in)
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            if (viewModel.uiState.showResult) {
                ResultView(
                    modifier = Modifier.padding(horizontal = Dimens16),
                    score = viewModel.uiState.correctCount,
                    total = viewModel.uiState.totalRounds,
                    title = stringResource(R.string.your_result),
                    primaryButtonText = stringResource(R.string.practice_again),
                    secondaryButtonText = stringResource(R.string.go_back),
                    onPrimaryTap = { viewModel.restartGame() },
                    onSecondaryTap = { navController.popBackStack() }
                )
            } else {

                TopArrangeLetterSlots(viewModel)

                Spacer(modifier = Modifier.height(Dimens16.scaled()))

                InstructionBadge(
                    text = stringResource(R.string.arrange_letter_tap_instruction),
                    icon = Icons.Rounded.SwapHoriz
                )

                Spacer(modifier = Modifier.height(Dimens16.scaled()))

                BottomArrangeLetterOptions(viewModel)

                Spacer(Modifier.weight(1f))

                FeedbackText(
                    title = stringResource(viewModel.uiState.feedbackTextRes),
                    subtitle = stringResource(viewModel.uiState.feedbackSubTextRes),
                    isSuccess = viewModel.uiState.showSuccess,
                    isVisible = viewModel.uiState.showError || viewModel.uiState.showNext
                )
            }
        }

        if (viewModel.uiState.showSuccess && !viewModel.uiState.showResult) {
            ConfettiRainEffect()
        }
    }
}
