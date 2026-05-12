package com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.pronouns.lesson.view_model

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
class PronounLessonViewModel @Inject constructor(
    private val ttsManager: TextToSpeechManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(PronounLessonUiState())
    val uiState: StateFlow<PronounLessonUiState> = _uiState.asStateFlow()

    init {
        loadExamples()
    }

    private fun loadExamples() {
        _uiState.value = _uiState.value.copy(
            examples = listOf(
                GrammarExampleModel("I", "I am happy.", R.drawable.pronoun_i),
                GrammarExampleModel("He", "He is a boy.", R.drawable.pronoun_he),
                GrammarExampleModel("She", "She is a girl.", R.drawable.pronoun_she),
                GrammarExampleModel("It", "It is a box.", R.drawable.pronoun_it),
                GrammarExampleModel("You", "You are smart.", R.drawable.pronoun_you),
                GrammarExampleModel("We", "We are friends.", R.drawable.pronoun_we),
                GrammarExampleModel("They", "They are a family.", R.drawable.pronoun_they),
                GrammarExampleModel("Them", "Tell them I am not coming.", R.drawable.pronoun_them)
            )
        )
    }

    fun onExampleTapped(example: GrammarExampleModel) {
        ttsManager.speak(example.speakText)
    }
}