package com.example.myapplication.main.age_group.from_5_to_7.vocabulary_building.category_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.data.model.VocabularyCategoryType
import com.example.myapplication.main.age_group.from_5_to_7.vocabulary_building.category_detail.view_model.VocabularyBuildingDetailViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.buttons.KidsIconButton
import com.example.myapplication.main.common.getImageResFromWord
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens40
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.KidIconSmall
import com.example.myapplication.ui.theme.AppDimens.VocabularyImageHeight
import com.example.myapplication.ui.theme.ButtonType


@Composable
fun VocabularyBuildingDetailPage(
    navController: NavController,
    type : String, title : String,
    viewModel: VocabularyBuildingDetailViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState
    LaunchedEffect(Unit) {
        viewModel.getDetailList(type)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundUI(isGreenGrassShow = false)
        Column(modifier = Modifier) {
            BackButtonWithText(
                title = title,
                onBackClick = { navController.popBackStack() }
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(uiState.words) { word ->
                    VocabularyWordCard(
                        viewModel = viewModel,
                        word = word,
                        categoryType = type
                    )
                }
            }
        }
    }
}

@Composable
fun VocabularyWordCard(
    word: String,
    categoryType: String,
    viewModel: VocabularyBuildingDetailViewModel
) {

    val isColorCategory = categoryType == VocabularyCategoryType.COLORS.name

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                viewModel.speak(word)
            }
            .background(viewModel.backgroundForCategory(word, categoryType))
            .padding(vertical = Dimens8)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens8),
            modifier = Modifier.fillMaxWidth()
        ) {

            if (!isColorCategory) {
                val res = getImageResFromWord(word)
                res?.let{
                    Image(
                        painter = painterResource(
                            id = res
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .height(VocabularyImageHeight)
                    )
                }
            }

            Text(
                text = word,
                style = if (isColorCategory) MaterialTheme.typography.headlineLarge else MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = when {
                    isColorCategory && word.lowercase() == "white" -> Color.Black
                    isColorCategory -> Color.White
                    else -> Color.Black
                },
                modifier = Modifier.padding(
                    vertical = if (isColorCategory) Dimens40 else 0.dp
                )
            )
        }

        // 🔊 Speaker icon (top-right)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = Dimens8)
                .background(Color.White.copy(alpha = 0.6f), CircleShape)
        ) {
            KidsIconButton(
                icon = Icons.AutoMirrored.Rounded.VolumeUp,
                onClick = { viewModel.speak(word) },
                type = viewModel.uiState.buttonType,
                size = KidIconSmall
            )
        }
    }
}
