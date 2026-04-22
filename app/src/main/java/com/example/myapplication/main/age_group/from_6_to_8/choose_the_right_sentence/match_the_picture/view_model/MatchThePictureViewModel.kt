package com.example.myapplication.main.age_group.from_6_to_8.choose_the_right_sentence.match_the_picture.view_model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MatchThePictureViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MatchThePictureUiState())
    val uiState: StateFlow<MatchThePictureUiState> = _uiState


}