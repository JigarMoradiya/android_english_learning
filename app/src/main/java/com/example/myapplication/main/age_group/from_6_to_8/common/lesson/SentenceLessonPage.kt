package com.example.myapplication.main.age_group.from_6_to_8.common.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
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
import com.example.myapplication.data.model.SentenceUnit
import com.example.myapplication.data.model.UnitSelectionScreen
import com.example.myapplication.main.age_group.from_6_to_8.common.lesson.view_model.SentenceLessonViewModel
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens1
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled


@Composable
fun SentenceLessonPage(
    screenType : UnitSelectionScreen,
    unit : SentenceUnit,
    level : SentenceLevel,
    navController: NavController,
    viewModel: SentenceLessonViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.setScreenTypeAndUnit(screenType,unit,level)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundUI(isGreenGrassShow = false)

        Column(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)) {

            BackButtonWithText(
                title = stringResource(R.string.lessons_of, viewModel.unitTitle),
                onBackClick = { navController.popBackStack() }
            )

            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = Dimens16, vertical = Dimens8)
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(Dimens12)
            ) {

                items(viewModel.uiState.lessons) { item ->
                    val isCompleted = viewModel.isCompleted(item.id)
                    StyledColumn(unlocked = true) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = "\uD83D\uDCD8 ${item.title}",
                                color = Color.Black,
                                style = MaterialTheme.typography.titleSmall.scaled().copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )

                            if (isCompleted){
                                KidsActionButton(
                                    text = "Completed",
                                    icon = Icons.AutoMirrored.Filled.List,
                                    type = ButtonType.GREEN,
                                    onClick = {},
                                    isSmall = true
                                )
                            }

                            KidsActionButton(
                                text = if (isCompleted) stringResource(R.string.practice_again) else stringResource(R.string.do_practice),
                                icon = if (isCompleted) Icons.Filled.Replay else Icons.Filled.PlayArrow,
                                type = ButtonType.BLUE,
                                onClick = { },
                                isSmall = true
                            )
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
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
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