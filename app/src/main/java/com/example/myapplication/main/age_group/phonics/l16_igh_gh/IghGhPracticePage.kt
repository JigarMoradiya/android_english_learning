package com.example.myapplication.main.age_group.phonics.l16_igh_gh

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.l16_igh_gh.view_model.IghGhPracticeQuestion
import com.example.myapplication.main.age_group.phonics.l16_igh_gh.view_model.IghGhPracticeUiState
import com.example.myapplication.main.age_group.phonics.l16_igh_gh.view_model.IghGhPracticeViewModel
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

private val ighAccent = Color(0xFF311B92)
private val ighShadow = Color(0xFF1A237E)

@Composable
fun IghGhPracticePage(
    navController: NavController,
    viewModel:     IghGhPracticeViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.indigoPurple, shape = KidsFloatingShape.sparkles)

        if (uiState.isFinished) {
            IghGhFinishedView(uiState = uiState, total = viewModel.totalQuestions) { viewModel.restart() }
        } else {
            val question = viewModel.currentQuestion ?: return@Box

            AnimatedContent(
                targetState = uiState.currentIndex,
                transitionSpec = { fadeIn(spring()) togetherWith fadeOut(spring()) },
                label = "ighGhQuestion",
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .fillMaxSize()
            ) { _ ->
                Row(modifier = Modifier.fillMaxSize()) {
                    // ── LEFT ─────────────────────────────────────────────────
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.42f)
                            .fillMaxHeight()
                    ) {
                        BackButtonWithText(title = "igh & gh Practice",
                            onBackClick = { navController.popBackStack() })

                        Spacer(modifier = Modifier.weight(1f))

                        Column(
                            modifier = Modifier.padding(horizontal = Dimens20),
                            verticalArrangement = Arrangement.spacedBy(Dimens16)
                        ) {
                            DotProgress(current = uiState.currentIndex, total = viewModel.totalQuestions)
                            InstructionCard()
                            WordDisplay(question = question, uiState = uiState)
                            ScoreChip(score = uiState.score)
                        }

                        Spacer(modifier = Modifier.weight(1f))
                    }

                    // ── RIGHT (2×2 options) ───────────────────────────────────
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(horizontal = Dimens20)
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(Dimens12),
                            verticalArrangement   = Arrangement.spacedBy(Dimens12),
                        ) {
                            items(question.options) { option ->
                                OptionTile(
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
private fun DotProgress(current: Int, total: Int) {
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
                                i < current  -> ighAccent
                                i == current -> Color(0xFF5E35B1)
                                else         -> ighAccent.copy(alpha = 0.20f)
                            }
                        )
                )
            }
        }
    }
}

@Composable
private fun InstructionCard() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens10),
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = 12.dp, strokeColor = ighAccent)
            .padding(Dimens12)
    ) {
        Text(text = "🔍", style = MaterialTheme.typography.titleLarge.scaled())
        Column(verticalArrangement = Arrangement.spacedBy(Dimens4)) {
            Text(
                text  = "Fill the Blank!",
                style = MaterialTheme.typography.bodyMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = ighAccent
            )
            Text(
                text  = "Which letters complete the word?",
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = Color(0xFF546E7A)
            )
        }
    }
}

@Composable
private fun WordDisplay(question: IghGhPracticeQuestion, uiState: IghGhPracticeUiState) {
    val answered = uiState.selectedAnswer != null
    val isCorrect = uiState.isCorrect
    val parts = question.displayWord.split(question.blank)
    val pre = parts.getOrNull(0) ?: ""
    val suf = parts.getOrNull(1) ?: ""

    val slotColor = when {
        !answered   -> Color(0xFF90A4AE)
        isCorrect == true  -> Color(0xFF2E7D32)
        else               -> Color(0xFFC62828)
    }
    val slotBg = when {
        !answered   -> Color(0xFFEDE7F6)
        isCorrect == true  -> Color(0xFFC8E6C9)
        else               -> Color(0xFFFFCDD2)
    }

    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = 12.dp, strokeColor = ighAccent)
            .padding(Dimens16)
    ) {
        if (pre.isNotEmpty()) {
            Text(pre, style = MaterialTheme.typography.headlineMedium.scaled(),
                fontWeight = FontWeight.ExtraBold, color = Color(0xFF263238))
        }
        Text(
            text  = uiState.selectedAnswer ?: question.blank,
            style = MaterialTheme.typography.headlineMedium.scaled(),
            fontWeight = FontWeight.ExtraBold,
            color = slotColor,
            modifier = Modifier
                .padding(horizontal = Dimens8, vertical = Dimens4)
                .background(slotBg, RoundedCornerShape(8.dp))
                .padding(horizontal = Dimens8, vertical = Dimens4)
        )
        if (suf.isNotEmpty()) {
            Text(suf, style = MaterialTheme.typography.headlineMedium.scaled(),
                fontWeight = FontWeight.ExtraBold, color = Color(0xFF263238))
        }
    }
}

