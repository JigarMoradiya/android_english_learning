package com.example.myapplication.main.age_group.phonics.l3_blending.view_model

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
class BlendingViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {

    var uiState by mutableStateOf(BlendingUiState()); private set

    private var animJob: Job? = null

    fun onWordTap(word: BlendingWordModel) {
        animJob?.cancel()
        audioManager.stop()
        val isVC = uiState.isVC
        uiState = BlendingUiState(isVC = isVC, selectedWord = word)
        animJob = viewModelScope.launch { runSequence(word) }
    }

    fun onTabChange(isVC: Boolean) {
        animJob?.cancel()
        audioManager.stop()
        uiState = BlendingUiState(isVC = isVC)
    }

    fun replayAnimation() { uiState.selectedWord?.let { onWordTap(it) } }

    fun stop() {
        animJob?.cancel()
        audioManager.stop()
    }

    private suspend fun runSequence(word: BlendingWordModel) {
        delay(150)

        // Box 1 drops in, play seg 0
        uiState = uiState.copy(boxPhase = BlendBoxPhase.BOX1_IN, highlightedIndex = 0, phonicsPhase = BlendPhonicsPhase.INDIVIDUAL)
        suspendCancellableCoroutine { cont ->
            audioManager.playPhonicsSound("phonics_letter/${word.segments[0].soundFile}")
            audioManager.onAudioCompleted = { if (cont.isActive) cont.resume(Unit) }
            cont.invokeOnCancellation { audioManager.stop() }
        }

        delay(200)

        // Box 2 drops in, play seg 1
        uiState = uiState.copy(boxPhase = BlendBoxPhase.BOX2_IN, highlightedIndex = 1)
        suspendCancellableCoroutine { cont ->
            audioManager.playPhonicsSound("phonics_letter/${word.segments[1].soundFile}")
            audioManager.onAudioCompleted = { if (cont.isActive) cont.resume(Unit) }
            cont.invokeOnCancellation { audioManager.stop() }
        }

        delay(600)

        // Both glow (blending)
        uiState = uiState.copy(highlightedIndex = -1, phonicsPhase = BlendPhonicsPhase.BLENDING)
        delay(500)

        // Slide to merge
        uiState = uiState.copy(boxPhase = BlendBoxPhase.MERGING)
        delay(650)

        // Merged tile springs in — play word audio immediately as tile appears
        uiState = uiState.copy(boxPhase = BlendBoxPhase.MERGED, phonicsPhase = BlendPhonicsPhase.COMPLETE)

        // Play word audio
        suspendCancellableCoroutine { cont ->
            audioManager.playPhonicsSound("phonics_word/${word.audioFileName}")
            audioManager.onAudioCompleted = { if (cont.isActive) cont.resume(Unit) }
            cont.invokeOnCancellation { audioManager.stop() }
        }
    }
}
