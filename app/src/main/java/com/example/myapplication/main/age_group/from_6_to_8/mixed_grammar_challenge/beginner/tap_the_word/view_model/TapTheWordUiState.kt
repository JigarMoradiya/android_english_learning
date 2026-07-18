package com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.beginner.tap_the_word.view_model

import com.example.myapplication.data.generation.loader.MixedBeginnerQuestion

data class TapTheWordUiState(
    val isLoading: Boolean = true,
    val questionsAll: List<MixedBeginnerQuestion> = emptyList(),
    val questions: List<MixedBeginnerQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedWord: String? = null,   // lowercased tapped word (unused in tap-all, kept for parity)
    val foundWords: Set<String> = emptySet(),   // correct taps (lowercased)
    val wrongWords: Set<String> = emptySet(),   // incorrect taps (lowercased)
    val hadWrongTap: Boolean = false,           // any wrong tap → question scored incorrect
    val isAnswerCorrect: Boolean = false,
    val feedbackTitleRes: Int? = null,
    val feedbackSubTitle: Int? = null,          // success subline (string res)
    val feedbackSubTitleText: String? = null,   // failure message (dynamic, tap-all)
    val showNext: Boolean = false,
    val score: Int = 0,
    val isCompleted: Boolean = false
) {
    val currentQuestion: MixedBeginnerQuestion? get() = questions.getOrNull(currentIndex)
    val isLastIndex: Boolean get() = currentIndex == questions.size - 1
}
