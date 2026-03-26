package com.example.myapplication.ui.theme

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.main.age_group.from_3_to_5.abcd_with_images.view_model.ABCDWithImagesUiState

object AppDimens {
    val isTablet = DeviceInfo.isTablet
    // Spacing
    val Dimens2 = if (isTablet) 3.dp else 2.dp
    val Dimens4 = if (isTablet) 6.dp else 4.dp
    val Dimens6 = if (isTablet) 8.dp else 6.dp
    val Dimens8 = if (isTablet) 12.dp else 8.dp
    val Dimens12 = if (isTablet) 16.dp else 12.dp
    val Dimens16 = if (isTablet) 24.dp else 16.dp
    val Dimens24 = if (isTablet) 36.dp else 24.dp
    val ABCDWithImagesBigTextSize = if (isTablet) 150.sp else 120.sp
    val ABCDWithImagesSmallImageSize = if (isTablet) 100.dp else 80.dp

    val FontExtraLarge36 = if (isTablet) 54.sp else 36.sp
}