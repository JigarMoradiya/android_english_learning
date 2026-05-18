package com.example.myapplication.main.age_group.from_5_to_7.opposite_words.activities

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.SwapHorizontalCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.min
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.common.AppToolbarDropDownOnRight
import com.example.myapplication.data.generation.loader.OppositeDifficulty
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.buttons.KidsActivityCard
import com.example.myapplication.ui.theme.AppDimens.Dimens16

@Composable
fun OppositeWordActivitiesPage(navController: NavController) {

    var selectedDifficulty by remember { mutableStateOf(OppositeDifficulty.EASY) }

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundUI(isGreenGrassShow = false)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            // Header: Back button + difficulty dropdown
            AppToolbarDropDownOnRight(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.opposite_word_activities),
                currentSelected = selectedDifficulty.name,
                modes = OppositeDifficulty.entries.map { it.name },
                onItemChange = {
                    val mode = OppositeDifficulty.entries.first { m -> m.name == it }
                    selectedDifficulty = mode
                },
                onBackClick = { navController.popBackStack() }
            )

            BoxWithConstraints(
                modifier = Modifier.fillMaxSize()
            ) {
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
                        title = "Match\nOpposite Words",
                        icon = Icons.Filled.SwapHorizontalCircle,
                        accentColor = Color(0xFF7B2D8B)
                    ) {
                        navController.navigate(RouteNavigation.MatchOpposites.matchOpposites(selectedDifficulty.name))
                    }
                    Spacer(Modifier.width(spacing))
                    KidsActivityCard(
                        size = size,
                        title = "Choose Correct\nOpposite",
                        icon = Icons.Filled.CheckCircle,
                        accentColor = Color(0xFFD35400),
                    ) {
                        navController.navigate(RouteNavigation.ChooseOpposite.chooseOpposite(selectedDifficulty.name))
                    }
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}
