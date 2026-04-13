package com.example.myapplication.main.age_group.from_5_to_7.sight_words

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.main.age_group.from_5_to_7.sight_words.components.SightWordsScreen
import com.example.myapplication.main.age_group.from_5_to_7.sight_words.view_model.SightWordsViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.ButtonType


@Composable
fun SightWordsPage(
    navController: NavController,
    viewModel: SightWordsViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState
    val isFirst = uiState.currentIndex == 0
    val isLast = uiState.currentIndex == uiState.words.lastIndex

    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundUI(isGreenGrassShow = false)
        Column(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)) {
            BackButtonWithText(
                title = stringResource(R.string.sight_words),
                onBackClick = { navController.popBackStack() }
            )

            SightWordsScreen(viewModel,modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .padding(start = DeviceInfo.screenPadding(), end = Dimens16)
                    .padding(bottom = Dimens16, top = Dimens8),
                verticalAlignment = Alignment.Bottom
            ) {

                KidsActionButton(
                    text = stringResource(R.string.previous),
                    icon = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                    type = if (isFirst) ButtonType.DISABLE else ButtonType.ORANGE,
                    onClick = { viewModel.previousWord() }
                )

                Spacer(modifier = Modifier.weight(1f))

                KidsActionButton(
                    text = stringResource(R.string.next),
                    icon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    type = if (isLast) ButtonType.DISABLE else ButtonType.ORANGE,
                    isIconStart = false,
                    onClick = { viewModel.nextWord() }
                )
            }
        }
    }
}
