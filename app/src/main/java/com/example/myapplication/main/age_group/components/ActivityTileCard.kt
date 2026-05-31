package com.example.myapplication.main.age_group.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.myapplication.utils.extensions.scaled

private val TILE_CORNER = 18.dp

@Composable
fun ActivityTileCard(
    activity: LearningActivityModel,
    tileHeight: Dp,
    onClick: () -> Unit
) {
    val imageSize = tileHeight * 0.9f
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(tileHeight)
            .height(tileHeight)
    ) {
        Image(
            painter = painterResource(id = activity.img),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(imageSize)
                .clip(RoundedCornerShape(TILE_CORNER))
                .clickable(onClick = onClick)
        )

        Text(
            text = stringResource(activity.titleRes),
            style = if (DeviceInfo.isTablet) MaterialTheme.typography.titleSmall.scaled()
                    else MaterialTheme.typography.labelMedium.scaled(),
            fontWeight = FontWeight.ExtraBold,
            color = activity.txtColor,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(tileHeight)
        )
    }
}
