package com.example.myapplication.main.age_group.phonics.l4_cvc_words.view_model

import androidx.compose.ui.graphics.Color

data class CvcSegment(val letter: String, val isVowel: Boolean)

data class CvcWordModel(
    val word: String,
    val segments: List<CvcSegment>,
    val hasImage: Boolean = false,
    val imageName: String = word
)

data class CvcGroup(
    val vowel: String,
    val label: String,
    val emoji: String,
    val color: Color,
    val words: List<CvcWordModel>
)

enum class CvcBoxPhase { HIDDEN, BOX1_IN, BOX2_IN, BOX3_IN, ALL_SHOWN, MERGING, MERGED }
enum class CvcPhonicsPhase { IDLE, INDIVIDUAL, BLENDING, COMPLETE }

data class CvcUiState(
    val selectedWord: CvcWordModel? = null,
    val boxPhase: CvcBoxPhase = CvcBoxPhase.HIDDEN,
    val highlightedIndex: Int = -1,
    val phonicsPhase: CvcPhonicsPhase = CvcPhonicsPhase.IDLE,
    val animateImage: Boolean = false
)

private val wordsWithImages = setOf("bat", "cat", "fan", "hat", "map", "rat", "hen", "pen", "pig", "dog", "pot", "sun")

private fun cvcWord(word: String): CvcWordModel {
    val vowels = setOf('a', 'e', 'i', 'o', 'u')
    val segs = word.map { c -> CvcSegment(c.toString(), c.lowercaseChar() in vowels) }
    return CvcWordModel(word, segs, hasImage = word in wordsWithImages)
}

val cvcGroups = listOf(
    CvcGroup("A", "Short A", "🍎", Color(0xFFE53935),
        listOf("bat", "cat", "fan", "hat", "man", "map", "rat", "tap").map { cvcWord(it) }),
    CvcGroup("E", "Short E", "🥚", Color(0xFF2E7D32),
        listOf("hen", "pen", "red", "ten").map { cvcWord(it) }),
    CvcGroup("I", "Short I", "🍦", Color(0xFF6A1B9A),
        listOf("big", "pig", "sit", "win").map { cvcWord(it) }),
    CvcGroup("O", "Short O", "🐙", Color(0xFFE65100),
        listOf("dog", "hot", "log", "pot").map { cvcWord(it) }),
    CvcGroup("U", "Short U", "☂️", Color(0xFF1565C0),
        listOf("bug", "cup", "run", "sun").map { cvcWord(it) }),
)
