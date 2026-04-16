package com.example.myapplication.main.age_group.from_3_to_5.abcd_with_images

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.main.age_group.from_3_to_5.abcd_with_images.view_model.ABCDWithImagesViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.buttons.KidsIconButton
import com.example.myapplication.main.common.getImageResFromWord
import com.example.myapplication.ui.theme.AppDimens.ABCDWithImagesBigTextSize
import com.example.myapplication.ui.theme.AppDimens.ABCDWithImagesSmallImageSize
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.isTablet
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.ui.theme.Gray


@Composable
fun ABCDWithImagesPage(
    navController: NavController,
    viewModel: ABCDWithImagesViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState
    val item = viewModel.currentLetterData

    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundUI(isGreenGrassShow = false)

        Row(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {

            // -------------------------------
            // LEFT SIDE → GRID
            // -------------------------------
            Column(modifier = Modifier.weight(1.5f)) {
                BackButtonWithText(
                    title = stringResource(R.string.abcd_with_images),
                    onBackClick = { navController.popBackStack() }
                )
                val listState = rememberLazyListState()
                LaunchedEffect(uiState.currentWord) {
                    listState.scrollToItem(0)
                }
                if (isTablet){
                    Spacer(Modifier.weight(1f))

                    getImageResFromWord(uiState.currentWord)?.let {
                        Image(
                            painter = painterResource(it),
                            contentDescription = null,
                            modifier = Modifier.fillMaxHeight(0.4f)
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    LazyRow(
                        state = listState,
                        horizontalArrangement = Arrangement.spacedBy(Dimens12),
                        modifier = Modifier.padding(horizontal = Dimens12)
                    ) {
                        items(uiState.currentMatches) { match ->
                            getImageResFromWord(uiState.currentWord)?.let {
                                Image(
                                    painter = painterResource(it),
                                    contentDescription = match,
                                    modifier = Modifier
                                        .size(ABCDWithImagesSmallImageSize)
                                        .clip(RoundedCornerShape(Dimens12))
                                        .clickable {
                                            viewModel.swapWithMain(match)
                                        }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.weight(1f))
                }else{
                    Row(
                        modifier = Modifier.padding(start = DeviceInfo.screenHorizontalPadding()),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.padding(bottom = Dimens16, top = Dimens8),
                            verticalArrangement = Arrangement.spacedBy(Dimens12)
                        ) {
                            items(uiState.currentMatches) { match ->

                                val res = getImageResFromWord(match)

                                res?.let {
                                    Image(
                                        painter = painterResource(it),
                                        contentDescription = match,
                                        modifier = Modifier
                                            .height(ABCDWithImagesSmallImageSize)
                                            .aspectRatio(1f)
                                            .clip(RoundedCornerShape(Dimens12))
                                            .clickable {
                                                viewModel.swapWithMain(match)
                                            }
                                    )
                                }
                            }
                        }

                        getImageResFromWord(uiState.currentWord)?.let {
                            Image(
                                painter = painterResource(it),
                                contentDescription = null,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(0.8f)
                            )
                        }
                    }
                }
            }

            // -------------------------
            // RIGHT → TEXT + CONTROLS
            // -------------------------
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.weight(1f))

                // BIG LETTER

                Text(
                    text = item.letter,
                    fontSize = ABCDWithImagesBigTextSize,
                    fontWeight = FontWeight.Black,
                    color = Gray
                )

                // WORD TEXT
                Text(
                    text = buildAnnotatedString {

                        withStyle(
                            style = SpanStyle(
                                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                                fontWeight = FontWeight.Black,
                                color = Gray
                            )
                        ) {
                            append(item.letter)
                        }

                        append(" ")

                        withStyle(
                            style = SpanStyle(
                                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                                fontWeight = FontWeight.SemiBold,
                                color = Gray
                            )
                        ) {
                            append("for")
                        }

                        append(" ")

                        withStyle(
                            style = SpanStyle(
                                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                                fontWeight = FontWeight.Black,
                                color = Color.Black,
                            )
                        ) {
                            append(uiState.currentWord)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(Dimens16))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens24),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // ⬅️ Previous
                    KidsIconButton(
                        icon = Icons.AutoMirrored.Rounded.ArrowBack,
                        onClick = { viewModel.previous() },
                        type = ButtonType.ORANGE
                    )

                    // 🔊 Speak (FUN COLOR 🎤)
                    KidsIconButton(
                        icon = Icons.AutoMirrored.Rounded.VolumeUp,
                        onClick = { viewModel.speakCurrent() },
                        type = ButtonType.PINK
                    )

                    // ➡️ Next
                    KidsIconButton(
                        icon = Icons.AutoMirrored.Rounded.ArrowForward,
                        onClick = { viewModel.next() },
                        type = ButtonType.ORANGE
                    )
                }

                Spacer(Modifier.weight(1f))
            }

        }
    }
}
