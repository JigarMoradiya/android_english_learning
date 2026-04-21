package com.example.myapplication.main.age_group.from_6_to_8.common.unit.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.SentenceUnit
import com.example.myapplication.data.model.UnitSelectionScreen
import com.example.myapplication.data.generation.loader.LessonLoader
import com.example.myapplication.main.age_group.from_6_to_8.common.unit.data.SentenceProgressManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SentenceUnitViewModel @Inject constructor(
    private val progressManager: SentenceProgressManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(SentenceUnitUiState())
    val uiState: StateFlow<SentenceUnitUiState> = _uiState

    // ✅ Set screen type
    fun setScreenType(screenType: UnitSelectionScreen) {
        _uiState.update {
            it.copy(screenType = screenType)
        }
    }

    // ✅ Change level
    fun changeLevel(level: SentenceLevel) {
        _uiState.update {
            it.copy(level = level)
        }
    }

    // ✅ Get lesson counts
    fun getLessonCounts(unit: SentenceUnit): Pair<Int, Int> {

        val state = _uiState.value

        val lessons = LessonLoader.load(
            context = context,
            unit = unit,
            level = state.level
        )

        val completed = lessons.count {
            progressManager.isCompleted(
                type = state.screenType,
                lessonId = it.id
            )
        }

        return Pair(completed, lessons.size)
    }
}