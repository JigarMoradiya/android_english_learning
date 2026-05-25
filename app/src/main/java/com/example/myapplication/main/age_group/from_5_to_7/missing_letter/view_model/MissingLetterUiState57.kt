package com.example.myapplication.main.age_group.from_5_to_7.missing_letter.view_model

import com.example.myapplication.R
import java.util.UUID

data class MissingLetterUiState57(
    val showSuccess: Boolean = false,
    val feedbackTextRes: Int = R.string.feedbackPhrases_1,
    val feedbackSubTextRes: Int = R.string.feedbackMissingLetter_1,
    val showError: Boolean = false,
    val countdownValue: Int = 3,
    val wrongSlots: Set<Int> = emptySet(),
    val round: Int = 1,
    val totalRounds: Int = 10,
    val correctCount: Int = 0,
    val showResult: Boolean = false,
)

data class LetterItem(
    val letter: String,
    val id: String = UUID.randomUUID().toString()
)

enum class DifficultyLevel {
    EASY, MEDIUM, HARD
}
