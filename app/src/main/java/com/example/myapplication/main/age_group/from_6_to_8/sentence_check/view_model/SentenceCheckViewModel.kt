package com.example.myapplication.main.age_group.from_6_to_8.sentence_check.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.generation.loader.FunFactBank
import com.example.myapplication.data.generation.loader.MatchPictureLoader
import com.example.myapplication.data.generation.loader.SentenceBuilderLogic
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.SentenceUnit
import com.example.myapplication.data.model.TrueFalseQuestion
import com.example.myapplication.data.model.displayTitle
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.SessionRepository
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
    @ApplicationContext private val context: Context,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private var startTimeMs = System.currentTimeMillis()

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

        // 3 grammar True/False from the sentence data...
        val selected = allQuestions
            .shuffled()
            .take(3)

        val questions = mutableListOf<TrueFalseQuestion>()

        selected.forEach { item ->
            // 50% chance TRUE / FALSE; each item carries an explanation. (item 3.2)
            val useCorrect = if (item.wrongOptions.isEmpty()) true else listOf(true, false).random()
            questions.add(SentenceBuilderLogic.makeTrueFalse(item = item, useCorrect = useCorrect))
        }

        // ...mixed with 2 fun-fact knowledge questions. (items 3.1 / 3.2)
        questions.addAll(FunFactBank.questions(2))
        questions.shuffle()

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
        if (_uiState.value.currentIndex == _uiState.value.questions.size - 1) {
            val state = _uiState.value
            val durationSec = ((System.currentTimeMillis() - startTimeMs) / 1000).toInt()
            sessionRepository.record(
                LearningSession(
                    moduleId = ModuleID.SENTENCE_CHECK,
                    ageGroup = AgeGroup.SIX_TO_EIGHT,
                    durationSeconds = durationSec,
                    score = state.score,
                    totalQuestions = state.questions.size,
                    correctItems = emptyList(),
                    wrongItems = emptyList(),
                    subConfig = "",
                    lessonTitle = null,
                    chapterTitle = state.unit.displayTitle
                )
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
        startTimeMs = System.currentTimeMillis()
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