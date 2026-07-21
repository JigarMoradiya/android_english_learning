package com.example.myapplication.main.age_group.phonics.compare

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.progress.PhonicsSessionRecorder
import com.example.myapplication.main.age_group.from_3_to_5.phonics_reading.FREE_PHONICS_LEVELS
import com.example.myapplication.main.common.ActivityCompletePopup
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.kidsGlassCard
import com.example.myapplication.main.common.sheets.LocalAccessSheetViewModel
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
import com.example.myapplication.utilities.AudioPhonicsManager
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.extensions.scaled
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.math.sin
import androidx.compose.ui.draw.clip

// ComparisonDetailPage.kt
// Reusable "Compare & Choose" detail — detective rule card + level link,
// team zones to learn from, and a Sort-It! quiz at the bottom.
// One template renders all comparisons; content comes from ComparisonData.
// Keep identical to iOS ComparisonDetailView.

// ── ViewModel ────────────────────────────────────────────────────────────────

data class ComparisonDetailUiState(
    val currentIndex: Int = 0,
    val score: Int = 0,
    val wrongCount: Int = 0,
    val answeredTeam: Int? = null,   // team tapped for the current question
    val isFinished: Boolean = false,
    val shakeWrong: Boolean = false,
    /** Quiz words that landed in each zone — they "fly in" as the kid sorts them. */
    val solvedWords: List<List<ComparisonWord>> = emptyList(),
)

@HiltViewModel
class ComparisonDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val audioManager: AudioPhonicsManager,
    private val phonicsSessions: PhonicsSessionRecorder,
) : ViewModel() {

    val comparison: PhonicsComparison =
        phonicsComparison(savedStateHandle.get<String>("comparisonId").orEmpty())
            ?: phonicsComparisons.first()

    var uiState by mutableStateOf(
        ComparisonDetailUiState(solvedWords = List(comparison.teams.size) { emptyList() })
    ); private set

    private var quizItems = makeComparisonQuizRound(comparison)
    private var sessionStartMs = System.currentTimeMillis()
    private var recordedPractice = false
    private val wrongWords = mutableListOf<String>()
    private val correctWords = mutableListOf<String>()

    val currentItem: ComparisonQuizItem? get() = quizItems.getOrNull(uiState.currentIndex)
    val totalQuestions: Int get() = quizItems.size

    fun playWord(audio: String) {
        // Letter sounds (sound_c, letter_k) live in their own folder; the rest are words/blends.
        val folder = if (audio.startsWith("letter_") || audio.startsWith("sound_")) "phonics_letter" else "phonics_word"
        audioManager.playPhonicsSound("$folder/$audio")
    }

    fun answer(team: Int) {
        if (uiState.answeredTeam != null) return
        val item = currentItem ?: return
        val correct = team == item.answerTeam
        uiState = uiState.copy(answeredTeam = team)
        if (correct) {
            correctWords.add(item.word)
            audioManager.playPhonicsSound("phonics_word/${item.word}")
            uiState = uiState.copy(
                score = uiState.score + 1,
                solvedWords = uiState.solvedWords.mapIndexed { index, list ->
                    if (index == item.answerTeam) list + ComparisonWord(item.word) else list
                },
            )
        } else {
            wrongWords.add(item.word)
            AudioPlayerManager.playSoundWrongAnswer()
            uiState = uiState.copy(wrongCount = uiState.wrongCount + 1, shakeWrong = true)
            viewModelScope.launch {
                delay(600)
                uiState = uiState.copy(shakeWrong = false)
            }
        }
        viewModelScope.launch {
            delay(if (correct) 1000 else 1600)
            advance()
        }
    }

    fun restart() {
        quizItems = makeComparisonQuizRound(comparison)
        uiState = ComparisonDetailUiState(solvedWords = List(comparison.teams.size) { emptyList() })
        wrongWords.clear(); correctWords.clear()
        sessionStartMs = System.currentTimeMillis()
        recordedPractice = false
    }

    /** If the kid leaves without finishing the quiz, still count the time as learning. */
    fun recordLearnTimeIfNeeded() {
        if (recordedPractice) return
        val seconds = ((System.currentTimeMillis() - sessionStartMs) / 1000).toInt()
        phonicsSessions.recordLearning(
            title = "Compare: ${comparison.shortTitle}", mode = "LEARN", durationSeconds = seconds)
    }

    private fun advance() {
        val next = uiState.currentIndex + 1
        if (next >= quizItems.size) {
            uiState = uiState.copy(isFinished = true)
            recordedPractice = true
            phonicsSessions.recordPractice(
                title = "Compare: ${comparison.shortTitle}",
                score = uiState.score, total = quizItems.size,
                durationSeconds = ((System.currentTimeMillis() - sessionStartMs) / 1000).toInt(),
                wrongItems = wrongWords.toList(), correctItems = correctWords.toList(),
            )
        } else {
            uiState = uiState.copy(currentIndex = next, answeredTeam = null)
        }
    }
}

