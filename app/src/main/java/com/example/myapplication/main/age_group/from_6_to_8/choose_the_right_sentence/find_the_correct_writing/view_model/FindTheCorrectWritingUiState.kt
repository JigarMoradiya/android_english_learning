package com.example.myapplication.main.age_group.from_6_to_8.choose_the_right_sentence.find_the_correct_writing.view_model

import com.example.myapplication.data.model.GrammarQuestion
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.SentenceUnit

data class FindTheCorrectWritingUiState(
    val unit: SentenceUnit = SentenceUnit.PLAY_AND_FUN,
    val level: SentenceLevel = SentenceLevel.EASY,

    val questions: List<GrammarQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedAnswer: String? = null,
    val score: Int = 0,

    val showResult: Boolean = false,
    val options: List<String> = emptyList()
) {
    val currentQuestion: GrammarQuestion?
        get() = questions.getOrNull(currentIndex)

    val correctAnswer: String
        get() = currentQuestion?.correctSentence ?: ""
}