package com.example.myapplication.main.age_group.phonics.l6_word_families

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.PhonicsLearnSessionEffect
import com.example.myapplication.main.age_group.phonics.listen.view_model.PhonicsListenLevelKey
import com.example.myapplication.main.age_group.phonics.l6_word_families.view_model.FamilyOnset
import com.example.myapplication.main.age_group.phonics.l6_word_families.view_model.WordFamily
import com.example.myapplication.main.age_group.phonics.l6_word_families.view_model.WordFamiliesUiState
import com.example.myapplication.main.age_group.phonics.l6_word_families.view_model.WordFamiliesViewModel
import com.example.myapplication.main.age_group.phonics.l6_word_families.view_model.WordFamilyHighlight
import com.example.myapplication.main.age_group.phonics.l6_word_families.view_model.wordFamiliesData
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
import com.example.myapplication.ui.theme.AppDimens.Dimens28
import com.example.myapplication.utils.extensions.scaled
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.SpanStyle

@Composable
fun WordFamiliesLearnPage(
    navController: NavController,
    viewModel: WordFamiliesViewModel = hiltViewModel()
) {
    // Times this visit for the parent report (Phonics tab)
    PhonicsLearnSessionEffect(PhonicsListenLevelKey.wordFamilies)

    val uiState = viewModel.uiState

    DisposableEffect(Unit) {
        onDispose { viewModel.stop() }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.aquaGreen, shape = KidsFloatingShape.leaves)

        Row(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {

            // ── LEFT: family list ─────────────────────────────────────────
            Column(
                modifier = Modifier
                    .weight(0.33f)
                    .fillMaxHeight()
            ) {
                BackButtonWithText(
                    title = "Word Families",
                    onBackClick = { navController.popBackStack() }
                )

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = Dimens16),
                    verticalArrangement = Arrangement.spacedBy(Dimens8)
                ) {
                    items(wordFamiliesData) { family ->
                        FamilyTile(
                            family = family,
                            isSelected = uiState.selectedFamily?.rime == family.rime,
                            onClick = { viewModel.onFamilyTap(family) }
                        )
                    }
                }
            }

            // ── RIGHT: family detail ──────────────────────────────────────
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(0.67f)
                    .fillMaxHeight()
                    .padding(end = Dimens16)
            ) {
                val family = uiState.selectedFamily
                if (family != null) {
                    FamilyPanel(
                        family = family,
                        uiState = uiState,
                        onOnsetTap = { onset -> viewModel.onOnsetTap(onset, family) },
                        onReplayWord = { viewModel.replayWord() }
                    )
                } else {
                    Text(
                        text = "👈 Tap a family\nto build words!",
                        style = MaterialTheme.typography.titleLarge.scaled(),
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2E7D32).copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// ── Family tile ───────────────────────────────────────────────────────────────

@Composable
private fun FamilyTile(family: WordFamily, isSelected: Boolean, onClick: () -> Unit) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1.0f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessMedium),
        label = "familyTileScale"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens12),
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .background(
                if (isSelected) {
                    Brush.linearGradient(listOf(family.color, family.shadowColor))
                } else {
                    Brush.linearGradient(listOf(Color.White, Color(0xD1FFFFFF)))
                },
                RoundedCornerShape(Dimens14)
            )
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onClick)
            .padding(horizontal = Dimens12, vertical = Dimens8)
    ) {
        // Rime badge
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(width = 56.dp, height = 52.dp)
                .background(
                    if (isSelected) Color.White.copy(alpha = 0.22f) else family.color.copy(alpha = 0.12f),
                    RoundedCornerShape(Dimens10)
                )
        ) {
            Text(
                text = "-${family.rime}",
                style = MaterialTheme.typography.titleLarge.scaled(),
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else family.color
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(Dimens2),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "${family.onsets.size} words",
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = if (isSelected) Color.White.copy(alpha = 0.75f) else Color.Gray
            )
            Text(
                text = family.onsets.take(4).joinToString(", ") { it.word },
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = if (isSelected) Color.White.copy(alpha = 0.6f) else Color.Gray.copy(alpha = 0.7f),
                maxLines = 1
            )
        }

        if (isSelected) {
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.White.copy(alpha = 0.85f), modifier = Modifier.size(Dimens20))
        }
    }
}

// ── Family panel ──────────────────────────────────────────────────────────────

