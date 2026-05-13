package com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.adjectives.lesson.view_model

import com.example.myapplication.data.model.GrammarExampleModel

data class AdjectivesLessonUiState(
    val explanationText1: String =
        """
        <font color="#EE0000"><b>What is an Adjective?</b></font><br>
        
        An <b>adjective</b> is a word that describes a noun.<br>
        
        👉 Adjectives tell us more about a person, animal, place, or thing.<br>
        
        They tell us about color, size, shape, feeling, and more.<br>
        
        Without adjectives, we would not know how something looks or feels.<br><br>
        
        <font color="#EE0000"><b>Examples around you:</b></font><br>
        big, small, tall, short, red, blue, happy, sad, round, long, clean, fast, slow
    """.trimIndent(),

    val explanationText2: String =
        """
        <font color="#EE0000"><b>Types of Adjectives:</b></font><br>
        
        <b>1. Size 📏</b> (Describes size)<br>
        Examples: big, small, tall, short.<br>
        Sentence: The elephant is <b>big</b>.<br><br>
        
        <b>2. Color 🎨</b> (Describes color)<br>
        Examples: red, blue, green, yellow.<br>
        Sentence: The ball is <b>red</b>.<br><br>
        
        <b>3. Feeling 😊</b> (Describes feelings)<br>
        Examples: happy, sad, excited, tired.<br>
        Sentence: The girl is <b>happy</b>.<br><br>
        
        <b>4. Shape 🔵</b> (Describes shape)<br>
        Examples: round, square, long, short.<br>
        Sentence: The clock is <b>round</b>.
    """.trimIndent(),

    val explanationText3: String =
        """
        <font color="#EE0000"><b>Easy Trick 💡</b></font><br>
        
        <b>Ask yourself:</b><br>
        What color is it?<br>
        What size is it?<br>
        How does it look?<br>
        How does it feel?<br>
        
        The answer is usually an adjective ✅
    """.trimIndent(),

    val explanationText4: String =
        """
        <font color="#EE0000"><b>Practice:</b></font><br>
        
        The apple is red.<br>
        Answer: <b>red</b><br><br>
        
        The boy is tall.<br>
        Answer: <b>tall</b>
    """.trimIndent(),

    val examples: List<GrammarExampleModel> = emptyList()
)