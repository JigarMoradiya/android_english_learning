package com.example.myapplication.main.age_group.from_6_to_8.common.unit.view_model

import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.SentenceUnitModel
import com.example.myapplication.data.model.UnitSelectionScreen
import com.example.myapplication.main.age_group.from_6_to_8.common.unit.data.sentenceUnits

data class SentenceUnitUiState(
    val screenType : UnitSelectionScreen = UnitSelectionScreen.READ_AND_LISTEN_SENTENCE,
    val level: SentenceLevel = SentenceLevel.EASY,
    val sentenceUnitsList: List<SentenceUnitModel> = sentenceUnits,
)