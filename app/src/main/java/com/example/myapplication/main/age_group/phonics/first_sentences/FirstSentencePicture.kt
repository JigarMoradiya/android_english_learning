package com.example.myapplication.main.age_group.phonics.first_sentences

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.example.myapplication.main.age_group.phonics.first_sentences.view_model.FirstSentence

/**
 * The picture that proves the line was understood.
 *
 * The app does not ship artwork for every one of these words yet, so a missing drawable
 * falls back to the sentence's emoji — the same fallback iOS uses via `hasImage`. The
 * glyph is sized FROM the slot so it can never draw past it.
 */
@Composable
fun FirstSentencePicture(
    sentence: FirstSentence,
    height: Dp,
    emojiSize: TextUnit,
    modifier: Modifier = Modifier,
) {
    val ctx = LocalContext.current
    val imgId = remember(sentence.image) {
        ctx.resources.getIdentifier(sentence.image, "drawable", ctx.packageName)
    }

    // A SQUARE slot, width pinned as well as height. `Modifier.height()` alone leaves the
    // width free, and inside a Row an unweighted Image then claims the whole remaining
    // width — which starved the sentence column beside it and left a wide blank slab.
    Box(contentAlignment = Alignment.Center, modifier = modifier.size(height)) {
        if (imgId != 0) {
            Image(
                painter = painterResource(id = imgId),
                contentDescription = sentence.text,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Text(text = sentence.emoji, style = TextStyle(fontSize = emojiSize))
        }
    }
}
