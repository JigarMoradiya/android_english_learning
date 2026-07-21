package com.example.myapplication.main.age_group.phonics.reading_ladder

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.draw.shadow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.progress.PhonicsSessionRecorder
import com.example.myapplication.main.age_group.from_3_to_5.phonics_reading.FREE_PHONICS_LEVELS
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.kidsGlassCard
import com.example.myapplication.main.common.sheets.LocalAccessSheetViewModel
import com.example.myapplication.ui.theme.AppDimens.Dimens1
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens14
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens40
import com.example.myapplication.utilities.AudioPhonicsManager
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.extensions.scaled
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

// ReadingLadderPage.kt
// "The Reading Ladder" — the capstone decoding strategy page.
// A kid meets a NEW word: which rule do I try first? Five tappable rungs,
// each with a listen-able example word and a link into its full level.
// Keep identical to iOS ReadingLadderView.

// ── Data ─────────────────────────────────────────────────────────────────────

private data class LadderRung(
    val step: Int,
    val emoji: String,
    val question: String,      // **marked** words get bold + tinted
    val hint: String,          // what to do on YES — **marked** too
    val examples: List<String>, // tappable word chips (audio file = word)
    val color: Color,
    val levelNumber: Int,
    val levelLabel: String,    // "L12 · Magic E"
    val route: String,
)

private val ladderRungs = listOf(
    LadderRung(1, "✨",
        "Does it end with **Magic E**?",
        "YES → the vowel says its **name**, the e stays silent!",
        listOf("cake"), Color(0xFF880E4F),
        12, "L12 · Magic E", RouteNavigation.MagicEIntro.route),
    LadderRung(2, "🤝",
        "Are **two vowels** walking together?",
        "YES → the **first one talks**! (ai · ee · oa)",
        listOf("rain"), Color(0xFFEF6C00),
        13, "L13 · Vowel Teams", RouteNavigation.VowelTeamsIntro.route),
    LadderRung(3, "🌀",
        "Is a **Bossy R** after the vowel?",
        "YES → r changes the sound: **ar · or · er · ir · ur**!",
        listOf("bird"), Color(0xFF1A237E),
        15, "L15 · R-Controlled", RouteNavigation.RControlledIntro.route),
    LadderRung(4, "🔓",
        "Is the syllable **open** or **closed**?",
        "**Open** ends in a vowel → long sound! **Closed** → short!",
        listOf("go", "cat"), Color(0xFF6A1B9A),
        11, "L11 · Open Syllable", RouteNavigation.OpenSyllableIntro.route),
    LadderRung(5, "🔤",
        "No pattern? **Sound it out**!",
        "Say every sound, then **blend** them fast!",
        listOf("sun"), Color(0xFFAD1457),
        4, "L4 · CVC Words", RouteNavigation.CvcWordsIntro.route),
)

// ── ViewModel ────────────────────────────────────────────────────────────────

@HiltViewModel
class ReadingLadderViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager,
    private val phonicsSessions: PhonicsSessionRecorder,
) : ViewModel() {

    private val sessionStartMs = System.currentTimeMillis()
    private var recorded = false

    fun playWord(word: String) = audioManager.playPhonicsSound("phonics_word/$word")

    fun recordLearnTimeIfNeeded() {
        if (recorded) return
        recorded = true
        phonicsSessions.recordLearning(
            title = "Reading Ladder", mode = "LEARN",
            durationSeconds = ((System.currentTimeMillis() - sessionStartMs) / 1000).toInt())
    }
}

// ── Page ─────────────────────────────────────────────────────────────────────

@Composable
fun ReadingLadderPage(
    navController: NavController,
    viewModel: ReadingLadderViewModel = hiltViewModel(),
) {
    val accessVM = LocalAccessSheetViewModel.current
    val scope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        onDispose { viewModel.recordLearnTimeIfNeeded() }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.skyLavender, shape = KidsFloatingShape.sparkles)

        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            BackButtonWithText(
                title = "Reading Ladder",
                onBackClick = { navController.popBackStack() }
            )

            HeaderCard(modifier = Modifier.padding(horizontal = Dimens16))

            PlayDetectiveButton(
                onTap = {
                    AudioPlayerManager.playSoundMenuClick()
                    navController.navigate(RouteNavigation.WordDetective.route)
                },
                modifier = Modifier
                    .padding(horizontal = Dimens16)
                    .padding(top = Dimens8),
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = Dimens16)
                    .padding(top = Dimens8, bottom = Dimens16),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens2),
            ) {
                ladderRungs.forEachIndexed { index, rung ->
                    RungCard(
                        rung = rung,
                        onWordTap = { viewModel.playWord(it) },
                        onLevelTap = {
                            AudioPlayerManager.playSoundMenuClick()
                            if (rung.levelNumber > FREE_PHONICS_LEVELS) {
                                scope.launch {
                                    val allowed = accessVM.checkAccess(ModuleID.PHONICS_READING_PREMIUM)
                                    if (allowed) navController.navigate(rung.route)
                                }
                            } else {
                                navController.navigate(rung.route)
                            }
                        },
                    )
                    if (index < ladderRungs.lastIndex) {
                        Icon(
                            imageVector = Icons.Default.ArrowDownward,
                            contentDescription = null,
                            tint = Color(0xFF5532D2).copy(alpha = 0.5f),
                            modifier = Modifier.size(Dimens14),
                        )
                    }
                }
            }
        }
    }
}

// ── Play button — the ladder's game mode ─────────────────────────────────────

