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
 * Tracks how many times a user has accessed each FREE_LIMITED module today.
 *
 * Key format:  "<moduleId>_<yyyy-MM-dd>"   e.g. "coloring_alphabets_2026-05-16"
 * Including the date in the key means counts auto-reset each new day —
 * no cleanup logic needed.
 */
@Singleton
class DailyLimitManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    // ── Public API ────────────────────────────────────────────────────

    /** Returns how many attempts the user has used today for [moduleId]. */
    suspend fun getUsedCount(moduleId: String): Int {
        val key = preferenceKey(moduleId)
        return context.dailyLimitDataStore.data.first()[key] ?: 0
    }

    /**
     * Increments the today-counter for [moduleId] by 1.
     * Call this AFTER the user successfully starts the activity.
     */
    suspend fun increment(moduleId: String) {
        val key = preferenceKey(moduleId)
        context.dailyLimitDataStore.edit { prefs ->
            prefs[key] = (prefs[key] ?: 0) + 1
        }
    }

    /**
     * Returns true if the user still has attempts remaining today.
     *
     * @param moduleId  The module to check.
     * @param limit     The max allowed per day for this user tier.
     */
    suspend fun hasAttemptsLeft(moduleId: String, limit: Int): Boolean {
        return getUsedCount(moduleId) < limit
    }

    /**
     * Returns remaining attempts for [moduleId] today.
     * Returns 0 if the limit has been reached.
     */
    suspend fun remainingAttempts(moduleId: String, limit: Int): Int {
        return maxOf(0, limit - getUsedCount(moduleId))
    }

    // ── Private helpers ───────────────────────────────────────────────

    /**
     * Builds the DataStore key for today.
     * e.g. "coloring_alphabets_2026-05-16"
     * A new date = a new key = automatic daily reset.
     */
    private fun preferenceKey(moduleId: String): Preferences.Key<Int> {
        val today = LocalDate.now().toString()   // "yyyy-MM-dd"
        return intPreferencesKey("${moduleId}_$today")
    }
}
