package com.example.myapplication.main.age_group.phonics.l4_cvc_words

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.Image
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.PhonicsLearnSessionEffect
import com.example.myapplication.main.age_group.phonics.listen.view_model.PhonicsListenLevelKey
import com.example.myapplication.main.age_group.phonics.l4_cvc_words.view_model.CvcBoxPhase
import com.example.myapplication.main.age_group.phonics.l4_cvc_words.view_model.CvcPhonicsPhase
import com.example.myapplication.main.age_group.phonics.l4_cvc_words.view_model.CvcUiState
import com.example.myapplication.main.age_group.phonics.l4_cvc_words.view_model.CvcWordModel
import com.example.myapplication.main.age_group.phonics.l4_cvc_words.view_model.CvcWordsViewModel
import com.example.myapplication.main.age_group.phonics.l4_cvc_words.view_model.cvcGroups
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.getImageResFromWord
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.utils.extensions.scaled
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.SpanStyle

@Composable
fun CvcWordsLearnPage(
    navController: NavController,
    viewModel: CvcWordsViewModel = hiltViewModel()
) {
    // Times this visit for the parent report (Phonics tab)
    PhonicsLearnSessionEffect(PhonicsListenLevelKey.cvcWords)

    val uiState = viewModel.uiState

    DisposableEffect(Unit) {
        onDispose { viewModel.stop() }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.skyLavender, shape = KidsFloatingShape.sparkles)

        Row(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {

            // ── LEFT: group sections + word list (iOS style scroll) ───────────
            Column(
                modifier = Modifier
                    .weight(0.38f)
                    .fillMaxHeight()
            ) {
                BackButtonWithText(
                    title = "CVC Words",
                    onBackClick = { navController.popBackStack() }
                )

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = Dimens16),
                    verticalArrangement = Arrangement.spacedBy(Dimens6)
                ) {
                    cvcGroups.forEach { group ->
                        item {
                            // Section header: emoji + label
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(Dimens6),
                                modifier = Modifier.padding(top = Dimens12, bottom = Dimens4)
                            ) {
                                Text(
                                    text = group.emoji,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = group.label,
                                    style = MaterialTheme.typography.titleSmall.scaled(),
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1A237E)
                                )
                            }
                        }

                        group.words.forEach { word ->
                            item {
                                WordRow(
                                    word = word,
                                    isSelected = uiState.selectedWord?.word == word.word,
                                    onClick = { viewModel.onWordTap(word) }
                                )
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(Dimens16)) }
                }
            }

            // ── RIGHT: animation panel ────────────────────────────────────────
            Column(
                modifier = Modifier
                    .weight(0.62f)
                    .fillMaxHeight()
            ) {
                // Replay button row (top right, visible only when word selected)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = Dimens16, top = Dimens8),
                    horizontalArrangement = Arrangement.End
                ) {
                    AnimatedVisibility(
                        visible = uiState.selectedWord != null,
                        enter = scaleIn(initialScale = 0.85f, animationSpec = tween(200)),
                        exit = scaleOut(targetScale = 0.85f, animationSpec = tween(200))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Dimens4),
                            modifier = Modifier
                                .background(Color(0xD9FFFFFF), RoundedCornerShape(50))
                                .border(Dimens2, Color(0x4D3949AB), RoundedCornerShape(50))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { viewModel.replayAnimation() }
                                .padding(horizontal = Dimens12, vertical = Dimens6)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Replay,
                                contentDescription = null,
                                tint = Color(0xFF3949AB),
                                modifier = Modifier.size(Dimens16)
                            )
                            Text(
                                text = "Replay",
                                style = MaterialTheme.typography.labelLarge.scaled(),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF3949AB)
                            )
                        }
                    }
                }

                // Content: empty state or animation panel
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.weight(1f).fillMaxWidth()
                ) {
                    val word = uiState.selectedWord
                    if (word != null) {
                        CvcAnimationPanel(word = word, uiState = uiState, viewModel = viewModel)
                    } else {
                        Text(
                            text = "👈 Tap a word to\nsee it come alive!",
                            style = MaterialTheme.typography.titleLarge.scaled(),
                            fontWeight = FontWeight.Medium,
                            color = Color(0xBF4527A0),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

// ── Word row (iOS-matching style) ─────────────────────────────────────────────

@Composable
private fun WordRow(word: CvcWordModel, isSelected: Boolean, onClick: () -> Unit) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1.0f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMedium),
        label = "wordRowScale"
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens6),
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .background(
                if (isSelected) Color(0xFFE8EAF6) else Color(0xB3FFFFFF),
                RoundedCornerShape(Dimens12)
            )
            .border(
                Dimens2,
                if (isSelected) Color(0x803949AB) else Color.Transparent,
                RoundedCornerShape(Dimens12)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = Dimens12, vertical = Dimens8)
    ) {
        // Mini C/V/C tiles
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens2)) {
            word.segments.forEach { seg ->
                val bgColor = if (seg.isVowel) {
                    if (isSelected) Color(0xFFFF5252) else Color(0xFFFFCDD2)
                } else {
                    if (isSelected) Color(0xFF2979FF) else Color(0xFFBBDEFB)
                }
                val fgColor = if (isSelected) Color.White else {
                    if (seg.isVowel) Color(0xFFC62828) else Color(0xFF1565C0)
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(22.dp)
                        .background(bgColor, RoundedCornerShape(5.dp))
                ) {
                    Text(
                        text = seg.letter,
                        style = MaterialTheme.typography.labelSmall.scaled(),
                        fontWeight = FontWeight.Bold,
                        color = fgColor
                    )
                }
            }
        }

        Text(
            text = word.word,
            style = MaterialTheme.typography.titleMedium.scaled(),
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color(0xFF1A237E) else Color(0xBF000000),
            modifier = Modifier.weight(1f)
        )

        // Word image thumbnail (iOS: shown always when hasImage)
        if (word.hasImage) {
            val resId = getImageResFromWord(word.word)
            if (resId != null) {
                Image(
                    painter = painterResource(resId),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(6.dp))
                )
            }
        }

        if (isSelected) {
            Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = null,
                tint = Color(0xFF3949AB),
                modifier = Modifier.size(Dimens16)
            )
        }
    }
}

