package com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.advanced.sentence_builder.view_model

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.generation.loader.AdvBuildWord
import com.example.myapplication.data.generation.loader.AdvSentenceBuilderFactory
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.SessionRepository
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.FeedbackConstant.feedbackTitles
import com.example.myapplication.utils.FeedbackConstant.feedbackGiveAnswerSubTitleCorrect
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GrammarSentenceBuilderViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private var startTimeMs = System.currentTimeMillis()

    private val _uiState = MutableStateFlow(GrammarSentenceBuilderUiState())
    val uiState: StateFlow<GrammarSentenceBuilderUiState> = _uiState.asStateFlow()

    init { loadInitialData() }

    fun loadInitialData() {
        val all = AdvSentenceBuilderFactory.generateQuestions(context)
        val questions = all.take(10)
        _uiState.value = GrammarSentenceBuilderUiState(questionsAll = all, questions = questions)
        setupCurrentQuestion()
    }

    fun restart() {
        startTimeMs = System.currentTimeMillis()
        val all = _uiState.value.questionsAll
        _uiState.value = GrammarSentenceBuilderUiState(
            questionsAll = all,
            questions = all.shuffled().take(10)
        )
        setupCurrentQuestion()
    }

    private fun setupCurrentQuestion() {
        val q = _uiState.value.currentQuestion ?: return
        _uiState.update {
            it.copy(
                availableWords = q.shuffledWords,
                placedWords = emptyList(),
                isAnswerSubmitted = false,
                isAnswerCorrect = false,
                feedbackTitleRes = null,
                feedbackSubTitleRes = null
            )
        }
    }

    // ── Word interaction (mirrors iOS placeWord / removeWord) ─────────────────

    /** User taps a word from pool → move to placed area */
    fun placeWord(word: AdvBuildWord) {
        if (_uiState.value.isAnswerSubmitted) return
        _uiState.update {
            it.copy(
                availableWords = it.availableWords.filter { w -> w.id != word.id },
                placedWords = it.placedWords + word
            )
        }
        AudioPlayerManager.playSoundDragItem()
        // Auto-check once all words are placed (mirrors iOS)
        if (_uiState.value.availableWords.isEmpty()) checkAnswer()
    }

    /** User taps a placed word → return it to pool */
    fun removeWord(word: AdvBuildWord) {
        if (_uiState.value.isAnswerSubmitted) return
        _uiState.update {
            it.copy(
                placedWords = it.placedWords.filter { w -> w.id != word.id },
                availableWords = it.availableWords + word
            )
        }
    }

    // ── Evaluation (auto-called when all words placed) ────────────────────────

    private fun checkAnswer() {
        val q = _uiState.value.currentQuestion ?: return
        val placed = _uiState.value.placedWords.map { it.text.lowercase() }
        val correct = q.correctWords.map { it.lowercase() }
        val isCorrect = placed == correct

        if (isCorrect) {
            AudioPlayerManager.playSoundCorrectAnswer()
            _uiState.update {
                it.copy(
                    isAnswerSubmitted = true,
                    isAnswerCorrect = true,
                    feedbackTitleRes = feedbackTitles.random(),
                    feedbackSubTitleRes = feedbackGiveAnswerSubTitleCorrect.random(),
                    score = it.score + 1
                )
            }
        } else {
            AudioPlayerManager.playSoundWrongAnswer()
            _uiState.update {
                it.copy(
                    isAnswerSubmitted = true,
                    isAnswerCorrect = false,
                    feedbackTitleRes = R.string.its_wrong,
                    feedbackSubTitleRes = null
                )
            }
        }
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    fun moveToNextQuestion() {
        val state = _uiState.value
        if (state.currentIndex < state.questions.size - 1) {
            _uiState.update { it.copy(currentIndex = it.currentIndex + 1) }
            setupCurrentQuestion()
        } else {
            val durationSec = ((System.currentTimeMillis() - startTimeMs) / 1000).toInt()
            sessionRepository.record(
                LearningSession(
                    moduleId = ModuleID.GRAMMAR_CHALLENGE_ADVANCED,
                    ageGroup = AgeGroup.SIX_TO_EIGHT,
                    durationSeconds = durationSec,
                    score = state.score,
                    totalQuestions = state.questions.size,
                    subConfig = "",
                    lessonTitle = null,
                    chapterTitle = "Sentence Builder"
                )
            )
            _uiState.update { it.copy(isCompleted = true) }
        }
    }

    // ── Chip color helper (mirrors iOS placedChipColor) ───────────────────────
    // Returns the color for a placed chip based on whether its position is correct

    fun placedChipColor(index: Int): Color {
        val state = _uiState.value
        if (!state.isAnswerSubmitted) return Color(0xFF1565C0) // blue default
        val q = state.currentQuestion ?: return Color.Red
        val correct = q.correctWords.map { it.lowercase() }
        val placed = state.placedWords.map { it.text.lowercase() }
        if (index >= placed.size || index >= correct.size) return Color.Red
        return if (placed[index] == correct[index]) Color(0xFF388E3C) else Color.Red
    }
}
