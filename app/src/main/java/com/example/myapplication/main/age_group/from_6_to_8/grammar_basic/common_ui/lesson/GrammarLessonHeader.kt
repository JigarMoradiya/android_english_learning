package com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.common_ui.lesson

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.myapplication.R
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.ButtonType

@Composable
fun GrammarLessonHeader(
    title: String,
    onBackClick: () -> Unit,
    onPracticeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackButtonWithText(
            title = title,
            onBackClick = onBackClick,
            modifier = Modifier.weight(1f)
        )

        KidsActionButton(
            modifier = Modifier.padding(end = Dimens16),
            text = stringResource(R.string.practice_activity),
            icon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            type = ButtonType.BLUE,
            onClick = onPracticeClick,
            isSmall = true,
            isIconStart = false
        )
    }
}