package com.example.myapplication.main.age_group.phonics.l14_diphthongs

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.example.myapplication.main.age_group.phonics.l14_diphthongs.view_model.DiphthongsListenEntry
import com.example.myapplication.main.age_group.phonics.l14_diphthongs.view_model.DiphthongsListenUiState
import com.example.myapplication.main.age_group.phonics.l14_diphthongs.view_model.DiphthongsListenViewModel
import com.example.myapplication.main.age_group.phonics.l14_diphthongs.view_model.diphthongsListenEntries
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.kidsGlassCapsule
import com.example.myapplication.main.common.kidsGlassCard
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens32
import com.example.myapplication.utils.extensions.scaled

@Composable
fun DiphthongsListenPage(navController: NavController) {
    val viewModel: DiphthongsListenViewModel = hiltViewModel()
    val uiState = viewModel.uiState
    val entry   = viewModel.currentEntry
    val accent  = entry?.accentColor ?: Color(0xFFE65100)
    val shadow  = entry?.shadowColor ?: Color(0xFFBF360C)

    DisposableEffect(Unit) { onDispose { viewModel.stop() } }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.peachCoral, shape = KidsFloatingShape.bubbles)

        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
                .padding(bottom = Dimens16)
        ) {
            // ── Header ─────────────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                BackButtonWithText(
                    title = "Diphthongs Listen",
                    expandWidth = false,
                    onBackClick = { navController.popBackStack() }
                )
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(Dimens4),
                    modifier = Modifier.padding(top = Dimens16, end = Dimens16)
                ) {
                    if (entry != null) {
                        Text(
                            text = "'${entry.diphthong}' glides as one sound ${entry.sound}",
                            style = MaterialTheme.typography.labelSmall.scaled(),
                            color = Color(0xFF78909C)
                        )
                    }
                    Text(
                        text = "${viewModel.wordIndex + 1} of ${viewModel.totalWords}",
                        style = MaterialTheme.typography.labelLarge.scaled(),
                        fontWeight = FontWeight.Bold,
                        color = accent,
                        modifier = Modifier
                            .background(accent.copy(alpha = 0.12f), RoundedCornerShape(50))
                            .padding(horizontal = Dimens12, vertical = Dimens4)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // ── Word Card ──────────────────────────────────────────────────────
            if (entry != null) {
                DiphthongsWordCard(
                    entry          = entry,
                    uiState        = uiState,
                    wordIndex      = viewModel.wordIndex,
                    accent         = accent,
                    onSegmentTap   = { idx -> if (!uiState.isAutoMode) viewModel.onSegmentTap(idx) }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // ── Bottom Controls ────────────────────────────────────────────────
            DiphthongsBottomControls(
                uiState      = uiState,
                wordIndex    = viewModel.wordIndex,
                totalWords   = viewModel.totalWords,
                accent       = accent,
                shadow       = shadow,
                onPrev       = { viewModel.prevWord() },
                onNext       = { viewModel.nextWord() },
                onToggleMode = { viewModel.toggleMode() },
                onPlayPause  = {
                    if (uiState.isPlaying) viewModel.pauseAutoPlay()
                    else viewModel.startAutoPlay()
                }
            )
        }
    }
}

// ── Word Card ─────────────────────────────────────────────────────────────────

@Composable
private fun DiphthongsWordCard(
    entry: DiphthongsListenEntry,
    uiState: DiphthongsListenUiState,
    wordIndex: Int,
    accent: Color,
    onSegmentTap: (Int) -> Unit
) {
    AnimatedContent(
        modifier = Modifier.fillMaxWidth().clipToBounds(),
        targetState = wordIndex,
        transitionSpec = {
            if (targetState > initialState) {
                (slideInHorizontally { it } + fadeIn(tween(300)))
                    .togetherWith(slideOutHorizontally { -it } + fadeOut(tween(300)))
            } else {
                (slideInHorizontally { -it } + fadeIn(tween(300)))
                    .togetherWith(slideOutHorizontally { it } + fadeOut(tween(300)))
            }
        },
        label = "diphthongWord"
    ) { currentIndex ->
        val currentEntry = diphthongsListenEntries.getOrNull(currentIndex) ?: return@AnimatedContent
        var canvasRootX  by remember { mutableFloatStateOf(0f) }
        var pair1LeftX   by remember { mutableFloatStateOf(0f) }
        var pair2RightX  by remember { mutableFloatStateOf(0f) }

        val showArc = currentEntry.showArc

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens20),
            modifier = Modifier
                .padding(horizontal = Dimens32)
                .kidsGlassCard(cornerRadius = 20.dp, strokeColor = accent)
                .padding(horizontal = Dimens32, vertical = Dimens24)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (showArc) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                            .onGloballyPositioned { canvasRootX = it.positionInRoot().x }
                    ) {
                        if (pair1LeftX > 0f && pair2RightX > 0f) {
                            val fromX    = pair1LeftX - canvasRootX
                            val toX      = pair2RightX - canvasRootX
                            val midX     = (fromX + toX) / 2f
                            val arcColor = if (uiState.segmentIndex == 1 || uiState.wordDone) currentEntry.accentColor else Color(0xFFD0D0D0)
                            val path = Path().apply {
                                moveTo(fromX, size.height)
                                quadraticBezierTo(midX, 2.dp.toPx(), toX, size.height)
                            }
                            drawPath(
                                path = path, color = arcColor,
                                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round,
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 6f)))
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(Dimens4))
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {
                    currentEntry.chars.forEachIndexed { idx, ch ->
                        DiphthongCharCell(
                            char    = ch,
                            charIdx = idx,
                            entry   = currentEntry,
                            uiState = uiState,
                            onPositioned = { leftX, rightX ->
                                if (idx == currentEntry.pairIdx1) pair1LeftX  = leftX
                                if (idx == currentEntry.pairIdx2) pair2RightX = rightX
                            }
                        )
                    }
                }
            }

            // only dots for segments that have content
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens24),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (segIdx in 0..2) {
                    if (currentEntry.hasSegment(segIdx)) {
                        DiphthongSegmentDot(
                            segIdx      = segIdx,
                            entry       = currentEntry,
                            uiState     = uiState,
                            accentColor = accent,
                            onClick     = { onSegmentTap(segIdx) }
                        )
                    }
                }
            }
        }
        } // Box
    }
}

