package com.example.myapplication.main.age_group.phonics.worksheets

import com.example.myapplication.data.generation.letter.LetterRepository
import kotlin.math.ceil

/**
 * The content: which worksheets each level shows. Through Unit 2 this is the whole of
 * Level 1 (Letter Sounds) — Learn letter pages, two Reading alphabet sheets, and the
 * Circle / Matcher / Match-Picture activities. The combined "Letter Practice" mixed
 * sheet and levels 2–28 follow in later units. Mirrors the Level-1 slice of iOS
 * `WorksheetCatalog`.
 */
object WorksheetCatalog {

    /** GENERATED from LetterRepository, not hand-listed — add a letter and the level grows. */
    private val alphabet: List<String> = LetterRepository.all.map { it.letter.lowercase() }

    fun worksheets(level: WorksheetLevelKey): List<WorksheetSpec> =
        when (level) {
            WorksheetLevelKey.LETTER_SOUNDS -> level1LetterSounds()
        }

    private fun level1LetterSounds(): List<WorksheetSpec> {
        val out = mutableListOf<WorksheetSpec>()

        // 26 Learn sheets — one letter each.
        alphabet.forEach { letter ->
            val upper = letter.uppercase()
            out += WorksheetSpec(
                id = "l1-learn-$letter",
                level = WorksheetLevelKey.LETTER_SOUNDS,
                kind = WorksheetKind.LEARN,
                title = "The Letter $upper$letter",
                subtitle = "$upper$letter says “${WorksheetLetters.sound(letter)}”.",
                instruction = "Say the sound. Name each picture and hear “${WorksheetLetters.sound(letter)}” at the start.",
                body = WorksheetBody.LetterPage(letter = letter),
                showsNameDate = false
            )
        }

        // Reading — the whole alphabet on one page, then again with the SOUND printed.
        out += WorksheetSpec(
            id = "l1-reading-1",
            level = WorksheetLevelKey.LETTER_SOUNDS,
            kind = WorksheetKind.READING,
            title = "Read the Alphabet",
            subtitle = "Turning a letter into a sound is reading. This is where it starts.",
            instruction = "Say each letter’s sound, then name its picture.",
            body = WorksheetBody.LetterRead(letters = alphabet, columns = 6)
        )
        out += WorksheetSpec(
            id = "l1-reading-2",
            level = WorksheetLevelKey.LETTER_SOUNDS,
            kind = WorksheetKind.READING,
            title = "Alphabet Sounds",
            subtitle = "Every letter, its picture word, and the sound it says.",
            instruction = "Point to each letter. Say its sound, then name the picture.",
            body = WorksheetBody.LetterRead(letters = alphabet, columns = 6, showsSound = true)
        )

        // Activities — a different kind of thinking on each.
        out += circleSheets()
        out += matcherSheets()
        out += matchImageSheets()

        return out
    }

    // ── Activity · circle the first letter ──────────────────────────────────────

    private fun circleSheets(): List<WorksheetSpec> {
        val pool = mutableListOf<Pair<String, String>>() // (word, letter)
        alphabet.forEach { letter ->
            WorksheetLetters.pictureWords(letter, limit = 8).forEach { pic ->
                // Only if the word really starts with its letter — otherwise the
                // choices would have no right answer.
                if (pic.word.lowercase().startsWith(letter)) pool += pic.word to letter
            }
        }
        val shuffled = pool.shuffledSeeded(SeededGenerator(21UL))

        val perSheet = 10
        val out = mutableListOf<WorksheetSpec>()
        var idx = 0
        var start = 0
        while (start <= shuffled.size - perSheet) {
            val slice = shuffled.subList(start, minOf(start + perSheet, shuffled.size))
            out += WorksheetSpec(
                id = "l1-activity-circle-${idx + 1}",
                level = WorksheetLevelKey.LETTER_SOUNDS,
                kind = WorksheetKind.ACTIVITY,
                title = "Circle the First Letter",
                subtitle = "Name the picture, then find the letter it starts with.",
                instruction = "Say the picture’s name. Circle the letter it begins with.",
                body = WorksheetBody.CircleLetter(choices(slice, seed = idx.toULong() * 31UL + 7UL))
            )
            idx++
            start += perSheet
        }
        return out
    }

