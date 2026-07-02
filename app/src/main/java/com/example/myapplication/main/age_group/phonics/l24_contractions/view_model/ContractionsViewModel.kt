package com.example.myapplication.main.age_group.phonics.l24_contractions.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

// ── Models ────────────────────────────────────────────────────────────────────

data class ContractionWord(
    val word1: String,
    val word2: String,
    val contraction: String,
    val dropped: String,
    val isIrregular: Boolean = false
)

data class ContractionGroup(
    val type: String,
    val emoji: String,
    val rule: String,
    val accentColor: Color,
    val shadowColor: Color,
    val words: List<ContractionWord>
)

// ── Data ──────────────────────────────────────────────────────────────────────

val contractionGroups: List<ContractionGroup> = listOf(
    ContractionGroup(
        type = "not", emoji = "🚫", rule = "Drop the 'o' from 'not' and add apostrophe",
        accentColor = Color(0xFFC62828), shadowColor = Color(0xFFB71C1C),
        words = listOf(
            ContractionWord("do",     "not", "don't",    "o"),
            ContractionWord("did",    "not", "didn't",   "o"),
            ContractionWord("does",   "not", "doesn't",  "o"),
            ContractionWord("can",    "not", "can't",    "no"),
            ContractionWord("is",     "not", "isn't",    "o"),
            ContractionWord("are",    "not", "aren't",   "o"),
            ContractionWord("could",  "not", "couldn't", "o"),
            ContractionWord("would",  "not", "wouldn't", "o"),
            ContractionWord("should", "not", "shouldn't","o"),
            ContractionWord("will",   "not", "won't",    "ill+o", isIrregular = true)
        )
    ),
    ContractionGroup(
        type = "am/is/are", emoji = "✨", rule = "Drop the first letter(s) of am/is/are",
        accentColor = Color(0xFF2E7D32), shadowColor = Color(0xFF1B5E20),
        words = listOf(
            ContractionWord("I",     "am",  "I'm",      "a"),
            ContractionWord("you",   "are", "you're",   "a"),
            ContractionWord("he",    "is",  "he's",     "i"),
            ContractionWord("she",   "is",  "she's",    "i"),
            ContractionWord("it",    "is",  "it's",     "i"),
            ContractionWord("we",    "are", "we're",    "a"),
            ContractionWord("they",  "are", "they're",  "a"),
            ContractionWord("that",  "is",  "that's",   "i"),
            ContractionWord("there", "is",  "there's",  "i"),
            ContractionWord("who",   "is",  "who's",    "i")
        )
    ),
    ContractionGroup(
        type = "will", emoji = "🔮", rule = "Drop 'wi' from 'will' and add apostrophe",
        accentColor = Color(0xFF1565C0), shadowColor = Color(0xFF0D47A1),
        words = listOf(
            ContractionWord("I",    "will", "I'll",     "wi"),
            ContractionWord("you",  "will", "you'll",   "wi"),
            ContractionWord("he",   "will", "he'll",    "wi"),
            ContractionWord("she",  "will", "she'll",   "wi"),
            ContractionWord("we",   "will", "we'll",    "wi"),
            ContractionWord("they", "will", "they'll",  "wi"),
            ContractionWord("it",   "will", "it'll",    "wi"),
            ContractionWord("that", "will", "that'll",  "wi")
        )
    ),
    ContractionGroup(
        type = "have/had", emoji = "💎", rule = "Drop 'ha' from have/had and add apostrophe",
        accentColor = Color(0xFF6A1B9A), shadowColor = Color(0xFF4A148C),
        words = listOf(
            ContractionWord("I",    "have",  "I've",     "ha"),
            ContractionWord("you",  "have",  "you've",   "ha"),
            ContractionWord("we",   "have",  "we've",    "ha"),
            ContractionWord("they", "have",  "they've",  "ha"),
            ContractionWord("I",    "had",   "I'd",      "ha"),
            ContractionWord("you",  "had",   "you'd",    "ha"),
            ContractionWord("he",   "had",   "he'd",     "ha"),
            ContractionWord("she",  "had",   "she'd",    "ha"),
            ContractionWord("we",   "would", "we'd",     "woul"),
            ContractionWord("they", "would", "they'd",   "woul")
        )
    )
)

// ── Learn ViewModel ───────────────────────────────────────────────────────────

data class ContractionsLearnUiState(
    val selectedGroupIndex: Int = 0,
    val highlightedWord: String? = null,
    val showWords: Boolean = false
)

@HiltViewModel
class ContractionsLearnViewModel @Inject constructor() : ViewModel() {
    var uiState by mutableStateOf(ContractionsLearnUiState()); private set

    val selectedGroup: ContractionGroup get() = contractionGroups[uiState.selectedGroupIndex]

    fun onScreenAppear() {
        uiState = uiState.copy(showWords = false, highlightedWord = null)
        viewModelScope.launch {
            delay(100)
            uiState = uiState.copy(showWords = true)
        }
    }

    fun onGroupTap(index: Int) {
        if (index == uiState.selectedGroupIndex) return
        uiState = uiState.copy(highlightedWord = null, selectedGroupIndex = index)
    }

