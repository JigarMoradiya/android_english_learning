package com.example.myapplication.main.age_group.phonics.l22_prefixes

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.PhonicsLearnSessionEffect
import com.example.myapplication.main.age_group.phonics.listen.view_model.PhonicsListenLevelKey
import com.example.myapplication.main.age_group.phonics.l22_prefixes.view_model.PrefixGroup
import com.example.myapplication.main.age_group.phonics.l22_prefixes.view_model.PrefixWord
import com.example.myapplication.main.age_group.phonics.l22_prefixes.view_model.PrefixesLearnViewModel
import com.example.myapplication.main.age_group.phonics.l22_prefixes.view_model.prefixGroups
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
import com.example.myapplication.main.common.PhonicsIntroAudioViewModel
import androidx.compose.ui.draw.clip

@Composable
fun PrefixesLearnPage(
    navController: NavController,
    viewModel: PrefixesLearnViewModel = hiltViewModel()
) {
    // Times this visit for the parent report (Phonics tab)
    PhonicsLearnSessionEffect(PhonicsListenLevelKey.prefixes)

    val uiState = viewModel.uiState
    val selectedGroup = viewModel.selectedGroup

    LaunchedEffect(Unit) { viewModel.onScreenAppear() }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.blueIndigo, shape = KidsFloatingShape.sparkles)

        Row(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            // ── LEFT 26% ───────────────────────────────────────────────────────
            Column(modifier = Modifier.fillMaxWidth(0.26f).fillMaxHeight()) {
                BackButtonWithText(title = "Prefixes", onBackClick = { navController.popBackStack() })
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = Dimens10)
                        .padding(top = Dimens8, bottom = Dimens8),
                    verticalArrangement = Arrangement.spacedBy(Dimens8),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    prefixGroups.forEachIndexed { index, group ->
                        PFLGroupButton(
                            group      = group,
                            isSelected = uiState.selectedGroupIndex == index,
                            onClick    = { viewModel.onGroupTap(index) }
                        )
                    }
                    PFLMeaningLegend(accentColor = selectedGroup.accentColor)
                }
            }

            // ── RIGHT 74% ──────────────────────────────────────────────────────
            AnimatedContent(
                targetState = selectedGroup,
                transitionSpec = { fadeIn(spring()) togetherWith fadeOut(spring()) },
                modifier = Modifier.weight(1f).fillMaxHeight(),
                label = "pfLearnContent"
            ) { group ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(Dimens14),
                    verticalArrangement = Arrangement.spacedBy(Dimens12)
                ) {
                    PFLGroupHeader(group = group)
                    if (uiState.showWords) {
                        PFLWordGrid(
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
private fun PFLGroupButton(group: PrefixGroup, isSelected: Boolean, onClick: () -> Unit) {
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
                text       = group.displayPrefix,
                style      = MaterialTheme.typography.titleSmall.scaled(),
                fontWeight = FontWeight.ExtraBold,
                color      = if (isSelected) Color.White else group.accentColor
            )
            Text(
                text  = group.meaning,
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = if (isSelected) Color.White.copy(alpha = 0.80f) else Color(0xFF78909C)
            )
        }
    }
}

@Composable
private fun PFLMeaningLegend(accentColor: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .kidsGlassCard(cornerRadius = 10.dp, strokeColor = accentColor.copy(alpha = 0.30f))
            .padding(Dimens10),
        verticalArrangement = Arrangement.spacedBy(Dimens6)
    ) {
        Text(
            text       = "What each means",
            style      = MaterialTheme.typography.labelSmall.scaled(),
            fontWeight = FontWeight.Bold,
            color      = Color(0xFF455A64)
        )
        prefixGroups.forEach { g ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens6)
            ) {
                Text(
                    text       = g.displayPrefix,
                    style      = MaterialTheme.typography.labelSmall.scaled(),
                    fontWeight = FontWeight.ExtraBold,
                    color      = g.accentColor,
                    modifier   = Modifier.width(36.dp)
                )
                Text(
                    text  = g.meaning,
                    style = MaterialTheme.typography.labelSmall.scaled(),
                    color = Color(0xFF455A64)
                )
            }
        }
    }
}

