package com.example.myapplication.main.age_group.phonics.l20_silent_letters

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.draw.drawWithContent
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
import com.example.myapplication.main.age_group.phonics.l20_silent_letters.view_model.SilentLettersGroup
import com.example.myapplication.main.age_group.phonics.l20_silent_letters.view_model.SilentLettersLearnViewModel
import com.example.myapplication.main.age_group.phonics.l20_silent_letters.view_model.SilentLettersWord
import com.example.myapplication.main.age_group.phonics.l20_silent_letters.view_model.silentLettersGroups
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

private val silentLetterMap = mapOf("kn" to "k", "wr" to "w", "mb" to "b", "gn" to "g", "bt" to "b")
private val soundedLetterMap = mapOf("kn" to "n", "wr" to "r", "mb" to "m", "gn" to "n", "bt" to "t")

@Composable
fun SilentLettersLearnPage(
    navController: NavController,
    viewModel: SilentLettersLearnViewModel = hiltViewModel()
) {
    // Times this visit for the parent report (Phonics tab)
    PhonicsLearnSessionEffect(PhonicsListenLevelKey.silentLetters)

    val uiState = viewModel.uiState
    val selectedGroup = viewModel.selectedGroup

    LaunchedEffect(Unit) { viewModel.onScreenAppear() }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.grayBlue, shape = KidsFloatingShape.stars)

        Row(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            // ── LEFT (ghost detective cards, 25%) ───────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.25f)
                    .fillMaxHeight()
            ) {
                BackButtonWithText(title = "Silent Letters", onBackClick = { navController.popBackStack() })

                // 7 detective groups don't fit on phones — the list must scroll.
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = Dimens10, vertical = Dimens8),
                    verticalArrangement = Arrangement.spacedBy(Dimens8)
                ) {
                    silentLettersGroups.forEachIndexed { index, group ->
                        GhostDetectiveCard(
                            group      = group,
                            isSelected = uiState.selectedGroupIndex == index,
                            onClick    = { viewModel.onGroupTap(index) }
                        )
                    }
                }
            }

            // ── RIGHT (case file + word grid) ───────────────────────────────
            AnimatedContent(
                targetState = selectedGroup,
                transitionSpec = { fadeIn(spring()) togetherWith fadeOut(spring()) },
                modifier = Modifier.weight(1f).fillMaxHeight(),
                label = "silentLearnContent"
            ) { group ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(Dimens14),
                    verticalArrangement = Arrangement.spacedBy(Dimens12)
                ) {
                    CaseFileCard(group = group)

                    if (uiState.showWords) {
                        SilentWordGrid(
                            group           = group,
                            highlightedWord = uiState.highlightedWord,
                            onWordTap       = { viewModel.onWordTap(it) }
                        )

                        PhonicsWrongReadingCard(accentColor = Color(0xFF455A64), examples = slWrongReading(group))
                    }
                }
            }
        }
    }
}

@Composable
private fun GhostDetectiveCard(group: SilentLettersGroup, isSelected: Boolean, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val ghostLetter = silentLetterMap[group.pattern] ?: group.pattern.first().toString()
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
            .padding(vertical = Dimens8, horizontal = Dimens8)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens4),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "👻", style = MaterialTheme.typography.bodyMedium.scaled())
            Text(
                text  = group.pattern,
                style = MaterialTheme.typography.titleSmall.scaled(),
                fontWeight = FontWeight.ExtraBold,
                color = if (isSelected) Color.White else group.accentColor
            )
        }
        Text(
            text  = "silent $ghostLetter",
            style = MaterialTheme.typography.labelSmall.scaled(),
            color = if (isSelected) Color.White.copy(alpha = 0.80f) else Color(0xFF78909C)
        )
    }
}

