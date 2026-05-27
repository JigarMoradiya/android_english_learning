package com.example.myapplication.main.age_group.from_6_to_8.one_word_answer.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.generation.loader.OneWordAnswerLoader
import com.example.myapplication.data.model.ReadSentenceItemNew
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.UnitSelectionScreen
import com.example.myapplication.data.model.displayTitle
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.SessionRepository
import com.example.myapplication.main.age_group.from_6_to_8.common.unit.data.SentenceProgressManager
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.AudioPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class OneWordAnswerViewModel @Inject constructor(
    private val progressManager: SentenceProgressManager,
    private val sessionRepository: SessionRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val startTimeMs = System.currentTimeMillis()

    private val _uiState = MutableStateFlow(OneWordAnswerUiState())
    val uiState: StateFlow<OneWordAnswerUiState> = _uiState

    // Call this from UI
    fun setScreenTypeAndLessonData(screenType: UnitSelectionScreen, lessonData: ReadSentenceItemNew, level: SentenceLevel) {
        _uiState.update {
            it.copy(
                screenType = screenType,
                lessonData = lessonData,
                level = level
            )
        }

        loadQuestions()
    }

    // Load + shuffle
    private fun loadQuestions() {
        val state = _uiState.value
        var loadedQuestions = OneWordAnswerLoader.load(
            context = context,
            lessonId = state.lessonData?.id?:"colors_1",
            level = state.level
        ).toMutableList()

        // 🔀 Shuffle questions
        loadedQuestions.shuffle()

        // 🔀 Shuffle options
        loadedQuestions = loadedQuestions.map { question ->
            question.copy(options = question.options.shuffled())
        }.toMutableList()

        _uiState.update {
            it.copy(
                questions = loadedQuestions,
                currentIndex = 0,
                selectedAnswer = null,
                score = 0,
                showResult = false
            )
        }
    }

    fun backgroundType(option: String): ButtonType {
        val state = _uiState.value
        val selected = state.selectedAnswer ?: return ButtonType.OPTIONS

        // Correct answer
        if (option == state.currentQuestion?.correctAnswer) {
            return ButtonType.GREEN
        }

        // Selected wrong answer
        if (option == selected) {
            return ButtonType.RED
        }

        return ButtonType.OPTIONS
    }

    // Select Answer
    fun selectAnswer(answer: String) {
        val state = _uiState.value
        val currentQuestion = state.currentQuestion ?: return

        val isCorrect = answer == currentQuestion.correctAnswer

        if (isCorrect) {
            AudioPlayerManager.playSoundCorrectAnswer()
        } else {
            AudioPlayerManager.playSoundWrongAnswer()
        }

        _uiState.update {
            it.copy(
                selectedAnswer = answer,
                score = if (isCorrect) it.score + 1 else it.score
            )
        }
    }

    // Next Question
    fun nextQuestion() {
        val state = _uiState.value

        if (state.currentIndex < state.questions.size - 1) {
            _uiState.update {
                it.copy(
                    currentIndex = it.currentIndex + 1,
                    selectedAnswer = null
                )
            }
        } else {
            finishLesson()
        }
    }

    // Finish
    private fun finishLesson() {
        val state = _uiState.value
        progressManager.markCompleted(
            type = state.screenType,
            lessonId = state.lessonData?.id ?: "colors_1"
        )
        sessionRepository.record(
            LearningSession(
                moduleId = ModuleID.ONE_WORD_ANSWER,
                ageGroup = AgeGroup.SIX_TO_EIGHT,
                durationSeconds = ((System.currentTimeMillis() - startTimeMs) / 1000).toInt(),
                score = state.score,
                totalQuestions = state.questions.size,
                correctItems = emptyList(),
                wrongItems = emptyList(),
                subConfig = state.level?.title ?: "Short Sentence",
                lessonTitle = state.lessonData?.title,
                chapterTitle = state.lessonData?.unit?.displayTitle
            )
        )
        _uiState.update {
            it.copy(showResult = true)
        }
    }
}