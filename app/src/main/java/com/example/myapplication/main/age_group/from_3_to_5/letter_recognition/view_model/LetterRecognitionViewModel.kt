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
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.utilities.TextToSpeechManager
import com.example.myapplication.utils.AudioPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LetterRecognitionViewModel @Inject constructor(
    private val ttsManager: TextToSpeechManager,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    var uiState by mutableStateOf(LetterRecognitionUiState())
        private set

    val lettersData: List<Pair<String, String>> =
        LetterRepository.all.map { data ->
            data.letter to data.mainWord
        }

    private val tappedLetters = mutableSetOf<String>()
    private var startTimeMs: Long? = null

    fun onLetterClick(letter: String, word: String) {
        if (startTimeMs == null) startTimeMs = System.currentTimeMillis()
        tappedLetters.add(letter)
        AudioPlayerManager.playSoundMenuClick()
        uiState = uiState.copy(selectedLetter = letter)
        ttsManager.speak("$letter, $word", RouteNavigation.LetterRecognition.route)
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