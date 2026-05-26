package com.example.myapplication.main.parent

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.progress.LearningSession
import com.example.myapplication.data.progress.SessionRepository
import com.example.myapplication.main.base.nav.RouteNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

data class WeakLetterEntry(
    val label: String,
    val subLabel: String?,
    val letters: List<Char>
)

data class WeakArrangeEntry(
    val label: String,
    val subLabel: String?,
    val sequences: List<String>
)

data class ModuleProgressRow(
    val moduleId: String,
    val displayName: String,
    val subLabel: String? = null,   // e.g. "Before Letter · ABC" for Fill the Blank sub-rows
    val ageGroupLabel: String,
    val rounds: Int,
    val avgAccuracy: Double,        // 0.0–1.0
    val avgStars: Double,           // 0.0–3.0
    val hasQuiz: Boolean = false,   // true = show stars + accuracy bar even when value is 0
    val route: String?,             // RouteNavigation route string, null = not tappable
    val scoreText: String? = null   // e.g. "21/30 ×3" for Fill the Blank sub-rows
)

@HiltViewModel
class ParentProgressViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    var weekOffset by mutableStateOf(0)
        private set

    // null = whole week; 0–6 = Mon–Sun filter
    var selectedDayIndex by mutableStateOf<Int?>(null)
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
    // Unified weak-letter rows sorted by most-recently-played first
    var weakLetterRows by mutableStateOf(emptyList<WeakLetterEntry>())
        private set
    var weakArrangeRows by mutableStateOf(emptyList<WeakArrangeEntry>())
        private set
    var masteredLetterRows by mutableStateOf(emptyList<WeakLetterEntry>())
        private set
    var masteredSequenceRows by mutableStateOf(emptyList<WeakArrangeEntry>())
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

    fun selectDay(index: Int) {
        selectedDayIndex = if (selectedDayIndex == index) null else index
        reloadStats()
    }

    fun goToPreviousWeek() {
        if (!canGoBack) return
        weekOffset--
        selectedDayIndex = null
        reload()
    }

    fun goToNextWeek() {
        if (!canGoForward) return
        weekOffset++
        selectedDayIndex = null
        reload()
    }

    // MARK: - Load

    fun load() {
        weekOffset = 0
        selectedDayIndex = null
        reload()
    }

    private var cachedWeekSessions: List<LearningSession> = emptyList()

    private fun reload() {
        val allSessions = sessionRepository.allSessions()
        val weekStart = weekStartMs(weekOffset)
        val weekEnd = weekStart + 7L * 24 * 60 * 60 * 1000
        cachedWeekSessions = allSessions.filter { it.timestampMs in weekStart until weekEnd }
        computeStreak(allSessions)
        computeActiveDays(cachedWeekSessions, weekStart)
        reloadStats()
    }

    // Re-filters stats/rows when day selection changes without re-fetching sessions
    private fun reloadStats() {
        val dayMs = 24L * 60 * 60 * 1000
        val weekStart = weekStartMs(weekOffset)
        val sessions = if (selectedDayIndex == null) {
            cachedWeekSessions
        } else {
            val dayStart = weekStart + selectedDayIndex!! * dayMs
            cachedWeekSessions.filter { it.timestampMs in dayStart until dayStart + dayMs }
        }

        weeklySessionCount = sessions.size
        weeklyDurationSeconds = sessions.sumOf { it.durationSeconds }
        val quizSessions = sessions.filter { it.totalQuestions > 0 }
        weeklyAccuracy = if (quizSessions.isEmpty()) 0.0
        else quizSessions.sumOf { it.accuracy } / quizSessions.size

        buildModuleRows(sessions)
        loadWeakLetters(sessions)
        loadWeakArrange(sessions)
        loadMasteredLetters(sessions)
        loadMasteredSequences(sessions)
    }

    // MARK: - Helpers

    private fun weekStartMs(offset: Int): Long {
        return sessionRepository.mondayOfWeek(offset)
    }

    private fun computeStreak(allSessions: List<LearningSession>) {
        val daysWithSessions = allSessions.map { session ->
            val c = Calendar.getInstance()
            c.timeInMillis = session.timestampMs
            c.set(Calendar.HOUR_OF_DAY, 0); c.set(Calendar.MINUTE, 0)
            c.set(Calendar.SECOND, 0); c.set(Calendar.MILLISECOND, 0)
            c.timeInMillis
        }.toSet()

        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }
        val yesterday = (today.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -1) }

        // Start from today if played today, otherwise from yesterday.
        // This keeps the streak alive on days when the user hasn't played yet.
        val startCal = when {
            daysWithSessions.contains(today.timeInMillis)     -> today.clone() as Calendar
            daysWithSessions.contains(yesterday.timeInMillis) -> yesterday.clone() as Calendar
            else -> {
                currentStreak = 0
                bestStreak = computeBestStreak(daysWithSessions)
                return
            }
        }

        var streak = 0
        val cursor = startCal
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
        // (row, latestTimestampMs) — sorted by latestTimestampMs descending at end
        val rowsWithTime = mutableListOf<Pair<ModuleProgressRow, Long>>()

        grouped.forEach { (moduleId, sessions) ->
            val info = moduleInfo[moduleId] ?: return@forEach

            if (moduleId == ModuleID.ARRANGE_LETTER_SEQUENCE) {
                val byConfig = sessions.groupBy { it.subConfig ?: "UPPERCASE" }
                byConfig.forEach { (config, configSessions) ->
                    val latest = configSessions.maxOf { it.timestampMs }
                    val quizSessions = configSessions.filter { it.totalQuestions > 0 }
                    val totalScore = quizSessions.sumOf { it.score }
                    val totalQs    = quizSessions.sumOf { it.totalQuestions }
                    val avgAcc = if (totalQs > 0) totalScore.toDouble() / totalQs else 0.0
                    val subLabel = if (config == "UPPERCASE") "ABC" else "abc"
                    val modeRoute = RouteNavigation.ArrangeLetterInSequence.createRoute(config)
                    val scoreStr = if (totalQs > 0) "$totalScore/$totalQs" else null
                    rowsWithTime.add(ModuleProgressRow(
                        moduleId = "$moduleId|$config",
                        displayName = info.first,
                        subLabel = subLabel,
                        ageGroupLabel = info.second,
                        rounds = configSessions.size,
                        avgAccuracy = avgAcc,
                        avgStars = if (totalQs > 0) minOf(3.0, avgAcc * 3.0) else 0.0,
                        hasQuiz = quizSessions.isNotEmpty(),
                        route = modeRoute,
                        scoreText = scoreStr
                    ) to latest)
                }
            } else if (moduleId == ModuleID.FILL_THE_BLANK_LETTER) {
                val byConfig = sessions.groupBy { it.subConfig ?: "?" }
                byConfig.forEach { (config, configSessions) ->
                    val latest = configSessions.maxOf { it.timestampMs }
                    val quizSessions = configSessions.filter { it.totalQuestions > 0 }
                    val totalScore = quizSessions.sumOf { it.score }
                    val totalQs    = quizSessions.sumOf { it.totalQuestions }
                    val avgAcc = if (totalQs > 0) totalScore.toDouble() / totalQs else 0.0
                    val sessionCount = quizSessions.size
                    val scoreStr = if (totalQs > 0)
                        "$totalScore/$totalQs${if (sessionCount > 1) " ×$sessionCount" else ""}"
                    else null
                    rowsWithTime.add(ModuleProgressRow(
                        moduleId = "$moduleId|$config",
                        displayName = "Fill the Blank",
                        subLabel = fillBlankSubLabel(config),
                        ageGroupLabel = info.second,
                        rounds = configSessions.size,
                        avgAccuracy = avgAcc,
                        avgStars = if (totalQs > 0) minOf(3.0, avgAcc * 3.0) else 0.0,
                        hasQuiz = totalQs > 0,
                        route = moduleRoutes[moduleId],
                        scoreText = scoreStr
                    ) to latest)
                }
            } else {
                val latest = sessions.maxOf { it.timestampMs }
                val quizSessions = sessions.filter { it.totalQuestions > 0 }
                val totalScore = quizSessions.sumOf { it.score }
                val totalQs    = quizSessions.sumOf { it.totalQuestions }
                val avgAcc = if (totalQs > 0) totalScore.toDouble() / totalQs else 0.0
                val stars = if (totalQs > 0) minOf(3.0, avgAcc * 3.0) else 0.0
                val scoreModules = setOf(ModuleID.MISSING_LETTER, ModuleID.DRAG_DROP_LETTERS, ModuleID.MISSING_LETTER_57, ModuleID.WORD_JIGSAW)
                val scoreStr = if (moduleId in scoreModules && totalQs > 0) "$totalScore/$totalQs" else null
                rowsWithTime.add(ModuleProgressRow(
                    moduleId = moduleId,
                    displayName = info.first,
                    ageGroupLabel = info.second,
                    rounds = sessions.size,
                    avgAccuracy = avgAcc,
                    avgStars = stars,
                    hasQuiz = totalQs > 0,
                    route = moduleRoutes[moduleId],
                    scoreText = scoreStr
                ) to latest)
            }
        }
        moduleRows = rowsWithTime.sortedByDescending { it.second }.map { it.first }
    }

    private fun fillBlankSubLabel(config: String): String {
        val parts = config.split("|")
        val posLabel = when (parts.getOrNull(0)) {
            "BEFORE"  -> "Before Letter"
            "BETWEEN" -> "Between Letter"
            "AFTER"   -> "After Letter"
            "RANDOM"  -> "Surprise!"
            else      -> parts.getOrNull(0) ?: config
        }
        val modeLabel = when (parts.getOrNull(1)) {
            "UPPERCASE" -> "ABC"
            "LOWERCASE" -> "abc"
            else        -> parts.getOrNull(1) ?: ""
        }
        return "$posLabel · $modeLabel"
    }

    private fun loadMasteredLetters(sessions: List<LearningSession>) {
        data class Entry(val label: String, val subLabel: String?, val letters: List<Char>, val latestMs: Long)
        val entries = mutableListOf<Entry>()

        fun addIfNeeded(moduleId: String, label: String) {
            val matching = sessions.filter { it.moduleId == moduleId }
            val chars = matching.flatMap { it.correctItems.orEmpty() }
                .mapNotNull { it.firstOrNull() }.distinct().sorted()
            if (chars.isEmpty()) return
            entries.add(Entry(label, null, chars, matching.maxOf { it.timestampMs }))
        }

        addIfNeeded(ModuleID.MATCH_UPPER_LOWER,       "Match Letters")
        addIfNeeded(ModuleID.MATCH_LETTER_WITH_IMAGE, "Match with Image")

        val fbSessions = sessions.filter { it.moduleId == ModuleID.FILL_THE_BLANK_LETTER }
        fbSessions.groupBy { it.subConfig ?: "?" }.forEach { (config, cfgSessions) ->
            val chars = cfgSessions.flatMap { it.correctItems.orEmpty() }
                .mapNotNull { it.firstOrNull() }.distinct().sorted()
            if (chars.isEmpty()) return@forEach
            entries.add(Entry("Fill the Blank", fillBlankSubLabel(config), chars, cfgSessions.maxOf { it.timestampMs }))
        }

        masteredLetterRows = entries.sortedByDescending { it.latestMs }
            .map { WeakLetterEntry(it.label, it.subLabel, it.letters) }
    }

    private fun loadMasteredSequences(sessions: List<LearningSession>) {
        data class Entry(val label: String, val subLabel: String?, val sequences: List<String>, val latestMs: Long)
        val result = mutableListOf<Entry>()

        sessions.filter { it.moduleId == ModuleID.ARRANGE_LETTER_SEQUENCE }
            .groupBy { it.subConfig ?: "UPPERCASE" }
            .forEach { (config, cfgSessions) ->
                val sequences = cfgSessions.flatMap { it.correctItems.orEmpty() }
                    .filter { it.isNotEmpty() }.distinct().sorted()
                if (sequences.isNotEmpty()) {
                    result.add(Entry(
                        label = "Arrange in Sequence",
                        subLabel = if (config == "UPPERCASE") "ABC" else "abc",
                        sequences = sequences,
                        latestMs = cfgSessions.maxOf { it.timestampMs }
                    ))
                }
            }

        val ml35Sessions = sessions.filter { it.moduleId == ModuleID.MISSING_LETTER }
        val ml35Words = ml35Sessions.flatMap { it.correctItems.orEmpty() }.filter { it.isNotEmpty() }.distinct().sorted()
        if (ml35Words.isNotEmpty()) {
            result.add(Entry(label = "Missing Letter", subLabel = "Age 3-5", sequences = ml35Words, latestMs = ml35Sessions.maxOf { it.timestampMs }))
        }

        val ml57Sessions = sessions.filter { it.moduleId == ModuleID.MISSING_LETTER_57 }
        val ml57Words = ml57Sessions.flatMap { it.correctItems.orEmpty() }.filter { it.isNotEmpty() }.distinct().sorted()
        if (ml57Words.isNotEmpty()) {
            result.add(Entry(label = "Missing Letter", subLabel = "Age 5-7", sequences = ml57Words, latestMs = ml57Sessions.maxOf { it.timestampMs }))
        }

        val dd35Sessions = sessions.filter { it.moduleId == ModuleID.DRAG_DROP_LETTERS }
        val dd35Words = dd35Sessions.flatMap { it.correctItems.orEmpty() }.filter { it.isNotEmpty() }.distinct().sorted()
        if (dd35Words.isNotEmpty()) {
            result.add(Entry(label = "Drag & Drop", subLabel = "Age 3-5", sequences = dd35Words, latestMs = dd35Sessions.maxOf { it.timestampMs }))
        }

        val wj57Sessions = sessions.filter { it.moduleId == ModuleID.WORD_JIGSAW }
        val wj57Words = wj57Sessions.flatMap { it.correctItems.orEmpty() }.filter { it.isNotEmpty() }.distinct().sorted()
        if (wj57Words.isNotEmpty()) {
            result.add(Entry(label = "Word Jigsaw", subLabel = "Age 5-7", sequences = wj57Words, latestMs = wj57Sessions.maxOf { it.timestampMs }))
        }

        masteredSequenceRows = result.sortedByDescending { it.latestMs }
            .map { WeakArrangeEntry(it.label, it.subLabel, it.sequences) }
    }

    private fun loadWeakLetters(sessions: List<LearningSession>) {
        data class Entry(val label: String, val subLabel: String?, val letters: List<Char>, val latestMs: Long)
        val entries = mutableListOf<Entry>()

        fun addIfNeeded(moduleId: String, label: String) {
            val matching = sessions.filter { it.moduleId == moduleId }
            val chars = matching.flatMap { it.wrongItems.orEmpty() }
                .mapNotNull { it.firstOrNull() }.distinct().sorted()
            if (chars.isEmpty()) return
            entries.add(Entry(label, null, chars, matching.maxOf { it.timestampMs }))
        }

        addIfNeeded(ModuleID.MATCH_UPPER_LOWER,       "Match Letters")
        addIfNeeded(ModuleID.MATCH_LETTER_WITH_IMAGE, "Match with Image")

        val fbSessions = sessions.filter { it.moduleId == ModuleID.FILL_THE_BLANK_LETTER }
        fbSessions.groupBy { it.subConfig ?: "?" }.forEach { (config, cfgSessions) ->
            val chars = cfgSessions.flatMap { it.wrongItems.orEmpty() }
                .mapNotNull { it.firstOrNull() }.distinct().sorted()
            if (chars.isEmpty()) return@forEach
            entries.add(Entry("Fill the Blank", fillBlankSubLabel(config), chars, cfgSessions.maxOf { it.timestampMs }))
        }

        weakLetterRows = entries.sortedByDescending { it.latestMs }
            .map { WeakLetterEntry(it.label, it.subLabel, it.letters) }
    }

    private fun loadWeakArrange(sessions: List<LearningSession>) {
        data class Entry(val label: String, val subLabel: String?, val sequences: List<String>, val latestMs: Long)
        val result = mutableListOf<Entry>()

        sessions.filter { it.moduleId == ModuleID.ARRANGE_LETTER_SEQUENCE }
            .groupBy { it.subConfig ?: "UPPERCASE" }
            .forEach { (config, cfgSessions) ->
                val sequences = cfgSessions.flatMap { it.wrongItems.orEmpty() }
                    .filter { it.isNotEmpty() }.distinct().sorted()
                if (sequences.isNotEmpty()) {
                    result.add(Entry(
                        label = "Arrange in Sequence",
                        subLabel = if (config == "UPPERCASE") "ABC" else "abc",
                        sequences = sequences,
                        latestMs = cfgSessions.maxOf { it.timestampMs }
                    ))
                }
            }

        val ml35Sessions = sessions.filter { it.moduleId == ModuleID.MISSING_LETTER }
        val ml35Words = ml35Sessions.flatMap { it.wrongItems.orEmpty() }.filter { it.isNotEmpty() }.distinct().sorted()
        if (ml35Words.isNotEmpty()) {
            result.add(Entry(label = "Missing Letter", subLabel = "Age 3-5", sequences = ml35Words, latestMs = ml35Sessions.maxOf { it.timestampMs }))
        }

        val ml57Sessions = sessions.filter { it.moduleId == ModuleID.MISSING_LETTER_57 }
        val ml57Words = ml57Sessions.flatMap { it.wrongItems.orEmpty() }.filter { it.isNotEmpty() }.distinct().sorted()
        if (ml57Words.isNotEmpty()) {
            result.add(Entry(label = "Missing Letter", subLabel = "Age 5-7", sequences = ml57Words, latestMs = ml57Sessions.maxOf { it.timestampMs }))
        }

        val dd35Sessions = sessions.filter { it.moduleId == ModuleID.DRAG_DROP_LETTERS }
        val dd35Words = dd35Sessions.flatMap { it.wrongItems.orEmpty() }.filter { it.isNotEmpty() }.distinct().sorted()
        if (dd35Words.isNotEmpty()) {
            result.add(Entry(label = "Drag & Drop", subLabel = "Age 3-5", sequences = dd35Words, latestMs = dd35Sessions.maxOf { it.timestampMs }))
        }

        val wj57Sessions = sessions.filter { it.moduleId == ModuleID.WORD_JIGSAW }
        val wj57Words = wj57Sessions.flatMap { it.wrongItems.orEmpty() }.filter { it.isNotEmpty() }.distinct().sorted()
        if (wj57Words.isNotEmpty()) {
            result.add(Entry(label = "Word Jigsaw", subLabel = "Age 5-7", sequences = wj57Words, latestMs = wj57Sessions.maxOf { it.timestampMs }))
        }

        weakArrangeRows = result.sortedByDescending { it.latestMs }
            .map { WeakArrangeEntry(it.label, it.subLabel, it.sequences) }
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
            ModuleID.MATCH_UPPER_LOWER       to ("Match Letters"        to "3–5"),
            ModuleID.MATCH_LETTER_WITH_IMAGE to ("Match with Image"     to "3–5"),
            ModuleID.FILL_THE_BLANK_LETTER   to ("Fill the Blank"       to "3–5"),
            ModuleID.ARRANGE_LETTER_SEQUENCE to ("Arrange in Sequence"  to "3–5"),
            ModuleID.DRAG_DROP_LETTERS       to ("Drag & Drop Word"     to "3–5"),
            ModuleID.MISSING_LETTER          to ("Missing Letter"       to "3–5"),
            ModuleID.MISSING_LETTER_57       to ("Missing Letter"       to "5–7"),
            ModuleID.WORD_JIGSAW             to ("Word Jigsaw"          to "5–7"),
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
            // ARRANGE_LETTER_SEQUENCE handled inline in buildModuleRows (mode-specific routes)
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
