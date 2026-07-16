package com.example.myapplication.main.age_group.from_6_to_8.grammar_drill.view_model

import com.example.myapplication.data.generation.loader.GrammarDrillQuestion
import com.example.myapplication.data.generation.loader.GrammarDrillType

data class GrammarDrillUiState(
    val type: GrammarDrillType = GrammarDrillType.HAS_HAVE,
    val questions: List<GrammarDrillQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedAnswer: String? = null,
    val score: Int = 0,
    val showResult: Boolean = false
) {
    val currentQuestion: GrammarDrillQuestion?
        get() = questions.getOrNull(currentIndex)

    val correctAnswer: String
        get() = currentQuestion?.correctAnswer ?: ""

    val title: String
        get() = if (type == GrammarDrillType.HAS_HAVE) "Has or Have" else "He, She, It"
}
