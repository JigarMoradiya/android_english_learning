package com.example.myapplication.data.generation.loader

import android.content.Context
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.WordType
import java.util.UUID

// ─────────────────────────────────────────────────────────────────────────────
// MARK: - Fill the Blanks models (mirrors iOS FillBlanksQuestion)
// ─────────────────────────────────────────────────────────────────────────────

data class FillBlanksQuestion(
    val id: UUID = UUID.randomUUID(),
    val imageName: String,
    val segments: List<SentenceSegment>,  // sentence split into text + blank slots
    val wordPool: List<FillBlankWord>     // shuffled words to place in blanks
)

/** Mirrors iOS enum SentenceSegment */
sealed class SentenceSegment {
    data class Word(val id: UUID = UUID.randomUUID(), val text: String) : SentenceSegment()
    data class Blank(val slot: BlankSlot) : SentenceSegment()
}

data class BlankSlot(
    val id: UUID = UUID.randomUUID(),
    val correctWord: String,  // lowercased
    val wordType: WordType
)

data class FillBlankWord(
    val id: UUID = UUID.randomUUID(),
    val word: String,
    val type: WordType
)

/** Mirrors iOS FillSlotState */
enum class FillSlotState { EMPTY, CORRECT, WRONG }

// ─────────────────────────────────────────────────────────────────────────────
// MARK: - Factory (mirrors iOS FillBlanksFactory.generateQuestions)
// ─────────────────────────────────────────────────────────────────────────────

object FillBlanksFactory {

    fun generateQuestions(context: Context): List<FillBlanksQuestion> {
        val lessons = LessonLoader.loadAllUnits(context, SentenceLevel.MEDIUM)
        val questions = mutableListOf<FillBlanksQuestion>()

        for (lesson in lessons) {
            for (sentence in lesson.sentences.filter { it.difficulty == 2 }) {
                val blankable = sentence.blankableWords
                // Need at least 2 blanks to be interesting
                if (blankable.size < 2) continue

                // Build lookup: lowercased word → blankable info (first match)
                val blankLookup = blankable.associate { it.word.lowercase() to it }

                val rawWords = sentence.text
                    .replace(".", "")
                    .replace("!", "")
                    .replace("?", "")
                    .split(" ")
                    .filter { it.isNotEmpty() }

                val segments = mutableListOf<SentenceSegment>()
                val usedBlanks = mutableListOf<BlankSlot>()
                val usedKeys = mutableSetOf<String>()

                for (w in rawWords) {
                    val key = w.lowercase()
                    if (blankLookup.containsKey(key) && !usedKeys.contains(key)) {
                        usedKeys.add(key)
                        val b = blankLookup[key]!!
                        val slot = BlankSlot(correctWord = key, wordType = b.type)
                        segments.add(SentenceSegment.Blank(slot))
                        usedBlanks.add(slot)
                    } else {
                        segments.add(SentenceSegment.Word(text = w))
                    }
                }

                if (usedBlanks.size < 2) continue

                val wordPool = usedBlanks
                    .map { FillBlankWord(word = it.correctWord, type = it.wordType) }
                    .shuffled()

                questions.add(
                    FillBlanksQuestion(
                        imageName = lesson.imageName,
                        segments = segments,
                        wordPool = wordPool
                    )
                )
            }
        }

        return questions.shuffled()
    }
}
