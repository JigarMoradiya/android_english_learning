package com.example.myapplication.main.age_group.phonics.first_sentences

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.first_sentences.view_model.FirstSentence
import com.example.myapplication.main.age_group.phonics.first_sentences.view_model.FirstSentenceBuildPhase
import com.example.myapplication.main.age_group.phonics.first_sentences.view_model.FirstSentencesBuildViewModel
import com.example.myapplication.main.age_group.phonics.first_sentences.view_model.firstSentenceGroups
import com.example.myapplication.main.age_group.phonics.first_sentences.view_model.firstSentenceHelperSet
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
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
import com.example.myapplication.ui.theme.AppDimens.Dimens100
import com.example.myapplication.utils.extensions.scaled

/**
 * MILESTONE · Read Your First Sentences — screen 3, "Build a Sentence".
 * Follows the shape of the Level 4 build page.
 * Keep identical to iOS FirstSentencesBuildView.swift.
 */
private val buildAccent = Color(0xFFE65100)

@Composable
fun FirstSentencesBuildPage(navController: NavController) {
    val viewModel: FirstSentencesBuildViewModel = hiltViewModel()
    val uiState = viewModel.uiState

    DisposableEffect(Unit) { onDispose { viewModel.stop() } }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.peachCoral, shape = KidsFloatingShape.sparkles)

        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            BackButtonWithText(title = "Read Sentences", onBackClick = { navController.popBackStack() })

            GroupRow(
                selected = uiState.groupIndex,
                onSelect = { viewModel.selectGroup(it) }
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens16),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = Dimens20)
                    .padding(top = Dimens4, bottom = Dimens20)
            ) {
                SentenceList(
                    sentences = viewModel.group.sentences,
                    selected = uiState.sentenceIndex,
                    onSelect = { viewModel.selectSentence(it) },
                    modifier = Modifier.weight(0.34f).fillMaxHeight()
                )
                Stage(
                    viewModel = viewModel,
                    modifier = Modifier.weight(0.66f).fillMaxHeight()
                )
            }
        }
    }
}

// ── Groups ────────────────────────────────────────────────────────────────────

/**
 * Five chips fit a landscape tablet, so they are CENTRED rather than left-aligned in a
 * scroller — a row that starts hard against the left edge under a centred stage reads as
 * misaligned even when nothing is cut.
 */
@Composable
private fun GroupRow(selected: Int, onSelect: (Int) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens10, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Dimens8, bottom = Dimens16)
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

// ── Sentence list ─────────────────────────────────────────────────────────────

@Composable
private fun SentenceList(
    sentences: List<FirstSentence>,
    selected: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimens10),
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(vertical = Dimens4)
    ) {
        sentences.forEachIndexed { index, item ->
            val on = index == selected
            Text(
                text = item.text,
                style = MaterialTheme.typography.labelMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = if (on) Color.White else Color(0xFF37474F),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (on) buildAccent else Color.White.copy(alpha = 0.92f),
                        RoundedCornerShape(Dimens12)
                    )
                    .clip(RoundedCornerShape(Dimens12))
                    .clickable { onSelect(index) }
                    .padding(horizontal = Dimens12, vertical = Dimens10)
            )
        }
    }
}

// ── Stage ─────────────────────────────────────────────────────────────────────

@Composable
private fun Stage(viewModel: FirstSentencesBuildViewModel, modifier: Modifier) {
    val uiState = viewModel.uiState

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens20),
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.weight(1f))

        WordRow(viewModel)

        if (uiState.showImage) {
            val pop by animateFloatAsState(
                targetValue = if (uiState.showImage) 1f else 0.7f,
                animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessMediumLow),
                label = "picturePop"
            )
            FirstSentencePicture(
                sentence = viewModel.sentence,
                height = Dimens100,
                // the glyph is sized FROM the slot, so it can never draw past it
                emojiSize = (Dimens100.value * 0.62f).sp,
                modifier = Modifier.graphicsLayer { scaleX = pop; scaleY = pop; alpha = pop }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens8),
            modifier = Modifier
                .background(buildAccent, RoundedCornerShape(50))
                .clip(RoundedCornerShape(50))
                .clickable { viewModel.play() }
                .padding(horizontal = Dimens24, vertical = Dimens12)
        ) {
            Icon(
                imageVector = Icons.Default.PlayCircle,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(Dimens20)
            )
            Text(
                text = if (uiState.phase == FirstSentenceBuildPhase.idle) "Read it" else "Again",
                style = MaterialTheme.typography.bodyMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

/** the words, arriving one at a time and then read with a travelling highlight */
@Composable
private fun WordRow(viewModel: FirstSentencesBuildViewModel) {
    val uiState = viewModel.uiState
    val words = viewModel.sentence.words
    val shown = if (uiState.phase == FirstSentenceBuildPhase.idle) words.size else uiState.wordsShown

    // one line, centred — six words never need to wrap, and a wrapped sentence stops
    // looking like a sentence
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens4, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        words.forEachIndexed { index, word ->
            val visible = index < shown
            val lit = uiState.readingIndex == index
            val isHelper = firstSentenceHelperSet.contains(FirstSentence.key(word))

            val scale by animateFloatAsState(
                targetValue = if (lit) 1.14f else if (visible) 1f else 0.6f,
                animationSpec = spring(dampingRatio = 0.62f, stiffness = Spring.StiffnessMediumLow),
                label = "wordScale"
            )
            val alpha by animateFloatAsState(
                targetValue = if (visible) 1f else 0f,
                animationSpec = spring(dampingRatio = 0.62f, stiffness = Spring.StiffnessMediumLow),
                label = "wordAlpha"
            )

            Text(
                text = word,
                style = MaterialTheme.typography.displaySmall.scaled(),
                fontWeight = FontWeight.Bold,
                color = when {
                    lit -> Color.White
                    isHelper -> Color(0xFF8E24AA)
                    else -> Color(0xFF1565C0)
                },
                maxLines = 1,
                modifier = Modifier
                    .scale(scale)
                    .graphicsLayer { this.alpha = alpha }
                    .background(
                        when {
                            lit -> buildAccent
                            isHelper -> Color(0xFFF3E5F5)
                            else -> Color(0xFFE3F2FD)
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
