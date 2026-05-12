package com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.verb.practice.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.data.model.SentenceLevel.EASY
import com.example.myapplication.data.model.WordType
import com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.common_ui.practice.GrammarPracticeQuestionFactory.generateQuestions
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utilities.TextToSpeechManager
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.FeedbackConstant.feedbackGiveAnswerSubTitleCorrect
import com.example.myapplication.utils.FeedbackConstant.feedbackTitles
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class VerbPracticeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ttsManager: TextToSpeechManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(VerbPracticeUiState())
    val uiState: StateFlow<VerbPracticeUiState> = _uiState.asStateFlow()

    init {
        generateQuestions()
    }
    private fun generateQuestions() {
        val allQuestions = generateQuestions(context = context, level = EASY, targetType = WordType.VERB)
        _uiState.update {
            it.copy(questionsAll = allQuestions)
        }
        loadInitialData()
    }

    val isLastIndex: Boolean
        get() = _uiState.value.currentIndex ==
                _uiState.value.questions.lastIndex

    private fun loadInitialData() {
        val selectedQuestions = _uiState.value.questionsAll
            .shuffled()
            .take(10)

        _uiState.update {
            it.copy(questions = selectedQuestions)
        }

        loadCurrentQuestionOptions()
    }

    private fun loadCurrentQuestionOptions() {
        val question = _uiState.value.currentQuestion ?: return

        _uiState.update {
            it.copy(shuffledOptions = question.options.shuffled())
        }
    }

    fun selectAnswer(option: String) {
        val currentQuestion = _uiState.value.currentQuestion ?: return

        val isCorrect = option == currentQuestion.correctAnswer
        val randomTitle = feedbackTitles.random()
        val randomSubTitle = feedbackGiveAnswerSubTitleCorrect.random()
        if (isCorrect){
            AudioPlayerManager.playSoundCorrectAnswer()
        }else{
            AudioPlayerManager.playSoundWrongAnswer()
        }
        _uiState.update {
            it.copy(
                selectedAnswer = option,
                feedbackTextRes = if (isCorrect) randomTitle else R.string.its_wrong,
                feedbackSubTextRes = if (isCorrect) randomSubTitle else null,
                showNext = true,
                isAnswerCorrect = isCorrect,
                score = if (isCorrect) it.score + 1 else it.score
            )
        }
    }

    fun moveToNextQuestion() {
        val state = _uiState.value

        if (state.currentIndex < state.questions.lastIndex) {
            _uiState.update {
                it.copy(
                    currentIndex = it.currentIndex + 1
                )
            }

            resetFeedback()
            loadCurrentQuestionOptions()

        } else {
            _uiState.update {
                it.copy(
                    isCompleted = true
                )
            }
        }
    }

    fun restart() {
        _uiState.update {
            it.copy(
                currentIndex = 0,
                feedbackTextRes = null,
                feedbackSubTextRes = null,
                showNext = false,
                isAnswerCorrect = false,
                selectedAnswer = null,
                questions = emptyList(),
                shuffledOptions = emptyList(),
                score = 0,
                isCompleted = false,
            )
        }
        loadInitialData()
    }

    private fun resetFeedback() {
        _uiState.update {
            it.copy(
                feedbackTextRes = null,
                feedbackSubTextRes = null,
                showNext = false,
                isAnswerCorrect = false,
                selectedAnswer = null
            )
        }
    }

    fun backgroundType(option: String): ButtonType {
        val selected = _uiState.value.selectedAnswer
            ?: return ButtonType.OPTIONS

        val correctAnswer = _uiState.value.currentQuestion?.correctAnswer

        return when (option) {
            correctAnswer -> ButtonType.GREEN
            selected -> ButtonType.RED
            else -> ButtonType.OPTIONS
        }
    }
}