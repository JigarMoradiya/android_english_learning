package com.example.myapplication.data.generation.loader

import android.content.Context
import android.util.Log
import com.example.myapplication.data.model.ComprehensionQuestion
import com.example.myapplication.data.model.SentenceLevel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object OneWordAnswerLoader {

    private val cache =
        mutableMapOf<String, Map<String, List<ComprehensionQuestion>>>()

    fun load(
        context: Context,
        lessonId: String,
        level: SentenceLevel
    ): List<ComprehensionQuestion> {

        val fileName = "one_word_answer_${level.name.lowercase()}.json"

        // ✅ Return from cache
        cache[fileName]?.let { cachedFile ->
            return cachedFile[lessonId] ?: emptyList()
        }

        return try {
            val jsonString = context.assets
                .open("one_word_answer_question/$fileName") // adjust path if inside folder
                .bufferedReader()
                .use { it.readText() }

            // ✅ Parse full dictionary
            val type = object :
                TypeToken<Map<String, List<ComprehensionQuestion>>>() {}.type

            val allLessons: Map<String, List<ComprehensionQuestion>> =
                Gson().fromJson(jsonString, type)

            // ✅ Cache full file
            cache[fileName] = allLessons

            Log.d(
                "OneWordAnswerLoader",
                "✅ Loaded ${allLessons.size} lessons from $fileName"
            )

            // ✅ Return specific lesson
            allLessons[lessonId] ?: emptyList()

        } catch (e: Exception) {
            Log.e("OneWordAnswerLoader", "❌ Error loading $fileName", e)
            emptyList()
        }
    }
}