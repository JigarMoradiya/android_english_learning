package com.example.myapplication.main.age_group.from_6_to_8.read_listen.view_model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ReadAndListenViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ReadAndListenUiState())
    val uiState: StateFlow<ReadAndListenUiState> = _uiState


}