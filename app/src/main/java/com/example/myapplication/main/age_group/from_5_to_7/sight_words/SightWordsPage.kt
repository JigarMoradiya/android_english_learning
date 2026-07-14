package com.example.myapplication.main.age_group.from_5_to_7.sight_words

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.main.age_group.from_5_to_7.sight_words.components.UseCaseCard
import com.example.myapplication.main.age_group.from_5_to_7.sight_words.view_model.SightWordsViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.buttons.KidsIconButton
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.SightWordFont
import com.example.myapplication.ui.theme.PrimaryBlue


@Composable
fun SightWordsPage(
    navController: NavController,
    viewModel: SightWordsViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState
    val isFirst = uiState.currentIndex == 0
    val isLast = uiState.currentIndex == uiState.words.lastIndex

    Box(modifier = Modifier.fillMaxSize()) {

        KidsGradientBackground(gradient = KidsGradient.tealCyan, shape = KidsFloatingShape.musicNotes)
        Column(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)) {
            val currentWord = viewModel.currentWord
            Row(Modifier.weight(1f)) {
                Column(
                    modifier = Modifier.weight(0.4f),
                ){
                    BackButtonWithText(
                        title = stringResource(R.string.sight_words),
                        onBackClick = { navController.popBackStack() }
                    )
                    Spacer(Modifier.weight(1f))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(Dimens20),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = currentWord.word,
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontSize = SightWordFont
                            ),
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(Dimens16)) {
                            KidsIconButton(
                                icon = Icons.AutoMirrored.Rounded.VolumeUp,
                                onClick = { viewModel.speak(currentWord.word) },
                                type = ButtonType.BLUE
                            )

                            KidsIconButton(
                                icon = Icons.Filled.Bolt,
                                onClick = { navController.navigate(RouteNavigation.SightWordSpeed.route) },
                                type = ButtonType.ORANGE
                            )
                        }

                    }
                    Spacer(Modifier.weight(1f))
                }


                // 🔹 RIGHT SIDE
                LazyColumn(
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxHeight().padding(top = Dimens16,end = Dimens16),
                    verticalArrangement = Arrangement.spacedBy(Dimens12)
                ) {

                    items(currentWord.useCases) { useCase ->

                        UseCaseCard(
                            description = useCase.description,
                            example = useCase.example,
                            currentWord = currentWord.word,
                            onSpeak = { viewModel.speak(it) }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .padding(start = DeviceInfo.screenHorizontalPadding(), end = Dimens16)
                    .padding(bottom = Dimens16, top = Dimens8),
                verticalAlignment = Alignment.Bottom
            ) {

                KidsActionButton(
                    text = stringResource(R.string.previous),
                    icon = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                    type = if (isFirst) ButtonType.DISABLE else ButtonType.ORANGE,
                    isSmall = true,
                    onClick = { viewModel.previousWord() }
                )

                Spacer(modifier = Modifier.weight(1f))

                KidsActionButton(
                    text = stringResource(R.string.next),
                    icon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    type = if (isLast) ButtonType.DISABLE else ButtonType.ORANGE,
                    isIconStart = false,
                    isSmall = true,
                    onClick = { viewModel.nextWord() }
                )
            }
        }
    }
}
