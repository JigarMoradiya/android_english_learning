package com.example.myapplication.main.age_group.phonics.first_sentences

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.first_sentences.view_model.FirstSentence
import com.example.myapplication.main.age_group.phonics.first_sentences.view_model.FirstSentencesMissingViewModel
import com.example.myapplication.main.age_group.phonics.first_sentences.view_model.firstSentenceHelperSet
import com.example.myapplication.main.common.ActivityCompletePopup
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.kidsGlassCapsule
import com.example.myapplication.main.common.kidsGlassCard
import com.example.myapplication.ui.theme.AppDimens.Dimens3
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens14
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.utils.extensions.scaled

/**
 * MILESTONE · Read Your First Sentences — screen 6, "Missing Word".
 *
 * Built on the app's standard phonics-practice shape (see MagicEPracticePage): question
 * on the left as a glass card, options on the right, shared completion popup over the
 * finished question. A milestone that looks like a different app is a milestone the child
 * has to re-learn how to use.
 *
 * Keep identical to iOS FirstSentencesMissingView.swift.
 */
private val missingAccent = Color(0xFF6A1B9A)
private val missingDeep = Color(0xFF4A148C)

@Composable
fun FirstSentencesMissingPage(navController: NavController) {
    val viewModel: FirstSentencesMissingViewModel = hiltViewModel()
    val uiState = viewModel.uiState

    DisposableEffect(Unit) { onDispose { viewModel.stop() } }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.indigoPurple, shape = KidsFloatingShape.sparkles)

        if (viewModel.displaySentence != null) {
            Box(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .fillMaxSize()
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    // the line needs most of the width — it is a sentence, not a word,
                    // and the three choices beside it are three letters each
                    LeftPanel(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() },
                        modifier = Modifier.weight(0.65f).fillMaxHeight()
                    )
                    RightPanel(
                        viewModel = viewModel,
                        modifier = Modifier.weight(0.35f).fillMaxHeight()
                    )
                }
            }
        }

        // the app's shared completion popup, OVER the finished question
        if (uiState.finished) {
            val pct = if (viewModel.total > 0) uiState.score * 100 / viewModel.total else 0
            ActivityCompletePopup(
                stars = if (pct >= 100) 3 else if (pct >= 70) 2 else 1,
                score = uiState.score,
                total = viewModel.total,
                scoreLabel = "correct 🎯",
                feedbackText = if (pct >= 70) "Well done! 🎉" else "Good try! 💪",
                onNext = { viewModel.start() },
                nextLabel = "Try Again",
                onClose = { navController.popBackStack() }
            )
        }
    }
}

// ── Left Panel ────────────────────────────────────────────────────────────────

@Composable
private fun LeftPanel(
    viewModel: FirstSentencesMissingViewModel,
    onBack: () -> Unit,
    modifier: Modifier,
) {
    Column(modifier = modifier) {
        BackButtonWithText(title = "Missing Word", onBackClick = onBack)

        Spacer(modifier = Modifier.weight(1f))

        Column(
            verticalArrangement = Arrangement.spacedBy(Dimens16),
            modifier = Modifier.padding(horizontal = Dimens20)
        ) {
            ProgressBar(round = viewModel.uiState.round, total = viewModel.total)
            QuestionCard(viewModel)
            ScoreView(viewModel.uiState.score)
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun ProgressBar(round: Int, total: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(Dimens4)) {
        Text(
            text = "Question ${minOf(round + 1, total)} of $total",
            style = MaterialTheme.typography.labelMedium.scaled(),
            color = Color(0xFF546E7A)
        )
        LinearProgressIndicator(
            progress = if (total == 0) 0f else (round + 1).toFloat() / total.toFloat(),
            modifier = Modifier.fillMaxWidth().height(Dimens8),
            color = missingDeep,
            trackColor = Color.White.copy(alpha = 0.4f),
            strokeCap = StrokeCap.Round
        )
    }
}

@Composable
private fun QuestionCard(viewModel: FirstSentencesMissingViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens10),
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = Dimens12, strokeColor = missingAccent)
            .padding(Dimens16)
    ) {
        // icon above the title, not beside it — it reads as a heading rather than a label
        // with a sticker on the front, and it centres cleanly
        Text(text = "🔍", style = MaterialTheme.typography.displayMedium.scaled())

        Text(
            text = "Which word is missing?",
            style = MaterialTheme.typography.titleLarge.scaled(),
            fontWeight = FontWeight.Bold,
            color = missingAccent,
            textAlign = TextAlign.Center
        )

        BlankedLine(viewModel)
    }
}

