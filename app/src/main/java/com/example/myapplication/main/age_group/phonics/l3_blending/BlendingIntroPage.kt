package com.example.myapplication.main.age_group.phonics.l3_blending

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.l3_blending.view_model.cvBlendingWords
import com.example.myapplication.main.age_group.phonics.l3_blending.view_model.vcBlendingWords
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
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled

private val blueDeep = Color(0xFF1565C0)
private val blueLight = Color(0xFFE3F2FD)

@Composable
fun BlendingIntroPage(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.blueIndigo, shape = KidsFloatingShape.sparkles)

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
                    BackButtonWithText(title = "Level 3", onBackClick = { navController.popBackStack() })

                    Spacer(modifier = Modifier.weight(1f))

                    Column(
                        modifier = Modifier.padding(horizontal = Dimens24),
                        verticalArrangement = Arrangement.spacedBy(Dimens20)
                    ) {
                        Text(
                            text = "2-Sound Blending",
                            style = MaterialTheme.typography.headlineLarge.scaled(),
                            fontWeight = FontWeight.ExtraBold,
                            color = blueDeep
                        )

                        // VC / CV tab preview tiles
                        Row(horizontalArrangement = Arrangement.spacedBy(Dimens16)) {
                            BlendTypeChip(label = "V + C", example = "at, in", blueDeep)
                            BlendTypeChip(label = "C + V", example = "ba, go", Color(0xFF1A237E))
                        }

                        // Description rows
                        Column(verticalArrangement = Arrangement.spacedBy(Dimens8)) {
                            IntroRow("🧩", "Blend vowel + consonant sounds")
                            IntroRow("🔤", "${vcBlendingWords.size} VC words + ${cvBlendingWords.size} CV words")
                            IntroRow("👂", "Watch the boxes merge into a word!")
                        }

                        // Sample words row
                        Row(horizontalArrangement = Arrangement.spacedBy(Dimens8)) {
                            listOf("at", "in", "ba", "go", "us").forEach { word ->
                                Box(
                                    modifier = Modifier
                                        .background(blueLight, RoundedCornerShape(Dimens8))
                                        .padding(horizontal = Dimens10, vertical = Dimens6)
                                ) {
                                    Text(
                                        text = word,
                                        style = MaterialTheme.typography.labelLarge.scaled(),
                                        fontWeight = FontWeight.Bold,
                                        color = blueDeep
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                }

                // ── RIGHT: start card ─────────────────────────────────────────
                PhonicsIntroRightPanel(
                    screenHeight = screenH,
                    strokeColor = blueDeep,
                    title = "Ready to blend?",
                    titleColor = blueDeep,
                    descLine1 = "Tap any word to watch",
                    descLine2 = "the two sounds blend together!",
                    learnButton = PhonicsIntroBtnConfig(
                        text = "Start Learning",
                        icon = Icons.Default.ArrowCircleRight,
                        type = ButtonType.BLUE,
                        onClick = { navController.navigate(RouteNavigation.BlendingLearn.route) }
                    ),
                    listenButton = PhonicsIntroBtnConfig(
                        text = "Listen",
                        icon = Icons.Default.Hearing,
                        type = ButtonType.TEAL,
                        onClick = { navController.navigate(RouteNavigation.PhonicsListen.createRoute("blending")) }
                    ),
                    modifier = Modifier.weight(0.48f).fillMaxHeight()
                )
            }
        }
    }
}

@Composable
private fun BlendTypeChip(label: String, example: String, color: Color) {
    Column(
        modifier = Modifier
            .background(color.copy(alpha = 0.10f), RoundedCornerShape(Dimens12))
            .padding(horizontal = Dimens12, vertical = Dimens8),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge.scaled(),
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = example,
            style = MaterialTheme.typography.labelSmall.scaled(),
            color = color.copy(alpha = 0.7f)
        )
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
