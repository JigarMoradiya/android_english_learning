package com.example.myapplication.main.age_group.from_6_to_8.read_listen.view_model

import com.example.myapplication.data.model.ReadSentenceItemNew
import com.example.myapplication.data.model.UnitSelectionScreen

data class ReadAndListenUiState(
    val screenType : UnitSelectionScreen = UnitSelectionScreen.READ_AND_LISTEN_SENTENCE,
    val lessonData: ReadSentenceItemNew? = null,
    val hasMarkedComplete: Boolean = false,
    val isSentenceJoined: Boolean = false,
    val currentSentenceIndex: Int = 0,
)