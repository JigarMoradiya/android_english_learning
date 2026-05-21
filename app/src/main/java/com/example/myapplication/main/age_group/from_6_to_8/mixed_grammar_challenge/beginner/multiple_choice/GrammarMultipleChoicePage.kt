package com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.beginner.multiple_choice

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
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
import com.example.myapplication.main.age_group.from_6_to_8.common.ResultView
import com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.beginner.multiple_choice.view_model.GrammarMultipleChoiceViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.FeedbackText
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.buttons.KidsLabel
import com.example.myapplication.main.common.buttons.KidsOptionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled
import androidx.compose.ui.unit.sp
import com.example.myapplication.main.common.getImageResForSentence
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.grammarBasicBeginnerMultipleChoiceOptionsHeight
import com.example.myapplication.ui.theme.AppDimens.grammarBasicBeginnerMultipleChoiceOptionsWidth
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground

@Composable
fun GrammarMultipleChoicePage(
    navController: NavController,
    viewModel: GrammarMultipleChoiceViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.skyLavender, shape = KidsFloatingShape.diamonds)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {

            // ── Header ────────────────────────────────────────────────────────
            Row(verticalAlignment = Alignment.CenterVertically) {
                BackButtonWithText(
                    title = "Multiple Choice",
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
                    title = stringResource(R.string.your_result),
                    primaryButtonText = stringResource(R.string.want_to_continue),
                    secondaryButtonText = stringResource(R.string.go_back),
                    onSecondaryTap = { navController.popBackStack() },
                    onPrimaryTap = { viewModel.restart() },
                    modifier = Modifier.padding(horizontal = Dimens16)
                )
                Spacer(Modifier.weight(1f))
            } else {
                uiState.currentQuestion?.let { q ->

                    val typeColor = viewModel.typeColor(q.targetType)
                    val typeLabel = viewModel.typeLabel(q.targetType)
                    val typeIcon  = viewModel.typeIcon(q.targetType)

                    // HStack: Left 50% image+sentence | Right 50% badge+options+feedback
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {

                        // ── Left section: Image + Sentence (50%) ─────────────
                        Column(
                            modifier = Modifier
                                .weight(0.5f)
                                .fillMaxHeight()
                                .padding(horizontal = Dimens16),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Spacer(Modifier.weight(1f))

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

                            Spacer(Modifier.height(Dimens10))

                            Text(
                                text = q.sentence,
                                style = MaterialTheme.typography.titleMedium.scaled(),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = Dimens16)
                            )

                            Spacer(Modifier.weight(1f))
                        }

                        // ── Right section: Badge + Options + Feedback (50%) ───
                        Column(
                            modifier = Modifier
                                .weight(0.5f)
                                .fillMaxHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(Dimens16)
                        ) {
                            Spacer(Modifier.weight(1f))

                            // Type badge: "Which word is the [VERB]?"
                            MultipleChoiceTypeBadge(
                                icon = typeIcon,
                                typeLabel = typeLabel,
                                typeColor = typeColor
                            )

                            // 3 option buttons stacked vertically
                            Column(
                                verticalArrangement = Arrangement.spacedBy(Dimens10),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                q.options.forEach { option ->
                                    KidsOptionButton(
                                        text = option,
                                        type = viewModel.optionType(option),
                                        fontSize = (grammarBasicBeginnerMultipleChoiceOptionsHeight.value * 0.45f).sp,
                                        enabled = !uiState.showNext,
                                        onClick = { viewModel.selectAnswer(option) },
                                        modifier = Modifier
                                            .width(grammarBasicBeginnerMultipleChoiceOptionsWidth)
                                            .height(grammarBasicBeginnerMultipleChoiceOptionsHeight)
                                    )
                                }
                            }

                            Spacer(Modifier.weight(1f))

                            // Feedback overlay pinned to bottom of right section
                            val subtitle = if (uiState.showNext){
                                if (uiState.isAnswerCorrect){
                                    uiState.feedbackSubTitle?.let { stringResource(it) }
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
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Type Badge ────────────────────────────────────────────────────────────────
// "Which word is the [VERB]?"

@Composable
private fun MultipleChoiceTypeBadge(
    icon: ImageVector,
    typeLabel: String,
    typeColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .background(typeColor.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
            .border(1.5.dp, typeColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = typeColor,
            modifier = Modifier.size(18.dp)
        )

        Text(
            text = "Which word is the",
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
            text = "?",
            style = MaterialTheme.typography.bodyMedium.scaled(),
            color = Color.Black.copy(alpha = 0.7f)
        )
    }
}
