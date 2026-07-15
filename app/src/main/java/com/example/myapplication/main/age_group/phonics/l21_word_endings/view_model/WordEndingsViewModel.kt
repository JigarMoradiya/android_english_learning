package com.example.myapplication.main.age_group.phonics.l21_word_endings.view_model

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

// ── Rule ──────────────────────────────────────────────────────────────────────

enum class WordEndingRule {
    JUST_ADD, DOUBLE, DROP_E, DROP_Y;

    val label: String get() = when (this) {
        JUST_ADD -> "just add"
        DOUBLE   -> "double"
        DROP_E   -> "drop-e"
        DROP_Y   -> "y → i"
    }
    val color: Color get() = when (this) {
        JUST_ADD -> Color(0xFF2E7D32)
        DOUBLE   -> Color(0xFFE65100)
        DROP_E   -> Color(0xFF1565C0)
        DROP_Y   -> Color(0xFFAD1457)
    }
}

// ── Models ────────────────────────────────────────────────────────────────────

data class WordEndingWord(
    val id:      String = UUID.randomUUID().toString(),
    val base:    String,
    val derived: String,
    val rule:    WordEndingRule
)

data class WordEndingGroup(
    val suffix:      String,
    val suffixLen:   Int,
    val emoji:       String,
    val meaning:     String,
    val accentColor: Color,
    val shadowColor: Color,
    val words:       List<WordEndingWord>
)

// ── Data ──────────────────────────────────────────────────────────────────────

