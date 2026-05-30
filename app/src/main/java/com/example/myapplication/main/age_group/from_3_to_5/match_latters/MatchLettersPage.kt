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
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_3_to_5.match_latters.view_model.MatchLettersViewModel
import com.example.myapplication.main.common.ActivityCompletePopup
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.buttons.KidsLabel
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.MatchLetterBoxSize
import com.example.myapplication.ui.theme.PrimaryBlue
import com.example.myapplication.ui.theme.PrimaryGreen
import com.example.myapplication.utils.extensions.scaled

@Composable
fun MatchLettersPage(
    navController: NavController,
    viewModel: MatchLettersViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState

    Box(modifier = Modifier.fillMaxSize()) {

        KidsGradientBackground(gradient = KidsGradient.skyLavender, shape = KidsFloatingShape.diamonds)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButtonWithText(
                    title = stringResource(R.string.match_letters),
                    modifier = Modifier.weight(1f),
                    onBackClick = { navController.popBackStack() }
                )
                KidsLabel("🎯  Round ${uiState.round}")
            }

            Spacer(Modifier.weight(1f))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Uppercase row
                Row(horizontalArrangement = Arrangement.spacedBy(Dimens12)) {
                    uiState.currentBatch.forEach { letter ->
                        val isMatched = uiState.matchedPairs.contains(letter)
                        val isSelected = uiState.selectedUpper == letter

                        Box(
                            modifier = Modifier
                                .size(MatchLetterBoxSize)
                                .clip(RoundedCornerShape(Dimens12))
                                .background(
                                    when {
                                        isMatched -> PrimaryGreen.copy(alpha = 0.3f)
                                        isSelected -> PrimaryBlue.copy(alpha = 0.5f)
                                        else -> Color.Gray.copy(alpha = 0.3f)
                                    }
                                )
                                .clickable(enabled = !isMatched) { viewModel.selectUpper(letter) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = letter.toString(),
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = (MatchLetterBoxSize.value * 0.75).sp,
                                    shadow = Shadow(
                                        color = if (isMatched) Color.Transparent else Color.Black.copy(alpha = 0.6f),
                                        offset = Offset(2f, 2f),
                                        blurRadius = 2f
                                    )
                                ),
                                color = if (isMatched) Color.DarkGray.copy(alpha = 0.4f) else PrimaryBlue,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(Modifier.height(Dimens24))

                // Lowercase row
                Row(horizontalArrangement = Arrangement.spacedBy(Dimens12)) {
                    uiState.shuffledLowercase.forEach { letter ->
                        val isMatched = uiState.matchedPairs.contains(letter.uppercaseChar())
                        val isSelected = uiState.selectedLower == letter

                        Box(
                            modifier = Modifier
                                .size(MatchLetterBoxSize)
                                .clip(RoundedCornerShape(Dimens12))
                                .background(
                                    when {
                                        isMatched -> PrimaryGreen.copy(alpha = 0.3f)
                                        isSelected -> PrimaryBlue.copy(alpha = 0.5f)
                                        else -> Color.Gray.copy(alpha = 0.3f)
                                    }
                                )
                                .clickable(enabled = !isMatched) { viewModel.selectLower(letter) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = letter.lowercaseChar().toString(),
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = (MatchLetterBoxSize.value * 0.75).sp,
                                    shadow = Shadow(
                                        color = if (isMatched) Color.Transparent else Color.Black.copy(alpha = 0.6f),
                                        offset = Offset(2f, 2f),
                                        blurRadius = 2f
                                    )
                                ),
                                color = if (isMatched) Color.DarkGray.copy(alpha = 0.4f) else PrimaryBlue,
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
            ActivityCompletePopup(
                stars = uiState.earnedStars,
                score = uiState.batchScore,
                total = 5,
                scoreLabel = uiState.scoreLabel,
                feedbackTextRes = uiState.feedbackTextRes,
                feedbackSubTextRes = uiState.feedbackSubTextRes,
                onNext = { viewModel.playAgain() },
                onClose = {
                    viewModel.closePopup()
                    navController.popBackStack()
                }
            )
        }
    }
}
