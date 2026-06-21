package com.example.myapplication.main.age_group.phonics.l3_blending

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.l3_blending.view_model.BlendBoxPhase
import com.example.myapplication.main.age_group.phonics.l3_blending.view_model.BlendPhonicsPhase
import com.example.myapplication.main.age_group.phonics.l3_blending.view_model.BlendingViewModel
import com.example.myapplication.main.age_group.phonics.l3_blending.view_model.BlendingWordModel
import com.example.myapplication.main.age_group.phonics.l3_blending.view_model.cvBlendingWords
import com.example.myapplication.main.age_group.phonics.l3_blending.view_model.vcBlendingWords
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled
import kotlinx.coroutines.launch

private val vowelColor = Color(0xFFC62828)
private val consColor  = Color(0xFF1565C0)

@Composable
fun BlendingLearnPage(
    navController: NavController,
    viewModel: BlendingViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState

    DisposableEffect(Unit) {
        onDispose { viewModel.stop() }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.blueIndigo, shape = KidsFloatingShape.sparkles)

        Row(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {

            // ── LEFT: tab + word list ─────────────────────────────────────
            Column(
                modifier = Modifier
                    .weight(0.38f)
                    .fillMaxHeight()
            ) {
                BackButtonWithText(
                    title = "2-Sound Blending",
                    onBackClick = { navController.popBackStack() }
                )

                Spacer(modifier = Modifier.height(Dimens12))

                // VC / CV tab switcher
                TabSwitcher(
                    isVC = uiState.isVC,
                    onTabChange = { viewModel.onTabChange(it) },
                    modifier = Modifier.padding(horizontal = Dimens16)
                )

                Spacer(modifier = Modifier.height(Dimens8))

                val words = if (uiState.isVC) vcBlendingWords else cvBlendingWords
                val listState = rememberLazyListState()
                val scope = rememberCoroutineScope()
                LaunchedEffect(uiState.isVC) {
                    scope.launch { listState.animateScrollToItem(0) }
                }

                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = Dimens16),
                    verticalArrangement = Arrangement.spacedBy(Dimens8)
                ) {
                    items(words) { word ->
                        WordRow(
                            word = word,
                            isSelected = uiState.selectedWord?.word == word.word && uiState.selectedWord?.isVC == word.isVC,
                            onClick = { viewModel.onWordTap(word) }
                        )
                    }
                }
            }

            // ── RIGHT: animation panel ────────────────────────────────────
            Column(
                modifier = Modifier
                    .weight(0.62f)
                    .fillMaxHeight()
            ) {
                // Replay button row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = Dimens16, top = Dimens8),
                    horizontalArrangement = Arrangement.End
                ) {
                    AnimatedVisibility(
                        visible = uiState.selectedWord != null,
                        enter = fadeIn() + scaleIn(initialScale = 0.85f),
                        exit = fadeOut()
                    ) {
                        KidsActionButton(
                            text = "Replay",
                            icon = Icons.Default.Refresh,
                            type = ButtonType.BLUE,
                            isSmall = true,
                            onClick = { viewModel.replayAnimation() }
                        )
                    }
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    if (uiState.selectedWord != null) {
                        AnimationPanel(
                            word = uiState.selectedWord,
                            boxPhase = uiState.boxPhase,
                            highlightedIndex = uiState.highlightedIndex,
                            phonicsPhase = uiState.phonicsPhase
                        )
                    } else {
                        Text(
                            text = "👈 Tap a word to\nhear it blend!",
                            style = MaterialTheme.typography.titleLarge.scaled(),
                            fontWeight = FontWeight.Medium,
                            color = consColor.copy(alpha = 0.75f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

// ── Tab switcher ──────────────────────────────────────────────────────────────

@Composable
private fun TabSwitcher(isVC: Boolean, onTabChange: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(Dimens12))
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .background(if (isVC) consColor else Color.Transparent, RoundedCornerShape(Dimens12))
                .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onTabChange(true) }
                .padding(vertical = Dimens6)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens4), verticalAlignment = Alignment.CenterVertically) {
                Text("V+C", style = MaterialTheme.typography.labelLarge.scaled(), fontWeight = FontWeight.Bold, color = if (isVC) Color.White else consColor)
                Text("(at, in)", style = MaterialTheme.typography.labelSmall.scaled(), color = if (isVC) Color.White.copy(alpha = 0.8f) else consColor.copy(alpha = 0.6f))
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .background(if (!isVC) consColor else Color.Transparent, RoundedCornerShape(Dimens12))
                .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onTabChange(false) }
                .padding(vertical = Dimens6)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens4), verticalAlignment = Alignment.CenterVertically) {
                Text("C+V", style = MaterialTheme.typography.labelLarge.scaled(), fontWeight = FontWeight.Bold, color = if (!isVC) Color.White else consColor)
                Text("(ba, go)", style = MaterialTheme.typography.labelSmall.scaled(), color = if (!isVC) Color.White.copy(alpha = 0.8f) else consColor.copy(alpha = 0.6f))
            }
        }
    }
}

