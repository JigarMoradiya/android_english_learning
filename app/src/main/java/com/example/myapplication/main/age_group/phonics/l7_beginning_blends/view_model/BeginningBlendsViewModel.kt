package com.example.myapplication.main.age_group.phonics.l7_beginning_blends.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.progress.PhonicsLevelProgressRepository
import com.example.myapplication.data.progress.PhonicsSessionRecorder
import com.example.myapplication.main.age_group.phonics.listen.view_model.PhonicsListenLevelKey
import com.example.myapplication.utilities.AudioPhonicsManager
import com.example.myapplication.utils.AudioPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.resume

// ── Models ────────────────────────────────────────────────────────────────────

enum class BlendGroup(val label: String, val emoji: String,
                      val color: Color, val shadowColor: Color,
                      val hint: String) {
    L_BLENDS("L-Blends", "🍃", Color(0xFF00897B), Color(0xFF00695C), "bl cl fl gl pl sl"),
    R_BLENDS("R-Blends", "🌊", Color(0xFFE64A19), Color(0xFFBF360C), "br cr dr fr gr pr tr"),
    S_BLENDS("S-Blends", "⭐", Color(0xFF7B1FA2), Color(0xFF4A148C), "sm sn sp st sw sc sk"),
    W_BLENDS("W-Blends", "🌀", Color(0xFF1565C0), Color(0xFF0D47A1), "tw")
}

data class BlendWord(
    val id: String = UUID.randomUUID().toString(),
    val word: String,
    val blendLength: Int
) {
    val blendPart: String get() = word.take(blendLength)
    val restPart: String get() = word.drop(blendLength)
}

data class BlendEntry(
    val id: String = UUID.randomUUID().toString(),
    val blend: String,
    val phonetic: String,
    val soundHint: String,
    val group: BlendGroup,
    val words: List<BlendWord>
)

data class BlendPracticeQuestion(
    val id: String = UUID.randomUUID().toString(),
    val word: String,
    val blendLength: Int,
    val correctBlend: String,
    val options: List<String>,
    val imageName: String
) {
    val displayWord: String get() = "___${word.drop(blendLength)}"
}

// ── Learn UI State ─────────────────────────────────────────────────────────────

data class BlendLearnUiState(
    val selectedGroup: BlendGroup = BlendGroup.L_BLENDS,
    val selectedBlend: BlendEntry? = null,
    val highlightedWordId: String? = null,
    val showWords: Boolean = false
)

// ── Practice UI State ──────────────────────────────────────────────────────────

data class BlendPracticeUiState(
    val currentIndex: Int = 0,
    val score: Int = 0,
    val selectedAnswer: String? = null,
    val isCorrect: Boolean? = null,
    val isFinished: Boolean = false,
    val shakeWrong: Boolean = false
)

// ── Data ───────────────────────────────────────────────────────────────────────

