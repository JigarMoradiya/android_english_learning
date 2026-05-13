package com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.common_ui.practice
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.core.text.toSpanned
import com.example.myapplication.main.common.FeedbackText
import com.example.myapplication.main.common.buttons.KidsOptionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.grammarBasicOptionsHeight
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled

@Composable
fun GrammarPracticeQuestionLayout(
    imageRes: Int,
    question: String,
    instructionText: String,
    options: List<String>,
    feedbackTitle: String?,
    feedbackSubTitle: String?,
    isAnswerCorrect: Boolean,
    isFeedbackVisible: Boolean,
    optionTypeProvider: (String) -> ButtonType,
    onOptionTap: (String) -> Unit,
    isOptionDisabled: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier.fillMaxSize()
    ) {

        LeftSection(
            imageRes = imageRes,
            question = question,
            modifier = Modifier.weight(1f)
        )

        RightSection(
            instructionText = instructionText,
            options = options,
            feedbackTitle = feedbackTitle,
            feedbackSubTitle = feedbackSubTitle,
            isAnswerCorrect = isAnswerCorrect,
            isFeedbackVisible = isFeedbackVisible,
            optionTypeProvider = optionTypeProvider,
            onOptionTap = onOptionTap,
            isOptionDisabled = isOptionDisabled,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun LeftSection(
    imageRes: Int,
    question: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxHeight(0.7f)
                .clip(RoundedCornerShape(Dimens12))
        )

        Spacer(modifier = Modifier.height(Dimens8))

        Text(
            text = question,
            style = MaterialTheme.typography.titleMedium.scaled(),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun RightSection(
    instructionText: String,
    options: List<String>,
    feedbackTitle: String?,
    feedbackSubTitle: String?,
    isAnswerCorrect: Boolean,
    isFeedbackVisible: Boolean,
    optionTypeProvider: (String) -> ButtonType,
    onOptionTap: (String) -> Unit,
    isOptionDisabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = AnnotatedString.fromHtml(instructionText),
            style = MaterialTheme.typography.bodyLarge.scaled(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            color = Color.Black.copy(alpha = 0.8f)
        )

        Spacer(modifier = Modifier.height(Dimens8))

        options.forEach { option ->

            KidsOptionButton(
                text = option.replaceFirstChar { it.uppercase() },
                type = optionTypeProvider(option),
                fontSize = grammarBasicOptionsHeight.value.sp * 0.45f,
                onClick = {
                    onOptionTap(option)
                },
                enabled = !isOptionDisabled,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(grammarBasicOptionsHeight)
            )

            Spacer(modifier = Modifier.height(Dimens8))
        }

        Spacer(modifier = Modifier.weight(1f))

        FeedbackText(
            title = feedbackTitle?:"",
            subtitle = feedbackSubTitle?:"",
            isSuccess = isAnswerCorrect,
            isVisible = isFeedbackVisible
        )
    }
}