package com.example.myapplication.main.common

import android.text.Html
import android.widget.TextView
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.toColorInt
import com.example.myapplication.R
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.ui.theme.AppDimens.CommonPopupImageSize
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.ui.theme.PrimaryGreen
import com.example.myapplication.utils.extensions.StringEx.htmlToAnnotatedString

@Composable
fun CustomPopupView(
    title: String? = null,
    description: String? = null,
    notes: String? = null,
    position: Alignment = Alignment.Center,
    positiveButtonText: String? = null,
    negativeButtonText: String? = null,
    onPositiveTapped: (() -> Unit)? = null,
    onNegativeTapped: (() -> Unit)? = null,
    widthMultiplier: Float = 0.5f,
    icon: Int? = null
) {

    val popupWidth = LocalConfiguration.current.screenWidthDp * widthMultiplier

    // 🎮 Animation state
    var startAnimation by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.7f,
        animationSpec = spring(
            dampingRatio = 0.4f,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = ""
    )

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.55f)),
        contentAlignment = position
    ) {

        Box {

            // Shadow layer
            Box(
                modifier = Modifier
                    .width(popupWidth.dp)
                    .offset(y = 6.dp)
                    .clip(RoundedCornerShape(Dimens24))
                    .background(Color.Black.copy(alpha = 0.1f))
            )

            // Main popup
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(popupWidth.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                    .clip(RoundedCornerShape(Dimens24))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFFE8F5E9), // light green
                                Color(0xFFC8E6C9)
                            )
                        )
                    )
                    .drawBehind {
                        val strokeWidth = 6.dp.toPx()
                        val radius = Dimens24.toPx()

                        drawRoundRect(
                            brush = Brush.verticalGradient(
                                listOf(
                                    PrimaryGreen,
                                    Color(0xFF66BB6A),
                                    Color(0xFFC8E6C9),
                                    Color(0xFFC8E6C9)
                                )
                            ),
                            size = size,
                            cornerRadius = CornerRadius(radius, radius),
                            style = Stroke(width = strokeWidth)
                        )
                    }
                    .padding(Dimens20)
            ) {

                // Icon
                if (icon != null) {
                    Image(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        modifier = Modifier
                            .height(CommonPopupImageSize)
                            .padding(top = Dimens8)
                    )

                    Spacer(modifier = Modifier.height(Dimens8))
                }

                // Title
                if (!title.isNullOrEmpty()) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryGreen,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = Dimens12)
                    )
                }

                // Description
                if (!description.isNullOrEmpty()) {
                    AndroidView(
                        factory = { context ->
                            TextView(context).apply {
                                textSize = 15f
                                setTextColor("#2E2E2E".toColorInt())
                                textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                                typeface = ResourcesCompat.getFont(context, R.font.font_medium)
                            }
                        },
                        update = { textView ->
                            textView.text = Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY)
                        },
                        modifier = Modifier.padding(top = Dimens6)
                    )
                }

                //  Notes
                if (!notes.isNullOrEmpty()) {
                    Text(
                        text = notes.htmlToAnnotatedString(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFFD32F2F),
                            fontWeight = FontWeight.SemiBold
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = Dimens8)
                    )
                }

                Spacer(modifier = Modifier.height(Dimens16))

                // Buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens12)
                ) {

                    if (!positiveButtonText.isNullOrEmpty() && onPositiveTapped != null) {
                        KidsActionButton(
                            text = positiveButtonText,
                            type = ButtonType.POSITIVE,
                            onClick = onPositiveTapped
                        )
                    }

                    if (!negativeButtonText.isNullOrEmpty() && onNegativeTapped != null) {
                        KidsActionButton(
                            text = negativeButtonText,
                            type = ButtonType.NEGATIVE,
                            onClick = onNegativeTapped
                        )
                    }
                }
            }
        }
    }
}