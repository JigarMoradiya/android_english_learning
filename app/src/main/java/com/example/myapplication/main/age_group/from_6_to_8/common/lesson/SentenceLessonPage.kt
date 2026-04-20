package com.example.myapplication.main.age_group.from_6_to_8.common.lesson

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.SentenceUnit
import com.example.myapplication.data.model.UnitSelectionScreen
import com.example.myapplication.main.age_group.from_6_to_8.common.StyledRow
import com.example.myapplication.main.age_group.from_6_to_8.common.lesson.view_model.SentenceLessonViewModel
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.getImageResFromWord
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled
import com.google.gson.Gson


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
                    .padding(horizontal = Dimens16)
                    .padding(bottom = Dimens8)
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(Dimens12)
            ) {

                items(viewModel.uiState.lessons) { item ->
                    val isCompleted = viewModel.isCompleted(item.id)

                    var rowHeight by remember { mutableStateOf(0) }

                    StyledRow(unlocked = true, modifier = Modifier, padding = 0.dp) {

                        // Image
                        getImageResFromWord(item.imageName)?.let { resId ->
                            if (rowHeight > 0) {
                                Image(
                                    painter = painterResource(resId),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .height(with(LocalDensity.current) { rowHeight.toDp() })
                                        .aspectRatio(1f) // square
                                        .clip(RoundedCornerShape(Dimens8,0.dp,0.dp,Dimens8))
                                )
                            }
                        }

                        // Inner Row (wrap height)
                        Row(
                            modifier = Modifier
                                .onSizeChanged {
                                    rowHeight = it.height // 👈 capture height
                                }
                                .padding(Dimens16),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                modifier = Modifier.weight(1f),
                                text = item.title,
                                color = Color.Black,
                                style = MaterialTheme.typography.titleSmall.scaled().copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )

                            if (isCompleted){
                                KidsActionButton(
                                    text = "Completed",
                                    icon = Icons.Filled.CheckCircle,
                                    type = ButtonType.GREEN,
                                    onClick = {},
                                    isSmall = true,
                                    modifier = Modifier.padding(end = Dimens8)
                                )
                            }

                            KidsActionButton(
                                text = if (isCompleted) stringResource(R.string.practice_again) else stringResource(R.string.do_practice),
                                icon = if (isCompleted) Icons.Filled.Replay else Icons.Filled.PlayArrow,
                                type = ButtonType.BLUE,
                                onClick = {
                                    navController.navigate(RouteNavigation.ReadAndListen.readAndListen(screenType.name, Gson().toJson(item)))
                                },
                                isSmall = true
                            )
                        }
                    }
                }
            }

        }
    }
}
