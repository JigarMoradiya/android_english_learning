package com.example.myapplication.main.onboarding

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.AppDimens
import com.example.myapplication.ui.theme.customFonts
import com.example.myapplication.utils.AudioPlayerManager
import kotlinx.coroutines.delay
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.zIndex
import android.os.SystemClock
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.helper.distance
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.helper.getStrokesForLetter
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.helper.sampleStroke
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.LetterMode
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin

private val TITLE_COLOR = Color(0xFF3D2B9E)

/** Landscape column split, shared by the page body and the button row so Next sits under the play area. */
private const val LEFT_WEIGHT = 0.46f
private const val RIGHT_WEIGHT = 0.54f

/** The iPad multiplier so type/sizes grow the same way on tablets. */
private val k: Float get() = if (AppDimens.isLargeTablet) 1.6f else if (AppDimens.isTablet) 1.35f else 1f

/**
 * First-launch "learn by doing" tutorial. Welcome → Tap → Drag → Trace → Go, with the app's
 * playful look: floating decorations, gold step-pills, speech bubbles, chunky 3D buttons, a
 * bobbing mascot, and celebrations. Interactive pages lock Next until the child does the action.
 * [onFinish] is wired by the nav host to persist the "seen" flag and move on to home.
 */
@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val pages = onboardingPages
    var index by remember {
        mutableIntStateOf(if (OnboardingDebug.force) OnboardingDebug.startPage.coerceIn(0, onboardingPages.lastIndex) else 0)
    }
    val isLast = index == pages.lastIndex
    val page = pages[index]

    // Per-page completion. Intro pages are "done" from the start; play pages flip when solved.
    val doneStates = remember {
        mutableStateListOf(*Array(pages.size) { OnboardingDebug.forceDone || pages[it].kind == OnbKind.INTRO })
    }
    val currentDone = doneStates[index]

    // Idle coach: any touch anywhere resets the clock; a solved page left idle gets a hand pointing at Next.
    val clock = remember { TouchClock() }
    val nextIdle = rememberIdle(clock, index * 10 + if (currentDone) 1 else 0)

    // Going back clears interactive answers, so the child does them again and Next re-locks.
    fun goBack() {
        val to = index - 1
        if (to < 0) return
        if (pages[index].kind != OnbKind.INTRO) doneStates[index] = false
        if (pages[to].kind != OnbKind.INTRO) doneStates[to] = false
        index = to
    }

    BackHandler(enabled = index > 0) { goBack() }

    val isLandscape = androidx.compose.ui.platform.LocalConfiguration.current.orientation ==
        android.content.res.Configuration.ORIENTATION_LANDSCAPE

    Box(
        Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        awaitPointerEvent(PointerEventPass.Initial)
                        clock.last = SystemClock.uptimeMillis()
                    }
                }
            }
            .background(Brush.verticalGradient(listOf(page.topColor, page.bottomColor)))
    ) {
        FloatingDeco(page.deco, page.accent)

        // Background runs edge to edge; content stays clear of the notch, cutout and system bars.
        Column(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)) {
            // Top: step pills + Skip
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(top = AppDimens.Dimens16, start = AppDimens.Dimens16, end = AppDimens.Dimens16),
                contentAlignment = Alignment.Center
            ) {
                StepPills(pages, index, doneStates)
                if (!isLast) {
                    Text(
                        "Skip",
                        color = page.accentDark,
                        fontFamily = customFonts,
                        fontWeight = FontWeight.Bold,
                        fontSize = (15f * k).sp,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clip(RoundedCornerShape(50))
                            .background(Color.White.copy(alpha = 0.65f))
                            .clickable {
                                AudioPlayerManager.playSoundMenuClick()
                                onFinish()
                            }
                            .padding(horizontal = AppDimens.Dimens16, vertical = AppDimens.Dimens8)
                    )
                }
            }

            // Middle: the page, sliding in from the side
            AnimatedContent(
                targetState = index,
                transitionSpec = {
                    val forward = targetState > initialState
                    val dir = if (forward) 1 else -1
                    (slideInHorizontally { w -> dir * w } + fadeIn()) togetherWith
                        (slideOutHorizontally { w -> -dir * w } + fadeOut())
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                label = "obPage"
            ) { i ->
                CompositionLocalProvider(LocalTouchClock provides clock) {
                    PageBody(
                        page = pages[i],
                        done = doneStates[i],
                        onDone = {
                            if (!doneStates[i]) {
                                doneStates[i] = true
                                AudioPlayerManager.playSoundCorrectAnswer()
                            }
                        }
                    )
                }
            }

            // Bottom: Previous + Next (Next locked until the page is done)
            val back: @Composable () -> Unit = {
                if (index > 0) {
                    GhostButton(text = "Back", accent = page.accentDark) {
                        AudioPlayerManager.playSoundMenuClick()
                        goBack()
                    }
                } else {
                    Spacer(Modifier.width(1.dp))
                }
            }
            val next: @Composable () -> Unit = {
                ChunkyButton(
                    text = if (isLast) "Let's Start! 🚀" else "Next",
                    accent = page.accent,
                    accentDark = page.accentDark,
                    enabled = currentDone,
                    pulse = currentDone && !isLast,
                    coach = currentDone && nextIdle
                ) {
                    AudioPlayerManager.playSoundMenuClick()
                    if (isLast) onFinish() else index++
                }
            }
            if (isLandscape) {
                // Same columns as the page body: Back under the text, Next centred under the play area.
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppDimens.Dimens32, vertical = AppDimens.Dimens16),
                    horizontalArrangement = Arrangement.spacedBy(AppDimens.Dimens24),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.weight(LEFT_WEIGHT)) { back() }
                    Box(Modifier.weight(RIGHT_WEIGHT), contentAlignment = Alignment.Center) { next() }
                }
            } else {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppDimens.Dimens24, vertical = AppDimens.Dimens16),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    back()
                    next()
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------------------------
// Page body — adaptive: side-by-side in landscape, stacked in portrait
// ---------------------------------------------------------------------------------------------