@Composable
private fun ScoreChip(score: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens6),
        modifier = Modifier
            .kidsGlassCapsule(strokeColor = ighAccent)
            .padding(horizontal = Dimens14, vertical = Dimens6)
    ) {
        Icon(Icons.Default.Star, contentDescription = null,
            tint = Color(0xFFFFD54F), modifier = Modifier.size(18.dp))
        Text(
            text  = "Score: $score",
            style = MaterialTheme.typography.bodyMedium.scaled(),
            fontWeight = FontWeight.Bold,
            color = ighAccent
        )
    }
}

@Composable
private fun OptionTile(
    option:   String,
    question: IghGhPracticeQuestion,
    uiState:  IghGhPracticeUiState,
    onClick:  () -> Unit
) {
    val answered  = uiState.selectedAnswer != null
    val selected  = uiState.selectedAnswer == option
    val isCorrect = option == question.answer
    val interactionSource = remember { MutableInteractionSource() }

    val fillColor = when {
        !answered   -> Color.Transparent
        selected && isCorrect  -> Color(0xFFC8E6C9)
        selected && !isCorrect -> Color(0xFFFFCDD2)
        isCorrect              -> Color(0xFFC8E6C9)
        else                   -> Color.Transparent
    }
    val borderColor = when {
        !answered   -> ighAccent.copy(alpha = 0.35f)
        isCorrect   -> Color(0xFF2E7D32)
        selected    -> Color(0xFFC62828)
        else        -> Color(0xFFB0BEC5).copy(alpha = 0.5f)
    }
    val textColor = when {
        !answered   -> ighAccent
        isCorrect   -> Color(0xFF1B5E20)
        selected    -> Color(0xFFB71C1C)
        else        -> ighAccent.copy(alpha = 0.40f)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .height(130.dp)
            .fillMaxWidth()
            .background(fillColor, RoundedCornerShape(12.dp))
            .kidsGlassCard(cornerRadius = 12.dp, strokeColor = borderColor)
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                enabled           = !answered,
                interactionSource = interactionSource,
                indication        = null
            ) { onClick() }
            .padding(Dimens8)
    ) {
        Text(
            text  = option,
            style = MaterialTheme.typography.headlineMedium.scaled(),
            fontWeight = FontWeight.ExtraBold,
            color = textColor
        )
        if (!answered) {
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                repeat(3) {
                    Text("⭐", style = MaterialTheme.typography.labelSmall.scaled(),
                        color = ighAccent.copy(alpha = 0.25f))
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
private fun IghGhFinishedView(
    uiState: IghGhPracticeUiState,
    total:   Int,
    onRetry: () -> Unit
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens24),
            modifier = Modifier
                .kidsGlassCard(cornerRadius = 20.dp, strokeColor = ighAccent)
                .padding(Dimens32)
        ) {
            Text("🌙", style = MaterialTheme.typography.displayLarge.scaled())

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens8)
            ) {
                Text(
                    text  = if (uiState.score >= total / 2) "Brilliant! ⭐" else "Keep shining! 💫",
                    style = MaterialTheme.typography.headlineMedium.scaled(),
                    fontWeight = FontWeight.ExtraBold,
                    color = ighAccent
                )
                Text(
                    text  = "You got ${uiState.score} out of $total",
                    style = MaterialTheme.typography.titleMedium.scaled(),
                    color = Color(0xFF546E7A)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens8),
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Brush.linearGradient(listOf(ighAccent, ighShadow)))
                    .clickable { onRetry() }
                    .padding(horizontal = Dimens24, vertical = Dimens12)
            ) {
                Icon(Icons.Default.Refresh, null, tint = Color.White,
                    modifier = Modifier.size(18.dp))
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
