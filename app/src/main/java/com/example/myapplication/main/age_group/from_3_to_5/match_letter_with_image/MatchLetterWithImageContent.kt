package com.example.myapplication.main.age_group.from_3_to_5.match_letter_with_image

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.main.age_group.from_3_to_5.match_letter_with_image.components.MatchWithImagesGrid
import com.example.myapplication.main.age_group.from_3_to_5.match_letter_with_image.components.drawDragConnection
import com.example.myapplication.main.age_group.from_3_to_5.match_letter_with_image.components.drawMatchedConnections
import com.example.myapplication.main.age_group.from_3_to_5.match_letter_with_image.view_model.MatchLetterWithImageViewModel
import com.example.myapplication.main.common.getImageResFromWord
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.MatchLetterBoxSize
import com.example.myapplication.ui.theme.PrimaryGreen

@Composable
fun MatchContent(
    viewModel: MatchLetterWithImageViewModel,
    modifier: Modifier = Modifier
) {

    val uiState = viewModel.uiState

    // ✅ ROOT COORDINATE HOLDER
    var rootCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = Dimens8)
            .onGloballyPositioned { rootCoords = it }, // 🔥 IMPORTANT
        contentAlignment = Alignment.Center
    ) {

        // -------------------------
        // CANVAS (LINES)
        // -------------------------
        Canvas(modifier = Modifier.matchParentSize()) {

            // ✅ Matched lines
            drawMatchedConnections(
                matchedOrder = uiState.matchedOrder,
                letterPositions = uiState.letterPositions,
                imagePositions = uiState.imagePositions,
                getColor = viewModel::getLineColor
            )

            // ✅ Drag line
            drawDragConnection(
                start = viewModel.dragStart,
                end = viewModel.dragEnd,
                color = Color.Black
            )
        }

        // -------------------------
        // CONTENT
        // -------------------------
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // -------------------------
            // LETTERS
            // -------------------------
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens16)) {

                uiState.batchLetters.forEach { (letter, _) ->

                    val isMatched = uiState.matchedLetters.contains(letter)

                    Box(
                        modifier = Modifier.size(MatchLetterBoxSize),
                        contentAlignment = Alignment.Center
                    ) {

                        // MAIN CARD
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clip(RoundedCornerShape(Dimens16))
                                .background(viewModel.getLetterColor(letter))
                                .graphicsLayer {
                                    alpha = if (isMatched) 0.4f else 1f
                                }
                                .onGloballyPositioned { coords ->

                                    val root = rootCoords ?: return@onGloballyPositioned

                                    // 🔥 FIX: SAME COORDINATE SYSTEM
                                    val pos = root.localPositionOf(coords, Offset.Zero)
                                    val size = coords.size

                                    val bottomCenter = Offset(
                                        pos.x + size.width / 2f,
                                        pos.y + size.height.toFloat()
                                    )

                                    viewModel.updateLetterPosition(letter, bottomCenter)
                                }
                                .pointerInput(letter,isMatched) {
                                    if (isMatched) return@pointerInput // ❌ disable drag
                                    detectDragGestures(
                                        onDragStart = {
                                            val start = uiState.letterPositions[letter]
                                                ?: return@detectDragGestures
                                            viewModel.startDrag(letter, start)
                                        },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            viewModel.updateDrag(dragAmount)
                                        },
                                        onDragEnd = {
                                            viewModel.endDrag()
                                        }
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = letter,
                                color = Color.White,
                                fontSize = (MatchLetterBoxSize.value * 0.7).sp,
                                fontWeight = FontWeight.Bold,
                                style = TextStyle(
                                    shadow = Shadow(
                                        color = if (isMatched) Color.Transparent else Color.Black.copy(alpha = 0.4f),
                                        offset = Offset(2f, 2f),
                                        blurRadius = 4f
                                    )
                                )
                            )
                        }

                        // DOT
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .offset(y = 3.dp)
                                .size(6.dp)
                                .background(Color.DarkGray, CircleShape)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // -------------------------
            // IMAGES
            // -------------------------
            MatchWithImagesGrid(
                images = uiState.shuffledImages,
                matchedLetters = uiState.matchedLetters,
                rootCoords = rootCoords,
                onUpdateImagePosition = viewModel::updateImagePosition,
                onUpdateImageRect = viewModel::updateImageRect
            )
        }
    }
}