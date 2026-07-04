package com.example.myapplication.main.age_group.from_3_to_5.letter_recognition

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import com.example.myapplication.main.age_group.from_3_to_5.letter_recognition.view_model.LetterRecognitionSegment
import com.example.myapplication.main.age_group.from_3_to_5.letter_recognition.view_model.LetterRecognitionViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.getImageResFromWord
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.LetterRecognitionLetterSize
import com.example.myapplication.ui.theme.AppDimens.isTablet
import com.example.myapplication.ui.theme.PrimaryBlue
import com.example.myapplication.ui.theme.PrimaryOrange
import com.example.myapplication.utils.extensions.scaled
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.ui.theme.AppDimens.Dimens20


@Composable
fun LetterRecognitionPage(
    navController: NavController,
    viewModel: LetterRecognitionViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState
    val letters = viewModel.lettersData

    val selectedItem = letters.find { it.first == uiState.selectedLetter }

    // Matches iOS's `.purple` and `.easeInOut(duration: 0.2)` so both platforms feel identical.
    val highlightPurple = Color(0xFFBF5AF2)
    val dimBlack = Color.Black.copy(alpha = 0.4f)
    val colorSpec = tween<Color>(durationMillis = 200, easing = FastOutSlowInEasing)
    val scaleSpec = tween<Float>(durationMillis = 200, easing = FastOutSlowInEasing)

    val letterColor by animateColorAsState(
        targetValue = if (uiState.spokenSegment == LetterRecognitionSegment.LETTER) highlightPurple else dimBlack,
        animationSpec = colorSpec,
        label = "letterColor"
    )
    val letterScale by animateFloatAsState(
        targetValue = if (uiState.spokenSegment == LetterRecognitionSegment.LETTER) 1.15f else 1f,
        animationSpec = scaleSpec,
        label = "letterScale"
    )
    val saysColor by animateColorAsState(
        targetValue = if (uiState.spokenSegment == LetterRecognitionSegment.SAYS) highlightPurple else dimBlack,
        animationSpec = colorSpec,
        label = "saysColor"
    )
    val saysScale by animateFloatAsState(
        targetValue = if (uiState.spokenSegment == LetterRecognitionSegment.SAYS) 1.15f else 1f,
        animationSpec = scaleSpec,
        label = "saysScale"
    )
    val soundColor by animateColorAsState(
        targetValue = if (uiState.spokenSegment == LetterRecognitionSegment.SOUND) highlightPurple else dimBlack,
        animationSpec = colorSpec,
        label = "soundColor"
    )
    val soundScale by animateFloatAsState(
        targetValue = if (uiState.spokenSegment == LetterRecognitionSegment.SOUND) 1.15f else 1f,
        animationSpec = scaleSpec,
        label = "soundScale"
    )

    Box(modifier = Modifier.fillMaxSize()) {

        KidsGradientBackground(gradient = KidsGradient.tealCyan, shape = KidsFloatingShape.musicNotes)
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
                    BoxWithConstraints(modifier = Modifier.padding(start = DeviceInfo.screenHorizontalPadding())) {

                        val totalCols = if (isTablet) 6 else 7
                        val totalRows = if (isTablet) 5 else 4

                        val spacing = Dimens8

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
                                        .clip(RoundedCornerShape(Dimens12))
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
                                        style = MaterialTheme.typography.headlineLarge.copy(
                                            fontSize = (boxSize.value * 0.7).sp,
                                        ),
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
                    val sound = selectedItem.third

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(Dimens16)
                    ) {

                        Text(
                            text = letter,
                            style = MaterialTheme.typography.displayLarge.scaled().copy(fontSize = LetterRecognitionLetterSize * 1.2f),
                            fontWeight = FontWeight.Bold,
                            color = letterColor,
                            modifier = Modifier.graphicsLayer {
                                scaleX = letterScale
                                scaleY = letterScale
                            }
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "says",
                                style = MaterialTheme.typography.displayMedium.scaled(),
                                fontWeight = FontWeight.Medium,
                                color = saysColor,
                                modifier = Modifier.graphicsLayer {
                                    scaleX = saysScale
                                    scaleY = saysScale
                                }
                            )
                            Text(
                                text = " ${sound.lowercase()}",
                                style = MaterialTheme.typography.displayMedium.scaled(),
                                fontWeight = FontWeight.Medium,
                                color = soundColor,
                                modifier = Modifier.graphicsLayer {
                                    scaleX = soundScale
                                    scaleY = soundScale
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(Dimens20))
                        getImageResFromWord(word)?.let {
                            Image(
                                painter = painterResource(id = it),
                                contentDescription = word,
                                modifier = Modifier
                                    .fillMaxHeight(0.5f)
                            )
                        }
                        Spacer(modifier = Modifier.height(Dimens20))
                        Text(
                            text = word,
                            style = MaterialTheme.typography.displaySmall.scaled(),
                            fontWeight = FontWeight.Medium,
                            color = Color.Black.copy(alpha = 0.8f)
                        )
                    }

                } else {

                    Text(
                        text = stringResource(R.string.tap_a_letter),
                        style = MaterialTheme.typography.headlineLarge.scaled(),
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}
