package com.example.myapplication.main.age_group.from_5_to_7.singular_plural.choose_form.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.generation.loader.singularPluralWords
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.SessionRepository
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
class ChooseSingularPluralFormViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChooseSingularPluralFormUiState())
    val uiState: StateFlow<ChooseSingularPluralFormUiState> = _uiState.asStateFlow()

    private var questionSet = emptyList<com.example.myapplication.data.generation.loader.SingularPluralPair>()
    private val sessionCorrect = mutableListOf<String>()
    private val sessionWrong = mutableListOf<String>()
    private var startTimeMs = System.currentTimeMillis()
    private var sessionRecorded = false

    init { startNewRound() }

    fun startNewRound() {
        questionSet = singularPluralWords.shuffled().take(_uiState.value.totalQuestions)
        sessionCorrect.clear()
        sessionWrong.clear()
        startTimeMs = System.currentTimeMillis()
        sessionRecorded = false
        _uiState.update { it.copy(questionIndex = 0, score = 0, showCompletePopup = false) }
        loadNextQuestion()
    }

    fun loadNextQuestion() {
        val index = _uiState.value.questionIndex
        if (index >= questionSet.size) return
        val pair = questionSet[index]
        val showSingular = listOf(true, false).random()
        val count = if (showSingular) 1 else listOf(2, 3, 4).random()

        _uiState.update {
            it.copy(
                currentImageName = pair.singular.lowercase(),
                currentCount = count,
                correctAnswer = if (showSingular) pair.singular else pair.plural,
                options = listOf(pair.singular, pair.plural).shuffled(),
                selectedAnswer = null,
                isAnswerCorrect = false,
                feedbackText = null,
                countdown = null
            )
        }
    }

    fun checkAnswer(answer: String) {
        if (_uiState.value.selectedAnswer != null) return

        val state = _uiState.value
        val isCorrect = answer == state.correctAnswer

        if (isCorrect) {
            AudioPlayerManager.playSoundCorrectAnswer()
            sessionCorrect.add(state.currentImageName)
        } else {
            AudioPlayerManager.playSoundWrongAnswer()
            sessionWrong.add(state.currentImageName)
        }

        val feedback = if (isCorrect) positiveFeedbacks.random()
                       else wrongFeedback(state.correctAnswer)
        val nextIndex = state.questionIndex + 1
        val isLast = nextIndex >= state.totalQuestions

        _uiState.update {
            it.copy(
                selectedAnswer = answer,
                isAnswerCorrect = isCorrect,
                feedbackText = feedback,
                countdown = 3,
                questionIndex = nextIndex,
                score = if (isCorrect) it.score + 1 else it.score
            )
        }

        viewModelScope.launch {
            delay(1_000); _uiState.update { it.copy(countdown = 2) }
            delay(1_000); _uiState.update { it.copy(countdown = 1) }
            delay(1_000)
            if (isLast) {
                recordSession()
                _uiState.update { it.copy(showCompletePopup = true, countdown = null) }
            } else {
                loadNextQuestion()
            }
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

    private fun recordSession() {
        if (sessionRecorded) return
        if (_uiState.value.questionIndex < _uiState.value.totalQuestions) return
        sessionRecorded = true
        val duration = ((System.currentTimeMillis() - startTimeMs) / 1000).toInt()
        sessionRepository.record(
            LearningSession(
                moduleId = ModuleID.SINGULAR_PLURAL,
                ageGroup = AgeGroup.FIVE_TO_SEVEN,
                durationSeconds = duration,
                score = sessionCorrect.size,
                totalQuestions = _uiState.value.totalQuestions,
                wrongItems = sessionWrong.toList(),
                correctItems = sessionCorrect.toList(),
                subConfig = "CHOOSE_FORM"
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        recordSession()
    }

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
