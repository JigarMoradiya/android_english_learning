package com.example.myapplication.main.age_group.phonics.super_quiz

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
import androidx.compose.material.icons.automirrored.filled.VolumeUp
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.myapplication.data.progress.PhonicsLevelProgressRepository
import com.example.myapplication.data.progress.PhonicsSessionRecorder
import com.example.myapplication.main.age_group.phonics.listen.view_model.ListenWord
import com.example.myapplication.main.age_group.phonics.listen.view_model.PhonicsListenLevelKey
import com.example.myapplication.main.age_group.phonics.listen.view_model.phonicsListenConfigs
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
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.extensions.scaled
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.sin

// SuperQuizPage.kt
// "Super Quiz" 🏆 — the mixed-rules retention round.
// 10 questions drawn from the levels the kid has done/started: hear the word,
// see it with its key pattern blanked (r__n), pick the right pattern from 4.
// Content + audio come straight from the existing Listen configs — no new data.
// Keep identical to iOS SuperQuizView.

// ── Rule lines (also used by the recap on the finish popup) ──────────────────

fun phonicsRuleLine(level: PhonicsListenLevelKey): String = when (level) {
    PhonicsListenLevelKey.letterSounds      -> "Every letter has its own **sound**!"
    PhonicsListenLevelKey.shortVowels       -> "Short vowels **a e i o u** say /ă ĕ ĭ ŏ ŭ/!"
    PhonicsListenLevelKey.blending          -> "**Blend** the two sounds together!"
    PhonicsListenLevelKey.cvcWords          -> "Say each sound, then **blend fast**!"
    PhonicsListenLevelKey.shortVowelRules   -> "Short vowel endings **double up**: ff ll ss zz · ck ng nk!"
    PhonicsListenLevelKey.wordFamilies      -> "Word families **rhyme** — same ending team!"
    PhonicsListenLevelKey.openSyllable      -> "**Open** syllable — the vowel at the end says its **name**!"
    PhonicsListenLevelKey.vowelTeams        -> "**Two vowels walk** — the first one talks!"
    PhonicsListenLevelKey.beginningBlends   -> "**Blends** keep both sounds: bl cr st…"
    PhonicsListenLevelKey.endingBlends      -> "Ending **blends** keep both sounds: nd mp st…"
    PhonicsListenLevelKey.digraphs          -> "**Digraphs** make ONE new sound: ch sh th wh ph qu!"
    PhonicsListenLevelKey.specialEndings    -> "Special endings **-tch -dge -nk** follow short vowels!"
    PhonicsListenLevelKey.magicE            -> "**Magic E** makes the vowel say its **name**!"
    PhonicsListenLevelKey.diphthongs        -> "**Gliding sounds**: oi oy · ou ow · au aw!"
    PhonicsListenLevelKey.rControlled       -> "**Bossy R** changes the vowel: ar or er ir ur!"
    PhonicsListenLevelKey.ighGh             -> "**igh** says /ī/ — gh can say /f/!"
    PhonicsListenLevelKey.yAsVowel          -> "**Y** is a vowel: /ī/ in fly, /ē/ in happy!"
    PhonicsListenLevelKey.threeLetterBlends -> "**Three-letter blends**: str spl spr thr scr!"
    PhonicsListenLevelKey.softCSoftG        -> "**c/g + e i y** go soft: /s/ and /j/!"
    PhonicsListenLevelKey.silentLetters     -> "Some letters are **silent**: kn wr mb gn!"
    PhonicsListenLevelKey.wordEndings       -> "Endings **-s -ing -ed** change the word's job!"
    PhonicsListenLevelKey.prefixes          -> "**Prefixes** go in FRONT and change meaning!"
    PhonicsListenLevelKey.suffixes          -> "**Suffixes** go at the END and change meaning!"
    PhonicsListenLevelKey.contractions      -> "**Contractions** squeeze two words — ' marks lost letters!"
    PhonicsListenLevelKey.consonantLe       -> "**-le** grabs the consonant before it: ta-ble!"
    PhonicsListenLevelKey.compoundWords     -> "**Two words** join into one: sun + flower!"
    PhonicsListenLevelKey.syllableDivision  -> "**Clap the beats** — split the word into syllables!"
    PhonicsListenLevelKey.sightWords        -> "**Sight words** — know them by heart! ❤️"
}

