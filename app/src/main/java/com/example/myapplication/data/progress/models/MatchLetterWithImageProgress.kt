package com.example.myapplication.data.progress.models

data class MatchLetterWithImageProgress(
    val totalRoundsCompleted: Int = 0,
    val weakLetterIndices: List<Int> = emptyList(),
    val masteredLetterIndices: List<Int> = emptyList()
)
