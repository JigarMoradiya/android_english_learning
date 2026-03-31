package com.example.myapplication.main.age_group.from_3_to_5.missing_letter.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.myapplication.main.age_group.from_3_to_5.missing_letter.view_model.MissingLetterViewModel


@Composable
fun WordSlots(viewModel: MissingLetterViewModel) {

    val dropped = viewModel.dropped
    val fixed = viewModel.fixedIndices

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

        dropped.forEachIndexed { index, item ->

            val isFixed = fixed.contains(index)

            var boxPos by remember { mutableStateOf(Offset.Zero) }

            val isDragging = viewModel.dragging == item

            Box(
                modifier = Modifier
                    .size(60.dp)

                    .onGloballyPositioned {
                        boxPos = it.positionInRoot()
                        viewModel.updateSlotRect(index, it.boundsInRoot())
                    }

                    // ✅ MOVE WHEN DRAGGING
                    .offset {
                        if (isDragging && viewModel.dragPosition != null) {
                            IntOffset(
                                (viewModel.dragPosition!!.x - boxPos.x - 30).toInt(),
                                (viewModel.dragPosition!!.y - boxPos.y - 30).toInt()
                            )
                        } else IntOffset.Zero
                    }

                    .zIndex(if (isDragging) 1f else 0f)

                    .border(
                        2.dp,
                        if (isFixed) Color.Green else Color.Gray,
                        RoundedCornerShape(12.dp)
                    )
                    .background(Color.White)

                    // ✅ DRAG ENABLE ONLY IF NOT FIXED & HAS ITEM
                    .pointerInput(item) {
                        if (item != null && !isFixed) {

                            detectDragGestures(

                                onDragStart = { touch ->
                                    viewModel.dragging = item
                                    viewModel.dragPosition = boxPos + touch
                                },

                                onDrag = { change, dragAmount ->
                                    change.consume()

                                    viewModel.dragPosition =
                                        (viewModel.dragPosition ?: Offset.Zero) + dragAmount
                                },

                                onDragEnd = {

                                    val end = viewModel.dragPosition ?: Offset.Zero

                                    val targetIndex =
                                        viewModel.slotRects.entries.firstOrNull {
                                            it.value.contains(end)
                                        }?.key

                                    if (targetIndex != null &&
                                        viewModel.dropped[targetIndex] == null
                                    ) {
                                        // move to another slot
                                        viewModel.place(item, targetIndex)
                                    } else {
                                        // ⭐ RETURN TO POOL
                                        viewModel.returnToPool(item, index)
                                    }

                                    viewModel.clearDrag()
                                }
                            )
                        }
                    },

                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = item?.letter ?: "",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isFixed) Color.Black else Color.Blue
                )
            }
        }
    }
}