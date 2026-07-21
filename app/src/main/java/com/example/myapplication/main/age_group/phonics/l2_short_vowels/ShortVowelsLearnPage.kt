package com.example.myapplication.main.age_group.phonics.l2_short_vowels

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.PhonicsLearnSessionEffect
import com.example.myapplication.main.age_group.phonics.listen.view_model.PhonicsListenLevelKey
import com.example.myapplication.main.age_group.phonics.l2_short_vowels.view_model.ShortVowelModel
import com.example.myapplication.main.age_group.phonics.l2_short_vowels.view_model.ShortVowelPhase
import com.example.myapplication.main.age_group.phonics.l2_short_vowels.view_model.ShortVowelsUiState
import com.example.myapplication.main.age_group.phonics.l2_short_vowels.view_model.ShortVowelsViewModel
import com.example.myapplication.main.age_group.phonics.l2_short_vowels.view_model.shortVowelData
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens40
import com.example.myapplication.utils.extensions.scaled
import kotlinx.coroutines.delay
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.draw.clip
import com.example.myapplication.main.common.PhonicsIntroAudioViewModel

@Composable
fun ShortVowelsLearnPage(
    navController: NavController,
    viewModel: ShortVowelsViewModel = hiltViewModel()
) {
    // Times this visit for the parent report (Phonics tab)
    PhonicsLearnSessionEffect(PhonicsListenLevelKey.shortVowels)

    val uiState = viewModel.uiState

    DisposableEffect(Unit) {
        onDispose { viewModel.stop() }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.lilacPink, shape = KidsFloatingShape.sparkles)

        Row(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            // ── LEFT: vowel list ─────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .weight(0.32f)
                    .fillMaxHeight()
            ) {
                BackButtonWithText(
                    title = "Short Vowels",
                    onBackClick = { navController.popBackStack() }
                )

                Spacer(modifier = Modifier.weight(1f))

                Column(
                    modifier = Modifier.padding(horizontal = Dimens16),
                    verticalArrangement = Arrangement.spacedBy(Dimens12)
                ) {
                    shortVowelData.forEach { vowel ->
                        VowelTile(
                            vowel = vowel,
                            isSelected = uiState.selectedVowel?.vowel == vowel.vowel,
                            onClick = { viewModel.onVowelTap(vowel) }
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
            }

            // ── RIGHT: detail panel ──────────────────────────────────────────
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(0.68f)
                    .fillMaxHeight()
            ) {
                val selectedVowel = uiState.selectedVowel
                if (selectedVowel != null) {
                    key(selectedVowel.vowel) {
                        VowelDetailPanel(
                            vowel = selectedVowel,
                            uiState = uiState,
                            onAnchorTap = { viewModel.playAnchorWord(selectedVowel) },
                            onExampleTap = { viewModel.playExampleWord(it) }
                        )
                    }
                } else {
                    Text(
                        text = "👈 Tap a vowel to\nhear its sound!",
                        style = MaterialTheme.typography.titleLarge.scaled(),
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF3949AB).copy(alpha = 0.75f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// ── Vowel tile ────────────────────────────────────────────────────────────────

@Composable
private fun VowelTile(vowel: ShortVowelModel, isSelected: Boolean, onClick: () -> Unit) {
    val color = Color(vowel.colorHex)

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.08f else 1.0f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMedium),
        label = "tileScale"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens12),
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .background(
                if (isSelected) color.copy(alpha = 0.10f) else Color.White.copy(alpha = 0.7f),
                RoundedCornerShape(Dimens12)
            )
            .border(Dimens2, if (isSelected) color.copy(alpha = 0.5f) else Color.Transparent, RoundedCornerShape(Dimens12))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
            .padding(horizontal = Dimens12, vertical = Dimens8)
    ) {
        // Letter box
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(Dimens40)
                .background(
                    if (isSelected) color else color.copy(alpha = 0.15f),
                    RoundedCornerShape(Dimens10)
                )
        ) {
            Text(
                text = vowel.vowel,
                style = MaterialTheme.typography.titleLarge.scaled(),
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else color
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(Dimens2)) {
            Text(
                text = "Short ${vowel.vowel}",
                style = MaterialTheme.typography.labelLarge.scaled(),
                fontWeight = FontWeight.Bold,
                color = if (isSelected) color else Color.Black.copy(alpha = 0.7f)
            )
            Text(
                text = vowel.anchorWord,
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        if (isSelected) {
            Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(Dimens16)
            )
        }
    }
}

// ── Vowel detail panel ────────────────────────────────────────────────────────

@Composable
private fun VowelDetailPanel(
    vowel: ShortVowelModel,
    uiState: ShortVowelsUiState,
    onAnchorTap: () -> Unit,
    onExampleTap: (String) -> Unit
) {
    val color = Color(vowel.colorHex)
    val phase = uiState.phase

    // Big letter animation
    val letterScale by animateFloatAsState(
        targetValue = if (phase != ShortVowelPhase.IDLE) 1.0f else 0.3f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMediumLow),
        label = "letterScale"
    )
    val letterAlpha by animateFloatAsState(
        targetValue = if (phase != ShortVowelPhase.IDLE) 1.0f else 0.0f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMediumLow),
        label = "letterAlpha"
    )

    // Anchor tap bounce
    var anchorBounceTrigger by remember { mutableIntStateOf(0) }
    var anchorBounceTarget by remember { mutableFloatStateOf(1.0f) }
    val anchorBounceScale by animateFloatAsState(
        targetValue = anchorBounceTarget,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium),
        label = "anchorBounce"
    )
    LaunchedEffect(anchorBounceTrigger) {
        if (anchorBounceTrigger > 0) {
            anchorBounceTarget = 1.1f
            delay(150)
            anchorBounceTarget = 1.0f
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val bigLetterSizeDp = minOf(maxHeight * 0.28f, maxWidth * 0.20f)
        val imgSizeDp = minOf(maxHeight * 0.22f, maxWidth * 0.14f)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Dimens24)
        ) {
        // Tight group: big letter + sound badge
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens4)
        ) {
            // Big vowel letter with glow
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.graphicsLayer {
                    scaleX = letterScale
                    scaleY = letterScale
                    alpha = letterAlpha
                }
            ) {
                Box(
                    modifier = Modifier
                        .size(bigLetterSizeDp * 1.25f)
                        .background(color.copy(alpha = 0.12f), CircleShape)
                        .blur(Dimens12)
                )
                Text(
                    text = vowel.vowel,
                    style = TextStyle(
                        fontSize = (bigLetterSizeDp.value * 0.85f).sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = color
                )
            }

            // Sound word + label
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens2),
                modifier = Modifier.graphicsLayer { alpha = letterAlpha }
            ) {
                val svAudioVm: PhonicsIntroAudioViewModel = hiltViewModel()
                Text(
                    text = "\"${vowel.soundWord}\"",
                    style = MaterialTheme.typography.headlineMedium.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = color,
                modifier = Modifier
                    .clip(RoundedCornerShape(Dimens8))
                    .clickable { svAudioVm.play("sound_" + vowel.vowel.lowercase()) }
            )
                Text(
                    text = vowel.label,
                    style = MaterialTheme.typography.titleMedium.scaled(),
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = Color(0xFF546E7A))) { append("⚡ Short vowels say a ") }
                        withStyle(SpanStyle(color = color, fontWeight = FontWeight.Bold)) { append("QUICK sound") }
                        withStyle(SpanStyle(color = Color(0xFF546E7A))) { append(" — not the letter's ") }
                        withStyle(SpanStyle(color = Color(0xFFC62828), fontWeight = FontWeight.Bold)) { append("name") }
                        withStyle(SpanStyle(color = Color(0xFF546E7A))) { append("!") }
                    },
                    style = MaterialTheme.typography.labelMedium.scaled(),
                    modifier = Modifier
                        .background(color.copy(alpha = 0.10f), RoundedCornerShape(Dimens12))
                        .padding(horizontal = Dimens10, vertical = Dimens4)
                )
            }
        }

        Spacer(modifier = Modifier.height(Dimens16))

        // Anchor word + emoji (tappable, springs in when anchor word plays)
        AnimatedVisibility(
            visible = phase == ShortVowelPhase.ANCHOR_IN || phase == ShortVowelPhase.EXAMPLES_IN,
            enter = fadeIn() + scaleIn(
                initialScale = 0.5f,
                animationSpec = spring(dampingRatio = 0.70f, stiffness = Spring.StiffnessMediumLow)
            )
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens16),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .graphicsLayer { scaleX = anchorBounceScale; scaleY = anchorBounceScale }
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        anchorBounceTrigger++
                        anchorBounceTarget = 1.1f
                        onAnchorTap()
                    }
            ) {
                Text(
                    text = vowel.anchorEmoji,
                    style = TextStyle(fontSize = (imgSizeDp.value * 0.75f).sp)
                )
                WordWithVowelHighlighted(
                    word = vowel.anchorWord,
                    vowelChar = vowel.vowel[0].lowercaseChar(),
                    color = color,
                    style = MaterialTheme.typography.displaySmall.scaled()
                )
            }
        }

        Spacer(modifier = Modifier.height(Dimens16))

        // Example chips (appear one by one)
        AnimatedVisibility(
            visible = phase == ShortVowelPhase.ANCHOR_IN || phase == ShortVowelPhase.EXAMPLES_IN,
            enter = fadeIn()
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens10)) {
                vowel.examples.forEachIndexed { index, example ->
                    AnimatedVisibility(
                        visible = index < uiState.visibleExamples,
                        enter = fadeIn() + scaleIn(
                            initialScale = 0.5f,
                            animationSpec = spring(dampingRatio = 0.70f, stiffness = Spring.StiffnessMedium)
                        )
                    ) {
                        ExampleChip(
                            word = example.word,
                            vowelChar = vowel.vowel[0].lowercaseChar(),
                            color = color,
                            onClick = { onExampleTap(example.word) }
                        )
                    }
                }
            }
        }
    } // end Column
    } // end BoxWithConstraints
}

