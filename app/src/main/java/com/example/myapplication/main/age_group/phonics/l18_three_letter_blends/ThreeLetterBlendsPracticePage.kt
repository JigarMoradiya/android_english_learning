package com.example.myapplication.main.age_group.phonics.l18_three_letter_blends

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.l18_three_letter_blends.view_model.ThreeLetterBlendsPracticeQuestion
import com.example.myapplication.main.age_group.phonics.l18_three_letter_blends.view_model.ThreeLetterBlendsPracticeUiState
import com.example.myapplication.main.age_group.phonics.l18_three_letter_blends.view_model.ThreeLetterBlendsPracticeViewModel
import com.example.myapplication.main.age_group.phonics.l18_three_letter_blends.view_model.threeLetterBlendsGroups
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

private val blendAccent = Color(0xFFE65100)
private val blendShadow = Color(0xFFBF360C)

@Composable
fun ThreeLetterBlendsPracticePage(
    navController: NavController,
    viewModel: ThreeLetterBlendsPracticeViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.sunsetCoral, shape = KidsFloatingShape.sparkles)

        if (uiState.isFinished) {
            BlendFinishedView(score = uiState.score, total = viewModel.totalQuestions) { viewModel.restart() }
        } else {
            val question = viewModel.currentQuestion ?: return@Box

            Row(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .fillMaxSize()
            ) {
                // ── LEFT (40%) — stable, no animation ────────────────────────
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.40f)
                        .fillMaxHeight()
                ) {
                    BackButtonWithText(
                        title = "3-Letter Blends Practice",
                        onBackClick = { navController.popBackStack() }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        modifier = Modifier.padding(horizontal = Dimens20),
                        verticalArrangement = Arrangement.spacedBy(Dimens16)
                    ) {
                        BlendDotProgress(current = uiState.currentIndex, total = viewModel.totalQuestions)
                        BlendInstructionCard()
                        BlendWordDisplay(question = question, uiState = uiState)
                        BlendScoreChip(score = uiState.score)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }

                // ── RIGHT (60%) — only grid animates ─────────────────────────
                AnimatedContent(
                    targetState = uiState.currentIndex,
                    transitionSpec = { fadeIn(spring()) togetherWith fadeOut(spring()) },
                    label = "blendPractice",
                    modifier = Modifier.weight(1f).fillMaxHeight()
                ) { _ ->
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(horizontal = Dimens20)
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(Dimens12),
                            verticalArrangement   = Arrangement.spacedBy(Dimens12),
                        ) {
                            items(question.options) { option ->
                                BlendOptionTile(
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
private fun BlendDotProgress(current: Int, total: Int) {
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
                                i < current  -> blendAccent
                                i == current -> Color(0xFFF9A825)
                                else         -> blendAccent.copy(alpha = 0.20f)
                            }
                        )
                )
            }
        }
    }
}

@Composable
private fun BlendInstructionCard() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens10),
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = 12.dp, strokeColor = blendAccent)
            .padding(Dimens12)
    ) {
        Text(text = "💥", style = MaterialTheme.typography.titleLarge.scaled())
        Column(verticalArrangement = Arrangement.spacedBy(Dimens4)) {
            Text(
                text  = "Fill the Blend!",
                style = MaterialTheme.typography.bodyMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = blendAccent
            )
            Text(
                text  = "Which 3-letter blend starts this word?",
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = Color(0xFF546E7A)
            )
        }
    }
}

@Composable
private fun BlendWordDisplay(
    question: ThreeLetterBlendsPracticeQuestion,
    uiState: ThreeLetterBlendsPracticeUiState
) {
    val answered  = uiState.selectedAnswer != null
    val isCorrect = uiState.isCorrect
    val rest      = question.word.drop(question.answer.length)
    val blendDisplay = if (answered) question.answer else "___"

    val slotColor = when {
        !answered          -> Color(0xFF90A4AE)
        isCorrect == true  -> Color(0xFF2E7D32)
        else               -> Color(0xFFC62828)
    }
    val slotBg = when {
        !answered          -> Color(0xFFECEFF1)
        isCorrect == true  -> Color(0xFFC8E6C9)
        else               -> Color(0xFFFFCDD2)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = 12.dp, strokeColor = blendAccent)
            .padding(Dimens16)
    ) {
        Text(
            text  = blendDisplay,
            style = MaterialTheme.typography.headlineMedium.scaled(),
            fontWeight = FontWeight.ExtraBold,
            color = slotColor,
            modifier = Modifier
                .background(slotBg, RoundedCornerShape(8.dp))
                .padding(horizontal = Dimens8)
        )
        Text(
            text  = rest,
            style = MaterialTheme.typography.headlineMedium.scaled(),
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF263238)
        )
    }
}

