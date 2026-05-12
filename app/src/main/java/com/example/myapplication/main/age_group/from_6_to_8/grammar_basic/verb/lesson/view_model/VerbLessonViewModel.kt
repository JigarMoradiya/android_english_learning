package com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.verb.lesson.view_model

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
class VerbLessonViewModel @Inject constructor(
    private val ttsManager: TextToSpeechManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(VerbLessonUiState())
    val uiState: StateFlow<VerbLessonUiState> = _uiState.asStateFlow()

    init {
        loadExamples()
    }

    private fun loadExamples() {
        _uiState.value = _uiState.value.copy(
            examples = listOf(
                GrammarExampleModel("Run", "I run in the park.", R.drawable.action_run),
                GrammarExampleModel("Jump", "The girl can jump high.", R.drawable.action_jump),
                GrammarExampleModel("Eat", "I eat an apple.", R.drawable.action_eat),
                GrammarExampleModel("Play", "We play with toys.", R.drawable.action_play),
                GrammarExampleModel("Swim", "He can swim in the pool.", R.drawable.action_swim),
                GrammarExampleModel("Sing", "The girl sings a happy song.", R.drawable.action_sing),
                GrammarExampleModel("Dance", "The boy likes to dance.", R.drawable.action_dance),
                GrammarExampleModel("Read", "He reads a book.", R.drawable.action_read),
                GrammarExampleModel("Write", "I write in my notebook.", R.drawable.action_write)
            )
        )
    }

    fun onExampleTapped(example: GrammarExampleModel) {
        ttsManager.speak(example.speakText)
    }
}