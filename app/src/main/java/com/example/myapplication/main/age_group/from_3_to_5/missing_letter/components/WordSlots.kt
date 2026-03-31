package com.example.myapplication.main.age_group.from_3_to_5.missing_letter.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.main.age_group.from_3_to_5.missing_letter.view_model.MissingLetterViewModel


@Composable
fun WordSlots(viewModel: MissingLetterViewModel) {

    val dropped = viewModel.dropped

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

        dropped.forEachIndexed { index, item ->

            Box(
                modifier = Modifier
                    .size(60.dp)

                    // ⭐ IMPORTANT
                    .onGloballyPositioned {
                        viewModel.updateSlotRect(index, it.boundsInRoot())
                    }

                    .border(2.dp, Color.Gray, RoundedCornerShape(12.dp))
                    .background(Color.White),

                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = item?.letter ?: "",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}