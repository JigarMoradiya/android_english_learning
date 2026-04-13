package com.example.myapplication.main.age_group.from_5_to_7.vocabulary_building.category_detail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.main.common.buttons.KidsIconButton
import com.example.myapplication.main.common.getImageResFromWord
import com.example.myapplication.ui.theme.AppDimens.Dimens40
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.KidIconSmall
import com.example.myapplication.ui.theme.AppDimens.VocabularyImageHeight
import com.example.myapplication.ui.theme.ButtonType


@Composable
fun WordCard(
    buttonType: ButtonType,
    word: String,
    img: String,
    bgColor: Color,
    isColorCategory : Boolean,
    onSpeakClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                onSpeakClick()
            }
            .background(bgColor)
            .padding(vertical = Dimens8)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens8),
            modifier = Modifier.fillMaxWidth()
        ) {

            if (!isColorCategory) {
                val res = getImageResFromWord(img)
                res?.let{
                    Image(
                        painter = painterResource(
                            id = res
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .height(VocabularyImageHeight)
                    )
                }
            }

            Text(
                text = word,
                style = if (isColorCategory) MaterialTheme.typography.headlineLarge else MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = when {
                    isColorCategory && word.lowercase() == "white" -> Color.Black
                    isColorCategory -> Color.White
                    else -> Color.Black
                },
                modifier = Modifier.padding(
                    vertical = if (isColorCategory) Dimens40 else 0.dp
                )
            )
        }

        // 🔊 Speaker icon (top-right)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = Dimens8)
                .background(Color.White.copy(alpha = 0.6f), CircleShape)
        ) {
            KidsIconButton(
                icon = Icons.AutoMirrored.Rounded.VolumeUp,
                onClick = { onSpeakClick() },
                type = buttonType,
                size = KidIconSmall
            )
        }
    }
}