package com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.noun.practice.view_model

import com.example.myapplication.R
import com.example.myapplication.data.model.NounVerbAdjectiveCommonQuestionModel

data class NounPracticeUiState(
    val currentIndex: Int = 0,
    val feedbackTextRes: Int? = R.string.feedbackPhrases_1,
    val feedbackSubTextRes: Int? = R.string.feedbackGiveAnswerSubTitleCorrect_1,
    val showNext: Boolean = false,
    val isAnswerCorrect: Boolean = false,
    val selectedAnswer: String? = null,

    val questionsAll: List<NounVerbAdjectiveCommonQuestionModel> = emptyList(),
    val questions: List<NounVerbAdjectiveCommonQuestionModel> = emptyList(),

    val score: Int = 0,
    val isCompleted: Boolean = false,
    val shuffledOptions: List<String> = emptyList()
) {
    val currentQuestion: NounVerbAdjectiveCommonQuestionModel?
        get() = questions.getOrNull(currentIndex)
}