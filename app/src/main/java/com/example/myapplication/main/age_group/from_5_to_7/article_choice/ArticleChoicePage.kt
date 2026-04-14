package com.example.myapplication.main.age_group.from_5_to_7.article_choice

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_5_to_7.article_choice.view_model.ArticleChoiceViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.buttons.KidsOptionButton
import com.example.myapplication.main.common.getImageResFromWord
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.articleChoiceHeight
import com.example.myapplication.ui.theme.AppDimens.articleChoiceImageHeight
import com.example.myapplication.ui.theme.AppDimens.articleChoiceWidth
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.ui.theme.PrimaryGreen


@Composable
fun ArticleChoicePage(
    navController: NavController,
    viewModel: ArticleChoiceViewModel = hiltViewModel()
) {

    val state = viewModel.uiState.collectAsState().value
    val style = MaterialTheme.typography.titleLarge.copy(
        fontSize = articleChoiceHeight.value.sp * 0.8
    )
    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundUI(isGreenGrassShow = false)
        Column(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)) {
            BackButtonWithText(
                title = stringResource(R.string.article_choice),
                onBackClick = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens24),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Spacer(modifier = Modifier.weight(1f))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimens16)
                ) {

                    // 🔹 IMAGE
                    state.currentImageName?.let { rawName ->
                        val imageName = rawName.replace(" ", "")

                        val res = getImageResFromWord(imageName)

                        res?.let {
                            Image(
                                painter = painterResource(id = res),
                                contentDescription = null,
                                modifier = Modifier
                                    .height(articleChoiceImageHeight)
                            )
                        }
                    }

                    // 🔹 ARTICLE + WORD
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Dimens8)
                    ) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            // Selected OR Placeholder
                            Text(
                                text = state.selectedAnswer?.replaceFirstChar { it.lowercase() } ?: "an",
                                style = style,
                                color = when {
                                    state.selectedAnswer == null -> Color.Transparent
                                    state.isAnswerCorrect -> PrimaryGreen
                                    else -> Color.Red
                                },
                                fontWeight = FontWeight.Bold
                            )

                            // Underline
                            Box(
                                modifier = Modifier
                                    .width(articleChoiceWidth * 0.75f)
                                    .height(Dimens2)
                                    .background(Color.Black)
                            )
                        }

                        Spacer(Modifier.width(Dimens4))

                        Text(
                            text = state.currentWord.lowercase(),
                            style = style,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // 🔹 OPTIONS
                    Row(horizontalArrangement = Arrangement.spacedBy(Dimens16)) {

                        KidsOptionButton(
                            text = "a",
                            type = ButtonType.OPTIONS,
                            fontSize = articleChoiceHeight.value.sp * 0.6,
                            onClick = {
                                if (state.selectedAnswer == null){
                                    viewModel.checkAnswer("a")
                                }
                            },
                            modifier = Modifier
                                .width(articleChoiceWidth)
                                .height(articleChoiceHeight),

                        )
                        KidsOptionButton(
                            text = "an",
                            type = ButtonType.OPTIONS,
                            fontSize = articleChoiceHeight.value.sp * 0.6,
                            onClick = {
                                if (state.selectedAnswer == null){
                                    viewModel.checkAnswer("an")
                                }
                            },
                            modifier = Modifier
                                .width(articleChoiceWidth)
                                .height(articleChoiceHeight),

                        )
                    }

                    // 🔹 FEEDBACK
                    Text(
                        text = state.feedbackText ?: " ",
                        color = when {
                            state.feedbackText == null -> Color.Transparent
                            state.isAnswerCorrect -> PrimaryGreen
                            else -> Color.Red
                        },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                KidsActionButton(
                    text = stringResource(R.string.next),
                    icon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    type = if (state.selectedAnswer == null) ButtonType.DISABLE else ButtonType.ORANGE,
                    isIconStart = false,
                    onClick = {
                        if (state.selectedAnswer != null){
                            viewModel.loadNextWord()
                        }
                    }
                )

                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
