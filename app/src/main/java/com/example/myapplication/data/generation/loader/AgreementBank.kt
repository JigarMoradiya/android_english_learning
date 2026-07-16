package com.example.myapplication.data.generation.loader

/**
 * Phase 4 content: curated, kid-friendly subject-verb agreement sentences for the
 * He/She/It drill (sentence to verb-to-blank). Third-person items use the -s form;
 * plural/first/second-person items use the plain verb. Mirrors iOS AgreementBank.swift.
 */
object AgreementBank {
    val items: List<Pair<String, String>> = listOf(
        // He / She / It  → verb + -s
        "She reads a book." to "reads",
        "He plays football." to "plays",
        "It rains a lot." to "rains",
        "She sings a song." to "sings",
        "He runs very fast." to "runs",
        "She eats an apple." to "eats",
        "He drinks milk." to "drinks",
        "She likes flowers." to "likes",
        "He watches TV." to "watches",
        "It flies in the sky." to "flies",
        "She walks to school." to "walks",
        "He rides a bike." to "rides",
        "It swims in water." to "swims",
        "She draws a picture." to "draws",
        "He helps his mum." to "helps",
        "It barks loudly." to "barks",
        "She claps her hands." to "claps",
        "He kicks the ball." to "kicks",
        "It grows fast." to "grows",
        "She opens the door." to "opens",
        // I / You / We / They  → plain verb
        "They play in the park." to "play",
        "We read every day." to "read",
        "I like ice cream." to "like",
        "You run so fast." to "run",
        "They sing together." to "sing",
        "We eat lunch." to "eat",
        "I draw a star." to "draw",
        "You help at home." to "help",
        "They jump on the bed." to "jump",
        "We walk to the shop." to "walk",
        "I ride my bike." to "ride",
        "They watch a film." to "watch",
        "We swim in the pool." to "swim",
        "You clap your hands." to "clap",
        "I drink water." to "drink",
        "They kick the ball." to "kick",
        "We grow plants." to "grow",
        "You fly a kite." to "fly",
        "I open the box." to "open",
        "They wash the car." to "wash",
    )
}
