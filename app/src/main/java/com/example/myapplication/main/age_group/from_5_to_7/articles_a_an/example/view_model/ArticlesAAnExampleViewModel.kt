package com.example.myapplication.main.age_group.from_5_to_7.articles_a_an.example.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.model.VocabularyCategoryType
import com.example.myapplication.main.age_group.from_5_to_7.articles_a_an.info.data.articleLearningGoalA
import com.example.myapplication.main.age_group.from_5_to_7.articles_a_an.info.data.articleLearningGoalAn
import com.example.myapplication.ui.theme.ButtonColors
import com.example.myapplication.ui.theme.colorFromWord
import com.example.myapplication.utilities.TextToSpeechManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticlesAAnExampleViewModel @Inject constructor(
    private val ttsManager: TextToSpeechManager
) : ViewModel() {

    var uiState by mutableStateOf(ArticlesAAnExampleUiState(examplesA = articleLearningGoalA, examplesAn = articleLearningGoalAn))
        private set

    fun speak(txt: String) {
        ttsManager.speak(txt)
    }

    fun changeMode(mode: ArticleMode) {
        uiState = uiState.copy(mode = mode)
    }

    fun backgroundForCategory(): ButtonColors {
        return uiState.cardColors
    }
}