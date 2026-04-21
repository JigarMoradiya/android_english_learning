package com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.common.AppToolbarDropDownOnRight
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.presentation.BottomTracingControls
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.presentation.CenterLearningLayout
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.LetterMode
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.AlphabetTracingViewModel
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.getImageResFromWord


@Composable
fun AlphabetTracingPage(
    navController: NavController,
    viewModel: AlphabetTracingViewModel = hiltViewModel()
) {

    val currentItem = viewModel.lettersData.getOrNull(viewModel.uiState.currentIndex)
    val word = currentItem?.second

    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundUI(isGreenGrassShow = false)

        Column(
            modifier = Modifier.fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {

            AppToolbarDropDownOnRight(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.alphabet_tracing),
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
                imageRes = getImageResFromWord(word),
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
