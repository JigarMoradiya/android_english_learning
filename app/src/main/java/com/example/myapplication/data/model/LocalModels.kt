package com.example.myapplication.data.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color

data class LearningActivityModel(
    @StringRes val titleRes: Int,
    val img: Int,
    val destination: String,
    val txtColor : Color = Color(0xFF000000)
)

data class VocabularyCategoryData(
    val type: String,
    @StringRes val titleRes: Int,
    val imageName: Int,
    val words: List<String>
)