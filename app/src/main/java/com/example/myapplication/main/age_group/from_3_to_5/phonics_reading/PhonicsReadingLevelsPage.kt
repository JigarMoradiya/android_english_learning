package com.example.myapplication.main.age_group.from_3_to_5.phonics_reading

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import com.example.myapplication.ui.theme.AppDimens.isTablet
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.phonics.listen.view_model.PhonicsListenLevelKey
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.ui.theme.AppDimens.Dimens1
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens3
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens14
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens28
import com.example.myapplication.ui.theme.AppDimens.Dimens32
import com.example.myapplication.ui.theme.AppDimens.Dimens40
import com.example.myapplication.ui.theme.AppDimens.Dimens60
import com.example.myapplication.ui.theme.AppDimens.Dimens80
import com.example.myapplication.main.common.sheets.LocalAccessSheetViewModel
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.extensions.scaled
import kotlinx.coroutines.launch
import kotlin.random.Random

// ── Data ─────────────────────────────────────────────────────────────────────

/** Levels 1..N are free; everything after needs a subscription. */
const val FREE_PHONICS_LEVELS = 3

data class PhonicsLevelItem(
    val number: Int,
    val title: String,
    val subtitle: String,
    val emoji: String,
    val color: Color,
    val ageTag: String,
    val levelKey: PhonicsListenLevelKey,
    val route: String,
)

data class PhonicsZoneItem(
    val title: String,
    val emoji: String,
    val color: Color,
    val ageTag: String,
    val levelNumbers: IntRange,
)

val phonicsZoneItems = listOf(
    PhonicsZoneItem("Foundation",        "🌱", Color(0xFF2E7D32), "Age 3-4", 1..2),
    PhonicsZoneItem("Early Blending",    "🧩", Color(0xFF1565C0), "Age 4-5", 3..5),
    PhonicsZoneItem("Word Patterns",     "🏠", Color(0xFFE91E63), "Age 5-6", 6..6),
    PhonicsZoneItem("Blends & Digraphs", "🎨", Color(0xFF4527A0), "Age 6-7", 7..10),
    PhonicsZoneItem("Long Vowels",       "🌈", Color(0xFFEF6C00), "Age 6-7", 11..16),
    PhonicsZoneItem("Advanced Patterns", "🚀", Color(0xFF0097A7), "Age 7+",  17..20),
    PhonicsZoneItem("Word Building",     "🏗️", Color(0xFF283593), "Age 7+",  21..27),
    PhonicsZoneItem("Sight Words",       "⭐", Color(0xFFD81B60), "Age 3+",  28..28),
)

