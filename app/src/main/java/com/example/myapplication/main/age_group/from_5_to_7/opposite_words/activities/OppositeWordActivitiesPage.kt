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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.min
import androidx.navigation.NavController
import com.example.myapplication.data.generation.loader.OppositeDifficulty
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.buttons.KidsActivityCard
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens40
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.extensions.scaled

@Composable
fun OppositeWordActivitiesPage(navController: NavController) {

    var selectedDifficulty by remember { mutableStateOf(OppositeDifficulty.EASY) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    val difficultyButtonType = when (selectedDifficulty) {
        OppositeDifficulty.EASY   -> ButtonType.GREEN
        OppositeDifficulty.MEDIUM -> ButtonType.ORANGE
        OppositeDifficulty.HARD   -> ButtonType.RED
    }

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundUI(isGreenGrassShow = false)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            // Header: Back button + difficulty dropdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButtonWithText(
                    title = "Opposite Activities",
                    modifier = Modifier.weight(1f),
                    onBackClick = { navController.popBackStack() }
                )
                Box(modifier = Modifier.padding(end = Dimens16)) {
                    KidsActionButton(
                        text = selectedDifficulty.name.lowercase().replaceFirstChar { it.uppercase() },
                        icon = Icons.Default.KeyboardArrowDown,
                        isIconStart = false,
                        type = difficultyButtonType,
                        isSmall = true,
                        onClick = { dropdownExpanded = true }
                    )
                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        OppositeDifficulty.entries.forEach { level ->
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(Dimens12),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = level.name.lowercase().replaceFirstChar { it.uppercase() },
                                            style = MaterialTheme.typography.bodyMedium.scaled(),
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        if (selectedDifficulty == level) {
                                            Text("✓", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                                        }
                                    }
                                },
                                onClick = {
                                    selectedDifficulty = level
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.weight(0.5f))

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
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
                        icon = Icons.Filled.SwapHoriz,
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

            Spacer(Modifier.weight(0.5f))
        }
    }
}
