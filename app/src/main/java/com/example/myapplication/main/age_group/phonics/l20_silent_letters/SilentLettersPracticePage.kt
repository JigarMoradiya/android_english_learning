package com.example.myapplication.main.age_group.phonics.l20_silent_letters

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.l20_silent_letters.view_model.SilentLettersPracticeQuestion
import com.example.myapplication.main.age_group.phonics.l20_silent_letters.view_model.SilentLettersPracticeUiState
import com.example.myapplication.main.age_group.phonics.l20_silent_letters.view_model.SilentLettersPracticeViewModel
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
import kotlin.math.sin

private val slAccent = Color(0xFF455A64)
private val slShadow = Color(0xFF263238)

@Composable
fun SilentLettersPracticePage(
    navController: NavController,
    viewModel: SilentLettersPracticeViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.grayBlue, shape = KidsFloatingShape.sparkles)

        if (uiState.isFinished) {
            SilentFinishedView(score = uiState.score, total = viewModel.totalQuestions) { viewModel.restart() }
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
                        title = "Silent Letters Practice",
                        onBackClick = { navController.popBackStack() }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        modifier = Modifier.padding(horizontal = Dimens20),
                        verticalArrangement = Arrangement.spacedBy(Dimens16)
                    ) {
                        SilentDotProgress(current = uiState.currentIndex, total = viewModel.totalQuestions)
                        SilentInstructionCard()
                        SilentPreviewWord(uiState = uiState)
                        SilentScoreChip(score = uiState.score)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }

                // ── RIGHT (60%) — only letter tiles animate ───────────────────
                AnimatedContent(
                    targetState = uiState.currentIndex,
                    transitionSpec = { fadeIn(spring()) togetherWith fadeOut(spring()) },
                    label = "silentPractice",
                    modifier = Modifier.weight(1f).fillMaxHeight()
                ) { _ ->
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(horizontal = Dimens20)
                    ) {
                        Text(
                            text  = "Which letter is silent?",
                            style = MaterialTheme.typography.titleSmall.scaled(),
                            fontWeight = FontWeight.Bold,
                            color = slAccent
                        )
                        Spacer(modifier = Modifier.size(Dimens16))
                        Row(horizontalArrangement = Arrangement.spacedBy(Dimens10)) {
                            question.word.forEachIndexed { idx, char ->
                                SilentLetterTile(
                                    char     = char,
                                    index    = idx,
                                    question = question,
                                    uiState  = uiState,
                                    onClick  = { viewModel.onLetterTap(idx) }
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
private fun SilentDotProgress(current: Int, total: Int) {
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
                                i < current  -> slAccent
                                i == current -> Color(0xFF607D8B)
                                else         -> slAccent.copy(alpha = 0.20f)
                            }
                        )
                )
            }
        }
    }
}

@Composable
private fun SilentInstructionCard() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens10),
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = 12.dp, strokeColor = slAccent)
            .padding(Dimens12)
    ) {
        Text(text = "👻", style = MaterialTheme.typography.titleLarge.scaled())
        Column(verticalArrangement = Arrangement.spacedBy(Dimens4)) {
            Text(
                text  = "Spot the Ghost!",
                style = MaterialTheme.typography.bodyMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = slAccent
            )
            Text(
                text  = "Tap the SILENT (ghost) letter in the word!",
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = Color(0xFF546E7A)
            )
        }
    }
}

@Composable
private fun SilentPreviewWord(uiState: SilentLettersPracticeUiState) {
    val answered  = uiState.selectedLetterIndex != null
    val isCorrect = uiState.isCorrect

    val resultColor = when {
        !answered         -> slAccent
        isCorrect == true  -> Color(0xFF2E7D32)
        else               -> Color(0xFFC62828)
    }

    Box(
        modifier = Modifier
            .kidsGlassCard(cornerRadius = 12.dp, strokeColor = slAccent)
            .padding(Dimens12),
        contentAlignment = Alignment.Center
    ) {
        if (answered) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens6)
            ) {
                Icon(
                    imageVector = if (isCorrect == true) Icons.Default.CheckCircle else Icons.Default.Cancel,
                    contentDescription = null,
                    tint = resultColor,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text  = if (isCorrect == true) "Found it! 👻" else "Not quite!",
                    style = MaterialTheme.typography.labelMedium.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = resultColor
                )
            }
        }
    }
}

