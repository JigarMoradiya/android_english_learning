package com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.pronouns.practice

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_6_to_8.common.BlankSentenceView
import com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.pronouns.practice.view_model.PronounPracticeViewModel
import com.example.myapplication.main.common.ActivityCompletePopup
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.FeedbackText
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.buttons.KidsLabel
import com.example.myapplication.main.common.buttons.KidsOptionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.grammarBasicOptionsHeight
import com.example.myapplication.ui.theme.AppDimens.grammarBasicPronounOptionsWidth
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled
import kotlin.collections.chunked
import kotlin.collections.forEach
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground

@Composable
fun PronounPracticePage(
    navController: NavController,
    viewModel: PronounPracticeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        KidsGradientBackground(gradient = KidsGradient.creamMint, shape = KidsFloatingShape.mixShapes)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {

            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                BackButtonWithText(
                    title = stringResource(R.string.practice_pronouns),
                    onBackClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier.weight(1f)
                )

                KidsLabel(
                    txt = "Question ${uiState.currentIndex + 1} / ${uiState.questions.size}",
                    type = ButtonType.PURPLE,
                )

                if (!uiState.isCompleted) {
                    KidsActionButton(
                        text = if (uiState.selectedAnswer == null) {
                            stringResource(R.string.next_sentence)
                        } else if (viewModel.isLastIndex) {
                            stringResource(R.string.check_result)
                        } else {
                            stringResource(R.string.next_sentence)
                        },
                        type = if (uiState.selectedAnswer == null) {
                            ButtonType.DISABLE
                        } else if (viewModel.isLastIndex) {
                            ButtonType.POSITIVE
                        } else {
                            ButtonType.ORANGE
                        },
                        onClick = {
                            viewModel.moveToNextQuestion()
                        },
                        isSmall = true,
                        disable = uiState.selectedAnswer == null,
                        modifier = Modifier.padding(end = Dimens16)
                    )
                }
            }

            if (!uiState.isCompleted) {

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Dimens8)
                    ) {
                        Spacer(modifier = Modifier.weight(1f))

                        uiState.currentQuestion?.let { question ->
                            BlankSentenceView(
                                sentence = question.question,
                                correctAnswer = question.correctAnswer,
                                selectedAnswer = uiState.selectedAnswer,
                                isAnswerCorrect = uiState.isAnswerCorrect,
                                options = question.options,
                                modifier = Modifier.padding(horizontal = Dimens16)
                            )
                        }

                        Spacer(modifier = Modifier.height(Dimens8))

                        uiState.shuffledOptions.chunked(2).forEach { rowItems ->

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(Dimens8),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                rowItems.forEach { word ->

                                    KidsOptionButton(
                                        text = word,
                                        type = ButtonType.OPTIONS,
                                        fontSize = grammarBasicOptionsHeight.value.sp * 0.5,
                                        onClick = {
                                            viewModel.selectAnswer(word)
                                        },
                                        enabled = !uiState.showNext,
                                        modifier = Modifier
                                            .width(grammarBasicPronounOptionsWidth)
                                            .height(grammarBasicOptionsHeight)
                                    )
                                }

                                if (rowItems.size == 1) {
                                    Spacer(modifier = Modifier.width(grammarBasicPronounOptionsWidth))
                                }
                            }
                        }

                        Text(
                            text = AnnotatedString.fromHtml(stringResource(R.string.tap_the_correct_pronoun)),
                            style = MaterialTheme.typography.bodyLarge.scaled(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black.copy(alpha = 0.8f)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        FeedbackText(
                            title = uiState.feedbackTextRes?.let { stringResource(it) }?:"",
                            subtitle = uiState.feedbackSubTextRes?.let { stringResource(it) }?:"",
                            isSuccess = uiState.isAnswerCorrect,
                            isVisible = uiState.showNext
                        )
                    }
                }
            }
        }

        if (uiState.isCompleted) {
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
