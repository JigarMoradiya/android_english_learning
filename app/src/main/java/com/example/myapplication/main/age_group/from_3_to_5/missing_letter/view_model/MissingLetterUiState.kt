package com.example.myapplication.main.age_group.from_3_to_5.missing_letter.view_model

import java.util.UUID


data class MissingLetterUiState(
    val showPopup: Boolean = false,
    val feedbackText: String = "",
    val feedbackSubText: String = "",

    val showError: Boolean = false,
    val errorText: String = ""
)

data class LetterItem(
    val letter: String,
    val id: String = UUID.randomUUID().toString()
)

enum class DifficultyLevel {
    EASY, MEDIUM, HARD
}