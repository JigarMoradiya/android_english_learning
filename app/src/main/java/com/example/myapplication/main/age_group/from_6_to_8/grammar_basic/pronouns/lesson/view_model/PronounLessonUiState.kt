package com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.pronouns.lesson.view_model

import com.example.myapplication.data.model.GrammarExampleModel

data class PronounLessonUiState(
    val explanationText1: String =
        """
        <font color="#EE0000"><b>What is a Pronoun?</b></font><br>
        
        A <b>pronoun</b> is a word that takes the place of a noun.<br>
        
        👉 Instead of repeating the same noun again and again, we use pronouns.<br>
        
        Pronouns make sentences shorter and easier to read.<br><br>
        
        <font color="#EE0000"><b>Examples around you:</b></font><br>
        I, you, he, she, it, we, they, me, him, her, us, them
    """.trimIndent(),

    val explanationText2: String =
        """
        <font color="#EE0000"><b>Types of Pronouns:</b></font><br>
        
        <b>1. Singular Pronouns 👦</b> (Used for one person/thing)<br>
        Examples: he, she, it.<br>
        Sentence: Riya has a book. <b>She</b> is reading.<br><br>
        
        <b>2. Plural Pronouns 👨‍👩‍👧</b> (Used for more than one)<br>
        Examples: we, they.<br>
        Sentence: Sam and I are friends. <b>We</b> play together.<br><br>
        
        <b>3. First Person 🙋</b> (Talking about yourself)<br>
        Examples: I, we.<br>
        Sentence: <b>I</b> am happy today.<br><br>
        
        <b>4. Second Person 👉</b> (Talking to someone)<br>
        Example: you.<br>
        Sentence: <b>You</b> are my friend.
    """.trimIndent(),

    val explanationText3: String =
        """
        <font color="#EE0000"><b>Easy Trick 💡</b></font><br>
        
        <b>Ask yourself:</b><br>
        Is a noun repeated many times?<br>
        
        Replace it with:<br>
        he, she, it, they, we, I, you<br>
        
        Then it is a pronoun ✅
    """.trimIndent(),

    val explanationText4: String =
        """
        <font color="#EE0000"><b>Practice:</b></font><br>
        
        Ramesh is my friend. <b>He</b> is kind.<br>
        Answer: <b>He</b><br><br>
        
        Tina and I are going to school.<br>
        Answer: <b>I</b>
    """.trimIndent(),

    val examples: List<GrammarExampleModel> = emptyList()
)