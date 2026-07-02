package com.example.myapplication.main.age_group.phonics.l23_suffixes

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
import com.example.myapplication.main.age_group.phonics.l23_suffixes.view_model.SuffixesPracticeQuestion
import com.example.myapplication.main.age_group.phonics.l23_suffixes.view_model.SuffixesPracticeUiState
import com.example.myapplication.main.age_group.phonics.l23_suffixes.view_model.SuffixesPracticeViewModel
import com.example.myapplication.main.age_group.phonics.l23_suffixes.view_model.suffixGroups
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

private val sfAccent = Color(0xFF3949AB)
private val sfShadow = Color(0xFF283593)

private fun suffixGroupColor(suffix: String): Color =
    suffixGroups.firstOrNull { it.suffix == suffix }?.accentColor ?: sfAccent

@Composable
fun SuffixesPracticePage(
    navController: NavController,
    viewModel: SuffixesPracticeViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.skyLavender, shape = KidsFloatingShape.stars)

        if (uiState.isFinished) {
            SFFinishedView(score = uiState.score, total = viewModel.totalQuestions) { viewModel.restart() }
        } else {
            val question = viewModel.currentQuestion ?: return@Box

            Row(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .fillMaxSize()
            ) {
                // ── LEFT 46% ──────────────────────────────────────────────────
                Column(modifier = Modifier.fillMaxWidth(0.46f).fillMaxHeight()) {
                    BackButtonWithText(title = "Suffixes Practice", onBackClick = { navController.popBackStack() })
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        modifier = Modifier.padding(horizontal = Dimens16),
                        verticalArrangement = Arrangement.spacedBy(Dimens14)
                    ) {
                        SFDotProgress(current = uiState.currentIndex, total = viewModel.totalQuestions)
                        SFInstructionCard()
                        SFFormulaCard(question = question, uiState = uiState)
                        SFScoreView(score = uiState.score, currentIndex = uiState.currentIndex)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }

                // ── RIGHT 54% ─────────────────────────────────────────────────
                AnimatedContent(
                    targetState = uiState.currentIndex,
                    transitionSpec = { fadeIn(spring()) togetherWith fadeOut(spring()) },
                    label = "sfPractice",
                    modifier = Modifier.weight(1f).fillMaxHeight()
                ) { _ ->
                    Column(
                        verticalArrangement   = Arrangement.Center,
                        horizontalAlignment   = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxHeight().padding(Dimens20)
                    ) {
                        LazyVerticalGrid(
                            columns               = GridCells.Fixed(2),
                            contentPadding        = PaddingValues(vertical = Dimens8),
                            horizontalArrangement = Arrangement.spacedBy(Dimens12),
                            verticalArrangement   = Arrangement.spacedBy(Dimens12)
                        ) {
                            items(question.options) { option ->
                                SFOptionTile(option = option, question = question, uiState = uiState, onClick = { viewModel.onAnswerTap(option) })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SFDotProgress(current: Int, total: Int) {
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
                        i < current  -> Color(0xFF3949AB)
                        i == current -> Color(0xFF5C6BC0)
                        else         -> Color(0xFF3949AB).copy(alpha = 0.20f)
                    })
                )
            }
        }
    }
}

@Composable
private fun SFInstructionCard() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens6),
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = 12.dp, strokeColor = sfAccent.copy(alpha = 0.30f))
            .padding(Dimens12)
    ) {
        Text(
            text       = "🔡 Grow the Word!",
            style      = MaterialTheme.typography.bodyMedium.scaled(),
            fontWeight = FontWeight.Bold,
            color      = sfAccent,
            textAlign  = TextAlign.Center
        )
        Text(
            text      = "Pick the correct word\nwith the right suffix!",
            style     = MaterialTheme.typography.labelSmall.scaled(),
            color     = Color(0xFF455A64),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SFFormulaCard(question: SuffixesPracticeQuestion, uiState: SuffixesPracticeUiState) {
    val answered    = uiState.selectedAnswer != null
    val groupColor  = suffixGroupColor(question.suffix)
    val rawSuffix   = if (question.suffix.startsWith("-")) question.suffix.drop(1) else question.suffix
    val isDropY     = question.base.endsWith("y")

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens8),
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = 12.dp, strokeColor = sfAccent.copy(alpha = 0.25f))
            .padding(Dimens12)
    ) {
        Row(
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens8)
        ) {
            Text(text = question.base, style = MaterialTheme.typography.bodyMedium.scaled(), fontWeight = FontWeight.Bold, color = Color(0xFF263238))
            Text(text = "+", style = MaterialTheme.typography.labelSmall.scaled(), fontWeight = FontWeight.Bold, color = Color(0xFF546E7A))
            Text(
                text       = question.suffix,
                style      = MaterialTheme.typography.bodyMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color      = groupColor,
                modifier   = Modifier
                    .background(groupColor.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                    .padding(horizontal = Dimens8, vertical = Dimens4)
            )
            Text(text = "=", style = MaterialTheme.typography.labelSmall.scaled(), fontWeight = FontWeight.Bold, color = Color(0xFF546E7A))
            if (answered) {
                Text(text = question.correct, style = MaterialTheme.typography.bodyMedium.scaled(), fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
            } else {
                Text(text = "?", style = MaterialTheme.typography.bodyMedium.scaled(), fontWeight = FontWeight.Bold, color = Color(0xFF9E9E9E))
            }
        }
        if (uiState.selectedAnswer != null && uiState.selectedAnswer != question.correct) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Dimens4)) {
                Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF2E7D32), modifier = Modifier.size(14.dp))
                Text(text = question.correct, style = MaterialTheme.typography.labelSmall.scaled(), fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
            }
        }
        if (isDropY) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens4),
                modifier = Modifier
                    .background(Color(0xFFE65100).copy(alpha = 0.10f), RoundedCornerShape(20.dp))
                    .padding(horizontal = Dimens8, vertical = Dimens4)
            ) {
                Text(text = "💡", style = MaterialTheme.typography.labelSmall.scaled())
                Text(text = "Y → I before the suffix!", style = MaterialTheme.typography.labelSmall.scaled(), color = Color(0xFFE65100))
            }
        }
    }
}

