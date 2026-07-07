package com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model.ColoringAlphabetModel
import com.example.myapplication.ui.theme.AppDimens.Dimens32
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.utils.extensions.scaled

// A-Z jump picker — auto-scrolls to keep the current letter visible,
// whether it changed via a tap here or via Next/Previous.
@Composable
fun LetterPickerRow(
    items: List<ColoringAlphabetModel>,
    currentIndex: Int,
    onSelect: (Int) -> Unit
) {
    val listState = rememberLazyListState()

    LaunchedEffect(currentIndex) {
        val visibleCount = listState.layoutInfo.visibleItemsInfo.size.coerceAtLeast(1)
        val target = (currentIndex - visibleCount / 2).coerceIn(0, items.lastIndex)
        listState.animateScrollToItem(target)
    }

    LazyRow(
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(Dimens6)
    ) {
        itemsIndexed(items) { index, item ->
            val isSelected = index == currentIndex
            Box(
                modifier = Modifier
                    .size(Dimens32)
                    .clip(CircleShape)
                    .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.White)
                    .then(
                        if (!isSelected) Modifier.border(1.dp, Color.Black.copy(alpha = 0.08f), CircleShape)
                        else Modifier
                    )
                    .clickable { onSelect(index) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.letter,
                    color = if (isSelected) Color.White else Color.Black.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge.scaled()
                )
            }
        }
    }
}