    /** Three letters per picture: the right one plus two others. */
    private fun choices(items: List<Pair<String, String>>, seed: ULong): List<CircleItem> {
        val rng = SeededGenerator(seed)
        return items.map { (word, letter) ->
            val others = alphabet.filter { it != letter }.shuffledSeeded(rng).take(2)
            CircleItem(word = word, choices = (listOf(letter) + others).shuffledSeeded(rng))
        }
    }

    // ── Activity · Letter Matcher (uppercase ↔ lowercase) ───────────────────────

    private fun matcherSheets(): List<WorksheetSpec> {
        val perSheet = 12
        val shuffledAlpha = alphabet.shuffledSeeded(SeededGenerator(91UL))
        val sheetCount = ceil(shuffledAlpha.size / perSheet.toDouble()).toInt()

        val out = mutableListOf<WorksheetSpec>()
        for (i in 0 until sheetCount) {
            // 26 does not divide by 12, so the last sheet wraps back to the start.
            val group = (0 until perSheet).map { k -> shuffledAlpha[(i * perSheet + k) % shuffledAlpha.size] }
            out += WorksheetSpec(
                id = "l1-activity-matcher-${i + 1}",
                level = WorksheetLevelKey.LETTER_SOUNDS,
                kind = WorksheetKind.ACTIVITY,
                title = "Letter Matcher",
                subtitle = "Every letter has two shapes and one sound.",
                instruction = "Draw a line from each uppercase letter to its lowercase letter.",
                body = WorksheetBody.LetterMatcher(group)
            )
        }
        // The confusable shapes, ordered so each tricky PAIR lands in the same block.
        out += WorksheetSpec(
            id = "l1-activity-matcher-tricky",
            level = WorksheetLevelKey.LETTER_SOUNDS,
            kind = WorksheetKind.ACTIVITY,
            title = "Letter Matcher – Tricky Ones",
            subtitle = "These shapes are the easiest to mix up.",
            instruction = "Draw a line from each uppercase letter to its lowercase letter.",
            body = WorksheetBody.LetterMatcher(
                listOf("b", "d", "p", "q", "i", "j", "m", "w", "n", "u", "g", "y")
            )
        )
        return out
    }

    // ── Activity · join the letter to its picture ───────────────────────────────

    private fun matchImageSheets(): List<WorksheetSpec> {
        val shuffledAlpha = alphabet.shuffledSeeded(SeededGenerator(47UL))
        val perSheet = 12
        val sheetCount = 4
        val seen = HashMap<String, Int>()
        var cursor = 0

        return (0 until sheetCount).map { i ->
            val items = mutableListOf<MatchImageItem>()
            var attempts = 0
            while (items.size < perSheet && attempts < shuffledAlpha.size * 2) {
                attempts++
                val letter = shuffledAlpha[cursor % shuffledAlpha.size]
                cursor++
                val pics = WorksheetLetters.pictureWords(letter, limit = 8)
                if (pics.isEmpty()) continue
                // Each pass over the alphabet takes that letter's NEXT picture.
                val use = (seen[letter] ?: 0) % pics.size
                seen[letter] = (seen[letter] ?: 0) + 1
                items += MatchImageItem(letter = letter, imageRes = pics[use].imageRes)
            }
            WorksheetSpec(
                id = "l1-activity-match-image-${i + 1}",
                level = WorksheetLevelKey.LETTER_SOUNDS,
                kind = WorksheetKind.ACTIVITY,
                title = "Letter and Picture",
                subtitle = "Every letter belongs to a picture.",
                instruction = "Draw a line from each letter to the picture that starts with it.",
                body = WorksheetBody.MatchLetterImage(items)
            )
        }
    }

    /**
     * Each SHEET gets its own colour, cycling the palette by position within its level,
     * so a child flicking through 26 letter sheets sees 26 different pages rather than
     * one colour repeated. Mirrors iOS `WorksheetCatalog.theme`.
     */
    fun theme(spec: WorksheetSpec): WorksheetTheme {
        val all = worksheets(spec.level)
        val index = all.indexOfFirst { it.id == spec.id }.coerceAtLeast(0)
        return WorksheetTheme.forIndex(index)
    }
}
