package com.example.myapplication.main.age_group.phonics.l9_beginning_blends

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.l9_beginning_blends.view_model.BeginningBlendsLearnViewModel
import com.example.myapplication.main.age_group.phonics.l9_beginning_blends.view_model.BlendEntry
import com.example.myapplication.main.age_group.phonics.l9_beginning_blends.view_model.BlendGroup
import com.example.myapplication.main.age_group.phonics.l9_beginning_blends.view_model.BlendLearnUiState
import com.example.myapplication.main.age_group.phonics.l9_beginning_blends.view_model.BlendWord
import com.example.myapplication.main.age_group.phonics.l9_beginning_blends.view_model.beginningBlendsData
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
import com.example.myapplication.ui.theme.AppDimens.Dimens28
import com.example.myapplication.ui.theme.AppDimens.Dimens32
import com.example.myapplication.utils.extensions.scaled

@Composable
fun BeginningBlendsLearnPage(
    navController: NavController,
    viewModel: BeginningBlendsLearnViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState

    LaunchedEffect(Unit) {
        val first = beginningBlendsData.firstOrNull { it.group == BlendGroup.L_BLENDS }
        if (first != null && uiState.selectedBlend == null) viewModel.onBlendTap(first)
    }

    androidx.compose.runtime.DisposableEffect(Unit) { onDispose { viewModel.stop() } }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.seaBlue, shape = KidsFloatingShape.bubbles)

        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            // Header
            BlendLearnHeader(
                uiState = uiState,
                onBack = { navController.popBackStack() },
                onGroupTap = { viewModel.onGroupTap(it) }
            )

            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val totalW = maxWidth
                Row(modifier = Modifier.fillMaxSize()) {
                    // Left 28%: blend list
                    LeftBlendList(
                        uiState = uiState,
                        modifier = Modifier
                            .width(totalW * 0.28f)
                            .fillMaxHeight(),
                        onBlendTap = { viewModel.onBlendTap(it) }
                    )

                    // Right 72%: detail
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        val blend = uiState.selectedBlend
                        if (blend != null) {
                            AnimatedContent(
                                targetState = blend.blend,
                                transitionSpec = {
                                    fadeIn(spring()) togetherWith fadeOut(spring())
                                },
                                label = "blendDetail"
                            ) { _ ->
                                val b = uiState.selectedBlend ?: blend
                                BlendDetailView(
                                    blend = b,
                                    uiState = uiState,
                                    availableWidth = totalW * 0.72f,
                                    onWordTap = { viewModel.onWordTap(it) },
                                    onLetterSoundTap = { viewModel.onLetterSoundTap(it) },
                                    onBlendSoundTap = { viewModel.onBlendSoundTap(b) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BlendLearnHeader(
    uiState: BlendLearnUiState,
    onBack: () -> Unit,
    onGroupTap: (BlendGroup) -> Unit
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        BackButtonWithText(title = "Beginning Blends", expandWidth = false, onBackClick = onBack)

        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens8, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = Dimens8)
                .padding(end = Dimens16)
        ) {
            BlendGroup.entries.forEach { group ->
                GroupTabButton(
                    group = group,
                    isSelected = uiState.selectedGroup == group,
                    onTap = { onGroupTap(group) }
                )
            }
        }
    }
}

@Composable
private fun GroupTabButton(group: BlendGroup, isSelected: Boolean, onTap: () -> Unit) {
    Box(
        modifier = Modifier
            .then(
                if (isSelected) Modifier.shadow(
                    elevation = 6.dp,
                    shape = RoundedCornerShape(Dimens14),
                    clip = false,
                    ambientColor = group.color.copy(alpha = 0.42f),
                    spotColor = group.color.copy(alpha = 0.42f)
                ) else Modifier
            )
            .kidsGlassCard(
                cornerRadius = Dimens14,
                strokeColor = if (isSelected) Color.Transparent else group.color.copy(alpha = 0.4f)
            )
            .then(
                if (isSelected) Modifier.background(
                    brush = Brush.linearGradient(listOf(group.color, group.shadowColor)),
                    shape = RoundedCornerShape(Dimens14)
                ) else Modifier
            )
            .clickable { onTap() }
            .padding(horizontal = Dimens12, vertical = Dimens6)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens6),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = group.emoji, style = MaterialTheme.typography.titleMedium)
            Column(verticalArrangement = Arrangement.spacedBy(Dimens2)) {
                Text(
                    text = group.label,
                    style = MaterialTheme.typography.labelLarge.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else group.color
                )
                Text(
                    text = group.hint,
                    style = MaterialTheme.typography.labelSmall.scaled(),
                    color = if (isSelected) Color.White.copy(alpha = 0.82f) else Color(0xFF546E7A)
                )
            }
        }
    }
}

