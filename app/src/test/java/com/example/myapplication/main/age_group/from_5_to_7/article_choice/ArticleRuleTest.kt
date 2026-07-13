package com.example.myapplication.main.age_group.from_5_to_7.article_choice

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ArticleRuleTest {

    // Plain vowel sounds → "an"
    @Test
    fun vowelSoundWordsNeedAn() {
        listOf(
            "Apple", "Elephant", "Icecream", "Orange", "Umbrella",
            "Ant", "Owl", "Ear", "Insect", "Octopus", "Envelope", "Igloo"
        ).forEach { word ->
            assertTrue("expected 'an $word'", ArticleRule.needsAn(word))
        }
    }

    // Plain consonant sounds → "a"
    @Test
    fun consonantSoundWordsNeedA() {
        listOf("Ball", "Cat", "Dog", "Hen", "King", "Train", "Zip").forEach { word ->
            assertFalse("expected 'a $word'", ArticleRule.needsAn(word))
        }
    }

    // Vowel letter but consonant sound ("yoo", "wa") → "a"
    @Test
    fun vowelLetterConsonantSoundWordsNeedA() {
        listOf(
            "Unicorn", "Uniform", "University", "Unique", "Utensil", "UFO",
            "User", "Usual", "Ukulele", "European", "Eucalyptus", "One", "Once"
        ).forEach { word ->
            assertFalse("expected 'a $word'", ArticleRule.needsAn(word))
        }
    }

    // Consonant letter but silent 'h' → "an"
    @Test
    fun silentHWordsNeedAn() {
        listOf("Hour", "Hourglass", "Honest", "Honour", "Honor", "Heir").forEach { word ->
            assertTrue("expected 'an $word'", ArticleRule.needsAn(word))
        }
    }

    @Test
    fun blankAndWhitespaceDefaultToA() {
        assertFalse(ArticleRule.needsAn(""))
        assertFalse(ArticleRule.needsAn("   "))
    }

    @Test
    fun isCaseInsensitive() {
        assertTrue(ArticleRule.needsAn("aPPle"))
        assertFalse(ArticleRule.needsAn("uNICORN"))
        assertTrue(ArticleRule.needsAn("HOUR"))
    }
}
