package com.example.myapplication.main.parent

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.ModuleProgressRepository
import com.example.myapplication.data.progress.SessionRepository
import com.example.myapplication.data.progress.models.MatchUpperLowerProgress
import com.example.myapplication.main.base.nav.RouteNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

data class ModuleProgressRow(
    val moduleId: String,
    val displayName: String,
    val ageGroupLabel: String,
    val rounds: Int,
    val avgAccuracy: Double,    // 0.0–1.0
    val avgStars: Double,       // 0.0–3.0
    val route: String?          // RouteNavigation route string, null = not tappable
)

@HiltViewModel
class ParentProgressViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val moduleProgressRepository: ModuleProgressRepository
) : ViewModel() {

    var weekOffset by mutableStateOf(0)
        private set

    var currentStreak by mutableStateOf(0)
        private set
    var bestStreak by mutableStateOf(0)
        private set
    var weeklySessionCount by mutableStateOf(0)
        private set
    var weeklyDurationSeconds by mutableStateOf(0)
        private set
    var weeklyAccuracy by mutableStateOf(0.0)
        private set
    var activeDays by mutableStateOf(List(7) { false })
        private set
    var moduleRows by mutableStateOf(emptyList<ModuleProgressRow>())
        private set
    var weakLetters by mutableStateOf(emptyList<Char>())
        private set

    val canGoBack: Boolean
        get() {
            val prevStart = weekStartMs(weekOffset - 1)
            val cutoff = System.currentTimeMillis() - 90L * 24 * 60 * 60 * 1000
            return prevStart >= cutoff
        }

    val canGoForward: Boolean get() = weekOffset < 0

    val weekLabel: String
        get() {
            if (weekOffset == 0) return "This Week"
            val startMs = weekStartMs(weekOffset)
            val endMs = startMs + 6L * 24 * 60 * 60 * 1000
            val fmt = SimpleDateFormat("MMM d", Locale.getDefault())
            return "${fmt.format(startMs)} – ${fmt.format(endMs)}"
        }

    // MARK: - Navigation

    fun goToPreviousWeek() {
        if (!canGoBack) return
        weekOffset--
        reload()
    }

    fun goToNextWeek() {
        if (!canGoForward) return
        weekOffset++
        reload()
    }

    // MARK: - Load

    fun load() {
        weekOffset = 0
        reload()
    }

    private fun reload() {
        val allSessions = sessionRepository.allSessions()

        val weekStart = weekStartMs(weekOffset)
        val weekEnd = weekStart + 7L * 24 * 60 * 60 * 1000
        val weekSessions = allSessions.filter { it.timestampMs in weekStart until weekEnd }

        weeklySessionCount = weekSessions.size
        weeklyDurationSeconds = weekSessions.sumOf { it.durationSeconds }

        val quizSessions = weekSessions.filter { it.totalQuestions > 0 }
        weeklyAccuracy = if (quizSessions.isEmpty()) 0.0
        else quizSessions.sumOf { it.accuracy } / quizSessions.size

        computeStreak(allSessions)
        computeActiveDays(weekSessions, weekStart)
        buildModuleRows(weekSessions)
        loadWeakLetters()
    }

    // MARK: - Helpers

    private fun weekStartMs(offset: Int): Long {
        return sessionRepository.mondayOfWeek(offset)
    }

    private fun computeStreak(allSessions: List<LearningSession>) {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        val daysWithSessions = allSessions.map { session ->
            val c = Calendar.getInstance()
            c.timeInMillis = session.timestampMs
            c.set(Calendar.HOUR_OF_DAY, 0); c.set(Calendar.MINUTE, 0)
            c.set(Calendar.SECOND, 0); c.set(Calendar.MILLISECOND, 0)
            c.timeInMillis
        }.toSet()

        var streak = 0
        val cursor = cal.clone() as Calendar
        while (daysWithSessions.contains(cursor.timeInMillis)) {
            streak++
            cursor.add(Calendar.DAY_OF_YEAR, -1)
        }
        currentStreak = streak
        bestStreak = computeBestStreak(daysWithSessions)
    }

    private fun computeBestStreak(daysMs: Set<Long>): Int {
        val sorted = daysMs.sorted()
        val dayMs = 24L * 60 * 60 * 1000
        var best = 0
        var current = 0
        var prev = -1L
        for (day in sorted) {
            current = if (prev >= 0 && day - prev == dayMs) current + 1 else 1
            best = maxOf(best, current)
            prev = day
        }
        return best
    }

    private fun computeActiveDays(weekSessions: List<LearningSession>, weekStartMs: Long) {
        val flags = MutableList(7) { false }
        val dayMs = 24L * 60 * 60 * 1000
        for (session in weekSessions) {
            val dayOffset = ((session.timestampMs - weekStartMs) / dayMs).toInt()
            if (dayOffset in 0..6) flags[dayOffset] = true
        }
        activeDays = flags
    }

    private fun buildModuleRows(weekSessions: List<LearningSession>) {
        val grouped = weekSessions.groupBy { it.moduleId }
        val rows = grouped.mapNotNull { (moduleId, sessions) ->
            val info = moduleInfo[moduleId] ?: return@mapNotNull null
            val quizSessions = sessions.filter { it.totalQuestions > 0 }
            val avgAcc = if (quizSessions.isEmpty()) 0.0
            else quizSessions.sumOf { it.accuracy } / quizSessions.size
            val stars = if (quizSessions.isEmpty()) 0.0 else minOf(3.0, avgAcc * 3.0)

            ModuleProgressRow(
                moduleId = moduleId,
                displayName = info.first,
                ageGroupLabel = info.second,
                rounds = sessions.size,
                avgAccuracy = avgAcc,
                avgStars = stars,
                route = moduleRoutes[moduleId]
            )
        }.sortedBy { it.ageGroupLabel }
        moduleRows = rows
    }

    private fun loadWeakLetters() {
        val progress = moduleProgressRepository.load(ModuleID.MATCH_UPPER_LOWER, MatchUpperLowerProgress::class.java)
            ?: return run { weakLetters = emptyList() }
        val alphabet = ('A'..'Z').toList()
        weakLetters = progress.weakPairIndices.mapNotNull { idx ->
            alphabet.getOrNull(idx)
        }
    }

    // MARK: - Duration formatting

    fun formatDuration(seconds: Int): String = when {
        seconds < 60 -> "${seconds}s"
        seconds < 3600 -> String.format("%02d:%02d", seconds / 60, seconds % 60)
        else -> String.format("%02d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60)
    }

    // MARK: - Module info

    companion object {
        private val moduleInfo = mapOf(
            ModuleID.ALPHABET_TRACING        to ("Alphabet Tracing"     to "3–5"),
            ModuleID.ABCD_WITH_IMAGES        to ("ABCD with Images"     to "3–5"),
            ModuleID.COLORING_ALPHABETS      to ("Coloring Alphabets"   to "3–5"),
            ModuleID.LETTER_RECOGNITION      to ("Letter Recognition"   to "3–5"),
            ModuleID.MATCH_UPPER_LOWER       to ("Match Upper & Lower"  to "3–5"),
            ModuleID.MATCH_LETTER_WITH_IMAGE to ("Match Letter & Image" to "3–5"),
            ModuleID.FILL_THE_BLANK_LETTER   to ("Fill the Blank"       to "3–5"),
            ModuleID.ARRANGE_LETTER_SEQUENCE to ("Arrange Sequence"     to "3–5"),
            ModuleID.DRAG_DROP_LETTERS       to ("Drag & Drop Letters"  to "3–5"),
            ModuleID.SIGHT_WORDS             to ("Sight Words"          to "5–7"),
            ModuleID.ARTICLES_A_AN           to ("Articles A / An"      to "5–7"),
            ModuleID.OPPOSITES_WORD          to ("Opposite Words"       to "5–7"),
            ModuleID.SINGULAR_PLURAL         to ("Singular & Plural"    to "5–7"),
            ModuleID.LISTEN_AND_SELECT       to ("Listen & Select"      to "5–7"),
            ModuleID.MATCH_WORD_WITH_PICTURE to ("Match Word & Image"   to "5–7"),
        )

        private val moduleRoutes = mapOf(
            ModuleID.MATCH_UPPER_LOWER       to RouteNavigation.MatchLetters.route,
            ModuleID.MATCH_LETTER_WITH_IMAGE to RouteNavigation.MatchLetterWithImage.route,
            ModuleID.ALPHABET_TRACING        to RouteNavigation.AlphabetTracing.route,
            ModuleID.LETTER_RECOGNITION      to RouteNavigation.LetterRecognition.route,
            ModuleID.COLORING_ALPHABETS      to RouteNavigation.ColoringAlphabets.route,
            ModuleID.ABCD_WITH_IMAGES        to RouteNavigation.ABCDWithImages.route,
            ModuleID.FILL_THE_BLANK_LETTER   to RouteNavigation.FillTheBlankLetters.route,
            ModuleID.ARRANGE_LETTER_SEQUENCE to RouteNavigation.ArrangeLetterInSequence.route,
            ModuleID.DRAG_DROP_LETTERS       to RouteNavigation.DragDropWord.route,
            ModuleID.SIGHT_WORDS             to RouteNavigation.SightWords.route,
            ModuleID.ARTICLES_A_AN           to RouteNavigation.ArticlesAAn.route,
            ModuleID.OPPOSITES_WORD          to RouteNavigation.OppositeWords.route,
            ModuleID.SINGULAR_PLURAL         to RouteNavigation.SingularPlural.route,
            ModuleID.LISTEN_AND_SELECT       to RouteNavigation.ListenAndSelectWord.route,
            ModuleID.MATCH_WORD_WITH_PICTURE to RouteNavigation.WordMatchImage.route,
        )
    }
}
