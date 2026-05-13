package com.example.myapplication.data.generation.loader

import android.content.Context
import android.util.Log
import com.example.myapplication.data.model.PronounPracticeQuestion
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

object GrammarBasicPronounsLoader {

    private val cache =
        mutableMapOf<String, List<PronounPracticeQuestion>>()

    fun load(
        context: Context
    ): List<PronounPracticeQuestion> {

        val fileName = "grammar_basic_pronouns.json"

        cache[fileName]?.let {
            return it
        }

        return try {
            val jsonString = context.assets
                .open("grammar_basic/$fileName")
                .bufferedReader()
                .use { it.readText() }

            val type = object :
                TypeToken<List<PronounPracticeQuestion>>() {}.type

            val questions: List<PronounPracticeQuestion> =
                Gson().fromJson(jsonString, type)

            cache[fileName] = questions

            Log.d(
                "GrammarBasicPronouns",
                "✅ Loaded ${questions.size} pronoun questions"
            )

            questions

        } catch (e: Exception) {
            Log.e(
                "GrammarBasicPronouns",
                "❌ Error loading pronoun json",
                e
            )
            emptyList()
        }
    }
}