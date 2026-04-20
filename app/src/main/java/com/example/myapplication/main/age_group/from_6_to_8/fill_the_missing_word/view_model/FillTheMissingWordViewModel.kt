package com.example.myapplication.main.age_group.from_6_to_8.fill_the_missing_word.view_model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FillTheMissingWordViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FillTheMissingWordUiState())
    val uiState: StateFlow<FillTheMissingWordUiState> = _uiState


}