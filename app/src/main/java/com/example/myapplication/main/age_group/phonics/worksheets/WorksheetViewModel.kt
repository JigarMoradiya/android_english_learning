package com.example.myapplication.main.age_group.phonics.worksheets

import androidx.lifecycle.ViewModel
import com.example.myapplication.utilities.AudioPhonicsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/** Plays a worksheet word's audio when tapped — same audio banks the phonics
 *  screens use, so paper and screen never sound different. */
@HiltViewModel
class WorksheetViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager
) : ViewModel() {

    fun playWord(word: String, letter: String) {
        val wordPath = "phonics_word/${word.lowercase()}"
        if (audioManager.audioExists(wordPath)) {
            audioManager.playPhonicsSound(wordPath)
            return
        }
        // Fall back to the letter's own example-word recording.
        WorksheetLetters.data(letter)?.let { d ->
            val abcd = "phonics_abcd/${letter.lowercase()}_${d.mainWord.lowercase()}_sound"
            if (audioManager.audioExists(abcd)) {
                audioManager.playPhonicsSound(abcd)
                return
            }
        }
        // Last resort: the isolated letter sound.
        val isolated = "sound_${letter.lowercase()}"
        if (audioManager.audioExists(isolated)) {
            audioManager.playPhonicsSound(isolated)
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioManager.stop()
    }
}
