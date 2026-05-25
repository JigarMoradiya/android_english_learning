package com.example.myapplication.main.age_group.from_3_to_5.drag_and_drop_word.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.myapplication.main.age_group.from_3_to_5.drag_and_drop_word.view_model.DragDropWordViewModel35
import com.example.myapplication.main.common.getImageResFromWord
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens50
import com.example.myapplication.utils.extensions.scaled

@Composable
fun DragDropScreen35(
    viewModel: DragDropWordViewModel35,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.weight(1f))

            getImageResFromWord(viewModel.targetWord)?.let {
                Image(
                    painter = painterResource(it),
                    contentDescription = null,
                    modifier = Modifier.fillMaxHeight(0.17f)
                )

                Spacer(modifier = Modifier.height(Dimens16.scaled()))
            }

            // -------------------------
            // WORD SLOTS
            // -------------------------
            DragDropTopSlots(viewModel)

            Spacer(modifier = Modifier.height(Dimens50))

            // -------------------------
            // LETTER POOL
            // -------------------------
            DragDropBottomPool(viewModel)

            Spacer(Modifier.weight(1f))
        }


    }
}