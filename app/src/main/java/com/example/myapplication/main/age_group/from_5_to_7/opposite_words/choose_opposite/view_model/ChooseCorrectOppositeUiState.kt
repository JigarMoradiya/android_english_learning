package com.example.myapplication.main.age_group.from_5_to_7.opposite_words.choose_opposite.view_model

data class ChooseCorrectOppositeUiState(
    val currentWord: String = "",
    val correctAnswer: String = "",
    val options: List<String> = emptyList(),
    val selectedAnswer: String? = null,
    val isAnswerCorrect: Boolean = false,
    val feedbackText: String? = null,
    val countdown: Int? = null,
    val questionIndex: Int = 0,
    val totalQuestions: Int = 5,
    val score: Int = 0,
    val showCompletePopup: Boolean = false
)