@Composable
private fun LeftBlendList(
    uiState: BlendLearnUiState,
    modifier: Modifier,
    onBlendTap: (BlendEntry) -> Unit
) {
    val blends = beginningBlendsData.filter { it.group == uiState.selectedGroup }
    val scrollState = rememberScrollState()

    LaunchedEffect(uiState.selectedGroup) { scrollState.scrollTo(0) }

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(horizontal = Dimens10, vertical = Dimens12),
        verticalArrangement = Arrangement.spacedBy(Dimens8)
    ) {
        blends.forEach { blend ->
            BlendTile(
                blend = blend,
                isSelected = uiState.selectedBlend?.blend == blend.blend,
                onTap = { onBlendTap(blend) }
            )
        }
    }
}

@Composable
private fun BlendTile(blend: BlendEntry, isSelected: Boolean, onTap: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTap() }
    ) {
        // Shadow layer offset
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(blend.group.shadowColor, RoundedCornerShape(Dimens8))
                .offset(y = 3.dp)
                .height(Dimens32 + Dimens16)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens8),
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (isSelected) Modifier.shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(Dimens8),
                        clip = false,
                        ambientColor = blend.group.color.copy(alpha = 0.35f),
                        spotColor = blend.group.color.copy(alpha = 0.35f)
                    ) else Modifier.shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(Dimens8),
                        clip = false,
                        ambientColor = blend.group.color.copy(alpha = 0.08f),
                        spotColor = blend.group.color.copy(alpha = 0.08f)
                    )
                )
                .clip(RoundedCornerShape(Dimens8))
                .background(
                    if (isSelected)
                        Brush.linearGradient(listOf(blend.group.color, blend.group.shadowColor))
                    else
                        Brush.linearGradient(listOf(Color.White, Color.White))
                )
                .padding(horizontal = Dimens10, vertical = Dimens8)
        ) {
            Text(
                text = blend.blend.uppercase(),
                style = MaterialTheme.typography.titleLarge.scaled(),
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else blend.group.color,
                modifier = Modifier.width(Dimens32 + Dimens8)
            )
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(Dimens28)
                    .background(
                        if (isSelected) Color.White.copy(alpha = 0.35f)
                        else Color(0xFFCFD8DC)
                    )
            )
            Column(verticalArrangement = Arrangement.spacedBy(Dimens2)) {
                Text(
                    text = blend.phonetic,
                    style = MaterialTheme.typography.labelMedium.scaled(),
                    color = if (isSelected) Color.White.copy(alpha = 0.90f) else Color(0xFF546E7A)
                )
                Text(
                    text = "${blend.words.size} words",
                    style = MaterialTheme.typography.labelSmall.scaled(),
                    color = if (isSelected) Color.White.copy(alpha = 0.65f) else Color(0xFF90A4AE)
                )
            }
        }
    }
}

@Composable
private fun BlendDetailView(
    blend: BlendEntry,
    uiState: BlendLearnUiState,
    availableWidth: androidx.compose.ui.unit.Dp,
    onWordTap: (BlendWord) -> Unit,
    onLetterSoundTap: (String) -> Unit,
    onBlendSoundTap: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Dimens14, vertical = Dimens10)
            .padding(bottom = Dimens20),
        verticalArrangement = Arrangement.spacedBy(Dimens14)
    ) {
        BigBlendCard(
            blend = blend,
            onLetterSoundTap = onLetterSoundTap,
            onBlendSoundTap = onBlendSoundTap
        )

        AnimatedVisibility(
            visible = uiState.showWords,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ExampleWordsSection(
                blend = blend,
                uiState = uiState,
                availableWidth = availableWidth,
                onWordTap = onWordTap
            )
        }
    }
}

