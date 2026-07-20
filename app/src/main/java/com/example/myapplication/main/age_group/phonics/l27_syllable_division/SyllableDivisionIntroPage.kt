package com.example.myapplication.main.age_group.phonics.l27_syllable_division

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.l27_syllable_division.view_model.SDGroup
import com.example.myapplication.main.age_group.phonics.l27_syllable_division.view_model.sdGroups
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.*
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens14
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled
import androidx.compose.material.icons.filled.Bedtime

private val sdAccent      = Color(0xFF00897B)
private val sdAccentLight = Color(0xFF00695C)

@Composable
fun SyllableDivisionIntroPage(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.tealCyan, shape = KidsFloatingShape.waves)

        BoxWithConstraints(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            val totalW = maxWidth
            val totalH = maxHeight
            Row(modifier = Modifier.fillMaxSize()) {

                // ── LEFT 54% ──────────────────────────────────────────────────
                Column(
                    modifier = Modifier
                        .width(totalW * 0.54f)
                        .fillMaxHeight()
                ) {
                    BackButtonWithText(title = "Level 27", onBackClick = { navController.popBackStack() })
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = Dimens20, vertical = Dimens10),
                        verticalArrangement = Arrangement.spacedBy(Dimens14, Alignment.CenterVertically)
                    ) {
                        Text(
                            text       = "Syllable Division",
                            style      = MaterialTheme.typography.headlineMedium.scaled(),
                            fontWeight = FontWeight.Bold,
                            color      = sdAccent
                        )
                        Text(
                            text       = "Chop big words into beats! ✂️",
                            style      = MaterialTheme.typography.titleSmall.scaled(),
                            fontWeight = FontWeight.Bold,
                            color      = sdAccentLight
                        )
                        // Group chips
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(Dimens8),
                            verticalArrangement = Arrangement.spacedBy(Dimens8)
                        ) {
                            sdGroups.forEach { group -> SDGroupChip(group) }
                        }
                        // Bullet rows
                        Column(verticalArrangement = Arrangement.spacedBy(Dimens10)) {
                            SDBulletRow(Icons.Default.ContentCut,            Color(0xFF2E7D32), "VC/CV · two consonants? chop between: rab-bit")
                            SDBulletRow(Icons.AutoMirrored.Filled.VolumeUp,  Color(0xFF1565C0), "V/CV · first vowel says its name: ti-ger")
                            SDBulletRow(Icons.Default.Bolt,                  Color(0xFF7B1FA2), "VC/V · first vowel is short: cam-el")
                            SDBulletRow(Icons.Default.Bedtime,               Color(0xFF5D4037), "Schwa /ə/ · lazy \"uh\" sound: a-bout, so-fa")
                        }
                        // Syllable note
                        Row(
                            verticalAlignment     = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Dimens6),
                            modifier = Modifier
                                .background(sdAccent.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                                .border(1.dp, sdAccent.copy(alpha = 0.30f), RoundedCornerShape(8.dp))
                                .padding(horizontal = Dimens10, vertical = Dimens6)
                        ) {
                            Text(text = "✂️", style = MaterialTheme.typography.bodyMedium.scaled())
                            Text(
                                text  = "Every syllable has ONE vowel sound · rab-bit = 2 beats!",
                                style = MaterialTheme.typography.labelSmall.scaled(),
                                color = sdAccent
                            )
                        }
                    }
                }

                // ── RIGHT 46% ─────────────────────────────────────────────────
                PhonicsIntroRightPanel(
                    screenHeight   = totalH,
                    strokeColor    = sdAccent,
                    title          = "Big words break\ninto small beats!",
                    titleColor     = sdAccent,
                    descLine1      = "🐰 rab-bit  🐯 ti-ger  🐫 cam-el",
                    descLine2      = "One vowel sound in every beat!",
                    learnButton    = PhonicsIntroBtnConfig(
                        text    = "Learn Words",
                        icon    = Icons.Default.CheckCircle,
                        type    = ButtonType.ORANGE,
                        onClick = { navController.navigate(RouteNavigation.SyllableDivisionLearn.route) }
                    ),
                    practiceButton = PhonicsIntroBtnConfig(
                        text    = "Practice",
                        icon    = Icons.Default.CheckCircle,
                        type    = ButtonType.BLUE,
                        onClick = { navController.navigate(RouteNavigation.SyllableDivisionPractice.route) }
                    ),
                    listenButton   = PhonicsIntroBtnConfig(
                        text    = "Listen",
                        icon    = Icons.Default.Hearing,
                        type    = ButtonType.TEAL,
                        onClick = { navController.navigate(RouteNavigation.PhonicsListen.createRoute("syllableDivision")) }
                    ),
                    modifier       = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
        }
    }
}

@Composable
private fun SDGroupChip(group: SDGroup) {
    PhonicsGroupChip(
        emoji = group.emoji,
        title = group.name,
        accentColor = group.accentColor,
        tapAudio = group.words.firstOrNull()?.full
    )
}

@Composable
private fun SDBulletRow(icon: ImageVector, color: Color, text: String) {
    Row(
        verticalAlignment     = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(Dimens8)
    ) {
        androidx.compose.material3.Icon(
            imageVector        = icon,
            contentDescription = null,
            tint               = color,
            modifier           = Modifier.size(14.dp).padding(top = Dimens2)
        )
        Text(text = text, style = MaterialTheme.typography.labelSmall.scaled(), color = sdAccent)
    }
}
