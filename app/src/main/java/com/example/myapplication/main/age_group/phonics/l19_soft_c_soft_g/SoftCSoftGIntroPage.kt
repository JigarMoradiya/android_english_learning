package com.example.myapplication.main.age_group.phonics.l19_soft_c_soft_g

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Star
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
import com.example.myapplication.main.age_group.phonics.l19_soft_c_soft_g.view_model.SoftCSoftGGroup
import com.example.myapplication.main.age_group.phonics.l19_soft_c_soft_g.view_model.softCSoftGGroups
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

private val softCGAccent = Color(0xFFBF360C)

@Composable
fun SoftCSoftGIntroPage(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.sunsetCoral, shape = KidsFloatingShape.stars)

        BoxWithConstraints(
            modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing).fillMaxSize()
        ) {
            val screenH = maxHeight
            Row(modifier = Modifier.fillMaxSize()) {

                // ── LEFT ─────────────────────────────────────────────────────
                Column(modifier = Modifier.weight(0.54f).fillMaxHeight()) {
                    BackButtonWithText(title = "Level 19", onBackClick = { navController.popBackStack() })
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = Dimens20, vertical = Dimens10),
                        verticalArrangement = Arrangement.spacedBy(Dimens14, Alignment.CenterVertically)
                    ) {
                        Text(
                            text = "Soft C & Soft G",
                            style = MaterialTheme.typography.headlineMedium.scaled(),
                            fontWeight = FontWeight.ExtraBold,
                            color = softCGAccent
                        )
                        Text(
                            text = "Two letters with double personalities! 🎭",
                            style = MaterialTheme.typography.titleSmall.scaled(),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE64A19)
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(Dimens8),
                            verticalArrangement = Arrangement.spacedBy(Dimens8)
                        ) {
                            softCSoftGGroups.forEach { group -> SoftCGGroupChip(group) }
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(Dimens10)) {
                            IntroRow(Icons.Default.GraphicEq, softCGAccent,
                                "Soft C: city, cent, cycle, ice, face → /s/")
                            IntroRow(Icons.Default.Pets,       Color(0xFF546E7A),
                                "Hard C: cat, cup, coat, car → /k/")
                            IntroRow(Icons.Default.Star,       Color(0xFFE64A19),
                                "Soft G: gem, giraffe, gym, giant → /j/")
                            IntroRow(Icons.Default.FitnessCenter, Color(0xFF455A64),
                                "Hard G: gap, got, gum, game → /g/")
                        }
                    }
                }

                // ── RIGHT ────────────────────────────────────────────────────
                PhonicsIntroRightPanel(
                    screenHeight   = screenH,
                    strokeColor    = softCGAccent,
                    title          = "C and G each\nhave two sides!",
                    titleColor     = softCGAccent,
                    descLine1      = "Soft C=/s/ · Hard C=/k/ · Soft G=/j/ · Hard G=/g/",
                    descLine2      = "The letters that follow decide the sound!",
                    learnButton    = PhonicsIntroBtnConfig(
                        text  = "Learn Words",
                        icon  = Icons.Default.MenuBook,
                        type  = ButtonType.PURPLE,
                        onClick = { navController.navigate(RouteNavigation.SoftCSoftGLearn.route) }
                    ),
                    practiceButton = PhonicsIntroBtnConfig(
                        text  = "Practice",
                        icon  = Icons.Default.CheckCircle,
                        type  = ButtonType.BLUE,
                        onClick = { navController.navigate(RouteNavigation.SoftCSoftGPractice.route) }
                    ),
                    listenButton   = PhonicsIntroBtnConfig(
                        text  = "Listen",
                        icon  = Icons.Default.Hearing,
                        type  = ButtonType.TEAL,
                        onClick = { navController.navigate(RouteNavigation.PhonicsListen.createRoute("softCSoftG")) }
                    ),
                    modifier = Modifier.weight(0.46f).fillMaxHeight()
                )
            }
        }
    }
}

@Composable
private fun SoftCGGroupChip(group: SoftCSoftGGroup) {
    PhonicsGroupChip(
        emoji = group.emoji,
        title = group.title,
        subtitle = group.sound,
        accentColor = group.accentColor
    )
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
