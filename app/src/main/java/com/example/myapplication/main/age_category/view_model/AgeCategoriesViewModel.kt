package com.example.myapplication.main.age_category.view_model

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.data.progress.SessionRepository
import com.example.myapplication.main.base.nav.RouteNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AgeCategoriesViewModel @Inject constructor(
    application: Application,
    private val sessionRepository: SessionRepository
) : AndroidViewModel(application) {

    private val _categories = MutableStateFlow(
        listOf(
            AgeCategoryData(img = R.drawable._menu_age_3_5, destination = RouteNavigation.AgeGroup3to5.route),
            AgeCategoryData(img = R.drawable._menu_age_5_7, destination = RouteNavigation.AgeGroup5to7.route),
            AgeCategoryData(img = R.drawable._menu_age_6_8, destination = RouteNavigation.AgeGroup6to8.route)
        )
    )
    val categories: StateFlow<List<AgeCategoryData>> = _categories.asStateFlow()

    var currentStreak by mutableIntStateOf(0)
        private set
    var bestStreak by mutableIntStateOf(0)
        private set

    val todayLetter: Char
        get() {
            val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
            return 'A' + (dayOfYear - 1) % 26
        }

    init {
        viewModelScope.launch { computeStreak() }
    }

    fun refresh() {
        viewModelScope.launch { computeStreak() }
    }

    private fun computeStreak() {
        val sessions = sessionRepository.allSessions()
        val days = sessions.map { session ->
            val c = Calendar.getInstance().apply { timeInMillis = session.timestampMs }
            c.set(Calendar.HOUR_OF_DAY, 0); c.set(Calendar.MINUTE, 0)
            c.set(Calendar.SECOND, 0); c.set(Calendar.MILLISECOND, 0)
            c.timeInMillis
        }.toSortedSet()

        if (days.isEmpty()) { currentStreak = 0; bestStreak = 0; return }

        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }
        val yesterday = (today.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -1) }

        var streak = when {
            days.contains(today.timeInMillis)     -> 1
            days.contains(yesterday.timeInMillis) -> 1
            else -> 0
        }

        if (streak > 0) {
            val check = if (days.contains(today.timeInMillis)) today.clone() as Calendar else yesterday.clone() as Calendar
            check.add(Calendar.DAY_OF_YEAR, -1)
            while (days.contains(check.timeInMillis)) {
                streak++
                check.add(Calendar.DAY_OF_YEAR, -1)
            }
        }

        var best = 0
        var run = 0
        var prev: Long? = null
        for (day in days) {
            if (prev == null) { run = 1 } else {
                val expected = (Calendar.getInstance().apply { timeInMillis = prev!! }).also {
                    it.add(Calendar.DAY_OF_YEAR, 1)
                }.timeInMillis
                run = if (day == expected) run + 1 else 1
            }
            best = maxOf(best, run)
            prev = day
        }

        currentStreak = streak
        bestStreak = maxOf(best, streak)
    }
}
