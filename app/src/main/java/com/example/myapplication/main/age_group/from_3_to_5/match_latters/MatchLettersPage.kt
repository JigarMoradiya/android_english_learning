package com.example.myapplication.main.age_group.from_3_to_5.match_latters

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_3_to_5.match_latters.view_model.MatchLettersViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.CustomPopupView
import com.example.myapplication.ui.theme.AppDimens.MatchLetterBoxSize
import com.example.myapplication.ui.theme.PrimaryBlue
import com.example.myapplication.ui.theme.PrimaryGreen


@Composable
fun MatchLettersPage(
    navController: NavController,
    viewModel: MatchLettersViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState

    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundUI(isGreenGrassShow = false)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {

            // HEADER
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                BackButtonWithText(
                    title = stringResource(R.string.match_letters),
                    modifier = Modifier.weight(1f),
                    onBackClick = { navController.popBackStack() }
                )

                Spacer(Modifier.weight(1f))

                Text(
                    text = "Round ${uiState.round}",
                    fontSize = 22.sp,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }

            Spacer(Modifier.weight(1f))

            // CENTER CONTENT
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // -------------------------
                // UPPERCASE
                // -------------------------
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                    uiState.currentBatch.forEach { letter ->

                        val isMatched = uiState.matchedPairs.contains(letter)
                        val isSelected = uiState.selectedUpper == letter

                        Box(
                            modifier = Modifier
                                .size(MatchLetterBoxSize)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    when {
                                        isMatched -> PrimaryGreen.copy(alpha = 0.3f)
                                        isSelected -> PrimaryBlue.copy(alpha = 0.5f)
                                        else -> Color.Gray.copy(alpha = 0.3f)
                                    }
                                )
                                .clickable(enabled = !isMatched) {
                                    viewModel.selectUpper(letter)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = letter.toString(),
                                fontSize = (MatchLetterBoxSize.value * 0.75).sp,
                                color = when {
                                    isMatched -> Color.DarkGray.copy(alpha = 0.4f)
                                    else -> PrimaryBlue
                                },
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // -------------------------
                // LOWERCASE
                // -------------------------
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                    uiState.shuffledLowercase.forEach { letter ->

                        val isMatched = uiState.matchedPairs.contains(letter.uppercaseChar())
                        val isSelected = uiState.selectedLower == letter

                        Box(
                            modifier = Modifier
                                .size(MatchLetterBoxSize)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    when {
                                        isMatched -> PrimaryGreen.copy(alpha = 0.3f)
                                        isSelected -> PrimaryBlue.copy(alpha = 0.5f)
                                        else -> Color.Gray.copy(alpha = 0.3f)
                                    }
                                )
                                .clickable(enabled = !isMatched) {
                                    viewModel.selectLower(letter)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = letter.lowercaseChar().toString(),
                                fontSize = (MatchLetterBoxSize.value * 0.75).sp,
                                color =  when {
                                    isMatched -> Color.DarkGray.copy(alpha = 0.4f)
                                    else -> PrimaryBlue
                                },
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))
        }


        AnimatedVisibility(
            visible = uiState.showPopup,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            CustomPopupView(
                title = uiState.feedbackText,
                description = uiState.feedbackSubText,
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

