package com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.pronouns.lesson

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.common_ui.lesson.GrammarExamplesSection
import com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.common_ui.lesson.GrammarExplanationSection
import com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.common_ui.lesson.GrammarLessonHeader
import com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.noun.lesson.view_model.NounLessonViewModel
import com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.pronouns.lesson.view_model.PronounLessonViewModel
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.nounLessonImagesDimension
import com.example.myapplication.ui.theme.AppDimens.nounLessonImagesDimensionNoPadding

@Composable
fun PronounLessonPage(
    navController: NavController,
    viewModel: PronounLessonViewModel = hiltViewModel()
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
                title = stringResource(R.string.pronoun_title),
                onBackClick = {
                    navController.popBackStack()
                },
                onPracticeClick = {
//                    navController.navigate(RouteNavigation.GrammarBasicPronounsPractice.route)
                }
            )

            Spacer(modifier = Modifier.height(Dimens16))

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

                // Explanation
                item {
                    GrammarExplanationSection(explanationText = uiState.explanationText)
                }

                item { Spacer(modifier = Modifier.height(Dimens16)) }

                // Examples
                item {
                    GrammarExamplesSection(
                        examples = uiState.examples,
                        cardColor = Color(0xFFD62828),
                        size =  nounLessonImagesDimensionNoPadding,
                        padding = 0.dp,
                        onExampleClick = {
                            viewModel.onExampleTapped(it)
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(Dimens20))
                }
            }
        }
    }
}
