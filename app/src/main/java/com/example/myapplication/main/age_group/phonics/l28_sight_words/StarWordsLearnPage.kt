package com.example.myapplication.main.age_group.phonics.l28_sight_words

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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.PhonicsLearnSessionEffect
import com.example.myapplication.main.age_group.phonics.listen.view_model.PhonicsListenLevelKey
import com.example.myapplication.main.age_group.phonics.l28_sight_words.view_model.SWLearnViewModel
import com.example.myapplication.main.age_group.phonics.l28_sight_words.view_model.SWSet
import com.example.myapplication.main.age_group.phonics.l28_sight_words.view_model.SWWord
import com.example.myapplication.main.age_group.phonics.l28_sight_words.view_model.swSets
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
import com.example.myapplication.utils.extensions.scaled
import com.example.myapplication.main.common.PhonicsWrongReadingCard
import com.example.myapplication.main.common.WrongReadingExample

private val swAccent = Color(0xFFD81B60)

@Composable
fun StarWordsLearnPage(
    navController: NavController,
    viewModel: SWLearnViewModel = hiltViewModel()
) {
    // Times this visit for the parent report (Phonics tab)
    PhonicsLearnSessionEffect(PhonicsListenLevelKey.sightWords)

    val uiState     = viewModel.uiState
    val selectedSet = viewModel.selectedSet

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.pinkPeach, shape = KidsFloatingShape.stars)

        Row(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            // ── LEFT 30% ──────────────────────────────────────────────────────
            Column(modifier = Modifier.fillMaxWidth(0.30f).fillMaxHeight()) {
                BackButtonWithText(title = "Sight Words", onBackClick = { navController.popBackStack() })
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = Dimens10)
                        .padding(top = Dimens8, bottom = Dimens8),
                    verticalArrangement = Arrangement.spacedBy(Dimens8)
                ) {
                    Text(
                        text       = "Tap a star to light it up!",
                        style      = MaterialTheme.typography.labelSmall.scaled(),
                        color      = Color(0xFF78909C),
                        modifier   = Modifier
                            .padding(bottom = Dimens4)
                            .padding(horizontal = Dimens4)
                    )
                    swSets.forEachIndexed { index, set ->
                        SWSetButton(
                            set        = set,
                            isSelected = uiState.selectedSetIndex == index,
                            onClick    = { viewModel.onSetTap(index) }
                        )
                    }
                    Row(
                        verticalAlignment     = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(Dimens6),
                        modifier              = Modifier
                            .padding(top = Dimens6)
                            .padding(horizontal = Dimens4)
                    ) {
                        Text(text = "💡", style = MaterialTheme.typography.labelSmall.scaled())
                        Text(
                            text  = "You can't sound these out — know them by heart!",
                            style = MaterialTheme.typography.labelSmall.scaled(),
                            color = Color(0xFF90A4AE)
                        )
                    }
                }
            }

            // ── RIGHT 70% ─────────────────────────────────────────────────────
            AnimatedContent(
                targetState    = selectedSet,
                transitionSpec = { fadeIn(spring()) togetherWith fadeOut(spring()) },
                modifier       = Modifier.weight(1f).fillMaxHeight(),
                label          = "swLearnContent"
            ) { set ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens14),
                    verticalArrangement = Arrangement.spacedBy(Dimens10)
                ) {
                    SWSetHeader(set = set)
                    LazyVerticalGrid(
                        columns               = GridCells.Fixed(3),
                        contentPadding        = PaddingValues(vertical = Dimens8),
                        horizontalArrangement = Arrangement.spacedBy(Dimens10),
                        verticalArrangement   = Arrangement.spacedBy(Dimens10)
                    ) {
                        items(set.words) { word ->
                            SWStarCard(
                                word     = word,
                                accent   = set.accentColor,
                                shadow   = set.shadowColor,
                                isActive = uiState.activeWord == word.word,
                                onClick  = { viewModel.onWordTap(word) }
                            )
                        }
                    }

                    PhonicsWrongReadingCard(accentColor = Color(0xFFD81B60), examples = listOf(
                        WrongReadingExample("sa·id (sounding it out)", "/sed/ — sight words: know them by heart!", "said"),
                        WrongReadingExample("w·a·s", "/woz/ — a rule-breaker, just remember it!", "was"),
                    ))
                }
            }
        }
    }
}

