package com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.common.AppToolbarDropDownOnRight
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.presentation.BottomTracingControls
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.presentation.CenterLearningLayout
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.AlphabetTracingViewModel
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.LetterMode
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.getImageResForLetterArrow
import com.example.myapplication.main.common.getImageResFromWord
import com.example.myapplication.main.common.sheets.LocalAccessSheetViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch


// Letters K–Z start at index 10 (A=0, B=1, … J=9, K=10, … Z=25)

// Ignore next/previous taps within this window so a double-tap moves only one letter
private const val NAV_CLICK_THROTTLE_MS = 400L

@Composable
fun AlphabetTracingPage(
    navController: NavController,
    viewModel: AlphabetTracingViewModel = hiltViewModel()
) {
    val accessVM = LocalAccessSheetViewModel.current
    val scope = rememberCoroutineScope()

    val currentItem = viewModel.lettersData.getOrNull(viewModel.uiState.currentIndex)
    val word = currentItem?.second
    val displayString = viewModel.uiState.mode.displayString(viewModel.uiState.currentIndex)
    val arrowImageRes = getImageResForLetterArrow("${displayString.lowercase()}_arrow")

    val lastNavClickMs = remember { longArrayOf(0L) }

    // Throttled navigation so rapid taps don't advance multiple letters at once.
    fun navigateTo(targetIndex: Int, doNavigate: () -> Unit) {
        val now = System.currentTimeMillis()
        if (now - lastNavClickMs[0] < NAV_CLICK_THROTTLE_MS) return
        lastNavClickMs[0] = now
        doNavigate()
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
                word = word,
                arrowImageRes = arrowImageRes
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
                },
                onPractice = {
                    navController.navigate(RouteNavigation.LetterPractice.route)
                }
            )
        }
    }
}
//@Preview(
//    showBackground = true,
//    showSystemUi = true,
//    device = "spec:width=1280dp,height=720dp,dpi=240"
//)
//@Composable
//private fun AlphabetTracingLandscapePreview() {
//    AlphabetTracingPreviewContent()
//}
//@Composable
//private fun AlphabetTracingPreviewContent() {
//    val fakeViewModel = remember { AlphabetTracingViewModel().also { it.isPreviewMode = true } }
//    val previewWord = fakeViewModel.lettersData.getOrNull(fakeViewModel.uiState.currentIndex)?.second
//    val previewImageRes = getImageResFromWord(previewWord)
//    MyApplicationTheme {
//        Box(modifier = Modifier.fillMaxSize()) {
//            KidsGradientBackground(gradient = KidsGradient.pinkPeach, shape = KidsFloatingShape.sparkles)
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .windowInsetsPadding(WindowInsets.safeDrawing)
//            ) {
//                AppToolbarDropDownOnRight(
//                    modifier = Modifier.fillMaxWidth(),
//                    title = stringResource(R.string.alphabet_tracing),
//                    currentSelected = fakeViewModel.uiState.mode.title,
//                    modes = LetterMode.entries.map { it.title },
//                    onItemChange = {},
//                    onBackClick = {}
//                )
//                CenterLearningLayout(
//                    modifier = Modifier.weight(1f),
//                    viewModel = fakeViewModel,
//                    imageRes = previewImageRes,
//                    word = previewWord
//                )
//                BottomTracingControls(
//                    onClear = {},
//                    onPrevious = {},
//                    onNext = {}
//                )
//            }
//        }
//    }
//}
