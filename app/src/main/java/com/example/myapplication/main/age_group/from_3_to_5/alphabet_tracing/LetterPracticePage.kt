package com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.common.AppToolbarDropDownOnRight
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.presentation.BottomTracingControls
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.presentation.canvas.AutoTracePreviewCanvas
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.presentation.canvas.FreeDrawCanvas
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.presentation.canvas.GuidelineCanvas
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.AlphabetTracingViewModel
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.LetterMode
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.sheets.LocalAccessSheetViewModel
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens40
import com.example.myapplication.ui.theme.AppDimens.isLargeTablet
import com.example.myapplication.ui.theme.AppDimens.isTablet
import kotlinx.coroutines.launch

// Letters K–Z start at index 10 (A=0, B=1, … J=9, K=10, … Z=25)
private const val LOCKED_FROM_INDEX = 10

// Ignore next/previous taps within this window so a double-tap moves only one letter
private const val NAV_CLICK_THROTTLE_MS = 400L

@Composable
fun LetterPracticePage(
    navController: NavController,
    viewModel: AlphabetTracingViewModel = hiltViewModel()
) {
    val accessVM = LocalAccessSheetViewModel.current
    val scope = rememberCoroutineScope()

    val uiState = viewModel.uiState

    // Free-draw strokes are screen-local; reset when the letter or mode changes
    var finishedStrokes by remember(uiState.currentIndex, uiState.mode) {
        mutableStateOf<List<List<Offset>>>(emptyList())
    }
    var currentStroke by remember(uiState.currentIndex, uiState.mode) {
        mutableStateOf<List<Offset>>(emptyList())
    }

    val lastNavClickMs = remember { longArrayOf(0L) }
    val isAccessCheckInFlight = remember { booleanArrayOf(false) }

    // Same gated + throttled navigation as AlphabetTracingPage
    fun navigateTo(targetIndex: Int, doNavigate: () -> Unit) {
        val now = System.currentTimeMillis()
        if (now - lastNavClickMs[0] < NAV_CLICK_THROTTLE_MS || isAccessCheckInFlight[0]) return
        lastNavClickMs[0] = now

        if (targetIndex >= LOCKED_FROM_INDEX) {
            isAccessCheckInFlight[0] = true
            scope.launch {
                try {
                    val allowed = accessVM.checkAccess(ModuleID.ALPHABET_TRACING_NZ)
                    if (allowed) doNavigate()
                } finally {
                    isAccessCheckInFlight[0] = false
                }
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
                title = stringResource(R.string.letter_practice),
                currentSelected = uiState.mode.title,
                modes = LetterMode.entries.map { it.title },
                onItemChange = {
                    val mode = LetterMode.entries.first { m -> m.title == it }
                    viewModel.changeMode(mode)
                },
                onBackClick = { navController.popBackStack() }
            )

            Box(
                modifier = Modifier.weight(1f).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                BoxWithConstraints {

                    val minusExtraSpace = if (isLargeTablet) 100.dp else if (isTablet) 60.dp else 0.dp
                    val gap = Dimens40
                    // Cap so preview (0.45×) + gap + canvas always fit side by side
                    val size = min(
                        min(maxWidth - minusExtraSpace, maxHeight - minusExtraSpace),
                        (maxWidth - gap) / 1.5f
                    )
                    val previewSize = size * 0.45f

                    // Preview + canvas centered together as one group
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // Auto-play tracing preview
                        Box(
                            modifier = Modifier.size(previewSize),
                            contentAlignment = Alignment.Center
                        ) {
                            Card(
                                modifier = Modifier.fillMaxSize(),
                                shape = RoundedCornerShape(Dimens16),
                                elevation = CardDefaults.cardElevation(Dimens4),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                GuidelineCanvas(viewModel, lineAlphaFactor = 0.2f)
                            }
                            AutoTracePreviewCanvas(
                                letter = viewModel.currentLetter,
                                mode = uiState.mode,
                                strokeColor = viewModel.getLetterColor(),
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.width(gap))

                        // Big free-draw canvas
                        Box(
                            modifier = Modifier.size(size),
                            contentAlignment = Alignment.Center
                        ) {
                            Card(
                                modifier = Modifier.fillMaxSize(),
                                shape = RoundedCornerShape(Dimens16),
                                elevation = CardDefaults.cardElevation(Dimens4),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                GuidelineCanvas(viewModel)
                            }
                            FreeDrawCanvas(
                                letter = viewModel.currentLetter,
                                mode = uiState.mode,
                                strokeColor = viewModel.getLetterColor(),
                                finishedStrokes = finishedStrokes,
                                currentStroke = currentStroke,
                                onStrokeStart = {
                                    currentStroke = listOf(it)
                                    viewModel.playPhonicsSound()
                                },
                                onStrokeMove = { currentStroke = currentStroke + it },
                                onStrokeEnd = {
                                    if (currentStroke.size > 1) {
                                        finishedStrokes = finishedStrokes + listOf(currentStroke)
                                    }
                                    currentStroke = emptyList()
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }

            BottomTracingControls(
                onClear = {
                    finishedStrokes = emptyList()
                    currentStroke = emptyList()
                },
                onPrevious = {
                    val currentIndex = uiState.currentIndex
                    val prevIndex = if (currentIndex == 0) 25 else currentIndex - 1
                    navigateTo(prevIndex) { viewModel.previous() }
                },
                onNext = {
                    val currentIndex = uiState.currentIndex
                    val nextIndex = (currentIndex + 1) % 26
                    navigateTo(nextIndex) { viewModel.next() }
                }
            )
        }
    }
}