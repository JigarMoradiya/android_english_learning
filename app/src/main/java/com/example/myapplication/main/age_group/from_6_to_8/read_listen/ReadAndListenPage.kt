package com.example.myapplication.main.age_group.from_6_to_8.read_listen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.model.ReadSentenceItemNew
import com.example.myapplication.data.model.UnitSelectionScreen
import com.example.myapplication.main.age_group.from_6_to_8.read_listen.components.SentenceWordsView
import com.example.myapplication.main.age_group.from_6_to_8.read_listen.view_model.ReadAndListenViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.buttons.KidsIconButton
import com.example.myapplication.main.common.getImageResForSentence
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.KidIconMedium
import com.example.myapplication.ui.theme.AppDimens.isTablet
import com.example.myapplication.ui.theme.ButtonType


@Composable
fun ReadAndListenPage(
    screenType : UnitSelectionScreen,
    lessonData: ReadSentenceItemNew,
    navController: NavController,
    viewModel: ReadAndListenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
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
                    text = if (uiState.isSentenceJoined)
                        stringResource(R.string.split_sentences)
                    else stringResource(R.string.join_sentences),
                    type = ButtonType.BLUE,
                    onClick = { viewModel.toggleJoinWords() },
                    isSmall = true,
                    modifier = Modifier.padding(end = Dimens8)
                )

                KidsIconButton(
                    icon = Icons.AutoMirrored.Rounded.VolumeUp,
                    onClick = {
                        viewModel.speak()
                    },
                    type = ButtonType.PINK,
                    size = KidIconMedium,
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
                        isSmall = true,
                        onClick = { viewModel.previousSentence() }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // ➡️ RIGHT BUTTON
                    KidsActionButton(
                        text = stringResource(R.string.next),
                        icon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        type = if (viewModel.isAtLast) ButtonType.DISABLE else ButtonType.ORANGE,
                        isIconStart = false,
                        isSmall = true,
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

                    val modifier : Modifier = if (isTablet){
                        val screenHeight = with(LocalDensity.current) {
                            LocalWindowInfo.current.containerSize.height.toDp()
                        }
                        Modifier.size(screenHeight * 0.6f) // 60% of screen height
                    } else{
                        Modifier.aspectRatio(1f) // perfect square
                    }

                    // 🟩 CENTER IMAGE
                    getImageResForSentence(uiState.lessonData?.imageName)?.let { resId ->
                        Image(
                            painter = painterResource(resId),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = modifier
                                .clip(RoundedCornerShape(Dimens16))
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            // 🔵 FOOTER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Dimens16),
                contentAlignment = Alignment.Center
            ) {
                SentenceWordsView(
                    words = viewModel.words,
                    isJoined = uiState.isSentenceJoined,
                    speakingIndex = uiState.joinSentenceSpeakingIndex,
                    currentWordIndex = uiState.splitSentenceWordIndex
                )
            }
        }
    }
}
