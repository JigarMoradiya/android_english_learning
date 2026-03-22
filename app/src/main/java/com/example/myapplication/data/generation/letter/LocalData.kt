package com.example.myapplication.data.generation.letter

import java.util.UUID

data class LetterData(
    val letter: String,
    val mainWord: String,
    val altWords: List<String>,
    val soundFile: String,
    val id: String = UUID.randomUUID().toString(), // same as UUID()
)