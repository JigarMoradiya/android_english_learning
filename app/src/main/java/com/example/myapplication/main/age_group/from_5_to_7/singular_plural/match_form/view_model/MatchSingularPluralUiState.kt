package com.example.myapplication.main.age_group.from_5_to_7.singular_plural.match_form.view_model

import com.example.myapplication.data.generation.loader.SingularPluralPair

data class MatchSingularPluralUiState(
    val pairs: List<SingularPluralPair> = emptyList(),
    val leftWords: List<String> = emptyList(),
    val rightWords: List<String> = emptyList(),
    val selectedLeft: String? = null,
    val selectedRight: String? = null,
    val matchedKeys: Set<String> = emptySet(),
    val wrongFlashLeft: String? = null,
    val wrongFlashRight: String? = null,
    val isCompleted: Boolean = false
)