// ── Page ─────────────────────────────────────────────────────────────────────

@Composable
fun ComparisonDetailPage(
    navController: NavController,
    viewModel: ComparisonDetailViewModel = hiltViewModel(),
) {
    val comparison = viewModel.comparison
    val uiState = viewModel.uiState
    val accessVM = LocalAccessSheetViewModel.current
    val scope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        onDispose { viewModel.recordLearnTimeIfNeeded() }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = comparison.gradient, shape = comparison.shape)

        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            BackButtonWithText(
                title = comparison.shortTitle,
                onBackClick = { navController.popBackStack() }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Dimens16)
                    .padding(bottom = Dimens10),
                verticalArrangement = Arrangement.spacedBy(Dimens8),
            ) {
                RuleCard(
                    comparison = comparison,
                    modifier = Modifier.padding(top = Dimens10),
                    onLevelTap = {
                        AudioPlayerManager.playSoundMenuClick()
                        if (comparison.linkedLevelNumber > FREE_PHONICS_LEVELS) {
                            scope.launch {
                                val allowed = accessVM.checkAccess(ModuleID.PHONICS_READING_PREMIUM)
                                if (allowed) navController.navigate(comparison.linkedRoute)
                            }
                        } else {
                            navController.navigate(comparison.linkedRoute)
                        }
                    },
                )

                // Team zones — the learn area
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(Dimens10),
                ) {
                    comparison.teams.forEachIndexed { index, team ->
                        Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                            TeamZone(
                                team = team,
                                solvedWords = uiState.solvedWords.getOrElse(index) { emptyList() },
                                onWordTap = { viewModel.playWord(it) },
                                onMarkerTap = { viewModel.playWord(it) },
                            )
                        }
                    }
                }

                QuizBar(viewModel = viewModel)
            }
        }

        if (uiState.isFinished) {
            val pct = if (viewModel.totalQuestions > 0) uiState.score * 100 / viewModel.totalQuestions else 0
            ActivityCompletePopup(
                stars = if (pct >= 100) 3 else if (pct >= 70) 2 else 1,
                score = uiState.score,
                total = viewModel.totalQuestions,
                scoreLabel = "correct 🎯",
                feedbackText = if (pct >= 70) "🕵️⭐ Detective Badge!" else "Good try! 💪",
                onNext = { viewModel.restart() },
                nextLabel = "Try Again",
                onClose = { navController.popBackStack() }
            )
        }
    }
}

// ── Rule card + level link ───────────────────────────────────────────────────

