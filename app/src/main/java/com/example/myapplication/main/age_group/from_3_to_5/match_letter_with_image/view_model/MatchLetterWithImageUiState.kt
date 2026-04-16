package com.example.myapplication.main.age_group.from_3_to_5.match_letter_with_image.view_model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.example.myapplication.R


data class MatchLetterWithImageUiState(
    val batchLetters: List<Pair<String, String>> = emptyList(),
    val shuffledImages: List<Pair<String, String>> = emptyList(),

    val draggingLetter: String? = null,

    val matchedLetters: Set<String> = emptySet(),
    val matchedOrder: List<String> = emptyList(),

    val letterPositions: Map<String, Offset> = emptyMap(),
    val imagePositions: Map<String, Offset> = emptyMap(),
    val imageRects: Map<String, Rect> = emptyMap(),
    val framesReady: Boolean = false,

    val round: Int = 1,
    val showPopup: Boolean = false,
    val feedbackTextRes: Int = R.string.feedbackPhrases_1,
    val feedbackSubTextRes: Int = R.string.match_letter_with_image_done
)
