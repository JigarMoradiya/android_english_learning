package com.example.myapplication.main.age_group.from_5_to_7.sight_words.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.SessionRepository
import com.example.myapplication.main.age_group.from_5_to_7.sight_words.data.SightWord
import com.example.myapplication.main.age_group.from_5_to_7.sight_words.data.sightWordsAgeGroup5_7
import com.example.myapplication.utilities.TextToSpeechManager
import com.example.myapplication.utilities.pref.AppPreferencesHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SightWordsViewModel @Inject constructor(
    private val ttsManager: TextToSpeechManager,
    private val sessionRepository: SessionRepository,
    private val prefs: AppPreferencesHelper
) : ViewModel() {

    private val words = sightWordsAgeGroup5_7
    private val savedIndex = prefs.getCustomParamInt(AppPreferencesHelper.KEY_SIGHT_WORD_INDEX, 0)
        .coerceIn(0, words.lastIndex)

    var uiState by mutableStateOf(SightWordsUiState(words = words, currentIndex = savedIndex))
        private set

    private val visitedWords = mutableSetOf<String>()
    private var startTimeMs = System.currentTimeMillis()

    init {
        visitedWords.add(currentWord.word)
        speak(currentWord.word)
    }

    val currentWord: SightWord
        get() = uiState.words[uiState.currentIndex]

    fun nextWord() {
        if (uiState.currentIndex < uiState.words.lastIndex) {
            val nextIndex = uiState.currentIndex + 1
            uiState = uiState.copy(currentIndex = nextIndex)
            prefs.setCustomParamInt(AppPreferencesHelper.KEY_SIGHT_WORD_INDEX, nextIndex)
            visitedWords.add(currentWord.word)
            speak(currentWord.word)
        }
    }

    fun previousWord() {
        if (uiState.currentIndex > 0) {
            val prevIndex = uiState.currentIndex - 1
            uiState = uiState.copy(currentIndex = prevIndex)
            prefs.setCustomParamInt(AppPreferencesHelper.KEY_SIGHT_WORD_INDEX, prevIndex)
            visitedWords.add(currentWord.word)
            speak(currentWord.word)
        }
    }

    fun speak(text: String) {
        ttsManager.speak(text)
    }

    override fun onCleared() {
        super.onCleared()
        if (visitedWords.isEmpty()) return
        val duration = ((System.currentTimeMillis() - startTimeMs) / 1000).toInt()
        sessionRepository.record(
            LearningSession(
                moduleId = ModuleID.SIGHT_WORDS,
                ageGroup = AgeGroup.FIVE_TO_SEVEN,
                durationSeconds = duration,
                score = visitedWords.size,
                totalQuestions = 0,
                wrongItems = emptyList(),
                correctItems = visitedWords.sorted()
            )
        )
    }
}
