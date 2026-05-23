package com.example.myapplication.data.progress.models

data class MatchUpperLowerProgress(
    val totalRoundsCompleted: Int = 0,
    val weakPairIndices: List<Int> = emptyList()
)