// ── Character Cell ────────────────────────────────────────────────────────────

@Composable
private fun DiphthongCharCell(
    char: String,
    charIdx: Int,
    entry: DiphthongsListenEntry,
    uiState: DiphthongsListenUiState,
    onPositioned: (leftX: Float, rightX: Float) -> Unit
) {
    val seg      = entry.charSegment(charIdx)
    val isActive = uiState.segmentIndex == seg && entry.hasSegment(seg)
    val wordDone = uiState.wordDone

    val targetColor = when {
        wordDone -> entry.accentColor
        isActive -> entry.accentColor
        uiState.playedSegments.contains(seg) -> Color(0xFF37474F)
        else     -> Color(0xFF78909C)
    }
    val charColor by animateColorAsState(
        targetValue   = targetColor,
        animationSpec = spring(stiffness = 350f, dampingRatio = 0.7f),
        label         = "diphthongCharColor_$charIdx"
    )
    val scale by animateFloatAsState(
        targetValue = when {
            isActive && !wordDone -> 1.14f
            wordDone              -> 1.06f
            else                  -> 1.0f
        },
        animationSpec = spring(stiffness = 350f, dampingRatio = 0.7f),
        label = "diphthongCharScale_$charIdx"
    )

    Text(
        text       = char,
        style      = MaterialTheme.typography.displayLarge.scaled(),
        fontWeight = FontWeight.Bold,
        color      = charColor,
        modifier   = Modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .onGloballyPositioned { coords ->
                val rootX = coords.positionInRoot().x
                onPositioned(rootX, rootX + coords.size.width)
            }
    )
}

// ── Segment Dot ───────────────────────────────────────────────────────────────

@Composable
private fun DiphthongSegmentDot(
    segIdx: Int,
    entry: DiphthongsListenEntry,
    uiState: DiphthongsListenUiState,
    accentColor: Color,
    onClick: () -> Unit
) {
    val isActive = uiState.segmentIndex == segIdx
    val isDone   = uiState.playedSegments.contains(segIdx)
    val isAll    = uiState.wordDone
    val hasData  = entry.hasSegment(segIdx)

    val dotScale by animateFloatAsState(
        targetValue   = if (isActive) 1.35f else 1.0f,
        animationSpec = spring(stiffness = 300f, dampingRatio = 0.6f),
        label         = "diphthongDotScale_$segIdx"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens6),
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            enabled = !uiState.isAutoMode && hasData
        ) { onClick() }
    ) {
        Text(
            text       = entry.segmentLabel(segIdx),
            style      = MaterialTheme.typography.bodyLarge.scaled(),
            fontWeight = FontWeight.Bold,
            color      = when {
                isActive -> accentColor
                isDone   -> Color(0xFF546E7A)
                else     -> Color(0xFF607D8B)
            },
            modifier = Modifier.graphicsLayer { alpha = if (hasData) 1f else 0.35f }
        )
        Box(
            modifier = Modifier
                .size(Dimens16)
                .graphicsLayer { scaleX = dotScale; scaleY = dotScale }
                .background(
                    color = when {
                        !hasData -> Color(0xFF90A4AE).copy(alpha = 0.3f)
                        isActive -> accentColor
                        isAll    -> accentColor
                        isDone   -> accentColor.copy(alpha = 0.35f)
                        else     -> Color(0xFF90A4AE)
                    },
                    shape = CircleShape
                )
        )
    }
}

