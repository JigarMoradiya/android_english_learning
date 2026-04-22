package com.example.myapplication.main.age_group.from_6_to_8.choose_the_right_sentence.which_sentence_sound_right.view_model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WhichSentenceSoundRightViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WhichSentenceSoundRightUiState())
    val uiState: StateFlow<WhichSentenceSoundRightUiState> = _uiState


}