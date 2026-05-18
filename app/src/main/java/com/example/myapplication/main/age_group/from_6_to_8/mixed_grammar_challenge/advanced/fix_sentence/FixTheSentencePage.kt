package com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.advanced.fix_sentence

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCut
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_6_to_8.common.ResultView
import com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.advanced.fix_sentence.view_model.FixOptionState
import com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.advanced.fix_sentence.view_model.FixTheSentenceViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.FeedbackText
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.buttons.KidsLabel
import com.example.myapplication.main.common.getImageResForSentence
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.mixGrammarAdvanceFixSentenceImageSize
import com.example.myapplication.ui.theme.ButtonType

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FixTheSentencePage(
    navController: NavController,
    viewModel: FixTheSentenceViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Box(Modifier.fillMaxSize()) {
        BackgroundUI(isGreenGrassShow = false)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            // ── Header ────────────────────────────────────────────────────────
            Row(verticalAlignment = Alignment.CenterVertically) {
                BackButtonWithText(
                    title = stringResource(R.string.fix_the_sentence),
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
                        icon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        isIconStart = false,
                        isSmall = true,
                        type = when {
                            !uiState.showNext   -> ButtonType.DISABLE
                            uiState.isLastIndex -> ButtonType.POSITIVE
                            else                -> ButtonType.ORANGE
                        },
                        disable = !uiState.showNext,
                        onClick = { viewModel.moveToNext() },
                        modifier = Modifier.padding(end = Dimens16)
                    )
                }
            }

            // ── Body ──────────────────────────────────────────────────────────
            if (uiState.isCompleted) {
                ResultView(
                    score = uiState.score,
                    total = uiState.questions.size,
                    title = stringResource(R.string.your_result),
                    firstBtnTxt = stringResource(R.string.go_back),
                    onBack = { navController.popBackStack() },
                    onContinue = { viewModel.restart() },
                    modifier = Modifier.padding(horizontal = Dimens16)
                )
            } else {
                uiState.currentQuestion?.let { q ->
                    val typeColor = viewModel.typeColor(q.targetType)

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = Dimens16),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(Modifier.weight(1f))

                        // ── Image + instruction panel ─────────────────────────
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Image
                            getImageResForSentence(q.imageName)?.let { resId ->
                                Image(
                                    painter = painterResource(id = resId),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(mixGrammarAdvanceFixSentenceImageSize)
                                        .shadow(Dimens8, RoundedCornerShape(Dimens16))
                                        .clip(RoundedCornerShape(Dimens16))
                                )
                            }

                            Spacer(Modifier.width(Dimens16))

                            // Instruction badge + hint line
                            Column(
                                verticalArrangement = Arrangement.spacedBy(Dimens8)
                            ) {
                                // Instruction badge (scissors)
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(Dimens12))
                                        .background(Color(0xFFC62828).copy(alpha = 0.10f))
                                        .border(
                                            width = 1.5.dp,
                                            color = Color(0xFFC62828).copy(alpha = 0.3f),
                                            shape = RoundedCornerShape(Dimens12)
                                        )
                                        .padding(horizontal = Dimens12, vertical = Dimens8),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(Dimens6)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.ContentCut,
                                        contentDescription = null,
                                        tint = Color(0xFFC62828),
                                        modifier = Modifier.size(Dimens16)
                                    )
                                    Text(
                                        text = AnnotatedString.fromHtml(stringResource(R.string.tap_the_correct_word_to_replace_wrong_word)),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Black.copy(alpha = 0.75f)
                                    )
                                }

                                // Hint line: "Pick the correct [noun/verb/...]"
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(Dimens6)
                                ) {
                                    Icon(
                                        imageVector = viewModel.typeIcon(q.targetType),
                                        contentDescription = null,
                                        tint = typeColor,
                                        modifier = Modifier.size(Dimens16)
                                    )
                                    Text(
                                        text = buildAnnotatedString {
                                            append("Pick the correct ")
                                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                                append(viewModel.typeLabel(q.targetType).lowercase())
                                            }
                                        },
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        color = typeColor
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(Dimens24))

                        // ── Sentence word chips ───────────────────────────────
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            itemVerticalAlignment = Alignment.CenterVertically
                        ) {
                            q.displayWords.forEachIndexed { index, fixWord ->
                                val displayText = if (index == 0)
                                    fixWord.text.replaceFirstChar { it.uppercaseChar() }
                                else
                                    fixWord.text

                                if (fixWord.isWrong) {
                                    Text(
                                        text = displayText,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = Color.White,
                                        modifier = Modifier
                                            .padding(horizontal = 3.dp, vertical = 4.dp)
                                            .clip(RoundedCornerShape(Dimens8))
                                            .background(Color.Red.copy(alpha = 0.85f))
                                            .border(1.5.dp, Color.Red, RoundedCornerShape(Dimens8))
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                } else {
                                    Text(
                                        text = displayText,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = Color.Black.copy(alpha = 0.85f),
                                        modifier = Modifier.padding(horizontal = 3.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(Dimens24))

                        // ── Option chips ──────────────────────────────────────
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            q.options.forEachIndexed { index, option ->
                                if (index > 0) Spacer(Modifier.width(Dimens12))
                                FixOptionChip(
                                    option = option,
                                    state = viewModel.optionState(option),
                                    enabled = !uiState.isAnswerSubmitted,
                                    onClick = { viewModel.selectOption(option) }
                                )
                            }
                        }

                        Spacer(Modifier.weight(1f))

                        // ── Feedback ──────────────────────────────────────────
                        FeedbackText(
                            title = uiState.feedbackTitleRes?.let { stringResource(it) }?:"",
                            subtitle = uiState.feedbackTitleRes?.let { stringResource(it) }?:"",
                            isSuccess = uiState.isAnswerCorrect,
                            isVisible = uiState.isAnswerSubmitted,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        Spacer(Modifier.height(Dimens16))
                    }
                }
            }
        }
    }
}

