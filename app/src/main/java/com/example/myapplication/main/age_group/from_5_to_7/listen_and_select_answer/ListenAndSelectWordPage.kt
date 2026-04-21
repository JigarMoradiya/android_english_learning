package com.example.myapplication.main.age_group.from_5_to_7.listen_and_select_answer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_5_to_7.listen_and_select_answer.view_model.ListenAndSelectWordViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.FeedbackText
import com.example.myapplication.main.common.animations.ConfettiRainEffect
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.buttons.KidsOptionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.listenAndAnswerOptionsHeight
import com.example.myapplication.ui.theme.AppDimens.listenAndAnswerOptionsWidth
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.ui.theme.PrimaryGreen


@Composable
fun ListenAndSelectWordPage(
    navController: NavController,
    viewModel: ListenAndSelectWordViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState

    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundUI(isGreenGrassShow = false)
        Column(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButtonWithText(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.listen_and_select_answer),
                    onBackClick = { navController.popBackStack() }
                )

                if (uiState.showSuccess) {
                    KidsActionButton(
                        modifier = Modifier.padding(end = Dimens16),
                        text = stringResource(R.string.next),
                        icon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        type = ButtonType.GREEN,
                        isIconStart = false,
                        onClick = {
                            viewModel.playAgain()
                        }
                    )
                }
            }
            Spacer(Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                KidsActionButton(
                    text = stringResource(R.string.listen_word),
                    icon = Icons.AutoMirrored.Rounded.VolumeUp,
                    type = ButtonType.PINK,
                    onClick = {
                        viewModel.speakWord()
                    }
                )
                Spacer(Modifier.width(Dimens16))
                Image(
                    painter = painterResource(R.drawable.ic_kid_listen),
                    contentDescription = null,
                    modifier = Modifier.fillMaxHeight(0.35f)
                )
            }

            val gridWidth = (listenAndAnswerOptionsWidth * 2) + Dimens16

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.width(gridWidth),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimens16)
                ) {

                    // Split list into rows of 2
                    uiState.optionsWord.chunked(2).forEach { rowItems ->

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Dimens16),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            rowItems.forEach { word ->

                                KidsOptionButton(
                                    text = word.replaceFirstChar { it.uppercase() },
                                    type = ButtonType.OPTIONS,
                                    fontSize = listenAndAnswerOptionsHeight.value.sp * 0.5,
                                    onClick = {
                                        viewModel.checkCorrectOrWrong(word)
                                    },
                                    enabled = !uiState.showSuccess,
                                    modifier = Modifier
                                        .width(listenAndAnswerOptionsWidth)
                                        .height(listenAndAnswerOptionsHeight)
                                )
                            }

                            // 👇 Important: if odd items (safety)
                            if (rowItems.size == 1) {
                                Spacer(modifier = Modifier.width(listenAndAnswerOptionsWidth))
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            FeedbackText(
                title = stringResource(viewModel.uiState.feedbackTextRes),
                subtitle = stringResource(viewModel.uiState.feedbackSubTextRes),
                isSuccess = viewModel.uiState.showSuccess,
                isVisible = viewModel.uiState.showError || viewModel.uiState.showSuccess
            )
        }

        if (viewModel.uiState.showSuccess) {
            ConfettiRainEffect()
        }
    }
}
