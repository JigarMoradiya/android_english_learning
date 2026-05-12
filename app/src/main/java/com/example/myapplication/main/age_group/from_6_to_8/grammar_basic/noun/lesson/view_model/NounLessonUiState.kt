package com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.noun.lesson.view_model

import com.example.myapplication.data.model.GrammarExampleModel

data class NounLessonUiState(
    val explanationText: String =
    """
        <font color='#EE0000'><b>A noun is a word that names</b></font> a person, an animal, a place, or a thing.<br><br>
        
        👉 Nouns are also called <b>naming words</b> because they tell us what something is called.<br>
        
        📌 Without nouns, we would not know what to call people, places, animals, or things around us.<br><br>
        
        <b>Examples:</b><br>
        
        <b>Persons:</b> boy, girl, teacher, doctor.<br>
        <b>Animals:</b> dog, cat, lion, elephant.<br>
        <b>Places:</b> school, park, house, city.<br>
        <b>Things:</b> book, ball, apple, chair.
    """.trimIndent(),

    val examples: List<GrammarExampleModel> = emptyList()
)