package com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.presentation.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.LetterMode
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.AlphabetTracingViewModel


@Composable
fun GuidelineCanvas(
    viewModel: AlphabetTracingViewModel
) {

    val uiState = viewModel.uiState
    val isLowercase = uiState.mode == LetterMode.LOWERCASE

    Canvas(modifier = Modifier.fillMaxSize()) {

        val startY = 0.1f
        val endY = 0.9f

        drawRoundRect(
            color = Color.White,
            size = size,
            cornerRadius = CornerRadius(20f)
        )

        val dash = PathEffect.dashPathEffect(floatArrayOf(12f, 8f))

        if (isLowercase) {

            // -------------------------------
            // 🔤 LOWERCASE → 4 LINES
            // -------------------------------
            val section = (endY - startY) / 3f

            val line1 = size.height * startY
            val line2 = size.height * (startY + section)
            val line3 = size.height * (startY + 2 * section)
            val line4 = size.height * endY

            listOf(line1, line2, line3, line4).forEachIndexed { i, y ->

                drawLine(
                    color = if (i == 0 || i == 3)
                        Color.Red.copy(alpha = 0.3f)
                    else
                        Color.Blue.copy(alpha = 0.2f),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 4f,
                    pathEffect = dash
                )
            }

        } else {

            // -------------------------------
            // 🔠 UPPERCASE → 3 LINES
            // -------------------------------
            val section = (endY - startY) / 2f

            val line1 = size.height * startY
            val line2 = size.height * (startY + section)
            val line3 = size.height * endY

            listOf(line1, line2, line3).forEachIndexed { i, y ->

                drawLine(
                    color = if (i == 0 || i == 2)
                        Color.Red.copy(alpha = 0.3f)
                    else
                        Color.Blue.copy(alpha = 0.2f),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 4f,
                    pathEffect = dash
                )
            }
        }
    }
}