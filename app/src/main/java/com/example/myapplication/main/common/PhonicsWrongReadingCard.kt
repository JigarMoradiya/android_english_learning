package com.example.myapplication.main.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.utils.extensions.scaled
import androidx.compose.ui.draw.clip

/** One ❌→✅ row for the "Not like this!" card. */
data class WrongReadingExample(
    val wrong: String,     // how kids wrongly read it, e.g. "k·nife"
    val right: String,     // the correct reading + why, e.g. "/nīf/ — the K is hiding!"
    val playWord: String   // audio file played on tap, e.g. "knife"
)

/**
 * Shared "🙅 Not like this!" card for learn pages — shows the common WRONG reading
 * crossed out in red next to the correct one in green. Tapping a row plays the word.
 */
@Composable
fun PhonicsWrongReadingCard(
    accentColor: Color,
    examples: List<WrongReadingExample>,
    modifier: Modifier = Modifier
) {
    val audioVm: PhonicsIntroAudioViewModel = hiltViewModel()

    Column(
        verticalArrangement = Arrangement.spacedBy(Dimens8),
        modifier = modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = Dimens12, strokeColor = Color(0xFFC62828))
            .padding(Dimens12)
    ) {
        Text(
            text = "🙅 Not like this!",
            style = MaterialTheme.typography.titleSmall.scaled(),
            fontWeight = FontWeight.Bold,
            color = Color(0xFFC62828)
        )

        examples.forEach { example ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens8),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.55f), RoundedCornerShape(Dimens8))
                    .clip(RoundedCornerShape(Dimens8))
                    .clickable { audioVm.play(example.playWord) }
                    .padding(horizontal = Dimens8, vertical = Dimens6)
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("❌ ")
                        withStyle(SpanStyle(color = Color(0xFFC62828), textDecoration = TextDecoration.LineThrough)) {
                            append(example.wrong)
                        }
                    },
                    style = MaterialTheme.typography.labelMedium.scaled()
                )
                Text(
                    text = buildAnnotatedString {
                        append("✅ ")
                        withStyle(SpanStyle(color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)) {
                            append(example.right)
                        }
                    },
                    style = MaterialTheme.typography.labelMedium.scaled(),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "🔊",
                    style = MaterialTheme.typography.labelMedium.scaled()
                )
            }
        }
    }
}
