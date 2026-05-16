package com.example.myapplication.main.common.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.extensions.scaled

@Composable
fun KidsActivityCard(
    size: Dp,
    title: String,
    subtitle: String? = null,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(Dimens24))
            .background(accentColor.copy(alpha = 0.12f))
            .border(Dimens2, accentColor.copy(alpha = 0.3f), RoundedCornerShape(Dimens24))
            .clickable {
                AudioPlayerManager.playSoundMenuClick()
                onClick()
            }
            .padding(vertical = Dimens20, horizontal = Dimens16),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens12,Alignment.CenterVertically)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = accentColor,
            modifier = Modifier.size(size * 0.42f)
        )
        Text(
            text = title,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black.copy(alpha = 0.85f),
            style = MaterialTheme.typography.titleMedium.scaled(),
            textAlign = TextAlign.Center
        )

        subtitle?.let{
            Text(
                text = subtitle,
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall.scaled(),
                textAlign = TextAlign.Center
            )
        }
    }
}