package com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge.advanced

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.min
import androidx.navigation.NavController
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.buttons.KidsActivityCard
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground

@Composable
fun MixedGrammarAdvancedMenuPage(navController: NavController) {
    Box(Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.aquaGreen, shape = KidsFloatingShape.leaves)
        Column(
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            BackButtonWithText("Advanced", onBackClick = { navController.popBackStack() })
            BoxWithConstraints(Modifier.fillMaxSize()) {
                val spacing = Dimens16
                val cardWidth = (maxWidth - spacing * 8) / 3
                val cardHeight = maxHeight * 0.7f
                val size = min(cardWidth,cardHeight)
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.weight(1f))
                    KidsActivityCard(
                        size = size,
                        title = "Fix the\nSentence",
                        subtitle = "Correct the errors",
                        icon = Icons.Filled.ContentCut,
                        accentColor = Color(0xFFC62828)
                    ) {
                        navController.navigate(RouteNavigation.FixTheSentence.route)
                    }
                    Spacer(Modifier.width(spacing))
                    KidsActivityCard(
                        size = size,
                        title = "Sentence\nBuilder",
                        subtitle = "Build it right",
                        icon = Icons.Filled.PostAdd,
                        accentColor = Color(0xFF1565C0)
                    ) {
                        navController.navigate(RouteNavigation.GrammarSentenceBuilder.route)
                    }
                    Spacer(Modifier.width(spacing))
                    KidsActivityCard(
                        size = size,
                        title = "Fill the\nBlanks",
                        subtitle = "Complete the gaps",
                        icon = Icons.Filled.EditNote,
                        accentColor = Color(0xFF6A1B9A)
                    ) {
                        navController.navigate(RouteNavigation.GrammarFillTheBlanks.route)
                    }
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}