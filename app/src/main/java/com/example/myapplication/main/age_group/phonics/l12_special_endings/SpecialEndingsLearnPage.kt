package com.example.myapplication.main.age_group.phonics.l12_special_endings

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TouchApp
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.l12_special_endings.view_model.SpecialEndingEntry
import com.example.myapplication.main.age_group.phonics.l12_special_endings.view_model.SpecialEndingWord
import com.example.myapplication.main.age_group.phonics.l12_special_endings.view_model.SpecialEndingsLearnViewModel
import com.example.myapplication.main.age_group.phonics.l12_special_endings.view_model.specialEndingsData
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SpecialEndingsLearnPage(
    navController: NavController,
    viewModel: SpecialEndingsLearnViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState
    val allEntries = specialEndingsData

    DisposableEffect(Unit) { onDispose { viewModel.stop() } }

    LaunchedEffect(Unit) {
        if (uiState.selectedEntry == null) viewModel.onEntryTap(allEntries.first())
    }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.mintLime, shape = KidsFloatingShape.bubbles)

        Column(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing).fillMaxSize()) {

            // Header: BackButton + 3 ending tabs
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                BackButtonWithText(title = "Special Endings", expandWidth = false, onBackClick = { navController.popBackStack() })
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens8, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f).padding(vertical = Dimens8).padding(end = Dimens16)
                ) {
                    allEntries.forEach { entry ->
                        val isSelected = uiState.selectedEntry?.id == entry.id
                        val accent = entry.group.color
                        val shadow = entry.group.shadowColor
                        Box(
                            modifier = Modifier
                                .then(
                                    if (isSelected)
                                        Modifier.shadow(8.dp, RoundedCornerShape(Dimens14), clip = false, ambientColor = accent.copy(0.42f), spotColor = accent.copy(0.42f))
                                    else Modifier
                                )
                                .kidsGlassCard(cornerRadius = Dimens14, strokeColor = accent.copy(if (isSelected) 0f else 0.4f))
                                .then(
                                    if (isSelected)
                                        Modifier.background(Brush.linearGradient(listOf(accent, shadow)), RoundedCornerShape(Dimens14))
                                    else Modifier
                                )
                                .clickable { viewModel.onEntryTap(entry) }
                                .padding(horizontal = Dimens12, vertical = Dimens6)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Dimens8)) {
                                Text(entry.group.emoji, style = MaterialTheme.typography.titleMedium)
                                Column(verticalArrangement = Arrangement.spacedBy(Dimens2)) {
                                    Text(
                                        entry.displayEnding.uppercase(),
                                        style = MaterialTheme.typography.titleSmall.scaled(),
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else accent
                                    )
                                    Text(
                                        entry.phonetic,
                                        style = MaterialTheme.typography.labelSmall.scaled(),
                                        color = if (isSelected) Color.White.copy(0.82f) else Color(0xFF546E7A)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Full-width content area
            val selectedEntry = uiState.selectedEntry
            if (selectedEntry != null) {
                AnimatedContent(
                    targetState = selectedEntry.id,
                    transitionSpec = { fadeIn(spring()) togetherWith fadeOut(spring()) },
                    label = "endingDetail"
                ) { _ ->
                    val e = uiState.selectedEntry ?: selectedEntry
                    Column(
                        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                            .padding(horizontal = Dimens16, vertical = Dimens12),
                        verticalArrangement = Arrangement.spacedBy(Dimens14)
                    ) {
                        L10BigEndingCard(
                            entry = e,
                            onLetterSoundTap = { viewModel.onLetterSoundTap(it) },
                            onEndingSoundTap = { viewModel.onEndingSoundTap(e) }
                        )
                        AnimatedVisibility(visible = uiState.showWords, enter = fadeIn(), exit = fadeOut()) {
                            L10ExampleWordsSection(
                                entry = e,
                                highlightedWordId = uiState.highlightedWordId,
                                onWordTap = { viewModel.onWordTap(it) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun L10BigEndingCard(
    entry: SpecialEndingEntry,
    onLetterSoundTap: (String) -> Unit,
    onEndingSoundTap: () -> Unit
) {
    val accent = entry.group.color
    val shadow = entry.group.shadowColor

    // Whole HStack inside a single glass card (matching iOS)
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens12),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
            .kidsGlassCard(cornerRadius = Dimens12, strokeColor = accent)
            .padding(vertical = Dimens8, horizontal = Dimens14)
    ) {
        // Left (40%): large displayEnding + phonetic capsule + emoji
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens10),
            modifier = Modifier.weight(0.4f)
                .background(accent.copy(0.08f), RoundedCornerShape(Dimens12))
                .padding(vertical = Dimens10)
        ) {
            Text(
                entry.displayEnding.uppercase(),
                style = MaterialTheme.typography.displaySmall.scaled(),
                fontWeight = FontWeight.Bold,
                color = accent
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.background(Color(0xFFECEFF1), RoundedCornerShape(50))
                    .padding(horizontal = Dimens8, vertical = Dimens2)
            ) {
                Text(entry.phonetic, style = MaterialTheme.typography.bodyMedium.scaled(), color = Color(0xFF546E7A))
            }
            Text(entry.group.emoji, style = MaterialTheme.typography.headlineMedium)
        }

        // Right (60%): Spelling Rule + Word End badge + spellingRule box + soundHint + letter circles
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimens10),
            modifier = Modifier.weight(0.6f)
                .padding(vertical = Dimens10, horizontal = Dimens4)
        ) {
            // Header: Spelling Rule title + Word End badge
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Dimens8)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Dimens6), modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.School, null, tint = accent, modifier = Modifier.size(Dimens16))
                    Text("Spelling Rule", style = MaterialTheme.typography.titleSmall.scaled(), fontWeight = FontWeight.Bold, color = accent)
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.background(accent, RoundedCornerShape(50)).padding(horizontal = Dimens8, vertical = Dimens2)
                ) {
                    Text("Word End", style = MaterialTheme.typography.labelSmall.scaled(), color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            // Spelling rule text box with colored bg + border
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(accent.copy(0.08f), RoundedCornerShape(Dimens8))
                    .border(Dp(1f), accent.copy(0.28f), RoundedCornerShape(Dimens8))
                    .padding(horizontal = Dimens10, vertical = Dimens6)
            ) {
                Text(
                    entry.spellingRule,
                    style = MaterialTheme.typography.bodyMedium.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF263238)
                )
            }

            // Sound hint
            Text(entry.soundHint, style = MaterialTheme.typography.bodyMedium.scaled(), color = Color(0xFF37474F))

            // Letter circles → phonetic button
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Dimens6)) {
                entry.ending.forEach { char ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(Dimens32).background(accent, CircleShape)
                            .clickable { onLetterSoundTap(char.toString()) }
                    ) {
                        Text(char.uppercaseChar().toString(), style = MaterialTheme.typography.labelLarge.scaled(), fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
                Icon(Icons.Default.ArrowForward, null, tint = Color(0xFF90A4AE), modifier = Modifier.size(Dimens20))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.shadow(4.dp, RoundedCornerShape(Dimens8), spotColor = accent.copy(0.4f))
                        .clip(RoundedCornerShape(Dimens8))
                        .background(Brush.linearGradient(listOf(accent, shadow)))
                        .clickable { onEndingSoundTap() }
                        .padding(horizontal = Dimens10, vertical = Dimens6)
                ) {
                    Text(entry.phonetic, style = MaterialTheme.typography.labelLarge.scaled(), fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun L10ExampleWordsSection(
    entry: SpecialEndingEntry,
    highlightedWordId: String?,
    onWordTap: (SpecialEndingWord) -> Unit
) {
    val accent = entry.group.color
    val shadow = entry.group.shadowColor

    Column(
        verticalArrangement = Arrangement.spacedBy(Dimens10),
        modifier = Modifier.fillMaxWidth().kidsGlassCard(cornerRadius = Dimens12, strokeColor = accent).padding(Dimens14)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Dimens6)) {
            Icon(Icons.Default.TouchApp, null, tint = accent, modifier = Modifier.size(Dimens16))
            Text("Tap a word to hear it", style = MaterialTheme.typography.labelLarge.scaled(), fontWeight = FontWeight.Bold, color = accent)
        }
        FlowRow(horizontalArrangement = Arrangement.spacedBy(Dimens8), verticalArrangement = Arrangement.spacedBy(Dimens8)) {
            entry.words.forEach { word ->
                val isHighlighted = word.id == highlightedWordId
                Box(modifier = Modifier.scale(if (isHighlighted) 1.04f else 1.0f).clickable { onWordTap(word) }) {
                    Box(modifier = Modifier.matchParentSize().offset(y = 3.dp).background(shadow, RoundedCornerShape(Dimens8)))
                    Row(
                        modifier = Modifier
                            .then(
                                if (isHighlighted) Modifier.shadow(6.dp, RoundedCornerShape(Dimens8), ambientColor = accent.copy(0.35f), spotColor = accent.copy(0.35f))
                                else Modifier.shadow(2.dp, RoundedCornerShape(Dimens8), ambientColor = accent.copy(0.08f), spotColor = accent.copy(0.08f))
                            )
                            .clip(RoundedCornerShape(Dimens8))
                            .background(
                                if (isHighlighted) Brush.linearGradient(listOf(accent, shadow))
                                else Brush.linearGradient(listOf(Color.White, Color.White))
                            )
                            .padding(horizontal = Dimens10, vertical = Dimens6)
                    ) {
                        Text(word.basePart, style = MaterialTheme.typography.bodyLarge.scaled(), fontWeight = FontWeight.Bold, color = if (isHighlighted) Color.White.copy(0.82f) else Color(0xFF263238).copy(0.55f))
                        Text(word.endingPart, style = MaterialTheme.typography.bodyLarge.scaled(), fontWeight = FontWeight.Bold, color = if (isHighlighted) Color.White else accent)
                    }
                }
            }
        }
    }
}
