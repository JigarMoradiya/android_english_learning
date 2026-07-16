package com.example.myapplication.data.generation.loader

import com.example.myapplication.data.model.LessonSentence
import com.example.myapplication.data.model.MatchPictureQuestion
import com.example.myapplication.data.model.ReadSentenceItemNew
import com.example.myapplication.data.model.SentenceActivities
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.SentenceType
import com.example.myapplication.data.model.SentenceUnit
import org.junit.Assert.*
import org.junit.Test

/**
 * Verifies the Age 6-8 Phase 2 pure logic (items C.2, 5.2, 3.2).
 * Mirrors the iOS swiftc harness.
 */
class SentenceBuilderLogicTest {

    private fun sentence(text: String, builder: Boolean) = LessonSentence(
        id = text.hashCode().toString(),
        text = text,
        type = SentenceType.STATEMENT,
        tense = "present",
        difficulty = 1,
        isPrimary = true,
        grammarFocus = emptyList(),
        tags = emptyList(),
        distractorGroup = "",
        activities = SentenceActivities(
            readListen = true, fillBlank = true, chooseCorrect = true, sentenceBuilder = builder
        ),
        blankableWords = emptyList()
    )

    private fun lesson(image: String, level: SentenceLevel, sentences: List<LessonSentence>) =
        ReadSentenceItemNew(
            id = image, imageName = image, title = "t", unit = SentenceUnit.PLAY_AND_FUN,
            level = level, ageGroup = "6-8", learningGoals = emptyList(), order = 0,
            sentences = sentences
        )

    // ---- C.2 ----
    @Test
    fun buildSoundOptions_easy_capsAtThree() {
        val opts = SentenceBuilderLogic.buildSoundOptions("A", listOf("B", "C", "D", "E"), SentenceLevel.EASY)
        assertEquals(3, opts.size)
        assertTrue(opts.contains("A"))
    }

    @Test
    fun buildSoundOptions_medium_capsAtFour() {
        val opts = SentenceBuilderLogic.buildSoundOptions("A", listOf("B", "C", "D", "E"), SentenceLevel.MEDIUM)
        assertEquals(4, opts.size)
        assertTrue(opts.contains("A"))
    }

    @Test
    fun buildSoundOptions_fewerWrongsThanCap() {
        val opts = SentenceBuilderLogic.buildSoundOptions("A", listOf("B"), SentenceLevel.MEDIUM)
        assertEquals(2, opts.size)
    }

    // ---- 5.2 ----
    @Test
    fun makeBuilderQuestions_onlyBuilderFlaggedSentences() {
        val lessons = listOf(
            lesson("img_short", SentenceLevel.EASY, listOf(
                sentence("The cat sits.", builder = true),
                sentence("A dog.", builder = false)
            )),
            lesson("img_short2", SentenceLevel.EASY, listOf(
                sentence("The boy runs fast.", builder = true)
            ))
        )
        val qs = SentenceBuilderLogic.makeBuilderQuestions(lessons, limit = 5)
        assertEquals(2, qs.size)
        assertTrue(qs.none { it.correctSentence == "A dog." })
        assertTrue(qs.any { it.imageName == "img_short" && it.correctSentence == "The cat sits." })
    }

    @Test
    fun makeBuilderQuestions_respectsLimit() {
        val lessons = (0 until 10).map { i ->
            lesson("i$i", SentenceLevel.MEDIUM, listOf(sentence("Sentence number $i here.", builder = true)))
        }
        assertEquals(5, SentenceBuilderLogic.makeBuilderQuestions(lessons, limit = 5).size)
    }

    // ---- 3.2 ----
    @Test
    fun makeTrueFalse_true_showsCorrectSentenceWithExplanation() {
        val item = MatchPictureQuestion("q1", "i1", "img", "The cat sits.", listOf("The cat sit."))
        val t = SentenceBuilderLogic.makeTrueFalse(item, useCorrect = true)
        assertEquals("true", t.isTrue)
        assertEquals("The cat sits.", t.statement)
        assertFalse(t.explanation.isNullOrEmpty())
    }

    @Test
    fun makeTrueFalse_false_explanationSurfacesCorrectSentence() {
        val item = MatchPictureQuestion("q1", "i1", "img", "The cat sits.", listOf("The cat sit.", "The cat sitting."))
        val f = SentenceBuilderLogic.makeTrueFalse(item, useCorrect = false)
        assertEquals("false", f.isTrue)
        assertTrue(item.wrongOptions.contains(f.statement))
        assertTrue(f.explanation!!.contains("The cat sits."))
        assertNotEquals(f.id, SentenceBuilderLogic.makeTrueFalse(item, useCorrect = true).id)
    }

    // ---- 4.2 ----
    @Test
    fun wordDiff_findsSingleChangedWord() {
        assertEquals("runs" to "run", SentenceBuilderLogic.wordDiff("The dog runs.", "The dog run."))
        assertNull(SentenceBuilderLogic.wordDiff("a b c", "a x y"))
        assertNull(SentenceBuilderLogic.wordDiff("a b", "a b c"))
    }

    // ---- 1.3 ----
    @Test
    fun echoGapSeconds_scalesWithLengthWithFloor() {
        assertEquals(2.0, SentenceBuilderLogic.echoGapSeconds(3), 0.0001)
        assertEquals(6.0, SentenceBuilderLogic.echoGapSeconds(10), 0.0001)
        assertEquals(2.0, SentenceBuilderLogic.echoGapSeconds(0), 0.0001)
    }
}
