package com.example.myapplication.main.age_group.phonics.l4_cvc_words.view_model

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
class CvcWordsViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {

    var uiState by mutableStateOf(CvcUiState()); private set

    private var animJob: Job? = null

    fun onGroupTap(group: CvcGroup) {
        if (uiState.selectedGroup?.vowel == group.vowel) return
        animJob?.cancel()
        audioManager.stop()
        uiState = CvcUiState(selectedGroup = group)
    }

    fun onWordTap(word: CvcWordModel) {
        animJob?.cancel()
        audioManager.stop()
        val group = uiState.selectedGroup
        uiState = CvcUiState(selectedGroup = group, selectedWord = word)
        animJob = viewModelScope.launch { runSequence(word) }
    }

    fun stop() {
        animJob?.cancel()
        audioManager.stop()
    }

    private suspend fun runSequence(word: CvcWordModel) {
        // 3 boxes drop in sequentially
        delay(100)
        uiState = uiState.copy(boxPhase = CvcBoxPhase.BOX1_IN)
        delay(200)
        uiState = uiState.copy(boxPhase = CvcBoxPhase.BOX2_IN)
        delay(200)
        uiState = uiState.copy(boxPhase = CvcBoxPhase.BOX3_IN)
        delay(250)
        uiState = uiState.copy(boxPhase = CvcBoxPhase.ALL_SHOWN)
        delay(250)
        uiState = uiState.copy(phonicsPhase = CvcPhonicsPhase.INDIVIDUAL)

        // Play each segment sound
        for (i in word.segments.indices) {
            uiState = uiState.copy(highlightedIndex = i)
            suspendCancellableCoroutine { cont ->
                audioManager.playPhonicsSound("phonics_letter/sound_${word.segments[i].letter}")
                audioManager.onAudioCompleted = { if (cont.isActive) cont.resume(Unit) }
                cont.invokeOnCancellation { audioManager.stop() }
            }
            delay(200)
        }

        // Merge sequence
        uiState = uiState.copy(highlightedIndex = -1, phonicsPhase = CvcPhonicsPhase.BLENDING)
        delay(500)
        uiState = uiState.copy(boxPhase = CvcBoxPhase.MERGING)
        delay(650)
        uiState = uiState.copy(boxPhase = CvcBoxPhase.MERGED, phonicsPhase = CvcPhonicsPhase.COMPLETE)
        delay(700)

        // Play word audio
        suspendCancellableCoroutine { cont ->
            audioManager.playPhonicsSound("phonics_word/${word.word}")
            audioManager.onAudioCompleted = { if (cont.isActive) cont.resume(Unit) }
            cont.invokeOnCancellation { audioManager.stop() }
        }

        // Wiggle image
        uiState = uiState.copy(animateImage = true)
    }
}
