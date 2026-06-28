package com.example.myapplication.main.age_group.phonics.l12_magic_e

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Divider
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
import com.example.myapplication.main.age_group.phonics.l12_magic_e.view_model.MagicEGroup
import com.example.myapplication.main.age_group.phonics.l12_magic_e.view_model.MagicELearnUiState
import com.example.myapplication.main.age_group.phonics.l12_magic_e.view_model.MagicELearnViewModel
import com.example.myapplication.main.age_group.phonics.l12_magic_e.view_model.MagicEWord
import com.example.myapplication.main.age_group.phonics.l12_magic_e.view_model.magicEGroups
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
import com.example.myapplication.utils.extensions.scaled

private val shortVowelColor = Color(0xFFFF7043)
private val silentEColor    = Color(0xFFFFC107)

@Composable
fun MagicELearnPage(navController: NavController) {
    val viewModel: MagicELearnViewModel = hiltViewModel()
    val uiState = viewModel.uiState

    DisposableEffect(Unit) { onDispose { viewModel.stop() } }
    LaunchedEffect(Unit) { viewModel.onScreenAppear() }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.lilacPink, shape = KidsFloatingShape.stars)

        Box(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            Row(modifier = Modifier.fillMaxSize()) {

                // ── LEFT: group list ─────────────────────────────────────────
                Column(
                    modifier = Modifier
                        .weight(0.30f)
                        .fillMaxHeight()
                ) {
                    BackButtonWithText(title = "Magic E", onBackClick = { navController.popBackStack() })

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = Dimens10, vertical = Dimens8),
                        verticalArrangement = Arrangement.spacedBy(Dimens8)
                    ) {
                        magicEGroups.forEach { group ->
                            MagicEGroupCard(
                                group = group,
                                isSelected = uiState.selectedGroup.id == group.id,
                                onClick = { viewModel.onGroupTap(group) }
                            )
                        }
                    }
                }

                // ── RIGHT: content ───────────────────────────────────────────
                AnimatedContent(
                    targetState = uiState.selectedGroup,
                    transitionSpec = {
                        fadeIn(spring()) togetherWith fadeOut(spring())
                    },
                    modifier = Modifier
                        .weight(0.70f)
                        .fillMaxHeight(),
                    label = "magicEGroupContent"
                ) { group ->
                    MagicEGroupContent(
                        group = group,
                        uiState = uiState,
                        onWordTap = { viewModel.onWordTap(it) }
                    )
                }
            }
        }
    }
}

// ── Left: Group Card ──────────────────────────────────────────────────────────

@Composable
private fun MagicEGroupCard(
    group: MagicEGroup,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bg = if (isSelected)
        Brush.linearGradient(listOf(group.accentColor, group.shadowColor))
    else
        Brush.linearGradient(listOf(Color.White.copy(0.75f), Color.White.copy(0.75f)))

    val shape = RoundedCornerShape(Dimens12)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(bg, shape)
            .clickable { onClick() }
            .padding(horizontal = Dimens12, vertical = Dimens10)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens4)) {

            // Header: emoji + vowel pair
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens6),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = group.emoji, style = MaterialTheme.typography.titleMedium.scaled())
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens2),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = group.vowel.toString(),
                        style = MaterialTheme.typography.titleMedium.scaled(),
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isSelected) Color.White else shortVowelColor
                    )
                    Text(
                        text = "→",
                        style = MaterialTheme.typography.bodySmall.scaled(),
                        color = if (isSelected) Color.White.copy(0.65f) else Color(0xFF90A4AE)
                    )
                    Text(
                        text = group.longLabel,
                        style = MaterialTheme.typography.titleMedium.scaled(),
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isSelected) Color.White else group.accentColor
                    )
                }
                Spacer(Modifier.weight(1f))
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = Color.White.copy(0.80f),
                        modifier = Modifier.height(Dimens14)
                    )
                }
            }

            // Mini example with gold E
            val first = group.words.firstOrNull()
            if (first != null) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens4),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = first.base,
                        style = MaterialTheme.typography.labelMedium.scaled(),
                        color = if (isSelected) Color.White.copy(0.70f) else Color(0xFF78909C)
                    )
                    Text(
                        text = "→",
                        style = MaterialTheme.typography.labelSmall.scaled(),
                        color = if (isSelected) Color.White.copy(0.50f) else Color(0xFFB0BEC5)
                    )
                    // Magic word with gold E
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(
                                color = if (isSelected) Color.White.copy(0.70f) else Color(0xFF78909C)
                            )) {
                                append(first.magic.dropLast(1))
                            }
                            withStyle(SpanStyle(color = silentEColor, fontWeight = FontWeight.Bold)) {
                                append("e")
                            }
                        },
                        style = MaterialTheme.typography.labelMedium.scaled()
                    )
                }
            }

            Text(
                text = "${group.words.size} word pairs",
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = if (isSelected) Color.White.copy(0.60f) else Color(0xFF90A4AE)
            )
        }
    }
}

