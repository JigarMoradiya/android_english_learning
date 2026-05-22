package com.example.myapplication.main.age_group.from_5_to_7.sight_word_choice.view_model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.main.age_group.from_5_to_7.sight_word_choice.data.sightWordsAgeGroup_5_7Example
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.FeedbackConstant.feedbackTitles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SightWordChoiceViewModel @Inject constructor() : ViewModel() {

    var uiState by mutableStateOf(SightWordChoiceUiState())
        private set

    private var allWords = sightWordsAgeGroup_5_7Example.shuffled()

    private var currentIndex = 0

    init {
        if (uiState.currentExample.isEmpty()) {
            loadWord()
        }
    }

    fun loadWord() {
        if (allWords.isEmpty()) return

        val currentWord = allWords[currentIndex]

        val example = currentWord.examples.random()
        val (prefix, suffix) = splitSentence(example, currentWord.word)

        val wrongs = allWords
            .filter { !it.word.equals(currentWord.word, true) }
            .shuffled()
            .take(2)
            .map { it.word }

        val options = (listOf(currentWord.word) + wrongs).shuffled()

        uiState = uiState.copy(
            currentWord = currentWord,
            currentExample = example,
            sentencePrefix = prefix,
            sentenceSuffix = suffix,
            options = options,
            selectedAnswer = null,
            isAnswerCorrect = false,
            feedbackTextCorrect = null,
            feedbackTextWrong = null
        )
    }

    fun checkAnswer(choice: String) {
        if (uiState.selectedAnswer != null) return
        val isCorrect = choice.equals(uiState.currentWord.word, true)

        uiState = uiState.copy(
            selectedAnswer = choice,
            isAnswerCorrect = isCorrect,
            countdown = 3,
            feedbackTextCorrect =  if (isCorrect) {
                feedbackTitles.random()
            } else {
                null
            },
            feedbackTextWrong = if (isCorrect) {
                null
            } else {
                if (uiState.sentencePrefix.isEmpty()) {
                    "Wrong! Correct answer : ${uiState.currentWord.word.replaceFirstChar { it.uppercase() }}"
                } else {
                    "Wrong! Correct answer : ${uiState.currentWord.word.lowercase()}"
                }
            }
        )

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
                uiState = uiState.copy(countdown = i)
            }
            delay(1000)
            loadNextWord()
        }
    }

    fun loadNextWord() {
        currentIndex = (currentIndex + 1) % allWords.size
        loadWord()
    }

    // ✅ same as Swift regex logic
    private fun splitSentence(text: String, word: String): Pair<String, String> {
        val regex = "\\b${Regex.escape(word)}\\b".toRegex(RegexOption.IGNORE_CASE)
        val match = regex.find(text)

        return if (match != null) {
            val prefix = text.substring(0, match.range.first).trim()
            val suffix = text.substring(match.range.last + 1).trim()
            prefix to suffix
        } else {
            text to ""
        }
    }
}