// ── Question building ────────────────────────────────────────────────────────

data class SuperQuizQuestion(
    val word: String,
    val blanked: String,             // "r__n"
    val correct: String,             // "ai" (or "a_e" for magic e)
    val options: List<String>,       // 4, shuffled
    val patternIndices: List<Int>,   // chars to tint once solved
    val level: PhonicsListenLevelKey,
)

/**
 * 10 mixed questions from the kid's done/started levels (round-robin so one
 * level can't dominate). Falls back to the first free levels for new kids.
 */
fun makeSuperQuizRound(repo: PhonicsLevelProgressRepository, count: Int = 10): List<SuperQuizQuestion> {
    var levels = PhonicsListenLevelKey.entries.filter {
        phonicsListenConfigs[it] != null && (repo.isDone(it) || repo.isStarted(it))
    }
    if (levels.isEmpty()) levels = listOf(PhonicsListenLevelKey.letterSounds, PhonicsListenLevelKey.shortVowels)

    // Distractor pool: every distinct segment spelling across all levels.
    val allSegmentTexts: Set<String> = phonicsListenConfigs.values
        .flatMap { it.words.flatMap { w -> w.segments } }
        .filter { !it.isSilent }
        .map { it.text }
        .toSet()

    val questions = mutableListOf<SuperQuizQuestion>()
    val usedWords = mutableSetOf<String>()
    val shuffledLevels = levels.shuffled()
    val levelWordPools: MutableMap<PhonicsListenLevelKey, MutableList<ListenWord>> =
        shuffledLevels.associateWith { phonicsListenConfigs[it]!!.words.shuffled().toMutableList() }.toMutableMap()

    while (questions.size < count) {
        var madeProgress = false
        for (level in shuffledLevels) {
            if (questions.size >= count) break
            val pool = levelWordPools[level] ?: continue

            var picked: SuperQuizQuestion? = null
            while (picked == null && pool.isNotEmpty()) {
                val word = pool.removeAt(0)
                if (word.word in usedWords) continue
                // Key segment = the longest audible one; needs ≥2 segments so a blank makes sense.
                if (word.segments.size < 2) continue
                val seg = word.segments.filter { !it.isSilent }
                    .maxByOrNull { it.text.replace("_", "").length } ?: continue

                val chars = word.word.toCharArray().map { it.toString() }.toMutableList()
                seg.indices.forEach { idx -> if (idx < chars.size) chars[idx] = "_" }
                val blanked = chars.joinToString("")

                val sameWordSegs = word.segments.map { it.text }.toSet()
                val cleanLen = seg.text.replace("_", "").length
                var distractors = allSegmentTexts
                    .filter { it !in sameWordSegs && it.replace("_", "").length == cleanLen }
                    .shuffled()
                if (distractors.size < 3) {
                    distractors = distractors + allSegmentTexts
                        .filter { it !in sameWordSegs && it !in distractors }
                        .shuffled()
                }
                if (distractors.size < 3) continue

                picked = SuperQuizQuestion(
                    word = word.word,
                    blanked = blanked,
                    correct = seg.text,
                    options = (distractors.take(3) + seg.text).shuffled(),
                    patternIndices = seg.indices,
                    level = level,
                )
                usedWords.add(word.word)
            }
            if (picked != null) {
                questions.add(picked)
                madeProgress = true
            }
        }
        if (!madeProgress) break
    }
    return questions.shuffled()
}

// ── ViewModel ────────────────────────────────────────────────────────────────

data class SuperQuizUiState(
    val currentIndex: Int = 0,
    val score: Int = 0,
    val wrongCount: Int = 0,
    val selectedOption: String? = null,
    val isFinished: Boolean = false,
    val shakeWrong: Boolean = false,
    val recapItems: List<ActivityRecapItem> = emptyList(),
)

