package com.example.myapplication.data.generation.loader

import com.example.myapplication.data.model.MatchPictureQuestion
import com.example.myapplication.data.model.ReadSentenceItemNew
import com.example.myapplication.data.model.SentenceBuilderQuestion
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.TrueFalseQuestion

/**
 * Dependency-free pure logic for the Age 6-8 sentence modules (items C.2, 5.2, 3.2).
 * Mirrors iOS `SentenceBuilderLogic.swift`; unit-tested via `SentenceBuilderLogicTest`.
 */
object SentenceBuilderLogic {

    // Shared with the UI: the explanation card bolds + colours whatever follows this prefix.
    const val CORRECT_SENTENCE_PREFIX = "The correct sentence is: "

    // C.2 — "Which Sentence Sounds Right" level-scaled option count (easy 3 / medium 4).
    fun buildSoundOptions(
        correct: String,
        wrongOptions: List<String>,
        level: SentenceLevel
    ): List<String> {
        val requiredCount = if (level == SentenceLevel.EASY) 3 else 4
        val options = mutableListOf(correct)
        for (wrong in wrongOptions.shuffled()) {
            if (options.size >= requiredCount) break
            options.add(wrong)
        }
        return options.shuffled()
    }

    // 5.2 — "Build the Sentence" questions sourced from the Sentences pool
    // (short = easy, long = medium); only sentenceBuilder-flagged sentences,
    // each inheriting its lesson's image.
    fun makeBuilderQuestions(
        lessons: List<ReadSentenceItemNew>,
        limit: Int
    ): List<SentenceBuilderQuestion> {
        val questions = mutableListOf<SentenceBuilderQuestion>()
        for (lesson in lessons) {
            for (sentence in lesson.sentences) {
                if (sentence.activities.sentenceBuilder) {
                    questions.add(
                        SentenceBuilderQuestion(
                            id = sentence.id,
                            imageName = lesson.imageName,
                            correctSentence = sentence.text
                        )
                    )
                }
            }
        }
        return questions.shuffled().take(limit)
    }

    // 3.2 — "Sentence Check" True/False with an explanation. For a false statement
    // the explanation surfaces the correct sentence; Phase 4 enriches these with
    // authored fun-fact / knowledge text.
    fun makeTrueFalse(
        item: MatchPictureQuestion,
        useCorrect: Boolean
    ): TrueFalseQuestion {
        return if (useCorrect) {
            TrueFalseQuestion(
                id = item.id + "_T",
                imageName = item.imageName,
                statement = item.correctSentence,
                isTrue = "true",
                explanation = "Correct — this sentence is written the right way."
            )
        } else {
            val wrong = item.wrongOptions.randomOrNull() ?: item.correctSentence
            TrueFalseQuestion(
                id = item.id + "_F",
                imageName = item.imageName,
                statement = wrong,
                isTrue = "false",
                explanation = "$CORRECT_SENTENCE_PREFIX${item.correctSentence}"
            )
        }
    }

    // 1.3 — Echo reading "your turn" repeat gap (seconds), scales with sentence
    // length, never shorter than 2s.
    fun echoGapSeconds(wordCount: Int): Double {
        return maxOf(2.0, wordCount * 0.6)
    }
}
