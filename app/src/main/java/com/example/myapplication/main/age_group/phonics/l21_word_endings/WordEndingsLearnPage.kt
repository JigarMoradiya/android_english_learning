package com.example.myapplication.main.age_group.phonics.l21_word_endings

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.l21_word_endings.view_model.*
import com.example.myapplication.main.common.*
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

private val WordEndingRule.icon: ImageVector
    get() = when (this) {
        WordEndingRule.JUST_ADD -> Icons.Default.AddCircle
        WordEndingRule.DOUBLE   -> Icons.Default.Repeat
        WordEndingRule.DROP_E   -> Icons.Default.RemoveCircle
    }

@Composable
fun WordEndingsLearnPage(
    navController: NavController,
    viewModel: WordEndingsLearnViewModel = hiltViewModel()
) {
    val uiState       = viewModel.uiState
    val selectedGroup = viewModel.selectedGroup

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.meadowGreen, shape = KidsFloatingShape.leaves)

        BoxWithConstraints(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            val totalW = maxWidth
            Row(modifier = Modifier.fillMaxSize()) {

                // ── LEFT 26% ──────────────────────────────────────────────────
                Column(
                    modifier = Modifier
                        .width(totalW * 0.26f)
                        .fillMaxHeight(),

                ) {
                    BackButtonWithText(title = "Word Endings", onBackClick = { navController.popBackStack() })
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = Dimens10)
                            .padding(top = Dimens8, bottom = Dimens8),
                        verticalArrangement = Arrangement.spacedBy(Dimens8),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        wordEndingGroups.forEachIndexed { idx, group ->
                            WELGroupButton(
                                group      = group,
                                isSelected = uiState.selectedGroupIndex == idx,
                                onClick    = { viewModel.onGroupTap(idx) }
                            )
                        }
                        WELRuleLegend()
                    }
                }

                // ── RIGHT 74% ─────────────────────────────────────────────────
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState())
                        .padding(Dimens14),
                    verticalArrangement = Arrangement.spacedBy(Dimens12)
                ) {
                    WELGroupHeader(group = selectedGroup)
                    WELRulesExplanation(group = selectedGroup)
                    LazyVerticalGrid(
                        columns               = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(Dimens10),
                        verticalArrangement   = Arrangement.spacedBy(Dimens10),
                        contentPadding        = PaddingValues(bottom = Dimens8),
                        modifier              = Modifier.heightIn(max = 2000.dp)
                    ) {
                        items(selectedGroup.words) { word ->
                            WELTransformCard(
                                word     = word,
                                group    = selectedGroup,
                                isActive = uiState.tappedWordId == word.id,
                                onClick  = { viewModel.onWordTap(word) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Left panel: group button (Row: emoji + suffix + meaning) ──────────────────

@Composable
private fun WELGroupButton(group: WordEndingGroup, isSelected: Boolean, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val bgMod = if (isSelected)
        Modifier.background(Brush.linearGradient(listOf(group.accentColor, group.shadowColor)), RoundedCornerShape(12.dp))
    else
        Modifier.background(Color.White.copy(alpha = 0.75f), RoundedCornerShape(12.dp))
    val borderColor = if (isSelected) Color.White.copy(alpha = 0.30f) else group.accentColor.copy(alpha = 0.25f)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens8),
        modifier = Modifier
            .fillMaxWidth()
            .then(bgMod)
            .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .padding(vertical = Dimens8, horizontal = Dimens10)
    ) {
        Text(text = group.emoji, style = MaterialTheme.typography.bodyMedium.scaled())
        Column(verticalArrangement = Arrangement.spacedBy(Dimens2)) {
            Text(
                text       = group.suffix,
                style      = MaterialTheme.typography.titleSmall.scaled(),
                fontWeight = FontWeight.Bold,
                color      = if (isSelected) Color.White else group.accentColor
            )
            Text(
                text    = group.meaning,
                style   = MaterialTheme.typography.labelSmall.scaled(),
                color   = if (isSelected) Color.White.copy(alpha = 0.80f) else Color(0xFF546E7A),
                maxLines = 2
            )
        }
    }
}

// ── Left panel: rule legend at bottom ─────────────────────────────────────────

@Composable
private fun WELRuleLegend() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .kidsGlassCard(cornerRadius = 12.dp, strokeColor = Color(0xFF2E7D32).copy(alpha = 0.30f))
            .padding(Dimens10),
        verticalArrangement = Arrangement.spacedBy(Dimens6)
    ) {
        Text(
            text       = "Spelling rules",
            style      = MaterialTheme.typography.labelSmall.scaled(),
            fontWeight = FontWeight.Bold,
            color      = Color(0xFF546E7A)
        )
        listOf(WordEndingRule.JUST_ADD, WordEndingRule.DOUBLE, WordEndingRule.DROP_E).forEach { rule ->
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens6)
            ) {
                Icon(rule.icon, null, tint = rule.color, modifier = Modifier.size(12.dp))
                Text(
                    text  = rule.label,
                    style = MaterialTheme.typography.labelSmall.scaled(),
                    color = rule.color
                )
            }
        }
    }
}