val phonicsLevelItems = listOf(

    // ── Foundation (Age 3–4) ──────────────────────────────────────────────────
    PhonicsLevelItem(1,  "Letter Sounds",
        "Every letter has its own sound",
        "🔤", Color(0xFF2E7D32), "Age 3-4",
        PhonicsListenLevelKey.letterSounds, RouteNavigation.LetterSoundsIntro.route),
    PhonicsLevelItem(2,  "Short Vowels",
        "/a/ ant · /e/ egg\n/i/ ink · /o/ ox · /u/ up",
        "🍎", Color(0xFFE64A19), "Age 3-4",
        PhonicsListenLevelKey.shortVowels, RouteNavigation.ShortVowelsIntro.route),

    // ── Early Blending (Age 4–5) ──────────────────────────────────────────────
    PhonicsLevelItem(3,  "2-Sound Blending",
        "VC: at, an, in, up\nCV: ba, ma, si, do",
        "🔗", Color(0xFF1565C0), "Age 4-5",
        PhonicsListenLevelKey.blending, RouteNavigation.BlendingIntro.route),
    PhonicsLevelItem(4,  "CVC Words",
        "cat, dog, pig, sit, bug",
        "🐱", Color(0xFFAD1457), "Age 4-5",
        PhonicsListenLevelKey.cvcWords, RouteNavigation.CvcWordsIntro.route),
    PhonicsLevelItem(5,  "Short Vowel Spelling Rules",
        "-ff/-ll/-ss/-zz off, bell, miss\n-ck duck, back · -ng ring, song",
        "📏", Color(0xFF00695C), "Age 4-5",
        PhonicsListenLevelKey.shortVowelRules, RouteNavigation.ShortVowelRulesIntro.route),

    // ── Word Patterns (Age 5–6) ───────────────────────────────────────────────
    PhonicsLevelItem(6,  "Word Families",
        "-at bat, cat · -en hen, ten\n-ig big, pig · -og log, dog\n-un sun, run",
        "👨‍👩‍👧", Color(0xFFE91E63), "Age 5-6",
        PhonicsListenLevelKey.wordFamilies, RouteNavigation.WordFamiliesIntro.route),

    // ── Consonant Blends & Digraphs (Age 6–7) ────────────────────────────────
    PhonicsLevelItem(7,  "Beginning Blends",
        "bl, cl, fl, pl, sl\nbr, cr, dr, fr, gr, tr\nsm, sn, sp, st, sw",
        "🌟", Color(0xFFD32F2F), "Age 6-7",
        PhonicsListenLevelKey.beginningBlends, RouteNavigation.BeginningBlendsIntro.route),
    PhonicsLevelItem(8,  "Ending Blends",
        "nd hand · nt tent\nmp lamp · lk milk\nsk desk · ft left",
        "🎯", Color(0xFF4527A0), "Age 6-7",
        PhonicsListenLevelKey.endingBlends, RouteNavigation.EndingBlendsIntro.route),
    PhonicsLevelItem(9,  "Digraphs",
        "ch chip · sh ship\nth thin · wh whip\nph graph · qu quiz",
        "🔊", Color(0xFF0277BD), "Age 6-7",
        PhonicsListenLevelKey.digraphs, RouteNavigation.DigraphsIntro.route),
    PhonicsLevelItem(10, "Special Endings",
        "-tch catch, watch\n-dge bridge, fudge\n-nk sink, tank, honk",
        "🔚", Color(0xFF558B2F), "Age 6-7",
        PhonicsListenLevelKey.specialEndings, RouteNavigation.SpecialEndingsIntro.route),

    // ── Long Vowels (Age 6–7) ─────────────────────────────────────────────────
    PhonicsLevelItem(11, "Open Syllable",
        "me, he, go, no, she, be · vowel at end = long sound",
        "🔓", Color(0xFF6A1B9A), "Age 6-7",
        PhonicsListenLevelKey.openSyllable, RouteNavigation.OpenSyllableIntro.route),
    PhonicsLevelItem(12, "Magic E",
        "cap→cape · hop→hope · bit→bite · cub→cube · pet→Pete",
        "✨", Color(0xFF880E4F), "Age 6-7",
        PhonicsListenLevelKey.magicE, RouteNavigation.MagicEIntro.route),
    PhonicsLevelItem(13, "Vowel Teams",
        "ai/ay rain · ee/ea feet · oa/ow boat · oo moon/book · ew/ue new, blue · ea bread",
        "🤝", Color(0xFFEF6C00), "Age 6-7",
        PhonicsListenLevelKey.vowelTeams, RouteNavigation.VowelTeamsIntro.route),
    PhonicsLevelItem(14, "Diphthongs",
        "oi/oy coin, boy · ou/ow cloud, cow · au/aw pause, saw",
        "🔄", Color(0xFF4E342E), "Age 6-7",
        PhonicsListenLevelKey.diphthongs, RouteNavigation.DiphthongsIntro.route),
    PhonicsLevelItem(15, "R-Controlled Vowels",
        "ar car · or fork · er her · ir bird · ur burn",
        "🌀", Color(0xFF1A237E), "Age 6-7",
        PhonicsListenLevelKey.rControlled, RouteNavigation.RControlledIntro.route),
    PhonicsLevelItem(16, "igh & gh Patterns",
        "igh night, light, fight · gh=/f/: enough, laugh",
        "🌙", Color(0xFF311B92), "Age 6-7",
        PhonicsListenLevelKey.ighGh, RouteNavigation.IghGhIntro.route),

    // ── Advanced Patterns (Age 7+) ────────────────────────────────────────────
    PhonicsLevelItem(17, "Y as a Vowel",
        "fly, sky = /i/ · happy, baby = /e/ · gym, myth = /i/",
        "🦋", Color(0xFF0097A7), "Age 7+",
        PhonicsListenLevelKey.yAsVowel, RouteNavigation.YAsVowelIntro.route),
    PhonicsLevelItem(18, "3-Letter Blends",
        "str strong · spl splash · spr spring · thr three · scr scream",
        "💪", Color(0xFFF9A825), "Age 7+",
        PhonicsListenLevelKey.threeLetterBlends, RouteNavigation.ThreeLetterBlendsIntro.route),
    PhonicsLevelItem(19, "Soft C & Soft G",
        "c+e/i/y = /s/ city, ice · g+e/i/y = /j/ gem, giraffe, gym",
        "🎭", Color(0xFFBF360C), "Age 7+",
        PhonicsListenLevelKey.softCSoftG, RouteNavigation.SoftCSoftGIntro.route),
    PhonicsLevelItem(20, "Silent Letters",
        "kn knife, know · wr write, wrist · mb lamb, climb · gn gnat, sign",
        "🤫", Color(0xFF37474F), "Age 7+",
        PhonicsListenLevelKey.silentLetters, RouteNavigation.SilentLettersIntro.route),

    // ── Word Building (Age 7+) ────────────────────────────────────────────────
    PhonicsLevelItem(21, "Word Endings",
        "-s/-es cats, boxes · -ing/-ed/-er/-est · double run→running · drop-e · y→i cry→cried",
        "🔧", Color(0xFF2E7D32), "Age 7+",
        PhonicsListenLevelKey.wordEndings, RouteNavigation.WordEndingsIntro.route),
    PhonicsLevelItem(22, "Prefixes",
        "un- unhappy · re- redo · pre- preview · dis- disagree · mis- mistake",
        "⬅️", Color(0xFF283593), "Age 7+",
        PhonicsListenLevelKey.prefixes, RouteNavigation.PrefixesIntro.route),
    PhonicsLevelItem(23, "Suffixes",
        "-ful helpful · -less careless · -ness kindness · -tion/-sion nation",
        "➡️", Color(0xFF3949AB), "Age 7+",
        PhonicsListenLevelKey.suffixes, RouteNavigation.SuffixesIntro.route),
    PhonicsLevelItem(24, "Contractions",
        "do+not=don't · can+not=can't · I+am=I'm · it+is=it's",
        "🤏", Color(0xFF8E24AA), "Age 7+",
        PhonicsListenLevelKey.contractions, RouteNavigation.ContractionsIntro.route),
    PhonicsLevelItem(25, "Consonant + -le",
        "ap-ple, lit-tle, ta-ble, pur-ple, bub-ble · final -e is silent",
        "🍏", Color(0xFFAD1457), "Age 7+",
        PhonicsListenLevelKey.consonantLe, RouteNavigation.ConsonantLeIntro.route),
    PhonicsLevelItem(26, "Compound Words",
        "sun+flower · rain+bow · bed+room · butter+fly",
        "🌈", Color(0xFFF57C00), "Age 7+",
        PhonicsListenLevelKey.compoundWords, RouteNavigation.CompoundWordsIntro.route),
    PhonicsLevelItem(27, "Syllable Division",
        "VCCV rab-bit · VCV pi-lot · V/CV ti-ger · VC/V cam-el",
        "✂️", Color(0xFF00897B), "Age 7+",
        PhonicsListenLevelKey.syllableDivision, RouteNavigation.SyllableDivisionIntro.route),

    // ── Sight Words (All Ages) ────────────────────────────────────────────────
    PhonicsLevelItem(28, "Sight Words",
        "the, was, said, have, they, once, who, your, because, friend",
        "⭐", Color(0xFFD81B60), "Age 3+",
        PhonicsListenLevelKey.sightWords, RouteNavigation.StarWordsIntro.route),
)

