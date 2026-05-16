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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.from_5_to_7.singular_plural.choose_form.view_model.ChooseSingularPluralFormViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.ColoredFeedbackView
import com.example.myapplication.main.common.buttons.KidsOptionButton
import com.example.myapplication.main.common.getImageResFromWord
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.grammarBasicOptionsHeight
import com.example.myapplication.ui.theme.AppDimens.grammarBasicOptionsWidth
import com.example.myapplication.ui.theme.ButtonType

@Composable
fun ChooseSingularPluralFormPage(
    navController: NavController,
    viewModel: ChooseSingularPluralFormViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Image size per count: fewer = bigger
    val imageSize = when (uiState.currentCount) {
        1    -> 130.dp
        2    -> 100.dp
        3    -> 80.dp
        else -> 70.dp
    }

    Box(Modifier.fillMaxSize()) {
        BackgroundUI(isGreenGrassShow = false)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            // ── Header ────────────────────────────────────────────────────────
            Row(verticalAlignment = Alignment.CenterVertically) {
                BackButtonWithText(
                    title = "Choose Correct Form",
                    modifier = Modifier.weight(1f),
                    onBackClick = { navController.popBackStack() }
                )

                // Countdown: "Next image in 3"
                AnimatedVisibility(
                    visible = uiState.countdown != null,
                    enter = fadeIn() + scaleIn(initialScale = 0.8f),
                    exit  = fadeOut() + scaleOut(targetScale = 0.8f)
                ) {
                    uiState.countdown?.let { n ->
                        AnimatedContent(
                            targetState = n,
                            transitionSpec = {
                                (fadeIn(tween(200)) + scaleIn(tween(200), initialScale = 0.85f))
                                    .togetherWith(fadeOut(tween(150)) + scaleOut(tween(150), targetScale = 1.1f))
                            },
                            label = "countdown"
                        ) { count ->
                            Text(
                                text = "Next image in $count",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = when (count) {
                                    3    -> Color(0xFF2E7D32)
                                    2    -> Color(0xFFF57F17)
                                    else -> Color(0xFFC62828)
                                },
                                modifier = Modifier.padding(end = Dimens16, top = 8.dp)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens24)
            ) {
                // ── Image Card ────────────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(Dimens24))
                        .background(Color.White, RoundedCornerShape(Dimens24))
                        .border(2.dp, Color(0xFF6A5AE0).copy(alpha = 0.25f), RoundedCornerShape(Dimens24))
                        .padding(horizontal = 32.dp, vertical = Dimens24),
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
                                // Single image
                                if (imageRes != 0) {
                                    Image(
                                        painter = painterResource(imageRes),
                                        contentDescription = uiState.currentImageName,
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier.size(imageSize)
                                    )
                                } else {
                                    ImagePlaceholder(uiState.currentImageName, imageSize.value.toInt())
                                }
                            } else {
                                // Multiple images side by side
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
                                                modifier = Modifier.size(imageSize)
                                            )
                                        } else {
                                            ImagePlaceholder(uiState.currentImageName, imageSize.value.toInt())
                                        }
                                    }
                                }
                            }
                        }

                    }
                }

                // ── Prompt ────────────────────────────────────────────────────
                Text(
                    text = if (uiState.currentCount == 1) "What is this called?"
                           else "What are these called?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                // ── 2 Option Buttons ──────────────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimens16)
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

            Spacer(Modifier.weight(1f))

            // ── Feedback ──────────────────────────────────────────────────────
            ColoredFeedbackView(
                feedbackText    = uiState.feedbackText,
                isAnswerCorrect = uiState.isAnswerCorrect,
                correctAnswer   = uiState.correctAnswer
            )
            Spacer(Modifier.height(Dimens24))
        }
    }
}

// ── Placeholder when drawable not found ───────────────────────────────────────

@Composable
private fun ImagePlaceholder(name: String, sizeDp: Int) {
    Box(
        modifier = Modifier
            .size(sizeDp.dp)
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
