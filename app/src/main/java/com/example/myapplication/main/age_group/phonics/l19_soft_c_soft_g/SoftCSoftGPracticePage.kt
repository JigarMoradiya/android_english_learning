package com.example.myapplication.main.age_group.phonics.l19_soft_c_soft_g

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
import com.example.myapplication.main.age_group.phonics.l19_soft_c_soft_g.view_model.SoftCSoftGPracticeQuestion
import com.example.myapplication.main.age_group.phonics.l19_soft_c_soft_g.view_model.SoftCSoftGPracticeUiState
import com.example.myapplication.main.age_group.phonics.l19_soft_c_soft_g.view_model.SoftCSoftGPracticeViewModel
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

private val softCGAccent = Color(0xFFBF360C)
private val softCGShadow = Color(0xFF7F0000)

@Composable
fun SoftCSoftGPracticePage(
    navController: NavController,
    viewModel: SoftCSoftGPracticeViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.sunsetCoral, shape = KidsFloatingShape.sparkles)

        if (uiState.isFinished) {
            SoftCGFinishedView(score = uiState.score, total = viewModel.totalQuestions) { viewModel.restart() }
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
                        title = "Soft C & Soft G Practice",
                        onBackClick = { navController.popBackStack() }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        modifier = Modifier.padding(horizontal = Dimens20),
                        verticalArrangement = Arrangement.spacedBy(Dimens16)
                    ) {
                        SoftCGDotProgress(current = uiState.currentIndex, total = viewModel.totalQuestions)
                        SoftCGInstructionCard()
                        SoftCGWordDisplay(question = question, uiState = uiState)
                        SoftCGScoreChip(score = uiState.score)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }

                // ── RIGHT (60%) — only buttons animate ───────────────────────
                AnimatedContent(
                    targetState = uiState.currentIndex,
                    transitionSpec = { fadeIn(spring()) togetherWith fadeOut(spring()) },
                    label = "softCGPractice",
                    modifier = Modifier.weight(1f).fillMaxHeight()
                ) { _ ->
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(horizontal = Dimens20)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Dimens16),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            SoftCGSoundButton(
                                label    = "Hard sound\n/k/ or /g/",
                                emoji    = "🔨",
                                answer   = "hard",
                                accent   = Color(0xFF546E7A),
                                shadow   = Color(0xFF37474F),
                                question = question,
                                uiState  = uiState,
                                onClick  = { viewModel.onAnswerTap("hard") },
                                modifier = Modifier.weight(1f)
                            )
                            SoftCGSoundButton(
                                label    = "Soft sound\n/s/ or /j/",
                                emoji    = "🐍",
                                answer   = "soft",
                                accent   = softCGAccent,
                                shadow   = softCGShadow,
                                question = question,
                                uiState  = uiState,
                                onClick  = { viewModel.onAnswerTap("soft") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SoftCGDotProgress(current: Int, total: Int) {
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
                                i < current  -> softCGAccent
                                i == current -> Color(0xFFE64A19)
                                else         -> softCGAccent.copy(alpha = 0.20f)
                            }
                        )
                )
            }
        }
    }
}

@Composable
private fun SoftCGInstructionCard() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens10),
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = 12.dp, strokeColor = softCGAccent)
            .padding(Dimens12)
    ) {
        Text(text = "🎭", style = MaterialTheme.typography.titleLarge.scaled())
        Column(verticalArrangement = Arrangement.spacedBy(Dimens4)) {
            Text(
                text  = "Hard or Soft?",
                style = MaterialTheme.typography.bodyMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = softCGAccent
            )
            Text(
                text  = "Listen to the word and choose the sound!",
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = Color(0xFF546E7A)
            )
        }
    }
}

@Composable
private fun SoftCGWordDisplay(
    question: SoftCSoftGPracticeQuestion,
    uiState: SoftCSoftGPracticeUiState
) {
    val answered  = uiState.selectedAnswer != null
    val isCorrect = uiState.isCorrect

    val wordLower  = question.word.lowercase()
    val lIndex     = wordLower.indexOf(question.letter)
    val pre        = if (lIndex > 0) question.word.substring(0, lIndex) else ""
    val suf        = if (lIndex >= 0 && lIndex + 1 < question.word.length) question.word.substring(lIndex + 1) else ""

    val hlColor = when {
        !answered          -> softCGAccent
        isCorrect == true  -> Color(0xFF2E7D32)
        else               -> Color(0xFFC62828)
    }
    val hlBg = when {
        !answered          -> softCGAccent.copy(alpha = 0.12f)
        isCorrect == true  -> Color(0xFFC8E6C9)
        else               -> Color(0xFFFFCDD2)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = 12.dp, strokeColor = softCGAccent)
            .padding(Dimens16)
    ) {
        if (pre.isNotEmpty()) {
            Text(
                text  = pre,
                style = MaterialTheme.typography.headlineMedium.scaled(),
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF263238)
            )
        }
        Text(
            text  = question.letter,
            style = MaterialTheme.typography.headlineMedium.scaled(),
            fontWeight = FontWeight.ExtraBold,
            color = hlColor,
            modifier = Modifier
                .background(hlBg, RoundedCornerShape(6.dp))
                .padding(horizontal = Dimens4)
        )
        if (suf.isNotEmpty()) {
            Text(
                text  = suf,
                style = MaterialTheme.typography.headlineMedium.scaled(),
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF263238)
            )
        }
    }
}

