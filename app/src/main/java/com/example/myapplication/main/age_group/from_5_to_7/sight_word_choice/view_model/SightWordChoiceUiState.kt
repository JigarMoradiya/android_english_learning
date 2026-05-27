package com.example.myapplication.main.age_group.from_5_to_7.sight_word_choice.view_model

import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_5_to_7.sight_word_choice.data.SightWordMultipleExample

data class SightWordChoiceUiState(
    val currentWord: SightWordMultipleExample = SightWordMultipleExample("", emptyList()),
    val currentExample: String = "",
    val sentencePrefix: String = "",
    val sentenceSuffix: String = "",
    val options: List<String> = emptyList(),
    val selectedAnswer: String? = null,
    val isAnswerCorrect: Boolean = false,
    val feedbackTextCorrect: Int? = null,
    val feedbackTextWrong: String? = null,
    val countdown: Int = 3,
    val questionIndex: Int = 0,
    val totalQuestions: Int = 5,
    val showBatchPopup: Boolean = false,
    val lastScore: Int = 0,
    val feedbackBatchTextRes: Int = R.string.feedbackPhrases_1,
    val feedbackBatchSubTextRes: Int = R.string.feedbackPhrasesSubtitle_1,
    val scoreLabel: String = "first try 🎯",
)
