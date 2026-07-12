package com.example.myapplication.main.age_group.phonics.l24_contractions

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.PhonicsLearnSessionEffect
import com.example.myapplication.main.age_group.phonics.listen.view_model.PhonicsListenLevelKey
import com.example.myapplication.main.age_group.phonics.l24_contractions.view_model.ContractionGroup
import com.example.myapplication.main.age_group.phonics.l24_contractions.view_model.ContractionWord
import com.example.myapplication.main.age_group.phonics.l24_contractions.view_model.ContractionsLearnViewModel
import com.example.myapplication.main.age_group.phonics.l24_contractions.view_model.contractionGroups
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
import com.example.myapplication.utils.extensions.scaled

private val ctPurple = Color(0xFF8E24AA)

@Composable
fun ContractionsLearnPage(
    navController: NavController,
    viewModel: ContractionsLearnViewModel = hiltViewModel()
) {
    // Times this visit for the parent report (Phonics tab)
    PhonicsLearnSessionEffect(PhonicsListenLevelKey.contractions)

    val uiState = viewModel.uiState
    val selectedGroup = viewModel.selectedGroup

    LaunchedEffect(Unit) { viewModel.onScreenAppear() }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.lilacPink, shape = KidsFloatingShape.speechBubbles)

        Row(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            // ── LEFT 26% ───────────────────────────────────────────────────────
            Column(modifier = Modifier.fillMaxWidth(0.26f).fillMaxHeight()) {
                BackButtonWithText(title = "Contractions", onBackClick = { navController.popBackStack() })
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = Dimens10)
                        .padding(top = Dimens8, bottom = Dimens8),
                    verticalArrangement = Arrangement.spacedBy(Dimens8),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    contractionGroups.forEachIndexed { index, group ->
                        CTLGroupButton(
                            group      = group,
                            isSelected = uiState.selectedGroupIndex == index,
                            onClick    = { viewModel.onGroupTap(index) }
                        )
                    }
                    CTLApostropheLegend(accentColor = selectedGroup.accentColor)
                }
            }

            // ── RIGHT 74% ──────────────────────────────────────────────────────
            AnimatedContent(
                targetState = selectedGroup,
                transitionSpec = { fadeIn(spring()) togetherWith fadeOut(spring()) },
                modifier = Modifier.weight(1f).fillMaxHeight(),
                label = "ctLearnContent"
            ) { group ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(Dimens14),
                    verticalArrangement = Arrangement.spacedBy(Dimens12)
                ) {
                    CTLGroupHeader(group = group)
                    if (uiState.showWords) {
                        CTLWordGrid(
                            group           = group,
                            highlightedWord = uiState.highlightedWord,
                            onWordTap       = { viewModel.onWordTap(it) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CTLGroupButton(group: ContractionGroup, isSelected: Boolean, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val bg = if (isSelected)
        Modifier.background(Brush.linearGradient(listOf(group.accentColor, group.shadowColor)), RoundedCornerShape(12.dp))
    else
        Modifier.background(Color.White.copy(alpha = 0.75f), RoundedCornerShape(12.dp))
    val borderColor = if (isSelected) Color.White.copy(alpha = 0.30f) else group.accentColor.copy(alpha = 0.25f)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens8),
        modifier = Modifier
            .fillMaxWidth()
            .then(bg)
            .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .padding(vertical = Dimens8, horizontal = Dimens10)
    ) {
        Text(text = group.emoji, style = MaterialTheme.typography.bodyMedium.scaled())
        Column(verticalArrangement = Arrangement.spacedBy(Dimens2)) {
            Text(
                text       = group.type,
                style      = MaterialTheme.typography.titleSmall.scaled(),
                fontWeight = FontWeight.ExtraBold,
                color      = if (isSelected) Color.White else group.accentColor
            )
            Text(
                text  = group.rule,
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = if (isSelected) Color.White.copy(alpha = 0.80f) else Color(0xFF78909C),
                maxLines = 2
            )
        }
    }
}

@Composable
private fun CTLApostropheLegend(accentColor: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .kidsGlassCard(cornerRadius = 10.dp, strokeColor = accentColor.copy(alpha = 0.30f))
            .padding(Dimens10),
        verticalArrangement = Arrangement.spacedBy(Dimens6)
    ) {
        Text(
            text       = "Remember!",
            style      = MaterialTheme.typography.labelSmall.scaled(),
            fontWeight = FontWeight.ExtraBold,
            color      = ctPurple
        )
        Text(
            text  = "The ' apostrophe shows where letters were removed",
            style = MaterialTheme.typography.labelSmall.scaled(),
            color = Color(0xFF455A64)
        )
        Text(
            text = buildAnnotatedString {
                append("do + ")
                withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough, color = Color(0xFF90A4AE))) {
                    append("not")
                }
                append(" = don't")
            },
            style      = MaterialTheme.typography.labelSmall.scaled(),
            fontWeight = FontWeight.Bold,
            color      = ctPurple
        )
    }
}