@Composable
private fun SilentScoreChip(score: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens6),
        modifier = Modifier
            .kidsGlassCapsule(strokeColor = slAccent)
            .padding(horizontal = Dimens14, vertical = Dimens6)
    ) {
        Icon(Icons.Default.Star, contentDescription = null,
            tint = Color(0xFFFFD54F), modifier = Modifier.size(18.dp))
        Text(
            text  = "Score: $score",
            style = MaterialTheme.typography.bodyMedium.scaled(),
            fontWeight = FontWeight.Bold,
            color = slAccent
        )
    }
}

@Composable
private fun SilentLetterTile(
    char:     Char,
    index:    Int,
    question: SilentLettersPracticeQuestion,
    uiState:  SilentLettersPracticeUiState,
    onClick:  () -> Unit
) {
    val answered       = uiState.selectedLetterIndex != null
    val isSelected     = uiState.selectedLetterIndex == index
    val isCorrectSlot  = index == question.silentIndex
    val isShaking      = uiState.shakeIndex == index && answered && !isCorrectSlot
    val interactionSource = remember { MutableInteractionSource() }

    val shakeOffset = if (isShaking) {
        (sin(System.currentTimeMillis() / 50.0) * 7).roundToInt()
    } else 0

    val bgColor = when {
        !answered                  -> Color.White
        isSelected && isCorrectSlot  -> Color(0xFFE0E0E0)
        isSelected && !isCorrectSlot -> Color(0xFFFFCDD2)
        isCorrectSlot && answered    -> Color(0xFFE0E0E0)
        else                         -> Color.White.copy(alpha = 0.70f)
    }
    val textColor = when {
        !answered   -> Color(0xFF263238)
        isCorrectSlot -> Color(0xFF9E9E9E)
        isSelected    -> Color(0xFFC62828)
        else          -> Color(0xFF263238).copy(alpha = 0.50f)
    }
    val borderColor = when {
        !answered   -> slAccent.copy(alpha = 0.40f)
        isCorrectSlot -> Color(0xFF9E9E9E)
        isSelected    -> Color(0xFFC62828)
        else          -> Color(0xFFB0BEC5).copy(alpha = 0.40f)
    }
    val borderWidth = if (isSelected || (isCorrectSlot && answered)) 2.5.dp else 1.5.dp

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(64.dp)
            .offset { IntOffset(shakeOffset, 0) }
            .background(bgColor, RoundedCornerShape(8.dp))
            .border(borderWidth, borderColor, RoundedCornerShape(8.dp))
            .then(if (!answered) Modifier.clickable(
                interactionSource = interactionSource, indication = null) { onClick() } else Modifier)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text  = char.uppercaseChar().toString(),
                style = MaterialTheme.typography.headlineMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            AnimatedVisibility(
                visible = isCorrectSlot && answered,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                Text(
                    text  = "👻",
                    style = MaterialTheme.typography.labelSmall.scaled()
                )
            }
        }
    }
}

@Composable
private fun SilentFinishedView(score: Int, total: Int, onRestart: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens24),
            modifier = Modifier
                .kidsGlassCard(cornerRadius = 20.dp, strokeColor = slAccent)
                .padding(Dimens32)
        ) {
            Text("👻", style = MaterialTheme.typography.displayLarge.scaled())

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens4)
            ) {
                Text(
                    text  = if (score >= total / 2) "Ghost Hunter! ⭐" else "Keep detecting! 🔍",
                    style = MaterialTheme.typography.headlineMedium.scaled(),
                    fontWeight = FontWeight.ExtraBold,
                    color = slAccent
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
                    .background(Brush.linearGradient(listOf(slAccent, slShadow)))
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
