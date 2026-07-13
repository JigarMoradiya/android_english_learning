package com.example.myapplication.main.age_group.from_5_to_7.singular_plural.spot_wrong.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.generation.loader.WrongPluralEntry
import com.example.myapplication.data.generation.loader.WrongPluralQuestionFactory
import com.example.myapplication.data.generation.loader.wrongPluralEntries
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

data class SpotWrongPluralUiState(
    val options: List<String> = emptyList(),
    val wrongOption: String = "",
    val ruleHint: String = "",
    val selectedAnswer: String? = null,
    val isAnswerCorrect: Boolean = false,
    val feedbackText: String? = null,
    val countdown: Int? = null,
    val questionIndex: Int = 0,
    val totalQuestions: Int = 5,
    val score: Int = 0,
    val showCompletePopup: Boolean = false,
)

@HiltViewModel
class SpotWrongPluralViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SpotWrongPluralUiState())
    val uiState: StateFlow<SpotWrongPluralUiState> = _uiState.asStateFlow()

    private var questionSet = emptyList<WrongPluralEntry>()
    private val sessionCorrect = mutableListOf<String>()
    private val sessionWrong = mutableListOf<String>()
    private var startTimeMs = System.currentTimeMillis()
    private var sessionRecorded = false

    init { startNewRound() }

    fun startNewRound() {
        questionSet = wrongPluralEntries.shuffled().take(_uiState.value.totalQuestions)
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
        val question = WrongPluralQuestionFactory.make(questionSet[index], wrongPluralEntries)

        _uiState.update {
            it.copy(
                options = question.options,
                wrongOption = question.wrongOption,
                ruleHint = question.entry.ruleHint,
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
        val isCorrect = answer == state.wrongOption

        if (isCorrect) {
            AudioPlayerManager.playSoundCorrectAnswer()
            sessionCorrect.add(state.wrongOption)
        } else {
            AudioPlayerManager.playSoundWrongAnswer()
            sessionWrong.add(state.wrongOption)
        }

        val feedbackText = if (isCorrect) "Yes! ${state.ruleHint}"
                           else "${state.wrongOption} is the fake! ${state.ruleHint}"
        val nextIndex = state.questionIndex + 1
        val isLast = nextIndex >= state.totalQuestions

        _uiState.update {
            it.copy(
                selectedAnswer = answer,
                isAnswerCorrect = isCorrect,
                feedbackText = feedbackText,
                countdown = 3,
                questionIndex = nextIndex,
                score = if (isCorrect) it.score + 1 else it.score
            )
        }

        viewModelScope.launch {
            for (i in 2 downTo 1) {
                delay(1000)
                _uiState.update { it.copy(countdown = i) }
            }
            delay(1000)
            if (isLast) {
                recordSession()
                _uiState.update { it.copy(showCompletePopup = true) }
            } else {
                loadNextQuestion()
            }
        }
    }

    fun optionButtonType(option: String): ButtonType {
        val state = _uiState.value
        val selected = state.selectedAnswer ?: return ButtonType.OPTIONS
        return when (option) {
            state.wrongOption -> ButtonType.GREEN
            selected          -> ButtonType.RED
            else              -> ButtonType.OPTIONS
        }
    }

    fun recordSession() {
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
                subConfig = "SPOT_WRONG_PLURAL"
            )
        )
    }
}
