package com.example.myapplication.main.common

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.PrimaryBlue


@Composable
fun BackButtonWithText(
    title: String,
    size: Dp = 42.dp,
    onBackClick: () -> Unit
) {
    val headerColors = getButtonColors(ButtonType.BLUE)

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = Dimens8).padding(start = DeviceInfo.screenPadding(), end = Dimens16),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier.wrapContentSize()
        ) {

            // BACK CAPSULE (BEHIND)
            Box(
                modifier = Modifier
                    .padding(start = (size / 2) + Dimens12) // ⭐ push to allow circle overlap
                    .height(size * 0.8f)
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            bottomStart = 0.dp,
                            topEnd = 100.dp,
                            bottomEnd = 100.dp
                        ),
                        clip = false
                    )
                    .background(
                        brush = headerColors.gradient,
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            bottomStart = 0.dp,
                            topEnd = 100.dp,
                            bottomEnd = 100.dp
                        )
                    )
                    .padding(start = size / 2, end = Dimens16) // ⭐ space for circle
                    .align(Alignment.CenterStart)
            ) {

                // TEXT inside capsule
                Box(
                    modifier = Modifier.fillMaxHeight(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = title,
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                    )
                }
            }

            val interactionSource = remember { MutableInteractionSource() }

            Box(
                modifier = Modifier
                    .size(size)
                    .shadow(8.dp, CircleShape, clip = false)
                    .background(headerColors.base, CircleShape)
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = LocalIndication.current
                    ) {
                        onBackClick()
                    }
                    .align(Alignment.CenterStart),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}