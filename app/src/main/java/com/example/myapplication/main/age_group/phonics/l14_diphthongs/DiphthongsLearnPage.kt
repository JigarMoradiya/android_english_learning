package com.example.myapplication.main.age_group.phonics.l14_diphthongs

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.l14_diphthongs.view_model.DiphthongGroup
import com.example.myapplication.main.age_group.phonics.l14_diphthongs.view_model.DiphthongWord
import com.example.myapplication.main.age_group.phonics.l14_diphthongs.view_model.DiphthongsLearnUiState
import com.example.myapplication.main.age_group.phonics.l14_diphthongs.view_model.DiphthongsLearnViewModel
import com.example.myapplication.main.age_group.phonics.l14_diphthongs.view_model.diphthongGroups
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

@Composable
fun DiphthongsLearnPage(navController: NavController) {
    val viewModel: DiphthongsLearnViewModel = hiltViewModel()
    val uiState = viewModel.uiState

    DisposableEffect(Unit) { onDispose { viewModel.stop() } }
    LaunchedEffect(Unit) { viewModel.onScreenAppear() }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.peachCoral, shape = KidsFloatingShape.stars)

        Box(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            Row(modifier = Modifier.fillMaxSize()) {

                // ── LEFT: group list ─────────────────────────────────────────
                Column(
                    modifier = Modifier
                        .weight(0.28f)
                        .fillMaxHeight()
                ) {
                    BackButtonWithText(title = "Diphthongs", onBackClick = { navController.popBackStack() })

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = Dimens10, vertical = Dimens8),
                        verticalArrangement = Arrangement.spacedBy(Dimens8)
                    ) {
                        diphthongGroups.forEach { group ->
                            DiphthongGroupCard(
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
                        .weight(0.72f)
                        .fillMaxHeight(),
                    label = "diphthongsGroupContent"
                ) { group ->
                    DiphthongGroupContent(
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
private fun DiphthongGroupCard(
    group: DiphthongGroup,
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
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens6),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = group.emoji, style = MaterialTheme.typography.titleMedium.scaled())
                Text(
                    text = group.spellings.joinToString("/"),
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
                        modifier = Modifier.height(Dimens14)
                    )
                }
            }
            Text(
                text = group.sound,
                style = MaterialTheme.typography.labelMedium.scaled(),
                color = if (isSelected) Color.White.copy(0.85f) else Color(0xFF78909C)
            )
            Text(
                text = "${group.words.size} words",
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = if (isSelected) Color.White.copy(0.60f) else Color(0xFF90A4AE)
            )
        }
    }
}

// ── Group Content ─────────────────────────────────────────────────────────────

