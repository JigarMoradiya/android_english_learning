package com.example.myapplication.main.age_group.from_5_to_7.sight_words.speed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_5_to_7.sight_words.speed.view_model.SightWordSpeedViewModel
import com.example.myapplication.main.common.ActivityCompletePopup
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.GameTimerBar
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.buttons.KidsLabel
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.SightWordFont
import com.example.myapplication.ui.theme.AppDimens.grammarBasicOptionsWidth

/**
 * Speed reading mode: sight words flash one at a time, starting slow
 * and getting faster — the child reads each word aloud before it disappears.
 */
@Composable
fun SightWordSpeedPage(
    navController: NavController,
    viewModel: SightWordSpeedViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState

    Box(Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.mintLime, shape = KidsFloatingShape.musicNotes)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                BackButtonWithText(
                    title = stringResource(R.string.speed_mode_title),
                    modifier = Modifier.weight(1f),
                    onBackClick = { navController.popBackStack() }
                )

                KidsLabel(txt = "${minOf(uiState.index + 1, uiState.words.size)}/${uiState.words.size}")

                IconButton(onClick = { viewModel.togglePause() }) {
                    Icon(
                        imageVector = if (uiState.isPaused) Icons.Filled.PlayArrow else Icons.Filled.Pause,
                        contentDescription = if (uiState.isPaused) "Play" else "Pause",
                        tint = Color(0xFF2E7D32)
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens16),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens20)
            ) {
                Text(
                    text = stringResource(R.string.speed_mode_prompt),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = uiState.currentWord,
                    style = MaterialTheme.typography.displayLarge.copy(fontSize = SightWordFont),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                GameTimerBar(
                    progress = uiState.progress,
                    modifier = Modifier.width(grammarBasicOptionsWidth * 1.6f)
                )

                Text(
                    text = viewModel.currentDurationLabel,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Spacer(Modifier.weight(1f))
        }

        if (uiState.showComplete) {
            ActivityCompletePopup(
                stars = 3,
                score = uiState.words.size,
                total = uiState.words.size,
                scoreLabel = "speed reader 🚀",
                feedbackText = stringResource(R.string.your_result),
                onNext = { viewModel.restart() },
                onClose = { navController.popBackStack() }
            )
        }
    }
}
