package com.example.myapplication.main.age_group.phonics.worksheets

import androidx.annotation.DrawableRes
import com.example.myapplication.data.generation.letter.ImageWordMapper
import com.example.myapplication.data.generation.letter.LetterData
import com.example.myapplication.data.generation.letter.LetterRepository

/**
 * Bridges the app's alphabet data to the worksheets. `LetterRepository` already holds,
 * for all 26 letters, the main word, the alternates and the phonics sound the app
 * teaches — worksheets read the same source so paper and screen never teach different
 * words. Mirrors iOS `WorksheetLetters`.
 */
object WorksheetLetters {

    data class PictureWord(val word: String, @DrawableRes val imageRes: Int)

    fun data(letter: String): LetterData? =
        LetterRepository.all.firstOrNull { it.letter.equals(letter, ignoreCase = true) }

    /**
     * "Aaa Aaa Aaa" → "aaa". The app repeats it three times for the spoken animation;
     * a printed page needs it once.
     */
    fun sound(letter: String): String {
        val raw = data(letter)?.phonicsSound ?: return ""
        return raw.split(" ").firstOrNull()?.lowercase().orEmpty()
    }

    /** Words for a letter that actually have a picture, main word first. */
    fun pictureWords(letter: String, limit: Int): List<PictureWord> {
        val d = data(letter) ?: return emptyList()
        return (listOf(d.mainWord) + d.altWords)
            .mapNotNull { w -> ImageWordMapper.get(w.lowercase())?.let { PictureWord(w, it) } }
            .take(limit)
    }

    /** The first picture word for a letter, or null if it has no artwork. */
    fun mainPicture(letter: String): PictureWord? = pictureWords(letter, limit = 1).firstOrNull()

    /**
     * The picture word to use when the letter's SOUND is printed beside it. Normally
     * the main word, but a few main words do not start with the sound the letter is
     * taught to say (Icecream opens on long /ai/, not short /i/), so those are
     * overridden. Mirrors iOS `soundMatchedPicture`.
     */
    fun soundMatchedPicture(letter: String): PictureWord? {
        soundMatchedWord[letter.lowercase()]?.let { w ->
            ImageWordMapper.get(w.lowercase())?.let { return PictureWord(w, it) }
        }
        return mainPicture(letter)
    }

    private val soundMatchedWord: Map<String, String> = mapOf(
        "i" to "Igloo"   // Icecream is /ai/, not the short /i/ this letter says
    )
}
