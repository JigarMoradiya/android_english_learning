package com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.noun.lesson.view_model

import com.example.myapplication.data.model.GrammarExampleModel

data class NounLessonUiState(
    val explanationText1: String =
    """
        <font color="#EE0000"><b>What is a Noun?</b></font><br>
        
        A <b>noun</b> is a word that names a person, animal, place, or thing.<br>
        
        👉 Nouns are called <b>naming words</b> because they tell us the name of someone or something.<br>
        
        Without nouns, we would not know what to call things around us.<br><br>
        
        <font color="#EE0000"><b>Examples around you:</b></font><br>
        chair, table, book, pencil, bag, teacher, student, dog, cat, house, school, car, ball, apple
    """.trimIndent(),

    val explanationText2: String =
        """       
        <font color="#EE0000"><b>Types of Nouns:</b></font><br>
        
        <b>1. Person 👦</b> (Names of people)<br>        
        Examples: boy, girl, teacher, doctor, mother, friend.<br>
        Sentence: The <b>boy</b> is running.<br><br>
        
        <b>2. Animal 🐶</b> (Names of animals)<br>        
        Examples: dog, cat, lion, elephant, monkey.<br>
        Sentence: The <b>dog</b> is barking.<br><br>
        
        <b>3. Place 🏫</b> (Names of places)<br>
        Examples: school, park, house, city, zoo.<br>
        Sentence: I go to <b>school</b> every day.<br><br>
        
        <b>4. Thing 🍎</b> (Names of things)<br>
        Examples: book, ball, apple, chair, pencil.<br>
        Sentence: This is an <b>apple</b>.
    """.trimIndent(),

    val explanationText3: String =
        """
         <font color="#EE0000"><b>Easy Trick 💡</b></font><br>
        <b>Ask yourself:</b><br>
        Is it a person?<br>
        Is it an animal?<br>
        Is it a place?<br>
        Is it a thing?<br>
        
        If yes → it is a noun ✅
    """.trimIndent(),

    val explanationText4: String =
        """
         <font color="#EE0000"><b>Practice:</b></font><br>
        The cat is sleeping on the bed.<br>
        Answer: <b>cat, bed</b><br><br>   
             
        Aarvi goes to school.<br>
        Answer: <b>Aarvi, school</b>
    """.trimIndent(),

    val examples: List<GrammarExampleModel> = emptyList()
)