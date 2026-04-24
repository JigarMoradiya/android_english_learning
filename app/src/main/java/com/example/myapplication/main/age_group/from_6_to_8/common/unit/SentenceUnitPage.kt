package com.example.myapplication.main.age_group.from_6_to_8.common.unit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.common.AppToolbarDropDownOnRight
import com.example.myapplication.data.model.SentenceLevel
import com.example.myapplication.data.model.UnitSelectionScreen
import com.example.myapplication.main.age_group.from_6_to_8.common.StyledColumn
import com.example.myapplication.main.age_group.from_6_to_8.common.unit.view_model.SentenceUnitViewModel
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.PrimaryBlue
import com.example.myapplication.ui.theme.PrimaryGreen
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.extensions.scaled


@Composable
fun SentenceUnitPage(
    screenType : UnitSelectionScreen,
    navController: NavController,
    viewModel: SentenceUnitViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.setScreenType(screenType)
    }
    val hideProgressFor = setOf(
        UnitSelectionScreen.MATCH_THE_PICTURE,
        UnitSelectionScreen.WHICH_SENTENCE_SOUNDS_RIGHT,
        UnitSelectionScreen.FIND_THE_CORRECT_WRITING,
        UnitSelectionScreen.SENTENCE_CHECK,
        UnitSelectionScreen.BUILD_THE_SENTENCE
    )

    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundUI(isGreenGrassShow = false)

        Column(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)) {

            if (screenType in hideProgressFor){
                BackButtonWithText(
                    title = stringResource(R.string.list_of_chapters),
                    onBackClick = { navController.popBackStack() }
                )
            }else{
                AppToolbarDropDownOnRight(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.list_of_chapters),
                    currentSelected = uiState.level.title,
                    modes = SentenceLevel.entries.map { it.title },
                    onItemChange = {
                        val level = SentenceLevel.entries.first { m -> m.title == it }
                        viewModel.changeLevel(level)
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }

            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = Dimens16)
                    .padding(bottom = Dimens8)
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(Dimens12)
            ) {

                items(uiState.sentenceUnitsList) { item ->
                    StyledColumn(
                        unlocked = true,
                        modifier = Modifier
                            .clip(RoundedCornerShape(Dimens12)) // 👈 IMPORTANT
                            .clickable {
                                AudioPlayerManager.playSoundMenuClick()
                                when (screenType) {
                                    UnitSelectionScreen.MATCH_THE_PICTURE -> {
                                        navController.navigate(RouteNavigation.MatchThePicture.matchThePicture(item.unit.name, uiState.level.name))
                                    }
                                    UnitSelectionScreen.WHICH_SENTENCE_SOUNDS_RIGHT -> {
                                        navController.navigate(RouteNavigation.WhichSentenceSoundRight.whichSentenceSoundRight(item.unit.name, uiState.level.name))
                                    }
                                    UnitSelectionScreen.FIND_THE_CORRECT_WRITING -> {
                                        navController.navigate(RouteNavigation.FindTheCorrectWriting.findTheCorrectWriting(item.unit.name, uiState.level.name))
                                    }
                                    UnitSelectionScreen.SENTENCE_CHECK -> {
                                        navController.navigate(RouteNavigation.SentenceCheck.sentenceCheck(item.unit.name, uiState.level.name))
                                    }
                                    UnitSelectionScreen.BUILD_THE_SENTENCE -> {

                                    }
                                    else -> {
                                        navController.navigate(RouteNavigation.SentenceLessonList.sentenceLessonList(screenType.name, item.unit.name, uiState.level.name))
                                    }
                                }
                            }
                    ) {
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
                                    text = stringResource(R.string.progress),
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

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(Dimens6)
                                    .clip(RoundedCornerShape(50))
                                    .background(Color.LightGray.copy(alpha = 0.6f))
                            ) {
                                if (progressValue > 0f) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(progressValue)
                                            .fillMaxHeight()
                                            .background(PrimaryGreen)
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
