package com.example.myapplication.main.age_group.from_5_to_7.article_choice

/**
 * "a" vs "an" is decided by the first SOUND of the word, not its first letter:
 * silent-h words ("hour") take "an"; vowel-letter words that start with a
 * consonant sound like "yoo" ("unicorn") or "wa" ("one") take "a".
 */
object ArticleRule {

    // Consonant letter, but silent 'h' → vowel sound → "an"
    private val silentHPrefixes = listOf("hour", "honest", "honor", "honour", "heir")

    // Vowel letter, but consonant sound ("yoo…", "wa…") → "a"
    private val consonantSoundPrefixes = listOf(
        "uni", "use", "usu", "usa", "ute", "uku", "ufo", "eu", "one", "once"
    )

    fun needsAn(word: String): Boolean {
        val w = word.trim().lowercase()
        val firstChar = w.firstOrNull() ?: return false
        if (silentHPrefixes.any { w.startsWith(it) }) return true
        if (consonantSoundPrefixes.any { w.startsWith(it) }) return false
        return "aeiou".contains(firstChar)
    }
}
