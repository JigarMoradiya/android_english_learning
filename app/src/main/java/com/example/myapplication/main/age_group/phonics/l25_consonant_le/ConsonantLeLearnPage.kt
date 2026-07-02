package com.example.myapplication.main.age_group.phonics.l25_consonant_le

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.l25_consonant_le.view_model.CLEGroup
import com.example.myapplication.main.age_group.phonics.l25_consonant_le.view_model.CLEWord
import com.example.myapplication.main.age_group.phonics.l25_consonant_le.view_model.ConsonantLeLearnViewModel
import com.example.myapplication.main.age_group.phonics.l25_consonant_le.view_model.cleGroups
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

private val cleAccent = Color(0xFFAD1457)

@Composable
fun ConsonantLeLearnPage(
    navController: NavController,
    viewModel: ConsonantLeLearnViewModel = hiltViewModel()
) {
    val uiState       = viewModel.uiState
    val selectedGroup = viewModel.selectedGroup

    LaunchedEffect(Unit) { viewModel.onScreenAppear() }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.rosePink, shape = KidsFloatingShape.bubbles)

        Row(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            // ── LEFT 26% ───────────────────────────────────────────────────────
            Column(modifier = Modifier.fillMaxWidth(0.26f).fillMaxHeight()) {
                BackButtonWithText(title = "Consonant + -le", onBackClick = { navController.popBackStack() })
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = Dimens10)
                        .padding(top = Dimens8, bottom = Dimens8),
                    verticalArrangement = Arrangement.spacedBy(Dimens8),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    cleGroups.forEachIndexed { index, group ->
                        CLELGroupButton(
                            group      = group,
                            isSelected = uiState.selectedGroupIndex == index,
                            onClick    = { viewModel.onGroupTap(index) }
                        )
                    }
                    CLELSilentELegend()
                }
            }

            // ── RIGHT 74% ──────────────────────────────────────────────────────
            AnimatedContent(
                targetState    = selectedGroup,
                transitionSpec = { fadeIn(spring()) togetherWith fadeOut(spring()) },
                modifier       = Modifier.weight(1f).fillMaxHeight(),
                label          = "cleLearnContent"
            ) { group ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(Dimens14),
                    verticalArrangement = Arrangement.spacedBy(Dimens12)
                ) {
                    CLELGroupHeader(group = group)
                    if (uiState.showWords) {
                        CLELWordGrid(
                            group        = group,
                            tappedWord   = uiState.tappedWordFull,
                            onWordTap    = { viewModel.onWordTap(it) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CLELGroupButton(group: CLEGroup, isSelected: Boolean, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val bg = if (isSelected)
        Modifier.background(Brush.linearGradient(listOf(group.accentColor, group.shadowColor)), RoundedCornerShape(12.dp))
    else
        Modifier.background(Color.White.copy(alpha = 0.75f), RoundedCornerShape(12.dp))
    val borderColor = if (isSelected) Color.White.copy(alpha = 0.30f) else group.accentColor.copy(alpha = 0.25f)

    Row(
        verticalAlignment     = Alignment.CenterVertically,
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
                text       = group.ending,
                style      = MaterialTheme.typography.titleSmall.scaled(),
                fontWeight = FontWeight.ExtraBold,
                color      = if (isSelected) Color.White else group.accentColor
            )
            Text(
                text     = group.rule,
                style    = MaterialTheme.typography.labelSmall.scaled(),
                color    = if (isSelected) Color.White.copy(alpha = 0.80f) else Color(0xFF78909C),
                maxLines = 2
            )
        }
    }
}

@Composable
private fun CLELSilentELegend() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .kidsGlassCard(cornerRadius = 10.dp, strokeColor = cleAccent.copy(alpha = 0.30f))
            .padding(Dimens10),
        verticalArrangement = Arrangement.spacedBy(Dimens6)
    ) {
        Text(
            text       = "Remember!",
            style      = MaterialTheme.typography.labelSmall.scaled(),
            fontWeight = FontWeight.ExtraBold,
            color      = cleAccent
        )
        Text(
            text  = "The final -e is silent.\nOnly the consonant + /l/ sound!",
            style = MaterialTheme.typography.labelSmall.scaled(),
            color = Color(0xFF455A64)
        )
        Text(
            text = buildAnnotatedString {
                pushStyle(SpanStyle(color = Color(0xFF263238), fontWeight = FontWeight.Bold))
                append("ap")
                pop()
                pushStyle(SpanStyle(color = Color(0xFF90A4AE)))
                append("•")
                pop()
                pushStyle(SpanStyle(color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold))
                append("ple")
                pop()
                pushStyle(SpanStyle(color = cleAccent, fontWeight = FontWeight.Bold))
                append(" = apple")
                pop()
            },
            style = MaterialTheme.typography.labelSmall.scaled()
        )
    }
}

