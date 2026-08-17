package com.example.myapplication.main.age_group.phonics.l8_ending_blends

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.widthIn
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
import com.example.myapplication.main.age_group.phonics.l8_ending_blends.view_model.EndBlendEntry
import com.example.myapplication.main.age_group.phonics.l8_ending_blends.view_model.EndBlendGroup
import com.example.myapplication.main.age_group.phonics.l8_ending_blends.view_model.EndBlendLearnUiState
import com.example.myapplication.main.age_group.phonics.l8_ending_blends.view_model.EndBlendWord
import com.example.myapplication.main.age_group.phonics.l8_ending_blends.view_model.EndingBlendsLearnViewModel
import com.example.myapplication.main.age_group.phonics.l8_ending_blends.view_model.endingBlendsData
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
fun EndingBlendsLearnPage(
    navController: NavController,
    viewModel: EndingBlendsLearnViewModel = hiltViewModel()
) {
    // Times this visit for the parent report (Phonics tab)
    PhonicsLearnSessionEffect(PhonicsListenLevelKey.endingBlends)

    val uiState = viewModel.uiState

    LaunchedEffect(Unit) {
        val first = endingBlendsData.firstOrNull { it.group == EndBlendGroup.ND_NT }
        if (first != null && uiState.selectedBlend == null) viewModel.onBlendTap(first)
    }

    DisposableEffect(Unit) { onDispose { viewModel.stop() } }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.blueIndigo, shape = KidsFloatingShape.sparkles)

        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            // Header — now the BLEND list for the selected group, scrolling horizontally.
            // A group with only 1-2 blends must not pin them to the left with dead space
            // trailing — centre when the tiles fit, and fall back to scrolling once a
            // bigger group (like L-Endings' 4 tiles) actually overflows.
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                BackButtonWithText(title = "Ending Blends", expandWidth = false, onBackClick = { navController.popBackStack() })
                BoxWithConstraints(modifier = Modifier.weight(1f)) {
                    val availableWidth = maxWidth
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Dimens8, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .widthIn(min = availableWidth)
                            .horizontalScroll(rememberScrollState())
                            .padding(vertical = Dimens8)
                            .padding(horizontal = Dimens16)
                    ) {
                        val blendsInSelectedGroup = endingBlendsData.filter { it.group == uiState.selectedGroup }
                        blendsInSelectedGroup.forEach { blend ->
                            EndBlendHeaderTile(
                                blend = blend,
                                isSelected = uiState.selectedBlend?.blend == blend.blend,
                                onTap = { viewModel.onBlendTap(blend) }
                            )
                        }
                    }
                }
            }

            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val totalW = maxWidth
                Row(modifier = Modifier.fillMaxSize()) {
                    // Left 26% — now the CATEGORY picker, N/L/M/S/F-Endings, stacked vertically
                    EndBlendGroupList(
                        uiState = uiState,
                        modifier = Modifier.width(totalW * 0.26f).fillMaxHeight(),
                        onGroupTap = { viewModel.onGroupTap(it) }
                    )
                    // Right 74%
                    Box(modifier = Modifier.fillMaxSize()) {
                        val blend = uiState.selectedBlend
                        if (blend != null) {
                            AnimatedContent(
                                targetState = blend.blend,
                                transitionSpec = { fadeIn(spring()) togetherWith fadeOut(spring()) },
                                label = "endBlendDetail"
                            ) { _ ->
                                val b = uiState.selectedBlend ?: blend
                                EndBlendDetailView(
                                    blend = b,
                                    uiState = uiState,
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

// The CATEGORY picker — N/L/M/S/F-Endings — now stacked vertically on the left.
@Composable
private fun EndBlendGroupList(
    uiState: EndBlendLearnUiState,
    modifier: Modifier,
    onGroupTap: (EndBlendGroup) -> Unit
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()).padding(horizontal = Dimens10, vertical = Dimens12),
        verticalArrangement = Arrangement.spacedBy(Dimens8)
    ) {
        EndBlendGroup.entries.forEach { group ->
            EndBlendGroupTab(
                group = group,
                isSelected = uiState.selectedGroup == group,
                onTap = { onGroupTap(group) }
            )
        }
    }
}

@Composable
private fun EndBlendGroupTab(group: EndBlendGroup, isSelected: Boolean, onTap: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (isSelected) Modifier.shadow(6.dp, RoundedCornerShape(Dimens14), clip = false, ambientColor = group.color.copy(0.42f), spotColor = group.color.copy(0.42f)) else Modifier)
            .kidsGlassCard(cornerRadius = Dimens14, strokeColor = if (isSelected) Color.Transparent else group.color.copy(0.4f))
            .then(if (isSelected) Modifier.background(Brush.linearGradient(listOf(group.color, group.shadowColor)), RoundedCornerShape(Dimens14)) else Modifier)
            .clickable { onTap() }
            .padding(horizontal = Dimens10, vertical = Dimens8)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens8), verticalAlignment = Alignment.CenterVertically) {
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
                    color = if (isSelected) Color.White.copy(0.82f) else Color(0xFF546E7A)
                )
            }
        }
    }
}

