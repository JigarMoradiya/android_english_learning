package com.example.myapplication.main.age_group.phonics.l9_digraphs

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.PhonicsLearnSessionEffect
import com.example.myapplication.main.age_group.phonics.listen.view_model.PhonicsListenLevelKey
import com.example.myapplication.main.age_group.phonics.l9_digraphs.view_model.DigraphEntry
import com.example.myapplication.main.age_group.phonics.l9_digraphs.view_model.DigraphLearnUiState
import com.example.myapplication.main.age_group.phonics.l9_digraphs.view_model.DigraphWord
import com.example.myapplication.main.age_group.phonics.l9_digraphs.view_model.DigraphsLearnViewModel
import com.example.myapplication.main.age_group.phonics.l9_digraphs.view_model.digraphsData
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
import com.example.myapplication.main.common.PhonicsWrongReadingCard
import com.example.myapplication.main.common.WrongReadingExample
import com.example.myapplication.main.common.PhonicsRuleBreakerCard
import com.example.myapplication.main.common.RuleBreakerEntry

private fun digraphColor(digraph: String): Color = when (digraph) {
    "ch" -> Color(0xFFFF7043)
    "sh" -> Color(0xFF29B6F6)
    "th" -> Color(0xFFAB47BC)
    "wh" -> Color(0xFF26C6DA)
    "ph" -> Color(0xFFEC407A)
    "qu" -> Color(0xFF66BB6A)
    else -> Color(0xFF78909C)
}

private fun digraphShadow(digraph: String): Color = when (digraph) {
    "ch" -> Color(0xFFBF360C)
    "sh" -> Color(0xFF0277BD)
    "th" -> Color(0xFF6A1B9A)
    "wh" -> Color(0xFF006064)
    "ph" -> Color(0xFF880E4F)
    "qu" -> Color(0xFF1B5E20)
    else -> Color(0xFF546E7A)
}

