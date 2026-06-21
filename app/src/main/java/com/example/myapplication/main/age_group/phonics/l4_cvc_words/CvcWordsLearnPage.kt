package com.example.myapplication.main.age_group.phonics.l4_cvc_words

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.l4_cvc_words.view_model.CvcBoxPhase
import com.example.myapplication.main.age_group.phonics.l4_cvc_words.view_model.CvcGroup
import com.example.myapplication.main.age_group.phonics.l4_cvc_words.view_model.CvcPhonicsPhase
import com.example.myapplication.main.age_group.phonics.l4_cvc_words.view_model.CvcWordModel
import com.example.myapplication.main.age_group.phonics.l4_cvc_words.view_model.CvcWordsViewModel
import com.example.myapplication.main.age_group.phonics.l4_cvc_words.view_model.cvcGroups
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.utils.extensions.scaled


@Composable
fun CvcWordsLearnPage(
    navController: NavController,
    viewModel: CvcWordsViewModel = hiltViewModel()
) {
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

            // ── LEFT: group tabs + word list ──────────────────────────────
            Column(
                modifier = Modifier
                    .weight(0.38f)
                    .fillMaxHeight()
            ) {
                BackButtonWithText(
                    title = "CVC Words",
                    onBackClick = { navController.popBackStack() }
                )

                // Vowel group tabs
                Row(
                    modifier = Modifier.padding(horizontal = Dimens12, vertical = Dimens8),
                    horizontalArrangement = Arrangement.spacedBy(Dimens6)
                ) {
                    cvcGroups.forEach { group ->
                        val isSelected = uiState.selectedGroup?.vowel == group.vowel
                        val scale by animateFloatAsState(
                            targetValue = if (isSelected) 1.08f else 1.0f,
                            animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMedium),
                            label = "groupScale"
                        )
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .graphicsLayer { scaleX = scale; scaleY = scale }
                                .background(
                                    if (isSelected) group.color else group.color.copy(alpha = 0.15f),
                                    RoundedCornerShape(Dimens8)
                                )
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { viewModel.onGroupTap(group) }
                                .padding(vertical = Dimens8)
                        ) {
                            Text(
                                text = group.vowel,
                                style = MaterialTheme.typography.titleMedium.scaled(),
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else group.color
                            )
                        }
                    }
                }

                // Word list for selected group
                val group = uiState.selectedGroup
                if (group != null) {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = Dimens12),
                        verticalArrangement = Arrangement.spacedBy(Dimens8)
                    ) {
                        items(group.words) { word ->
                            WordTile(
                                word = word,
                                group = group,
                                isSelected = uiState.selectedWord?.word == word.word,
                                onClick = { viewModel.onWordTap(word) }
                            )
                        }
                    }
                } else {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "👆 Pick a vowel\ngroup above",
                            style = MaterialTheme.typography.bodyMedium.scaled(),
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // ── RIGHT: animation panel ────────────────────────────────────
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(0.62f)
                    .fillMaxHeight()
            ) {
                val word = uiState.selectedWord
                val group = uiState.selectedGroup
                if (word != null && group != null) {
                    CvcAnimationPanel(
                        word = word,
                        group = group,
                        uiState = uiState
                    )
                } else {
                    Text(
                        text = "👈 Tap a word to\nsee it come alive!",
                        style = MaterialTheme.typography.titleLarge.scaled(),
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF4527A0).copy(alpha = 0.75f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun WordTile(word: CvcWordModel, group: CvcGroup, isSelected: Boolean, onClick: () -> Unit) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.03f else 1.0f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMedium),
        label = "wordTileScale"
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens8),
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .background(
                if (isSelected) Color(0xFFE8EAF6) else Color.White.copy(alpha = 0.7f),
                RoundedCornerShape(Dimens10)
            )
            .border(Dimens2, if (isSelected) Color(0xFF3949AB).copy(alpha = 0.5f) else Color.Transparent, RoundedCornerShape(Dimens10))
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onClick)
            .padding(horizontal = Dimens12, vertical = Dimens8)
    ) {
        // Mini CVC boxes
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens2)) {
            word.segments.forEach { seg ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(20.dp)
                        .background(
                            if (seg.isVowel) group.color.copy(alpha = if (isSelected) 0.85f else 0.2f)
                            else Color.Gray.copy(alpha = if (isSelected) 0.7f else 0.15f),
                            RoundedCornerShape(3.dp)
                        )
                ) {
                    Text(
                        text = seg.letter,
                        style = MaterialTheme.typography.labelSmall.scaled(),
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else if (seg.isVowel) group.color else Color.Gray
                    )
                }
            }
        }
        Text(
            text = word.word,
            style = MaterialTheme.typography.titleMedium.scaled(),
            fontWeight = FontWeight.Bold,
            color = if (isSelected) group.color else Color.Black.copy(alpha = 0.75f),
            modifier = Modifier.weight(1f)
        )
        if (isSelected) Text("🔊", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun CvcAnimationPanel(
    word: CvcWordModel,
    group: CvcGroup,
    uiState: com.example.myapplication.main.age_group.phonics.l4_cvc_words.view_model.CvcUiState
) {
    val isMerging = uiState.boxPhase == CvcBoxPhase.MERGING || uiState.boxPhase == CvcBoxPhase.MERGED
    val isMerged  = uiState.boxPhase == CvcBoxPhase.MERGED

    // Wiggle animation for image
    val infiniteTransition = rememberInfiniteTransition(label = "wiggle")
    val wiggleAngle by infiniteTransition.animateFloat(
        initialValue = -6f, targetValue = 6f,
        animationSpec = infiniteRepeatable(tween(150, easing = LinearEasing), RepeatMode.Reverse),
        label = "wiggleAngle"
    )
    val imageRotation = if (uiState.animateImage) wiggleAngle else 0f

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(Dimens24)
    ) {

        // C / V / C labels aligned above each box (iOS ZStack with offsets)
        BoxWithConstraints(modifier = Modifier.fillMaxWidth().alpha(if (isMerging) 0f else 1f)) {
            val labelSpreadX = maxWidth * 0.30f
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (word.segments[0].isVowel) "V" else "C",
                    style = MaterialTheme.typography.titleMedium.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = if (word.segments[0].isVowel) Color(0xFFC62828).copy(alpha = 0.7f) else Color(0xFF1565C0).copy(alpha = 0.7f),
                    modifier = Modifier.offset(x = -labelSpreadX)
                )
                Text(
                    text = if (word.segments[1].isVowel) "V" else "C",
                    style = MaterialTheme.typography.titleMedium.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = if (word.segments[1].isVowel) Color(0xFFC62828).copy(alpha = 0.7f) else Color(0xFF1565C0).copy(alpha = 0.7f)
                )
                Text(
                    text = if (word.segments[2].isVowel) "V" else "C",
                    style = MaterialTheme.typography.titleMedium.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = if (word.segments[2].isVowel) Color(0xFFC62828).copy(alpha = 0.7f) else Color(0xFF1565C0).copy(alpha = 0.7f),
                    modifier = Modifier.offset(x = labelSpreadX)
                )
            }
        }

        Spacer(modifier = Modifier.height(Dimens12))

        // 3-box animation
        BoxWithConstraints {
            val boxSizeDp = minOf(maxHeight * 0.28f, maxWidth * 0.20f)
            val spreadXDp = boxSizeDp * 1.5f
            val touchXDp  = boxSizeDp * 1.0f
            val offsets = listOf(-1f, 0f, 1f)

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
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().graphicsLayer { alpha = boxesAlpha }) {
                    word.segments.forEachIndexed { index, seg ->
                        val visibleFromPhase = when (index) {
                            0 -> uiState.boxPhase != CvcBoxPhase.HIDDEN
                            1 -> uiState.boxPhase != CvcBoxPhase.HIDDEN && uiState.boxPhase != CvcBoxPhase.BOX1_IN
                            else -> uiState.boxPhase == CvcBoxPhase.BOX3_IN || uiState.boxPhase == CvcBoxPhase.ALL_SHOWN || uiState.boxPhase == CvcBoxPhase.MERGING
                        }
                        val alpha by animateFloatAsState(
                            targetValue = if (visibleFromPhase) 1f else 0f,
                            animationSpec = spring(dampingRatio = 0.7f), label = "boxAlpha$index"
                        )
                        val offsetY by animateFloatAsState(
                            targetValue = if (visibleFromPhase) 0f else -40f,
                            animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMediumLow), label = "boxY$index"
                        )
                        val offsetX by animateFloatAsState(
                            targetValue = if (isMerging) offsets[index] * touchXDp.value else offsets[index] * spreadXDp.value,
                            animationSpec = tween(durationMillis = 450), label = "boxX$index"
                        )
                        CvcBox(
                            letter = seg.letter,
                            isVowel = seg.isVowel,
                            size = boxSizeDp,
                            isHighlighted = uiState.highlightedIndex == index || uiState.phonicsPhase == CvcPhonicsPhase.BLENDING,
                            isDimmed = uiState.phonicsPhase == CvcPhonicsPhase.INDIVIDUAL && uiState.highlightedIndex != index,
                            isBlending = uiState.phonicsPhase == CvcPhonicsPhase.BLENDING,
                            isMerging = isMerging,
                            modifier = Modifier
                                .alpha(alpha)
                                .offset(y = offsetY.dp, x = offsetX.dp)
                        )
                    }
                }
                val mergedAlpha by animateFloatAsState(
                    targetValue = if (isMerged) 1f else 0f,
                    animationSpec = tween(300),
                    label = "mergedAlpha"
                )
                val mergedScale by animateFloatAsState(
                    targetValue = if (isMerged) 1f else 0.5f,
                    animationSpec = spring(dampingRatio = 0.7f),
                    label = "mergedScale"
                )
                Box(modifier = Modifier.graphicsLayer { alpha = mergedAlpha; scaleX = mergedScale; scaleY = mergedScale }) {
                    CvcMergedTile(word = word, size = boxSizeDp)
                }
            }
        }

        Spacer(modifier = Modifier.height(Dimens20))

        // Word image (placeholder emoji) with wiggle
        AnimatedVisibility(
            visible = isMerged,
            enter = scaleIn(initialScale = 0.5f) + fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens8)
            ) {
                Text(
                    text = wordToEmoji(word.word),
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.graphicsLayer { rotationZ = imageRotation }
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens8),
                    modifier = Modifier
                        .background(Color(0xFFEDE7F6), RoundedCornerShape(50))
                        .padding(horizontal = Dimens16, vertical = Dimens8)
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = null,
                        tint = Color(0xFF7B1FA2),
                        modifier = Modifier.size(Dimens20)
                    )
                    Text(
                        text = word.word,
                        style = MaterialTheme.typography.headlineMedium.scaled(),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A148C)
                    )
                }
            }
        }
    }
}

