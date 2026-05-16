package com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.advanced.sentence_builder.view_model

import com.example.myapplication.data.generation.loader.GrammarSentenceItem

data class GrammarSentenceBuilderUiState(
    val questions: List<GrammarSentenceItem> = emptyList(),
    val currentIndex: Int = 0,
    val shuffledWords: List<String> = emptyList(),
    val arrangedWords: List<String> = emptyList(),
    val isCorrect: Boolean? = null,
    val feedbackTextRes: Int = 0,
    val score: Int = 0,
    val isCompleted: Boolean = false
) {
    val currentQuestion: GrammarSentenceItem? get() = questions.getOrNull(currentIndex)
}
