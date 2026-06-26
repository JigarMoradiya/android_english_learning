package com.example.myapplication.main.age_group.phonics.l11_open_syllable

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
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import com.example.myapplication.main.age_group.phonics.l11_open_syllable.view_model.OpenSyllableGroup
import com.example.myapplication.main.age_group.phonics.l11_open_syllable.view_model.OpenSyllableLearnUiState
import com.example.myapplication.main.age_group.phonics.l11_open_syllable.view_model.OpenSyllableLearnViewModel
import com.example.myapplication.main.age_group.phonics.l11_open_syllable.view_model.OpenSyllableWord
import com.example.myapplication.main.age_group.phonics.l11_open_syllable.view_model.openSyllableData
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

@Composable
fun OpenSyllableLearnPage(
    navController: NavController,
    viewModel: OpenSyllableLearnViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState

    DisposableEffect(Unit) { onDispose { viewModel.stop() } }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.periwinkleBlue, shape = KidsFloatingShape.bubbles)

        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            // Header with group tabs
            LearnHeader(
                uiState = uiState,
                onBack = { navController.popBackStack() },
                onGroupTap = { viewModel.onGroupTap(it) }
            )

            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val totalW = maxWidth
                Row(modifier = Modifier.fillMaxSize()) {
                    // Left 30%: word list
                    LeftWordList(
                        uiState = uiState,
                        modifier = Modifier
                            .width(totalW * 0.30f)
                            .fillMaxHeight(),
                        onWordTap = { viewModel.onWordTap(it) }
                    )

                    // Right 70%: detail panel
                    AnimatedContent(
                        targetState = uiState.selectedGroup,
                        transitionSpec = { fadeIn(spring()) togetherWith fadeOut(spring()) },
                        label = "groupDetail",
                        modifier = Modifier.fillMaxSize()
                    ) { group ->
                        GroupDetailView(
                            group = group,
                            uiState = uiState,
                            onWordTap = { viewModel.onWordTap(it) },
                            onVowelTap = { viewModel.onVowelSoundTap(it) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LearnHeader(
    uiState: OpenSyllableLearnUiState,
    onBack: () -> Unit,
    onGroupTap: (OpenSyllableGroup) -> Unit
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        BackButtonWithText(title = "Open Syllable", expandWidth = false, onBackClick = onBack)

        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens8, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = Dimens8)
                .padding(end = Dimens16)
        ) {
            OpenSyllableGroup.entries.forEach { group ->
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
private fun GroupTabButton(group: OpenSyllableGroup, isSelected: Boolean, onTap: () -> Unit) {
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
private fun LeftWordList(
    uiState: OpenSyllableLearnUiState,
    modifier: Modifier,
    onWordTap: (OpenSyllableWord) -> Unit
) {
    val words = openSyllableData[uiState.selectedGroup] ?: emptyList()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Dimens10, vertical = Dimens12),
        verticalArrangement = Arrangement.spacedBy(Dimens8)
    ) {
        words.forEach { word ->
            WordTile(
                word = word,
                group = uiState.selectedGroup,
                isHighlighted = uiState.highlightedWordId == word.id,
                onTap = { onWordTap(word) }
            )
        }
    }
}

@Composable
private fun WordTile(
    word: OpenSyllableWord,
    group: OpenSyllableGroup,
    isHighlighted: Boolean,
    onTap: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(if (isHighlighted) 1.04f else 1.0f)
            .clickable { onTap() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(group.shadowColor, RoundedCornerShape(Dimens8))
                .offset(y = 3.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (isHighlighted) Modifier.shadow(6.dp, RoundedCornerShape(Dimens8), clip = false,
                        ambientColor = group.color.copy(0.35f), spotColor = group.color.copy(0.35f))
                    else Modifier.shadow(2.dp, RoundedCornerShape(Dimens8), clip = false,
                        ambientColor = group.color.copy(0.08f), spotColor = group.color.copy(0.08f))
                )
                .clip(RoundedCornerShape(Dimens8))
                .background(
                    if (isHighlighted) Brush.linearGradient(listOf(group.color, group.shadowColor))
                    else Brush.linearGradient(listOf(Color.White, Color.White))
                )
                .padding(horizontal = Dimens10, vertical = Dimens8)
        ) {
            Text(
                text = word.prefix,
                style = MaterialTheme.typography.titleMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = if (isHighlighted) Color.White.copy(0.85f) else Color(0xFF263238).copy(0.55f)
            )
            Text(
                text = word.vowelPart,
                style = MaterialTheme.typography.titleMedium.scaled(),
                fontWeight = FontWeight.ExtraBold,
                color = if (isHighlighted) Color.White else group.color
            )
            Text(
                text = word.suffix,
                style = MaterialTheme.typography.titleMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = if (isHighlighted) Color.White.copy(0.85f) else Color(0xFF263238).copy(0.55f)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GroupDetailView(
    group: OpenSyllableGroup,
    uiState: OpenSyllableLearnUiState,
    onWordTap: (OpenSyllableWord) -> Unit,
    onVowelTap: (String) -> Unit
) {
    val words = openSyllableData[group] ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Dimens14, vertical = Dimens10)
            .padding(bottom = Dimens20),
        verticalArrangement = Arrangement.spacedBy(Dimens14)
    ) {
        // Rule card
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens12),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Vowel button
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens8),
                modifier = Modifier
                    .weight(1f)
                    .background(group.color.copy(alpha = 0.08f), RoundedCornerShape(Dimens12))
                    .padding(vertical = Dimens20)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(Dimens32 + Dimens20)
                        .background(group.color, CircleShape)
                        .clickable { onVowelTap(group.hint.take(1).lowercase()) }
                ) {
                    Text(
                        text = group.emoji,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                Text(
                    text = group.rule,
                    style = MaterialTheme.typography.labelMedium.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = group.color,
                    modifier = Modifier.padding(horizontal = Dimens8)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens4),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LightMode,
                        contentDescription = null,
                        tint = group.color,
                        modifier = Modifier.size(Dimens14)
                    )
                    Text(
                        text = "long vowel sound",
                        style = MaterialTheme.typography.labelSmall.scaled(),
                        color = Color(0xFF546E7A)
                    )
                }
            }

            // How to say it
            Column(
                verticalArrangement = Arrangement.spacedBy(Dimens12),
                modifier = Modifier
                    .weight(2f)
                    .kidsGlassCard(cornerRadius = Dimens12, strokeColor = group.color)
                    .padding(horizontal = Dimens14, vertical = Dimens20)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens6),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = null,
                        tint = group.color,
                        modifier = Modifier.size(Dimens16)
                    )
                    Text(
                        text = "The Open Syllable Rule",
                        style = MaterialTheme.typography.labelLarge.scaled(),
                        fontWeight = FontWeight.Bold,
                        color = group.color
                    )
                }
                Text(
                    text = "When a syllable ends in a vowel, that vowel says its long (alphabet) name.",
                    style = MaterialTheme.typography.bodyMedium.scaled(),
                    color = Color(0xFF37474F)
                )
                Text(
                    text = "Examples: ${words.take(3).joinToString(" · ") { it.word }}",
                    style = MaterialTheme.typography.bodyMedium.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = group.shadowColor
                )
            }
        }

        // Word tiles
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimens10),
            modifier = Modifier
                .fillMaxWidth()
                .kidsGlassCard(cornerRadius = Dimens12, strokeColor = group.color)
                .padding(Dimens14)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens6),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.TouchApp,
                    contentDescription = null,
                    tint = group.color,
                    modifier = Modifier.size(Dimens16)
                )
                Text(
                    text = "Tap a word to hear it",
                    style = MaterialTheme.typography.labelLarge.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = group.color
                )
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(Dimens8),
                verticalArrangement = Arrangement.spacedBy(Dimens8)
            ) {
                words.forEach { word ->
                    WordChip(
                        word = word,
                        group = group,
                        isHighlighted = uiState.highlightedWordId == word.id,
                        onTap = { onWordTap(word) }
                    )
                }
            }
        }
    }
}

