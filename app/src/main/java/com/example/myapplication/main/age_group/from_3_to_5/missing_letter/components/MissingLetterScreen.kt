package com.example.myapplication.main.age_group.from_3_to_5.missing_letter.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.main.age_group.from_3_to_5.missing_letter.view_model.MissingLetterViewModel
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24

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

            // -------------------------
            // WORD SLOTS
            // -------------------------
            WordTopSlots(viewModel)

            Spacer(modifier = Modifier.height(Dimens20 * 2))

            // -------------------------
            // LETTER POOL
            // -------------------------
            LetterBottomPool(viewModel)

            Spacer(Modifier.weight(1f))

            Text(
                text = uiState.errorText,
                color = Color.Red,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = Dimens16).alpha(if (uiState.showError) 1f else 0f)
            )
        }


    }
}