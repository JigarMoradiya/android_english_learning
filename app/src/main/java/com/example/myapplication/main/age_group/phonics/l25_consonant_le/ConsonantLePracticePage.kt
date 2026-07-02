package com.example.myapplication.main.age_group.phonics.l25_consonant_le

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.l25_consonant_le.view_model.CLEPracticeQuestion
import com.example.myapplication.main.age_group.phonics.l25_consonant_le.view_model.CLEPracticeUiState
import com.example.myapplication.main.age_group.phonics.l25_consonant_le.view_model.ConsonantLePracticeViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.kidsGlassCard
import com.example.myapplication.ui.theme.AppDimens.Dimens2
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

private val cleAccent = Color(0xFFAD1457)
private val cleShadow = Color(0xFF880E4F)

@Composable
fun ConsonantLePracticePage(
    navController: NavController,
    viewModel: ConsonantLePracticeViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.rosePink, shape = KidsFloatingShape.bubbles)

        if (uiState.isFinished) {
            CLEFinishedView(score = uiState.score, total = viewModel.totalQuestions) { viewModel.restart() }
        } else {
            val question = viewModel.currentQuestion ?: return@Box

            Row(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .fillMaxSize()
            ) {
                // ── LEFT 46% ──────────────────────────────────────────────────
                Column(modifier = Modifier.fillMaxWidth(0.46f).fillMaxHeight()) {
                    BackButtonWithText(title = "Consonant + -le Practice", onBackClick = { navController.popBackStack() })
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        modifier = Modifier.padding(horizontal = Dimens16),
                        verticalArrangement = Arrangement.spacedBy(Dimens14)
                    ) {
                        CLEDotProgress(current = uiState.currentIndex, total = viewModel.totalQuestions)
                        CLEInstructionCard()
                        CLEFormulaCard(question = question, uiState = uiState)
                        CLEScoreView(score = uiState.score, currentIndex = uiState.currentIndex)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }

                // ── RIGHT 54% ─────────────────────────────────────────────────
                AnimatedContent(
                    targetState    = uiState.currentIndex,
                    transitionSpec = { fadeIn(spring()) togetherWith fadeOut(spring()) },
                    label          = "clePractice",
                    modifier       = Modifier.weight(1f).fillMaxHeight()
                ) { _ ->
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxHeight().padding(Dimens20)
                    ) {
                        LazyVerticalGrid(
                            columns               = GridCells.Fixed(2),
                            contentPadding        = PaddingValues(vertical = Dimens8),
                            horizontalArrangement = Arrangement.spacedBy(Dimens12),
                            verticalArrangement   = Arrangement.spacedBy(Dimens12)
                        ) {
                            items(question.options) { option ->
                                CLEOptionTile(
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

@Composable
private fun CLEDotProgress(current: Int, total: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(Dimens6)) {
        Text(
            text  = "Question ${current + 1} of $total",
            style = MaterialTheme.typography.labelMedium.scaled(),
            color = Color(0xFF546E7A)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens4)) {
            repeat(minOf(total, 17)) { i ->
                Box(modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(when {
                        i < current  -> cleAccent
                        i == current -> Color(0xFFE91E63)
                        else         -> cleAccent.copy(alpha = 0.20f)
                    })
                )
            }
        }
    }
}

@Composable
private fun CLEInstructionCard() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens6),
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = 12.dp, strokeColor = cleAccent.copy(alpha = 0.30f))
            .padding(Dimens12)
    ) {
        Text(
            text       = "🔡 Spell it Right!",
            style      = MaterialTheme.typography.bodyMedium.scaled(),
            fontWeight = FontWeight.Bold,
            color      = cleAccent,
            textAlign  = TextAlign.Center
        )
        Text(
            text      = "Choose the correct spelling!",
            style     = MaterialTheme.typography.labelSmall.scaled(),
            color     = Color(0xFF455A64),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun CLEFormulaCard(question: CLEPracticeQuestion, uiState: CLEPracticeUiState) {
    val answered = uiState.selectedAnswer != null

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens8),
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = 12.dp, strokeColor = cleAccent.copy(alpha = 0.25f))
            .padding(Dimens12)
    ) {
        Row(
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens8)
        ) {
            Text(
                text       = question.first,
                style      = MaterialTheme.typography.bodyMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color      = Color(0xFF263238),
                modifier   = Modifier
                    .background(Color(0xFFECEFF1), RoundedCornerShape(6.dp))
                    .padding(horizontal = Dimens8, vertical = Dimens4)
            )
            Text(text = "+", style = MaterialTheme.typography.labelSmall.scaled(), fontWeight = FontWeight.Bold, color = cleAccent)
            Text(
                text       = question.ending,
                style      = MaterialTheme.typography.bodyMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color      = cleAccent,
                modifier   = Modifier
                    .background(cleAccent.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                    .border(1.5.dp, cleAccent.copy(alpha = 0.30f), RoundedCornerShape(6.dp))
                    .padding(horizontal = Dimens8, vertical = Dimens4)
            )
            Text(text = "=", style = MaterialTheme.typography.labelSmall.scaled(), fontWeight = FontWeight.Bold, color = Color(0xFF546E7A))
            if (answered) {
                Text(
                    text       = uiState.selectedAnswer ?: "",
                    style      = MaterialTheme.typography.bodyMedium.scaled(),
                    fontWeight = FontWeight.Bold,
                    color      = if (uiState.isCorrect == true) Color(0xFF2E7D32) else Color(0xFFC62828)
                )
            } else {
                Text(text = "?", style = MaterialTheme.typography.bodyMedium.scaled(), fontWeight = FontWeight.Bold, color = Color(0xFF9E9E9E))
            }
        }
        if (uiState.selectedAnswer != null && uiState.isCorrect == false) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Dimens4)) {
                Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF2E7D32), modifier = Modifier.size(14.dp))
                Text(text = question.correct, style = MaterialTheme.typography.labelSmall.scaled(), fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
            }
        }
    }
}

