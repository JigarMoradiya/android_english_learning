package com.example.myapplication.main.age_group.from_3_to_5.missing_letter.view_model

import com.example.myapplication.R
import java.util.UUID


data class MissingLetterUiState(
    val showPopup: Boolean = false,
    val feedbackTextRes: Int = R.string.feedbackPhrases_1,
    val feedbackSubTextRes: Int = R.string.feedbackMissingLetter_1,

    val showError: Boolean = false,
    val feedbackWrongTextRes: Int = R.string.feedbackMissingLetterTitleForWrong_1,
    val feedbackWrongSubTextRes: Int = R.string.feedbackMissingLetterSubTitleForWrong_1,
)

data class LetterItem(
    val letter: String,
    val id: String = UUID.randomUUID().toString()
)

enum class DifficultyLevel {
    EASY, MEDIUM, HARD
}