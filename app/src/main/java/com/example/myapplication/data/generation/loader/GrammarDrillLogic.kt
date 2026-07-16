package com.example.myapplication.data.generation.loader

import com.example.myapplication.data.model.ReadSentenceItemNew
import com.example.myapplication.data.model.WordType

data class GrammarDrillQuestion(
    val id: String,
    val promptWithBlank: String,
    val options: List<String>,
    val correctAnswer: String,
    val explanation: String
)

enum class GrammarDrillType { SUBJECT_VERB_AGREEMENT, HAS_HAVE }

/**
 * Phase 3 grammar drills built by filtering the existing tagged sentence pool
 * (2.2a He/She/It agreement, 4.1a has/have). Mirrors iOS GrammarDrillLogic.swift;
 * unit-tested via GrammarDrillLogicTest.
 */
object GrammarDrillLogic {

    // 4.1a — has vs have
    fun makeHasHaveQuestions(
        lessons: List<ReadSentenceItemNew>,
        extraSentences: List<String> = emptyList(),
        limit: Int
    ): List<GrammarDrillQuestion> {
        val texts = lessons.flatMap { it.sentences.map { s -> s.text } } + extraSentences
        val out = mutableListOf<GrammarDrillQuestion>()
        for (text in texts) {
            val words = text.split(" ").toMutableList()
            val idx = words.indexOfFirst { normalize(it) == "has" || normalize(it) == "have" }
            if (idx == -1) continue
            val correct = normalize(words[idx])
            words[idx] = "___"
            out.add(
                GrammarDrillQuestion(
                    id = "hh_${out.size}",
                    promptWithBlank = words.joinToString(" "),
                    options = listOf("has", "have"),
                    correctAnswer = correct,
                    explanation = "HAS → he/she/it.  HAVE → I/you/we/they."
                )
            )
        }
        return out.shuffled().take(limit)
    }

    // 2.2a — subject-verb agreement (auxiliaries/linking verbs skipped)
    private val agreementStopVerbs = setOf(
        "is", "are", "was", "were", "be", "been", "being", "am",
        "has", "have", "do", "does", "did", "can", "will",
        "'s", "'re", "'m", "'ll", "'ve", "'d"
    )

    /**
     * Mixes He/She/It (answer = verb + -s) with I/You/We/They (answer = plain verb)
     * so the correct choice is NOT always the -s form.
     */
    fun makeAgreementQuestions(lessons: List<ReadSentenceItemNew>, limit: Int): List<GrammarDrillQuestion> {
        val out = mutableListOf<GrammarDrillQuestion>()
        for (lesson in lessons) for (s in lesson.sentences) {
            val lower = s.text.lowercase()
            val isThirdPerson = when {
                lower.startsWith("he ") || lower.startsWith("she ") || lower.startsWith("it ") -> true
                lower.startsWith("i ") || lower.startsWith("you ") ||
                    lower.startsWith("we ") || lower.startsWith("they ") -> false
                else -> continue
            }
            val vb = s.blankableWords.firstOrNull {
                it.type == WordType.VERB && normalize(it.word) !in agreementStopVerbs
            } ?: continue
            val verb = normalize(vb.word)

            val distractor: String
            val explanation: String
            if (isThirdPerson) {
                if (!verb.endsWith("s")) continue
                distractor = baseForm(verb)
                explanation = "With he, she or it, add -s."
            } else {
                if (verb.endsWith("s")) continue
                distractor = thirdPersonForm(verb)
                explanation = "With I, you, we or they, no -s."
            }
            if (distractor == verb || distractor.isEmpty()) continue
            val blanked = blankWord(s.text, vb.word) ?: continue

            out.add(
                GrammarDrillQuestion(
                    id = s.id,
                    promptWithBlank = blanked,
                    options = listOf(verb, distractor).shuffled(),
                    correctAnswer = verb,
                    explanation = explanation
                )
            )
        }
        return out.shuffled().take(limit)
    }

    /** Build agreement questions from a curated (sentence to verb) bank — clean,
     *  kid-friendly sentences instead of arbitrary pool sentences. */
    fun makeAgreementFromBank(bank: List<Pair<String, String>>, limit: Int): List<GrammarDrillQuestion> {
        val out = mutableListOf<GrammarDrillQuestion>()
        for ((sentence, verbRaw) in bank) {
            val verb = normalize(verbRaw)
            val thirdPerson = verb.endsWith("s")
            val distractor = if (thirdPerson) baseForm(verb) else thirdPersonForm(verb)
            if (distractor == verb || distractor.isEmpty()) continue
            val blanked = blankWord(sentence, verbRaw) ?: continue
            out.add(
                GrammarDrillQuestion(
                    id = "ag_${out.size}",
                    promptWithBlank = blanked,
                    options = listOf(verb, distractor).shuffled(),
                    correctAnswer = verb,
                    explanation = if (thirdPerson) "With he, she or it, add -s." else "With I, you, we or they, no -s."
                )
            )
        }
        return out.shuffled().take(limit)
    }

    /** Base verb → third-person form (add -s/-es/-ies). */
    fun thirdPersonForm(v: String): String {
        if (v.endsWith("s") || v.endsWith("x") || v.endsWith("z") ||
            v.endsWith("o") || v.endsWith("sh") || v.endsWith("ch")
        ) return v + "es"
        if (v.endsWith("y") && v.length >= 2) {
            val beforeY = v[v.length - 2]
            if (beforeY !in setOf('a', 'e', 'i', 'o', 'u')) return v.dropLast(1) + "ies"
        }
        return v + "s"
    }

    // Helpers
    fun normalize(w: String): String = w.lowercase().trim('.', ',', '!', '?', ';', ':', '"', '\'')

    /** Reverse the third-person "-s/-es/-ies" ending to the base verb form. */
    fun baseForm(v: String): String {
        if (v.endsWith("ies") && v.length > 3) return v.dropLast(3) + "y"      // studies→study
        if (v.endsWith("es") && v.length > 2) {
            val stem = v.dropLast(2)
            val esStemEndings = setOf('s', 'x', 'z', 'o')
            if (stem.lastOrNull() in esStemEndings) return stem                // goes→go, boxes→box
            if (stem.endsWith("sh") || stem.endsWith("ch")) return stem        // watches→watch
            return v.dropLast(1)                                              // likes→like
        }
        if (v.endsWith("s") && v.length > 1) return v.dropLast(1)            // runs→run, plays→play
        return v
    }

    fun blankWord(sentence: String, word: String): String? {
        val words = sentence.split(" ").toMutableList()
        val idx = words.indexOfFirst { normalize(it) == normalize(word) }
        if (idx == -1) return null
        words[idx] = "___"
        return words.joinToString(" ")
    }
}