/** the line with a dashed hole where the word should be — tap it to hear the sentence */
@Composable
private fun BlankedLine(viewModel: FirstSentencesMissingViewModel) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens6, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { viewModel.playSentence() }
    ) {
        viewModel.blanked.forEach { word ->
            val isBlank = word == "___"
            val isHelper = firstSentenceHelperSet.contains(FirstSentence.key(word))
            Text(
                text = if (isBlank) "?" else word,
                style = MaterialTheme.typography.displaySmall.scaled(),
                fontWeight = FontWeight.Bold,
                color = when {
                    isBlank -> Color(0xFFAB47BC)
                    isHelper -> Color(0xFF8E24AA)
                    else -> Color(0xFF311B92)
                },
                maxLines = 1,
                modifier = Modifier
                    .then(
                        if (isBlank) {
                            // a dashed hole, not a filled tile — it reads as something
                            // waiting rather than something already answered
                            Modifier.border(Dimens3, Color(0xFFAB47BC), RoundedCornerShape(Dimens8))
                        } else {
                            Modifier.background(Color.White.copy(alpha = 0.55f), RoundedCornerShape(Dimens8))
                        }
                    )
                    .padding(horizontal = Dimens10, vertical = Dimens6)
            )
        }
    }
}

@Composable
private fun ScoreView(score: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens6),
        modifier = Modifier
            .kidsGlassCapsule(strokeColor = missingDeep)
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
            style = MaterialTheme.typography.bodyLarge.scaled(),
            fontWeight = FontWeight.Bold,
            color = missingDeep
        )
    }
}

// ── Right Panel ───────────────────────────────────────────────────────────────

@Composable
private fun RightPanel(viewModel: FirstSentencesMissingViewModel, modifier: Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens14),
        modifier = modifier.padding(horizontal = Dimens20, vertical = Dimens20)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        viewModel.uiState.choices.forEach { word ->
            ChoiceButton(viewModel, word)
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun ChoiceButton(viewModel: FirstSentencesMissingViewModel, word: String) {
    val uiState = viewModel.uiState
    val answered = uiState.chosen != null
    val selected = uiState.chosen == word
    val isCorrect = word == viewModel.displaySentence?.missing

    // once an answer is in, the right word is always revealed — a wrong pick must never
    // leave the child without knowing which one it was
    val fillColor = when {
        !answered -> Color.White
        selected && isCorrect -> Color(0xFFC8E6C9)
        selected && !isCorrect -> Color(0xFFFFCDD2)
        isCorrect -> Color(0xFFC8E6C9)
        else -> Color.White.copy(alpha = 0.60f)
    }
    val borderColor = when {
        !answered -> missingAccent.copy(alpha = 0.35f)
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

    val scale by animateFloatAsState(
        targetValue = if (selected) 1.02f else 1f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMedium),
        label = "choiceScale"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens6),
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .background(if (answered) fillColor else Color.Transparent, RoundedCornerShape(Dimens12))
            .kidsGlassCard(cornerRadius = Dimens12, strokeColor = borderColor)
            .border(Dimens3, borderColor, RoundedCornerShape(Dimens12))
            .clip(RoundedCornerShape(Dimens12))
            .then(if (!answered) Modifier.clickable { viewModel.choose(word) } else Modifier)
            .padding(horizontal = Dimens16, vertical = Dimens16)
    ) {
        Text(
            text = word,
            style = MaterialTheme.typography.displayMedium.scaled(),
            fontWeight = FontWeight.Bold,
            color = textColor,
            maxLines = 1
        )
        if (answered) {
            Spacer(modifier = Modifier.weight(1f))
            if (isCorrect) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF2E7D32),
                    modifier = Modifier.size(Dimens20)
                )
            } else if (selected) {
                Icon(
                    imageVector = Icons.Default.Cancel,
                    contentDescription = null,
                    tint = Color(0xFFC62828),
                    modifier = Modifier.size(Dimens20)
                )
            }
        }
    }
}
