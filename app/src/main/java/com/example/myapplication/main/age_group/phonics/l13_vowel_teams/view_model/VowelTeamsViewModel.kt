package com.example.myapplication.main.age_group.phonics.l13_vowel_teams.view_model

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

// ── Models ────────────────────────────────────────────────────────────────────

enum class VowelTeamGroup(
    val label: String,
    val emoji: String,
    val color: Color,
    val shadowColor: Color,
    val rule: String,
    val hint: String,
    val teams: String
) {
    AI_AY("AI / AY", "🌧️", Color(0xFFD32F2F), Color(0xFFB71C1C),
        "AI and AY together say /ā/ (long A)",
        "rain tail day play",
        "ai · ay"),
    EE_EA("EE / EA", "🌿", Color(0xFF00897B), Color(0xFF00695C),
        "EE and EA together say /ē/ (long E)",
        "feet tree read team",
        "ee · ea"),
    OA_OW("OA / OW", "🌊", Color(0xFF1565C0), Color(0xFF0D47A1),
        "OA and OW together say /ō/ (long O)",
        "boat coat snow grow",
        "oa · ow"),
    OO_LONG("OO · moon", "🌙", Color(0xFF5E35B1), Color(0xFF4527A0),
        "OO usually says /oo/ — long like a ghost: \"oooo\" 👻",
        "moon food zoo pool",
        "oo 🌙"),
    OO_SHORT("OO · book", "📖", Color(0xFFE65100), Color(0xFFBF360C),
        "A small OO family says a quick /u/ — no rule, learn them by heart! Tip: oo + k almost always sounds like book",
        "book look good foot",
        "oo 📖"),
    EW_UE_UI("EW / UE / UI", "💧", Color(0xFF0277BD), Color(0xFF01579B),
        "EW, UE and UI all say /oo/ — just like the moon sound!",
        "new blue glue fruit",
        "ew · ue · ui"),
    EA_SHORT("EA · bread", "🍞", Color(0xFF6D4C41), Color(0xFF4E342E),
        "A few EA words say short /e/ (like bread) — no rule, learn this little family by heart!",
        "bread head ready",
        "ea 🍞")
}

data class VowelTeamWord(
    val id: String = UUID.randomUUID().toString(),
    val word: String,
    val teamStart: Int,
    val teamLength: Int
) {
    val prefix: String get() = word.take(teamStart)
    val teamPart: String get() = word.substring(teamStart, teamStart + teamLength)
    val suffix: String get() = word.drop(teamStart + teamLength)
}

data class VowelTeamPracticeQuestion(
    val id: String = UUID.randomUUID().toString(),
    val word: String,
    val correctTeam: String,
    val options: List<String>,
    val teamStart: Int,
    val teamLength: Int
)

// ── Data ──────────────────────────────────────────────────────────────────────

val vowelTeamWordData: Map<VowelTeamGroup, List<VowelTeamWord>> = mapOf(
    VowelTeamGroup.AI_AY to listOf(
        VowelTeamWord(word = "rain",  teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "tail",  teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "wait",  teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "mail",  teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "pain",  teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "day",   teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "play",  teamStart = 2, teamLength = 2),
        VowelTeamWord(word = "say",   teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "stay",  teamStart = 2, teamLength = 2),
        VowelTeamWord(word = "clay",  teamStart = 2, teamLength = 2),
    ),
    VowelTeamGroup.EE_EA to listOf(
        VowelTeamWord(word = "feet",  teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "see",   teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "tree",  teamStart = 2, teamLength = 2),
        VowelTeamWord(word = "keep",  teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "green", teamStart = 2, teamLength = 2),
        VowelTeamWord(word = "eat",   teamStart = 0, teamLength = 2),
        VowelTeamWord(word = "read",  teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "team",  teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "beach", teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "dream", teamStart = 2, teamLength = 2),
    ),
    VowelTeamGroup.OA_OW to listOf(
        VowelTeamWord(word = "boat",  teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "coat",  teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "goat",  teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "road",  teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "toast", teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "snow",  teamStart = 2, teamLength = 2),
        VowelTeamWord(word = "blow",  teamStart = 2, teamLength = 2),
        VowelTeamWord(word = "grow",  teamStart = 2, teamLength = 2),
        VowelTeamWord(word = "flow",  teamStart = 2, teamLength = 2),
        VowelTeamWord(word = "show",  teamStart = 2, teamLength = 2),
    ),
    VowelTeamGroup.OO_LONG to listOf(
        VowelTeamWord(word = "moon",  teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "food",  teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "zoo",   teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "roof",  teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "spoon", teamStart = 2, teamLength = 2),
        VowelTeamWord(word = "cool",  teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "pool",  teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "boot",  teamStart = 1, teamLength = 2),
    ),
    VowelTeamGroup.OO_SHORT to listOf(
        VowelTeamWord(word = "book",  teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "look",  teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "took",  teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "cook",  teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "hook",  teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "good",  teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "wood",  teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "foot",  teamStart = 1, teamLength = 2),
    ),
    VowelTeamGroup.EW_UE_UI to listOf(
        VowelTeamWord(word = "new",   teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "grew",  teamStart = 2, teamLength = 2),
        VowelTeamWord(word = "flew",  teamStart = 2, teamLength = 2),
        VowelTeamWord(word = "chew",  teamStart = 2, teamLength = 2),
        VowelTeamWord(word = "blue",  teamStart = 2, teamLength = 2),
        VowelTeamWord(word = "glue",  teamStart = 2, teamLength = 2),
        VowelTeamWord(word = "true",  teamStart = 2, teamLength = 2),
        VowelTeamWord(word = "clue",  teamStart = 2, teamLength = 2),
        VowelTeamWord(word = "fruit", teamStart = 2, teamLength = 2),
        VowelTeamWord(word = "juice", teamStart = 1, teamLength = 2),
    ),
    VowelTeamGroup.EA_SHORT to listOf(
        VowelTeamWord(word = "bread",  teamStart = 2, teamLength = 2),
        VowelTeamWord(word = "head",   teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "thread", teamStart = 3, teamLength = 2),
        VowelTeamWord(word = "spread", teamStart = 3, teamLength = 2),
        VowelTeamWord(word = "ready",  teamStart = 1, teamLength = 2),
        VowelTeamWord(word = "heavy",  teamStart = 1, teamLength = 2),
    )
)

