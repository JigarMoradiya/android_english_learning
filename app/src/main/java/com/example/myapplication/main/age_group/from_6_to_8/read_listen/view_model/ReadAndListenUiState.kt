package com.example.myapplication.main.age_group.from_6_to_8.read_listen.view_model

data class ReadAndListenUiState(
    val currentWord: String = "",
    val currentImageName: String? = null,
    val selectedAnswer: String? = null,
    val isAnswerCorrect: Boolean = false,
    val feedbackText: String? = null
)