package com.example.myapplication.main.age_group.phonics.l21_word_endings

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.l21_word_endings.view_model.WordEndingsPracticeQuestion
import com.example.myapplication.main.age_group.phonics.l21_word_endings.view_model.WordEndingsPracticeUiState
import com.example.myapplication.main.age_group.phonics.l21_word_endings.view_model.WordEndingsPracticeViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.kidsGlassCard
import com.example.myapplication.main.common.kidsGlassCapsule
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
import com.example.myapplication.utils.extensions.scaled
import kotlin.math.roundToInt

private val weGreen = Color(0xFF2E7D32)
private val weDark  = Color(0xFF1B5E20)
private val weLight = Color(0xFF43A047)

@Composable
fun WordEndingsPracticePage(
    navController: NavController,
    viewModel: WordEndingsPracticeViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.meadowGreen, shape = KidsFloatingShape.leaves)

        if (uiState.isFinished) {
            WEPFinishedView(score = uiState.score, total = viewModel.totalQuestions) { viewModel.restart() }
        } else {
            val question = viewModel.currentQuestion ?: return@Box

            Row(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .fillMaxSize()
            ) {
                // ── LEFT 46% ──────────────────────────────────────────────────
                Column(modifier = Modifier.fillMaxWidth(0.46f).fillMaxHeight()) {
                    BackButtonWithText(title = "Word Endings Practice", onBackClick = { navController.popBackStack() })
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        modifier = Modifier.padding(horizontal = Dimens20),
                        verticalArrangement = Arrangement.spacedBy(Dimens16)
                    ) {
                        WEPDotProgress(current = uiState.currentIndex, total = viewModel.totalQuestions)
                        WEPInstructionCard()
                        WEPWordFormula(question = question, uiState = uiState)
                        WEPScoreView(score = uiState.score)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }

                // ── RIGHT 54% ─────────────────────────────────────────────────
                AnimatedContent(
                    targetState  = uiState.currentIndex,
                    transitionSpec = { fadeIn(spring()) togetherWith fadeOut(spring()) },
                    label        = "wePractice",
                    modifier     = Modifier.weight(1f).fillMaxHeight()
                ) { _ ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxHeight().padding(horizontal = Dimens20)
                    ) {
                        LazyVerticalGrid(
                            columns               = GridCells.Fixed(2),
                            contentPadding        = PaddingValues(vertical = Dimens8),
                            horizontalArrangement = Arrangement.spacedBy(Dimens12),
                            verticalArrangement   = Arrangement.spacedBy(Dimens12)
                        ) {
                            items(question.options) { option ->
                                WEPSpellingTile(
                                    option   = option,
                                    question = question,
                                    uiState  = uiState,
                                    onClick  = { viewModel.onAnswerTap(option) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── Dot progress ──────────────────────────────────────────────────────────────

@Composable
private fun WEPDotProgress(current: Int, total: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(Dimens6)) {
        Text(
            text  = "Question ${current + 1} of $total",
            style = MaterialTheme.typography.labelMedium.scaled(),
            color = Color(0xFF546E7A)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens4)) {
            repeat(minOf(total, 17)) { i ->
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                i < current  -> weGreen
                                i == current -> weLight
                                else         -> weGreen.copy(alpha = 0.20f)
                            }
                        )
                )
            }
        }
    }
}

// ── Instruction card: 🔡 Spell it Right! ─────────────────────────────────────

@Composable
private fun WEPInstructionCard() {
    Row(
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens10),
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = 12.dp, strokeColor = weGreen)
            .padding(Dimens12)
    ) {
        Text(text = "🔡", style = MaterialTheme.typography.titleLarge.scaled())
        Column(verticalArrangement = Arrangement.spacedBy(Dimens4)) {
            Text(
                text       = "Spell it Right!",
                style      = MaterialTheme.typography.bodyMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color      = weGreen
            )
            Text(
                text  = "Choose the correct spelling!",
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = Color(0xFF546E7A)
            )
        }
    }
}

