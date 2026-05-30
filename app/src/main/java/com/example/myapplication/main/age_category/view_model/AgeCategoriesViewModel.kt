package com.example.myapplication.main.age_category.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.data.progress.StreakRepository
import com.example.myapplication.main.base.nav.RouteNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AgeCategoriesViewModel @Inject constructor(
    application: Application,
    private val streakRepository: StreakRepository
) : AndroidViewModel(application) {

    private val _categories = MutableStateFlow(
        listOf(
            AgeCategoryData(img = R.drawable._menu_age_3_5, destination = RouteNavigation.AgeGroup3to5.route),
            AgeCategoryData(img = R.drawable._menu_age_5_7, destination = RouteNavigation.AgeGroup5to7.route),
            AgeCategoryData(img = R.drawable._menu_age_6_8, destination = RouteNavigation.AgeGroup6to8.route)
        )
    )
    val categories: StateFlow<List<AgeCategoryData>> = _categories.asStateFlow()

    val currentStreak: StateFlow<Int> = streakRepository.streak
        .map { it.currentStreak }
        .stateIn(viewModelScope, SharingStarted.Eagerly, streakRepository.streak.value.currentStreak)

    val bestStreak: StateFlow<Int> = streakRepository.streak
        .map { it.bestStreak }
        .stateIn(viewModelScope, SharingStarted.Eagerly, streakRepository.streak.value.bestStreak)

    val todayLetter: Char
        get() {
            val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
            return 'A' + (dayOfYear - 1) % 26
        }

    fun refresh() {
        // No-op — streak updates automatically via StateFlow when SessionRepository.record() is called
    }
}