// ── Animation panel ───────────────────────────────────────────────────────────

@Composable
private fun CvcAnimationPanel(
    word: CvcWordModel,
    uiState: CvcUiState,
    viewModel: CvcWordsViewModel
) {
    val boxPhase    = uiState.boxPhase
    val phonicsPhase = uiState.phonicsPhase
    val highlightedIndex = uiState.highlightedIndex
    val isMerging   = boxPhase == CvcBoxPhase.MERGING || boxPhase == CvcBoxPhase.MERGED
    val isMerged    = boxPhase == CvcBoxPhase.MERGED

    // Freeze displayed word so MergedTile shows the correct word during exit animation
    var displayedWord by remember { mutableStateOf(word) }
    LaunchedEffect(isMerged) { if (isMerged) displayedWord = word }

    // Wiggle animation for emoji (matches iOS: scale 1→1.12, rotation ±3°, 500ms easeInOut repeating)
    val infiniteTransition = rememberInfiniteTransition(label = "wiggle")
    val wiggleScale by infiniteTransition.animateFloat(
        initialValue = 1.0f, targetValue = 1.12f,
        animationSpec = infiniteRepeatable(tween(500, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "wiggleScale"
    )
    val wiggleRotation by infiniteTransition.animateFloat(
        initialValue = -3f, targetValue = 3f,
        animationSpec = infiniteRepeatable(tween(500, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "wiggleRot"
    )

    // boxesAlpha: the container holding individual boxes fades out when merged
    val boxesAlpha by animateFloatAsState(
        targetValue = if (isMerged) 0f else 1f,
        animationSpec = tween(300),
        label = "boxesAlpha"
    )

    // V/C label row fades out during merge
    val vcLabelAlpha by animateFloatAsState(
        targetValue = if (isMerging) 0f else 1f,
        animationSpec = tween(250),
        label = "vcLabelAlpha"
    )

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val boxSizeDp = minOf(maxHeight * 0.20f, maxWidth * 0.13f)
        val imageSizeDp = boxSizeDp * 1.8f
        val spreadXDp = boxSizeDp * 1.5f
        val touchXDp  = boxSizeDp * 1.0f

        // Per-box entry alpha: instant (0 or 1) — no animated alpha to avoid offscreen buffer clipping
        val box0Alpha = if (boxPhase != CvcBoxPhase.HIDDEN) 1f else 0f
        val box1Alpha = if (boxPhase != CvcBoxPhase.HIDDEN && boxPhase != CvcBoxPhase.BOX1_IN) 1f else 0f
        val box2Alpha = if (boxPhase != CvcBoxPhase.HIDDEN && boxPhase != CvcBoxPhase.BOX1_IN && boxPhase != CvcBoxPhase.BOX2_IN) 1f else 0f

        // Per-box dim alpha: lives in outer modifier graphicsLayer (2.0f × size buffer, safe from clipping)
        val box0DimAlpha by animateFloatAsState(
            targetValue = if (phonicsPhase == CvcPhonicsPhase.INDIVIDUAL && highlightedIndex != 0 && !isMerging) 0.45f else 1f,
            animationSpec = tween(200), label = "box0Dim"
        )
        val box1DimAlpha by animateFloatAsState(
            targetValue = if (phonicsPhase == CvcPhonicsPhase.INDIVIDUAL && highlightedIndex != 1 && !isMerging) 0.45f else 1f,
            animationSpec = tween(200), label = "box1Dim"
        )
        val box2DimAlpha by animateFloatAsState(
            targetValue = if (phonicsPhase == CvcPhonicsPhase.INDIVIDUAL && highlightedIndex != 2 && !isMerging) 0.45f else 1f,
            animationSpec = tween(200), label = "box2Dim"
        )

        // Y spring for drop-in (alpha stays 1, Y spring gives the bounce)
        val box0OffsetY by animateFloatAsState(
            targetValue = if (box0Alpha == 1f) 0f else -40f,
            animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMediumLow),
            label = "box0Y"
        )
        val box1OffsetY by animateFloatAsState(
            targetValue = if (box1Alpha == 1f) 0f else -40f,
            animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMediumLow),
            label = "box1Y"
        )
        val box2OffsetY by animateFloatAsState(
            targetValue = if (box2Alpha == 1f) 0f else -40f,
            animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMediumLow),
            label = "box2Y"
        )

        // X: tween for merge slide; instant at spread positions (no animation on initial layout)
        val box0OffsetX by animateFloatAsState(
            targetValue = if (isMerging) -touchXDp.value else -spreadXDp.value,
            animationSpec = tween(durationMillis = 550),
            label = "box0X"
        )
        val box2OffsetX by animateFloatAsState(
            targetValue = if (isMerging) touchXDp.value else spreadXDp.value,
            animationSpec = tween(durationMillis = 550),
            label = "box2X"
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize().padding(Dimens24)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                // Rule line — what C·V·C means
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = Color(0xFF1565C0), fontWeight = FontWeight.Bold)) { append("C") }
                        withStyle(SpanStyle(color = Color(0xFF546E7A))) { append("onsonant · ") }
                        withStyle(SpanStyle(color = Color(0xFFC62828), fontWeight = FontWeight.Bold)) { append("V") }
                        withStyle(SpanStyle(color = Color(0xFF546E7A))) { append("owel · ") }
                        withStyle(SpanStyle(color = Color(0xFF1565C0), fontWeight = FontWeight.Bold)) { append("C") }
                        withStyle(SpanStyle(color = Color(0xFF546E7A))) { append("onsonant — sound out each letter, then ") }
                        withStyle(SpanStyle(color = Color(0xFF6A1B9A), fontWeight = FontWeight.Bold)) { append("blend") }
                        withStyle(SpanStyle(color = Color(0xFF546E7A))) { append(" them into a word!") }
                    },
                    style = MaterialTheme.typography.labelMedium.scaled(),
                    modifier = Modifier
                        .background(Color(0xFF6A1B9A).copy(alpha = 0.08f), RoundedCornerShape(Dimens12))
                        .padding(horizontal = Dimens10, vertical = Dimens4)
                )
                Spacer(modifier = Modifier.height(Dimens12))

                // V/C labels aligned above each box at ±spreadXDp
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer { alpha = vcLabelAlpha }
                ) {
                    Text(
                        text = if (word.segments[0].isVowel) "V" else "C",
                        style = MaterialTheme.typography.titleMedium.scaled(),
                        fontWeight = FontWeight.Bold,
                        color = if (word.segments[0].isVowel) Color(0xB3C62828)
                                else Color(0xB31565C0),
                        modifier = Modifier.offset(x = -spreadXDp)
                    )
                    Text(
                        text = if (word.segments[1].isVowel) "V" else "C",
                        style = MaterialTheme.typography.titleMedium.scaled(),
                        fontWeight = FontWeight.Bold,
                        color = if (word.segments[1].isVowel) Color(0xB3C62828)
                                else Color(0xB31565C0)
                    )
                    Text(
                        text = if (word.segments[2].isVowel) "V" else "C",
                        style = MaterialTheme.typography.titleMedium.scaled(),
                        fontWeight = FontWeight.Bold,
                        color = if (word.segments[2].isVowel) Color(0xB3C62828)
                                else Color(0xB31565C0),
                        modifier = Modifier.offset(x = spreadXDp)
                    )
                }

                Spacer(modifier = Modifier.height(Dimens8))

                // 3-box animation area
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(boxSizeDp + Dimens8)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize().graphicsLayer { alpha = boxesAlpha; clip = false }
                    ) {
                        CvcBox(
                            letter = word.segments[0].letter,
                            isVowel = word.segments[0].isVowel,
                            size = boxSizeDp,
                            isHighlighted = highlightedIndex == 0 || phonicsPhase == CvcPhonicsPhase.BLENDING,
                            isBlending = phonicsPhase == CvcPhonicsPhase.BLENDING,
                            isMerging = isMerging,
                            modifier = Modifier.graphicsLayer {
                                alpha = if (box0Alpha == 0f) 0f else box0DimAlpha
                                translationX = box0OffsetX * density
                                translationY = box0OffsetY * density
                                clip = false
                            }
                        )
                        CvcBox(
                            letter = word.segments[1].letter,
                            isVowel = word.segments[1].isVowel,
                            size = boxSizeDp,
                            isHighlighted = highlightedIndex == 1 || phonicsPhase == CvcPhonicsPhase.BLENDING,
                            isBlending = phonicsPhase == CvcPhonicsPhase.BLENDING,
                            isMerging = isMerging,
                            modifier = Modifier.graphicsLayer {
                                alpha = if (box1Alpha == 0f) 0f else box1DimAlpha
                                translationY = box1OffsetY * density
                                clip = false
                            }
                        )
                        CvcBox(
                            letter = word.segments[2].letter,
                            isVowel = word.segments[2].isVowel,
                            size = boxSizeDp,
                            isHighlighted = highlightedIndex == 2 || phonicsPhase == CvcPhonicsPhase.BLENDING,
                            isBlending = phonicsPhase == CvcPhonicsPhase.BLENDING,
                            isMerging = isMerging,
                            modifier = Modifier.graphicsLayer {
                                alpha = if (box2Alpha == 0f) 0f else box2DimAlpha
                                translationX = box2OffsetX * density
                                translationY = box2OffsetY * density
                                clip = false
                            }
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        AnimatedVisibility(
                            visible = isMerged,
                            enter = scaleIn(
                                initialScale = 0.5f,
                                animationSpec = spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessMediumLow)
                            ),
                            exit = scaleOut(targetScale = 0.5f)
                        ) {
                            CvcMergedTile(word = displayedWord, size = boxSizeDp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(Dimens24))

            // Merged word banner — tappable to replay word audio
            AnimatedVisibility(
                visible = isMerged,
                enter = scaleIn(
                    initialScale = 0.5f,
                    animationSpec = spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessMediumLow)
                ),
                exit = scaleOut(targetScale = 0.5f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens8),
                    modifier = Modifier
                        .background(Color(0xFFEDE7F6), RoundedCornerShape(50))
                        .shadow(
                            elevation = Dimens4,
                            shape = RoundedCornerShape(50),
                            clip = false,
                            ambientColor = Color(0x334A148C),
                            spotColor = Color(0x334A148C)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { viewModel.playWordAudio() }
                        .padding(horizontal = Dimens20, vertical = Dimens12)
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = null,
                        tint = Color(0xFF7B1FA2),
                        modifier = Modifier.size(Dimens20)
                    )
                    Text(
                        text = displayedWord.word,
                        style = MaterialTheme.typography.headlineMedium.scaled(),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A148C)
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens16))

            // Word image with wiggle (real image asset, matching iOS)
            AnimatedVisibility(
                visible = uiState.animateImage,
                enter = scaleIn(initialScale = 0.5f, animationSpec = spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessMediumLow)),
                exit = scaleOut(targetScale = 0.5f)
            ) {
                val resId = getImageResFromWord(displayedWord.word)
                val wiggleMod = Modifier.graphicsLayer {
                    scaleX = wiggleScale; scaleY = wiggleScale
                    rotationZ = wiggleRotation
                }
                if (displayedWord.hasImage && resId != null) {
                    Image(
                        painter = painterResource(resId),
                        contentDescription = displayedWord.word,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(imageSizeDp)
                            .then(wiggleMod)
                    )
                } else {
                    Text(
                        text = wordToEmoji(displayedWord.word),
                        style = MaterialTheme.typography.displayMedium,
                        modifier = wiggleMod
                    )
                }
            }
        }
    }
}

