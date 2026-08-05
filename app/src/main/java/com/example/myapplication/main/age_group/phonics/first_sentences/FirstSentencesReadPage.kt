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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.first_sentences.view_model.FirstSentence
import com.example.myapplication.main.age_group.phonics.first_sentences.view_model.FirstSentencesReadViewModel
import com.example.myapplication.main.age_group.phonics.first_sentences.view_model.firstSentenceGroups
import com.example.myapplication.main.age_group.phonics.first_sentences.view_model.firstSentenceHelperSet
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
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens28
import com.example.myapplication.ui.theme.AppDimens.Dimens32
import com.example.myapplication.ui.theme.AppDimens.Dimens80
import com.example.myapplication.utils.extensions.scaled

/**
 * MILESTONE · Read Your First Sentences — screen 4, "Read It Yourself".
 * Keep identical to iOS FirstSentencesReadView.swift.
 */
private val readAccent = Color(0xFF1565C0)

@Composable
fun FirstSentencesReadPage(navController: NavController) {
    val viewModel: FirstSentencesReadViewModel = hiltViewModel()
    val uiState = viewModel.uiState

    DisposableEffect(Unit) { onDispose { viewModel.stop() } }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.softBlue, shape = KidsFloatingShape.sparkles)

        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                // expandWidth = false: the default fills the whole row, which left the count
                // pill measured at zero width — its text wrapped and inflated the header
                BackButtonWithText(
                    title = "Read It Yourself",
                    expandWidth = false,
                    onBackClick = { navController.popBackStack() }
                )
                Spacer(modifier = Modifier.weight(1f))
                CountView(index = uiState.index, total = viewModel.total)
                Spacer(modifier = Modifier.size(Dimens24))
            }

            GroupRow(selected = uiState.groupIndex, onSelect = { viewModel.selectGroup(it) })

            // The card is sized FROM the room it actually has. On a landscape phone that
            // room is short and wide, so the picture sits BESIDE the line rather than under
            // it — stacked, the card grew past the bottom and the dots drew across it.
            BoxWithConstraints(modifier = Modifier.weight(1f)) {
                val available = maxHeight
                // The picture slot is a SHARE of the height actually available, never a
                // fixed 80/100/120dp — a fixed slot is what overflowed the short screen.
                val pictureHeight: Dp = minOf(Dimens80, available * 0.46f)

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = Dimens20, vertical = Dimens8)
                ) {
                    SentenceCard(viewModel = viewModel, pictureHeight = pictureHeight)
                }
            }

            // a dot per line in this vowel — seven is a journey, not a treadmill
            ProgressDots(viewModel)
            Spacer(modifier = Modifier.size(Dimens12))

            Controls(viewModel)
            Spacer(modifier = Modifier.size(Dimens20))
        }
    }
}

// ── Header pill ───────────────────────────────────────────────────────────────

/** the app's own header pill — same shape as every phonics practice screen */
@Composable
private fun CountView(index: Int, total: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens6),
        modifier = Modifier
            .kidsGlassCapsule(strokeColor = Color(0xFF0D47A1))
            .padding(horizontal = Dimens14, vertical = Dimens6)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.MenuBook,
            contentDescription = null,
            tint = readAccent,
            modifier = Modifier.size(Dimens16)
        )
        Text(
            text = "${index + 1} of $total",
            style = MaterialTheme.typography.bodyLarge.scaled(),
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0D47A1)
        )
    }
}

// ── Groups ────────────────────────────────────────────────────────────────────

/** the five vowels, the same chips as Build a Sentence so the two screens navigate alike */
@Composable
private fun GroupRow(selected: Int, onSelect: (Int) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens10, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Dimens8)
    ) {
        firstSentenceGroups.forEachIndexed { index, group ->
            val on = index == selected
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens6),
                modifier = Modifier
                    .background(
                        if (on) group.color else Color.White.copy(alpha = 0.9f),
                        RoundedCornerShape(50)
                    )
                    .clip(RoundedCornerShape(50))
                    .clickable { onSelect(index) }
                    .padding(horizontal = Dimens14, vertical = Dimens8)
            ) {
                Text(text = group.emoji, style = MaterialTheme.typography.bodyMedium.scaled())
                Text(
                    text = group.title,
                    style = MaterialTheme.typography.labelSmall.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = if (on) Color.White else group.color,
                    maxLines = 1
                )
            }
        }
    }
}

// ── Card ──────────────────────────────────────────────────────────────────────

/**
 * the line on a card, not loose on the wallpaper — a bare sentence on a gradient reads as
 * a placeholder
 */
