package com.example.myapplication.main.age_group.from_3_to_5.letter_phonics_sound

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

private data class LetterTile(val letter: String, val color: Long)

private val letterTiles = listOf(
    LetterTile("A", 0xFFE53935), LetterTile("B", 0xFF1565C0), LetterTile("C", 0xFF2E7D32),
    LetterTile("D", 0xFFF57C00), LetterTile("E", 0xFF7B1FA2), LetterTile("F", 0xFF00897B)
)

@Composable
fun LetterSoundsIntroPage(navController: NavController) {
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
                    BackButtonWithText(title = "Level 1", onBackClick = { navController.popBackStack() })

                    Spacer(modifier = Modifier.weight(1f))

                    Column(
                        modifier = Modifier.padding(horizontal = Dimens24),
                        verticalArrangement = Arrangement.spacedBy(Dimens20)
                    ) {
                        Text(
                            text = "Letter Sounds",
                            style = MaterialTheme.typography.headlineLarge.scaled(),
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFBF360C)
                        )

                        // Letter tiles row
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Dimens10),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val tileSize = min(screenH * 0.08f, screenW * 0.06f)
                            letterTiles.forEach { t ->
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(tileSize)
                                        .background(Color(t.color), RoundedCornerShape(tileSize * 0.22f))
                                ) {
                                    Text(
                                        text = t.letter,
                                        style = MaterialTheme.typography.titleLarge.scaled(),
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                            Text(
                                text = "…+20",
                                style = MaterialTheme.typography.titleMedium.scaled(),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFBF360C)
                            )
                        }

                        // Description rows
                        Column(verticalArrangement = Arrangement.spacedBy(Dimens8)) {
                            IntroDescRow(icon = "🔤", text = "26 letters, each with its own sound")
                            IntroDescRow(icon = "👂", text = "Tap a letter to hear its sound")
                            IntroDescRow(icon = "✨", text = "Sounds join together to make words")
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                }

                // ── RIGHT: start card ─────────────────────────────────────────
                PhonicsIntroRightPanel(
                    screenHeight = screenH,
                    strokeColor = Color(0xFFE65100),
                    title = "Ready to learn?",
                    titleColor = Color(0xFFBF360C),
                    descLine1 = "Tap each letter to hear",
                    descLine2 = "its very own sound!",
                    learnButton = PhonicsIntroBtnConfig(
                        text = "Start Learning",
                        icon = Icons.Default.ArrowCircleRight,
                        type = ButtonType.GREEN,
                        onClick = { gated { navController.navigate(RouteNavigation.LetterPhonicsSoundRoute.route) } }
                    ),
                    practiceButton = PhonicsIntroBtnConfig(
                        text = "Practice",
                        icon = Icons.Default.CheckCircle,
                        type = ButtonType.BLUE,
                        onClick = { gated { navController.navigate(RouteNavigation.LetterSoundsPractice.route) } }
                    ),
                    listenButton = PhonicsIntroBtnConfig(
                        text = "Listen",
                        icon = Icons.Default.Hearing,
                        type = ButtonType.TEAL,
                        onClick = { gated { navController.navigate(RouteNavigation.PhonicsListen.createRoute("letterSounds")) } }
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
