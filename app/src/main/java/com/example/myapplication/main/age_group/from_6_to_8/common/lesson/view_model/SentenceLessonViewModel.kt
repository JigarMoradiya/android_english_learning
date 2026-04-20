package com.example.myapplication.main.age_group.from_6_to_8.common.lesson.view_model

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.SentenceUnit
import com.example.myapplication.data.model.UnitSelectionScreen
import com.example.myapplication.main.age_group.from_6_to_8.common.unit.data.LessonLoader
import com.example.myapplication.main.age_group.from_6_to_8.common.unit.data.SentenceProgressManager
import com.example.myapplication.main.age_group.from_6_to_8.common.unit.data.sentenceUnits
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SentenceLessonViewModel @Inject constructor(
    private val progressManager: SentenceProgressManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var uiState by mutableStateOf(SentenceLessonUiState())
        private set

    // ✅ Set screen type once
    fun setScreenTypeAndUnit(screenType: UnitSelectionScreen, unit: SentenceUnit, level: SentenceLevel) {
        uiState = uiState.copy(screenType = screenType, unit = unit, level = level)
        loadLessons()
    }

    val unitTitle: String
        get() = sentenceUnits
            .firstOrNull { it.unit == uiState.unit }
            ?.title ?: ""

    // ✅ Clean function (no external params)
    fun loadLessons() {
        uiState = uiState.copy(
            lessons = LessonLoader
                .load(context, uiState.unit, uiState.level)
                .sortedBy { it.order }
        )

    }

    fun isCompleted(lessonId: String): Boolean {
        return progressManager.isCompleted(type = uiState.screenType,lessonId = lessonId)
    }
}