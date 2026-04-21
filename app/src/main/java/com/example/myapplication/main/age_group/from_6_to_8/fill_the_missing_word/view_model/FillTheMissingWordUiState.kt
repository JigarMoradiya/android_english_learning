package com.example.myapplication.main.age_group.from_6_to_8.fill_the_missing_word.view_model

import com.example.myapplication.data.model.BlankableWord
import com.example.myapplication.data.model.ComprehensionQuestion
import com.example.myapplication.data.model.LessonSentence
import com.example.myapplication.data.model.ReadSentenceItemNew
import com.example.myapplication.data.model.UnitSelectionScreen

data class FillTheMissingWordUiState(
    val screenType: UnitSelectionScreen = UnitSelectionScreen.READ_AND_LISTEN_SENTENCE,
    val lessonData: ReadSentenceItemNew? = null,
    val questions: List<LessonSentence> = emptyList(),
    val currentIndex: Int = 0,
    val selectedAnswer: String? = null,
    val score: Int = 0,
    val isCorrect: Boolean = false,
    val showResult: Boolean = false,

    // 🔥 ADD THESE
    val currentBlankWord: BlankableWord? = null,
    val displaySentence: String = "",
    val options: List<String> = emptyList()
) {
    val currentQuestion: LessonSentence?
        get() = questions.getOrNull(currentIndex)

    val correctAnswer: String
        get() = currentBlankWord?.word ?: ""
}