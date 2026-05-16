package com.example.myapplication.main.age_group.from_5_to_7.singular_plural.choose_form.view_model

import com.example.myapplication.data.generation.loader.SingularPluralPair

enum class FormQuestion { GIVE_PLURAL, GIVE_SINGULAR }

data class ChooseSingularPluralFormUiState(
    val allPairs: List<SingularPluralPair> = emptyList(),
    val currentIndex: Int = 0,
    val questionType: FormQuestion = FormQuestion.GIVE_PLURAL,
    val prompt: String = "",
    val options: List<String> = emptyList(),
    val correctAnswer: String = "",
    val selectedAnswer: String? = null,
    val isAnswerCorrect: Boolean = false,
    val feedbackTitleRes: Int? = null,
    val showFeedback: Boolean = false,
    val score: Int = 0,
    val isCompleted: Boolean = false
)