@Composable
private fun BigBlendCard(
    blend: BlendEntry,
    onLetterSoundTap: (String) -> Unit,
    onBlendSoundTap: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens12),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Left: large letters + phonetic
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens8),
            modifier = Modifier
                .weight(1f)
                .background(
                    color = blend.group.color.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(Dimens12)
                )
                .padding(vertical = Dimens20)
        ) {
            Row(horizontalArrangement = Arrangement.Center) {
                blend.blend.forEachIndexed { idx, char ->
                    Text(
                        text = char.uppercaseChar().toString(),
                        style = MaterialTheme.typography.displaySmall.scaled(),
                        fontWeight = FontWeight.ExtraBold,
                        color = if (idx == 0) blend.group.color else blend.group.shadowColor
                    )
                }
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(Color(0xFFECEFF1), RoundedCornerShape(50))
                    .padding(horizontal = Dimens8, vertical = Dimens2)
            ) {
                Text(
                    text = blend.phonetic,
                    style = MaterialTheme.typography.bodyMedium.scaled(),
                    color = Color(0xFF546E7A)
                )
            }
        }

        // Right: how to say it
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimens8),
            modifier = Modifier
                .weight(1f)
                .kidsGlassCard(cornerRadius = Dimens12, strokeColor = blend.group.color)
                .padding(horizontal = Dimens14, vertical = Dimens20)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens6),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = null,
                        tint = blend.group.color,
                        modifier = Modifier.size(Dimens16)
                    )
                    Text(
                        text = "How to say it",
                        style = MaterialTheme.typography.labelLarge.scaled(),
                        fontWeight = FontWeight.Bold,
                        color = blend.group.color
                    )
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(blend.group.color, RoundedCornerShape(50))
                        .padding(horizontal = Dimens8, vertical = Dimens2)
                ) {
                    Text(
                        text = "Word Start",
                        style = MaterialTheme.typography.labelSmall.scaled(),
                        color = Color.White
                    )
                }
            }

            Text(
                text = blend.soundHint,
                style = MaterialTheme.typography.bodyMedium.scaled(),
                color = Color(0xFF37474F)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens6),
                verticalAlignment = Alignment.CenterVertically
            ) {
                blend.blend.forEach { char ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(Dimens32)
                            .background(blend.group.color, CircleShape)
                            .clickable { onLetterSoundTap(char.toString()) }
                    ) {
                        Text(
                            text = char.uppercaseChar().toString(),
                            style = MaterialTheme.typography.labelLarge.scaled(),
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Color(0xFF90A4AE),
                    modifier = Modifier.size(Dimens20)
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .shadow(4.dp, RoundedCornerShape(Dimens8), spotColor = blend.group.color.copy(alpha = 0.4f))
                        .clip(RoundedCornerShape(Dimens8))
                        .background(
                            Brush.linearGradient(listOf(blend.group.color, blend.group.shadowColor))
                        )
                        .clickable { onBlendSoundTap() }
                        .padding(horizontal = Dimens10, vertical = Dimens4)
                ) {
                    Text(
                        text = blend.blend.uppercase(),
                        style = MaterialTheme.typography.labelLarge.scaled(),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ExampleWordsSection(
    blend: BlendEntry,
    uiState: BlendLearnUiState,
    availableWidth: androidx.compose.ui.unit.Dp,
    onWordTap: (BlendWord) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimens10),
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = Dimens12, strokeColor = blend.group.color)
            .padding(Dimens14)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens6),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.TouchApp,
                contentDescription = null,
                tint = blend.group.color,
                modifier = Modifier.size(Dimens16)
            )
            Text(
                text = "Tap a word to hear it",
                style = MaterialTheme.typography.labelLarge.scaled(),
                fontWeight = FontWeight.Bold,
                color = blend.group.color
            )
        }

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(Dimens8),
            verticalArrangement = Arrangement.spacedBy(Dimens8)
        ) {
            blend.words.forEach { word ->
                WordTile(
                    word = word,
                    blend = blend,
                    isHighlighted = uiState.highlightedWordId == word.id,
                    onTap = { onWordTap(word) }
                )
            }
        }
    }
}

@Composable
private fun WordTile(word: BlendWord, blend: BlendEntry, isHighlighted: Boolean, onTap: () -> Unit) {
    Box(
        modifier = Modifier
            .scale(if (isHighlighted) 1.04f else 1.0f)
            .clickable { onTap() }
    ) {
        // Shadow layer
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 3.dp)
                .background(blend.group.shadowColor, RoundedCornerShape(Dimens8))
        )
        Row(
            modifier = Modifier
                .then(
                    if (isHighlighted) Modifier.shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(Dimens8),
                        ambientColor = blend.group.color.copy(alpha = 0.35f),
                        spotColor = blend.group.color.copy(alpha = 0.35f)
                    ) else Modifier.shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(Dimens8),
                        ambientColor = blend.group.color.copy(alpha = 0.08f),
                        spotColor = blend.group.color.copy(alpha = 0.08f)
                    )
                )
                .clip(RoundedCornerShape(Dimens8))
                .background(
                    if (isHighlighted)
                        Brush.linearGradient(listOf(blend.group.color, blend.group.shadowColor))
                    else
                        Brush.linearGradient(listOf(Color.White, Color.White))
                )
                .padding(horizontal = Dimens10, vertical = Dimens6)
        ) {
            Text(
                text = word.blendPart,
                style = MaterialTheme.typography.bodyMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = if (isHighlighted) Color.White else blend.group.color
            )
            Text(
                text = word.restPart,
                style = MaterialTheme.typography.bodyMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = if (isHighlighted) Color.White.copy(alpha = 0.82f) else Color(0xFF263238).copy(alpha = 0.55f)
            )
        }
    }
}

