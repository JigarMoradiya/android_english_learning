package com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.advanced.sentence_builder

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_6_to_8.common.ResultView
import com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.advanced.sentence_builder.view_model.GrammarSentenceBuilderViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.buttons.KidsLabel
import com.example.myapplication.ui.theme.AppDimens.Dimens1
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GrammarSentenceBuilderPage(
    navController: NavController,
    viewModel: GrammarSentenceBuilderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundUI(isGreenGrassShow = false)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                BackButtonWithText(
                    title = "Sentence Builder",
                    modifier = Modifier.weight(1f),
                    onBackClick = { navController.popBackStack() }
                )
                KidsLabel(txt = "Q ${uiState.currentIndex + 1} / ${uiState.questions.size}")
            }

            if (uiState.isCompleted) {
                ResultView(
                    score = uiState.score,
                    total = uiState.questions.size,
                    title = "Sentence Builder",
                    firstBtnTxt = stringResource(R.string.go_back),
                    onBack = { navController.popBackStack() },
                    onContinue = { viewModel.load() },
                    modifier = Modifier.padding(horizontal = Dimens16)
                )
            } else {
                Spacer(Modifier.weight(1f))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens16),
                    verticalArrangement = Arrangement.spacedBy(Dimens12)
                ) {
                    Text(
                        text = "Arrange the words to build a correct sentence!",
                        style = MaterialTheme.typography.titleSmall.scaled(),
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(Dimens8))

                    // Arranged words (tap to remove)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(Dimens4, Alignment.CenterHorizontally),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF0F4FF), RoundedCornerShape(Dimens12))
                            .border(Dimens1, Color(0xFFBBCCFF), RoundedCornerShape(Dimens12))
                            .padding(Dimens12)
                    ) {
                        if (uiState.arrangedWords.isEmpty()) {
                            Text(
                                text = "Your sentence will appear here…",
                                style = MaterialTheme.typography.bodyMedium.scaled(),
                                color = Color.Gray,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        } else {
                            uiState.arrangedWords.forEach { word ->
                                val shape = RoundedCornerShape(Dimens12)
                                val interactionSource = remember { MutableInteractionSource() }
                                Text(
                                    text = word,
                                    style = MaterialTheme.typography.titleSmall.scaled(),
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier
                                        .padding(Dimens4)
                                        .clip(shape)
                                        .background(Color(0xFF42A5F5).copy(alpha = 0.2f), shape)
                                        .clickable(
                                            enabled = uiState.isCorrect == null,
                                            interactionSource = interactionSource,
                                            indication = LocalIndication.current
                                        ) { viewModel.removeWord(word) }
                                        .padding(horizontal = Dimens10, vertical = Dimens6)
                                )
                            }
                        }
                    }

                    // Formatted sentence preview
                    if (uiState.arrangedWords.isNotEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = viewModel.formattedSentence(),
                                style = MaterialTheme.typography.headlineSmall.scaled(),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1565C0),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .background(Color.Yellow.copy(alpha = 0.2f), RoundedCornerShape(Dimens12))
                                    .padding(horizontal = Dimens16, vertical = Dimens8)
                            )
                        }
                    }

                    Spacer(Modifier.height(Dimens8))

                    // Word pool (tap to add)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(Dimens4, Alignment.CenterHorizontally),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        uiState.shuffledWords.forEach { word ->
                            val shape = RoundedCornerShape(Dimens12)
                            val interactionSource = remember { MutableInteractionSource() }
                            Text(
                                text = word,
                                style = MaterialTheme.typography.titleSmall.scaled(),
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                                    .padding(Dimens4)
                                    .clip(shape)
                                    .background(Color.White, shape)
                                    .border(Dimens1, Color.Gray.copy(alpha = 0.3f), shape)
                                    .clickable(
                                        enabled = uiState.isCorrect == null,
                                        interactionSource = interactionSource,
                                        indication = LocalIndication.current
                                    ) { viewModel.addWord(word) }
                                    .padding(horizontal = Dimens12, vertical = Dimens6)
                            )
                        }
                    }

                    // Feedback
                    uiState.isCorrect?.let { correct ->
                        if (correct) {
                            Text(
                                text = stringResource(uiState.feedbackTextRes),
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleLarge.scaled(),
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.its_wrong),
                                color = Color.Red,
                                style = MaterialTheme.typography.titleLarge.scaled(),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            uiState.currentQuestion?.let { q ->
                                Text(
                                    text = buildAnnotatedString {
                                        withStyle(SpanStyle(color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)) {
                                            append(stringResource(R.string.correct_sentence_is))
                                        }
                                        append(q.correctSentence)
                                    },
                                    style = MaterialTheme.typography.titleMedium.scaled(),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    // Next button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        KidsActionButton(
                            text = if (viewModel.isLastQuestion) stringResource(R.string.check_result)
                                   else stringResource(R.string.next),
                            icon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                            isIconStart = false,
                            type = if (uiState.isCorrect == null) ButtonType.DISABLE else ButtonType.ORANGE,
                            disable = uiState.isCorrect == null,
                            onClick = { if (uiState.isCorrect != null) viewModel.next() }
                        )
                    }
                }

                Spacer(Modifier.weight(1f))
            }
        }
    }
}
