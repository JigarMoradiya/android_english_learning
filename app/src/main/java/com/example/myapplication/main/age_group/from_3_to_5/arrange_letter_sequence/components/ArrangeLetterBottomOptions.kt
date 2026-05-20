package com.example.myapplication.main.age_group.from_3_to_5.arrange_letter_sequence.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.myapplication.main.age_group.from_3_to_5.arrange_letter_sequence.view_model.ArrangeLetterInSequenceViewModel
import com.example.myapplication.ui.theme.AppDimens.ArrangeLetterInSequenceBoxSize
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.utils.extensions.scaled

private val kidsColors = listOf(
    Color(0xFFFF8C42),
    Color(0xFF5B9BD5),
    Color(0xFFC86DD7),
    Color(0xFF4CAF78),
    Color(0xFFFF6B8A),
)

@Composable
fun BottomArrangeLetterOptions(viewModel: ArrangeLetterInSequenceViewModel) {

    val uiState = viewModel.uiState

    Row(horizontalArrangement = Arrangement.spacedBy(Dimens12.scaled())) {

        uiState.bottomOptions.forEachIndexed { index, letter ->

            val tileColor = kidsColors[index % kidsColors.size]

            Box(
                modifier = Modifier
                    .size(ArrangeLetterInSequenceBoxSize)
                    .shadow(elevation = Dimens6, shape = RoundedCornerShape(Dimens16))
                    .clip(RoundedCornerShape(Dimens16))
                    .background(tileColor)
                    .clickable { viewModel.onBottomLetterClick(letter) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = letter,
                    fontSize = (ArrangeLetterInSequenceBoxSize.value * 0.75).sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge.scaled().copy(shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.25f),
                        offset = Offset(1f, 1f),
                        blurRadius = 2f
                    )),
                )
            }
        }
    }
}
