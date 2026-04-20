package com.example.myapplication.main.age_group.from_6_to_8.common.unit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.common.AppToolbarDropDownOnRight
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.UnitSelectionScreen
import com.example.myapplication.main.age_group.from_6_to_8.common.unit.view_model.SentenceUnitViewModel
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.ui.theme.AppDimens.Dimens1
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.PrimaryBlue
import com.example.myapplication.utils.extensions.scaled


@Composable
fun SentenceUnitPage(
    screenType : UnitSelectionScreen,
    navController: NavController,
    viewModel: SentenceUnitViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.setScreenType(screenType)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundUI(isGreenGrassShow = false)

        Column(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)) {

            AppToolbarDropDownOnRight(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.list_of_chapters),
                currentSelected = viewModel.uiState.level.title,
                modes = SentenceLevel.entries.map { it.title },
                onItemChange = {
                    val level = SentenceLevel.entries.first { m -> m.title == it }
                    viewModel.changeLevel(level)
                },
                onBackClick = { navController.popBackStack() }
            )


            LazyColumn(
                modifier = Modifier.padding(horizontal = Dimens16, vertical = Dimens8)
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(Dimens12)
            ) {

                items(viewModel.uiState.sentenceUnitsList) { item ->
                    StyledColumn(unlocked = true, modifier = Modifier.clickable {
                        navController.navigate(RouteNavigation.SentenceLessonList.sentenceLessonList(screenType.name,item.unit.name,viewModel.uiState.level.name))
                    }) {
                        Text(
                            text = item.title,
                            color = Color.Black,
                            style = MaterialTheme.typography.titleSmall.scaled().copy(
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Text(
                            text = item.description,
                            color = Color.DarkGray,
                            style = MaterialTheme.typography.labelMedium.scaled().copy(
                                fontWeight = FontWeight.Normal
                            )
                        )

                        val hideProgressFor = setOf(
                            UnitSelectionScreen.MATCH_THE_PICTURE,
                            UnitSelectionScreen.WHICH_SENTENCE_SOUNDS_RIGHT,
                            UnitSelectionScreen.FIND_THE_CORRECT_WRITING,
                            UnitSelectionScreen.SENTENCE_CHECK,
                            UnitSelectionScreen.BUILD_THE_SENTENCE
                        )

                        if (screenType !in hideProgressFor) {

                            Spacer(modifier = Modifier.height(Dimens12))

                            val lessonCounts = viewModel.getLessonCounts(item.unit)
                            val completed = lessonCounts.first
                            val total = lessonCounts.second
                            // 📊 Header Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Progress",
                                    style = MaterialTheme.typography.titleSmall.scaled(),
                                    fontWeight = FontWeight.Medium,
                                    color = PrimaryBlue // replace with your color
                                )

                                Text(
                                    text = "$completed / $total",
                                    style = MaterialTheme.typography.titleSmall.scaled(),
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryBlue
                                )
                            }

                            Spacer(modifier = Modifier.height(Dimens8))

                            val progressValue = if (total > 0) completed / total.toFloat() else 0f

                            if (progressValue > 0f) {
                                LinearProgressIndicator(
                                    progress = { progressValue },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(Dimens6),
                                    color = Color(0xFF2E7D32),
                                    trackColor = Color.LightGray.copy(alpha = 0.6f)
                                )
                            } else {
                                // Show only track (no green dot)
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(Dimens6)
                                        .background(
                                            Color.LightGray.copy(alpha = 0.6f),
                                            shape = RoundedCornerShape(50)
                                        )
                                )
                            }
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun StyledColumn(
    unlocked: Boolean,
    modifier: Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()

            // ✨ Scale effect
            .graphicsLayer {
                scaleX = if (unlocked) 1f else 0.95f
                scaleY = if (unlocked) 1f else 0.95f
            }
            // 🎨 Background
            .background(
                brush = if (unlocked) {
                    Brush.linearGradient(
                        colors = listOf(
                            Color.Yellow.copy(alpha = 0.2f),
                            Color(0xFFFFA500).copy(alpha = 0.2f)
                        )
                    )
                } else {
                    Brush.linearGradient( // 👈 use brush instead of SolidColor
                        colors = listOf(
                            Color.Gray.copy(alpha = 0.15f),
                            Color.Gray.copy(alpha = 0.15f)
                        )
                    )
                },
                shape = RoundedCornerShape(Dimens12)
            )

            // 🧱 Border
            .border(
                width = if (unlocked) Dimens1 else 0.dp,
                color = if (unlocked) Color(0xFFFFA500).copy(alpha = 0.5f) else Color.Transparent,
                shape = RoundedCornerShape(Dimens12)
            )

            // 🔒 Locked opacity
            .alpha(if (unlocked) 1f else 0.6f)
            .padding(Dimens16) // inner padding like SwiftUI
    ) {
        content()
    }
}