// ── Word row ──────────────────────────────────────────────────────────────────

@Composable
private fun WordRow(word: BlendingWordModel, isSelected: Boolean, onClick: () -> Unit) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1.0f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMedium),
        label = "wordRowScale"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens8),
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .shadow(
                elevation = if (isSelected) Dimens4 else 0.dp,
                shape = RoundedCornerShape(Dimens12),
                clip = false
            )
            .background(
                if (isSelected) Color(0xFFE3F2FD) else Color.White.copy(alpha = 0.7f),
                RoundedCornerShape(Dimens12)
            )
            .border(
                Dimens2,
                if (isSelected) consColor.copy(alpha = 0.5f) else Color.Transparent,
                RoundedCornerShape(Dimens12)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = Dimens10, vertical = Dimens8)
    ) {
        // Mini segment tiles
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens4)) {
            word.segments.forEach { seg ->
                MiniTile(letter = seg.letters, isVowel = seg.isVowel, selected = isSelected)
            }
        }

        Text(
            text = word.word,
            style = MaterialTheme.typography.titleMedium.scaled(),
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color(0xFF1A237E) else Color.Black.copy(alpha = 0.75f),
            modifier = Modifier.weight(1f)
        )

        if (isSelected) {
            Icon(Icons.Default.VolumeUp, contentDescription = null, tint = consColor, modifier = Modifier.size(Dimens16))
        }
    }
}

@Composable
private fun MiniTile(letter: String, isVowel: Boolean, selected: Boolean) {
    val bg = if (isVowel) {
        if (selected) Color(0xFFFF5252) else Color(0xFFFFCDD2)
    } else {
        if (selected) Color(0xFF2979FF) else Color(0xFFBBDEFB)
    }
    val fg = if (selected) Color.White else if (isVowel) Color(0xFFC62828) else Color(0xFF1565C0)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(22.dp)
            .background(bg, RoundedCornerShape(5.dp))
    ) {
        Text(
            text = letter,
            style = MaterialTheme.typography.labelSmall.scaled(),
            fontWeight = FontWeight.Bold,
            color = fg
        )
    }
}

// ── Animation panel ───────────────────────────────────────────────────────────

