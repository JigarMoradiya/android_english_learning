package com.example.myapplication.data.generation.loader

import org.junit.Assert.*
import org.junit.Test

class FunFactBankTest {

    @Test
    fun questions_returnRequestedCountWithData() {
        val qs = FunFactBank.questions(2)
        assertEquals(2, qs.size)
        assertTrue(qs.all { it.statement.isNotEmpty() })
        assertTrue(qs.all { !it.explanation.isNullOrEmpty() })
        assertTrue(qs.all { it.isTrue == "true" || it.isTrue == "false" })
    }
}
