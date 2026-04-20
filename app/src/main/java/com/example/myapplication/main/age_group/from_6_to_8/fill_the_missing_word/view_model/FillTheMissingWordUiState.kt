package com.example.myapplication.main.age_group.from_6_to_8.fill_the_missing_word.view_model

data class FillTheMissingWordUiState(
    val currentWord: String = "",
    val currentImageName: String? = null,
    val selectedAnswer: String? = null,
    val isAnswerCorrect: Boolean = false,
    val feedbackText: String? = null
)