@Composable
private fun WordChip(
    word: OpenSyllableWord,
    group: OpenSyllableGroup,
    isHighlighted: Boolean,
    onTap: () -> Unit
) {
    Box(
        modifier = Modifier
            .scale(if (isHighlighted) 1.06f else 1.0f)
            .shadow(
                if (isHighlighted) 6.dp else 2.dp,
                RoundedCornerShape(Dimens8),
                ambientColor = group.color.copy(if (isHighlighted) 0.35f else 0.08f),
                spotColor = group.color.copy(if (isHighlighted) 0.35f else 0.08f)
            )
            .clip(RoundedCornerShape(Dimens8))
            .background(
                if (isHighlighted) Brush.linearGradient(listOf(group.color, group.shadowColor))
                else Brush.linearGradient(listOf(Color.White, Color.White))
            )
            .clickable { onTap() }
            .padding(horizontal = Dimens12, vertical = Dimens8)
    ) {
        Row {
            Text(
                text = word.prefix,
                style = MaterialTheme.typography.bodyLarge.scaled(),
                fontWeight = FontWeight.Bold,
                color = if (isHighlighted) Color.White.copy(0.80f) else Color(0xFF263238).copy(0.55f)
            )
            Text(
                text = word.vowelPart,
                style = MaterialTheme.typography.bodyLarge.scaled(),
                fontWeight = FontWeight.ExtraBold,
                color = if (isHighlighted) Color.White else group.color
            )
            if (word.suffix.isNotEmpty()) {
                Text(
                    text = word.suffix,
                    style = MaterialTheme.typography.bodyLarge.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = if (isHighlighted) Color.White.copy(0.80f) else Color(0xFF263238).copy(0.55f)
                )
            }
        }
    }
}
