package com.example.myapplication.main.age_group.from_3_to_5.letter_recognition.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.generation.letter.LetterRepository
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.utilities.TextToSpeechManager
import com.example.myapplication.utils.AudioPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LetterRecognitionViewModel @Inject constructor(
    private val ttsManager: TextToSpeechManager,
) : ViewModel() {

    var uiState by mutableStateOf(LetterRecognitionUiState())
        private set

    val lettersData: List<Pair<String, String>> =
        LetterRepository.all.map { data ->
            data.letter to data.mainWord
        }

    fun onLetterClick(letter: String, word: String) {
        AudioPlayerManager.playSoundMenuClick()
        uiState = uiState.copy(selectedLetter = letter)
        ttsManager.speak("$letter, $word", RouteNavigation.LetterRecognition.route)
    }

}