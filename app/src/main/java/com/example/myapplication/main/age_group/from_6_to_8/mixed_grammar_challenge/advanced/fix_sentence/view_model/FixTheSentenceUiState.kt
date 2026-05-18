package com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.advanced.fix_sentence.view_model

import com.example.myapplication.data.generation.loader.FixSentenceQuestion

enum class FixOptionState { NORMAL, SELECTED, CORRECT, WRONG }

data class FixTheSentenceUiState(
    val questions: List<FixSentenceQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedOption: String? = null,
    val isAnswerSubmitted: Boolean = false,
    val isAnswerCorrect: Boolean = false,
    val feedbackTitleRes: Int? = null,
    val feedbackSubTitleRes: Int? = null,
    val score: Int = 0,
    val isCompleted: Boolean = false
) {
    val currentQuestion: FixSentenceQuestion? get() = questions.getOrNull(currentIndex)
    val isLastIndex: Boolean get() = currentIndex >= questions.size - 1
    val showNext: Boolean get() = isAnswerSubmitted
}
