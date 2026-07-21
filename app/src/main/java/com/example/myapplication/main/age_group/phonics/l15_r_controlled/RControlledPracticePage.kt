package com.example.myapplication.main.age_group.phonics.l15_r_controlled

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.l15_r_controlled.view_model.RControlledPracticeQuestion
import com.example.myapplication.main.age_group.phonics.l15_r_controlled.view_model.RControlledPracticeUiState
import com.example.myapplication.main.age_group.phonics.l15_r_controlled.view_model.RControlledPracticeViewModel
import com.example.myapplication.main.age_group.phonics.l15_r_controlled.view_model.rControlledGroups
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
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
import com.example.myapplication.utils.extensions.scaled
import androidx.compose.ui.draw.clip

private val practiceAccentColor = Color(0xFF2E7D32)
private val bossyRColor = Color(0xFFC62828)

private fun groupForRTeam(rTeam: String) =
    rControlledGroups.find { it.rTeam == rTeam }

@Composable
fun RControlledPracticePage(navController: NavController) {
    val viewModel: RControlledPracticeViewModel = hiltViewModel()
    val uiState = viewModel.uiState
    val question = viewModel.currentQuestion

    DisposableEffect(Unit) { onDispose { viewModel.stop() } }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.meadowGreen, shape = KidsFloatingShape.stars)

        if (uiState.isFinished) {
            RControlledPracticeFinishedView(
                score = uiState.score,
                total = viewModel.totalQuestions,
                onRestart = { viewModel.restart() }
            )
        } else if (question != null) {
            Box(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .fillMaxSize()
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    RControlledPracticeLeftPanel(
                        question = question,
                        uiState = uiState,
                        total = viewModel.totalQuestions,
                        onBack = { navController.popBackStack() },
                        modifier = Modifier
                            .weight(0.45f)
                            .fillMaxHeight()
                    )
                    RControlledPracticeRightPanel(
                        question = question,
                        uiState = uiState,
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

// ── Left Panel ────────────────────────────────────────────────────────────────

@Composable
private fun RControlledPracticeLeftPanel(
    question: RControlledPracticeQuestion,
    uiState: RControlledPracticeUiState,
    total: Int,
    onBack: () -> Unit,
    modifier: Modifier
) {
    Column(modifier = modifier) {
        BackButtonWithText(title = "R-Controlled", onBackClick = onBack)

        Spacer(modifier = Modifier.weight(1f))

        Column(
            verticalArrangement = Arrangement.spacedBy(Dimens16),
            modifier = Modifier.padding(horizontal = Dimens20)
        ) {
            // Progress
            Column(verticalArrangement = Arrangement.spacedBy(Dimens4)) {
                Text(
                    text = "Question ${uiState.currentIndex + 1} of $total",
                    style = MaterialTheme.typography.labelMedium.scaled(),
                    color = Color(0xFF546E7A)
                )
                LinearProgressIndicator(
                    progress = (uiState.currentIndex + 1).toFloat() / total.toFloat(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens8),
                    color = practiceAccentColor,
                    trackColor = Color.White.copy(alpha = 0.4f),
                    strokeCap = StrokeCap.Round
                )
            }

            // Instruction card
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens8),
                modifier = Modifier
                    .fillMaxWidth()
                    .kidsGlassCard(cornerRadius = Dimens12, strokeColor = practiceAccentColor)
                    .padding(Dimens16)
            ) {
                Text(
                    text = "Fill the R-Team! 👑",
                    style = MaterialTheme.typography.titleLarge.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = practiceAccentColor
                )
                Text(
                    text = "Which R-team fits in '${question.displayWord}'?",
                    style = MaterialTheme.typography.bodyMedium.scaled(),
                    color = Color(0xFF546E7A)
                )
            }

            // Word display
            val shakeScale by animateFloatAsState(
                targetValue = if (uiState.shakeWrong) 0.94f else 1f,
                animationSpec = spring(dampingRatio = 0.2f, stiffness = 600f),
                label = "shakeScale"
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(shakeScale)
                    .kidsGlassCard(cornerRadius = Dimens12, strokeColor = practiceAccentColor)
                    .padding(Dimens16)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens10),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(practiceAccentColor.copy(alpha = 0.10f), RoundedCornerShape(Dimens8))
                            .padding(horizontal = Dimens10, vertical = Dimens6)
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                val parts = question.displayWord.split("__")
                                withStyle(SpanStyle(color = Color(0xFF455A64))) {
                                    append(parts.getOrElse(0) { "" })
                                }
                                withStyle(SpanStyle(color = practiceAccentColor, fontWeight = FontWeight.ExtraBold)) {
                                    append("__")
                                }
                                withStyle(SpanStyle(color = Color(0xFF455A64))) {
                                    append(parts.getOrElse(1) { "" })
                                }
                            },
                            style = MaterialTheme.typography.titleLarge.scaled(),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Answer slot
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .background(
                                color = when {
                                    uiState.isCorrect == true  -> Color(0xFFC8E6C9)
                                    uiState.isCorrect == false -> Color(0xFFFFCDD2)
                                    else                       -> Color(0xFFECEFF1)
                                },
                                shape = RoundedCornerShape(Dimens8)
                            )
                            .padding(horizontal = Dimens10, vertical = Dimens6)
                    ) {
                        Text(
                            text = uiState.selectedAnswer ?: "?",
                            style = MaterialTheme.typography.titleLarge.scaled(),
                            fontWeight = FontWeight.Bold,
                            color = when {
                                uiState.isCorrect == true  -> Color(0xFF2E7D32)
                                uiState.isCorrect == false -> Color(0xFFC62828)
                                else                       -> Color(0xFF90A4AE)
                            }
                        )
                    }
                }
            }

            // Score row
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens6),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFF9A825),
                    modifier = Modifier.size(Dimens16)
                )
                Text(
                    text = "Score: ${uiState.score}",
                    style = MaterialTheme.typography.bodyMedium.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = practiceAccentColor
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

// ── Right Panel ───────────────────────────────────────────────────────────────

@Composable
private fun RControlledPracticeRightPanel(
    question: RControlledPracticeQuestion,
    uiState: RControlledPracticeUiState,
    onAnswerTap: (String) -> Unit,
    modifier: Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens14),
        modifier = modifier.padding(vertical = Dimens20, horizontal = Dimens16)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        question.options.forEach { option ->
            RControlledOptionTile(
                option = option,
                correctRTeam = question.rTeam,
                uiState = uiState,
                onTap = { onAnswerTap(option) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

// ── Option Tile ───────────────────────────────────────────────────────────────

@Composable
private fun RControlledOptionTile(
    option: String,
    correctRTeam: String,
    uiState: RControlledPracticeUiState,
    onTap: () -> Unit,
    modifier: Modifier
) {
    val answered = uiState.selectedAnswer != null
    val selected = uiState.selectedAnswer == option
    val isCorrect = option == correctRTeam
    val group = groupForRTeam(option)
    val accentColor = group?.accentColor ?: practiceAccentColor

    val bg = when {
        answered && selected && isCorrect ->
            androidx.compose.ui.graphics.Brush.linearGradient(listOf(Color(0xFF43A047), Color(0xFF2E7D32)))
        answered && selected && !isCorrect ->
            androidx.compose.ui.graphics.Brush.linearGradient(listOf(Color(0xFFEF9A9A), Color(0xFFEF9A9A)))
        answered && isCorrect ->
            androidx.compose.ui.graphics.Brush.linearGradient(listOf(Color(0xFFC8E6C9), Color(0xFFC8E6C9)))
        else ->
            androidx.compose.ui.graphics.Brush.linearGradient(listOf(Color.White.copy(0.85f), Color.White.copy(0.85f)))
    }

    val textColor = when {
        answered && selected && isCorrect -> Color.White
        answered && selected && !isCorrect -> Color(0xFFC62828)
        answered && isCorrect -> Color(0xFF1B5E20)
        else -> accentColor
    }

    Box(
        modifier = modifier
            .scale(if (selected) 1.03f else 1f)
            .shadow(if (selected && isCorrect) 8.dp else 2.dp, RoundedCornerShape(Dimens12))
            .background(bg, RoundedCornerShape(Dimens12))
            .then(if (!answered) Modifier.clickable { onTap() } else Modifier)
            .padding(horizontal = Dimens16, vertical = Dimens20),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens6)
        ) {
            // rTeam display: vowelChar(accentColor) + r(red)
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = textColor, fontWeight = FontWeight.ExtraBold)) {
                        append(option.first().toString())
                    }
                    withStyle(SpanStyle(
                        color = if (answered) textColor else bossyRColor,
                        fontWeight = FontWeight.ExtraBold
                    )) {
                        append("r")
                    }
                },
                style = MaterialTheme.typography.headlineMedium.scaled(),
                fontWeight = FontWeight.ExtraBold
            )
            if (!answered) {
                Text(
                    text = group?.sound ?: "",
                    style = MaterialTheme.typography.labelMedium.scaled(),
                    color = textColor.copy(alpha = 0.65f)
                )
                Text(
                    text = "_ $option",
                    style = MaterialTheme.typography.labelSmall.scaled(),
                    color = textColor.copy(alpha = 0.50f)
                )
            }
        }
    }
}

// ── Finished View ─────────────────────────────────────────────────────────────

@Composable
private fun RControlledPracticeFinishedView(score: Int, total: Int, onRestart: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens24),
            modifier = Modifier
                .kidsGlassCard(cornerRadius = Dimens20, strokeColor = practiceAccentColor)
                .padding(Dimens24)
        ) {
            Text(
                text = if (score >= total / 2) "Well done! 🎉" else "Good try! 💪",
                style = MaterialTheme.typography.headlineMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = practiceAccentColor
            )
            Text(
                text = "You got $score out of $total",
                style = MaterialTheme.typography.titleMedium.scaled(),
                color = Color(0xFF546E7A)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens8),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(practiceAccentColor, RoundedCornerShape(50))
                    .clip(RoundedCornerShape(50))
                    .clickable { onRestart() }
                    .padding(horizontal = Dimens24, vertical = Dimens12)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(Dimens20)
                )
                Text(
                    text = "Try Again",
                    style = MaterialTheme.typography.titleMedium.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
