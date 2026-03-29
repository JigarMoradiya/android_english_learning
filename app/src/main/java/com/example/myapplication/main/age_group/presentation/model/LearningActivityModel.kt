package com.example.myapplication.main.age_group.presentation.model

import androidx.annotation.StringRes
import com.example.myapplication.R
import com.example.myapplication.main.base.nav.RouteNavigation

data class LearningActivityModel(
    @StringRes val titleRes: Int,
    val destination: String
)

// List of activities
val activities_age_3_5 = listOf(
    LearningActivityModel(R.string.alphabet_tracing, RouteNavigation.AlphabetTracing.name),
    LearningActivityModel(R.string.letter_recognition, RouteNavigation.LetterRecognition.name),
    LearningActivityModel(R.string.abcd_with_images, RouteNavigation.ABCDWithImages.name),
    LearningActivityModel(R.string.match_letters, RouteNavigation.MatchLetters.name),
    LearningActivityModel(R.string.match_letter_image, RouteNavigation.MatchLetterWithImage.name),
    LearningActivityModel(R.string.coloring_alphabet, "coloring_alphabet"),
    LearningActivityModel(R.string.menu_missing_letter, "menu_missing_letter"),
    LearningActivityModel(R.string.drag_drop_words, "drag_drop_words"),
)