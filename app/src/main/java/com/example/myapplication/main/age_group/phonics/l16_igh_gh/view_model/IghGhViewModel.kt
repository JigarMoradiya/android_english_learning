package com.example.myapplication.main.age_group.phonics.l16_igh_gh.view_model

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
import javax.inject.Inject

// MARK: - Models

data class IghGhWord(
    val word:      String,
    val highlight: String
) {
    val pre: String get() = word.substringBefore(highlight).takeIf { word.contains(highlight) } ?: word
    val suf: String get() = word.substringAfter(highlight).takeIf { word.contains(highlight) } ?: ""
}

data class IghGhGroup(
    val pattern:     String,
    val sound:       String,
    val rule:        String,
    val emoji:       String,
    val accentColor: Color,
    val shadowColor: Color,
    val words:       List<IghGhWord>
)

// MARK: - Data

val ighGhGroups: List<IghGhGroup> = listOf(
    IghGhGroup(
        pattern = "igh", sound = "/aɪ/ long i",
        rule    = "i·g·h together make the long \"i\" sound — the g and h are both silent",
        emoji   = "🌙",
        accentColor = Color(0xFF311B92), shadowColor = Color(0xFF1A237E),
        words = listOf(
            IghGhWord("night",  "igh"), IghGhWord("light",  "igh"),
            IghGhWord("high",   "igh"), IghGhWord("tight",  "igh"),
            IghGhWord("right",  "igh"), IghGhWord("sight",  "igh"),
            IghGhWord("might",  "igh"), IghGhWord("fight",  "igh"),
            IghGhWord("bright", "igh"), IghGhWord("flight", "igh"),
            IghGhWord("sigh",   "igh"), IghGhWord("slight", "igh"),
        )
    ),
    IghGhGroup(
        pattern = "gh", sound = "silent",
        rule    = "g and h are two ghosts 👻👻 — after -ough and -augh they float by in total silence!",
        emoji   = "👻",
        accentColor = Color(0xFF00695C), shadowColor = Color(0xFF004D40),
        words = listOf(
            IghGhWord("though",  "gh"), IghGhWord("through", "gh"),
            IghGhWord("thought", "gh"), IghGhWord("bought",  "gh"),
            IghGhWord("caught",  "gh"), IghGhWord("taught",  "gh"),
            IghGhWord("dough",   "gh"), IghGhWord("brought", "gh"),
        )
    ),
    IghGhGroup(
        pattern = "gh", sound = "/f/ sound",
        rule    = "Sometimes the gh ghosts 👻 moan out loud — they say /f/ like in fun!",
        emoji   = "🎺",
        accentColor = Color(0xFFBF360C), shadowColor = Color(0xFF7F0000),
        words = listOf(
            IghGhWord("enough", "gh"), IghGhWord("laugh", "gh"),
            IghGhWord("cough",  "gh"), IghGhWord("rough", "gh"),
            IghGhWord("tough",  "gh"),
        )
    ),
    IghGhGroup(
        pattern = "eigh", sound = "/ā/ long a",
        rule    = "EIGH says /ā/ — like eight! The g and h hide again",
        emoji   = "8️⃣",
        accentColor = Color(0xFFEF6C00), shadowColor = Color(0xFFE65100),
        words = listOf(
            IghGhWord("eight",  "eigh"), IghGhWord("weigh",  "eigh"),
            IghGhWord("weight", "eigh"), IghGhWord("sleigh", "eigh"),
            IghGhWord("neigh",  "eigh"),
        )
    ),
)

// MARK: - Learn

data class IghGhLearnUiState(
    val selectedGroupIndex: Int  = 0,
    val highlightedWord:    String? = null,
    val showWords:          Boolean = false
)

@HiltViewModel
class IghGhLearnViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {
    var uiState by mutableStateOf(IghGhLearnUiState()); private set

    val selectedGroup: IghGhGroup get() = ighGhGroups[uiState.selectedGroupIndex]

    fun onScreenAppear() {
        uiState = uiState.copy(showWords = false, highlightedWord = null)
        viewModelScope.launch {
            delay(100)
            uiState = uiState.copy(showWords = true)
        }
    }

    fun onGroupTap(index: Int) {
        if (index == uiState.selectedGroupIndex) return
        // Don't reset showWords — AnimatedContent handles the cross-fade.
        // Resetting it causes the old content to drop its word grid immediately
        // before the fade-out starts, producing the double-animation glitch.
        uiState = uiState.copy(highlightedWord = null, selectedGroupIndex = index)
    }

    fun onWordTap(word: IghGhWord) {
        // Set highlight and keep it until another word is tapped (radio-button UX, matches iOS).
        uiState = uiState.copy(highlightedWord = word.word)
        audioManager.playPhonicsSound("phonics_word/${word.word}")
    }
}