@Composable
private fun CvcBox(
    letter: String,
    isVowel: Boolean,
    size: androidx.compose.ui.unit.Dp,
    isHighlighted: Boolean,
    isDimmed: Boolean,
    isBlending: Boolean,
    isMerging: Boolean = false,
    modifier: Modifier = Modifier
) {
    val gradStart = if (isVowel) Color(0xFFFF5252) else Color(0xFF2979FF)
    val gradEnd   = if (isVowel) Color(0xFFC62828) else Color(0xFF1565C0)
    val glowColor = if (isVowel) Color(0xFFFF5252) else Color(0xFF2979FF)
    val cornerShape = RoundedCornerShape(size * 0.22f)

    val scale by animateFloatAsState(
        targetValue = if (isHighlighted && !isMerging) 1.15f else 1.0f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMedium),
        label = "cvcBoxScale"
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .graphicsLayer {
                scaleX = scale; scaleY = scale
                alpha = if (isDimmed && !isMerging) 0.45f else 1f
            }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .shadow(elevation = Dimens4, shape = cornerShape, clip = false)
                .background(Brush.linearGradient(listOf(gradStart, gradEnd)), cornerShape)
                .then(
                    if (isHighlighted && !isMerging)
                        Modifier.border(Dimens2, glowColor, cornerShape)
                    else Modifier
                )
        ) {
            Text(
                text = letter,
                style = MaterialTheme.typography.displaySmall.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(alpha = 0.25f),
                modifier = Modifier.offset(x = 1.dp, y = 1.dp)
            )
            Text(
                text = letter,
                style = MaterialTheme.typography.displaySmall.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun CvcMergedTile(word: CvcWordModel, size: androidx.compose.ui.unit.Dp) {
    val totalWidth = size * 2.8f
    val cornerShape = RoundedCornerShape(size * 0.20f)
    val gradStart  = Color(0xFFAB47BC)
    val gradEnd    = Color(0xFF6A1B9A)
    val glowColor  = Color(0xFFCE93D8)
    val sparkleXOff = size * 1.25f
    val sparkleYOff = -(size * 0.30f)

    Box(contentAlignment = Alignment.Center) {
        // Glow halo
        Box(
            modifier = Modifier
                .size(width = totalWidth + size * 0.3f, height = size * 1.15f)
                .background(glowColor.copy(alpha = 0.25f), cornerShape)
                .blur(Dimens12)
        )
        // Main gradient tile
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(width = totalWidth, height = size)
                .shadow(elevation = Dimens4, shape = cornerShape, clip = false)
                .background(Brush.linearGradient(listOf(gradStart, gradEnd)), cornerShape)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(size * 0.06f)) {
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
        // Sparkle icons at top corners
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

private fun wordToEmoji(word: String): String = when (word) {
    "bat" -> "🦇"; "cat" -> "🐱"; "fan" -> "🌀"; "hat" -> "🎩"
    "man" -> "👨"; "map" -> "🗺️"; "rat" -> "🐀"; "tap" -> "🚰"
    "hen" -> "🐔"; "pen" -> "✏️"; "red" -> "🔴"; "ten" -> "🔟"
    "big" -> "🐘"; "pig" -> "🐷"; "sit" -> "🪑"; "win" -> "🏆"
    "dog" -> "🐶"; "hot" -> "🌶️"; "log" -> "🪵"; "pot" -> "🍯"
    "bug" -> "🐛"; "cup" -> "☕"; "run" -> "🏃"; "sun" -> "☀️"
    else -> "📖"
}