@Composable
private fun CLEScoreView(score: Int, currentIndex: Int) {
    val wrong = currentIndex - score
    Row(
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = 12.dp, strokeColor = cleAccent.copy(alpha = 0.20f))
            .padding(Dimens10)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(Dimens2)) {
            Text(text = "$score", style = MaterialTheme.typography.titleLarge.scaled(), fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
            Text(text = "Correct", style = MaterialTheme.typography.labelSmall.scaled(), color = Color(0xFF546E7A))
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(Dimens2)) {
            Text(text = "$wrong", style = MaterialTheme.typography.titleLarge.scaled(), fontWeight = FontWeight.Bold, color = Color(0xFFC62828))
            Text(text = "Wrong", style = MaterialTheme.typography.labelSmall.scaled(), color = Color(0xFF546E7A))
        }
    }
}

@Composable
private fun CLEOptionTile(
    option:   String,
    question: CLEPracticeQuestion,
    uiState:  CLEPracticeUiState,
    onClick:  () -> Unit
) {
    val answered      = uiState.selectedAnswer != null
    val isSelected    = uiState.selectedAnswer == option
    val isCorrect     = option == question.correct
    val alsoCorrect   = answered && isCorrect && !isSelected
    val interactionSource = remember { MutableInteractionSource() }

    val isRight = isSelected && uiState.isCorrect == true
    val isWrong = isSelected && uiState.isCorrect == false
    val offsetX = remember { Animatable(0f) }
    LaunchedEffect(uiState.shakeWrong) {
        if (uiState.shakeWrong && isWrong) {
            offsetX.animateTo(0f, animationSpec = keyframes {
                durationMillis = 500
                0f at 0; -10f at 60; 10f at 120; -8f at 180
                8f at 240; -5f at 320; 5f at 400; 0f at 500
            })
        }
    }

    val highlighted = isRight || alsoCorrect || isWrong
    val bgColor: Color = when {
        isRight || alsoCorrect -> Color(0xFFC8E6C9)
        isWrong                -> Color(0xFFFFCDD2)
        answered               -> Color.White.copy(alpha = 0.60f)
        else                   -> Color.White
    }
    val strokeColor: Color = when {
        isRight || alsoCorrect -> Color(0xFF2E7D32)
        isWrong                -> Color(0xFFC62828)
        answered               -> Color(0xFFB0BEC5).copy(alpha = 0.50f)
        else                   -> cleAccent.copy(alpha = 0.25f)
    }
    val textColor: Color = when {
        isRight || alsoCorrect -> Color(0xFF1B5E20)
        isWrong                -> Color(0xFFB71C1C)
        answered               -> cleAccent.copy(alpha = 0.30f)
        else                   -> cleAccent
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .height(130.dp)
            .fillMaxWidth()
            .offset { IntOffset(offsetX.value.roundToInt(), 0) }
            .scale(if (isSelected) 1.04f else 1.0f)
            .shadow(if (highlighted) 6.dp else 2.dp, RoundedCornerShape(16.dp),
                ambientColor = if (highlighted) strokeColor else Color.Transparent,
                spotColor    = if (highlighted) strokeColor else Color.Transparent)
            .background(bgColor, RoundedCornerShape(16.dp))
            .border(if (isSelected || (isCorrect && answered)) 2.5.dp else 1.5.dp, strokeColor, RoundedCornerShape(16.dp))
            .clickable(enabled = !answered, interactionSource = interactionSource, indication = null) { onClick() }
            .padding(Dimens14)
    ) {
        Text(
            text       = option,
            style      = MaterialTheme.typography.headlineLarge.scaled(),
            fontWeight = FontWeight.Bold,
            color      = textColor,
            textAlign  = TextAlign.Center,
            maxLines   = 1
        )
        when {
            isRight || alsoCorrect ->
                Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF2E7D32), modifier = Modifier.size(20.dp))
            isWrong ->
                Text("✗", style = MaterialTheme.typography.titleMedium.scaled(), color = Color(0xFFC62828), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun CLEFinishedView(score: Int, total: Int, onRestart: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens24),
            modifier = Modifier.kidsGlassCard(cornerRadius = 20.dp, strokeColor = cleAccent).padding(Dimens32)
        ) {
            Text("🍎", style = MaterialTheme.typography.displayLarge.scaled())
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(Dimens4)) {
                Text(
                    text       = if (score >= total * 3 / 4) "Spelling Star! ⭐" else "Keep practicing! 💪",
                    style      = MaterialTheme.typography.headlineMedium.scaled(),
                    fontWeight = FontWeight.ExtraBold,
                    color      = cleAccent
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
                    .background(Brush.linearGradient(listOf(cleAccent, cleShadow)))
                    .clickable { onRestart() }
                    .padding(horizontal = Dimens24, vertical = Dimens12)
            ) {
                Icon(Icons.Default.Refresh, null, tint = Color.White, modifier = Modifier.size(18.dp))
                Text(text = "Try Again", style = MaterialTheme.typography.labelLarge.scaled(), fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}
