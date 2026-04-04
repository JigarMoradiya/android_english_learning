package com.example.myapplication.main.age_group.from_3_to_5.drag_and_drop_word.view_model

import com.example.myapplication.R
import java.util.UUID


data class DragDropWordUiState(
    val showPopup: Boolean = false,
    val feedbackTextRes: Int = R.string.feedbackPhrases_1,
    val feedbackSubTextRes: Int = R.string.feedbackMissingLetter_1,

    val showError: Boolean = false,
    val feedbackWrongTextRes: Int = R.string.feedbackMissingLetterTitleForWrong_1,
    val feedbackWrongSubTextRes: Int = R.string.feedbackMissingLetterSubTitleForWrong_1,
)
