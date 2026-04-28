package com.example.myapplication.main.age_group.from_3_to_5.missing_letter.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.myapplication.main.age_group.from_3_to_5.missing_letter.view_model.MissingLetterViewModel
import com.example.myapplication.main.common.FeedbackText
import com.example.myapplication.main.common.getImageResFromWord
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens50
import com.example.myapplication.utils.extensions.scaled

@Composable
fun MissingLetterScreen(
    viewModel: MissingLetterViewModel,
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState
    Box(modifier = modifier.fillMaxSize()) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.weight(1f))

            getImageResFromWord(viewModel.targetWord)?.let {
                Image(
                    painter = painterResource(it),
                    contentDescription = null,
                    modifier = Modifier.fillMaxHeight(0.17f)
                )

                Spacer(modifier = Modifier.height(Dimens16.scaled()))
            }

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