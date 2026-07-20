package com.example.myapplication.main.age_group.phonics.l7_beginning_blends

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.l7_beginning_blends.view_model.BlendGroup
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.PhonicsIntroBtnConfig
import com.example.myapplication.main.common.PhonicsIntroRightPanel
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens14
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled

@Composable
fun BeginningBlendsIntroPage(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.mintLime, shape = KidsFloatingShape.curveLines)

        BoxWithConstraints(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            val screenH = maxHeight

            Row(modifier = Modifier.fillMaxSize()) {

                // ── LEFT 54% ────────────────────────────────────────────────
                Column(
                    modifier = Modifier
                        .weight(0.54f)
                        .fillMaxHeight()
                ) {
                    BackButtonWithText(title = "Level 7", onBackClick = { navController.popBackStack() })

                    Column(

                        modifier = Modifier

                            .weight(1f)

                            .verticalScroll(rememberScrollState())

                            .padding(horizontal = Dimens20, vertical = Dimens10),

                        verticalArrangement = Arrangement.spacedBy(Dimens14, Alignment.CenterVertically)

                    ) {
                        Text(
                            text = "Beginning Blends",
                            style = MaterialTheme.typography.headlineLarge.scaled(),
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF1B5E20)
                        )

                        // Group badges
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(Dimens8),
                            verticalArrangement = Arrangement.spacedBy(Dimens8)
                        ) {
                            BlendGroup.entries.forEach { group ->
                                GroupBadge(group = group, modifier = Modifier.weight(1f))
                            }
                        }

                        Column(
                            verticalArrangement = Arrangement.spacedBy(Dimens10),
                            modifier = Modifier.padding(top = Dimens4)
                        ) {
                            BulletRow(
                                icon = Icons.Default.LibraryMusic,
                                color = Color(0xFF00897B),
                                text = "21 blends across 4 groups — L, R, S and W blends"
                            )
                            BulletRow(
                                icon = Icons.Default.Spellcheck,
                                color = Color(0xFF1565C0),
                                text = "Two consonants at the start, blended together fast"
                            )
                            BulletRow(
                                icon = Icons.Default.RecordVoiceOver,
                                color = Color(0xFF7B1FA2),
                                text = "Tap any word to hear the blend sound clearly"
                            )
                            BulletRow(
                                icon = Icons.Default.CheckCircle,
                                color = Color(0xFFE65100),
                                text = "Practice quiz — pick the correct beginning blend"
                            )
                        }
                    }
                }

                // ── RIGHT 46% ────────────────────────────────────────────────
                PhonicsIntroRightPanel(
                    screenHeight = screenH,
                    strokeColor = Color(0xFF1B5E20),
                    title = "Let's learn blends!",
                    titleColor = Color(0xFF1B5E20),
                    descLine1 = "Two letters blended into",
                    descLine2 = "one smooth sound at the start",
                    learnButton = PhonicsIntroBtnConfig(
                        text = "Learn Blends",
                        icon = Icons.Default.MenuBook,
                        type = ButtonType.GREEN,
                        onClick = { navController.navigate(RouteNavigation.BeginningBlendsLearn.route) }
                    ),
                    practiceButton = PhonicsIntroBtnConfig(
                        text = "Practice",
                        icon = Icons.Default.CheckCircle,
                        type = ButtonType.BLUE,
                        onClick = { navController.navigate(RouteNavigation.BeginningBlendsPractice.route) }
                    ),
                    listenButton = PhonicsIntroBtnConfig(
                        text = "Listen",
                        icon = Icons.Default.Hearing,
                        type = ButtonType.TEAL,
                        onClick = { navController.navigate(RouteNavigation.PhonicsListen.createRoute("beginningBlends")) }
                    ),
                    modifier = Modifier.weight(0.46f).fillMaxHeight()
                )
            }
        }
    }
}

@Composable
private fun GroupBadge(group: BlendGroup, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens4),
        modifier = modifier
            .background(
                color = group.color.copy(alpha = 0.10f),
                shape = RoundedCornerShape(Dimens12)
            )
            .border(
                width = androidx.compose.ui.unit.Dp(1.5f),
                color = group.color.copy(alpha = 0.30f),
                shape = RoundedCornerShape(Dimens12)
            )
            .padding(horizontal = Dimens10, vertical = Dimens8)
    ) {
        Text(
            text = group.emoji,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = group.label,
            style = MaterialTheme.typography.labelMedium.scaled(),
            fontWeight = FontWeight.Bold,
            color = group.color
        )
    }
}

@Composable
private fun BulletRow(icon: ImageVector, color: Color, text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens10),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.width(Dimens24)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.scaled(),
            color = Color(0xFF1B5E20)
        )
    }
}