@Composable
private fun SFScoreView(score: Int, currentIndex: Int) {
    val wrong = currentIndex - score
    Row(
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = 12.dp, strokeColor = sfAccent.copy(alpha = 0.20f))
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
private fun SFOptionTile(
    option: String,
    question: SuffixesPracticeQuestion,
    uiState: SuffixesPracticeUiState,
    onClick: () -> Unit
) {
    val answered     = uiState.selectedAnswer != null
    val isSelected   = uiState.selectedAnswer == option
    val isCorrect    = option == question.correct
    val alsoCorrect  = answered && isCorrect && !isSelected
    val groupColor   = suffixGroupColor(question.suffix)
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
    val textOnColor = isRight || alsoCorrect || isWrong

    val rawSuffix = if (question.suffix.startsWith("-")) question.suffix.drop(1) else question.suffix
    val suffixLen = rawSuffix.length
    val rootPart  = if (option.length > suffixLen) option.dropLast(suffixLen) else option
    val sufPart   = if (option.length > suffixLen) option.takeLast(suffixLen) else ""

    val bgColor: Color = when {
        isRight || alsoCorrect -> Color(0xFF2E7D32)
        isWrong                -> Color(0xFFC62828)
        else                   -> Color.White
    }
    val strokeColor: Color = when {
        isRight || alsoCorrect -> Color(0xFF1B5E20)
        isWrong                -> Color(0xFFB71C1C)
        else                   -> sfAccent.copy(alpha = 0.25f)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .height(130.dp)
            .fillMaxWidth()
            .offset { IntOffset(offsetX.value.roundToInt(), 0) }
            .scale(if (isSelected) 1.04f else 1.0f)
            .shadow(if (textOnColor) 6.dp else 2.dp, RoundedCornerShape(16.dp), ambientColor = bgColor, spotColor = bgColor)
            .background(bgColor, RoundedCornerShape(16.dp))
            .border(1.5.dp, strokeColor, RoundedCornerShape(16.dp))
            .clickable(enabled = !answered, interactionSource = interactionSource, indication = null) { onClick() }
            .padding(Dimens14)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Text(
                text       = rootPart,
                style      = MaterialTheme.typography.headlineLarge.scaled(),
                fontWeight = FontWeight.ExtraBold,
                color      = if (textOnColor) Color.White else Color(0xFF263238)
            )
            if (sufPart.isNotEmpty()) {
                Text(
                    text       = sufPart,
                    style      = MaterialTheme.typography.headlineLarge.scaled(),
                    fontWeight = FontWeight.ExtraBold,
                    color      = if (textOnColor) Color.White else groupColor,
                    modifier   = Modifier
                        .background(
                            if (textOnColor) Color.White.copy(alpha = 0.20f) else groupColor.copy(alpha = 0.12f),
                            RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = Dimens4)
                )
            }
        }
        when {
            isRight || alsoCorrect ->
                Icon(Icons.Default.CheckCircle, null, tint = Color.White, modifier = Modifier.size(20.dp))
            isWrong ->
                Text("✗", style = MaterialTheme.typography.titleMedium.scaled(), color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SFFinishedView(score: Int, total: Int, onRestart: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens24),
            modifier = Modifier.kidsGlassCard(cornerRadius = 20.dp, strokeColor = sfAccent).padding(Dimens32)
        ) {
            Text("🔡", style = MaterialTheme.typography.displayLarge.scaled())
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(Dimens4)) {
                Text(
                    text       = if (score >= total * 3 / 4) "Word Builder! ⭐" else "Keep Growing! 🌱",
                    style      = MaterialTheme.typography.headlineMedium.scaled(),
                    fontWeight = FontWeight.ExtraBold,
                    color      = sfAccent
                )
                Text(text = "You got $score out of $total", style = MaterialTheme.typography.titleMedium.scaled(), color = Color(0xFF546E7A))
            }
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens8),
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Brush.linearGradient(listOf(sfAccent, sfShadow)))
                    .clickable { onRestart() }
                    .padding(horizontal = Dimens24, vertical = Dimens12)
            ) {
                Icon(Icons.Default.Refresh, null, tint = Color.White, modifier = Modifier.size(18.dp))
                Text(text = "Try Again", style = MaterialTheme.typography.labelLarge.scaled(), fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}
