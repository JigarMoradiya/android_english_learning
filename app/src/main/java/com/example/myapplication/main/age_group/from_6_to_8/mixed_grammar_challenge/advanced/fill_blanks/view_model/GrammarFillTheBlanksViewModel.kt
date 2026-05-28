package com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.advanced.fill_blanks.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.generation.loader.BlankSlot
import com.example.myapplication.data.generation.loader.FillBlankWord
import com.example.myapplication.data.generation.loader.FillBlanksFactory
import com.example.myapplication.data.generation.loader.FillBlanksQuestion
import com.example.myapplication.data.generation.loader.FillSlotState
import com.example.myapplication.data.generation.loader.SentenceSegment
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
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class GrammarFillTheBlanksViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private var startTimeMs = System.currentTimeMillis()

    private val _uiState = MutableStateFlow(GrammarFillTheBlanksUiState())
    val uiState: StateFlow<GrammarFillTheBlanksUiState> = _uiState.asStateFlow()

    init { loadInitialData() }

    fun loadInitialData() {
        val all = FillBlanksFactory.generateQuestions(context)
        val questions = all.take(10)
        _uiState.value = GrammarFillTheBlanksUiState(questionsAll = all, questions = questions)
        setupCurrentQuestion()
    }

    fun restart() {
        startTimeMs = System.currentTimeMillis()
        val all = _uiState.value.questionsAll
        _uiState.value = GrammarFillTheBlanksUiState(
            questionsAll = all,
            questions = all.shuffled().take(10)
        )
        setupCurrentQuestion()
    }

    private fun setupCurrentQuestion() {
        val q = _uiState.value.currentQuestion ?: return
        _uiState.update {
            it.copy(
                availableWords = q.wordPool,
                slotAnswers = emptyMap(),
                isAnswerSubmitted = false,
                isAnswerCorrect = false,
                correctSentence = "",
                feedbackTitleRes = null,
                feedbackSubTitleRes = null
            )
        }
    }

    // ── Word Placement ────────────────────────────────────────────────────────

    /** User taps a word chip → places it in the first empty blank slot (mirrors iOS placeWord) */
    fun placeWord(word: FillBlankWord) {
        val state = _uiState.value
        if (state.isAnswerSubmitted) return
        val q = state.currentQuestion ?: return

        // Find the first unfilled blank slot in order
        for (segment in q.segments) {
            if (segment is SentenceSegment.Blank && state.slotAnswers[segment.slot.id] == null) {
                _uiState.update {
                    it.copy(
                        slotAnswers = it.slotAnswers + (segment.slot.id to word),
                        availableWords = it.availableWords.filter { w -> w.id != word.id }
                    )
                }
                AudioPlayerManager.playSoundDragItem()
                // Auto-check once all blanks filled
                if (_uiState.value.allBlanksFilled) checkAnswer()
                return
            }
        }
    }

    /** User taps a filled slot → returns word to pool (mirrors iOS removeWordFromSlot) */
    fun removeWordFromSlot(slotId: UUID) {
        val state = _uiState.value
        if (state.isAnswerSubmitted) return
        val word = state.slotAnswers[slotId] ?: return
        _uiState.update {
            it.copy(
                slotAnswers = it.slotAnswers - slotId,
                availableWords = it.availableWords + word
            )
        }
    }

    // ── Evaluation ────────────────────────────────────────────────────────────

    private fun checkAnswer() {
        val q = _uiState.value.currentQuestion ?: return
        val state = _uiState.value

        val allCorrect = q.segments.all { segment ->
            if (segment !is SentenceSegment.Blank) return@all true
            val placed = state.slotAnswers[segment.slot.id] ?: return@all false
            placed.word.lowercase() == segment.slot.correctWord.lowercase()
        }

        if (allCorrect) {
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
                    feedbackSubTitleRes = null,
                    correctSentence = buildCorrectSentence(q)
                )
            }
        }
        if (_uiState.value.currentIndex == _uiState.value.questions.size - 1) {
            val state = _uiState.value
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
                    chapterTitle = "Fill the Blanks"
                )
            )
        }
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    fun moveToNextQuestion() {
        val state = _uiState.value
        if (state.currentIndex < state.questions.size - 1) {
            _uiState.update { it.copy(currentIndex = it.currentIndex + 1) }
            setupCurrentQuestion()
        } else {
            _uiState.update { it.copy(isCompleted = true) }
        }
    }

    // ── Slot State Helper (mirrors iOS slotState) ─────────────────────────────

    fun slotState(slot: BlankSlot): FillSlotState {
        val state = _uiState.value
        if (!state.isAnswerSubmitted) return FillSlotState.EMPTY
        val placed = state.slotAnswers[slot.id] ?: return FillSlotState.EMPTY
        return if (placed.word.lowercase() == slot.correctWord.lowercase())
            FillSlotState.CORRECT else FillSlotState.WRONG
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun buildCorrectSentence(q: FillBlanksQuestion): String {
        return q.segments.joinToString(" ") { segment ->
            when (segment) {
                is SentenceSegment.Word  -> segment.text
                is SentenceSegment.Blank -> segment.slot.correctWord
            }
        }
            .replace(" ,", ",")
            .replace(" .", ".")
            .replace(" !", "!")
            .replace(" ?", "?")
    }
}
