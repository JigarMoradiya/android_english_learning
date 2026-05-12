package com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.adjectives.lesson.view_model

import com.example.myapplication.data.model.GrammarExampleModel

data class AdjectivesLessonUiState(
    val explanationText: String =
    """
        <font color='#EE0000'><b>An adjective is a word that describes a noun.</b></font><br>👉 Adjectives tell us more about a person, an animal, a place, or a thing.<br>They describe qualities like <b>size, color, shape, feeling</b>, and more.<br><br><b>Examples:</b><br><b>Size:</b> big, small, tall, short.<br><b>Color:</b> red, blue, green.<br><b>Feeling:</b> happy, sad, excited, tired.<br><b>Shape:</b> round, square, long, short.
    """.trimIndent(),

    val examples: List<GrammarExampleModel> = emptyList()
)