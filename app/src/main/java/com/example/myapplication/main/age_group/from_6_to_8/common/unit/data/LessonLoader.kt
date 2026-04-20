package com.example.myapplication.main.age_group.from_6_to_8.common.unit.data

import android.content.Context
import android.util.Log
import com.example.myapplication.data.model.ReadSentenceItemNew
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.SentenceUnit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object LessonLoader {

    private val cache = mutableMapOf<String, List<ReadSentenceItemNew>>()

    fun load(context: Context, unit: SentenceUnit, level: SentenceLevel): List<ReadSentenceItemNew> {

        val unitName = unit.name.lowercase()
        val levelName = level.name.lowercase()
        val fileName = "${unitName}_${levelName}"

        // ✅ Return cached data
        cache[fileName]?.let {
            return it
        }

        return try {
            val jsonString = context.assets
                .open("sentences_${levelName}/$fileName.json") // 👈 your folder
                .bufferedReader()
                .use { it.readText() }

            val type = object : TypeToken<List<ReadSentenceItemNew>>() {}.type
            val lessons: List<ReadSentenceItemNew> =
                Gson().fromJson(jsonString, type)

            cache[fileName] = lessons

            Log.d("LessonLoader", "✅ Loaded ${lessons.size} lessons from $fileName")

            lessons

        } catch (e: Exception) {
            Log.e("LessonLoader", "❌ Error loading $fileName", e)
            emptyList()
        }
    }
}