// ── Word formula: base [chip] + suffix [chip] = answer ────────────────────────

@Composable
private fun WEPWordFormula(question: WordEndingsPracticeQuestion, uiState: WordEndingsPracticeUiState) {
    val answered  = uiState.selectedAnswer != null
    val isCorrect = uiState.isCorrect == true

    Column(
        modifier            = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Dimens6)
    ) {
        Row(
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens6),
            modifier = Modifier
                .fillMaxWidth()
                .kidsGlassCard(
                    cornerRadius = 12.dp,
                    strokeColor  = if (!answered) weGreen else if (isCorrect) Color(0xFF388E3C) else Color(0xFFC62828)
                )
                .padding(Dimens12)
        ) {
            // base chip — ECEFF1 background
            Text(
                text       = question.base,
                style      = MaterialTheme.typography.titleMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color      = Color(0xFF455A64),
                modifier   = Modifier
                    .background(Color(0xFFECEFF1), RoundedCornerShape(6.dp))
                    .padding(horizontal = Dimens8, vertical = Dimens4)
            )
            Text(
                text       = "+",
                style      = MaterialTheme.typography.titleMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color      = Color(0xFF388E3C)
            )
            // suffix chip — green tinted background
            Text(
                text       = question.suffix,
                style      = MaterialTheme.typography.titleMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color      = weGreen,
                modifier   = Modifier
                    .background(weGreen.copy(alpha = 0.10f), RoundedCornerShape(6.dp))
                    .padding(horizontal = Dimens8, vertical = Dimens4)
            )
            Text(
                text       = "=",
                style      = MaterialTheme.typography.titleMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color      = Color(0xFF388E3C)
            )
            // Revealed answer or "?"
            Text(
                text       = if (answered) (uiState.selectedAnswer ?: "?") else "?",
                style      = MaterialTheme.typography.titleMedium.scaled(),
                fontWeight = FontWeight.ExtraBold,
                color      = when {
                    !answered -> weGreen
                    isCorrect -> Color(0xFF2E7D32)
                    else      -> Color(0xFFC62828)
                }
            )
        }
        // Show "✓ correctAnswer" below when wrong
        if (answered && !isCorrect) {
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens4),
                modifier              = Modifier.padding(start = Dimens4)
            ) {
                Icon(
                    Icons.Default.CheckCircle, null,
                    tint     = Color(0xFF2E7D32),
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text       = question.correct,
                    style      = MaterialTheme.typography.labelMedium.scaled(),
                    fontWeight = FontWeight.Bold,
                    color      = Color(0xFF2E7D32)
                )
            }
        }
    }
}

// ── Score: star + "Score: X" in green glass capsule ──────────────────────────

@Composable
private fun WEPScoreView(score: Int) {
    Row(
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens6),
        modifier = Modifier
            .kidsGlassCapsule(strokeColor = weGreen)
            .padding(horizontal = Dimens14, vertical = Dimens6)
    ) {
        Icon(Icons.Default.Star, null, tint = Color(0xFFFFD54F), modifier = Modifier.size(18.dp))
        Text(
            text       = "Score: $score",
            style      = MaterialTheme.typography.bodyMedium.scaled(),
            fontWeight = FontWeight.Bold,
            color      = weGreen
        )
    }
}

// ── Spelling tile (full word, shake on wrong) ─────────────────────────────────

