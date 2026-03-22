package com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.view_model

import androidx.compose.ui.geometry.Offset

data class LetterTracingUiState(
    val mode: LetterMode = LetterMode.UPPERCASE,
    val currentIndex: Int = 0,
    val strokeIndex: Int = 0,
    val progressIndex: Int = 0,
    val isOnStroke: Boolean = false,
    val drawnPoints: List<Offset> = emptyList(),
    val finishedStrokes: List<List<Offset>> = emptyList(),
    val isWaitingForNextStroke: Boolean = false,
)

enum class LetterMode(val title: String) {
    UPPERCASE("Uppercase"),
    LOWERCASE("Lowercase");

    fun displayString(index: Int): String {
        return when (this) {
            UPPERCASE -> {
                ('A' + index).toString()
            }
            LOWERCASE -> {
                val upper = ('A' + index)
                val lower = ('a' + index)
                "$upper$lower"
            }
        }
    }
}