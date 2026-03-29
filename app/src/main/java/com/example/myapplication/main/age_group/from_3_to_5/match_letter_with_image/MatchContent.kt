package com.example.myapplication.main.age_group.from_3_to_5.match_letter_with_image

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.main.age_group.from_3_to_5.match_letter_with_image.view_model.MatchLetterWithImageViewModel
import com.example.myapplication.main.common.getImageResFromWord
import com.example.myapplication.ui.theme.AppDimens.MatchLetterBoxSize

@Composable
fun MatchContent(
    viewModel: MatchLetterWithImageViewModel,
    modifier: Modifier = Modifier
) {

    val uiState = viewModel.uiState

    Box(modifier = modifier.fillMaxSize()) {

        // -------------------------
        // CANVAS (LINES)
        // -------------------------
        Canvas(modifier = Modifier.matchParentSize()) {

            // MATCHED LINES
            uiState.matchedOrder.forEach { letter ->

                val start = uiState.letterPositions[letter]
                val end = uiState.imagePositions[letter]

                if (start != null && end != null) {
                    drawLine(
                        color = Color.Green,
                        start = start,
                        end = end,
                        strokeWidth = 6f
                    )
                }
            }

            // DRAG LINE
            val start = uiState.dragStart
            val end = uiState.dragEnd

            if (start != null && end != null) {
                drawLine(
                    color = Color(0xFFFF9800),
                    start = start,
                    end = end,
                    strokeWidth = 6f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                )
            }
        }

        // -------------------------
        // CONTENT
        // -------------------------
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.weight(1f))

            // -------------------------
            // LETTERS
            // -------------------------
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

                uiState.batchLetters.forEach { (letter, _) ->

                    Box(
                        modifier = Modifier
                            .size(MatchLetterBoxSize)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.Blue)

                            // ✅ EXACT BOTTOM CENTER
                            .onGloballyPositioned { coords ->
                                val pos = coords.positionInRoot()
                                val size = coords.size
                                val bottomCenter = Offset(
                                    pos.x + size.width / 2f,
                                    pos.y  + (MatchLetterBoxSize.value / 2) // ✅ REAL BOTTOM EDGE
                                )

                                viewModel.updateLetterPosition(letter, bottomCenter)
                            }

                            // ✅ DRAG
                            .pointerInput(letter) {
                                detectDragGestures(

                                    onDragStart = {
                                        val start = uiState.letterPositions[letter]
                                            ?: return@detectDragGestures

                                        viewModel.startDrag(letter, start)
                                    },

                                    onDrag = { change, dragAmount ->
                                        change.consume()

                                        viewModel.updateDrag(dragAmount) // ✅ ONLY DELTA
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
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(60.dp))

            // -------------------------
            // IMAGES
            // -------------------------
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(220.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(uiState.shuffledImages) { (letter, word) ->

                    val res = getImageResFromWord(word)

                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)

                            // ✅ EXACT TOP CENTER
                            .onGloballyPositioned { coords ->

                                val pos = coords.positionInRoot()
                                val size = coords.size

                                val topCenter = Offset(
                                    pos.x + size.width / 2f,
                                    pos.y - size.height / 2f // ✅ REAL TOP EDGE
                                )

                                viewModel.updateImagePosition(letter, topCenter)
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
                }
            }

            Spacer(Modifier.weight(1f))
        }
    }
}