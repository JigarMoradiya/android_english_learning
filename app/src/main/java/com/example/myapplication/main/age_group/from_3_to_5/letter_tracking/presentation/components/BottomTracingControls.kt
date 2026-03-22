package com.example.myapplication.main.age_group.from_3_to_5.letter_tracking.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.myapplication.R
import com.example.myapplication.main.common.ActionButton
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
            onClick = onPrevious
        )

        Spacer(modifier = Modifier.weight(1f))

        ActionButton(
            text = stringResource(R.string.clear),
            icon = Icons.Default.Refresh,
            onClick = onClear
        )

        Spacer(modifier = Modifier.weight(1f))

        ActionButton(
            text = stringResource(R.string.next),
            icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            onClick = onNext,
            isIconStart = false // icon on right
        )
    }
}