// ── Bottom Controls ───────────────────────────────────────────────────────────

@Composable
private fun DiphthongsBottomControls(
    uiState: DiphthongsListenUiState,
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
        val prevEnabled = wordIndex > 0
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens6),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .kidsGlassCapsule(strokeColor = if (prevEnabled) accent else Color(0xFFB0BEC5))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null, enabled = prevEnabled
                ) { onPrev() }
                .padding(horizontal = Dimens20, vertical = Dimens8)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, null,
                tint = if (prevEnabled) accent else Color(0xFFB0BEC5),
                modifier = Modifier.size(Dimens16))
            Text("Prev",
                style = MaterialTheme.typography.labelLarge.scaled(),
                fontWeight = FontWeight.Bold,
                color = if (prevEnabled) accent else Color(0xFFB0BEC5))
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens8),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .kidsGlassCapsule(strokeColor = accent)
                .padding(horizontal = Dimens12)
        ) {
            Icon(
                imageVector = if (uiState.isAutoMode) Icons.Default.PlayCircle else Icons.Default.TouchApp,
                contentDescription = null, tint = accent, modifier = Modifier.size(Dimens16)
            )
            Text(
                text = if (uiState.isAutoMode) "Auto" else "Manual",
                style = MaterialTheme.typography.labelLarge.scaled(),
                fontWeight = FontWeight.Medium, color = Color(0xFF546E7A)
            )
            Switch(
                checked = uiState.isAutoMode,
                onCheckedChange = { onToggleMode() },
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = accent),
                modifier = Modifier.scale(0.75f)
            )
        }

        Spacer(modifier = Modifier.width(Dimens12))

        if (uiState.isAutoMode) {
            val playScale by animateFloatAsState(
                targetValue = if (uiState.isPlaying) 0.96f else 1.0f,
                animationSpec = spring(stiffness = 300f, dampingRatio = 0.65f),
                label = "diphthongPlayBtn"
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens8),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .graphicsLayer { scaleX = playScale; scaleY = playScale }
                    .background(Brush.linearGradient(listOf(accent, shadow)), RoundedCornerShape(50))
                    .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onPlayPause() }
                    .padding(horizontal = Dimens20, vertical = Dimens8)
            ) {
                Icon(
                    imageVector = if (uiState.isPlaying) Icons.Default.PauseCircle else Icons.Default.VolumeUp,
                    contentDescription = null, tint = Color.White, modifier = Modifier.size(Dimens20)
                )
                Text(
                    text = if (uiState.isPlaying) "Pause" else "Hear It!",
                    style = MaterialTheme.typography.titleSmall.scaled(),
                    fontWeight = FontWeight.Bold, color = Color.White
                )
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens6),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .kidsGlassCapsule(strokeColor = accent)
                    .padding(horizontal = Dimens16, vertical = Dimens8)
            ) {
                Icon(Icons.Default.TouchApp, null, tint = accent, modifier = Modifier.size(Dimens16))
                Text("Tap a sound",
                    style = MaterialTheme.typography.labelLarge.scaled(),
                    fontWeight = FontWeight.Medium, color = Color(0xFF546E7A))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        val nextEnabled = wordIndex < totalWords - 1
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens6),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .kidsGlassCapsule(strokeColor = if (nextEnabled) accent else Color(0xFFB0BEC5))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null, enabled = nextEnabled
                ) { onNext() }
                .padding(horizontal = Dimens20, vertical = Dimens8)
        ) {
            Text("Next",
                style = MaterialTheme.typography.labelLarge.scaled(),
                fontWeight = FontWeight.Bold,
                color = if (nextEnabled) accent else Color(0xFFB0BEC5))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, null,
                tint = if (nextEnabled) accent else Color(0xFFB0BEC5),
                modifier = Modifier.size(Dimens16))
        }
    }
}