@Composable
private fun AnimationPanel(
    word: BlendingWordModel,
    boxPhase: BlendBoxPhase,
    highlightedIndex: Int,
    phonicsPhase: BlendPhonicsPhase
) {
    val seg0 = word.segments[0]
    val seg1 = word.segments[1]
    val isMerging = boxPhase == BlendBoxPhase.MERGING || boxPhase == BlendBoxPhase.MERGED
    val isMerged  = boxPhase == BlendBoxPhase.MERGED

    // Freeze the word shown in MergedTile: only update when entering merged state,
    // so the old word stays visible during the exit animation when a new card is tapped.
    var displayedWord by remember { mutableStateOf(word) }
    LaunchedEffect(isMerged) {
        if (isMerged) displayedWord = word
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(1f))
        // Pattern label: "Vowel + Consonant" etc.
        val color0 = if (seg0.isVowel) vowelColor else consColor
        val color1 = if (seg1.isVowel) vowelColor else consColor
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LabelChip(text = if (seg0.isVowel) "Vowel" else "Consonant", color = color0)
            Text("+", style = MaterialTheme.typography.titleMedium.scaled(), color = Color.Gray)
            LabelChip(text = if (seg1.isVowel) "Vowel" else "Consonant", color = color1)
        }

        Spacer(modifier = Modifier.height(Dimens20))

        // V/C labels + Box animation
        BoxWithConstraints {
            val boxSizeDp = minOf(maxHeight * 0.22f, maxWidth * 0.14f)
            val spreadXDp = boxSizeDp * 0.80f
            val touchXDp  = boxSizeDp * 0.5f

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // V/C labels aligned above each box (iOS ZStack with offsets)
                val vcLabelAlpha by animateFloatAsState(
                    targetValue = if (isMerging) 0f else 1f,
                    animationSpec = tween(durationMillis = 250),
                    label = "vcLabelAlpha"
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(vcLabelAlpha)
                ) {
                    Text(
                        text = if (seg0.isVowel) "V" else "C",
                        style = MaterialTheme.typography.titleMedium.scaled(),
                        fontWeight = FontWeight.Bold,
                        color = if (seg0.isVowel) Color(0xFFC62828).copy(alpha = 0.7f) else Color(0xFF1565C0).copy(alpha = 0.7f),
                        modifier = Modifier.offset(x = -spreadXDp)
                    )
                    Text(
                        text = if (seg1.isVowel) "V" else "C",
                        style = MaterialTheme.typography.titleMedium.scaled(),
                        fontWeight = FontWeight.Bold,
                        color = if (seg1.isVowel) Color(0xFFC62828).copy(alpha = 0.7f) else Color(0xFF1565C0).copy(alpha = 0.7f),
                        modifier = Modifier.offset(x = spreadXDp)
                    )
                }

                Spacer(modifier = Modifier.height(Dimens8))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(boxSizeDp + Dimens8)
            ) {
                val boxesAlpha by animateFloatAsState(
                    targetValue = if (isMerged) 0f else 1f,
                    animationSpec = tween(300),
                    label = "boxesAlpha"
                )
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().graphicsLayer { alpha = boxesAlpha; clip = false }) {
                    // Alpha is instant (0 or 1) — animated alpha creates an offscreen buffer that
                    // clips the 10dp glow shadow. The Y spring provides the drop-in visual.
                    val box1Alpha = if (boxPhase == BlendBoxPhase.HIDDEN) 0f else 1f
                    val box1DimAlpha by animateFloatAsState(
                        targetValue = if (phonicsPhase == BlendPhonicsPhase.INDIVIDUAL && highlightedIndex != 0 && !isMerging) 0.45f else 1f,
                        animationSpec = tween(200), label = "box1Dim"
                    )
                    val box1OffsetY by animateFloatAsState(
                        targetValue = if (boxPhase == BlendBoxPhase.HIDDEN) -40f else 0f,
                        animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMediumLow), label = "box1Y"
                    )
                    // X: raw spread when not merging (no animation = no horizontal entry motion)
                    //    tween(550) only during merge slide
                    val box1MergeX by animateFloatAsState(
                        targetValue = -touchXDp.value,
                        animationSpec = tween(durationMillis = 550),
                        label = "box1MergeX"
                    )
                    PhonicsBox(
                        letter = seg0.letters,
                        isVowel = seg0.isVowel,
                        size = boxSizeDp,
                        isHighlighted = highlightedIndex == 0 || phonicsPhase == BlendPhonicsPhase.BLENDING,
                        isBlending = phonicsPhase == BlendPhonicsPhase.BLENDING,
                        isMerging = isMerging,
                        modifier = Modifier.graphicsLayer {
                            alpha = if (box1Alpha == 0f) 0f else box1DimAlpha
                            translationX = (if (isMerging) box1MergeX else -spreadXDp.value) * density
                            translationY = box1OffsetY * density
                            clip = false
                        }
                    )

                    val box2Visible = boxPhase != BlendBoxPhase.HIDDEN && boxPhase != BlendBoxPhase.BOX1_IN
                    val box2Alpha = if (box2Visible) 1f else 0f
                    val box2DimAlpha by animateFloatAsState(
                        targetValue = if (phonicsPhase == BlendPhonicsPhase.INDIVIDUAL && highlightedIndex != 1 && !isMerging) 0.45f else 1f,
                        animationSpec = tween(200), label = "box2Dim"
                    )
                    val box2OffsetY by animateFloatAsState(
                        targetValue = if (box2Visible) 0f else -40f,
                        animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMediumLow), label = "box2Y"
                    )
                    val box2MergeX by animateFloatAsState(
                        targetValue = touchXDp.value,
                        animationSpec = tween(durationMillis = 550),
                        label = "box2MergeX"
                    )
                    PhonicsBox(
                        letter = seg1.letters,
                        isVowel = seg1.isVowel,
                        size = boxSizeDp,
                        isHighlighted = highlightedIndex == 1 || phonicsPhase == BlendPhonicsPhase.BLENDING,
                        isBlending = phonicsPhase == BlendPhonicsPhase.BLENDING,
                        isMerging = isMerging,
                        modifier = Modifier.graphicsLayer {
                            alpha = if (box2Alpha == 0f) 0f else box2DimAlpha
                            translationX = (if (isMerging) box2MergeX else spreadXDp.value) * density
                            translationY = box2OffsetY * density
                            clip = false
                        }
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AnimatedVisibility(
                        visible = isMerged,
                        enter = scaleIn(initialScale = 0.5f, animationSpec = spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessMediumLow)),
                        exit = scaleOut(targetScale = 0.5f)
                    ) {
                        MergedTile(word = displayedWord, size = boxSizeDp)
                    }
                }
            }
            } // end Column
        }

        Spacer(modifier = Modifier.height(Dimens24))

        // Merged word banner
        AnimatedVisibility(
            visible = isMerged,
            enter = scaleIn(
                animationSpec = spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessMediumLow),
                initialScale = 0.5f
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens12),
                modifier = Modifier
                    .shadow(elevation = Dimens8, shape = RoundedCornerShape(Dimens16), clip = false)
                    .background(Color.White.copy(alpha = 0.88f), RoundedCornerShape(Dimens16))
                    .padding(horizontal = Dimens20, vertical = Dimens12)
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(30.dp))
                Text(
                    text = word.word,
                    style = MaterialTheme.typography.displaySmall.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A237E)
                )
                Text(
                    text = "= ${if (word.isVC) "VC" else "CV"} blend!",
                    style = MaterialTheme.typography.titleSmall.scaled(),
                    color = Color.Gray
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun PhonicsBox(
    letter: String,
    isVowel: Boolean,
    size: androidx.compose.ui.unit.Dp,
    isHighlighted: Boolean,
    isBlending: Boolean,
    isMerging: Boolean = false,
    modifier: Modifier = Modifier
) {
    val gradStart   = if (isVowel) Color(0xFFFF5252) else Color(0xFF2979FF)
    val gradEnd     = if (isVowel) Color(0xFFC62828) else Color(0xFF1565C0)
    val shadowColor = if (isVowel) Color(0xFFB71C1C) else Color(0xFF0D47A1)
    // glow darkens during blending phase, matching iOS
    val glowColor = if (isVowel) {
        if (isBlending) Color(0xFFB71C1C) else Color(0xFFFF5252)
    } else {
        if (isBlending) Color(0xFF0D47A1) else Color(0xFF2979FF)
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
        label = "boxScale"
    )
    // Outer box: 1.50f provides buffer room for scale spring (max ~1.20×) + drop shadows at any scale
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(size * 1.50f)
    ) {
        // Inner box owns scale graphicsLayer only (dim alpha lives in the caller's outer graphicsLayer)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(size)
                .graphicsLayer {
                    scaleX = scale; scaleY = scale
                    clip = false
                }
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
                        ambientColor = shadowColor.copy(alpha = 0.45f),
                        spotColor = shadowColor.copy(alpha = 0.45f)
                    )
                    .background(diagonalGradient, cornerShape)
            ) {
                Text(
                    text = letter,
                    style = MaterialTheme.typography.displaySmall.copy(fontSize = (size.value * 0.52f).sp, fontWeight = FontWeight.Bold).scaled(),
                    color = Color.Black.copy(alpha = 0.25f),
                    modifier = Modifier.offset(x = 1.dp, y = 1.5.dp)
                )
                Text(
                    text = letter,
                    style = MaterialTheme.typography.displaySmall.copy(fontSize = (size.value * 0.52f).sp, fontWeight = FontWeight.Bold).scaled(),
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
                            ambientColor = glowColor.copy(alpha = 0.7f),
                            spotColor = glowColor.copy(alpha = 0.7f)
                        )
                        .border(3.5.dp, glowColor, cornerShape)
                )
            }
        }
    }
}

