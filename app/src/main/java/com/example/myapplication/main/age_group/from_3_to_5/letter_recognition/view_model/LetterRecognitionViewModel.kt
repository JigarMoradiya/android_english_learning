package com.example.myapplication.main.age_group.from_3_to_5.letter_recognition.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.generation.letter.LetterRepository
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.SessionRepository
import com.example.myapplication.utilities.AudioPhonicsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// Drives which word is highlighted on the right panel: the letter, "says", or the sound (e.g. "Aah")
enum class LetterRecognitionSegment { NONE, LETTER, SAYS, SOUND }

@HiltViewModel
class LetterRecognitionViewModel @Inject constructor(
    private val audioManager: AudioPhonicsManager,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    var uiState by mutableStateOf(LetterRecognitionUiState())
        private set

    // (letter, word, sound) — sound is the first word of phonicsSound, e.g. "Aah"
    val lettersData: List<Triple<String, String, String>> =
        LetterRepository.all.map { data ->
            Triple(data.letter, data.mainWord, data.phonicsSound.substringBefore(" "))
        }

    private val tappedLetters = mutableSetOf<String>()
    private var startTimeMs: Long? = null

    // Guards the chained playback so a stale completion callback from a previously-tapped
    // letter can't fire after the child has moved on to a new letter.
    private var speakToken = 0L

    fun onLetterClick(letter: String, word: String) {
        if (startTimeMs == null) startTimeMs = System.currentTimeMillis()
        tappedLetters.add(letter)

        speakToken += 1
        val token = speakToken

        uiState = uiState.copy(selectedLetter = letter, spokenSegment = LetterRecognitionSegment.NONE)

        playStep(0, letterKey = letter.lowercase(), token = token)
    }

    // "A" → "says" → "Aah": the bare letter, then the shared "says" clip, then the
    // letter's pure phonics sound. Each step flips which word is highlighted.
    private fun playStep(step: Int, letterKey: String, token: Long) {
        if (speakToken != token) return

        val (fileName, segment) = when (step) {
            0 -> "phonics_letter/letter_$letterKey" to LetterRecognitionSegment.LETTER
            1 -> "phonics_letter/says" to LetterRecognitionSegment.SAYS
            2 -> "phonics_letter/sound_$letterKey" to LetterRecognitionSegment.SOUND
            else -> {
                // Sequence finished — reset so nothing stays highlighted.
                uiState = uiState.copy(spokenSegment = LetterRecognitionSegment.NONE)
                return
            }
        }

        // MediaPlayer.prepare() blocks synchronously and can take noticeably longer than
        // iOS's AVAudioPlayer — play first, THEN flip the highlight, so the highlight lands
        // when the audio is actually audible instead of a beat before it.
        audioManager.playPhonicsSound(fileName)
        uiState = uiState.copy(spokenSegment = segment)

        audioManager.onAudioCompleted = {
            playStep(step + 1, letterKey, token)
        }
    }

    override fun onCleared() {
        super.onCleared()
        val start = startTimeMs ?: return
        if (tappedLetters.isEmpty()) return
        val duration = ((System.currentTimeMillis() - start) / 1000).toInt()
        sessionRepository.record(LearningSession(
            moduleId = ModuleID.LETTER_RECOGNITION,
            ageGroup = AgeGroup.THREE_TO_FIVE,
            durationSeconds = duration,
            score = tappedLetters.size,
            totalQuestions = 0,
            correctItems = tappedLetters.sorted()
        ))
    }
}