package com.example.myapplication.main.age_group.phonics.l23_suffixes

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
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.RemoveCircle
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
import com.example.myapplication.main.age_group.phonics.l23_suffixes.view_model.SuffixGroup
import com.example.myapplication.main.age_group.phonics.l23_suffixes.view_model.suffixGroups
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.*
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens14
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled

private val sfBlue      = Color(0xFF3949AB)
private val sfBlueLight = Color(0xFF5C6BC0)
private val sfOrange    = Color(0xFFE65100)

@Composable
fun SuffixesIntroPage(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.skyLavender, shape = KidsFloatingShape.stars)

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
                    BackButtonWithText(title = "Level 23", onBackClick = { navController.popBackStack() })
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = Dimens20, vertical = Dimens10),
                        verticalArrangement = Arrangement.spacedBy(Dimens14, Alignment.CenterVertically)
                    ) {
                        Text(
                            text       = "Suffixes",
                            style      = MaterialTheme.typography.headlineMedium.scaled(),
                            fontWeight = FontWeight.Bold,
                            color      = sfBlue
                        )
                        Text(
                            text       = "Add to the END to make new words! ➡️",
                            style      = MaterialTheme.typography.titleSmall.scaled(),
                            fontWeight = FontWeight.Bold,
                            color      = sfBlueLight
                        )
                        // Group chips: emoji + suffix
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(Dimens8),
                            verticalArrangement = Arrangement.spacedBy(Dimens8)
                        ) {
                            suffixGroups.forEach { group -> SFGroupChip(group) }
                        }
                        // Bullet rows
                        Column(verticalArrangement = Arrangement.spacedBy(Dimens10)) {
                            SFBulletRow(Icons.Default.Favorite,    Color(0xFF00695C), "-ful = full of → helpful, joyful, peaceful")
                            SFBulletRow(Icons.Default.RemoveCircle,Color(0xFF283593), "-less = without → careless, hopeless, fearless")
                            SFBulletRow(Icons.Default.AutoAwesome, sfOrange,          "-ness = quality of → kindness, happiness ★")
                            SFBulletRow(Icons.Default.FlashOn,     Color(0xFF6A1B9A), "-tion = act of → action, direction, connection")
                        }
                        // Note row: ★ happy + ness → happiness
                        Row(
                            verticalAlignment     = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Dimens6),
                            modifier = Modifier
                                .background(sfOrange.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                                .border(1.dp, sfOrange.copy(alpha = 0.30f), RoundedCornerShape(8.dp))
                                .padding(horizontal = Dimens10, vertical = Dimens6)
                        ) {
                            Icon(Icons.Default.Star, null, tint = sfOrange, modifier = Modifier.size(12.dp))
                            Text(
                                text  = "happy + ness → happiness (y changes to i!)",
                                style = MaterialTheme.typography.labelSmall.scaled(),
                                color = sfOrange
                            )
                        }
                    }
                }

                // ── RIGHT 46% ─────────────────────────────────────────────────
                PhonicsIntroRightPanel(
                    screenHeight   = totalH,
                    strokeColor    = sfBlue,
                    title          = "Add a suffix to the END\nto grow new meanings!",
                    titleColor     = sfBlue,
                    descLine1      = "-ful  ·  -less  ·  -ness  ·  -tion",
                    descLine2      = "Watch out for -ness: words ending in Y change Y to I first!",
                    learnButton    = PhonicsIntroBtnConfig(
                        text    = "Learn Words",
                        icon    = Icons.Default.MenuBook,
                        type    = ButtonType.PURPLE,
                        onClick = { navController.navigate(RouteNavigation.SuffixesLearn.route) }
                    ),
                    practiceButton = PhonicsIntroBtnConfig(
                        text    = "Practice",
                        icon    = Icons.Default.CheckCircle,
                        type    = ButtonType.BLUE,
                        onClick = { navController.navigate(RouteNavigation.SuffixesPractice.route) }
                    ),
                    listenButton   = PhonicsIntroBtnConfig(
                        text    = "Listen",
                        icon    = Icons.Default.Hearing,
                        type    = ButtonType.TEAL,
                        onClick = { navController.navigate(RouteNavigation.PhonicsListen.createRoute("suffixes")) }
                    ),
                    modifier       = Modifier.weight(1f).fillMaxHeight()
                )
            }
        }
    }
}

@Composable
private fun SFGroupChip(group: SuffixGroup) {
    PhonicsGroupChip(
        emoji = group.emoji,
        title = group.suffix,
        accentColor = group.accentColor
    )
}

@Composable
private fun SFBulletRow(icon: ImageVector, color: Color, text: String) {
    Row(
        verticalAlignment     = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(Dimens8)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = color,
            modifier = Modifier.size(14.dp).padding(top = Dimens2))
        Text(text = text, style = MaterialTheme.typography.labelSmall.scaled(), color = sfBlue)
    }
}

