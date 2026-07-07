package com.example.myapplication.main.age_group.from_3_to_5.letter_recognition.mirror_letters

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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.min
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.presentation.BottomTracingControls
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.presentation.canvas.AutoTracePreviewCanvas
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.presentation.canvas.FreeDrawCanvas
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.presentation.canvas.GuidelineCanvas
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.LetterMode
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.letterPalettes
import com.example.myapplication.main.age_group.from_3_to_5.letter_recognition.mirror_letters.view_model.MirrorLettersViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens40

// Same auto-preview + free-draw canvas as the main Letter Tracing "practice"
// screen, just constrained to the pair's 2 letters instead of the full
// alphabet — the tracing canvases already take letter/mode explicitly, so no
// need to touch AlphabetTracingViewModel at all.
@Composable
fun MirrorLettersPracticePage(
    navController: NavController,
    pair: MirrorLetterPair,
    viewModel: MirrorLettersViewModel = hiltViewModel()
) {
    val letters = remember(pair) { listOf(pair.first, pair.second) }
    var currentIndex by remember(pair) { mutableIntStateOf(0) }
    val currentLetter = letters[currentIndex]

    var finishedStrokes by remember(currentIndex) {
        mutableStateOf<List<List<Offset>>>(emptyList())
    }
    var currentStroke by remember(currentIndex) {
        mutableStateOf<List<Offset>>(emptyList())
    }

    // Picked once per letter (not re-randomized on every switch), keyed off the
    // letter itself since a 2-item custom sequence doesn't line up with the
    // full-alphabet palette order.
    val strokeColors = remember(pair) {
        letters.associateWith { letter ->
            val idx = kotlin.math.abs(letter.hashCode()) % letterPalettes.size
            letterPalettes[idx].random()
        }
    }
    val strokeColor = strokeColors.getValue(currentLetter)

    DisposableEffect(pair) {
        onDispose { viewModel.recordSession(pair) }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.pinkPeach, shape = KidsFloatingShape.sparkles)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            BackButtonWithText(
                title = "${stringResource(R.string.menu_tricky_twins)} - ${pair.title}",
                onBackClick = { navController.popBackStack() }
            )

            Box(
                modifier = Modifier.weight(1f).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                BoxWithConstraints {
                    val gap = Dimens40
                    val size = min(
                        min(maxWidth, maxHeight),
                        (maxWidth - gap) / 1.5f
                    )
                    val previewSize = size * 0.45f

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                                GuidelineCanvas(LetterMode.LOWERCASE, lineAlphaFactor = 0.2f)
                            }
                            AutoTracePreviewCanvas(
                                letter = currentLetter,
                                mode = LetterMode.LOWERCASE,
                                strokeColor = strokeColor,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.width(gap))

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
                                GuidelineCanvas(LetterMode.LOWERCASE)
                            }
                            FreeDrawCanvas(
                                letter = currentLetter,
                                mode = LetterMode.LOWERCASE,
                                strokeColor = strokeColor,
                                finishedStrokes = finishedStrokes,
                                currentStroke = currentStroke,
                                onStrokeStart = {
                                    currentStroke = listOf(it)
                                    viewModel.playPhonicsSound(currentLetter)
                                    viewModel.markLetterCompleted(currentLetter)
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
                onPrevious = { currentIndex = (currentIndex - 1 + letters.size) % letters.size },
                onNext = { currentIndex = (currentIndex + 1) % letters.size }
            )
        }
    }
}
