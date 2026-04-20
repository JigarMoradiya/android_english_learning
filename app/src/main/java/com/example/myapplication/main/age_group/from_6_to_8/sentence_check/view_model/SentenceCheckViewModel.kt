package com.example.myapplication.main.age_group.from_6_to_8.sentence_check.view_model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SentenceCheckViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SentenceCheckUiState())
    val uiState: StateFlow<SentenceCheckUiState> = _uiState


}