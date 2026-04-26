package com.example.myapplication.main.age_group.from_6_to_8.sentence_builder.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.generation.loader.MatchPictureLoader
import com.example.myapplication.data.model.SentenceBuilderQuestion
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.SentenceUnit
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.FeedbackConstant.feedbackTitles
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SentenceBuilderViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(SentenceBuilderUiState())
    val uiState: StateFlow<SentenceBuilderUiState> = _uiState

    fun setData(unit: SentenceUnit, level: SentenceLevel) {
        _uiState.update { it.copy(unit = unit, level = level) }
        loadQuestions()
    }

    // Load Questions
    private fun loadQuestions() {
        val state = _uiState.value

        val all = MatchPictureLoader.load(
            context = context,
            unit = state.unit,
            level = state.level
        )
        .shuffled()
        .take(5)

        val questions = all.map {
            SentenceBuilderQuestion(
                id = it.id,
                imageName = it.imageName,
                correctSentence = it.correctSentence
            )
        }

        _uiState.update {
            it.copy(
                questions = questions,
                currentIndex = 0,
                score = 0,
                isCompleted = false
            )
        }

        prepareCurrentQuestion()
    }

    fun restart() {
        _uiState.update {
            it.copy(
                score = 0,
                currentIndex = 0,
                questions = emptyList()
            )
        }
        loadQuestions()
    }

    // Prepare Words
    private fun prepareCurrentQuestion() {
        val state = _uiState.value
        val sentence = state.currentQuestion?.correctSentence ?: return

        val clean = sentence
            .replace(".", "")
            .lowercase()

        val words = clean.split(" ")

        _uiState.update {
            it.copy(
                shuffledWords = words.shuffled(),
                arrangedWords = emptyList(),
                isCorrect = null
            )
        }
    }

    // Add Word
    fun addWord(word: String) {
        val state = _uiState.value
        val shuffled = state.shuffledWords.toMutableList()
        val arranged = state.arrangedWords.toMutableList()

        val index = shuffled.indexOf(word)
        if (index == -1) return

        arranged.add(word)
        shuffled.removeAt(index)
        AudioPlayerManager.playSoundMenuClick()

        _uiState.update {
            it.copy(
                arrangedWords = arranged,
                shuffledWords = shuffled
            )
        }

        checkIfComplete()
    }

    // Remove Word
    fun removeWord(word: String) {
        val state = _uiState.value
        val shuffled = state.shuffledWords.toMutableList()
        val arranged = state.arrangedWords.toMutableList()

        val index = arranged.indexOf(word)
        if (index == -1) return

        shuffled.add(word)
        arranged.removeAt(index)

        AudioPlayerManager.playSoundMenuClick()

        _uiState.update {
            it.copy(
                arrangedWords = arranged,
                shuffledWords = shuffled,
                isCorrect = null
            )
        }
    }

    // Check
    private fun checkIfComplete() {
        val state = _uiState.value

        val user = state.arrangedWords.joinToString(" ")

        val correct = state.currentQuestion?.correctSentence
            ?.replace(".", "")
            ?.lowercase()

        if (correct != null &&
            state.arrangedWords.size == correct.split(" ").size
        ) {
            if (user == correct) {
                AudioPlayerManager.playSoundCorrectAnswer()

                _uiState.update {
                    it.copy(
                        isCorrect = true,
                        score = it.score + 1,
                        feedbackTextRes = feedbackTitles.random()
                    )
                }
            } else {
                AudioPlayerManager.playSoundWrongAnswer()

                _uiState.update {
                    it.copy(isCorrect = false)
                }
            }
        }
    }

    // Next
    fun next() {
        val state = _uiState.value

        if (state.currentIndex < state.questions.size - 1) {

            _uiState.update {
                it.copy(currentIndex = it.currentIndex + 1)
            }

            prepareCurrentQuestion()

        } else {
            _uiState.update {
                it.copy(isCompleted = true)
            }
        }
    }

    // Helpers (same as iOS)
    fun formattedSentence(): String {
        val words = _uiState.value.arrangedWords
        if (words.isEmpty()) return ""

        val sentence = words.joinToString(" ")
        return sentence.replaceFirstChar { it.uppercase() } + "."
    }
}