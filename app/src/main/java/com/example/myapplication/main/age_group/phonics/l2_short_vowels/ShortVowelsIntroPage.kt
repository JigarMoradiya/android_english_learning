package com.example.myapplication.main.age_group.phonics.l2_short_vowels

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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.min
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.l2_short_vowels.view_model.shortVowelData
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
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled
import kotlinx.coroutines.launch
import com.example.myapplication.main.common.PhonicsIntroAudioViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.clickable

@Composable
fun ShortVowelsIntroPage(navController: NavController) {
    // L1–L3 are free but count against the daily limit — check on every
    // Learn/Practice/Listen start (login/limit sheets shown automatically).
    val accessVM = LocalAccessSheetViewModel.current
    val scope = rememberCoroutineScope()
    fun gated(action: () -> Unit) {
        scope.launch { if (accessVM.checkAccess(ModuleID.PHONICS_READING)) action() }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.peachCoral, shape = KidsFloatingShape.sparkles)

        BoxWithConstraints(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            val screenW = maxWidth
            val screenH = maxHeight

            Row(modifier = Modifier.fillMaxSize()) {

                // ── LEFT: info panel ─────────────────────────────────────────
                Column(
                    modifier = Modifier
                        .weight(0.52f)
                        .fillMaxHeight()
                ) {
                    BackButtonWithText(title = "Level 2", onBackClick = { navController.popBackStack() })

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = Dimens24),
                        verticalArrangement = Arrangement.spacedBy(Dimens20, Alignment.CenterVertically)
                    ) {
                        Text(
                            text = "Short Vowels",
                            style = MaterialTheme.typography.headlineLarge.scaled(),
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFB71C1C)
                        )

                        // Vowel tiles row
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(Dimens10),
                            verticalArrangement = Arrangement.spacedBy(Dimens10)
                        ) {
                            val tileSize = min(screenH * 0.08f, screenW * 0.06f)
                            shortVowelData.forEach { v ->
                                val color = Color(v.colorHex)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(tileSize)
                                        .background(color, RoundedCornerShape(tileSize * 0.22f))
                                ) {
                                    Text(
                                        text = v.vowel,
                                        style = MaterialTheme.typography.titleLarge.scaled(),
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        // Description rows
                        Column(verticalArrangement = Arrangement.spacedBy(Dimens8)) {
                            IntroDescRow(icon = "🔤", text = "5 vowels: A, E, I, O, U")
                            IntroDescRow(icon = "👂", text = "Tap each vowel to hear its sound")
                            IntroDescRow(icon = "✨", text = "See example words with each vowel")
                        }

                        // Example anchor words
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(Dimens8),
                            verticalArrangement = Arrangement.spacedBy(Dimens8)
                        ) {
                            val introAudioVm: PhonicsIntroAudioViewModel = hiltViewModel()
                            listOf("ant", "egg", "ink", "ox", "up").forEach { word ->
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFFFFEBEE), RoundedCornerShape(Dimens8))
                                        .clickable { introAudioVm.play(word) }
                                        .padding(horizontal = Dimens10, vertical = Dimens6)
                                ) {
                                    Text(
                                        text = word,
                                        style = MaterialTheme.typography.labelLarge.scaled(),
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFB71C1C)
                                    )
                                }
                            }
                        }
                    }
                }

                // ── RIGHT: start card ─────────────────────────────────────────
                PhonicsIntroRightPanel(
                    screenHeight = screenH,
                    strokeColor = Color(0xFFE53935),
                    title = "Ready to learn?",
                    titleColor = Color(0xFFB71C1C),
                    descLine1 = "Tap each vowel to hear",
                    descLine2 = "its sound and see examples!",
                    learnButton = PhonicsIntroBtnConfig(
                        text = "Start Learning",
                        icon = Icons.Default.ArrowCircleRight,
                        type = ButtonType.GREEN,
                        onClick = { gated { navController.navigate(RouteNavigation.ShortVowelsLearn.route) } }
                    ),
                    practiceButton = PhonicsIntroBtnConfig(
                        text = "Practice",
                        icon = Icons.Default.CheckCircle,
                        type = ButtonType.BLUE,
                        onClick = { gated { navController.navigate(RouteNavigation.ShortVowelsPractice.route) } }
                    ),
                    listenButton = PhonicsIntroBtnConfig(
                        text = "Listen",
                        icon = Icons.Default.Hearing,
                        type = ButtonType.TEAL,
                        onClick = { gated { navController.navigate(RouteNavigation.PhonicsListen.createRoute("shortVowels")) } }
                    ),
                    modifier = Modifier.weight(0.48f).fillMaxHeight()
                )
            }
        }
    }
}

@Composable
private fun IntroDescRow(icon: String, text: String) {
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