/** Snapshot of one level's progress, read once per repository change. */
private data class NodeProgress(
    val stars: Int,
    val isDone: Boolean,
    val isStarted: Boolean,
    val listenDone: Int,
    val listenTotal: Int,
    val bestScore: Int?,
    val bestTotal: Int?,
)

// ── Journey layout ─────────────────────────────────────────────────────────────
//
// A vertical road with alternating stops. The layout is deterministic (fixed row
// heights) so the curved path geometry can be computed up front, mirroring the iOS
// JourneyLayout. Banners announce a zone; nodes are a stop-badge + billboard card.

private sealed interface JourneyRow {
    val topY: Dp
    val height: Dp
    val centerY: Dp get() = topY + height / 2

    data class Banner(val zone: PhonicsZoneItem, override val topY: Dp, override val height: Dp) : JourneyRow
    data class Node(
        val level: PhonicsLevelItem,
        val nodeIndex: Int,
        val badgeOnLeft: Boolean,
        override val topY: Dp,
        override val height: Dp,
    ) : JourneyRow
}

private data class JourneyLayout(
    val rows: List<JourneyRow>,
    val nodePoints: List<Pair<Float, Dp>>, // xFrac to centerY
    val totalHeight: Dp,
)

private fun badgeXFrac(nodeIndex: Int): Float = if (nodeIndex % 2 == 0) 0.10f else 0.90f

