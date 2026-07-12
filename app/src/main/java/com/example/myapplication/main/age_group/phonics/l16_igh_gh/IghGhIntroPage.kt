package com.example.myapplication.main.age_group.phonics.l16_igh_gh

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.l16_igh_gh.view_model.ighGhGroups
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.PhonicsIntroBtnConfig
import com.example.myapplication.main.common.PhonicsIntroRightPanel
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens14
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled

private val ighAccentColor = Color(0xFF311B92)

@Composable
fun IghGhIntroPage(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.indigoPurple, shape = KidsFloatingShape.stars)

        BoxWithConstraints(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            val screenH = maxHeight

            Row(modifier = Modifier.fillMaxSize()) {

                // ── LEFT ─────────────────────────────────────────────────────
                Column(
                    modifier = Modifier
                        .weight(0.54f)
                        .fillMaxHeight()
                ) {
                    BackButtonWithText(title = "Level 16", onBackClick = { navController.popBackStack() })

                    Column(

                        modifier = Modifier

                            .weight(1f)

                            .verticalScroll(rememberScrollState())

                            .padding(horizontal = Dimens20, vertical = Dimens10),

                        verticalArrangement = Arrangement.spacedBy(Dimens14, Alignment.CenterVertically)

                    ) {
                        Text(
                            text = "igh & gh Patterns",
                            style = MaterialTheme.typography.headlineLarge.scaled(),
                            fontWeight = FontWeight.ExtraBold,
                            color = ighAccentColor
                        )
                        Text(
                            text = "The Ghost Letters! 👻",
                            style = MaterialTheme.typography.titleMedium.scaled(),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5E35B1)
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(Dimens8),
                            verticalArrangement = Arrangement.spacedBy(Dimens8)
                        ) {
                            ighGhGroups.forEach { group ->
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .background(group.accentColor.copy(alpha = 0.12f), RoundedCornerShape(50))
                                        .padding(horizontal = Dimens10, vertical = Dimens6)
                                ) {
                                    Text(
                                        text = "${group.emoji} ${group.pattern}",
                                        style = MaterialTheme.typography.labelLarge.scaled(),
                                        fontWeight = FontWeight.ExtraBold,
                                        color = group.accentColor
                                    )
                                }
                            }
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(Dimens8)) {
                            IghGhIntroRow("🌙", "igh = /aɪ/ — night, light, high")
                            IghGhIntroRow("👻", "Silent gh — though, thought, dough")
                            IghGhIntroRow("🎺", "gh = /f/ — enough, laugh, cough")
                        }
                    }
                }

                // ── RIGHT ────────────────────────────────────────────────────
                PhonicsIntroRightPanel(
                    screenHeight = screenH,
                    strokeColor  = ighAccentColor,
                    title        = "g and h love\nhiding together!",
                    titleColor   = ighAccentColor,
                    descLine1    = "igh · silent gh · gh = /f/",
                    descLine2    = "Three tricks, one magic pair of letters",
                    learnButton  = PhonicsIntroBtnConfig(
                        text  = "Learn Words",
                        icon  = Icons.Default.MenuBook,
                        type  = ButtonType.PURPLE,
                        onClick = { navController.navigate(RouteNavigation.IghGhLearn.route) }
                    ),
                    practiceButton = PhonicsIntroBtnConfig(
                        text  = "Practice",
                        icon  = Icons.Default.CheckCircle,
                        type  = ButtonType.BLUE,
                        onClick = { navController.navigate(RouteNavigation.IghGhPractice.route) }
                    ),
                    listenButton = PhonicsIntroBtnConfig(
                        text  = "Listen",
                        icon  = Icons.Default.Hearing,
                        type  = ButtonType.TEAL,
                        onClick = { navController.navigate(RouteNavigation.PhonicsListen.createRoute("ighGh")) }
                    ),
                    modifier = Modifier.weight(0.46f).fillMaxHeight()
                )
            }
        }
    }
}

@Composable
private fun IghGhIntroRow(emoji: String, text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens10),
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(text = emoji, style = MaterialTheme.typography.titleMedium.scaled())
        Text(
            text  = text,
            style = MaterialTheme.typography.bodyMedium.scaled(),
            color = Color(0xFF455A64)
        )
    }
}
