package com.example.myapplication.main.age_group.from_6_to_8.choose_the_right_sentence.find_the_correct_writing

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.example.myapplication.data.generation.loader.SentenceBuilderLogic
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.SentenceUnit
import com.example.myapplication.main.age_group.from_6_to_8.choose_the_right_sentence.find_the_correct_writing.view_model.FindTheCorrectWritingViewModel
import com.example.myapplication.main.common.ActivityCompletePopup
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.buttons.KidsLabel
import com.example.myapplication.main.common.buttons.KidsOptionButton
import com.example.myapplication.main.common.getImageResForSentence
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.isTablet
import com.example.myapplication.ui.theme.AppDimens.listenAndAnswerOptionsHeight
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground


@Composable
fun FindTheCorrectWritingPage(
    unit : SentenceUnit,
    level : SentenceLevel,
    navController: NavController,
    viewModel: FindTheCorrectWritingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.setData(unit,level)
    }
    Box(modifier = Modifier.fillMaxSize()) {

        KidsGradientBackground(gradient = KidsGradient.blueIndigo, shape = KidsFloatingShape.speechBubbles)
        Column(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)) {
            // HEADER
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                BackButtonWithText(
                    title = stringResource(R.string.find_the_correct_writing),
                    modifier = Modifier.weight(1f),
                    onBackClick = { navController.popBackStack() }
                )

                KidsLabel("Question ${uiState.currentIndex + 1} / ${uiState.questions.size}",)
            }


            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens16),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = Dimens16)
                        .padding(bottom = Dimens16)
                ) {

                    // CENTER IMAGE
                    getImageResForSentence(uiState.currentQuestion?.imageName)?.let { resId ->
                        val modifier : Modifier = if (isTablet){
                            val screenHeight = with(LocalDensity.current) {
                                LocalWindowInfo.current.containerSize.height.toDp()
                            }
                            Modifier.size(screenHeight * 0.5f)
                        } else{
                            Modifier.aspectRatio(1f)
                        }
                        Image(
                            painter = painterResource(resId),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = modifier
                                .clip(RoundedCornerShape(Dimens16))
                        )
                    }

                    if (!uiState.showResult) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(Dimens12)
                        ) {
                            uiState.options.forEach { word ->

                                KidsOptionButton(
                                    text = word.replaceFirstChar { it.uppercase() },
                                    type = viewModel.backgroundType(word),
                                    fontSize = listenAndAnswerOptionsHeight.value.sp * 0.37,
                                    onClick = {
                                        viewModel.selectAnswer(word)
                                    },
                                    enabled = uiState.selectedAnswer == null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(listenAndAnswerOptionsHeight),
                                    textAlign = TextAlign.Left
                                )
                            }

                            // Explanation shown after answering — names the exact word fix (item 4.2)
                            if (uiState.selectedAnswer != null) {
                                val correction = uiState.correction
                                val styled = if (correction != null) {
                                    buildAnnotatedString {
                                        withStyle(SpanStyle(color = Color.Black)) { append("It is ") }
                                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))) { append(correction.first) }
                                        withStyle(SpanStyle(color = Color.Black)) { append(", not ") }
                                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFFD32F2F))) { append(correction.second) }
                                        withStyle(SpanStyle(color = Color.Black)) { append(".") }
                                    }
                                } else {
                                    val prefix = SentenceBuilderLogic.CORRECT_SENTENCE_PREFIX
                                    val ex = uiState.explanation ?: ""
                                    if (ex.startsWith(prefix)) buildAnnotatedString {
                                        withStyle(SpanStyle(color = Color.Black)) { append(prefix) }
                                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFF3265D3))) { append(ex.removePrefix(prefix)) }
                                    } else buildAnnotatedString { withStyle(SpanStyle(color = Color.Black)) { append(ex) } }
                                }
                                val isCorrect = uiState.selectedAnswer == uiState.correctAnswer
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(Dimens16))
                                        .background(Color.White.copy(alpha = 0.85f))
                                        .padding(Dimens16),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(Dimens12)
                                ) {
                                    Text(
                                        text = if (isCorrect) "🎉 Correct!" else "🤔 Not quite.",
                                        color = if (isCorrect) Color(0xFF2E7D32) else Color(0xFFD32F2F),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                    Text(
                                        text = styled,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            // Next Button Row
                            Row {
                                Spacer(Modifier.weight(1f))

                                KidsActionButton(
                                    text = stringResource(R.string.next),
                                    icon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                    type = if (uiState.selectedAnswer == null)
                                        ButtonType.DISABLE else ButtonType.ORANGE,
                                    isIconStart = false,
                                    onClick = {
                                        if (uiState.selectedAnswer != null) {
                                            viewModel.next()
                                        }
                                    }
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