private fun buildJourneyLayout(
    nodeRowHeight: Dp,
    bannerRowHeight: Dp,
    topPadding: Dp,
    bottomPadding: Dp,
): JourneyLayout {
    val rows = mutableListOf<JourneyRow>()
    val points = mutableListOf<Pair<Float, Dp>>()
    var y = topPadding
    var nodeIndex = 0
    for (zone in phonicsZoneItems) {
        rows += JourneyRow.Banner(zone, topY = y, height = bannerRowHeight)
        y += bannerRowHeight
        for (number in zone.levelNumbers) {
            val level = phonicsLevelItems.firstOrNull { it.number == number } ?: continue
            val row = JourneyRow.Node(
                level = level,
                nodeIndex = nodeIndex,
                badgeOnLeft = nodeIndex % 2 == 0,
                topY = y,
                height = nodeRowHeight,
            )
            rows += row
            points += badgeXFrac(nodeIndex) to row.centerY
            y += nodeRowHeight
            nodeIndex++
        }
    }
    return JourneyLayout(rows, points, totalHeight = y + bottomPadding)
}

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun PhonicsReadingLevelsPage(
    navController: NavController,
    viewModel: PhonicsJourneyViewModel = hiltViewModel(),
) {
    val repo = viewModel.levelProgress
    val progressMap by repo.progress.collectAsState()

    // Re-read every level's progress whenever the stored map changes.
    val nodeProgress = remember(progressMap) {
        phonicsLevelItems.associate { level ->
            val (listenDone, listenTotal) = repo.listenProgress(level.levelKey)
            val best = repo.bestResult(level.levelKey)
            level.number to NodeProgress(
                stars = repo.stars(level.levelKey),
                isDone = repo.isDone(level.levelKey),
                isStarted = repo.isStarted(level.levelKey),
                listenDone = listenDone,
                listenTotal = listenTotal,
                bestScore = best?.first,
                bestTotal = best?.second,
            )
        }
    }
    val doneCount = remember(progressMap) { repo.doneCount }
    val totalStars = remember(progressMap) { repo.totalStars }
    val nextUpLevel = remember(progressMap) {
        phonicsLevelItems.firstOrNull { !repo.isDone(it.levelKey) }
    }
    val totalLevels = phonicsLevelItems.size

    // Intro animations run only the first time this screen composes; coming back
    // from a level just refreshes progress/counts silently (matches iOS).
    val playIntro = remember {
        val first = !viewModel.introPlayed
        viewModel.introPlayed = true
        first
    }
    val ringTarget = if (totalLevels > 0) doneCount.toFloat() / totalLevels else 0f
    val ringStart = remember { viewModel.lastRingFraction }
    LaunchedEffect(ringTarget) { viewModel.lastRingFraction = ringTarget }

    // Levels 1–3 are free; 4+ need a subscription. Lock badges hide for premium users
    // (and while DEV_MODE bypasses gating).
    val accessVM = LocalAccessSheetViewModel.current
    val scope = rememberCoroutineScope()
    val userState by accessVM.userState.collectAsState()
    val showLocks = !userState.isPremium && !accessVM.isDevMode

    val openLevel: (PhonicsLevelItem) -> Unit = { level ->
        if (level.number > FREE_PHONICS_LEVELS) {
            scope.launch {
                val allowed = accessVM.checkAccess(ModuleID.PHONICS_READING_PREMIUM)
                if (allowed) {
                    AudioPlayerManager.playSoundMenuClick()
                    viewModel.markVisited(level.levelKey)
                    navController.navigate(level.route)
                }
            }
        } else {
            AudioPlayerManager.playSoundMenuClick()
            viewModel.markVisited(level.levelKey)
            navController.navigate(level.route)
        }
    }

    // Celebrate once when a new level got completed since the last visit.
    var celebrate by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (viewModel.consumeCelebration()) {
            celebrate = true
            kotlinx.coroutines.delay(1600)
            celebrate = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.mintLime, shape = KidsFloatingShape.musicNotes)

        Row(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            // ── LEFT PANEL — progress hub ───────────────────────────────────
            Column(
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                BackButtonWithText(
                    title = stringResource(R.string.phonics_reading),
                    onBackClick = { navController.popBackStack() }
                )

                // Weighted scroll-safe slot: the hub can never push the strip off-screen
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {

                    ProgressHub(
                        doneCount = doneCount,
                        totalLevels = totalLevels,
                        totalStars = totalStars,
                        nextUp = nextUpLevel,
                        celebrate = celebrate,
                        ringStart = ringStart,
                        onContinue = { nextUpLevel?.let(openLevel) },
                        modifier = Modifier.padding(horizontal = Dimens16),
                    )

                    Spacer(modifier = Modifier.height(Dimens8))
                }

                CompareStrip(
                    onTap = {
                        AudioPlayerManager.playSoundMenuClick()
                        navController.navigate(RouteNavigation.PhonicsComparisons.route)
                    },
                    modifier = Modifier
                        .padding(horizontal = Dimens16)
                        .padding(bottom = Dimens12),
                )
            }

            // ── RIGHT PANEL — journey map ───────────────────────────────────
            JourneyMap(
                nodeProgress = nodeProgress,
                nextUpNumber = nextUpLevel?.number,
                playIntro = playIntro,
                showLocks = showLocks,
                onOpen = openLevel,
                modifier = Modifier
                    .weight(0.65f)
                    .fillMaxHeight(),
            )
        }
    }
}

// ── Journey Map ─────────────────────────────────────────────────────────────

