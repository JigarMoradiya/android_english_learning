package com.example.myapplication.main.age_group.from_6_to_8.choose_the_right_sentence.find_the_correct_writing.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.generation.loader.GrammarQuestionGenerator
import com.example.myapplication.data.generation.loader.LessonLoader
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.SentenceUnit
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.AudioPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FindTheCorrectWritingViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(FindTheCorrectWritingUiState())
    val uiState: StateFlow<FindTheCorrectWritingUiState> = _uiState

    fun setData(unit: SentenceUnit, level: SentenceLevel) {
        _uiState.update {
            it.copy(unit = unit, level = level)
        }
        loadQuestions()
    }

    // Load from lessons → generate grammar questions
    private fun loadQuestions() {
        val state = _uiState.value

        val lessons = LessonLoader.load(
            context = context,
            unit = state.unit,
            level = state.level
        )

        val questions = GrammarQuestionGenerator
            .generate(lessons)
            .shuffled()
            .take(5)

        _uiState.update {
            it.copy(
                questions = questions,
                currentIndex = 0,
                selectedAnswer = null,
                score = 0,
                showResult = false
            )
        }

        generateOptions()
    }

    // SIMPLE (no generation logic needed)
    private fun generateOptions() {
        val state = _uiState.value
        val question = state.currentQuestion ?: return

        _uiState.update {
            it.copy(options = question.options)
        }
    }

    // Select
    fun selectAnswer(answer: String) {
        val state = _uiState.value

        if (state.selectedAnswer != null) return // prevent double click

        val isCorrect = answer == state.correctAnswer

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

    // Next
    fun next() {
        val state = _uiState.value

        if (state.currentIndex < state.questions.size - 1) {

            _uiState.update {
                it.copy(
                    currentIndex = it.currentIndex + 1,
                    selectedAnswer = null
                )
            }

            generateOptions()

        } else {
            _uiState.update {
                it.copy(showResult = true)
            }
        }
    }

    // Restart
    fun restart() {
        _uiState.update {
            it.copy(
                currentIndex = 0,
                score = 0,
                selectedAnswer = null,
                showResult = false
            )
        }

        loadQuestions()
    }

    // UI helper
    fun backgroundType(option: String): ButtonType {
        val state = _uiState.value
        val selected = state.selectedAnswer ?: return ButtonType.OPTIONS

        return when (option) {
            state.correctAnswer -> ButtonType.GREEN
            selected -> ButtonType.RED
            else -> ButtonType.OPTIONS
        }
    }
}