package com.example.myapplication.main.age_group.from_5_to_7.articles_a_an.example

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_5_to_7.articles_a_an.example.view_model.ArticleMode
import com.example.myapplication.main.age_group.from_5_to_7.articles_a_an.example.view_model.ArticlesAAnExampleViewModel
import com.example.myapplication.main.age_group.from_5_to_7.vocabulary_building.category_detail.components.WordCard
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import java.util.Locale


@Composable
fun ArticlesAAnExamplePage(
    navController: NavController,
    viewModel: ArticlesAAnExampleViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState
    val cardColors = viewModel.backgroundForCategory()

    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundUI(isGreenGrassShow = false)
        Column(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                BackButtonWithText(
                    title = stringResource(R.string.articles_a_an),
                    onBackClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f)
                )

                KidsArticleToggle(
                    uiState = uiState,
                    cardColors = cardColors,
                    onModeChange = { viewModel.changeMode(it) }
                )
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(Dimens12),
                verticalArrangement = Arrangement.spacedBy(Dimens12),
                horizontalArrangement = Arrangement.spacedBy(Dimens12)
            ) {

                items(if (uiState.mode == ArticleMode.A)uiState.examplesA else uiState.examplesAn) { item ->
                    WordCard(
                        buttonType = uiState.buttonType,
                        word = "${item.article} ${item.word}".lowercase(Locale.ROOT),
                        img = item.word,
                        bgColor = cardColors.base.copy(alpha = 0.2f),
                        isColorCategory = false,
                        onSpeakClick = {
                            viewModel.speak("${item.article} ${item.word}".lowercase(Locale.ROOT))
                        }
                    )
                }
            }
        }
    }
}

