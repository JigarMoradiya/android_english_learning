package com.example.myapplication.main.age_group.from_3_to_5.missing_letter.components


import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.myapplication.main.age_group.from_3_to_5.missing_letter.view_model.MissingLetterViewModel
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.DragLetterBoxSize
import com.example.myapplication.ui.theme.PrimaryBlue


@Composable
fun WordTopSlots(viewModel: MissingLetterViewModel) {

    val dropped = viewModel.dropped
    val fixed = viewModel.fixedIndices

    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens24),
        verticalAlignment = Alignment.CenterVertically
    ) {

        dropped.forEachIndexed { index, item ->

            val isFixed = fixed.contains(index)

            var boxPos by remember { mutableStateOf(Offset.Zero) }
            val isDragging = viewModel.dragging == item

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier

                        // ⭐ keep position tracking
                        .onGloballyPositioned {
                            boxPos = it.positionInRoot()
                            viewModel.updateSlotRect(index, it.boundsInRoot())
                        }

                        // ⭐ drag movement (same logic)
                        .offset {
                            if (isDragging && viewModel.dragPosition != null) {
                                IntOffset(
                                    (viewModel.dragPosition!!.x - boxPos.x - 20).toInt(),
                                    (viewModel.dragPosition!!.y - boxPos.y - 20).toInt()
                                )
                            } else IntOffset.Zero
                        }

                        .zIndex(if (isDragging) 1f else 0f)

                        // ⭐ DRAG ENABLE
                        .pointerInput(item) {
                            if (item != null && !isFixed) {

                                detectDragGestures(

                                    onDragStart = { touch ->
                                        viewModel.dragging = item
                                        viewModel.dragPosition = boxPos + touch
                                        viewModel.removeError()
                                    },

                                    onDrag = { change, dragAmount ->
                                        change.consume()
                                        viewModel.dragPosition = (viewModel.dragPosition ?: Offset.Zero) + dragAmount
                                    },

                                    onDragEnd = {

                                        val end = viewModel.dragPosition ?: Offset.Zero

                                        val targetIndex =
                                            viewModel.slotRects.entries.firstOrNull { entry ->
                                                val rect = entry.value.inflate(60f)   // 👈 increase touch area
                                                rect.contains(end)
                                            }?.key

                                        if (targetIndex != null && targetIndex != index && viewModel.dropped[targetIndex] == null ) {

                                            // ✅ REMOVE from old slot FIRST
                                            viewModel.clearSlot(index)

                                            // ✅ PLACE in new slot
                                            viewModel.place(item, targetIndex)

                                            viewModel.validate()

                                        } else {
                                            // return back if same place or invalid
                                            viewModel.returnToPool(item, index)
                                        }

                                        viewModel.clearDrag()
                                    }
                                )
                            }
                        }
                        .width(DragLetterBoxSize)
                ) {

                    // 🔤 LETTER
                    Text(
                        text = item?.letter ?: "",
                        fontSize = (DragLetterBoxSize.value * 0.7).sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isFixed) Color.Black else PrimaryBlue
                    )


                }

                Spacer(modifier = Modifier.height(Dimens2))

                // ➖ UNDERLINE (MAIN UI CHANGE)
                Box(
                    modifier = Modifier
                        .width(DragLetterBoxSize)
                        .height(Dimens2)
                        .background(
                            if (isFixed) Color.Black else Color.DarkGray,
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }

        }
    }
}