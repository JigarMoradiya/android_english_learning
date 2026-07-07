package com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.rounded.Redo
import androidx.compose.material.icons.automirrored.rounded.Undo
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.components.ColoringCanvas
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.components.ColoringColorRail
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.components.LetterPickerRow
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model.ColoringAlphabetsViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.getImageResForAlphabet
import com.example.myapplication.main.common.getImageResForLetterArrow
import com.example.myapplication.main.common.getImageResFromWord
import com.example.myapplication.ui.theme.AppDimens.AlphabetTracingArrowImageHeight
import com.example.myapplication.ui.theme.AppDimens.AlphabetTracingLetterSize
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens32
import com.example.myapplication.ui.theme.AppDimens.Dimens50
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.DimensColorCircles
import com.example.myapplication.ui.theme.AppDimens.isLargeTablet
import com.example.myapplication.ui.theme.AppDimens.isTablet
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground


@Composable
fun ColoringAlphabetsPage(
    navController: NavController,
    viewModel: ColoringAlphabetsViewModel = hiltViewModel()
) {

    val state = viewModel.uiState
    val item = state.items[state.currentIndex]

    Box(modifier = Modifier.fillMaxSize()) {

        KidsGradientBackground(gradient = KidsGradient.lilacPink, shape = KidsFloatingShape.sparkles)

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

                    Spacer(modifier = Modifier.width(Dimens8))

                    KidsActionButton(
                        text = stringResource(R.string.eraser),
                        icon = Icons.Default.AutoFixHigh,
                        type = ButtonType.RED,
                        onClick = { viewModel.selectEraser() },
                        isSmall = !state.isEraser
                    )
                }
            }

            // 🔥 MAIN CONTENT — canvas centered, letter/word centered in side spaces (same as letter tracing)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {

                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {

                    val minusExtraSpace = if (isLargeTablet) 100.dp else if (isTablet) 60.dp else 0.dp
                    val size = min(maxWidth - minusExtraSpace, maxHeight - minusExtraSpace)
                    val canvasWidth = size + size * 0.20f

                    // Every position below is computed from this single
                    // BoxWithConstraints, in explicit pixels — NOT from
                    // Row/weight() distribution. weight(1f) on both the
                    // letter panel AND the word+image panel meant word+image
                    // was really centered in "half of whatever's left after
                    // the canvas and rail", a number that depends on the
                    // letter panel too — not specifically the gap between
                    // the canvas and the rail. Explicit offsets fix that.
                    val railWidth = DimensColorCircles + Dimens32 + Dimens6 * 3
                    val railMarginEnd = Dimens16
                    val wordImageWidth = if (isLargeTablet) 250.dp else if (isTablet) 210.dp else 170.dp
                    val letterWidth = if (isLargeTablet) 240.dp else if (isTablet) 200.dp else 160.dp

                    val railX = maxWidth - railWidth - railMarginEnd
                    // Canvas centered in the space to the left of the rail
                    val canvasX = (railX - canvasWidth) / 2
                    val canvasRightEdge = canvasX + canvasWidth

                    // Word + image centered exactly between the canvas's
                    // right edge and the rail's left edge — nothing else.
                    val gapCenterX = (canvasRightEdge + railX) / 2
                    val wordImageX = gapCenterX - wordImageWidth / 2

                    // Letter panel centered in the space to the left of canvas
                    val letterX = canvasX / 2 - letterWidth / 2

                    // LEFT — letter (image or text), tap to play phonics sound
                    val phonicsColor by animateColorAsState(
                        targetValue = if (viewModel.isPhonicsHighlighted) MaterialTheme.colorScheme.primary else Color(0xFF9E9E9E),
                        animationSpec = tween(200),
                        label = "phonicsColor"
                    )
                    val phonicsScale by animateFloatAsState(
                        targetValue = if (viewModel.isPhonicsHighlighted) 1.2f else 1f,
                        animationSpec = tween(200),
                        label = "phonicsScale"
                    )

                    Box(
                        modifier = Modifier
                            .offset(x = letterX)
                            .width(letterWidth)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { viewModel.playPhonicsSound() }
                        ) {
                            val arrowRes = getImageResForLetterArrow("${item.letter.lowercase()}_arrow")
                            if (arrowRes != null) {
                                Image(
                                    painter = painterResource(id = arrowRes),
                                    contentDescription = item.letter,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.height(AlphabetTracingArrowImageHeight)
                                )
                            } else {
                                Text(
                                    text = item.letter,
                                    color = Color.Black,
                                    fontSize = AlphabetTracingLetterSize,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Box(
                                modifier = Modifier.height(Dimens50),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = viewModel.phonicsSound,
                                    color = phonicsColor,
                                    style = MaterialTheme.typography.bodyLarge.scaled(),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.graphicsLayer(scaleX = phonicsScale, scaleY = phonicsScale)
                                )
                            }
                        }
                    }

                    // 🟦 CANVAS CARD (fixed size, positioned explicitly)
                    Box(
                        modifier = Modifier
                            .offset(x = canvasX)
                            .height(size).width(canvasWidth)
                            .shadow(Dimens8, RoundedCornerShape(Dimens24))
                            .background(Color(0xFFEFEFEF), RoundedCornerShape(Dimens24))
                            .padding(Dimens16)
                    ) {
                        val res = getImageResForAlphabet(item.outlineImageName)
                        ColoringCanvas(
                            res = res ?: R.drawable.a_outline_c,
                            outlineName = item.outlineImageName,
                            strokes = state.strokes,
                            viewModel = viewModel
                        )
                    }

                    // Word + image — centered exactly between canvas and rail.
                    Box(
                        modifier = Modifier
                            .offset(x = wordImageX)
                            .width(wordImageWidth)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            val res = getImageResFromWord(item.word)
                            res?.let {
                                Image(
                                    painter = painterResource(id = res),
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.fillMaxHeight(0.32f)
                                )

                                Spacer(modifier = Modifier.height(Dimens8))
                            }

                            Text(
                                text = item.word,
                                style = MaterialTheme.typography.headlineSmall.scaled(),
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Color rail — flush against the right edge of the
                    // screen, vertical strip instead of a full-width bottom
                    // bar since landscape has more spare width than height.
                    ColoringColorRail(
                        state = state,
                        onBrushSelect = { brush, style -> viewModel.selectBrush(brush, style) },
                        modifier = Modifier
                            .offset(x = railX)
                            .width(railWidth)
                            .height(size)
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens8))

            // Previous / A-Z picker / Next — one small strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DeviceInfo.screenHorizontalPadding())
                    .padding(bottom = Dimens16),
                verticalAlignment = Alignment.CenterVertically
            ) {
                KidsActionButton(
                    text = stringResource(R.string.previous),
                    icon = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                    type = ButtonType.ORANGE,
                    onClick = { viewModel.previous() },
                    isSmall = true
                )

                Spacer(modifier = Modifier.width(Dimens8))

                Box(modifier = Modifier.weight(1f)) {
                    LetterPickerRow(
                        items = state.items,
                        currentIndex = state.currentIndex,
                        onSelect = { viewModel.jumpToLetter(it) }
                    )
                }

                Spacer(modifier = Modifier.width(Dimens8))

                KidsActionButton(
                    text = stringResource(R.string.next),
                    icon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    type = ButtonType.ORANGE,
                    onClick = { viewModel.next() },
                    isSmall = true,
                    isIconStart = false
                )
            }
        }
    }
}