package com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.beginner.multiple_choice

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.model.WordType
import com.example.myapplication.main.age_group.from_6_to_8.common.ResultView
import com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.beginner.multiple_choice.view_model.GrammarMultipleChoiceViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.FeedbackText
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.buttons.KidsLabel
import com.example.myapplication.main.common.buttons.KidsOptionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.grammarBasicOptionsHeight
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled

@Composable
fun GrammarMultipleChoicePage(
    navController: NavController,
    viewModel: GrammarMultipleChoiceViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val allTypes = listOf(WordType.NOUN, WordType.VERB, WordType.ADJECTIVE, WordType.PRONOUN)

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundUI(isGreenGrassShow = false)
        Column(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                BackButtonWithText(
                    title = "Multiple Choice",
                    modifier = Modifier.weight(1f),
                    onBackClick = { navController.popBackStack() }
                )
                KidsLabel(txt = "Q ${uiState.currentIndex + 1} / ${uiState.questions.size}", type = ButtonType.GREEN)
                if (!uiState.isCompleted) {
                    KidsActionButton(
                        text = if (viewModel.isLastQuestion) stringResource(R.string.check_result)
                               else stringResource(R.string.next),
                        icon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        isIconStart = false, isSmall = true,
                        type = if (!uiState.showFeedback) ButtonType.DISABLE
                               else if (viewModel.isLastQuestion) ButtonType.POSITIVE
                               else ButtonType.ORANGE,
                        disable = !uiState.showFeedback,
                        onClick = { if (uiState.showFeedback) viewModel.next() },
                        modifier = Modifier.padding(end = Dimens16)
                    )
                }
            }

            if (uiState.isCompleted) {
                ResultView(
                    score = uiState.score, total = uiState.questions.size,
                    title = "Multiple Choice", firstBtnTxt = stringResource(R.string.go_back),
                    onBack = { navController.popBackStack() }, onContinue = { viewModel.load() },
                    modifier = Modifier.padding(horizontal = Dimens16)
                )
            } else {
                Spacer(Modifier.weight(1f))

                uiState.currentQuestion?.let { q ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = Dimens24)
                    ) {
                        Text(
                            text = "What type of word is…",
                            style = MaterialTheme.typography.titleSmall.scaled(),
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(Dimens8))
                        Text(
                            text = "\"${q.word}\"",
                            style = MaterialTheme.typography.displayMedium.scaled(),
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(Modifier.height(Dimens24))

                // 2×2 grid of word-type options
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = Dimens24),
                    verticalArrangement = Arrangement.spacedBy(Dimens12),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    allTypes.chunked(2).forEach { rowTypes ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Dimens12),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            rowTypes.forEach { type ->
                                val label = type.name.lowercase().replaceFirstChar { it.uppercase() }
                                KidsOptionButton(
                                    text = label,
                                    type = viewModel.optionButtonType(type),
                                    fontSize = (grammarBasicOptionsHeight.value * 0.55f).sp,
                                    enabled = !uiState.showFeedback,
                                    onClick = { viewModel.selectAnswer(type) },
                                    modifier = Modifier.weight(1f).height(grammarBasicOptionsHeight)
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(Dimens16))

                FeedbackText(
                    title = uiState.feedbackTitleRes?.let { stringResource(it) },
                    subtitle = if (uiState.showFeedback && !uiState.isAnswerCorrect)
                        "\"${uiState.currentQuestion?.word}\" is a ${uiState.currentQuestion?.correctType?.name?.lowercase()}" else null,
                    isSuccess = uiState.isAnswerCorrect,
                    isVisible = uiState.showFeedback,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(Modifier.weight(1f))
            }
        }
    }
}
