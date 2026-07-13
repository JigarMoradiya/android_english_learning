package com.example.myapplication.main.common

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.example.myapplication.ui.theme.AppDimens.Dimens40
import com.example.myapplication.utils.extensions.scaled

@Composable
fun ColoredFeedbackView(
    feedbackText: String?,
    isAnswerCorrect: Boolean,
    correctAnswer: String
) {
    val warmColors = listOf(
        Color(0xFFFF9800), // orange
        Color(0xFFFF5722),
        Color(0xFFE91E63),
        Color(0xFF9C27B0)
    )

    AnimatedContent(
        targetState = feedbackText,
        transitionSpec = {
            (fadeIn() + scaleIn(initialScale = 0.95f)) togetherWith
                    (fadeOut() + scaleOut(targetScale = 0.95f))
        },
        label = "feedbackAnimation"
    ){ feedback ->

        if (feedback.isNullOrEmpty()) {
            Text(
                text = " ",
                style = MaterialTheme.typography.titleSmall.scaled(),
                fontWeight = FontWeight.SemiBold,
                color = Color.Transparent
            )
        } else {
            Text(
                text = buildAnnotatedString {
                    if (isAnswerCorrect) {
                        withStyle(
                            SpanStyle(
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(feedback)
                        }
                    } else {
                        val regex = Regex(
                            "\\b${Regex.escape(correctAnswer)}\\b",
                            RegexOption.IGNORE_CASE
                        )

                        // The answer word can also appear mid-sentence — highlight
                        // the last occurrence (the one inside the spoken answer)
                        val match = regex.findAll(feedback).lastOrNull()
                        if (match != null) {

                            val beforeText = feedback.substring(0, match.range.first)
                            val afterText = feedback.substring(match.range.last + 1)

                            appendRainbowText(beforeText, warmColors)

                            withStyle(
                                SpanStyle(
                                    color = Color(0xFF2E7D32),
                                    fontWeight = FontWeight.ExtraBold,
                                    textDecoration = TextDecoration.Underline
                                )
                            ) {
                                append(correctAnswer)
                            }
                            if (afterText.startsWith(" ")) append(" ")

                            appendRainbowText(
                                afterText,
                                warmColors
                            )

                        } else {
                            appendRainbowText(
                                feedback,
                                warmColors
                            )
                        }
                    }
                },
                style = MaterialTheme.typography.titleSmall.scaled(),
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = Dimens40)
            )
        }
    }
}

private fun AnnotatedString.Builder.appendRainbowText(
    text: String,
    colors: List<Color>
) {
    val words = text.split(" ")
        .filter { it.isNotBlank() }

    words.forEachIndexed { index, word ->
        withStyle(
            SpanStyle(
                color = colors[index % colors.size]
            )
        ) {
            append(word)
            append(" ")   // always keep spacing
        }
    }
}