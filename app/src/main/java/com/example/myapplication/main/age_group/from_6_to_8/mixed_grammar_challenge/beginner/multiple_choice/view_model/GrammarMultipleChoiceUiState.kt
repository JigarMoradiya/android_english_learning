package com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.beginner.multiple_choice.view_model

import com.example.myapplication.data.generation.loader.MixedBeginnerQuestion

data class GrammarMultipleChoiceUiState(
    val questionsAll: List<MixedBeginnerQuestion> = emptyList(),
    val questions: List<MixedBeginnerQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedWord: String? = null,   // lowercased selected option
    val isAnswerCorrect: Boolean = false,
    val feedbackTitleRes: Int? = null,
    val feedbackSubTitle: Int? = null,
    val showNext: Boolean = false,
    val score: Int = 0,
    val isCompleted: Boolean = false
) {
    val currentQuestion: MixedBeginnerQuestion? get() = questions.getOrNull(currentIndex)
    val isLastIndex: Boolean get() = currentIndex == questions.size - 1
}
