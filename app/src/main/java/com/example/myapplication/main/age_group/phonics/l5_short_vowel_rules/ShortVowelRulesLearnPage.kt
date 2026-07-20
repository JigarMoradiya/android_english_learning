package com.example.myapplication.main.age_group.phonics.l5_short_vowel_rules

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.PhonicsLearnSessionEffect
import com.example.myapplication.main.age_group.phonics.listen.view_model.PhonicsListenLevelKey
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.main.age_group.phonics.l5_short_vowel_rules.view_model.RuleComparison
import com.example.myapplication.main.age_group.phonics.l5_short_vowel_rules.view_model.RuleWordExample
import com.example.myapplication.main.age_group.phonics.l5_short_vowel_rules.view_model.RuleWordExampleGroup
import com.example.myapplication.main.age_group.phonics.l5_short_vowel_rules.view_model.ShortVowelRulesViewModel
import com.example.myapplication.main.age_group.phonics.l5_short_vowel_rules.view_model.SpellingRule
import com.example.myapplication.main.age_group.phonics.l5_short_vowel_rules.view_model.spellingRulesData
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ShortVowelRulesLearnPage(
    navController: NavController,
    viewModel: ShortVowelRulesViewModel = hiltViewModel()
) {
    // Times this visit for the parent report (Phonics tab)
    PhonicsLearnSessionEffect(PhonicsListenLevelKey.shortVowelRules)

    val uiState = viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.onRuleTap(spellingRulesData[0])
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.stop() }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.oceanBlue, shape = KidsFloatingShape.waves)

        Row(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            // ── LEFT: rule list (side-by-side, like L13/L15 — header tabs cut the cards) ──
            Column(
                modifier = Modifier
                    .weight(0.25f)
                    .fillMaxHeight()
            ) {
                BackButtonWithText(
                    title = "Spelling Rules",
                    onBackClick = { navController.popBackStack() }
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = Dimens10, vertical = Dimens8),
                    verticalArrangement = Arrangement.spacedBy(Dimens6)
                ) {
                    spellingRulesData.forEach { rule ->
                        RuleTab(
                            rule = rule,
                            isSelected = uiState.selectedRule?.id == rule.id,
                            onClick = { viewModel.onRuleTap(rule) }
                        )
                    }
                }
            }

            // ── RIGHT: content — key(rule.id) resets scroll when rule changes ──
            Box(modifier = Modifier.weight(0.75f).fillMaxHeight().padding(start = Dimens12)) {
            uiState.selectedRule?.let { rule ->
                key(rule.id) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(top = Dimens6, bottom = Dimens14)
                        .padding(horizontal = Dimens16),
                    verticalArrangement = Arrangement.spacedBy(Dimens14)
                ) {
                    // THE RULE — full width
                    SectionCard(
                        icon = Icons.Default.Book,
                        iconColor = rule.color,
                        label = "THE RULE"
                    ) {
                        RichText(
                            text = rule.ruleStatement,
                            highlight = rule.color,
                            baseColor = Color(0xFF263238)
                        )
                    }

                    // WHEN TO USE + WHY IT WORKS — side by side
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Dimens12),
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        SectionCard(
                            icon = Icons.Default.Schedule,
                            iconColor = Color(0xFF1565C0),
                            label = "WHEN TO USE",
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(Dimens8)) {
                                rule.whenToUse.forEach { point ->
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(Dimens8),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .padding(top = Dimens6)
                                                .size(Dimens6)
                                                .background(rule.color, CircleShape)
                                        )
                                        RichText(
                                            text = point,
                                            highlight = rule.color,
                                            baseColor = Color(0xFF263238),
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }

                        SectionCard(
                            icon = Icons.Default.LightMode,
                            iconColor = Color(0xFFF9A825),
                            label = "WHY IT WORKS",
                            modifier = Modifier.weight(1f)
                        ) {
                            RichText(
                                text = rule.whyItWorks,
                                highlight = rule.color,
                                baseColor = Color(0xFF263238)
                            )
                        }
                    }

                    // WATCH OUT + MEMORY TIP — side by side
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Dimens12),
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        SectionCard(
                            icon = Icons.Default.Cancel,
                            iconColor = Color(0xFFE53935),
                            label = "WATCH OUT",
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(Dimens8)) {
                                rule.watchOut.forEach { point ->
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(Dimens8),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Cancel,
                                            contentDescription = null,
                                            tint = Color(0xFFE53935),
                                            modifier = Modifier
                                                .padding(top = Dimens4)
                                                .size(Dimens14)
                                        )
                                        RichText(
                                            text = point,
                                            highlight = Color(0xFFC62828),
                                            baseColor = Color(0xFF37474F),
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }

                        // Memory tip — green card
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Dimens10),
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier
                                .weight(1f)
                                .background(Color(0xFFE8F5E9), RoundedCornerShape(Dimens12))
                                .padding(Dimens14)
                        ) {
                            Text(
                                text = "💡",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(Dimens4)) {
                                Text(
                                    text = "MEMORY TIP",
                                    style = MaterialTheme.typography.bodySmall.scaled(),
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2E7D32),
                                    letterSpacing = 0.8.sp
                                )
                                RichText(
                                    text = rule.tip,
                                    highlight = Color(0xFF1B5E20),
                                    baseColor = Color(0xFF1B5E20)
                                )
                            }
                        }
                    }

                    // COMPARE — full width
                    if (rule.comparisons.isNotEmpty()) {
                        CompareSection(rule = rule)
                    }

                    // EXAMPLES — full width
                    ExamplesSection(
                        rule = rule,
                        showExamples = uiState.showExamples,
                        highlightedId = uiState.highlightedExampleId,
                        onExampleTap = { viewModel.onExampleTap(it) }
                    )

                    Spacer(modifier = Modifier.height(Dimens20))
                }
                } // key
            } // let
            } // right Box
        }
    }
}