@Composable
private fun PFLGroupHeader(group: PrefixGroup) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens14),
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = 12.dp, strokeColor = group.accentColor)
            .padding(Dimens14)
    ) {
        val badgeAudioVm: PhonicsIntroAudioViewModel = hiltViewModel()
        Box(
            modifier = Modifier
                .background(Brush.linearGradient(listOf(group.accentColor, group.shadowColor)), RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .clickable { badgeAudioVm.play(group.prefix) }
                .padding(horizontal = Dimens14, vertical = Dimens10),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text       = group.displayPrefix,
                style      = MaterialTheme.typography.headlineLarge.scaled(),
                fontWeight = FontWeight.ExtraBold,
                color      = Color.White
            )
        }
        Text(text = group.emoji, style = MaterialTheme.typography.headlineMedium.scaled())
        Column(verticalArrangement = Arrangement.spacedBy(Dimens4)) {
            Text(
                text       = "${group.displayPrefix} means \"${group.meaning}\"",
                style      = MaterialTheme.typography.titleMedium.scaled(),
                fontWeight = FontWeight.ExtraBold,
                color      = group.accentColor
            )
            Text(
                text  = "Add it BEFORE the word — no spelling change! 👆",
                style = MaterialTheme.typography.bodySmall.scaled(),
                color = Color(0xFF455A64)
            )
        }
    }
}

@Composable
private fun PFLWordGrid(
    group: PrefixGroup,
    highlightedWord: String?,
    onWordTap: (PrefixWord) -> Unit
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
                    PFLWordCard(
                        modifier = Modifier.weight(1f),
                        word     = word,
                        group    = group,
                        isActive = highlightedWord == word.full,
                        onClick  = { onWordTap(word) }
                    )
                }
                if (pair.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun PFLWordCard(
    modifier: Modifier = Modifier,
    word: PrefixWord,
    group: PrefixGroup,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val progress by animateFloatAsState(
        targetValue   = if (isActive) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 400f),
        label         = "pfCardProgress"
    )
    val cardShape   = RoundedCornerShape(Dimens8)
    val prefixLen   = group.prefix.length
    val bgBrush     = Brush.linearGradient(listOf(
        lerp(Color.White, group.accentColor, progress),
        lerp(Color.White, group.shadowColor, progress)
    ))
    val borderColor = lerp(group.accentColor.copy(alpha = 0.20f), Color.White.copy(alpha = 0.35f), progress)

    Box(modifier = modifier.scale(1f + 0.04f * progress)) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 3.dp)
                .background(group.shadowColor, cardShape)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(bgBrush, cardShape)
                .border(1.5.dp, borderColor, cardShape)
                .clickable(interactionSource = interactionSource, indication = null) { onClick() }
                .padding(vertical = Dimens12, horizontal = Dimens8)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens8)
            ) {
                Text(
                    text       = word.full,
                    style      = MaterialTheme.typography.bodyMedium.scaled(),
                    fontWeight = FontWeight.Bold,
                    color      = lerp(Color(0xFF455A64), Color(0xFFECEFF1), progress),
                    modifier   = Modifier
                        .background(
                            lerp(Color(0xFFECEFF1), Color.White.copy(alpha = 0.20f), progress),
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = Dimens8, vertical = Dimens4)
                )
                Text(
                    text  = "→",
                    style = MaterialTheme.typography.labelSmall.scaled(),
                    color = lerp(group.accentColor, Color.White.copy(alpha = 0.80f), progress)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text       = word.full.take(prefixLen),
                        style      = MaterialTheme.typography.bodyMedium.scaled(),
                        fontWeight = FontWeight.ExtraBold,
                        color      = lerp(group.accentColor, Color(0xFFFFD54F), progress),
                        modifier   = Modifier
                            .background(
                                lerp(group.accentColor.copy(alpha = 0.15f), Color.White.copy(alpha = 0.28f), progress),
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = Dimens4, vertical = Dimens2)
                    )
                    Text(
                        text       = word.full.drop(prefixLen),
                        style      = MaterialTheme.typography.bodyMedium.scaled(),
                        fontWeight = FontWeight.Bold,
                        color      = lerp(Color(0xFF263238), Color.White, progress)
                    )
                }
            }
            AnimatedVisibility(
                visible = isActive,
                enter = expandVertically(spring(dampingRatio = 0.65f, stiffness = 400f)) +
                        fadeIn(spring(dampingRatio = 0.65f, stiffness = 400f)),
                exit  = shrinkVertically(spring(dampingRatio = 0.65f, stiffness = 400f)) +
                        fadeOut(spring(dampingRatio = 0.65f, stiffness = 400f))
            ) {
                Column {
                    Spacer(modifier = Modifier.height(Dimens6))
                    Box(
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.20f), RoundedCornerShape(20.dp))
                            .padding(horizontal = Dimens8, vertical = Dimens2),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text       = "${group.emoji} ${group.meaning.lowercase()} ${word.base}",
                            style      = MaterialTheme.typography.labelSmall.scaled(),
                            fontWeight = FontWeight.Bold,
                            color      = Color(0xFFFFD54F)
                        )
                    }
                }
            }
        }
    }
}