@Composable
private fun PageBody(page: OnboardingPage, done: Boolean, onDone: () -> Unit) {
    val isLandscape = androidx.compose.ui.platform.LocalConfiguration.current.orientation ==
        android.content.res.Configuration.ORIENTATION_LANDSCAPE

    // The "what's inside" page shows a full-width grid of feature chips instead of the split layout.
    if (page.kind == OnbKind.INTRO && page.points.isNotEmpty()) {
        ScaleToFit { FeatureShowcase(page, isLandscape) }
        return
    }

    val left: @Composable () -> Unit = { TextSide(page, align = TextAlign.Center) }
    val right: @Composable () -> Unit = {
        when (page.kind) {
            OnbKind.INTRO -> HeroImage(page)
            OnbKind.CHOICE -> PlayChoice(page, done, onDone)
            OnbKind.DRAG -> PlayDrag(page, done, onDone)
            OnbKind.TRACE -> PlayTrace(page, done, onDone)
        }
    }

    if (isLandscape) {
        Row(
            Modifier
                .fillMaxSize()
                .padding(horizontal = AppDimens.Dimens32),
            horizontalArrangement = Arrangement.spacedBy(AppDimens.Dimens24),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.weight(LEFT_WEIGHT)) { left() }
            Box(Modifier.weight(RIGHT_WEIGHT).fillMaxHeight()) { ScaleToFit { right() } }
        }
    } else {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = AppDimens.Dimens24),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(Modifier.weight(1f, fill = false), contentAlignment = Alignment.Center) { right() }
            Spacer(Modifier.height(AppDimens.Dimens24))
            left()
        }
    }
}

@Composable
private fun TextSide(page: OnboardingPage, align: TextAlign) {
    val start = align == TextAlign.Start
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (start) Alignment.Start else Alignment.CenterHorizontally
    ) {
        if (page.icon.isNotEmpty()) {
            Box(Modifier.align(Alignment.CenterHorizontally)) {
                SectionIcon(page.icon, page.accent, page.accentDark)
            }
            Spacer(Modifier.height(AppDimens.Dimens12))
        }
        Text(
            page.title,
            color = TITLE_COLOR,
            fontFamily = customFonts,
            fontWeight = FontWeight.Black,
            fontSize = (26f * k).sp,
            lineHeight = (32f * k).sp,
            textAlign = align
        )
        Spacer(Modifier.height(AppDimens.Dimens12))
        SpeechBubble(page.subtitle, page.accent)
        if (page.points.isNotEmpty()) {
            Spacer(Modifier.height(AppDimens.Dimens8))
            Column(verticalArrangement = Arrangement.spacedBy(AppDimens.Dimens6)) {
                page.points.forEach { line ->
                    Text(
                        line,
                        color = TITLE_COLOR,
                        fontFamily = customFonts,
                        fontWeight = FontWeight.Bold,
                        fontSize = (15f * k).sp,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color.White.copy(alpha = 0.7f))
                            .padding(horizontal = AppDimens.Dimens12, vertical = AppDimens.Dimens6)
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------------------------
// Feature showcase (the final "what's inside" page)
// ---------------------------------------------------------------------------------------------

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
private fun FeatureShowcase(page: OnboardingPage, isLandscape: Boolean) {
    val mascot = if (AppDimens.isLargeTablet) 120.dp else if (AppDimens.isTablet) 100.dp else 72.dp
    Column(
        Modifier.fillMaxWidth().padding(horizontal = AppDimens.Dimens32, vertical = AppDimens.Dimens8),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppDimens.Dimens16)
        ) {
            Bobbing {
                Image(
                    painter = painterResource(id = page.image),
                    contentDescription = page.title,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(mascot)
                )
            }
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    page.title, color = TITLE_COLOR, fontFamily = customFonts,
                    fontWeight = FontWeight.Black, fontSize = (24f * k).sp
                )
                Spacer(Modifier.height(AppDimens.Dimens8))
                SpeechBubble(page.subtitle, page.accent)
            }
        }
        Spacer(Modifier.height(AppDimens.Dimens20))
        FlowRow(
            modifier = Modifier.fillMaxWidth(if (isLandscape) 0.92f else 1f),
            horizontalArrangement = Arrangement.spacedBy(AppDimens.Dimens8, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(AppDimens.Dimens8)
        ) {
            page.points.forEach { FeatureChip(it, page.accent, page.accentDark) }
        }
    }
}

@Composable
private fun FeatureChip(text: String, accent: Color, accentDark: Color) {
    Text(
        text,
        color = accentDark,
        fontFamily = customFonts,
        fontWeight = FontWeight.Bold,
        fontSize = (15f * k).sp,
        modifier = Modifier
            .shadow(4.dp, RoundedCornerShape(50), spotColor = accent, ambientColor = accent)
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.95f))
            .border(2.dp, accent.copy(alpha = 0.55f), RoundedCornerShape(50))
            .padding(horizontal = AppDimens.Dimens16, vertical = AppDimens.Dimens12)
    )
}

// ---------------------------------------------------------------------------------------------
// Intro hero
// ---------------------------------------------------------------------------------------------

@Composable
private fun HeroImage(page: OnboardingPage) {
    val size = if (AppDimens.isLargeTablet) 300.dp else if (AppDimens.isTablet) 240.dp else 190.dp
    if (!page.heroFramed) {
        // Cut-out character: shown whole (never cropped), standing on a soft shadow.
        Box(contentAlignment = Alignment.BottomCenter) {
            Box(
                Modifier
                    .size(width = size * 0.7f, height = size * 0.1f)
                    .offset(y = size * 0.02f)
                    .clip(CircleShape)
                    .background(page.accentDark.copy(alpha = 0.18f))
            )
            Bobbing {
                Image(
                    painter = painterResource(id = page.image),
                    contentDescription = page.title,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(size * 1.08f)
                )
            }
        }
        return
    }
    Bobbing {
        ToyCircle(size = size, accent = page.accent, accentDark = page.accentDark) {
            Image(
                painter = painterResource(id = page.image),
                contentDescription = page.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(size * 0.86f).clip(CircleShape)
            )
        }
    }
}

/**
 * Reserves a row above [content] for the "Great job!" badge — or, while the child is idle on an
 * unsolved page, the coach line — so neither ever overlaps or clips the play area.
 */
@Composable
private fun WithSuccessBadge(
    done: Boolean,
    accent: Color,
    coach: Boolean = false,
    hint: String = "",
    content: @Composable () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Tall enough to hold the whole badge (no clipped text), with a gap below so it never touches the content.
        Box(Modifier.height(54.dp * k), contentAlignment = Alignment.Center) {
            SuccessBadge(done, accent)
            CoachLine(show = !done && coach && hint.isNotEmpty(), text = hint, accent = accent)
        }
        Spacer(Modifier.height(AppDimens.Dimens12))
        content()
    }
}

