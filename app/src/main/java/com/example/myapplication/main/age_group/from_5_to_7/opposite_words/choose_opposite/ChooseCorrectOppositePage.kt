package com.example.myapplication.main.age_group.from_5_to_7.opposite_words.choose_opposite

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.generation.loader.OppositeDifficulty
import com.example.myapplication.main.age_group.from_5_to_7.opposite_words.choose_opposite.view_model.ChooseCorrectOppositeViewModel
import com.example.myapplication.main.common.ActivityCompletePopup
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.ColoredFeedbackView
import com.example.myapplication.main.common.CountdownBadge
import com.example.myapplication.main.common.buttons.KidsOptionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens40
import com.example.myapplication.ui.theme.AppDimens.Dimens50
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.grammarBasicOptionsHeight
import com.example.myapplication.ui.theme.AppDimens.grammarBasicOptionsWidth
import com.example.myapplication.utils.extensions.appScale
import com.example.myapplication.utils.extensions.scaled
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.buttons.KidsLabel
import com.example.myapplication.ui.theme.AppDimens.Dimens28

@Composable
fun ChooseCorrectOppositePage(
    difficulty: OppositeDifficulty,
    navController: NavController,
    viewModel: ChooseCorrectOppositeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(difficulty) {
        viewModel.loadDifficulty(difficulty)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.peachCoral, shape = KidsFloatingShape.diamonds)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Header ────────────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButtonWithText(
                    title = stringResource(R.string.choose_correct_opposite),
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

                // ── Word card + prompt + options ───────────────────────────────
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens40),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimens24)
                ) {
                    AnimatedContent(
                        targetState = uiState.currentWord,
                        transitionSpec = {
                            (fadeIn() + scaleIn(initialScale = 0.92f)) togetherWith
                                (fadeOut() + scaleOut(targetScale = 0.92f))
                        },
                        label = "wordCard"
                    ) { word ->
                        Box(
                            modifier = Modifier
                                .shadow(
                                    elevation = Dimens6,
                                    shape = RoundedCornerShape(Dimens24),
                                    ambientColor = Color.Black.copy(alpha = 0.12f)
                                )
                                .clip(RoundedCornerShape(Dimens24))
                                .background(Color.White)
                                .border(
                                    width = Dimens2,
                                    color = Color(0xFF0074D5).copy(alpha = 0.25f),
                                    shape = RoundedCornerShape(Dimens24)
                                )
                                .padding(horizontal = Dimens50, vertical = Dimens28),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = word,
                                style = MaterialTheme.typography.displayLarge.scaled().copy(fontSize = 40.sp * appScale()),
                                fontWeight = FontWeight.Black,
                                textAlign = TextAlign.Center,
                                color = Color.Black
                            )
                        }
                    }

                    Text(
                        text = AnnotatedString.fromHtml(stringResource(R.string.what_is_the_opposite_of, uiState.currentWord)),
                        style = MaterialTheme.typography.titleSmall.scaled(),
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Dimens16, Alignment.CenterHorizontally)
                    ) {
                        uiState.options.forEach { option ->
                            KidsOptionButton(
                                text = option,
                                type = viewModel.optionButtonType(option),
                                fontSize = (grammarBasicOptionsHeight.value * 0.45f).sp,
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
                    correctAnswer = uiState.correctAnswer
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

