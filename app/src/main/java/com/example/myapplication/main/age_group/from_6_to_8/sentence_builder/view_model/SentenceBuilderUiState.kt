package com.example.myapplication.main.age_group.from_6_to_8.sentence_builder.view_model

data class SentenceBuilderUiState(
    val currentWord: String = "",
    val currentImageName: String? = null,
    val selectedAnswer: String? = null,
    val isAnswerCorrect: Boolean = false,
    val feedbackText: String? = null
)