package com.example.myapplication.main.age_group.from_3_to_5.arrange_letter_sequence.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.generation.letter.LetterRepository
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.utilities.TextToSpeechManager
import com.example.myapplication.utils.AudioPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArrangeLetterInSequenceViewModel @Inject constructor(
    private val ttsManager: TextToSpeechManager,
) : ViewModel() {

    var uiState by mutableStateOf(ArrangeLetterInSequenceUiState())
        private set


}