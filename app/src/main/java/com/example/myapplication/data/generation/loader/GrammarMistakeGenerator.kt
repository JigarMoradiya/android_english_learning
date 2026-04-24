package com.example.myapplication.data.generation.loader

import com.example.myapplication.data.model.BlankableWord
import com.example.myapplication.data.model.WordType

object GrammarMistakeGenerator {

    fun generateMistakes(
        sentence: String,
        words: List<BlankableWord>
    ): List<String> {

        val mistakes = mutableSetOf<String>()

        // MARK: Verb mistakes
        words.firstOrNull { it.type == WordType.VERB }?.let { verb ->
            val v = verb.word

            if (v == "has") {
                mistakes.add(sentence.replace(v, "was"))
            } else {
                if (v.endsWith("s")) {
                    val base = v.dropLast(1)
                    mistakes.add(sentence.replace(v, base))
                } else {
                    mistakes.add(sentence.replace(v, v + "s"))
                }
            }
        }

        // MARK: Noun mistakes
        words.firstOrNull { it.type == WordType.NOUN }?.let { noun ->
            val n = noun.word

            if (n.endsWith("s")) {
                val singular = n.dropLast(1)
                mistakes.add(sentence.replace(n, singular))
            } else {
                mistakes.add(sentence.replace(n, n + "s"))
            }
        }

        // MARK: Remove article
        mistakes.addAll(articleMistakes(sentence))

        // MARK: Adjective mistake
        words.firstOrNull { it.type == WordType.ADJECTIVE }?.let { adj ->
            val word = adj.word
            mistakes.add(sentence.replace(word, word + "s"))
        }

        // MARK: Pronoun mistake
        mistakes.addAll(pronounMistakes(sentence))

        return mistakes.shuffled().take(3)
    }

    fun articleMistakes(sentence: String): List<String> {

        val articles = listOf("a", "an", "the")
        val mistakes = mutableSetOf<String>()

        for (article in articles) {

            val pattern = " $article "

            if (sentence.contains(pattern)) {
                val newSentence = sentence.replace(pattern, " ")
                mistakes.add(newSentence)
            }
        }

        return mistakes.toList()
    }

    fun pronounMistakes(sentence: String): List<String> {

        val pronounMap = mapOf(
            "I" to "Me",
            "You" to "Your",
            "He" to "Him",
            "She" to "Her",
            "It" to "Its",
            "We" to "Us",
            "They" to "Them",
            "Mine" to "My",
            "Yours" to "Your",
            "His" to "Him",
            "Hers" to "Her",
            "Ours" to "Our",
            "Theirs" to "Their"
        )

        val mistakes = mutableSetOf<String>()

        for ((correct, wrong) in pronounMap) {

            val pattern = "$correct "

            if (sentence.contains(pattern)) {
                val newSentence = sentence.replace(pattern, "$wrong ")
                mistakes.add(newSentence)
            }

            // also check end of sentence
            if (sentence.endsWith(correct)) {
                val newSentence = sentence.replace(correct, wrong)
                mistakes.add(newSentence)
            }
        }

        return mistakes.toList()
    }
}