package com.example.myapplication.main.age_group.from_6_to_8.common.unit.view_model

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.SentenceUnit
import com.example.myapplication.data.model.UnitSelectionScreen
import com.example.myapplication.main.age_group.from_6_to_8.common.unit.data.LessonLoader
import com.example.myapplication.main.age_group.from_6_to_8.common.unit.data.SentenceProgressManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SentenceUnitViewModel @Inject constructor(
    private val progressManager: SentenceProgressManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var uiState by mutableStateOf(SentenceUnitUiState())
        private set

    // ✅ Set screen type once
    fun setScreenType(screenType: UnitSelectionScreen) {
        uiState = uiState.copy(screenType = screenType)
    }

    // ✅ Clean function (no external params)
    fun getLessonCounts(
        unit: SentenceUnit,
    ): Pair<Int, Int> {

        val lessons = LessonLoader.load(context, unit, uiState.level)
        val completed = lessons.count {
            progressManager.isCompleted(uiState.screenType, it.id)
        }

        return Pair(completed, lessons.size)
    }

    fun changeLevel(level: SentenceLevel) {
        uiState = uiState.copy(level = level)
    }
}