// ── Rule Tab ──────────────────────────────────────────────────────────────────

@Composable
private fun RuleTab(rule: SpellingRule, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) rule.color else Color.White.copy(alpha = 0.22f),
        animationSpec = spring(), label = "tabBg"
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else rule.color,
        animationSpec = spring(), label = "tabText"
    )
    val subColor by animateColorAsState(
        targetValue = if (isSelected) Color.White.copy(alpha = 0.82f) else Color(0xFF546E7A),
        animationSpec = spring(), label = "tabSub"
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens8),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(
                cornerRadius = Dimens14,
                strokeColor = if (isSelected) rule.color else rule.color.copy(alpha = 0.40f),
                elevation = if (isSelected) 6.dp else 2.dp
            )
            .background(bgColor, RoundedCornerShape(Dimens14))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(horizontal = Dimens12, vertical = Dimens6)
    ) {
        Text(
            text = rule.emoji,
            style = MaterialTheme.typography.titleMedium
        )
        Column {
            Text(
                text = rule.name,
                style = MaterialTheme.typography.bodyMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = textColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = rule.pattern,
                style = MaterialTheme.typography.bodySmall.scaled(),
                color = subColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ── Section Card ──────────────────────────────────────────────────────────────

@Composable
private fun SectionCard(
    icon: ImageVector,
    iconColor: Color,
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimens10),
        modifier = modifier
            .kidsGlassCard(cornerRadius = Dimens12, strokeColor = iconColor.copy(alpha = 0.35f))
            .padding(Dimens14)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens6),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(Dimens16)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.scaled(),
                fontWeight = FontWeight.Bold,
                color = iconColor,
                letterSpacing = 0.8.sp
            )
        }
        content()
    }
}

// ── Rich Text ─────────────────────────────────────────────────────────────────

@Composable
private fun RichText(
    text: String,
    highlight: Color,
    baseColor: Color,
    modifier: Modifier = Modifier
) {
    val parts = text.split("**")
    val annotated = buildAnnotatedString {
        parts.forEachIndexed { i, part ->
            if (part.isEmpty()) return@forEachIndexed
            if (i % 2 == 1) {
                pushStyle(SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = highlight,
                    background = highlight.copy(alpha = 0.13f)
                ))
                append(part)
                pop()
            } else {
                pushStyle(SpanStyle(color = baseColor))
                append(part)
                pop()
            }
        }
    }
    Text(
        text = annotated,
        style = MaterialTheme.typography.bodyMedium.scaled(),
        lineHeight = 20.sp,
        modifier = modifier
    )
}

