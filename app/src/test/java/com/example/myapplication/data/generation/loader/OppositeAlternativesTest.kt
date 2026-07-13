package com.example.myapplication.data.generation.loader

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OppositeAlternativesTest {

    @Test
    fun expectedAnswerIsAlwaysCorrect() {
        assertTrue(OppositeAlternatives.isCorrect("Hot", chosen = "Cold", expected = "Cold"))
    }

    @Test
    fun knownAlternativeOppositesAreAlsoCorrect() {
        assertTrue(OppositeAlternatives.isCorrect("Brave", chosen = "Fearful", expected = "Cowardly"))
        assertTrue(OppositeAlternatives.isCorrect("Brave", chosen = "Timid", expected = "Cowardly"))
        assertTrue(OppositeAlternatives.isCorrect("Old", chosen = "Young", expected = "New"))
        assertTrue(OppositeAlternatives.isCorrect("Big", chosen = "Tiny", expected = "Small"))
    }

    @Test
    fun unrelatedWordsStayWrong() {
        assertFalse(OppositeAlternatives.isCorrect("Brave", chosen = "Happy", expected = "Cowardly"))
        assertFalse(OppositeAlternatives.isCorrect("Hot", chosen = "Big", expected = "Cold"))
    }

    @Test
    fun checkIsCaseInsensitive() {
        assertTrue(OppositeAlternatives.isCorrect("bRAVE", chosen = "fearful", expected = "Cowardly"))
    }

    @Test
    fun reportsWhenAnswerWasAnAlternative() {
        assertTrue(OppositeAlternatives.isAlternative("Brave", chosen = "Fearful", expected = "Cowardly"))
        assertFalse(OppositeAlternatives.isAlternative("Brave", chosen = "Cowardly", expected = "Cowardly"))
    }
}
