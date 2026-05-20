package com.example.myapplication.main.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.utils.extensions.scaled

@Composable
fun InstructionBadge(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Rounded.TouchApp,
    color: Color = Color(0xFF5C6BC0)
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(Dimens12))
            .background(color.copy(alpha = 0.10f))
            .border(1.5.dp, color.copy(alpha = 0.3f), RoundedCornerShape(Dimens12))
            .padding(horizontal = Dimens12, vertical = Dimens4),
        horizontalArrangement = Arrangement.spacedBy(Dimens8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(Dimens16)
        )
        Text(
            text = AnnotatedString.fromHtml(text),
            style = MaterialTheme.typography.bodySmall.scaled(),
            color = Color.Black.copy(alpha = 0.75f)
        )
    }
}