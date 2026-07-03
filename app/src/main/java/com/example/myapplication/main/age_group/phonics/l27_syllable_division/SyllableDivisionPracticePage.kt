package com.example.myapplication.main.age_group.phonics.l27_syllable_division

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.l27_syllable_division.view_model.SDPracticeQuestion
import com.example.myapplication.main.age_group.phonics.l27_syllable_division.view_model.SDPracticeUiState
import com.example.myapplication.main.age_group.phonics.l27_syllable_division.view_model.SDPracticeViewModel
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
import kotlin.math.roundToInt

private val sdAccent = Color(0xFF00897B)
private val sdShadow = Color(0xFF00695C)

@Composable
fun SyllableDivisionPracticePage(
    navController: NavController,
    viewModel: SDPracticeViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.tealCyan, shape = KidsFloatingShape.waves)

        if (uiState.isFinished) {
            SDFinishedView(score = uiState.score, total = viewModel.totalQuestions) { viewModel.restart() }
        } else {
            val question = viewModel.currentQuestion ?: return@Box

            Row(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .fillMaxSize()
            ) {
                // ── LEFT 38% ──────────────────────────────────────────────────
                Column(modifier = Modifier.fillMaxWidth(0.38f).fillMaxHeight()) {
                    BackButtonWithText(title = "Practice", onBackClick = { navController.popBackStack() })
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        modifier = Modifier.padding(horizontal = Dimens16),
                        verticalArrangement = Arrangement.spacedBy(Dimens14)
                    ) {
                        SDQuestionCard(question = question, uiState = uiState)
                        SDScoreView(score = uiState.score, currentIndex = uiState.currentIndex)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }

                // ── RIGHT 62% ─────────────────────────────────────────────────
                AnimatedContent(
                    targetState    = uiState.currentIndex,
                    transitionSpec = { fadeIn(spring()) togetherWith fadeOut(spring()) },
                    label          = "sdPractice",
                    modifier       = Modifier.weight(1f).fillMaxHeight()
                ) { _ ->
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(horizontal = Dimens20)
                    ) {
                        // Progress dots above the grid, centered
                        SDDotProgress(
                            current  = uiState.currentIndex,
                            total    = viewModel.totalQuestions,
                            modifier = Modifier.padding(bottom = Dimens12)
                        )
                        LazyVerticalGrid(
                            columns               = GridCells.Fixed(2),
                            contentPadding        = PaddingValues(vertical = Dimens8),
                            horizontalArrangement = Arrangement.spacedBy(Dimens12),
                            verticalArrangement   = Arrangement.spacedBy(Dimens12)
                        ) {
                            items(question.options) { option ->
                                SDOptionTile(
                                    option   = option,
                                    question = question,
                                    uiState  = uiState,
                                    onClick  = { viewModel.onOptionTap(option) }
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
private fun SDDotProgress(current: Int, total: Int, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens6),
        modifier            = modifier.fillMaxWidth()
    ) {
        Text(
            text      = "Question ${current + 1} of $total",
            style     = MaterialTheme.typography.labelMedium.scaled(),
            color     = Color(0xFF546E7A),
            textAlign = TextAlign.Center
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier              = Modifier.fillMaxWidth()
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens4)) {
                repeat(minOf(total, 20)) { i ->
                    Box(modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(when {
                            i < current  -> sdAccent
                            i == current -> Color(0xFF26A69A)
                            else         -> sdAccent.copy(alpha = 0.20f)
                        })
                    )
                }
            }
        }
    }
}

@Composable
private fun SDQuestionCard(question: SDPracticeQuestion, uiState: SDPracticeUiState) {
    val answered = uiState.selectedOption != null

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens12),
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = 16.dp, strokeColor = sdAccent)
            .padding(Dimens16)
    ) {
        Text(
            text  = question.emoji,
            style = MaterialTheme.typography.displaySmall.scaled()
        )
        Text(
            text       = "Where do we chop? ✂️",
            style      = MaterialTheme.typography.labelSmall.scaled(),
            color      = Color(0xFF78909C),
            textAlign  = TextAlign.Center
        )
        // Whole word waiting for the chop
        Text(
            text       = question.word,
            style      = MaterialTheme.typography.titleLarge.scaled(),
            fontWeight = FontWeight.ExtraBold,
            color      = sdAccent,
            modifier   = Modifier
                .background(sdAccent.copy(alpha = 0.10f), RoundedCornerShape(10.dp))
                .border(1.5.dp, sdAccent.copy(alpha = 0.40f), RoundedCornerShape(10.dp))
                .padding(horizontal = Dimens16, vertical = Dimens8)
        )
        // Result chop label when answered
        AnimatedVisibility(
            visible = answered,
            enter   = scaleIn(spring(dampingRatio = 0.65f, stiffness = 400f)) + fadeIn(),
            exit    = scaleOut() + fadeOut()
        ) {
            val isCorrect = uiState.isCorrect == true
            val parts     = question.correct.split("-")
            val fillColor = if (isCorrect) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
            val textColor = if (isCorrect) Color(0xFF2E7D32) else Color(0xFFC62828)
            val borderCol = if (isCorrect) Color(0xFFA5D6A7) else Color(0xFFFFCDD2)

            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens4),
                modifier = Modifier
                    .background(fillColor, RoundedCornerShape(50.dp))
                    .border(1.dp, borderCol, RoundedCornerShape(50.dp))
                    .padding(horizontal = Dimens12, vertical = Dimens6)
            ) {
                Text(
                    text       = parts.firstOrNull() ?: "",
                    style      = MaterialTheme.typography.titleSmall.scaled(),
                    fontWeight = FontWeight.ExtraBold,
                    color      = textColor
                )
                Text(text = "✂️", style = MaterialTheme.typography.labelSmall.scaled())
                Text(
                    text       = parts.lastOrNull() ?: "",
                    style      = MaterialTheme.typography.titleSmall.scaled(),
                    fontWeight = FontWeight.ExtraBold,
                    color      = textColor
                )
            }
        }
    }
}