// ── Compare Section ───────────────────────────────────────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CompareSection(rule: SpellingRule) {
    Column(verticalArrangement = Arrangement.spacedBy(Dimens10)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens6),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.CompareArrows,
                contentDescription = null,
                tint = Color(0xFF455A64),
                modifier = Modifier.size(Dimens16)
            )
            Text(
                text = "COMPARE",
                style = MaterialTheme.typography.bodySmall.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF455A64),
                letterSpacing = 0.8.sp
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens8),
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            rule.comparisons.forEach { col ->
                CompareColumn(col = col, modifier = Modifier.weight(1f))
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CompareColumn(col: RuleComparison, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimens8),
        modifier = modifier
            .kidsGlassCard(cornerRadius = Dimens12, strokeColor = col.color.copy(alpha = 0.40f))
            .padding(Dimens12)
    ) {
        Box(
            modifier = Modifier
                .background(col.color, RoundedCornerShape(50))
                .padding(horizontal = Dimens10, vertical = Dimens4)
        ) {
            Text(
                text = col.label,
                style = MaterialTheme.typography.bodySmall.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Text(
            text = col.condition,
            style = MaterialTheme.typography.bodySmall.scaled(),
            color = Color.Gray,
            lineHeight = 18.sp
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(Dimens4),
            verticalArrangement = Arrangement.spacedBy(Dimens4)
        ) {
            col.words.forEach { word ->
                Box(
                    modifier = Modifier
                        .background(col.color.copy(alpha = 0.10f), RoundedCornerShape(Dimens6))
                        .padding(horizontal = Dimens8, vertical = Dimens4)
                ) {
                    Text(
                        text = word,
                        style = MaterialTheme.typography.bodySmall.scaled(),
                        fontWeight = FontWeight.Bold,
                        color = col.color
                    )
                }
            }
        }
    }
}

// ── Examples Section ──────────────────────────────────────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ExamplesSection(
    rule: SpellingRule,
    showExamples: Boolean,
    highlightedId: String?,
    onExampleTap: (RuleWordExample) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(Dimens10)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens6),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = null,
                tint = rule.color,
                modifier = Modifier.size(Dimens16)
            )
            Text(
                text = "TAP A WORD TO HEAR IT",
                style = MaterialTheme.typography.bodySmall.scaled(),
                fontWeight = FontWeight.Bold,
                color = rule.color,
                letterSpacing = 0.8.sp
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(Dimens8)) {
            rule.exampleGroups.forEach { group ->
                ExampleGroup(
                    group = group,
                    rule = rule,
                    showExamples = showExamples,
                    highlightedId = highlightedId,
                    onExampleTap = onExampleTap
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ExampleGroup(
    group: RuleWordExampleGroup,
    rule: SpellingRule,
    showExamples: Boolean,
    highlightedId: String?,
    onExampleTap: (RuleWordExample) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(Dimens8)) {
        group.label?.let { label ->
            Box(
                modifier = Modifier
                    .background(rule.color.copy(alpha = 0.10f), RoundedCornerShape(50))
                    .padding(horizontal = Dimens10, vertical = Dimens4)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = rule.color.copy(alpha = 0.75f)
                )
            }
        }

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(Dimens8),
            verticalArrangement = Arrangement.spacedBy(Dimens8)
        ) {
            group.examples.forEachIndexed { index, example ->
                ExampleTile(
                    example = example,
                    rule = rule,
                    index = index,
                    showExamples = showExamples,
                    isHighlighted = highlightedId == example.id,
                    onTap = { onExampleTap(example) }
                )
            }
        }
    }
}

@Composable
private fun ExampleTile(
    example: RuleWordExample,
    rule: SpellingRule,
    index: Int,
    showExamples: Boolean,
    isHighlighted: Boolean,
    onTap: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isHighlighted) 1.12f else 1.0f,
        animationSpec = spring(stiffness = 400f, dampingRatio = 0.65f),
        label = "scale"
    )
    val alpha by animateFloatAsState(
        targetValue = if (showExamples) 1f else 0f,
        animationSpec = spring(stiffness = 500f, dampingRatio = 0.85f),
        label = "alpha"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .graphicsLayer(scaleX = scale, scaleY = scale, alpha = alpha)
            .shadow(
                elevation = if (isHighlighted) 12.dp else 4.dp,
                shape = RoundedCornerShape(Dimens12),
                ambientColor = rule.color.copy(alpha = if (isHighlighted) 0.85f else 0.50f),
                spotColor = rule.color.copy(alpha = if (isHighlighted) 0.85f else 0.50f)
            )
            .background(
                brush = Brush.linearGradient(listOf(rule.color, rule.shadowColor)),
                shape = RoundedCornerShape(Dimens12)
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onTap() }
            .padding(horizontal = Dimens16, vertical = Dimens10)
    ) {
        Row {
            if (example.prefix.isNotEmpty()) {
                val prefixColor = when {
                    example.highlightPrefix && isHighlighted -> Color(0xFFFFF176)
                    example.highlightPrefix -> Color.White
                    else -> Color.White.copy(alpha = 0.55f)
                }
                Text(
                    text = example.prefix,
                    style = MaterialTheme.typography.titleMedium.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = prefixColor,
                    textDecoration = if (example.highlightPrefix && isHighlighted)
                        TextDecoration.Underline else TextDecoration.None
                )
            }
            if (example.suffix.isNotEmpty()) {
                val suffixColor = when {
                    !example.highlightPrefix && isHighlighted -> Color(0xFFFFF176)
                    !example.highlightPrefix -> Color.White
                    else -> Color.White.copy(alpha = 0.55f)
                }
                Text(
                    text = example.suffix,
                    style = MaterialTheme.typography.titleMedium.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = suffixColor,
                    textDecoration = if (!example.highlightPrefix && isHighlighted)
                        TextDecoration.Underline else TextDecoration.None
                )
            }
        }
    }
}
