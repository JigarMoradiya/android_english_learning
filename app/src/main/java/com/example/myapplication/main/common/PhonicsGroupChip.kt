package com.example.myapplication.main.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.utils.extensions.scaled

/**
 * Compact horizontal chip for phonics intro pages:
 * emoji on the left, title + optional subtitle stacked on the right.
 * Place inside a FlowRow so chips wrap instead of getting cut.
 */
@Composable
fun PhonicsGroupChip(
    emoji: String,
    title: String,
    accentColor: Color,
    subtitle: String? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens6),
        modifier = Modifier
            .background(accentColor.copy(alpha = 0.10f), RoundedCornerShape(Dimens8))
            .border(1.5.dp, accentColor.copy(alpha = 0.35f), RoundedCornerShape(Dimens8))
            .padding(horizontal = Dimens8, vertical = Dimens6)
    ) {
        Text(text = emoji, style = MaterialTheme.typography.titleMedium.scaled())
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.scaled(),
                fontWeight = FontWeight.ExtraBold,
                color = accentColor
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall.scaled(),
                    color = accentColor.copy(alpha = 0.80f)
                )
            }
        }
    }
}
