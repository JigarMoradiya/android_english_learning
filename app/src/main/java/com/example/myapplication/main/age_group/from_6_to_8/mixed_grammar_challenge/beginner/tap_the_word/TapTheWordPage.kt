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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.beginner.tap_the_word.view_model.TapTheWordViewModel
import com.example.myapplication.main.common.ActivityCompletePopup
import com.example.myapplication.main.common.BackButtonWithText
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
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TapTheWordPage(
    navController: NavController,
    viewModel: TapTheWordViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.lilacPink, shape = KidsFloatingShape.sparkles)

        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Box
        }

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

            // ── Body ──────────────────────────────────────────────────────────
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
                                .weight(0.4f)
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
                                        .fillMaxHeight(0.7f)
                                        .aspectRatio(1f)
                                        .shadow(Dimens6, RoundedCornerShape(Dimens12))
                                        .clip(RoundedCornerShape(Dimens12))
                                )
                            }
                        }

                        // ── Right section: Sentence + Badge + Word Chips (70%) ─
                        Box(
                            modifier = Modifier
                                .weight(0.6f)
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

                                // Instruction badge: "Tap all the [NOUN]s in the sentence"
                                TapInstructionBadge(
                                    icon = viewModel.tapBadgeIcon(),
                                    typeLabel = typeLabel,
                                    typeColor = typeColor,
                                    isPlural = q.allCorrectWords.size > 1
                                )

                                // Live progress — only when 2+ words must be found
                                if (q.allCorrectWords.size > 1 && !uiState.showNext) {
                                    val progressColor = Color(0xFF5C6BC0)  // neutral indigo — a hint, not correct/wrong
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(Dimens6),
                                        modifier = Modifier
                                            .background(progressColor.copy(alpha = 0.12f), RoundedCornerShape(50.dp))
                                            .border(1.dp, progressColor.copy(alpha = 0.3f), RoundedCornerShape(50.dp))
                                            .padding(horizontal = Dimens12, vertical = Dimens6)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Search,
                                            contentDescription = null,
                                            tint = progressColor,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        val remaining = q.allCorrectWords.size - uiState.foundWords.size
                                        Text(
                                            text = "Found ${uiState.foundWords.size} of ${q.allCorrectWords.size}" +
                                                   if (remaining > 0) " — keep going!" else "",
                                            style = MaterialTheme.typography.bodySmall.scaled(),
                                            fontWeight = FontWeight.Bold,
                                            color = progressColor
                                        )
                                    }
                                }

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
                                        uiState.feedbackSubTitleText ?: ""
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

        if (uiState.isCompleted) {
            val accuracy = if (uiState.questions.isNotEmpty())
                uiState.score.toDouble() / uiState.questions.size else 0.0
            ActivityCompletePopup(
                stars = when {
                    accuracy >= 0.8 -> 3
                    accuracy >= 0.5 -> 2
                    else -> 1
                },
                score = uiState.score,
                total = uiState.questions.size,
                scoreLabel = stringResource(R.string.correct_answers),
                feedbackTextRes = when {
                    accuracy >= 0.8 -> R.string.feedbackPhrases_1
                    accuracy >= 0.5 -> R.string.feedbackPhrases_2
                    else -> R.string.feedbackPhrases_3
                },
                onNext = { viewModel.restart() },
                nextLabel = stringResource(R.string.want_to_continue),
                dismissLabel = stringResource(R.string.go_back),
                onClose = { navController.popBackStack() }
            )
        }
    }
}

// ── Instruction Badge ─────────────────────────────────────────────────────────
// "Tap the [VERB] in the sentence"

@Composable
private fun TapInstructionBadge(
    icon: ImageVector,
    typeLabel: String,
    typeColor: Color,
    isPlural: Boolean
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
            text = if (isPlural) "Tap all the" else "Tap the",
            style = MaterialTheme.typography.bodyMedium.scaled(),
            color = Color.Black.copy(alpha = 0.7f)
        )

        Text(
            text = if (isPlural) "${typeLabel}s" else typeLabel,
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
