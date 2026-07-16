package com.example.myapplication.data.generation.loader

import com.example.myapplication.data.model.BlankableWord
import com.example.myapplication.data.model.LessonSentence
import com.example.myapplication.data.model.ReadSentenceItemNew
import com.example.myapplication.data.model.SentenceActivities
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.SentenceType
import com.example.myapplication.data.model.SentenceUnit
import com.example.myapplication.data.model.WordType
import org.junit.Assert.*
import org.junit.Test

/** Verifies the Phase 3 grammar drill generators. Mirrors the iOS harness. */
class GrammarDrillLogicTest {

    private fun sent(text: String, verbs: List<String> = emptyList()) = LessonSentence(
        id = text.hashCode().toString(), text = text, type = SentenceType.STATEMENT,
        tense = "present_simple", difficulty = 1, isPrimary = true,
        grammarFocus = emptyList(), tags = emptyList(), distractorGroup = "",
        activities = SentenceActivities(readListen = true, fillBlank = true, chooseCorrect = true, sentenceBuilder = true),
        blankableWords = verbs.map { BlankableWord(it, WordType.VERB) }
    )
    private fun lesson(ss: List<LessonSentence>) = ReadSentenceItemNew(
        id = "L", imageName = "img", title = "t", unit = SentenceUnit.PLAY_AND_FUN,
        level = SentenceLevel.EASY, ageGroup = "6-8", learningGoals = emptyList(), order = 0, sentences = ss
    )

    @Test
    fun baseForm_reversesThirdPerson() {
        val cases = mapOf(
            "goes" to "go", "runs" to "run", "likes" to "like", "watches" to "watch",
            "studies" to "study", "plays" to "play", "does" to "do", "boxes" to "box", "tries" to "try"
        )
        cases.forEach { (v, b) -> assertEquals(b, GrammarDrillLogic.baseForm(v)) }
    }

    @Test
    fun hasHave_onlyFromHasHaveSentences() {
        val q = GrammarDrillLogic.makeHasHaveQuestions(
            listOf(lesson(listOf(sent("The boy has a ball."), sent("They have two dogs."), sent("A cat sits.")))),
            limit = 5
        )
        assertEquals(2, q.size)
        val has = q.first { it.correctAnswer == "has" }
        assertTrue(has.promptWithBlank.contains("___"))
        assertFalse(has.promptWithBlank.lowercase().contains(" has "))
        assertEquals(listOf("has", "have"), has.options.sorted())
    }

    @Test
    fun thirdPersonForm_addsSuffix() {
        val cases = mapOf(
            "go" to "goes", "watch" to "watches", "box" to "boxes",
            "study" to "studies", "play" to "plays", "run" to "runs", "try" to "tries"
        )
        cases.forEach { (b, t) -> assertEquals(t, GrammarDrillLogic.thirdPersonForm(b)) }
    }

    @Test
    fun agreement_mixesSubjectsSoAnswerIsNotAlwaysS() {
        val q = GrammarDrillLogic.makeAgreementQuestions(
            listOf(lesson(listOf(
                sent("She goes to school.", listOf("goes")),  // 3rd -> goes / go
                sent("He runs fast.", listOf("runs")),        // 3rd -> runs / run
                sent("They play outside.", listOf("play")),   // plural -> play / plays
                sent("We watch TV.", listOf("watch")),        // plural -> watch / watches
                sent("It is red.", listOf("is"))              // stop verb
            ))),
            limit = 20
        )
        assertEquals(4, q.size)
        val goes = q.first { it.correctAnswer == "goes" }
        assertTrue(goes.options.contains("goes") && goes.options.contains("go"))
        val play = q.first { it.correctAnswer == "play" }
        assertTrue(play.options.contains("play") && play.options.contains("plays"))
        // key fix: not always the -s form
        assertEquals(2, q.count { it.correctAnswer.endsWith("s") })
    }

    @Test
    fun agreement_fromCuratedBank() {
        val bank = listOf(
            "She reads a book." to "reads", "They play games." to "play",
            "He watches TV." to "watches", "You fly a kite." to "fly"
        )
        val qs = GrammarDrillLogic.makeAgreementFromBank(bank, 10)
        assertEquals(4, qs.size)
        assertTrue(qs.all { it.promptWithBlank.contains("___") })
        assertTrue(qs.first { it.correctAnswer == "reads" }.options.containsAll(listOf("reads", "read")))
        assertTrue(qs.first { it.correctAnswer == "play" }.options.containsAll(listOf("play", "plays")))
        assertTrue(qs.first { it.correctAnswer == "watches" }.options.contains("watch"))
        assertTrue(qs.first { it.correctAnswer == "fly" }.options.contains("flies"))
    }
}
