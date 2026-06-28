package com.example.myapplication.main.age_group.phonics.listen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.listen.view_model.ListenSegment
import com.example.myapplication.main.age_group.phonics.listen.view_model.ListenWord
import com.example.myapplication.main.age_group.phonics.listen.view_model.PhonicsListenUiState
import com.example.myapplication.main.age_group.phonics.listen.view_model.PhonicsListenViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.kidsGlassCapsule
import com.example.myapplication.main.common.kidsGlassCard
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens14
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens32
import com.example.myapplication.utils.extensions.scaled

@Composable
fun PhonicsListenPage(
    navController: NavController,
    viewModel: PhonicsListenViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState
    val accent  = viewModel.config.accentColor
    val shadow  = viewModel.config.shadowColor

    DisposableEffect(Unit) {
        onDispose { viewModel.stop() }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.creamMint, shape = KidsFloatingShape.sparkles)

        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
                .padding(bottom = Dimens16)
        ) {
            ListenHeaderRow(
                title   = viewModel.config.title,
                subtitle = viewModel.config.subtitle,
                wordIndex = viewModel.wordIndex,
                totalWords = viewModel.totalWords,
                accent = accent,
                onBack = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.weight(1f))

            ListenWordCard(
                words   = viewModel.config.words,
                uiState = uiState,
                accent  = accent,
                wordIndex = viewModel.wordIndex,
                onSegmentTap = { idx -> if (!uiState.isAutoMode) viewModel.onSegmentTap(idx) }
            )

            Spacer(modifier = Modifier.weight(1f))

            ListenBottomControls(
                uiState   = uiState,
                wordIndex = viewModel.wordIndex,
                totalWords = viewModel.totalWords,
                accent    = accent,
                shadow    = shadow,
                onPrev    = { viewModel.prevWord() },
                onNext    = { viewModel.nextWord() },
                onToggleMode = { viewModel.toggleMode() },
                onPlayPause  = {
                    if (uiState.isPlaying) viewModel.pauseAutoPlay()
                    else viewModel.startAutoPlay()
                }
            )
        }
    }
}

// ── Header ────────────────────────────────────────────────────────────────────

@Composable
private fun ListenHeaderRow(
    title: String,
    subtitle: String,
    wordIndex: Int,
    totalWords: Int,
    accent: Color,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        BackButtonWithText(title = title, expandWidth = false, onBackClick = onBack)

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(Dimens4),
            modifier = Modifier.padding(top = Dimens16, end = Dimens16)
        ) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = Color(0xFF78909C)
            )
            Text(
                text = "${wordIndex + 1} of $totalWords",
                style = MaterialTheme.typography.labelLarge.scaled(),
                fontWeight = FontWeight.Bold,
                color = accent,
                modifier = Modifier
                    .background(accent.copy(alpha = 0.12f), RoundedCornerShape(50))
                    .padding(horizontal = Dimens12, vertical = Dimens4)
            )
        }
    }
}

// ── Word Display Card ─────────────────────────────────────────────────────────

