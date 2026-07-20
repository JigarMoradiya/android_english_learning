package com.example.myapplication.main.age_group.phonics.l28_sight_words

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Star
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
import com.example.myapplication.main.age_group.phonics.l28_sight_words.view_model.SWSet
import com.example.myapplication.main.age_group.phonics.l28_sight_words.view_model.swSets
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
import androidx.compose.material.icons.filled.Stars

private val swAccent      = Color(0xFFD81B60)
private val swAccentLight = Color(0xFF880E4F)

@Composable
fun StarWordsIntroPage(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.pinkPeach, shape = KidsFloatingShape.stars)

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
                    BackButtonWithText(title = "Level 28", onBackClick = { navController.popBackStack() })
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = Dimens20, vertical = Dimens10),
                        verticalArrangement = Arrangement.spacedBy(Dimens14, Alignment.CenterVertically)
                    ) {
                        Text(
                            text       = "Sight Words",
                            style      = MaterialTheme.typography.headlineMedium.scaled(),
                            fontWeight = FontWeight.Bold,
                            color      = swAccent
                        )
                        Text(
                            text       = "Words you know by heart! ⭐",
                            style      = MaterialTheme.typography.titleSmall.scaled(),
                            fontWeight = FontWeight.Bold,
                            color      = swAccentLight
                        )
                        // Set chips
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(Dimens8),
                            verticalArrangement = Arrangement.spacedBy(Dimens8)
                        ) {
                            swSets.forEach { set -> SWSetChip(set) }
                        }
                        // Bullet rows
                        Column(verticalArrangement = Arrangement.spacedBy(Dimens10)) {
                            SWBulletRow(Icons.Default.Star,        Color(0xFFD81B60), "Set 1 · the, was, said, they, because")
                            SWBulletRow(Icons.Default.AutoAwesome, Color(0xFF6A1B9A), "Set 2 · what, where, come, one, people")
                            SWBulletRow(Icons.Default.Stars,       Color(0xFF00838F), "Set 3 · there, here, want, very…")
                            SWBulletRow(Icons.Default.Favorite,    Color(0xFFC62828), "Too tricky to sound out — know them by heart!")
                        }
                        // Star note
                        Row(
                            verticalAlignment     = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Dimens6),
                            modifier = Modifier
                                .background(swAccent.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                                .border(1.dp, swAccent.copy(alpha = 0.30f), RoundedCornerShape(8.dp))
                                .padding(horizontal = Dimens10, vertical = Dimens6)
                        ) {
                            Text(text = "⭐", style = MaterialTheme.typography.bodyMedium.scaled())
                            Text(
                                text  = "Star words appear everywhere · learn them by sight!",
                                style = MaterialTheme.typography.labelSmall.scaled(),
                                color = swAccent
                            )
                        }
                    }
                }

                // ── RIGHT 46% ─────────────────────────────────────────────────
                PhonicsIntroRightPanel(
                    screenHeight   = totalH,
                    strokeColor    = swAccent,
                    title          = "Know them the\nmoment you see them!",
                    titleColor     = swAccent,
                    descLine1      = "⭐ the · was · said · once · who",
                    descLine2      = "No sounding out — just know them!",
                    learnButton    = PhonicsIntroBtnConfig(
                        text    = "Learn Words",
                        icon    = Icons.Default.CheckCircle,
                        type    = ButtonType.ORANGE,
                        onClick = { navController.navigate(RouteNavigation.StarWordsLearn.route) }
                    ),
                    practiceButton = PhonicsIntroBtnConfig(
                        text    = "Practice",
                        icon    = Icons.Default.CheckCircle,
                        type    = ButtonType.BLUE,
                        onClick = { navController.navigate(RouteNavigation.StarWordsPractice.route) }
                    ),
                    listenButton   = PhonicsIntroBtnConfig(
                        text    = "Listen",
                        icon    = Icons.Default.Hearing,
                        type    = ButtonType.TEAL,
                        onClick = { navController.navigate(RouteNavigation.PhonicsListen.createRoute("sightWords")) }
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
private fun SWSetChip(set: SWSet) {
    PhonicsGroupChip(
        emoji = set.emoji,
        title = set.name,
        accentColor = set.accentColor,
        tapAudio = set.words.firstOrNull()?.word
    )
}

@Composable
private fun SWBulletRow(icon: ImageVector, color: Color, text: String) {
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
        Text(text = text, style = MaterialTheme.typography.labelSmall.scaled(), color = swAccent)
    }
}
