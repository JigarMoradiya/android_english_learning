package com.example.myapplication.main.age_group.from_5_to_7.articles_a_an.example.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.model.VocabularyCategoryType
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.SessionRepository
import com.example.myapplication.main.age_group.from_5_to_7.articles_a_an.info.data.articleLearningGoalA
import com.example.myapplication.main.age_group.from_5_to_7.articles_a_an.info.data.articleLearningGoalAn
import com.example.myapplication.ui.theme.ButtonColors
import com.example.myapplication.ui.theme.colorFromWord
import com.example.myapplication.utilities.TextToSpeechManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticlesAAnExampleViewModel @Inject constructor(
    private val ttsManager: TextToSpeechManager,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    var uiState by mutableStateOf(ArticlesAAnExampleUiState(examplesA = articleLearningGoalA, examplesAn = articleLearningGoalAn))
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

    fun changeMode(mode: ArticleMode) {
        uiState = uiState.copy(mode = mode)
    }

    fun backgroundForCategory(): ButtonColors {
        return uiState.cardColors
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
