package com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.beginner.tap_the_word

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.model.WordType
import com.example.myapplication.main.age_group.from_6_to_8.common.ResultView
import com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.beginner.tap_the_word.view_model.TapTheWordViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.FeedbackText
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.buttons.KidsLabel
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.grammarBasicOptionsHeight
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.ui.theme.getButtonColors
import com.example.myapplication.utils.extensions.scaled

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TapTheWordPage(
    navController: NavController,
    viewModel: TapTheWordViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundUI(isGreenGrassShow = false)
        Column(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                BackButtonWithText(
                    title = "Tap the Word",
                    modifier = Modifier.weight(1f),
                    onBackClick = { navController.popBackStack() }
                )
                KidsLabel(txt = "Q ${uiState.currentIndex + 1} / ${uiState.questions.size}", type = ButtonType.BLUE)
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
                    title = "Tap the Word", firstBtnTxt = stringResource(R.string.go_back),
                    onBack = { navController.popBackStack() }, onContinue = { viewModel.load() },
                    modifier = Modifier.padding(horizontal = Dimens16)
                )
            } else {
                Spacer(Modifier.weight(1f))

                uiState.currentQuestion?.let { q ->
                    // Instruction banner
                    val typeLabel = when (q.targetType) {
                        WordType.NOUN      -> "noun"
                        WordType.VERB      -> "verb"
                        WordType.ADJECTIVE -> "adjective"
                        WordType.PRONOUN   -> "pronoun"
                    }
                    val typeColor = when (q.targetType) {
                        WordType.NOUN      -> Color(0xFF2196F3)
                        WordType.VERB      -> Color(0xFF4CAF50)
                        WordType.ADJECTIVE -> Color(0xFFFF9800)
                        WordType.PRONOUN   -> Color(0xFF9C27B0)
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = Dimens24),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                append("Tap the ")
                                withStyle(SpanStyle(color = typeColor, fontWeight = FontWeight.ExtraBold)) { append(typeLabel) }
                                append(" in the sentence!")
                            },
                            style = MaterialTheme.typography.titleSmall.scaled(),
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(Dimens24))

                        // Word chips
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(Dimens8, Alignment.CenterHorizontally),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            q.allWordsInSentence.forEach { word ->
                                val btnType = viewModel.wordButtonType(word)
                                val colors = getButtonColors(btnType)
                                val isTarget = word == q.targetWord && uiState.showFeedback
                                Box(
                                    modifier = Modifier
                                        .padding(vertical = Dimens4)
                                        .clip(RoundedCornerShape(50))
                                        .background(colors.gradient)
                                        .border(
                                            width = if (isTarget) Dimens4 else Dimens4,
                                            color = if (isTarget && uiState.isAnswerCorrect) Color(0xFF1B5E20)
                                                    else Color.Transparent,
                                            shape = RoundedCornerShape(50)
                                        )
                                        .clickable(enabled = !uiState.showFeedback) { viewModel.tapWord(word) }
                                        .padding(horizontal = Dimens16, vertical = Dimens8),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = word,
                                        style = MaterialTheme.typography.titleMedium.scaled(),
                                        fontWeight = FontWeight.Bold,
                                        color = if (btnType == ButtonType.OPTIONS) Color.Black else Color.White
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(Dimens16))

                FeedbackText(
                    title = uiState.feedbackTitleRes?.let { stringResource(it) },
                    subtitle = if (uiState.showFeedback && !uiState.isAnswerCorrect)
                        "The ${uiState.currentQuestion?.targetType?.name?.lowercase()} was \"${uiState.currentQuestion?.targetWord}\"" else null,
                    isSuccess = uiState.isAnswerCorrect,
                    isVisible = uiState.showFeedback,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(Modifier.weight(1f))
            }
        }
    }
}
