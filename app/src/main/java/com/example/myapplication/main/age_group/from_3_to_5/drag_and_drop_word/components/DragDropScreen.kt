package com.example.myapplication.main.age_group.from_3_to_5.drag_and_drop_word.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.myapplication.main.age_group.from_3_to_5.drag_and_drop_word.view_model.DragDropWordViewModel
import com.example.myapplication.main.age_group.from_3_to_5.missing_letter.view_model.MissingLetterViewModel
import com.example.myapplication.main.common.getImageResFromWord
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens4

@Composable
fun DragDropScreen(
    viewModel: DragDropWordViewModel,
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
                    modifier = Modifier.fillMaxHeight(0.2f)
                )

                Spacer(modifier = Modifier.height(Dimens16))
            }

            // -------------------------
            // WORD SLOTS
            // -------------------------
            DragDropTopSlots(viewModel)

            Spacer(modifier = Modifier.height(Dimens24 * 2))

            // -------------------------
            // LETTER POOL
            // -------------------------
            DragDropBottomPool(viewModel)

            Spacer(Modifier.weight(1f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = Dimens16)
            ) {

                Text(
                    text = stringResource(uiState.feedbackWrongTextRes),
                    color = Color.Red,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.alpha(if (uiState.showError) 1f else 0f)
                )

                Spacer(modifier = Modifier.height(Dimens4))

                Text(
                    text = stringResource(uiState.feedbackWrongSubTextRes),
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.alpha(if (uiState.showError) 1f else 0f)
                )
            }
        }


    }
}