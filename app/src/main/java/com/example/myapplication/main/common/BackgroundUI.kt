package com.example.myapplication.main.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.MyApplicationTheme

@Composable
fun BackgroundUI(
    isGreenGrassShow: Boolean = true
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
    ) {

        val screenWidth = maxWidth
        val screenHeight = maxHeight

        // 🌤 Sky Gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x1A2196F3),
                            Color(0x332196F3),
                            Color(0x402196F3)
                        )
                    )
                )
        )

        // ☁️ Cloud 1
        Image(
            painter = painterResource(id = R.drawable.cloud1),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.25f)
                .offset(
                    x = screenWidth * 0.05f,
                    y = screenHeight * 0.15f
                )
                .alpha(0.08f)
        )

        // 🐦 Bird
        Image(
            painter = painterResource(id = R.drawable.fling_bird),
            contentDescription = null,
            modifier = Modifier.padding(horizontal = 200.dp)
                .fillMaxWidth()
                .offset(y = screenHeight * 0.05f)
                .alpha(0.1f)
        )

        // ☁️ Cloud 2 (right aligned)
        Image(
            painter = painterResource(id = R.drawable.cloud2),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.30f)
                .wrapContentWidth(Alignment.End)
                .offset(y = screenHeight * 0.08f)
                .alpha(0.12f)
        )

        if (isGreenGrassShow) {

            // 🏔 Mountain background
            Image(
                painter = painterResource(id = R.drawable.mountain),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.1f),
                contentScale = ContentScale.FillBounds
            )

            // 🌿 Grass images
            GrassImage(R.drawable.grass2, 0.25f, 0.1f, 0.10f)
            GrassImage(R.drawable.grass5, 0.85f, 0.08f, 0.10f)

            // 🌱 Bottom Hill (IMPORTANT)
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {

                Box {
                    Column {

                        BottomHillShapeComposable(
                            height = screenHeight * 0.10f
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(screenHeight * 0.08f)
                                .background(Color(0xFF9ECC55))
                        )
                    }

                }
            }

            // 🌿 Extra grass
            GrassImage(R.drawable.grass1, 0.1f, 0.06f, 0.06f, -10f)
            GrassImage(R.drawable.grass1, 0.35f, 0.08f, 0.06f, 5f)
            GrassImage(R.drawable.grass3, 0.5f, 0.11f, 0.06f, 1f)
            GrassImage(R.drawable.grass4, 0.7f, 0.07f, 0.10f)
        }
    }
}

@Composable
fun GrassImage(
    drawable: Int,
    startX: Float,
    bottomPadding: Float,
    heightPercent: Float,
    rotation: Float = 0f
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val w = maxWidth
        val h = maxHeight

        Image(
            painter = painterResource(id = drawable),
            contentDescription = null,
            modifier = Modifier
                .height(h * heightPercent)
                .offset(
                    x = w * startX,
                    y = h - (h * bottomPadding)
                )
                .graphicsLayer {
                    rotationZ = rotation
                }
        )
    }
}

@Composable
fun BottomHillShapeComposable(height: Dp) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
    ) {

        val path = Path().apply {
            moveTo(0f, size.height)

            quadraticTo(
                size.width / 2,
                size.height - size.height * 1f,
                size.width,
                size.height
            )

            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }

        drawPath(
            path = path,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFC3DA6B),
                    Color(0xFF9ECC55)
                )
            )
        )

        // Stroke
        drawPath(
            path = Path().apply {
                moveTo(0f, size.height)
                quadraticTo(
                    size.width / 2,
                    size.height - size.height * 1f,
                    size.width,
                    size.height
                )
            },
            color = Color(0xFF97BE51),
            style = Stroke(width = 6.dp.toPx())
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=1280dp,height=720dp,dpi=240" // landscape feel
)
@Composable
fun BackgroundUIPreview() {
    MyApplicationTheme {
        BackgroundUI(isGreenGrassShow = true)
    }
}