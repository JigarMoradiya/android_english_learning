package com.example.myapplication.main.age_group.from_5_to_7.opposite_words.choose_opposite.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.generation.loader.OppositeDifficulty
import com.example.myapplication.data.generation.loader.OppositeWordsData
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
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChooseCorrectOppositeUiState())
    val uiState: StateFlow<ChooseCorrectOppositeUiState> = _uiState.asStateFlow()

    private var wordPool: List<com.example.myapplication.data.generation.loader.OppositeWordPair> = emptyList()
    private val usedIndices = mutableSetOf<Int>()

    fun loadDifficulty(difficulty: OppositeDifficulty) {
        wordPool = OppositeWordsData.getPairsForDifficulty(difficulty)
        usedIndices.clear()
        loadNextQuestion()
    }

    fun loadNextQuestion() {
        if (wordPool.isEmpty()) return

        // Reset used pool when all pairs have been shown
        if (usedIndices.size >= wordPool.size) {
            usedIndices.clear()
        }

        // Pick a random unused index
        var index: Int
        do { index = wordPool.indices.random() } while (usedIndices.contains(index))
        usedIndices.add(index)

        val pair = wordPool[index]

        // Randomly decide which side is the "question" — word or opposite
        val showWordAsQuestion = (0..1).random() == 0
        val questionWord = if (showWordAsQuestion) pair.word else pair.opposite
        val correctAnswer = if (showWordAsQuestion) pair.opposite else pair.word

        // Pick 2 wrong distractors from the same side
        val wrongOptions = wordPool
            .filter { it != pair }
            .shuffled()
            .take(2)
            .map { if (showWordAsQuestion) it.opposite else it.word }

        val options = (wrongOptions + correctAnswer).shuffled()

        _uiState.update {
            it.copy(
                currentWord = questionWord,
                correctAnswer = correctAnswer,
                options = options,
                selectedAnswer = null,
                isAnswerCorrect = false,
                feedbackText = null,
                countdown = null
            )
        }
    }

    fun checkAnswer(answer: String) {
        if (_uiState.value.selectedAnswer != null) return  // already answered

        val isCorrect = answer == _uiState.value.correctAnswer

        if (isCorrect) AudioPlayerManager.playSoundCorrectAnswer()
        else AudioPlayerManager.playSoundWrongAnswer()

        val feedbackText = if (isCorrect) {
            context.getString(feedbackTitles.random())
        } else {
            wrongFeedback(_uiState.value.correctAnswer)
        }

        _uiState.update {
            it.copy(
                selectedAnswer = answer,
                isAnswerCorrect = isCorrect,
                feedbackText = feedbackText,
                countdown = 3
            )
        }

        // 3 → 2 → 1 → next question
        viewModelScope.launch {
            delay(1_000)
            _uiState.update { it.copy(countdown = 2) }
            delay(1_000)
            _uiState.update { it.copy(countdown = 1) }
            delay(1_000)
            loadNextQuestion()
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
