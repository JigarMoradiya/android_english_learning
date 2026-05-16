package com.example.myapplication.data.generation.loader

import com.example.myapplication.data.model.WordType
import java.util.UUID

// ── Medium: Drag-to-Bucket question model ────────────────────────────────────

data class WordDragItem(
    val id: UUID = UUID.randomUUID(),
    val word: String,
    val type: WordType
)

data class MixedMediumQuestion(
    val imageName: String,
    val sentence: String,
    val wordPool: List<WordDragItem>,
    val bucketTypes: List<WordType>
)

data class WrongCorrection(
    val word: String,
    val correctType: WordType
)

object MixedGrammarMediumQuestions {
    fun generate(): List<MixedMediumQuestion> = listOf(
        MixedMediumQuestion(
            imageName = "cat_on_tree",
            sentence = "The cat sits on a tall tree.",
            wordPool = listOf(
                WordDragItem(word = "cat", type = WordType.NOUN),
                WordDragItem(word = "sits", type = WordType.VERB),
                WordDragItem(word = "tall", type = WordType.ADJECTIVE)
            ),
            bucketTypes = listOf(WordType.NOUN, WordType.VERB, WordType.ADJECTIVE)
        ),
        MixedMediumQuestion(
            imageName = "dog_in_park",
            sentence = "The big dog runs in the park.",
            wordPool = listOf(
                WordDragItem(word = "dog", type = WordType.NOUN),
                WordDragItem(word = "big", type = WordType.ADJECTIVE),
                WordDragItem(word = "runs", type = WordType.VERB)
            ),
            bucketTypes = listOf(WordType.NOUN, WordType.VERB, WordType.ADJECTIVE)
        ),
        MixedMediumQuestion(
            imageName = "going_to_school",
            sentence = "She walks to school every day.",
            wordPool = listOf(
                WordDragItem(word = "she", type = WordType.PRONOUN),
                WordDragItem(word = "walks", type = WordType.VERB),
                WordDragItem(word = "school", type = WordType.NOUN)
            ),
            bucketTypes = listOf(WordType.NOUN, WordType.VERB, WordType.PRONOUN)
        ),
        MixedMediumQuestion(
            imageName = "family_dinner",
            sentence = "They eat a tasty dinner together.",
            wordPool = listOf(
                WordDragItem(word = "they", type = WordType.PRONOUN),
                WordDragItem(word = "eat", type = WordType.VERB),
                WordDragItem(word = "tasty", type = WordType.ADJECTIVE),
                WordDragItem(word = "dinner", type = WordType.NOUN)
            ),
            bucketTypes = listOf(WordType.NOUN, WordType.VERB, WordType.ADJECTIVE, WordType.PRONOUN)
        ),
        MixedMediumQuestion(
            imageName = "bird_on_branch",
            sentence = "A small bird sings on the branch.",
            wordPool = listOf(
                WordDragItem(word = "bird", type = WordType.NOUN),
                WordDragItem(word = "small", type = WordType.ADJECTIVE),
                WordDragItem(word = "sings", type = WordType.VERB)
            ),
            bucketTypes = listOf(WordType.NOUN, WordType.VERB, WordType.ADJECTIVE)
        ),
        MixedMediumQuestion(
            imageName = "boy_playing_ball",
            sentence = "He plays with a red ball.",
            wordPool = listOf(
                WordDragItem(word = "he", type = WordType.PRONOUN),
                WordDragItem(word = "plays", type = WordType.VERB),
                WordDragItem(word = "red", type = WordType.ADJECTIVE),
                WordDragItem(word = "ball", type = WordType.NOUN)
            ),
            bucketTypes = listOf(WordType.NOUN, WordType.VERB, WordType.ADJECTIVE, WordType.PRONOUN)
        ),
        MixedMediumQuestion(
            imageName = "girl_reading_home",
            sentence = "She reads a long book at home.",
            wordPool = listOf(
                WordDragItem(word = "she", type = WordType.PRONOUN),
                WordDragItem(word = "reads", type = WordType.VERB),
                WordDragItem(word = "long", type = WordType.ADJECTIVE),
                WordDragItem(word = "book", type = WordType.NOUN)
            ),
            bucketTypes = listOf(WordType.NOUN, WordType.VERB, WordType.ADJECTIVE, WordType.PRONOUN)
        ),
        MixedMediumQuestion(
            imageName = "cycling_park",
            sentence = "I ride my bicycle in the park.",
            wordPool = listOf(
                WordDragItem(word = "I", type = WordType.PRONOUN),
                WordDragItem(word = "ride", type = WordType.VERB),
                WordDragItem(word = "bicycle", type = WordType.NOUN)
            ),
            bucketTypes = listOf(WordType.NOUN, WordType.VERB, WordType.PRONOUN)
        ),
        MixedMediumQuestion(
            imageName = "birthday_party",
            sentence = "We had a happy birthday party.",
            wordPool = listOf(
                WordDragItem(word = "we", type = WordType.PRONOUN),
                WordDragItem(word = "happy", type = WordType.ADJECTIVE),
                WordDragItem(word = "birthday", type = WordType.NOUN)
            ),
            bucketTypes = listOf(WordType.NOUN, WordType.ADJECTIVE, WordType.PRONOUN)
        ),
        MixedMediumQuestion(
            imageName = "doing_homework",
            sentence = "She writes her homework carefully.",
            wordPool = listOf(
                WordDragItem(word = "she", type = WordType.PRONOUN),
                WordDragItem(word = "writes", type = WordType.VERB),
                WordDragItem(word = "homework", type = WordType.NOUN)
            ),
            bucketTypes = listOf(WordType.NOUN, WordType.VERB, WordType.PRONOUN)
        )
    ).shuffled()
}

