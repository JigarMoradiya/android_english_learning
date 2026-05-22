package com.example.myapplication.main.age_group.from_5_to_7.singular_plural.match_form.view_model

import com.example.myapplication.data.generation.loader.SingularPluralPair

data class MatchSingularPluralUiState(
    val leftWords: List<SingularPluralPair> = emptyList(),    // words row (top)
    val rightWords: List<SingularPluralPair> = emptyList(),   // shuffled row (bottom)
    val matchedWords: Set<String> = emptySet(),             // matched "word" keys
    val matchedOrder: List<String> = emptyList(),           // ordered list for color index
    val showPopup: Boolean = false,
    val feedbackTitleRes: Int = 0,
    val feedbackSubTitleRes: Int = 0,
    val round: Int = 0
)