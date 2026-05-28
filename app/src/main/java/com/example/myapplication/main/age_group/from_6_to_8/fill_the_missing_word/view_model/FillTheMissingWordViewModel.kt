package com.example.myapplication.main.age_group.from_6_to_8.fill_the_missing_word.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.generation.WordBank
import com.example.myapplication.data.generation.loader.OneWordAnswerLoader
import com.example.myapplication.data.model.BlankableWord
import com.example.myapplication.data.model.ReadSentenceItemNew
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.UnitSelectionScreen
import com.example.myapplication.data.model.displayTitle
import com.example.myapplication.data.model.WordType
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
class FillTheMissingWordViewModel @Inject constructor(
    private val progressManager: SentenceProgressManager,
    private val sessionRepository: SessionRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val startTimeMs = System.currentTimeMillis()

    private val _uiState = MutableStateFlow(FillTheMissingWordUiState())
    val uiState: StateFlow<FillTheMissingWordUiState> = _uiState

    // Call this from UI
    fun setScreenTypeAndLessonData(screenType: UnitSelectionScreen, lessonData: ReadSentenceItemNew, level: SentenceLevel) {
        _uiState.update {
            it.copy(
                screenType = screenType,
                lessonData = lessonData,
                level = level,
            )
        }

        loadQuestions()
    }

    // Load + shuffle
    private fun loadQuestions() {
        val state = _uiState.value

        val shuffled = state.lessonData?.sentences?.shuffled() ?: emptyList()

        _uiState.update {
            it.copy(
                questions = shuffled,
                currentIndex = 0,
                selectedAnswer = null,
                score = 0,
                showResult = false
            )
        }

        prepareCurrentQuestion() // 🔥 IMPORTANT
    }

    private fun prepareCurrentQuestion() {
        val state = _uiState.value
        val question = state.currentQuestion ?: return

        val blank = question.blankableWords
            .filter { it.type != WordType.PRONOUN }
            .randomOrNull() ?: return

        val displaySentence = question.text.replace(blank.word, "____")

        val options = generateOptions(blank)

        _uiState.update {
            it.copy(
                currentBlankWord = blank,
                displaySentence = displaySentence,
                options = options,
                selectedAnswer = null
            )
        }
    }

    private fun generateOptions(blank: BlankableWord): List<String> {

        val type = blank.type ?: WordType.NOUN // 👈 fallback (important)

        val pool = when (type) {
            WordType.NOUN -> WordBank.nouns
            WordType.VERB -> WordBank.verbs
            WordType.ADJECTIVE -> WordBank.adjectives
            WordType.PRONOUN -> WordBank.pronouns
        }

        val wrongOptions = pool
            .filter { it.equals(blank.word, ignoreCase = true).not() }
            .shuffled()
            .take(3)

        return (wrongOptions + blank.word).shuffled()
    }

    fun selectAnswer(answer: String) {
        val state = _uiState.value
        if (state.selectedAnswer != null) return // prevent double click

        val correct = state.correctAnswer

        val isCorrect = answer.equals(correct, ignoreCase = true)

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

    fun backgroundType(option: String): ButtonType {
        val state = _uiState.value
        val selected = state.selectedAnswer ?: return ButtonType.OPTIONS

        return when {
            option.equals(state.correctAnswer, true) -> ButtonType.GREEN
            option.equals(selected, true) -> ButtonType.RED
            else -> ButtonType.OPTIONS
        }
    }

    // Next Question
    fun nextQuestion() {
        val state = _uiState.value

        if (state.currentIndex < state.questions.size - 1) {
            _uiState.update {
                it.copy(currentIndex = it.currentIndex + 1)
            }
            prepareCurrentQuestion() // 🔥 IMPORTANT
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
                moduleId = ModuleID.FILL_MISSING_WORD,
                ageGroup = AgeGroup.SIX_TO_EIGHT,
                durationSeconds = ((System.currentTimeMillis() - startTimeMs) / 1000).toInt(),
                score = state.score,
                totalQuestions = state.questions.size,
                correctItems = emptyList(),
                wrongItems = emptyList(),
                subConfig = state.level.title,
                lessonTitle = state.lessonData?.title,
                chapterTitle = state.lessonData?.unit?.displayTitle
            )
        )
        _uiState.update {
            it.copy(showResult = true)
        }
    }
}