@Composable
private fun PlayDetectiveButton(onTap: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(Dimens4, RoundedCornerShape(Dimens12), spotColor = Color(0xFF5532D2))
            .background(
                Brush.horizontalGradient(listOf(Color(0xFF7C4DFF), Color(0xFF512DA8))),
                RoundedCornerShape(Dimens12),
            )
            .border(Dimens1, Color.White.copy(alpha = 0.25f), RoundedCornerShape(Dimens12))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onTap,
            )
            .padding(horizontal = Dimens14, vertical = Dimens10),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens8),
    ) {
        Text(text = "👾", style = MaterialTheme.typography.titleSmall.scaled())
        Text(
            text = "Play Word Detective!",
            style = MaterialTheme.typography.titleSmall.scaled(),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            maxLines = 1,
        )
        Text(
            text = "Solve alien words with your ladder",
            style = MaterialTheme.typography.labelSmall.scaled(),
            color = Color.White.copy(alpha = 0.85f),
            maxLines = 1,
            modifier = Modifier.weight(1f),
        )
        Icon(
            imageVector = Icons.Default.PlayCircle,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(Dimens20),
        )
    }
}

// ── Header ───────────────────────────────────────────────────────────────────

@Composable
private fun HeaderCard(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = Dimens12, strokeColor = Color(0xFF1A237E))
            .padding(horizontal = Dimens14, vertical = Dimens8),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens10),
    ) {
        Text(text = "👀", style = MaterialTheme.typography.titleLarge.scaled())
        Text(
            text = styled(
                "You meet a **NEW word**… Try the rules in **this order** — top to bottom!",
                Color(0xFF5532D2)
            ),
            style = MaterialTheme.typography.bodyMedium.scaled(),
            color = Color(0xFF263238),
            modifier = Modifier.weight(1f),
        )
        Text(text = "🪜", style = MaterialTheme.typography.titleLarge.scaled())
    }
}

// ── Rung card ────────────────────────────────────────────────────────────────

@Composable
private fun RungCard(
    rung: LadderRung,
    onWordTap: (String) -> Unit,
    onLevelTap: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = Dimens12, strokeColor = rung.color)
            .padding(horizontal = Dimens14, vertical = Dimens8),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens10),
    ) {
        // Step badge — number on the rung's color + big emoji
        Box(modifier = Modifier.size(Dimens40)) {
            Text(
                text = rung.emoji,
                style = MaterialTheme.typography.headlineMedium.scaled(),
                modifier = Modifier.align(Alignment.Center),
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = -Dimens4, y = -Dimens4)
                    .size(Dimens16)
                    .background(rung.color, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "${rung.step}",
                    style = centeredGlyph(MaterialTheme.typography.labelSmall.scaled()),
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
            }
        }

        // Question + hint
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = styled(rung.question, rung.color),
                style = MaterialTheme.typography.bodyMedium.scaled(),
                fontWeight = FontWeight.Medium,
                color = Color(0xFF263238),
            )
            Text(
                text = styled(rung.hint, rung.color),
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = Color(0xFF546E7A),
            )
        }

        // Example word chips — tap to hear
        rung.examples.forEach { word ->
            LadderWordChip(word = word, color = rung.color, onTap = { onWordTap(word) })
        }

        // Level link — jump into the full lesson (premium gate for L4+)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens4),
            modifier = Modifier
                .background(rung.color.copy(alpha = 0.12f), RoundedCornerShape(50))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onLevelTap,
                )
                .padding(horizontal = Dimens10, vertical = Dimens4),
        ) {
            Text(
                text = "📖 ${rung.levelLabel}",
                style = MaterialTheme.typography.labelSmall.scaled(),
                fontWeight = FontWeight.Bold,
                color = rung.color,
                maxLines = 1,
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = rung.color,
                modifier = Modifier.size(Dimens12),
            )
        }
    }
}

// ── Example word chip — audio + bounce ───────────────────────────────────────

@Composable
private fun LadderWordChip(word: String, color: Color, onTap: () -> Unit) {
    var bounce by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val scale by animateFloatAsState(
        targetValue = if (bounce) 1.15f else 1.0f,
        animationSpec = spring(stiffness = 300f, dampingRatio = 0.5f),
        label = "chipBounce_$word",
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens4),
        modifier = Modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .shadow(if (bounce) Dimens4 else Dimens1, RoundedCornerShape(50), spotColor = color)
            .background(Color.White.copy(alpha = 0.92f), RoundedCornerShape(50))
            .border(Dimens1, color.copy(alpha = 0.4f), RoundedCornerShape(50))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) {
                onTap()
                bounce = true
                scope.launch {
                    delay(350)
                    bounce = false
                }
            }
            .padding(horizontal = Dimens10, vertical = Dimens4),
    ) {
        Text(
            text = word,
            style = MaterialTheme.typography.bodyLarge.scaled(),
            fontWeight = FontWeight.Bold,
            color = Color(0xFF263238),
        )
        Text(text = "🔊", style = MaterialTheme.typography.labelSmall.scaled())
    }
}

// ── Helpers ──────────────────────────────────────────────────────────────────

/** Bold-keyword rendering: **marked** parts get bold + tinted (** split pattern). */
private fun styled(text: String, tint: Color) = buildAnnotatedString {
    text.split("**").forEachIndexed { index, part ->
        if (index % 2 == 1) {
            withStyle(SpanStyle(color = tint, fontWeight = FontWeight.Bold)) { append(part) }
        } else {
            append(part)
        }
    }
}

/** Tight glyph-centered style so the step number sits dead-center in its circle. */
private fun centeredGlyph(base: TextStyle) = base.copy(
    platformStyle = PlatformTextStyle(includeFontPadding = false),
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.Both,
    ),
)