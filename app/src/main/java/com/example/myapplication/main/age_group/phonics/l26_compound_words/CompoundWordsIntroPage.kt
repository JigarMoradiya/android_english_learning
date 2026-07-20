package com.example.myapplication.main.age_group.phonics.l26_compound_words

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.WbSunny
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
import com.example.myapplication.main.age_group.phonics.l26_compound_words.view_model.CWGroup
import com.example.myapplication.main.age_group.phonics.l26_compound_words.view_model.cwGroups
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

private val cwAccent      = Color(0xFFF57C00)
private val cwAccentLight = Color(0xFFE65100)

@Composable
fun CompoundWordsIntroPage(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.peachYellow, shape = KidsFloatingShape.speechBubbles)

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
                    BackButtonWithText(title = "Level 26", onBackClick = { navController.popBackStack() })
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = Dimens20, vertical = Dimens10),
                        verticalArrangement = Arrangement.spacedBy(Dimens14, Alignment.CenterVertically)
                    ) {
                        Text(
                            text       = "Compound Words",
                            style      = MaterialTheme.typography.headlineMedium.scaled(),
                            fontWeight = FontWeight.Bold,
                            color      = cwAccent
                        )
                        Text(
                            text       = "Two words join to make one! 🔗",
                            style      = MaterialTheme.typography.titleSmall.scaled(),
                            fontWeight = FontWeight.Bold,
                            color      = cwAccentLight
                        )
                        // Group chips
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(Dimens8),
                            verticalArrangement = Arrangement.spacedBy(Dimens8)
                        ) {
                            cwGroups.forEach { group -> CWGroupChip(group) }
                        }
                        // Bullet rows
                        Column(verticalArrangement = Arrangement.spacedBy(Dimens10)) {
                            CWBulletRow(Icons.Default.WbSunny,         Color(0xFF2E7D32), "Nature · rain+bow, sun+flower, snow+flake")
                            CWBulletRow(Icons.Default.Home,             Color(0xFF1565C0), "Home · bed+room, tooth+brush, door+bell")
                            CWBulletRow(Icons.Default.Pets,             Color(0xFF7B1FA2), "Animals · butter+fly, lady+bug, sea+horse")
                            CWBulletRow(Icons.Default.SportsBasketball, Color(0xFFC62828), "Play · foot+ball, birth+day, air+plane")
                        }
                        // Compound note
                        Row(
                            verticalAlignment     = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Dimens6),
                            modifier = Modifier
                                .background(cwAccent.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                                .border(1.dp, cwAccent.copy(alpha = 0.30f), RoundedCornerShape(8.dp))
                                .padding(horizontal = Dimens10, vertical = Dimens6)
                        ) {
                            Text(text = "🔗", style = MaterialTheme.typography.bodyMedium.scaled())
                            Text(
                                text  = "Both parts keep their meaning when joined together!",
                                style = MaterialTheme.typography.labelSmall.scaled(),
                                color = cwAccent
                            )
                        }
                    }
                }

                // ── RIGHT 46% ─────────────────────────────────────────────────
                PhonicsIntroRightPanel(
                    screenHeight   = totalH,
                    strokeColor    = cwAccent,
                    title          = "Two words become\none new word!",
                    titleColor     = cwAccent,
                    descLine1      = "\uD83C\uDF3B sun+flower  \uD83E\uDD8B butter+fly",
                    descLine2      = "Each part keeps its own meaning!",
                    learnButton    = PhonicsIntroBtnConfig(
                        text    = "Learn Words",
                        icon    = Icons.Default.CheckCircle,
                        type    = ButtonType.ORANGE,
                        onClick = { navController.navigate(RouteNavigation.CompoundWordsLearn.route) }
                    ),
                    practiceButton = PhonicsIntroBtnConfig(
                        text    = "Practice",
                        icon    = Icons.Default.CheckCircle,
                        type    = ButtonType.BLUE,
                        onClick = { navController.navigate(RouteNavigation.CompoundWordsPractice.route) }
                    ),
                    listenButton   = PhonicsIntroBtnConfig(
                        text    = "Listen",
                        icon    = Icons.Default.Hearing,
                        type    = ButtonType.TEAL,
                        onClick = { navController.navigate(RouteNavigation.PhonicsListen.createRoute("compoundWords")) }
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
private fun CWGroupChip(group: CWGroup) {
    PhonicsGroupChip(
        emoji = group.emoji,
        title = group.name,
        accentColor = group.accentColor,
        tapAudio = group.words.firstOrNull()?.full
    )
}

@Composable
private fun CWBulletRow(icon: ImageVector, color: Color, text: String) {
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
        Text(text = text, style = MaterialTheme.typography.labelSmall.scaled(), color = cwAccent)
    }
}
