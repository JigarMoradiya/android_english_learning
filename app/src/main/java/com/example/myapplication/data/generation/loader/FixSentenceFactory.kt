package com.example.myapplication.data.generation.loader

import android.content.Context
import com.example.myapplication.data.model.SentenceLevel

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
        val lessons   = LessonLoader.loadAllUnits(context, SentenceLevel.MEDIUM)
        val allWords  = lessons.flatMap { it.sentences }.flatMap { it.blankableWords }
        val questions = mutableListOf<FixSentenceQuestion>()

        for (lesson in lessons) {
//            for (sentence in lesson.sentences.filter { it.difficulty == 2 }) {
            for (sentence in lesson.sentences) {

                if (sentence.blankableWords.isEmpty()) continue

                // ── 1. Target word (the correct answer) ─────────────────────
                val target      = sentence.blankableWords.randomOrNull() ?: continue
                val correctWord = target.word.lowercase()

                // ── 2. Distractor substituted INTO the sentence ─────────────
                val distractorPool = allWords.filter { it.type != target.type }
                val wrongWord = distractorPool.randomOrNull()?.word?.lowercase() ?: continue

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
                if (!swapped) continue   // correctWord not found in sentence text

                // ── 4. Options: correctWord + 3 distractors of different type ─
                val wrongs = sentence.blankableWords
                    .filter { it.type != target.type }
                    .map { it.word.lowercase() }
                    .distinct()
                    .filter { it != wrongWord }   // don't include the substituted wrong word
                    .shuffled()
                    .toMutableList()

                if (wrongs.size < 3) {
                    val extra = allWords
                        .filter {
                            it.type != target.type &&
                            it.word.lowercase() != correctWord &&
                            it.word.lowercase() != wrongWord &&
                            !wrongs.contains(it.word.lowercase())
                        }
                        .map { it.word.lowercase() }
                        .distinct()
                        .shuffled()
                        .take(3 - wrongs.size)
                    wrongs.addAll(extra)
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
