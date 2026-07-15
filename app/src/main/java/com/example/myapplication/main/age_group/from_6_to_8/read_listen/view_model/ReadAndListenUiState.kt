package com.example.myapplication.main.age_group.from_6_to_8.read_listen.view_model

import com.example.myapplication.data.model.ReadSentenceItemNew
import com.example.myapplication.data.model.UnitSelectionScreen

// Echo / read-listen-repeat phases (item 1.3)
enum class EchoPhase { IDLE, LISTENING, YOUR_TURN }

data class ReadAndListenUiState(
    val screenType : UnitSelectionScreen = UnitSelectionScreen.READ_AND_LISTEN_SENTENCE,
    val lessonData: ReadSentenceItemNew? = null,
    val hasMarkedComplete: Boolean = false,
    val isSentenceJoined: Boolean = false,
    val currentSentenceIndex: Int = 0,

    val joinSentenceSpeakingIndex: Int? = null,
    val splitSentenceWordIndex: Int = -1,
    val isSpeaking: Boolean = false,

    val echoPhase: EchoPhase = EchoPhase.IDLE
)