// ── Example chip ──────────────────────────────────────────────────────────────

@Composable
private fun ExampleChip(word: String, vowelChar: Char, color: Color, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.1f else 1.0f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium),
        label = "chipScale"
    )

    Box(
        modifier = Modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .background(color.copy(alpha = 0.10f), RoundedCornerShape(Dimens12))
            .border(Dimens2, color.copy(alpha = 0.35f), RoundedCornerShape(Dimens12))
            .clip(RoundedCornerShape(Dimens12))
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .padding(horizontal = Dimens16, vertical = Dimens8)
    ) {
        WordWithVowelHighlighted(
            word = word,
            vowelChar = vowelChar,
            color = color,
            style = MaterialTheme.typography.titleMedium.scaled()
        )
    }
}

// ── Word with vowel highlighted ───────────────────────────────────────────────

@Composable
private fun WordWithVowelHighlighted(word: String, vowelChar: Char, color: Color, style: TextStyle) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        word.forEach { char ->
            val isVowel = char.lowercaseChar() == vowelChar
            Text(
                text = char.toString(),
                style = style.copy(
                    fontSize = if (isVowel) (style.fontSize.value * 1.1f).sp else style.fontSize
                ),
                fontWeight = FontWeight.Bold,
                color = if (isVowel) color else Color(0xFF424242)
            )
        }
    }
}
