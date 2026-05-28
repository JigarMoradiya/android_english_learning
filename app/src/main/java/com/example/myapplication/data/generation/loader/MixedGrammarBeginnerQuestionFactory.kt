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

        // Global word pool — pre-grouped by excluded type for O(1) distractor lookup
        val allWords = lessons.flatMap { it.sentences }.flatMap { it.blankableWords }
        val distractorPool: Map<WordType, List<BlankableWord>> = WordType.entries.associateWith { excluded ->
            allWords.filter { it.type != excluded }
        }

        val questions = mutableListOf<MixedBeginnerQuestion>()

        for (lesson in lessons) {
            for (sentence in lesson.sentences.filter { it.difficulty == 1 }) {

                if (sentence.blankableWords.isEmpty()) continue

                val wordsByType: Map<WordType, List<BlankableWord>> =
                    sentence.blankableWords.groupBy { it.type }

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
                val seen = mutableSetOf(correctWord)
                val wrongOptions = mutableListOf<String>()
                for (bw in sentence.blankableWords.shuffled()) {
                    if (bw.type == targetType) continue
                    val w = bw.word.lowercase()
                    if (seen.add(w)) { wrongOptions += w }
                    if (wrongOptions.size == 2) break
                }

                // Fill remaining slots from pre-grouped global pool — no O(n) filter
                if (wrongOptions.size < 2) {
                    val pool = distractorPool[targetType] ?: emptyList()
                    var attempts = 0
                    while (wrongOptions.size < 2 && attempts < pool.size) {
                        val w = pool[pool.indices.random()].word.lowercase()
                        if (seen.add(w)) wrongOptions += w
                        attempts++
                    }
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