@Composable
private fun CaseFileCard(group: SilentLettersGroup) {
    val ghostLetter  = silentLetterMap[group.pattern] ?: group.pattern.first().toString()
    val soundedLetter = soundedLetterMap[group.pattern] ?: group.pattern.last().toString()

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
                text  = group.pattern,
                style = MaterialTheme.typography.headlineLarge.scaled(),
                fontWeight = FontWeight.ExtraBold,
                color = group.accentColor.copy(alpha = 0.25f)
            )
            Text(
                text  = group.pattern,
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

        Column(verticalArrangement = Arrangement.spacedBy(Dimens4)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens4),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "👻", style = MaterialTheme.typography.labelSmall.scaled())
                Text(
                    text  = "Ghost Letter: ${ghostLetter.uppercase()}",
                    style = MaterialTheme.typography.labelSmall.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD0D0D0)
                )
            }
            Text(
                text  = "Sounds: ${soundedLetter.uppercase()}",
                style = MaterialTheme.typography.labelSmall.scaled(),
                fontWeight = FontWeight.Bold,
                color = group.accentColor
            )
        }

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
                Icon(Icons.Default.Search, contentDescription = null, tint = group.accentColor,
                    modifier = Modifier.size(16.dp))
                Text(
                    text  = "Case File",
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
private fun SilentWordGrid(
    group: SilentLettersGroup,
    highlightedWord: String?,
    onWordTap: (SilentLettersWord) -> Unit
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
                text  = "Tap to hear — ghost letter glows gray 👻",
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
                    SilentWordCard(
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
private fun SilentWordCard(
    word:     SilentLettersWord,
    group:    SilentLettersGroup,
    isActive: Boolean,
    onClick:  () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val scale by animateFloatAsState(
        targetValue   = if (isActive) 1.06f else 1.0f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 500f),
        label         = "silentWordScale"
    )
    val bg = if (isActive)
        Brush.linearGradient(listOf(group.accentColor, group.shadowColor))
    else
        Brush.linearGradient(listOf(Color.White, Color.White))
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
                word.word.forEachIndexed { idx, char ->
                    val isSilent = idx == word.silentIndex
                    val charColor = when {
                        isSilent && isActive  -> Color(0xFFBDBDBD)
                        isSilent && !isActive -> Color(0xFFD0D0D0)
                        isActive              -> Color.White.copy(alpha = 0.88f)
                        else                  -> Color(0xFF263238)
                    }
                    Text(
                        text  = char.toString(),
                        style = if (isSilent)
                            MaterialTheme.typography.titleSmall.scaled()
                        else
                            MaterialTheme.typography.bodyLarge.scaled(),
                        fontWeight = FontWeight.Bold,
                        color = charColor,
                        modifier = Modifier
                            .drawWithContent {
                                drawContent()
                                if (isSilent && isActive) {
                                    drawLine(
                                        color = Color(0xFFFF5252).copy(alpha = 0.70f),
                                        start = androidx.compose.ui.geometry.Offset(0f, size.height / 2f),
                                        end   = androidx.compose.ui.geometry.Offset(size.width, size.height / 2f),
                                        strokeWidth = 4f
                                    )
                                }
                            }
                    )
                }
            }
            AnimatedVisibility(
                visible = isActive,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                Text(
                    text  = "👻 silent!",
                    style = MaterialTheme.typography.labelSmall.scaled(),
                    color = Color(0xFF9E9E9E)
                )
            }
        }
    }
}

// Per-pattern wrong-reading — follows the left-panel selection.
private fun slWrongReading(group: SilentLettersGroup): List<WrongReadingExample> = when (group.pattern) {
    "kn" -> listOf(WrongReadingExample("k·nife (saying the k)", "/nīf/ — the K ninja makes no sound!", "knife"))
    "wr" -> listOf(WrongReadingExample("w·rite (saying the w)", "/rīt/ — the W ninja hides!", "write"))
    "mb" -> listOf(WrongReadingExample("lam·b (saying the b)", "/lam/ — the B hides after M!", "lamb"))
    "gn" -> listOf(WrongReadingExample("sig·n (saying the g)", "/sīn/ — the G slips away!", "sign"))
    "bt" -> listOf(WrongReadingExample("deb·t (saying the b)", "/det/ — the B hides before T!", "debt"))
    "h"  -> listOf(WrongReadingExample("h·our (saying the h)", "/our/ — the H hides completely!", "hour"))
    "l"  -> listOf(WrongReadingExample("wa·l·k (saying the l)", "/wok/ — the L turns ninja after A!", "walk"))
    else -> listOf(WrongReadingExample("lis·t·en (saying the t)", "/lissen/ — the T sneaks away!", "listen"))
}
