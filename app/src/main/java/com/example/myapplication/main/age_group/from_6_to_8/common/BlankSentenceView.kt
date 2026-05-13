package com.example.myapplication.main.age_group.from_6_to_8.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.PrimaryGreen
import com.example.myapplication.utils.extensions.appScale
import kotlin.math.max

@Composable
fun BlankSentenceView(
    sentence: String,
    correctAnswer: String,
    selectedAnswer: String?,
    isAnswerCorrect: Boolean?,
    options: List<String>,
    modifier: Modifier = Modifier
) {

    val lines = remember(sentence) {
        sentence.split("\n")
            .filter { it.trim().isNotEmpty() }
    }

    val fontSize = 20.sp * appScale()

    val longestOption = remember(
        selectedAnswer,
        options,
        correctAnswer
    ) {
        val allOptions =
            if (selectedAnswer != null) {
                options + correctAnswer
            } else {
                options
            }

        allOptions.maxByOrNull { it.length }
            ?: correctAnswer
    }

    val blankWidth = remember(longestOption) {
        max(
            longestOption.length * 14,
            80
        )
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens4)
    ) {

        lines.forEach { line ->

            val parts = line.split("___")

            Row(
                verticalAlignment = Alignment.Bottom
            ) {

                parts.forEachIndexed { index, part ->

                    Text(
                        text = part,
                        fontSize = fontSize,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )

                    if (index < parts.lastIndex) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = selectedAnswer ?: " ",
                                fontSize = fontSize,
                                fontWeight = FontWeight.SemiBold,
                                color = getAnswerColor(
                                    selectedAnswer,
                                    isAnswerCorrect
                                ),
                                modifier = Modifier.width(
                                    blankWidth.dp
                                ),
                                textAlign = TextAlign.Center
                            )

                            Box(
                                modifier = Modifier
                                    .width(blankWidth.dp)
                                    .height(Dimens2)
                                    .background(
                                        Color.Black.copy(alpha = 0.8f)
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun getAnswerColor(
    selectedAnswer: String?,
    isAnswerCorrect: Boolean?
): Color {

    if (selectedAnswer == null || isAnswerCorrect == null) {
        return Color.Transparent
    }

    return if (isAnswerCorrect) {
        PrimaryGreen
    } else {
        Color.Red
    }
}