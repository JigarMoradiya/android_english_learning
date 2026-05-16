package com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.advanced.fill_blanks.view_model

import com.example.myapplication.data.generation.loader.GrammarFillBlankQuestion

data class GrammarFillTheBlanksUiState(
    val questions: List<GrammarFillBlankQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedAnswer: String? = null,
    val isAnswerCorrect: Boolean = false,
    val feedbackTitleRes: Int? = null,
    val showFeedback: Boolean = false,
    val score: Int = 0,
    val isCompleted: Boolean = false
) {
    val currentQuestion: GrammarFillBlankQuestion? get() = questions.getOrNull(currentIndex)
}
