package com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.common.AppToolbarDropDownOnRight
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.presentation.BottomTracingControls
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.presentation.CenterLearningLayout
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.LetterMode
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.AlphabetTracingViewModel
import com.example.myapplication.main.common.getImageResFromWord
import com.example.myapplication.main.common.sheets.LocalAccessSheetViewModel
import kotlinx.coroutines.launch
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground


// Letters N–Z start at index 13 (A=0, B=1, … M=12, N=13, … Z=25)
private const val LOCKED_FROM_INDEX = 13

@Composable
fun AlphabetTracingPage(
    navController: NavController,
    viewModel: AlphabetTracingViewModel = hiltViewModel()
) {
    val accessVM = LocalAccessSheetViewModel.current
    val scope = rememberCoroutineScope()

    val currentItem = viewModel.lettersData.getOrNull(viewModel.uiState.currentIndex)
    val word = currentItem?.second

    // Helper: navigate to [targetIndex] but first gate N–Z for guests
    fun navigateTo(targetIndex: Int, doNavigate: () -> Unit) {
        if (targetIndex >= LOCKED_FROM_INDEX) {
            scope.launch {
                val allowed = accessVM.checkAccess(ModuleID.ALPHABET_TRACING_NZ)
                if (allowed) doNavigate()
            }
        } else {
            doNavigate()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        KidsGradientBackground(gradient = KidsGradient.pinkPeach, shape = KidsFloatingShape.sparkles)

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
                onPrevious = {
                    val currentIndex = viewModel.uiState.currentIndex
                    val prevIndex = if (currentIndex == 0) 25 else currentIndex - 1
                    navigateTo(prevIndex) { viewModel.previous() }
                },
                onNext = {
                    val currentIndex = viewModel.uiState.currentIndex
                    val nextIndex = (currentIndex + 1) % 26
                    navigateTo(nextIndex) { viewModel.next() }
                }
            )
        }
    }
}
