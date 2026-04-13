package com.example.myapplication.main.age_group.from_5_to_7.sight_words.view_model

import com.example.myapplication.main.age_group.from_5_to_7.sight_words.data.SightWord


data class SightWordsUiState(
    val words: List<SightWord> = emptyList(),
    val currentIndex : Int = 0
)
