package com.example.myapplication.data.progress

import java.util.Calendar

data class DailyStreak(
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val lastActivityDateMs: Long? = null
) {
    /** True if [lastActivityDateMs] falls on today's calendar day — used to decide
     *  whether the daily streak-reminder notification still needs to fire. */
    val hasCompletedToday: Boolean
        get() {
            val last = lastActivityDateMs ?: return false
            val today = Calendar.getInstance()
            val lastCal = Calendar.getInstance().apply { timeInMillis = last }
            return today.get(Calendar.YEAR) == lastCal.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == lastCal.get(Calendar.DAY_OF_YEAR)
        }
}
