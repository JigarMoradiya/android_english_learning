package com.example.myapplication.data.generation.loader

import com.example.myapplication.data.model.GrammarQuestion
import com.example.myapplication.data.model.ReadSentenceItemNew

object GrammarQuestionGenerator {

    fun generate(lessons: List<ReadSentenceItemNew>): List<GrammarQuestion> {

        val questions = mutableListOf<GrammarQuestion>()

        lessons.forEach { lesson ->

            lesson.sentences.forEach { sentence ->

                // Skip if not enabled
                if (!sentence.activities.chooseCorrect) return@forEach

                val correct = sentence.text

                val mistakes = GrammarMistakeGenerator.generateMistakes(
                    sentence = correct,
                    words = sentence.blankableWords
                )

                // Need at least 2 wrong options
                if (mistakes.size < 2) return@forEach

                val options = mistakes
                    .shuffled()
                    .take(2)
                    .toMutableList()

                options.add(correct)
                options.shuffle()

                val question = GrammarQuestion(
                    id = sentence.id,
                    correctSentence = correct,
                    imageName = lesson.imageName,
                    options = options
                )

                questions.add(question)
            }
        }

        return questions
    }
}