@Composable
private fun FamilyPanel(
    family: WordFamily,
    uiState: WordFamiliesUiState,
    onOnsetTap: (FamilyOnset) -> Unit,
    onReplayWord: () -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val tileH = min(maxHeight * 0.14f, 90.dp)
        val onsetTileSize = min(maxHeight * 0.12f, maxWidth * 0.10f)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Header: big rime tile + description
            FamilyHeader(family = family, tileH = tileH, isRimeHighlighted = uiState.highlightHeaderRime)

            Spacer(modifier = Modifier.height(Dimens24))

            // Onset grid
            OnsetGrid(
                family = family,
                uiState = uiState,
                tileSize = onsetTileSize,
                onOnsetTap = onOnsetTap
            )

            Spacer(modifier = Modifier.height(Dimens24))

            // Equation (always in layout, opacity-hidden until showEquation)
            val onset = uiState.selectedOnset ?: family.onsets.first()
            Box(modifier = Modifier.alpha(if (uiState.showEquation) 1f else 0f)) {
                EquationView(
                    onset = onset,
                    family = family,
                    highlight = uiState.highlight,
                    tileH = tileH,
                    onReplayWord = onReplayWord
                )
            }
        }
    }
}

@Composable
private fun FamilyHeader(family: WordFamily, tileH: Dp, isRimeHighlighted: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens20),
        modifier = Modifier.padding(horizontal = Dimens28)
    ) {
        RimeTile(rime = family.rime, color = family.color, shadow = family.shadowColor, height = tileH, isHighlighted = isRimeHighlighted)

        Column(verticalArrangement = Arrangement.spacedBy(Dimens4)) {
            Text(
                text = "Word Family",
                style = MaterialTheme.typography.bodyMedium.scaled(),
                color = family.color.copy(alpha = 0.65f)
            )
            Text(
                text = "-${family.rime} words",
                style = MaterialTheme.typography.headlineMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = family.color
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = Color.Gray)) { append("All these words end in ") }
                    withStyle(SpanStyle(color = family.color, fontWeight = FontWeight.Bold)) { append("-${family.rime}") }
                    withStyle(SpanStyle(color = Color.Gray)) { append(" — same ending, same sound: they all ") }
                    withStyle(SpanStyle(color = family.color, fontWeight = FontWeight.Bold)) { append("RHYME") }
                    withStyle(SpanStyle(color = Color.Gray)) { append("!") }
                },
                style = MaterialTheme.typography.labelSmall.scaled()
            )
        }
    }
}

@Composable
private fun RimeTile(rime: String, color: Color, shadow: Color, height: Dp, isHighlighted: Boolean) {
    val width = height * 1.55f
    val scale by animateFloatAsState(
        targetValue = if (isHighlighted) 1.12f else 1.0f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMedium),
        label = "rimeTileScale"
    )
    val cornerShape = RoundedCornerShape(height * 0.22f)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(width = width, height = height)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .shadow(
                elevation = if (isHighlighted) Dimens16 else Dimens4,
                shape = cornerShape,
                clip = false
            )
            .background(Brush.linearGradient(listOf(color, shadow)), cornerShape)
    ) {
        Text(
            text = "-$rime",
            style = TextStyle(fontSize = (height.value * 0.50f).sp, fontWeight = FontWeight.Bold),
            color = Color.White
        )
    }
}

@Composable
private fun OnsetGrid(
    family: WordFamily,
    uiState: WordFamiliesUiState,
    tileSize: Dp,
    onOnsetTap: (FamilyOnset) -> Unit
) {
    val rows = family.onsets.chunked(4)

    Column(verticalArrangement = Arrangement.spacedBy(Dimens4)) {
        Text(
            text = "Tap a letter to build a word",
            style = MaterialTheme.typography.bodySmall.scaled(),
            color = Color.Gray.copy(alpha = 0.75f)
        )
        Spacer(modifier = Modifier.height(Dimens6))
        rows.forEachIndexed { rowIdx, row ->
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens10)) {
                row.forEachIndexed { colIdx, onset ->
                    val idx = rowIdx * 4 + colIdx
                    val isSelected = uiState.selectedOnset?.onset == onset.onset && uiState.selectedOnset?.word == onset.word
                    OnsetTile(
                        onset = onset,
                        family = family,
                        index = idx,
                        showOnsets = uiState.showOnsets,
                        isSelected = isSelected,
                        tileSize = tileSize,
                        onClick = { onOnsetTap(onset) }
                    )
                }
            }
        }
    }
}

