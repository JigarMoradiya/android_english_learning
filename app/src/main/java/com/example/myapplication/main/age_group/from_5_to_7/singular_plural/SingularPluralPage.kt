package com.example.myapplication.main.age_group.from_5_to_7.singular_plural

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
<font color='#EE0000'><b>Singular &amp; Plural</b></font>
Singular = ONE thing. Plural = MORE than one.
👉 Most words just add -s or -es!
""".trimIndent()

private val explanationText1 = """
<font color='#EE0000'><b>Add -s rule:</b></font>
dog → dogs
cat → cats
book → books
bird → birds
""".trimIndent()

private val explanationText2 = """
<font color='#EE0000'><b>Add -es rule:</b></font>
Words ending in s, x, z, ch, sh:
bus → buses
box → boxes
bunch → bunches
""".trimIndent()

private val explanationText3 = """
<font color='#EE0000'><b>Irregular Plurals:</b></font>
Some words change completely!
child → children
man → men
woman → women
mouse → mice
foot → feet
tooth → teeth
""".trimIndent()

private val explanationText4 = """
<font color='#EE0000'><b>Quick Quiz:</b></font>
cat → ? <b>cats</b>
church → ? <b>churches</b>
child → ? <b>children</b>
man → ? <b>men</b>
""".trimIndent()

@Composable
fun SingularPluralPage(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundUI(isGreenGrassShow = false)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            GrammarLessonHeader(
                title = "Singular & Plural",
                onBackClick = { navController.popBackStack() },
                onPracticeClick = { navController.navigate(RouteNavigation.SingularPluralActivities.route) }
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Dimens16),
                horizontalArrangement = Arrangement.spacedBy(Dimens16)
            ) {
                // Left column — 40%
                LazyColumn(
                    modifier = Modifier.weight(0.4f),
                    verticalArrangement = Arrangement.spacedBy(Dimens16),
                    contentPadding = PaddingValues(vertical = Dimens8)
                ) {
                    item { GrammarExplanationSection(explanationText = explanationText0) }
                    item { GrammarExplanationSection(explanationText = explanationText1) }
                    item { GrammarExplanationSection(explanationText = explanationText2) }
                }

                // Right column — 60%
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
