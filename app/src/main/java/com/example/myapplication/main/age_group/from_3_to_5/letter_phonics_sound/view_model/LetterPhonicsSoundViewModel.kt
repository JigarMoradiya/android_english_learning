package com.example.myapplication.main.age_group.from_3_to_5.letter_phonics_sound.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.generation.letter.LetterRepository
import com.example.myapplication.utilities.AudioPhonicsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LetterPhonicsSoundViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {

    var uiState by mutableStateOf(LetterPhonicsSoundUiState())
        private set

    init {
        uiState = uiState.copy(
            letters = LetterRepository.all.map { data ->
                LetterPhonicsSoundItem(
                    letter = data.letter,
                    word = data.mainWord,
                    phonicsSoundText = data.phonicsSound,
                    highlightTimings = data.highlightTimings,
                )
            }
        )
    }

    fun onLetterTap(item: LetterPhonicsSoundItem) {
        uiState = uiState.copy(
            selectedLetter = item.letter,
            animateObjectImage = false,
            phonicsAnimationTrigger = uiState.phonicsAnimationTrigger + 1,
        )
        audioManager.playPhonicsSound("${item.letter.lowercase()}_${item.word.lowercase()}_sound")
        audioManager.onAudioCompleted = {
            uiState = uiState.copy(animateObjectImage = false)
        }
    }

    fun setAnimateObjectImage(animate: Boolean) {
        uiState = uiState.copy(animateObjectImage = animate)
    }

    fun stopAudio() {
        audioManager.stop()
    }

    override fun onCleared() {
        super.onCleared()
        audioManager.stop()
    }
}
