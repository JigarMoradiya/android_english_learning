package com.example.myapplication.main.age_group.from_5_to_7.vocabulary_building.category_detail.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.model.VocabularyCategoryType
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.SessionRepository
import com.example.myapplication.main.age_group.presentation.model.vocabularyCategoryDataList
import com.example.myapplication.ui.theme.colorFromWord
import com.example.myapplication.utilities.TextToSpeechManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VocabularyBuildingDetailViewModel @Inject constructor(
    private val ttsManager: TextToSpeechManager,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    var uiState by mutableStateOf(VocabularyBuildingDetailUiState())
        private set

    private val tappedWords = mutableSetOf<String>()
    private var startTimeMs = 0L
    private var currentCategoryType: String? = null

    fun getDetailList(type: String) {
        val categoryData = vocabularyCategoryDataList.toMutableList().first { it.type == type }
        uiState = uiState.copy(words = categoryData.words)
        currentCategoryType = type
        if (startTimeMs == 0L) startTimeMs = System.currentTimeMillis()
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
        tappedWords.add(word)
    }

    override fun onCleared() {
        super.onCleared()
        val moduleId = categoryTypeToModuleId(currentCategoryType ?: return) ?: return
        if (tappedWords.isEmpty()) return
        val duration = ((System.currentTimeMillis() - startTimeMs) / 1000).toInt()
        sessionRepository.record(
            LearningSession(
                moduleId = moduleId,
                ageGroup = AgeGroup.FIVE_TO_SEVEN,
                durationSeconds = duration,
                score = tappedWords.size,
                totalQuestions = 0,
                wrongItems = emptyList(),
                correctItems = tappedWords.sorted()
            )
        )
    }

    private fun categoryTypeToModuleId(type: String): String? = when (type) {
        VocabularyCategoryType.ANIMALS.name    -> ModuleID.VOCABULARY_ANIMALS
        VocabularyCategoryType.FRUITS.name     -> ModuleID.VOCABULARY_FRUITS
        VocabularyCategoryType.BIRDS.name      -> ModuleID.VOCABULARY_BIRDS
        VocabularyCategoryType.VEGETABLES.name -> ModuleID.VOCABULARY_VEGETABLES
        VocabularyCategoryType.COLORS.name     -> ModuleID.VOCABULARY_COLORS
        VocabularyCategoryType.SHAPES.name     -> ModuleID.VOCABULARY_SHAPES
        VocabularyCategoryType.VEHICLES.name   -> ModuleID.VOCABULARY_VEHICLES
        else -> null
    }
}
