package com.example.myapplication.main.age_group.presentation.model

import androidx.annotation.StringRes
import com.example.myapplication.R

data class LearningActivityModel(
    @StringRes val titleRes: Int,
    val destination: String
)

// List of activities
val activities_age_3_5 = listOf(
    LearningActivityModel(R.string.alphabet_tracking, "alphabet_tracking"),
    LearningActivityModel(R.string.letter_recognition, "letter_recognition"),
    LearningActivityModel(R.string.abcd_images, "abcd_images"),
    LearningActivityModel(R.string.match_letters, "match_letters"),
    LearningActivityModel(R.string.match_letter_image, "match_letter_image"),
    LearningActivityModel(R.string.coloring_alphabet, "coloring_alphabet"),
    LearningActivityModel(R.string.menu_missing_letter, "menu_missing_letter"),
    LearningActivityModel(R.string.drag_drop_words, "drag_drop_words"),
)