val wordEndingGroups: List<WordEndingGroup> = listOf(
    WordEndingGroup(
        suffix = "-ing", suffixLen = 3,
        emoji = "🎯", meaning = "happening NOW",
        accentColor = Color(0xFF2E7D32), shadowColor = Color(0xFF1B5E20),
        words = listOf(
            WordEndingWord(base = "jump",  derived = "jumping",  rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "walk",  derived = "walking",  rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "talk",  derived = "talking",  rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "read",  derived = "reading",  rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "play",  derived = "playing",  rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "sleep", derived = "sleeping", rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "cook",  derived = "cooking",  rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "run",   derived = "running",  rule = WordEndingRule.DOUBLE),
            WordEndingWord(base = "sit",   derived = "sitting",  rule = WordEndingRule.DOUBLE),
            WordEndingWord(base = "swim",  derived = "swimming", rule = WordEndingRule.DOUBLE),
            WordEndingWord(base = "hop",   derived = "hopping",  rule = WordEndingRule.DOUBLE),
            WordEndingWord(base = "cut",   derived = "cutting",  rule = WordEndingRule.DOUBLE),
            WordEndingWord(base = "get",   derived = "getting",  rule = WordEndingRule.DOUBLE),
            WordEndingWord(base = "shop",  derived = "shopping", rule = WordEndingRule.DOUBLE),
            WordEndingWord(base = "make",  derived = "making",   rule = WordEndingRule.DROP_E),
            WordEndingWord(base = "ride",  derived = "riding",   rule = WordEndingRule.DROP_E),
            WordEndingWord(base = "dance", derived = "dancing",  rule = WordEndingRule.DROP_E),
            WordEndingWord(base = "write", derived = "writing",  rule = WordEndingRule.DROP_E),
            WordEndingWord(base = "close", derived = "closing",  rule = WordEndingRule.DROP_E),
            WordEndingWord(base = "give",  derived = "giving",   rule = WordEndingRule.DROP_E),
        )
    ),
    WordEndingGroup(
        suffix = "-ed", suffixLen = 2,
        emoji = "📖", meaning = "happened in the PAST · says /t/ (jumped), /d/ (played) or /id/ (planted)!",
        accentColor = Color(0xFF1565C0), shadowColor = Color(0xFF0D47A1),
        words = listOf(
            WordEndingWord(base = "jump",  derived = "jumped",  rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "walk",  derived = "walked",  rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "talk",  derived = "talked",  rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "play",  derived = "played",  rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "rain",  derived = "rained",  rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "cook",  derived = "cooked",  rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "clean", derived = "cleaned", rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "clap",  derived = "clapped", rule = WordEndingRule.DOUBLE),
            WordEndingWord(base = "hop",   derived = "hopped",  rule = WordEndingRule.DOUBLE),
            WordEndingWord(base = "stop",  derived = "stopped", rule = WordEndingRule.DOUBLE),
            WordEndingWord(base = "drop",  derived = "dropped", rule = WordEndingRule.DOUBLE),
            WordEndingWord(base = "plan",  derived = "planned", rule = WordEndingRule.DOUBLE),
            WordEndingWord(base = "dance", derived = "danced",  rule = WordEndingRule.DROP_E),
            WordEndingWord(base = "bake",  derived = "baked",   rule = WordEndingRule.DROP_E),
            WordEndingWord(base = "smile", derived = "smiled",  rule = WordEndingRule.DROP_E),
            WordEndingWord(base = "love",  derived = "loved",   rule = WordEndingRule.DROP_E),
            WordEndingWord(base = "hope",  derived = "hoped",   rule = WordEndingRule.DROP_E),
            WordEndingWord(base = "move",  derived = "moved",   rule = WordEndingRule.DROP_E),
            // y → i — consonant + y: change y to i first
            WordEndingWord(base = "cry",   derived = "cried",   rule = WordEndingRule.DROP_Y),
            WordEndingWord(base = "carry", derived = "carried", rule = WordEndingRule.DROP_Y),
            WordEndingWord(base = "hurry", derived = "hurried", rule = WordEndingRule.DROP_Y),
        )
    ),
    WordEndingGroup(
        suffix = "-er", suffixLen = 2,
        emoji = "📏", meaning = "comparing TWO things",
        accentColor = Color(0xFFE65100), shadowColor = Color(0xFFBF360C),
        words = listOf(
            WordEndingWord(base = "fast",  derived = "faster",  rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "tall",  derived = "taller",  rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "cold",  derived = "colder",  rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "smart", derived = "smarter", rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "soft",  derived = "softer",  rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "cool",  derived = "cooler",  rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "deep",  derived = "deeper",  rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "clean", derived = "cleaner", rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "happy", derived = "happier", rule = WordEndingRule.DROP_Y),
            WordEndingWord(base = "big",   derived = "bigger",  rule = WordEndingRule.DOUBLE),
            WordEndingWord(base = "hot",   derived = "hotter",  rule = WordEndingRule.DOUBLE),
            WordEndingWord(base = "sad",   derived = "sadder",  rule = WordEndingRule.DOUBLE),
            WordEndingWord(base = "thin",  derived = "thinner", rule = WordEndingRule.DOUBLE),
            WordEndingWord(base = "fit",   derived = "fitter",  rule = WordEndingRule.DOUBLE),
            WordEndingWord(base = "nice",  derived = "nicer",   rule = WordEndingRule.DROP_E),
            WordEndingWord(base = "brave", derived = "braver",  rule = WordEndingRule.DROP_E),
            WordEndingWord(base = "cute",  derived = "cuter",   rule = WordEndingRule.DROP_E),
            WordEndingWord(base = "wide",  derived = "wider",   rule = WordEndingRule.DROP_E),
            WordEndingWord(base = "safe",  derived = "safer",   rule = WordEndingRule.DROP_E),
            WordEndingWord(base = "late",  derived = "later",   rule = WordEndingRule.DROP_E),
        )
    ),
    WordEndingGroup(
        suffix = "-est", suffixLen = 3,
        emoji = "🏆", meaning = "THE MOST of all!",
        accentColor = Color(0xFF6A1B9A), shadowColor = Color(0xFF4A148C),
        words = listOf(
            WordEndingWord(base = "fast",  derived = "fastest",  rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "tall",  derived = "tallest",  rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "cold",  derived = "coldest",  rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "smart", derived = "smartest", rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "soft",  derived = "softest",  rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "cool",  derived = "coolest",  rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "deep",  derived = "deepest",  rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "clean", derived = "cleanest", rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "happy", derived = "happiest", rule = WordEndingRule.DROP_Y),
            WordEndingWord(base = "big",   derived = "biggest",  rule = WordEndingRule.DOUBLE),
            WordEndingWord(base = "hot",   derived = "hottest",  rule = WordEndingRule.DOUBLE),
            WordEndingWord(base = "sad",   derived = "saddest",  rule = WordEndingRule.DOUBLE),
            WordEndingWord(base = "thin",  derived = "thinnest", rule = WordEndingRule.DOUBLE),
            WordEndingWord(base = "fit",   derived = "fittest",  rule = WordEndingRule.DOUBLE),
            WordEndingWord(base = "nice",  derived = "nicest",   rule = WordEndingRule.DROP_E),
            WordEndingWord(base = "brave", derived = "bravest",  rule = WordEndingRule.DROP_E),
            WordEndingWord(base = "cute",  derived = "cutest",   rule = WordEndingRule.DROP_E),
            WordEndingWord(base = "wide",  derived = "widest",   rule = WordEndingRule.DROP_E),
            WordEndingWord(base = "safe",  derived = "safest",   rule = WordEndingRule.DROP_E),
            WordEndingWord(base = "late",  derived = "latest",   rule = WordEndingRule.DROP_E),
        )
    ),
    WordEndingGroup(
        suffix = "-s", suffixLen = 1,
        emoji = "🐾", meaning = "MORE than one! · says /s/ (cats) or /z/ (dogs)",
        accentColor = Color(0xFF00897B), shadowColor = Color(0xFF00695C),
        words = listOf(
            WordEndingWord(base = "cat",  derived = "cats",  rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "dog",  derived = "dogs",  rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "cup",  derived = "cups",  rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "hat",  derived = "hats",  rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "pen",  derived = "pens",  rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "star", derived = "stars", rule = WordEndingRule.JUST_ADD),
        )
    ),
    WordEndingGroup(
        suffix = "-es", suffixLen = 2,
        emoji = "📦", meaning = "MORE than one — after s · x · ch · sh!",
        accentColor = Color(0xFF5D4037), shadowColor = Color(0xFF3E2723),
        words = listOf(
            WordEndingWord(base = "box",   derived = "boxes",   rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "bus",   derived = "buses",   rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "fox",   derived = "foxes",   rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "wish",  derived = "wishes",  rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "dish",  derived = "dishes",  rule = WordEndingRule.JUST_ADD),
            WordEndingWord(base = "class", derived = "classes", rule = WordEndingRule.JUST_ADD),
        )
    ),
)

