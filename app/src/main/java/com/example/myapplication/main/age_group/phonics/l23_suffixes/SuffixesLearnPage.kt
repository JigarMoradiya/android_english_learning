package com.example.myapplication.main.age_group.phonics.l23_suffixes

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.myapplication.main.age_group.phonics.l23_suffixes.view_model.SuffixGroup
import com.example.myapplication.main.age_group.phonics.l23_suffixes.view_model.SuffixRule
import com.example.myapplication.main.age_group.phonics.l23_suffixes.view_model.SuffixWord
import com.example.myapplication.main.age_group.phonics.l23_suffixes.view_model.SuffixesLearnViewModel
import com.example.myapplication.main.age_group.phonics.l23_suffixes.view_model.suffixGroups
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

private val sfDropYColor = Color(0xFFE65100)
private val sfJustAddColor = Color(0xFF00695C)

private val SuffixRule.label: String get() = when (this) {
    SuffixRule.JUST_ADD -> "just add"
    SuffixRule.DROP_Y   -> "drop y → i"
}
private val SuffixRule.badgeColor: Color get() = when (this) {
    SuffixRule.JUST_ADD -> sfJustAddColor
    SuffixRule.DROP_Y   -> sfDropYColor
}
private val SuffixRule.icon: ImageVector get() = when (this) {
    SuffixRule.JUST_ADD -> Icons.Default.AddCircle
    SuffixRule.DROP_Y   -> Icons.Default.SwapHoriz
}

@Composable
fun SuffixesLearnPage(
    navController: NavController,
    viewModel: SuffixesLearnViewModel = hiltViewModel()
) {
    // Times this visit for the parent report (Phonics tab)
    PhonicsLearnSessionEffect(PhonicsListenLevelKey.suffixes)

    val uiState = viewModel.uiState
    val selectedGroup = viewModel.selectedGroup

    LaunchedEffect(Unit) { viewModel.onScreenAppear() }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.skyLavender, shape = KidsFloatingShape.stars)

        Row(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            // ── LEFT 26% ───────────────────────────────────────────────────────
            Column(modifier = Modifier.fillMaxWidth(0.26f).fillMaxHeight()) {
                BackButtonWithText(title = "Suffixes", onBackClick = { navController.popBackStack() })
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = Dimens10)
                        .padding(top = Dimens8, bottom = Dimens8),
                    verticalArrangement = Arrangement.spacedBy(Dimens8),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    suffixGroups.forEachIndexed { index, group ->
                        SFLGroupButton(
                            group      = group,
                            isSelected = uiState.selectedGroupIndex == index,
                            onClick    = { viewModel.onGroupTap(index) }
                        )
                    }
                    SFLRuleLegend(accentColor = selectedGroup.accentColor)
                }
            }

            // ── RIGHT 74% ──────────────────────────────────────────────────────
            AnimatedContent(
                targetState = selectedGroup,
                transitionSpec = { fadeIn(spring()) togetherWith fadeOut(spring()) },
                modifier = Modifier.weight(1f).fillMaxHeight(),
                label = "sfLearnContent"
            ) { group ->
                val hasDropY = group.words.any { it.rule == SuffixRule.DROP_Y }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(Dimens14),
                    verticalArrangement = Arrangement.spacedBy(Dimens12)
                ) {
                    SFLGroupHeader(group = group)
                    if (hasDropY) {
                        SFLRulesExplanation(group = group)
                    }
                    if (uiState.showWords) {
                        SFLWordGrid(
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
private fun SFLGroupButton(group: SuffixGroup, isSelected: Boolean, onClick: () -> Unit) {
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
                text       = group.suffix,
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
private fun SFLRuleLegend(accentColor: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .kidsGlassCard(cornerRadius = 10.dp, strokeColor = accentColor.copy(alpha = 0.30f))
            .padding(Dimens10),
        verticalArrangement = Arrangement.spacedBy(Dimens6)
    ) {
        Text(
            text       = "Spelling rules",
            style      = MaterialTheme.typography.labelSmall.scaled(),
            fontWeight = FontWeight.Bold,
            color      = Color(0xFF455A64)
        )
        Row(
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens6)
        ) {
            Icon(Icons.Default.AddCircle, contentDescription = null, tint = sfJustAddColor, modifier = Modifier.size(12.dp))
            Text(
                text  = "just add the suffix",
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = sfJustAddColor
            )
        }
        Row(
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens6)
        ) {
            Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = sfDropYColor, modifier = Modifier.size(12.dp))
            Text(
                text  = "drop y → add i first",
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = sfDropYColor
            )
        }
    }
}

