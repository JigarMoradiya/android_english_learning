package com.example.myapplication.main.age_group.phonics.l2_short_vowels.view_model

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
class ShortVowelsViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {

    var uiState by mutableStateOf(ShortVowelsUiState())
        private set

    private var animJob: Job? = null

    fun onVowelTap(vowel: ShortVowelModel) {
        animJob?.cancel()
        audioManager.stop()
        uiState = ShortVowelsUiState(selectedVowel = vowel)
        animJob = viewModelScope.launch { startSequence(vowel) }
    }

    fun playAnchorWord(vowel: ShortVowelModel) {
        audioManager.playPhonicsSound(vowel.anchorAudioFile)
    }

    fun playExampleWord(word: String) {
        audioManager.playPhonicsSound("phonics_word/$word")
    }

    fun stop() {
        animJob?.cancel()
        audioManager.stop()
    }

    private suspend fun startSequence(vowel: ShortVowelModel) {
        delay(150)
        uiState = uiState.copy(phase = ShortVowelPhase.VOWEL_IN)
        delay(350)
        suspendCancellableCoroutine { cont ->
            audioManager.playPhonicsSound(vowel.soundFile)
            audioManager.onAudioCompleted = { if (cont.isActive) cont.resume(Unit) }
            cont.invokeOnCancellation { audioManager.stop() }
        }
        showAnchor(vowel)
    }

    private suspend fun showAnchor(vowel: ShortVowelModel) {
        delay(200)
        uiState = uiState.copy(phase = ShortVowelPhase.ANCHOR_IN, animateAnchor = true)
        suspendCancellableCoroutine { cont ->
            audioManager.playPhonicsSound(vowel.anchorAudioFile)
            audioManager.onAudioCompleted = { if (cont.isActive) cont.resume(Unit) }
            cont.invokeOnCancellation { audioManager.stop() }
        }
        showExamples(vowel.examples.size)
    }

    private suspend fun showExamples(count: Int) {
        for (i in 0 until count) {
            delay(if (i == 0) 200L else 180L)
            uiState = uiState.copy(
                visibleExamples = i + 1,
                phase = if (i == count - 1) ShortVowelPhase.EXAMPLES_IN else uiState.phase
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        stop()
    }
}
