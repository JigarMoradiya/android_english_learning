package com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.beginner.tap_the_word.view_model

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.generation.loader.BeginnerActivityType
import com.example.myapplication.data.generation.loader.MixedGrammarBeginnerQuestionFactory
import com.example.myapplication.data.model.WordType
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.SessionRepository
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.FeedbackConstant.feedbackGiveAnswerSubTitleCorrect
import com.example.myapplication.utils.FeedbackConstant.feedbackTitles
import com.example.myapplication.utils.FeedbackConstant.feedbackWrong
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TapTheWordViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private var startTimeMs = System.currentTimeMillis()

    private val _uiState = MutableStateFlow(TapTheWordUiState())
    val uiState: StateFlow<TapTheWordUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        val all = MixedGrammarBeginnerQuestionFactory.generateQuestions(context, BeginnerActivityType.TAP_WORD)
        val questions = all.shuffled().take(10)
        _uiState.value = TapTheWordUiState(
            questionsAll = all,
            questions = questions
        )
    }

    fun restart() {
        startTimeMs = System.currentTimeMillis()
        val all = _uiState.value.questionsAll
        _uiState.value = TapTheWordUiState(
            questionsAll = all,
            questions = all.shuffled().take(10)
        )
    }

    fun selectAnswer(word: String) {
        val state = _uiState.value
        if (state.selectedWord != null) return
        val q = state.currentQuestion ?: return

        val tapped = word.lowercase()
        // Accept any word of the correct type (mirrors iOS allCorrectWords check)
        val isCorrect = q.allCorrectWords.contains(tapped)

        if (isCorrect) AudioPlayerManager.playSoundCorrectAnswer()
        else AudioPlayerManager.playSoundWrongAnswer()

        _uiState.update {
            it.copy(
                selectedWord = tapped,
                isAnswerCorrect = isCorrect,
                feedbackTitleRes = if (isCorrect) feedbackTitles.random() else feedbackWrong.random(),
                feedbackSubTitle = if (isCorrect) feedbackGiveAnswerSubTitleCorrect.random() else null,
                showNext = true,
                score = if (isCorrect) it.score + 1 else it.score
            )
        }
        if (_uiState.value.currentIndex == _uiState.value.questions.size - 1) {
            val state = _uiState.value
            val durationSec = ((System.currentTimeMillis() - startTimeMs) / 1000).toInt()
            sessionRepository.record(
                LearningSession(
                    moduleId = ModuleID.GRAMMAR_CHALLENGE_BEGINNER,
                    ageGroup = AgeGroup.SIX_TO_EIGHT,
                    durationSeconds = durationSec,
                    score = state.score,
                    totalQuestions = state.questions.size,
                    subConfig = "",
                    lessonTitle = null,
                    chapterTitle = "Tap Correct Word"
                )
            )
        }
    }

    fun moveToNextQuestion() {
        val state = _uiState.value
        if (state.currentIndex < state.questions.size - 1) {
            _uiState.update {
                it.copy(
                    currentIndex = it.currentIndex + 1,
                    selectedWord = null,
                    isAnswerCorrect = false,
                    feedbackTitleRes = null,
                    showNext = false
                )
            }
        } else {
            _uiState.update { it.copy(isCompleted = true) }
        }
    }

    // ── Button type helpers ──────────────────────────────────────────────────

    /** Style for inline word chips (green = correct type, red = wrong tap) */
    fun wordChipType(word: String): ButtonType {
        val state = _uiState.value
        val selected = state.selectedWord ?: return ButtonType.OPTIONS
        val w = word.lowercase()
        return when {
            state.currentQuestion?.allCorrectWords?.contains(w) == true -> ButtonType.GREEN
            w == selected -> ButtonType.RED
            else -> ButtonType.OPTIONS
        }
    }

    // ── Type label / color / icon ────────────────────────────────────────────

    fun typeLabel(type: WordType): String = when (type) {
        WordType.NOUN      -> "Noun"
        WordType.VERB      -> "Verb"
        WordType.ADJECTIVE -> "Adjective"
        WordType.PRONOUN   -> "Pronoun"
    }

    fun typeColor(type: WordType): Color = when (type) {
        WordType.NOUN      -> Color(0xFF237227)  // green
        WordType.VERB      -> Color(0xFF85479D)  // purple
        WordType.ADJECTIVE -> Color(0xFFFF9D00)  // orange
        WordType.PRONOUN   -> Color(0xFFD62828)  // red
    }

    // For TapWord badge: "hand tap" icon (iOS: hand.point.up.fill)
    fun tapBadgeIcon(): ImageVector = Icons.Filled.TouchApp

    fun typeIcon(type: WordType): ImageVector = when (type) {
        WordType.NOUN      -> Icons.Filled.Image
        WordType.VERB      -> Icons.Filled.DirectionsRun
        WordType.ADJECTIVE -> Icons.Filled.Palette
        WordType.PRONOUN   -> Icons.Filled.Person
    }
}
