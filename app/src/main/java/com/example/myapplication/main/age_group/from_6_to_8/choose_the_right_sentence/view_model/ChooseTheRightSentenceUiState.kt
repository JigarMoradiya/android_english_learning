package com.example.myapplication.main.age_group.from_6_to_8.choose_the_right_sentence.view_model

data class ChooseTheRightSentenceUiState(
    val currentWord: String = "",
    val currentImageName: String? = null,
    val selectedAnswer: String? = null,
    val isAnswerCorrect: Boolean = false,
    val feedbackText: String? = null
)