package com.example.myapplication.main.age_group.phonics.l8_vowel_teams

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.l8_vowel_teams.view_model.VowelTeamPracticeQuestion
import com.example.myapplication.main.age_group.phonics.l8_vowel_teams.view_model.VowelTeamsPracticeViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens14
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens32
import com.example.myapplication.ui.theme.AppDimens.Dimens40
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled

private val accentColor = Color(0xFFEF6C00)

@Composable
fun VowelTeamsPracticePage(
    navController: NavController,
    viewModel: VowelTeamsPracticeViewModel = hiltViewModel()
) {
    DisposableEffect(Unit) { onDispose { viewModel.stop() } }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.peachYellow, shape = KidsFloatingShape.stars)

        Box(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            if (viewModel.uiState.isFinished) {
                finishedView(viewModel = viewModel, navController = navController)
            } else {
                val question = viewModel.currentQuestion
                if (question != null) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        leftPanel(
                            modifier = Modifier.weight(0.45f).fillMaxHeight(),
                            question = question,
                            viewModel = viewModel,
                            navController = navController
                        )
                        rightPanel(
                            modifier = Modifier.weight(0.55f).fillMaxHeight(),
                            question = question,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun leftPanel(
    modifier: Modifier,
    question: VowelTeamPracticeQuestion,
    viewModel: VowelTeamsPracticeViewModel,
    navController: NavController
) {
    Column(modifier = modifier) {
        BackButtonWithText(title = "Vowel Teams", onBackClick = { navController.popBackStack() })
        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier.padding(horizontal = Dimens20),
            verticalArrangement = Arrangement.spacedBy(Dimens16)
        ) {
            // Progress
            val progress by animateFloatAsState(
                targetValue = (viewModel.uiState.currentIndex + 1f) / viewModel.totalQuestions,
                animationSpec = tween(500), label = "progress"
            )
            Column(verticalArrangement = Arrangement.spacedBy(Dimens4)) {
                Text(
                    text = "Question ${viewModel.uiState.currentIndex + 1} of ${viewModel.totalQuestions}",
                    style = MaterialTheme.typography.labelLarge.scaled(),
                    color = Color(0xFF546E7A)
                )
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(Dimens8),
                    color = accentColor,
                    trackColor = Color.White.copy(alpha = 0.4f)
                )
            }

            // Instruction card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(Dimens12))
                    .background(Color.White, RoundedCornerShape(Dimens12))
                    .padding(Dimens16),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens10)
            ) {
                Text(
                    text = "Pick the Vowel Team",
                    style = MaterialTheme.typography.titleLarge.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
                Text(
                    text = "Which vowel team completes this word?",
                    style = MaterialTheme.typography.bodyMedium.scaled(),
                    color = Color(0xFF546E7A),
                    textAlign = TextAlign.Center
                )
            }

            // Word display with blank
            wordDisplay(question = question, viewModel = viewModel)

            // Score
            Box(
                modifier = Modifier
                    .shadow(2.dp, RoundedCornerShape(Dimens20))
                    .background(Color.White, RoundedCornerShape(Dimens20))
                    .padding(horizontal = Dimens14, vertical = Dimens6)
            ) {
                Text(
                    text = "⭐ Score: ${viewModel.uiState.score}",
                    style = MaterialTheme.typography.titleSmall.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun wordDisplay(question: VowelTeamPracticeQuestion, viewModel: VowelTeamsPracticeViewModel) {
    val answer = viewModel.uiState.selectedAnswer
    val isCorrect = viewModel.uiState.isCorrect
    val stem = question.word.removeRange(question.teamStart, question.teamStart + question.teamLength)
    val prefix = stem.take(question.teamStart)
    val suffix = stem.drop(question.teamStart)

    val shakeOffset by animateFloatAsState(
        targetValue = if (viewModel.uiState.shakeWrong) 10f else 0f,
        animationSpec = spring(stiffness = 1000f), label = "shake"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(Dimens12))
            .background(Color.White, RoundedCornerShape(Dimens12))
            .padding(Dimens16)
            .graphicsLayer { translationX = shakeOffset },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = Color(0xFF263238), fontWeight = FontWeight.Bold)) {
                    append(prefix)
                }
                withStyle(SpanStyle(
                    color = when (isCorrect) {
                        true  -> Color(0xFF2E7D32)
                        false -> Color(0xFFC62828)
                        null  -> Color(0xFF90A4AE)
                    },
                    fontWeight = FontWeight.Bold,
                    background = when (isCorrect) {
                        true  -> Color(0xFFC8E6C9)
                        false -> Color(0xFFFFCDD2)
                        null  -> Color(0xFFECEFF1)
                    }
                )) {
                    append(answer ?: "___")
                }
                withStyle(SpanStyle(color = Color(0xFF263238), fontWeight = FontWeight.Bold)) {
                    append(suffix)
                }
            },
            style = MaterialTheme.typography.displaySmall.scaled()
        )
    }
}

