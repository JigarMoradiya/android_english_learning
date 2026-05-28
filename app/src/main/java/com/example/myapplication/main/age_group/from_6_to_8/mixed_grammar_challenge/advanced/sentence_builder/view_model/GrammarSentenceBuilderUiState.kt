package com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.advanced.sentence_builder.view_model

import com.example.myapplication.data.generation.loader.AdvBuildWord
import com.example.myapplication.data.generation.loader.AdvSentenceBuilderQuestion

data class GrammarSentenceBuilderUiState(
    val isLoading: Boolean = true,
    val questionsAll: List<AdvSentenceBuilderQuestion> = emptyList(),
    val questions: List<AdvSentenceBuilderQuestion> = emptyList(),
    val currentIndex: Int = 0,

    // Per-question word state (mirrors iOS placedWords / availableWords)
    val availableWords: List<AdvBuildWord> = emptyList(),  // still in pool
    val placedWords: List<AdvBuildWord> = emptyList(),      // arranged by user in order

    // Answer state
    val isAnswerSubmitted: Boolean = false,
    val isAnswerCorrect: Boolean = false,

    // Feedback
    val feedbackTitleRes: Int? = null,
    val feedbackSubTitleRes: Int? = null,

    // Session
    val score: Int = 0,
    val isCompleted: Boolean = false
) {
    val currentQuestion: AdvSentenceBuilderQuestion? get() = questions.getOrNull(currentIndex)
    val isLastIndex: Boolean get() = currentIndex == questions.size - 1

    /** Next button enabled once answer has been auto-submitted (mirrors iOS showNext) */
    val showNext: Boolean get() = isAnswerSubmitted
}
