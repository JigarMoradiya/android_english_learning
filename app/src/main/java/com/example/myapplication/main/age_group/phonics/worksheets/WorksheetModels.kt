package com.example.myapplication.main.age_group.phonics.worksheets

import androidx.compose.ui.graphics.Color

/**
 * The four things a worksheet can ask a child to do. Mirrors iOS `WorksheetKind`,
 * and the app's own vocabulary — every level already has a Learn and a Practice
 * screen — so a parent does not learn a second set of words for the paper version.
 */
enum class WorksheetKind(val title: String, val tint: Color) {
    LEARN("Learn", Color(0xFF2E7D32)),
    READING("Reading", Color(0xFF1565C0)),
    PRACTICE("Practice", Color(0xFFC2185B)),
    ACTIVITY("Activity", Color(0xFFEF6C00))
}

/**
 * Which phonics level a sheet belongs to. Unit 1 ships only Level 1 (Letter Sounds);
 * the rest are added as later units port their bodies. `raw` matches the iOS
 * `PhonicsListenLevelKey` string so navigation keys stay identical across platforms.
 */
enum class WorksheetLevelKey(
    val raw: String,
    val levelNumber: Int,
    val title: String,
    val accent: Color
) {
    LETTER_SOUNDS("letterSounds", 1, "Letter Sounds", Color(0xFFE65100));

    companion object {
        fun from(raw: String?): WorksheetLevelKey =
            entries.firstOrNull { it.raw == raw } ?: LETTER_SOUNDS
    }
}

/**
 * What the middle of the page actually asks for. Only the Level-1 letter page is
 * ported in Unit 1; the full ~30-case set (pattern/sentences/trace/sort/…) arrives
 * with later units.
 */
sealed class WorksheetBody {
    /** Learn — one letter: the letter, its sound, and its picture words. */
    data class LetterPage(val letter: String) : WorksheetBody()

    /** Reading — the alphabet as Aa pairs, each with its word and picture. */
    data class LetterRead(
        val letters: List<String>,
        val columns: Int,
        val showsSound: Boolean = false
    ) : WorksheetBody()

    /** Activity — a picture and three letters; circle the one it starts with. */
    data class CircleLetter(val items: List<CircleItem>) : WorksheetBody()

    /** Activity — join each letter to the picture that starts with it. */
    data class MatchLetterImage(val items: List<MatchImageItem>) : WorksheetBody()

    /** Activity — Letter Matcher: join each uppercase letter to its lowercase. */
    data class LetterMatcher(val pairs: List<String>) : WorksheetBody()
}

/** One row of the circle-the-letter activity: a picture word and three letters. */
data class CircleItem(val word: String, val choices: List<String>)

/** One row of the letter→picture match activity. */
data class MatchImageItem(val letter: String, val imageRes: Int)

/** One worksheet page. Minimal port of iOS `WorksheetSpec` for Unit 1. */
data class WorksheetSpec(
    val id: String,
    val level: WorksheetLevelKey,
    val kind: WorksheetKind,
    /** Header title — with [subtitle], the only thing that changes in the header. */
    val title: String,
    val subtitle: String,
    /** The "what to do" line under the rainbow separator. */
    val instruction: String,
    val body: WorksheetBody,
    /** Whether the header carries a NAME / DATE line. Off for say-and-point pages. */
    val showsNameDate: Boolean = true
)