@Composable
private fun JourneyMap(
    nodeProgress: Map<Int, NodeProgress>,
    nextUpNumber: Int?,
    playIntro: Boolean,
    showLocks: Boolean,
    onOpen: (PhonicsLevelItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val nodeRowHeight = Dimens80 + Dimens28
    val bannerRowHeight = Dimens40 + Dimens20
    val layout = remember(nodeRowHeight, bannerRowHeight) {
        buildJourneyLayout(
            nodeRowHeight = nodeRowHeight,
            bannerRowHeight = bannerRowHeight,
            topPadding = Dimens16,
            bottomPadding = Dimens40 + Dimens8,
        )
    }

    val density = LocalDensity.current
    val scrollState = rememberScrollState()

    // Road draws itself in on first appearance only — on return it is already there.
    val pathProgress = remember { Animatable(if (playIntro) 0f else 1f) }
    var nodesAppeared by remember { mutableStateOf(!playIntro) }
    LaunchedEffect(Unit) {
        if (playIntro) {
            nodesAppeared = true
            pathProgress.animateTo(1f, tween(durationMillis = 1400, easing = LinearOutSlowInEasing))
        }
    }

    // Auto-scroll to the first unfinished stop so the kid lands on "where I am".
    LaunchedEffect(nextUpNumber, layout) {
        val target = nextUpNumber ?: return@LaunchedEffect
        if (target <= 2) return@LaunchedEffect
        val row = layout.rows.filterIsInstance<JourneyRow.Node>().firstOrNull { it.level.number == target }
            ?: return@LaunchedEffect
        kotlinx.coroutines.delay(600)
        val targetPx = with(density) { (row.topY - bannerRowHeight).coerceAtLeast(0.dp).roundToPx() }
        scrollState.animateScrollTo(targetPx)
    }

    val laneColor = Color.White.copy(alpha = 0.85f)
    val dashColor = Color(0xFFFFB74D)
    val laneWidthPx = with(density) { Dimens14.toPx() }
    val dashWidthPx = with(density) { Dimens3.toPx() }
    val dashOn = with(density) { Dimens8.toPx() }
    val dashOff = with(density) { Dimens10.toPx() }

    Box(
        modifier = modifier
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(layout.totalHeight)
                .padding(horizontal = Dimens32)
        ) {
            // Road: white lane + dashed center line, trimmed to pathProgress.
            Canvas(modifier = Modifier.fillMaxSize()) {
                val pts = layout.nodePoints.map { (xFrac, centerY) ->
                    Offset(xFrac * size.width, centerY.toPx())
                }
                val full = buildRoadPath(pts)
                val drawn = trimPath(full, pathProgress.value)
                drawPath(
                    drawn, laneColor,
                    style = Stroke(width = laneWidthPx, cap = StrokeCap.Round, join = StrokeJoin.Round),
                )
                drawPath(
                    drawn, dashColor,
                    style = Stroke(
                        width = dashWidthPx, cap = StrokeCap.Round, join = StrokeJoin.Round,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashOn, dashOff)),
                    ),
                )
            }

            layout.rows.forEach { row ->
                when (row) {
                    is JourneyRow.Banner -> {
                        Box(
                            modifier = Modifier
                                .offset(y = row.topY)
                                .fillMaxWidth()
                                .height(row.height),
                            contentAlignment = Alignment.Center,
                        ) {
                            ZoneBanner(row.zone)
                        }
                    }

                    is JourneyRow.Node -> {
                        val progress = nodeProgress[row.level.number]
                            ?: NodeProgress(0, false, false, 0, 0, null, null)
                        val appear by animateFloatAsState(
                            targetValue = if (nodesAppeared) 1f else 0f,
                            animationSpec = tween(
                                durationMillis = 350,
                                delayMillis = 150 + row.nodeIndex * 50,
                            ),
                            label = "nodeAppear",
                        )
                        Box(
                            modifier = Modifier
                                .offset(y = row.topY)
                                .fillMaxWidth()
                                .height(row.height)
                                .graphicsLayer {
                                    alpha = appear
                                    scaleX = 0.2f + 0.8f * appear
                                    scaleY = 0.2f + 0.8f * appear
                                },
                        ) {
                            JourneyNode(
                                level = row.level,
                                progress = progress,
                                badgeOnLeft = row.badgeOnLeft,
                                isNextUp = row.level.number == nextUpNumber,
                                isLocked = showLocks && row.level.number > FREE_PHONICS_LEVELS,
                                onTap = { onOpen(row.level) },
                            )
                        }
                    }
                }
            }
        }
    }
}

// Cubic road connecting the alternating stops, matching the iOS JourneyPathShape.
private fun buildRoadPath(points: List<Offset>): Path {
    val path = Path()
    if (points.isEmpty()) return path
    path.moveTo(points[0].x, points[0].y)
    for (i in 1 until points.size) {
        val prev = points[i - 1]
        val cur = points[i]
        val midY = (prev.y + cur.y) / 2
        path.cubicTo(prev.x, midY, cur.x, midY, cur.x, cur.y)
    }
    return path
}

private fun trimPath(full: Path, fraction: Float): Path {
    if (fraction >= 1f) return full
    if (fraction <= 0f) return Path()
    val measure = PathMeasure()
    measure.setPath(full, false)
    val out = Path()
    measure.getSegment(0f, measure.length * fraction, out, true)
    return out
}

// ── Zone Banner ─────────────────────────────────────────────────────────────

@Composable
private fun ZoneBanner(zone: PhonicsZoneItem) {
    Row(
        modifier = Modifier
            .shadow(Dimens4, RoundedCornerShape(50))
            .background(
                Brush.horizontalGradient(listOf(zone.color, zone.color.copy(alpha = 0.75f))),
                RoundedCornerShape(50),
            )
            .padding(horizontal = Dimens16, vertical = Dimens8),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens8),
    ) {
        Text(text = zone.emoji, style = MaterialTheme.typography.titleMedium.scaled())
        Text(
            text = zone.title,
            style = MaterialTheme.typography.titleSmall.scaled(),
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
        Text(
            text = zone.ageTag,
            style = MaterialTheme.typography.labelSmall.scaled(),
            color = Color.White.copy(alpha = 0.9f),
            modifier = Modifier
                .background(Color.White.copy(alpha = 0.22f), RoundedCornerShape(50))
                .padding(horizontal = Dimens8, vertical = Dimens2),
        )
    }
}

// ── Journey Node (stop badge on the road + billboard card) ──────────────────

