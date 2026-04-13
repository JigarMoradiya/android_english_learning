package com.example.myapplication.main.age_group.from_5_to_7.sight_words.data

data class SightWord(
    val word: String,
    val useCases: List<UseCase>
)

data class UseCase(
    val description: String,
    val example: String
)

val sightWordsAgeGroup5_7 = listOf(

    SightWord("The", listOf(
        UseCase("Used when both speaker and listener know the noun", "Please close the door."),
        UseCase("Used for something unique", "The sun rises in the east."),
        UseCase("Used when referring to something already mentioned", "I saw a dog. The dog was very friendly."),
        UseCase("Used before superlatives", "She is the best student in the class."),
        UseCase("Used before names of certain places", "The United States, The Himalayas."),
        UseCase("Used when something is obvious from context", "I’m going to the supermarket.")
    )),

    SightWord("Is", listOf(
        UseCase("Present tense of ‘be’ for singular subjects", "She is a doctor."),
        UseCase("Used in questions", "Is it raining?"),
        UseCase("Used in short answers", "Yes, it is."),
        UseCase("Used to describe state/condition", "The room is clean.")
    )),

    SightWord("And", listOf(
        UseCase("Connects words of the same type", "I like apples and bananas."),
        UseCase("Connects two sentences or ideas", "She studied hard, and she passed."),
        UseCase("Used for emphasis", "Try and do your best.")
    )),

    SightWord("In", listOf(
        UseCase("Shows position inside something", "The toys are in the box."),
        UseCase("Used for time expressions", "We will meet in the morning."),
        UseCase("Used for areas/places", "I live in New York.")
    )),

    SightWord("It", listOf(
        UseCase("Refers to an object, animal, or idea", "It is raining."),
        UseCase("Used as a dummy subject", "It is important to eat healthy."),
        UseCase("Refers to a situation", "I don’t like this movie. It is boring.")
    )),

    SightWord("To", listOf(
        UseCase("Shows direction", "I am going to school."),
        UseCase("Used with infinitive verbs", "I like to play football."),
        UseCase("Shows relation", "This gift belongs to her."),
        UseCase("Used to indicate limit", "The time is five to ten.")
    )),

    SightWord("You", listOf(
        UseCase("Refers to the person being spoken to", "You are my best friend."),
        UseCase("Used in instructions", "You must wear a helmet."),
        UseCase("Used for emphasis", "Only you can do this.")
    )),

    SightWord("That", listOf(
        UseCase("Refers to something far or not near", "That is my car."),
        UseCase("Connects clauses", "I know that she is honest."),
        UseCase("Used for emphasis", "He is not that tall."),
        UseCase("Used to identify something", "That book is interesting.")
    )),

    SightWord("He", listOf(
        UseCase("Refers to a male person", "He is my brother."),
        UseCase("Used as a subject in sentences", "He likes football."),
        UseCase("Used in storytelling", "He went into the forest.")
    )),

    SightWord("Was", listOf(
        UseCase("Past tense of ‘is’ for singular", "She was a teacher."),
        UseCase("Used in past continuous tense", "He was running fast."),
        UseCase("Used in reported speech", "He said he was busy.")
    )),

    SightWord("For", listOf(
        UseCase("Shows purpose", "This gift is for you."),
        UseCase("Shows duration", "I waited for two hours."),
        UseCase("Shows reason", "She was punished for being late.")
    )),

    SightWord("On", listOf(
        UseCase("Shows position above a surface", "The book is on the table."),
        UseCase("Shows a date/day", "I was born on Monday."),
        UseCase("Shows involvement", "He is on duty.")
    )),

    SightWord("Are", listOf(
        UseCase("Present tense of ‘be’ for plural subjects", "They are friends."),
        UseCase("Used in questions", "Are you ready?"),
        UseCase("Used in continuous tense", "We are studying now.")
    )),

    SightWord("As", listOf(
        UseCase("Used for comparison", "She is as tall as her brother."),
        UseCase("Shows role/function", "He works as a teacher."),
        UseCase("Shows reason", "As it was raining, we stayed inside.")
    )),

    SightWord("With", listOf(
        UseCase("Shows accompaniment", "I went with my friend."),
        UseCase("Shows tool/means", "He cut the paper with scissors."),
        UseCase("Shows manner", "She spoke with confidence.")
    )),

    SightWord("His", listOf(
        UseCase("Shows possession by a male person", "This is his book."),
        UseCase("Used as pronoun", "The decision was his."),
        UseCase("Used in storytelling", "He raised his hand.")
    )),

    SightWord("They", listOf(
        UseCase("Refers to more than one person/thing", "They are my classmates."),
        UseCase("Used as gender-neutral pronoun", "Someone left their bag. They will come back."),
        UseCase("Used in storytelling", "They went to the forest.")
    )),

    SightWord("I", listOf(
        UseCase("Refers to oneself (the speaker)", "I am happy."),
        UseCase("Used in introductions", "I am Jigar."),
        UseCase("Used to show opinion", "I think this is good.")
    )),

    SightWord("At", listOf(
        UseCase("Shows location", "She is at home."),
        UseCase("Shows time", "We met at 5 o’clock."),
        UseCase("Shows activity", "He laughed at the joke.")
    )),

    SightWord("Be", listOf(
        UseCase("Used to give instructions/commands", "Be quiet."),
        UseCase("Used to show state", "You must be careful."),
        UseCase("Used in future tense", "I will be ready soon.")
    ))
)