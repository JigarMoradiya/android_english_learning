package com.example.myapplication.main.age_group.from_5_to_7.articles_a_an.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticlesAAnViewModel @Inject constructor(
) : ViewModel() {

    var uiState by mutableStateOf(ArticlesAAnUiState())
        private set

}