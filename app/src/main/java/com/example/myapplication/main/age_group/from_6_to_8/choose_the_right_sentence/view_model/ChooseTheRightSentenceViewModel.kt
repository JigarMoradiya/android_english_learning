package com.example.myapplication.main.age_group.from_6_to_8.choose_the_right_sentence.view_model

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.generation.letter.LetterRepository
import com.example.myapplication.utils.AudioPlayerManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ChooseTheRightSentenceViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ChooseTheRightSentenceUiState())
    val uiState: StateFlow<ChooseTheRightSentenceUiState> = _uiState


}