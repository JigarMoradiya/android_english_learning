package com.example.myapplication.main.age_group.phonics.reading_ladder

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.myapplication.data.progress.PhonicsSessionRecorder
import com.example.myapplication.main.common.ActivityCompletePopup
import com.example.myapplication.main.common.ActivityRecapItem
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.kidsGlassCard
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens14
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.utilities.AudioPhonicsManager
import com.example.myapplication.utilities.TextToSpeechManager
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.extensions.scaled
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.sin

// WordDetectivePage.kt
// "Word Detective" 👾🪜 — the Reading Ladder as a game.
// A word appears (real 🌍 or alien 👾) and the kid taps the ladder rung whose
// rule unlocks it. Alien words can't be memorized — only decoded, so a correct
// answer proves real reading. Keep identical to iOS WordDetectiveView.

// ── Data ─────────────────────────────────────────────────────────────────────

data class DetectiveRung(
    val id: Int,               // 0…4, answer index
    val emoji: String,
    val title: String,
    val color: Color,
)

val detectiveRungs = listOf(
    DetectiveRung(0, "✨", "Magic E",       Color(0xFF880E4F)),
    DetectiveRung(1, "🤝", "Vowel Team",    Color(0xFFEF6C00)),
    DetectiveRung(2, "🌀", "Bossy R",       Color(0xFF1A237E)),
    DetectiveRung(3, "🔓", "Open Syllable", Color(0xFF6A1B9A)),
    DetectiveRung(4, "🔤", "Sound It Out",  Color(0xFFAD1457)),
)

data class DetectiveWord(
    val word: String,
    val rung: Int,             // index into detectiveRungs
    val isAlien: Boolean,
    val highlight: List<Int>,  // character indices tinted when solved
)

private fun real(w: String, rung: Int, hi: List<Int>) = DetectiveWord(w, rung, isAlien = false, highlight = hi)
private fun alien(w: String, rung: Int, hi: List<Int>) = DetectiveWord(w, rung, isAlien = true, highlight = hi)

/**
 * Pool — every word matches exactly ONE rung (open = ends in a vowel,
 * sound-it-out = closed short-vowel CVC), so answers are never ambiguous.
 */
val detectiveWordPool = listOf(
    // ✨ Magic E
    real("cake", 0, listOf(1, 3)), real("kite", 0, listOf(1, 3)),
    alien("zade", 0, listOf(1, 3)), alien("mipe", 0, listOf(1, 3)),
    alien("vune", 0, listOf(1, 3)), alien("dake", 0, listOf(1, 3)),
    // 🤝 Vowel team
    real("rain", 1, listOf(1, 2)), real("moon", 1, listOf(1, 2)),
    alien("soat", 1, listOf(1, 2)), alien("feen", 1, listOf(1, 2)),
    alien("jaip", 1, listOf(1, 2)), alien("toaf", 1, listOf(1, 2)),
    // 🌀 Bossy R
    real("bird", 2, listOf(1, 2)), real("star", 2, listOf(2, 3)),
    alien("chirn", 2, listOf(2, 3)), alien("morp", 2, listOf(1, 2)),
    alien("gurt", 2, listOf(1, 2)), alien("ferb", 2, listOf(1, 2)),
    // 🔓 Open syllable
    real("go", 3, listOf(1)), real("hi", 3, listOf(1)),
    alien("blo", 3, listOf(2)), alien("flo", 3, listOf(2)), alien("gri", 3, listOf(2)),
    // 🔤 Sound it out
    real("sun", 4, listOf(0, 1, 2)), real("cat", 4, listOf(0, 1, 2)),
    alien("vop", 4, listOf(0, 1, 2)), alien("zin", 4, listOf(0, 1, 2)),
    alien("mab", 4, listOf(0, 1, 2)), alien("hep", 4, listOf(0, 1, 2)),
)

/** One round: 8 words, every rung represented at least once, rest random. */
fun makeDetectiveRound(count: Int = 8): List<DetectiveWord> {
    val byRung = detectiveWordPool.groupBy { it.rung }
    val rungCount = detectiveRungs.size
    val base = count / rungCount
    val extra = count % rungCount
    val luckyRungs = (0 until rungCount).shuffled().take(extra).toSet()
    val round = mutableListOf<DetectiveWord>()
    for (rung in 0 until rungCount) {
        val take = base + (if (rung in luckyRungs) 1 else 0)
        round += (byRung[rung] ?: emptyList()).shuffled().take(take)
    }
    return round.shuffled()
}

// ── ViewModel ────────────────────────────────────────────────────────────────

data class WordDetectiveUiState(
    val currentIndex: Int = 0,
    val score: Int = 0,
    val wrongCount: Int = 0,
    val solvedRung: Int? = null,          // correct rung tapped for the current word
    val disabledRungs: Set<Int> = emptySet(), // wrong tries, greyed out
    val isFinished: Boolean = false,
    val shakeWrong: Boolean = false,
    val recapItems: List<ActivityRecapItem> = emptyList(),
)