// A single BLEND tile — now sits in the horizontally-scrolling header, sized to its own
// content rather than stretched (that stretch belonged to the old vertical list).
@Composable
private fun EndBlendHeaderTile(blend: EndBlendEntry, isSelected: Boolean, onTap: () -> Unit) {
    Box(modifier = Modifier.clickable { onTap() }) {
        Box(modifier = Modifier.matchParentSize().background(blend.group.shadowColor, RoundedCornerShape(Dimens8)).offset(y = 3.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens8),
            modifier = Modifier
                .then(if (isSelected) Modifier.shadow(6.dp, RoundedCornerShape(Dimens8), clip = false, ambientColor = blend.group.color.copy(0.35f), spotColor = blend.group.color.copy(0.35f)) else Modifier.shadow(2.dp, RoundedCornerShape(Dimens8), clip = false, ambientColor = blend.group.color.copy(0.08f), spotColor = blend.group.color.copy(0.08f)))
                .clip(RoundedCornerShape(Dimens8))
                .background(if (isSelected) Brush.linearGradient(listOf(blend.group.color, blend.group.shadowColor)) else Brush.linearGradient(listOf(Color.White, Color.White)))
                .padding(horizontal = Dimens10, vertical = Dimens8)
        ) {
            Text(text = blend.blend.uppercase(), style = MaterialTheme.typography.titleLarge.scaled(), fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else blend.group.color, modifier = Modifier.width(Dimens32 + Dimens8))
            Box(modifier = Modifier.width(1.dp).height(Dimens28).background(if (isSelected) Color.White.copy(0.35f) else Color(0xFFCFD8DC)))
            Column(verticalArrangement = Arrangement.spacedBy(Dimens2)) {
                Text(text = blend.phonetic, style = MaterialTheme.typography.labelMedium.scaled(), color = if (isSelected) Color.White.copy(0.90f) else Color(0xFF546E7A))
                Text(text = "${blend.words.size} words", style = MaterialTheme.typography.labelSmall.scaled(), color = if (isSelected) Color.White.copy(0.65f) else Color(0xFF90A4AE))
            }
        }
    }
}

