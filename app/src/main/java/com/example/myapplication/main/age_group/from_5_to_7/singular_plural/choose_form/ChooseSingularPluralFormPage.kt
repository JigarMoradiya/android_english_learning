package com.example.myapplication.main.age_group.from_5_to_7.singular_plural.choose_form

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_5_to_7.singular_plural.choose_form.view_model.ChooseSingularPluralFormViewModel
import com.example.myapplication.main.common.ActivityCompletePopup
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.ColoredFeedbackView
import com.example.myapplication.main.common.CountdownBadge
import com.example.myapplication.main.common.buttons.KidsOptionButton
import com.example.myapplication.main.common.getImageResFromWord
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens40
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.grammarBasicOptionsHeight
import com.example.myapplication.ui.theme.AppDimens.grammarBasicOptionsWidth
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.buttons.KidsLabel

@Composable
fun ChooseSingularPluralFormPage(
    navController: NavController,
    viewModel: ChooseSingularPluralFormViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val density = LocalDensity.current
    var outerColumnHeightPx by remember { mutableIntStateOf(0) }

    // Image size per count: fewer = bigger

    Box(Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.mintLime, shape = KidsFloatingShape.musicNotes)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .onGloballyPositioned { outerColumnHeightPx = it.size.height },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // ── Header ────────────────────────────────────────────────────────
            Row(verticalAlignment = Alignment.CenterVertically) {
                BackButtonWithText(
                    title = stringResource(R.string.choose_correct_form_),
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
                    verticalArrangement = Arrangement.spacedBy(Dimens24)
                ) {
                    // ── Image Card ────────────────────────────────────────────────
                    Box(
                        modifier = Modifier
                            .height(with(density) { (outerColumnHeightPx * 0.3f).toDp() })
                            .shadow(8.dp, RoundedCornerShape(Dimens24))
                            .background(Color.White, RoundedCornerShape(Dimens24))
                            .border(2.dp, Color(0xFF6A5AE0).copy(alpha = 0.25f), RoundedCornerShape(Dimens24))
                            .padding(horizontal = Dimens40, vertical = Dimens24),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedContent(
                            targetState = uiState.currentCount,
                            transitionSpec = {
                                (fadeIn(tween(300)) + scaleIn(tween(300), initialScale = 0.9f))
                                    .togetherWith(fadeOut(tween(200)))
                            },
                            label = "imageCount"
                        ) { count ->
                            getImageResFromWord(uiState.currentImageName)?.let { imageRes ->
                                if (count == 1) {
                                    if (imageRes != 0) {
                                        Image(
                                            painter = painterResource(imageRes),
                                            contentDescription = uiState.currentImageName,
                                            contentScale = ContentScale.Fit,
                                            modifier = Modifier.fillMaxHeight().aspectRatio(1f)
                                        )
                                    } else {
                                        ImagePlaceholder(uiState.currentImageName, modifier = Modifier.fillMaxHeight().aspectRatio(1f))
                                    }
                                } else {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(Dimens16),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        repeat(count) {
                                            if (imageRes != 0) {
                                                Image(
                                                    painter = painterResource(imageRes),
                                                    contentDescription = uiState.currentImageName,
                                                    contentScale = ContentScale.Fit,
                                                    modifier = Modifier.fillMaxHeight().aspectRatio(1f)
                                                )
                                            } else {
                                                ImagePlaceholder(uiState.currentImageName, modifier = Modifier.fillMaxHeight().aspectRatio(1f))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Text(
                        text = if (uiState.currentCount == 1) "What is this called?"
                               else "What are these called?",
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

// ── Placeholder when drawable not found ───────────────────────────────────────

@Composable
private fun ImagePlaceholder(name: String, modifier: Modifier) {
    Box(
        modifier = modifier
            .background(Color(0xFFF3F0FF), RoundedCornerShape(Dimens8)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.take(1).uppercase(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6A5AE0)
        )
    }
}
