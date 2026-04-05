package com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model

import java.util.UUID

data class ColoringAlphabetModel(
    val id: String = UUID.randomUUID().toString(),
    val letter: String,
    val word: String,
    val outlineImageName: String,
    val audioName: String?
)