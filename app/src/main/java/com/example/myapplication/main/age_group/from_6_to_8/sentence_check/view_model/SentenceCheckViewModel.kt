package com.example.myapplication.main.age_group.from_6_to_8.sentence_check.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.generation.loader.MatchPictureLoader
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.SentenceUnit
import com.example.myapplication.data.model.TrueFalseQuestion
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.AudioPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SentenceCheckViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(SentenceCheckUiState())
    val uiState: StateFlow<SentenceCheckUiState> = _uiState

    fun setData(unit: SentenceUnit, level: SentenceLevel) {
        _uiState.update { it.copy(unit = unit, level = level) }
        loadQuestions()
    }

    // CORE LOGIC
    private fun loadQuestions() {
        val state = _uiState.value

        val allQuestions = MatchPictureLoader.load(
            context = context,
            unit = state.unit,
            level = state.level
        )

        val selected = allQuestions
            .shuffled()
            .take(5)

        val questions = mutableListOf<TrueFalseQuestion>()

        selected.forEach { item ->

            val useCorrect = listOf(true, false).random()

            if (useCorrect) {
                // TRUE
                questions.add(
                    TrueFalseQuestion(
                        id = item.id + "_T",
                        imageName = item.imageName,
                        statement = item.correctSentence,
                        isTrue = "true"
                    )
                )
            } else {
                // FALSE
                val wrong = item.wrongOptions.randomOrNull()
                if (wrong != null) {
                    questions.add(
                        TrueFalseQuestion(
                            id = item.id + "_F",
                            imageName = item.imageName,
                            statement = wrong,
                            isTrue = "false"
                        )
                    )
                }
            }
        }

        _uiState.update {
            it.copy(
                questions = questions,
                currentIndex = 0,
                selectedAnswer = null,
                score = 0,
                showResult = false
            )
        }
    }

    // Select
    fun selectAnswer(answer: String) {
        val state = _uiState.value

        if (state.showResult) return

        val isCorrect = answer.equals(state.correctAnswer, ignoreCase = true)

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
                showResult = false,
            )
        }
        loadQuestions()
    }

    // UI helper
    fun backgroundType(option: String): ButtonType {
        val state = _uiState.value
        val selected = state.selectedAnswer ?: return ButtonType.OPTIONS

        return when {
            option.equals(state.correctAnswer, true) -> ButtonType.GREEN
            option.equals(selected, true) -> ButtonType.RED
            else -> ButtonType.OPTIONS
        }
    }
}