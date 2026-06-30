package com.example.myapplication.main.age_group.phonics.l7_beginning_blends

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.phonics.l7_beginning_blends.view_model.BeginningBlendsPracticeViewModel
import com.example.myapplication.main.age_group.phonics.l7_beginning_blends.view_model.BlendPracticeQuestion
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.kidsGlassCapsule
import com.example.myapplication.main.common.kidsGlassCard
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
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun BeginningBlendsPracticePage(
    navController: NavController,
    viewModel: BeginningBlendsPracticeViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState
    val question = viewModel.currentQuestion

    DisposableEffect(Unit) { onDispose { viewModel.stop() } }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.peachCoral, shape = KidsFloatingShape.stars)

        if (uiState.isFinished) {
            FinishedView(
                score = uiState.score,
                total = viewModel.totalQuestions,
                onRestart = { viewModel.restart() }
            )
        } else if (question != null) {
            AnimatedContent(
                targetState = uiState.currentIndex,
                transitionSpec = {
                    fadeIn(spring()) togetherWith fadeOut(spring())
                },
                label = "practiceQuestion"
            ) { _ ->
                BoxWithConstraints(
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.safeDrawing)
                        .fillMaxSize()
                ) {
                    val screenH = maxHeight
                    Row(modifier = Modifier.fillMaxSize()) {
                        // Left 45%
                        LeftInfoPanel(
                            question = question,
                            uiState = uiState,
                            total = viewModel.totalQuestions,
                            onBack = { navController.popBackStack() },
                            modifier = Modifier
                                .weight(0.45f)
                                .fillMaxHeight()
                        )
                        // Right 55%
                        RightAnswerPanel(
                            question = question,
                            uiState = uiState,
                            screenH = screenH,
                            onAnswerTap = { viewModel.onAnswerTap(it) },
                            modifier = Modifier
                                .weight(0.55f)
                                .fillMaxHeight()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LeftInfoPanel(
    question: BlendPracticeQuestion,
    uiState: com.example.myapplication.main.age_group.phonics.l7_beginning_blends.view_model.BlendPracticeUiState,
    total: Int,
    onBack: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        BackButtonWithText(title = "Beginning Blends", onBackClick = onBack)

        Spacer(modifier = Modifier.weight(1f))

        Column(
            verticalArrangement = Arrangement.spacedBy(Dimens16),
            modifier = Modifier.padding(horizontal = Dimens20)
        ) {
            // Progress bar
            ProgressBar(currentIndex = uiState.currentIndex, total = total)

            // Instruction card
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens10),
                modifier = Modifier
                    .fillMaxWidth()
                    .kidsGlassCard(cornerRadius = Dimens12, strokeColor = Color(0xFF1A237E))
                    .padding(Dimens16)
            ) {
                Text(
                    text = "Complete the Blend",
                    style = MaterialTheme.typography.titleLarge.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A237E)
                )
                Text(
                    text = "Which two letters start this word?",
                    style = MaterialTheme.typography.bodyMedium.scaled(),
                    color = Color(0xFF546E7A),
                    textAlign = TextAlign.Center
                )
            }

            // Partial word display with shake
            PartialWordDisplay(question = question, uiState = uiState)

            // Score chip
            ScoreChip(score = uiState.score)
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun ProgressBar(currentIndex: Int, total: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(Dimens4)) {
        Text(
            text = "Question ${currentIndex + 1} of $total",
            style = MaterialTheme.typography.labelMedium.scaled(),
            color = Color(0xFF546E7A)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens8)
                .clip(RoundedCornerShape(Dimens4))
                .background(Color.White.copy(alpha = 0.4f))
        ) {
            val progress = (currentIndex + 1).toFloat() / total.toFloat()
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(Dimens4))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF42A5F5), Color(0xFF1565C0))
                        )
                    )
            )
        }
    }
}

@Composable
private fun PartialWordDisplay(
    question: BlendPracticeQuestion,
    uiState: com.example.myapplication.main.age_group.phonics.l7_beginning_blends.view_model.BlendPracticeUiState
) {
    val isCorrect = uiState.isCorrect
    val answer = uiState.selectedAnswer
    val shakeOffset = if (uiState.shakeWrong) {
        (sin(System.currentTimeMillis() / 50.0) * 7).roundToInt()
    } else 0

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = Dimens12, strokeColor = Color(0xFF1A237E))
            .padding(Dimens16),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens4)
        ) {
            // Blank / answer box
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .offset { IntOffset(shakeOffset, 0) }
                    .background(
                        color = when {
                            isCorrect == true  -> Color(0xFFC8E6C9)
                            isCorrect == false -> Color(0xFFFFCDD2)
                            else               -> Color(0xFFECEFF1)
                        },
                        shape = RoundedCornerShape(Dimens8)
                    )
                    .padding(horizontal = Dimens8, vertical = Dimens4)
            ) {
                Text(
                    text = answer ?: "___",
                    style = MaterialTheme.typography.displaySmall.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = when {
                        isCorrect == true  -> Color(0xFF2E7D32)
                        isCorrect == false -> Color(0xFFC62828)
                        else               -> Color(0xFF90A4AE)
                    }
                )
            }
            // Rest of word
            Text(
                text = question.word.drop(question.blendLength),
                style = MaterialTheme.typography.displaySmall.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF263238)
            )
        }
    }
}

