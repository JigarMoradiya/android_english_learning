package com.example.myapplication.data.generation.loader

import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.progress.LearningSession
import java.util.Calendar

data class SentenceModuleProgress(
    val moduleId: String,
    val title: String,
    val sessions: Int,
    val bestAccuracy: Double
)

data class SentenceProgressSummary(
    val totalActivities: Int,
    val last7DayCounts: List<Int>,   // 7 entries, oldest → today
    val perModule: List<SentenceModuleProgress>
)

/**
 * Progress-over-time aggregation for the Age 6-8 sentence modules (item 5.3).
 * Mirrors iOS `SentenceProgress.swift`; unit-tested via `SentenceProgressTest`.
 */
object SentenceProgress {

    val modules: List<Pair<String, String>> = listOf(
        ModuleID.READ_LISTEN to "Read & Listen",
        ModuleID.FILL_MISSING_WORD to "Fill the Missing Word",
        ModuleID.SENTENCE_CHECK to "True or False",
        ModuleID.MATCH_THE_PICTURE to "Match the Picture",
        ModuleID.WHICH_SENTENCE_RIGHT to "Which Sounds Right",
        ModuleID.FIND_CORRECT_WRITING to "Find the Correct Writing",
        ModuleID.SENTENCE_BUILDER to "Build the Sentence",
        ModuleID.ONE_WORD_ANSWER to "One Word Answer"
    )

    fun summary(
        sessions: List<LearningSession>,
        nowMs: Long = System.currentTimeMillis()
    ): SentenceProgressSummary {
        val ids = modules.map { it.first }.toSet()
        val relevant = sessions.filter { it.moduleId in ids }

        val perModule = modules.mapNotNull { (id, title) ->
            val s = relevant.filter { it.moduleId == id }
            if (s.isEmpty()) null
            else SentenceModuleProgress(
                moduleId = id,
                title = title,
                sessions = s.size,
                bestAccuracy = s.maxOf { accuracy(it) }
            )
        }.sortedByDescending { it.sessions }

        val counts = IntArray(7)
        val todayStart = startOfDay(nowMs)
        val dayMs = 24L * 60 * 60 * 1000
        for (session in relevant) {
            val diff = ((todayStart - startOfDay(session.timestampMs)) / dayMs).toInt()
            if (diff in 0..6) counts[6 - diff] += 1
        }

        return SentenceProgressSummary(relevant.size, counts.toList(), perModule)
    }

    private fun accuracy(s: LearningSession): Double =
        if (s.totalQuestions > 0) s.score.toDouble() / s.totalQuestions else 0.0

    private fun startOfDay(ms: Long): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = ms
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}
