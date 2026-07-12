package com.example.myapplication.main.age_group.phonics.l17_y_as_vowel

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.PhonicsLearnSessionEffect
import com.example.myapplication.main.age_group.phonics.listen.view_model.PhonicsListenLevelKey
import com.example.myapplication.main.age_group.phonics.l17_y_as_vowel.view_model.YAsVowelGroup
import com.example.myapplication.main.age_group.phonics.l17_y_as_vowel.view_model.YAsVowelLearnViewModel
import com.example.myapplication.main.age_group.phonics.l17_y_as_vowel.view_model.YAsVowelWord
import com.example.myapplication.main.age_group.phonics.l17_y_as_vowel.view_model.yAsVowelGroups
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.kidsGlassCard
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens14
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.utils.extensions.scaled

@Composable
fun YAsVowelLearnPage(
    navController: NavController,
    viewModel: YAsVowelLearnViewModel = hiltViewModel()
) {
    // Times this visit for the parent report (Phonics tab)
    PhonicsLearnSessionEffect(PhonicsListenLevelKey.yAsVowel)

    val uiState = viewModel.uiState
    val selectedGroup = viewModel.selectedGroup

    LaunchedEffect(Unit) { viewModel.onScreenAppear() }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.tealCyan, shape = KidsFloatingShape.stars)

        Row(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            // ── LEFT (group cards, 28%) ─────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.25f)
                    .fillMaxHeight()
            ) {
                BackButtonWithText(title = "Y as a Vowel", onBackClick = { navController.popBackStack() })

                Column(
                    modifier = Modifier.padding(horizontal = Dimens10, vertical = Dimens8),
                    verticalArrangement = Arrangement.spacedBy(Dimens8)
                ) {
                    yAsVowelGroups.forEachIndexed { index, group ->
                        YGroupCard(
                            group      = group,
                            isSelected = uiState.selectedGroupIndex == index,
                            onClick    = { viewModel.onGroupTap(index) }
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
            }

            // ── RIGHT (rule + word grid) ────────────────────────────────────
            AnimatedContent(
                targetState = selectedGroup,
                transitionSpec = { fadeIn(spring()) togetherWith fadeOut(spring()) },
                modifier = Modifier.weight(1f).fillMaxHeight(),
                label = "yLearnContent"
            ) { group ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(Dimens14),
                    verticalArrangement = Arrangement.spacedBy(Dimens12)
                ) {
                    YRuleBanner(group = group)

                    if (uiState.showWords) {
                        YWordGrid(
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
private fun YGroupCard(group: YAsVowelGroup, isSelected: Boolean, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val bg = if (isSelected) {
        Modifier.background(
            Brush.linearGradient(listOf(group.accentColor, group.shadowColor)),
            RoundedCornerShape(12.dp)
        )
    } else {
        Modifier.background(Color.White.copy(alpha = 0.75f), RoundedCornerShape(12.dp))
    }
    val borderColor = if (isSelected) Color.White.copy(alpha = 0.30f) else group.accentColor.copy(alpha = 0.25f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens4),
        modifier = Modifier
            .fillMaxWidth()
            .then(bg)
            .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .padding(vertical = Dimens8, horizontal = Dimens6)
    ) {
        Text(text = group.emoji, style = MaterialTheme.typography.titleSmall.scaled())
        Text(
            text  = group.label,
            style = MaterialTheme.typography.labelMedium.scaled(),
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.White else group.accentColor,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Text(
            text  = group.sound,
            style = MaterialTheme.typography.labelSmall.scaled(),
            color = if (isSelected) Color.White.copy(alpha = 0.80f) else Color(0xFF78909C)
        )
    }
}

@Composable
private fun YRuleBanner(group: YAsVowelGroup) {
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
                .background(
                    Brush.linearGradient(listOf(group.accentColor, group.shadowColor)),
                    RoundedCornerShape(12.dp)
                )
                .padding(horizontal = Dimens16, vertical = Dimens10),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text  = "Y",
                style = MaterialTheme.typography.headlineLarge.scaled(),
                fontWeight = FontWeight.ExtraBold,
                color = group.accentColor.copy(alpha = 0.25f)
            )
            Text(
                text  = "Y",
                style = MaterialTheme.typography.headlineLarge.scaled(),
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
        }

        Icon(
            Icons.Default.ArrowForward, contentDescription = null,
            tint = group.accentColor, modifier = Modifier.padding(horizontal = Dimens4)
        )

        Text(text = group.emoji, style = MaterialTheme.typography.headlineMedium.scaled())

        Text(
            text  = group.sound,
            style = MaterialTheme.typography.titleMedium.scaled(),
            fontWeight = FontWeight.Bold,
            color = group.accentColor
        )

        Box(
            modifier = Modifier
                .width(1.dp)
                .height(48.dp)
                .background(group.accentColor.copy(alpha = 0.30f))
        )

        Column(verticalArrangement = Arrangement.spacedBy(Dimens4), modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens4)
            ) {
                Icon(Icons.Default.Lightbulb, contentDescription = null, tint = group.accentColor)
                Text(
                    text  = "The Rule",
                    style = MaterialTheme.typography.labelLarge.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = group.accentColor
                )
            }
            Text(
                text  = group.rule,
                style = MaterialTheme.typography.bodySmall.scaled(),
                color = Color(0xFF455A64)
            )
        }
    }
}

@Composable
private fun YWordGrid(
    group: YAsVowelGroup,
    highlightedWord: String?,
    onWordTap: (YAsVowelWord) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = 12.dp, strokeColor = group.accentColor)
            .padding(Dimens14),
        verticalArrangement = Arrangement.spacedBy(Dimens10)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens6)
        ) {
            Icon(Icons.Default.TouchApp, contentDescription = null, tint = group.accentColor)
            Text(
                text  = "Tap a word to hear it ✨",
                style = MaterialTheme.typography.titleSmall.scaled(),
                fontWeight = FontWeight.Bold,
                color = group.accentColor
            )
        }

        val chunked = group.words.chunked(4)
        chunked.forEach { rowWords ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens8),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                rowWords.forEach { word ->
                    YWordCard(
                        word     = word,
                        group    = group,
                        isActive = highlightedWord == word.word,
                        onClick  = { onWordTap(word) },
                        modifier = Modifier.weight(1f)
                    )
                }
                repeat(4 - rowWords.size) { Spacer(modifier = Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
private fun YWordCard(
    word:     YAsVowelWord,
    group:    YAsVowelGroup,
    isActive: Boolean,
    onClick:  () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val scale by animateFloatAsState(
        targetValue   = if (isActive) 1.06f else 1.0f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 500f),
        label         = "yWordScale"
    )
    val bg = if (isActive)
        Brush.linearGradient(listOf(group.accentColor, group.shadowColor))
    else
        Brush.linearGradient(listOf(Color.White, Color.White))
    val normalColor = if (isActive) Color.White.copy(alpha = 0.88f) else Color(0xFF263238)
    val hlColor     = if (isActive) group.shadowColor else group.accentColor
    val cardShape   = RoundedCornerShape(Dimens8)
    val borderColor = if (isActive) Color.White.copy(alpha = 0.40f) else group.accentColor.copy(alpha = 0.20f)

    Box(modifier = modifier.scale(scale)) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 3.dp)
                .background(group.shadowColor.copy(alpha = if (isActive) 0f else 0.55f), cardShape)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens4),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(if (isActive) 8.dp else 2.dp, cardShape,
                    ambientColor = group.accentColor, spotColor = group.accentColor)
                .then(
                    if (isActive) Modifier.background(bg, cardShape)
                    else Modifier.background(Color.White, cardShape)
                )
                .border(1.5.dp, borderColor, cardShape)
                .clickable(interactionSource = interactionSource, indication = null) { onClick() }
                .padding(vertical = Dimens8, horizontal = Dimens10)
        ) {
            Row(horizontalArrangement = Arrangement.Center) {
                if (word.pre.isNotEmpty()) {
                    Text(
                        word.pre,
                        style = MaterialTheme.typography.titleSmall.scaled(),
                        fontWeight = FontWeight.Bold,
                        color = normalColor
                    )
                }
                Text(
                    word.highlight,
                    style = MaterialTheme.typography.titleSmall.scaled(),
                    fontWeight = FontWeight.ExtraBold,
                    color = hlColor,
                    modifier = Modifier
                        .background(
                            color = if (isActive) Color.White.copy(alpha = 0.82f) else group.accentColor.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 2.dp)
                )
                if (word.suf.isNotEmpty()) {
                    Text(
                        word.suf,
                        style = MaterialTheme.typography.titleSmall.scaled(),
                        fontWeight = FontWeight.Bold,
                        color = normalColor
                    )
                }
            }
            if (isActive) {
                Text(text = "⭐", style = MaterialTheme.typography.labelSmall.scaled())
            }
        }
    }
}