// ── Right panel: group header card ───────────────────────────────────────────

@Composable
private fun WELGroupHeader(group: WordEndingGroup) {
    Row(
        verticalAlignment      = Alignment.CenterVertically,
        horizontalArrangement  = Arrangement.spacedBy(Dimens14),
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
                text       = group.suffix,
                style      = MaterialTheme.typography.headlineLarge.scaled(),
                fontWeight = FontWeight.Bold,
                color      = Color.White
            )
        }
        Text(text = group.emoji, style = MaterialTheme.typography.headlineMedium.scaled())
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimens4),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text       = group.meaning,
                style      = MaterialTheme.typography.titleSmall.scaled(),
                fontWeight = FontWeight.Bold,
                color      = group.accentColor
            )
            Text(
                text  = "Tap a card to see the spelling trick 👆",
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = Color(0xFF546E7A)
            )
        }
    }
}

// ── Right panel: 3-tile rules explanation (DOUBLE / DROP-E / JUST ADD) ────────

@Composable
private fun WELRulesExplanation(group: WordEndingGroup) {
    val suf      = group.suffix.drop(1)
    val jaWord   = group.words.firstOrNull { it.rule == WordEndingRule.JUST_ADD }
    val dbWord   = group.words.firstOrNull { it.rule == WordEndingRule.DOUBLE }
    val deWord   = group.words.firstOrNull { it.rule == WordEndingRule.DROP_E }

    fun splitDerived(word: WordEndingWord?): Pair<String, String> {
        if (word == null) return "—" to suf
        return word.derived.dropLast(group.suffixLen) to word.derived.takeLast(group.suffixLen)
    }

    val (dbRoot, dbSuf) = splitDerived(dbWord)
    val (deRoot, deSuf) = splitDerived(deWord)
    val (jaRoot, jaSuf) = splitDerived(jaWord)

    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens10),
        modifier              = Modifier.fillMaxWidth()
    ) {
        WELRuleExplainTile(
            rule     = WordEndingRule.DOUBLE,
            whenStr  = "1 short vowel + 1 final consonant",
            howStr   = "Double the last letter",
            base     = dbWord?.base ?: "—",
            root     = dbRoot, suffix = dbSuf,
            note     = "Vowel teams (ay, ee…) = long vowel → just add!",
            modifier = Modifier.weight(1f)
        )
        WELRuleExplainTile(
            rule     = WordEndingRule.DROP_E,
            whenStr  = "Word ends in silent 'e'",
            howStr   = "Drop the 'e' first",
            base     = deWord?.base ?: "—",
            root     = deRoot, suffix = deSuf,
            modifier = Modifier.weight(1f)
        )
        WELRuleExplainTile(
            rule     = WordEndingRule.JUST_ADD,
            whenStr  = "Most words",
            howStr   = "Just add the ending",
            base     = jaWord?.base ?: "—",
            root     = jaRoot, suffix = jaSuf,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun WELRuleExplainTile(
    rule:     WordEndingRule,
    whenStr:  String,
    howStr:   String,
    base:     String,
    root:     String,
    suffix:   String,
    note:     String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color.White.copy(alpha = 0.88f), RoundedCornerShape(12.dp))
            .border(1.5.dp, rule.color.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
    ) {
        // Colored header badge
        Row(
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens4),
            modifier = Modifier
                .fillMaxWidth()
                .background(rule.color, RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .padding(horizontal = Dimens8, vertical = Dimens4)
        ) {
            Icon(rule.icon, null, tint = Color.White, modifier = Modifier.size(12.dp))
            Text(
                text       = rule.label.uppercase(),
                style      = MaterialTheme.typography.labelSmall.scaled(),
                fontWeight = FontWeight.Bold,
                color      = Color.White
            )
        }
        Column(
            modifier = Modifier.padding(horizontal = Dimens8, vertical = Dimens8),
            verticalArrangement = Arrangement.spacedBy(Dimens4)
        ) {
            // WHEN section
            Column(verticalArrangement = Arrangement.spacedBy(Dimens2)) {
                Text("WHEN", style = MaterialTheme.typography.labelSmall.scaled(),
                    fontWeight = FontWeight.Bold, color = rule.color)
                Text(whenStr, style = MaterialTheme.typography.labelSmall.scaled(),
                    color = Color(0xFF455A64))
            }
            // HOW section
            Column(verticalArrangement = Arrangement.spacedBy(Dimens2)) {
                Text("HOW", style = MaterialTheme.typography.labelSmall.scaled(),
                    fontWeight = FontWeight.Bold, color = rule.color)
                Text(howStr, style = MaterialTheme.typography.labelSmall.scaled(),
                    color = Color(0xFF455A64))
            }
            // Mini example: root + colored suffix
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text       = root,
                    style      = MaterialTheme.typography.labelMedium.scaled(),
                    fontWeight = FontWeight.Bold,
                    color      = Color(0xFF263238)
                )
                Text(
                    text       = suffix,
                    style      = MaterialTheme.typography.labelMedium.scaled(),
                    fontWeight = FontWeight.Bold,
                    color      = rule.color,
                    modifier   = Modifier
                        .background(rule.color.copy(alpha = 0.12f), RoundedCornerShape(4.dp))
                        .padding(horizontal = Dimens4)
                )
            }
            // Optional tip note
            if (note != null) {
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens4),
                    modifier = Modifier
                        .background(Color(0xFFFFF8E1), RoundedCornerShape(6.dp))
                        .border(1.dp, Color(0xFFFFE082), RoundedCornerShape(6.dp))
                        .padding(horizontal = Dimens6, vertical = Dimens4)
                ) {
                    Icon(Icons.Default.Lightbulb, null,
                        tint = Color(0xFFF57F17), modifier = Modifier.size(10.dp))
                    Text(note, style = MaterialTheme.typography.labelSmall.scaled(),
                        color = Color(0xFFE65100))
                }
            }
        }
    }
}

