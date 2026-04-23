package com.example.myapplication.data.generation.loader

import android.content.Context
import android.util.Log
import com.example.myapplication.data.model.MatchPictureQuestion
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.SentenceUnit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object MatchPictureLoader {

    private val cache = mutableMapOf<String, List<MatchPictureQuestion>>()

    fun load(
        context: Context,
        unit: SentenceUnit,
        level: SentenceLevel
    ): List<MatchPictureQuestion> {

        val unitName = unit.name.lowercase()
        val levelName = level.name.lowercase()

        val fileName = "match_${unitName}.json"
        val folderName = "match_the_picture_${levelName}"

        val cacheKey = "$folderName/$fileName"

        // ✅ Return cached
        cache[cacheKey]?.let { return it }

        return try {

            val jsonString = context.assets
                .open("$folderName/$fileName")
                .bufferedReader()
                .use { it.readText() }

            val type = object : TypeToken<List<MatchPictureQuestion>>() {}.type

            val questions: List<MatchPictureQuestion> =
                Gson().fromJson(jsonString, type)

            cache[cacheKey] = questions

            Log.d("MatchLoader", "✅ Loaded ${questions.size} from $cacheKey")

            questions

        } catch (e: Exception) {
            Log.e("MatchLoader", "❌ Error loading $cacheKey", e)
            emptyList()
        }
    }
}