/** Rung → one-line rule reminder for the finish-popup recap. */
private val rungRules = listOf(
    "**Magic E** makes the vowel say its **name**!",
    "**Two vowels walk** — the first one talks!",
    "**Bossy R** changes the vowel: ar or er ir ur!",
    "**Open** syllable — the vowel at the end says its **name**!",
    "Say each sound, then **blend fast**!",
)

@HiltViewModel
class WordDetectiveViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager,
    private val ttsManager: TextToSpeechManager,
    private val phonicsSessions: PhonicsSessionRecorder,
) : ViewModel() {

    var uiState by mutableStateOf(WordDetectiveUiState()); private set

    private var roundWords = makeDetectiveRound()
    private var sessionStartMs = System.currentTimeMillis()
    private var recordedPractice = false
    private val wrongWords = mutableListOf<String>()
    private val correctWords = mutableListOf<String>()

    val currentWord: DetectiveWord? get() = roundWords.getOrNull(uiState.currentIndex)
    val totalQuestions: Int get() = roundWords.size

    fun answer(rung: Int) {
        if (uiState.solvedRung != null) return
        val word = currentWord ?: return
        if (rung in uiState.disabledRungs) return

        if (rung == word.rung) {
            // Only a clean first try earns the point.
            val cleanTry = uiState.disabledRungs.isEmpty()
            uiState = uiState.copy(
                solvedRung = rung,
                score = uiState.score + (if (cleanTry) 1 else 0),
            )
            if (cleanTry) correctWords.add(word.word)
            playWord(word)
            viewModelScope.launch {
                delay(1400)
                advance()
            }
        } else {
            val firstWrong = uiState.disabledRungs.isEmpty()
            uiState = uiState.copy(
                disabledRungs = uiState.disabledRungs + rung,
                wrongCount = uiState.wrongCount + (if (firstWrong) 1 else 0), // count the word wrong once
                shakeWrong = true,
                recapItems = if (firstWrong)
                    uiState.recapItems + ActivityRecapItem(word = word.word, rule = rungRules[word.rung])
                else uiState.recapItems,
            )
            if (firstWrong) wrongWords.add(word.word)
            AudioPlayerManager.playSoundWrongAnswer()
            viewModelScope.launch {
                delay(600)
                uiState = uiState.copy(shakeWrong = false)
            }
        }
    }

    /** Recap chips: real words have recordings, alien words go through TTS. */
    fun playRecapWord(text: String) {
        val match = roundWords.firstOrNull { it.word == text }
        if (match != null) playWord(match)
        else ttsManager.speak(text, utteranceId = "detective_$text")
    }

    /** Real words play their recording; alien words can only exist through TTS. */
    private fun playWord(word: DetectiveWord) {
        val fileName = "phonics_word/${word.word}"
        if (!word.isAlien && audioManager.audioExists(fileName)) {
            audioManager.playPhonicsSound(fileName)
        } else {
            ttsManager.speak(word.word, utteranceId = "detective_${word.word}")
        }
    }

    fun restart() {
        roundWords = makeDetectiveRound()
        uiState = WordDetectiveUiState()
        wrongWords.clear(); correctWords.clear()
        sessionStartMs = System.currentTimeMillis()
        recordedPractice = false
    }

    /** If the kid leaves mid-round, still count the time as learning. */
    fun recordLearnTimeIfNeeded() {
        if (recordedPractice) return
        val seconds = ((System.currentTimeMillis() - sessionStartMs) / 1000).toInt()
        phonicsSessions.recordLearning(
            title = "Word Detective", mode = "LEARN", durationSeconds = seconds)
    }

    private fun advance() {
        val next = uiState.currentIndex + 1
        if (next >= roundWords.size) {
            uiState = uiState.copy(isFinished = true)
            recordedPractice = true
            phonicsSessions.recordPractice(
                title = "Word Detective",
                score = uiState.score, total = roundWords.size,
                durationSeconds = ((System.currentTimeMillis() - sessionStartMs) / 1000).toInt(),
                wrongItems = wrongWords.toList(), correctItems = correctWords.toList(),
            )
        } else {
            uiState = uiState.copy(currentIndex = next, solvedRung = null, disabledRungs = emptySet())
        }
    }

    fun stop() {
        audioManager.stop()
        ttsManager.stop()
    }

    override fun onCleared() {
        super.onCleared()
        stop()
    }
}

// ── Page ─────────────────────────────────────────────────────────────────────

