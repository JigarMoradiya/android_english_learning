package com.example.myapplication.main.common

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
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
import androidx.compose.ui.platform.LocalDensity
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
import com.example.myapplication.ui.theme.AppDimens.Dimens30
import com.example.myapplication.ui.theme.AppDimens.isTablet
import com.example.myapplication.utils.extensions.scaled
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun MascotPanel(
    message: String,
    textColor: Color,
    borderColor: Color,
    modifier: Modifier = Modifier,
    scrollOffset: () -> Float = { 0f }
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

    val density = LocalDensity.current.density

    // Animatables are read only inside graphicsLayer blocks, so scroll motion
    // updates the render layer directly — no recomposition, no relayout
    val tiltAnim = remember { Animatable(0f) }
    val squashAnim = remember { Animatable(0f) }
    // Smooth spring while tracking the scroll; bouncy one for the settle-hop and wiggle
    val trackSpring = remember { spring<Float>(dampingRatio = 0.8f, stiffness = 1700f) }
    val bounceSpring = remember { spring<Float>(dampingRatio = 0.4f, stiffness = 300f) }

    // Hello wiggle on entry
    LaunchedEffect(Unit) {
        delay(800)
        tiltAnim.animateTo(8f, bounceSpring)
        tiltAnim.animateTo(0f, bounceSpring)
    }

    // Lean + squash while the menu scrolls; stretch-hop once it settles
    LaunchedEffect(Unit) {
        val scope = this
        var last = Float.NaN
        var smoothed = 0f
        var relaxJob: Job? = null
        snapshotFlow { scrollOffset() }.collect { offset ->
            if (last.isNaN()) {
                last = offset
                return@collect
            }
            // px → dp so speed feel matches iOS points regardless of screen density
            val delta = (offset - last) / density
            last = offset
            if (abs(delta) <= 0.1f) return@collect

            // Low-pass the scroll speed so the lean holds steady during a drag
            smoothed = smoothed * 0.7f + delta * 0.3f
            val tiltTarget = (-smoothed).coerceIn(-14f, 14f)
            val squashTarget = (abs(smoothed) * 0.003f).coerceAtMost(0.08f)

            relaxJob?.cancel()
            scope.launch { tiltAnim.animateTo(tiltTarget, trackSpring) }
            scope.launch { squashAnim.animateTo(squashTarget, trackSpring) }
            relaxJob = scope.launch {
                delay(200)
                smoothed = 0f
                launch { tiltAnim.animateTo(0f, bounceSpring) }
                squashAnim.animateTo(-0.08f, bounceSpring)   // negative = stretch up
                squashAnim.animateTo(0f, bounceSpring)
            }
        }
    }

    Column(
        modifier = modifier
            .padding(start = Dimens8)
            .padding(top = Dimens8),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = if (isTablet) Arrangement.Center else Arrangement.Top
    ) {
        // Speech bubble — wraps to text width with downward-pointing tail, right-aligned
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .align(Alignment.End)
                .padding(end = Dimens8)
                .graphicsLayer {
                    rotationZ = tiltAnim.value * 0.35f
                    transformOrigin = TransformOrigin(0.5f, 1f)
                }
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
                style = MaterialTheme.typography.labelLarge.scaled(),
                fontWeight = FontWeight.Black,
                color = textColor
            )
        }

        if (isTablet) Spacer(modifier = Modifier.height(Dimens30))

        // Mascot — fills remaining height. Bounds must hug the artwork so the
        // tilt/squash pivot sits at the mascot's feet, not the panel bottom.
        val mascotPainter = painterResource(id = R.drawable._mascot_)
        val mascotRatio = mascotPainter.intrinsicSize.let {
            if (it.height > 0f) it.width / it.height else 1f
        }
        Box(
            modifier = (if (!isTablet) Modifier.weight(1f) else Modifier)
                .fillMaxWidth()
                .padding(horizontal = Dimens4)
                .graphicsLayer { translationY = (bob - 16f).dp.toPx() },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = mascotPainter,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .aspectRatio(mascotRatio)
                    .graphicsLayer {
                        rotationZ = tiltAnim.value
                        scaleX = 1f + squashAnim.value * 0.7f
                        scaleY = 1f - squashAnim.value
                        transformOrigin = TransformOrigin(0.5f, 1f)
                    }
            )
        }
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
