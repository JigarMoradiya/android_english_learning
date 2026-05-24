package com.example.myapplication.main.age_group.from_3_to_5.fill_blank

import com.example.myapplication.R

enum class BlankPosition(
    val displayNameRes: Int,
    val preview: List<String?>   // null = blank slot, "?" = random
) {
    FIRST(  displayNameRes = R.string.fill_blank_position_before,  preview = listOf(null, "B", "C")),
    MIDDLE( displayNameRes = R.string.fill_blank_position_between,  preview = listOf("A", null, "C")),
    LAST(   displayNameRes = R.string.fill_blank_position_after,    preview = listOf("A", "B", null)),
    RANDOM( displayNameRes = R.string.fill_blank_position_surprise,  preview = listOf("?", "?", "?"));

    val subConfigName: String get() = when (this) {
        FIRST  -> "BEFORE"
        MIDDLE -> "BETWEEN"
        LAST   -> "AFTER"
        RANDOM -> "RANDOM"
    }

    val accentHex: String get() = when (this) {
        FIRST  -> "#5532D2"
        MIDDLE -> "#2AA65C"
        LAST   -> "#E8923A"
        RANDOM -> "#D63FAC"
    }
}