// ── Right: Group Content ──────────────────────────────────────────────────────

@Composable
private fun MagicEGroupContent(
    group: MagicEGroup,
    uiState: MagicELearnUiState,
    onWordTap: (MagicEWord) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimens14),
        verticalArrangement = Arrangement.spacedBy(Dimens12)
    ) {
        // Rule banner
        MagicERuleBanner(group = group)

        // Word pair grid
        if (uiState.showWords) {
            MagicEWordPairsSection(
                group = group,
                highlightedWordId = uiState.highlightedWordId,
                onWordTap = onWordTap
            )
        }
    }
}

// ── Rule Banner ───────────────────────────────────────────────────────────────

@Composable
private fun MagicERuleBanner(group: MagicEGroup) {
    val example = group.words[0]

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = Dimens12, strokeColor = group.accentColor)
            .padding(Dimens14),
        horizontalArrangement = Arrangement.spacedBy(Dimens14),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Visual transformation
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            InlineWordChip(word = example, group = group, showMagic = false)

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "✨", style = MaterialTheme.typography.labelSmall.scaled())
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = group.accentColor,
                    modifier = Modifier.height(Dimens16)
                )
            }

            InlineWordChip(word = example, group = group, showMagic = true)
        }

        Divider(
            modifier = Modifier
                .height(Dimens24)
                .width(1.dp),
            color = group.accentColor.copy(0.25f)
        )

        // Rule text
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Dimens4)
        ) {
            Text(
                text = "🪄 The Magic E Rule",
                style = MaterialTheme.typography.labelLarge.scaled(),
                fontWeight = FontWeight.Bold,
                color = group.accentColor
            )
            Text(
                text = "Add silent E to the end — the vowel says its LONG name!",
                style = MaterialTheme.typography.labelMedium.scaled(),
                color = Color(0xFF455A64)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens6),
                verticalAlignment = Alignment.CenterVertically
            ) {
                VowelChip(label = group.vowel.toString(), color = shortVowelColor)
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Color(0xFF90A4AE),
                    modifier = Modifier.height(Dimens12)
                )
                VowelChip(label = group.longLabel, color = group.accentColor)
            }
        }
    }
}

@Composable
private fun InlineWordChip(word: MagicEWord, group: MagicEGroup, showMagic: Boolean) {
    val bgColor = if (showMagic) group.accentColor.copy(0.10f) else shortVowelColor.copy(0.10f)
    Column(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(Dimens8))
            .padding(horizontal = Dimens10, vertical = Dimens6),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens2)
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = Color(0xFF455A64))) { append(word.preVowel) }
                withStyle(SpanStyle(color = if (showMagic) group.accentColor else shortVowelColor)) {
                    append(word.vowelChar)
                }
                withStyle(SpanStyle(color = Color(0xFF455A64))) { append(word.postVowel) }
                if (showMagic) {
                    withStyle(SpanStyle(color = silentEColor, fontWeight = FontWeight.Bold)) { append("e") }
                }
            },
            style = MaterialTheme.typography.titleMedium.scaled(),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = if (showMagic) "long ${group.longLabel}" else "short ${group.vowel}",
            style = MaterialTheme.typography.labelSmall.scaled(),
            color = if (showMagic) group.accentColor.copy(0.75f) else shortVowelColor.copy(0.75f)
        )
    }
}