val beginningBlendsData: List<BlendEntry> = listOf(
    // ── L-Blends ──
    BlendEntry("", "bl", "/bl/", "Say /b/ then /l/ — they run together, like 'blue'",
               BlendGroup.L_BLENDS, listOf(
        BlendWord(word="blue",  blendLength=2), BlendWord(word="black", blendLength=2),
        BlendWord(word="block", blendLength=2), BlendWord(word="blend", blendLength=2),
        BlendWord(word="blank", blendLength=2), BlendWord(word="blast", blendLength=2),
        BlendWord(word="blaze", blendLength=2), BlendWord(word="bliss", blendLength=2))),
    BlendEntry("", "cl", "/cl/", "Say /k/ then /l/ — they run together, like 'clap'",
               BlendGroup.L_BLENDS, listOf(
        BlendWord(word="clap",  blendLength=2), BlendWord(word="clay",  blendLength=2),
        BlendWord(word="clip",  blendLength=2), BlendWord(word="clock", blendLength=2),
        BlendWord(word="cloud", blendLength=2), BlendWord(word="clean", blendLength=2),
        BlendWord(word="clown", blendLength=2), BlendWord(word="cloth", blendLength=2))),
    BlendEntry("", "fl", "/fl/", "Say /f/ then /l/ — they run together, like 'flag'",
               BlendGroup.L_BLENDS, listOf(
        BlendWord(word="flag",  blendLength=2), BlendWord(word="flat",  blendLength=2),
        BlendWord(word="flip",  blendLength=2), BlendWord(word="flow",  blendLength=2),
        BlendWord(word="flea",  blendLength=2), BlendWord(word="flock", blendLength=2),
        BlendWord(word="flour", blendLength=2), BlendWord(word="flute", blendLength=2))),
    BlendEntry("", "gl", "/gl/", "Say /g/ then /l/ — they run together, like 'glow'",
               BlendGroup.L_BLENDS, listOf(
        BlendWord(word="glad",  blendLength=2), BlendWord(word="glow",  blendLength=2),
        BlendWord(word="glue",  blendLength=2), BlendWord(word="glide", blendLength=2),
        BlendWord(word="globe", blendLength=2), BlendWord(word="glare", blendLength=2))),
    BlendEntry("", "pl", "/pl/", "Say /p/ then /l/ — they run together, like 'play'",
               BlendGroup.L_BLENDS, listOf(
        BlendWord(word="plan",  blendLength=2), BlendWord(word="play",  blendLength=2),
        BlendWord(word="plus",  blendLength=2), BlendWord(word="plug",  blendLength=2),
        BlendWord(word="plum",  blendLength=2), BlendWord(word="plate", blendLength=2),
        BlendWord(word="plant", blendLength=2), BlendWord(word="plop",  blendLength=2))),
    BlendEntry("", "sl", "/sl/", "Say /s/ then /l/ — they run together, like 'sled'",
               BlendGroup.L_BLENDS, listOf(
        BlendWord(word="sled",  blendLength=2), BlendWord(word="slip",  blendLength=2),
        BlendWord(word="slam",  blendLength=2), BlendWord(word="slim",  blendLength=2),
        BlendWord(word="slow",  blendLength=2), BlendWord(word="sleep", blendLength=2),
        BlendWord(word="slap",  blendLength=2), BlendWord(word="slot",  blendLength=2))),
    // ── R-Blends ──
    BlendEntry("", "br", "/br/", "Say /b/ then /r/ — they run together, like 'brick'",
               BlendGroup.R_BLENDS, listOf(
        BlendWord(word="brag",  blendLength=2), BlendWord(word="brick", blendLength=2),
        BlendWord(word="brush", blendLength=2), BlendWord(word="brown", blendLength=2),
        BlendWord(word="brim",  blendLength=2), BlendWord(word="brave", blendLength=2),
        BlendWord(word="bread", blendLength=2), BlendWord(word="brat",  blendLength=2))),
    BlendEntry("", "cr", "/cr/", "Say /k/ then /r/ — they run together, like 'crab'",
               BlendGroup.R_BLENDS, listOf(
        BlendWord(word="crab",  blendLength=2), BlendWord(word="crop",  blendLength=2),
        BlendWord(word="crust", blendLength=2), BlendWord(word="crew",  blendLength=2),
        BlendWord(word="crisp", blendLength=2), BlendWord(word="crown", blendLength=2),
        BlendWord(word="crack", blendLength=2), BlendWord(word="cram",  blendLength=2))),
    BlendEntry("", "dr", "/dr/", "Say /d/ then /r/ — they run together, like 'drum'",
               BlendGroup.R_BLENDS, listOf(
        BlendWord(word="drag",  blendLength=2), BlendWord(word="drip",  blendLength=2),
        BlendWord(word="drop",  blendLength=2), BlendWord(word="drum",  blendLength=2),
        BlendWord(word="dress", blendLength=2), BlendWord(word="drift", blendLength=2),
        BlendWord(word="drank", blendLength=2), BlendWord(word="drove", blendLength=2))),
    BlendEntry("", "fr", "/fr/", "Say /f/ then /r/ — they run together, like 'frog'",
               BlendGroup.R_BLENDS, listOf(
        BlendWord(word="frog",  blendLength=2), BlendWord(word="from",  blendLength=2),
        BlendWord(word="free",  blendLength=2), BlendWord(word="fray",  blendLength=2),
        BlendWord(word="fresh", blendLength=2), BlendWord(word="fret",  blendLength=2),
        BlendWord(word="front", blendLength=2), BlendWord(word="frizz", blendLength=2))),
    BlendEntry("", "gr", "/gr/", "Say /g/ then /r/ — they run together, like 'grass'",
               BlendGroup.R_BLENDS, listOf(
        BlendWord(word="grab",  blendLength=2), BlendWord(word="grip",  blendLength=2),
        BlendWord(word="grin",  blendLength=2), BlendWord(word="grow",  blendLength=2),
        BlendWord(word="grass", blendLength=2), BlendWord(word="greet", blendLength=2),
        BlendWord(word="grill", blendLength=2), BlendWord(word="grunt", blendLength=2))),
    BlendEntry("", "pr", "/pr/", "Say /p/ then /r/ — they run together, like 'prize'",
               BlendGroup.R_BLENDS, listOf(
        BlendWord(word="pray",  blendLength=2), BlendWord(word="prey",  blendLength=2),
        BlendWord(word="prim",  blendLength=2), BlendWord(word="prop",  blendLength=2),
        BlendWord(word="proud", blendLength=2), BlendWord(word="print", blendLength=2),
        BlendWord(word="press", blendLength=2), BlendWord(word="prize", blendLength=2))),
    BlendEntry("", "tr", "/tr/", "Say /t/ then /r/ — they run together, like 'tree'",
               BlendGroup.R_BLENDS, listOf(
        BlendWord(word="trip",  blendLength=2), BlendWord(word="trim",  blendLength=2),
        BlendWord(word="trap",  blendLength=2), BlendWord(word="tree",  blendLength=2),
        BlendWord(word="track", blendLength=2), BlendWord(word="truck", blendLength=2),
        BlendWord(word="truth", blendLength=2), BlendWord(word="trade", blendLength=2))),
    // ── S-Blends ──
    BlendEntry("", "sm", "/sm/", "Say /s/ then /m/ — they run together, like 'smile'",
               BlendGroup.S_BLENDS, listOf(
        BlendWord(word="smell", blendLength=2), BlendWord(word="smoke", blendLength=2),
        BlendWord(word="smile", blendLength=2), BlendWord(word="small", blendLength=2),
        BlendWord(word="smack", blendLength=2), BlendWord(word="smart", blendLength=2),
        BlendWord(word="smash", blendLength=2), BlendWord(word="smug",  blendLength=2))),
    BlendEntry("", "sn", "/sn/", "Say /s/ then /n/ — they run together, like 'snap'",
               BlendGroup.S_BLENDS, listOf(
        BlendWord(word="snap",  blendLength=2), BlendWord(word="snip",  blendLength=2),
        BlendWord(word="snow",  blendLength=2), BlendWord(word="snug",  blendLength=2),
        BlendWord(word="snack", blendLength=2), BlendWord(word="snake", blendLength=2),
        BlendWord(word="snore", blendLength=2), BlendWord(word="snob",  blendLength=2))),
    BlendEntry("", "sp", "/sp/", "Say /s/ then /p/ — they run together, like 'spin'",
               BlendGroup.S_BLENDS, listOf(
        BlendWord(word="spin",  blendLength=2), BlendWord(word="spot",  blendLength=2),
        BlendWord(word="spill", blendLength=2), BlendWord(word="speak", blendLength=2),
        BlendWord(word="spend", blendLength=2), BlendWord(word="spit",  blendLength=2),
        BlendWord(word="spur",  blendLength=2), BlendWord(word="spun",  blendLength=2))),
    BlendEntry("", "st", "/st/", "Say /s/ then /t/ — they run together, like 'star'",
               BlendGroup.S_BLENDS, listOf(
        BlendWord(word="stop",  blendLength=2), BlendWord(word="step",  blendLength=2),
        BlendWord(word="stem",  blendLength=2), BlendWord(word="star",  blendLength=2),
        BlendWord(word="stick", blendLength=2), BlendWord(word="stone", blendLength=2),
        BlendWord(word="sting", blendLength=2), BlendWord(word="stir",  blendLength=2))),
    BlendEntry("", "sw", "/sw/", "Say /s/ then /w/ — they run together, like 'swim'",
               BlendGroup.S_BLENDS, listOf(
        BlendWord(word="swim",  blendLength=2), BlendWord(word="swan",  blendLength=2),
        BlendWord(word="sweet", blendLength=2), BlendWord(word="swing", blendLength=2),
        BlendWord(word="swam",  blendLength=2), BlendWord(word="swept", blendLength=2),
        BlendWord(word="swift", blendLength=2), BlendWord(word="swish", blendLength=2))),
    BlendEntry("", "sc", "/sc/", "Say /s/ then /k/ — they run together, like 'scan'",
               BlendGroup.S_BLENDS, listOf(
        BlendWord(word="scan",  blendLength=2), BlendWord(word="scar",  blendLength=2),
        BlendWord(word="scab",  blendLength=2), BlendWord(word="scam",  blendLength=2),
        BlendWord(word="scald", blendLength=2), BlendWord(word="scale", blendLength=2))),
    BlendEntry("", "sk", "/sk/", "Say /s/ then /k/ — they run together, like 'skip'",
               BlendGroup.S_BLENDS, listOf(
        BlendWord(word="skip",  blendLength=2), BlendWord(word="skin",  blendLength=2),
        BlendWord(word="sky",   blendLength=2), BlendWord(word="skull", blendLength=2),
        BlendWord(word="skill", blendLength=2), BlendWord(word="skunk", blendLength=2),
        BlendWord(word="skid",  blendLength=2), BlendWord(word="skim",  blendLength=2))),
    BlendEntry("", "tw", "/tw/", "Say /t/ then /w/ — they run together, like 'twin'",
               BlendGroup.W_BLENDS, listOf(
        BlendWord(word="twin",   blendLength=2), BlendWord(word="twelve", blendLength=2),
        BlendWord(word="twenty", blendLength=2), BlendWord(word="twist",  blendLength=2),
        BlendWord(word="twig",   blendLength=2), BlendWord(word="tweet",  blendLength=2)))
)

