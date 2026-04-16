package com.example.myapplication.utils.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.helper.scale
import com.example.myapplication.ui.theme.AppDimens.isLargeTablet
import com.example.myapplication.ui.theme.AppDimens.isTablet

@Composable
fun appScale(): Float {
    val phoneScale = 1f
    val tabletScale = 1.3f
    val largeTabletScale = 1.5f

    return if (DeviceInfo.isLargeTablet) {
        largeTabletScale
    } else if (DeviceInfo.isTablet) {
        tabletScale
    } else {
        phoneScale
    }
}

@Composable
fun TextStyle.scaled(): TextStyle {
    val scale = appScale()

    return this.copy(
        fontSize = if (fontSize != TextUnit.Unspecified) {
            fontSize * scale
        } else fontSize,

        lineHeight = if (lineHeight != TextUnit.Unspecified) {
            lineHeight * scale
        } else lineHeight
    )
}

@Composable
fun TextUnit.scaled(): TextUnit {
    return this * appScale()
}

@Composable
fun Dp.scaled(): Dp {
    val scale = appScale()
    return this * scale
}