/** Short side-to-side wiggle for a wrong answer. */
private suspend fun Animatable<Float, AnimationVector1D>.wiggle() {
    for (x in listOf(-14f, 14f, -9f, 9f, -4f, 0f)) animateTo(x, tween(55))
}

// ---------------------------------------------------------------------------------------------
// Play: CHOICE — tap the right answer (Word Adventure picture, or Grammar fill-the-blank)
// ---------------------------------------------------------------------------------------------

@Composable
private fun PlayChoice(page: OnboardingPage, done: Boolean, onDone: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    var wrong by remember(done) { mutableIntStateOf(0) }
    val idle = rememberIdle(LocalTouchClock.current, wrong * 2 + if (done) 1 else 0, if (wrong >= 2) IDLE_WRONG_MS else IDLE_MS)
    val coach = idle && !done

    WithSuccessBadge(done, page.accent, coach = coach, hint = page.hint) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Prompt: a picture or a fill-the-blank sentence (Word missing letter / Grammar).
            if (page.promptEmoji.isNotEmpty()) {
                val s = if (AppDimens.isLargeTablet) 118.dp else if (AppDimens.isTablet) 98.dp else 74.dp
                ToyCircle(size = s, accent = page.accent, accentDark = page.accentDark) {
                    Text(page.promptEmoji, fontSize = (s.value * 0.5f).sp)
                }
            } else {
                PromptCard(page, done)
            }
            Spacer(Modifier.height(AppDimens.Dimens16))
            Row(
                // Room under the pills for the coach hand, so it's measured and never clipped.
                Modifier.padding(bottom = 30.dp * k),
                horizontalArrangement = Arrangement.spacedBy(AppDimens.Dimens12),
                verticalAlignment = Alignment.Top
            ) {
                page.choiceOptions.forEach { opt ->
                    val isAnswer = opt == page.choiceAnswer
                    ChoiceTile(
                        text = opt,
                        page = page,
                        dimmed = done && !isAnswer,
                        correct = done && isAnswer,
                        pulse = coach && isAnswer,
                        showHand = coach && isAnswer,
                        enabled = !done
                    ) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        if (isAnswer) onDone() else { wrong++; AudioPlayerManager.playSoundWrongAnswer() }
                        isAnswer
                    }
                }
            }
        }
    }
}

/** The grammar sentence card; the blank fills in with the answer (in colour) once solved. */
@Composable
private fun PromptCard(page: OnboardingPage, done: Boolean) {
    val shown = if (done) page.promptText.replace("___", page.choiceAnswer) else page.promptText
    Box(
        Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.95f))
            .border(3.dp, page.accent, RoundedCornerShape(20.dp))
            .padding(horizontal = AppDimens.Dimens24, vertical = AppDimens.Dimens16)
    ) {
        Text(
            shown,
            color = if (done) page.accentDark else TITLE_COLOR,
            fontFamily = customFonts,
            fontWeight = FontWeight.Black,
            fontSize = (22f * k).sp
        )
    }
}

/** One answer pill. [onTap] returns true when it was correct; a wrong tap wiggles. */
@Composable
private fun ChoiceTile(
    text: String,
    page: OnboardingPage,
    dimmed: Boolean,
    correct: Boolean,
    pulse: Boolean,
    showHand: Boolean,
    enabled: Boolean,
    onTap: () -> Boolean
) {
    val scope = rememberCoroutineScope()
    val shake = remember { Animatable(0f) }
    val beat by rememberInfiniteTransition(label = "choice").animateFloat(
        1f, 1.08f, infiniteRepeatable(tween(650, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "choicePulse"
    )
    val top = if (correct) Color(0xFF43A047) else page.accent
    val base = if (correct) Color(0xFF2E7D32) else page.accentDark
    // Depth comes from a drop shadow (stays inside bounds), not a separate 3D base that could clip.
    Box(contentAlignment = Alignment.BottomCenter) {
    Box(
        Modifier
            .graphicsLayer {
                translationX = shake.value * density
                val sc = if (pulse) beat else 1f
                scaleX = sc; scaleY = sc
                alpha = if (dimmed) 0.5f else 1f
            }
            .shadow(6.dp, RoundedCornerShape(50), spotColor = base, ambientColor = base)
            .clip(RoundedCornerShape(50))
            .background(Brush.horizontalGradient(listOf(top, base)))
            .border(3.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(50))
            .pointerTap(enabled) { if (!onTap()) scope.launch { shake.wiggle() } }
            .padding(horizontal = AppDimens.Dimens24, vertical = AppDimens.Dimens14),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(AppDimens.Dimens6)) {
            if (correct) Icon(Icons.Rounded.Check, null, Modifier.size(20.dp * k), tint = Color.White)
            Text(text, color = Color.White, fontFamily = customFonts, fontWeight = FontWeight.Black, fontSize = (18f * k).sp)
        }
    }
    if (showHand) TapHand(page.accentDark, Modifier.pinAt(0.5f, 0.3f))
    }
}


// ---------------------------------------------------------------------------------------------
// Play: DRAG — pick the right letter and drop it in its box
// ---------------------------------------------------------------------------------------------

@Composable
private fun PlayDrag(page: OnboardingPage, done: Boolean, onDone: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val chip = if (AppDimens.isLargeTablet) 86.dp else if (AppDimens.isTablet) 72.dp else 56.dp
    var targetRect by remember { mutableStateOf<Rect?>(null) }
    val anchors = remember { Anchors() }
    var dragging by remember { mutableStateOf(false) }
    var wrong by remember(done) { mutableIntStateOf(0) }
    val idle = rememberIdle(LocalTouchClock.current, wrong * 2 + if (done) 1 else 0, if (wrong >= 2) IDLE_WRONG_MS else IDLE_MS)
    val coach = idle && !done && !dragging

    Box(Modifier.onGloballyPositioned { anchors.root = it }) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            WithSuccessBadge(done, page.accent, coach = coach, hint = page.hint) {
                DropTarget(
                    letter = page.letter, size = chip, accent = page.accent, accentDark = page.accentDark, filled = done,
                    modifier = Modifier.onGloballyPositioned { targetRect = it.boundsInRoot(); anchors.items[TARGET] = it }
                )
            }
            Spacer(Modifier.height(AppDimens.Dimens8))
            Row(horizontalArrangement = Arrangement.spacedBy(AppDimens.Dimens16)) {
                page.dragOptions.forEach { letter ->
                    val isTarget = letter == page.letter
                    DraggableTile(
                        letter = letter,
                        size = chip,
                        page = page,
                        hidden = done && isTarget,
                        dimmed = done && !isTarget,
                        enabled = !done,
                        onPlaced = { anchors.items[letter] = it },
                        onDragState = { dragging = it }
                    ) { rect ->
                        val t = targetRect
                        val onTarget = t != null && rect.overlaps(t)
                        when {
                            onTarget && isTarget -> { haptic.performHapticFeedback(HapticFeedbackType.LongPress); onDone(); true }
                            onTarget -> { wrong++; AudioPlayerManager.playSoundWrongAnswer(); false }
                            else -> false
                        }
                    }
                }
            }
        }

        // Coach: a ghost of the right letter and a hand glide into the box, on repeat.
        if (coach) GuideHand(anchors, from = page.letter, to = TARGET, letter = page.letter, size = chip, page = page)
    }
}

