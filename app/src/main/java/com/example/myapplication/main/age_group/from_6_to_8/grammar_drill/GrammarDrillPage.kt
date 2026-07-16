package com.example.myapplication.main.age_group.from_6_to_8.grammar_drill

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.generation.loader.GrammarDrillType
import com.example.myapplication.main.age_group.from_6_to_8.grammar_drill.view_model.GrammarDrillViewModel
import com.example.myapplication.main.common.ActivityCompletePopup
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.buttons.KidsLabel
import com.example.myapplication.main.common.buttons.KidsOptionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.listenAndAnswerOptionsHeight
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled

@Composable
fun GrammarDrillPage(
    type: GrammarDrillType,
    navController: NavController,
    viewModel: GrammarDrillViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) { viewModel.setType(type) }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.skyLavender, shape = KidsFloatingShape.diamonds)

        Column(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)) {

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                BackButtonWithText(
                    title = uiState.title,
                    modifier = Modifier.weight(1f),
                    onBackClick = { navController.popBackStack() }
                )
                KidsLabel("Question ${uiState.currentIndex + 1} / ${uiState.questions.size}")
            }

            Box(modifier = Modifier.weight(1f).fillMaxSize()) {
                if (!uiState.showResult) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Dimens16),
                        verticalArrangement = Arrangement.spacedBy(Dimens16, Alignment.CenterVertically)
                    ) {
                        uiState.currentQuestion?.let { q ->
                            Text(
                                text = q.promptWithBlank,
                                modifier = Modifier.fillMaxWidth(),
                                style = MaterialTheme.typography.headlineLarge.scaled(),
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )

                            val blankAtStart = q.promptWithBlank.startsWith("___")
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(Dimens12, Alignment.CenterHorizontally),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                q.options.forEach { option ->
                                    KidsOptionButton(
                                        text = displayOption(option, blankAtStart),
                                        type = viewModel.backgroundType(option),
                                        fontSize = listenAndAnswerOptionsHeight.value.sp * 0.45,
                                        onClick = { viewModel.selectAnswer(option) },
                                        enabled = uiState.selectedAnswer == null,
                                        modifier = Modifier
                                            .width(140.dp)
                                            .height(listenAndAnswerOptionsHeight)
                                    )
                                }
                            }

                            if (uiState.selectedAnswer != null) {
                                val isCorrect = uiState.selectedAnswer == uiState.correctAnswer
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .clip(RoundedCornerShape(Dimens16))
                                        .background(Color.White.copy(alpha = 0.85f))
                                        .padding(Dimens16),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(Dimens12)
                                ) {
                                    Text(
                                        text = if (isCorrect) "🎉 Correct!" else "🤔 Not quite.",
                                        style = MaterialTheme.typography.titleMedium.scaled(),
                                        color = if (isCorrect) Color(0xFF2E7D32) else Color(0xFFD32F2F),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = styledRule(q.explanation, uiState.type),
                                        style = MaterialTheme.typography.bodyLarge.scaled(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            Row {
                                Spacer(Modifier.weight(1f))
                                KidsActionButton(
                                    text = stringResourceNext(),
                                    icon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                    type = if (uiState.selectedAnswer == null) ButtonType.DISABLE else ButtonType.ORANGE,
                                    isIconStart = false,
                                    onClick = { if (uiState.selectedAnswer != null) viewModel.next() }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (uiState.showResult) {
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
                scoreLabel = androidx.compose.ui.res.stringResource(R.string.correct_answers),
                feedbackTextRes = when {
                    accuracy >= 0.8 -> R.string.feedbackPhrases_1
                    accuracy >= 0.5 -> R.string.feedbackPhrases_2
                    else -> R.string.feedbackPhrases_3
                },
                onNext = { viewModel.restart() },
                nextLabel = androidx.compose.ui.res.stringResource(R.string.want_to_continue),
                dismissLabel = androidx.compose.ui.res.stringResource(R.string.go_back),
                onClose = { navController.popBackStack() }
            )
        }
    }
}

@Composable
private fun stringResourceNext(): String =
    androidx.compose.ui.res.stringResource(R.string.next)

// Options show lowercase; first letter capitalised only when the blank is the
// first word of the sentence (so casing never reveals the answer).
private fun displayOption(option: String, capitalizeFirst: Boolean): String {
    val lower = option.lowercase()
    return if (capitalizeFirst && lower.isNotEmpty())
        lower.replaceFirstChar { it.uppercase() } else lower
}

// Explanation with key words bold + coloured (each keyword its own colour).
private fun styledRule(text: String, type: GrammarDrillType): AnnotatedString {
    val keywords: Map<String, Color> = when (type) {
        GrammarDrillType.HAS_HAVE -> mapOf("HAS" to Color(0xFF2E7D32), "HAVE" to Color(0xFF2563EB))
        GrammarDrillType.SUBJECT_VERB_AGREEMENT -> mapOf("-S" to Color(0xFF2563EB))
    }
    return buildAnnotatedString {
        val tokens = text.split(" ")
        tokens.forEachIndexed { i, token ->
            val cleaned = token.trim('.', ',', '!', '?', ';', ':').uppercase()
            val color = keywords[cleaned]
            if (color != null) {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = color)) { append(token) }
            } else {
                withStyle(SpanStyle(color = Color.Black)) { append(token) }
            }
            if (i < tokens.size - 1) append(" ")
        }
    }
}
