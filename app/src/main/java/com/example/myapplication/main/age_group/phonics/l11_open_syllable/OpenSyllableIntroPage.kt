package com.example.myapplication.main.age_group.phonics.l11_open_syllable

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
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens14
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled
import com.example.myapplication.main.common.PhonicsIntroAudioViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip

private val accentColor = Color(0xFF6A1B9A)

@Composable
fun OpenSyllableIntroPage(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.indigoPurple, shape = KidsFloatingShape.stars)

        BoxWithConstraints(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            val screenH = maxHeight

            Row(modifier = Modifier.fillMaxSize()) {

                // ── LEFT: info panel ─────────────────────────────────────────
                Column(
                    modifier = Modifier
                        .weight(0.54f)
                        .fillMaxHeight()
                ) {
                    BackButtonWithText(title = "Level 11", onBackClick = { navController.popBackStack() })

                    Column(

                        modifier = Modifier

                            .weight(1f)

                            .verticalScroll(rememberScrollState())

                            .padding(horizontal = Dimens20, vertical = Dimens10),

                        verticalArrangement = Arrangement.spacedBy(Dimens14, Alignment.CenterVertically)

                    ) {
                        Text(
                            text = "Open Syllable",
                            style = MaterialTheme.typography.headlineLarge.scaled(),
                            fontWeight = FontWeight.ExtraBold,
                            color = accentColor
                        )

                        // Example vowel chips
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(Dimens8),
                            verticalArrangement = Arrangement.spacedBy(Dimens8)
                        ) {
                            listOf(
                                "E" to Color(0xFF00897B),
                                "O" to Color(0xFFE65100),
                                "I/Y" to Color(0xFF6A1B9A)
                            ).forEach { (label, color) ->
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .background(color, RoundedCornerShape(Dimens8))
                                        .padding(horizontal = Dimens12, vertical = Dimens8)
                                ) {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.titleLarge.scaled(),
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        // Description rows
                        Column(verticalArrangement = Arrangement.spacedBy(Dimens8)) {
                            IntroRow("🔓", "Vowel at end of syllable says its long name")
                            IntroRow("🌿", "me, he, she, be, we — long /ē/")
                            IntroRow("🌕", "go, no, so, do — long /ō/")
                            IntroRow("⚡", "fly, shy, sky, by — long /ī/")
                        }

                        // Sample words
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(Dimens8),
                            verticalArrangement = Arrangement.spacedBy(Dimens8)
                        ) {
                            val samples = listOf("me" to Color(0xFF00897B), "go" to Color(0xFFE65100), "fly" to Color(0xFF6A1B9A))
                            val introAudioVm: PhonicsIntroAudioViewModel = hiltViewModel()
                            samples.forEach { (word, color) ->
                                Box(
                                    modifier = Modifier
                                        .background(color.copy(alpha = 0.12f), RoundedCornerShape(Dimens8))
                                        .clip(RoundedCornerShape(Dimens8))
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

                // ── RIGHT: start card ────────────────────────────────────────
                PhonicsIntroRightPanel(
                    screenHeight = screenH,
                    strokeColor = accentColor,
                    title = "When the vowel is\nat the end — it speaks!",
                    titleColor = accentColor,
                    descLine1 = "An open syllable ends in a vowel",
                    descLine2 = "that says its long sound",
                    learnButton = PhonicsIntroBtnConfig(
                        text = "Learn Words",
                        icon = Icons.Default.MenuBook,
                        type = ButtonType.PURPLE,
                        onClick = { navController.navigate(RouteNavigation.OpenSyllableLearn.route) }
                    ),
                    practiceButton = PhonicsIntroBtnConfig(
                        text = "Practice",
                        icon = Icons.Default.CheckCircle,
                        type = ButtonType.BLUE,
                        onClick = { navController.navigate(RouteNavigation.OpenSyllablePractice.route) }
                    ),
                    listenButton = PhonicsIntroBtnConfig(
                        text = "Listen",
                        icon = Icons.Default.Hearing,
                        type = ButtonType.TEAL,
                        onClick = { navController.navigate(RouteNavigation.PhonicsListen.createRoute("openSyllable")) }
                    ),
                    modifier = Modifier.weight(0.46f).fillMaxHeight()
                )
            }
        }
    }
}

@Composable
private fun IntroRow(icon: String, text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, style = MaterialTheme.typography.bodyLarge)
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.scaled(),
            color = accentColor.copy(alpha = 0.8f)
        )
    }
}
