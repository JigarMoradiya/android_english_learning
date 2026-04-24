package com.example.myapplication.main.age_group.from_6_to_8.choose_the_right_sentence.which_sentence_sound_right.view_model

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.generation.loader.SoundCorrectLoader
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
class WhichSentenceSoundRightViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(WhichSentenceSoundRightUiState())
    val uiState: StateFlow<WhichSentenceSoundRightUiState> = _uiState

    fun setData(unit: SentenceUnit, level: SentenceLevel) {
        _uiState.update {
            it.copy(unit = unit, level = level)
        }
        loadQuestions()
    }

    // Load + filter + shuffle + take 5
    private fun loadQuestions() {
        val state = _uiState.value

        val allQuestions = SoundCorrectLoader.load(
            context = context,
            unit = state.unit,
            level = state.level
        )
        val filtered = allQuestions
            .shuffled()
            .take(5)

        _uiState.update {
            it.copy(
                questions = filtered,
                currentIndex = 0,
                selectedAnswer = null,
                score = 0,
                showResult = false
            )
        }

        generateOptions()
    }

    // Generate options per question
    private fun generateOptions() {
        val state = _uiState.value
        val question = state.currentQuestion ?: return

        val requiredCount = 4
        val options = mutableListOf(question.correctSentence)
        val wrongs = question.wrongOptions.shuffled()

        for (wrong in wrongs) {
            if (options.size >= requiredCount) break
            options.add(wrong)
        }
        _uiState.update {
            it.copy(options = options.shuffled())
        }
    }

    // Select answer

    fun selectAnswer(answer: String) {
        val state = _uiState.value

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

    // Next question
    fun next() {
        val state = _uiState.value
        Log.e("jigarMatch","questions size = "+ state.questions.size)
        Log.e("jigarMatch","currentIndex = "+ state.currentIndex)
        if (state.currentIndex < state.questions.size - 1) {

            _uiState.update {
                it.copy(
                    currentIndex = it.currentIndex + 1,
                    selectedAnswer = null,
                    showResult = false
                )
            }

            generateOptions()

        } else {
            finish()
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

    // Finish
    private fun finish() {
        _uiState.update {
            it.copy(showResult = true)
        }
    }

    // UI helper (same as iOS)
    fun backgroundType(option: String): ButtonType {
        val state = _uiState.value
        val selected = state.selectedAnswer ?: return ButtonType.OPTIONS

        // Correct answer
        if (option == state.correctAnswer) {
            return ButtonType.GREEN
        }

        // Selected wrong answer
        if (option == selected) {
            return ButtonType.RED
        }

        return ButtonType.OPTIONS
    }
}