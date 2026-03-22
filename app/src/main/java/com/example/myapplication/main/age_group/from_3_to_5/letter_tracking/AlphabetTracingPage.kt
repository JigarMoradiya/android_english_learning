package com.example.myapplication.main.age_group.from_3_to_5.letter_tracking

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.common.AppToolbarDropDownOnRight
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.presentation.components.BottomTracingControls
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.presentation.components.CenterLearningLayout
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.view_model.LetterMode
import com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.view_model.LetterTracingViewModel
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.safeImageRes


@Composable
fun AlphabetTrackingPage(
    navController: NavController,
    viewModel: LetterTracingViewModel = hiltViewModel()
) {

    val filteredItem = viewModel.lettersData
        .getOrNull(viewModel.uiState.currentIndex)
    val word = filteredItem?.second
    val imageName = word
        ?.lowercase()
        ?.replace(" ", "")

    val imageRes = imageName?.let { safeImageRes(it) }
    Box{
        BackgroundUI(isGreenGrassShow = false)
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // ✅ HEADER (fixed)
            AppToolbarDropDownOnRight(
                title = "Alphabet Tracking",
                currentSelected = viewModel.uiState.mode.title,
                modes = LetterMode.entries.map { it.title },
                onItemChange = { selected ->
                    val mode = LetterMode.entries.first { it.title == selected }
                    viewModel.changeMode(mode)
                },
                onBackClick = { navController.popBackStack() },
            )

            // ✅ CENTER (fixed)
            CenterLearningLayout(
                modifier = Modifier.weight(1f),
                viewModel = viewModel,
                imageRes = imageRes,
                word = word
            )

            // ✅ FOOTER (fixed)
            BottomTracingControls(
                onClear = { viewModel.clear() },
                onPrevious = { viewModel.previous() },
                onNext = { viewModel.next() }
            )
        }

    }
}