@Composable
private fun SWSetButton(set: SWSet, isSelected: Boolean, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val bg = if (isSelected)
        Modifier.background(Brush.linearGradient(listOf(set.accentColor, set.shadowColor)), RoundedCornerShape(12.dp))
    else
        Modifier.background(Color.White.copy(alpha = 0.75f), RoundedCornerShape(12.dp))
    val borderColor = if (isSelected) Color.White.copy(alpha = 0.30f) else set.accentColor.copy(alpha = 0.25f)

    Row(
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens8),
        modifier = Modifier
            .fillMaxWidth()
            .then(bg)
            .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .padding(vertical = Dimens8, horizontal = Dimens10)
    ) {
        Text(text = set.emoji, style = MaterialTheme.typography.bodyMedium.scaled())
        Column(verticalArrangement = Arrangement.spacedBy(Dimens2), modifier = Modifier.weight(1f)) {
            Text(
                text       = set.name,
                style      = MaterialTheme.typography.titleSmall.scaled(),
                fontWeight = FontWeight.ExtraBold,
                color      = if (isSelected) Color.White else set.accentColor
            )
            Text(
                text  = "${set.words.size} star words",
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = if (isSelected) Color.White.copy(alpha = 0.80f) else Color(0xFF78909C)
            )
        }
        if (isSelected) {
            Text(
                text  = "›",
                style = MaterialTheme.typography.titleMedium.scaled(),
                color = Color.White.copy(alpha = 0.80f),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SWSetHeader(set: SWSet) {
    Row(
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens12),
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = 12.dp, strokeColor = set.accentColor)
            .padding(Dimens14)
    ) {
        Text(text = set.emoji, style = MaterialTheme.typography.headlineMedium.scaled())
        Column(verticalArrangement = Arrangement.spacedBy(Dimens2)) {
            Text(
                text       = set.name,
                style      = MaterialTheme.typography.titleLarge.scaled(),
                fontWeight = FontWeight.ExtraBold,
                color      = set.accentColor
            )
            Text(
                text  = "${set.words.size} star words",
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = Color(0xFF78909C)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text  = "Tap a star! ⭐",
            style = MaterialTheme.typography.labelSmall.scaled(),
            color = Color(0xFF90A4AE)
        )
    }
}

@Composable
private fun SWStarCard(
    word:     SWWord,
    accent:   Color,
    shadow:   Color,
    isActive: Boolean,
    onClick:  () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val scale by animateFloatAsState(
        targetValue   = if (isActive) 1.06f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f),
        label         = "swStarScale"
    )
    val rotation by animateFloatAsState(
        targetValue   = if (isActive) 2f else 0f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f),
        label         = "swStarRotation"
    )

    val bg = if (isActive)
        Modifier.background(Brush.linearGradient(listOf(accent, shadow)), RoundedCornerShape(12.dp))
    else
        Modifier.background(Color.White.copy(alpha = 0.75f), RoundedCornerShape(12.dp))
    val borderColor = if (isActive) Color.White.copy(alpha = 0.30f) else accent.copy(alpha = 0.25f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens6),
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .graphicsLayer { rotationZ = rotation }
            .then(bg)
            .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .padding(vertical = Dimens14)
    ) {
        Text(
            text     = if (isActive) "⭐" else "☆",
            style    = MaterialTheme.typography.bodyMedium.scaled(),
            modifier = Modifier.alpha(if (isActive) 1f else 0.4f)
        )
        Text(
            text       = word.word,
            style      = MaterialTheme.typography.titleSmall.scaled(),
            fontWeight = FontWeight.ExtraBold,
            color      = if (isActive) Color.White else accent,
            maxLines   = 1
        )
    }
}
