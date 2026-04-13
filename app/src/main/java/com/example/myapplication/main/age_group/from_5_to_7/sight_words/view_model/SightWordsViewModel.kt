package com.example.myapplication.main.age_group.from_5_to_7.sight_words.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.main.age_group.from_5_to_7.sight_words.data.SightWord
import com.example.myapplication.main.age_group.from_5_to_7.sight_words.data.sightWordsAgeGroup5_7
import com.example.myapplication.utilities.TextToSpeechManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SightWordsViewModel @Inject constructor(
    private val ttsManager: TextToSpeechManager
) : ViewModel() {

    var uiState by mutableStateOf(SightWordsUiState(words = sightWordsAgeGroup5_7,currentIndex = 0))
        private set

    init {
        speak(currentWord.word)
    }

    val currentWord: SightWord
        get() = uiState.words[uiState.currentIndex]

    fun nextWord() {
        if (uiState.currentIndex < uiState.words.lastIndex) {
            uiState = uiState.copy(currentIndex = uiState.currentIndex + 1)
            speak(currentWord.word)
        }
    }

    fun previousWord() {
        if (uiState.currentIndex > 0) {
            uiState = uiState.copy(currentIndex = uiState.currentIndex - 1)
            speak(currentWord.word)
        }
    }

    fun speak(text: String) {
        ttsManager.speak(text)
    }
}