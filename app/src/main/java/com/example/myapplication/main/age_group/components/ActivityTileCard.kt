package com.example.myapplication.main.age_group.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.model.LearningActivityModel
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.utils.extensions.scaled

private val TILE_CORNER = 18.dp

/**
 * Activity menu tile card.
 *
 * Shows the activity icon image directly (as designed) with a bright
 * accent-colored label beneath on a white background.
 *
 * @param tileHeight  Height of the image portion of the card.
 */
@Composable
fun ActivityTileCard(
    activity: LearningActivityModel,
    tileHeight: Dp,
    onClick: () -> Unit
) {
    // Square tile: width == height (icons are square by design)
    val tileWidth = tileHeight

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(tileWidth)
    ) {

        // Only image clickable
        Image(
            painter = painterResource(id = activity.img),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(tileHeight)
                .clip(RoundedCornerShape(Dimens16))
                .clickable(onClick = onClick)
        )

        Text(
            text = stringResource(activity.titleRes),
            style = MaterialTheme.typography.labelMedium.scaled(),
            fontWeight = FontWeight.ExtraBold,
            color = activity.txtColor,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
