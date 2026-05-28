package com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.adjectives.practice.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.model.SentenceLevel.EASY
import com.example.myapplication.data.model.WordType
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.SessionRepository
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
class AdjectivesPracticeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ttsManager: TextToSpeechManager,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private var startTimeMs = System.currentTimeMillis()

    private val _uiState = MutableStateFlow(AdjectivesPracticeUiState())
    val uiState: StateFlow<AdjectivesPracticeUiState> = _uiState.asStateFlow()

    init {
        generateQuestions()
    }
    private fun generateQuestions() {
        val allQuestions = generateQuestions(context = context, level = EASY, targetType = WordType.ADJECTIVE)
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
        if (_uiState.value.currentIndex == _uiState.value.questions.lastIndex) {
            val state = _uiState.value
            val durationSec = ((System.currentTimeMillis() - startTimeMs) / 1000).toInt()
            sessionRepository.record(
                LearningSession(
                    moduleId = ModuleID.GRAMMAR_ADJECTIVES,
                    ageGroup = AgeGroup.SIX_TO_EIGHT,
                    durationSeconds = durationSec,
                    score = state.score,
                    totalQuestions = state.questions.size,
                    subConfig = "",
                    lessonTitle = null,
                    chapterTitle = "Practice"
                )
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
                it.copy(isCompleted = true)
            }
        }
    }

    fun restart() {
        startTimeMs = System.currentTimeMillis()
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