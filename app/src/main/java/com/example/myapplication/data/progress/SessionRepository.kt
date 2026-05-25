package com.example.myapplication.data.progress

import com.example.myapplication.utilities.pref.AppPreferencesHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionRepository @Inject constructor(
    private val prefs: AppPreferencesHelper
) {

    private val gson = Gson()
    private val key = "learning_sessions_v1"
    private val maxAgeMs = 90L * 24 * 60 * 60 * 1000

    private var cache: MutableList<LearningSession> = mutableListOf()

    init {
        load()
    }

    fun record(session: LearningSession) {
        cache.add(session)
        prune()
        save()
    }

    fun sessionsThisWeek(weekOffset: Int = 0): List<LearningSession> {
        val monday = mondayOfWeek(weekOffset)
        val sunday = monday + 7L * 24 * 60 * 60 * 1000
        return cache.filter { it.timestampMs in monday until sunday }
    }

    fun allSessions(): List<LearningSession> = cache.toList()

    fun removeSessions(moduleId: String) {
        cache.removeAll { it.moduleId == moduleId }
        save()
    }

    fun earliestSessionDate(): Long? = cache.minOfOrNull { it.timestampMs }

    // MARK: - Monday calculation (locale-safe)

    fun mondayOfWeek(weekOffset: Int = 0): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        // dayOfWeek: 1=Sun, 2=Mon, ..., 7=Sat
        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        val daysFromMonday = (dayOfWeek + 5) % 7   // Mon→0, Tue→1, ..., Sun→6
        cal.add(Calendar.DAY_OF_YEAR, -daysFromMonday + weekOffset * 7)
        return cal.timeInMillis
    }

    // MARK: - Internal

    private fun prune() {
        val cutoff = System.currentTimeMillis() - maxAgeMs
        cache.removeAll { it.timestampMs < cutoff }
    }

    private fun save() {
        prefs.setCustomParam(key, gson.toJson(cache))
    }

    private fun load() {
        val json = prefs.getCustomParam(key, "")
        if (json.isNotEmpty()) {
            val type = object : TypeToken<List<LearningSession>>() {}.type
            cache = gson.fromJson<List<LearningSession>>(json, type)
                .toMutableList()
        }
    }
}
