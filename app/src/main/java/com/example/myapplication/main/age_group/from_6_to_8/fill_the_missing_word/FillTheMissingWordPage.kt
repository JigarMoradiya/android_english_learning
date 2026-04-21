package com.example.myapplication.main.age_group.from_6_to_8.fill_the_missing_word

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.model.ReadSentenceItemNew
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.UnitSelectionScreen
import com.example.myapplication.main.age_group.from_6_to_8.fill_the_missing_word.view_model.FillTheMissingWordViewModel
import com.example.myapplication.main.age_group.from_6_to_8.one_word_answer.components.ResultView
import com.example.myapplication.main.age_group.from_6_to_8.one_word_answer.view_model.OneWordAnswerViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.buttons.KidsLabel
import com.example.myapplication.main.common.buttons.KidsOptionButton
import com.example.myapplication.main.common.getImageResForSentence
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.listenAndAnswerOptionsHeight
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled
import kotlin.collections.chunked
import kotlin.collections.forEach
import kotlin.text.replaceFirstChar
@Composable
fun FillTheMissingWordPage(
    screenType : UnitSelectionScreen,
    lessonData: ReadSentenceItemNew,
    navController: NavController,
    viewModel: FillTheMissingWordViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.setScreenTypeAndLessonData(screenType,lessonData)
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
                    title = stringResource(R.string.fillTheMissingWord),
                    modifier = Modifier.weight(1f),
                    onBackClick = { navController.popBackStack() }
                )

                Spacer(Modifier.weight(1f))

                KidsLabel("Question ${uiState.currentIndex + 1} / ${uiState.questions.size}",)
            }


            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens16),
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = Dimens16).padding(bottom = Dimens16)
                ) {

                    // 🟩 CENTER IMAGE
                    getImageResForSentence(uiState.lessonData?.imageName)?.let { resId ->
                        Image(
                            painter = painterResource(resId),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .aspectRatio(1f) // perfect square
                                .clip(RoundedCornerShape(Dimens16))
                        )
                    }

                    if (uiState.showResult){
                        ResultView(uiState.score,uiState.questions.size){
                            navController.popBackStack()
                        }
                    }else{
                        Column(modifier = Modifier.weight(1f),verticalArrangement = Arrangement.spacedBy(Dimens16)) {
                            uiState.currentQuestion?.let{ currentQuestion ->

                                Text(
                                    text = uiState.displaySentence,
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = Dimens2),
                                    style = MaterialTheme.typography.headlineLarge.scaled(),
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )

                                // Split list into rows of 2
                                uiState.options.chunked(2).forEach { rowItems ->

                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(Dimens8),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        rowItems.forEach { word ->

                                            KidsOptionButton(
                                                text = word.replaceFirstChar { it.uppercase() },
                                                type = viewModel.backgroundType(word),
                                                fontSize = listenAndAnswerOptionsHeight.value.sp * 0.45,
                                                onClick = {
                                                    viewModel.selectAnswer(word)
                                                },
                                                enabled = uiState.selectedAnswer == null,
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .height(listenAndAnswerOptionsHeight)
                                            )
                                        }

                                        // 👇 Important: if odd items (safety)
                                        if (rowItems.size == 1) {
                                            Spacer(modifier = Modifier.weight(1f) )
                                        }
                                    }
                                }

                                Row {
                                    Spacer(Modifier.weight(1f))
                                    KidsActionButton(
                                        text = stringResource(R.string.next),
                                        icon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                        type = if (uiState.selectedAnswer == null) ButtonType.DISABLE else ButtonType.ORANGE,
                                        isIconStart = false,
                                        onClick = {
                                            if (uiState.selectedAnswer != null){
                                                viewModel.nextQuestion()
                                            }
                                        }
                                    )
                                }
                            }

                        }
                    }
                }
            }

        }
    }
}