@Composable
private fun rightPanel(
    modifier: Modifier,
    question: VowelTeamPracticeQuestion,
    viewModel: VowelTeamsPracticeViewModel
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Large word card with blank
        Box(
            modifier = Modifier
                .padding(horizontal = Dimens20)
                .fillMaxWidth()
                .shadow(6.dp, RoundedCornerShape(Dimens20))
                .background(Color.White, RoundedCornerShape(Dimens20))
                .padding(Dimens24),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens8)
            ) {
                Text(
                    text = buildAnnotatedString {
                        append(question.word.take(question.teamStart))
                        withStyle(SpanStyle(color = accentColor, fontWeight = FontWeight.ExtraBold)) {
                            append("___")
                        }
                        append(question.word.drop(question.teamStart + question.teamLength))
                    },
                    style = MaterialTheme.typography.displaySmall.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF263238)
                )
                Text(
                    text = "What vowel team fits here?",
                    style = MaterialTheme.typography.labelLarge.scaled(),
                    color = Color(0xFF78909C)
                )
            }
        }

        Spacer(modifier = Modifier.height(Dimens20))

        // Option buttons
        Row(
            modifier = Modifier.padding(horizontal = Dimens20),
            horizontalArrangement = Arrangement.spacedBy(Dimens12)
        ) {
            question.options.forEach { option ->
                optionButton(
                    modifier = Modifier.weight(1f),
                    option = option,
                    question = question,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
private fun optionButton(
    modifier: Modifier,
    option: String,
    question: VowelTeamPracticeQuestion,
    viewModel: VowelTeamsPracticeViewModel
) {
    val selected  = viewModel.uiState.selectedAnswer == option
    val isCorrect = option == question.correctTeam
    val answered  = viewModel.uiState.selectedAnswer != null

    val bgColor = when {
        !answered               -> Color.White
        selected && isCorrect   -> Color(0xFFC8E6C9)
        selected && !isCorrect  -> Color(0xFFFFCDD2)
        isCorrect               -> Color(0xFFC8E6C9)
        else                    -> Color.White.copy(alpha = 0.6f)
    }
    val borderColor = when {
        !answered  -> accentColor.copy(alpha = 0.4f)
        isCorrect  -> Color(0xFF2E7D32)
        selected   -> Color(0xFFC62828)
        else       -> Color(0xFFB0BEC5).copy(alpha = 0.5f)
    }
    val textColor = when {
        !answered               -> Color(0xFF263238)
        isCorrect               -> Color(0xFF1B5E20)
        selected && !isCorrect  -> Color(0xFFB71C1C)
        else                    -> Color(0xFF90A4AE)
    }

    Box(
        modifier = modifier
            .shadow(if (selected) 6.dp else 2.dp, RoundedCornerShape(Dimens12))
            .background(bgColor, RoundedCornerShape(Dimens12))
            .border(2.dp, borderColor, RoundedCornerShape(Dimens12))
            .then(if (!answered) Modifier.clickable { viewModel.onAnswerTap(option) } else Modifier)
            .padding(vertical = Dimens16),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(Dimens4)) {
            Text(
                text = option.uppercase(),
                style = MaterialTheme.typography.headlineSmall.scaled(),
                fontWeight = FontWeight.ExtraBold,
                color = textColor
            )
            if (answered && isCorrect) {
                Text("✓", style = MaterialTheme.typography.titleMedium.scaled(), color = Color(0xFF2E7D32))
            } else if (answered && selected && !isCorrect) {
                Text("✗", style = MaterialTheme.typography.titleMedium.scaled(), color = Color(0xFFC62828))
            }
        }
    }
}

@Composable
private fun finishedView(viewModel: VowelTeamsPracticeViewModel, navController: NavController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .padding(Dimens40)
                .shadow(8.dp, RoundedCornerShape(Dimens20))
                .background(Color.White, RoundedCornerShape(Dimens20))
                .padding(Dimens32),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens24)
        ) {
            Text("🤝", style = MaterialTheme.typography.displayLarge.scaled())

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens8)
            ) {
                Text(
                    text = if (viewModel.uiState.score >= viewModel.totalQuestions / 2) "Well done! 🎉" else "Good try! 💪",
                    style = MaterialTheme.typography.headlineMedium.scaled(),
                    fontWeight = FontWeight.ExtraBold,
                    color = accentColor
                )
                Text(
                    text = "You got ${viewModel.uiState.score} out of ${viewModel.totalQuestions}",
                    style = MaterialTheme.typography.titleLarge.scaled(),
                    color = Color(0xFF546E7A)
                )
            }

            KidsActionButton(
                text = "Try Again",
                type = ButtonType.ORANGE,
                isSmall = true,
                onClick = { viewModel.restart() }
            )
        }
    }
}
