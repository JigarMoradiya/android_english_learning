package com.example.myapplication.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
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
    displayLarge = TextStyle(fontFamily = customFonts, fontWeight = FontWeight.Bold, fontSize = 32.sp, lineHeight = 38.sp),
    displayMedium = TextStyle(fontFamily = customFonts, fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 34.sp),
    displaySmall = TextStyle(fontFamily = customFonts, fontWeight = FontWeight.SemiBold, fontSize = 24.sp, lineHeight = 30.sp),

    headlineLarge = TextStyle(fontFamily = customFonts, fontWeight = FontWeight.Bold, fontSize = 22.sp, lineHeight = 28.sp),
    headlineMedium = TextStyle(fontFamily = customFonts, fontWeight = FontWeight.SemiBold, fontSize = 20.sp, lineHeight = 26.sp),
    headlineSmall = TextStyle(fontFamily = customFonts, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, lineHeight = 24.sp),

    titleLarge = TextStyle(fontFamily = customFonts, fontWeight = FontWeight.Bold, fontSize = 20.sp, lineHeight = 26.sp),
    titleMedium = TextStyle(fontFamily = customFonts, fontWeight = FontWeight.Medium, fontSize = 18.sp, lineHeight = 24.sp),
    titleSmall = TextStyle(fontFamily = customFonts, fontWeight = FontWeight.Medium, fontSize = 16.sp, lineHeight = 22.sp),

    bodyLarge = TextStyle(fontFamily = customFonts, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 22.sp),
    bodyMedium = TextStyle(fontFamily = customFonts, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp),
    bodySmall = TextStyle(fontFamily = customFonts, fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 18.sp),

    labelLarge = TextStyle(fontFamily = customFonts, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 20.sp),
    labelMedium = TextStyle(fontFamily = customFonts, fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp),
    labelSmall = TextStyle(fontFamily = customFonts, fontWeight = FontWeight.Medium, fontSize = 11.sp, lineHeight = 14.sp)
)