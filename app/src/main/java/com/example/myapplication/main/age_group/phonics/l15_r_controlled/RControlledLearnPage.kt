package com.example.myapplication.main.age_group.phonics.l15_r_controlled

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.safeDrawing
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
import com.example.myapplication.main.age_group.phonics.PhonicsLearnSessionEffect
import com.example.myapplication.main.age_group.phonics.listen.view_model.PhonicsListenLevelKey
import com.example.myapplication.main.age_group.phonics.l15_r_controlled.view_model.RControlledGroup
import com.example.myapplication.main.age_group.phonics.l15_r_controlled.view_model.RControlledLearnUiState
import com.example.myapplication.main.age_group.phonics.l15_r_controlled.view_model.RControlledLearnViewModel
import com.example.myapplication.main.age_group.phonics.l15_r_controlled.view_model.RControlledWord
import com.example.myapplication.main.age_group.phonics.l15_r_controlled.view_model.rControlledGroups
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

private val bossyRColor = Color(0xFFC62828)

@Composable
fun RControlledLearnPage(navController: NavController) {
    // Times this visit for the parent report (Phonics tab)
    PhonicsLearnSessionEffect(PhonicsListenLevelKey.rControlled)

    val viewModel: RControlledLearnViewModel = hiltViewModel()
    val uiState = viewModel.uiState

    DisposableEffect(Unit) { onDispose { viewModel.stop() } }
    LaunchedEffect(Unit) { viewModel.onScreenAppear() }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.meadowGreen, shape = KidsFloatingShape.stars)

        Box(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            Row(modifier = Modifier.fillMaxSize()) {

                // ── LEFT: group list ─────────────────────────────────────────
                Column(
                    modifier = Modifier
                        .weight(0.25f)
                        .fillMaxHeight()
                ) {
                    BackButtonWithText(title = "R-Controlled", onBackClick = { navController.popBackStack() })

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = Dimens10, vertical = Dimens8),
                        verticalArrangement = Arrangement.spacedBy(Dimens6)
                    ) {
                        rControlledGroups.forEach { group ->
                            RControlledGroupCard(
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
                    transitionSpec = { fadeIn(spring()) togetherWith fadeOut(spring()) },
                    modifier = Modifier
                        .weight(0.75f)
                        .fillMaxHeight(),
                    label = "rControlledGroupContent"
                ) { group ->
                    RControlledGroupContent(
                        group = group,
                        uiState = uiState,
                        onWordTap = { viewModel.onWordTap(it) }
                    )
                }
            }
        }
    }
}

// ── Group Card ────────────────────────────────────────────────────────────────

@Composable
private fun RControlledGroupCard(
    group: RControlledGroup,
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
            .padding(horizontal = Dimens10, vertical = Dimens8)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens2)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens6),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = group.emoji, style = MaterialTheme.typography.bodyLarge.scaled())
                Text(
                    text = group.rTeam,
                    style = MaterialTheme.typography.titleMedium.scaled(),
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isSelected) Color.White else group.accentColor
                )
                Spacer(Modifier.weight(1f))
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = Color.White.copy(0.80f),
                        modifier = Modifier.height(Dimens12)
                    )
                }
            }
            Text(
                text = group.sound,
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = if (isSelected) Color.White.copy(0.80f) else Color(0xFF78909C)
            )
        }
    }
}

// ── Group Content ─────────────────────────────────────────────────────────────

@Composable
private fun RControlledGroupContent(
    group: RControlledGroup,
    uiState: RControlledLearnUiState,
    onWordTap: (RControlledWord) -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .heightIn(min = this@BoxWithConstraints.maxHeight)
                .padding(Dimens14),
            verticalArrangement = Arrangement.spacedBy(Dimens12, Alignment.CenterVertically)
        ) {
            RControlledRuleBanner(group = group)

            if (uiState.showWords) {
                RControlledWordGrid(
                    group = group,
                    highlightedWordId = uiState.highlightedWordId,
                    onWordTap = onWordTap
                )
            }
        }
    }
}

// ── Rule Banner ───────────────────────────────────────────────────────────────