// ── Learn ViewModel ───────────────────────────────────────────────────────────

data class WordEndingsLearnUiState(
    val selectedGroupIndex: Int     = 0,
    val tappedWordId:       String? = null
)

@HiltViewModel
class WordEndingsLearnViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {
    var uiState by mutableStateOf(WordEndingsLearnUiState()); private set

    val selectedGroup: WordEndingGroup get() = wordEndingGroups[uiState.selectedGroupIndex]

    fun onGroupTap(index: Int) {
        if (index == uiState.selectedGroupIndex) return
        uiState = uiState.copy(tappedWordId = null, selectedGroupIndex = index)
    }

    fun onWordTap(word: WordEndingWord) {
        val newId = if (uiState.tappedWordId == word.id) null else word.id
        uiState = uiState.copy(tappedWordId = newId)
        if (newId != null) {
            audioManager.playPhonicsSound("phonics_word/${word.derived}")
        }
    }
}

// ── Practice Questions ────────────────────────────────────────────────────────

data class WordEndingsPracticeQuestion(
    val base:    String,
    val suffix:  String,
    val correct: String,
    val options: List<String>
)

val wordEndingsPracticeQuestions: List<WordEndingsPracticeQuestion> = listOf(
    // -ing just add
    WordEndingsPracticeQuestion("jump",  "-ing", "jumping",  listOf("jumping","jumpeing","jumpping","jumped").shuffled()),
    WordEndingsPracticeQuestion("talk",  "-ing", "talking",  listOf("talking","talkking","talkeing","talked").shuffled()),
    WordEndingsPracticeQuestion("play",  "-ing", "playing",  listOf("playing","playying","plaiing","played").shuffled()),
    WordEndingsPracticeQuestion("sleep", "-ing", "sleeping", listOf("sleeping","sleping","slepping","sleeped").shuffled()),
    WordEndingsPracticeQuestion("cook",  "-ing", "cooking",  listOf("cooking","coking","cookking","cooked").shuffled()),
    // -ing double
    WordEndingsPracticeQuestion("run",   "-ing", "running",  listOf("running","runing","runned","runnning").shuffled()),
    WordEndingsPracticeQuestion("sit",   "-ing", "sitting",  listOf("sitting","siting","sitted","sittting").shuffled()),
    WordEndingsPracticeQuestion("cut",   "-ing", "cutting",  listOf("cutting","cuting","cutted","cuttting").shuffled()),
    WordEndingsPracticeQuestion("shop",  "-ing", "shopping", listOf("shopping","shoping","shoppping","shopped").shuffled()),
    // -ing drop-e
    WordEndingsPracticeQuestion("make",  "-ing", "making",   listOf("making","makeing","maked","makking").shuffled()),
    WordEndingsPracticeQuestion("write", "-ing", "writing",  listOf("writing","writeing","writting","writed").shuffled()),
    WordEndingsPracticeQuestion("give",  "-ing", "giving",   listOf("giving","giveing","givving","gived").shuffled()),
    // -ed just add
    WordEndingsPracticeQuestion("walk",  "-ed", "walked",   listOf("walked","walkked","walkes","walking").shuffled()),
    WordEndingsPracticeQuestion("jump",  "-ed", "jumped",   listOf("jumped","jumppped","jumpes","jumping").shuffled()),
    WordEndingsPracticeQuestion("rain",  "-ed", "rained",   listOf("rained","rainned","raind","raining").shuffled()),
    WordEndingsPracticeQuestion("cook",  "-ed", "cooked",   listOf("cooked","cookked","cookt","cooking").shuffled()),
    WordEndingsPracticeQuestion("clean", "-ed", "cleaned",  listOf("cleaned","cleanned","cleand","cleaning").shuffled()),
    // -ed double
    WordEndingsPracticeQuestion("clap",  "-ed", "clapped",  listOf("clapped","claped","claping","clappd").shuffled()),
    WordEndingsPracticeQuestion("drop",  "-ed", "dropped",  listOf("dropped","droped","droppped","droping").shuffled()),
    WordEndingsPracticeQuestion("plan",  "-ed", "planned",  listOf("planned","planed","plannd","planing").shuffled()),
    // -ed drop-e
    WordEndingsPracticeQuestion("bake",  "-ed", "baked",    listOf("baked","bakeed","baking","bakked").shuffled()),
    WordEndingsPracticeQuestion("love",  "-ed", "loved",    listOf("loved","loveed","loving","lovved").shuffled()),
    WordEndingsPracticeQuestion("hope",  "-ed", "hoped",    listOf("hoped","hopeed","hopping","hoppped").shuffled()),
    // -er just add
    WordEndingsPracticeQuestion("fast",  "-er", "faster",   listOf("faster","fasted","fasster","fasting").shuffled()),
    WordEndingsPracticeQuestion("smart", "-er", "smarter",  listOf("smarter","smartter","smartest","smarted").shuffled()),
    WordEndingsPracticeQuestion("cool",  "-er", "cooler",   listOf("cooler","cooller","coolier","coolest").shuffled()),
    WordEndingsPracticeQuestion("deep",  "-er", "deeper",   listOf("deeper","depper","deepper","deepest").shuffled()),
    WordEndingsPracticeQuestion("clean", "-er", "cleaner",  listOf("cleaner","cleanner","cleanier","cleanest").shuffled()),
    // -er double
    WordEndingsPracticeQuestion("big",   "-er", "bigger",   listOf("bigger","biger","bigeer","biggest").shuffled()),
    WordEndingsPracticeQuestion("hot",   "-er", "hotter",   listOf("hotter","hoter","hottest","hottter").shuffled()),
    WordEndingsPracticeQuestion("thin",  "-er", "thinner",  listOf("thinner","thiner","thinest","thinnner").shuffled()),
    // -er drop-e
    WordEndingsPracticeQuestion("nice",  "-er", "nicer",    listOf("nicer","niceer","nicest","niccer").shuffled()),
    WordEndingsPracticeQuestion("wide",  "-er", "wider",    listOf("wider","wideer","widder","widest").shuffled()),
    WordEndingsPracticeQuestion("safe",  "-er", "safer",    listOf("safer","safeer","safest","saffer").shuffled()),
    // -est just add
    WordEndingsPracticeQuestion("tall",  "-est", "tallest",  listOf("tallest","talest","talled","talliest").shuffled()),
    WordEndingsPracticeQuestion("soft",  "-est", "softest",  listOf("softest","sofest","softtest","softier").shuffled()),
    WordEndingsPracticeQuestion("cool",  "-est", "coolest",  listOf("coolest","cooliest","coolled","cooled").shuffled()),
    WordEndingsPracticeQuestion("deep",  "-est", "deepest",  listOf("deepest","deepiest","deeppest","deeper").shuffled()),
    WordEndingsPracticeQuestion("clean", "-est", "cleanest", listOf("cleanest","cleaniest","cleannest","cleaner").shuffled()),
    // -est double
    WordEndingsPracticeQuestion("big",   "-est", "biggest",  listOf("biggest","bigest","biggesst","biger").shuffled()),
    WordEndingsPracticeQuestion("thin",  "-est", "thinnest", listOf("thinnest","thinest","thinnst","thinner").shuffled()),
    WordEndingsPracticeQuestion("fit",   "-est", "fittest",  listOf("fittest","fitest","fitttest","fitter").shuffled()),
    // -est drop-e
    WordEndingsPracticeQuestion("brave", "-est", "bravest",  listOf("bravest","braveest","bravvest","braver").shuffled()),
    WordEndingsPracticeQuestion("wide",  "-est", "widest",   listOf("widest","wideest","widdest","wider").shuffled()),
    WordEndingsPracticeQuestion("late",  "-est", "latest",   listOf("latest","lateest","lattest","later").shuffled()),
    // -s / -es plurals
    WordEndingsPracticeQuestion("cat",   "-s",  "cats",     listOf("cats","cates","catss","cat").shuffled()),
    WordEndingsPracticeQuestion("dog",   "-s",  "dogs",     listOf("dogs","doges","dogss","dogz").shuffled()),
    WordEndingsPracticeQuestion("box",   "-es", "boxes",    listOf("boxes","boxs","boxies","boxess").shuffled()),
    WordEndingsPracticeQuestion("bus",   "-es", "buses",    listOf("buses","buss","busies","busess").shuffled()),
    WordEndingsPracticeQuestion("wish",  "-es", "wishes",   listOf("wishes","wishs","wishies","wishess").shuffled()),
    // y → i (tricky!)
    WordEndingsPracticeQuestion("cry",   "-ed", "cried",    listOf("cried","cryed","cride","cryd").shuffled()),
    WordEndingsPracticeQuestion("carry", "-ed", "carried",  listOf("carried","carryed","carrid","caried").shuffled()),
    WordEndingsPracticeQuestion("happy", "-er", "happier",  listOf("happier","happyer","hapier","happyier").shuffled()),
    WordEndingsPracticeQuestion("happy", "-est","happiest", listOf("happiest","happyest","hapiest","happyiest").shuffled()),
)

