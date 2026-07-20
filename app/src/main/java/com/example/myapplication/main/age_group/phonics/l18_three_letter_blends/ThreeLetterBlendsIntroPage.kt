package com.example.myapplication.main.age_group.phonics.l18_three_letter_blends

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.WaterDrop
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
import com.example.myapplication.main.age_group.phonics.l18_three_letter_blends.view_model.ThreeLetterBlendsGroup
import com.example.myapplication.main.age_group.phonics.l18_three_letter_blends.view_model.threeLetterBlendsGroups
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
import androidx.compose.material.icons.filled.Water

private val blendAccentColor = Color(0xFFE65100)

@Composable
fun ThreeLetterBlendsIntroPage(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.sunsetCoral, shape = KidsFloatingShape.stars)

        BoxWithConstraints(
            modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing).fillMaxSize()
        ) {
            val screenH = maxHeight
            Row(modifier = Modifier.fillMaxSize()) {

                // ── LEFT ─────────────────────────────────────────────────────
                Column(modifier = Modifier.weight(0.54f).fillMaxHeight()) {
                    BackButtonWithText(title = "Level 18", onBackClick = { navController.popBackStack() })
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = Dimens20, vertical = Dimens10),
                        verticalArrangement = Arrangement.spacedBy(Dimens14, Alignment.CenterVertically)
                    ) {
                        Text(
                            text = "3-Letter Blends",
                            style = MaterialTheme.typography.headlineMedium.scaled(),
                            fontWeight = FontWeight.ExtraBold,
                            color = blendAccentColor
                        )
                        Text(
                            text = "Three sounds snap together! 💥",
                            style = MaterialTheme.typography.titleSmall.scaled(),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF9A825)
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(Dimens6),
                            verticalArrangement = Arrangement.spacedBy(Dimens6)
                        ) {
                            threeLetterBlendsGroups.forEach { group -> BlendGroupChip(group) }
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(Dimens10)) {
                            IntroRow(Icons.Default.FitnessCenter, Color(0xFFF9A825),
                                "str — strong, street, string, strip")
                            IntroRow(Icons.Default.WaterDrop,    Color(0xFFEF6C00),
                                "spl — splash, split, splat")
                            IntroRow(Icons.Default.Eco,          Color(0xFFF57F17),
                                "spr — spring, spray, sprout")
                            IntroRow(Icons.Default.RecordVoiceOver, Color(0xFFE65100),
                                "thr — three, throw, thread")
                            IntroRow(Icons.Default.Campaign,    Color(0xFFBF360C),
                                "scr — scream, scratch, screen")
                            IntroRow(Icons.Default.Water,       Color(0xFF00838F),
                                "squ — squid, square · shr — shrimp, shrub")
                        }
                    }
                }

                // ── RIGHT ────────────────────────────────────────────────────
                PhonicsIntroRightPanel(
                    screenHeight   = screenH,
                    strokeColor    = blendAccentColor,
                    title          = "Three letters,\none powerful sound!",
                    titleColor     = blendAccentColor,
                    descLine1      = "str · spl · spr · thr · scr",
                    descLine2      = "Snap all three letters together at the start!",
                    learnButton    = PhonicsIntroBtnConfig(
                        text  = "Learn Words",
                        icon  = Icons.Default.MenuBook,
                        type  = ButtonType.PURPLE,
                        onClick = { navController.navigate(RouteNavigation.ThreeLetterBlendsLearn.route) }
                    ),
                    practiceButton = PhonicsIntroBtnConfig(
                        text  = "Practice",
                        icon  = Icons.Default.CheckCircle,
                        type  = ButtonType.BLUE,
                        onClick = { navController.navigate(RouteNavigation.ThreeLetterBlendsPractice.route) }
                    ),
                    listenButton   = PhonicsIntroBtnConfig(
                        text  = "Listen",
                        icon  = Icons.Default.Hearing,
                        type  = ButtonType.TEAL,
                        onClick = { navController.navigate(RouteNavigation.PhonicsListen.createRoute("threeLetterBlends")) }
                    ),
                    modifier = Modifier.weight(0.46f).fillMaxHeight()
                )
            }
        }
    }
}

@Composable
private fun BlendGroupChip(group: ThreeLetterBlendsGroup) {
    PhonicsGroupChip(
        emoji = group.emoji,
        title = group.blend,
        subtitle = "${group.words.size} words",
        accentColor = group.accentColor,
        tapAudio = group.words.firstOrNull()?.word
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
