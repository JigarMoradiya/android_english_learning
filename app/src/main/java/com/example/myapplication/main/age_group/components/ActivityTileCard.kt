package com.example.myapplication.main.age_group.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.data.model.LearningActivityModel
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.utils.extensions.scaled

private val TILE_CORNER = 18.dp

@Composable
fun ActivityTileCard(
    activity: LearningActivityModel,
    tileHeight: Dp,
    showCard: Boolean = false,
    onClick: () -> Unit
) {
    val imageSize = tileHeight * (if (showCard) 0.72f else 0.9f)

    val cardModifier = if (showCard) {
        val cardShape = RoundedCornerShape(tileHeight * 0.18f)
        val tint = activity.txtColor
        Modifier
            .shadow(
                elevation = Dimens4,
                shape = cardShape,
                clip = false,
                ambientColor = tint.copy(alpha = 0.3f),
                spotColor = tint.copy(alpha = 0.3f)
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, tint.copy(alpha = 0.14f))
                ),
                shape = cardShape
            )
            .border(width = 1.5.dp, color = tint.copy(alpha = 0.45f), shape = cardShape)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
    } else {
        Modifier
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = if (showCard) Arrangement.Center else Arrangement.Top,
        modifier = Modifier
            .width(tileHeight)
            .height(tileHeight)
            .then(cardModifier)
    ) {
        Image(
            painter = painterResource(id = activity.img),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(imageSize)
                .clip(RoundedCornerShape(TILE_CORNER))
                .then(if (showCard) Modifier else Modifier.clickable(onClick = onClick))
        )

        if (showCard) Spacer(Modifier.height(Dimens4))

        Text(
            text = stringResource(activity.titleRes),
            style = if (DeviceInfo.isTablet) MaterialTheme.typography.titleSmall.scaled()
                    else MaterialTheme.typography.labelMedium.scaled(),
            fontWeight = FontWeight.ExtraBold,
            color = activity.txtColor,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(if (showCard) tileHeight * 0.88f else tileHeight)
        )
    }
}
