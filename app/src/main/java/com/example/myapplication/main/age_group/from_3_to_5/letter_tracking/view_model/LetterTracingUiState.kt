package com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.view_model

import androidx.compose.ui.geometry.Offset

data class LetterTracingUiState(
    val mode: LetterMode = LetterMode.UPPERCASE, // 👈 ADD THIS
    val strokeIndex: Int = 0,
    val progressIndex: Int = 0,
    val isOnStroke: Boolean = false,
    val drawnPoints: List<Offset> = emptyList(),
    val finishedStrokes: List<List<Offset>> = emptyList()
)

enum class LetterMode(val title: String) {
    UPPERCASE("Uppercase"),
    LOWERCASE("Lowercase")
}