val vowelTeamPracticeQuestions: List<VowelTeamPracticeQuestion> = listOf(
    VowelTeamPracticeQuestion(word = "rain",  correctTeam = "ai", options = listOf("ai", "ee", "oa"), teamStart = 1, teamLength = 2),
    VowelTeamPracticeQuestion(word = "feet",  correctTeam = "ee", options = listOf("ee", "ai", "ow"), teamStart = 1, teamLength = 2),
    VowelTeamPracticeQuestion(word = "boat",  correctTeam = "oa", options = listOf("oa", "ai", "ee"), teamStart = 1, teamLength = 2),
    VowelTeamPracticeQuestion(word = "day",   correctTeam = "ay", options = listOf("ay", "ow", "ea"), teamStart = 1, teamLength = 2),
    VowelTeamPracticeQuestion(word = "read",  correctTeam = "ea", options = listOf("ea", "oa", "ay"), teamStart = 1, teamLength = 2),
    VowelTeamPracticeQuestion(word = "snow",  correctTeam = "ow", options = listOf("ow", "ai", "ee"), teamStart = 2, teamLength = 2),
    VowelTeamPracticeQuestion(word = "tail",  correctTeam = "ai", options = listOf("ai", "ea", "ow"), teamStart = 1, teamLength = 2),
    VowelTeamPracticeQuestion(word = "tree",  correctTeam = "ee", options = listOf("ee", "oa", "ay"), teamStart = 2, teamLength = 2),
    VowelTeamPracticeQuestion(word = "coat",  correctTeam = "oa", options = listOf("oa", "ee", "ai"), teamStart = 1, teamLength = 2),
    VowelTeamPracticeQuestion(word = "play",  correctTeam = "ay", options = listOf("ay", "ea", "oa"), teamStart = 2, teamLength = 2),
    VowelTeamPracticeQuestion(word = "team",  correctTeam = "ea", options = listOf("ea", "ai", "ow"), teamStart = 1, teamLength = 2),
    VowelTeamPracticeQuestion(word = "grow",  correctTeam = "ow", options = listOf("ow", "ee", "ai"), teamStart = 2, teamLength = 2),
    VowelTeamPracticeQuestion(word = "wait",  correctTeam = "ai", options = listOf("ai", "oa", "ee"), teamStart = 1, teamLength = 2),
    VowelTeamPracticeQuestion(word = "beach", correctTeam = "ea", options = listOf("ea", "ow", "ay"), teamStart = 1, teamLength = 2),
    VowelTeamPracticeQuestion(word = "green", correctTeam = "ee", options = listOf("ee", "oa", "ai"), teamStart = 2, teamLength = 2),
    VowelTeamPracticeQuestion(word = "moon",  correctTeam = "oo", options = listOf("oo", "ee", "oa"), teamStart = 1, teamLength = 2),
    VowelTeamPracticeQuestion(word = "book",  correctTeam = "oo", options = listOf("oo", "ai", "ee"), teamStart = 1, teamLength = 2),
    VowelTeamPracticeQuestion(word = "food",  correctTeam = "oo", options = listOf("oo", "oa", "ay"), teamStart = 1, teamLength = 2),
    VowelTeamPracticeQuestion(word = "look",  correctTeam = "oo", options = listOf("oo", "ea", "ai"), teamStart = 1, teamLength = 2),
    VowelTeamPracticeQuestion(word = "new",   correctTeam = "ew", options = listOf("ew", "oo", "ay"), teamStart = 1, teamLength = 2),
    VowelTeamPracticeQuestion(word = "blue",  correctTeam = "ue", options = listOf("ue", "oo", "ea"), teamStart = 2, teamLength = 2),
    VowelTeamPracticeQuestion(word = "fruit", correctTeam = "ui", options = listOf("ui", "oo", "ee"), teamStart = 2, teamLength = 2),
    VowelTeamPracticeQuestion(word = "bread", correctTeam = "ea", options = listOf("ea", "ee", "oo"), teamStart = 2, teamLength = 2),
)

