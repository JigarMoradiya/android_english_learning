package com.example.myapplication.data.generation.loader

import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.progress.AgeGroup
import com.example.myapplication.data.progress.LearningSession
import org.junit.Assert.*
import org.junit.Test

/**
 * Verifies the Age 6-8 progress aggregation (item 5.3). Mirrors the iOS harness.
 */
class SentenceProgressTest {

    private fun session(moduleId: String, score: Int, total: Int, timestampMs: Long) =
        LearningSession(moduleId, AgeGroup.SIX_TO_EIGHT, 10, score, total, timestampMs)

    @Test
    fun summary_countsOnlySentenceModules_andBestAccuracy() {
        val now = System.currentTimeMillis()
        val sessions = listOf(
            session(ModuleID.SENTENCE_BUILDER, 5, 5, now),
            session(ModuleID.SENTENCE_BUILDER, 3, 5, now),
            session(ModuleID.SENTENCE_CHECK, 4, 5, now),
            session("some_other_module", 1, 5, now)
        )
        val sum = SentenceProgress.summary(sessions, now)
        assertEquals(3, sum.totalActivities)
        assertEquals(2, sum.perModule.size)
        assertEquals(ModuleID.SENTENCE_BUILDER, sum.perModule.first().moduleId)
        assertEquals(2, sum.perModule.first().sessions)
        assertEquals(1.0, sum.perModule.first().bestAccuracy, 0.0001)
    }

    @Test
    fun last7DayCounts_bucketsByDay() {
        val now = System.currentTimeMillis()
        val dayMs = 24L * 60 * 60 * 1000
        val sessions = listOf(
            session(ModuleID.SENTENCE_CHECK, 1, 1, now),              // today
            session(ModuleID.SENTENCE_CHECK, 1, 1, now - 2 * dayMs),  // 2 days ago
            session(ModuleID.SENTENCE_CHECK, 1, 1, now - 30 * dayMs)  // outside 7-day window
        )
        val sum = SentenceProgress.summary(sessions, now)
        assertEquals(7, sum.last7DayCounts.size)
        assertEquals(1, sum.last7DayCounts[6])          // today
        assertEquals(1, sum.last7DayCounts[4])          // 2 days ago -> index 6-2
        assertEquals(2, sum.last7DayCounts.sum())       // 30-days-ago excluded
    }
}
