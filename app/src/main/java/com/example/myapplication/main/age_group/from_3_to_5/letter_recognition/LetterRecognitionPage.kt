package com.example.myapplication.main.age_group.from_3_to_5.letter_recognition

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.main.age_group.from_3_to_5.letter_recognition.view_model.LetterRecognitionViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.getImageResFromWord
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.PrimaryBlue
import com.example.myapplication.ui.theme.PrimaryOrange


@Composable
fun LetterRecognitionPage(
    navController: NavController,
    viewModel: LetterRecognitionViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState
    val letters = viewModel.lettersData

    val selectedItem = letters.find { it.first == uiState.selectedLetter }

    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundUI(isGreenGrassShow = false)
        Row(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {

            // -------------------------------
            // LEFT SIDE → GRID
            // -------------------------------
            Column(modifier = Modifier.weight(1f)) {
                BackButtonWithText(
                    title = stringResource(R.string.letter_recognition),
                    onBackClick = { navController.popBackStack() }
                )

                Spacer(modifier = Modifier)
                // THIS BOX WILL CENTER CONTENT
                Box(
                    modifier = Modifier
                        .weight(1f) // IMPORTANT
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    BoxWithConstraints(modifier = Modifier.padding(start = DeviceInfo.screenPadding())) {
                        val isTablet = maxWidth > 600.dp

                        val totalCols = if (isTablet) 5 else 7
                        val totalRows = if (isTablet) 6 else 4

                        val spacing = 8.dp

                        val boxSize = min(
                            (maxWidth - spacing * (totalCols - 1)) / totalCols,
                            (maxHeight - spacing * (totalRows - 1)) / totalRows
                        )

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(totalCols),
                            verticalArrangement = Arrangement.spacedBy(spacing),
                            horizontalArrangement = Arrangement.spacedBy(spacing),
                            contentPadding = PaddingValues(vertical = Dimens24),
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            itemsIndexed(letters) { index, item ->

                                val isSelected = uiState.selectedLetter == item.first

                                val scale by animateFloatAsState(
                                    targetValue = if (isSelected) 1.15f else 1f,
                                    label = "scale"
                                )

                                Box(
                                    modifier = Modifier
                                        .size(boxSize) // FIXED SIZE (VERY IMPORTANT)
                                        .graphicsLayer {
                                            scaleX = scale
                                            scaleY = scale
                                        }
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (isSelected) PrimaryOrange
                                            else PrimaryBlue.copy(alpha = 0.2f)
                                        )
                                        .clickable {
                                            viewModel.onLetterClick(item.first, item.second)
                                        },
                                    contentAlignment = Alignment.Center
                                ) {

                                    Text(
                                        text = item.first,
                                        fontSize = (boxSize.value * 0.6f).sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else Color.Black
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier)
            }

            // -------------------------------
            // RIGHT SIDE → DETAILS
            // -------------------------------
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {

                if (selectedItem != null) {

                    val letter = selectedItem.first
                    val word = selectedItem.second

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(Dimens16)
                    ) {

                        Text(
                            text = letter,
                            style = MaterialTheme.typography.displayLarge,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryOrange
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "for",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black.copy(alpha = 0.8f)
                            )
                            Text(
                                text = " $word",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black.copy(alpha = 0.8f)
                            )
                        }
                        Spacer(modifier = Modifier.height(Dimens16))
                        getImageResFromWord(word)?.let {
                            Image(
                                painter = painterResource(id = it),
                                contentDescription = word,
                                modifier = Modifier
                                    .fillMaxHeight(0.5f)
                            )
                        }
                    }

                } else {

                    Text(
                        text = stringResource(R.string.tap_a_letter),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}
