package com.example.myapplication.data.model

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.AppDimens.Dimens16

object DeviceInfo {
    var hasNotch: Boolean = false
    var isTablet: Boolean = false

    fun screenPadding() : Dp {
        return if (hasNotch) {
            0.dp
        } else {
            Dimens16
        }
    }
}