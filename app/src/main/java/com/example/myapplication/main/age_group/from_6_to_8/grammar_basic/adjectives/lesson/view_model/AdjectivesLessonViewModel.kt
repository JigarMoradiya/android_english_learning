package com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.adjectives.lesson.view_model

import com.example.myapplication.data.model.GrammarExampleModel

import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.utilities.TextToSpeechManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AdjectivesLessonViewModel @Inject constructor(
    private val ttsManager: TextToSpeechManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdjectivesLessonUiState())
    val uiState: StateFlow<AdjectivesLessonUiState> = _uiState.asStateFlow()

    init {
        loadExamples()
    }

    private fun loadExamples() {
        _uiState.value = _uiState.value.copy(
            examples = listOf(
                GrammarExampleModel("Big", "Teddy is big.", R.drawable.adjective_big),
                GrammarExampleModel("Small", "Teddy is small.", R.drawable.adjective_small),
                GrammarExampleModel("Happy", "I am happy.", R.drawable.adjective_happy),
                GrammarExampleModel("Sad", "I am sad.", R.drawable.adjective_sad),
                GrammarExampleModel("Red", "The ball is red.", R.drawable.adjective_red),
                GrammarExampleModel("Blue", "The ball is blue.", R.drawable.adjective_blue),
                GrammarExampleModel("Tall", "The boy is tall.", R.drawable.adjective_tall),
                GrammarExampleModel("Short", "The girl is short.", R.drawable.adjective_short)
            )
        )
    }

    fun onExampleTapped(example: GrammarExampleModel) {
        ttsManager.speak(example.speakText)
    }
}