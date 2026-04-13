package com.example.myapplication.main.age_group.from_3_to_5.abcd_with_images.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.generation.letter.LetterData
import com.example.myapplication.data.generation.letter.LetterRepository
import com.example.myapplication.ui.theme.randomButtonType
import com.example.myapplication.utilities.TextToSpeechManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ABCDWithImagesViewModel @Inject constructor(
    private val ttsManager: TextToSpeechManager
) : ViewModel() {

    private val letters = LetterRepository.all

    var uiState by mutableStateOf(
        ABCDWithImagesUiState(
            currentWord = letters.first().mainWord,
            currentMatches = letters.first().altWords,
            gradientType = randomButtonType
        )
    )
        private set

    // -----------------------------
    // CURRENT DATA
    // -----------------------------
    val currentLetterData: LetterData
        get() = letters[uiState.currentIndex]


    // NEXT / PREVIOUS
    fun next() {
        val nextIndex = (uiState.currentIndex + 1) % letters.size
        val item = letters[nextIndex]

        uiState = uiState.copy(
            currentIndex = nextIndex,
            isNext = true,
            currentWord = item.mainWord,
            currentMatches = item.altWords,
            gradientType = randomButtonType
        )

        speakCurrent()
    }

    fun previous() {
        val prevIndex = (uiState.currentIndex - 1 + letters.size) % letters.size
        val item = letters[prevIndex]

        uiState = uiState.copy(
            currentIndex = prevIndex,
            isNext = false,
            currentWord = item.mainWord,
            currentMatches = item.altWords,
            gradientType = randomButtonType
        )

        speakCurrent()
    }

    // SPEAK
    fun speakCurrent() {
        val item = currentLetterData
        ttsManager.speak("${item.letter}, for ${uiState.currentWord}")
    }

    // SWAP LOGIC
    fun swapWithMain(match: String) {

        val oldMain = uiState.currentWord

        val newMatches = uiState.currentMatches.toMutableList()
        val index = newMatches.indexOf(match)

        if (index != -1) {
            newMatches[index] = oldMain

            uiState = uiState.copy(
                currentWord = match,
                currentMatches = newMatches
            )

            speakCurrent()
        }
    }
}