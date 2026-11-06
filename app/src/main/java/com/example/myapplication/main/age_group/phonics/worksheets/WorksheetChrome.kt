package com.example.myapplication.main.age_group.phonics.worksheets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import com.example.myapplication.ui.theme.customFonts

/** The app's own face at a given weight + size. The whole page is scaled as one
 *  piece, so iOS point sizes map straight to sp. */
fun wsFont(size: Float, weight: FontWeight) =
    TextStyle(fontFamily = customFonts, fontWeight = weight, fontSize = size.sp)

/**
 * The fixed A4 canvas: decorated frame, white card, header, body, footer, watermark.
 * Everything is laid out at absolute sizes against [WorksheetMetrics]; [ScaledWorksheet]
 * scales the finished page to whatever space it is given. Mirrors iOS `WorksheetPage`.
 */
@Composable
fun WorksheetPage(
    spec: WorksheetSpec,
    theme: WorksheetTheme,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.size(
            WorksheetMetrics.PAGE_WIDTH.dp,
            WorksheetMetrics.PAGE_HEIGHT.dp
        )
    ) {
        // Decorative tinted border in the level's colour.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(40.dp))
                .background(theme.accent.copy(alpha = 0.16f))
        )
        // The work on a white card inset from the border.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(WorksheetMetrics.BORDER_WIDTH.dp)
                .clip(RoundedCornerShape(34.dp))
                .background(Color.White)
        )
        // Header nailed to the top, footer to the bottom, body gets what is left.
        Column(modifier = Modifier.fillMaxSize().padding(46.dp)) {
            WorksheetHeader(spec = spec, theme = theme)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 24.dp),
                contentAlignment = Alignment.TopCenter
            ) { content() }
            WorksheetFooter(theme = theme)
        }
        // The watermark sits faintly OVER the work. Not interactive.
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Kids English Learning by Vedaavi",
                style = wsFont(40f, FontWeight.Black),
                color = Color(0xFF9CA3AF).copy(alpha = 0.15f),
                maxLines = 1,
                modifier = Modifier.rotate(-45f)
            )
        }
    }
}

/** Draws a worksheet at its design size, then scales the whole page to fit. */
@Composable
fun ScaledWorksheet(
    spec: WorksheetSpec,
    modifier: Modifier = Modifier,
    tappable: Boolean = true,
    onWordTap: (word: String, letter: String) -> Unit = { _, _ -> }
) {
    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.Center) {
        val density = LocalDensity.current
        val pageWpx = with(density) { WorksheetMetrics.PAGE_WIDTH.dp.toPx() }
        val pageHpx = with(density) { WorksheetMetrics.PAGE_HEIGHT.dp.toPx() }
        val maxWpx = constraints.maxWidth.toFloat()
        val maxHpx = constraints.maxHeight.toFloat()
        val scale = minOf(maxWpx / pageWpx, maxHpx / pageHpx)

        // The page is drawn at its fixed design size, centred, and scaled as one
        // piece around its centre so it always fits the space it is given.
        Box(
            modifier = Modifier
                .requiredSize(WorksheetMetrics.PAGE_WIDTH.dp, WorksheetMetrics.PAGE_HEIGHT.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
        ) {
            WorksheetSheet(spec = spec, tappable = tappable, onWordTap = onWordTap)
        }
    }
}

/** The top of every sheet — only title, subtitle and level number change. */
@Composable
private fun WorksheetHeader(spec: WorksheetSpec, theme: WorksheetTheme) {
    Column(modifier = Modifier.fillMaxWidth()) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(58.dp), contentAlignment = Alignment.Center) {
                Text(text = theme.mascot, style = wsFont(40f, FontWeight.Normal))
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "KIDS ENGLISH LEARNING",
                    style = wsFont(9f, FontWeight.Black),
                    color = Color(0xFF9AA0A6)
                )
                Text(
                    text = spec.title,
                    style = wsFont(26f, FontWeight.Black),
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = spec.subtitle,
                    style = wsFont(12f, FontWeight.Medium),
                    color = Color(0xFF6B7280),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(theme.accent.copy(alpha = 0.08f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = if (spec.level.levelNumber == 0) "MILESTONE" else "LEVEL ${spec.level.levelNumber}",
                    style = wsFont(10f, FontWeight.Black),
                    color = theme.accent
                )
            }
        }

        if (spec.showsNameDate) {
            Row(modifier = Modifier.fillMaxWidth().padding(top = 14.dp)) {
                WriteOnLine("NAME", 250.dp)
                Spacer(Modifier.weight(1f))
                WriteOnLine("DATE", 160.dp)
            }
        }

        // Rainbow separator.
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            WorksheetMetrics.RAINBOW.forEach { c ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(3.dp)
                        .clip(RoundedCornerShape(50))
                        .background(c.copy(alpha = 0.85f))
                )
            }
        }

        // What to do.
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 11.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.width(28.dp).height(1.dp).background(theme.accent.copy(alpha = 0.4f)))
            Spacer(Modifier.width(9.dp))
            Text(text = "✏️", style = wsFont(14f, FontWeight.Normal))
            Spacer(Modifier.width(9.dp))
            Text(
                text = spec.instruction,
                style = wsFont(12f, FontWeight.Bold),
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.width(9.dp))
            Box(Modifier.width(28.dp).height(1.dp).background(theme.accent.copy(alpha = 0.4f)))
        }
    }
}

@Composable
private fun WriteOnLine(label: String, width: androidx.compose.ui.unit.Dp) {
    Row(verticalAlignment = Alignment.Bottom) {
        Text(text = label, style = wsFont(10f, FontWeight.Bold), color = Color(0xFF6B7280))
        Spacer(Modifier.width(6.dp))
        Box(
            modifier = Modifier
                .width(width)
                .height(2.dp)
                .background(Color(0xFFCBD5E1))
        )
    }
}

/** The bottom of every sheet — identical everywhere, like the header. */
@Composable
private fun WorksheetFooter(theme: WorksheetTheme) {
    Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.weight(1f).height(1.dp).background(theme.accent.copy(alpha = 0.33f)))
            Row {
                Text("Learn", style = wsFont(14f, FontWeight.Black), color = Color(0xFFE63946))
                Text(" · ", style = wsFont(14f, FontWeight.Black), color = Color(0xFFF6A93B))
                Text("Play", style = wsFont(14f, FontWeight.Black), color = Color(0xFF1D3557))
                Text(" · ", style = wsFont(14f, FontWeight.Black), color = Color(0xFFF6A93B))
                Text("Grow", style = wsFont(14f, FontWeight.Black), color = Color(0xFF7209B7))
            }
            Box(Modifier.weight(1f).height(1.dp).background(theme.accent.copy(alpha = 0.33f)))
        }
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text(text = theme.mascot, style = wsFont(15f, FontWeight.Normal))
            Spacer(Modifier.width(5.dp))
            Text(
                text = "Colour a star for every word you read",
                style = wsFont(9.5f, FontWeight.Bold),
                color = theme.accent
            )
            Spacer(Modifier.width(5.dp))
            Text(text = "☆☆☆", style = wsFont(11f, FontWeight.Bold), color = theme.accent)
            Spacer(Modifier.weight(1f))
            Text(
                text = "Kids English Learning by Vedaavi",
                style = wsFont(8.5f, FontWeight.SemiBold),
                color = Color(0xFF9AA0A6)
            )
        }
    }
}
