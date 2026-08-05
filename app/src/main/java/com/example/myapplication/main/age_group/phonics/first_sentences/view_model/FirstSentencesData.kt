package com.example.myapplication.main.age_group.phonics.first_sentences.view_model

import androidx.compose.ui.graphics.Color
import com.example.myapplication.utilities.AudioPhonicsManager
import kotlinx.coroutines.delay
import java.util.UUID

/**
 * MILESTONE · Read Your First Sentences — the models and the content.
 *
 * This sits BETWEEN Level 4 and Level 5 and is deliberately not a level: it teaches no
 * new letter pattern. It is the first time the child reads a LINE instead of a word, and
 * the first time reading has to mean understanding rather than decoding.
 *
 * THE RULE THIS DATA OBEYS: every word in every sentence is either
 *   (a) a CVC word the child can already sound out after Level 4, or
 *   (b) one of the eight helper words below, which are taught by sight.
 * No plurals, no endings, no blends, no digraphs. If a sentence needs anything else, the
 * sentence is wrong — not the child.
 *
 * Keep identical to iOS FirstSentenceModel.swift + FirstSentencesDatabase.swift.
 */

// ── Helper words ──────────────────────────────────────────────────────────────

/**
 * The words a sentence cannot do without and phonics cannot yet reach. They are taught
 * honestly — "you don't sound these out, you just learn them" — which is also how the
 * Sight Words level (28) and Heart Words frame them.
 */
data class FirstSentenceHelper(val word: String)

val firstSentenceHelpers: List<FirstSentenceHelper> = listOf(
    FirstSentenceHelper("the"),
    FirstSentenceHelper("a"),
    FirstSentenceHelper("is"),
    FirstSentenceHelper("on"),
    FirstSentenceHelper("in"),
    FirstSentenceHelper("has"),
    FirstSentenceHelper("can"),
    FirstSentenceHelper("and"),
)

val firstSentenceHelperSet: Set<String> = firstSentenceHelpers.map { it.word }.toSet()

// ── Sentence ──────────────────────────────────────────────────────────────────

data class FirstSentence(
    val id: String = UUID.randomUUID().toString(),
    /** the line as it is read and shown, capital and full stop included */
    val text: String,
    /** the picture that proves it was understood — a drawable name */
    val image: String,
    /** shown when the app has no artwork for that word yet */
    val emoji: String,
    /** the word blanked out on the Missing Word screen */
    val missing: String,
    /** the two wrong choices beside it — near-misses, so the answer cannot be guessed by shape alone */
    val decoys: List<String>,
) {
    /** the line split for tap-a-word and for the travelling highlight */
    val words: List<String> get() = text.split(" ")

    /** audio file name for the whole line */
    val audioKey: String
        get() = text.lowercase()
            .filter { it.isLetter() || it == ' ' }
            .split(" ")
            .joinToString("_")

    companion object {
        /**
         * the plain lowercase word behind a display word ("mat." -> "mat"), which is also
         * its audio file name
         */
        fun key(display: String): String = display.lowercase().filter { it.isLetter() }
    }
}

data class FirstSentenceGroup(
    val key: String,
    val title: String,
    val emoji: String,
    val color: Color,
    val sentences: List<FirstSentence>,
)

/**
 * Five groups of seven, mirroring Level 4's own vowel groups so this reads as the next
 * step of the same journey rather than a new system.
 *
 * Seven rather than four: four lines per vowel is enough to demonstrate a vowel but not
 * enough to get fluent on it, and a child who has met a line once still has it memorised
 * the second time round — which is recitation, not reading. Thirty-five lines means the
 * shuffled practice runs draw a genuinely different set each time.
 */
