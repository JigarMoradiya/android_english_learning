package com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.advanced.sentence_builder

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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.HorizontalDivider
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
import com.example.myapplication.data.generation.loader.AdvBuildWord
import com.example.myapplication.main.age_group.from_6_to_8.common.ResultView
import com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.advanced.sentence_builder.view_model.GrammarSentenceBuilderViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.FeedbackText
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.buttons.KidsLabel
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GrammarSentenceBuilderPage(
    navController: NavController,
    viewModel: GrammarSentenceBuilderViewModel = hiltViewModel()
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
                    title = "Sentence Builder",
                    modifier = Modifier.weight(1f),
                    onBackClick = { navController.popBackStack() }
                )

                KidsLabel(
                    txt = "Q ${uiState.currentIndex + 1} / ${uiState.questions.size}",
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
                            !uiState.showNext   -> ButtonType.DISABLE
                            uiState.isLastIndex -> ButtonType.POSITIVE
                            else                -> ButtonType.ORANGE
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
                    title = "Sentence Builder",
                    firstBtnTxt = stringResource(R.string.go_back),
                    onBack = { navController.popBackStack() },
                    onContinue = { viewModel.restart() },
                    modifier = Modifier.padding(horizontal = Dimens16)
                )
                Spacer(Modifier.weight(1f))
            } else {
                uiState.currentQuestion?.let { q ->

                    val imageRes = remember(q.imageName) {
                        context.resources.getIdentifier(q.imageName, "drawable", context.packageName)
                    }

                    // HStack: Left 40% | Right 60% (mirrors iOS)
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {

                        // ── Left Panel: Image + Instruction Badge (40%) ───────
                        Column(
                            modifier = Modifier
                                .weight(0.4f)
                                .fillMaxHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Spacer(Modifier.weight(1f))

                            // Image
                            if (imageRes != 0) {
                                Image(
                                    painter = painterResource(imageRes),
                                    contentDescription = q.imageName,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .fillMaxWidth(0.8f)
                                        .aspectRatio(1f)
                                        .shadow(8.dp, RoundedCornerShape(Dimens16))
                                        .clip(RoundedCornerShape(Dimens16))
                                )
                            }

                            Spacer(Modifier.height(Dimens16))

                            // Instruction badge — blue (#1565C0)
                            SbInstructionBadge()

                            Spacer(Modifier.weight(1f))
                        }

                        // ── Right Panel: Answer Area + Word Pool + Feedback (60%) ─
                        Column(
                            modifier = Modifier
                                .weight(0.6f)
                                .fillMaxHeight()
                                .padding(horizontal = Dimens16),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(Dimens16)
                        ) {
                            Spacer(Modifier.weight(1f))

                            // Answer area — placed words
                            AnswerArea(
                                placedWords = uiState.placedWords,
                                isSubmitted = uiState.isAnswerSubmitted,
                                isCorrect = uiState.isAnswerCorrect,
                                correctSentence = if (uiState.isAnswerSubmitted && !uiState.isAnswerCorrect)
                                    q.sentence else null,
                                chipColor = { index -> viewModel.placedChipColor(index) },
                                onRemove = { word -> viewModel.removeWord(word) }
                            )

                            // Hide word pool once answer submitted (mirrors iOS)
                            if (!uiState.isAnswerSubmitted) {
                                HorizontalDivider(
                                    color = Color.Gray.copy(alpha = 0.2f),
                                    modifier = Modifier.padding(horizontal = Dimens24)
                                )

                                // Word pool chips
                                WordPool(
                                    availableWords = uiState.availableWords,
                                    isSubmitted = uiState.isAnswerSubmitted,
                                    onPlace = { word -> viewModel.placeWord(word) }
                                )
                            }

                            Spacer(Modifier.weight(1f))

                            // Feedback
                            FeedbackText(
                                title = uiState.feedbackTitleRes?.let { stringResource(it) },
                                subtitle = uiState.feedbackSubTitleRes?.let { stringResource(it) },
                                isSuccess = uiState.isAnswerCorrect,
                                isVisible = uiState.isAnswerSubmitted && uiState.isAnswerCorrect
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Instruction Badge ─────────────────────────────────────────────────────────

@Composable
private fun SbInstructionBadge() {
    val blue = Color(0xFF1565C0)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .padding(horizontal = Dimens16)
            .background(blue.copy(alpha = 0.10f), RoundedCornerShape(12.dp))
            .border(1.5.dp, blue.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Edit,
            contentDescription = null,
            tint = blue,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = "Arrange the words to build a sentence",
            style = MaterialTheme.typography.bodySmall.scaled(),
            color = Color.Black.copy(alpha = 0.75f),
            textAlign = TextAlign.Center
        )
    }
}

// ── Answer Area ───────────────────────────────────────────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AnswerArea(
    placedWords: List<AdvBuildWord>,
    isSubmitted: Boolean,
    isCorrect: Boolean,
    correctSentence: String?,
    chipColor: (Int) -> Color,
    onRemove: (AdvBuildWord) -> Unit
) {
    val borderColor = when {
        isSubmitted && isCorrect  -> Color(0xFF388E3C)
        isSubmitted && !isCorrect -> Color.Red
        else                      -> Color(0xFF1565C0).copy(alpha = 0.3f)
    }

    Column(verticalArrangement = Arrangement.spacedBy(Dimens8)) {
        Text(
            text = "Your Sentence",
            style = MaterialTheme.typography.labelMedium.scaled(),
            color = Color.Black.copy(alpha = 0.5f),
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(Dimens12))
                .border(2.dp, borderColor, RoundedCornerShape(Dimens12))
                .padding(Dimens8)
        ) {
            if (placedWords.isEmpty()) {
                Text(
                    text = "Tap words below to build your sentence…",
                    style = MaterialTheme.typography.bodyMedium.scaled(),
                    color = Color.Gray.copy(alpha = 0.5f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens8),
                    textAlign = TextAlign.Center
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(Dimens6)) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(Dimens6),
                        verticalArrangement = Arrangement.spacedBy(Dimens6)
                    ) {
                        placedWords.forEachIndexed { index, word ->
                            PlacedWordChip(
                                word = word,
                                index = index,
                                isSubmitted = isSubmitted,
                                color = chipColor(index),
                                onRemove = onRemove
                            )
                        }
                    }

                    // Show correct sentence when wrong (mirrors iOS)
                    if (!correctSentence.isNullOrEmpty()) {
                        Text(
                            text = "Correct Sentence",
                            style = MaterialTheme.typography.labelSmall.scaled(),
                            color = Color(0xFF388E3C),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = correctSentence,
                            style = MaterialTheme.typography.bodyMedium.scaled(),
                            color = Color.Black,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Dimens12),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

// ── Placed Word Chip ──────────────────────────────────────────────────────────

@Composable
private fun PlacedWordChip(
    word: AdvBuildWord,
    index: Int,
    isSubmitted: Boolean,
    color: Color,
    onRemove: (AdvBuildWord) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens4),
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color)
            .clickable(enabled = !isSubmitted) { onRemove(word) }
            .padding(horizontal = Dimens10, vertical = Dimens6)
    ) {
        if (isSubmitted) {
            Icon(
                imageVector = if (color == Color(0xFF388E3C)) Icons.Filled.CheckCircle else Icons.Filled.Close,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(14.dp)
            )
        }
        Text(
            text = if (index == 0) word.text.replaceFirstChar { it.uppercase() }
                   else word.text.lowercase(),
            style = MaterialTheme.typography.labelLarge.scaled(),
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

// ── Word Pool ─────────────────────────────────────────────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun WordPool(
    availableWords: List<AdvBuildWord>,
    isSubmitted: Boolean,
    onPlace: (AdvBuildWord) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(Dimens8)) {
        Text(
            text = "Word Pool",
            style = MaterialTheme.typography.labelMedium.scaled(),
            color = Color.Black.copy(alpha = 0.5f),
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        if (availableWords.isEmpty() && !isSubmitted) {
            Text(
                text = "✓ All words placed!",
                style = MaterialTheme.typography.bodyMedium.scaled(),
                color = Color(0xFF1565C0),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(Dimens8, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(Dimens8),
                modifier = Modifier.fillMaxWidth()
            ) {
                availableWords.forEach { word ->
                    PoolWordChip(word = word, onClick = { onPlace(word) })
                }
            }
        }
    }
}

// ── Pool Word Chip ────────────────────────────────────────────────────────────

@Composable
private fun PoolWordChip(word: AdvBuildWord, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .shadow(3.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.5.dp, Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = Dimens10, vertical = Dimens6)
    ) {
        Text(
            text = word.text.lowercase(),
            style = MaterialTheme.typography.titleSmall.scaled(),
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
    }
}
