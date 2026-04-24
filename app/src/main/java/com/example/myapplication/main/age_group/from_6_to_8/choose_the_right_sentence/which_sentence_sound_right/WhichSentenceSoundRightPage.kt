package com.example.myapplication.main.age_group.from_6_to_8.choose_the_right_sentence.which_sentence_sound_right

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.SentenceUnit
import com.example.myapplication.main.age_group.from_6_to_8.choose_the_right_sentence.which_sentence_sound_right.view_model.WhichSentenceSoundRightViewModel
import com.example.myapplication.main.age_group.from_6_to_8.common.ResultView
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.buttons.KidsLabel
import com.example.myapplication.main.common.buttons.KidsOptionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.listenAndAnswerOptionsHeight
import com.example.myapplication.ui.theme.ButtonType


@Composable
fun WhichSentenceSoundRightPage(
    unit : SentenceUnit,
    level : SentenceLevel,
    navController: NavController,
    viewModel: WhichSentenceSoundRightViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.setData(unit,level)
    }
    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundUI(isGreenGrassShow = false)
        Column(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)) {
            // HEADER
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                BackButtonWithText(
                    title = stringResource(R.string.which_sentence_sounds_right),
                    modifier = Modifier.weight(1f),
                    onBackClick = { navController.popBackStack() }
                )

                Spacer(Modifier.weight(1f))

                KidsLabel("Question ${uiState.currentIndex + 1} / ${uiState.questions.size}",)
            }

            Spacer(Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens16),
                modifier = Modifier.fillMaxSize()
                    .padding(horizontal = Dimens16).padding(bottom = Dimens16)
            ) {

                if (uiState.showResult){
                    ResultView(uiState.score,uiState.questions.size, onBack = {
                        navController.popBackStack()
                    },onContinue = {
                        viewModel.restart()
                    })
                }else{
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Dimens12)
                    ) {
                        uiState.options.forEach { word ->

                            KidsOptionButton(
                                text = word.replaceFirstChar { it.uppercase() },
                                type = viewModel.backgroundType(word),
                                fontSize = listenAndAnswerOptionsHeight.value.sp * 0.37,
                                onClick = {
                                    viewModel.selectAnswer(word)
                                },
                                enabled = uiState.selectedAnswer == null,
                                modifier = Modifier
                                    .fillMaxWidth() // 🔥 full width
                                    .height(listenAndAnswerOptionsHeight),
                                textAlign = TextAlign.Left
                            )
                        }

                        // 👉 Next Button Row
                        Row {
                            Spacer(Modifier.weight(1f))

                            KidsActionButton(
                                text = stringResource(R.string.next),
                                icon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                type = if (uiState.selectedAnswer == null)
                                    ButtonType.DISABLE else ButtonType.ORANGE,
                                isIconStart = false,
                                onClick = {
                                    if (uiState.selectedAnswer != null) {
                                        viewModel.next()
                                    }
                                }
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.weight(1f))
        }
    }
}
