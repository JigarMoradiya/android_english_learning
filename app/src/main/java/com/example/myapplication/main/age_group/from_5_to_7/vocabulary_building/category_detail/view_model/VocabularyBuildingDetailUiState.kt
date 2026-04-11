package com.example.myapplication.main.age_group.from_5_to_7.vocabulary_building.category_detail.view_model

import com.example.myapplication.ui.theme.ButtonColors
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.ui.theme.getButtonColors


data class VocabularyBuildingDetailUiState(
    var words: List<String> = emptyList(),
    var buttonType: ButtonType = ButtonType.entries.random(),
    var cardColors: ButtonColors = getButtonColors(buttonType)
)
