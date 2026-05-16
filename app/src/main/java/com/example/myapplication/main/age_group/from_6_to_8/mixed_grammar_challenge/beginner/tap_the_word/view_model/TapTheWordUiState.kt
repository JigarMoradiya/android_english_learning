package com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.beginner.tap_the_word.view_model

import com.example.myapplication.data.generation.loader.GrammarWordQuestion
import com.example.myapplication.data.model.WordType

data class TapTheWordUiState(
    val questions: List<GrammarWordQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val tappedWord: String? = null,
    val isAnswerCorrect: Boolean = false,
    val feedbackTitleRes: Int? = null,
    val showFeedback: Boolean = false,
    val score: Int = 0,
    val isCompleted: Boolean = false
) {
    val currentQuestion: GrammarWordQuestion? get() = questions.getOrNull(currentIndex)
}
