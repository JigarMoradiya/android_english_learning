package com.example.myapplication.main.age_group.phonics.l25_consonant_le

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Nature
import androidx.compose.material.icons.filled.Spa
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
import com.example.myapplication.main.age_group.phonics.l25_consonant_le.view_model.CLEGroup
import com.example.myapplication.main.age_group.phonics.l25_consonant_le.view_model.cleGroups
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

private val cleAccent      = Color(0xFFAD1457)
private val cleAccentLight = Color(0xFFC2185B)

@Composable
fun ConsonantLeIntroPage(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.rosePink, shape = KidsFloatingShape.bubbles)

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
                    BackButtonWithText(title = "Level 25", onBackClick = { navController.popBackStack() })
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = Dimens20, vertical = Dimens10),
                        verticalArrangement = Arrangement.spacedBy(Dimens14, Alignment.CenterVertically)
                    ) {
                        Text(
                            text       = "Consonant + -le",
                            style      = MaterialTheme.typography.headlineMedium.scaled(),
                            fontWeight = FontWeight.Bold,
                            color      = cleAccent
                        )
                        Text(
                            text       = "The -le makes its own syllable! 🍎",
                            style      = MaterialTheme.typography.titleSmall.scaled(),
                            fontWeight = FontWeight.Bold,
                            color      = cleAccentLight
                        )
                        // Group chips
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(Dimens8),
                            verticalArrangement = Arrangement.spacedBy(Dimens8)
                        ) {
                            cleGroups.forEach { group -> CLEGroupChip(group) }
                        }
                        // Bullet rows matching iOS
                        Column(verticalArrangement = Arrangement.spacedBy(Dimens10)) {
                            CLEBulletRow(Icons.Default.WaterDrop,           Color(0xFFC62828), "-ble · ta·ble, bub·ble, sta·ble")
                            CLEBulletRow(Icons.Default.Spa,                 Color(0xFF0277BD), "-tle · lit·tle, tur·tle, bot·tle")
                            CLEBulletRow(Icons.Default.Nature,              Color(0xFF2E7D32), "-ple · ap·ple, sim·ple, pur·ple")
                            CLEBulletRow(Icons.Default.LocalFireDepartment, Color(0xFFE65100), "-dle/-gle · can·dle, jun·gle, ea·gle")
                        }
                        // Silent-e note row
                        Row(
                            verticalAlignment     = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Dimens6),
                            modifier = Modifier
                                .background(cleAccent.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                                .border(1.dp, cleAccent.copy(alpha = 0.30f), RoundedCornerShape(8.dp))
                                .padding(horizontal = Dimens10, vertical = Dimens6)
                        ) {
                            Text(
                                text       = "🔇",
                                style      = MaterialTheme.typography.bodyMedium.scaled()
                            )
                            Text(
                                text  = "The final -e is silent. You only hear the consonant + /l/!",
                                style = MaterialTheme.typography.labelSmall.scaled(),
                                color = cleAccent
                            )
                        }
                    }
                }

                // ── RIGHT 46% ─────────────────────────────────────────────────
                PhonicsIntroRightPanel(
                    screenHeight   = totalH,
                    strokeColor    = cleAccent,
                    title          = "Consonant + le makes\nits own syllable!",
                    titleColor     = cleAccent,
                    descLine1      = "-ble · -tle · -ple · -dle · -gle",
                    descLine2      = "The final -e is always silent!",
                    learnButton    = PhonicsIntroBtnConfig(
                        text    = "Learn Words",
                        icon    = Icons.Default.CheckCircle,
                        type    = ButtonType.PINK,
                        onClick = { navController.navigate(RouteNavigation.ConsonantLeLearn.route) }
                    ),
                    practiceButton = PhonicsIntroBtnConfig(
                        text    = "Practice",
                        icon    = Icons.Default.CheckCircle,
                        type    = ButtonType.BLUE,
                        onClick = { navController.navigate(RouteNavigation.ConsonantLePractice.route) }
                    ),
                    listenButton   = PhonicsIntroBtnConfig(
                        text    = "Listen",
                        icon    = Icons.Default.Hearing,
                        type    = ButtonType.TEAL,
                        onClick = { navController.navigate(RouteNavigation.PhonicsListen.createRoute("consonantLe")) }
                    ),
                    modifier       = Modifier.weight(1f).fillMaxHeight()
                )
            }
        }
    }
}

@Composable
private fun CLEGroupChip(group: CLEGroup) {
    PhonicsGroupChip(
        emoji = group.emoji,
        title = group.ending,
        accentColor = group.accentColor
    )
}

@Composable
private fun CLEBulletRow(icon: ImageVector, color: Color, text: String) {
    Row(
        verticalAlignment     = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(Dimens8)
    ) {
        Icon(
            imageVector = icon, contentDescription = null, tint = color,
            modifier = Modifier.size(14.dp).padding(top = Dimens2)
        )
        Text(text = text, style = MaterialTheme.typography.labelSmall.scaled(), color = cleAccent)
    }
}
