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

            // MATCHED LINES
            uiState.matchedOrder.forEachIndexed { index, letter ->

                val start = uiState.letterPositions[letter]
                val end = uiState.imagePositions[letter]

                if (start != null && end != null) {
                    val color = viewModel.getLineColor(index)
                    drawLine(color = color.copy(alpha = 0.25f), start = start, end = end, strokeWidth = 14f, cap = Round)

                    // 🎯 Main line
                    drawLine(color = color, start = start, end = end, strokeWidth = 6f, cap = Round)
                }

            }

            val start = viewModel.dragStart
            val end = viewModel.dragEnd

            if (start != null && end != null) {
                val color = Color.Black

                // ✨ Glow line (background)
                drawLine(color = color.copy(alpha = 0.25f), start = start, end = end, strokeWidth = 14f, cap = Round)

                // 🎯 Main line
                drawLine(color = color, start = start, end = end, strokeWidth = 6f, cap = Round)

                // 🔵 Start dot
//                drawCircle(color = color, radius = 6f, center = start)

                // 🔴 Finger dot (end)
                drawCircle(color = color, radius = 10f, center = end)
            }
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
                                .pointerInput(letter) {
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
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                modifier = Modifier.fillMaxWidth(0.8f),
                horizontalArrangement = Arrangement.spacedBy(Dimens16),
                verticalArrangement = Arrangement.spacedBy(Dimens12)
            ) {

                items(uiState.shuffledImages) { (letter, word) ->

                    val res = getImageResFromWord(word)
                    val isMatched = uiState.matchedLetters.contains(letter)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Dimens6)
                    ) {

                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .padding(top = 3.dp),
                            contentAlignment = Alignment.Center
                        ) {

                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .shadow(6.dp, RoundedCornerShape(Dimens16))
                                    .clip(RoundedCornerShape(Dimens16))
                                    .background(Color.White)
                                    .border(
                                        width = Dimens2,
                                        color = if (isMatched) PrimaryGreen else Color.White,
                                        shape = RoundedCornerShape(Dimens16)
                                    )
                                    .onGloballyPositioned { coords ->

                                        val root = rootCoords ?: return@onGloballyPositioned

                                        // 🔥 FIX: SAME COORDINATE SYSTEM
                                        val pos = root.localPositionOf(coords, Offset.Zero)
                                        val size = coords.size

                                        val topCenter = Offset(
                                            pos.x + size.width / 2f,
                                            pos.y
                                        )

                                        viewModel.updateImagePosition(letter, topCenter)

//                                        val rect = coords.boundsInRoot()
                                        val topLeft = root.localPositionOf(coords, Offset.Zero)
                                        val rect = Rect(
                                            offset = topLeft,
                                            size = Size(size.width.toFloat(), size.height.toFloat())
                                        )
                                        viewModel.updateImageRect(letter, rect)
                                    },
                                contentAlignment = Alignment.Center
                            ) {

                                res?.let {
                                    Image(
                                        painter = painterResource(it),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(0.8f)
                                    )
                                }
                            }

                            // DOT
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .offset(y = (-2).dp)
                                    .size(6.dp)
                                    .background(Color.DarkGray, CircleShape)
                            )
                        }

                        Text(
                            text = word,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black,
                            maxLines = 1,
                            modifier = Modifier.alpha(if (isMatched) 1f else 0f)
                        )
                    }
                }
            }
        }
    }
}