@Composable
private fun WEPSpellingTile(
    option:   String,
    question: WordEndingsPracticeQuestion,
    uiState:  WordEndingsPracticeUiState,
    onClick:  () -> Unit
) {
    val answered  = uiState.selectedAnswer != null
    val selected  = uiState.selectedAnswer == option
    val isCorrect = option == question.correct
    val isWrong   = selected && !isCorrect
    val interactionSource = remember { MutableInteractionSource() }

    // Shake on wrong
    val offsetX = remember { Animatable(0f) }
    LaunchedEffect(uiState.shakeWrong) {
        if (uiState.shakeWrong && isWrong) {
            offsetX.animateTo(
                targetValue   = 0f,
                animationSpec = keyframes {
                    durationMillis = 500
                    0f   at 0
                    -10f at 60
                    10f  at 120
                    -8f  at 180
                    8f   at 240
                    -5f  at 320
                    5f   at 400
                    0f   at 500
                }
            )
        }
    }

    val fillColor = when {
        !answered              -> Color.White
        selected && isCorrect  -> Color(0xFFC8E6C9)
        selected && !isCorrect -> Color(0xFFFFCDD2)
        isCorrect              -> Color(0xFFC8E6C9)
        else                   -> Color.White.copy(alpha = 0.60f)
    }
    val borderColor = when {
        !answered -> weGreen.copy(alpha = 0.35f)
        isCorrect -> Color(0xFF2E7D32)
        selected  -> Color(0xFFC62828)
        else      -> Color(0xFFB0BEC5).copy(alpha = 0.50f)
    }
    val textColor = when {
        !answered -> weGreen
        isCorrect -> Color(0xFF1B5E20)
        selected  -> Color(0xFFB71C1C)
        else      -> weGreen.copy(alpha = 0.40f)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .height(130.dp)
            .fillMaxWidth()
            .offset { IntOffset(offsetX.value.roundToInt(), 0) }
            .scale(if (selected) 1.04f else 1.0f)
            .background(fillColor, RoundedCornerShape(12.dp))
            .kidsGlassCard(cornerRadius = 12.dp, strokeColor = borderColor)
            .border(
                width = if (selected || (isCorrect && answered)) 2.5.dp else 1.5.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(enabled = !answered, interactionSource = interactionSource, indication = null) { onClick() }
            .padding(Dimens8)
    ) {
        // Full word — large title font
        Text(
            text       = option,
            style      = MaterialTheme.typography.headlineMedium.scaled(),
            fontWeight = FontWeight.ExtraBold,
            color      = textColor
        )
        if (answered) {
            when {
                isCorrect -> Icon(
                    Icons.Default.CheckCircle, null,
                    tint     = Color(0xFF2E7D32),
                    modifier = Modifier.size(20.dp)
                )
                selected  -> Text(
                    text       = "✗",
                    style      = MaterialTheme.typography.titleMedium.scaled(),
                    color      = Color(0xFFC62828),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ── Finished screen: 🌿 "Spelling Star! ⭐" or "Keep growing! 🌱" ─────────────

@Composable
private fun WEPFinishedView(score: Int, total: Int, onRestart: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens24),
            modifier = Modifier
                .kidsGlassCard(cornerRadius = 20.dp, strokeColor = weGreen)
                .padding(Dimens32)
        ) {
            Text("🌿", style = MaterialTheme.typography.displayLarge.scaled())
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens4)
            ) {
                Text(
                    text       = if (score >= total * 3 / 4) "Spelling Star! ⭐" else "Keep growing! 🌱",
                    style      = MaterialTheme.typography.headlineMedium.scaled(),
                    fontWeight = FontWeight.ExtraBold,
                    color      = weGreen
                )
                Text(
                    text  = "You got $score out of $total",
                    style = MaterialTheme.typography.titleMedium.scaled(),
                    color = Color(0xFF546E7A)
                )
            }
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens8),
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Brush.linearGradient(listOf(weGreen, weDark)))
                    .clickable { onRestart() }
                    .padding(horizontal = Dimens24, vertical = Dimens12)
            ) {
                Icon(Icons.Default.Refresh, null, tint = Color.White, modifier = Modifier.size(18.dp))
                Text(
                    text       = "Try Again",
                    style      = MaterialTheme.typography.labelLarge.scaled(),
                    fontWeight = FontWeight.Bold,
                    color      = Color.White
                )
            }
        }
    }
}
