package com.example.myapplication.main.age_group.from_3_to_5.abcd_with_images.view_model

import com.example.myapplication.ui.theme.ButtonType


data class ABCDWithImagesUiState(
    val currentIndex: Int = 0,
    val isNext: Boolean = true,

    val currentWord: String = "",
    val currentMatches: List<String> = emptyList(),
    val gradientType : ButtonType
)