@Composable
private fun RuleCard(
    comparison: PhonicsComparison,
    onLevelTap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = Dimens12, strokeColor = Color(0xFF1A237E))
            .padding(horizontal = Dimens14, vertical = Dimens8),
        verticalArrangement = Arrangement.spacedBy(Dimens4),
    ) {
        // Title row — level chip centers against the title only
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens10),
        ) {
            Text(
                text = "🕵️",
                style = MaterialTheme.typography.titleLarge.scaled(),
            )
            Text(
                text = "“${comparison.rule}”",
                style = MaterialTheme.typography.titleSmall.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A237E),
                modifier = Modifier.weight(1f),
            )

            // Level link — jump into the full lesson (premium gate for L4+)
            Row(
                modifier = Modifier
                    .background(Color(0xFF5532D2).copy(alpha = 0.12f), RoundedCornerShape(50))
                    .clip(RoundedCornerShape(50))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onLevelTap,
                    )
                    .padding(horizontal = Dimens10, vertical = Dimens4),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens4),
            ) {
                Text(
                    text = "📖 From ${comparison.linkedLevelLabel}",
                    style = MaterialTheme.typography.labelSmall.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5532D2),
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color(0xFF5532D2),
                    modifier = Modifier.size(Dimens12),
                )
            }
        }

        // Description — full card width
        Text(
            text = highlightedExplanation(comparison.explanation),
            style = MaterialTheme.typography.labelMedium.scaled(),
            color = Color(0xFF455A64),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

/** Explanation with the **marked** words rendered bold + tinted. */
private fun highlightedExplanation(text: String) = buildAnnotatedString {
    text.split("**").forEachIndexed { index, part ->
        if (index % 2 == 1) {
            withStyle(SpanStyle(color = Color(0xFF5532D2), fontWeight = FontWeight.Bold)) {
                append(part)
            }
        } else {
            append(part)
        }
    }
}

// ── Team zones (learn) ───────────────────────────────────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TeamZone(
    team: ComparisonTeam,
    solvedWords: List<ComparisonWord>,
    onWordTap: (String) -> Unit,
    onMarkerTap: (String) -> Unit,
) {
    val teamColor = Color(team.colorHex)
    // Zones start EMPTY — words fly in only when the kid sorts them right.

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(teamColor.copy(alpha = 0.08f), RoundedCornerShape(Dimens12))
            .border(Dimens1 + Dimens1 / 2, teamColor.copy(alpha = 0.35f), RoundedCornerShape(Dimens12))
            .padding(Dimens6),
        verticalArrangement = Arrangement.spacedBy(Dimens6),
    ) {
        // Zone header — tap to hear the blend's own sound (ai, ay, ck…)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(listOf(teamColor, teamColor.copy(alpha = 0.75f))),
                    RoundedCornerShape(Dimens8),
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { team.markerAudio?.let(onMarkerTap) },
                )
                .padding(horizontal = Dimens10, vertical = Dimens4),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens4, Alignment.CenterHorizontally),
        ) {
            Text(
                text = team.zoneEmoji,
                style = MaterialTheme.typography.titleSmall.scaled(),
            )
            Text(
                text = team.marker,
                style = MaterialTheme.typography.titleLarge.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
            Text(
                text = team.zoneHint,
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = Color.White.copy(alpha = 0.9f),
            )
            if (team.markerAudio != null) {
                Text(
                    text = "🔊",
                    style = MaterialTheme.typography.labelMedium.scaled(),
                )
            }
        }

        if (solvedWords.isEmpty()) {
            // Faint watermark until the first word lands here.
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { alpha = 0.30f },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens4, Alignment.CenterVertically),
            ) {
                Text(
                    text = team.zoneEmoji,
                    style = MaterialTheme.typography.headlineLarge.scaled(),
                )
                Text(
                    text = "Sort words here!",
                    style = MaterialTheme.typography.labelSmall.scaled(),
                    color = teamColor,
                )
            }
        } else {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(Dimens6),
                horizontalArrangement = Arrangement.spacedBy(Dimens6, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(Dimens6),
            ) {
                solvedWords.forEach { word ->
                    ComparisonWordCapsule(
                        word = word,
                        marker = team.highlightText,
                        teamColor = teamColor,
                        onTap = { onWordTap(word.audio) },
                    )
                }
            }
        }
    }
}

// ── Word capsule (learn) — audio + bounce + marker glow ─────────────────────

@Composable
private fun ComparisonWordCapsule(
    word: ComparisonWord,
    marker: String,
    teamColor: Color,
    onTap: () -> Unit,
) {
    var bounce by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val scale by animateFloatAsState(
        targetValue = if (bounce) 1.15f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMediumLow),
        label = "capsuleBounce",
    )

    // Word text with the team's letters highlighted in team color.
    val styledWord = buildAnnotatedString {
        val start = word.display.indexOf(marker)
        if (start < 0) {
            withStyle(SpanStyle(color = Color(0xFF263238), fontWeight = FontWeight.Bold)) {
                append(word.display)
            }
        } else {
            withStyle(SpanStyle(color = Color(0xFF263238), fontWeight = FontWeight.Bold)) {
                append(word.display.substring(0, start))
            }
            withStyle(SpanStyle(color = teamColor, fontWeight = FontWeight.Bold)) {
                append(word.display.substring(start, start + marker.length))
            }
            withStyle(SpanStyle(color = Color(0xFF263238), fontWeight = FontWeight.Bold)) {
                append(word.display.substring(start + marker.length))
            }
        }
    }

    Text(
        text = styledWord,
        style = MaterialTheme.typography.bodyMedium.scaled(),
        modifier = Modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .background(Color.White.copy(alpha = 0.92f), RoundedCornerShape(50))
            .border(Dimens1, teamColor.copy(alpha = 0.4f), RoundedCornerShape(50))
            .clip(RoundedCornerShape(50))
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
    )
}

