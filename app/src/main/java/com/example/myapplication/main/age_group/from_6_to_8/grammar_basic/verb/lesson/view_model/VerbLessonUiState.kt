package com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.verb.lesson.view_model

import com.example.myapplication.data.model.GrammarExampleModel

data class VerbLessonUiState(
    val explanationText1: String =
    """
        <font color='#EE0000'><b>What is a Verb?</b></font><br>

        A <b>verb</b> is a word that tells what a person, animal, or thing does.<br>

        👉 Verbs are called <b>doing words</b> because they show an action.<br>

        Without verbs, we cannot know what someone is doing.<br><br>

        <font color='#EE0000'><b>Examples around you:</b></font><br>
        run, jump, eat, read, write, play, sleep, walk, sing, dance, talk, laugh, cry, drink, sit, stand, clap, draw, swim, cook
        """.trimIndent(),

    val explanationText2: String =
        """
        <font color="#EE0000"><b>Types of Verbs:</b></font><br>

        <b>1. Action Verbs 🏃</b> (These show action)<br>        
        Examples: run, jump, dance, swim, read, write, play, eat, drink, walk, talk, sing, draw, clap, laugh, sleep, cook, ride, throw, catch<br>
        Sentence: The boy <b>runs</b> fast.<br><br>

        <b>2. Speaking Verbs 🗣️</b> (These show speaking actions)<br>        
        Examples: talk, speak, shout, sing, whisper, ask, answer, tell, call, laugh, cry<br>
        Sentence: She <b>sings</b> a song.<br><br>

        <b>3. Being Verbs 😊</b> (These tell how someone or something is)<br>        
        Examples: is, am, are, was, were, be, being, been<br>
        Sentence: The cat <b>is</b> cute.
        """.trimIndent(),

    val explanationText3: String =
        """
        <font color="#EE0000"><b>Easy Trick 💡</b></font><br>
        Ask yourself:<br>
        What is someone doing?<br>

        The answer is usually a verb ✅
        """.trimIndent(),

    val explanationText4: String =
        """
        <font color="#EE0000"><b>Practice:</b></font><br>
        The dog runs fast.<br>
        Answer: <b>runs</b><br><br>

        I eat an apple.<br>
        Answer: <b>eat</b>
        """.trimIndent(),

    val examples: List<GrammarExampleModel> = emptyList()
)