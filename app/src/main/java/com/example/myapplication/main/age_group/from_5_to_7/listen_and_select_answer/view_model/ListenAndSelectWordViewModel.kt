package com.example.myapplication.main.age_group.from_5_to_7.listen_and_select_answer.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.generation.letter.LetterRepository
import com.example.myapplication.data.generation.letter.LetterRepository.vocabularyCategoryAllForListenAndSelect
import com.example.myapplication.utilities.TextToSpeechManager
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.FeedbackConstant.feedbackGiveAnswerSubTitleCorrect
import com.example.myapplication.utils.FeedbackConstant.feedbackGiveAnswerTitleCorrect
import com.example.myapplication.utils.FeedbackConstant.feedbackWrong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListenAndSelectWordViewModel @Inject constructor(
    private val ttsManager: TextToSpeechManager
) : ViewModel() {

    var uiState by mutableStateOf(ListenAndSelectWordUiState())
        private set

    init {
        loadWords()
    }

    private fun loadWords() {
        val allWords = (LetterRepository.all.flatMap {
            listOf(it.mainWord) + it.altWords
        } + vocabularyCategoryAllForListenAndSelect)

        uiState = uiState.copy(wordList = allWords.distinct())
        generateWord()
    }

    private fun generateWord() {
        val uniqueWord = uiState.wordList.shuffled().first()
        val otherWords = uiState.wordList
            .filter { it != uniqueWord }
            .shuffled()
            .take(3)
        val finalList = (listOf(uniqueWord) + otherWords).shuffled()
        uiState = uiState.copy(currentWord = uniqueWord, optionsWord = finalList)

        viewModelScope.launch {
            delay(500)
            speakWord()
        }
    }

    fun speakWord() {
        ttsManager.speak(uiState.currentWord)
    }

    fun playAgain() {
        uiState = uiState.copy(showPopup = false)
        generateWord()
    }

    fun closePopup() {
        uiState = uiState.copy(showPopup = false)
    }

    fun checkCorrectOrWrong(word: String) {
        val isCorrect = (word.equals(uiState.currentWord,true))
        if (isCorrect) {
            val randomTitle = feedbackGiveAnswerTitleCorrect.random()
            val randomSubTitle = feedbackGiveAnswerSubTitleCorrect.random()
            uiState = uiState.copy(feedbackTextRes = randomTitle, feedbackSubTextRes = randomSubTitle, showPopup = true, showError = false)
            AudioPlayerManager.playSoundCorrectAnswer()
        } else {
            val randomTitle = feedbackWrong.random()
            uiState = uiState.copy(feedbackTextRes = randomTitle, feedbackSubTextError = "Oops! The word is not $word", showError = true, showPopup = false)
            AudioPlayerManager.playSoundWrongAnswer()
        }

    }
}