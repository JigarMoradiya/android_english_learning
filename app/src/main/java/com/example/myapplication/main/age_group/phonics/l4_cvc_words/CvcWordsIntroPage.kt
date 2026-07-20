package com.example.myapplication.main.age_group.phonics.l4_cvc_words

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
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.l4_cvc_words.view_model.cvcGroups
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
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled
import com.example.myapplication.main.common.PhonicsIntroAudioViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.clickable

@Composable
fun CvcWordsIntroPage(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.skyLavender, shape = KidsFloatingShape.stars)

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
                        .weight(0.52f)
                        .fillMaxHeight()
                ) {
                    BackButtonWithText(title = "Level 4", onBackClick = { navController.popBackStack() })

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = Dimens24),
                        verticalArrangement = Arrangement.spacedBy(Dimens20, Alignment.CenterVertically)
                    ) {
                        Text(
                            text = "CVC Words",
                            style = MaterialTheme.typography.headlineLarge.scaled(),
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF4527A0)
                        )

                        // Vowel group chips
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(Dimens8),
                            verticalArrangement = Arrangement.spacedBy(Dimens8)
                        ) {
                            cvcGroups.forEach { group ->
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .background(group.color, RoundedCornerShape(Dimens8))
                                        .padding(horizontal = Dimens12, vertical = Dimens6)
                                ) {
                                    Text(
                                        text = group.vowel,
                                        style = MaterialTheme.typography.titleLarge.scaled(),
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        // Description rows
                        Column(verticalArrangement = Arrangement.spacedBy(Dimens8)) {
                            IntroRow("🐱", "Consonant + Vowel + Consonant")
                            IntroRow("🎯", "${cvcGroups.sumOf { it.words.size }} words across 5 vowel groups")
                            IntroRow("🎬", "Watch 3 sounds merge into a word!")
                        }

                        // Example words
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(Dimens8),
                            verticalArrangement = Arrangement.spacedBy(Dimens8)
                        ) {
                            val introAudioVm: PhonicsIntroAudioViewModel = hiltViewModel()
                            listOf("cat", "hen", "pig", "dog", "sun").forEachIndexed { i, word ->
                                Box(
                                    modifier = Modifier
                                        .background(
                                            cvcGroups[i].color.copy(alpha = 0.12f),
                                            RoundedCornerShape(Dimens8)
                                        )
                                        .clickable { introAudioVm.play(word) }
                                        .padding(horizontal = Dimens10, vertical = Dimens6)
                                ) {
                                    Text(
                                        text = word,
                                        style = MaterialTheme.typography.labelLarge.scaled(),
                                        fontWeight = FontWeight.Bold,
                                        color = cvcGroups[i].color
                                    )
                                }
                            }
                        }
                    }
                }

                // ── RIGHT: start card ─────────────────────────────────────────
                PhonicsIntroRightPanel(
                    screenHeight = screenH,
                    strokeColor = Color(0xFF3949AB),
                    title = "Ready to learn?",
                    titleColor = Color(0xFF1A237E),
                    descLine1 = "Tap each word to see",
                    descLine2 = "how it sounds and blends!",
                    learnButton = PhonicsIntroBtnConfig(
                        text = "Start Learning",
                        icon = Icons.Default.ArrowCircleRight,
                        type = ButtonType.GREEN,
                        onClick = { navController.navigate(RouteNavigation.CvcWordsLearn.route) }
                    ),
                    practiceButton = PhonicsIntroBtnConfig(
                        text = "Practice",
                        icon = Icons.Default.CheckCircle,
                        type = ButtonType.BLUE,
                        onClick = { navController.navigate(RouteNavigation.CvcWordsPractice.route) }
                    ),
                    listenButton = PhonicsIntroBtnConfig(
                        text = "Listen",
                        icon = Icons.Default.Hearing,
                        type = ButtonType.TEAL,
                        onClick = { navController.navigate(RouteNavigation.PhonicsListen.createRoute("cvcWords")) }
                    ),
                    modifier = Modifier.weight(0.48f).fillMaxHeight()
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
            color = Color(0xFF37474F)
        )
    }
}
