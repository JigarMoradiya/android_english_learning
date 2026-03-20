package com.example.myapplication.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.myapplication.R

// Step 1: Create FontFamily
val customFonts = FontFamily(
    Font(R.font.font_regular, FontWeight.Normal),
    Font(R.font.font_medium, FontWeight.Medium),
    Font(R.font.font_semi_bold, FontWeight.SemiBold),
    Font(R.font.font_bold, FontWeight.Bold),
)

// Step 2: Define Typography using that font
// Minimal Typography setup (no font sizes, only family + weight)
// Only define family + weight, no sizes
val AppTypography = Typography(
    bodySmall = TextStyle(fontFamily = customFonts, fontWeight = FontWeight.Normal),
    bodyMedium = TextStyle(fontFamily = customFonts, fontWeight = FontWeight.Medium),
    bodyLarge = TextStyle(fontFamily = customFonts, fontWeight = FontWeight.SemiBold),
    titleLarge = TextStyle(fontFamily = customFonts, fontWeight = FontWeight.Bold)
)