val beginningBlendsPracticeQuestions: List<BlendPracticeQuestion> = listOf(
    BlendPracticeQuestion(word="blue",  blendLength=2, correctBlend="bl", options=listOf("bl","cl","fl"), imageName="blue"),
    BlendPracticeQuestion(word="flag",  blendLength=2, correctBlend="fl", options=listOf("bl","fl","pl"), imageName="flag"),
    BlendPracticeQuestion(word="clap",  blendLength=2, correctBlend="cl", options=listOf("cl","gl","sl"), imageName="clap"),
    BlendPracticeQuestion(word="play",  blendLength=2, correctBlend="pl", options=listOf("pl","sl","bl"), imageName="play"),
    BlendPracticeQuestion(word="sled",  blendLength=2, correctBlend="sl", options=listOf("sl","cl","fl"), imageName="sled"),
    BlendPracticeQuestion(word="glue",  blendLength=2, correctBlend="gl", options=listOf("gl","bl","cl"), imageName="glue"),
    BlendPracticeQuestion(word="frog",  blendLength=2, correctBlend="fr", options=listOf("fr","br","gr"), imageName="frog"),
    BlendPracticeQuestion(word="tree",  blendLength=2, correctBlend="tr", options=listOf("tr","dr","gr"), imageName="tree"),
    BlendPracticeQuestion(word="crab",  blendLength=2, correctBlend="cr", options=listOf("cr","br","tr"), imageName="crab"),
    BlendPracticeQuestion(word="drum",  blendLength=2, correctBlend="dr", options=listOf("dr","fr","tr"), imageName="drum"),
    BlendPracticeQuestion(word="grass", blendLength=2, correctBlend="gr", options=listOf("gr","cr","pr"), imageName="grass"),
    BlendPracticeQuestion(word="brush", blendLength=2, correctBlend="br", options=listOf("br","dr","gr"), imageName="brush"),
    BlendPracticeQuestion(word="prize", blendLength=2, correctBlend="pr", options=listOf("pr","br","cr"), imageName="prize"),
    BlendPracticeQuestion(word="star",  blendLength=2, correctBlend="st", options=listOf("st","sp","sn"), imageName="star"),
    BlendPracticeQuestion(word="swim",  blendLength=2, correctBlend="sw", options=listOf("sw","sm","sp"), imageName="swim"),
    BlendPracticeQuestion(word="snap",  blendLength=2, correctBlend="sn", options=listOf("sn","sm","sw"), imageName="snap"),
    BlendPracticeQuestion(word="spin",  blendLength=2, correctBlend="sp", options=listOf("sp","st","sk"), imageName="spin"),
    BlendPracticeQuestion(word="smile", blendLength=2, correctBlend="sm", options=listOf("sm","sn","sw"), imageName="smile"),
    BlendPracticeQuestion(word="skip",  blendLength=2, correctBlend="sk", options=listOf("sk","sp","sc"), imageName="skip"),
    BlendPracticeQuestion(word="scan",  blendLength=2, correctBlend="sc", options=listOf("sc","sk","st"), imageName="scan"),
    BlendPracticeQuestion(word="twin",  blendLength=2, correctBlend="tw", options=listOf("tw","tr","sw"), imageName="twin"),
    BlendPracticeQuestion(word="twig",  blendLength=2, correctBlend="tw", options=listOf("tw","sw","dr"), imageName="twig")
)