@Composable
private fun MergedTile(word: BlendingWordModel, size: androidx.compose.ui.unit.Dp) {
    val totalWidth = size * 2.1f
    val cornerShape = RoundedCornerShape(size * 0.20f)
    val gradStart  = Color(0xFF3949AB)
    val gradEnd    = Color(0xFF1A237E)
    val glowColor  = Color(0xFF5C6BC0)
    val sparkleXOff = size * 0.85f
    val sparkleYOff = -(size * 0.30f)

    Box(contentAlignment = Alignment.Center) {
        // Main gradient tile
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(width = totalWidth, height = size)
                .shadow(elevation = Dimens12, shape = cornerShape, clip = false, ambientColor = gradEnd.copy(alpha = 0.5f), spotColor = gradEnd.copy(alpha = 0.5f))
                .background(
                    Brush.linearGradient(
                        colors = listOf(gradStart, gradEnd),
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    ),
                    cornerShape
                )
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(size * 0.08f), verticalAlignment = Alignment.CenterVertically) {
                word.segments.forEach { seg ->
                    Text(
                        text = seg.letters,
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontSize = if (seg.isVowel) (size.value * 0.55f).sp else (size.value * 0.50f).sp,
                            fontWeight = FontWeight.Bold
                        ).scaled(),
                        color = if (seg.isVowel) Color(0xFFFFCDD2) else Color(0xFF90CAF9)
                    )
                }
            }
        }
        // Sparkle icons at top corners
        Text(
            text = "✨",
            style = MaterialTheme.typography.titleSmall.scaled(),
            modifier = Modifier.offset(x = -sparkleXOff, y = sparkleYOff)
        )
        Text(
            text = "✨",
            style = MaterialTheme.typography.bodySmall.scaled(),
            modifier = Modifier.offset(x = sparkleXOff, y = sparkleYOff)
        )
    }
}

@Composable
private fun LabelChip(text: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.10f), RoundedCornerShape(50))
            .padding(horizontal = Dimens10, vertical = Dimens4)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.scaled(),
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}
