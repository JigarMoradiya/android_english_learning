package com.example.myapplication.data.progress

import com.example.myapplication.utilities.pref.AppPreferencesHelper
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModuleProgressRepository @Inject constructor(
    private val prefs: AppPreferencesHelper
) {

    private val gson = Gson()

    fun <T> load(moduleId: String, clazz: Class<T>): T? {
        val key = "progress_$moduleId"
        val json = prefs.getCustomParam(key, "")
        return if (json.isNotEmpty()) gson.fromJson(json, clazz) else null
    }

    fun <T : Any> save(progress: T, moduleId: String) {
        val key = "progress_$moduleId"
        prefs.setCustomParam(key, gson.toJson(progress))
    }
}
