package com.example.myapplication.main.age_group.from_3_to_5.drag_and_drop_word.view_model

import com.example.myapplication.R


data class DragDropWordUiState(
    val showSuccess: Boolean = false,
    val feedbackTextRes: Int = R.string.feedbackPhrases_1,
    val feedbackSubTextRes: Int = R.string.feedbackMissingLetter_1,

    val showError: Boolean = false,
)
