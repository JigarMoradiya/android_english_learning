package com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.beginner.multiple_choice.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.data.generation.loader.MixedGrammarData
import com.example.myapplication.data.model.WordType
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.FeedbackConstant.feedbackTitles
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GrammarMultipleChoiceViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(GrammarMultipleChoiceUiState())
    val uiState: StateFlow<GrammarMultipleChoiceUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        val questions = MixedGrammarData.multipleChoiceQuestions.shuffled()
        _uiState.update {
            it.copy(questions = questions, currentIndex = 0, score = 0,
                selectedAnswer = null, feedbackTitleRes = null,
                showFeedback = false, isAnswerCorrect = false, isCompleted = false)
        }
    }

    fun selectAnswer(type: WordType) {
        val state = _uiState.value
        if (state.showFeedback) return
        val isCorrect = type == state.currentQuestion?.correctType
        if (isCorrect) AudioPlayerManager.playSoundCorrectAnswer() else AudioPlayerManager.playSoundWrongAnswer()
        _uiState.update {
            it.copy(
                selectedAnswer = type,
                isAnswerCorrect = isCorrect,
                feedbackTitleRes = if (isCorrect) feedbackTitles.random() else R.string.its_wrong,
                showFeedback = true,
                score = if (isCorrect) it.score + 1 else it.score
            )
        }
    }

    fun next() {
        val nextIndex = _uiState.value.currentIndex + 1
        if (nextIndex >= _uiState.value.questions.size) { _uiState.update { it.copy(isCompleted = true) }; return }
        _uiState.update {
            it.copy(currentIndex = nextIndex, selectedAnswer = null,
                feedbackTitleRes = null, showFeedback = false, isAnswerCorrect = false)
        }
    }

    val isLastQuestion: Boolean get() = _uiState.value.currentIndex >= _uiState.value.questions.lastIndex

    fun optionButtonType(type: WordType): ButtonType {
        val state = _uiState.value
        if (!state.showFeedback) return ButtonType.OPTIONS
        return when (type) {
            state.currentQuestion?.correctType -> ButtonType.GREEN
            state.selectedAnswer               -> ButtonType.RED
            else                               -> ButtonType.OPTIONS
        }
    }
}
