package com.example.myapplication.data.progress

data class LearningSession(
    val moduleId: String,
    val ageGroup: AgeGroup,
    val durationSeconds: Int,
    val score: Int,
    val totalQuestions: Int,
    val timestampMs: Long = System.currentTimeMillis(),
    val wrongItems: List<String>? = null,
    val subConfig: String? = null
) {
    val accuracy: Double
        get() = if (totalQuestions > 0) score.toDouble() / totalQuestions else 0.0
}
