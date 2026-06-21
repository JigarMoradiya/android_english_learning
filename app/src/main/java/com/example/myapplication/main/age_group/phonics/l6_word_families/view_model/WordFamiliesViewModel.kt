package com.example.myapplication.main.age_group.phonics.l6_word_families.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.utilities.AudioPhonicsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

@HiltViewModel
class WordFamiliesViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {

    var uiState by mutableStateOf(WordFamiliesUiState()); private set

    private var animJob: Job? = null

    fun onFamilyTap(family: WordFamily) {
        animJob?.cancel()
        audioManager.stop()
        uiState = WordFamiliesUiState(selectedFamily = family)
        animJob = viewModelScope.launch {
            delay(200)
            uiState = uiState.copy(highlightHeaderRime = true)
            suspendCancellableCoroutine { cont ->
                audioManager.playPhonicsSound("phonics_word/${family.rimeAudio}")
                audioManager.onAudioCompleted = { if (cont.isActive) cont.resume(Unit) }
                cont.invokeOnCancellation { audioManager.stop() }
            }
            uiState = uiState.copy(highlightHeaderRime = false)
            delay(250)
            uiState = uiState.copy(showOnsets = true)
        }
    }

    fun onOnsetTap(onset: FamilyOnset, family: WordFamily) {
        animJob?.cancel()
        audioManager.stop()
        uiState = uiState.copy(selectedOnset = onset, showEquation = true, highlight = WordFamilyHighlight.NONE)
        animJob = viewModelScope.launch {
            delay(200)

            // Highlight onset, play onset sound
            uiState = uiState.copy(highlight = WordFamilyHighlight.ONSET)
            suspendCancellableCoroutine { cont ->
                audioManager.playPhonicsSound("phonics_letter/sound_${onset.onset}")
                audioManager.onAudioCompleted = { if (cont.isActive) cont.resume(Unit) }
                cont.invokeOnCancellation { audioManager.stop() }
            }

            delay(150)

            // Highlight rime, play rime sound
            uiState = uiState.copy(highlight = WordFamilyHighlight.RIME)
            suspendCancellableCoroutine { cont ->
                audioManager.playPhonicsSound("phonics_word/${family.rimeAudio}")
                audioManager.onAudioCompleted = { if (cont.isActive) cont.resume(Unit) }
                cont.invokeOnCancellation { audioManager.stop() }
            }

            delay(200)

            // Highlight word, play word
            uiState = uiState.copy(highlight = WordFamilyHighlight.WORD)
            suspendCancellableCoroutine { cont ->
                audioManager.playPhonicsSound("phonics_word/${onset.word}")
                audioManager.onAudioCompleted = { if (cont.isActive) cont.resume(Unit) }
                cont.invokeOnCancellation { audioManager.stop() }
            }

            uiState = uiState.copy(highlight = WordFamilyHighlight.NONE)
        }
    }

    fun replayWord() {
        val onset = uiState.selectedOnset ?: return
        animJob?.cancel()
        audioManager.stop()
        uiState = uiState.copy(highlight = WordFamilyHighlight.WORD)
        animJob = viewModelScope.launch {
            suspendCancellableCoroutine { cont ->
                audioManager.playPhonicsSound("phonics_word/${onset.word}")
                audioManager.onAudioCompleted = { if (cont.isActive) cont.resume(Unit) }
                cont.invokeOnCancellation { audioManager.stop() }
            }
            uiState = uiState.copy(highlight = WordFamilyHighlight.NONE)
        }
    }

    fun stop() {
        animJob?.cancel()
        audioManager.stop()
    }
}