// ── Option Chip ───────────────────────────────────────────────────────────────

@Composable
private fun FixOptionChip(
    option: String,
    state: FixOptionState,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val bgTarget = when (state) {
        FixOptionState.NORMAL   -> Color.White
        FixOptionState.SELECTED -> Color(0xFF5C6BC0).copy(alpha = 0.15f)
        FixOptionState.CORRECT  -> Color(0xFF2E7D32).copy(alpha = 0.15f)
        FixOptionState.WRONG    -> Color.Red.copy(alpha = 0.12f)
    }
    val borderTarget = when (state) {
        FixOptionState.NORMAL   -> Color.Gray.copy(alpha = 0.3f)
        FixOptionState.SELECTED -> Color(0xFF5C6BC0)
        FixOptionState.CORRECT  -> Color(0xFF2E7D32)
        FixOptionState.WRONG    -> Color.Red
    }
    val textColor = when (state) {
        FixOptionState.NORMAL   -> Color.Black
        FixOptionState.SELECTED -> Color(0xFF5C6BC0)
        FixOptionState.CORRECT  -> Color(0xFF2E7D32)
        FixOptionState.WRONG    -> Color.Red
    }
    val icon: ImageVector? = when (state) {
        FixOptionState.CORRECT -> Icons.Filled.CheckCircle
        FixOptionState.WRONG   -> Icons.Filled.Cancel
        else                   -> null
    }

    val bg by animateColorAsState(targetValue = bgTarget, animationSpec = tween(200), label = "chipBg")
    val border by animateColorAsState(targetValue = borderTarget, animationSpec = tween(200), label = "chipBorder")

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(Dimens12))
            .background(bg)
            .border(2.dp, border, RoundedCornerShape(Dimens12))
            .clickable(enabled = enabled) { onClick() }
            .padding(horizontal = Dimens16, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(16.dp)
            )
        }
        Text(
            text = option.lowercase(),
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
            color = textColor
        )
    }
}
