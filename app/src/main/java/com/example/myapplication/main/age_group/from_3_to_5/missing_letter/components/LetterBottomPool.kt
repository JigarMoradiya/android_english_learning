package com.example.myapplication.main.age_group.from_3_to_5.missing_letter.components


import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.myapplication.main.age_group.from_3_to_5.missing_letter.view_model.MissingLetterViewModel
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.DragLetterBoxSize
import com.example.myapplication.ui.theme.PrimaryOrangeLight
import com.example.myapplication.utils.AudioPlayerManager

@Composable
fun LetterBottomPool(viewModel: MissingLetterViewModel) {

    val letters = viewModel.letters
    val dragging = viewModel.dragging
    val dragPos = viewModel.dragPosition

    Row(horizontalArrangement = Arrangement.spacedBy(Dimens12)) {

        letters.forEach { item ->

            var boxPos by remember { mutableStateOf(Offset.Zero) }

            val isDragging = dragging == item

            Box(
                modifier = Modifier
                    .size(DragLetterBoxSize)

                    .onGloballyPositioned {
                        boxPos = it.positionInRoot()
                    }

                    // ⭐ MOVE ORIGINAL ITEM
                    .offset {
                        if (isDragging && dragPos != null) {
                            IntOffset(
                                (dragPos.x - boxPos.x - DragLetterBoxSize.value/2).toInt(),
                                (dragPos.y - boxPos.y - DragLetterBoxSize.value/2).toInt()
                            )
                        } else {
                            IntOffset.Zero
                        }
                    }

                    .zIndex(if (isDragging) 1f else 0f)

                    .background(PrimaryOrangeLight, RoundedCornerShape(Dimens16))

                    .pointerInput(item) {
                        detectDragGestures(

                            onDragStart = { touch ->
                                viewModel.dragging = item
                                viewModel.dragPosition = boxPos + touch
                                viewModel.removeError()
                            },

                            onDrag = { change, dragAmount ->
                                change.consume()

                                viewModel.dragPosition =
                                    (viewModel.dragPosition ?: Offset.Zero) + dragAmount
                            },

                            onDragEnd = {
                                AudioPlayerManager.playSoundDragItem()
                                val end = viewModel.dragPosition ?: Offset.Zero

                                val targetIndex =
                                    viewModel.slotRects.entries.firstOrNull { entry ->
                                        val rect = entry.value.inflate(20f)   // 👈 increase touch area
                                        rect.contains(end)
                                    }?.key

                                if (targetIndex != null) {
                                    // ⭐ CHECK IF SLOT IS EMPTY
                                    val isEmpty = viewModel.dropped[targetIndex] == null
                                    if (isEmpty) {
                                        viewModel.place(item, targetIndex)
                                        viewModel.validate()
                                    } else {
                                        // ❌ SLOT ALREADY FILLED
                                        viewModel.fallbackReturn(item)
                                    }
                                } else {
                                    viewModel.fallbackReturn(item)
                                }

                                viewModel.clearDrag()
                            }
                        )
                    },

                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = item.letter,
                    color = Color.Black,
                    fontSize = (DragLetterBoxSize.value * 0.65).sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}