package com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.adjectives.practice

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.adjectives.practice.view_model.AdjectivesPracticeViewModel
import com.example.myapplication.main.common.ActivityCompletePopup
import com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.common_ui.practice.GrammarPracticeQuestionLayout
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.buttons.KidsLabel
import com.example.myapplication.main.common.getImageResForSentence
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground

@Composable
fun AdjectivesPracticePage(
    navController: NavController,
    viewModel: AdjectivesPracticeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        KidsGradientBackground(gradient = KidsGradient.peachYellow, shape = KidsFloatingShape.bubbles)

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
                    title = stringResource(R.string.practice_adjectives),
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
                            stringResource(R.string.next_adjective)
                        } else if (viewModel.isLastIndex) {
                            stringResource(R.string.check_result)
                        } else {
                            stringResource(R.string.next_adjective)
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

                uiState.currentQuestion?.let { question ->
                    getImageResForSentence(question.imageName)?.let { resId ->
                        GrammarPracticeQuestionLayout(
                            imageRes = resId,
                            question = question.question,
                            instructionText = stringResource(R.string.tap_the_correct_adjective),
                            options = uiState.shuffledOptions,
                            feedbackTitle = uiState.feedbackTextRes?.let { stringResource(it) },
                            feedbackSubTitle = uiState.feedbackSubTextRes?.let { stringResource(it) },
                            isAnswerCorrect = uiState.isAnswerCorrect,
                            isFeedbackVisible = uiState.showNext,
                            optionTypeProvider = {
                                viewModel.backgroundType(it)
                            },
                            onOptionTap = {
                                viewModel.selectAnswer(it)
                            },
                            isOptionDisabled = uiState.showNext,
                            modifier = Modifier.weight(1f)
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
