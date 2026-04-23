package com.example.myapplication.main.age_group.from_6_to_8.choose_the_right_sentence.match_the_picture.view_model

import com.example.myapplication.data.model.MatchPictureQuestion
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.SentenceUnit
import com.example.myapplication.data.model.UnitSelectionScreen


data class MatchThePictureUiState(
    val unit: SentenceUnit = SentenceUnit.PLAY_AND_FUN,
    val level: SentenceLevel = SentenceLevel.EASY,

    val questions: List<MatchPictureQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedAnswer: String? = null,
    val score: Int = 0,

    val showResult: Boolean = false,
    val options: List<String> = emptyList()
) {
    val currentQuestion: MatchPictureQuestion?
        get() = questions.getOrNull(currentIndex)

    val correctAnswer: String?
        get() = currentQuestion?.correctSentence
}