// MARK: - Practice

data class IghGhPracticeQuestion(
    val word:    String,
    val answer:  String,
    val options: List<String>
) {
    val blank: String get() = if (answer == "igh") "___" else "__"
    val displayWord: String get() = word.replace(answer, blank)
}

val ighGhPracticeQuestions: List<IghGhPracticeQuestion> = listOf(
    // igh
    IghGhPracticeQuestion("night",  "igh", listOf("igh", "gh", "g",   "ugh")),
    IghGhPracticeQuestion("light",  "igh", listOf("igh", "gh", "ugh", "g"  )),
    IghGhPracticeQuestion("high",   "igh", listOf("igh", "gh", "g",   "ig" )),
    IghGhPracticeQuestion("tight",  "igh", listOf("igh", "ig", "gh",  "g"  )),
    IghGhPracticeQuestion("right",  "igh", listOf("igh", "gh", "g",   "ig" )),
    IghGhPracticeQuestion("fight",  "igh", listOf("igh", "ugh","gh",  "g"  )),
    IghGhPracticeQuestion("bright", "igh", listOf("igh", "gh", "g",   "ig" )),
    // silent gh
    IghGhPracticeQuestion("though",  "gh", listOf("gh", "igh", "g", "ugh")),
    IghGhPracticeQuestion("thought", "gh", listOf("gh", "igh", "g", "ig" )),
    IghGhPracticeQuestion("bought",  "gh", listOf("gh", "igh", "g", "ug" )),
    IghGhPracticeQuestion("caught",  "gh", listOf("gh", "igh", "g", "ig" )),
    IghGhPracticeQuestion("taught",  "gh", listOf("gh", "igh", "g", "ug" )),
    // gh=/f/
    IghGhPracticeQuestion("enough", "gh", listOf("gh", "igh", "g", "ig")),
    IghGhPracticeQuestion("laugh",  "gh", listOf("gh", "igh", "g", "ug")),
    IghGhPracticeQuestion("cough",  "gh", listOf("gh", "igh", "g", "ig")),
    IghGhPracticeQuestion("rough",  "gh", listOf("gh", "igh", "g", "ug")),
    IghGhPracticeQuestion("tough",  "gh", listOf("gh", "igh", "g", "ig")),
)

data class IghGhPracticeUiState(
    val currentIndex:   Int     = 0,
    val score:          Int     = 0,
    val selectedAnswer: String? = null,
    val isCorrect:      Boolean? = null,
    val isFinished:     Boolean = false,
    val shakeWrong:     Boolean = false
)

@HiltViewModel
class IghGhPracticeViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager,
    private val levelProgressRepo: PhonicsLevelProgressRepository,
    private val phonicsSessions: PhonicsSessionRecorder
) : ViewModel() {
    var uiState by mutableStateOf(IghGhPracticeUiState()); private set

    // Parent-report session tracking
    private var sessionStartMs = System.currentTimeMillis()
    private val wrongWords = mutableListOf<String>()
    private val correctWords = mutableListOf<String>()
    private val questions = ighGhPracticeQuestions.shuffled().take(10)

    val totalQuestions: Int get() = questions.size
    val currentQuestion: IghGhPracticeQuestion? get() = questions.getOrNull(uiState.currentIndex)

    fun onAnswerTap(answer: String) {
        if (uiState.selectedAnswer != null) return
        val q = currentQuestion ?: return
        val correct = answer == q.answer
        uiState = uiState.copy(selectedAnswer = answer, isCorrect = correct)
        if (correct) correctWords.add(q.word) else wrongWords.add(q.word)
        if (correct) {
            audioManager.playPhonicsSound("phonics_word/${q.word}")
        } else {
            AudioPlayerManager.playSoundWrongAnswer()
            viewModelScope.launch {
                delay(600); uiState = uiState.copy(shakeWrong = false)
            }
            uiState = uiState.copy(shakeWrong = true)
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
        uiState = IghGhPracticeUiState()
    }

    private fun advance() {
        val next = uiState.currentIndex + 1
        val newScore = uiState.score + (if (uiState.isCorrect == true) 1 else 0)
        if (next >= questions.size) {
            uiState = uiState.copy(isFinished = true, score = newScore)
            levelProgressRepo.recordPractice(level = PhonicsListenLevelKey.ighGh, score = newScore, total = questions.size)
            phonicsSessions.recordPractice(PhonicsListenLevelKey.ighGh, newScore, questions.size, ((System.currentTimeMillis() - sessionStartMs) / 1000).toInt(), wrongWords.toList(), correctWords.toList())
        }
        else { uiState = IghGhPracticeUiState(currentIndex = next, score = newScore) }
    }
}