@Composable
private fun JourneyNode(
    level: PhonicsLevelItem,
    progress: NodeProgress,
    badgeOnLeft: Boolean,
    isNextUp: Boolean,
    isLocked: Boolean,
    onTap: () -> Unit,
) {
    val badgeSize = Dimens60
    val gap = Dimens16
    val edgeMargin = Dimens8

    Box(modifier = Modifier.fillMaxSize()) {
        // Compute badge + card bounds against the row's real width.
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val panelWidth = maxWidth
            val badgeX = panelWidth * (if (badgeOnLeft) 0.10f else 0.90f)
            val cardLeft: Dp
            val cardWidth: Dp
            if (badgeOnLeft) {
                cardLeft = badgeX + badgeSize / 2 + gap
                cardWidth = (panelWidth - edgeMargin) - cardLeft
            } else {
                cardLeft = edgeMargin
                cardWidth = (badgeX - badgeSize / 2 - gap) - cardLeft
            }

            // Billboard card, vertically centered, filling the free width.
            Box(
                modifier = Modifier
                    .offset(x = cardLeft)
                    .width(cardWidth)
                    .align(Alignment.CenterStart),
            ) {
                BillboardCard(level = level, progress = progress, isLocked = isLocked, onTap = onTap)
            }

            // Round stop badge sitting on the road.
            Box(
                modifier = Modifier
                    .offset(x = badgeX - badgeSize / 2)
                    .align(Alignment.CenterStart)
                    .size(badgeSize)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onTap,
                    ),
            ) {
                StopBadge(level = level, progress = progress, isNextUp = isNextUp, isLocked = isLocked, badgeSize = badgeSize)
            }
        }
    }
}

@Composable
private fun StopBadge(
    level: PhonicsLevelItem,
    progress: NodeProgress,
    isNextUp: Boolean,
    isLocked: Boolean,
    badgeSize: Dp,
) {
    val active = progress.isStarted || progress.isDone
    val density = LocalDensity.current
    val emojiSize = with(density) { (badgeSize * 0.5f).toSp() }
    val cornerSize = badgeSize * 0.34f

    // Tight, glyph-centered style so a single character sits dead-center in the
    // little corner circles (default Text font padding pushes it low).
    val centeredGlyph = TextStyle(
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.Both,
        ),
    )

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        // Bouncing "you are here" marker above the next stop.
        if (isNextUp) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = -badgeSize * 0.55f),
            ) { BouncingMarker() }
        }

        // The disc.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (active) level.color else level.color.copy(alpha = 0.35f), CircleShape)
                .border(Dimens3, Color.White, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            if (isNextUp) {
                WigglingEmoji(emoji = level.emoji, fontSize = emojiSize)
            } else {
                Text(text = level.emoji, fontSize = emojiSize)
            }
        }

        // Level number, top-left.
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = -badgeSize * 0.36f, y = -badgeSize * 0.36f)
                .size(cornerSize)
                .background(Color(0xFF37474F), CircleShape)
                .border(Dimens1, Color.White, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "${level.number}",
                fontSize = with(density) { (badgeSize * 0.22f).toSp() },
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                style = centeredGlyph,
            )
        }

        // Lock seal (premium level), bottom-right — same spot as the done seal;
        // a locked level can never be done, so the two never collide.
        if (isLocked) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = badgeSize * 0.36f, y = badgeSize * 0.36f)
                    .size(cornerSize)
                    .background(Color(0xFFE65100), CircleShape)
                    .border(Dimens1, Color.White, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(badgeSize * 0.18f),
                )
            }
        } else if (progress.isDone) {
            // Done seal, bottom-right.
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = badgeSize * 0.36f, y = badgeSize * 0.36f)
                    .size(cornerSize)
                    .background(Color(0xFF1B5E20), CircleShape)
                    .border(Dimens1, Color.White, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "✓",
                    fontSize = with(density) { (badgeSize * 0.24f).toSp() },
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    style = centeredGlyph,
                )
            }
        }
    }
}

@Composable
private fun BillboardCard(
    level: PhonicsLevelItem,
    progress: NodeProgress,
    isLocked: Boolean,
    onTap: () -> Unit,
) {
    val strokeColor = if (progress.isDone) level.color else level.color.copy(alpha = 0.35f)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (isLocked) 0.75f else 1f)
            .shadow(Dimens4, RoundedCornerShape(Dimens16))
            .background(Color.White.copy(alpha = 0.94f), RoundedCornerShape(Dimens16))
            .border(Dimens2, strokeColor, RoundedCornerShape(Dimens16))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onTap,
            )
            .padding(Dimens12),
        verticalArrangement = Arrangement.spacedBy(Dimens3),
    ) {
        // Title + age tag
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = if (isLocked) "🔒 ${level.title}" else level.title,
                style = MaterialTheme.typography.titleSmall.scaled(),
                fontWeight = FontWeight.Bold,
                color = level.color,
                maxLines = 1,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(Dimens6))
            Text(
                text = level.ageTag,
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = level.color,
                modifier = Modifier
                    .background(level.color.copy(alpha = 0.12f), RoundedCornerShape(50))
                    .padding(horizontal = Dimens6, vertical = Dimens1),
            )
        }

        // Subtitle (single-line-ish preview)
        Text(
            text = level.subtitle.replace("\n", " "),
            style = MaterialTheme.typography.labelSmall.scaled(),
            color = Color.Black.copy(alpha = 0.55f),
            maxLines = 2,
        )

        Spacer(modifier = Modifier.height(Dimens4))

        // Stars + best/keep-going + listen bar
        Row(verticalAlignment = Alignment.CenterVertically) {
            StarsRow(stars = progress.stars)

            Spacer(modifier = Modifier.width(Dimens8))

            if (progress.bestScore != null && progress.bestTotal != null) {
                Text(
                    text = "Best ${progress.bestScore}/${progress.bestTotal}",
                    style = MaterialTheme.typography.labelSmall.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32),
                    modifier = Modifier
                        .background(Color(0xFF2E7D32).copy(alpha = 0.12f), RoundedCornerShape(50))
                        .padding(horizontal = Dimens6, vertical = Dimens1),
                )
            } else if (progress.isStarted && !progress.isDone) {
                Text(
                    text = "Keep going ▶",
                    style = MaterialTheme.typography.labelSmall.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .background(Color(0xFFEF6C00), RoundedCornerShape(50))
                        .padding(horizontal = Dimens6, vertical = Dimens1),
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            if (progress.listenTotal > 0) {
                ListenBar(done = progress.listenDone, total = progress.listenTotal)
            }
        }
    }
}