@Composable
private fun SFLGroupHeader(group: SuffixGroup) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens14),
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = 12.dp, strokeColor = group.accentColor)
            .padding(Dimens14)
    ) {
        Box(
            modifier = Modifier
                .background(Brush.linearGradient(listOf(group.accentColor, group.shadowColor)), RoundedCornerShape(12.dp))
                .padding(horizontal = Dimens14, vertical = Dimens10),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text       = group.suffix,
                style      = MaterialTheme.typography.headlineLarge.scaled(),
                fontWeight = FontWeight.ExtraBold,
                color      = Color.White
            )
        }
        Text(text = group.emoji, style = MaterialTheme.typography.headlineMedium.scaled())
        Column(verticalArrangement = Arrangement.spacedBy(Dimens4)) {
            Text(
                text       = "${group.suffix} means \"${group.meaning}\"",
                style      = MaterialTheme.typography.titleMedium.scaled(),
                fontWeight = FontWeight.ExtraBold,
                color      = group.accentColor
            )
            Text(
                text  = "Add it AFTER the base word 👆",
                style = MaterialTheme.typography.bodySmall.scaled(),
                color = Color(0xFF455A64)
            )
        }
    }
}

@Composable
private fun SFLRulesExplanation(group: SuffixGroup) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens8),
        modifier = Modifier.fillMaxWidth()
    ) {
        // JUST_ADD tile
        Column(
            modifier = Modifier
                .weight(1f)
                .kidsGlassCard(cornerRadius = 10.dp, strokeColor = sfJustAddColor)
                .padding(Dimens10),
            verticalArrangement = Arrangement.spacedBy(Dimens4)
        ) {
            Text(
                text       = "JUST ADD",
                style      = MaterialTheme.typography.labelSmall.scaled(),
                fontWeight = FontWeight.ExtraBold,
                color      = sfJustAddColor
            )
            Text(
                text  = "WHEN: ends in consonant",
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = Color(0xFF455A64)
            )
            Text(
                text  = "HOW: base + suffix",
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = Color(0xFF455A64)
            )
            Text(
                text       = "help + ful = helpful",
                style      = MaterialTheme.typography.labelSmall.scaled(),
                fontWeight = FontWeight.Bold,
                color      = sfJustAddColor
            )
        }
        // DROP_Y tile
        Column(
            modifier = Modifier
                .weight(1f)
                .kidsGlassCard(cornerRadius = 10.dp, strokeColor = sfDropYColor)
                .padding(Dimens10),
            verticalArrangement = Arrangement.spacedBy(Dimens4)
        ) {
            Text(
                text       = "DROP Y",
                style      = MaterialTheme.typography.labelSmall.scaled(),
                fontWeight = FontWeight.ExtraBold,
                color      = sfDropYColor
            )
            Text(
                text  = "WHEN: base ends in Y",
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = Color(0xFF455A64)
            )
            Text(
                text  = "HOW: drop Y, add i + suffix",
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = Color(0xFF455A64)
            )
            Text(
                text       = "happy → happi + ness",
                style      = MaterialTheme.typography.labelSmall.scaled(),
                fontWeight = FontWeight.Bold,
                color      = sfDropYColor
            )
        }
    }
}

@Composable
private fun SFLWordGrid(
    group: SuffixGroup,
    highlightedWord: String?,
    onWordTap: (SuffixWord) -> Unit
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
                    SFLWordCard(
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
private fun SFLWordCard(
    modifier: Modifier = Modifier,
    word: SuffixWord,
    group: SuffixGroup,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val progress by animateFloatAsState(
        targetValue   = if (isActive) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 400f),
        label         = "sfCardProgress"
    )
    val cardShape   = RoundedCornerShape(Dimens8)
    val suffixLen   = group.suffixLen
    val rootLen     = word.full.length - suffixLen
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
                    color = lerp(Color(0xFF90A4AE), Color.White.copy(alpha = 0.80f), progress)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text       = word.full.take(rootLen),
                        style      = MaterialTheme.typography.bodyMedium.scaled(),
                        fontWeight = FontWeight.Bold,
                        color      = lerp(Color(0xFF263238), Color.White, progress)
                    )
                    Text(
                        text       = word.full.drop(rootLen),
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
                    Row(
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Dimens4),
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.20f), RoundedCornerShape(20.dp))
                            .padding(horizontal = Dimens8, vertical = Dimens2)
                    ) {
                        Icon(word.rule.icon, null, tint = Color(0xFFFFD54F), modifier = Modifier.size(11.dp))
                        Text(
                            text       = word.rule.label,
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
