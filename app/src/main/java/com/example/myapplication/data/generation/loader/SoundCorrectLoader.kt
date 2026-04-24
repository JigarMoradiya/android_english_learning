package com.example.myapplication.data.generation.loader

import android.content.Context
import android.util.Log
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.SentenceUnit
import com.example.myapplication.data.model.WhichSentenceSoundsRight
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SoundCorrectLoader {

    private val cache = mutableMapOf<String, List<WhichSentenceSoundsRight>>()

    fun load(
        context: Context,
        unit: SentenceUnit,
        level: SentenceLevel
    ): List<WhichSentenceSoundsRight> {

        val unitName = unit.name.lowercase()
        val levelName = level.name.lowercase()

        val fileName = "sound_${unitName}.json"
        val folderName = "sound_correct_${levelName}"

        val cacheKey = "$folderName/$fileName"

        // ✅ Return cached
        cache[cacheKey]?.let { return it }

        return try {

            val jsonString = context.assets
                .open("$folderName/$fileName")
                .bufferedReader()
                .use { it.readText() }

            val type = object : TypeToken<List<WhichSentenceSoundsRight>>() {}.type

            val questions: List<WhichSentenceSoundsRight> =
                Gson().fromJson(jsonString, type)

            cache[cacheKey] = questions

            Log.d("SoundCorrectLoader", "✅ Loaded ${questions.size} from $cacheKey")

            questions

        } catch (e: Exception) {
            Log.e("SoundCorrectLoader", "❌ Error loading $cacheKey", e)
            emptyList()
        }
    }
}