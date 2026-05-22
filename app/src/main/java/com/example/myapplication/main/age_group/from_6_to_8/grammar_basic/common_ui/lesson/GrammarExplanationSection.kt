package com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.common_ui.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.utils.extensions.scaled

@Composable
fun GrammarExplanationSection(
    explanationText: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimens16))
            .background(Color(0xFFFFFDE7))
            .padding(Dimens16)
    ) {
        Text(
            text = AnnotatedString.fromHtml(explanationText),
            color = Color(0xFF4A3728),
            style = MaterialTheme.typography.bodyLarge.scaled(),
            textAlign = TextAlign.Start
        )
    }
}