@Composable
private fun EndBlendDetailView(
    blend: EndBlendEntry,
    uiState: EndBlendLearnUiState,
    onWordTap: (EndBlendWord) -> Unit,
    onLetterSoundTap: (String) -> Unit,
    onBlendSoundTap: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
            .padding(horizontal = Dimens14, vertical = Dimens10).padding(bottom = Dimens20),
        verticalArrangement = Arrangement.spacedBy(Dimens14)
    ) {
        // Big card
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens12), modifier = Modifier.fillMaxWidth()) {
            // Left: large letters + phonetic
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens8),
                modifier = Modifier.weight(1f).background(blend.group.color.copy(0.08f), RoundedCornerShape(Dimens12)).padding(vertical = Dimens20)
            ) {
                Row {
                    blend.blend.forEachIndexed { idx, char ->
                        Text(
                            text = char.uppercaseChar().toString(),
                            style = MaterialTheme.typography.displaySmall.scaled(),
                            fontWeight = FontWeight.ExtraBold,
                            color = if (idx == 0) blend.group.color else blend.group.shadowColor
                        )
                    }
                }
                Box(contentAlignment = Alignment.Center, modifier = Modifier.background(Color(0xFFECEFF1), RoundedCornerShape(50)).padding(horizontal = Dimens8, vertical = Dimens2)) {
                    Text(text = blend.phonetic, style = MaterialTheme.typography.bodyMedium.scaled(), color = Color(0xFF546E7A))
                }
            }
            // Right: how to say it
            Column(
                verticalArrangement = Arrangement.spacedBy(Dimens8),
                modifier = Modifier.weight(1f).kidsGlassCard(cornerRadius = Dimens12, strokeColor = blend.group.color).padding(horizontal = Dimens14, vertical = Dimens20)
            ) {
                Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(Dimens6), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.VolumeUp, null, tint = blend.group.color, modifier = Modifier.size(Dimens16))
                        Text("How to say it", style = MaterialTheme.typography.labelLarge.scaled(), fontWeight = FontWeight.Bold, color = blend.group.color)
                    }
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.background(blend.group.color, RoundedCornerShape(50)).padding(horizontal = Dimens8, vertical = Dimens2)) {
                        Text("Word End", style = MaterialTheme.typography.labelSmall.scaled(), color = Color.White)
                    }
                }
                Text(text = blend.soundHint, style = MaterialTheme.typography.bodyMedium.scaled(), color = Color(0xFF37474F))
                Row(horizontalArrangement = Arrangement.spacedBy(Dimens6), verticalAlignment = Alignment.CenterVertically) {
                    blend.blend.forEach { char ->
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(Dimens32).background(blend.group.color, CircleShape).clickable { onLetterSoundTap(char.toString()) }) {
                            Text(char.uppercaseChar().toString(), style = MaterialTheme.typography.labelLarge.scaled(), fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                    Icon(Icons.Default.ArrowForward, null, tint = Color(0xFF90A4AE), modifier = Modifier.size(Dimens20))
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.shadow(4.dp, RoundedCornerShape(Dimens8), spotColor = blend.group.color.copy(0.4f)).clip(RoundedCornerShape(Dimens8)).background(Brush.linearGradient(listOf(blend.group.color, blend.group.shadowColor))).clickable { onBlendSoundTap() }.padding(horizontal = Dimens10, vertical = Dimens4)) {
                        Text(blend.blend.uppercase(), style = MaterialTheme.typography.labelLarge.scaled(), fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        // Words
        AnimatedVisibility(visible = uiState.showWords, enter = fadeIn(), exit = fadeOut()) {
            EndBlendWordsSection(blend = blend, uiState = uiState, onWordTap = onWordTap)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EndBlendWordsSection(blend: EndBlendEntry, uiState: EndBlendLearnUiState, onWordTap: (EndBlendWord) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimens10),
        modifier = Modifier.fillMaxWidth().kidsGlassCard(cornerRadius = Dimens12, strokeColor = blend.group.color).padding(Dimens14)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens6), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.TouchApp, null, tint = blend.group.color, modifier = Modifier.size(Dimens16))
            Text("Tap a word to hear it", style = MaterialTheme.typography.labelLarge.scaled(), fontWeight = FontWeight.Bold, color = blend.group.color)
        }
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = Color(0xFF546E7A))) { append("👀 Focus on the ") }
                withStyle(SpanStyle(color = blend.group.color, fontWeight = FontWeight.Bold)) { append("last letters") }
                withStyle(SpanStyle(color = Color(0xFF546E7A))) { append(" — that's the ending blend! Some words use ") }
                withStyle(SpanStyle(color = Color(0xFFC62828), fontWeight = FontWeight.Bold)) { append("later-level sounds") }
                withStyle(SpanStyle(color = Color(0xFF546E7A))) { append(" — just listen and repeat!") }
            },
            style = MaterialTheme.typography.labelMedium.scaled()
        )
        FlowRow(horizontalArrangement = Arrangement.spacedBy(Dimens8), verticalArrangement = Arrangement.spacedBy(Dimens8)) {
            blend.words.forEach { word ->
                val isHighlighted = uiState.highlightedWordId == word.id
                Box(modifier = Modifier.scale(if (isHighlighted) 1.04f else 1.0f).clickable { onWordTap(word) }) {
                    Box(modifier = Modifier.matchParentSize().offset(y = 3.dp).background(blend.group.shadowColor, RoundedCornerShape(Dimens8)))
                    Row(modifier = Modifier
                        .then(if (isHighlighted) Modifier.shadow(6.dp, RoundedCornerShape(Dimens8), ambientColor = blend.group.color.copy(0.35f), spotColor = blend.group.color.copy(0.35f)) else Modifier.shadow(2.dp, RoundedCornerShape(Dimens8), ambientColor = blend.group.color.copy(0.08f), spotColor = blend.group.color.copy(0.08f)))
                        .clip(RoundedCornerShape(Dimens8))
                        .background(if (isHighlighted) Brush.linearGradient(listOf(blend.group.color, blend.group.shadowColor)) else Brush.linearGradient(listOf(Color.White, Color.White)))
                        .padding(horizontal = Dimens10, vertical = Dimens6)
                    ) {
                        // Ending blends: restPart first (gray), blendPart second (colored)
                        Text(word.restPart, style = MaterialTheme.typography.bodyMedium.scaled(), fontWeight = FontWeight.Bold,
                            color = if (isHighlighted) Color.White.copy(0.82f) else Color(0xFF263238).copy(0.55f))
                        Text(word.blendPart, style = MaterialTheme.typography.bodyMedium.scaled(), fontWeight = FontWeight.Bold,
                            color = if (isHighlighted) Color.White else blend.group.color)
                    }
                }
            }
        }
    }
}

