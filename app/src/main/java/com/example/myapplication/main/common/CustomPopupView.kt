package com.example.myapplication.main.common

import android.text.Html
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.toColorInt
import com.example.myapplication.R
import com.example.myapplication.ui.theme.AppDimens.CommonPopupImageSize
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.ButtonType
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
    icon: Int? = null // Drawable resource ID
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = position
    ) {
        // Calculate popup width based on screen width
        val popupWidth = LocalConfiguration.current.screenWidthDp * widthMultiplier

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .width(popupWidth.dp)
                .background(Color.White, RoundedCornerShape(20.dp))
                .padding(horizontal = Dimens20, vertical = Dimens4)
        ) {
            // 🔹 Optional Icon
            if (icon != null) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier
                        .height(CommonPopupImageSize)
                        .padding(top = Dimens12)
                )
            }

            // 🔹 Title
            if (!title.isNullOrEmpty()) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = Dimens16)
                )
            }

            // 🔹 Description
            if (!description.isNullOrEmpty()) {
                AndroidView(
                    factory = { context ->
                        TextView(context).apply {
                            textSize = 15f
                            setTextColor("#000000".toColorInt())
                            textAlignment = TextView.TEXT_ALIGNMENT_CENTER

                            typeface = ResourcesCompat.getFont(context, R.font.font_medium)
                        }
                    },
                    update = { textView ->
                        textView.text = Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY)
                    },
                    modifier = Modifier.padding(top = Dimens4)
                )
            }

            if (!notes.isNullOrEmpty()) {
                Text(
                    text = notes.htmlToAnnotatedString(),
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Red, fontWeight = FontWeight.SemiBold),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = Dimens8)
                )
            }

            // 🔹 Buttons
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = Dimens8)) {

                // ✅ Positive Button
                if (!positiveButtonText.isNullOrEmpty() && onPositiveTapped != null) {
                    KidsActionButton(
                        text = positiveButtonText,
                        type = ButtonType.GREEN,
                        onClick = onPositiveTapped
                    )
                }

                // ✅ Negative Button
                if (!negativeButtonText.isNullOrEmpty() && onNegativeTapped != null) {
                    KidsActionButton(
                        text = negativeButtonText,
                        type = ButtonType.TEAL,
                        onClick = onNegativeTapped
                    )
                }
            }
        }
    }
}