@Composable
private fun ListenWordCard(
    words: List<ListenWord>,
    uiState: PhonicsListenUiState,
    accent: Color,
    wordIndex: Int,
    onSegmentTap: (Int) -> Unit
) {
    AnimatedContent(
        targetState = wordIndex,
        modifier = Modifier.fillMaxWidth().clipToBounds(),
        transitionSpec = {
            if (targetState > initialState) {
                (slideInHorizontally { it } + fadeIn(tween(300)))
                    .togetherWith(slideOutHorizontally { -it } + fadeOut(tween(300)))
            } else {
                (slideInHorizontally { -it } + fadeIn(tween(300)))
                    .togetherWith(slideOutHorizontally { it } + fadeOut(tween(300)))
            }
        },
        label = "wordCard"
    ) { currentWordIndex ->
        val currentWord = words.getOrNull(currentWordIndex) ?: return@AnimatedContent
        val arcSeg = currentWord.segments.firstOrNull { seg ->
            seg.indices.size >= 2 && (seg.indices.max() - seg.indices.min()) > 1
        }
        val showArc  = arcSeg != null
        val arcSegIdx = if (arcSeg != null) currentWord.segments.indexOf(arcSeg) else -1

        val charLeft    = remember(currentWordIndex) { mutableStateMapOf<Int, Float>() }
        val charRight   = remember(currentWordIndex) { mutableStateMapOf<Int, Float>() }
        var canvasRootX by remember(currentWordIndex) { mutableFloatStateOf(0f) }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens20),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens32)
                .kidsGlassCard(cornerRadius = 20.dp, strokeColor = accent)
                .padding(horizontal = Dimens32, vertical = Dimens24)
        ) {
            // Arc canvas (only for Magic-E style non-adjacent pairs)
            if (showArc) {
                val arcColor = if (uiState.segmentIndex == arcSegIdx || uiState.wordDone)
                    accent else Color(0xFFD0D0D0)
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .onGloballyPositioned { canvasRootX = it.positionInRoot().x }
                ) {
                    val i1    = arcSeg!!.indices.min()
                    val i2    = arcSeg.indices.max()
                    val fromX = (charLeft[i1] ?: return@Canvas) - canvasRootX
                    val toX   = (charRight[i2] ?: return@Canvas) - canvasRootX
                    val midX  = (fromX + toX) / 2f
                    val path  = Path().apply {
                        moveTo(fromX, size.height)
                        quadraticTo(midX, 0f, toX, size.height)
                    }
                    drawPath(
                        path, color = arcColor,
                        style = Stroke(
                            width      = 3.dp.toPx(),
                            cap        = StrokeCap.Round,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 5f))
                        )
                    )
                }
                Spacer(modifier = Modifier.height(Dimens4))
            }

            // Character row
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxWidth()
            ) {
                currentWord.word.forEachIndexed { idx, char ->
                    val (_, isActive, isDone) = segmentStateFor(idx, currentWord, uiState)
                    CharacterView(
                        char     = char.toString(),
                        isActive = isActive,
                        isDone   = isDone,
                        wordDone = uiState.wordDone,
                        accent   = accent,
                        modifier = Modifier.onGloballyPositioned { coords ->
                            val pos = coords.positionInRoot()
                            charLeft[idx]  = pos.x
                            charRight[idx] = pos.x + coords.size.width
                        }
                    )
                }
            }

            // Segment dots row
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens24),
                verticalAlignment = Alignment.CenterVertically
            ) {
                currentWord.segments.forEachIndexed { segIdx, seg ->
                    SegmentDot(
                        segIdx   = segIdx,
                        seg      = seg,
                        uiState  = uiState,
                        accent   = accent,
                        onClick  = { onSegmentTap(segIdx) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CharacterView(
    char: String,
    isActive: Boolean,
    isDone: Boolean,
    wordDone: Boolean,
    accent: Color,
    modifier: Modifier = Modifier
) {
    val charColor = when {
        wordDone -> accent
        isActive -> accent
        isDone   -> Color(0xFF37474F)
        else     -> Color(0xFF78909C)
    }
    val scale by animateFloatAsState(
        targetValue = when {
            isActive && !wordDone -> 1.14f
            wordDone -> 1.06f
            else -> 1.0f
        },
        animationSpec = spring(stiffness = 350f, dampingRatio = 0.7f),
        label = "charScale_$char"
    )
    Text(
        text = char,
        style = MaterialTheme.typography.displayLarge.scaled(),
        fontWeight = FontWeight.Bold,
        color = charColor,
        modifier = modifier.graphicsLayer { scaleX = scale; scaleY = scale }
    )
}

@Composable
private fun SegmentDot(
    segIdx: Int,
    seg: ListenSegment,
    uiState: PhonicsListenUiState,
    accent: Color,
    onClick: () -> Unit
) {
    val isActive = uiState.segmentIndex == segIdx
    val isDone   = uiState.playedSegments.contains(segIdx)
    val isAll    = uiState.wordDone

    val dotScale by animateFloatAsState(
        targetValue = if (isActive) 1.35f else 1.0f,
        animationSpec = spring(stiffness = 300f, dampingRatio = 0.6f),
        label = "dotScale_$segIdx"
    )
    val dotColor = when {
        isActive -> accent
        isAll    -> accent
        isDone   -> accent.copy(alpha = 0.35f)
        else     -> Color(0xFF90A4AE)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens6),
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            enabled = !uiState.isAutoMode
        ) { onClick() }
    ) {
        Text(
            text = seg.text.replace("_", ""),
            style = MaterialTheme.typography.bodyLarge.scaled(),
            fontWeight = FontWeight.Bold,
            color = if (isActive) accent else if (isDone) Color(0xFF546E7A) else Color(0xFF607D8B)
        )
        Box(
            modifier = Modifier
                .size(Dimens16)
                .graphicsLayer { scaleX = dotScale; scaleY = dotScale }
                .background(dotColor, CircleShape)
        )
    }
}

// ── Bottom Controls ───────────────────────────────────────────────────────────

@Composable
private fun ListenBottomControls(
    uiState: PhonicsListenUiState,
    wordIndex: Int,
    totalWords: Int,
    accent: Color,
    shadow: Color,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onToggleMode: () -> Unit,
    onPlayPause: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens32),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Prev button
        val prevEnabled = wordIndex > 0
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens6),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .kidsGlassCapsule(strokeColor = if (prevEnabled) accent else Color(0xFFB0BEC5))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    enabled = prevEnabled
                ) { onPrev() }
                .padding(horizontal = Dimens20, vertical = Dimens10)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Prev",
                tint = if (prevEnabled) accent else Color(0xFFB0BEC5), modifier = Modifier.size(Dimens16))
            Text("Prev",
                style = MaterialTheme.typography.labelLarge.scaled(),
                fontWeight = FontWeight.Bold,
                color = if (prevEnabled) accent else Color(0xFFB0BEC5))
        }

        Spacer(modifier = Modifier.weight(1f))

        // Mode toggle
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens8),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .kidsGlassCapsule(strokeColor = accent)
                .padding(horizontal = Dimens12)
        ) {
            Icon(
                imageVector = if (uiState.isAutoMode) Icons.Default.PlayCircle else Icons.Default.TouchApp,
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(Dimens16)
            )
            Text(
                text = if (uiState.isAutoMode) "Auto" else "Manual",
                style = MaterialTheme.typography.labelLarge.scaled(),
                fontWeight = FontWeight.Medium,
                color = Color(0xFF546E7A)
            )
            Switch(
                checked = uiState.isAutoMode,
                onCheckedChange = { onToggleMode() },
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = accent),
                modifier = Modifier.scale(0.75f)
            )
        }

        Spacer(modifier = Modifier.width(Dimens12))

        // Auto play / Tap hint
        if (uiState.isAutoMode) {
            AutoPlayButton(isPlaying = uiState.isPlaying, accent = accent, shadow = shadow, onClick = onPlayPause)
        } else {
            TapHintPill(accent = accent)
        }

        Spacer(modifier = Modifier.weight(1f))

        // Next button
        val nextEnabled = wordIndex < totalWords - 1
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens6),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .kidsGlassCapsule(strokeColor = if (nextEnabled) accent else Color(0xFFB0BEC5))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    enabled = nextEnabled
                ) { onNext() }
                .padding(horizontal = Dimens20, vertical = Dimens10)
        ) {
            Text("Next",
                style = MaterialTheme.typography.labelLarge.scaled(),
                fontWeight = FontWeight.Bold,
                color = if (nextEnabled) accent else Color(0xFFB0BEC5))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next",
                tint = if (nextEnabled) accent else Color(0xFFB0BEC5), modifier = Modifier.size(Dimens16))
        }
    }
}

