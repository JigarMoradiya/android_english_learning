package com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.noun.lesson.view_model

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
class NounLessonViewModel @Inject constructor(
    private val ttsManager: TextToSpeechManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(NounLessonUiState())
    val uiState: StateFlow<NounLessonUiState> = _uiState.asStateFlow()

    init {
        loadExamples()
    }

    private fun loadExamples() {
        _uiState.value = _uiState.value.copy(
            examples = listOf(
                GrammarExampleModel("Boy", "The boy wears a blue t-shirt.", R.drawable.boy),
                GrammarExampleModel("Dog", "The dog is faithful.", R.drawable.dog),
                GrammarExampleModel("House", "This is my house.", R.drawable.house),
                GrammarExampleModel("Table", "The table top is round.", R.drawable.table),
                GrammarExampleModel("Cat", "The cat is so cute.", R.drawable.cat),
                GrammarExampleModel("Girl", "The girl's eyes are so beautiful.", R.drawable.girl),
                GrammarExampleModel("Apple", "The apple is red.", R.drawable.apple),
                GrammarExampleModel("Ball", "The ball has two colors.", R.drawable.ball),
                GrammarExampleModel("Bag", "I have a bag.", R.drawable.bag),
                GrammarExampleModel("Car", "This car is blue.", R.drawable.car),
            )
        )
    }

    fun onExampleTapped(example: GrammarExampleModel) {
        ttsManager.speak(example.speakText)
    }
}