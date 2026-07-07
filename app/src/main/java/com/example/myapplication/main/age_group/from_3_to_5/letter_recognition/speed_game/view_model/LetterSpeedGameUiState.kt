package com.example.myapplication.main.age_group.from_3_to_5.letter_recognition.speed_game.view_model

data class LetterSpeedGameUiState(
    val pool: List<Char> = emptyList(),
    val displayOptions: List<Char> = emptyList(),
    val targetLetter: Char? = null,
    val selectedAnswer: Char? = null,
    val isAnswerCorrect: Boolean = false,
    val countdown: Int = 3,
    val questionIndex: Int = 0,
    val totalQuestions: Int = 5,
    val correctCount: Int = 0,
    val showBatchPopup: Boolean = false,
    val lastScore: Int = 0
)
