package com.example.myapplication.main.age_group.from_5_to_7.sight_word_choice

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_5_to_7.sight_word_choice.view_model.SightWordChoiceViewModel
import com.example.myapplication.main.common.ActivityCompletePopup
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.ColoredFeedbackView
import com.example.myapplication.main.common.CountdownBadge
import com.example.myapplication.main.common.InstructionBadge
import com.example.myapplication.main.common.buttons.KidsLabel
import com.example.myapplication.main.common.buttons.KidsOptionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens50
import com.example.myapplication.ui.theme.AppDimens.articleChoiceHeight
import com.example.myapplication.ui.theme.AppDimens.articleChoiceWidth
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.ui.theme.PrimaryGreen
import com.example.myapplication.utils.extensions.scaled
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground


@Composable
fun SightWordChoicePage(
    navController: NavController,
    viewModel: SightWordChoiceViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState
    val style = MaterialTheme.typography.titleLarge.scaled().copy(
        fontSize = articleChoiceHeight.value.sp * 0.6
    )

    Box(modifier = Modifier.fillMaxSize()) {

        KidsGradientBackground(gradient = KidsGradient.mintLime, shape = KidsFloatingShape.musicNotes)

        Column(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButtonWithText(
                    title = stringResource(R.string.choose_the_correct_word),
                    modifier = Modifier.weight(1f),
                    onBackClick = { navController.popBackStack() }
                )
                if (uiState.selectedAnswer != null) {
                    KidsLabel("${uiState.questionIndex + 1}/${uiState.totalQuestions}")
                    CountdownBadge(
                        count = uiState.countdown,
                        modifier = Modifier.padding(end = Dimens16)
                    )
                } else {
                    InstructionBadge(
                        text = stringResource(R.string.pick_the_correct_option),
                        isSmall = true,
                        modifier = Modifier.padding(end = Dimens16)
                    )
                    KidsLabel("${uiState.questionIndex + 1}/${uiState.totalQuestions}")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.safeDrawing),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Spacer(modifier = Modifier.weight(1f))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Sentence
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = uiState.sentencePrefix,
                            style = style,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(Dimens4))
                        AnswerSlot(
                            options = uiState.options,
                            selected = uiState.selectedAnswer,
                            isCorrect = uiState.isAnswerCorrect,
                            sentencePrefix = uiState.sentencePrefix,
                            style = style
                        )
                        Spacer(modifier = Modifier.width(Dimens4))
                        Text(
                            uiState.sentenceSuffix,
                            style = style,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(Dimens50))

                    // Options
                    Row(horizontalArrangement = Arrangement.spacedBy(Dimens16)) {
                        uiState.options.forEach { option ->
                            KidsOptionButton(
                                text = if (uiState.sentencePrefix.isEmpty()) option.replaceFirstChar { it.uppercase() } else option.lowercase(),
                                type = ButtonType.OPTIONS,
                                fontSize = articleChoiceHeight.value.sp * 0.6,
                                onClick = { viewModel.checkAnswer(option) },
                                enabled = uiState.selectedAnswer == null,
                                modifier = Modifier
                                    .width(articleChoiceWidth)
                                    .height(articleChoiceHeight)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(Dimens16))

                    // Feedback
                    val str = if (uiState.isAnswerCorrect && uiState.feedbackTextCorrect != null)
                        stringResource(uiState.feedbackTextCorrect)
                    else
                        uiState.feedbackTextWrong
                    ColoredFeedbackView(
                        feedbackText = str ?: "",
                        isAnswerCorrect = uiState.isAnswerCorrect,
                        correctAnswer = if (uiState.sentencePrefix.isEmpty())
                            uiState.currentWord.word.replaceFirstChar { it.uppercase() }
                        else
                            uiState.currentWord.word.lowercase()
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        if (uiState.showBatchPopup) {
            ActivityCompletePopup(
                stars = when {
                    uiState.lastScore.toFloat() / uiState.totalQuestions >= 0.8f -> 3
                    uiState.lastScore.toFloat() / uiState.totalQuestions >= 0.5f -> 2
                    else -> 1
                },
                score = uiState.lastScore,
                total = uiState.totalQuestions,
                scoreLabel = uiState.scoreLabel,
                feedbackTextRes = uiState.feedbackBatchTextRes,
                feedbackSubTextRes = uiState.feedbackBatchSubTextRes,
                onNext = { viewModel.loadNewBatch() },
                onClose = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun AnswerSlot(
    options: List<String>,
    selected: String?,
    isCorrect: Boolean,
    sentencePrefix: String,
    style: TextStyle
) {
    val maxOption = options.maxByOrNull { it.length } ?: "____"

    val displayAnswer = selected?.let {
        if (sentencePrefix.isBlank()) {
            it.replaceFirstChar { c -> c.uppercase() }
        } else {
            it.lowercase()
        }
    }

    Box(contentAlignment = Alignment.BottomCenter) {
        Text(
            text = maxOption,
            color = Color.Transparent,
            style = style,
            fontWeight = FontWeight.ExtraBold
        )

        if (displayAnswer != null) {
            Text(
                text = displayAnswer,
                color = if (isCorrect) PrimaryGreen else Color.Red,
                style = style,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Box(
            modifier = Modifier
                .height(Dimens2)
                .width(articleChoiceWidth * 0.75f)
                .background(Color.Black)
        )
    }
}
