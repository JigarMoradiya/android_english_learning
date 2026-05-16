package com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.beginner.tap_the_word

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_6_to_8.common.ResultView
import com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.beginner.tap_the_word.view_model.TapTheWordViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.FeedbackText
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.buttons.KidsLabel
import com.example.myapplication.main.common.getImageResForSentence
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.extensions.scaled

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TapTheWordPage(
    navController: NavController,
    viewModel: TapTheWordViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundUI(isGreenGrassShow = false)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {

            // ── Header ────────────────────────────────────────────────────────
            Row(verticalAlignment = Alignment.CenterVertically) {
                BackButtonWithText(
                    title = stringResource(R.string.tap_the_word_),
                    modifier = Modifier.weight(1f),
                    onBackClick = { navController.popBackStack() }
                )

                KidsLabel(
                    txt = "Question ${uiState.currentIndex + 1} / ${uiState.questions.size}",
                    type = ButtonType.PURPLE
                )

                if (!uiState.isCompleted) {
                    KidsActionButton(
                        text = if (uiState.isLastIndex) stringResource(R.string.check_result)
                               else stringResource(R.string.next),
                        icon = if (uiState.isLastIndex) null
                               else Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        isIconStart = false,
                        isSmall = true,
                        type = when {
                            !uiState.showNext  -> ButtonType.DISABLE
                            uiState.isLastIndex -> ButtonType.POSITIVE
                            else               -> ButtonType.ORANGE
                        },
                        disable = !uiState.showNext,
                        onClick = { viewModel.moveToNextQuestion() },
                        modifier = Modifier.padding(end = Dimens16)
                    )
                }
            }

            // ── Body ──────────────────────────────────────────────────────────
            if (uiState.isCompleted) {
                Spacer(Modifier.weight(1f))
                ResultView(
                    score = uiState.score,
                    total = uiState.questions.size,
                    title = "Tap the Word",
                    firstBtnTxt = stringResource(R.string.go_back),
                    onBack = { navController.popBackStack() },
                    onContinue = { viewModel.restart() },
                    modifier = Modifier.padding(horizontal = Dimens16)
                )
                Spacer(Modifier.weight(1f))
            } else {
                uiState.currentQuestion?.let { q ->

                    val typeColor = viewModel.typeColor(q.targetType)
                    val typeLabel = viewModel.typeLabel(q.targetType)

                    // HStack: Left 30% image | Right 70% content (mirrors iOS)
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {

                        // ── Left section: Image (30%) ─────────────────────────
                        Column(
                            modifier = Modifier
                                .weight(0.35f)
                                .fillMaxHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            getImageResForSentence(q.imageName)?.let {
                                Image(
                                    painter = painterResource(it),
                                    contentDescription = q.imageName,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth(0.85f)
                                        .aspectRatio(1f)
                                        .shadow(Dimens6, RoundedCornerShape(Dimens12))
                                        .clip(RoundedCornerShape(Dimens12))
                                )
                            }
                        }

                        // ── Right section: Sentence + Badge + Word Chips (70%) ─
                        Box(
                            modifier = Modifier
                                .weight(0.65f)
                                .fillMaxHeight()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.Center)
                                    .padding(horizontal = Dimens12),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(Dimens16)
                            ) {
                                Spacer(Modifier.weight(1f))

                                // Sentence text
                                Text(
                                    text = q.sentence,
                                    style = MaterialTheme.typography.titleMedium.scaled(),
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = Dimens12)
                                )

                                // Instruction badge: "Tap the [VERB] in the sentence"
                                TapInstructionBadge(
                                    icon = viewModel.tapBadgeIcon(),
                                    typeLabel = typeLabel,
                                    typeColor = typeColor
                                )

                                // Word chips — FlowRow so they wrap naturally
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(Dimens8, Alignment.CenterHorizontally),
                                    verticalArrangement = Arrangement.spacedBy(Dimens8),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = Dimens16)
                                ) {
                                    q.sentenceWords.forEach { word ->
                                        TapWordChip(
                                            word = word,
                                            chipType = viewModel.wordChipType(word),
                                            enabled = !uiState.showNext,
                                            onTap = {
                                                AudioPlayerManager.playSoundMenuClick()
                                                viewModel.selectAnswer(word)
                                            }
                                        )
                                    }
                                }

                                Spacer(Modifier.weight(1f))

                                // Feedback overlay pinned to bottom of right section
                                val subtitle = if (uiState.showNext){
                                    if (uiState.isAnswerCorrect){
                                        uiState.feedbackSubTitle?.let { stringResource(it) }?:""
                                    }else{
                                        stringResource(R.string.the_was_font_color_2e7d32_b_b_font, typeLabel.lowercase(), q.correctWord)
                                    }
                                }else{
                                    ""
                                }
                                FeedbackText(
                                    title = uiState.feedbackTitleRes?.let { stringResource(it) }?:"",
                                    subtitle = subtitle,
                                    isSuccess = uiState.isAnswerCorrect,
                                    isVisible = uiState.showNext,
                                    modifier = Modifier
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── Instruction Badge ─────────────────────────────────────────────────────────
// "Tap the [VERB] in the sentence"

@Composable
private fun TapInstructionBadge(
    icon: ImageVector,
    typeLabel: String,
    typeColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .background(typeColor.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
            .border(1.5.dp, typeColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = typeColor,
            modifier = Modifier.size(18.dp)
        )

        Text(
            text = "Tap the",
            style = MaterialTheme.typography.bodyMedium.scaled(),
            color = Color.Black.copy(alpha = 0.7f)
        )

        Text(
            text = typeLabel,
            style = MaterialTheme.typography.bodyMedium.scaled(),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .background(typeColor, RoundedCornerShape(50.dp))
                .padding(horizontal = 12.dp, vertical = 4.dp)
        )

        Text(
            text = "in the sentence",
            style = MaterialTheme.typography.bodyMedium.scaled(),
            color = Color.Black.copy(alpha = 0.7f)
        )
    }
}

// ── Word Chip ─────────────────────────────────────────────────────────────────

@Composable
private fun TapWordChip(
    word: String,
    chipType: ButtonType,
    enabled: Boolean,
    onTap: () -> Unit
) {
    val bgColor = when (chipType) {
        ButtonType.GREEN -> Color(0xFF388E3C).copy(alpha = 0.85f)
        ButtonType.RED   -> Color.Red.copy(alpha = 0.85f)
        else             -> Color.White
    }
    val textColor = when (chipType) {
        ButtonType.GREEN, ButtonType.RED -> Color.White
        else -> Color.Black
    }

    Box(
        modifier = Modifier
            .shadow(3.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.dp, Color.Gray.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
            .clickable(enabled = enabled) { onTap() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = word,
            style = MaterialTheme.typography.titleSmall.scaled(),
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}
