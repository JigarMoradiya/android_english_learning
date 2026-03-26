package com.example.myapplication.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

enum class ButtonType {
    BLUE,
    PINK,
    ORANGE,
    PURPLE,
    GREEN,
    RED,
    TEAL,
}

data class ButtonColors(
    val base: Color,
    val gradient: Brush
)

fun getButtonColors(type: ButtonType): ButtonColors {
    return when (type) {

        ButtonType.BLUE -> ButtonColors(
            base = Color(0xFF005EA9),
            gradient = Brush.horizontalGradient(
                listOf(
                    Color(0xFF42A5F5),
                    PrimaryBlue
                )
            )
        )

        ButtonType.PINK -> ButtonColors(
            base = Color(0xFFD5084E),
            gradient = Brush.horizontalGradient(
                listOf(
                    Color(0xFFF5538A),
                    Color(0xFFE91E63)
                )
            )
        )

        ButtonType.ORANGE -> ButtonColors(
            base = Color(0xFFC77601),
            gradient = Brush.horizontalGradient(
                listOf(
                    Color(0xFFEEAA47),
                    Color(0xFFFB8C00)
                )
            )
        )
        ButtonType.PURPLE -> ButtonColors(
            base = Color(0xFF5532D2),
            gradient = Brush.horizontalGradient(
                listOf(
                    Color(0xFF9374EF),
                    Color(0xFF6446CC)
                )
            )
        )

        ButtonType.GREEN -> ButtonColors(
            base = Color(0xFF108156),
            gradient = Brush.horizontalGradient(
                listOf(
                    Color(0xFF4CD09C),
                    Color(0xFF129864)
                )
            )
        )

        ButtonType.TEAL -> ButtonColors(
            base = Color(0xFF0B5960),
            gradient = Brush.horizontalGradient(
                listOf(
                    Color(0xFF46BBC5),
                    Color(0xFF107D86)
                )
            )
        )
        ButtonType.RED -> ButtonColors(
            base = Color(0xFF880606),
            gradient = Brush.horizontalGradient(
                listOf(
                    Color(0xFFD55E5E),
                    Color(0xFFAF0909)
                )
            )
        )
    }
}