// ── Right panel: transform word card ─────────────────────────────────────────

@Composable
private fun WELTransformCard(
    word:     WordEndingWord,
    group:    WordEndingGroup,
    isActive: Boolean,
    onClick:  () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val scale by animateFloatAsState(
        targetValue   = if (isActive) 1.04f else 1.0f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 400f),
        label         = "weTransformScale"
    )
    val shape    = RoundedCornerShape(12.dp)
    val rootPart = word.derived.dropLast(group.suffixLen)
    val sufPart  = word.derived.takeLast(group.suffixLen)

    Box(modifier = Modifier.fillMaxWidth().scale(scale)) {
        // Shadow layer
        Box(modifier = Modifier.matchParentSize().offset(y = 3.dp).background(group.shadowColor, shape))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (isActive) Brush.linearGradient(listOf(group.accentColor, group.shadowColor))
                    else Brush.linearGradient(listOf(Color.White, Color.White)),
                    shape
                )
                .border(
                    1.5.dp,
                    if (isActive) Color.White.copy(alpha = 0.35f) else group.accentColor.copy(alpha = 0.20f),
                    shape
                )
                .clickable(interactionSource = interactionSource, indication = null) { onClick() }
                .padding(Dimens12),
            verticalArrangement = Arrangement.spacedBy(Dimens10)
        ) {
            // Row: base [chip] → arrow → rootPart + suffixHighlighted
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens6)
            ) {
                // Base word chip (ECEFF1 background)
                Text(
                    text       = word.base,
                    style      = MaterialTheme.typography.titleSmall.scaled(),
                    fontWeight = FontWeight.Bold,
                    color      = if (isActive) Color(0xFFECEFF1) else Color(0xFF455A64),
                    modifier   = Modifier
                        .background(
                            if (isActive) Color.White.copy(alpha = 0.20f) else Color(0xFFECEFF1),
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = Dimens8, vertical = Dimens4)
                )
                // Arrow icon (388E3C)
                Text(
                    text  = "→",
                    style = MaterialTheme.typography.titleSmall.scaled(),
                    color = if (isActive) Color.White.copy(alpha = 0.80f) else Color(0xFF388E3C)
                )
                // Derived: rootPart + suffixHighlighted
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text       = rootPart,
                        style      = MaterialTheme.typography.titleSmall.scaled(),
                        fontWeight = FontWeight.Bold,
                        color      = if (isActive) Color.White else Color(0xFF263238)
                    )
                    Text(
                        text       = sufPart,
                        style      = MaterialTheme.typography.titleSmall.scaled(),
                        fontWeight = FontWeight.Bold,
                        color      = if (isActive) Color.White else group.accentColor,
                        modifier   = Modifier
                            .background(
                                if (isActive) Color.White.copy(alpha = 0.25f) else group.accentColor.copy(alpha = 0.15f),
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = Dimens4, vertical = Dimens2)
                    )
                }
            }
            // Rule badge capsule
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens4),
                modifier = Modifier
                    .background(
                        if (isActive) word.rule.color.copy(alpha = 0.30f) else word.rule.color.copy(alpha = 0.10f),
                        RoundedCornerShape(20.dp)
                    )
                    .border(
                        1.dp,
                        if (isActive) word.rule.color.copy(alpha = 0.45f) else word.rule.color.copy(alpha = 0.25f),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = Dimens8, vertical = Dimens4)
            ) {
                Icon(word.rule.icon, null,
                    tint     = if (isActive) Color.White else word.rule.color,
                    modifier = Modifier.size(11.dp))
                Text(
                    text  = word.rule.label,
                    style = MaterialTheme.typography.labelSmall.scaled(),
                    color = if (isActive) Color.White.copy(alpha = 0.90f) else word.rule.color
                )
            }
        }
    }
}

private val Dimens16 = 16.dp
