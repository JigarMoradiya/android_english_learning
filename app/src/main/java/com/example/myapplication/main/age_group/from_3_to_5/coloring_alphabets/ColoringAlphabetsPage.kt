package com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.components.ColoringCanvas
import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model.ColoringAlphabetsViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.buttons.KidsIconButton
import com.example.myapplication.main.common.getImageResFromWord
import com.example.myapplication.ui.theme.ButtonType


@Composable
fun ColoringAlphabetsPage(
    navController: NavController,
    viewModel: ColoringAlphabetsViewModel = hiltViewModel()
) {

    val state = viewModel.uiState
    val item = state.items[state.currentIndex]

    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundUI()

        Column(modifier = Modifier.fillMaxSize()) {

            BackButtonWithText(
                title = stringResource(R.string.coloring_alphabet),
                onBackClick = { navController.popBackStack() }
            )

            // 🔥 MAIN CONTENT
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                // 🟦 CANVAS CARD
                Box(
                    modifier = Modifier
                        .size(320.dp)
                        .shadow(8.dp, RoundedCornerShape(24.dp))
                        .background(Color(0xFFEFEFEF), RoundedCornerShape(24.dp))
                        .padding(16.dp)
                ) {
                    ColoringCanvas(
                        outlineName = item.outlineImageName,
                        strokes = state.strokes,
                        viewModel = viewModel
                    )
                }

                Spacer(modifier = Modifier.width(40.dp))

                // 🍎 RIGHT SIDE (IMAGE + WORD)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    val res = getImageResFromWord(item.word)
                    res?.let {
                        Image(
                            painter = painterResource(id = res),
                            contentDescription = null,
                            modifier = Modifier.size(140.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Text(
                        text = item.word,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // 🎨 BOTTOM CONTROLS
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {


                KidsActionButton(
                    text = stringResource(R.string.previous),
                    icon = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                    type = ButtonType.ORANGE,
                    onClick = {
                        viewModel.previous()
                    }
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                    // 👉 simple colors (no picker for now)
                    listOf(
                        Color.Red,
                        Color.Green,
                        Color.Blue,
                        Color.Yellow,
                        Color.Magenta
                    ).forEach { color ->

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(color, CircleShape)
                                .border(2.dp, Color.Black, CircleShape)
                                .clickable {
                                    // later hook color
                                }
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                    IconButton(onClick = { viewModel.clear() }) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                    }


                    KidsActionButton(
                        text = stringResource(R.string.next),
                        icon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        type = ButtonType.ORANGE,
                        onClick = { viewModel.next() },
                        isIconStart = false
                    )
                }
            }
        }
    }
}