@Composable
private fun SentenceCard(viewModel: FirstSentencesReadViewModel, pictureHeight: Dp) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens16),
        modifier = Modifier
            .fillMaxWidth()
            // the app's own glass card — hand-rolling shadow + background + border gave a
            // black spot shadow (only ambientColor was tinted) clipped to the card
            .kidsGlassCard(cornerRadius = Dimens24, strokeColor = readAccent)
            .padding(horizontal = Dimens20, vertical = Dimens12)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens8),
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens8)
            ) {
                Text(text = "👀", style = MaterialTheme.typography.bodyMedium.scaled())
                Text(
                    text = "Your turn — read it out loud",
                    style = MaterialTheme.typography.bodyMedium.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF546E7A)
                )
            }

            WordRow(viewModel)
        }

        // the picture keeps its slot whether or not it is showing, so the card is the same
        // size before and after "Hear it" — no jump, and nothing to overflow
        val on = viewModel.uiState.revealed
        val pop by animateFloatAsState(
            targetValue = if (on) 1f else 0.7f,
            animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessMediumLow),
            label = "picturePop"
        )
        val fade by animateFloatAsState(
            targetValue = if (on) 1f else 0f,
            animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessMediumLow),
            label = "pictureFade"
        )
        FirstSentencePicture(
            sentence = viewModel.sentence,
            height = pictureHeight,
            // the glyph is sized FROM the slot, so it can never draw past it
            emojiSize = (pictureHeight.value * 0.78f).sp,
            modifier = Modifier.graphicsLayer { scaleX = pop; scaleY = pop; alpha = fade }
        )
    }
}

@Composable
private fun WordRow(viewModel: FirstSentencesReadViewModel) {
    val uiState = viewModel.uiState
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens4, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        viewModel.sentence.words.forEachIndexed { index, word ->
            val lit = uiState.readingIndex == index
            val isHelper = firstSentenceHelperSet.contains(FirstSentence.key(word))
            val scale by animateFloatAsState(
                targetValue = if (lit) 1.12f else 1f,
                animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
                label = "readWordScale"
            )

            Text(
                text = word,
                style = MaterialTheme.typography.displayMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = when {
                    lit -> Color.White
                    isHelper -> Color(0xFF8E24AA)
                    else -> readAccent
                },
                maxLines = 1,
                modifier = Modifier
                    .scale(scale)
                    .background(
                        when {
                            lit -> readAccent
                            isHelper -> Color(0xFFF3E5F5)
                            else -> Color.White.copy(alpha = 0.9f)
                        },
                        RoundedCornerShape(Dimens12)
                    )
                    .clip(RoundedCornerShape(Dimens12))
                    .clickable { viewModel.onWordTap(index) }
                    .padding(horizontal = Dimens12, vertical = Dimens8)
            )
        }
    }
}

// ── Dots + controls ───────────────────────────────────────────────────────────

@Composable
private fun ProgressDots(viewModel: FirstSentencesReadViewModel) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens6, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        for (i in 0 until viewModel.total) {
            Box(
                modifier = Modifier
                    .size(Dimens8)
                    .background(
                        when {
                            i == viewModel.uiState.index -> readAccent
                            viewModel.isHeard(i) -> readAccent.copy(alpha = 0.45f)
                            else -> Color.White.copy(alpha = 0.7f)
                        },
                        CircleShape
                    )
            )
        }
    }
}

@Composable
private fun Controls(viewModel: FirstSentencesReadViewModel) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens16, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        NavButton(
            icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            enabled = viewModel.uiState.index > 0,
            onClick = { viewModel.previous() }
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens8),
            modifier = Modifier
                .background(readAccent, RoundedCornerShape(50))
                .clip(RoundedCornerShape(50))
                .clickable { viewModel.reveal() }
                .padding(horizontal = Dimens28, vertical = Dimens12)
        ) {
            Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(Dimens20)
            )
            Text(
                text = if (viewModel.uiState.revealed) "Again" else "Hear it",
                style = MaterialTheme.typography.bodyMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        NavButton(
            icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            enabled = !viewModel.isLast,
            onClick = { viewModel.next() }
        )
    }
}

@Composable
private fun NavButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(Color.White.copy(alpha = if (enabled) 0.95f else 0.6f), CircleShape)
            .clip(CircleShape)
            .then(if (enabled) Modifier.clickable { onClick() } else Modifier)
            .padding(Dimens12)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (enabled) readAccent else Color(0xFFB0BEC5),
            modifier = Modifier.size(Dimens20)
        )
    }
}