val firstSentenceGroups: List<FirstSentenceGroup> = listOf(

    FirstSentenceGroup("shortA", "Short A", "🍎", Color(0xFFE53935), listOf(
        FirstSentence(text = "The cat sat on a mat.",   image = "cat", emoji = "🐱", missing = "sat", decoys = listOf("sit", "sun")),
        FirstSentence(text = "A man has a red cap.",    image = "cap", emoji = "🧢", missing = "cap", decoys = listOf("cup", "cop")),
        FirstSentence(text = "Dad can nap in the van.", image = "van", emoji = "🚐", missing = "nap", decoys = listOf("net", "nip")),
        FirstSentence(text = "The rat ran and ran.",    image = "rat", emoji = "🐀", missing = "ran", decoys = listOf("run", "rat")),
        FirstSentence(text = "A fat cat has a hat.",    image = "hat", emoji = "🎩", missing = "hat", decoys = listOf("hot", "hut")),
        FirstSentence(text = "The map is in the bag.",  image = "bag", emoji = "🎒", missing = "bag", decoys = listOf("big", "bug")),
        FirstSentence(text = "Sam sat on a mat.",       image = "mat", emoji = "🟫", missing = "mat", decoys = listOf("met", "mop")),
    )),

    FirstSentenceGroup("shortE", "Short E", "🥚", Color(0xFF8E24AA), listOf(
        FirstSentence(text = "The hen is in a pen.",     image = "hen", emoji = "🐔", missing = "hen", decoys = listOf("hat", "hot")),
        FirstSentence(text = "Ted has a red pen.",       image = "pen", emoji = "🖊️", missing = "red", decoys = listOf("rod", "rid")),
        FirstSentence(text = "A wet pet is in the net.", image = "net", emoji = "🥅", missing = "wet", decoys = listOf("wig", "win")),
        FirstSentence(text = "Get the red bed.",         image = "bed", emoji = "🛏️", missing = "bed", decoys = listOf("bad", "bud")),
        FirstSentence(text = "Ten men can get the jet.", image = "jet", emoji = "✈️", missing = "jet", decoys = listOf("jam", "jog")),
        FirstSentence(text = "The pet has a wet leg.",   image = "leg", emoji = "🦵", missing = "leg", decoys = listOf("log", "lip")),
        FirstSentence(text = "The vet is in the den.",   image = "den", emoji = "🦉", missing = "den", decoys = listOf("dig", "dog")),
    )),

    FirstSentenceGroup("shortI", "Short I", "🍦", Color(0xFF1E88E5), listOf(
        FirstSentence(text = "A big pig can dig.",       image = "pig", emoji = "🐷", missing = "dig", decoys = listOf("dog", "bag")),
        FirstSentence(text = "Tim can sit and win.",     image = "win", emoji = "🏆", missing = "sit", decoys = listOf("sat", "set")),
        FirstSentence(text = "The pin is in the tin.",   image = "pin", emoji = "📌", missing = "tin", decoys = listOf("ten", "tan")),
        FirstSentence(text = "Kim has a big lip.",       image = "lip", emoji = "👄", missing = "lip", decoys = listOf("lap", "log")),
        FirstSentence(text = "The fig is in the tin.",   image = "fig", emoji = "🍇", missing = "fig", decoys = listOf("fog", "fun")),
        FirstSentence(text = "The kid can zip the bag.", image = "zip", emoji = "🤐", missing = "zip", decoys = listOf("zap", "top")),
        FirstSentence(text = "A big fin is in the bin.", image = "fin", emoji = "🦈", missing = "fin", decoys = listOf("fan", "fun")),
    )),

    FirstSentenceGroup("shortO", "Short O", "🐙", Color(0xFF00897B), listOf(
        FirstSentence(text = "The dog can run and hop.", image = "dog", emoji = "🐶", missing = "hop", decoys = listOf("hat", "hip")),
        FirstSentence(text = "A hot pot is on top.",     image = "pot", emoji = "🍯", missing = "pot", decoys = listOf("pit", "pat")),
        FirstSentence(text = "The fox has a box.",       image = "fox", emoji = "🦊", missing = "box", decoys = listOf("bag", "bug")),
        FirstSentence(text = "Tom got a mop.",           image = "mop", emoji = "🧹", missing = "mop", decoys = listOf("map", "mud")),
        FirstSentence(text = "The cod is in the pot.",   image = "cod", emoji = "🐟", missing = "cod", decoys = listOf("cap", "cup")),
        FirstSentence(text = "A log is on the top.",     image = "log", emoji = "🪵", missing = "log", decoys = listOf("leg", "lip")),
        FirstSentence(text = "The hog can hop and jog.", image = "hog", emoji = "🐗", missing = "hog", decoys = listOf("hug", "hen")),
    )),

    FirstSentenceGroup("shortU", "Short U", "☂️", Color(0xFFF4511E), listOf(
        FirstSentence(text = "A bug is on the rug.",     image = "bug", emoji = "🐛", missing = "rug", decoys = listOf("rag", "rig")),
        FirstSentence(text = "The sun is hot.",          image = "sun", emoji = "☀️", missing = "sun", decoys = listOf("sit", "sat")),
        FirstSentence(text = "Gus can run up.",          image = "run", emoji = "🏃", missing = "run", decoys = listOf("ran", "rat")),
        FirstSentence(text = "The pup is in the tub.",   image = "tub", emoji = "🛁", missing = "pup", decoys = listOf("pop", "pip")),
        FirstSentence(text = "The bus is in the mud.",   image = "bus", emoji = "🚌", missing = "mud", decoys = listOf("mad", "mop")),
        FirstSentence(text = "A nut is in the jug.",     image = "jug", emoji = "🫙", missing = "nut", decoys = listOf("net", "not")),
        FirstSentence(text = "The cub can hug and tug.", image = "cub", emoji = "🐻", missing = "hug", decoys = listOf("hog", "hat")),
    )),
)

val allFirstSentences: List<FirstSentence> = firstSentenceGroups.flatMap { it.sentences }

// ── Playing a line ────────────────────────────────────────────────────────────

/** Every word file for this module lives in the shared phonics_word folder. */
fun phonicsWordPath(word: String): String = "phonics_word/$word"

/**
 * Reads a whole sentence aloud.
 *
 * The line recordings are not in the assets yet, and until they are a "Hear it" button
 * that checks for one file and finds nothing simply does nothing — which reads as broken.
 * Every WORD is already recorded though, so the line falls back to speaking its words in
 * order. It upgrades itself the moment the whole-line file exists, with no code change.
 *
 * [onWord] fires as each word starts so the caller can travel a highlight along the line.
 * Cancel the calling coroutine to abandon a line mid-way.
 */
suspend fun playFirstSentence(
    audioManager: AudioPhonicsManager,
    sentence: FirstSentence,
    onWord: (Int?) -> Unit,
) {
    audioManager.stop()
    val words = sentence.words

    if (audioManager.audioExists(phonicsWordPath(sentence.audioKey))) {
        // one take of the whole line — the highlight just walks it
        audioManager.playPhonicsSound(phonicsWordPath(sentence.audioKey))
        for (i in words.indices) {
            onWord(i)
            delay(380)
        }
    } else {
        // word by word, in order — the same line, assembled from what we have
        for (i in words.indices) {
            onWord(i)
            val key = FirstSentence.key(words[i])
            if (audioManager.audioExists(phonicsWordPath(key))) {
                audioManager.playPhonicsSound(phonicsWordPath(key))
            }
            delay(520)
        }
    }
    onWord(null)
}
