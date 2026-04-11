package com.example.myapplication.main.age_group.from_5_to_7.vocabulary_building.category_detail.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.model.VocabularyCategoryType
import com.example.myapplication.main.age_group.presentation.model.vocabularyCategoryDataList
import com.example.myapplication.ui.theme.colorFromWord
import com.example.myapplication.ui.theme.colorList
import com.example.myapplication.utilities.TextToSpeechManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VocabularyBuildingDetailViewModel @Inject constructor(
    private val ttsManager: TextToSpeechManager
) : ViewModel() {

    var uiState by mutableStateOf(VocabularyBuildingDetailUiState())
        private set

    fun getDetailList(type: String) {
        val categoryData = vocabularyCategoryDataList.toMutableList().first { it.type == type }
        uiState = uiState.copy(words = categoryData.words)
    }

    fun backgroundForCategory(word: String, categoryType: String): Color {
        return if (categoryType == VocabularyCategoryType.COLORS.name) {
            colorFromWord(word)
        } else {
            uiState.cardColors.base.copy(alpha = 0.2f)
        }
    }

    fun speak(word: String) {
        ttsManager.speak(word)
    }
}