private const val TARGET = "__target"

/** A letter tile the child can drag. [onDrop] gets the tile's bounds and returns true when it was accepted. */
@Composable
private fun DraggableTile(
    letter: String,
    size: Dp,
    page: OnboardingPage,
    hidden: Boolean,
    dimmed: Boolean,
    enabled: Boolean,
    onPlaced: (LayoutCoordinates) -> Unit,
    onDragState: (Boolean) -> Unit,
    onDrop: (Rect) -> Boolean
) {
    val scope = rememberCoroutineScope()
    val shake = remember { Animatable(0f) }
    var drag by remember { mutableStateOf(Offset.Zero) }
    var rect by remember { mutableStateOf<Rect?>(null) }
    var home by remember { mutableStateOf<Rect?>(null) }
    Box(Modifier.size(size).onGloballyPositioned { if (drag == Offset.Zero) { home = it.boundsInRoot(); onPlaced(it) } }) {
        if (!hidden) {
            LetterTile(
                letter = letter, size = size, accent = page.accent, accentDark = page.accentDark,
                modifier = Modifier
                    .zIndex(if (drag != Offset.Zero) 1f else 0f)
                    .offset { IntOffset((drag.x + shake.value * density).roundToInt(), drag.y.roundToInt()) }
                    .graphicsLayer { alpha = if (dimmed) 0.4f else 1f }
                    .onGloballyPositioned { rect = it.boundsInRoot() }
                    .pointerInput(enabled) {
                        if (!enabled) return@pointerInput
                        detectDragGestures(
                            onDragStart = { onDragState(true) },
                            onDragEnd = {
                                onDragState(false)
                                val r = rect
                                val accepted = r != null && onDrop(r)
                                if (!accepted) {
                                    val wasOnBox = drag != Offset.Zero
                                    drag = Offset.Zero
                                    if (wasOnBox) scope.launch { shake.wiggle() }
                                }
                            },
                            onDragCancel = { onDragState(false); drag = Offset.Zero }
                        ) { change, delta -> change.consume(); drag += delta }
                    }
            )
        }
    }
}

/**
 * Coach for the drag page: a translucent copy of the right tile with a hand on it glides from
 * the tile into the box, on repeat. Positions come from [Anchors] in the play area's own
 * (unscaled) coordinates, so it lines up even when the page is scaled to fit.
 */
@Composable
private fun GuideHand(anchors: Anchors, from: String, to: String, letter: String, size: Dp, page: OnboardingPage) {
    val t by rememberInfiniteTransition(label = "hand").animateFloat(
        0f, 1f, infiniteRepeatable(tween(1600, delayMillis = 300, easing = FastOutSlowInEasing), RepeatMode.Restart), label = "handT"
    )
    Box(
        Modifier
            .zIndex(2f)
            .offset {
                val a = anchors.rect(from); val b = anchors.rect(to)
                if (a == null || b == null) IntOffset(-100_000, 0)
                else {
                    val p = a.center + (b.center - a.center) * t
                    IntOffset((p.x - size.toPx() / 2f).roundToInt(), (p.y - size.toPx() / 2f).roundToInt())
                }
            }
            .graphicsLayer { alpha = if (t > 0.92f) (1f - t) / 0.08f else 1f }
    ) {
        LetterTile(letter, size, page.accent, page.accentDark, Modifier.graphicsLayer { alpha = 0.45f })
        Text(
            "👆", fontSize = (30f * k).sp,
            modifier = Modifier.align(Alignment.BottomCenter).pinAt(0.4f, 0.35f)
        )
    }
}

@Composable
private fun LetterTile(letter: String, size: Dp, accent: Color, accentDark: Color, modifier: Modifier = Modifier) {
    // Depth is a drop shadow (stays within bounds), not a separate base that drew outside and clipped.
    Box(
        modifier
            .size(size)
            .shadow(6.dp, RoundedCornerShape(22.dp), spotColor = accentDark, ambientColor = accentDark)
            .clip(RoundedCornerShape(22.dp))
            .background(Brush.verticalGradient(listOf(accent, accentDark)))
            .border(3.dp, Color.White.copy(alpha = 0.7f), RoundedCornerShape(22.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(letter, color = Color.White, fontFamily = customFonts, fontWeight = FontWeight.Black, fontSize = (size.value * 0.5f).sp)
    }
}

@Composable
private fun DropTarget(letter: String, size: Dp, accent: Color, accentDark: Color, filled: Boolean, modifier: Modifier = Modifier) {
    val boxSize = size * 1.15f
    Box(
        modifier
            .size(boxSize)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = if (filled) 0.95f else 0.55f))
            .drawBehind {
                drawRoundRect(
                    color = accent.copy(alpha = if (filled) 1f else 0.7f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(24.dp.toPx()),
                    style = Stroke(
                        width = 3.dp.toPx(),
                        pathEffect = if (filled) null else androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(16f, 14f))
                    )
                )
            },
        contentAlignment = Alignment.Center
    ) {
        // A faint letter shows which tile belongs here; it turns solid once placed.
        Text(
            letter,
            color = if (filled) accentDark else accent.copy(alpha = 0.28f),
            fontFamily = customFonts,
            fontWeight = FontWeight.Black,
            fontSize = (size.value * 0.6f).sp
        )
    }
}

// ---------------------------------------------------------------------------------------------
// Play: TRACE — the real letter strokes from Alphabet Tracing, one line at a time
// ---------------------------------------------------------------------------------------------

