package com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.components.ColorPickerRow
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.components.ColoringCanvasFinal
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model.ColoringAlphabetsViewModel
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.buttons.KidsIconButton
import com.example.myapplication.main.common.getImageResFromWord
import com.example.myapplication.ui.theme.ButtonType


@Composable
fun ColoringAlphabetsPage(
    navController: NavController,
    viewModel: ColoringAlphabetsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val item = viewModel.currentItem

    Box(modifier = Modifier.fillMaxSize()) {

        // 🌿 Background (reuse your existing)
        BackgroundUI(isGreenGrassShow = false)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // -------------------------
            // TOP BAR
            // -------------------------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                // ⬅️ Back
                KidsIconButton(
                    icon = Icons.AutoMirrored.Rounded.ArrowBack,
                    onClick = { navController.popBackStack() },
                    type = ButtonType.BLUE
                )

                // 🔊 Audio
                KidsIconButton(
                    icon = Icons.AutoMirrored.Rounded.VolumeUp,
                    onClick = {
                        viewModel.playAudio(context)
                    },
                    type = ButtonType.BLUE
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // -------------------------
            // LETTER TITLE
            // -------------------------
            Text(
                text = item?.letter ?: "",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // -------------------------
            // CANVAS AREA
            // -------------------------
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
            ) {
//                ColoringCanvas(viewModel)

                val img = getImageResFromWord(item?.outlineImageName)
                ColoringCanvasFinal(
                    viewModel = viewModel,
                    letterRes = img ?: R.drawable.a_outline_c
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // -------------------------
            // COLOR PICKER
            // -------------------------
            ColorPickerRow(viewModel)

            Spacer(modifier = Modifier.height(12.dp))

            // -------------------------
            // CONTROLS
            // -------------------------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                // ⬅️ Previous
                KidsIconButton(
                    icon = Icons.AutoMirrored.Rounded.ArrowBack,
                    onClick = { viewModel.previousItem(context) },
                    type = ButtonType.BLUE

                )

                // 🧹 Clear
                KidsIconButton(
                    icon = Icons.Rounded.Delete,
                    onClick = { viewModel.clearCanvas() },
                    type = ButtonType.BLUE

                )

                // ➡️ Next
                KidsIconButton(
                    icon = Icons.AutoMirrored.Rounded.ArrowForward,
                    onClick = { viewModel.nextItem(context) },
                    type = ButtonType.BLUE
                )
            }
        }
    }
}