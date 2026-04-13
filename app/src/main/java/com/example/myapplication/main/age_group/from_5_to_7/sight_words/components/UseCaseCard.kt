package com.example.myapplication.main.age_group.from_5_to_7.sight_words.components
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.PrimaryBlue

@Composable
fun UseCaseCard(
    description: String,
    example: String,
    currentWord: String,
    onSpeak: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Yellow.copy(alpha = 0.2f))
            .padding(Dimens12),
        verticalArrangement = Arrangement.spacedBy(Dimens6)
    ) {

        Text(
            text = "• $description",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.clickable {
                onSpeak(example.lowercase().replace(".", ","))
            }
        ) {

            Icon(
                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = null,
                modifier = Modifier.size(Dimens20)
            )

            Text(
                text = highlightWord(example, currentWord),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black.copy(alpha = 0.6f)
            )
        }
    }

}
fun highlightWord(text: String, word: String): AnnotatedString {

    val lowerText = text.lowercase()
    val lowerWord = word.lowercase()

    return buildAnnotatedString {

        var startIndex = 0

        while (true) {
            val index = lowerText.indexOf(lowerWord, startIndex)
            if (index == -1) {
                append(text.substring(startIndex))
                break
            }

            append(text.substring(startIndex, index))

            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                )
            ) {
                append(text.substring(index, index + word.length))
            }

            startIndex = index + word.length
        }
    }
}
