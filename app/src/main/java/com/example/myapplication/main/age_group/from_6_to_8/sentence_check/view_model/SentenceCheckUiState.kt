package com.example.myapplication.main.age_group.from_6_to_8.sentence_check.view_model

import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.SentenceUnit
import com.example.myapplication.data.model.TrueFalseQuestion

data class SentenceCheckUiState(
    val unit: SentenceUnit = SentenceUnit.PLAY_AND_FUN,
    val level: SentenceLevel = SentenceLevel.EASY,

    val questions: List<TrueFalseQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedAnswer: String? = null,
    val score: Int = 0,

    val showResult: Boolean = false,

    val options: List<String> = listOf("true", "false")
) {
    val currentQuestion: TrueFalseQuestion?
        get() = questions.getOrNull(currentIndex)

    val correctAnswer: String
        get() = currentQuestion?.isTrue ?: ""
}