package com.example.myapplication.main.age_group.from_6_to_8.sentence_builder.view_model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SentenceBuilderViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SentenceBuilderUiState())
    val uiState: StateFlow<SentenceBuilderUiState> = _uiState


}