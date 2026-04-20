package com.example.myapplication.main.age_group.from_6_to_8.read_listen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.rounded.Undo
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.data.model.ReadSentenceItemNew
import com.example.myapplication.data.model.UnitSelectionScreen
import com.example.myapplication.main.age_group.from_6_to_8.read_listen.view_model.ReadAndListenViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.buttons.KidsIconButton
import com.example.myapplication.main.common.getImageResFromWord
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.KidIconSmall
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled


@Composable
fun ReadAndListenPage(
    screenType : UnitSelectionScreen,
    lessonData: ReadSentenceItemNew,
    navController: NavController,
    viewModel: ReadAndListenViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.setScreenTypeAndLessonData(screenType,lessonData)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundUI(isGreenGrassShow = false)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {

            // 🔵 HEADER
            Row(verticalAlignment = Alignment.CenterVertically) {
                BackButtonWithText(
                    modifier = Modifier.weight(1f),
                    title = lessonData.title,
                    onBackClick = { navController.popBackStack() }
                )

                KidsActionButton(
                    text = if (viewModel.uiState.isSentenceJoined)
                        stringResource(R.string.split_sentences)
                    else stringResource(R.string.join_sentences),
                    type = ButtonType.BLUE,
                    onClick = { viewModel.toggleJoinWords() },
                    isSmall = true,
                    modifier = Modifier.padding(end = Dimens8)
                )

                KidsIconButton(
                    icon = Icons.AutoMirrored.Rounded.VolumeUp,
                    onClick = { },
                    type = ButtonType.PINK,
                    size = KidIconSmall,
                    modifier = Modifier.padding(end = Dimens16)
                )
            }

            // 🟡 MIDDLE (takes full remaining space)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = Dimens16)
                ) {

                    // ⬅️ LEFT BUTTON
                    KidsActionButton(
                        text = stringResource(R.string.previous),
                        icon = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                        type = if (viewModel.isAtFirst) ButtonType.DISABLE else ButtonType.ORANGE,
                        onClick = { viewModel.previousSentence() }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // ➡️ RIGHT BUTTON
                    KidsActionButton(
                        text = stringResource(R.string.next),
                        icon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        type = if (viewModel.isAtLast) ButtonType.DISABLE else ButtonType.ORANGE,
                        isIconStart = false,
                        onClick = { viewModel.nextSentence() }
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = Dimens16)
                ) {

                    Spacer(modifier = Modifier.weight(1f))

                    // 🟩 CENTER IMAGE
                    getImageResFromWord(viewModel.uiState.lessonData?.imageName)?.let { resId ->
                        Image(
                            painter = painterResource(resId),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .aspectRatio(1f) // perfect square
                                .clip(RoundedCornerShape(Dimens16))
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            // 🔵 FOOTER
            Text(
                modifier = Modifier.fillMaxWidth().padding(horizontal = Dimens16, vertical = Dimens16),
                text = viewModel.currentSentence,
                color = Color.Black,
                style = MaterialTheme.typography.titleSmall.scaled().copy(
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}