@Composable
private fun PlayTrace(page: OnboardingPage, done: Boolean, onDone: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val canvasSize = if (AppDimens.isLargeTablet) 290.dp else if (AppDimens.isTablet) 244.dp else 178.dp
    val strokes = remember(page.letter) { getStrokesForLetter(page.letter.first(), LetterMode.UPPERCASE) }
    var guides by remember { mutableStateOf<List<List<Offset>>>(emptyList()) }
    var strokeIndex by remember(done) { mutableIntStateOf(if (done) strokes.size else 0) }
    var progress by remember(done) { mutableIntStateOf(0) }
    var onStroke by remember(done) { mutableStateOf(false) }
    val drawn = remember(done) { mutableStateListOf<Offset>() }
    val handT by rememberInfiniteTransition(label = "traceHand").animateFloat(
        0f, 1f, infiniteRepeatable(tween(1800, delayMillis = 400, easing = LinearEasing), RepeatMode.Restart), label = "traceHandT"
    )

    val idle = rememberIdle(LocalTouchClock.current, strokeIndex * 2 + if (done) 1 else 0)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        WithSuccessBadge(done, page.accent, coach = idle && !onStroke, hint = page.hint) {
          Box {
            Canvas(
                Modifier
                    .size(canvasSize)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White.copy(alpha = 0.6f))
                    .onGloballyPositioned { c ->
                        val w = c.size.width.toFloat()
                        if (guides.isEmpty()) guides = strokes.map { sampleStroke(it, Rect(0f, 0f, w, w), w * 0.012f) }
                    }
                    .pointerInput(done, guides) {
                        if (done || guides.isEmpty()) return@pointerInput
                        val tolerance = size.width * 0.14f
                        detectDragGestures(
                            onDragStart = { p ->
                                val g = guides.getOrNull(strokeIndex) ?: return@detectDragGestures
                                if (distance(p, g.first()) <= tolerance) {
                                    onStroke = true; progress = 0; drawn.clear(); drawn.add(g.first())
                                }
                            },
                            onDragEnd = { if (onStroke) { onStroke = false; progress = 0; drawn.clear() } },
                            onDragCancel = { onStroke = false; progress = 0; drawn.clear() }
                        ) { change, _ ->
                            change.consume()
                            val g = guides.getOrNull(strokeIndex) ?: return@detectDragGestures
                            if (!onStroke) return@detectDragGestures
                            // Same rule as Alphabet Tracing: only move forward along the line, snapping to it.
                            val end = (progress + 20).coerceAtMost(g.lastIndex)
                            var best = -1; var bestD = Float.MAX_VALUE
                            for (i in progress + 1..end) {
                                val d = distance(change.position, g[i]); if (d < bestD) { bestD = d; best = i }
                            }
                            if (best == -1 || bestD > tolerance) return@detectDragGestures
                            drawn.addAll(g.subList(progress + 1, best + 1)); progress = best
                            if (best == g.lastIndex) {
                                onStroke = false; progress = 0; drawn.clear()
                                strokeIndex++
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                if (strokeIndex >= guides.size) onDone() else AudioPlayerManager.playSoundDragItem()
                            }
                        }
                    }
            ) {
                if (guides.isEmpty()) return@Canvas
                val w = size.width * 0.1f
                fun path(points: List<Offset>) = Path().apply {
                    moveTo(points.first().x, points.first().y); points.drop(1).forEach { lineTo(it.x, it.y) }
                }
                val round = Stroke(w, cap = StrokeCap.Round, join = androidx.compose.ui.graphics.StrokeJoin.Round)
                // Whole letter as a soft track
                guides.forEach { drawPath(path(it), page.accent.copy(alpha = 0.18f), style = round) }
                // Finished lines
                guides.take(strokeIndex).forEach { drawPath(path(it), page.accent, style = round) }
                val g = guides.getOrNull(strokeIndex) ?: return@Canvas
                // Current line: dashed centre + arrows pointing the way
                drawPath(
                    path(g), Color.White,
                    style = Stroke(w * 0.14f, cap = StrokeCap.Round, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(w * 0.3f, w * 0.3f)))
                )
                val step = (g.size / 3).coerceAtLeast(2)
                var i = step
                while (i < g.lastIndex) { drawChevron(g[i - 1], g[i], w * 0.42f, page.accentDark); i += step }
                // Child's ink on the current line
                if (drawn.size > 1) drawPath(path(drawn), page.accentDark, style = round)
                // Numbered start dot
                drawCircle(Color(0xFF43A047), w * 0.62f, g.first())
                drawCircle(Color.White, w * 0.62f, g.first(), style = Stroke(w * 0.12f))
                // Demo hand position (drawn as a dot trail; the emoji sits in an overlay below)
                if (!onStroke) {
                    val p = g[(handT * g.lastIndex).roundToInt().coerceIn(0, g.lastIndex)]
                    drawCircle(page.accentDark.copy(alpha = 0.35f), w * 0.45f, p)
                }
            }
            // Number on the start dot + demo hand, as text over the canvas.
            val g = guides.getOrNull(strokeIndex)
            if (!done && g != null) {
                val d = androidx.compose.ui.platform.LocalDensity.current
                val dot = g.first()
                // Same size as the drawn dot (radius = 0.062 of the canvas), number centred inside it.
                val dotSize = canvasSize * 0.124f
                Box(
                    Modifier
                        .align(Alignment.TopStart)
                        .size(dotSize)
                        .offset { val half = dotSize.toPx() / 2f; IntOffset((dot.x - half).roundToInt(), (dot.y - half).roundToInt()) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "${strokeIndex + 1}",
                        color = Color.White, fontFamily = customFonts, fontWeight = FontWeight.Black,
                        fontSize = (dotSize.value * 0.55f).sp, lineHeight = (dotSize.value * 0.6f).sp
                    )
                }
                if (!onStroke) {
                    val p = g[(handT * g.lastIndex).roundToInt().coerceIn(0, g.lastIndex)]
                    Text(
                        "👆", fontSize = (26f * k).sp,
                        modifier = Modifier.align(Alignment.TopStart).offset { IntOffset((p.x - with(d) { (9.dp * k).toPx() }).roundToInt(), (p.y - with(d) { (2.dp * k).toPx() }).roundToInt()) }
                    )
                }
            }
          }
        }
        // Room for the demo hand when it reaches the bottom of the letter, so it never covers this line.
        Spacer(Modifier.height(AppDimens.Dimens20))
        Text(
            if (done) "All ${strokes.size} lines done!" else "Line ${strokeIndex + 1} of ${strokes.size}",
            color = page.accentDark, fontFamily = customFonts, fontWeight = FontWeight.Black, fontSize = (15f * k).sp
        )
    }
}

