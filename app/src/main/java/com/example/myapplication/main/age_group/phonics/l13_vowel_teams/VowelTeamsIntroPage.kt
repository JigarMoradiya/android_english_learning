package com.example.myapplication.main.age_group.phonics.l13_vowel_teams

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
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.PhonicsIntroBtnConfig
import com.example.myapplication.main.common.PhonicsIntroRightPanel
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens14
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled
import com.example.myapplication.main.common.PhonicsIntroAudioViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.clickable

private val accentColor = Color(0xFFEF6C00)

@Composable
fun VowelTeamsIntroPage(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.peachYellow, shape = KidsFloatingShape.hearts)

        BoxWithConstraints(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            val screenH = maxHeight
            Row(modifier = Modifier.fillMaxSize()) {

                // ── LEFT panel ───────────────────────────────────────────────
                Column(
                    modifier = Modifier
                        .weight(0.54f)
                        .fillMaxHeight()
                ) {
                    BackButtonWithText(title = "Level 13", onBackClick = { navController.popBackStack() })

                    Column(

                        modifier = Modifier

                            .weight(1f)

                            .verticalScroll(rememberScrollState())

                            .padding(horizontal = Dimens20, vertical = Dimens10),

                        verticalArrangement = Arrangement.spacedBy(Dimens14, Alignment.CenterVertically)

                    ) {
                        Text(
                            text = "Vowel Teams",
                            style = MaterialTheme.typography.headlineLarge.scaled(),
                            fontWeight = FontWeight.ExtraBold,
                            color = accentColor
                        )

                        // Team chips
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(Dimens8),
                            verticalArrangement = Arrangement.spacedBy(Dimens8)
                        ) {
                            listOf(
                                "AI/AY" to Color(0xFFD32F2F),
                                "EE/EA" to Color(0xFF00897B),
                                "OA/OW" to Color(0xFF1565C0),
                                "OO" to Color(0xFF5E35B1),
                                "EW/UE/UI" to Color(0xFF0277BD),
                                "EY" to Color(0xFFC62828),
                                "IE" to Color(0xFFAD1457)
                            ).forEach { (label, color) ->
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .background(color, RoundedCornerShape(Dimens8))
                                        .padding(horizontal = Dimens12, vertical = Dimens6)
                                ) {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.titleMedium.scaled(),
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        // Bullet rows
                        Column(verticalArrangement = Arrangement.spacedBy(Dimens10)) {
                            bulletRow("🤝", Color(0xFFD32F2F),
                                "AI/AY — rain, tail, day, play → long /ā/")
                            bulletRow("🤝", Color(0xFF00897B),
                                "EE/EA — feet, tree, read, team → long /ē/")
                            bulletRow("🤝", Color(0xFF1565C0),
                                "OA/OW — boat, coat, snow, grow → long /ō/")
                            bulletRow("🌙", Color(0xFF5E35B1),
                                "OO — moon 🌙 or book 📖 · EW/UE/UI — new, blue, fruit")
                            bulletRow("✨", Color(0xFFC62828),
                                "EA — bread · EY — monkey · IE — pie or chief")
                            bulletRow("👆", accentColor,
                                "Tap any word to hear the vowel team sound")
                        }

                        // Sample words
                        val introAudioVm: PhonicsIntroAudioViewModel = hiltViewModel()
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(Dimens8),
                            verticalArrangement = Arrangement.spacedBy(Dimens8)
                        ) {
                            listOf(
                                "rain" to Color(0xFFD32F2F),
                                "feet" to Color(0xFF00897B),
                                "boat" to Color(0xFF1565C0)
                            ).forEach { (word, color) ->
                                Box(
                                    modifier = Modifier
                                        .background(color.copy(alpha = 0.12f), RoundedCornerShape(Dimens8))
                                        .clickable { introAudioVm.play(word) }
                                        .padding(horizontal = Dimens10, vertical = Dimens8)
                                ) {
                                    Text(
                                        text = word,
                                        style = MaterialTheme.typography.titleMedium.scaled(),
                                        fontWeight = FontWeight.Bold,
                                        color = color
                                    )
                                }
                            }
                        }
                    }
                }

                // ── RIGHT panel ──────────────────────────────────────────────
                PhonicsIntroRightPanel(
                    screenHeight = screenH,
                    strokeColor = accentColor,
                    title = "Two vowels team up!",
                    titleColor = accentColor,
                    descLine1 = "When two vowels go walking,",
                    descLine2 = "the first one does the talking",
                    learnButton = PhonicsIntroBtnConfig(
                        text = "Learn Teams",
                        icon = Icons.Default.MenuBook,
                        type = ButtonType.ORANGE,
                        onClick = { navController.navigate(RouteNavigation.VowelTeamsLearn.route) }
                    ),
                    practiceButton = PhonicsIntroBtnConfig(
                        text = "Practice",
                        icon = Icons.Default.CheckCircle,
                        type = ButtonType.BLUE,
                        onClick = { navController.navigate(RouteNavigation.VowelTeamsPractice.route) }
                    ),
                    listenButton = PhonicsIntroBtnConfig(
                        text = "Listen",
                        icon = Icons.Default.Hearing,
                        type = ButtonType.TEAL,
                        onClick = { navController.navigate(RouteNavigation.PhonicsListen.createRoute("vowelTeams")) }
                    ),
                    modifier = Modifier.weight(0.46f).fillMaxHeight()
                )
            }
        }
    }
}

@Composable
private fun bulletRow(icon: String, color: Color, text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens10),
        verticalAlignment = Alignment.Top
    ) {
        Text(text = icon, style = MaterialTheme.typography.bodyLarge.scaled())
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.scaled(),
            color = accentColor.copy(alpha = 0.85f)
        )
    }
}
