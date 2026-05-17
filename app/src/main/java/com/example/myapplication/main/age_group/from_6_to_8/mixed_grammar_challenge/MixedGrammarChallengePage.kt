package com.example.myapplication.main.age_group.from_6_to_8.mixed_grammar_challenge

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.buttons.KidsActivityCard
import com.example.myapplication.main.common.sheets.LocalAccessSheetViewModel
import kotlinx.coroutines.launch
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.extensions.scaled

@Composable
fun MixedGrammarChallengePage(navController: NavController) {
    val accessVM = LocalAccessSheetViewModel.current
    val scope = rememberCoroutineScope()

    Box(Modifier.fillMaxSize()) {
        BackgroundUI(isGreenGrassShow = false)
        Column(
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            BackButtonWithText("Grammar Challenge", onBackClick = { navController.popBackStack() })

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
                    // Beginner: FREE_LIMITED — 3/day guest, 5/day login, unlimited premium
                    KidsActivityCard(
                        size = size,
                        title = "Beginner",
                        subtitle = "Start simple",
                        icon = Icons.Filled.Star,
                        accentColor = Color(0xFF2E7D32)
                    ) {
                        scope.launch {
                            val allowed = accessVM.checkAccess(ModuleID.GRAMMAR_CHALLENGE_BEGINNER)
                            if (allowed) navController.navigate(RouteNavigation.MixedGrammarBeginner.route)
                        }
                    }
                    Spacer(Modifier.width(spacing))
                    // Medium: PREMIUM
                    KidsActivityCard(
                        size = size,
                        title = "Medium",
                        subtitle = "Step it up",
                        icon = Icons.Filled.Whatshot,
                        accentColor = Color(0xFFE65100)
                    ) {
                        scope.launch {
                            val allowed = accessVM.checkAccess(ModuleID.GRAMMAR_CHALLENGE_MEDIUM)
                            if (allowed) navController.navigate(RouteNavigation.DragToGrammarBucket.route)
                        }
                    }
                    Spacer(Modifier.width(spacing))
                    // Advanced: PREMIUM
                    KidsActivityCard(
                        size = size,
                        title = "Advanced",
                        subtitle = "Master level",
                        icon = Icons.Filled.EmojiEvents,
                        accentColor = Color(0xFF6A1B9A)
                    ) {
                        scope.launch {
                            val allowed = accessVM.checkAccess(ModuleID.GRAMMAR_CHALLENGE_ADVANCED)
                            if (allowed) navController.navigate(RouteNavigation.MixedGrammarAdvanced.route)
                        }
                    }
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}