@Composable
private fun ScoreChip(score: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens6),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .kidsGlassCapsule(strokeColor = Color(0xFF1A237E))
            .padding(horizontal = Dimens14, vertical = Dimens6)
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = Color(0xFFF9A825),
            modifier = Modifier.size(Dimens16)
        )
        Text(
            text = "Score: $score",
            style = MaterialTheme.typography.bodyMedium.scaled(),
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A237E)
        )
    }
}

@Composable
private fun RightAnswerPanel(
    question: BlendPracticeQuestion,
    uiState: com.example.myapplication.main.age_group.phonics.l7_beginning_blends.view_model.BlendPracticeUiState,
    screenH: androidx.compose.ui.unit.Dp,
    onAnswerTap: (String) -> Unit,
    modifier: Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens20),
        modifier = modifier.padding(vertical = Dimens20)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Image card
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(horizontal = Dimens20)
                .fillMaxWidth()
                .height(screenH * 0.38f)
                .kidsGlassCard(cornerRadius = Dimens20, strokeColor = Color(0xFF1A237E))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens8)
            ) {
                val ctx = androidx.compose.ui.platform.LocalContext.current
                val imgId = ctx.resources.getIdentifier(question.imageName, "drawable", ctx.packageName)

                if (imgId != 0) {
                    Image(
                        painter = painterResource(id = imgId),
                        contentDescription = question.imageName,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(top = Dimens10)
                    )
                } else {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text(
                            text = question.word,
                            style = MaterialTheme.typography.displayMedium.scaled(),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A237E)
                        )
                    }
                }

                Text(
                    text = question.word.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.labelMedium.scaled(),
                    color = Color(0xFF78909C),
                    modifier = Modifier.padding(bottom = Dimens8)
                )
            }
        }

        // 3 blend option buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens12),
            modifier = Modifier.padding(horizontal = Dimens20)
        ) {
            question.options.forEach { option ->
                BlendOptionButton(
                    option = option,
                    question = question,
                    uiState = uiState,
                    onTap = { onAnswerTap(option) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun BlendOptionButton(
    option: String,
    question: BlendPracticeQuestion,
    uiState: com.example.myapplication.main.age_group.phonics.l7_beginning_blends.view_model.BlendPracticeUiState,
    onTap: () -> Unit,
    modifier: Modifier
) {
    val answered = uiState.selectedAnswer != null
    val selected = uiState.selectedAnswer == option
    val isCorrect = option == question.correctBlend

    val fillColor = when {
        !answered -> Color.Transparent
        selected && isCorrect -> Color(0xFFC8E6C9)
        selected && !isCorrect -> Color(0xFFFFCDD2)
        isCorrect -> Color(0xFFC8E6C9)
        else -> Color.White.copy(alpha = 0.60f)
    }
    val borderColor = when {
        !answered -> Color(0xFFB0BEC5)
        isCorrect -> Color(0xFF2E7D32)
        selected -> Color(0xFFC62828)
        else -> Color(0xFFB0BEC5).copy(alpha = 0.5f)
    }
    val textColor = when {
        !answered -> Color(0xFF263238)
        isCorrect -> Color(0xFF1B5E20)
        selected -> Color(0xFFB71C1C)
        else -> Color(0xFF90A4AE)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens4),
        modifier = modifier
            .scale(if (selected) 1.03f else 1.0f)
            .kidsGlassCard(
                cornerRadius = Dimens12,
                strokeColor = if (answered) borderColor else Color(0xFF1A237E).copy(alpha = 0.4f)
            )
            .background(fillColor, RoundedCornerShape(Dimens12))
            .border(2.dp, borderColor, RoundedCornerShape(Dimens12))
            .then(if (!answered) Modifier.clickable { onTap() } else Modifier)
            .padding(vertical = Dimens16, horizontal = Dimens8)
    ) {
        Text(
            text = option.uppercase(),
            style = MaterialTheme.typography.titleLarge.scaled(),
            fontWeight = FontWeight.Bold,
            color = textColor
        )
        if (answered && isCorrect) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF2E7D32),
                modifier = Modifier.size(Dimens20)
            )
        } else if (answered && selected && !isCorrect) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFFC62828),
                modifier = Modifier.size(Dimens20)
            )
        }
    }
}

@Composable
private fun FinishedView(score: Int, total: Int, onRestart: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens24),
            modifier = Modifier
                .kidsGlassCard(cornerRadius = Dimens20, strokeColor = Color(0xFF1A237E))
                .padding(Dimens32)
        ) {
            Image(
                painter = painterResource(R.drawable._mascot_),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.height(160.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens8)
            ) {
                Text(
                    text = if (score >= total / 2) "Well done! 🎉" else "Good try! 💪",
                    style = MaterialTheme.typography.displaySmall.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A237E)
                )
                Text(
                    text = "You got $score out of $total",
                    style = MaterialTheme.typography.titleMedium.scaled(),
                    color = Color(0xFF546E7A)
                )
            }
            KidsActionButton(
                text = "Try Again",
                icon = Icons.Default.ArrowBack,
                type = ButtonType.BLUE,
                isIconStart = true,
                isSmall = true,
                onClick = onRestart
            )
        }
    }
}

