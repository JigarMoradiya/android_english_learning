package com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.noun.practice

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
import com.example.myapplication.main.age_group.from_6_to_8.common.ResultView
import com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.common_ui.practice.GrammarPracticeQuestionLayout
import com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.noun.practice.view_model.NounPracticeViewModel
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
fun NounPracticePage(
    navController: NavController,
    viewModel: NounPracticeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        KidsGradientBackground(gradient = KidsGradient.peachCoral, shape = KidsFloatingShape.diamonds)

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
                    title = stringResource(R.string.practice_nouns),
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
                            stringResource(R.string.next_noun)
                        } else if (viewModel.isLastIndex) {
                            stringResource(R.string.check_result)
                        } else {
                            stringResource(R.string.next_noun)
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
                            instructionText = stringResource(R.string.tap_the_correct_noun),
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

            } else {

                ResultView(
                    score = uiState.score,
                    total = uiState.questions.size,
                    primaryButtonText = stringResource(R.string.want_to_continue),
                    secondaryButtonText = stringResource(R.string.go_back),
                    title = stringResource(R.string.your_result),
                    modifier = Modifier.padding(horizontal = Dimens16),
                    onSecondaryTap = { navController.popBackStack() },
                    onPrimaryTap = { viewModel.restart() }
                )
            }
        }
    }
}
