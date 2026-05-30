package com.example.myapplication.main.age_group.from_5_to_7.opposite_words.choose_opposite.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.generation.loader.OppositeDifficulty
import com.example.myapplication.data.generation.loader.OppositeWordPair
import com.example.myapplication.data.generation.loader.OppositeWordsData
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.SessionRepository
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.FeedbackConstant.feedbackTitles
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseCorrectOppositeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChooseCorrectOppositeUiState())
    val uiState: StateFlow<ChooseCorrectOppositeUiState> = _uiState.asStateFlow()

    private var fullWordPool: List<OppositeWordPair> = emptyList()
    private var questionSet: List<OppositeWordPair> = emptyList()
    private var currentDifficulty = OppositeDifficulty.EASY

    private val sessionCorrect = mutableListOf<String>()
    private val sessionWrong = mutableListOf<String>()
    private var startTimeMs = System.currentTimeMillis()
    private var sessionRecorded = false

    private val difficultySubConfig get() = "CHOOSE_${currentDifficulty.name}"

    fun loadDifficulty(difficulty: OppositeDifficulty) {
        currentDifficulty = difficulty
        fullWordPool = OppositeWordsData.getPairsForDifficulty(difficulty)
        startNewRound()
    }

    fun startNewRound() {
        questionSet = fullWordPool.shuffled().take(10)
        sessionCorrect.clear()
        sessionWrong.clear()
        startTimeMs = System.currentTimeMillis()
        sessionRecorded = false
        _uiState.update {
            it.copy(questionIndex = 0, score = 0, showCompletePopup = false)
        }
        loadNextQuestion()
    }

    fun loadNextQuestion() {
        val index = _uiState.value.questionIndex
        if (index >= questionSet.size) return
        val pair = questionSet[index]

        val showWordAsQuestion = (0..1).random() == 0
        val questionWord = if (showWordAsQuestion) pair.word else pair.opposite
        val correctAnswer = if (showWordAsQuestion) pair.opposite else pair.word

        val wrongOptions = fullWordPool
            .filter { it != pair }
            .shuffled()
            .take(2)
            .map { if (showWordAsQuestion) it.opposite else it.word }

        _uiState.update {
            it.copy(
                currentWord = questionWord,
                correctAnswer = correctAnswer,
                options = (wrongOptions + correctAnswer).shuffled(),
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
            sessionCorrect.add(state.currentWord)
        } else {
            AudioPlayerManager.playSoundWrongAnswer()
            sessionWrong.add(state.currentWord)
        }

        val feedbackText = if (isCorrect) context.getString(feedbackTitles.random())
                           else wrongFeedback(state.correctAnswer)
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
                moduleId = ModuleID.OPPOSITES_WORD,
                ageGroup = AgeGroup.FIVE_TO_SEVEN,
                durationSeconds = duration,
                score = sessionCorrect.size,
                totalQuestions = _uiState.value.totalQuestions,
                wrongItems = sessionWrong.toList(),
                correctItems = sessionCorrect.toList(),
                subConfig = difficultySubConfig
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        recordSession()
    }

    private fun wrongFeedback(correctAnswer: String): String {
        val templates = listOf(
            "Not quite! It's $correctAnswer",
            "Oops! The opposite is $correctAnswer",
            "Almost! It's $correctAnswer",
            "Try again! It's $correctAnswer",
            "Incorrect! It's $correctAnswer",
            "The opposite was $correctAnswer"
        )
        return templates.random()
    }
}