// ── UI States ─────────────────────────────────────────────────────────────────

data class VowelTeamLearnUiState(
    val selectedGroup: VowelTeamGroup = VowelTeamGroup.AI_AY,
    val highlightedWordId: String? = null,
    val showWords: Boolean = true
)

data class VowelTeamPracticeUiState(
    val currentIndex: Int = 0,
    val score: Int = 0,
    val selectedAnswer: String? = null,
    val isCorrect: Boolean? = null,
    val isFinished: Boolean = false,
    val shakeWrong: Boolean = false
)

// ── Learn ViewModel ───────────────────────────────────────────────────────────

@HiltViewModel
class VowelTeamsLearnViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {

    var uiState by mutableStateOf(VowelTeamLearnUiState())
        private set

    fun onGroupTap(group: VowelTeamGroup) {
        audioManager.stop()
        uiState = VowelTeamLearnUiState(selectedGroup = group)
    }

    fun onWordTap(word: VowelTeamWord) {
        val wordId = word.id
        audioManager.stop()
        uiState = uiState.copy(highlightedWordId = wordId)
        audioManager.playPhonicsSound("phonics_word/${word.word}")
        audioManager.onAudioCompleted = {
            if (uiState.highlightedWordId == wordId) {
                uiState = uiState.copy(highlightedWordId = null)
            }
        }
    }

    fun stop() { audioManager.stop() }

    override fun onCleared() { stop() }
}

// ── Practice ViewModel ────────────────────────────────────────────────────────

@HiltViewModel
class VowelTeamsPracticeViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager,
    private val levelProgressRepo: PhonicsLevelProgressRepository,
    private val phonicsSessions: PhonicsSessionRecorder
) : ViewModel() {

    var uiState by mutableStateOf(VowelTeamPracticeUiState())
        private set

    // Parent-report session tracking
    private var sessionStartMs = System.currentTimeMillis()
    private val wrongWords = mutableListOf<String>()
    private val correctWords = mutableListOf<String>()

    private val questions = vowelTeamPracticeQuestions.shuffled()
    val totalQuestions: Int get() = questions.size
    val currentQuestion: VowelTeamPracticeQuestion? get() = questions.getOrNull(uiState.currentIndex)

    fun onAnswerTap(answer: String) {
        val q = currentQuestion ?: return
        if (uiState.selectedAnswer != null) return
        val correct = answer == q.correctTeam
        uiState = uiState.copy(selectedAnswer = answer, isCorrect = correct)
        if (correct) correctWords.add(q.word) else wrongWords.add(q.word)
        if (correct) {
            uiState = uiState.copy(score = uiState.score + 1)
            audioManager.playPhonicsSound("phonics_word/${q.word}")
        } else {
            AudioPlayerManager.playSoundWrongAnswer()
            uiState = uiState.copy(shakeWrong = true)
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
        sessionStartMs = System.currentTimeMillis()
        wrongWords.clear()
        correctWords.clear()
        audioManager.stop()
        uiState = VowelTeamPracticeUiState()
    }

    fun stop() { audioManager.stop() }

    private fun advance() {
        val next = uiState.currentIndex + 1
        uiState = if (next >= questions.size) {
            levelProgressRepo.recordPractice(level = PhonicsListenLevelKey.vowelTeams, score = uiState.score, total = questions.size)
            phonicsSessions.recordPractice(PhonicsListenLevelKey.vowelTeams, uiState.score, questions.size, ((System.currentTimeMillis() - sessionStartMs) / 1000).toInt(), wrongWords.toList(), correctWords.toList())
            uiState.copy(isFinished = true)
        } else {
            VowelTeamPracticeUiState(currentIndex = next)
        }
    }

    override fun onCleared() { stop() }
}