@Composable
private fun StarsRow(stars: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(Dimens2)) {
        repeat(3) { idx ->
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (idx < stars) Color(0xFFFFC107) else Color(0xFFBDBDBD).copy(alpha = 0.6f),
                modifier = Modifier.size(Dimens12),
            )
        }
    }
}

@Composable
private fun ListenBar(done: Int, total: Int) {
    val trackColor = Color(0xFF1565C0)
    val fraction = if (total > 0) done.toFloat() / total else 0f
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens3),
    ) {
        Text(text = "🎧", style = MaterialTheme.typography.labelSmall.scaled())
        Box(
            modifier = Modifier
                .width(Dimens40)
                .height(Dimens4)
                .clip(RoundedCornerShape(50))
                .background(trackColor.copy(alpha = 0.15f)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction)
                    .clip(RoundedCornerShape(50))
                    .background(trackColor),
            )
        }
        Text(
            text = "$done/$total",
            style = MaterialTheme.typography.labelSmall.scaled(),
            color = Color.Gray,
        )
    }
}

// ── Left Panel Progress Hub ─────────────────────────────────────────────────

@Composable
private fun ProgressHub(
    doneCount: Int,
    totalLevels: Int,
    totalStars: Int,
    nextUp: PhonicsLevelItem?,
    celebrate: Boolean,
    ringStart: Float,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Phones in landscape have very little height — tighten the hub so the
    // Compare strip below always stays fully on screen.
    val compact = !isTablet

    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(Dimens8, RoundedCornerShape(Dimens24))
                .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(Dimens24))
                .padding(vertical = if (compact) Dimens12 else Dimens20, horizontal = Dimens16),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(if (compact) Dimens6 else Dimens10),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens16),
            ) {
                BobbingTeddy(height = if (compact) Dimens80 else Dimens80 + Dimens16)
                ProgressRing(doneCount = doneCount, totalLevels = totalLevels, startFraction = ringStart)
            }

            Text(
                text = "Phonics Journey",
                style = MaterialTheme.typography.titleMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A237E),
            )

            Text(
                text = progressMessage(doneCount),
                style = MaterialTheme.typography.bodySmall.scaled(),
                color = Color.Gray,
                textAlign = TextAlign.Center,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens4),
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(Dimens16),
                )
                Text(
                    text = "$totalStars / ${totalLevels * 3}",
                    style = MaterialTheme.typography.titleMedium.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE65100),
                )
            }

            if (nextUp != null) {
                Row(
                    modifier = Modifier
                        .shadow(Dimens4, RoundedCornerShape(50))
                        .background(Color(0xFF2E7D32), RoundedCornerShape(50))
                        .clickable(onClick = onContinue)
                        .padding(horizontal = Dimens16, vertical = Dimens8),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens6),
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(Dimens16),
                    )
                    Text(
                        text = if (doneCount == 0) "Start Journey" else "Continue Journey",
                        style = MaterialTheme.typography.titleSmall.scaled(),
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }
            } else {
                Text(
                    text = "Journey Complete! 🎉",
                    style = MaterialTheme.typography.titleSmall.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32),
                )
            }
        }

        if (celebrate) {
            CelebrationBurst(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
private fun ProgressRing(doneCount: Int, totalLevels: Int, startFraction: Float = 0f) {
    val ringSize = Dimens80
    val density = LocalDensity.current
    val strokePx = with(density) { Dimens8.toPx() }
    val target = if (totalLevels > 0) doneCount.toFloat() / totalLevels else 0f
    // On return, animate from the last shown value to the new one (silent update).
    val ring = remember { Animatable(startFraction) }
    LaunchedEffect(target) {
        ring.animateTo(target, tween(durationMillis = 1000, delayMillis = 300))
    }
    val trackColor = Color(0xFF2E7D32).copy(alpha = 0.15f)
    val sweep = Brush.sweepGradient(
        listOf(Color(0xFF66BB6A), Color(0xFF2E7D32), Color(0xFF66BB6A)),
    )

    Box(modifier = Modifier.size(ringSize), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val inset = strokePx / 2
            val arcSize = Size(size.width - strokePx, size.height - strokePx)
            val topLeft = Offset(inset, inset)
            drawArc(
                color = trackColor,
                startAngle = 0f, sweepAngle = 360f, useCenter = false,
                topLeft = topLeft, size = arcSize,
                style = Stroke(width = strokePx),
            )
            drawArc(
                brush = sweep,
                startAngle = -90f, sweepAngle = 360f * ring.value, useCenter = false,
                topLeft = topLeft, size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round),
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$doneCount",
                style = MaterialTheme.typography.titleLarge.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32),
            )
            Text(
                text = "of $totalLevels",
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = Color.Gray,
            )
        }
    }
}