@Composable
private fun DiphthongGroupContent(
    group: DiphthongGroup,
    uiState: DiphthongsLearnUiState,
    onWordTap: (DiphthongWord) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimens14),
        verticalArrangement = Arrangement.spacedBy(Dimens12)
    ) {
        DiphthongRuleBanner(group = group)

        if (uiState.showWords) {
            // One glass card — two spelling columns side by side (matches iOS splitColumns)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .kidsGlassCard(cornerRadius = Dimens12, strokeColor = group.accentColor)
                    .padding(Dimens14),
                horizontalArrangement = Arrangement.spacedBy(Dimens10)
            ) {
                group.spellings.forEach { spelling ->
                    val words = group.wordsBySpelling(spelling)
                    if (words.isNotEmpty()) {
                        DiphthongSpellingColumn(
                            spelling = spelling,
                            words = words,
                            group = group,
                            highlightedWordId = uiState.highlightedWordId,
                            onWordTap = onWordTap,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

// ── Rule Banner ───────────────────────────────────────────────────────────────

@Composable
private fun DiphthongRuleBanner(group: DiphthongGroup) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = Dimens12, strokeColor = group.accentColor)
            .padding(Dimens14),
        horizontalArrangement = Arrangement.spacedBy(Dimens14),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Spelling tiles with "=" between them
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            group.spellings.forEachIndexed { idx, spelling ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(group.accentColor, RoundedCornerShape(Dimens8))
                        .padding(horizontal = Dimens10, vertical = Dimens6)
                ) {
                    Text(
                        text = spelling,
                        style = MaterialTheme.typography.titleMedium.scaled(),
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
                if (idx < group.spellings.size - 1) {
                    Text(
                        text = "=",
                        style = MaterialTheme.typography.titleSmall.scaled(),
                        fontWeight = FontWeight.Bold,
                        color = group.accentColor.copy(alpha = 0.6f)
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
                text = "🎢 Diphthong Sound",
                style = MaterialTheme.typography.labelLarge.scaled(),
                fontWeight = FontWeight.Bold,
                color = group.accentColor
            )
            Text(
                text = "Both spellings make the same ${group.sound} sound",
                style = MaterialTheme.typography.labelMedium.scaled(),
                color = Color(0xFF455A64)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens6),
                verticalAlignment = Alignment.CenterVertically
            ) {
                group.spellings.forEach { spelling ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .background(group.accentColor, RoundedCornerShape(50))
                            .padding(horizontal = Dimens6, vertical = Dimens2)
                    ) {
                        Text(
                            text = spelling,
                            style = MaterialTheme.typography.labelMedium.scaled(),
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
                Text(
                    text = group.sound,
                    style = MaterialTheme.typography.labelMedium.scaled(),
                    color = group.accentColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ── Spelling Column (inside shared card) ─────────────────────────────────────

@Composable
private fun DiphthongSpellingColumn(
    spelling: String,
    group: DiphthongGroup,
    words: List<DiphthongWord>,
    highlightedWordId: String?,
    onWordTap: (DiphthongWord) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens8)
    ) {
        // Header pill + word count (matches iOS capsule + "X words")
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens6)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(group.accentColor, RoundedCornerShape(50))
                    .padding(horizontal = Dimens8, vertical = Dimens4)
            ) {
                Text(
                    text = spelling.uppercase(),
                    style = MaterialTheme.typography.labelLarge.scaled(),
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
            Text(
                text = "${words.size} words",
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = Color(0xFF90A4AE)
            )
        }

        // Words stacked vertically (matches iOS vertical ForEach)
        words.forEach { word ->
            DiphthongWordCard(
                word = word,
                group = group,
                isHighlighted = highlightedWordId == word.id,
                onTap = { onWordTap(word) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// ── Word Card ─────────────────────────────────────────────────────────────────

@Composable
private fun DiphthongWordCard(
    word: DiphthongWord,
    group: DiphthongGroup,
    isHighlighted: Boolean,
    onTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isHighlighted) 1.03f else 1.0f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 500f),
        label = "wordScale"
    )
    val normalColor = if (isHighlighted) Color.White.copy(alpha = 0.88f) else Color(0xFF263238)

    val cardShape = RoundedCornerShape(Dimens8)
    val textStyle = MaterialTheme.typography.bodyLarge.scaled()
    val iconTint = if (isHighlighted) Color.White.copy(alpha = 0.80f) else group.accentColor.copy(alpha = 0.70f)
    val borderColor = if (isHighlighted) Color.White.copy(alpha = 0.40f) else group.accentColor.copy(alpha = 0.20f)
    val diphthongBg = if (isHighlighted) Color.White.copy(alpha = 0.82f) else group.accentColor.copy(alpha = 0.12f)

    Box(modifier = modifier.scale(scale)) {
        // 3D base — iOS: RoundedRectangle(shadowColor).offset(y: 3)
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 3.dp)
                .background(group.shadowColor.copy(alpha = if (isHighlighted) 0f else 0.55f), cardShape)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(if (isHighlighted) 8.dp else 2.dp, cardShape,
                    ambientColor = group.accentColor, spotColor = group.accentColor)
                .then(
                    if (isHighlighted)
                        Modifier.background(Brush.linearGradient(listOf(group.accentColor, group.shadowColor)), cardShape)
                    else
                        Modifier.background(Color.White, cardShape)
                )
                .border(1.5.dp, borderColor, cardShape)
                .clickable { onTap() }
                .padding(horizontal = Dimens10, vertical = Dimens8)
        ) {
            // iOS HStack(spacing: 0): pre | diphthong(rounded bg) | suf | Spacer | speaker icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                if (word.pre.isNotEmpty()) {
                    Text(
                        text = word.pre,
                        style = textStyle,
                        fontWeight = FontWeight.Bold,
                        color = normalColor
                    )
                }
                Text(
                    text = word.diphthong,
                    style = textStyle,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isHighlighted) group.shadowColor else group.accentColor,
                    modifier = Modifier
                        .background(diphthongBg, RoundedCornerShape(4.dp))
                        .padding(horizontal = 3.dp)
                )
                if (word.suf.isNotEmpty()) {
                    Text(
                        text = word.suf,
                        style = textStyle,
                        fontWeight = FontWeight.Bold,
                        color = normalColor
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
