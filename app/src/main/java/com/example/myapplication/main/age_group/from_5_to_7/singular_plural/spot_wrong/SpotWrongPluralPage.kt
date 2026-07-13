package com.example.myapplication.main.age_group.from_5_to_7.singular_plural.spot_wrong

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_5_to_7.singular_plural.spot_wrong.view_model.SpotWrongPluralViewModel
import com.example.myapplication.main.common.ActivityCompletePopup
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.ColoredFeedbackView
import com.example.myapplication.main.common.CountdownBadge
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.buttons.KidsLabel
import com.example.myapplication.main.common.buttons.KidsOptionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens40
import com.example.myapplication.ui.theme.AppDimens.grammarBasicOptionsHeight
import com.example.myapplication.ui.theme.AppDimens.grammarBasicOptionsWidth

@Composable
fun SpotWrongPluralPage(
    navController: NavController,
    viewModel: SpotWrongPluralViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.mintLime, shape = KidsFloatingShape.musicNotes)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                BackButtonWithText(
                    title = stringResource(R.string.spot_wrong_plural_),
                    modifier = Modifier.weight(1f),
                    onBackClick = { navController.popBackStack() }
                )

                KidsLabel(
                    txt = "${minOf(uiState.questionIndex, uiState.totalQuestions)}/${uiState.totalQuestions}",
                )

                uiState.countdown?.let { count ->
                    CountdownBadge(
                        count = count,
                        modifier = Modifier.padding(end = Dimens16)
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens40),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens16)
            ) {
                Text(
                    text = stringResource(R.string.spot_wrong_plural_prompt),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimens16, Alignment.CenterHorizontally)
                ) {
                    uiState.options.forEach { option ->
                        KidsOptionButton(
                            text = option,
                            type = viewModel.optionButtonType(option),
                            fontSize = (grammarBasicOptionsHeight.value * 0.35f).sp,
                            enabled = uiState.selectedAnswer == null,
                            onClick = { viewModel.checkAnswer(option) },
                            modifier = Modifier
                                .width(grammarBasicOptionsWidth)
                                .height(grammarBasicOptionsHeight)
                        )
                    }
                }
            }

            Spacer(Modifier.height(Dimens10))

            ColoredFeedbackView(
                feedbackText = uiState.feedbackText,
                isAnswerCorrect = uiState.isAnswerCorrect,
                correctAnswer = uiState.wrongOption
            )

            Spacer(Modifier.weight(1f))
        }

        if (uiState.showCompletePopup) {
            ActivityCompletePopup(
                stars = when {
                    uiState.score.toFloat() / uiState.totalQuestions >= 0.8f -> 3
                    uiState.score.toFloat() / uiState.totalQuestions >= 0.5f -> 2
                    else -> 1
                },
                score = uiState.score,
                total = uiState.totalQuestions,
                scoreLabel = if (uiState.score == uiState.totalQuestions) "perfect! 🎯" else "first try 🎯",
                feedbackText = stringResource(R.string.your_result),
                onNext = { viewModel.startNewRound() },
                onClose = { navController.popBackStack() }
            )
        }
    }
}
