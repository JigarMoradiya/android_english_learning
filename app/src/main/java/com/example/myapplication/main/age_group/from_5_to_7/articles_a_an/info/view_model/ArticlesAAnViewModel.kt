package com.example.myapplication.main.age_group.from_5_to_7.articles_a_an.info.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.SessionRepository
import com.example.myapplication.main.age_group.from_5_to_7.articles_a_an.info.data.articleLearningGoalExampleAAn
import com.example.myapplication.utilities.TextToSpeechManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticlesAAnViewModel @Inject constructor(
    private val ttsManager: TextToSpeechManager,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    var uiState by mutableStateOf(ArticlesAAnUiState(examples = articleLearningGoalExampleAAn))
        private set

    private val tappedWords = mutableSetOf<String>()
    private var startTimeMs = 0L

    fun speak(txt: String) {
        ttsManager.speak(txt)
    }

    fun speakAndTrack(txt: String, word: String) {
        if (startTimeMs == 0L) startTimeMs = System.currentTimeMillis()
        ttsManager.speak(txt)
        tappedWords.add(word)
    }

    override fun onCleared() {
        super.onCleared()
        if (tappedWords.isEmpty()) return
        val duration = ((System.currentTimeMillis() - startTimeMs) / 1000).toInt()
        sessionRepository.record(
            LearningSession(
                moduleId = ModuleID.ARTICLES_A_AN,
                ageGroup = AgeGroup.FIVE_TO_SEVEN,
                durationSeconds = duration,
                score = tappedWords.size,
                totalQuestions = 0,
                wrongItems = emptyList(),
                correctItems = tappedWords.sorted()
            )
        )
    }
}
