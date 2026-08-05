package com.example.myapplication.main.age_group.phonics.first_sentences

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.first_sentences.view_model.FirstSentence
import com.example.myapplication.main.age_group.phonics.first_sentences.view_model.FirstSentencesPictureViewModel
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
import com.example.myapplication.ui.theme.AppDimens.Dimens100
import com.example.myapplication.utils.extensions.scaled

/**
 * MILESTONE · Read Your First Sentences — screen 5, "Which Picture?".
 *
 * Built on the app's standard phonics-practice shape (see MagicEPracticePage): question
 * on the left as a glass card, options on the right, shared completion popup over the
 * finished question.
 *
 * Keep identical to iOS FirstSentencesPictureView.swift.
 */
private val pictureAccent = Color(0xFF00897B)
private val pictureDeep = Color(0xFF00695C)

@Composable
fun FirstSentencesPicturePage(navController: NavController) {
    val viewModel: FirstSentencesPictureViewModel = hiltViewModel()
    val uiState = viewModel.uiState

    DisposableEffect(Unit) { onDispose { viewModel.stop() } }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.aquaGreen, shape = KidsFloatingShape.sparkles)

        val shown = viewModel.displaySentence
        if (shown != null) {
            BoxWithConstraints(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .fillMaxSize()
            ) {
                val screenH = maxHeight
                Row(modifier = Modifier.fillMaxSize()) {
                    // the line needs most of the width — it is a sentence, and the three
                    // pictures beside it read fine in a narrower column
                    LeftPanel(
                        viewModel = viewModel,
                        sentence = shown,
                        onBack = { navController.popBackStack() },
                        modifier = Modifier.weight(0.65f).fillMaxHeight()
                    )
                    RightPanel(
                        viewModel = viewModel,
                        answer = shown,
                        screenH = screenH,
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
    viewModel: FirstSentencesPictureViewModel,
    sentence: FirstSentence,
    onBack: () -> Unit,
    modifier: Modifier,
) {
    Column(modifier = modifier) {
        BackButtonWithText(title = "Which Picture?", onBackClick = onBack)

        Spacer(modifier = Modifier.weight(1f))

        Column(
            verticalArrangement = Arrangement.spacedBy(Dimens16),
            modifier = Modifier.padding(horizontal = Dimens20)
        ) {
            ProgressBar(round = viewModel.uiState.round, total = viewModel.total)
            QuestionCard(viewModel, sentence)
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
            color = pictureDeep,
            trackColor = Color.White.copy(alpha = 0.4f),
            strokeCap = StrokeCap.Round
        )
    }
}

/** the line, tappable to hear — reading it is the task, hearing it is a help */
@Composable
private fun QuestionCard(viewModel: FirstSentencesPictureViewModel, sentence: FirstSentence) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens10),
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = Dimens12, strokeColor = pictureAccent)
            .padding(Dimens16)
    ) {
        Text(text = "🖼️", style = MaterialTheme.typography.displayMedium.scaled())

        Text(
            text = "Which picture shows this?",
            style = MaterialTheme.typography.titleLarge.scaled(),
            fontWeight = FontWeight.Bold,
            color = pictureAccent,
            textAlign = TextAlign.Center
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens10, Alignment.CenterHorizontally),
            modifier = Modifier
                .background(Color.White.copy(alpha = 0.55f), RoundedCornerShape(Dimens8))
                .clip(RoundedCornerShape(Dimens8))
                .clickable { viewModel.playSentence() }
                .padding(horizontal = Dimens14, vertical = Dimens8)
        ) {
            Text(
                text = sentence.text,
                style = MaterialTheme.typography.displaySmall.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF004D40),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false)
            )
            Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = null,
                tint = pictureAccent,
                modifier = Modifier.size(Dimens16)
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
            .kidsGlassCapsule(strokeColor = pictureDeep)
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
            color = pictureDeep
        )
    }
}

// ── Right Panel ───────────────────────────────────────────────────────────────

@Composable
private fun RightPanel(
    viewModel: FirstSentencesPictureViewModel,
    answer: FirstSentence,
    screenH: Dp,
    modifier: Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens14),
        modifier = modifier.padding(horizontal = Dimens20, vertical = Dimens20)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        viewModel.uiState.options.forEach { option ->
            OptionButton(viewModel, option, answer, screenH)
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun OptionButton(
    viewModel: FirstSentencesPictureViewModel,
    option: FirstSentence,
    answer: FirstSentence,
    screenH: Dp,
) {
    val uiState = viewModel.uiState
    val answered = uiState.chosen != null
    val selected = uiState.chosen == option.id
    val isCorrect = option.id == answer.id
    val btnH: Dp = minOf(screenH * 0.24f, Dimens100)

    // once an answer is in, the right card is always shown — a wrong pick must never
    // leave the child without knowing which one it was
    val fillColor = when {
        !answered -> Color.White
        selected && isCorrect -> Color(0xFFC8E6C9)
        selected && !isCorrect -> Color(0xFFFFCDD2)
        isCorrect -> Color(0xFFC8E6C9)
        else -> Color.White.copy(alpha = 0.60f)
    }
    val borderColor = when {
        !answered -> pictureAccent.copy(alpha = 0.35f)
        isCorrect -> Color(0xFF2E7D32)
        selected -> Color(0xFFC62828)
        else -> Color(0xFFB0BEC5).copy(alpha = 0.5f)
    }

    val scale by animateFloatAsState(
        targetValue = if (selected) 1.02f else 1f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMedium),
        label = "optionScale"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens10, Alignment.CenterHorizontally),
        modifier = Modifier
            .fillMaxWidth()
            .height(btnH)
            .scale(scale)
            .background(if (answered) fillColor else Color.Transparent, RoundedCornerShape(Dimens12))
            .kidsGlassCard(cornerRadius = Dimens12, strokeColor = borderColor)
            .border(Dimens3, borderColor, RoundedCornerShape(Dimens12))
            .clip(RoundedCornerShape(Dimens12))
            .then(if (!answered) Modifier.clickable { viewModel.choose(option) } else Modifier)
            .padding(horizontal = Dimens16)
    ) {
        FirstSentencePicture(
            sentence = option,
            height = btnH * 0.72f,
            // the glyph is sized FROM the slot, so it can never draw past it
            emojiSize = (btnH.value * 0.56f).sp
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
