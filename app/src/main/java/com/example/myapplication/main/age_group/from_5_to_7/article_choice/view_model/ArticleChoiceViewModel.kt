package com.example.myapplication.main.age_group.from_5_to_7.article_choice.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.generation.letter.LetterRepository
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.FeedbackConstant.feedbackTitles
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ArticleChoiceViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ArticleChoiceUiState())
    val uiState: StateFlow<ArticleChoiceUiState> = _uiState

    private val allWords: List<String> by lazy {
        LetterRepository.all.flatMap { element ->
            listOf(element.mainWord) + element.altWords
        }.shuffled()
    }

    private var currentIndex: Int = 0

    init {
        loadWord()
    }

    private fun loadWord() {
        if (allWords.isEmpty()) return

        val word = allWords[currentIndex]

        _uiState.update {
            it.copy(
                currentWord = word,
                currentImageName = word.lowercase(),
                selectedAnswer = null,
                feedbackTextCorrect = null,
                feedbackTextWrong = null,
                isAnswerCorrect = false
            )
        }
    }

    fun checkAnswer(choice: String) {
        if (_uiState.value.selectedAnswer != null) return
        val correct = articleFor()
        val isCorrect = choice == correct

        _uiState.update {
            it.copy(
                selectedAnswer = choice,
                isAnswerCorrect = isCorrect,
                countdown = 3,
                feedbackTextCorrect =  if (isCorrect) {
                    feedbackTitles.random()
                } else {
                    null
                },
                feedbackTextWrong =  if (isCorrect) {
                    null
                } else {
                    "Wrong! Correct answer is $correct"
                }
            )
        }

        if (isCorrect) {
            AudioPlayerManager.playSoundCorrectAnswer()
        } else {
            AudioPlayerManager.playSoundWrongAnswer()
        }
        startCountdown()
    }

    private fun startCountdown() {
        viewModelScope.launch {
            for (i in 2 downTo 1) {
                delay(1000)
                _uiState.update { it.copy(countdown = i) }
            }
            delay(1000)
            loadNextWord()
        }
    }

    fun loadNextWord() {
        if (allWords.isEmpty()) return

        currentIndex = (currentIndex + 1) % allWords.size

        loadWord()
    }

    fun articleFor(): String {
        return if (needsAn(_uiState.value.currentWord)) "an" else "a"
    }
    private fun needsAn(word: String): Boolean {
        val firstChar = word.lowercase().firstOrNull() ?: return false
        return "aeiou".contains(firstChar)
    }
}