// ── Learn ViewModel ────────────────────────────────────────────────────────────

@HiltViewModel
class BeginningBlendsLearnViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {

    var uiState by mutableStateOf(BlendLearnUiState()); private set
    private var animSession = UUID.randomUUID().toString()
    private var showWordsJob: Job? = null

    fun onGroupTap(group: BlendGroup) {
        animSession = UUID.randomUUID().toString()
        audioManager.stop()
        val firstBlend = beginningBlendsData.firstOrNull { it.group == group }
        uiState = BlendLearnUiState(selectedGroup = group, selectedBlend = firstBlend)
        scheduleShowWords()
    }

    fun onBlendTap(blend: BlendEntry) {
        val session = UUID.randomUUID().toString()
        animSession = session
        audioManager.stop()
        uiState = uiState.copy(selectedBlend = blend, showWords = false, highlightedWordId = null)
        showWordsJob?.cancel()
        showWordsJob = viewModelScope.launch {
            delay(150)
            if (animSession == session) uiState = uiState.copy(showWords = true)
        }
    }

    fun onWordTap(word: BlendWord) {
        val session = UUID.randomUUID().toString()
        animSession = session
        audioManager.stop()
        uiState = uiState.copy(highlightedWordId = word.id)
        audioManager.playPhonicsSound("phonics_word/${word.word}")
        audioManager.onAudioCompleted = {
            if (animSession == session) uiState = uiState.copy(highlightedWordId = null)
        }
    }

