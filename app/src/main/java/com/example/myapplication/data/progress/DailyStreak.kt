package com.example.myapplication.data.progress

data class DailyStreak(
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val lastActivityDateMs: Long? = null
)
