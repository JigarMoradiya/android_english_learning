package com.example.myapplication.data.generation.loader

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WrongPluralQuestionFactoryTest {

    @Test
    fun questionHasFourOptionsWithExactlyOneWrongPlural() {
        val entry = wrongPluralEntries.first()
        val q = WrongPluralQuestionFactory.make(entry, wrongPluralEntries)
        assertEquals(4, q.options.size)
        assertEquals(4, q.options.distinct().size)
        assertEquals(1, q.options.count { it == entry.wrongPlural })
        val correctForms = wrongPluralEntries.map { it.correctPlural }
        q.options.filter { it != entry.wrongPlural }.forEach { option ->
            assertTrue("distractor $option must be a real correct plural", option in correctForms)
        }
    }

    @Test
    fun dataCoversTheTrickyFamilies() {
        val wrongs = wrongPluralEntries.map { it.wrongPlural }
        assertTrue("Leafs" in wrongs)   // f/fe → ves
        assertTrue("Childs" in wrongs)  // irregular
        assertTrue("Boxs" in wrongs)    // -es rule
        assertTrue("Babys" in wrongs)   // y → ies
        assertTrue(wrongPluralEntries.size >= 15)
    }

    @Test
    fun everyEntryHasARuleHint() {
        wrongPluralEntries.forEach { assertTrue(it.ruleHint.isNotBlank()) }
    }
}