    fun onLetterSoundTap(letter: String) {
        audioManager.playPhonicsSound("phonics_letter/sound_$letter")
    }

    fun onBlendSoundTap(blend: BlendEntry) {
        audioManager.playPhonicsSound("phonics_word/${blend.blend}")
    }

    fun stop() {
        animSession = UUID.randomUUID().toString()
        showWordsJob?.cancel()
        audioManager.stop()
    }

    override fun onCleared() { super.onCleared(); stop() }

    private fun scheduleShowWords() {
        val session = animSession
        showWordsJob?.cancel()
        showWordsJob = viewModelScope.launch {
            delay(250)
            if (animSession == session) uiState = uiState.copy(showWords = true)
        }
    }
}

// ── Practice ViewModel ─────────────────────────────────────────────────────────

@HiltViewModel
class BeginningBlendsPracticeViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager,
    private val levelProgressRepo: PhonicsLevelProgressRepository,
    private val phonicsSessions: PhonicsSessionRecorder
) : ViewModel() {

    private val questions = beginningBlendsPracticeQuestions.shuffled().take(10)
    var uiState by mutableStateOf(BlendPracticeUiState()); private set

    // Parent-report session tracking
    private var sessionStartMs = System.currentTimeMillis()
    private val wrongWords = mutableListOf<String>()
    private val correctWords = mutableListOf<String>()

    val totalQuestions: Int get() = questions.size
    val currentQuestion: BlendPracticeQuestion? get() =
        questions.getOrNull(uiState.currentIndex)

    private var shakeJob: Job? = null

    fun onAnswerTap(answer: String) {
        val q = currentQuestion ?: return
        if (uiState.selectedAnswer != null) return
        val correct = answer == q.correctBlend
        uiState = uiState.copy(selectedAnswer = answer, isCorrect = correct)
        if (correct) correctWords.add(q.word) else wrongWords.add(q.word)
        if (correct) {
            uiState = uiState.copy(score = uiState.score + 1)
            audioManager.playPhonicsSound("phonics_word/${q.word}")
        } else {
            AudioPlayerManager.playSoundWrongAnswer()
            uiState = uiState.copy(shakeWrong = true)
            shakeJob?.cancel()
            shakeJob = viewModelScope.launch {
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
        sessionStartMs = System.currentTimeMillis()
        wrongWords.clear()
        correctWords.clear()
        audioManager.stop()
        uiState = BlendPracticeUiState()
    }

    fun stop() { audioManager.stop() }

    override fun onCleared() { super.onCleared(); stop() }

    private fun advance() {
        val next = uiState.currentIndex + 1
        uiState = if (next >= questions.size) {
            levelProgressRepo.recordPractice(level = PhonicsListenLevelKey.beginningBlends, score = uiState.score, total = questions.size)
            phonicsSessions.recordPractice(PhonicsListenLevelKey.beginningBlends, uiState.score, questions.size, ((System.currentTimeMillis() - sessionStartMs) / 1000).toInt(), wrongWords.toList(), correctWords.toList())
            uiState.copy(isFinished = true)
        } else {
            uiState.copy(currentIndex = next, selectedAnswer = null, isCorrect = null, shakeWrong = false)
        }
    }
}
