package com.example.myapplication.ui.theme

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.DeviceInfo

object AppDimens {
    val isTablet = DeviceInfo.isTablet
    // Spacing
    val ToolbarIconSize = if (isTablet) 56.dp else 42.dp
    val KidsIconSize = if (isTablet) 72.dp else 56.dp
    val Dimens2 = if (isTablet) 3.dp else 2.dp
    val ShadowOffset = if (isTablet) 2.5.dp else 1.5.dp
    val ShadowOffsetText = if (isTablet) 2.dp else 1.dp
    val Dimens4 = if (isTablet) 6.dp else 4.dp
    val Dimens6 = if (isTablet) 8.dp else 6.dp
    val Dimens8 = if (isTablet) 12.dp else 8.dp
    val Dimens10 = if (isTablet) 14.dp else 10.dp
    val Dimens12 = if (isTablet) 16.dp else 12.dp
    val Dimens16 = if (isTablet) 24.dp else 16.dp
    val Dimens20 = if (isTablet) 30.dp else 20.dp
    val Dimens24 = if (isTablet) 36.dp else 24.dp
    val Dimens28 = if (isTablet) 36.dp else 28.dp
    val Dimens40 = if (isTablet) 48.dp else 40.dp
    val DimensColorCircles = if (isTablet) 44.dp else 36.dp
    val CommonPopupImageSize = if (isTablet) 96.dp else 72.dp
    val ABCDWithImagesBigTextSize = if (isTablet) 150.sp else 120.sp
    val ABCDWithImagesSmallImageSize = if (isTablet) 100.dp else 80.dp
    val MatchLetterBoxSize = if (isTablet) 100.dp else 80.dp
    val DragLetterBoxSize = if (isTablet) 80.dp else 60.dp
    val ColoringAlphabetsImageWidth = if (isTablet) 200.dp else 170.dp

    val FontExtraLarge36 = if (isTablet) 54.sp else 36.sp
}