/** Small arrowhead pointing from [a] towards [b], centred on [b]. */
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawChevron(a: Offset, b: Offset, size: Float, color: Color) {
    val ang = kotlin.math.atan2(b.y - a.y, b.x - a.x)
    val tip = Offset(b.x + cos(ang) * size * 0.5f, b.y + sin(ang) * size * 0.5f)
    val back = Offset(b.x - cos(ang) * size * 0.5f, b.y - sin(ang) * size * 0.5f)
    val nx = -sin(ang) * size * 0.55f; val ny = cos(ang) * size * 0.55f
    drawPath(
        Path().apply { moveTo(tip.x, tip.y); lineTo(back.x + nx, back.y + ny); lineTo(back.x - nx, back.y - ny); close() },
        color
    )
}

// ---------------------------------------------------------------------------------------------
// Playful UI kit
// ---------------------------------------------------------------------------------------------

/** A rounded emoji badge shown above a section title. */
@Composable
private fun SectionIcon(emoji: String, accent: Color, accentDark: Color) {
    val s = 50.dp * k
    Box(Modifier.size(s + 5.dp)) {
        Box(Modifier.size(s).offset(y = 4.dp).clip(RoundedCornerShape(50)).background(accentDark.copy(alpha = 0.5f)))
        Box(
            Modifier.size(s).clip(RoundedCornerShape(50)).background(Color.White.copy(alpha = 0.95f)).border(3.dp, accent, RoundedCornerShape(50)),
            contentAlignment = Alignment.Center
        ) {
            Text(emoji, fontSize = (24f * k).sp)
        }
    }
}

/** Scales its content down (never up) so it always fits the available height — no page ever clips. */
@Composable
private fun ScaleToFit(content: @Composable () -> Unit) {
    androidx.compose.foundation.layout.BoxWithConstraints(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        // Leave ~10% headroom so 3D bases / shadows drawn just outside layout bounds never clip.
        val maxHpx = with(androidx.compose.ui.platform.LocalDensity.current) { maxHeight.toPx() } * 0.9f
        var contentH by remember { mutableStateOf(0) }
        val scale = if (contentH <= 0 || maxHpx.isInfinite()) 1f else minOf(1f, maxHpx / contentH)
        Box(
            Modifier
                // Measure at the content's true height (ignore the band's max height), so the scale
                // is computed from real content and nothing is clipped before it's scaled to fit.
                .wrapContentHeight(align = Alignment.CenterVertically, unbounded = true)
                .onGloballyPositioned { contentH = it.size.height }
                .graphicsLayer { scaleX = scale; scaleY = scale }
        ) { content() }
    }
}

