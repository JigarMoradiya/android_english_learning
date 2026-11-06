package com.example.myapplication.main.age_group.phonics.worksheets

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text

/**
 * Level 1 "Learn" sheet — the letter, the sound it says, and up to six picture words.
 * A child here knows single letter sounds and cannot blend, so words appear as
 * PICTURES with their spelling. Mirrors iOS `WorksheetLetterPageBody`.
 */
@Composable
fun WorksheetLetterPageBody(
    theme: WorksheetTheme,
    letter: String,
    tappable: Boolean,
    onWordTap: (word: String, letter: String) -> Unit
) {
    val words = WorksheetLetters.pictureWords(letter, limit = 6)
    // Four words make a 2×2 square with bigger cards; everything else is 3 across.
    val cols = if (words.size == 4) 2 else 3
    val rows = words.chunked(cols)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // "Aa says aaa", centred.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${letter.uppercase()}${letter.lowercase()}",
                style = wsFont(80f, FontWeight.Black),
                color = theme.accent
            )
            Spacer(Modifier.width(12.dp))
            Text(text = "says", style = wsFont(20f, FontWeight.Bold), color = Color(0xFF8A9097))
            Spacer(Modifier.width(12.dp))
            Text(
                text = WorksheetLetters.sound(letter),
                style = wsFont(46f, FontWeight.Black),
                color = theme.accent
            )
        }

        // The picture grid fills the rest of the page.
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            rows.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Spacers either side, so a short last row sits centred.
                    Spacer(Modifier.weight(0.0001f))
                    row.forEach { item ->
                        Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                            WorksheetPictureWord(
                                word = item.word,
                                imageRes = item.imageRes,
                                accent = theme.accent,
                                tappable = tappable,
                                onClick = { onWordTap(item.word, letter) }
                            )
                        }
                    }
                    // Pad a short last row so its cards keep the full-row width.
                    repeat(cols - row.size) { Spacer(Modifier.weight(1f)) }
                    Spacer(Modifier.weight(0.0001f))
                }
            }
        }
    }
}

/** A picture over its word, first letter tinted so the eye lands on the taught sound. */
@Composable
private fun WorksheetPictureWord(
    word: String,
    imageRes: Int,
    accent: Color,
    tappable: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(14.dp))
            .background(accent.copy(alpha = 0.05f))
            .border(1.3.dp, accent.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
            .then(if (tappable) Modifier.clickable { onClick() } else Modifier)
            .padding(vertical = 10.dp, horizontal = 6.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = word,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 18.dp, vertical = 10.dp)
        )
        Text(
            text = buildAnnotatedString {
                withStyle(androidx.compose.ui.text.SpanStyle(color = accent)) {
                    append(word.take(1))
                }
                withStyle(androidx.compose.ui.text.SpanStyle(color = Color(0xFF44474B))) {
                    append(word.drop(1))
                }
            },
            style = wsFont(34f, FontWeight.Black),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}
