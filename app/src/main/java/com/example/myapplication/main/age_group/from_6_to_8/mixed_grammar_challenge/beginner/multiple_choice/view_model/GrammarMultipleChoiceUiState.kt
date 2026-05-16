package com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.beginner.multiple_choice.view_model

import com.example.myapplication.data.generation.loader.GrammarMatchQuestion
import com.example.myapplication.data.model.WordType

data class GrammarMultipleChoiceUiState(
    val questions: List<GrammarMatchQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedAnswer: WordType? = null,
    val isAnswerCorrect: Boolean = false,
    val feedbackTitleRes: Int? = null,
    val showFeedback: Boolean = false,
    val score: Int = 0,
    val isCompleted: Boolean = false
) {
    val currentQuestion: GrammarMatchQuestion? get() = questions.getOrNull(currentIndex)
}
