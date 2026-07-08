package com.example.myapplication.data.progress.models

data class MatchUpperLowerProgress(
    val totalRoundsCompleted: Int = 0,
    val weakPairIndices: List<Int> = emptyList(),
    // Correct-answer streak per weak letter (key = index into A-Z, same as
    // weakPairIndices). Reaching 2 graduates the letter out of the weak
    // list. Nullable, not defaulted to emptyMap() — ModuleProgressRepository
    // uses a bare Gson() with no Kotlin-aware adapter, so it deserializes
    // via unsafe allocation and never calls this constructor; a field
    // missing from an existing user's stored JSON is left as raw null, not
    // this default. Read via `.orEmpty()` everywhere, never directly.
    val weakLetterStreaks: Map<Int, Int>? = null
)