// ── Tap the Word / Multiple Choice ──────────────────────────────────────────

data class GrammarWordQuestion(
    val sentence: String,
    val targetWord: String,       // the highlighted word the child should tap
    val targetType: WordType,     // NOUN / VERB / ADJECTIVE / PRONOUN
    val allWordsInSentence: List<String>  // tokenised, punctuation stripped
)

// ── Drag-to-Bucket ───────────────────────────────────────────────────────────

data class BucketWord(
    val word: String,
    val bucket: WordType           // which bucket it belongs to
)

// ── Fix the Sentence ─────────────────────────────────────────────────────────

data class FixWord(
    val id: java.util.UUID = java.util.UUID.randomUUID(),
    val text: String,
    val isWrong: Boolean = false
)

data class FixSentenceQuestion(
    val imageName: String,
    val displayWords: List<FixWord>,
    val correctWord: String,
    val options: List<String>,
    val targetType: WordType
)

// ── Grammar Sentence Builder ─────────────────────────────────────────────────

data class GrammarSentenceItem(
    val words: List<String>,       // shuffled; child orders them
    val correctSentence: String
)

// ── Grammar Match (word → category) ─────────────────────────────────────────

data class GrammarMatchQuestion(
    val word: String,
    val correctType: WordType,
    val options: List<WordType> = listOf(WordType.NOUN, WordType.VERB, WordType.ADJECTIVE, WordType.PRONOUN)
)

// ── Fill the Blanks ──────────────────────────────────────────────────────────

data class GrammarFillBlankQuestion(
    val sentenceWithBlank: String,  // use "_____" as the blank marker
    val options: List<String>,
    val correctAnswer: String,
    val hint: String = ""
)

// ════════════════════════════════════════════════════════════════════════════
// DATA  POOLS
// ════════════════════════════════════════════════════════════════════════════

object MixedGrammarData {

