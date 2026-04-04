package com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.generation.letter.LetterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ColoringAlphabetsViewModel @Inject constructor() : ViewModel() {

    var uiState by mutableStateOf(ColoringAlphabetsUiState())
        private set

    init {

    }

    fun loadNextWord() {

    }

    fun closePopup() {
        uiState = uiState.copy(showPopup = false)
    }

    fun removeError() {
        uiState = uiState.copy(showError = false)
    }


}