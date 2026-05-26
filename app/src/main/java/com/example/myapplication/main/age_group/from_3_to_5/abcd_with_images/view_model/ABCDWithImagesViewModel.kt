package com.example.myapplication.main.age_group.from_3_to_5.abcd_with_images.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.generation.letter.LetterData
import com.example.myapplication.data.generation.letter.LetterRepository
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.SessionRepository
import com.example.myapplication.ui.theme.randomButtonType
import com.example.myapplication.utilities.TextToSpeechManager
import com.example.myapplication.utilities.pref.AppPreferencesHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ABCDWithImagesViewModel @Inject constructor(
    private val ttsManager: TextToSpeechManager,
    private val sessionRepository: SessionRepository,
    private val prefs: AppPreferencesHelper
) : ViewModel() {

    private val letters = LetterRepository.all
    private val visitedLetters = mutableSetOf<String>()
    private var startTimeMs: Long = System.currentTimeMillis()

    var uiState by mutableStateOf(
        ABCDWithImagesUiState(
            currentWord = letters.first().mainWord,
            currentMatches = letters.first().altWords,
            gradientType = randomButtonType
        )
    )
        private set

    val currentLetterData: LetterData
        get() = letters[uiState.currentIndex]

    init {
        val savedIndex = prefs.getCustomParamInt("abcd_images_index", 0).coerceIn(0, letters.lastIndex)
        if (savedIndex > 0) {
            val item = letters[savedIndex]
            uiState = uiState.copy(
                currentIndex = savedIndex,
                currentWord = item.mainWord,
                currentMatches = item.altWords,
                gradientType = randomButtonType
            )
        }
        speakCurrent()
    }

    // NEXT / PREVIOUS
    fun next() {
        visitedLetters.add(currentLetterData.letter)
        val nextIndex = (uiState.currentIndex + 1) % letters.size
        val item = letters[nextIndex]
        uiState = uiState.copy(
            currentIndex = nextIndex,
            isNext = true,
            currentWord = item.mainWord,
            currentMatches = item.altWords,
            gradientType = randomButtonType
        )
        prefs.setCustomParamInt("abcd_images_index", nextIndex)
        speakCurrent()
    }

    fun previous() {
        visitedLetters.add(currentLetterData.letter)
        val prevIndex = (uiState.currentIndex - 1 + letters.size) % letters.size
        val item = letters[prevIndex]
        uiState = uiState.copy(
            currentIndex = prevIndex,
            isNext = false,
            currentWord = item.mainWord,
            currentMatches = item.altWords,
            gradientType = randomButtonType
        )
        prefs.setCustomParamInt("abcd_images_index", prevIndex)
        speakCurrent()
    }

    override fun onCleared() {
        super.onCleared()
        if (visitedLetters.isEmpty()) return
        val duration = ((System.currentTimeMillis() - startTimeMs) / 1000).toInt()
        sessionRepository.record(LearningSession(
            moduleId = ModuleID.ABCD_WITH_IMAGES,
            ageGroup = AgeGroup.THREE_TO_FIVE,
            durationSeconds = duration,
            score = visitedLetters.size,
            totalQuestions = 0,
            correctItems = visitedLetters.sorted()
        ))
    }

    // SPEAK
    fun speakCurrent() {
        val item = currentLetterData
        ttsManager.speak("${item.letter}, for ${uiState.currentWord}")
    }

    // SWAP LOGIC
    fun swapWithMain(match: String) {

        val oldMain = uiState.currentWord

        val newMatches = uiState.currentMatches.toMutableList()
        val index = newMatches.indexOf(match)

        if (index != -1) {
            newMatches[index] = oldMain

            uiState = uiState.copy(
                currentWord = match,
                currentMatches = newMatches
            )

            speakCurrent()
        }
    }
}