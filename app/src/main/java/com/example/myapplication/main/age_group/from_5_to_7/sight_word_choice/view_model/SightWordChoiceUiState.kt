package com.example.myapplication.main.age_group.from_5_to_7.sight_word_choice.view_model

import com.example.myapplication.main.age_group.from_5_to_7.sight_word_choice.data.SightWordMultipleExample


data class SightWordChoiceUiState(
    val currentWord: SightWordMultipleExample = SightWordMultipleExample("", emptyList()),
    val currentExample: String = "",
    val sentencePrefix: String = "",
    val sentenceSuffix: String = "",
    val options: List<String> = emptyList(),
    val selectedAnswer: String? = null,
    val isAnswerCorrect: Boolean = false,
    val feedbackText: String? = null
)
