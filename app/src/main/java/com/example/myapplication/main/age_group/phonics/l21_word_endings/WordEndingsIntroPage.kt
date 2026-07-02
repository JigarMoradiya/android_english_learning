package com.example.myapplication.main.age_group.phonics.l21_word_endings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
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
import com.example.myapplication.main.age_group.phonics.l21_word_endings.view_model.WordEndingGroup
import com.example.myapplication.main.age_group.phonics.l21_word_endings.view_model.wordEndingGroups
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

private val weGreen      = Color(0xFF2E7D32)
private val weGreenLight = Color(0xFF388E3C)

@Composable
fun WordEndingsIntroPage(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.meadowGreen, shape = KidsFloatingShape.leaves)

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
                    BackButtonWithText(title = "Level 21", onBackClick = { navController.popBackStack() })
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        modifier = Modifier.padding(horizontal = Dimens20),
                        verticalArrangement = Arrangement.spacedBy(Dimens14)
                    ) {
                        Text(
                            text       = "Word Endings",
                            style      = MaterialTheme.typography.headlineMedium.scaled(),
                            fontWeight = FontWeight.Bold,
                            color      = weGreen
                        )
                        Text(
                            text       = "Endings that change what words do! 🔧",
                            style      = MaterialTheme.typography.titleSmall.scaled(),
                            fontWeight = FontWeight.Bold,
                            color      = weGreenLight
                        )
                        // Group chips: emoji + suffix
                        Row(horizontalArrangement = Arrangement.spacedBy(Dimens8)) {
                            wordEndingGroups.forEach { group -> WEGroupChip(group) }
                        }
                        // Bullet rows
                        Column(verticalArrangement = Arrangement.spacedBy(Dimens10)) {
                            WEBulletRow(Icons.Default.PlayArrow,   weGreen,           "-ing: jump → jumping — what's happening now")
                            WEBulletRow(Icons.Default.History,     Color(0xFF1565C0), "-ed: jump → jumped — what happened in the past")
                            WEBulletRow(Icons.Default.BarChart,    Color(0xFFE65100), "-er: tall → taller — comparing two things")
                            WEBulletRow(Icons.Default.EmojiEvents, Color(0xFF6A1B9A), "-est: tall → tallest — the most of all!")
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }

                // ── RIGHT 46% ─────────────────────────────────────────────────
                PhonicsIntroRightPanel(
                    screenHeight   = totalH,
                    strokeColor    = weGreen,
                    title          = "Add endings to change\nwhat a word means!",
                    titleColor     = weGreen,
                    descLine1      = "-ing  ·  -ed  ·  -er  ·  -est",
                    descLine2      = "Some words need special spelling tricks — double or drop the e!",
                    learnButton    = PhonicsIntroBtnConfig(
                        text    = "Learn Words",
                        icon    = Icons.Default.MenuBook,
                        type    = ButtonType.PURPLE,
                        onClick = { navController.navigate(RouteNavigation.WordEndingsLearn.route) }
                    ),
                    practiceButton = PhonicsIntroBtnConfig(
                        text    = "Practice",
                        icon    = Icons.Default.CheckCircle,
                        type    = ButtonType.BLUE,
                        onClick = { navController.navigate(RouteNavigation.WordEndingsPractice.route) }
                    ),
                    listenButton   = PhonicsIntroBtnConfig(
                        text    = "Listen",
                        icon    = Icons.Default.Hearing,
                        type    = ButtonType.TEAL,
                        onClick = { navController.navigate(RouteNavigation.PhonicsListen.createRoute("wordEndings")) }
                    ),
                    modifier       = Modifier.weight(1f).fillMaxHeight()
                )
            }
        }
    }
}

@Composable
private fun WEGroupChip(group: WordEndingGroup) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens2),
        modifier = Modifier
            .background(group.accentColor.copy(alpha = 0.10f), RoundedCornerShape(8.dp))
            .border(1.5.dp, group.accentColor.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
            .padding(horizontal = Dimens8, vertical = Dimens6)
    ) {
        Text(text = group.emoji, style = MaterialTheme.typography.bodyMedium.scaled())
        Text(
            text       = group.suffix,
            style      = MaterialTheme.typography.labelSmall.scaled(),
            fontWeight = FontWeight.Bold,
            color      = group.accentColor
        )
    }
}

@Composable
private fun WEBulletRow(icon: ImageVector, color: Color, text: String) {
    Row(
        verticalAlignment     = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(Dimens8)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = color,
            modifier = Modifier.size(14.dp).padding(top = Dimens2))
        Text(text = text, style = MaterialTheme.typography.labelSmall.scaled(), color = weGreen)
    }
}
