package com.example.myapplication.data.access

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dailyLimitDataStore: DataStore<Preferences>
        by preferencesDataStore(name = "daily_limits")

/**
 * Tracks how many FREE_LIMITED activities the user has played today.
 *
 * Storage key (date-scoped, auto-reset each new day):
 *   "global_count_<yyyy-MM-dd>" → Int — total FREE_LIMITED plays today
 *
 * Limits:
 *   Guest      → 1 activity/day
 *   Free login → 3 activities/day
 *   Same activity CAN be replayed (each replay costs 1 slot).
 */
@Singleton
class DailyLimitManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val guestLimit = 1
    private val freeLimit  = 3

    // ── Public API ────────────────────────────────────────────────────

    /** Returns true if the user still has activity slots remaining today. */
    suspend fun hasAttemptsLeft(isLoggedIn: Boolean): Boolean {
        val limit = if (isLoggedIn) freeLimit else guestLimit
        return globalCount() < limit
    }

    /** Returns how many activity slots are left today (minimum 0). */
    suspend fun remainingAttempts(isLoggedIn: Boolean): Int {
        val limit = if (isLoggedIn) freeLimit else guestLimit
        return maxOf(0, limit - globalCount())
    }

    /** Clears today's count. For testing only. */
    suspend fun clearTodayCount() {
        val countKey = intPreferencesKey("global_count_${today()}")
        context.dailyLimitDataStore.edit { prefs ->
            prefs.remove(countKey)
        }
    }

    /** Increments the global daily count. Called once per completed FREE_LIMITED session. */
    suspend fun recordModulePlayed(moduleId: String) {
        val countKey = intPreferencesKey("global_count_${today()}")
        context.dailyLimitDataStore.edit { prefs ->
            prefs[countKey] = (prefs[countKey] ?: 0) + 1
        }
    }

    // ── Private helpers ───────────────────────────────────────────────

    private suspend fun globalCount(): Int {
        val key = intPreferencesKey("global_count_${today()}")
        return context.dailyLimitDataStore.data.first()[key] ?: 0
    }

    private fun today(): String = LocalDate.now().toString()
}