@HiltViewModel
class SuperQuizViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager,
    private val levelProgressRepo: PhonicsLevelProgressRepository,
    private val phonicsSessions: PhonicsSessionRecorder,
) : ViewModel() {

    var uiState by mutableStateOf(SuperQuizUiState()); private set

    var questions: List<SuperQuizQuestion> = makeSuperQuizRound(levelProgressRepo); private set
    private var sessionStartMs = System.currentTimeMillis()
    private var recordedPractice = false
    private val wrongWords = mutableListOf<String>()
    private val correctWords = mutableListOf<String>()

    val currentQuestion: SuperQuizQuestion? get() = questions.getOrNull(uiState.currentIndex)
    val totalQuestions: Int get() = questions.size

    fun playCurrentWord() {
        val q = currentQuestion ?: return
        audioManager.playPhonicsSound("phonics_word/${q.word}")
    }

    fun playWord(word: String) = audioManager.playPhonicsSound("phonics_word/$word")

    fun answer(option: String) {
        if (uiState.selectedOption != null) return
        val q = currentQuestion ?: return
        val correct = option == q.correct
        if (correct) {
            uiState = uiState.copy(selectedOption = option, score = uiState.score + 1)
            correctWords.add(q.word)
            audioManager.playPhonicsSound("phonics_word/${q.word}")
        } else {
            uiState = uiState.copy(
                selectedOption = option,
                wrongCount = uiState.wrongCount + 1,
                shakeWrong = true,
                recapItems = uiState.recapItems +
                    ActivityRecapItem(word = q.word, rule = phonicsRuleLine(q.level)),
            )
            wrongWords.add(q.word)
            AudioPlayerManager.playSoundWrongAnswer()
            viewModelScope.launch {
                delay(600)
                uiState = uiState.copy(shakeWrong = false)
            }
        }
        viewModelScope.launch {
            delay(if (correct) 1200L else 1800L)
            advance()
        }
    }

    fun restart() {
        questions = makeSuperQuizRound(levelProgressRepo)
        uiState = SuperQuizUiState()
        wrongWords.clear(); correctWords.clear()
        sessionStartMs = System.currentTimeMillis()
        recordedPractice = false
        playCurrentWord()
    }

    /** If the kid leaves mid-round, still count the time as learning. */
    fun recordLearnTimeIfNeeded() {
        if (recordedPractice) return
        val seconds = ((System.currentTimeMillis() - sessionStartMs) / 1000).toInt()
        phonicsSessions.recordLearning(
            title = "Super Quiz", mode = "LEARN", durationSeconds = seconds)
    }

    private fun advance() {
        val next = uiState.currentIndex + 1
        if (next >= questions.size) {
            uiState = uiState.copy(isFinished = true)
            recordedPractice = true
            phonicsSessions.recordPractice(
                title = "Super Quiz",
                score = uiState.score, total = questions.size,
                durationSeconds = ((System.currentTimeMillis() - sessionStartMs) / 1000).toInt(),
                wrongItems = wrongWords.toList(), correctItems = correctWords.toList(),
            )
        } else {
            uiState = uiState.copy(currentIndex = next, selectedOption = null)
            playCurrentWord()
        }
    }

    fun stop() = audioManager.stop()

    override fun onCleared() {
        super.onCleared()
        stop()
    }
}

// ── Page ─────────────────────────────────────────────────────────────────────

