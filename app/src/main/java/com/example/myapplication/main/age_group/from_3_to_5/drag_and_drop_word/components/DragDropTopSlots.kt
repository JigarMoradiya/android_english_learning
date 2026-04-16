package com.example.myapplication.main.age_group.from_3_to_5.drag_and_drop_word.components


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.myapplication.main.age_group.from_3_to_5.drag_and_drop_word.view_model.DragDropWordViewModel
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.DragLetterBoxSize
import com.example.myapplication.ui.theme.PrimaryOrange
import com.example.myapplication.utils.AudioPlayerManager


@Composable
fun DragDropTopSlots(viewModel: DragDropWordViewModel) {

    val dropped = viewModel.dropped
    val fixed = viewModel.fixedIndices

    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens12),
        verticalAlignment = Alignment.CenterVertically
    ) {

        dropped.forEachIndexed { index, item ->

            val isFixed = fixed.contains(index)

            var boxPos by remember { mutableStateOf(Offset.Zero) }
            val isDragging = viewModel.dragging == item

            Box(modifier = Modifier
            ){
                Box( modifier = Modifier
                    .size(DragLetterBoxSize)

                    // ✅ background on container (not text)
                    .background(Color.Transparent)
                    .border(
                        width = 2.dp,
                        color = PrimaryOrange,
                        shape = RoundedCornerShape(12.dp)
                    )
                ){

                }

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
                            .pointerInput(item, isFixed, viewModel.uiState.showPopup) {

                                if (item != null && !isFixed && !viewModel.uiState.showPopup) {

                                    detectDragGestures(

                                        onDragStart = { touch ->
                                            viewModel.dragging = item
                                            viewModel.dragPosition = boxPos + touch
                                            viewModel.dragFromIndex = index
                                            viewModel.removeError()
                                        },

                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            viewModel.dragPosition = (viewModel.dragPosition ?: Offset.Zero) + dragAmount
                                        },

                                        onDragEnd = {
                                            AudioPlayerManager.playSoundDragItem()
                                            val end = viewModel.dragPosition ?: Offset.Zero

                                            val targetIndex =
                                                viewModel.slotRects.entries.firstOrNull { entry ->
                                                    val rect = entry.value.inflate(20f)   // 👈 increase touch area
                                                    rect.contains(end)
                                                }?.key

                                            if (targetIndex != null && targetIndex != index) {
                                                // ❌ TARGET FILLED → RETURN BACK
                                                Log.e("jigarDragNDrop","viewModel.dropped[targetIndex] = "+viewModel.dropped[targetIndex])
                                                if (viewModel.dropped[targetIndex] != null) {

                                                    val fromIndex = viewModel.dragFromIndex

                                                    if (fromIndex != null) {
                                                        // just restore (no place)
                                                        viewModel.restoreToSlot(item, fromIndex)
                                                    } else {
                                                        viewModel.returnToPool(item, index)
                                                    }
                                                }

                                                // ✅ TARGET EMPTY → MOVE
                                                else {
                                                    viewModel.clearSlot(index)
                                                    viewModel.place(item, targetIndex)
                                                    viewModel.validate()
                                                }

                                            } else {
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
                            fontSize = (DragLetterBoxSize.value * 0.75).sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.size(DragLetterBoxSize)
                                .background(if (item?.letter.isNullOrEmpty()) Color.Transparent else PrimaryOrange,
                                    shape = RoundedCornerShape(Dimens12)
                                )
                                .wrapContentHeight(Alignment.CenterVertically)
                        )

                    }

                }
            }


        }
    }
}