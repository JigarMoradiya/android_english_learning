package com.example.myapplication.data.generation.loader

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ListenQuestionFactoryTest {

    private val pool = listOf("Pig", "Dog", "Sun", "Hat", "Cup", "Star", "Moon", "Fish", "Tree", "Ball")

    @Test
    fun wordOptionsContainTheAnswerAndAreFourUnique() {
        val q = ListenQuestionFactory.wordQuestion("Dog", pool)
        assertEquals(4, q.options.size)
        assertEquals(4, q.options.distinct().size)
        assertTrue(q.options.contains("Dog"))
        assertEquals("Dog", q.answer)
        assertEquals("Dog", q.spokenText)
    }

    @Test
    fun confusableTargetsUseSoundAlikeDistractors() {
        val q = ListenQuestionFactory.wordQuestion("Pig", pool)
        // B/P ear-training: "Pig" must be offered next to "Big"
        assertTrue("options were ${q.options}", q.options.contains("Big"))
    }

    @Test
    fun homophoneQuestionsOfferExactlyThePair() {
        val q = ListenQuestionFactory.homophoneQuestion()
        assertEquals(2, q.options.size)
        assertTrue(q.options.contains(q.answer))
        assertTrue(q.isHomophone)
        // Spoken text is a sentence giving the meaning, not the bare word
        assertTrue(q.spokenText.contains(" "))
    }

    @Test
    fun batchMixesThreeWordAndTwoHomophoneQuestions() {
        val batch = ListenQuestionFactory.buildBatch(pool, total = 5)
        assertEquals(5, batch.size)
        assertEquals(2, batch.count { it.isHomophone })
        assertEquals(3, batch.count { !it.isHomophone })
    }

    @Test
    fun batchWordAnswersComeFromThePool() {
        val batch = ListenQuestionFactory.buildBatch(pool, total = 5)
        batch.filter { !it.isHomophone }.forEach {
            assertTrue(pool.contains(it.answer))
        }
    }
}
