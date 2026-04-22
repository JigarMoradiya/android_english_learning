package com.example.myapplication.main.age_group.from_6_to_8.choose_the_right_sentence.find_the_correct_writing.view_model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FindTheCorrectWritingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FindTheCorrectWritingUiState())
    val uiState: StateFlow<FindTheCorrectWritingUiState> = _uiState


}