@Composable
private fun Bobbing(content: @Composable () -> Unit) {
    val bob by rememberInfiniteTransition(label = "bob").animateFloat(
        -8f, 8f, infiniteRepeatable(tween(2200, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "bobY"
    )
    Box(Modifier.graphicsLayer { translationY = bob }, contentAlignment = Alignment.Center) { content() }
}

/** White circle with a thick colored border and a 3D base, holding [content]. */
@Composable
private fun ToyCircle(size: Dp, accent: Color, accentDark: Color, content: @Composable () -> Unit) {
    Box(Modifier.size(size + 8.dp), contentAlignment = Alignment.Center) {
        Box(Modifier.size(size).offset(y = 7.dp).clip(CircleShape).background(accentDark.copy(alpha = 0.55f)))
        Box(
            Modifier.size(size).clip(CircleShape).background(Color.White.copy(alpha = 0.95f)).border(5.dp, accent, CircleShape),
            contentAlignment = Alignment.Center
        ) { content() }
    }
}

@Composable
private fun ChunkyButton(
    text: String,
    accent: Color,
    accentDark: Color,
    enabled: Boolean,
    pulse: Boolean,
    coach: Boolean = false,
    onClick: () -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val press by animateFloatAsState(if (pressed && enabled) 4f else 0f, label = "btnPress")
    val nudge by rememberInfiniteTransition(label = "btnNudge").animateFloat(
        1f, 1.08f, infiniteRepeatable(tween(560, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "btnNudgeScale"
    )
    val scale = if (pulse) (if (coach) 1f + (nudge - 1f) * 1.5f else nudge) else 1f
    val h = AppDimens.Dimens60
    val top = if (enabled) accent else Color(0xFFB9B4CC)
    val base = if (enabled) accentDark else Color(0xFF8E89A3)

    Box(
        Modifier
            .width(220.dp * k)
            .height(h + 6.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
    ) {
        Box(Modifier.fillMaxWidth().height(h).align(Alignment.BottomCenter).clip(RoundedCornerShape(50)).background(base))
        Box(
            Modifier
                .fillMaxWidth()
                .height(h)
                .align(Alignment.TopCenter)
                .offset { IntOffset(0, press.roundToInt()) }
                .clip(RoundedCornerShape(50))
                .background(Brush.horizontalGradient(listOf(top, base)))
                .border(3.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(50))
                .clickable(interaction, indication = null, enabled = enabled) { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(text, color = Color.White, fontFamily = customFonts, fontWeight = FontWeight.Black, fontSize = (18f * k).sp)
        }
        // Coach: a hand pointing at Next once the page is done and the child has gone idle.
        if (coach) PointHand(Modifier.align(Alignment.CenterStart).pinAt(1.12f, 0.5f))
    }
}

@Composable
private fun GhostButton(text: String, accent: Color, onClick: () -> Unit) {
    Text(
        text,
        color = accent,
        fontFamily = customFonts,
        fontWeight = FontWeight.Bold,
        fontSize = (15f * k).sp,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.65f))
            .clickable { onClick() }
            .padding(horizontal = AppDimens.Dimens20, vertical = AppDimens.Dimens12)
    )
}

/** Step pills: current = gold pill with number + label, completed = green check, upcoming = numbered circle. */
@Composable
private fun StepPills(pages: List<OnboardingPage>, index: Int, done: List<Boolean>) {
    Row(horizontalArrangement = Arrangement.spacedBy(AppDimens.Dimens8), verticalAlignment = Alignment.CenterVertically) {
        pages.forEachIndexed { i, page ->
            val isCurrent = i == index
            val isDone = i < index
            if (isCurrent) {
                val dot = 26.dp * k
                Row(
                    Modifier
                        .drawBehind {
                            drawRoundRect(Color(0xFFFF8F00).copy(alpha = 0.35f), Offset(0f, 3.dp.toPx()), size, androidx.compose.ui.geometry.CornerRadius(size.height / 2f))
                        }
                        .background(Brush.verticalGradient(listOf(Color(0xFFFFCA38), Color(0xFFFFB300))), CircleShape)
                        .border(3.dp, Color(0xFFFF8F00), CircleShape)
                        .padding(start = 4.dp * k, end = 14.dp * k, top = 4.dp * k, bottom = 4.dp * k),
                    horizontalArrangement = Arrangement.spacedBy(AppDimens.Dimens6),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.size(dot).background(Color.White, CircleShape), contentAlignment = Alignment.Center) {
                        Text("${i + 1}", fontFamily = customFonts, fontWeight = FontWeight.Black, fontSize = (dot.value * 0.5f).sp, color = Color(0xFFB35A00))
                    }
                    Text(page.stepLabel, fontFamily = customFonts, fontWeight = FontWeight.Black, fontSize = (15f * k).sp, color = Color(0xFF5A3A00), maxLines = 1)
                }
            } else {
                val s = 24.dp * k
                Box(
                    Modifier
                        .size(s)
                        .background(if (isDone) Color(0xFF43A047) else Color.White.copy(alpha = 0.7f), CircleShape)
                        .border(2.dp, if (isDone) Color(0xFF2E7D32) else Color(0xFFB9B4CC), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (isDone) Icon(Icons.Rounded.Check, null, Modifier.size(s * 0.6f), tint = Color.White)
                    else Text("${i + 1}", fontFamily = customFonts, fontWeight = FontWeight.Black, fontSize = (s.value * 0.5f).sp, color = Color(0xFF8E89A3))
                }
            }
        }
    }
}

/** Speech bubble with a 3D edge and a downward tail. */
@Composable
private fun SpeechBubble(text: String, accent: Color) {
    val shape = remember { SpeechBubbleShape() }
    Box(
        Modifier
            .drawBehind {
                val outline = shape.createOutline(size, layoutDirection, this) as Outline.Generic
                translate(top = 4.dp.toPx()) { drawPath(outline.path, accent.copy(alpha = 0.45f)) }
            }
            .background(Color.White.copy(alpha = 0.95f), shape)
            .border(3.dp, accent, shape)
            .padding(start = AppDimens.Dimens16, end = AppDimens.Dimens16, top = AppDimens.Dimens12, bottom = AppDimens.Dimens12 + 16.dp)
    ) {
        Text(text, color = TITLE_COLOR.copy(alpha = 0.85f), fontFamily = customFonts, fontWeight = FontWeight.Bold, fontSize = (17f * k).sp)
    }
}

private class SpeechBubbleShape : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val d = density.density
        val tailH = 16f * d
        val tailW = 26f * d
        val bodyH = size.height - tailH
        val r = min(20f * d, bodyH / 2f)
        val left = 46f * d
        val right = left + tailW
        val path = Path().apply {
            moveTo(r, 0f)
            lineTo(size.width - r, 0f)
            arcTo(Rect(size.width - 2 * r, 0f, size.width, 2 * r), -90f, 90f, false)
            lineTo(size.width, bodyH - r)
            arcTo(Rect(size.width - 2 * r, bodyH - 2 * r, size.width, bodyH), 0f, 90f, false)
            lineTo(right, bodyH)
            lineTo(left + tailW * 0.25f, size.height)
            lineTo(left, bodyH)
            lineTo(r, bodyH)
            arcTo(Rect(0f, bodyH - 2 * r, 2 * r, bodyH), 90f, 90f, false)
            lineTo(0f, r)
            arcTo(Rect(0f, 0f, 2 * r, 2 * r), 180f, 90f, false)
            close()
        }
        return Outline.Generic(path)
    }
}

/** A "Great job!" pill that pops in when a play page is solved. */
@Composable
private fun SuccessBadge(show: Boolean, accent: Color, modifier: Modifier = Modifier) {
    AnimatedVisibility(
        visible = show,
        enter = scaleIn(initialScale = 0.5f) + fadeIn(),
        exit = scaleOut(targetScale = 0.5f) + fadeOut(),
        modifier = modifier
    ) {
        Row(
            Modifier
                .background(Color.White.copy(alpha = 0.95f), CircleShape)
                .border(3.dp, accent, CircleShape)
                .padding(horizontal = AppDimens.Dimens16, vertical = AppDimens.Dimens8),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppDimens.Dimens6)
        ) {
            Text("⭐", fontSize = (18f * k).sp)
            Text("Great job!", color = accent, fontFamily = customFonts, fontWeight = FontWeight.Black, fontSize = (16f * k).sp)
        }
    }
}

// ---------------------------------------------------------------------------------------------
// Idle coach — nobody gets stuck. 3 s without a touch (0.8 s after two wrong tries) shows the
// page's instruction and an animated hand doing the exact move; solved pages point at Next.
// ---------------------------------------------------------------------------------------------

private const val IDLE_MS = 3000L
private const val IDLE_WRONG_MS = 800L

/** Time of the last touch anywhere on the screen. Written by a root pointer observer, read by timers only. */
private class TouchClock { var last = 0L }

private val LocalTouchClock = staticCompositionLocalOf { TouchClock() }

/** True once nothing was touched for [afterMs] since the last touch or the last change of [reset]. */
@Composable
private fun rememberIdle(clock: TouchClock, reset: Any, afterMs: Long = IDLE_MS): Boolean {
    var idle by remember { mutableStateOf(false) }
    LaunchedEffect(reset, afterMs) {
        idle = false
        val start = SystemClock.uptimeMillis()
        while (true) {
            val v = SystemClock.uptimeMillis() - maxOf(clock.last, start) >= afterMs
            if (v != idle) idle = v
            delay(200)
        }
    }
    return idle
}

/** Layout positions of named elements, read lazily in the play area's own (unscaled) coordinates. */
private class Anchors {
    var root: LayoutCoordinates? = null
    val items = HashMap<String, LayoutCoordinates>()
    fun rect(key: String): Rect? {
        val r = root ?: return null
        val t = items[key] ?: return null
        return if (r.isAttached && t.isAttached) r.localBoundingBoxOf(t, clipBounds = false) else null
    }
}

/**
 * Lays the child out at zero size, so it never grows or shifts its parent, and places it so the
 * point ([fx], [fy]) — fractions of its own size — sits exactly here.
 */
private fun Modifier.pinAt(fx: Float, fy: Float): Modifier = layout { measurable, _ ->
    val p = measurable.measure(Constraints())
    layout(0, 0) { p.placeRelative((-p.width * fx).roundToInt(), (-p.height * fy).roundToInt()) }
}

/** Pops the coach line in and out. */
@Composable
private fun CoachLine(show: Boolean, text: String, accent: Color) {
    AnimatedVisibility(
        visible = show,
        enter = scaleIn(initialScale = 0.6f) + fadeIn(),
        exit = scaleOut(targetScale = 0.6f) + fadeOut()
    ) { CoachPill(text, accent) }
}

/** The coach line — bounces gently so it catches the eye. */
@Composable
private fun CoachPill(text: String, accent: Color) {
    val bounce by rememberInfiniteTransition(label = "coach").animateFloat(
        -3f, 3f, infiniteRepeatable(tween(600, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "coachY"
    )
    Row(
        Modifier
            .graphicsLayer { translationY = bounce.dp.toPx() }
            .shadow(4.dp, CircleShape, spotColor = accent, ambientColor = accent)
            .background(accent, CircleShape)
            .border(2.dp, Color.White.copy(alpha = 0.8f), CircleShape)
            .padding(horizontal = AppDimens.Dimens16, vertical = AppDimens.Dimens8),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppDimens.Dimens6)
    ) {
        Text("👆", fontSize = (16f * k).sp)
        Text(text, color = Color.White, fontFamily = customFonts, fontWeight = FontWeight.Black, fontSize = (15f * k).sp)
    }
}

/** A hand that taps: fingertip at the top, pressing down with an expanding ripple each beat. */
@Composable
private fun TapHand(color: Color, modifier: Modifier = Modifier) {
    val ph by rememberInfiniteTransition(label = "tap").animateFloat(
        0f, 1f, infiniteRepeatable(tween(1200, easing = LinearEasing), RepeatMode.Restart), label = "tapPh"
    )
    val press = ph in 0.30f..0.55f
    Text(
        "👆",
        fontSize = (32f * k).sp,
        modifier = modifier
            .drawBehind {
                drawCircle(
                    color.copy(alpha = (0.8f - ph).coerceAtLeast(0f)),
                    radius = ((7f + 20f * ph) * k).dp.toPx(),
                    center = Offset(size.width * 0.45f, 2.dp.toPx()),
                    style = Stroke(3.dp.toPx())
                )
            }
            .graphicsLayer {
                val s = if (press) 0.86f else 1f
                scaleX = s; scaleY = s
                transformOrigin = TransformOrigin(0.5f, 0f)
                translationY = if (press) 4.dp.toPx() else 0f
            }
    )
}

/** A hand pointing right at Next, nudging toward it. */
@Composable
private fun PointHand(modifier: Modifier = Modifier) {
    val x by rememberInfiniteTransition(label = "point").animateFloat(
        -6f, 6f, infiniteRepeatable(tween(450, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "pointX"
    )
    Text("👉", fontSize = (34f * k).sp, modifier = modifier.graphicsLayer { translationX = x.dp.toPx() })
}

/** Tap-only pointer input that no-ops when disabled. */
private fun Modifier.pointerTap(enabled: Boolean, onTap: () -> Unit): Modifier =
    if (!enabled) this else this.pointerInput(Unit) { detectTapGestures { onTap() } }

/** Drifting background shapes tinted with the page accent. */
@Composable
private fun FloatingDeco(deco: OnboardingDeco, color: Color) {
    val phase by rememberInfiniteTransition(label = "deco").animateFloat(
        0f, (2 * PI).toFloat(),
        infiniteRepeatable(tween(9000, easing = LinearEasing), RepeatMode.Restart), label = "decoPhase"
    )
    Canvas(Modifier.fillMaxSize()) {
        val minDim = min(size.width, size.height)
        val amp = size.height * 0.03f
        decoPositions.forEach { s ->
            val cx = s.x * size.width
            val cy = s.y * size.height + sin((phase * s.speed + s.phase).toDouble()).toFloat() * amp
            val r = s.size * minDim
            when (deco) {
                OnboardingDeco.STARS -> drawDecoStar(cx, cy, r, color)
                OnboardingDeco.BUBBLES -> {
                    drawCircle(color.copy(alpha = 0.10f), r, Offset(cx, cy))
                    drawCircle(color.copy(alpha = 0.18f), r, Offset(cx, cy), style = Stroke(2.dp.toPx()))
                }
                OnboardingDeco.SPARKLES -> drawDecoSparkle(cx, cy, r, color)
            }
        }
    }
}

private data class DecoPos(val x: Float, val y: Float, val size: Float, val speed: Float, val phase: Float)

private val decoPositions = listOf(
    DecoPos(0.06f, 0.14f, 0.045f, 1.00f, 0.0f),
    DecoPos(0.16f, 0.72f, 0.034f, 0.75f, 1.2f),
    DecoPos(0.30f, 0.22f, 0.040f, 1.25f, 0.7f),
    DecoPos(0.52f, 0.84f, 0.050f, 0.90f, 2.1f),
    DecoPos(0.74f, 0.16f, 0.030f, 1.10f, 0.5f),
    DecoPos(0.88f, 0.50f, 0.044f, 0.65f, 1.8f),
    DecoPos(0.94f, 0.80f, 0.036f, 0.95f, 0.3f),
    DecoPos(0.40f, 0.52f, 0.026f, 1.35f, 3.0f),
    DecoPos(0.66f, 0.40f, 0.030f, 0.80f, 1.6f),
    DecoPos(0.10f, 0.46f, 0.028f, 1.05f, 2.5f)
)

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawDecoStar(cx: Float, cy: Float, outer: Float, color: Color) {
    val inner = outer * 0.42f
    val path = Path()
    for (i in 0 until 10) {
        val angle = i * PI / 5.0 - PI / 2.0
        val r = if (i % 2 == 0) outer else inner
        val x = cx + r * cos(angle).toFloat()
        val y = cy + r * sin(angle).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    drawPath(path, color = color.copy(alpha = 0.16f))
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawDecoSparkle(cx: Float, cy: Float, outer: Float, color: Color) {
    val inner = outer * 0.25f
    val path = Path()
    for (i in 0 until 8) {
        val angle = i * PI / 4.0 - PI / 4.0
        val r = if (i % 2 == 0) outer else inner
        val x = cx + r * cos(angle).toFloat()
        val y = cy + r * sin(angle).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    drawPath(path, color = color.copy(alpha = 0.16f))
}
