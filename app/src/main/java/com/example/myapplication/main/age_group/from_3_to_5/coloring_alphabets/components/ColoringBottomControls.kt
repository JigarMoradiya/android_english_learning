package com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model.ColoringAlphabetsUiState
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens28
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.DimensColorCircles

@Composable
fun ColoringBottomControls(
    state: ColoringAlphabetsUiState,
    onBrushSelect: (Brush) -> Unit
) {

    val brushes = state.brushes
    val selectedBrush = state.selectedBrush
    val isSelectedEraser = state.isEraser

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = DeviceInfo.screenHorizontalPadding(), end = Dimens16)
            .padding(bottom = Dimens16, top = Dimens8),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // 🎨 BRUSH SCROLL (GRADIENTS)
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically
        ) {

            brushes.forEach { brush ->

                val isSelected = !isSelectedEraser && brush == selectedBrush

                val innerSize by animateDpAsState(
                    targetValue = if (isSelected) DimensColorCircles else Dimens28,
                    label = ""
                )

                val alphaValue = if (isSelectedEraser) 0.4f else 1f

                Box(
                    modifier = Modifier.size(DimensColorCircles),
                    contentAlignment = Alignment.Center
                ) {

                    Box(
                        modifier = Modifier
                            .size(innerSize)
                            .alpha(alphaValue)
                            .clip(CircleShape)
                            .background(brush) // 🔥 gradient support
                            .then(
                                if (isSelected) {
                                    Modifier.border(Dimens2, Color.Black, CircleShape)
                                } else Modifier
                            )
                            .clickable {
                                onBrushSelect(brush)
                            }
                    )
                }
            }
        }
    }
}