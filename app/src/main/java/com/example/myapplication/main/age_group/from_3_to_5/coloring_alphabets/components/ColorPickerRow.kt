package com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model.ColorOption
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model.ColoringAlphabetsViewModel

@Composable
fun ColorPickerRow(viewModel: ColoringAlphabetsViewModel) {

    val colors = listOf(
        Color.Red,
        Color.Blue,
        Color.Green,
        Color.Yellow,
        Color.Magenta,
        Color.Cyan
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        colors.forEach { color ->

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color)
                    .border(2.dp, Color.Black, CircleShape)
                    .clickable {
                        viewModel.currentColor =
                            ColorOption.Solid(color, color.toString())
                        viewModel.isEraser = false
                    }
            )
        }

        // 🧽 Eraser
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(2.dp, Color.Black, CircleShape)
                .clickable {
                    viewModel.isEraser = true
                },
            contentAlignment = Alignment.Center
        ) {
            Text("E")
        }
    }
}