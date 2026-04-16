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
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.common.AppToolbarDropDownOnRight
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.LetterMode
import com.example.myapplication.main.age_group.from_3_to_5.fill_blank.components.BottomLetterOptions
import com.example.myapplication.main.age_group.from_3_to_5.fill_blank.components.TopLetterSlots
import com.example.myapplication.main.age_group.from_3_to_5.fill_blank.view_model.FillBlankLettersViewModel
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.animations.ConfettiRainEffect
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.buttons.KidsLabel
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens40
import com.example.myapplication.ui.theme.AppDimens.Dimens50
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.ui.theme.PrimaryGreen


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

                if (viewModel.uiState.showSuccess) {
                    KidsActionButton(
                        modifier = Modifier.padding(end = Dimens16),
                        text = stringResource(R.string.next),
                        icon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        type = ButtonType.GREEN,
                        isIconStart = false,
                        onClick = {
                            viewModel.next()
                        }
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            TopLetterSlots(viewModel)

            Spacer(modifier = Modifier.height(Dimens50))

            BottomLetterOptions(viewModel)

            Spacer(Modifier.weight(1f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = stringResource(viewModel.uiState.feedbackTextRes),
                    color = if (viewModel.uiState.showSuccess) PrimaryGreen else Color.Red,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.alpha(
                        if (viewModel.uiState.showError || viewModel.uiState.showSuccess) 1f else 0f
                    )
                )

                Spacer(modifier = Modifier.height(Dimens4))

                Text(
                    text = stringResource(viewModel.uiState.feedbackSubTextRes),
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.alpha(
                        if (viewModel.uiState.showError || viewModel.uiState.showSuccess) 1f else 0f
                    )
                )
            }

            Spacer(modifier = Modifier.height(Dimens16))
        }

        if (viewModel.uiState.showSuccess) {
            ConfettiRainEffect()
        }
    }
}
