package com.example.myapplication.main.age_group.phonics.worksheets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import com.example.myapplication.data.generation.letter.ImageWordMapper
import kotlin.math.ceil

// ── Reading · the alphabet with a picture each ──────────────────────────────────

/** Every letter as an Aa pair with its main word and picture. Mirrors iOS
 *  `WorksheetLetterReadBody`. */
@Composable
fun WorksheetLetterReadBody(
    theme: WorksheetTheme,
    letters: List<String>,
    columns: Int,
    showsSound: Boolean
) {
    val rows = letters.balancedChunked(columns)
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Spacer(Modifier.weight(0.0001f))
                row.forEach { l ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(12.dp))
                            .background(theme.accent.copy(alpha = 0.05f))
                            .border(1.2.dp, theme.accent.copy(alpha = 0.22f), RoundedCornerShape(12.dp))
                            .padding(vertical = 6.dp, horizontal = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = "${l.uppercase()}${l.lowercase()}",
                            style = wsFont(30f, FontWeight.Black),
                            color = theme.accent
                        )
                        val pic = if (showsSound)
                            WorksheetLetters.soundMatchedPicture(l)
                        else
                            WorksheetLetters.mainPicture(l)
                        if (pic != null) {
                            Image(
                                painter = painterResource(id = pic.imageRes),
                                contentDescription = pic.word,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                            )
                            Text(
                                text = pic.word,
                                style = wsFont(13f, FontWeight.Bold),
                                color = Color(0xFF5A6067),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        if (showsSound) {
                            Text(
                                text = "“${WorksheetLetters.sound(l)}”",
                                style = wsFont(13f, FontWeight.Black),
                                color = theme.accent,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                repeat(columns - row.size) { Spacer(Modifier.weight(1f)) }
                Spacer(Modifier.weight(0.0001f))
            }
        }
    }
}

// ── Activity · circle the letter the picture starts with ────────────────────────

@Composable
fun WorksheetCircleLetterBody(theme: WorksheetTheme, items: List<CircleItem>) {
    val rows = items.balancedChunked(2)
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                row.forEach { item ->
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(14.dp))
                            .background(theme.accent.copy(alpha = 0.04f))
                            .border(1.2.dp, theme.accent.copy(alpha = 0.18f), RoundedCornerShape(14.dp))
                            .padding(vertical = 10.dp, horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        ImageWordMapper.get(item.word.lowercase())?.let { res ->
                            Image(
                                painter = painterResource(id = res),
                                contentDescription = item.word,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.height(76.dp)
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            item.choices.forEach { c ->
                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .dashedCircle(Color(0xFFB9BEC4)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = c, style = wsFont(30f, FontWeight.Black), color = Color.Black)
                                }
                            }
                        }
                    }
                }
                repeat(2 - row.size) { Spacer(Modifier.weight(1f)) }
            }
        }
    }
}

// ── Activity · join the letter to its picture ───────────────────────────────────

@Composable
fun WorksheetMatchLetterImageBody(theme: WorksheetTheme, items: List<MatchImageItem>) {
    val half = ceil(items.size / 2.0).toInt()
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(34.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(Modifier.weight(1f)) { MatchImageBlock(theme, items.take(half), seed = 7UL) }
        Box(Modifier.width(1.5.dp).fillMaxHeight().background(theme.accent.copy(alpha = 0.2f)))
        Box(Modifier.weight(1f)) { MatchImageBlock(theme, items.drop(half), seed = 11UL) }
    }
}

@Composable
private fun MatchImageBlock(theme: WorksheetTheme, group: List<MatchImageItem>, seed: ULong) {
    val rng = SeededGenerator(seed)
    val ordered = group.shuffledSeeded(rng)
    val keys = ordered.map { it.letter }
    val pictures = deranged(keys, rng).mapNotNull { k -> ordered.firstOrNull { it.letter == k } }

    Column(modifier = Modifier.fillMaxSize()) {
        ordered.forEachIndexed { i, item ->
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "${item.letter.uppercase()}${item.letter.lowercase()}",
                    style = wsFont(34f, FontWeight.Black),
                    color = Color.Black,
                    modifier = Modifier.width(66.dp)
                )
                JoinDot(theme.accent)
                Spacer(Modifier.weight(1f))
                JoinDot(theme.accent)
                pictures.getOrNull(i)?.let { p ->
                    Image(
                        painter = painterResource(id = p.imageRes),
                        contentDescription = p.letter,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(62.dp)
                    )
                }
            }
        }
    }
}

// ── Activity · Letter Matcher (uppercase ↔ lowercase) ───────────────────────────

@Composable
fun WorksheetLetterMatcherBody(theme: WorksheetTheme, pairs: List<String>) {
    val half = ceil(pairs.size / 2.0).toInt()
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(34.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(Modifier.weight(1f)) { MatcherBlock(theme, pairs.take(half), seed = 1UL) }
        Box(Modifier.width(1.5.dp).fillMaxHeight().background(theme.accent.copy(alpha = 0.2f)))
        Box(Modifier.weight(1f)) { MatcherBlock(theme, pairs.drop(half), seed = 5UL) }
    }
}

@Composable
private fun MatcherBlock(theme: WorksheetTheme, group: List<String>, seed: ULong) {
    val rng = SeededGenerator(seed)
    val left = group.shuffledSeeded(rng)
    val right = deranged(left, rng)

    Column(modifier = Modifier.fillMaxSize()) {
        left.forEachIndexed { i, l ->
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = l.uppercase(),
                    style = wsFont(36f, FontWeight.Black),
                    color = Color.Black,
                    modifier = Modifier.width(46.dp)
                )
                JoinDot(theme.accent)
                Spacer(Modifier.weight(1f))
                JoinDot(theme.accent)
                Text(
                    text = right.getOrNull(i).orEmpty(),
                    style = wsFont(36f, FontWeight.Black),
                    color = Color.Black,
                    textAlign = TextAlign.End,
                    modifier = Modifier.width(46.dp)
                )
            }
        }
    }
}

// ── Shared bits ─────────────────────────────────────────────────────────────────

@Composable
private fun JoinDot(color: Color) {
    Box(
        modifier = Modifier
            .size(13.dp)
            .drawBehind {
                drawCircle(color = color, style = Stroke(width = 2.dp.toPx()))
            }
    )
}

/** A light dashed ring — a guide showing where to draw, not an answer already circled. */
private fun Modifier.dashedCircle(color: Color, stroke: Dp = 2.dp): Modifier =
    this.drawBehind {
        drawCircle(
            color = color,
            style = Stroke(
                width = stroke.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 5f))
            )
        )
    }
