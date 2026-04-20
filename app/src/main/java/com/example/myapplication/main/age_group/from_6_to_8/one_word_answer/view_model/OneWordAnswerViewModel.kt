package com.example.myapplication.main.age_group.from_6_to_8.one_word_answer.view_model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OneWordAnswerViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(OneWordAnswerUiState())
    val uiState: StateFlow<OneWordAnswerUiState> = _uiState


}