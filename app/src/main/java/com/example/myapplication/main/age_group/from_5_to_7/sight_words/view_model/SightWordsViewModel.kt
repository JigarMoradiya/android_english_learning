package com.example.myapplication.main.age_group.from_5_to_7.sight_words.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SightWordsViewModel @Inject constructor(
) : ViewModel() {

    var uiState by mutableStateOf(SightWordsUiState())
        private set

}