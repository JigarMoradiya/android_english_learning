package com.example.myapplication.main.age_category.view_model

import java.util.UUID

// --- Model ---
data class AgeCategoryData(
    val id: String = UUID.randomUUID().toString(),
    val img: Int,
    val destination: String // navigation route
)