package com.example.myapplication.main.age_group.from_5_to_7.opposite_words.match_opposites.view_model

import com.example.myapplication.data.generation.loader.OppositeWordPair

data class MatchOppositesUiState(
    val leftWords: List<OppositeWordPair> = emptyList(),    // words row (top)
    val rightWords: List<OppositeWordPair> = emptyList(),   // shuffled opposites row (bottom)
    val matchedWords: Set<String> = emptySet(),             // matched "word" keys
    val matchedOrder: List<String> = emptyList(),           // ordered list for color index
    val showPopup: Boolean = false,
    val feedbackTitleRes: Int = 0,
    val feedbackSubTitleRes: Int = 0,
    val round: Int = 0,
    val lastScore: Int = 0,
    val lastTotal: Int = 0
)
