package com.example.myapplication.main.age_group.from_5_to_7.opposite_words

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.model.GrammarExampleModel
import com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.adjectives.lesson.view_model.AdjectivesLessonViewModel
import com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.common_ui.lesson.GrammarExamplesSection
import com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.common_ui.lesson.GrammarExplanationSection
import com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.common_ui.lesson.GrammarLessonHeader
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.nounLessonImagesDimension


@Composable
fun OppositeWordsPage(
    navController: NavController,
    viewModel: OppositeWordViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundUI(false)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {

            // Header
            GrammarLessonHeader(
                title = stringResource(R.string.opposite_words),
                onBackClick = {
                    navController.popBackStack()
                },
                onPracticeClick = {
                    navController.navigate(RouteNavigation.OppositeWordActivities.route)
                }
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Dimens16),
                contentPadding = PaddingValues(start = Dimens16, end = Dimens16, bottom = Dimens16, top = Dimens8)
            ) {

                // Explanation 0
                item {
                    GrammarExplanationSection(explanationText = uiState.explanationText0)
                }

                    // Explanation 1
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Dimens16),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(0.4f), verticalArrangement = Arrangement.spacedBy(Dimens16)) {
                            GrammarExplanationSection(explanationText = uiState.explanationText1)
                            GrammarExplanationSection(explanationText = uiState.explanationText2)
                        }

                        Column(modifier = Modifier.weight(0.6f), verticalArrangement = Arrangement.spacedBy(Dimens16)) {
                            GrammarExamplesSection(
                                examples = uiState.examples,
                                cardColor = Color(0xFFFF9D00),
                                size = nounLessonImagesDimension * 1.2f,
                                padding = Dimens8,
                                isSmallText = true,
                                onExampleClick = {
                                    viewModel.onExampleTapped(it)
                                },
                            )

                            GrammarExplanationSection(explanationText = uiState.explanationText3)

                            GrammarExplanationSection(explanationText = uiState.explanationText4)
                        }
                    }
                }
            }
        }
    }
}
