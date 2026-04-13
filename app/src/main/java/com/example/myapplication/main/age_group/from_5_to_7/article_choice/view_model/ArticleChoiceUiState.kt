package com.example.myapplication.main.age_group.from_5_to_7.article_choice.view_model

data class ArticleChoiceUiState(
    val currentWord: String = "",
    val currentImageName: String? = null,
    val selectedAnswer: String? = null,
    val isAnswerCorrect: Boolean = false,
    val feedbackText: String? = null
)