// ── Sort-It quiz bar ─────────────────────────────────────────────────────────

@Composable
private fun QuizBar(viewModel: ComparisonDetailViewModel) {
    val comparison = viewModel.comparison
    val uiState = viewModel.uiState
    val item = viewModel.currentItem

    val answered = uiState.answeredTeam
    val isCorrect = answered != null && item != null && answered == item.answerTeam

    val blankedText = when {
        item == null -> ""
        answered != null -> item.word
        else -> item.blanked
    }
    val blankedColor = when {
        answered == null -> Color(0xFF263238)
        isCorrect -> Color(0xFF2E7D32)
        else -> Color(0xFFC62828)
    }
    val blankedBackground = when {
        answered == null -> Color(0xFFECEFF1)
        isCorrect -> Color(0xFFC8E6C9)
        else -> Color(0xFFFFCDD2)
    }
    val shakeOffset = if (uiState.shakeWrong) (sin(System.currentTimeMillis() / 50.0) * 7).roundToInt() else 0

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = Dimens12, strokeColor = Color(0xFF1A237E))
            .padding(horizontal = Dimens14, vertical = Dimens8),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens12),
    ) {
        Text(
            text = "🎮 Sort it!",
            style = MaterialTheme.typography.titleSmall.scaled(),
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A237E),
        )

        // Blanked word (shakes on wrong) — the answer plays the word's sound
        Text(
            text = blankedText,
            style = MaterialTheme.typography.headlineSmall.scaled(),
            fontWeight = FontWeight.Bold,
            color = blankedColor,
            modifier = Modifier
                .offset { IntOffset(shakeOffset, 0) }
                .background(blankedBackground, RoundedCornerShape(Dimens8))
                .padding(horizontal = Dimens12, vertical = Dimens4),
        )

        // Team answer buttons
        comparison.teams.forEachIndexed { index, team ->
            TeamAnswerButton(
                comparison = comparison,
                team = team,
                index = index,
                answeredTeam = answered,
                correctTeam = item?.answerTeam,
                onTap = { viewModel.answer(index) },
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Progress dots + score
        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(Dimens2)) {
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens3)) {
                repeat(viewModel.totalQuestions) { i ->
                    Box(
                        modifier = Modifier
                            .size(Dimens6)
                            .background(
                                if (i <= uiState.currentIndex) Color(0xFF5532D2)
                                else Color(0xFF5532D2).copy(alpha = 0.2f),
                                RoundedCornerShape(50),
                            ),
                    )
                }
            }
            Text(
                text = "✔${uiState.score}  ✘${uiState.wrongCount}",
                style = MaterialTheme.typography.labelSmall.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF546E7A),
            )
        }
    }
}

@Composable
private fun TeamAnswerButton(
    comparison: PhonicsComparison,
    team: ComparisonTeam,
    index: Int,
    answeredTeam: Int?,
    correctTeam: Int?,
    onTap: () -> Unit,
) {
    val teamColor = Color(team.colorHex)
    val answered = answeredTeam != null
    val isCorrectTeam = correctTeam == index
    val isSelected = answeredTeam == index

    val background = when {
        !answered -> teamColor
        isCorrectTeam -> Color(0xFF2E7D32)
        isSelected -> Color(0xFFC62828)
        else -> teamColor.copy(alpha = 0.35f)
    }
    val scale by animateFloatAsState(
        targetValue = if (answered && isCorrectTeam) 1.08f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMediumLow),
        label = "answerScale",
    )

    Text(
        // Sound battles share the same letters — the zone emoji tells teams apart
        text = if (comparison.group == ComparisonGroup.sound) "${team.zoneEmoji} ${team.marker}" else team.marker,
        style = MaterialTheme.typography.titleLarge.scaled(),
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .background(background, RoundedCornerShape(50))
            .clip(RoundedCornerShape(50))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = !answered,
                onClick = onTap,
            )
            .padding(horizontal = Dimens16, vertical = Dimens6),
    )
}
