package com.example.myapplication.main.age_group.from_6_to_8.sentence_check.view_model

data class SentenceCheckUiState(
    val currentWord: String = "",
    val currentImageName: String? = null,
    val selectedAnswer: String? = null,
    val isAnswerCorrect: Boolean = false,
    val feedbackText: String? = null
)