package com.example.myapplication.main.age_group.from_5_to_7.missing_letter.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.myapplication.main.age_group.from_5_to_7.missing_letter.view_model.MissingLetterViewModel57
import com.example.myapplication.main.common.FeedbackText
import com.example.myapplication.main.common.getImageResFromWord
import com.example.myapplication.ui.theme.AppDimens.Dimens50

@Composable
fun MissingLetterScreen(
    viewModel: MissingLetterViewModel57,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {

        Row(
            modifier = Modifier.fillMaxSize()
        ) {

            // ── Left section: Image + Sentence (50%) ─────────────
            val image = getImageResFromWord(viewModel.targetWord)
            Column(
                modifier =
                    if (image == null) {
                        Modifier.fillMaxHeight()
                    } else {
                        Modifier.weight(0.3f).fillMaxHeight()
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                image?.let {
                    Image(
                        painter = painterResource(it),
                        contentDescription = null,
                        modifier = Modifier.fillMaxHeight(0.6f)
                    )
                }
            }

            Column(
                modifier =
                    if (image == null) {
                        Modifier.weight(1f).fillMaxHeight()
                    } else {
                        Modifier.weight(0.7f).fillMaxHeight()
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Spacer(Modifier.weight(1f))

                // -------------------------
                // WORD SLOTS
                // -------------------------
                WordTopSlots(viewModel)

                Spacer(modifier = Modifier.height(Dimens50))

                // -------------------------
                // LETTER POOL
                // -------------------------
                LetterBottomPool(viewModel)

                Spacer(Modifier.weight(1f))

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