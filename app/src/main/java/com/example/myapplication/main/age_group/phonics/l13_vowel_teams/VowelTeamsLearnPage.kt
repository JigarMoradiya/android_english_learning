package com.example.myapplication.main.age_group.phonics.l13_vowel_teams

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.myapplication.main.age_group.phonics.l13_vowel_teams.view_model.VowelTeamGroup
import com.example.myapplication.main.age_group.phonics.l13_vowel_teams.view_model.VowelTeamWord
import com.example.myapplication.main.age_group.phonics.l13_vowel_teams.view_model.VowelTeamsLearnViewModel
import com.example.myapplication.main.age_group.phonics.l13_vowel_teams.view_model.vowelTeamWordData
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens14
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.utils.extensions.scaled

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VowelTeamsLearnPage(
    navController: NavController,
    viewModel: VowelTeamsLearnViewModel = hiltViewModel()
) {
    // Times this visit for the parent report (Phonics tab)
    PhonicsLearnSessionEffect(PhonicsListenLevelKey.vowelTeams)

    DisposableEffect(Unit) { onDispose { viewModel.stop() } }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.peachCoral, shape = KidsFloatingShape.bubbles)

        Box(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            Row(modifier = Modifier.fillMaxSize()) {

                // ── LEFT: group list ─────────────────────────────────────────
                Column(
                    modifier = Modifier
                        .weight(0.25f)
                        .fillMaxHeight()
                ) {
                    BackButtonWithText(title = "Vowel Teams", onBackClick = { navController.popBackStack() })

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = Dimens10, vertical = Dimens8),
                        verticalArrangement = Arrangement.spacedBy(Dimens6)
                    ) {
                        VowelTeamGroup.entries.forEach { group ->
                            VowelTeamGroupCard(
                                group = group,
                                isSelected = viewModel.uiState.selectedGroup == group,
                                onClick = { viewModel.onGroupTap(group) }
                            )
                        }
                    }
                }

                // ── RIGHT: content ───────────────────────────────────────────
                AnimatedContent(
                    targetState = viewModel.uiState.selectedGroup,
                    transitionSpec = { fadeIn(spring()) togetherWith fadeOut(spring()) },
                    modifier = Modifier
                        .weight(0.75f)
                        .fillMaxHeight(),
                    label = "vowelTeamGroupContent"
                ) { g ->
                    val groupWords = vowelTeamWordData[g] ?: emptyList()
                    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .heightIn(min = this@BoxWithConstraints.maxHeight)
                            .padding(horizontal = Dimens14, vertical = Dimens10),
                        verticalArrangement = Arrangement.spacedBy(Dimens14, Alignment.CenterVertically)
                    ) {
                        // Rule card
                        ruleCard(group = g)

                        // Words section
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(4.dp, RoundedCornerShape(Dimens12))
                                .background(Color.White, RoundedCornerShape(Dimens12))
                                .padding(Dimens14),
                            verticalArrangement = Arrangement.spacedBy(Dimens10)
                        ) {
                            Text(
                                text = "👆 Tap a word to hear it",
                                style = MaterialTheme.typography.titleSmall.scaled(),
                                fontWeight = FontWeight.Bold,
                                color = g.color
                            )
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(Dimens8),
                                verticalArrangement = Arrangement.spacedBy(Dimens8)
                            ) {
                                groupWords.forEach { word ->
                                    wordDetailTile(word = word, group = g, viewModel = viewModel)
                                }
                            }
                        }
                    }
                    }
                }
            }
        }
    }
}

// ── Group Card ────────────────────────────────────────────────────────────────

@Composable
private fun VowelTeamGroupCard(
    group: VowelTeamGroup,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bg = if (isSelected)
        Brush.linearGradient(listOf(group.color, group.shadowColor))
    else
        Brush.linearGradient(listOf(Color.White.copy(0.75f), Color.White.copy(0.75f)))

    val shape = RoundedCornerShape(Dimens12)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(bg, shape)
            .clickable { onClick() }
            .padding(horizontal = Dimens10, vertical = Dimens8)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens2)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens6),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = group.emoji, style = MaterialTheme.typography.bodyLarge.scaled())
                Text(
                    text = group.label,
                    style = MaterialTheme.typography.titleMedium.scaled(),
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isSelected) Color.White else group.color,
                    maxLines = 1
                )
                Spacer(Modifier.weight(1f))
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = Color.White.copy(0.80f),
                        modifier = Modifier.height(Dimens12)
                    )
                }
            }
            Text(
                text = teamSound(group),
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = if (isSelected) Color.White.copy(0.80f) else Color(0xFF78909C),
                maxLines = 1
            )
        }
    }
}

