package com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.common_ui.practice

import android.content.Context
import com.example.myapplication.data.generation.loader.LessonLoader
import com.example.myapplication.data.model.NounVerbAdjectiveCommonQuestionModel
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.WordType

object GrammarPracticeQuestionFactory {

    fun generateQuestions(
        context: Context,
        level: SentenceLevel,
        targetType: WordType
    ): List<NounVerbAdjectiveCommonQuestionModel> {

        val lessons = LessonLoader.loadAllUnits(context,level)

        val questions = mutableListOf<NounVerbAdjectiveCommonQuestionModel>()

        // all words from all lessons
        val allWords = lessons
            .flatMap { it.sentences }
            .flatMap { it.blankableWords }

        lessons.forEach { lesson ->

            lesson.sentences
                .filter { it.difficulty == 1 }
                .forEach { sentence ->

                    // correct words only for target type
                    val targetWords = sentence.blankableWords.filter {
                        it.type == targetType
                    }

                    val correctWord = targetWords.shuffled().firstOrNull()
                        ?: return@forEach

                    // Step 1 → wrong options from same sentence
                    val wrongOptions = sentence.blankableWords
                        .filter {
                            it.word.lowercase() != correctWord.word.lowercase() &&
                                    it.type != targetType
                        }
                        .map { it.word.lowercase() }
                        .distinct()
                        .shuffled()
                        .toMutableList()

                    // Step 2 → fallback from all lessons
                    if (wrongOptions.size < 2) {

                        val remainingNeeded = 2 - wrongOptions.size

                        val extraOptions = allWords
                            .filter {
                                it.type != targetType &&
                                        it.word.lowercase() != correctWord.word.lowercase() &&
                                        !wrongOptions.contains(it.word.lowercase())
                            }
                            .map { it.word.lowercase() }
                            .distinct()
                            .shuffled()
                            .take(remainingNeeded)

                        wrongOptions.addAll(extraOptions)
                    }

                    val finalOptions = (
                            listOf(correctWord.word.lowercase()) +
                                    wrongOptions.take(2)
                            )
                        .shuffled()

                    questions.add(
                        NounVerbAdjectiveCommonQuestionModel(
                            question = sentence.text,
                            imageName = lesson.imageName,
                            correctAnswer = correctWord.word.lowercase(),
                            options = finalOptions
                        )
                    )
                }
        }

        return questions.shuffled()
    }
}