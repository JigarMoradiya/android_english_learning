package com.example.myapplication.main.age_group.phonics.l3_blending.view_model

data class BlendSegment(val letters: String, val soundFile: String, val isVowel: Boolean)

data class BlendingWordModel(
    val word: String,
    val isVC: Boolean,
    val segments: List<BlendSegment>,
    val audioFileName: String
)

enum class BlendBoxPhase { HIDDEN, BOX1_IN, BOX2_IN, ALL_SHOWN, MERGING, MERGED }
enum class BlendPhonicsPhase { IDLE, INDIVIDUAL, BLENDING, COMPLETE }

data class BlendingUiState(
    val selectedWord: BlendingWordModel? = null,
    val boxPhase: BlendBoxPhase = BlendBoxPhase.HIDDEN,
    val highlightedIndex: Int = -1,
    val phonicsPhase: BlendPhonicsPhase = BlendPhonicsPhase.IDLE,
    val isVC: Boolean = true
)

val vcBlendingWords = listOf(
    BlendingWordModel("at",  true, listOf(BlendSegment("a","sound_a",true), BlendSegment("t","sound_t",false)), "at"),
    BlendingWordModel("an",  true, listOf(BlendSegment("a","sound_a",true), BlendSegment("n","sound_n",false)), "an"),
    BlendingWordModel("am",  true, listOf(BlendSegment("a","sound_a",true), BlendSegment("m","sound_m",false)), "am"),
    BlendingWordModel("in",  true, listOf(BlendSegment("i","sound_i",true), BlendSegment("n","sound_n",false)), "in"),
    BlendingWordModel("it",  true, listOf(BlendSegment("i","sound_i",true), BlendSegment("t","sound_t",false)), "it"),
    BlendingWordModel("ox",  true, listOf(BlendSegment("o","sound_o",true), BlendSegment("x","sound_x",false)), "ox"),
    BlendingWordModel("up",  true, listOf(BlendSegment("u","sound_u",true), BlendSegment("p","sound_p",false)), "up"),
    BlendingWordModel("us",  true, listOf(BlendSegment("u","sound_u",true), BlendSegment("s","sound_s",false)), "us"),
)

val cvBlendingWords = listOf(
    BlendingWordModel("ba", false, listOf(BlendSegment("b","sound_b",false), BlendSegment("a","sound_a",true)), "ba"),
    BlendingWordModel("ma", false, listOf(BlendSegment("m","sound_m",false), BlendSegment("a","sound_a",true)), "ma"),
    BlendingWordModel("me", false, listOf(BlendSegment("m","sound_m",false), BlendSegment("e","sound_e",true)), "me"),
    BlendingWordModel("we", false, listOf(BlendSegment("w","sound_w",false), BlendSegment("e","sound_e",true)), "we"),
    BlendingWordModel("go", false, listOf(BlendSegment("g","sound_g",false), BlendSegment("o","sound_o",true)), "go"),
    BlendingWordModel("no", false, listOf(BlendSegment("n","sound_n",false), BlendSegment("o","sound_o",true)), "no"),
    BlendingWordModel("si", false, listOf(BlendSegment("s","sound_s",false), BlendSegment("i","sound_i",true)), "si"),
    BlendingWordModel("do", false, listOf(BlendSegment("d","sound_d",false), BlendSegment("o","sound_o",true)), "do"),
)
