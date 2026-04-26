package com.example.myapplication.main.age_group.from_6_to_8.sentence_builder.view_model

import com.example.myapplication.R
import com.example.myapplication.data.model.SentenceBuilderQuestion
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.SentenceUnit

data class SentenceBuilderUiState(
    val unit: SentenceUnit = SentenceUnit.PLAY_AND_FUN,
    val level: SentenceLevel = SentenceLevel.EASY,

    val questions: List<SentenceBuilderQuestion> = emptyList(),

    val currentIndex: Int = 0,
    val score: Int = 0,

    val shuffledWords: List<String> = emptyList(),
    val arrangedWords: List<String> = emptyList(),

    val isCorrect: Boolean? = null,
    val isCompleted: Boolean = false,

    val feedbackTextRes: Int = R.string.feedbackPhrases_1,
) {
    val currentQuestion: SentenceBuilderQuestion?
        get() = questions.getOrNull(currentIndex)
}