    fun onWordTap(word: ContractionWord) {
        uiState = uiState.copy(highlightedWord = word.contraction)
    }
}

// ── Practice ──────────────────────────────────────────────────────────────────

data class ContractionsPracticeQuestion(
    val word1: String,
    val word2: String,
    val correct: String,
    val options: List<String>
)

val contractionsPracticeQuestions: List<ContractionsPracticeQuestion> = listOf(
    // not
    ContractionsPracticeQuestion("do",    "not",  "don't",    listOf("don't",   "dont",     "do'nt",    "d'ont")),
    ContractionsPracticeQuestion("did",   "not",  "didn't",   listOf("didn't",  "didnt",    "did'nt",   "di'dnt")),
    ContractionsPracticeQuestion("can",   "not",  "can't",    listOf("can't",   "cant",     "ca'nt",    "c'ant")),
    ContractionsPracticeQuestion("is",    "not",  "isn't",    listOf("isn't",   "isnt",     "is'nt",    "i'snt")),
    ContractionsPracticeQuestion("are",   "not",  "aren't",   listOf("aren't",  "arent",    "are'nt",   "ar'ent")),
    ContractionsPracticeQuestion("will",  "not",  "won't",    listOf("won't",   "willn't",  "will'nt",  "wont")),
    ContractionsPracticeQuestion("could", "not",  "couldn't", listOf("couldn't","couldnt",  "could'nt", "c'ouldnt")),
    // am/is/are
    ContractionsPracticeQuestion("I",    "am",   "I'm",      listOf("I'm",     "Im",       "I'am",     "Iam")),
    ContractionsPracticeQuestion("you",  "are",  "you're",   listOf("you're",  "youre",    "you'are",  "y'oure")),
    ContractionsPracticeQuestion("he",   "is",   "he's",     listOf("he's",    "hes",      "he'is",    "h'es")),
    ContractionsPracticeQuestion("she",  "is",   "she's",    listOf("she's",   "shes",     "she'is",   "sh'es")),
    ContractionsPracticeQuestion("we",   "are",  "we're",    listOf("we're",   "were",     "we'are",   "w'ere")),
    ContractionsPracticeQuestion("they", "are",  "they're",  listOf("they're", "theyre",   "they'are", "th'eyre")),
    // will
    ContractionsPracticeQuestion("I",    "will", "I'll",     listOf("I'll",    "Il",       "I'will",   "I'l")),
    ContractionsPracticeQuestion("you",  "will", "you'll",   listOf("you'll",  "youll",    "you'will", "you'l")),
    ContractionsPracticeQuestion("he",   "will", "he'll",    listOf("he'll",   "hell",     "he'will",  "he'l")),
    ContractionsPracticeQuestion("we",   "will", "we'll",    listOf("we'll",   "well",     "we'will",  "we'l")),
    ContractionsPracticeQuestion("they", "will", "they'll",  listOf("they'll", "theyll",   "they'will","they'l")),
    // have/had
    ContractionsPracticeQuestion("I",    "have", "I've",     listOf("I've",    "Ive",      "I'have",   "I'v")),
    ContractionsPracticeQuestion("you",  "have", "you've",   listOf("you've",  "youve",    "you'have", "you'v")),
    ContractionsPracticeQuestion("they", "have", "they've",  listOf("they've", "theyve",   "they'have","they'v")),
    ContractionsPracticeQuestion("I",    "had",  "I'd",      listOf("I'd",     "Id",       "I'had",    "I'")),
    ContractionsPracticeQuestion("you",  "had",  "you'd",    listOf("you'd",   "youd",     "you'had",  "you'"))
)

data class ContractionsPracticeUiState(
    val currentIndex: Int = 0,
    val score: Int = 0,
    val selectedAnswer: String? = null,
    val isCorrect: Boolean? = null,
    val isFinished: Boolean = false,
    val shakeWrong: Boolean = false
)

@HiltViewModel
class ContractionsPracticeViewModel @Inject constructor() : ViewModel() {
    var uiState by mutableStateOf(ContractionsPracticeUiState()); private set
    private val questions = contractionsPracticeQuestions.shuffled().map { it.copy(options = it.options.shuffled()) }

    val totalQuestions: Int get() = questions.size
    val currentQuestion: ContractionsPracticeQuestion? get() = questions.getOrNull(uiState.currentIndex)

    fun onAnswerTap(answer: String) {
        if (uiState.selectedAnswer != null) return
        val q = currentQuestion ?: return
        val correct = answer == q.correct
        uiState = uiState.copy(selectedAnswer = answer, isCorrect = correct, shakeWrong = !correct)
        if (!correct) {
            viewModelScope.launch { delay(600); uiState = uiState.copy(shakeWrong = false) }
        }
        viewModelScope.launch {
            delay(if (correct) 1400L else 2000L)
            advance()
        }
    }

    fun restart() { uiState = ContractionsPracticeUiState() }

    private fun advance() {
        val next = uiState.currentIndex + 1
        val newScore = uiState.score + (if (uiState.isCorrect == true) 1 else 0)
        if (next >= questions.size) { uiState = uiState.copy(isFinished = true, score = newScore) }
        else { uiState = ContractionsPracticeUiState(currentIndex = next, score = newScore) }
    }
}
