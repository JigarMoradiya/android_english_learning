package com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.pronouns.lesson.view_model

import com.example.myapplication.data.model.GrammarExampleModel

data class PronounLessonUiState(
    val explanationText: String =
    """
       <font color='#EE0000'><b>A pronoun is a word that takes the place of a noun.</b></font><br>
       👉 Instead of repeating the noun again and again, we use pronouns.<br>📌 Common pronouns: <b>I, he, she, it, we, you, they, me, him, her, us, them</b>.<br><br>
       <b>Examples:</b><br>- Ramesh is my friend. <b>He</b> is very kind.
       <br>- This is my dog. <b>It</b> is cute.
       <br>- Tina and I are going. <b>We</b> are happy.
       <br>- Meet my parents. <b>They</b> are teachers.
    """.trimIndent(),

    val examples: List<GrammarExampleModel> = emptyList()
)