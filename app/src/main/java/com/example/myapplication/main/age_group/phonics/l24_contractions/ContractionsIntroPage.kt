package com.example.myapplication.main.age_group.phonics.l24_contractions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
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
import com.example.myapplication.main.age_group.phonics.l24_contractions.view_model.ContractionGroup
import com.example.myapplication.main.age_group.phonics.l24_contractions.view_model.contractionGroups
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

private val ctPurple      = Color(0xFF8E24AA)
private val ctPurpleLight = Color(0xFFAB47BC)

@Composable
fun ContractionsIntroPage(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.lilacPink, shape = KidsFloatingShape.speechBubbles)

        BoxWithConstraints(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            val totalW = maxWidth
            val totalH = maxHeight
            Row(modifier = Modifier.fillMaxSize()) {

                // ── LEFT 46% ──────────────────────────────────────────────────
                Column(
                    modifier = Modifier
                        .width(totalW * 0.46f)
                        .fillMaxHeight()
                ) {
                    BackButtonWithText(title = "Level 24", onBackClick = { navController.popBackStack() })
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        modifier = Modifier.padding(horizontal = Dimens20),
                        verticalArrangement = Arrangement.spacedBy(Dimens14)
                    ) {
                        Text(
                            text       = "Contractions",
                            style      = MaterialTheme.typography.headlineMedium.scaled(),
                            fontWeight = FontWeight.Bold,
                            color      = ctPurple
                        )
                        Text(
                            text       = "Two words squished into one! 🤏",
                            style      = MaterialTheme.typography.titleSmall.scaled(),
                            fontWeight = FontWeight.Bold,
                            color      = ctPurpleLight
                        )
                        // Group chips: emoji + type
                        Row(horizontalArrangement = Arrangement.spacedBy(Dimens8)) {
                            contractionGroups.forEach { group -> CTGroupChip(group) }
                        }
                        // Bullet rows — exact iOS wording
                        Column(verticalArrangement = Arrangement.spacedBy(Dimens10)) {
                            CTBulletRow(Icons.Default.Block,       Color(0xFFC62828), "not → do not = don't, can't, isn't")
                            CTBulletRow(Icons.Default.Person,      Color(0xFF2E7D32), "am/is/are → I am = I'm, you're, he's")
                            CTBulletRow(Icons.Default.FastForward, Color(0xFF1565C0), "will → I will = I'll, you'll, we'll")
                            CTBulletRow(Icons.Default.Diamond,     Color(0xFF6A1B9A), "have/had → I have = I've, you'd, they've")
                        }
                        // Apostrophe note row
                        Row(
                            verticalAlignment     = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Dimens6),
                            modifier = Modifier
                                .background(ctPurple.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                                .border(1.dp, ctPurple.copy(alpha = 0.30f), RoundedCornerShape(8.dp))
                                .padding(horizontal = Dimens10, vertical = Dimens6)
                        ) {
                            Text(
                                text       = "'",
                                style      = MaterialTheme.typography.titleMedium.scaled(),
                                fontWeight = FontWeight.Bold,
                                color      = ctPurple
                            )
                            Text(
                                text  = "The apostrophe ( ' ) shows where letters were removed!",
                                style = MaterialTheme.typography.labelSmall.scaled(),
                                color = ctPurple
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }

                // ── RIGHT 54% ─────────────────────────────────────────────────
                PhonicsIntroRightPanel(
                    screenHeight   = totalH,
                    strokeColor    = ctPurple,
                    title          = "Remove letters, add an apostrophe to make a contraction!",
                    titleColor     = ctPurple,
                    descLine1      = "not  ·  am/is/are  ·  will  ·  have/had",
                    descLine2      = "The apostrophe ' always replaces the missing letters!",
                    learnButton    = PhonicsIntroBtnConfig(
                        text    = "Learn Words",
                        icon    = Icons.AutoMirrored.Filled.MenuBook,
                        type    = ButtonType.PURPLE,
                        onClick = { navController.navigate(RouteNavigation.ContractionsLearn.route) }
                    ),
                    practiceButton = PhonicsIntroBtnConfig(
                        text    = "Practice",
                        icon    = Icons.Default.CheckCircle,
                        type    = ButtonType.BLUE,
                        onClick = { navController.navigate(RouteNavigation.ContractionsPractice.route) }
                    ),
                    listenButton   = PhonicsIntroBtnConfig(
                        text    = "Listen",
                        icon    = Icons.Default.Hearing,
                        type    = ButtonType.TEAL,
                        onClick = { navController.navigate(RouteNavigation.PhonicsListen.createRoute("contractions")) }
                    ),
                    modifier       = Modifier.weight(1f).fillMaxHeight()
                )
            }
        }
    }
}

@Composable
private fun CTGroupChip(group: ContractionGroup) {
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
            text       = group.type,
            style      = MaterialTheme.typography.labelSmall.scaled(),
            fontWeight = FontWeight.Bold,
            color      = group.accentColor
        )
    }
}

@Composable
private fun CTBulletRow(icon: ImageVector, color: Color, text: String) {
    Row(
        verticalAlignment     = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(Dimens8)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = color,
            modifier = Modifier.size(14.dp).padding(top = Dimens2))
        Text(text = text, style = MaterialTheme.typography.labelSmall.scaled(), color = ctPurple)
    }
}
