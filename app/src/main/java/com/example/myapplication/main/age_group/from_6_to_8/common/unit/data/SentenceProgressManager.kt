package com.example.myapplication.main.age_group.from_6_to_8.common.unit.data

import com.example.myapplication.data.model.UnitSelectionScreen
import com.example.myapplication.utilities.pref.AppPreferencesHelper
import com.example.myapplication.utilities.pref.PreferencesHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SentenceProgressManager @Inject constructor(
    private val prefs: AppPreferencesHelper
) {

    companion object {
        private const val KEY_PROGRESS = "sentence_progress"
        private const val KEY_UNLOCK_MODE = "isStepByStepUnlock"
    }

    // In-memory cache (same as iOS)
    private var progress: MutableMap<String, MutableSet<String>> = mutableMapOf()

    var isStepByStepUnlock: Boolean = false
        set(value) {
            field = value
            saveProgress()
        }

    init {
        loadProgress()
    }

    // MARK: - Load
    private fun loadProgress() {
        val json = prefs.getCustomParam(KEY_PROGRESS, "")

        if (json.isNotEmpty()) {
            val type = object : TypeToken<Map<String, Set<String>>>() {}.type
            val saved: Map<String, Set<String>> = Gson().fromJson(json, type)

            progress = saved.mapValues { it.value.toMutableSet() }.toMutableMap()
        }

        isStepByStepUnlock =
            prefs.getCustomParamBoolean(KEY_UNLOCK_MODE, false)
    }

    // MARK: - Save
    private fun saveProgress() {
        val json = Gson().toJson(progress)

        prefs.setCustomParam(KEY_PROGRESS, json)
        prefs.setCustomParamBoolean(KEY_UNLOCK_MODE, isStepByStepUnlock)
    }

    // MARK: - Mark Completed
    fun markCompleted(
        type: UnitSelectionScreen,
        lessonId: String
    ) {
        val key = type.name

        val set = progress.getOrPut(key) { mutableSetOf() }
        set.add(lessonId)

        saveProgress()
    }

    // MARK: - Check Completed
    fun isCompleted(
        type: UnitSelectionScreen,
        lessonId: String
    ): Boolean {
        return progress[type.name]?.contains(lessonId) ?: false
    }
}