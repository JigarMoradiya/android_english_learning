package com.example.myapplication.main.age_group.from_3_to_5.letter_recognition.speed_game

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_3_to_5.letter_recognition.speed_game.view_model.LetterSpeedGameUiState
import com.example.myapplication.main.age_group.from_3_to_5.letter_recognition.speed_game.view_model.LetterSpeedGameViewModel
import com.example.myapplication.main.common.ActivityCompletePopup
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.CountdownBadge
import com.example.myapplication.main.common.InstructionBadge
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.buttons.KidsLabel
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled

// "Find the Letter Fast" — a 5-round batch, same shape as Article Choice /
// Missing Letter: one question on screen at a time, tap an answer, a 3-2-1
// countdown before the next one, then a result popup. Only 4 letters ever
// show on screen at once (the target + 3 decoys) — a full alphabet grid
// buries the right answer in noise for this age group.
@Composable
fun LetterSpeedGamePage(
    navController: NavController,
    viewModel: LetterSpeedGameViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState

    LaunchedEffect(Unit) { viewModel.loadNewBatch() }
    DisposableEffect(Unit) {
        onDispose { viewModel.stopTimer() }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.peachCoral, shape = KidsFloatingShape.bubbles)

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
                    title = stringResource(R.string.menu_speed_round),
                    expandWidth = false,
                    onBackClick = {
                        viewModel.stopTimer()
                        navController.popBackStack()
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                if (uiState.selectedAnswer != null) {
                    KidsLabel("${uiState.questionIndex + 1}/${uiState.totalQuestions}", type = ButtonType.PURPLE)
                    CountdownBadge(count = uiState.countdown,modifier = Modifier.padding(end = Dimens16))
                } else {
                    InstructionBadge(
                        text = stringResource(R.string.tap_a_letter_you_hear),
                        isSmall = true,
                        modifier = Modifier.padding(end = Dimens16)
                    )
                    KidsLabel("${uiState.questionIndex + 1}/${uiState.totalQuestions}", type = ButtonType.PURPLE)
                }
            }

            Box(
                modifier = Modifier.weight(1f).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Tappable so a kid who missed the sound can hear it
                    // again — this game is audio-first.
                    ReplayAudioButton(
                        enabled = uiState.selectedAnswer == null,
                        onClick = { viewModel.replayTargetAudio() }
                    )

                    Spacer(modifier = Modifier.height(Dimens16))

                    OptionsRow(uiState = uiState, onTap = { viewModel.onTapLetter(it) })
                }
            }
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
                scoreLabel = stringResource(R.string.letters_found),
                feedbackText = stringResource(R.string.great_job),
                onNext = { viewModel.loadNewBatch() },
                nextLabel = stringResource(R.string.play_again),
                onClose = {
                    viewModel.stopTimer()
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
private fun ReplayAudioButton(enabled: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .shadow(6.dp, CircleShape, ambientColor = Color.Black.copy(alpha = 0.15f))
            .clip(CircleShape)
            .background(Color.White)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.VolumeUp,
            contentDescription = null,
            tint = Color(0xFFE67639),
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
private fun OptionsRow(uiState: LetterSpeedGameUiState, onTap: (Char) -> Unit) {
    // Only 4 choices on screen at once (the target + 3 decoys) — a full
    // alphabet grid buries the right answer in noise for this age group.
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val spacing = Dimens24
        val boxSize = min(
            (maxWidth - spacing * 3) / 4,
            100.dp
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            uiState.displayOptions.forEach { letter ->
                val hasAnswered = uiState.selectedAnswer != null
                val isCorrectTile = hasAnswered && letter == uiState.targetLetter
                val isWrongTap = hasAnswered && !uiState.isAnswerCorrect && letter == uiState.selectedAnswer
                LetterTile(
                    letter = letter,
                    boxSize = boxSize,
                    isWrongTap = isWrongTap,
                    isRevealCorrect = isCorrectTile,
                    enabled = !hasAnswered,
                    onTap = { onTap(letter) }
                )
            }
        }
    }
}

@Composable
private fun LetterTile(
    letter: Char,
    boxSize: Dp,
    isWrongTap: Boolean,
    isRevealCorrect: Boolean,
    enabled: Boolean,
    onTap: () -> Unit
) {
    val backgroundColor = when {
        isRevealCorrect -> Color(0xFF43A047)
        isWrongTap -> Color.Red.copy(alpha = 0.6f)
        else -> Color(0xFF3674B5).copy(alpha = 0.2f)
    }
    val textColor = if (isRevealCorrect || isWrongTap) Color.White else Color.Black
    val scale by animateFloatAsState(
        targetValue = if (isRevealCorrect) 1.15f else 1f,
        animationSpec = tween(200),
        label = "tileScale"
    )

    Box(
        modifier = Modifier
            .size(boxSize)
            .scale(scale)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable(enabled = enabled, onClick = onTap),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter.toString(),
            fontSize = (boxSize.value * 0.55f).sp.scaled(),
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}
