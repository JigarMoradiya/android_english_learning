package com.example.myapplication.data.progress

// Manages the daily learning streak.
// Called automatically by SessionRepository.record() — never call directly from ViewModels.
//
// Streak rules (identical to iOS StreakRepository):
//   diff == 0  → already played today, no change
//   diff == 1  → played yesterday, streak + 1
//   diff >= 2  → missed at least one day, reset to 1

import com.example.myapplication.data.access.ReviewManager
import com.example.myapplication.utilities.pref.AppPreferencesHelper
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StreakRepository @Inject constructor(
    private val prefs: AppPreferencesHelper,
    private val reviewManager: ReviewManager
) {

    private val gson = Gson()
    private val key = "streak_data"

    private val _streak = MutableStateFlow(load())
    val streak: StateFlow<DailyStreak> = _streak.asStateFlow()

    init {
        reviewManager.bootstrapStreakMilestones(currentStreak = _streak.value.currentStreak)
    }

    // Called by SessionRepository.record() only

    fun onActivityCompleted() {
        var s = _streak.value
        val todayStart = startOfDay(System.currentTimeMillis())

        val lastDateMs = s.lastActivityDateMs
        if (lastDateMs != null) {
            val lastDayStart = startOfDay(lastDateMs)
            val diffDays = TimeUnit.MILLISECONDS.toDays(todayStart - lastDayStart)

            s = when {
                diffDays == 0L -> s                                            // already played today
                diffDays == 1L -> s.copy(currentStreak = s.currentStreak + 1) // played yesterday
                else           -> s.copy(currentStreak = 1)                   // missed a day, reset
            }
        } else {
            s = s.copy(currentStreak = 1) // first time ever
        }

        s = s.copy(
            lastActivityDateMs = System.currentTimeMillis(),
            bestStreak = maxOf(s.bestStreak, s.currentStreak)
        )

        _streak.value = s
        save(s)

        reviewManager.onStreakUpdated(s.currentStreak)
    }

    // Dev only — sets streak to 6 days with yesterday as last activity date

    fun simulateSixDayStreak() {
        val yesterday = System.currentTimeMillis() - 24 * 60 * 60 * 1000L
        val s = DailyStreak(currentStreak = 6, bestStreak = 6, lastActivityDateMs = yesterday)
        _streak.value = s
        save(s)
    }

    // Private

    private fun startOfDay(timestampMs: Long): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = timestampMs
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun save(s: DailyStreak) {
        prefs.setCustomParam(key, gson.toJson(s))
    }

    private fun load(): DailyStreak {
        val json = prefs.getCustomParam(key, "")
        if (json.isEmpty()) return DailyStreak()
        return try {
            gson.fromJson(json, DailyStreak::class.java)
        } catch (e: Exception) {
            DailyStreak()
        }
    }
}