// ── CVC box (same structure as PhonicsBox in BlendingLearnPage) ───────────────

@Composable
private fun CvcBox(
    letter: String,
    isVowel: Boolean,
    size: androidx.compose.ui.unit.Dp,
    isHighlighted: Boolean,
    isBlending: Boolean,
    isMerging: Boolean = false,
    modifier: Modifier = Modifier
) {
    val gradStart      = if (isVowel) Color(0xFFFF5252) else Color(0xFF2979FF)
    val gradEnd        = if (isVowel) Color(0xFFC62828) else Color(0xFF1565C0)
    val shadowColor    = if (isVowel) Color(0xFFB71C1C) else Color(0xFF0D47A1)
    val shadowColorDim = if (isVowel) Color(0x73B71C1C) else Color(0x730D47A1)
    val glowColor = if (isVowel) {
        if (isBlending) Color(0xFFB71C1C) else Color(0xFFFF5252)
    } else {
        if (isBlending) Color(0xFF0D47A1) else Color(0xFF2979FF)
    }
    val glowColorDim = if (isVowel) {
        if (isBlending) Color(0xB3B71C1C) else Color(0xB3FF5252)
    } else {
        if (isBlending) Color(0xB30D47A1) else Color(0xB32979FF)
    }
    val cornerShape = RoundedCornerShape(size * 0.22f)
    val diagonalGradient = Brush.linearGradient(
        colors = listOf(gradStart, gradEnd),
        start = androidx.compose.ui.geometry.Offset(0f, 0f),
        end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    val scale by animateFloatAsState(
        targetValue = if (isHighlighted && !isMerging) 1.15f else 1.0f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMedium),
        label = "cvcScale"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(size * 2.0f)
    ) {
        // Inner box: scale graphicsLayer only — no alpha here (alpha lives in caller's outer graphicsLayer)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(size)
                .graphicsLayer { scaleX = scale; scaleY = scale; clip = false }
        ) {
            // 1. 3D depth shadow layer
            Box(
                modifier = Modifier
                    .size(size)
                    .offset(y = 3.dp)
                    .background(shadowColor, cornerShape)
            )

            // 2. Main tile — diagonal gradient + colored drop shadow
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(size)
                    .shadow(
                        elevation = Dimens4,
                        shape = cornerShape,
                        clip = false,
                        ambientColor = shadowColorDim,
                        spotColor = shadowColorDim
                    )
                    .background(diagonalGradient, cornerShape)
            ) {
                Text(
                    text = letter,
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontSize = (size.value * 0.52f).sp,
                        fontWeight = FontWeight.Bold
                    ).scaled(),
                    color = Color(0x40000000),
                    modifier = Modifier.offset(x = 1.dp, y = 1.5.dp)
                )
                Text(
                    text = letter,
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontSize = (size.value * 0.52f).sp,
                        fontWeight = FontWeight.Bold
                    ).scaled(),
                    color = Color.White
                )
            }

            // 3. Highlight glow overlay
            if (isHighlighted && !isMerging) {
                Box(
                    modifier = Modifier
                        .size(size)
                        .shadow(
                            elevation = 10.dp,
                            shape = cornerShape,
                            clip = false,
                            ambientColor = glowColorDim,
                            spotColor = glowColorDim
                        )
                        .border(3.5.dp, glowColor, cornerShape)
                )
            }
        }
    }
}

