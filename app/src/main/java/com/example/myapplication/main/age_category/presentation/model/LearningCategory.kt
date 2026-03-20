package com.example.myapplication.main.age_category.presentation.model

import java.util.UUID

// --- Model ---
data class LearningCategory(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val ageRange: String,
    val destination: String // navigation route
)