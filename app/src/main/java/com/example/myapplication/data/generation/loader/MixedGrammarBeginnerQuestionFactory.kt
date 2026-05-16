package com.example.myapplication.data.generation.loader

import android.content.Context
import com.example.myapplication.data.model.BlankableWord
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.WordType

/**
 * Mirrors iOS MixedGrammarBeginnerQuestionFactory.generateQuestions().
 * Builds questions dynamically from LessonLoader.loadAllUnits(context, SentenceLevel.EASY).
 * NEVER uses hardcoded static data.
 */
object MixedGrammarBeginnerQuestionFactory {

    fun generateQuestions(
        context: Context,
        activityType: BeginnerActivityType
    ): List<MixedBeginnerQuestion> {

        val lessons = LessonLoader.loadAllUnits(context, SentenceLevel.EASY)

        // Global word pool for fallback wrong-option generation
        val allWords = lessons
            .flatMap { it.sentences }
            .flatMap { it.blankableWords }

        val questions = mutableListOf<MixedBeginnerQuestion>()

        for (lesson in lessons) {
            for (sentence in lesson.sentences.filter { it.difficulty == 1 }) {

                if (sentence.blankableWords.isEmpty()) continue

                // Group blankable words by type
                val wordsByType: Map<WordType, List<BlankableWord>> =
                    sentence.blankableWords.groupBy { it.type }

                // For TAP_WORD: only pick a type that appears exactly once in the sentence
                // so there is a single unambiguous chip to tap.
                // For MULTIPLE_CHOICE: any type is fine.
                val eligibleTypes: List<WordType> = if (activityType == BeginnerActivityType.TAP_WORD) {
                    wordsByType.filter { it.value.size == 1 }.keys.toList()
                } else {
                    wordsByType.keys.toList()
                }

                if (eligibleTypes.isEmpty()) continue

                val targetType = eligibleTypes.random()
                val targetWords = wordsByType[targetType] ?: continue
                val correctBlankable = targetWords.random()
                val correctWord = correctBlankable.word.lowercase()
                val allCorrectWords = targetWords.map { it.word.lowercase() }

                // Wrong options: prefer words from different type in same sentence
                var wrongOptions = sentence.blankableWords
                    .filter { it.type != targetType }
                    .map { it.word.lowercase() }
                    .distinct()
                    .shuffled()
                    .toMutableList()

                // Fill remaining slots from global pool if needed
                if (wrongOptions.size < 2) {
                    val extra = allWords
                        .filter {
                            it.type != targetType &&
                            it.word.lowercase() != correctWord &&
                            !wrongOptions.contains(it.word.lowercase())
                        }
                        .map { it.word.lowercase() }
                        .distinct()
                        .shuffled()
                        .take(2 - wrongOptions.size)
                    wrongOptions.addAll(extra)
                }

                val options = (listOf(correctWord) + wrongOptions.take(2)).shuffled()

                // Sentence words stripped of punctuation (for tapWord chips)
                val sentenceWords = sentence.text
                    .replace(".", "")
                    .replace("!", "")
                    .replace("?", "")
                    .replace(",", "")
                    .split(" ")
                    .filter { it.isNotEmpty() }

                questions.add(
                    MixedBeginnerQuestion(
                        sentence = sentence.text,
                        imageName = lesson.imageName,
                        targetType = targetType,
                        correctWord = correctWord,
                        allCorrectWords = allCorrectWords,
                        options = options,
                        sentenceWords = sentenceWords,
                        activityType = activityType
                    )
                )
            }
        }

        return questions.shuffled()
    }
}
