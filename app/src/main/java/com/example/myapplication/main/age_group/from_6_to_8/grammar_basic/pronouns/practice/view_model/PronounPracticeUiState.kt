package com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.pronouns.practice.view_model

import com.example.myapplication.R
import com.example.myapplication.data.model.NounVerbAdjectiveCommonQuestionModel
import com.example.myapplication.data.model.PronounPracticeQuestion

data class PronounPracticeUiState(

    val currentIndex: Int = 0,
    val selectedAnswer: String? = null,
    val feedbackTextRes: Int? = R.string.feedbackPhrases_1,
    val feedbackSubTextRes: Int? = R.string.feedbackGiveAnswerSubTitleCorrect_1,
    val showNext: Boolean = false,
    val isAnswerCorrect: Boolean = false,

    val questionsAll: List<PronounPracticeQuestion> = emptyList(),
    val questions: List<PronounPracticeQuestion> = emptyList(),

    val score: Int = 0,
    val isCompleted: Boolean = false,
    val shuffledOptions: List<String> = emptyList()
) {
    val currentQuestion: PronounPracticeQuestion?
        get() = questions.getOrNull(currentIndex)
}