    // ── Beginner: Tap the Word questions ────────────────────────────────────
    val tapWordQuestions: List<GrammarWordQuestion> = listOf(
        GrammarWordQuestion("The cat sat on the mat.", "cat", WordType.NOUN, listOf("The","cat","sat","on","the","mat")),
        GrammarWordQuestion("She runs every morning.", "runs", WordType.VERB, listOf("She","runs","every","morning")),
        GrammarWordQuestion("The big elephant is grey.", "big", WordType.ADJECTIVE, listOf("The","big","elephant","is","grey")),
        GrammarWordQuestion("He gave me a book.", "He", WordType.PRONOUN, listOf("He","gave","me","a","book")),
        GrammarWordQuestion("A dog barks loudly.", "dog", WordType.NOUN, listOf("A","dog","barks","loudly")),
        GrammarWordQuestion("They are playing football.", "They", WordType.PRONOUN, listOf("They","are","playing","football")),
        GrammarWordQuestion("The red apple tastes sweet.", "red", WordType.ADJECTIVE, listOf("The","red","apple","tastes","sweet")),
        GrammarWordQuestion("Birds fly in the sky.", "fly", WordType.VERB, listOf("Birds","fly","in","the","sky")),
        GrammarWordQuestion("My sister loves singing.", "sister", WordType.NOUN, listOf("My","sister","loves","singing")),
        GrammarWordQuestion("We eat lunch together.", "eat", WordType.VERB, listOf("We","eat","lunch","together")),
        GrammarWordQuestion("The tiny puppy ran fast.", "tiny", WordType.ADJECTIVE, listOf("The","tiny","puppy","ran","fast")),
        GrammarWordQuestion("It is a sunny day.", "It", WordType.PRONOUN, listOf("It","is","a","sunny","day")),
    )

    // ── Beginner: Multiple Choice ────────────────────────────────────────────
    val multipleChoiceQuestions: List<GrammarMatchQuestion> = listOf(
        GrammarMatchQuestion("table", WordType.NOUN),
        GrammarMatchQuestion("jump", WordType.VERB),
        GrammarMatchQuestion("beautiful", WordType.ADJECTIVE),
        GrammarMatchQuestion("she", WordType.PRONOUN),
        GrammarMatchQuestion("school", WordType.NOUN),
        GrammarMatchQuestion("sing", WordType.VERB),
        GrammarMatchQuestion("happy", WordType.ADJECTIVE),
        GrammarMatchQuestion("they", WordType.PRONOUN),
        GrammarMatchQuestion("river", WordType.NOUN),
        GrammarMatchQuestion("dance", WordType.VERB),
        GrammarMatchQuestion("tiny", WordType.ADJECTIVE),
        GrammarMatchQuestion("we", WordType.PRONOUN),
        GrammarMatchQuestion("mountain", WordType.NOUN),
        GrammarMatchQuestion("swim", WordType.VERB),
        GrammarMatchQuestion("cold", WordType.ADJECTIVE),
        GrammarMatchQuestion("he", WordType.PRONOUN),
    )

    // ── Medium: Drag to Grammar Bucket ──────────────────────────────────────
    val bucketWords: List<BucketWord> = listOf(
        BucketWord("dog", WordType.NOUN),
        BucketWord("run", WordType.VERB),
        BucketWord("bright", WordType.ADJECTIVE),
        BucketWord("she", WordType.PRONOUN),
        BucketWord("house", WordType.NOUN),
        BucketWord("laugh", WordType.VERB),
        BucketWord("sweet", WordType.ADJECTIVE),
        BucketWord("they", WordType.PRONOUN),
        BucketWord("star", WordType.NOUN),
        BucketWord("sleep", WordType.VERB),
        BucketWord("soft", WordType.ADJECTIVE),
        BucketWord("it", WordType.PRONOUN),
        BucketWord("teacher", WordType.NOUN),
        BucketWord("write", WordType.VERB),
        BucketWord("brave", WordType.ADJECTIVE),
        BucketWord("we", WordType.PRONOUN),
        BucketWord("flower", WordType.NOUN),
        BucketWord("cook", WordType.VERB),
        BucketWord("loud", WordType.ADJECTIVE),
        BucketWord("I", WordType.PRONOUN),
    )

