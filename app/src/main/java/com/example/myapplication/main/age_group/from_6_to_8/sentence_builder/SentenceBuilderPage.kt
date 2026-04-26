package com.example.myapplication.main.age_group.from_6_to_8.sentence_builder

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.SentenceUnit
import com.example.myapplication.main.age_group.from_6_to_8.common.ResultView
import com.example.myapplication.main.age_group.from_6_to_8.sentence_builder.view_model.SentenceBuilderViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.buttons.KidsLabel
import com.example.myapplication.ui.theme.AppDimens.Dimens1
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.extensions.scaled


@Composable
fun SentenceBuilderPage(
    unit: SentenceUnit,
    level: SentenceLevel,
    navController: NavController,
    viewModel: SentenceBuilderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.setData(unit, level)
    }

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
                    title = stringResource(R.string.sentenceBuilder),
                    modifier = Modifier.weight(1f),
                    onBackClick = { navController.popBackStack() }
                )

                KidsLabel("Question ${uiState.currentIndex + 1} / ${uiState.questions.size}")
            }

            Spacer(Modifier.weight(1f))
            // CONTENT
            if (!uiState.isCompleted) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens16),
                    verticalArrangement = Arrangement.spacedBy(Dimens16)
                ) {

                    // ARRANGED WORDS (top)
                    FlowRow(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        uiState.arrangedWords.forEach { word ->

                            val shape = RoundedCornerShape(Dimens12)
                            val interactionSource = remember { MutableInteractionSource() }
                            Text(
                                text = word,
                                style = MaterialTheme.typography.titleSmall.scaled(),
                                modifier = Modifier
                                    .padding(Dimens4)
                                    .clip(shape)
                                    .background(Color.Green.copy(alpha = 0.2f), shape)
                                    .clickable(
                                        enabled = uiState.isCorrect == null,
                                        interactionSource = interactionSource,
                                        indication = LocalIndication.current
                                    ) {
                                        viewModel.removeWord(word)
                                    }
                                    .padding(horizontal = Dimens12, vertical = Dimens6),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // SENTENCE PREVIEW
                    if (uiState.arrangedWords.isNotEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = viewModel.formattedSentence(),
                                style = MaterialTheme.typography.headlineMedium.scaled(),
                                modifier = Modifier
                                    .background(
                                        Color.Yellow.copy(0.2f),
                                        RoundedCornerShape(Dimens12)
                                    )
                                    .padding(horizontal = Dimens16, vertical = Dimens12),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // SHUFFLED WORDS
                    FlowRow(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        uiState.shuffledWords.forEach { word ->
                            val shape = RoundedCornerShape(Dimens12)
                            val interactionSource = remember { MutableInteractionSource() }
                            Text(
                                text = word,
                                style = MaterialTheme.typography.titleSmall.scaled(),
                                modifier = Modifier
                                    .padding(Dimens4)
                                    .clip(shape)
                                    .background(Color.White, shape)
                                    .border(Dimens1, Color.Gray.copy(0.2f), shape)
                                    .clickable(
                                        enabled = uiState.isCorrect == null,
                                        interactionSource = interactionSource,
                                        indication = LocalIndication.current
                                    ) {
                                        viewModel.addWord(word)
                                    }
                                    .padding(horizontal = Dimens12, vertical = Dimens6),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // RESULT
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

                            uiState.currentQuestion?.let {
                                Text(
                                    text = buildAnnotatedString {
                                        withStyle(
                                            style = SpanStyle(
                                                color = Color(0xFF2E7D32),
                                                fontWeight = FontWeight.Bold
                                            )
                                        ) {
                                            append(stringResource(R.string.correct_sentence_is))
                                        }

                                        append(it.correctSentence)
                                    },
                                    style = MaterialTheme.typography.titleMedium.scaled(),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    // NEXT BUTTON
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        KidsActionButton(
                            text = stringResource(R.string.next),
                            type = if (uiState.isCorrect == null)
                                ButtonType.DISABLE else ButtonType.ORANGE,
                            onClick = {
                                if (uiState.isCorrect != null){
                                    viewModel.next()
                                }
                            }
                        )
                    }
                }

            } else {

                ResultView(uiState.score,uiState.questions.size, title = stringResource(R.string.completed),
                    onBack = {
                        navController.popBackStack()
                    },onContinue = {
                        viewModel.restart()
                    })
            }

            Spacer(Modifier.weight(1f))
        }
    }
}
