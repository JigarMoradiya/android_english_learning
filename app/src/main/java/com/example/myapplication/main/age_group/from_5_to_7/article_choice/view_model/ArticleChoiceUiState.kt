package com.example.myapplication.main.age_group.from_5_to_7.article_choice.view_model

import com.example.myapplication.R

data class ArticleChoiceUiState(
    val currentWord: String = "",
    val currentImageName: String? = null,
    val selectedAnswer: String? = null,
    val isAnswerCorrect: Boolean = false,
    val feedbackTextCorrect: Int? = null,
    val feedbackTextWrong: String? = null,
    val answerExplanation: String? = null,
    val countdown: Int = 3,
    val questionIndex: Int = 0,
    val totalQuestions: Int = 5,
    val showBatchPopup: Boolean = false,
    val lastScore: Int = 0,
    val feedbackBatchTextRes: Int = R.string.feedbackPhrases_1,
    val feedbackBatchSubTextRes: Int = R.string.feedbackPhrasesSubtitle_1,
    val scoreLabel: String = "first try 🎯",
)