@Composable
private fun SoftCGScoreChip(score: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens6),
        modifier = Modifier
            .kidsGlassCapsule(strokeColor = softCGAccent)
            .padding(horizontal = Dimens14, vertical = Dimens6)
    ) {
        Icon(Icons.Default.Star, contentDescription = null,
            tint = Color(0xFFFFD54F), modifier = Modifier.size(18.dp))
        Text(
            text  = "Score: $score",
            style = MaterialTheme.typography.bodyMedium.scaled(),
            fontWeight = FontWeight.Bold,
            color = softCGAccent
        )
    }
}

@Composable
private fun SoftCGSoundButton(
    label:    String,
    emoji:    String,
    answer:   String,
    accent:   Color,
    shadow:   Color,
    question: SoftCSoftGPracticeQuestion,
    uiState:  SoftCSoftGPracticeUiState,
    onClick:  () -> Unit,
    modifier: Modifier = Modifier
) {
    val answered  = uiState.selectedAnswer != null
    val selected  = uiState.selectedAnswer == answer
    val isCorrect = answer == question.answer
    val interactionSource = remember { MutableInteractionSource() }

    val fillColor = when {
        !answered              -> Color.White
        selected && isCorrect  -> Color(0xFFC8E6C9)
        selected && !isCorrect -> Color(0xFFFFCDD2)
        isCorrect              -> Color(0xFFC8E6C9)
        else                   -> Color.White.copy(alpha = 0.60f)
    }
    val borderColor = when {
        !answered   -> accent.copy(alpha = 0.35f)
        isCorrect   -> Color(0xFF2E7D32)
        selected    -> Color(0xFFC62828)
        else        -> Color(0xFFB0BEC5).copy(alpha = 0.5f)
    }
    val textColor = when {
        !answered   -> accent
        isCorrect   -> Color(0xFF1B5E20)
        selected    -> Color(0xFFB71C1C)
        else        -> accent.copy(alpha = 0.40f)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens12),
        modifier = modifier
            .height(200.dp)
            .scale(if (selected) 1.03f else 1.0f)
            .background(fillColor, RoundedCornerShape(12.dp))
            .kidsGlassCard(cornerRadius = 12.dp, strokeColor = borderColor)
            .border(
                width = if (selected || (isCorrect && answered)) 2.5.dp else 1.5.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .then(if (!answered) Modifier.clickable(
                interactionSource = interactionSource, indication = null) { onClick() } else Modifier)
            .padding(Dimens16)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(text = emoji, style = MaterialTheme.typography.titleLarge.scaled())
        Text(
            text  = label,
            style = MaterialTheme.typography.titleSmall.scaled(),
            fontWeight = FontWeight.Bold,
            color = textColor,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        if (answered) {
            when {
                isCorrect -> Icon(Icons.Default.CheckCircle, null,
                    tint = Color(0xFF2E7D32), modifier = Modifier.size(20.dp))
                selected  -> Text("✗", style = MaterialTheme.typography.titleMedium.scaled(),
                    color = Color(0xFFC62828), fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun SoftCGFinishedView(score: Int, total: Int, onRestart: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens24),
            modifier = Modifier
                .kidsGlassCard(cornerRadius = 20.dp, strokeColor = softCGAccent)
                .padding(Dimens32)
        ) {
            Text("🎭", style = MaterialTheme.typography.displayLarge.scaled())

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens4)
            ) {
                Text(
                    text  = if (score >= total / 2) "Brilliant! ⭐" else "Keep going! 💫",
                    style = MaterialTheme.typography.headlineMedium.scaled(),
                    fontWeight = FontWeight.ExtraBold,
                    color = softCGAccent
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
                    .background(Brush.linearGradient(listOf(softCGAccent, softCGShadow)))
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
