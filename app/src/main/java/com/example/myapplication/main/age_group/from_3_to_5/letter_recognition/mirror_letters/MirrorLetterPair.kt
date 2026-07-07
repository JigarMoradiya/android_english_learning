package com.example.myapplication.main.age_group.from_3_to_5.letter_recognition.mirror_letters

import androidx.compose.ui.graphics.Color

// The 4 letter pairs kids commonly mix up. Not all confused the same way:
// b/d and p/q are left-right mirrors (flip across a vertical axis), while
// m/w and n/u are upside-down flips (flip across a horizontal axis) — the
// intro animation rotates on a different axis per pair so it always shows
// the real transformation, never a misleading one.
enum class MirrorLetterPair(val first: Char, val second: Char, val title: String, val accentColor: Color) {
    BD('b', 'd', "b & d", Color(0xFF3949AB)),
    PQ('p', 'q', "p & q", Color(0xFF00897B)),
    MW('m', 'w', "m & w", Color(0xFF8E24AA)),
    NU('n', 'u', "n & u", Color(0xFFE67639));

    // Rotation axis for the flip animation: true = upside-down flip (rotate on X),
    // false = left-right mirror (rotate on Y)
    val isUpsideDownFlip: Boolean
        get() = this == MW || this == NU

    val subConfigName: String
        get() = name
}
