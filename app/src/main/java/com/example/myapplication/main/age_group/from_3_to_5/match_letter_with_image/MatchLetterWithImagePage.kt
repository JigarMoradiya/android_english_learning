package com.example.myapplication.main.age_group.from_3_to_5.match_letter_with_image

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.main.age_group.from_3_to_5.match_letter_with_image.view_model.MatchLetterWithImageViewModel
import com.example.myapplication.main.common.ActivityCompletePopup
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.InstructionBadge
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.buttons.KidsLabel
import com.example.myapplication.ui.theme.AppDimens.Dimens12

@Composable
fun MatchLetterWithImagePage(
    navController: NavController,
    viewModel: MatchLetterWithImageViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState

    Box(modifier = Modifier.fillMaxSize()) {

        KidsGradientBackground(gradient = KidsGradient.tealCyan, shape = KidsFloatingShape.musicNotes)

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
                    title = stringResource(R.string.match_letter_image),
                    modifier = Modifier.weight(1f),
                    onBackClick = { navController.popBackStack() }
                )

                Row(verticalAlignment = Alignment.CenterVertically,modifier = Modifier.padding(top = DeviceInfo.screenTopPadding())) {
                    InstructionBadge(
                        text = stringResource(R.string.drag_to_connect_letters),
                        isSmall = true,
                        modifier = Modifier.padding(horizontal = Dimens12)
                    )
                    KidsLabel("🎯 Round ${uiState.round}")
                }
            }

            MatchContent(
                viewModel,
                modifier = Modifier.weight(1f)
            )
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
