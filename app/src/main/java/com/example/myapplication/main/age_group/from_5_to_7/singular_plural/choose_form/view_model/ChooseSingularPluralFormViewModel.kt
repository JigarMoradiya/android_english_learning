package com.example.myapplication.main.age_group.from_5_to_7.singular_plural.choose_form.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.generation.loader.singularPluralWords
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.AudioPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseSingularPluralFormViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ChooseSingularPluralFormUiState())
    val uiState: StateFlow<ChooseSingularPluralFormUiState> = _uiState.asStateFlow()

    private val usedIndices = mutableSetOf<Int>()

    init { loadNextQuestion() }

    fun loadNextQuestion() {
        if (usedIndices.size >= singularPluralWords.size) usedIndices.clear()

        var index: Int
        do { index = (0 until singularPluralWords.size).random() } while (usedIndices.contains(index))
        usedIndices.add(index)

        val pair        = singularPluralWords[index]
        val showSingular = listOf(true, false).random()
        val count        = if (showSingular) 1 else listOf(2, 3, 4).random()

        _uiState.value = ChooseSingularPluralFormUiState(
            currentImageName = pair.singular.lowercase(),
            currentCount     = count,
            correctAnswer    = if (showSingular) pair.singular else pair.plural,
            options          = listOf(pair.singular, pair.plural).shuffled(),
            selectedAnswer   = null,
            isAnswerCorrect  = false,
            feedbackText     = null,
            countdown        = null
        )
    }

    fun checkAnswer(answer: String) {
        if (_uiState.value.selectedAnswer != null) return

        val state     = _uiState.value
        val isCorrect = answer == state.correctAnswer

        if (isCorrect) AudioPlayerManager.playSoundCorrectAnswer()
        else           AudioPlayerManager.playSoundWrongAnswer()

        val feedback = if (isCorrect) positiveFeedbacks.random()
                       else wrongFeedback(state.correctAnswer)

        _uiState.update {
            it.copy(
                selectedAnswer  = answer,
                isAnswerCorrect = isCorrect,
                feedbackText    = feedback,
                countdown       = 3
            )
        }

        // Countdown 3 → 2 → 1 → next question (mirrors iOS DispatchQueue.asyncAfter)
        viewModelScope.launch {
            delay(1_000); _uiState.update { it.copy(countdown = 2) }
            delay(1_000); _uiState.update { it.copy(countdown = 1) }
            delay(1_000); loadNextQuestion()
        }
    }

    fun optionButtonType(option: String): ButtonType {
        val state = _uiState.value
        val selected = state.selectedAnswer ?: return ButtonType.OPTIONS
        return when (option) {
            state.correctAnswer -> ButtonType.GREEN
            selected            -> ButtonType.RED
            else                -> ButtonType.OPTIONS
        }
    }

    // ── Feedback strings ──────────────────────────────────────────────────────

    private val positiveFeedbacks = listOf(
        "Great job! 🎉", "Excellent! 🌟", "Well done! 👏",
        "Fantastic! 🚀", "Amazing! ⭐", "You got it! 🎊",
        "Perfect! 🏆", "Brilliant! 💡"
    )

    private fun wrongFeedback(correctAnswer: String) = listOf(
        "Not quite! It's $correctAnswer",
        "Oops! The answer is $correctAnswer",
        "Almost! It's $correctAnswer",
        "Try again! It's $correctAnswer",
        "The correct form is $correctAnswer"
    ).random()
}