    // ── Advanced: Sentence Builder ──────────────────────────────────────────
    val sentenceBuilderItems: List<GrammarSentenceItem> = listOf(
        GrammarSentenceItem(listOf("is","The","happy","dog"), "The dog is happy."),
        GrammarSentenceItem(listOf("runs","every","She","morning"), "She runs every morning."),
        GrammarSentenceItem(listOf("We","in","the","play","garden"), "We play in the garden."),
        GrammarSentenceItem(listOf("singing","bird","The","is"), "The bird is singing."),
        GrammarSentenceItem(listOf("books","He","loves","reading"), "He loves reading books."),
        GrammarSentenceItem(listOf("dances","always","She","beautifully"), "She always dances beautifully."),
        GrammarSentenceItem(listOf("yellow","a","butterfly","is","It"), "It is a yellow butterfly."),
        GrammarSentenceItem(listOf("They","under","the","tree","sat"), "They sat under the tree."),
    )

    // ── Advanced: Grammar Match ──────────────────────────────────────────────
    val grammarMatchQuestions: List<GrammarMatchQuestion> = listOf(
        GrammarMatchQuestion("quickly", WordType.VERB),   // will be shown as "quickly (in a sentence)"
        GrammarMatchQuestion("elephant", WordType.NOUN),
        GrammarMatchQuestion("shout", WordType.VERB),
        GrammarMatchQuestion("purple", WordType.ADJECTIVE),
        GrammarMatchQuestion("him", WordType.PRONOUN),
        GrammarMatchQuestion("kitchen", WordType.NOUN),
        GrammarMatchQuestion("fly", WordType.VERB),
        GrammarMatchQuestion("enormous", WordType.ADJECTIVE),
        GrammarMatchQuestion("us", WordType.PRONOUN),
        GrammarMatchQuestion("library", WordType.NOUN),
        GrammarMatchQuestion("whisper", WordType.VERB),
        GrammarMatchQuestion("delicious", WordType.ADJECTIVE),
    )

    // ── Advanced: Fill the Blanks ────────────────────────────────────────────
    val fillBlankQuestions: List<GrammarFillBlankQuestion> = listOf(
        GrammarFillBlankQuestion("The _____ runs fast.", listOf("dog", "run", "fast", "happy"), "dog", "Choose a noun"),
        GrammarFillBlankQuestion("She _____ every morning.", listOf("sings", "song", "loud", "she"), "sings", "Choose a verb"),
        GrammarFillBlankQuestion("The _____ apple is red.", listOf("big", "apple", "red", "runs"), "big", "Choose an adjective"),
        GrammarFillBlankQuestion("_____ went to school today.", listOf("He", "school", "today", "went"), "He", "Choose a pronoun"),
        GrammarFillBlankQuestion("Birds _____ in the sky.", listOf("fly", "sky", "blue", "birds"), "fly", "Choose a verb"),
        GrammarFillBlankQuestion("The _____ elephant is grey.", listOf("tall", "elephant", "grey", "runs"), "tall", "Choose an adjective"),
        GrammarFillBlankQuestion("I love my _____.", listOf("cat", "love", "my", "cute"), "cat", "Choose a noun"),
        GrammarFillBlankQuestion("_____ are best friends.", listOf("We", "best", "friends", "are"), "We", "Choose a pronoun"),
        GrammarFillBlankQuestion("The kitten _____ softly.", listOf("meows", "kitten", "soft", "a"), "meows", "Choose a verb"),
        GrammarFillBlankQuestion("It was a _____ day.", listOf("sunny", "day", "was", "it"), "sunny", "Choose an adjective"),
    )
}
