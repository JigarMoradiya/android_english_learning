package com.example.myapplication.main.age_group.from_5_to_7.listen_and_select_answer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.myapplication.main.age_group.from_5_to_7.word_match_picture.view_model.WordMatchImageViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.CustomPopupView
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.buttons.KidsOptionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.listenAndAnswerOptionsHeight
import com.example.myapplication.ui.theme.AppDimens.listenAndAnswerOptionsWidth
import com.example.myapplication.ui.theme.ButtonType


@Composable
fun ListenAndSelectWordPage(
    navController: NavController,
    viewModel: ListenAndSelectWordViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState

    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundUI(isGreenGrassShow = false)
        Column(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)) {
            BackButtonWithText(
                title = stringResource(R.string.listen_and_select_answer),
                onBackClick = { navController.popBackStack() }
            )
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
                                    fontSize = listenAndAnswerOptionsHeight.value.sp * 0.45,
                                    onClick = {
                                        viewModel.checkCorrectOrWrong(word)
                                    },
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

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = Dimens16).fillMaxWidth()
            ) {

                Text(
                    text = stringResource(uiState.feedbackTextRes),
                    color =  Color.Red,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.alpha(if (uiState.showError) 1f else 0f)
                )

                Spacer(modifier = Modifier.height(Dimens4))

                Text(
                    text = uiState.feedbackSubTextError,
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.alpha(if (uiState.showError) 1f else 0f)
                )

            }
        }

        AnimatedVisibility(
            visible = uiState.showPopup,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            CustomPopupView(
                title = stringResource(uiState.feedbackTextRes),
                description = stringResource(uiState.feedbackSubTextRes),
                positiveButtonText = stringResource(R.string.continue_to_play),
                negativeButtonText = stringResource(R.string.no_i_want_to_close),
                icon = R.drawable.ic_complete,
                widthMultiplier = 0.5f,
                onPositiveTapped = { viewModel.playAgain() },
                onNegativeTapped = {
                    viewModel.closePopup()
                    navController.popBackStack()
                }
            )
        }
    }
}
