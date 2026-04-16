package com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.myapplication.R
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens8


@Composable
fun BottomTracingControls(
    onClear: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier.padding(start = DeviceInfo.screenHorizontalPadding(), end = Dimens16).padding(bottom = Dimens16,top = Dimens8),
        verticalAlignment = Alignment.Bottom
    ) {
        KidsActionButton(
            text = stringResource(R.string.previous),
            icon = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
            type = ButtonType.ORANGE,
            onClick = onPrevious
        )

        Spacer(modifier = Modifier.weight(1f))

        KidsActionButton(
            text = stringResource(R.string.clear),
            icon = Icons.Rounded.Refresh,
            type = ButtonType.PINK,
            onClick = onClear,
            isSmall = true
        )

        Spacer(modifier = Modifier.weight(1f))

        KidsActionButton(
            text = stringResource(R.string.next),
            icon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            type = ButtonType.ORANGE,
            onClick = onNext,
            isIconStart = false
        )
    }
}