@Composable
private fun CTLGroupHeader(group: ContractionGroup) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens14),
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = 12.dp, strokeColor = group.accentColor)
            .padding(Dimens14)
    ) {
        // apostrophe ' badge with gradient bg
        Box(
            modifier = Modifier
                .background(Brush.linearGradient(listOf(group.accentColor, group.shadowColor)), RoundedCornerShape(12.dp))
                .padding(horizontal = Dimens14, vertical = Dimens10),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text       = "'",
                style      = MaterialTheme.typography.headlineLarge.scaled(),
                fontWeight = FontWeight.ExtraBold,
                color      = Color.White
            )
        }
        Text(text = group.emoji, style = MaterialTheme.typography.headlineMedium.scaled())
        Column(verticalArrangement = Arrangement.spacedBy(Dimens4)) {
            Text(
                text       = group.type,
                style      = MaterialTheme.typography.titleMedium.scaled(),
                fontWeight = FontWeight.ExtraBold,
                color      = group.accentColor
            )
            Text(
                text  = "Tap a card to hear and see how it's made 👆",
                style = MaterialTheme.typography.bodySmall.scaled(),
                color = Color(0xFF455A64)
            )
        }
    }
}

@Composable
private fun CTLWordGrid(
    group: ContractionGroup,
    highlightedWord: String?,
    onWordTap: (ContractionWord) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimens8),
        modifier = Modifier.fillMaxWidth().padding(top = Dimens8, bottom = Dimens8)
    ) {
        group.words.chunked(2).forEach { pair ->
            Row(
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(Dimens8),
                verticalAlignment = Alignment.CenterVertically
            ) {
                pair.forEach { word ->
                    CTLWordCard(
                        modifier = Modifier.weight(1f),
                        word     = word,
                        group    = group,
                        isActive = highlightedWord == word.contraction,
                        onClick  = { onWordTap(word) }
                    )
                }
                if (pair.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun CTLWordCard(
    modifier: Modifier = Modifier,
    word: ContractionWord,
    group: ContractionGroup,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val progress by animateFloatAsState(
        targetValue   = if (isActive) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 400f),
        label         = "ctCardProgress"
    )
    val cardShape     = RoundedCornerShape(Dimens8)
    val apostropheIdx = word.contraction.indexOf('\'')
    val bgBrush       = Brush.linearGradient(listOf(
        lerp(Color.White, group.accentColor, progress),
        lerp(Color.White, group.shadowColor, progress)
    ))
    val borderColor   = lerp(group.accentColor.copy(alpha = 0.20f), Color.White.copy(alpha = 0.35f), progress)
    val chipBg        = lerp(Color(0xFFECEFF1), Color.White.copy(alpha = 0.20f), progress)
    val chipText      = lerp(Color(0xFF607D8B), Color.White.copy(alpha = 0.80f), progress)

    Box(modifier = modifier.scale(1f + 0.04f * progress)) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 3.dp)
                .background(group.shadowColor, cardShape)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens4),
            modifier = Modifier
                .fillMaxWidth()
                .background(bgBrush, cardShape)
                .border(1.5.dp, borderColor, cardShape)
                .clickable(interactionSource = interactionSource, indication = null) { onClick() }
                .padding(vertical = Dimens8, horizontal = Dimens8)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens4),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(chipBg, RoundedCornerShape(20.dp))
                        .padding(horizontal = Dimens6, vertical = Dimens2),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = word.word1, style = MaterialTheme.typography.labelSmall.scaled(), color = chipText)
                }
                Text(
                    text  = "+",
                    style = MaterialTheme.typography.labelSmall.scaled(),
                    color = lerp(Color(0xFF90A4AE), Color.White.copy(alpha = 0.60f), progress)
                )
                Box(
                    modifier = Modifier
                        .background(chipBg, RoundedCornerShape(20.dp))
                        .padding(horizontal = Dimens6, vertical = Dimens2),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = word.word2, style = MaterialTheme.typography.labelSmall.scaled(), color = chipText)
                }
            }
            Text(
                text  = "↓",
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = lerp(Color(0xFF90A4AE), Color.White.copy(alpha = 0.70f), progress)
            )
            Text(
                text = buildAnnotatedString {
                    word.contraction.forEachIndexed { idx, ch ->
                        val isApostrophe = idx == apostropheIdx
                        withStyle(
                            SpanStyle(
                                color = if (isApostrophe) lerp(group.accentColor, Color(0xFFFFD54F), progress)
                                        else lerp(Color(0xFF263238), Color.White, progress),
                                fontWeight = if (isApostrophe) FontWeight.ExtraBold else FontWeight.Bold
                            )
                        ) { append(ch.toString()) }
                    }
                },
                style = MaterialTheme.typography.bodyLarge.scaled()
            )
            AnimatedVisibility(
                visible = isActive,
                enter = expandVertically(spring(dampingRatio = 0.65f, stiffness = 400f)) +
                        fadeIn(spring(dampingRatio = 0.65f, stiffness = 400f)),
                exit  = shrinkVertically(spring(dampingRatio = 0.65f, stiffness = 400f)) +
                        fadeOut(spring(dampingRatio = 0.65f, stiffness = 400f))
            ) {
                Text(
                    text  = if (word.isIrregular) "⚠️ irregular form!" else "removes '${word.dropped}' → adds '",
                    style = MaterialTheme.typography.labelSmall.scaled(),
                    color = Color(0xFFFFD54F),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