@Composable
fun SuperQuizPage(
    navController: NavController,
    viewModel: SuperQuizViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState

    LaunchedEffect(Unit) { viewModel.playCurrentWord() }
    DisposableEffect(Unit) {
        onDispose {
            viewModel.recordLearnTimeIfNeeded()
            viewModel.stop()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.peachYellow, shape = KidsFloatingShape.stars)

        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            BackButtonWithText(
                title = "Super Quiz",
                onBackClick = { navController.popBackStack() }
            )

            if (viewModel.questions.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "🏆 Play some levels first,\nthen come back for the Super Quiz!",
                        style = MaterialTheme.typography.titleSmall.scaled(),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF546E7A),
                        textAlign = TextAlign.Center,
                    )
                }
            } else {
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
                        QuizWordPanel(viewModel = viewModel)
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(Dimens8),
                    ) {
                        Text(
                            text = "Which pattern is hiding?",
                            style = MaterialTheme.typography.labelMedium.scaled(),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF37474F),
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                        )
                        viewModel.currentQuestion?.let { q ->
                            q.options.forEach { option ->
                                QuizOptionButton(
                                    option = option,
                                    question = q,
                                    uiState = uiState,
                                    onTap = { viewModel.answer(option) },
                                )
                            }
                        }
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
                scoreLabel = "correct 🎯",
                feedbackText = if (pct >= 70) "🏆 Rule Master!" else "Good try! 💪",
                onNext = { viewModel.restart() },
                nextLabel = "Play Again",
                recapItems = uiState.recapItems,
                onRecapWordTap = { viewModel.playWord(it) },
                onClose = { navController.popBackStack() }
            )
        }
    }
}

// ── Word panel (left) ────────────────────────────────────────────────────────

@Composable
private fun QuizWordPanel(viewModel: SuperQuizViewModel) {
    val uiState = viewModel.uiState
    val q = viewModel.currentQuestion

    val shakeProgress by animateFloatAsState(
        targetValue = if (uiState.shakeWrong) 1f else 0f,
        animationSpec = spring(stiffness = 300f),
        label = "quizShake",
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = Dimens12, strokeColor = Color(0xFFEF6C00))
            .padding(Dimens16),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens10),
    ) {
        if (q != null) {
            Text(
                text = "👂 Listen, then fill the gap!",
                style = MaterialTheme.typography.labelMedium.scaled(),
                color = Color(0xFF546E7A),
            )

            val answered = uiState.selectedOption != null
            val display = if (answered) q.word else q.blanked
            val baseStyle = MaterialTheme.typography.displayMedium.scaled()
            Text(
                text = buildAnnotatedString {
                    display.forEachIndexed { idx, char ->
                        val color = if (answered && idx in q.patternIndices) Color(0xFF2E7D32)
                        else Color(0xFF263238)
                        withStyle(SpanStyle(color = color)) { append(char) }
                    }
                },
                style = baseStyle,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.graphicsLayer {
                    translationX = sin(shakeProgress * Math.PI.toFloat() * 3f) * 7f
                },
            )

            // Replay
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens6),
                modifier = Modifier
                    .background(Color(0xFFEF6C00), RoundedCornerShape(50))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { viewModel.playCurrentWord() }
                    .padding(horizontal = Dimens14, vertical = Dimens6),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(Dimens16),
                )
                Text(
                    text = "Hear it again",
                    style = MaterialTheme.typography.labelMedium.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }
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
                                if (i <= uiState.currentIndex) Color(0xFFEF6C00)
                                else Color(0xFFEF6C00).copy(alpha = 0.2f),
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

// ── Option button (right) ────────────────────────────────────────────────────

@Composable
private fun QuizOptionButton(
    option: String,
    question: SuperQuizQuestion,
    uiState: SuperQuizUiState,
    onTap: () -> Unit,
) {
    val answered = uiState.selectedOption != null
    val isCorrectOption = option == question.correct
    val isSelected = uiState.selectedOption == option

    val bgColor = when {
        !answered       -> Color(0xFFEF6C00)
        isCorrectOption -> Color(0xFF2E7D32)
        isSelected      -> Color(0xFFC62828)
        else            -> Color(0xFFEF6C00).copy(alpha = 0.35f)
    }
    val scale by animateFloatAsState(
        targetValue = if (answered && isCorrectOption) 1.05f else 1.0f,
        animationSpec = spring(stiffness = 300f, dampingRatio = 0.6f),
        label = "optionScale_$option",
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .background(bgColor, RoundedCornerShape(Dimens12))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = !answered,
                onClick = onTap,
            )
            .padding(vertical = Dimens8),
    ) {
        Text(
            text = option,
            style = MaterialTheme.typography.titleMedium.scaled(),
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
    }
}
