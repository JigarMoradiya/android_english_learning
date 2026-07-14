package com.example.myapplication.utils

/**
 * Flash-card pacing for speed modes: starts slow and builds up.
 * Word 0 shows for 3 seconds, each next word 0.25s faster, never under 1 second.
 */
object SpeedSchedule {
    private const val START_MS = 3000L
    private const val STEP_MS = 250L
    private const val MIN_MS = 1000L

    fun durationMillis(index: Int): Long =
        (START_MS - STEP_MS * index).coerceAtLeast(MIN_MS)
}
