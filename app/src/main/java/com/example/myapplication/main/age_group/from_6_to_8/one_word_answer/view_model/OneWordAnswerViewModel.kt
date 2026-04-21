package com.example.myapplication.main.age_group.from_6_to_8.one_word_answer.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.generation.loader.OneWordAnswerLoader
import com.example.myapplication.data.model.ReadSentenceItemNew
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.UnitSelectionScreen
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
    @ApplicationContext private val context: Context
) : ViewModel() {

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
                isCorrect = isCorrect,
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
            lessonId = state.lessonData?.id?:"colors_1"
        )

        _uiState.update {
            it.copy(showResult = true)
        }
    }
}