@Composable
private fun AutoPlayButton(isPlaying: Boolean, accent: Color, shadow: Color, onClick: () -> Unit) {
    val scale by animateFloatAsState(
        targetValue = if (isPlaying) 0.96f else 1.0f,
        animationSpec = spring(stiffness = 300f, dampingRatio = 0.65f),
        label = "playBtnScale"
    )
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens8),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .background(
                Brush.linearGradient(listOf(accent, shadow)),
                RoundedCornerShape(50)
            )
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onClick() }
            .padding(horizontal = Dimens20, vertical = Dimens10)
    ) {
        Icon(
            imageVector = if (isPlaying) Icons.Default.PauseCircle else Icons.Default.VolumeUp,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(Dimens20)
        )
        Text(
            text = if (isPlaying) "Pause" else "Hear It!",
            style = MaterialTheme.typography.titleSmall.scaled(),
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
private fun TapHintPill(accent: Color) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens6),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .kidsGlassCapsule(strokeColor = accent)
            .padding(horizontal = Dimens16, vertical = Dimens10)
    ) {
        Icon(Icons.Default.TouchApp, contentDescription = null, tint = accent, modifier = Modifier.size(Dimens16))
        Text("Tap a sound",
            style = MaterialTheme.typography.labelLarge.scaled(),
            fontWeight = FontWeight.Medium,
            color = Color(0xFF546E7A))
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────────

private fun segmentStateFor(
    charIdx: Int,
    word: ListenWord,
    state: PhonicsListenUiState
): Triple<Int, Boolean, Boolean> {
    val segIdx = word.segments.indexOfFirst { it.indices.contains(charIdx) }
    if (segIdx == -1) return Triple(-1, false, false)
    return Triple(segIdx, state.segmentIndex == segIdx, state.playedSegments.contains(segIdx))
}
