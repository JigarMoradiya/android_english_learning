package com.example.myapplication.main.age_group.from_5_to_7.articles_a_an.example.view_model

import com.example.myapplication.main.age_group.from_5_to_7.articles_a_an.info.data.ArticleItem
import com.example.myapplication.ui.theme.ButtonColors
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.ui.theme.getButtonColors


data class ArticlesAAnExampleUiState(
    val mode: ArticleMode = ArticleMode.A,
    val examplesA: List<ArticleItem> = emptyList(),
    val examplesAn: List<ArticleItem> = emptyList(),
//    var buttonType: ButtonType = randomType,
    var buttonType: ButtonType = ButtonType.BLUE,
    var cardColors: ButtonColors = getButtonColors(buttonType)
)

enum class ArticleMode(val label: String) {
    A("A"),
    AN("An")
}