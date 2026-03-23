package com.example.myapplication.main.age_group.from_3_to_5.letter_tracking

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.common.AppToolbarDropDownOnRight
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.presentation.BottomTracingControls
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.presentation.CenterLearningLayout
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.view_model.LetterMode
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.view_model.LetterTracingViewModel
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.safeImageRes


@Composable
fun AlphabetTrackingPage(
    navController: NavController,
    viewModel: LetterTracingViewModel = hiltViewModel()
) {

    val currentItem = viewModel.lettersData
        .getOrNull(viewModel.uiState.currentIndex)

    val word = currentItem?.second
    val imageRes = word
        ?.lowercase()
        ?.replace(" ", "")
        ?.let { safeImageRes(it) }

    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundUI(isGreenGrassShow = false)

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            AppToolbarDropDownOnRight(
                title = "Alphabet Tracking",
                currentSelected = viewModel.uiState.mode.title,
                modes = LetterMode.entries.map { it.title },
                onItemChange = {
                    val mode = LetterMode.entries.first { m -> m.title == it }
                    viewModel.changeMode(mode)
                },
                onBackClick = { navController.popBackStack() }
            )

            CenterLearningLayout(
                modifier = Modifier.weight(1f),
                viewModel = viewModel,
                imageRes = imageRes,
                word = word
            )

            BottomTracingControls(
                onClear = { viewModel.clear() },
                onPrevious = { viewModel.previous() },
                onNext = { viewModel.next() }
            )
        }
    }
}
