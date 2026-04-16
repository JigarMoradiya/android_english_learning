package com.example.myapplication.data.model

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens8

object DeviceInfo {
    var hasNotch: Boolean = false
    var isTablet: Boolean = false
    var isLargeTablet: Boolean = false

    fun screenHorizontalPadding() : Dp {
        return if (hasNotch) {
            0.dp
        } else {
            Dimens16
        }
    }

    fun screenTopPadding() : Dp {
        return if (isTablet) {
            0.dp
        } else {
            Dimens8
        }
    }
}