@Composable
private fun OnsetTile(
    onset: FamilyOnset,
    family: WordFamily,
    index: Int,
    showOnsets: Boolean,
    isSelected: Boolean,
    tileSize: Dp = 52.dp,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.14f else 1.0f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMedium),
        label = "onsetTileScale_$index"
    )
    val alpha by animateFloatAsState(
        targetValue = if (showOnsets) 1f else 0f,
        animationSpec = tween(durationMillis = 400, delayMillis = index * 70),
        label = "onsetAlpha_$index"
    )
    val offsetY by animateFloatAsState(
        targetValue = if (showOnsets) 0f else -24f,
        animationSpec = tween(durationMillis = 400, delayMillis = index * 70),
        label = "onsetOffY_$index"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(tileSize)
            .graphicsLayer { scaleX = scale; scaleY = scale; this.alpha = alpha; translationY = offsetY }
            .background(
                if (isSelected) Brush.linearGradient(listOf(family.color, family.shadowColor))
                else Brush.linearGradient(listOf(Color.White, Color(0xFFF0F0F0))),
                RoundedCornerShape(tileSize * 0.22f)
            )
            .border(Dimens2, if (isSelected) family.shadowColor else Color.Transparent, RoundedCornerShape(tileSize * 0.22f))
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onClick)
    ) {
        Text(
            text = onset.onset,
            style = TextStyle(fontSize = (tileSize.value * 0.44f).sp, fontWeight = FontWeight.Bold),
            color = if (isSelected) Color.White else family.color
        )
    }
}

// ── Equation view ─────────────────────────────────────────────────────────────

@Composable
private fun EquationView(
    onset: FamilyOnset,
    family: WordFamily,
    highlight: WordFamilyHighlight,
    tileH: Dp,
    onReplayWord: () -> Unit
) {
    val anyActive = highlight != WordFamilyHighlight.NONE

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens14),
        modifier = Modifier.padding(horizontal = Dimens28)
    ) {
        // [b] onset tile
        EquationTile(
            text = onset.onset,
            color = Color(0xFF1E88E5),
            shadow = Color(0xFF1565C0),
            height = tileH,
            isHighlighted = highlight == WordFamilyHighlight.ONSET,
            isDimmed = anyActive && highlight != WordFamilyHighlight.ONSET
        )

        Text("+", style = MaterialTheme.typography.titleLarge.scaled(), color = Color.Gray.copy(alpha = 0.6f))

        // [-at] rime tile
        EquationTile(
            text = "-${family.rime}",
            color = family.color,
            shadow = family.shadowColor,
            height = tileH,
            isHighlighted = highlight == WordFamilyHighlight.RIME,
            isDimmed = anyActive && highlight != WordFamilyHighlight.RIME
        )

        Text("=", style = MaterialTheme.typography.titleLarge.scaled(), color = Color.Gray.copy(alpha = 0.6f))

        // [word] tile — tappable for replay
        WordEquationTile(
            onset = onset,
            family = family,
            height = tileH,
            isHighlighted = highlight == WordFamilyHighlight.WORD,
            isDimmed = anyActive && highlight != WordFamilyHighlight.WORD,
            onClick = onReplayWord
        )
    }
}

@Composable
private fun EquationTile(
    text: String,
    color: Color,
    shadow: Color,
    height: Dp,
    isHighlighted: Boolean,
    isDimmed: Boolean
) {
    val charCount = text.length
    val width = height * (charCount * 0.42f + 0.7f)
    val scale by animateFloatAsState(
        targetValue = if (isHighlighted) 1.14f else 1.0f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMedium),
        label = "eqTileScale"
    )

    val eqCornerShape = RoundedCornerShape(height * 0.22f)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(width = width, height = height)
            .graphicsLayer { scaleX = scale; scaleY = scale; alpha = if (isDimmed) 0.32f else 1f }
            .shadow(
                elevation = if (isHighlighted) Dimens16 else Dimens4,
                shape = eqCornerShape,
                clip = false
            )
            .background(Brush.linearGradient(listOf(color, shadow)), eqCornerShape)
    ) {
        Text(
            text = text,
            style = TextStyle(fontSize = (height.value * 0.48f).sp, fontWeight = FontWeight.Bold),
            color = Color.White
        )
    }
}

@Composable
private fun WordEquationTile(
    onset: FamilyOnset,
    family: WordFamily,
    height: Dp,
    isHighlighted: Boolean,
    isDimmed: Boolean,
    onClick: () -> Unit
) {
    val charCount = onset.word.length
    val width = height * (charCount * 0.52f + 0.5f)
    val scale by animateFloatAsState(
        targetValue = if (isHighlighted) 1.14f else 1.0f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMedium),
        label = "wordTileScale"
    )

    val wordCornerShape = RoundedCornerShape(height * 0.22f)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(width = width, height = height)
            .graphicsLayer { scaleX = scale; scaleY = scale; alpha = if (isDimmed) 0.32f else 1f }
            .shadow(
                elevation = if (isHighlighted) Dimens16 else Dimens4,
                shape = wordCornerShape,
                clip = false
            )
            .background(Brush.linearGradient(listOf(family.color, family.shadowColor)), wordCornerShape)
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens4)
        ) {
            Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.85f),
                modifier = Modifier.size(height * 0.28f)
            )
            Text(
                text = onset.word,
                style = TextStyle(fontSize = (height.value * 0.46f).sp, fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }
    }
}
