package com.example.myapplication.main.age_group.phonics.l6_word_families.view_model

import androidx.compose.ui.graphics.Color

data class FamilyOnset(val onset: String, val word: String)

data class WordFamily(
    val rime: String,
    val rimeAudio: String,
    val color: Color,
    val shadowColor: Color,
    val onsets: List<FamilyOnset>
)

enum class WordFamilyHighlight { NONE, ONSET, RIME, WORD }

data class WordFamiliesUiState(
    val selectedFamily: WordFamily? = null,
    val selectedOnset: FamilyOnset? = null,
    val showOnsets: Boolean = false,
    val showEquation: Boolean = false,
    val highlight: WordFamilyHighlight = WordFamilyHighlight.NONE,
    val highlightHeaderRime: Boolean = false
)

val wordFamiliesData = listOf(
    WordFamily("at", "at", Color(0xFFE53935), Color(0xFFB71C1C), listOf(
        FamilyOnset("c","cat"), FamilyOnset("b","bat"), FamilyOnset("h","hat"),
        FamilyOnset("r","rat"), FamilyOnset("m","mat"), FamilyOnset("s","sat"),
        FamilyOnset("p","pat"), FamilyOnset("f","fat")
    )),
    WordFamily("an", "an", Color(0xFFF57C00), Color(0xFFE65100), listOf(
        FamilyOnset("c","can"), FamilyOnset("m","man"), FamilyOnset("f","fan"),
        FamilyOnset("r","ran"), FamilyOnset("p","pan"), FamilyOnset("t","tan"),
        FamilyOnset("v","van"), FamilyOnset("b","ban")
    )),
    WordFamily("og", "og", Color(0xFF388E3C), Color(0xFF1B5E20), listOf(
        FamilyOnset("d","dog"), FamilyOnset("l","log"), FamilyOnset("f","fog"),
        FamilyOnset("h","hog"), FamilyOnset("j","jog")
    )),
    WordFamily("en", "en", Color(0xFF0097A7), Color(0xFF006064), listOf(
        FamilyOnset("h","hen"), FamilyOnset("t","ten"), FamilyOnset("p","pen"),
        FamilyOnset("m","men"), FamilyOnset("d","den")
    )),
    WordFamily("ig", "ig", Color(0xFF7B1FA2), Color(0xFF4A148C), listOf(
        FamilyOnset("b","big"), FamilyOnset("p","pig"), FamilyOnset("d","dig"),
        FamilyOnset("w","wig"), FamilyOnset("j","jig"), FamilyOnset("f","fig")
    )),
    WordFamily("it", "it", Color(0xFF1565C0), Color(0xFF0D47A1), listOf(
        FamilyOnset("s","sit"), FamilyOnset("b","bit"), FamilyOnset("h","hit"),
        FamilyOnset("f","fit"), FamilyOnset("k","kit"), FamilyOnset("p","pit"),
        FamilyOnset("w","wit")
    )),
    WordFamily("ot", "ot", Color(0xFFE64A19), Color(0xFFBF360C), listOf(
        FamilyOnset("h","hot"), FamilyOnset("p","pot"), FamilyOnset("d","dot"),
        FamilyOnset("l","lot"), FamilyOnset("c","cot"), FamilyOnset("r","rot"),
        FamilyOnset("g","got")
    )),
    WordFamily("un", "un", Color(0xFFF9A825), Color(0xFFF57F17), listOf(
        FamilyOnset("s","sun"), FamilyOnset("r","run"), FamilyOnset("f","fun"),
        FamilyOnset("b","bun"), FamilyOnset("g","gun"), FamilyOnset("p","pun")
    )),
    WordFamily("ap", "ap", Color(0xFF00897B), Color(0xFF00695C), listOf(
        FamilyOnset("c","cap"), FamilyOnset("m","map"), FamilyOnset("n","nap"),
        FamilyOnset("t","tap"), FamilyOnset("l","lap"), FamilyOnset("g","gap")
    )),
    WordFamily("in", "in", Color(0xFF5E35B1), Color(0xFF4527A0), listOf(
        FamilyOnset("p","pin"), FamilyOnset("w","win"), FamilyOnset("f","fin"),
        FamilyOnset("t","tin"), FamilyOnset("b","bin")
    )),
    WordFamily("op", "op", Color(0xFFD81B60), Color(0xFFAD1457), listOf(
        FamilyOnset("t","top"), FamilyOnset("h","hop"), FamilyOnset("m","mop"),
        FamilyOnset("p","pop"), FamilyOnset("c","cop")
    )),
    WordFamily("ug", "ug", Color(0xFF6D4C41), Color(0xFF4E342E), listOf(
        FamilyOnset("b","bug"), FamilyOnset("r","rug"), FamilyOnset("h","hug"),
        FamilyOnset("m","mug"), FamilyOnset("d","dug"), FamilyOnset("j","jug")
    )),
)