@Composable
fun DigraphsLearnPage(
    navController: NavController,
    viewModel: DigraphsLearnViewModel = hiltViewModel()
) {
    // Times this visit for the parent report (Phonics tab)
    PhonicsLearnSessionEffect(PhonicsListenLevelKey.digraphs)

    val uiState = viewModel.uiState

    DisposableEffect(Unit) { onDispose { viewModel.stop() } }

    LaunchedEffect(Unit) {
        if (uiState.selectedDigraph == null) viewModel.onDigraphTap(digraphsData.first())
    }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.peachCoral, shape = KidsFloatingShape.leaves)

        BoxWithConstraints(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing).fillMaxSize()) {
            val totalW = maxWidth
            Row(modifier = Modifier.fillMaxSize()) {

                // Left panel (30%): BackButton + all 6 digraphs with per-digraph colors
                Column(modifier = Modifier.width(totalW * 0.30f).fillMaxHeight()) {
                    BackButtonWithText(title = "Digraphs", onBackClick = { navController.popBackStack() })
                    Column(
                        modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())
                            .padding(horizontal = Dimens10, vertical = Dimens8),
                        verticalArrangement = Arrangement.spacedBy(Dimens8)
                    ) {
                        digraphsData.forEach { entry ->
                            val isSelected = uiState.selectedDigraph?.digraph == entry.digraph
                            val accent = digraphColor(entry.digraph)
                            val shadow = digraphShadow(entry.digraph)
                            Box(
                                modifier = Modifier.fillMaxWidth()
                                    .then(
                                        if (isSelected)
                                            Modifier.shadow(6.dp, RoundedCornerShape(Dimens12), clip = false, ambientColor = accent.copy(0.30f), spotColor = accent.copy(0.30f))
                                        else
                                            Modifier.shadow(2.dp, RoundedCornerShape(Dimens12), clip = false, ambientColor = accent.copy(0.08f), spotColor = accent.copy(0.08f))
                                    )
                                    .background(
                                        brush = if (isSelected)
                                            Brush.linearGradient(listOf(accent, shadow))
                                        else
                                            Brush.linearGradient(listOf(Color.White.copy(0.75f), Color.White.copy(0.75f))),
                                        shape = RoundedCornerShape(Dimens12)
                                    )
                                    .clickable { viewModel.onDigraphTap(entry) }
                                    .padding(horizontal = Dimens12, vertical = Dimens8)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Dimens8)) {
                                    Box(modifier = Modifier.size(Dimens8).background(if (isSelected) Color.White else accent, CircleShape))
                                    Text(
                                        text = entry.digraph.uppercase(),
                                        style = MaterialTheme.typography.titleLarge.scaled(),
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else accent
                                    )
                                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Dimens2)) {
                                        Text(entry.phonetic, style = MaterialTheme.typography.labelMedium.scaled(), color = if (isSelected) Color.White.copy(0.90f) else Color(0xFF546E7A))
                                        Text("${entry.words.size} words", style = MaterialTheme.typography.labelSmall.scaled(), color = if (isSelected) Color.White.copy(0.70f) else Color(0xFF90A4AE))
                                    }
                                    if (isSelected) {
                                        Icon(Icons.Default.ArrowForward, null, tint = Color.White.copy(0.80f), modifier = Modifier.size(Dimens14))
                                    }
                                }
                            }
                        }
                    }
                }

                // Right panel (70%): animated detail view
                Box(modifier = Modifier.fillMaxSize()) {
                    val entry = uiState.selectedDigraph
                    if (entry != null) {
                        AnimatedContent(
                            targetState = entry.digraph,
                            transitionSpec = { fadeIn(spring()) togetherWith fadeOut(spring()) },
                            label = "digraphDetail"
                        ) { _ ->
                            val e = uiState.selectedDigraph ?: entry
                            DigraphDetailView(
                                entry = e,
                                uiState = uiState,
                                onWordTap = { viewModel.onWordTap(it) },
                                onLetterSoundTap = { viewModel.onLetterSoundTap(it) },
                                onDigraphSoundTap = { viewModel.onDigraphSoundTap(e) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DigraphDetailView(
    entry: DigraphEntry,
    uiState: DigraphLearnUiState,
    onWordTap: (DigraphWord) -> Unit,
    onLetterSoundTap: (String) -> Unit,
    onDigraphSoundTap: () -> Unit
) {
    val accent = digraphColor(entry.digraph)
    val shadow = digraphShadow(entry.digraph)

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            .padding(horizontal = Dimens14, vertical = Dimens10).padding(bottom = Dimens20),
        verticalArrangement = Arrangement.spacedBy(Dimens14)
    ) {
        // Big Digraph Card — whole HStack in a single glass card (matching iOS)
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens12),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
                .kidsGlassCard(cornerRadius = Dimens12, strokeColor = accent)
                .padding(vertical = Dimens20, horizontal = Dimens14)
        ) {
            // Left: first letter (kidsColor) + second letter (kidsShadow) + phonetic capsule
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens8),
                modifier = Modifier.weight(1f).background(accent.copy(0.08f), RoundedCornerShape(Dimens12)).padding(vertical = Dimens20)
            ) {
                Row {
                    Text(
                        entry.digraph.first().uppercaseChar().toString(),
                        style = MaterialTheme.typography.displaySmall.scaled(),
                        fontWeight = FontWeight.ExtraBold,
                        color = accent
                    )
                    Text(
                        entry.digraph.last().uppercaseChar().toString(),
                        style = MaterialTheme.typography.displaySmall.scaled(),
                        fontWeight = FontWeight.ExtraBold,
                        color = shadow
                    )
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.background(Color(0xFFECEFF1), RoundedCornerShape(50)).padding(horizontal = Dimens8, vertical = Dimens2)
                ) {
                    Text(entry.phonetic, style = MaterialTheme.typography.bodyMedium.scaled(), color = Color(0xFF546E7A))
                }
            }

            // Right: Brand-new sound concept (no separate glass card — it's inside the outer one)
            Column(
                verticalArrangement = Arrangement.spacedBy(Dimens12),
                modifier = Modifier.weight(1f).padding(vertical = Dimens4, horizontal = Dimens4)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Dimens6)) {
                    Icon(Icons.Default.Star, null, tint = accent, modifier = Modifier.size(Dimens14))
                    Text("Brand-new sound", style = MaterialTheme.typography.labelLarge.scaled(), fontWeight = FontWeight.Medium, color = accent)
                }
                Text(entry.soundHint, style = MaterialTheme.typography.titleMedium.scaled(), fontWeight = FontWeight.Bold, color = Color(0xFF263238))

                // 2 separate letter circles (first=kidsColor, second=kidsShadow) + arrow + phonetic button
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Dimens8)) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(Dimens32).background(accent, CircleShape).clickable { onLetterSoundTap(entry.digraph.first().toString()) }
                    ) {
                        Text(entry.digraph.first().uppercaseChar().toString(), style = MaterialTheme.typography.labelLarge.scaled(), fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(Dimens32).background(shadow, CircleShape).clickable { onLetterSoundTap(entry.digraph.last().toString()) }
                    ) {
                        Text(entry.digraph.last().uppercaseChar().toString(), style = MaterialTheme.typography.labelLarge.scaled(), fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Icon(Icons.Default.ArrowForward, null, tint = Color(0xFF90A4AE), modifier = Modifier.size(Dimens20))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.shadow(4.dp, RoundedCornerShape(Dimens8), spotColor = accent.copy(0.4f))
                            .clip(RoundedCornerShape(Dimens8))
                            .background(Brush.linearGradient(listOf(accent, shadow)))
                            .clickable { onDigraphSoundTap() }
                            .padding(horizontal = Dimens10, vertical = Dimens4)
                    ) {
                        Text(entry.phonetic, style = MaterialTheme.typography.labelLarge.scaled(), fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        if (entry.whenRule.isNotEmpty()) {
            WhenRuleCard(entry = entry, accent = accent)
        }

        if (uiState.showWords) {
            DigraphWordsSection(entry = entry, accent = accent, shadow = shadow, uiState = uiState, onWordTap = onWordTap)
        }

        PhonicsWrongReadingCard(accentColor = accent, examples = digraphWrongReading(entry))

        digraphRuleBreakers(entry)?.let { PhonicsRuleBreakerCard(entries = it) }
    }
}

// ── When-Which-Sound Card (th) ────────────────────────────────────────────────

@Composable
private fun WhenRuleCard(entry: DigraphEntry, accent: Color) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimens8),
        modifier = Modifier.fillMaxWidth().kidsGlassCard(cornerRadius = Dimens12, strokeColor = accent).padding(Dimens12)
    ) {
        Text(
            entry.whenRuleTitle,
            style = MaterialTheme.typography.titleSmall.scaled(),
            fontWeight = FontWeight.Bold,
            color = accent
        )
        entry.whenRule.forEach { line ->
            // **marked** words rendered bold + tinted in the digraph's color
            Text(
                buildAnnotatedString {
                    line.split("**").forEachIndexed { index, part ->
                        if (index % 2 == 1) {
                            withStyle(SpanStyle(color = accent, fontWeight = FontWeight.Bold)) { append(part) }
                        } else {
                            withStyle(SpanStyle(color = Color(0xFF37474F))) { append(part) }
                        }
                    }
                },
                style = MaterialTheme.typography.labelMedium.scaled()
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DigraphWordsSection(
    entry: DigraphEntry,
    accent: Color,
    shadow: Color,
    uiState: DigraphLearnUiState,
    onWordTap: (DigraphWord) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimens10),
        modifier = Modifier.fillMaxWidth().kidsGlassCard(cornerRadius = Dimens12, strokeColor = accent).padding(Dimens14)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Dimens6)) {
            Icon(Icons.Default.TouchApp, null, tint = accent, modifier = Modifier.size(Dimens16))
            Text("Tap a word to hear it", style = MaterialTheme.typography.labelLarge.scaled(), fontWeight = FontWeight.Bold, color = accent)
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(Dimens8),
            verticalArrangement = Arrangement.spacedBy(Dimens8),
            itemVerticalAlignment = Alignment.CenterVertically
        ) {
            entry.words.forEach { word ->
                val isHighlighted = uiState.highlightedWordId == word.id
                Box(modifier = Modifier.scale(if (isHighlighted) 1.04f else 1.0f).clickable { onWordTap(word) }) {
                    Box(modifier = Modifier.matchParentSize().offset(y = 3.dp).background(shadow, RoundedCornerShape(Dimens8)))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .then(
                                if (isHighlighted) Modifier.shadow(6.dp, RoundedCornerShape(Dimens8), ambientColor = accent.copy(0.35f), spotColor = accent.copy(0.35f))
                                else Modifier.shadow(2.dp, RoundedCornerShape(Dimens8), ambientColor = accent.copy(0.08f), spotColor = accent.copy(0.08f))
                            )
                            .clip(RoundedCornerShape(Dimens8))
                            .background(
                                if (isHighlighted) Brush.linearGradient(listOf(accent, shadow))
                                else Brush.linearGradient(listOf(Color.White, Color.White))
                            )
                            .padding(horizontal = Dimens10, vertical = Dimens6)
                    ) {
                        Row {
                            Text(word.digraphPart, style = MaterialTheme.typography.bodyMedium.scaled(), fontWeight = FontWeight.Bold, color = if (isHighlighted) Color.White else accent)
                            Text(word.restPart, style = MaterialTheme.typography.bodyMedium.scaled(), fontWeight = FontWeight.Bold, color = if (isHighlighted) Color.White.copy(0.82f) else Color(0xFF263238).copy(0.55f))
                        }
                        if (word.note.isNotEmpty()) {
                            Text(
                                text = word.note,
                                style = MaterialTheme.typography.labelSmall.scaled(),
                                color = if (isHighlighted) Color.White.copy(0.75f) else accent.copy(0.65f)
                            )
                        }
                    }
                }
            }
        }
    }
}

// Per-digraph wrong-reading + rebels — follow the left-panel selection.
private fun digraphWrongReading(entry: DigraphEntry): List<WrongReadingExample> = when (entry.digraph) {
    "ch" -> listOf(WrongReadingExample("c·h·ip (two sounds)", "/ch/-ip — c+h team up!", "chip"))
    "sh" -> listOf(WrongReadingExample("s·h·ip (two sounds)", "/sh/-ip — sh is ONE new sound!", "ship"))
    "th" -> listOf(WrongReadingExample("t·h·in (two sounds)", "/th/-in — tongue peeks out!", "thin"))
    "wh" -> listOf(WrongReadingExample("w·h·ip (two sounds)", "/w/-ip — wh just says /w/!", "whip"))
    "ph" -> listOf(WrongReadingExample("p·h·one (p + h)", "/f/-ōn — ph says /f/!", "phone"))
    else -> listOf(WrongReadingExample("q·u·iz (q alone)", "/kw/-iz — q + u shout together!", "quiz"))
}

private fun digraphRuleBreakers(entry: DigraphEntry): List<RuleBreakerEntry>? =
    if (entry.digraph == "ch") listOf(RuleBreakerEntry("school", "ch says /k/ here — sneaky!")) else null
