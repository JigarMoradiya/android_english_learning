package com.example.myapplication.main.common

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.AppDimens.Dimens3
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens14
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens18
import com.example.myapplication.utils.extensions.scaled

@Composable
fun MascotPanel(
    message: String,
    textColor: Color,
    borderColor: Color,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "mascot_bob")
    val bob by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bob"
    )

    Column(
        modifier = modifier
            .padding(start = Dimens8)
            .padding(top = Dimens8),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Speech bubble — wraps to text width with downward-pointing tail
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = Dimens8)
                .shadow(
                    elevation = Dimens3,
                    shape = DownBubbleShape(),
                    clip = false,
                    ambientColor = borderColor.copy(alpha = 0.18f),
                    spotColor = borderColor.copy(alpha = 0.18f)
                )
                .clip(DownBubbleShape())
                .background(Color.White.copy(alpha = 0.92f))
                .drawWithContent {
                    drawContent()
                    val outline = DownBubbleShape().createOutline(size, layoutDirection, this)
                    if (outline is Outline.Generic) {
                        drawPath(
                            path = outline.path,
                            color = borderColor.copy(alpha = 0.5f),
                            style = Stroke(width = 2.5.dp.toPx())
                        )
                    }
                }
                .padding(horizontal = Dimens10)
                .padding(top = Dimens8, bottom = Dimens18),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall.scaled(),
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }

        // Mascot — fills remaining height, bottom padding prevents overflow
        Image(
            painter = painterResource(id = R.drawable._mascot_lion),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = Dimens4)
                .padding(bottom = Dimens16)
                .offset(y = bob.dp)
        )
    }
}

class DownBubbleShape(
    private val cornerRadius: Dp = Dimens12,
    private val tailHeight: Dp  = Dimens10,
    private val tailWidth: Dp   = Dimens14
) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val r     = with(density) { cornerRadius.toPx() }
        val tailH = with(density) { tailHeight.toPx() }
        val tailW = with(density) { tailWidth.toPx() }
        val bH    = size.height - tailH
        val mx    = size.width / 2f

        val path = Path().apply {
            moveTo(r, 0f)
            lineTo(size.width - r, 0f)
            arcTo(Rect(size.width - 2 * r, 0f, size.width, 2 * r), -90f, 90f, false)
            lineTo(size.width, bH - r)
            arcTo(Rect(size.width - 2 * r, bH - 2 * r, size.width, bH), 0f, 90f, false)
            lineTo(mx + tailW / 2f, bH)
            lineTo(mx, size.height)
            lineTo(mx - tailW / 2f, bH)
            lineTo(r, bH)
            arcTo(Rect(0f, bH - 2 * r, 2 * r, bH), 90f, 90f, false)
            lineTo(0f, r)
            arcTo(Rect(0f, 0f, 2 * r, 2 * r), 180f, 90f, false)
            close()
        }
        return Outline.Generic(path)
    }
}
