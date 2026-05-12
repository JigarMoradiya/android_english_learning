package com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.verb.lesson.view_model

import com.example.myapplication.data.model.GrammarExampleModel

data class VerbLessonUiState(
    val explanationText: String =
    """
        <font color='#EE0000'><b>A verb is a word that tells us what someone or something does.</b></font><br>👉 Verbs show an <b>action</b> (like run, jump, eat, sing), a <b>state of being</b> (like is, am, are), or an <b>occurrence</b> (like happen, grow).<br>Verbs are also called <b>doing words</b> because they show what is happening.<br><br><b>Examples:</b><br><b>Action:</b> run, jump, dance, swim, read, write, speak, stand, draw, eat, play, sing.<br><b>State :</b> is, am, are, was, were.<br><b>Occurrence :</b> grow, happen, bloom, rain.
    """.trimIndent(),

    val examples: List<GrammarExampleModel> = emptyList()
)