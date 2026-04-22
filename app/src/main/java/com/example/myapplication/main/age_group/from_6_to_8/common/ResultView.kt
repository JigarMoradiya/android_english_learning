package com.example.myapplication.main.age_group.from_6_to_8.common

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.myapplication.R
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.ui.theme.PrimaryBlue
import com.example.myapplication.ui.theme.PrimaryGreen
import com.example.myapplication.ui.theme.PrimaryOrange
import com.example.myapplication.utils.extensions.scaled

@Composable
fun ResultView(
    score: Int,
    total: Int,
    onBack: () -> Unit
) {
    val percentage = score.toFloat() / maxOf(total, 1)

    val (title, emoji, color) = when {
        percentage >= 0.9f -> Triple("feedbackLetterImageMatchPhrases_8", "🌟", PrimaryGreen)
        percentage >= 0.7f -> Triple("great_job", "🎉", PrimaryOrange) // Orange
        percentage >= 0.5f -> Triple("good_try", "👍", PrimaryBlue)
        else -> Triple("keepPracticing", "💪", Color.Red)
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Dimens16),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.weight(1f))
        // 🎉 Title
        Text(
            text = stringResource(R.string.lesson_completed),
            style = MaterialTheme.typography.headlineLarge.scaled(),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        // ⭐ Score Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Dimens20))
                .background(color.copy(alpha = 0.2f))
                .border(
                    width = Dimens2,
                    color = color,
                    shape = RoundedCornerShape(Dimens20)
                )
                .padding(Dimens16),
            verticalArrangement = Arrangement.spacedBy(Dimens8),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🎉 Dynamic Title
            Text(
                text = "$emoji ${stringResource(getStringRes(title))}",
                style = MaterialTheme.typography.displaySmall.scaled(),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "$score / $total",
                style = MaterialTheme.typography.displayMedium.scaled(),
                fontWeight = FontWeight.Bold
            )

            Text(
                text = stringResource(R.string.correct_answers),
                style = MaterialTheme.typography.titleSmall.scaled(),
                color = Color.DarkGray
            )
        }

        // 👉 Button
        KidsActionButton(
            text = stringResource(R.string.go_back_to_lesson),
            type = ButtonType.ORANGE,
            onClick = onBack
        )

        Spacer(Modifier.weight(1f))
    }
}

@StringRes
fun getStringRes(key: String): Int {
    return when (key) {
        "feedbackLetterImageMatchPhrases_8" -> R.string.feedbackGiveAnswerTitleCorrect_8
        "great_job" -> R.string.great_job
        "good_try" -> R.string.good_try
        "keepPracticing" -> R.string.keepPracticing
        else -> R.string.app_name // fallback
    }
}