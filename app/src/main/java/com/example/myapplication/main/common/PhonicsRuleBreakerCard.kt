package com.example.myapplication.main.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.utils.extensions.scaled
import androidx.compose.ui.draw.clip

/** One rebel word for the "Rule Breakers!" card. */
data class RuleBreakerEntry(
    val word: String,   // the rebel word (also the audio file), e.g. "said"
    val why: String     // why it breaks the rule, e.g. "ai says /e/ here!"
)

/**
 * Shared "😈 Rule Breakers!" card — words that refuse to follow the level's rule.
 * Teaching exceptions honestly stops confusion when the rule fails. Tap plays the word.
 */
@Composable
fun PhonicsRuleBreakerCard(
    entries: List<RuleBreakerEntry>,
    modifier: Modifier = Modifier
) {
    val audioVm: PhonicsIntroAudioViewModel = hiltViewModel()
    val purple = Color(0xFF6A1B9A)

    Column(
        verticalArrangement = Arrangement.spacedBy(Dimens8),
        modifier = modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = Dimens12, strokeColor = purple)
            .padding(Dimens12)
    ) {
        Text(
            text = "😈 Rule Breakers!",
            style = MaterialTheme.typography.titleSmall.scaled(),
            fontWeight = FontWeight.Bold,
            color = purple
        )

        entries.forEach { entry ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens8),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(purple.copy(alpha = 0.08f), RoundedCornerShape(Dimens8))
                    .clip(RoundedCornerShape(Dimens8))
                    .clickable { audioVm.play(entry.word) }
                    .padding(horizontal = Dimens8, vertical = Dimens6)
            ) {
                Text(
                    text = entry.word,
                    style = MaterialTheme.typography.labelMedium.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .background(purple, CircleShape)
                        .padding(horizontal = Dimens8, vertical = Dimens4)
                )
                Text(
                    text = entry.why,
                    style = MaterialTheme.typography.labelMedium.scaled(),
                    color = Color(0xFF37474F),
                    modifier = Modifier.weight(1f)
                )
                Text(text = "🔊", style = MaterialTheme.typography.labelMedium.scaled())
            }
        }
    }
}
