package com.example.myapplication.main.age_group.from_5_to_7.article_choice.view_model

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.generation.letter.LetterRepository
import com.example.myapplication.utils.AudioPlayerManager
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
                feedbackText = null,
                isAnswerCorrect = false
            )
        }
    }

    fun checkAnswer(choice: String) {
        val correct = if (needsAn(_uiState.value.currentWord)) "an" else "a"
        val isCorrect = choice == correct

        _uiState.update {
            it.copy(
                selectedAnswer = choice,
                isAnswerCorrect = isCorrect,
                feedbackText = if (isCorrect) {
                    "Correct!"
                } else {
                    "Wrong! Correct answer is '$correct'"
                }
            )
        }

        if (isCorrect) {
            AudioPlayerManager.playSoundCorrectAnswer()
        } else {
            AudioPlayerManager.playSoundWrongAnswer()
        }
    }

    fun loadNextWord() {
        if (allWords.isEmpty()) return

        currentIndex = (currentIndex + 1) % allWords.size

        loadWord()
    }

    private fun needsAn(word: String): Boolean {
        val firstChar = word.lowercase().firstOrNull() ?: return false
        return "aeiou".contains(firstChar)
    }
}