@Composable
private fun SDScoreView(score: Int, currentIndex: Int) {
    val wrong = currentIndex - score
    Row(
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = 12.dp, strokeColor = sdAccent.copy(alpha = 0.20f))
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
private fun SDOptionTile(
    option:   String,
    question: SDPracticeQuestion,
    uiState:  SDPracticeUiState,
    onClick:  () -> Unit
) {
    val answered    = uiState.selectedOption != null
    val isSelected  = uiState.selectedOption == option
    val isCorrect   = option == question.correct
    val alsoCorrect = answered && isCorrect && !isSelected
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
        else                   -> sdAccent.copy(alpha = 0.30f)
    }
    val textColor: Color = when {
        isRight || alsoCorrect -> Color(0xFF1B5E20)
        isWrong                -> Color(0xFFB71C1C)
        answered               -> sdAccent.copy(alpha = 0.30f)
        else                   -> sdAccent
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .height(130.dp)
            .fillMaxWidth()
            .offset { IntOffset(offsetX.value.roundToInt(), 0) }
            .scale(if (isSelected) 1.04f else 1.0f)
            .shadow(
                elevation    = if (highlighted) 6.dp else 2.dp,
                shape        = RoundedCornerShape(16.dp),
                ambientColor = if (highlighted) strokeColor else Color.Transparent,
                spotColor    = if (highlighted) strokeColor else Color.Transparent
            )
            .background(bgColor, RoundedCornerShape(16.dp))
            .border(
                width  = if (isSelected || (isCorrect && answered)) 2.5.dp else 1.5.dp,
                color  = strokeColor,
                shape  = RoundedCornerShape(16.dp)
            )
            .clickable(enabled = !answered, interactionSource = interactionSource, indication = null) { onClick() }
            .padding(Dimens14)
    ) {
        Text(
            text       = option,
            style      = MaterialTheme.typography.headlineMedium.scaled(),
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
private fun SDFinishedView(score: Int, total: Int, onRestart: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens24),
            modifier = Modifier
                .kidsGlassCard(cornerRadius = 20.dp, strokeColor = sdAccent)
                .padding(Dimens32)
        ) {
            Text("🎉", style = MaterialTheme.typography.displayLarge.scaled())
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(Dimens4)) {
                Text(
                    text       = if (score >= total * 3 / 4) "Chop Champion! ⭐" else "Keep practicing! 💪",
                    style      = MaterialTheme.typography.headlineMedium.scaled(),
                    fontWeight = FontWeight.ExtraBold,
                    color      = sdAccent
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
                    .background(Brush.linearGradient(listOf(sdAccent, sdShadow)))
                    .clickable { onRestart() }
                    .padding(horizontal = Dimens24, vertical = Dimens12)
            ) {
                Icon(Icons.Default.Refresh, null, tint = Color.White, modifier = Modifier.size(18.dp))
                Text(text = "Try Again", style = MaterialTheme.typography.labelLarge.scaled(), fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}