private fun progressMessage(doneCount: Int): String = when (doneCount) {
    0 -> "Hop on the road —\nlet's learn to read!"
    in 1..9 -> "Great start!\nKeep following the road!"
    in 10..19 -> "Wow, halfway there!\nYou're a reading star!"
    in 20..27 -> "Almost at the finish line!\nYou can do it!"
    else -> "You finished the whole\nphonics journey! Amazing!"
}

// ── Compare & Choose entry strip ─────────────────────────────────────────────

@Composable
private fun CompareStrip(onTap: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(Dimens4, RoundedCornerShape(Dimens12), spotColor = Color(0xFF5532D2))
            .background(
                Brush.horizontalGradient(listOf(Color(0xFF7C4DFF), Color(0xFF536DFE))),
                RoundedCornerShape(Dimens12),
            )
            .border(Dimens1, Color.White.copy(alpha = 0.22f), RoundedCornerShape(Dimens12))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onTap,
            )
            .padding(horizontal = Dimens12, vertical = Dimens8),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens8),
    ) {
        Text(text = "⚖️", style = MaterialTheme.typography.titleSmall.scaled())
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Compare & Choose 🕵️",
                style = MaterialTheme.typography.labelMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
            )
            Text(
                text = "ai⚡ay · oo🌙⚡📖 · c⚡k⚡ck …",
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = Color.White.copy(alpha = 0.85f),
                maxLines = 1,
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.9f),
            modifier = Modifier.size(Dimens16),
        )
    }
}

// ── Small Animated Pieces ───────────────────────────────────────────────────

@Composable
private fun BobbingTeddy(height: Dp) {
    val transition = rememberInfiniteTransition(label = "teddy")
    val density = LocalDensity.current
    val bobPx = with(density) { Dimens8.toPx() }
    val bob by transition.animateFloat(
        initialValue = 0f, targetValue = -bobPx,
        animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Reverse),
        label = "bob",
    )
    Image(
        painter = painterResource(id = R.drawable._mascot_),
        contentDescription = null,
        modifier = Modifier
            .height(height)
            .graphicsLayer { translationY = bob },
    )
}

@Composable
private fun WigglingEmoji(emoji: String, fontSize: androidx.compose.ui.unit.TextUnit) {
    val transition = rememberInfiniteTransition(label = "wiggle")
    val angle by transition.animateFloat(
        initialValue = -8f, targetValue = 8f,
        animationSpec = infiniteRepeatable(tween(500), RepeatMode.Reverse),
        label = "angle",
    )
    Text(
        text = emoji,
        fontSize = fontSize,
        modifier = Modifier.graphicsLayer { rotationZ = angle },
    )
}

@Composable
private fun BouncingMarker() {
    val transition = rememberInfiniteTransition(label = "marker")
    val density = LocalDensity.current
    val upPx = with(density) { Dimens8.toPx() }
    val y by transition.animateFloat(
        initialValue = -upPx, targetValue = 0f,
        animationSpec = infiniteRepeatable(tween(550), RepeatMode.Reverse),
        label = "bounce",
    )
    Text(
        text = "👇",
        style = MaterialTheme.typography.titleMedium.scaled(),
        modifier = Modifier.graphicsLayer { translationY = y },
    )
}

@Composable
private fun CelebrationBurst(modifier: Modifier = Modifier) {
    val density = LocalDensity.current
    val particles = remember {
        List(12) { i ->
            Triple(
                listOf("🎉", "⭐", "✨", "🎈")[i % 4],
                Random.nextInt(-110, 111).toFloat(),
                Random.nextInt(-150, -30).toFloat(),
            )
        }
    }
    val fly = remember { Animatable(0f) }
    LaunchedEffect(Unit) { fly.animateTo(1f, tween(1200)) }

    Box(modifier = modifier) {
        particles.forEach { (emoji, dx, dy) ->
            Text(
                text = emoji,
                style = MaterialTheme.typography.titleLarge.scaled(),
                modifier = Modifier.graphicsLayer {
                    translationX = with(density) { dx.dp.toPx() } * fly.value
                    translationY = with(density) { dy.dp.toPx() } * fly.value
                    alpha = 1f - fly.value
                    scaleX = 0.4f + 0.9f * fly.value
                    scaleY = 0.4f + 0.9f * fly.value
                },
            )
        }
    }
}
