package com.example.myapplication.main.age_group.from_5_to_7.articles_a_an.info.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.main.age_group.from_5_to_7.articles_a_an.info.data.articleLearningGoalExampleAAn
import com.example.myapplication.utilities.TextToSpeechManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticlesAAnViewModel @Inject constructor(
    private val ttsManager: TextToSpeechManager
) : ViewModel() {

    var uiState by mutableStateOf(ArticlesAAnUiState(examples = articleLearningGoalExampleAAn))
        private set

    fun speak(txt: String) {
        ttsManager.speak(txt)
    }
}