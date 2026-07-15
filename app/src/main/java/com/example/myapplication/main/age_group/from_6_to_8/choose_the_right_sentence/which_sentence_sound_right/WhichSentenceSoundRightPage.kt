package com.example.myapplication.main.age_group.from_6_to_8.choose_the_right_sentence.which_sentence_sound_right

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.SentenceUnit
import com.example.myapplication.main.age_group.from_6_to_8.choose_the_right_sentence.which_sentence_sound_right.view_model.WhichSentenceSoundRightViewModel
import com.example.myapplication.main.common.ActivityCompletePopup
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.buttons.KidsIconButton
import com.example.myapplication.main.common.buttons.KidsLabel
import com.example.myapplication.main.common.buttons.KidsOptionButton
import com.example.myapplication.main.common.GameTimerBar
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.KidIconMedium
import com.example.myapplication.ui.theme.AppDimens.listenAndAnswerOptionsHeight
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground


@Composable
fun WhichSentenceSoundRightPage(
    unit : SentenceUnit,
    level : SentenceLevel,
    navController: NavController,
    viewModel: WhichSentenceSoundRightViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.setData(unit,level)
    }
    Box(modifier = Modifier.fillMaxSize()) {

        KidsGradientBackground(gradient = KidsGradient.tealCyan, shape = KidsFloatingShape.musicNotes)
        Column(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)) {
            // HEADER
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                BackButtonWithText(
                    title = stringResource(R.string.which_sentence_sounds_right),
                    modifier = Modifier.weight(1f),
                    onBackClick = { navController.popBackStack() }
                )

                // ⏱ Timed mode toggle (item 4.3)
                KidsIconButton(
                    icon = Icons.Rounded.Timer,
                    onClick = { viewModel.toggleTimedMode() },
                    type = if (uiState.timedMode) ButtonType.GREEN else ButtonType.DISABLE,
                    size = KidIconMedium,
                    modifier = Modifier.padding(end = Dimens8)
                )

                KidsLabel("Question ${uiState.currentIndex + 1} / ${uiState.questions.size}",)
            }

            // ⏱ Drain bar (item 4.3)
            if (uiState.timedMode) {
                GameTimerBar(
                    progress = uiState.timerProgress,
                    height = 10.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens16)
                        .padding(top = Dimens12, bottom = Dimens8)
                )
            }

            Spacer(Modifier.weight(1f))
            if (!uiState.showResult) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens16),
                    modifier = Modifier.fillMaxSize()
                        .padding(horizontal = Dimens16).padding(bottom = Dimens16)
                ) {

                    Column(
                        verticalArrangement = Arrangement.spacedBy(Dimens12)
                    ) {
                        uiState.options.forEach { word ->

                            KidsOptionButton(
                                text = word.replaceFirstChar { it.uppercase() },
                                type = viewModel.backgroundType(word),
                                fontSize = listenAndAnswerOptionsHeight.value.sp * 0.37,
                                onClick = {
                                    viewModel.selectAnswer(word)
                                },
                                enabled = uiState.selectedAnswer == null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(listenAndAnswerOptionsHeight),
                                textAlign = TextAlign.Left
                            )
                        }

                        // Next Button Row
                        Row {
                            Spacer(Modifier.weight(1f))

                            KidsActionButton(
                                text = stringResource(R.string.next),
                                icon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                type = if (uiState.selectedAnswer == null)
                                    ButtonType.DISABLE else ButtonType.ORANGE,
                                isIconStart = false,
                                onClick = {
                                    if (uiState.selectedAnswer != null) {
                                        viewModel.next()
                                    }
                                }
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.weight(1f))
        }

        if (uiState.showResult) {
            val accuracy = if (uiState.questions.isNotEmpty())
                uiState.score.toDouble() / uiState.questions.size else 0.0
            ActivityCompletePopup(
                stars = when {
                    accuracy >= 0.8 -> 3
                    accuracy >= 0.5 -> 2
                    else -> 1
                },
                score = uiState.score,
                total = uiState.questions.size,
                scoreLabel = stringResource(R.string.correct_answers),
                feedbackTextRes = when {
                    accuracy >= 0.8 -> R.string.feedbackPhrases_1
                    accuracy >= 0.5 -> R.string.feedbackPhrases_2
                    else -> R.string.feedbackPhrases_3
                },
                onNext = { viewModel.restart() },
                nextLabel = stringResource(R.string.want_to_continue),
                dismissLabel = stringResource(R.string.go_back),
                onClose = { navController.popBackStack() }
            )
        }
    }
}
