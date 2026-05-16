package com.example.myapplication.data.generation.loader

import android.content.Context
import com.example.myapplication.data.model.SentenceLevel
import java.util.UUID

// ─────────────────────────────────────────────────────────────────────────────
// MARK: - Sentence Builder (mirrors iOS AdvSentenceBuilderQuestion)
// ─────────────────────────────────────────────────────────────────────────────

data class AdvSentenceBuilderQuestion(
    val id: UUID = UUID.randomUUID(),
    val imageName: String,
    val sentence: String,           // original sentence (shown after answer)
    val correctWords: List<String>, // correct word order
    val shuffledWords: List<AdvBuildWord> // pool — shuffled, each with unique ID
)

data class AdvBuildWord(
    val id: UUID = UUID.randomUUID(),
    val text: String
)

// ─────────────────────────────────────────────────────────────────────────────
// MARK: - Factory (mirrors iOS AdvSentenceBuilderFactory.generateQuestions)
// ─────────────────────────────────────────────────────────────────────────────

object AdvSentenceBuilderFactory {

    fun generateQuestions(context: Context): List<AdvSentenceBuilderQuestion> {
        val lessons = LessonLoader.loadAllUnits(context, SentenceLevel.MEDIUM)
        val questions = mutableListOf<AdvSentenceBuilderQuestion>()

        for (lesson in lessons) {
            for (sentence in lesson.sentences.filter { it.difficulty == 2 }) {
                val raw = sentence.text
                    .replace(".", "")
                    .replace("!", "")
                    .replace("?", "")
                    .split(" ")
                    .filter { it.isNotEmpty() }

                // Need at least 4 words to be interesting
                if (raw.size < 4) continue

                val shuffled = raw.shuffled().map { AdvBuildWord(text = it) }

                questions.add(
                    AdvSentenceBuilderQuestion(
                        imageName = lesson.imageName,
                        sentence = sentence.text,
                        correctWords = raw,
                        shuffledWords = shuffled
                    )
                )
            }
        }

        return questions.shuffled()
    }
}
