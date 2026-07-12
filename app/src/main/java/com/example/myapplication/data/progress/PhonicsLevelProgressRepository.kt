package com.example.myapplication.data.progress

import com.example.myapplication.main.age_group.phonics.listen.view_model.PhonicsListenLevelKey
import com.example.myapplication.main.age_group.phonics.listen.view_model.phonicsListenConfigs
import com.example.myapplication.utilities.pref.AppPreferencesHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

/**
 * Permanent per-level phonics reading progress (never pruned).
 * Practice completion + best score per level, keyed by PhonicsListenLevelKey.name.
 * Listen progress counts words actually heard (auto or manual mode).
 *
 * Mirrors iOS PhonicsLevelProgressRepository — storage key "phonics_level_progress_v1".
 */
data class PhonicsLevelProgress(
    val completed: Boolean = false,
    val bestScore: Int = 0,
    val totalQuestions: Int = 0,
    val visited: Boolean = false,
    // Words the kid actually heard in Listen mode. Nullable so old records decode.
    val listenedWords: Set<String>? = null,
)

@Singleton
class PhonicsLevelProgressRepository @Inject constructor(
    private val prefs: AppPreferencesHelper
) {

    private val gson = Gson()
    private val key = "phonics_level_progress_v1"

    private val _progress = MutableStateFlow<Map<String, PhonicsLevelProgress>>(emptyMap())
    val progress: StateFlow<Map<String, PhonicsLevelProgress>> = _progress

    init {
        load()
    }

    companion object {
        /**
         * Levels with no scored practice (learn + listen only) — done = listen finished.
         * Note: unlike iOS, cvcWords is learn-only on Android (no Build/Missing modes).
         */
        val learnOnlyLevels = setOf(
            PhonicsListenLevelKey.letterSounds,
            PhonicsListenLevelKey.shortVowels,
            PhonicsListenLevelKey.blending,
            PhonicsListenLevelKey.cvcWords,
            PhonicsListenLevelKey.shortVowelRules,
            PhonicsListenLevelKey.wordFamilies,
        )

        const val TOTAL_LEVELS = 28
        const val MAX_STARS = TOTAL_LEVELS * 3
    }

    // MARK: - Write

    /** Call when a level's practice quiz finishes. */
    fun recordPractice(level: PhonicsListenLevelKey, score: Int, total: Int) {
        val entry = entry(level)
        // Keep the best run (by percentage) so stars never go down
        val keepNew = percentage(score, total) >= percentage(entry.bestScore, entry.totalQuestions)
        update(level, entry.copy(
            completed = true,
            visited = true,
            bestScore = if (keepNew) score else entry.bestScore,
            totalQuestions = if (keepNew) total else entry.totalQuestions,
        ))
    }

    /** Call each time a Listen word finishes playing (auto or manual mode). */
    fun markListened(level: PhonicsListenLevelKey, word: String) {
        val entry = entry(level)
        val words = entry.listenedWords ?: emptySet()
        val wordKey = word.lowercase()
        if (wordKey in words) return
        update(level, entry.copy(listenedWords = words + wordKey))
    }

    /** Call when the kid opens a level from the journey map. */
    fun markVisited(level: PhonicsListenLevelKey) {
        val entry = entry(level)
        if (entry.visited) return
        update(level, entry.copy(visited = true))
    }

    // MARK: - Read

    fun isDone(level: PhonicsListenLevelKey): Boolean {
        if (entry(level).completed) return true
        if (level in learnOnlyLevels) {
            val (done, total) = listenProgress(level)
            return total > 0 && done >= total
        }
        return false
    }

    /**
     * 0 = not completed · 1 = completed · 2 = ≥70% · 3 = perfect score.
     * Learn-only levels earn all 3 when listen is finished — that's all they offer.
     */
    fun stars(level: PhonicsListenLevelKey): Int {
        val entry = entry(level)
        if (entry.completed) {
            val pct = percentage(entry.bestScore, entry.totalQuestions)
            return when {
                pct >= 100 -> 3
                pct >= 70 -> 2
                else -> 1
            }
        }
        if (level in learnOnlyLevels && isDone(level)) return 3
        return 0
    }

    /** Best practice run, for showing "Best 8/10" on the level card. */
    fun bestResult(level: PhonicsListenLevelKey): Pair<Int, Int>? {
        val entry = entry(level)
        if (!entry.completed || entry.totalQuestions <= 0) return null
        return entry.bestScore to entry.totalQuestions
    }

    /** Started = visited the level, finished practice, or made listen progress. */
    fun isStarted(level: PhonicsListenLevelKey): Boolean {
        val entry = entry(level)
        if (entry.visited || entry.completed) return true
        return listenProgress(level).first > 0
    }

    /** Whether one specific Listen word was already heard. */
    fun isWordListened(level: PhonicsListenLevelKey, word: String): Boolean =
        entry(level).listenedWords?.contains(word.lowercase()) == true

    /** Words actually heard in the level's Listen mode → (done, total). */
    fun listenProgress(level: PhonicsListenLevelKey): Pair<Int, Int> {
        val total = phonicsListenConfigs[level]?.words?.size ?: 0
        val done = minOf(entry(level).listenedWords?.size ?: 0, total)
        return done to total
    }

    val doneCount: Int
        get() = PhonicsListenLevelKey.entries.count { isDone(it) }

    val totalStars: Int
        get() = PhonicsListenLevelKey.entries.sumOf { stars(it) }

    // MARK: - Internal

    private fun entry(level: PhonicsListenLevelKey): PhonicsLevelProgress =
        _progress.value[level.name] ?: PhonicsLevelProgress()

    private fun update(level: PhonicsListenLevelKey, entry: PhonicsLevelProgress) {
        _progress.value = _progress.value + (level.name to entry)
        save()
    }

    private fun percentage(score: Int, total: Int): Int {
        if (total <= 0) return 0
        return ((score.toDouble() / total) * 100).roundToInt()
    }

    private fun save() {
        prefs.setCustomParam(key, gson.toJson(_progress.value))
    }

    private fun load() {
        val json = prefs.getCustomParam(key, "")
        if (json.isNotEmpty()) {
            val type = object : TypeToken<Map<String, PhonicsLevelProgress>>() {}.type
            _progress.value = gson.fromJson(json, type) ?: emptyMap()
        }
    }
}