@Composable
private fun VowelChip(label: String, color: Color) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(color, RoundedCornerShape(50))
            .padding(horizontal = Dimens6, vertical = Dimens2)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.scaled(),
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

// ── Word Pairs Section ────────────────────────────────────────────────────────

@Composable
private fun MagicEWordPairsSection(
    group: MagicEGroup,
    highlightedWordId: String?,
    onWordTap: (MagicEWord) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = Dimens12, strokeColor = group.accentColor)
            .padding(Dimens14),
        verticalArrangement = Arrangement.spacedBy(Dimens10)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens6)
        ) {
            Icon(
                imageVector = Icons.Default.TouchApp,
                contentDescription = null,
                tint = group.accentColor,
                modifier = Modifier.height(Dimens16)
            )
            Text(
                text = "Tap a pair to hear both sounds",
                style = MaterialTheme.typography.titleSmall.scaled(),
                fontWeight = FontWeight.Bold,
                color = group.accentColor
            )
        }

        // 2-column grid via chunked rows
        group.words.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens8)
            ) {
                row.forEach { word ->
                    MagicEWordPairCard(
                        word = word,
                        group = group,
                        isHighlighted = highlightedWordId == word.id,
                        onTap = { onWordTap(word) },
                        modifier = Modifier.weight(1f)
                    )
                }
                // pad last row if odd count
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

// ── Word Pair Card ────────────────────────────────────────────────────────────

@Composable
private fun MagicEWordPairCard(
    word: MagicEWord,
    group: MagicEGroup,
    isHighlighted: Boolean,
    onTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val normalColor = if (isHighlighted) Color.White.copy(0.88f) else Color(0xFF263238)
    val bg = if (isHighlighted)
        androidx.compose.ui.graphics.Brush.linearGradient(listOf(group.accentColor, group.shadowColor))
    else
        androidx.compose.ui.graphics.Brush.linearGradient(listOf(Color.White, Color.White))

    Box(
        modifier = modifier.scale(if (isHighlighted) 1.03f else 1.0f)
    ) {
        // Shadow layer
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 3.dp)
                .background(group.shadowColor, RoundedCornerShape(Dimens10))
        )
        // Card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(if (isHighlighted) 8.dp else 2.dp, RoundedCornerShape(Dimens10))
                .background(bg, RoundedCornerShape(Dimens10))
                .clickable { onTap() }
                .padding(horizontal = Dimens10, vertical = Dimens8),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // BASE WORD — vowel in orange
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = normalColor)) { append(word.preVowel) }
                    withStyle(SpanStyle(color = if (isHighlighted) Color.White else shortVowelColor)) {
                        append(word.vowelChar)
                    }
                    withStyle(SpanStyle(color = normalColor)) { append(word.postVowel) }
                },
                style = MaterialTheme.typography.titleMedium.scaled(),
                fontWeight = FontWeight.Bold
            )

            // Sparkle arrow
            Row(
                modifier = Modifier.padding(horizontal = Dimens6),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens2)
            ) {
                Text(text = "✨", style = MaterialTheme.typography.labelSmall.scaled())
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = if (isHighlighted) Color.White.copy(0.80f) else group.accentColor,
                    modifier = Modifier.height(Dimens14)
                )
            }

            // MAGIC WORD — vowel in accent, silent E in gold
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = normalColor)) { append(word.preVowel) }
                    withStyle(SpanStyle(color = if (isHighlighted) Color.White else group.accentColor)) {
                        append(word.vowelChar)
                    }
                    withStyle(SpanStyle(color = normalColor)) { append(word.postVowel) }
                    withStyle(SpanStyle(
                        color = if (isHighlighted) silentEColor.copy(0.90f) else silentEColor,
                        fontWeight = FontWeight.Bold
                    )) {
                        append("e")
                    }
                },
                style = MaterialTheme.typography.titleMedium.scaled(),
                fontWeight = FontWeight.Bold
            )
        }
    }
}
