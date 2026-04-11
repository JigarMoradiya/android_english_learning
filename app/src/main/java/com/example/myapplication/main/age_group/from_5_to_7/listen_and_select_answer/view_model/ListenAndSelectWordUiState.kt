package com.example.myapplication.main.age_group.from_5_to_7.listen_and_select_answer.view_model

import com.example.myapplication.R


data class ListenAndSelectWordUiState(
    val wordList: List<String> = emptyList(),
    val currentWord: String = "",
    val optionsWord: List<String> = emptyList(),

    val showError: Boolean = false,
    val showPopup: Boolean = false,
    val feedbackTextRes: Int = R.string.feedbackPhrases_1,
    val feedbackSubTextRes: Int = R.string.feedbackPhrasesSubtitle_1,
    val feedbackSubTextError: String = "",
)
