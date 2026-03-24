package com.example.myapplication.main.age_category.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.main.age_category.view_model.AgeCategoryData
import com.example.myapplication.ui.theme.AppDimens.Dimens16

@Composable
fun CategoriesHorizontalList(
    categories: List<AgeCategoryData>,
    onClick: (AgeCategoryData) -> Unit
) {

    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {

        val boxWidth = maxWidth
        val boxHeight = maxHeight

        val spacing = 20.dp
        val visibleItems = 3f // change to 2.7f for iOS peek style

        // ✅ Calculate item width properly
        val totalSpacing = spacing * (visibleItems - 1)
        val itemWidth = (boxWidth - totalSpacing) / visibleItems

        // ✅ Content width for centering logic
        val contentWidth =
            (itemWidth * categories.size.toFloat()) +
                    (spacing * (categories.size - 1).coerceAtLeast(0).toFloat())

        val sidePadding =
            if (contentWidth < boxWidth) (boxWidth - contentWidth) / 2 else 0.dp

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing),
            contentPadding = PaddingValues(
                start = sidePadding,
                end = sidePadding
            )
        ) {

            items(categories) { item ->

                val ratio = 0.8f
                Box(
                    modifier = Modifier
                        .width(itemWidth)
                        .height(boxHeight * ratio),
                    contentAlignment = Alignment.Center
                ) {
                    val finalDimen = if (itemWidth > (boxHeight * ratio)) (boxHeight * ratio) else itemWidth
                    Box(
                        modifier = Modifier
                            .width(finalDimen)
                            .height(finalDimen)
                            .clip(RoundedCornerShape(500.dp)) // ⭐ IMPORTANT (ripple shape)
                            .clickable { onClick(item) },
                        contentAlignment = Alignment.Center
                    ){
                        Image(
                            painter = painterResource(id = item.img),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}