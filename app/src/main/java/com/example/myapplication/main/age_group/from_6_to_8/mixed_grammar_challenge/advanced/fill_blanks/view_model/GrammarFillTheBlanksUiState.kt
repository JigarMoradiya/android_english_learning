package com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.advanced.fill_blanks.view_model

import com.example.myapplication.data.generation.loader.BlankSlot
import com.example.myapplication.data.generation.loader.FillBlankWord
import com.example.myapplication.data.generation.loader.FillBlanksQuestion
import com.example.myapplication.data.generation.loader.SentenceSegment
import java.util.UUID

data class GrammarFillTheBlanksUiState(
    val questionsAll: List<FillBlanksQuestion> = emptyList(),
    val questions: List<FillBlanksQuestion> = emptyList(),
    val currentIndex: Int = 0,

    // Per-question: maps slotId → placed FillBlankWord (mirrors iOS slotAnswers)
    val slotAnswers: Map<UUID, FillBlankWord> = emptyMap(),
    // Words still available to place
    val availableWords: List<FillBlankWord> = emptyList(),

    // Answer state
    val isAnswerSubmitted: Boolean = false,
    val isAnswerCorrect: Boolean = false,
    val correctSentence: String = "",

    // Feedback
    val feedbackTitleRes: Int? = null,
    val feedbackSubTitleRes: Int? = null,

    // Session
    val score: Int = 0,
    val isCompleted: Boolean = false
) {
    val currentQuestion: FillBlanksQuestion? get() = questions.getOrNull(currentIndex)
    val isLastIndex: Boolean get() = currentIndex == questions.size - 1

    /** Next button enabled once answer has been auto-submitted (mirrors iOS showNext) */
    val showNext: Boolean get() = isAnswerSubmitted

    /** All blanks filled → auto-check (mirrors iOS allBlanksFilled) */
    val allBlanksFilled: Boolean
        get() {
            val q = currentQuestion ?: return false
            val blanks = q.segments.filterIsInstance<SentenceSegment.Blank>()
            return blanks.isNotEmpty() && blanks.all { slotAnswers[it.slot.id] != null }
        }
}
