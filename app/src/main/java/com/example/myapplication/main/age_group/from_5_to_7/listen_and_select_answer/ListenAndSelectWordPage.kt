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
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.main.age_group.from_5_to_7.listen_and_select_answer.view_model.ListenAndSelectWordViewModel
import com.example.myapplication.main.common.ActivityCompletePopup
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.CountdownBadge
import com.example.myapplication.main.common.FeedbackText
import com.example.myapplication.main.common.InstructionBadge
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.buttons.KidsLabel
import com.example.myapplication.main.common.buttons.KidsOptionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.listenAndAnswerOptionsHeight
import com.example.myapplication.ui.theme.AppDimens.listenAndAnswerOptionsWidth
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground


@Composable
fun ListenAndSelectWordPage(
    navController: NavController,
    viewModel: ListenAndSelectWordViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState

    Box(modifier = Modifier.fillMaxSize()) {

        KidsGradientBackground(gradient = KidsGradient.aquaGreen, shape = KidsFloatingShape.leaves)

        Column(
            modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                    KidsLabel("${uiState.questionIndex + 1}/${uiState.totalQuestions}")
                    CountdownBadge(
                        count = uiState.countdown,
                        modifier = Modifier.padding(end = Dimens16)
                    )
                } else {
                    InstructionBadge(
                        text = stringResource(R.string.listen_tap_the_word_),
                        isSmall = true,
                        modifier = Modifier.padding(end = Dimens16)
                    )
                    KidsLabel("${uiState.questionIndex + 1}/${uiState.totalQuestions}")
                }
            }

            Spacer(Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                KidsActionButton(
                    text = stringResource(if (uiState.isHomophone) R.string.play_sentence else R.string.listen_word),
                    icon = Icons.AutoMirrored.Rounded.VolumeUp,
                    type = ButtonType.PINK,
                    onClick = { viewModel.speakWord() }
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
                    uiState.optionsWord.chunked(2).forEach { rowItems ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Dimens16),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            rowItems.forEach { word ->
                                KidsOptionButton(
                                    text = word.replaceFirstChar { it.uppercase() },
                                    type = viewModel.optionType(word),
                                    fontSize = listenAndAnswerOptionsHeight.value.sp * 0.5,
                                    onClick = { viewModel.checkCorrectOrWrong(word) },
                                    enabled = !uiState.showSuccess,
                                    modifier = Modifier
                                        .width(listenAndAnswerOptionsWidth)
                                        .height(listenAndAnswerOptionsHeight)
                                )
                            }
                            if (rowItems.size == 1) {
                                Spacer(modifier = Modifier.width(listenAndAnswerOptionsWidth))
                            }
                        }
                    }
                }
            }

            if (uiState.isHomophone) {
                Text(
                    text = stringResource(R.string.homophone_hint),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens16, vertical = Dimens8)
                )
            }

            Spacer(Modifier.weight(1f))

            FeedbackText(
                title = stringResource(uiState.feedbackTextRes),
                subtitle = if (uiState.showError) uiState.feedbackSubTextError
                           else stringResource(uiState.feedbackSubTextRes),
                isSuccess = uiState.showSuccess,
                isVisible = uiState.showError || uiState.showSuccess
            )
        }

        if (uiState.showBatchPopup) {
            ActivityCompletePopup(
                stars = when {
                    uiState.lastScore.toFloat() / uiState.totalQuestions >= 0.8f -> 3
                    uiState.lastScore.toFloat() / uiState.totalQuestions >= 0.5f -> 2
                    else -> 1
                },
                score = uiState.lastScore,
                total = uiState.totalQuestions,
                scoreLabel = uiState.scoreLabel,
                feedbackTextRes = uiState.feedbackBatchTextRes,
                feedbackSubTextRes = uiState.feedbackBatchSubTextRes,
                onNext = { viewModel.loadNewBatch() },
                onClose = { navController.popBackStack() }
            )
        }
    }
}
