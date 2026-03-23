package com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.myapplication.R
import com.example.myapplication.main.common.ActionButton
import com.example.myapplication.main.common.ButtonType
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens8


@Composable
fun BottomTracingControls(
    onClear: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier.padding(horizontal = Dimens16).padding(bottom = Dimens16).padding(top = Dimens8)
    ) {
        ActionButton(
            text = stringResource(R.string.previous),
            icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            type = ButtonType.ORANGE,
            onClick = onPrevious
        )

        Spacer(modifier = Modifier.weight(1f))

        ActionButton(
            text = stringResource(R.string.clear),
            icon = Icons.Default.Refresh,
            type = ButtonType.PINK,
            onClick = onClear,
        )

        Spacer(modifier = Modifier.weight(1f))

        ActionButton(
            text = stringResource(R.string.next),
            icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            type = ButtonType.ORANGE,
            onClick = onNext,
            isIconStart = false
        )
    }
}