package com.example.myapplication.main.age_group.from_6_to_8.one_word_answer.view_model

import com.example.myapplication.data.model.ComprehensionQuestion
import com.example.myapplication.data.model.ReadSentenceItemNew
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.UnitSelectionScreen

data class OneWordAnswerUiState(
    val screenType : UnitSelectionScreen = UnitSelectionScreen.READ_AND_LISTEN_SENTENCE,
    val lessonData: ReadSentenceItemNew? = null,
    val level: SentenceLevel = SentenceLevel.EASY,
    val questions: List<ComprehensionQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedAnswer: String? = null,
    val score: Int = 0,
    val isCorrect: Boolean = false,
    val showResult: Boolean = false
) {
    val currentQuestion: ComprehensionQuestion?
        get() = questions.getOrNull(currentIndex)
}