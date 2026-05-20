package com.example.myapplication.main.age_group.from_3_to_5.fill_blank.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.main.age_group.from_3_to_5.fill_blank.view_model.FillBlankLettersViewModel
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.FillBlankLetterBoxSize
import com.example.myapplication.ui.theme.PrimaryBlue
import com.example.myapplication.utils.extensions.scaled

private val slotCornerRadius = Dimens16

@Composable
fun TopLetterSlots(viewModel: FillBlankLettersViewModel) {

    val uiState = viewModel.uiState

    Row(horizontalArrangement = Arrangement.spacedBy(Dimens12.scaled())) {

        uiState.topSlots.forEachIndexed { index, letter ->

            val isFixed = uiState.fixedIndices.contains(index)
            val isEmpty = letter == null

            // Mirrors iOS ZStack: background fill → dashed border → text
            Box(
                modifier = Modifier
                    .size(FillBlankLetterBoxSize)
                    .clickable { viewModel.onTopLetterClick(index) },
                contentAlignment = Alignment.Center
            ) {

                // Layer 1 — background fill (RoundedRectangle.fill in iOS)
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clip(RoundedCornerShape(slotCornerRadius))
                        .background(
                            when {
                                isEmpty -> Color.Gray.copy(alpha = 0.12f)
                                isFixed -> Color(0xFF4CAF50).copy(alpha = 0.18f)
                                else    -> PrimaryBlue.copy(alpha = 0.15f)
                            }
                        )
                )

                // Layer 2 — dashed stroke border for empty slots (strokeBorder + dash in iOS)
                if (isEmpty) {
                    Canvas(modifier = Modifier.matchParentSize()) {
                        val strokeWidth = 2.dp.toPx()
                        val inset = strokeWidth / 2f
                        val dashEffect = PathEffect.dashPathEffect(
                            floatArrayOf(8.dp.toPx(), 5.dp.toPx()), 0f
                        )
                        drawRoundRect(
                            color = Color.Gray.copy(alpha = 0.4f),
                            topLeft = Offset(inset, inset),
                            size = Size(size.width - strokeWidth, size.height - strokeWidth),
                            cornerRadius = CornerRadius(slotCornerRadius.toPx()),
                            style = Stroke(width = strokeWidth, pathEffect = dashEffect)
                        )
                    }
                }

                // Layer 3 — letter text
                Text(
                    text = if (isEmpty) "?" else letter,
                    style = MaterialTheme.typography.titleLarge.scaled(),
                    fontSize = (FillBlankLetterBoxSize.value * 0.75).sp,
                    fontWeight = FontWeight.Bold,
                    color = when {
                        isEmpty -> Color.Gray.copy(alpha = 0.35f)
                        isFixed -> Color.DarkGray
                        else    -> PrimaryBlue
                    }
                )
            }
        }
    }
}