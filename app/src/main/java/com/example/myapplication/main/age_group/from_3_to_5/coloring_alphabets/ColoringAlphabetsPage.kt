package com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.rounded.Redo
import androidx.compose.material.icons.automirrored.rounded.Undo
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.rounded.Redo
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Undo
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.components.ColoringCanvas
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model.ColoringAlphabetsViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.buttons.KidsIconButton
import com.example.myapplication.main.common.getImageResFromWord
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens28
import com.example.myapplication.ui.theme.AppDimens.DimensColorCircles
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.ToolbarIconSize
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

                    // 🔥 UNDO
//                    KidsIconButton(
//                        icon = Icons.AutoMirrored.Rounded.Undo,
//                        onClick = { viewModel.undo() },
//                        type = ButtonType.PINK,
//                        size = ToolbarIconSize
//                    )
                    KidsActionButton(
                        text = stringResource(R.string.undo),
                        icon = Icons.AutoMirrored.Rounded.Undo,
                        type = ButtonType.PINK,
                        onClick = { viewModel.undo() },
                        isSmall = true
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // 🔥 REDO
//                    KidsIconButton(
//                        icon = Icons.AutoMirrored.Rounded.Redo,
//                        onClick = { viewModel.redo() },
//                        type = ButtonType.PINK,
//                        size = ToolbarIconSize
//                    )
                    KidsActionButton(
                        text = stringResource(R.string.redo),
                        icon = Icons.AutoMirrored.Rounded.Redo,
                        type = ButtonType.PINK,
                        onClick = { viewModel.redo() },
                        isSmall = true
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // 🔥 CLEAR (existing)
                    KidsActionButton(
                        text = stringResource(R.string.clear),
                        icon = Icons.Rounded.Refresh,
                        type = ButtonType.PINK,
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
                Box(
                    modifier = Modifier
                        .size(320.dp)
                        .shadow(8.dp, RoundedCornerShape(24.dp))
                        .background(Color(0xFFEFEFEF), RoundedCornerShape(24.dp))
                        .padding(16.dp)
                ) {
                    val res = getImageResFromWord(item.outlineImageName)
                    ColoringCanvas(
                        res = res?:R.drawable.a_outline_circle,
                        outlineName = item.outlineImageName,
                        strokes = state.strokes,
                        viewModel = viewModel
                    )
                }

                Spacer(modifier = Modifier.width(40.dp))

                // 🍎 RIGHT SIDE (IMAGE + WORD)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    val res = getImageResFromWord(item.word)
                    res?.let {
                        Image(
                            painter = painterResource(id = res),
                            contentDescription = null,
                            modifier = Modifier.size(140.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Text(
                        text = item.word,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // 🎨 BOTTOM CONTROLS
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = DeviceInfo.screenPadding(), end = Dimens16)
                    .padding(bottom = Dimens16, top = Dimens8),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {


                KidsActionButton(
                    text = stringResource(R.string.previous),
                    icon = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                    type = ButtonType.ORANGE,
                    onClick = {
                        viewModel.previous()
                    }
                )

                val colors = state.colors
                val selectedColor = state.selectedColor
                val isEraser = state.isEraser

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = Dimens12)
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // 🔥 ERASER FIRST
                    val isSelectedEraser = isEraser

                    val eraserContainerSize = DimensColorCircles
                    val eraserInnerSize by animateDpAsState(
                        targetValue = if (isSelectedEraser) DimensColorCircles else Dimens28,
                        label = ""
                    )
                    val eraserIconSize by animateDpAsState(
                        targetValue = if (isSelectedEraser) Dimens20 else Dimens16,
                        label = ""
                    )

                    Box(
                        modifier = Modifier
                            .size(eraserContainerSize),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(eraserInnerSize)
                                .clip(CircleShape)
                                .background(Color.White, CircleShape)
                                .border(
                                    if (isSelectedEraser) Dimens2 else 1.dp,
                                    Color.Black,
                                    CircleShape
                                )
                                .clickable {
                                    viewModel.selectEraser()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoFixHigh,
                                contentDescription = "Eraser",
                                tint = Color.Black,
                                modifier = Modifier.size(eraserIconSize)
                            )
                        }
                    }

                    // 🔥 COLORS
                    colors.forEach { color ->

                        val isSelected = !isEraser && color == selectedColor

                        val innerSize by animateDpAsState(
                            targetValue = if (isSelected) DimensColorCircles else Dimens28,
                            label = ""
                        )

                        Box(
                            modifier = Modifier
                                .size(DimensColorCircles),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(innerSize)
                                    .clip(CircleShape)
                                    .background(color, CircleShape)
                                    .then(
                                        if (isSelected) {
                                            Modifier.border(Dimens2, Color.Black, CircleShape)
                                        } else Modifier
                                    )
                                    .clickable {
                                        viewModel.selectColor(color)
                                    }
                            )
                        }
                    }
                }

                KidsActionButton(
                    text = stringResource(R.string.next),
                    icon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    type = ButtonType.ORANGE,
                    onClick = { viewModel.next() },
                    isIconStart = false
                )
            }
        }
    }
}