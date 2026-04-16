package com.example.myapplication.main.age_group.from_3_to_5.match_letter_with_image.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.main.common.getImageResFromWord
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens3
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.PrimaryGreen
import com.example.myapplication.utils.extensions.scaled

@Composable
fun MatchWithImageItem(
    letter: String,
    word: String,
    isMatched: Boolean,
    rootCoords: LayoutCoordinates?,
    onUpdateImagePosition: (String, Offset) -> Unit,
    onUpdateImageRect: (String, Rect) -> Unit
) {

    val res = getImageResFromWord(word)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens6)
    ) {

        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .padding(top = Dimens3),
            contentAlignment = Alignment.Center
        ) {

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .shadow(Dimens6, RoundedCornerShape(Dimens16))
                    .clip(RoundedCornerShape(Dimens16))
                    .background(Color.White)
                    .border(
                        width = Dimens2,
                        color = if (isMatched) PrimaryGreen else Color.White,
                        shape = RoundedCornerShape(Dimens16)
                    )
                    .onGloballyPositioned { coords ->

                        val root = rootCoords ?: return@onGloballyPositioned

                        val pos = root.localPositionOf(coords, Offset.Zero)
                        val size = coords.size

                        val topCenter = Offset(
                            pos.x + size.width / 2f,
                            pos.y
                        )

                        onUpdateImagePosition(letter, topCenter)

                        val rect = Rect(
                            offset = pos,
                            size = Size(size.width.toFloat(), size.height.toFloat())
                        )
                        onUpdateImageRect(letter, rect)
                    },
                contentAlignment = Alignment.Center
            ) {

                res?.let {
                    Image(
                        painter = painterResource(it),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(0.8f)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = -Dimens3)
                    .size(Dimens6)
                    .background(Color.DarkGray, CircleShape)
            )
        }

        Text(
            text = word.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.titleSmall.scaled(),
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black,
            maxLines = 1,
            modifier = Modifier.alpha(if (isMatched) 1f else 0f)
        )
    }
}