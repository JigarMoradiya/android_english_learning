package com.example.myapplication.main.age_group.from_5_to_7.opposite_words

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.common_ui.lesson.GrammarExplanationSection
import com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.common_ui.lesson.GrammarLessonHeader
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens8

private val explanationText0 = """
<font color='#EE0000'><b>What are Opposites?</b></font>
Opposite words have completely different meanings.
👉 They show the reverse meaning of another word.
We use opposite words every day!
""".trimIndent()

private val explanationText1 = """
<font color='#EE0000'><b>Examples around you:</b></font>
big - small
hot - cold
day - night
up - down
happy - sad
""".trimIndent()

private val explanationText2 = """
<font color='#EE0000'><b>Common Opposites:</b></font>
<b>Size 📏</b>  big - small, tall - short
<b>Temperature 🌡️</b>  hot - cold
<b>Feelings 😊</b>  happy - sad, excited - tired
<b>Direction ⬆️</b>  up - down, left - right
""".trimIndent()

private val explanationText3 = """
<font color='#EE0000'><b>Easy Trick 💡</b></font>
Ask yourself: What means the opposite?

Example:
hot → cold
big → small
open → close
""".trimIndent()

private val explanationText4 = """
<font color='#EE0000'><b>Quick Quiz:</b></font>
big → ? <b>small</b>
happy → ? <b>sad</b>
day → ? <b>night</b>
""".trimIndent()

@Composable
fun OppositeWordsPage(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundUI(isGreenGrassShow = false)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            GrammarLessonHeader(
                title = "Opposite Words",
                onBackClick = { navController.popBackStack() },
                onPracticeClick = { navController.navigate(RouteNavigation.OppositeWordActivities.route) }
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Dimens16),
                horizontalArrangement = Arrangement.spacedBy(Dimens16)
            ) {
                // Left column
                LazyColumn(
                    modifier = Modifier.weight(0.4f),
                    verticalArrangement = Arrangement.spacedBy(Dimens16),
                    contentPadding = PaddingValues(vertical = Dimens8)
                ) {
                    item { GrammarExplanationSection(explanationText = explanationText0) }
                    item { GrammarExplanationSection(explanationText = explanationText1) }
                    item { GrammarExplanationSection(explanationText = explanationText2) }
                }

                // Right column
                LazyColumn(
                    modifier = Modifier.weight(0.6f),
                    verticalArrangement = Arrangement.spacedBy(Dimens16),
                    contentPadding = PaddingValues(vertical = Dimens8)
                ) {
                    item { GrammarExplanationSection(explanationText = explanationText3) }
                    item { GrammarExplanationSection(explanationText = explanationText4) }
                }
            }
        }
    }
}