@Composable
fun WordDetectivePage(
    navController: NavController,
    viewModel: WordDetectiveViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState

    DisposableEffect(Unit) {
        onDispose {
            viewModel.recordLearnTimeIfNeeded()
            viewModel.stop()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.skyLavender, shape = KidsFloatingShape.stars)

        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            BackButtonWithText(
                title = "Word Detective",
                onBackClick = { navController.popBackStack() }
            )

            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = Dimens20)
                    .padding(bottom = Dimens12),
                horizontalArrangement = Arrangement.spacedBy(Dimens16),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    WordPanel(viewModel = viewModel)
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(Dimens8),
                ) {
                    detectiveRungs.forEach { rung ->
                        RungButton(rung = rung, uiState = uiState, onTap = { viewModel.answer(rung.id) })
                    }
                }
            }
        }

        if (uiState.isFinished) {
            val pct = if (viewModel.totalQuestions > 0) uiState.score * 100 / viewModel.totalQuestions else 0
            ActivityCompletePopup(
                stars = if (pct >= 100) 3 else if (pct >= 70) 2 else 1,
                score = uiState.score,
                total = viewModel.totalQuestions,
                scoreLabel = "words solved 🕵️",
                feedbackText = if (pct >= 70) "👾⭐ Alien Reader!" else "Good try! 💪",
                onNext = { viewModel.restart() },
                nextLabel = "Play Again",
                recapItems = uiState.recapItems,
                onRecapWordTap = { viewModel.playRecapWord(it) },
                onClose = { navController.popBackStack() }
            )
        }
    }
}

// ── Word panel (left) ────────────────────────────────────────────────────────

@Composable
private fun WordPanel(viewModel: WordDetectiveViewModel) {
    val uiState = viewModel.uiState
    val word = viewModel.currentWord

    // Wrong-answer shake, mirroring the comparison quiz.
    val shakeProgress by animateFloatAsState(
        targetValue = if (uiState.shakeWrong) 1f else 0f,
        animationSpec = spring(stiffness = 300f),
        label = "detectiveShake",
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = Dimens12, strokeColor = Color(0xFF1A237E))
            .padding(Dimens16),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens10),
    ) {
        if (word != null) {
            val badgeColor = if (word.isAlien) Color(0xFF5532D2) else Color(0xFF2E7D32)
            Text(
                text = if (word.isAlien) "👾 Alien word!" else "🌍 Real word",
                style = MaterialTheme.typography.labelMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = badgeColor,
                modifier = Modifier
                    .background(badgeColor.copy(alpha = 0.12f), RoundedCornerShape(50))
                    .padding(horizontal = Dimens10, vertical = Dimens4),
            )

            // The word — pattern letters light up when solved
            val solved = uiState.solvedRung != null
            val rungColor = detectiveRungs[word.rung].color
            val baseStyle = MaterialTheme.typography.displayMedium.scaled()
            Text(
                text = buildAnnotatedString {
                    word.word.forEachIndexed { idx, char ->
                        val color = if (solved && idx in word.highlight) rungColor else Color(0xFF263238)
                        withStyle(SpanStyle(color = color)) { append(char) }
                    }
                },
                style = baseStyle,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.graphicsLayer {
                    translationX = sin(shakeProgress * Math.PI.toFloat() * 3f) * 7f
                },
            )

            Text(
                text = if (solved)
                    "✓ ${detectiveRungs[word.rung].emoji} ${detectiveRungs[word.rung].title}!"
                else
                    "Which rule unlocks this word?",
                style = MaterialTheme.typography.labelMedium.scaled(),
                color = if (solved) rungColor else Color(0xFF546E7A),
            )
        }

        // Progress dots + score
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens8),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens4 / 2)) {
                repeat(viewModel.totalQuestions) { i ->
                    Box(
                        modifier = Modifier
                            .size(Dimens6)
                            .background(
                                if (i <= uiState.currentIndex) Color(0xFF5532D2)
                                else Color(0xFF5532D2).copy(alpha = 0.2f),
                                CircleShape,
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

// ── Rung button (right board) ────────────────────────────────────────────────

@Composable
private fun RungButton(
    rung: DetectiveRung,
    uiState: WordDetectiveUiState,
    onTap: () -> Unit,
) {
    val isCorrect = uiState.solvedRung == rung.id
    val isDisabled = rung.id in uiState.disabledRungs
    val solved = uiState.solvedRung != null

    val scale by animateFloatAsState(
        targetValue = if (isCorrect) 1.04f else 1.0f,
        animationSpec = spring(stiffness = 300f, dampingRatio = 0.6f),
        label = "rungScale_${rung.id}",
    )
    val bgColor = when {
        isCorrect  -> Color(0xFF2E7D32)
        isDisabled -> rung.color.copy(alpha = 0.30f)
        else       -> rung.color
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens8),
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .background(bgColor, RoundedCornerShape(Dimens12))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = !solved && !isDisabled,
                onClick = onTap,
            )
            .padding(horizontal = Dimens14, vertical = Dimens10),
    ) {
        Text(text = rung.emoji, style = MaterialTheme.typography.titleSmall.scaled())
        Text(
            text = rung.title,
            style = MaterialTheme.typography.titleSmall.scaled(),
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
        Spacer(modifier = Modifier.weight(1f))
        if (isCorrect) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(Dimens16),
            )
        }
    }
}
