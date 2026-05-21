package com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.noun.lesson

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.common_ui.lesson.GrammarExamplesSection
import com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.common_ui.lesson.GrammarExplanationSection
import com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.common_ui.lesson.GrammarLessonHeader
import com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.noun.lesson.view_model.NounLessonViewModel
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.nounLessonImagesDimension
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground

@Composable
fun NounLessonPage(
    navController: NavController,
    viewModel: NounLessonViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        KidsGradientBackground(gradient = KidsGradient.tealCyan, shape = KidsFloatingShape.musicNotes)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {

            // Header
            GrammarLessonHeader(
                title = stringResource(R.string.noun_title),
                onBackClick = {
                    navController.popBackStack()
                },
                onPracticeClick = {
                    navController.navigate(RouteNavigation.GrammarBasicNounPractice.route)
                }
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Dimens16),
                contentPadding = PaddingValues(start = Dimens16, end = Dimens16, bottom = Dimens16, top = Dimens8)
            ) {

                // Explanation 1
                item {
                    GrammarExplanationSection(explanationText = uiState.explanationText1)
                }

                // Examples
                item {
                    GrammarExamplesSection(
                        examples = uiState.examples,
                        cardColor = Color(0xFF237227),
                        size =  nounLessonImagesDimension * 0.8f,
                        padding = Dimens8 * 0.8f,
                        onExampleClick = {
                            viewModel.onExampleTapped(it)
                        }
                    )
                }

                // Explanation 2
                item {
                    GrammarExplanationSection(explanationText = uiState.explanationText2)
                }

                // Explanation 3 and 4
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        GrammarExplanationSection(
                            explanationText = uiState.explanationText3,
                            modifier = Modifier.weight(1f)
                        )

                        GrammarExplanationSection(
                            explanationText = uiState.explanationText4,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
