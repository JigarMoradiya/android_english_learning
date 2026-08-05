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
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TouchApp
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.first_sentences.view_model.FirstSentenceHelper
import com.example.myapplication.main.age_group.phonics.first_sentences.view_model.FirstSentencesHelpersViewModel
import com.example.myapplication.main.age_group.phonics.first_sentences.view_model.firstSentenceHelpers
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.kidsGlassCard
import com.example.myapplication.main.common.kidsShadow
import com.example.myapplication.ui.theme.AppDimens.Dimens3
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens100
import com.example.myapplication.utils.extensions.scaled

/**
 * MILESTONE · Read Your First Sentences — screen 2, "Helper Words".
 * Keep identical to iOS FirstSentencesHelpersView.swift.
 */
private val helpersAccent = Color(0xFF8E24AA)

@Composable
fun FirstSentencesHelpersPage(navController: NavController) {
    val viewModel: FirstSentencesHelpersViewModel = hiltViewModel()
    val uiState = viewModel.uiState

    DisposableEffect(Unit) { onDispose { viewModel.stop() } }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.lilacPink, shape = KidsFloatingShape.sparkles)

        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            BackButtonWithText(title = "Helper Words", onBackClick = { navController.popBackStack() })

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens20, Alignment.CenterVertically),
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = Dimens24, vertical = Dimens8)
            ) {
                Header()

                // ALL EIGHT, always. The list IS the lesson — hiding the three with no
                // recording yet taught seven helper words instead of eight. A card that
                // can speak carries a speaker glyph; one that cannot simply doesn't, so
                // nothing promises audio it hasn't got. The glyphs appear on their own
                // once the files land.
                // ONE row. Eight helper words are one set the child is asked to learn —
                // wrapped onto two lines they read as two groups. Each card takes an equal
                // share of the width instead of sizing to its own text, so "the" and "and"
                // line up as a set rather than a ragged row.
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens8),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    firstSentenceHelpers.forEach { helper ->
                        HelperCard(
                            helper = helper,
                            hasAudio = viewModel.hasAudio(helper.word),
                            isSpeaking = uiState.speaking == helper.word,
                            isHeard = uiState.heard.contains(helper.word),
                            onTap = { viewModel.onWordTap(helper) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                if (viewModel.allHeard) AllDoneBanner()
            }
        }
    }
}

// ── Header ────────────────────────────────────────────────────────────────────

@Composable
private fun Header() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens12),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "✨ Why these words are special",
            style = MaterialTheme.typography.titleLarge.scaled(),
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4A148C),
            textAlign = TextAlign.Center
        )

        // The honest rule, spelled out rather than compressed into one line. Bold +
        // tinted key words, at bodyLarge — this IS the teaching text on this screen,
        // not a caption under something else.
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens10),
            modifier = Modifier
                .fillMaxWidth()
                .kidsGlassCard(cornerRadius = Dimens20, strokeColor = helpersAccent)
                .padding(horizontal = Dimens20, vertical = Dimens16)
        ) {
            RuleText {
                plain("You will see these words in ")
                key("almost every sentence")
                plain(".")
            }
            RuleText {
                plain("But you ")
                key("cannot sound them out")
                plain(" — ")
                key("the")
                plain(" does not say t‑h‑e.")
            }
            RuleText {
                plain("So we ")
                key("learn them by sight")
                plain(" — look, say, remember.")
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens6)
        ) {
            Icon(
                imageVector = Icons.Default.TouchApp,
                contentDescription = null,
                tint = Color(0xFF607D8B),
                modifier = Modifier.size(Dimens16)
            )
            Text(
                text = "Tap each one to hear it",
                style = MaterialTheme.typography.bodyMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF607D8B)
            )
        }
    }
}

/** tiny builder so a rule line stays readable instead of a wall of withStyle blocks */
private class RuleScope(val builder: androidx.compose.ui.text.AnnotatedString.Builder) {
    fun plain(text: String) =
        builder.withStyle(SpanStyle(color = Color(0xFF37474F))) { append(text) }

    fun key(text: String) =
        builder.withStyle(SpanStyle(color = helpersAccent, fontWeight = FontWeight.Bold)) { append(text) }
}

@Composable
private fun RuleText(content: RuleScope.() -> Unit) {
    Text(
        text = buildAnnotatedString { RuleScope(this).content() },
        style = MaterialTheme.typography.bodyLarge.scaled(),
        textAlign = TextAlign.Center
    )
}

// ── Card ──────────────────────────────────────────────────────────────────────

@Composable
private fun HelperCard(
    helper: FirstSentenceHelper,
    hasAudio: Boolean,
    isSpeaking: Boolean,
    isHeard: Boolean,
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scale by animateFloatAsState(
        targetValue = if (isSpeaking) 1.08f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
        label = "helperScale"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens6, Alignment.CenterHorizontally),
        modifier = modifier
            .scale(scale)
            .kidsShadow(
                color = helpersAccent,
                shape = RoundedCornerShape(Dimens16),
                strength = if (isSpeaking) 1.6f else 1f
            )
            .background(
                if (isSpeaking) helpersAccent else Color.White.copy(alpha = 0.92f),
                RoundedCornerShape(Dimens16)
            )
            .border(
                Dimens3,
                if (isHeard) helpersAccent else Color(0xFFE1BEE7),
                RoundedCornerShape(Dimens16)
            )
            .clip(RoundedCornerShape(Dimens16))
            .then(if (hasAudio) Modifier.clickable { onTap() } else Modifier)
            .padding(horizontal = Dimens20, vertical = Dimens12)
    ) {
        Text(
            text = helper.word,
            style = MaterialTheme.typography.displaySmall.scaled(),
            fontWeight = FontWeight.Bold,
            color = if (isSpeaking) Color.White else helpersAccent
        )
        if (hasAudio) {
            Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = null,
                tint = if (isSpeaking) Color.White else helpersAccent.copy(alpha = 0.55f),
                modifier = Modifier.size(Dimens16)
            )
        }
    }
}

// ── Done ──────────────────────────────────────────────────────────────────────

@Composable
private fun AllDoneBanner() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens10),
        modifier = Modifier
            .background(
                Brush.horizontalGradient(listOf(Color(0xFF43A047), Color(0xFF2E7D32))),
                RoundedCornerShape(Dimens16)
            )
            .padding(horizontal = Dimens20, vertical = Dimens12)
    ) {
        Text(text = "🎉", style = MaterialTheme.typography.titleLarge.scaled())
        Text(
            text = "You know all the helper words — now let's read!",
            style = MaterialTheme.typography.bodyMedium.scaled(),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            maxLines = 2
        )
    }
}
