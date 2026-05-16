package com.example.myapplication.main.age_group.from_5_to_7.opposite_words.choose_opposite.view_model

data class ChooseCorrectOppositeUiState(
    val currentWord: String = "",
    val correctAnswer: String = "",
    val options: List<String> = emptyList(),        // always 3 items
    val selectedAnswer: String? = null,
    val isAnswerCorrect: Boolean = false,
    val feedbackText: String? = null,
    val countdown: Int? = null                      // 3 → 2 → 1, then auto-advance
)
