package com.example.myapplication.main.age_group.from_3_to_5.fill_blank.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.myapplication.main.age_group.from_3_to_5.fill_blank.view_model.FillBlankLettersViewModel
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.FillBlankLetterBoxSize
import com.example.myapplication.ui.theme.PrimaryBlue
import com.example.myapplication.ui.theme.PrimaryGreenLight
import com.example.myapplication.utils.extensions.scaled

@Composable
fun TopLetterSlots(viewModel: FillBlankLettersViewModel) {

    val uiState = viewModel.uiState

    Row(horizontalArrangement = Arrangement.spacedBy(Dimens12.scaled())) {

        uiState.topSlots.forEachIndexed { index, letter ->

            val isFixed = uiState.fixedIndices.contains(index)
            Box(
                modifier = Modifier
                    .size(FillBlankLetterBoxSize)
                    .clip(RoundedCornerShape(Dimens12))
                    .background(if (isFixed) PrimaryGreenLight else Color.Transparent, RoundedCornerShape(Dimens12))
                    .clickable { viewModel.onTopLetterClick(index) },
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = letter ?: "",
                    fontSize = (FillBlankLetterBoxSize.value * 0.75).sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isFixed) Color.DarkGray else PrimaryBlue
                )

                if (!isFixed){
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .height(Dimens2)
                            .fillMaxWidth()
                            .background(Color.Black)
                    )
                }
            }
        }
    }
}