// ── Rule Card ─────────────────────────────────────────────────────────────────

@Composable
private fun ruleCard(group: VowelTeamGroup) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(Dimens12))
            .background(Color.White, RoundedCornerShape(Dimens12)),
        horizontalArrangement = Arrangement.spacedBy(Dimens12)
    ) {
        // Vowel team big display
        Column(
            modifier = Modifier
                .weight(1f)
                .background(group.color.copy(alpha = 0.08f), RoundedCornerShape(Dimens12))
                .padding(vertical = Dimens20),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens6)
        ) {
            Text(text = group.emoji, style = MaterialTheme.typography.displaySmall.scaled())
            Text(
                text = group.teams,
                style = MaterialTheme.typography.headlineMedium.scaled(),
                fontWeight = FontWeight.ExtraBold,
                color = group.color
            )
            Box(
                modifier = Modifier
                    .background(Color(0xFFECEFF1), RoundedCornerShape(Dimens8))
                    .padding(horizontal = Dimens8, vertical = Dimens2)
            ) {
                Text(
                    text = teamSound(group),
                    style = MaterialTheme.typography.labelLarge.scaled(),
                    color = Color(0xFF546E7A)
                )
            }
        }

        // Rule text
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = Dimens20, horizontal = Dimens14),
            verticalArrangement = Arrangement.spacedBy(Dimens8)
        ) {
            Text(
                text = "💡 The Rule",
                style = MaterialTheme.typography.titleSmall.scaled(),
                fontWeight = FontWeight.Bold,
                color = group.color
            )
            Text(
                text = group.rule,
                style = MaterialTheme.typography.bodyMedium.scaled(),
                color = Color(0xFF37474F)
            )
            Text(
                text = "👆 Tap a word to hear it",
                style = MaterialTheme.typography.labelMedium.scaled(),
                color = Color(0xFF90A4AE)
            )
        }
    }
}

private fun teamSound(group: VowelTeamGroup): String = when (group) {
    VowelTeamGroup.AI_AY    -> "/ā/ long A"
    VowelTeamGroup.EE_EA    -> "/ē/ long E"
    VowelTeamGroup.OA_OW    -> "/ō/ long O"
    VowelTeamGroup.OO_LONG  -> "/oo/ as in moon"
    VowelTeamGroup.OO_SHORT -> "/u/ as in book"
    VowelTeamGroup.EW_UE_UI -> "/oo/ as in blue"
    VowelTeamGroup.EA_SHORT -> "/e/ as in bread"
    VowelTeamGroup.EY_END   -> "/ē/ as in monkey"
    VowelTeamGroup.IE_LONG  -> "/ī/ as in pie"
    VowelTeamGroup.IE_LONG2 -> "/ē/ as in chief"
}

// ── Word Tile ─────────────────────────────────────────────────────────────────

@Composable
private fun wordDetailTile(word: VowelTeamWord, group: VowelTeamGroup, viewModel: VowelTeamsLearnViewModel) {
    val isHighlighted = viewModel.uiState.highlightedWordId == word.id

    Box(
        modifier = Modifier
            .shadow(if (isHighlighted) 6.dp else 2.dp, RoundedCornerShape(Dimens8))
            .background(
                if (isHighlighted) group.color else Color.White,
                RoundedCornerShape(Dimens8)
            )
            .clickable { viewModel.onWordTap(word) }
            .padding(horizontal = Dimens10, vertical = Dimens6)
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(
                    color = if (isHighlighted) Color.White.copy(alpha = 0.7f) else Color(0xFF263238).copy(alpha = 0.5f),
                    fontWeight = FontWeight.Bold
                )) { append(word.prefix) }
                withStyle(SpanStyle(
                    color = if (isHighlighted) Color.White else group.color,
                    fontWeight = FontWeight.Bold
                )) { append(word.teamPart) }
                withStyle(SpanStyle(
                    color = if (isHighlighted) Color.White.copy(alpha = 0.7f) else Color(0xFF263238).copy(alpha = 0.5f),
                    fontWeight = FontWeight.Bold
                )) { append(word.suffix) }
            },
            style = MaterialTheme.typography.bodyLarge.scaled()
        )
    }
}