@Composable
private fun RControlledRuleBanner(group: RControlledGroup) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = Dimens12, strokeColor = group.accentColor)
            .padding(Dimens14),
        horizontalArrangement = Arrangement.spacedBy(Dimens14),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // rTeam badge + arrow + vowelChar(accentColor) + R(red) + sound
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens6)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(group.accentColor, RoundedCornerShape(Dimens8))
                    .padding(horizontal = Dimens12, vertical = Dimens6)
            ) {
                Text(
                    text = group.rTeam,
                    style = MaterialTheme.typography.titleLarge.scaled(),
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = group.accentColor,
                modifier = Modifier.height(Dimens16)
            )
            // vowelChar + R visual
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens2),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(group.accentColor.copy(alpha = 0.15f), RoundedCornerShape(Dimens6))
                        .padding(horizontal = Dimens8, vertical = Dimens4)
                ) {
                    Text(
                        text = group.rTeam.first().toString(),
                        style = MaterialTheme.typography.titleMedium.scaled(),
                        fontWeight = FontWeight.ExtraBold,
                        color = group.accentColor
                    )
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(bossyRColor.copy(alpha = 0.15f), RoundedCornerShape(Dimens6))
                        .padding(horizontal = Dimens8, vertical = Dimens4)
                ) {
                    Text(
                        text = "r",
                        style = MaterialTheme.typography.titleMedium.scaled(),
                        fontWeight = FontWeight.ExtraBold,
                        color = bossyRColor
                    )
                }
            }
        }

        Divider(
            modifier = Modifier
                .height(Dimens24)
                .width(1.dp),
            color = group.accentColor.copy(0.25f)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Dimens4)
        ) {
            Text(
                text = "👑 Bossy R Rule",
                style = MaterialTheme.typography.labelLarge.scaled(),
                fontWeight = FontWeight.Bold,
                color = group.accentColor
            )
            Text(
                text = "R changes '${group.rTeam.first()}' to ${group.sound}",
                style = MaterialTheme.typography.labelMedium.scaled(),
                color = Color(0xFF455A64)
            )
            Text(
                text = "Example: ${group.words.firstOrNull()?.word ?: ""}",
                style = MaterialTheme.typography.labelMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = group.accentColor
            )
        }
    }
}

// ── Word Grid ─────────────────────────────────────────────────────────────────

@Composable
private fun RControlledWordGrid(
    group: RControlledGroup,
    highlightedWordId: String?,
    onWordTap: (RControlledWord) -> Unit
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
                text = "Tap a word to hear it",
                style = MaterialTheme.typography.titleSmall.scaled(),
                fontWeight = FontWeight.Bold,
                color = group.accentColor
            )
        }

        // 3-column grid
        group.words.chunked(3).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens8)
            ) {
                row.forEach { word ->
                    RControlledWordCard(
                        word = word,
                        group = group,
                        isHighlighted = highlightedWordId == word.id,
                        onTap = { onWordTap(word) },
                        modifier = Modifier.weight(1f)
                    )
                }
                repeat(3 - row.size) { Spacer(Modifier.weight(1f)) }
            }
        }
    }
}

// ── Word Card ─────────────────────────────────────────────────────────────────

@Composable
private fun RControlledWordCard(
    word: RControlledWord,
    group: RControlledGroup,
    isHighlighted: Boolean,
    onTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val normalColor = if (isHighlighted) Color.White.copy(0.88f) else Color(0xFF263238)
    val bg = if (isHighlighted)
        Brush.linearGradient(listOf(group.accentColor, group.shadowColor))
    else
        Brush.linearGradient(listOf(Color.White, Color.White))

    Box(
        modifier = modifier
            .scale(if (isHighlighted) 1.03f else 1.0f)
            .shadow(if (isHighlighted) 8.dp else 2.dp, RoundedCornerShape(Dimens10))
            .background(bg, RoundedCornerShape(Dimens10))
            .clickable { onTap() }
            .padding(horizontal = Dimens8, vertical = Dimens10),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens4)
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = normalColor)) { append(word.pre) }
                    withStyle(SpanStyle(
                        color = if (isHighlighted) Color.White else group.accentColor,
                        fontWeight = FontWeight.ExtraBold
                    )) { append(word.vowelChar) }
                    withStyle(SpanStyle(
                        color = if (isHighlighted) Color.White.copy(0.90f) else bossyRColor,
                        fontWeight = FontWeight.ExtraBold
                    )) { append("r") }
                    withStyle(SpanStyle(color = normalColor)) { append(word.postTeam) }
                },
                style = MaterialTheme.typography.titleMedium.scaled(),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "👑",
                style = MaterialTheme.typography.labelSmall.scaled()
            )
        }
    }
}
