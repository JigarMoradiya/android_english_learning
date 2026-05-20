package com.example.myapplication.main.age_group.from_3_to_5.fill_blank

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
import androidx.compose.material.icons.rounded.TouchApp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.common.AppToolbarDropDownOnRight
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.LetterMode
import com.example.myapplication.main.age_group.from_3_to_5.fill_blank.components.BottomLetterOptions
import com.example.myapplication.main.age_group.from_3_to_5.fill_blank.components.TopLetterSlots
import com.example.myapplication.main.age_group.from_3_to_5.fill_blank.view_model.FillBlankLettersViewModel
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.CountdownBadge
import com.example.myapplication.main.common.FeedbackText
import com.example.myapplication.main.common.InstructionBadge
import com.example.myapplication.main.common.animations.ConfettiRainEffect
import com.example.myapplication.main.common.buttons.KidsLabel
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.utils.extensions.scaled


@Composable
fun FillBlankLettersPage(
    navController: NavController,
    viewModel: FillBlankLettersViewModel = hiltViewModel()
) {
    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundUI(isGreenGrassShow = false)

        Column(
            modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // HEADER
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                AppToolbarDropDownOnRight(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.fill_the_blank),
                    currentSelected = viewModel.uiState.mode.title,
                    modes = LetterMode.entries.map { it.title },
                    onItemChange = {
                        val mode = LetterMode.entries.first { m -> m.title == it }
                        viewModel.changeMode(mode)
                    },
                    onBackClick = { navController.popBackStack() }
                )

                KidsLabel("🎯  Round ${viewModel.uiState.round}")

                if (viewModel.uiState.showNext) {
                    CountdownBadge(
                        count = viewModel.uiState.countdown,
                        modifier = Modifier.padding(end = Dimens16),
                        text = stringResource(R.string.next_letter_in)
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            TopLetterSlots(viewModel)

            Spacer(modifier = Modifier.height(Dimens16.scaled()))

            InstructionBadge(text = stringResource(R.string.fill_blank_tap_instruction), icon = Icons.Rounded.TouchApp)

            Spacer(modifier = Modifier.height(Dimens16.scaled()))

            BottomLetterOptions(viewModel)

            Spacer(Modifier.weight(1f))

            FeedbackText(
                title = stringResource(viewModel.uiState.feedbackTextRes),
                subtitle = stringResource(viewModel.uiState.feedbackSubTextRes),
                isSuccess = viewModel.uiState.isAnswerCorrect,
                isVisible = viewModel.uiState.showNext
            )
        }
    }
}