@Composable
private fun BlendScoreChip(score: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens6),
        modifier = Modifier
            .kidsGlassCapsule(strokeColor = blendAccent)
            .padding(horizontal = Dimens14, vertical = Dimens6)
    ) {
        Icon(Icons.Default.Star, contentDescription = null,
            tint = Color(0xFFFFD54F), modifier = Modifier.size(18.dp))
        Text(
            text  = "Score: $score",
            style = MaterialTheme.typography.bodyMedium.scaled(),
            fontWeight = FontWeight.Bold,
            color = blendAccent
        )
    }
}

@Composable
private fun BlendOptionTile(
    option:   String,
    question: ThreeLetterBlendsPracticeQuestion,
    uiState:  ThreeLetterBlendsPracticeUiState,
    onClick:  () -> Unit
) {
    val answered  = uiState.selectedAnswer != null
    val selected  = uiState.selectedAnswer == option
    val isCorrect = option == question.answer
    val interactionSource = remember { MutableInteractionSource() }

    val groupColor = threeLetterBlendsGroups.firstOrNull { it.blend == option }?.accentColor ?: blendAccent

    val fillColor = when {
        !answered              -> Color.White
        selected && isCorrect  -> Color(0xFFC8E6C9)
        selected && !isCorrect -> Color(0xFFFFCDD2)
        isCorrect              -> Color(0xFFC8E6C9)
        else                   -> Color.White.copy(alpha = 0.60f)
    }
    val borderColor = when {
        !answered   -> groupColor.copy(alpha = 0.35f)
        isCorrect   -> Color(0xFF2E7D32)
        selected    -> Color(0xFFC62828)
        else        -> Color(0xFFB0BEC5).copy(alpha = 0.5f)
    }
    val textColor = when {
        !answered   -> groupColor
        isCorrect   -> Color(0xFF1B5E20)
        selected    -> Color(0xFFB71C1C)
        else        -> groupColor.copy(alpha = 0.40f)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .height(130.dp)
            .fillMaxWidth()
            .scale(if (selected) 1.03f else 1.0f)
            .background(fillColor, RoundedCornerShape(12.dp))
            .kidsGlassCard(cornerRadius = 12.dp, strokeColor = borderColor)
            .border(
                width = if (selected || (isCorrect && answered)) 2.5.dp else 1.5.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                enabled = !answered,
                interactionSource = interactionSource,
                indication = null
            ) { onClick() }
            .padding(Dimens8)
    ) {
        Text(
            text  = option,
            style = MaterialTheme.typography.headlineLarge.scaled(),
            fontWeight = FontWeight.ExtraBold,
            color = textColor
        )
        if (!answered) {
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                repeat(3) {
                    Text("⭐", style = MaterialTheme.typography.labelSmall.scaled(),
                        color = groupColor.copy(alpha = 0.25f))
                }
            }
        } else {
            when {
                isCorrect -> Icon(Icons.Default.CheckCircle, null,
                    tint = Color(0xFF2E7D32), modifier = Modifier.size(20.dp))
                selected  -> Text("✗", style = MaterialTheme.typography.titleMedium.scaled(),
                    color = Color(0xFFC62828), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun BlendFinishedView(score: Int, total: Int, onRestart: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens24),
            modifier = Modifier
                .kidsGlassCard(cornerRadius = 20.dp, strokeColor = blendAccent)
                .padding(Dimens32)
        ) {
            Text("💪", style = MaterialTheme.typography.displayLarge.scaled())

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens4)
            ) {
                Text(
                    text  = if (score >= total / 2) "Superb Blending! ⭐" else "Keep practicing! 💫",
                    style = MaterialTheme.typography.headlineMedium.scaled(),
                    fontWeight = FontWeight.ExtraBold,
                    color = blendAccent
                )
                Text(
                    text  = "You got $score out of $total",
                    style = MaterialTheme.typography.titleMedium.scaled(),
                    color = Color(0xFF546E7A)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens8),
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Brush.linearGradient(listOf(blendAccent, blendShadow)))
                    .clickable { onRestart() }
                    .padding(horizontal = Dimens24, vertical = Dimens12)
            ) {
                Icon(Icons.Default.Refresh, null, tint = Color.White, modifier = Modifier.size(18.dp))
                Text(
                    text  = "Try Again",
                    style = MaterialTheme.typography.labelLarge.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
