package com.example.myapplication.main.age_group.phonics.l19_soft_c_soft_g

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
import androidx.compose.material3.Divider
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
import com.example.myapplication.main.age_group.phonics.l19_soft_c_soft_g.view_model.SoftCSoftGGroup
import com.example.myapplication.main.age_group.phonics.l19_soft_c_soft_g.view_model.SoftCSoftGLearnViewModel
import com.example.myapplication.main.age_group.phonics.l19_soft_c_soft_g.view_model.SoftCSoftGWord
import com.example.myapplication.main.age_group.phonics.l19_soft_c_soft_g.view_model.softCSoftGGroups
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
import com.example.myapplication.utils.extensions.scaled
import com.example.myapplication.main.common.PhonicsWrongReadingCard
import com.example.myapplication.main.common.WrongReadingExample
import com.example.myapplication.main.common.PhonicsRuleBreakerCard
import com.example.myapplication.main.common.RuleBreakerEntry
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.example.myapplication.main.common.PhonicsIntroAudioViewModel
import androidx.compose.ui.draw.clip

@Composable
fun SoftCSoftGLearnPage(
    navController: NavController,
    viewModel: SoftCSoftGLearnViewModel = hiltViewModel()
) {
    // Times this visit for the parent report (Phonics tab)
    PhonicsLearnSessionEffect(PhonicsListenLevelKey.softCSoftG)

    val uiState = viewModel.uiState
    val selectedGroup = viewModel.selectedGroup

    LaunchedEffect(Unit) { viewModel.onScreenAppear() }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.sunsetCoral, shape = KidsFloatingShape.stars)

        Row(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            // ── LEFT (28%) — two-section tabs ──────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.28f)
                    .fillMaxHeight()
            ) {
                BackButtonWithText(title = "Soft C & Soft G", onBackClick = { navController.popBackStack() })

                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = Dimens10, vertical = Dimens8),
                    verticalArrangement = Arrangement.spacedBy(Dimens6)
                ) {
                    SectionLabel(letter = "C", color = Color(0xFFBF360C))
                    softCSoftGGroups.forEachIndexed { idx, group ->
                        if (group.letter == "c") {
                            SoftCGTab(
                                group      = group,
                                isSelected = uiState.selectedGroupIndex == idx,
                                onClick    = { viewModel.onGroupTap(idx) }
                            )
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = Dimens4))

                    SectionLabel(letter = "G", color = Color(0xFFE64A19))
                    softCSoftGGroups.forEachIndexed { idx, group ->
                        if (group.letter == "g") {
                            SoftCGTab(
                                group      = group,
                                isSelected = uiState.selectedGroupIndex == idx,
                                onClick    = { viewModel.onGroupTap(idx) }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
            }

            // ── RIGHT (rule + word grid) ────────────────────────────────────
            AnimatedContent(
                targetState = selectedGroup,
                transitionSpec = { fadeIn(spring()) togetherWith fadeOut(spring()) },
                modifier = Modifier.weight(1f).fillMaxHeight(),
                label = "softCGLearnContent"
            ) { group ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(Dimens14),
                    verticalArrangement = Arrangement.spacedBy(Dimens12)
                ) {
                    SoftCGRuleBanner(group = group)

                    SoftCGWhenCard(group = group)

                    if (uiState.showWords) {
                        SoftCGWordGrid(
                            group           = group,
                            highlightedWord = uiState.highlightedWord,
                            onWordTap       = { viewModel.onWordTap(it) }
                        )

                        PhonicsWrongReadingCard(accentColor = Color(0xFF0277BD), examples = scgWrongReading(group))

                        scgRuleBreakers(group)?.let { PhonicsRuleBreakerCard(entries = it) }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionLabel(letter: String, color: Color) {
    Row {
        Text(
            text  = letter,
            style = MaterialTheme.typography.labelSmall.scaled(),
            fontWeight = FontWeight.Bold,
            color = color,
            modifier = Modifier
                .background(color.copy(alpha = 0.12f), RoundedCornerShape(50))
                .padding(horizontal = Dimens8, vertical = Dimens2)
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun SoftCGTab(group: SoftCSoftGGroup, isSelected: Boolean, onClick: () -> Unit) {
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
                text  = group.title,
                style = MaterialTheme.typography.labelMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else group.accentColor
            )
            Text(
                text  = group.sound,
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = if (isSelected) Color.White.copy(alpha = 0.80f) else Color(0xFF78909C)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun SoftCGRuleBanner(group: SoftCSoftGGroup) {
    val letter = if (group.letter == "c") "C" else "G"

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens14),
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = 12.dp, strokeColor = group.accentColor)
            .padding(Dimens14)
    ) {
        val bannerAudioVm: PhonicsIntroAudioViewModel = hiltViewModel()
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(listOf(group.accentColor, group.shadowColor)),
                    RoundedCornerShape(12.dp)
                )
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                    bannerAudioVm.play(
                        when (group.title) {
                            "Soft C" -> "sound_s"
                            "Hard C" -> "sound_c"
                            "Soft G" -> "sound_j"
                            else     -> "sound_g"
                        }
                    )
                }
                .padding(horizontal = Dimens16, vertical = Dimens10),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text  = letter,
                style = MaterialTheme.typography.headlineLarge.scaled(),
                fontWeight = FontWeight.ExtraBold,
                color = group.accentColor.copy(alpha = 0.25f)
            )
            Text(
                text  = letter,
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
private fun SoftCGWordGrid(
    group: SoftCSoftGGroup,
    highlightedWord: String?,
    onWordTap: (SoftCSoftGWord) -> Unit
) {
    val highlightColor = if (group.isSoft) group.accentColor else Color(0xFF90A4AE)

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
                    SoftCGWordCard(
                        word           = word,
                        group          = group,
                        highlightColor = highlightColor,
                        isActive       = highlightedWord == word.word,
                        onClick        = { onWordTap(word) },
                        modifier       = Modifier.weight(1f)
                    )
                }
                repeat(4 - rowWords.size) { Spacer(modifier = Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
private fun SoftCGWordCard(
    word:           SoftCSoftGWord,
    group:          SoftCSoftGGroup,
    highlightColor: Color,
    isActive:       Boolean,
    onClick:        () -> Unit,
    modifier:       Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val scale by animateFloatAsState(
        targetValue   = if (isActive) 1.06f else 1.0f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 500f),
        label         = "softCGWordScale"
    )
    val bg = if (isActive)
        Brush.linearGradient(listOf(group.accentColor, group.shadowColor))
    else
        Brush.linearGradient(listOf(Color.White, Color.White))
    val normalColor = if (isActive) Color.White.copy(alpha = 0.88f) else Color(0xFF263238)
    val hlColor     = if (isActive) group.shadowColor else highlightColor
    val cardShape   = RoundedCornerShape(Dimens8)
    val borderColor = if (isActive) Color.White.copy(alpha = 0.40f) else highlightColor.copy(alpha = 0.20f)

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
                .padding(vertical = Dimens8, horizontal = Dimens6)
        ) {
            Row(horizontalArrangement = Arrangement.Center) {
                if (word.pre.isNotEmpty()) {
                    Text(word.pre, style = MaterialTheme.typography.titleSmall.scaled(),
                        fontWeight = FontWeight.Bold, color = normalColor)
                }
                Text(
                    word.highlight,
                    style = MaterialTheme.typography.titleSmall.scaled(),
                    fontWeight = FontWeight.ExtraBold,
                    color = hlColor,
                    modifier = Modifier
                        .background(
                            color = if (isActive) Color.White.copy(alpha = 0.82f) else highlightColor.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 2.dp)
                )
                if (word.suf.isNotEmpty()) {
                    Text(word.suf, style = MaterialTheme.typography.titleSmall.scaled(),
                        fontWeight = FontWeight.Bold, color = normalColor)
                }
            }
            if (isActive) {
                Text(text = "⭐", style = MaterialTheme.typography.labelSmall.scaled())
            }
        }
    }
}

// Per-group wrong-reading — follows the left-panel selection.
private fun scgWrongReading(group: SoftCSoftGGroup): List<WrongReadingExample> = when (group.title) {
    "Soft C" -> listOf(WrongReadingExample("/k/-ity (hard c)", "/s/-ity — e, i, y melt the c soft!", "city"))
    "Hard C" -> listOf(WrongReadingExample("/s/-at (soft c)", "/k/-at — a, o, u keep C tough!", "cat"))
    "Soft G" -> listOf(WrongReadingExample("/g/-em (hard g)", "/j/-em — e makes g gentle!", "gem"))
    else     -> listOf(WrongReadingExample("/j/-oat (soft g)", "/g/-oat — a, o, u keep G hard!", "goat"))
}

// ── When soft, when hard? (follows the left-panel selection) ─────────────────

@Composable
private fun SoftCGWhenCard(group: SoftCSoftGGroup) {
    val accent = Color(0xFF0277BD)
    val (lines, reminder) = when (group.title) {
        "Soft C" -> listOf(
            "🕵️ Why is this c SOFT? Look at the letter right AFTER it!",
            "❄️ **e, i, y** melt it → **/s/** like a snake: c**e**nt · c**i**ty · c**y**cle"
        ) to "Spot a softener (**e, i, y**) after the c → it ALWAYS goes soft!"
        "Hard C" -> listOf(
            "🕵️ Why is this c HARD? Look at the letter right AFTER it!",
            "💪 **a, o, u** (or nothing after) keep it tough → **/k/**: c**a**t · c**o**ld · c**u**p"
        ) to "No softener (**e, i, y**) in sight → c stays **tough /k/**!"
        "Soft G" -> listOf(
            "🕵️ Why is this g GENTLE? Look at the letter right AFTER it!",
            "💎 **e, i, y** make it gentle → **/j/** like a jar: g**e**m · g**i**raffe · g**y**m"
        ) to "Spot a softener (**e, i, y**) after the g → it turns gentle!"
        else -> listOf(
            "🕵️ Why is this g HARD? Look at the letter right AFTER it!",
            "💪 **a, o, u** (or nothing after) keep it strong → **/g/**: g**a**te · g**o**at · g**u**m"
        ) to "No softener (**e, i, y**) in sight → g stays **strong /g/**!"
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(Dimens8),
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = Dimens12, strokeColor = accent)
            .padding(Dimens12)
    ) {
        Text(
            text = "❓ When soft, when hard?",
            style = MaterialTheme.typography.titleSmall.scaled(),
            fontWeight = FontWeight.Bold,
            color = accent
        )
        (lines + reminder).forEachIndexed { idx, line ->
            val bold = if (idx == lines.size) Color(0xFFC62828) else accent
            Text(
                text = buildAnnotatedString {
                    line.split("**").forEachIndexed { i, part ->
                        if (i % 2 == 1) withStyle(SpanStyle(color = bold, fontWeight = FontWeight.Bold)) { append(part) }
                        else withStyle(SpanStyle(color = Color(0xFF37474F))) { append(part) }
                    }
                },
                style = MaterialTheme.typography.labelMedium.scaled()
            )
        }
    }
}

private fun scgRuleBreakers(group: SoftCSoftGGroup): List<RuleBreakerEntry>? =
    if (group.title == "Soft G") listOf(
        RuleBreakerEntry("get", "hard /g/ before e — a rebel!"),
        RuleBreakerEntry("girl", "hard /g/ before i — catch it!"),
        RuleBreakerEntry("give", "the i doesn't soften this one!"),
        RuleBreakerEntry("gift", "hard /g/ before i — another rebel!")
    ) else null
