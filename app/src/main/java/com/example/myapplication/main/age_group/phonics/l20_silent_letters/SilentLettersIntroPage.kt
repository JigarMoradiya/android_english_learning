package com.example.myapplication.main.age_group.phonics.l20_silent_letters

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.VisibilityOff
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
import com.example.myapplication.main.age_group.phonics.l20_silent_letters.view_model.SilentLettersGroup
import com.example.myapplication.main.age_group.phonics.l20_silent_letters.view_model.silentLettersGroups
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

private val slAccent = Color(0xFF455A64)

private val silentLetterMap = mapOf("kn" to "k", "wr" to "w", "mb" to "b", "gn" to "g")

@Composable
fun SilentLettersIntroPage(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.grayBlue, shape = KidsFloatingShape.stars)

        BoxWithConstraints(
            modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing).fillMaxSize()
        ) {
            val screenH = maxHeight
            Row(modifier = Modifier.fillMaxSize()) {

                // ── LEFT ─────────────────────────────────────────────────────
                Column(modifier = Modifier.weight(0.54f).fillMaxHeight()) {
                    BackButtonWithText(title = "Level 20", onBackClick = { navController.popBackStack() })
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        modifier = Modifier.padding(horizontal = Dimens20),
                        verticalArrangement = Arrangement.spacedBy(Dimens14)
                    ) {
                        Text(
                            text = "Silent Letters",
                            style = MaterialTheme.typography.headlineMedium.scaled(),
                            fontWeight = FontWeight.ExtraBold,
                            color = slAccent
                        )
                        Text(
                            text = "Ghost letters that hide in words! 👻",
                            style = MaterialTheme.typography.titleSmall.scaled(),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF607D8B)
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(Dimens8)) {
                            silentLettersGroups.forEach { group -> SilentPatternChip(group) }
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(Dimens10)) {
                            IntroRow(Icons.Default.NotificationsOff, Color(0xFF455A64),
                                "kn: knife, know, knee — K is silent!")
                            IntroRow(Icons.Default.Edit,             Color(0xFF37474F),
                                "wr: write, wrist, wrong — W is silent!")
                            IntroRow(Icons.Default.Eco,              Color(0xFF546E7A),
                                "mb: lamb, climb, thumb — B is silent!")
                            IntroRow(Icons.Default.VisibilityOff,   Color(0xFF607D8B),
                                "gn: gnat, gnome, sign — G is silent!")
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }

                // ── RIGHT ────────────────────────────────────────────────────
                PhonicsIntroRightPanel(
                    screenHeight   = screenH,
                    strokeColor    = slAccent,
                    title          = "Some letters hide\nand stay quiet!",
                    titleColor     = slAccent,
                    descLine1      = "kn · wr · mb · gn",
                    descLine2      = "Can you spot the ghost letter in each word?",
                    learnButton    = PhonicsIntroBtnConfig(
                        text  = "Learn Words",
                        icon  = Icons.Default.MenuBook,
                        type  = ButtonType.PURPLE,
                        onClick = { navController.navigate(RouteNavigation.SilentLettersLearn.route) }
                    ),
                    practiceButton = PhonicsIntroBtnConfig(
                        text  = "Practice",
                        icon  = Icons.Default.CheckCircle,
                        type  = ButtonType.BLUE,
                        onClick = { navController.navigate(RouteNavigation.SilentLettersPractice.route) }
                    ),
                    listenButton   = PhonicsIntroBtnConfig(
                        text  = "Listen",
                        icon  = Icons.Default.Hearing,
                        type  = ButtonType.TEAL,
                        onClick = { navController.navigate(RouteNavigation.PhonicsListen.createRoute("silentLetters")) }
                    ),
                    modifier = Modifier.weight(0.46f).fillMaxHeight()
                )
            }
        }
    }
}

@Composable
private fun SilentPatternChip(group: SilentLettersGroup) {
    val ghostLetter = silentLetterMap[group.pattern] ?: group.pattern.first().toString()
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
            text = group.pattern,
            style = MaterialTheme.typography.labelSmall.scaled(),
            fontWeight = FontWeight.Bold,
            color = group.accentColor
        )
        Text(
            text = "silent $ghostLetter",
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
