package com.example.myapplication.main.age_group.from_5_to_7.word_match_picture.view_model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.example.myapplication.R


data class WordMatchImageUiState(
    val batchLetters: List<Pair<String, String>> = emptyList(),
    val shuffledImages: List<Pair<String, String>> = emptyList(),

    val draggingLetter: String? = null,

    val matchedLetters: Set<String> = emptySet(),
    val matchedOrder: List<String> = emptyList(),
    // Words whose image the kid has gotten wrong twice this round — shows
    // a pulsing hint on the correct image cell (doesn't auto-solve).
    val hintedLetters: Set<String> = emptySet(),

    val letterPositions: Map<String, Offset> = emptyMap(),
    val imagePositions: Map<String, Offset> = emptyMap(),
    val imageRects: Map<String, Rect> = emptyMap(),
    val framesReady: Boolean = false,

    val round: Int = 1,
    val showPopup: Boolean = false,
    val feedbackTextRes: Int = R.string.feedbackPhrases_1,
    val feedbackSubTextRes: Int = R.string.feedbackMatchLetterSubtitle_1,

    val batchScore: Int = 0,
    val earnedStars: Int = 1,
    val scoreLabel: String = ""
)
