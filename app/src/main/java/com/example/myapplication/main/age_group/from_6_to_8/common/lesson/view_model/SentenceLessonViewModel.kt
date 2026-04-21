package com.example.myapplication.main.age_group.from_6_to_8.common.lesson.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.SentenceUnit
import com.example.myapplication.data.model.UnitSelectionScreen
import com.example.myapplication.data.generation.loader.LessonLoader
import com.example.myapplication.main.age_group.from_6_to_8.common.unit.data.SentenceProgressManager
import com.example.myapplication.main.age_group.from_6_to_8.common.unit.data.sentenceUnits
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SentenceLessonViewModel @Inject constructor(
    private val progressManager: SentenceProgressManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    // ✅ StateFlow instead of mutableStateOf
    private val _uiState = MutableStateFlow(SentenceLessonUiState())
    val uiState: StateFlow<SentenceLessonUiState> = _uiState

    // ✅ Set screen type + unit
    fun setScreenTypeAndUnit(
        screenType: UnitSelectionScreen,
        unit: SentenceUnit,
        level: SentenceLevel
    ) {
        _uiState.update {
            it.copy(
                screenType = screenType,
                unit = unit,
                level = level
            )
        }

        loadLessons()
    }

    // ✅ Derived value (same as computed property in Swift)
    val unitTitle: String
        get() = sentenceUnits
            .firstOrNull { it.unit == _uiState.value.unit }
            ?.title ?: ""

    // ✅ Load lessons
    fun loadLessons() {

        val lessons = LessonLoader
            .load(context, _uiState.value.unit, _uiState.value.level)
            .sortedBy { it.order }

        _uiState.update {
            it.copy(lessons = lessons)
        }
    }

    // ✅ Progress check (no state mutation)
    fun isCompleted(lessonId: String): Boolean {
        return progressManager.isCompleted(
            type = _uiState.value.screenType,
            lessonId = lessonId
        )
    }
}