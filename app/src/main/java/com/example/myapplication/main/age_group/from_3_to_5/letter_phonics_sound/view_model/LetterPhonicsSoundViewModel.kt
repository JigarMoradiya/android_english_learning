package com.example.myapplication.main.age_group.from_3_to_5.letter_phonics_sound.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.access.AccessManager
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.generation.letter.LetterRepository
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.SessionRepository
import com.example.myapplication.utilities.AudioPhonicsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LetterPhonicsSoundViewModel @Inject constructor(
    private val accessManager: AccessManager,
    private val audioManager: AudioPhonicsManager,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    var uiState by mutableStateOf(LetterPhonicsSoundUiState())
        private set

    private val tappedLetters = mutableSetOf<String>()
    private var startTimeMs: Long? = null

    init {
        uiState = uiState.copy(
            isDevMode = accessManager.isDevMode,
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
        if (startTimeMs == null) startTimeMs = System.currentTimeMillis()
        tappedLetters.add(item.letter)
        uiState = uiState.copy(
            selectedLetter = item.letter,
            animateObjectImage = false,
            phonicsAnimationTrigger = uiState.phonicsAnimationTrigger + 1,
        )
        audioManager.playPhonicsSound("phonics_abcd/${item.letter.lowercase()}_${item.word.lowercase()}_sound")
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

    private fun recordSession() {
        val start = startTimeMs ?: return
        if (tappedLetters.isEmpty()) return
        val duration = ((System.currentTimeMillis() - start) / 1000).toInt()
        sessionRepository.record(
            LearningSession(
                moduleId = ModuleID.LETTER_PHONICS,
                ageGroup = AgeGroup.THREE_TO_FIVE,
                durationSeconds = duration,
                score = tappedLetters.size,
                totalQuestions = 0,
                wrongItems = emptyList(),
                correctItems = tappedLetters.sorted(),
                subConfig = null
            )
        )
        tappedLetters.clear()
        startTimeMs = null
    }

    override fun onCleared() {
        super.onCleared()
        recordSession()
        audioManager.stop()
    }
}