// ── Merged tile (3-segment wide tile, purple gradient) ────────────────────────

@Composable
private fun CvcMergedTile(word: CvcWordModel, size: androidx.compose.ui.unit.Dp) {
    val totalWidth  = size * 2.8f
    val cornerShape = RoundedCornerShape(size * 0.20f)
    val gradStart   = Color(0xFFAB47BC)
    val gradEnd     = Color(0xFF6A1B9A)
    val sparkleXOff = size * 1.25f
    val sparkleYOff = -(size * 0.30f)

    Box(contentAlignment = Alignment.Center) {
        // Main gradient tile — large colored shadow creates the glow halo (same as BlendingLearnPage MergedTile)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(width = totalWidth, height = size)
                .shadow(
                    elevation = Dimens12,
                    shape = cornerShape,
                    clip = false,
                    ambientColor = Color(0x806A1B9A),
                    spotColor = Color(0x806A1B9A)
                )
                .background(
                    Brush.linearGradient(
                        listOf(gradStart, gradEnd),
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    ),
                    cornerShape
                )
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(size * 0.08f), verticalAlignment = Alignment.CenterVertically) {
                word.segments.forEach { seg ->
                    Text(
                        text = seg.letter,
                        style = TextStyle(
                            fontSize = if (seg.isVowel) (size.value * 0.55f).sp else (size.value * 0.50f).sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = if (seg.isVowel) Color(0xFFFFCDD2) else Color(0xFF90CAF9)
                    )
                }
            }
        }
        // Sparkle accents
        Text(
            text = "✨",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.offset(x = -sparkleXOff, y = sparkleYOff)
        )
        Text(
            text = "✨",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.offset(x = sparkleXOff, y = sparkleYOff)
        )
    }
}

// ── Word → emoji mapping ──────────────────────────────────────────────────────

private fun wordToEmoji(word: String): String = when (word) {
    "bat" -> "🏏"; "cat" -> "🐱"; "fan" -> "🌀"; "hat" -> "🎩"
    "man" -> "👨"; "map" -> "🗺️"; "rat" -> "🐀"; "tap" -> "🚰"
    "hen" -> "🐔"; "pen" -> "✏️"; "red" -> "🔴"; "ten" -> "🔟"
    "big" -> "🐘"; "pig" -> "🐷"; "sit" -> "🪑"; "win" -> "🏆"
    "dog" -> "🐶"; "hot" -> "🌶️"; "log" -> "🪵"; "pot" -> "🍯"
    "bug" -> "🐛"; "cup" -> "☕"; "run" -> "🏃"; "sun" -> "☀️"
    else -> "📖"
}
