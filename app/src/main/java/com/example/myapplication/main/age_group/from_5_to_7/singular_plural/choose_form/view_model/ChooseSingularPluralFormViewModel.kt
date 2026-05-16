package com.example.myapplication.main.age_group.from_5_to_7.singular_plural.choose_form.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.data.generation.loader.SingularPluralData
import com.example.myapplication.data.generation.loader.SingularPluralPair
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
class ChooseSingularPluralFormViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChooseSingularPluralFormUiState())
    val uiState: StateFlow<ChooseSingularPluralFormUiState> = _uiState.asStateFlow()

    init { generateQuestions() }

    fun generateQuestions() {
        val pairs = SingularPluralData.allPairs.shuffled().take(12)
        _uiState.update { it.copy(allPairs = pairs, currentIndex = 0, score = 0, isCompleted = false) }
        loadQuestion(pairs, 0)
    }

    private fun loadQuestion(pairs: List<SingularPluralPair>, index: Int) {
        val pair = pairs.getOrNull(index) ?: return
        val qType = if (index % 2 == 0) FormQuestion.GIVE_PLURAL else FormQuestion.GIVE_SINGULAR
        val prompt: String
        val correct: String

        if (qType == FormQuestion.GIVE_PLURAL) {
            prompt = pair.singular; correct = pair.plural
        } else {
            prompt = pair.plural; correct = pair.singular
        }

        val pool = if (qType == FormQuestion.GIVE_PLURAL) pairs.map { it.plural } else pairs.map { it.singular }
        val options = (pool.filter { it != correct }.shuffled().take(3) + correct).shuffled()

        _uiState.update {
            it.copy(
                currentIndex = index,
                questionType = qType,
                prompt = prompt,
                options = options,
                correctAnswer = correct,
                selectedAnswer = null,
                isAnswerCorrect = false,
                feedbackTitleRes = null,
                showFeedback = false
            )
        }
    }

    fun selectAnswer(choice: String) {
        val state = _uiState.value
        if (state.showFeedback) return
        val isCorrect = choice == state.correctAnswer
        if (isCorrect) AudioPlayerManager.playSoundCorrectAnswer() else AudioPlayerManager.playSoundWrongAnswer()
        _uiState.update {
            it.copy(
                selectedAnswer = choice,
                isAnswerCorrect = isCorrect,
                feedbackTitleRes = if (isCorrect) feedbackTitles.random() else R.string.its_wrong,
                showFeedback = true,
                score = if (isCorrect) it.score + 1 else it.score
            )
        }
    }

    fun next() {
        val state = _uiState.value
        val nextIndex = state.currentIndex + 1
        if (nextIndex >= state.allPairs.size) { _uiState.update { it.copy(isCompleted = true) }; return }
        loadQuestion(state.allPairs, nextIndex)
    }

    val isLastQuestion: Boolean get() = _uiState.value.currentIndex >= _uiState.value.allPairs.lastIndex

    fun optionButtonType(option: String): ButtonType {
        val state = _uiState.value
        if (!state.showFeedback) return ButtonType.OPTIONS
        return when (option) {
            state.correctAnswer  -> ButtonType.GREEN
            state.selectedAnswer -> ButtonType.RED
            else                 -> ButtonType.OPTIONS
        }
    }
}
