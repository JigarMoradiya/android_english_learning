package com.example.myapplication.data.generation.loader

import com.example.myapplication.data.model.WordType

// ── Activity type ─────────────────────────────────────────────────────────────

enum class BeginnerActivityType {
    TAP_WORD,         // child taps the correct word inline in the sentence
    MULTIPLE_CHOICE   // child picks from 3 option chips
}

// ── Question model (mirrors iOS MixedBeginnerQuestion) ────────────────────────

data class MixedBeginnerQuestion(
    val sentence: String,             // full sentence text e.g. "The cat is sleeping."
    val imageName: String,            // drawable resource name from lesson JSON
    val targetType: WordType,         // NOUN / VERB / ADJECTIVE / PRONOUN
    val correctWord: String,          // primary correct answer (lowercased)
    val allCorrectWords: List<String>,// all words of targetType in sentence (lowercased)
    val options: List<String>,        // 3 shuffled options for multipleChoice
    val sentenceWords: List<String>,  // sentence split into words, punctuation stripped
    val activityType: BeginnerActivityType
)
