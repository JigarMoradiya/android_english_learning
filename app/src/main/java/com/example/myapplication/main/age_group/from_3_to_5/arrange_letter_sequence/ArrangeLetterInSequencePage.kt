package com.example.myapplication.main.age_group.from_3_to_5.arrange_letter_sequence

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_3_to_5.arrange_letter_sequence.view_model.ArrangeLetterInSequenceViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI


@Composable
fun ArrangeLetterInSequencePage(
    navController: NavController,
    viewModel: ArrangeLetterInSequenceViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState

    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundUI(isGreenGrassShow = false)

        BackButtonWithText(
            title = stringResource(R.string.arrange_letter_in_sequence),
            onBackClick = { navController.popBackStack() }
        )
    }
}
