package com.example.myapplication.data.generation.loader

import android.content.Context
import com.example.myapplication.data.model.BlankableWord
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.WordType

/**
 * Mirrors iOS FixSentenceFactory.generateQuestions()
 *
 * For each difficulty-2 sentence in all medium lessons:
 *  1. Pick a random blankable word as the TARGET (correct answer)
 *  2. Pick a random word of a DIFFERENT type as the WRONG substitute shown in the sentence
 *  3. Swap the correct word with the wrong word in the display chips
 *  4. Build options = [correctWord] + up to 3 distractors (different type, ≠ wrong word)
 */
object FixSentenceFactory {

    fun generateQuestions(context: Context): List<FixSentenceQuestion> {
        val lessons  = LessonLoader.loadAllUnits(context, SentenceLevel.MEDIUM)
        val allWords = lessons.flatMap { it.sentences }.flatMap { it.blankableWords }

        // Pre-group by excluded type so distractor lookup is O(1) per sentence
        // instead of filtering allWords (O(n)) on every iteration.
        val distractorPool: Map<WordType, List<BlankableWord>> = WordType.entries.associateWith { excluded ->
            allWords.filter { it.type != excluded }
        }

        val questions = mutableListOf<FixSentenceQuestion>()

        for (lesson in lessons) {
            for (sentence in lesson.sentences) {
                if (sentence.blankableWords.isEmpty()) continue

                // ── 1. Target word (the correct answer) ─────────────────────
                val target      = sentence.blankableWords.randomOrNull() ?: continue
                val correctWord = target.word.lowercase()

                // ── 2. Distractor substituted INTO the sentence ─────────────
                val pool      = distractorPool[target.type] ?: continue
                val wrongWord = pool.randomOrNull()?.word?.lowercase() ?: continue

                // ── 3. Build display chips ───────────────────────────────────
                val rawWords = sentence.text
                    .replace(".", "")
                    .replace("!", "")
                    .replace("?", "")
                    .split(" ")
                    .filter { it.isNotEmpty() }

                var swapped = false
                val displayWords: List<FixWord> = rawWords.map { w ->
                    if (!swapped && w.lowercase() == correctWord) {
                        swapped = true
                        FixWord(text = wrongWord, isWrong = true)
                    } else {
                        FixWord(text = w, isWrong = false)
                    }
                }
                if (!swapped) continue

                // ── 4. Options: correctWord + 3 distractors of different type ─
                val seen   = mutableSetOf(correctWord, wrongWord)
                val wrongs = mutableListOf<String>()

                for (bw in sentence.blankableWords.shuffled()) {
                    if (bw.type == target.type) continue
                    val w = bw.word.lowercase()
                    if (seen.add(w)) { wrongs += w }
                    if (wrongs.size == 3) break
                }

                if (wrongs.size < 3) {
                    var attempts = 0
                    while (wrongs.size < 3 && attempts < pool.size) {
                        val w = pool[pool.indices.random()].word.lowercase()
                        if (seen.add(w)) wrongs += w
                        attempts++
                    }
                }

                val options = (listOf(correctWord) + wrongs.take(3)).shuffled()

                questions.add(
                    FixSentenceQuestion(
                        imageName    = lesson.imageName,
                        displayWords = displayWords,
                        correctWord  = correctWord,
                        options      = options,
                        targetType   = target.type
                    )
                )
            }
        }

        return questions.shuffled()
    }
}