@Composable
private fun CLELGroupHeader(group: CLEGroup) {
    Row(
        verticalAlignment     = Alignment.CenterVertically,
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
                text       = group.ending,
                style      = MaterialTheme.typography.headlineLarge.scaled(),
                fontWeight = FontWeight.ExtraBold,
                color      = Color.White
            )
        }
        Text(text = group.emoji, style = MaterialTheme.typography.headlineMedium.scaled())
        Column(verticalArrangement = Arrangement.spacedBy(Dimens4)) {
            Text(
                text       = group.ending,
                style      = MaterialTheme.typography.titleMedium.scaled(),
                fontWeight = FontWeight.ExtraBold,
                color      = group.accentColor
            )
            Text(
                text  = "Tap a card to hear the word 👆",
                style = MaterialTheme.typography.bodySmall.scaled(),
                color = Color(0xFF455A64)
            )
        }
    }
}

@Composable
private fun CLELWordGrid(
    group:      CLEGroup,
    tappedWord: String?,
    onWordTap:  (CLEWord) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimens8),
        modifier = Modifier.fillMaxWidth().padding(top = Dimens8, bottom = Dimens8)
    ) {
        group.words.chunked(2).forEach { pair ->
            Row(
                modifier              = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(Dimens8),
                verticalAlignment     = Alignment.CenterVertically
            ) {
                pair.forEach { word ->
                    CLELWordCard(
                        modifier  = Modifier.weight(1f),
                        word      = word,
                        group     = group,
                        isActive  = tappedWord == word.full,
                        onClick   = { onWordTap(word) }
                    )
                }
                if (pair.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun CLELWordCard(
    modifier:  Modifier = Modifier,
    word:      CLEWord,
    group:     CLEGroup,
    isActive:  Boolean,
    onClick:   () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val progress by animateFloatAsState(
        targetValue   = if (isActive) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 400f),
        label         = "cleCardProgress"
    )
    val cardShape   = RoundedCornerShape(Dimens8)
    val bgBrush     = Brush.linearGradient(listOf(
        lerp(Color.White, group.accentColor, progress),
        lerp(Color.White, group.shadowColor, progress)
    ))
    val borderColor = lerp(group.accentColor.copy(alpha = 0.20f), Color.White.copy(alpha = 0.35f), progress)
    val chipBg      = lerp(Color(0xFFECEFF1), Color.White.copy(alpha = 0.20f), progress)
    val chipText    = lerp(Color(0xFF607D8B), Color.White.copy(alpha = 0.80f), progress)

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
            // [first] • [ending] chips
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens4),
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(chipBg, RoundedCornerShape(20.dp))
                        .padding(horizontal = Dimens6, vertical = Dimens2),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = word.first, style = MaterialTheme.typography.labelSmall.scaled(), color = chipText)
                }
                Text(
                    text  = "•",
                    style = MaterialTheme.typography.labelSmall.scaled(),
                    color = lerp(Color(0xFF90A4AE), Color.White.copy(alpha = 0.60f), progress)
                )
                Box(
                    modifier = Modifier
                        .background(chipBg, RoundedCornerShape(20.dp))
                        .padding(horizontal = Dimens6, vertical = Dimens2),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = word.ending, style = MaterialTheme.typography.labelSmall.scaled(),
                        color = lerp(group.accentColor, Color(0xFFFFD54F), progress))
                }
            }
            Text(
                text  = "↓",
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = lerp(Color(0xFF90A4AE), Color.White.copy(alpha = 0.70f), progress)
            )
            // Full word: first part + ending highlighted
            Text(
                text = buildAnnotatedString {
                    pushStyle(SpanStyle(
                        color      = lerp(Color(0xFF263238), Color.White, progress),
                        fontWeight = FontWeight.Bold
                    ))
                    append(word.first)
                    pop()
                    pushStyle(SpanStyle(
                        color      = lerp(group.accentColor, Color(0xFFFFD54F), progress),
                        fontWeight = FontWeight.Bold
                    ))
                    append(word.ending)
                    pop()
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
                    text       = "🔇 e is silent · ${group.rule}",
                    style      = MaterialTheme.typography.labelSmall.scaled(),
                    color      = Color(0xFFFFD54F),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
