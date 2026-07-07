package com.example.myapplication.main.age_group.from_3_to_5.letter_recognition.mirror_letters.view_model

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.SessionRepository
import com.example.myapplication.main.age_group.from_3_to_5.letter_recognition.mirror_letters.MirrorLetterPair
import com.example.myapplication.utilities.AudioPhonicsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// Just audio + session recording — the letter/index state itself is kept local
// to the Composable since the tracing canvases already take letter/mode
// directly (no need to route it through a ViewModel).
@HiltViewModel
class MirrorLettersViewModel @Inject constructor(
    private val audioPhonicsManager: AudioPhonicsManager,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val completedLettersInSession = mutableSetOf<String>()
    private var startTimeMs: Long = System.currentTimeMillis()

    fun playPhonicsSound(letter: Char) {
        audioPhonicsManager.playPhonicsSound("phonics_letter/sound_${letter.lowercaseChar()}")
    }

    fun markLetterCompleted(letter: Char) {
        completedLettersInSession.add(letter.toString())
    }

    fun recordSession(pair: MirrorLetterPair) {
        if (completedLettersInSession.isEmpty()) return
        val duration = ((System.currentTimeMillis() - startTimeMs) / 1000).toInt()
        sessionRepository.record(
            LearningSession(
                moduleId = ModuleID.LETTER_CONFUSION,
                ageGroup = AgeGroup.THREE_TO_FIVE,
                durationSeconds = duration,
                score = completedLettersInSession.size,
                totalQuestions = 0,
                correctItems = completedLettersInSession.sorted(),
                subConfig = pair.subConfigName
            )
        )
        completedLettersInSession.clear()
        startTimeMs = System.currentTimeMillis()
    }
}