// ── Practice ViewModel ────────────────────────────────────────────────────────

data class WordEndingsPracticeUiState(
    val currentIndex:   Int     = 0,
    val score:          Int     = 0,
    val selectedAnswer: String? = null,
    val isCorrect:      Boolean? = null,
    val isFinished:     Boolean = false,
    val shakeWrong:     Boolean = false
)

@HiltViewModel
class WordEndingsPracticeViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager,
    private val levelProgressRepo: PhonicsLevelProgressRepository,
    private val phonicsSessions: PhonicsSessionRecorder
) : ViewModel() {
    var uiState by mutableStateOf(WordEndingsPracticeUiState()); private set

    // Parent-report session tracking
    private var sessionStartMs = System.currentTimeMillis()
    private val wrongWords = mutableListOf<String>()
    private val correctWords = mutableListOf<String>()
    private val questions = wordEndingsPracticeQuestions.shuffled()

    val totalQuestions: Int get() = questions.size
    val currentQuestion: WordEndingsPracticeQuestion? get() = questions.getOrNull(uiState.currentIndex)

    fun onAnswerTap(answer: String) {
        if (uiState.selectedAnswer != null) return
        val q = currentQuestion ?: return
        val correct = answer == q.correct
        uiState = uiState.copy(selectedAnswer = answer, isCorrect = correct, shakeWrong = !correct)
        if (correct) {
            audioManager.playPhonicsSound("phonics_word/${q.correct}")
        } else {
            AudioPlayerManager.playSoundWrongAnswer()
            viewModelScope.launch { delay(600); uiState = uiState.copy(shakeWrong = false) }
        }
        viewModelScope.launch {
            delay(if (correct) 1400L else 2000L)
            advance()
        }
    }

    fun restart() {
        sessionStartMs = System.currentTimeMillis()
        wrongWords.clear()
        correctWords.clear()
        uiState = WordEndingsPracticeUiState()
    }

    private fun advance() {
        val next     = uiState.currentIndex + 1
        val newScore = uiState.score + (if (uiState.isCorrect == true) 1 else 0)
        if (next >= questions.size) {
            uiState = WordEndingsPracticeUiState(currentIndex = uiState.currentIndex, score = newScore, isFinished = true)
            levelProgressRepo.recordPractice(level = PhonicsListenLevelKey.wordEndings, score = newScore, total = questions.size)
            phonicsSessions.recordPractice(PhonicsListenLevelKey.wordEndings, newScore, questions.size, ((System.currentTimeMillis() - sessionStartMs) / 1000).toInt(), wrongWords.toList(), correctWords.toList())
        } else {
            uiState = WordEndingsPracticeUiState(currentIndex = next, score = newScore)
        }
    }
}
