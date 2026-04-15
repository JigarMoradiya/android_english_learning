package com.example.myapplication.main.age_group.from_3_to_5.arrange_letter_sequence.view_model

import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.LetterMode


data class ArrangeLetterInSequenceUiState(
    val feedbackTextRes: Int = R.string.feedbackPhrases_1,
    val feedbackSubTextRes: Int = R.string.feedbackMissingLetter_1,
    val showError: Boolean = false,
    val showSuccess: Boolean = false,

    val mode: LetterMode = LetterMode.UPPERCASE,
    val round: Int = 1,
    val topSlots: List<String?> = emptyList(),   // A B _ D _
    val bottomOptions: List<String> = emptyList(),
    val fullSequence: List<String> = emptyList(),

    val selectedLetter: String? = null,
)
