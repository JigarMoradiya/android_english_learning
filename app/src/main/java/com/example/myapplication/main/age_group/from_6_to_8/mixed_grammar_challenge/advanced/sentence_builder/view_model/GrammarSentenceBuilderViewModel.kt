package com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.advanced.sentence_builder.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.generation.loader.MixedGrammarData
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.FeedbackConstant.feedbackTitles
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GrammarSentenceBuilderViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(GrammarSentenceBuilderUiState())
    val uiState: StateFlow<GrammarSentenceBuilderUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        val questions = MixedGrammarData.sentenceBuilderItems.shuffled()
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

    private fun prepareCurrentQuestion() {
        val q = _uiState.value.currentQuestion ?: return
        _uiState.update {
            it.copy(
                shuffledWords = q.words.shuffled(),
                arrangedWords = emptyList(),
                isCorrect = null
            )
        }
    }

    fun addWord(word: String) {
        val state = _uiState.value
        val shuffled = state.shuffledWords.toMutableList()
        val arranged = state.arrangedWords.toMutableList()
        val index = shuffled.indexOf(word)
        if (index == -1) return
        arranged.add(word)
        shuffled.removeAt(index)
        AudioPlayerManager.playSoundMenuClick()
        _uiState.update { it.copy(arrangedWords = arranged, shuffledWords = shuffled) }
        checkIfComplete()
    }

    fun removeWord(word: String) {
        val state = _uiState.value
        val shuffled = state.shuffledWords.toMutableList()
        val arranged = state.arrangedWords.toMutableList()
        val index = arranged.indexOf(word)
        if (index == -1) return
        shuffled.add(word)
        arranged.removeAt(index)
        AudioPlayerManager.playSoundMenuClick()
        _uiState.update { it.copy(arrangedWords = arranged, shuffledWords = shuffled, isCorrect = null) }
    }

    private fun checkIfComplete() {
        val state = _uiState.value
        val correct = state.currentQuestion?.correctSentence
            ?.replace(".", "")?.replace("?", "")?.replace("!", "")
            ?.lowercase() ?: return
        val correctWords = correct.split(" ")
        if (state.arrangedWords.size != correctWords.size) return
        val userSentence = state.arrangedWords.joinToString(" ") { it.lowercase() }
        if (userSentence == correct) {
            AudioPlayerManager.playSoundCorrectAnswer()
            _uiState.update {
                it.copy(isCorrect = true, score = it.score + 1, feedbackTextRes = feedbackTitles.random())
            }
        } else {
            AudioPlayerManager.playSoundWrongAnswer()
            _uiState.update { it.copy(isCorrect = false) }
        }
    }

    fun next() {
        val nextIndex = _uiState.value.currentIndex + 1
        if (nextIndex >= _uiState.value.questions.size) {
            _uiState.update { it.copy(isCompleted = true) }
            return
        }
        _uiState.update { it.copy(currentIndex = nextIndex) }
        prepareCurrentQuestion()
    }

    val isLastQuestion: Boolean get() = _uiState.value.currentIndex >= _uiState.value.questions.lastIndex

    fun formattedSentence(): String {
        val words = _uiState.value.arrangedWords
        if (words.isEmpty()) return ""
        return words.joinToString(" ").replaceFirstChar { it.uppercase() } + "."
    }
}
