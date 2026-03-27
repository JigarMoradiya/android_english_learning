package com.example.myapplication.main.age_group.from_3_to_5.match_latters.view_model


data class MatchLettersUiState(
    val currentBatch: List<Char> = emptyList(),
    val shuffledLowercase: List<Char> = emptyList(),
    val selectedUpper: Char? = null,
    val selectedLower: Char? = null,
    val matchedPairs: Set<Char> = emptySet(),
    val round: Int = 1,

    val showPopup: Boolean = false,
    val feedbackText: String = "",
    val feedbackSubText: String = ""
)
