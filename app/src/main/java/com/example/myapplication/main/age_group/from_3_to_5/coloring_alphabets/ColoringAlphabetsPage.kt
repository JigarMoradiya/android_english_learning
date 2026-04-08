package com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Redo
import androidx.compose.material.icons.automirrored.rounded.Undo
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.components.ColoringBottomControls
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.components.ColoringCanvas
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model.ColoringAlphabetsViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.getImageResFromWord
import com.example.myapplication.ui.theme.AppDimens.ColoringAlphabetsImageWidth
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens40
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.ButtonType


@Composable
fun ColoringAlphabetsPage(
    navController: NavController,
    viewModel: ColoringAlphabetsViewModel = hiltViewModel()
) {

    val state = viewModel.uiState
    val item = state.items[state.currentIndex]

    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundUI(isGreenGrassShow = false)

        Column(modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                BackButtonWithText(
                    title = stringResource(R.string.coloring_alphabet),
                    modifier = Modifier.weight(1f),
                    onBackClick = { navController.popBackStack() }
                )

                Spacer(Modifier.weight(1f))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = Dimens16)
                ) {

                    KidsActionButton(
                        text = stringResource(R.string.undo),
                        icon = Icons.AutoMirrored.Rounded.Undo,
                        type = ButtonType.BLUE,
                        onClick = { viewModel.undo() },
                        isSmall = true
                    )

                    Spacer(modifier = Modifier.width(Dimens8))

                    KidsActionButton(
                        text = stringResource(R.string.redo),
                        icon = Icons.AutoMirrored.Rounded.Redo,
                        type = ButtonType.BLUE,
                        onClick = { viewModel.redo() },
                        isSmall = true
                    )

                    Spacer(modifier = Modifier.width(Dimens8))

                    // 🔥 CLEAR (existing)
                    KidsActionButton(
                        text = stringResource(R.string.clear),
                        icon = Icons.Rounded.Refresh,
                        type = ButtonType.BLUE,
                        onClick = { viewModel.clear() },
                        isSmall = true
                    )
                }
            }

            // 🔥 MAIN CONTENT
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                // 🟦 CANVAS CARD
                BoxWithConstraints(
                    contentAlignment = Alignment.Center
                ) {

                    val size = min(maxWidth, maxHeight) // make it square

                    Box(
                        modifier = Modifier
                            .height(size).width(size + (size * 0.20f))
                            .shadow(8.dp, RoundedCornerShape(24.dp))
                            .background(Color(0xFFEFEFEF), RoundedCornerShape(24.dp))
                            .padding(16.dp)
                    ) {
                        val res = getImageResFromWord(item.outlineImageName)
                        ColoringCanvas(
                            res = res ?: R.drawable.a_outline_c,
                            outlineName = item.outlineImageName,
                            strokes = state.strokes,
                            viewModel = viewModel
                        )
                    }
                }

                Spacer(modifier = Modifier.width(Dimens40))

                // RIGHT SIDE (IMAGE + WORD)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    val res = getImageResFromWord(item.word)
                    res?.let {
                        Image(
                            painter = painterResource(id = res),
                            contentDescription = null,
                            modifier = Modifier.width(ColoringAlphabetsImageWidth)
                        )

                        Spacer(modifier = Modifier.height(Dimens12))
                    }

                    Text(
                        text = item.word,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            ColoringBottomControls(
                state = state,
                onPrevious = { viewModel.previous() },
                onNext = { viewModel.next() },
                onColorSelect = { viewModel.selectColor(it) },
                onEraserSelect = { viewModel.selectEraser() }
            )
        }
    }
}