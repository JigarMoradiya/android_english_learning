package com.example.myapplication.main.age_group.from_3_to_5.match_letter_with_image.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutCoordinates
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16

@Composable
fun MatchWithImagesGrid(
    images: List<Pair<String, String>>,
    matchedLetters: Set<String>,
    rootCoords: LayoutCoordinates?,
    onUpdateImagePosition: (String, Offset) -> Unit,
    onUpdateImageRect: (String, Rect) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        modifier = Modifier.fillMaxWidth(0.8f),
        horizontalArrangement = Arrangement.spacedBy(Dimens16),
        verticalArrangement = Arrangement.spacedBy(Dimens12)
    ) {
        items(images) { (letter, word) ->

            MatchWithImageItem(
                letter = letter,
                word = word,
                isMatched = matchedLetters.contains(letter),
                rootCoords = rootCoords,
                onUpdateImagePosition = onUpdateImagePosition,
                onUpdateImageRect = onUpdateImageRect
            )
        }
    }
}