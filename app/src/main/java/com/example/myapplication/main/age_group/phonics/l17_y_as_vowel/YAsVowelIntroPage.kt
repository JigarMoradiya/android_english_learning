package com.example.myapplication.main.age_group.phonics.l17_y_as_vowel

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NightlightRound
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
import com.example.myapplication.main.age_group.phonics.l17_y_as_vowel.view_model.YAsVowelGroup
import com.example.myapplication.main.age_group.phonics.l17_y_as_vowel.view_model.yAsVowelGroups
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

private val yAccentColor = Color(0xFF0097A7)

@Composable
fun YAsVowelIntroPage(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.tealCyan, shape = KidsFloatingShape.stars)

        BoxWithConstraints(
            modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing).fillMaxSize()
        ) {
            val screenH = maxHeight
            Row(modifier = Modifier.fillMaxSize()) {

                // ── LEFT ─────────────────────────────────────────────────────
                Column(modifier = Modifier.weight(0.54f).fillMaxHeight()) {
                    BackButtonWithText(title = "Level 17", onBackClick = { navController.popBackStack() })
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        modifier = Modifier.padding(horizontal = Dimens20),
                        verticalArrangement = Arrangement.spacedBy(Dimens14)
                    ) {
                        Text(
                            text = "Y as a Vowel",
                            style = MaterialTheme.typography.headlineMedium.scaled(),
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF006064)
                        )
                        Text(
                            text = "Y plays three vowel roles! 🦋",
                            style = MaterialTheme.typography.titleSmall.scaled(),
                            fontWeight = FontWeight.Bold,
                            color = yAccentColor
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(Dimens8)) {
                            yAsVowelGroups.forEach { group -> YGroupChip(group) }
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(Dimens10)) {
                            IntroRow(Icons.Default.NightlightRound, yAccentColor,
                                "Y = /ī/ at end of 1-syllable words: fly, sky, cry")
                            IntroRow(Icons.Default.EmojiEmotions, Color(0xFF00838F),
                                "Y = /ē/ at end of 2+ syllable words: happy, baby")
                            IntroRow(Icons.Default.FitnessCenter, Color(0xFF006064),
                                "Y in the middle says /ī/: gym, myth, lynx")
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }

                // ── RIGHT ────────────────────────────────────────────────────
                PhonicsIntroRightPanel(
                    screenHeight   = screenH,
                    strokeColor    = yAccentColor,
                    title          = "Y loves being\na vowel too!",
                    titleColor     = Color(0xFF006064),
                    descLine1      = "Y = /ī/ · Y = /ē/ · Y in the middle",
                    descLine2      = "Three sounds, one letter — let's discover them!",
                    learnButton    = PhonicsIntroBtnConfig(
                        text  = "Learn Words",
                        icon  = Icons.Default.MenuBook,
                        type  = ButtonType.PURPLE,
                        onClick = { navController.navigate(RouteNavigation.YAsVowelLearn.route) }
                    ),
                    practiceButton = PhonicsIntroBtnConfig(
                        text  = "Practice",
                        icon  = Icons.Default.CheckCircle,
                        type  = ButtonType.BLUE,
                        onClick = { navController.navigate(RouteNavigation.YAsVowelPractice.route) }
                    ),
                    listenButton   = PhonicsIntroBtnConfig(
                        text  = "Listen",
                        icon  = Icons.Default.Hearing,
                        type  = ButtonType.TEAL,
                        onClick = { navController.navigate(RouteNavigation.PhonicsListen.createRoute("yAsVowel")) }
                    ),
                    modifier = Modifier.weight(0.46f).fillMaxHeight()
                )
            }
        }
    }
}

@Composable
private fun YGroupChip(group: YAsVowelGroup) {
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
            text = group.label,
            style = MaterialTheme.typography.labelSmall.scaled(),
            fontWeight = FontWeight.Bold,
            color = group.accentColor
        )
        Text(
            text = group.sound,
            style = MaterialTheme.typography.labelSmall.scaled(),
            color = Color(0xFF546E7A)
        )
    }
}

@Composable
private fun IntroRow(icon: ImageVector, iconColor: Color, text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens8),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(16.dp).padding(top = 2.dp)
        )
        Text(
            text  = text,
            style = MaterialTheme.typography.bodySmall.scaled(),
            color = Color(0xFF455A64)
        )
    }
}
