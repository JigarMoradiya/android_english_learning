package com.example.myapplication.main.age_group.phonics.l3_blending

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.l3_blending.view_model.cvBlendingWords
import com.example.myapplication.main.age_group.phonics.l3_blending.view_model.vcBlendingWords
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.sheets.LocalAccessSheetViewModel
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
import kotlinx.coroutines.launch
import com.example.myapplication.main.common.PhonicsIntroAudioViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.clickable

private val blueDeep = Color(0xFF1565C0)
private val blueLight = Color(0xFFE3F2FD)

@Composable
fun BlendingIntroPage(navController: NavController) {
    // L1–L3 are free but count against the daily limit — check on every
    // Learn/Practice/Listen start (login/limit sheets shown automatically).
    val accessVM = LocalAccessSheetViewModel.current
    val scope = rememberCoroutineScope()
    fun gated(action: () -> Unit) {
        scope.launch { if (accessVM.checkAccess(ModuleID.PHONICS_READING)) action() }
    }

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

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = Dimens24),
                        verticalArrangement = Arrangement.spacedBy(Dimens20, Alignment.CenterVertically)
                    ) {
                        Text(
                            text = "2-Sound Blending",
                            style = MaterialTheme.typography.headlineLarge.scaled(),
                            fontWeight = FontWeight.ExtraBold,
                            color = blueDeep
                        )

                        // VC / CV tab preview tiles
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(Dimens16),
                            verticalArrangement = Arrangement.spacedBy(Dimens16)
                        ) {
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
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(Dimens8),
                            verticalArrangement = Arrangement.spacedBy(Dimens8)
                        ) {
                            val introAudioVm: PhonicsIntroAudioViewModel = hiltViewModel()
                            listOf("at", "in", "ba", "go", "us").forEach { word ->
                                Box(
                                    modifier = Modifier
                                        .background(blueLight, RoundedCornerShape(Dimens8))
                                        .clickable { introAudioVm.play(word) }
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
                        type = ButtonType.GREEN,
                        onClick = { gated { navController.navigate(RouteNavigation.BlendingLearn.route) } }
                    ),
                    practiceButton = PhonicsIntroBtnConfig(
                        text = "Practice",
                        icon = Icons.Default.CheckCircle,
                        type = ButtonType.BLUE,
                        onClick = { gated { navController.navigate(RouteNavigation.BlendingPractice.route) } }
                    ),
                    listenButton = PhonicsIntroBtnConfig(
                        text = "Listen",
                        icon = Icons.Default.Hearing,
                        type = ButtonType.TEAL,
                        onClick = { gated { navController.navigate(RouteNavigation.PhonicsListen.createRoute("blending")) } }
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
